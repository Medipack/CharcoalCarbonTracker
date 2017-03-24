package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;
import sfu.cmpt276.carbontracker.ui.database.UtilityDataSource;
/*Activity to display graph for single day's data*/
public class SingleDayActivity extends AppCompatActivity {


    private Date singleDate;

    private List<Utility> utilityList;
    private List<Journey> journeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);

        populateUtilityList();
        populateJourneyList();

        setupSingleDayPieGraph();
    }

    private void populateJourneyList() {
        journeyList = new ArrayList<>();
        JourneyDataSource db = new JourneyDataSource(this);
        db.open();
        journeyList = db.getAllJourneys(this);
        db.close();
        Set<Journey> journeySet = new HashSet<>();
        for(Journey journey : journeyList)
            journeySet.add(journey);
        journeyList.clear();
        journeyList.addAll(journeySet);
    }

    private void populateUtilityList() {
        utilityList = new ArrayList<>();
        UtilityDataSource db = new UtilityDataSource(this);
        db.open();
        utilityList = db.getAllUtilities();
        db.close();
    }

    @SuppressLint("SimpleDateFormat")
    private void setupSingleDayPieGraph() {
        Intent intent = getIntent();
        String str_date = intent.getStringExtra("date string");

        //pie graph list
        List<PieEntry> pieEntries = new ArrayList<>();


        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            singleDate = formatter.parse(str_date);
        } catch (Exception e) {
            finish();
        }
        Toast.makeText(this, "" + singleDate.getTime(), Toast.LENGTH_SHORT).show();


        for (Utility utility : utilityList) {
            Date startDate = utility.getStartDate();
            Date endDate = utility.getEndDate();
            //gas
            Double emissionShare;
            if (Objects.equals(utility.getUtility_type(), Utility.GAS_NAME)) {
                //selected date in the period of one date
                if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                    Toast.makeText(this, "In the period", Toast.LENGTH_SHORT).show();
                    emissionShare = utility.getPerDayUsage();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    pieEntries.add(new PieEntry(temp, Utility.GAS_NAME));

                    Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                }

                //selected date not in the period
                else {
                    //selected date later than the end date of bill
                    //e.g. choose: Mar3, bill[i]: Feb8-Feb28
                    if (singleDate.getTime() > endDate.getTime()) {

                    }

                    //selected date earlier than the start date of bill
                    //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                    else if (singleDate.getTime() < startDate.getTime()) {
                    }

                }

            }

            //electricity
            else {
                //selected date in the period of one date
                if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                    Toast.makeText(this, "In the period", Toast.LENGTH_SHORT).show();
                    emissionShare = utility.getPerDayUsage();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));

                    Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                }

                //selected date not in the period
                else {
                    //selected date later than the end date of bill
                    //e.g. choose: Mar3, bill[i]: Feb8-Feb28
                    if (singleDate.getTime() > endDate.getTime()) {
                    }

                    //selected date earlier than the start date of bill
                    //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                    else if (singleDate.getTime() < startDate.getTime()) {
                    }

                }
            }

        /*
        //journey part
        String emission[] = new String[mainJourneyList.size()];
        for(int i=0; i<mainJourneyList.size();i++){
            String car = User.getInstance().getJourneyList().get(i).getVehicleName();

            double emissionTemp = User.getInstance().getJourneyList().get(i).getCarbonEmitted();
            String str_emissionTemp = String.valueOf(emissionTemp);
            emission[i] = str_emissionTemp;
            float temp = Float.valueOf(emission[i]);
            pieEntries.add(new PieEntry(temp, car));
        }
        */
            for(Journey journey : journeyList) {
                Calendar journeyDate = Calendar.getInstance();
                journeyDate.setTime(journey.getDate());
                Calendar singleDate_cal = Calendar.getInstance();
                singleDate_cal.setTime(singleDate);
                if(journeyDate.YEAR == singleDate_cal.YEAR
                        && journeyDate.MONTH == singleDate_cal.MONTH
                        && journeyDate.MONTH == singleDate_cal.MONTH) {
                    String name = journey.getVehicle()
                            .getShortDecription() + " : " + journey.getRoute().getRouteName();
                    float value = (float) journey.getCarbonEmitted();
                    Log.i("SingleDayActivity",
                            "Adding " + journey.getId() + " : " + name + " with carbon output of " + value);
                    pieEntries.add(new PieEntry(value, name));
                }
            }


            PieDataSet dataSet = new PieDataSet(pieEntries, "emission");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);

            //get the chart:
            PieChart chart = (PieChart) findViewById(R.id.singleDayChart);
            chart.setData(data);
            chart.animateY(1000);
            chart.invalidate();

            //first check the date: if selected date between startDate and endDate of a bill
            //use the avg data, if not, use nearest data, then check journey part
        }
    }
}
