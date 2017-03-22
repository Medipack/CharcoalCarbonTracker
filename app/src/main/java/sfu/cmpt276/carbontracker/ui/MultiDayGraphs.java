package sfu.cmpt276.carbontracker.ui;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Car;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.UtilityList;

public class MultiDayGraphs extends AppCompatActivity {


    public static final int DAYS_IN_4_WEEKS = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_day_graphs);
        setupChart(DAYS_IN_4_WEEKS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupChart(DAYS_IN_4_WEEKS);
    }

    private void setupChart(int days) {
        if(days == DAYS_IN_4_WEEKS)
        {
            BarChart chart = (BarChart) findViewById(R.id.barChart);

            List<BarEntry> entries = new ArrayList<>();
            List<Date> dateList = getDateList(DAYS_IN_4_WEEKS);
            float c = 0;
            for(int i = dateList.size()-1; i >= 0; i--)
            {
                float[] yvalues = {(float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.CAR),
                        (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.BUS),
                        (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.SKYTRAIN),
                        (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.WALK_BIKE)};
                entries.add(new BarEntry(c, yvalues));
                c++;
            }

            BarDataSet set = new BarDataSet(entries, "BarDataSet");

            BarData data = new BarData(set);
            //data.setBarWidth(0.9f); // set custom bar width
            chart.setData(data);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
        }
    }


    private double getTotalEmissionsForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        double totalEmissions = 0;
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Car car = journey.getCar();
            Date journeyDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                 journeyDateWithoutTime = sdf.parse(sdf.format(journey.getDate()));
                 dateWantedWithoutTime = sdf.parse(sdf.format(dateWanted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean isWantedTransportMode = car.getTransport_mode().equals(transportModeWanted);
            boolean isInDate = journeyDateWithoutTime.equals(dateWantedWithoutTime);
            if(isWantedTransportMode && isInDate)
            {
                totalEmissions += journey.getCarbonEmitted();
            }
        }

        return totalEmissions;
    }

    public List<Date> getDateList(int daysInPeriod) //gets dates in period
    {
        List<Date> dateList = new ArrayList<>(daysInPeriod);
        for (int i=0; i < daysInPeriod; i++) {
            Calendar cal = Calendar.getInstance(); // this would default to now
            cal.add(Calendar.DAY_OF_YEAR, -i);
            dateList.add(cal.getTime());
        }
        return dateList;
    }


}
