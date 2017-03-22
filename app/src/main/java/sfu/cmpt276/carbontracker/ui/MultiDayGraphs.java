package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Intent intent = getIntent();
        setupChart(intent.getIntExtra("days", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupChart(getIntent().getIntExtra("days", 0));
    }

    private void setupChart(int days) {
        if(days == DAYS_IN_4_WEEKS)
        {
            BarChart chart = (BarChart) findViewById(R.id.barChart);
            BarDataSet busSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet, carSet;

            List<BarEntry> busEntries = new ArrayList<>();
            List<BarEntry> skytrainEntries = new ArrayList<>();
            List<BarEntry> walk_bikeEntries = new ArrayList<>();
            List<BarEntry> entries = new ArrayList<>();

            List<Date> dateList = getDateList(DAYS_IN_4_WEEKS);
            float c = 0;
            for(int i = dateList.size()-1; i >= 0; i--)
            {
                List<Float> temp_yValues = new ArrayList<>();
               // temp_yValues.add((float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.BUS));
                //temp_yValues.add((float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.SKYTRAIN));
                //temp_yValues.add((float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.WALK_BIKE));


                Map<Car, Float> carMap = getCarEmissionTotalsFromJourneys(getJourneysForTransportModeOnDate(dateList.get(i), Car.CAR));
                carMap.size();
                for(Map.Entry<Car, Float> entry : carMap.entrySet()) {
                    Car car = entry.getKey();
                    Float emissionTotal = entry.getValue();
                    temp_yValues.add(emissionTotal);
                }
                float[] yvalues = new float[temp_yValues.size()];
                for(int u = 0; u < temp_yValues.size(); u++)
                {
                    yvalues[u] = temp_yValues.get(u);
                    entries.add(new BarEntry(c, temp_yValues.get(u)));
                }

                busEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.BUS)));
                skytrainEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.SKYTRAIN)));
                walk_bikeEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Car.WALK_BIKE)));
                //entries.add(new BarEntry(c, yvalues));
                c++;
            }
            busSet = new BarDataSet(busEntries, "bus");
            carSet = new BarDataSet(entries, "cars");
            skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
            walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");

            int[] carColors = {getResources().getColor(R.color.CarColor1), getResources().getColor(R.color.CarColor2), getResources().getColor(R.color.CarColor3), getResources().getColor(R.color.CarColor4), getResources().getColor(R.color.CarColor5)};
            carSet.setColors(carColors);
            busSet.setColor(getResources().getColor(R.color.BusColor));
            skytrainSet.setColor(getResources().getColor(R.color.TrainColor));
            walk_bikeSet.setColor(getResources().getColor(R.color.Walk_bikeColor));


            BarData data = new BarData(busSet, carSet, skytrainSet, walk_bikeSet);
            //data.setBarWidth(0.9f); // set custom bar width
            chart.setData(data);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.setDrawGridBackground(false);
            chart.setDrawValueAboveBar(false);
            chart.invalidate(); // refresh
        }
    }


    private double getTotalEmissionsForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        double totalEmissions = 0;
        for(Journey journey: getJourneysForTransportModeOnDate(dateWanted, transportModeWanted))
        {
                totalEmissions += journey.getCarbonEmitted();
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

    private List<Journey> getJourneysForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        List<Journey> journeyList = new ArrayList<>();
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
                journeyList.add(journey);
            }
        }

        return journeyList;
    }

    private Map<Car, Float> getCarEmissionTotalsFromJourneys(List<Journey> journeyList)
    {
        Map<Car, Float> map = new HashMap<>();
        for(Journey journey: journeyList)
        {
            if(!map.containsKey(journey.getCar()))
            {
                map.put(journey.getCar(), (float)journey.getCarbonEmitted());
            }
            else
            {
                float temp = map.get(journey.getCar());
                temp += journey.getCarbonEmitted();
                map.put(journey.getCar(), temp);
            }
        }
        return map;
    }

}
