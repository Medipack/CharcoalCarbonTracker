package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.DataOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class SingleDayActivity extends AppCompatActivity {
    private String[] charts = {"SINGLE DAY", "LAST 28 DAYS", "LAST 365 DAYS"};

    List journeyList = User.getInstance().getJourneyList();
    String str_date;
    Date singleDate;
    Date startDate;
    Date endDate;

    Date currentDate;
    Date before_28;      //28 days before current
    Date before_365;       //365 days before current

    long oneDay = 1000 * 60 * 60 * 24;
    long period_28 = 28;

    int curr_year;
    int curr_month;
    int curr_day;

    int year_28;
    int month_28;
    int day_28;

    int year_365;
    int month_365;
    int day_365;

    double emissionTemp;
    double emissionTotal_28;



    Double emissionShare;
    Date journeyDate;
    Utility utility;

    int dateIndex;

    long tempDiff_gas;
    long min_gas;

    long min = 5;       //check overlap

    long tempDiff_elec;
    long min_elec;

    Spinner spinner;
    int chart_position;

    PieChart chart_single;
    PieChart chart_28;
    PieChart chart_365;

    String vehicle_name;
    float temp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);
        //setupSingleDayPieGraph();
        setupSpinner();
        setupChart();


    }


    private void setupSpinner() {
        Spinner chartType = (Spinner) findViewById(R.id.graphSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SingleDayActivity.this,
                android.R.layout.simple_spinner_item, charts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartType.setAdapter(adapter);

        chartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chart_position = position;
                setupChart();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupChart() {
        if(chart_position == 0){
            Intent intent = getIntent();
            str_date = intent.getStringExtra("date string");
            //pie graph list
            List<PieEntry> pieEntries = new ArrayList<>();
            try {
                DateFormat formatter;
                formatter = new SimpleDateFormat("MM/dd/yyyy");
                singleDate = (Date) formatter.parse(str_date);
            } catch (Exception e) {
            }
            int size = User.getInstance().getUtilityList().countUtility();

            if(size != 0) {
                for (int i = 0; i < size; i++) {
                    utility = User.getInstance().getUtilityList().getUtility(i); //get the bill in list
                    startDate = utility.getStartDate();
                    endDate = utility.getEndDate();

                    //gas
                    if (Objects.equals(utility.getUtility_type(), Utility.GAS_NAME)) {
                        //selected date in the period of one date
                        if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                            min = 0;    //already in one period
                            emissionShare =utility.getPerDayUsage();
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
                                min_gas = singleDate.getTime() - User.getInstance().getUtilityList().getUtility(0).getEndDate().getTime();
                                tempDiff_gas = singleDate.getTime() - endDate.getTime();
                                if (tempDiff_gas < min_gas) {
                                    min_gas = tempDiff_gas;         //min update
                                    dateIndex = i;          //record i
                                }
                            }

                            //selected date earlier than the start date of bill
                            //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                            else if (singleDate.getTime() < startDate.getTime()) {
                                min_gas = User.getInstance().getUtilityList().getUtility(0).getStartDate().getTime() - singleDate.getTime();
                                tempDiff_gas = startDate.getTime() - singleDate.getTime();
                                if (tempDiff_gas < min_gas) {
                                    min_gas = tempDiff_gas;         //min update
                                    dateIndex = i;          //record i
                                }
                            }

                            if(min == 5){       //if two diff not equal
                                emissionShare = User.getInstance().getUtilityList().getUtility(dateIndex).getPerDayUsage();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                pieEntries.add(new PieEntry(temp, Utility.GAS_NAME));
                                Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }


                    //electricity
                    else {
                        //selected date in the period of one date
                        if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                            Toast.makeText(this, "In the period", Toast.LENGTH_SHORT).show();
                            //dateIndex = i;
                            emissionShare =utility.getPerDayUsage();
                            String str_singleEmission = String.valueOf(emissionShare);
                            float temp = Float.valueOf(str_singleEmission);
                            pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                            Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                            min = 0;
                        }
                        //selected date not in the period
                        else {
                            //selected date later than the end date of bill
                            //e.g. choose: Mar3, bill[i]: Feb8-Feb28
                            if (singleDate.getTime() > endDate.getTime()) {
                                min_elec = singleDate.getTime() - User.getInstance().getUtilityList().getUtility(0).getEndDate().getTime();
                                tempDiff_elec = singleDate.getTime() - endDate.getTime();
                                if (tempDiff_elec < min_elec) {
                                    min_elec = tempDiff_elec;         //min update
                                    dateIndex = i;          //record i
                                }
                            }

                            //selected date earlier than the start date of bill
                            //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                            else if (singleDate.getTime() < startDate.getTime()) {
                                min_elec = User.getInstance().getUtilityList().getUtility(0).getStartDate().getTime() - singleDate.getTime();
                                tempDiff_elec = startDate.getTime() - singleDate.getTime();
                                if (tempDiff_elec < min_elec) {
                                    min_elec = tempDiff_elec;         //min update
                                    dateIndex = i;                    //record i
                                }
                            }

                            if(min == 5) {
                                emissionShare = User.getInstance().getUtilityList().getUtility(dateIndex).getPerDayUsage();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                }
            }

            //journey part

            String emission[] = new String[journeyList.size()];
            for(int k=0; k<journeyList.size();k++){
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String str_single = formatter.format(singleDate);
                String str_journey = formatter.format(journeyDate);

                if(Objects.equals(str_single, str_journey)) {
                    String car = User.getInstance().getJourneyList().get(k).getVehicleName();
                    double emissionTemp = User.getInstance().getJourneyList().get(k).getCarbonEmitted();
                    String str_emissionTemp = String.valueOf(emissionTemp);
                    emission[k] = str_emissionTemp;
                    float temp1 = Float.valueOf(emission[k]);
                    pieEntries.add(new PieEntry(temp1, car));
                }
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "emission");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);
            //get the chart:
            chart_single = (PieChart) findViewById(R.id.singleDayChart);
            chart_single.setData(data);
            chart_single.animateY(1000);
            chart_single.invalidate();
            //chart_single.setVisibility(View.VISIBLE);
        }
        else if(chart_position == 1){
            //chart_single.setVisibility(View.INVISIBLE);
            List<PieEntry> pieEntries = new ArrayList<>();
            final Calendar cal_current = Calendar.getInstance();
            curr_year = cal_current.get(Calendar.YEAR) - 1900;
            curr_month = cal_current.get(Calendar.MONTH);
            curr_day = cal_current.get(Calendar.DAY_OF_MONTH);
            currentDate = new Date(curr_year, curr_month, curr_day);

            for(int i=28;i>0;i--){
                before_28 = new Date(currentDate.getTime() - (28 - i) * oneDay);
                String emission[] = new String[journeyList.size()];
                for(int k=0;k<journeyList.size();k++) {
                    Journey journey = User.getInstance().getJourneyList().get(k);
                    journeyDate = journey.getDate();

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String str_28 = formatter.format(before_28);
                    String str_journey = formatter.format(journeyDate);


                    if(Objects.equals(str_28, str_journey)) {
                        vehicle_name = User.getInstance().getJourneyList().get(k).getVehicleName();
                        emissionTotal_28 = emissionTotal_28 + User.getInstance().getJourneyList().get(k).getCarbonEmitted();
                        String str_emissionTotal = String.valueOf(emissionTotal_28);
                        emission[k] = str_emissionTotal;
                        temp1 = Float.valueOf(emission[k]);
                        pieEntries.add(new PieEntry(temp1, vehicle_name));
                    }
                }

                /*
                pieEntries.add(new PieEntry(temp1, vehicle_name));
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

                PieDataSet dataSet = new PieDataSet(pieEntries, "emission");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                chart_28 = (PieChart) findViewById(R.id.singleDayChart);
                chart_28.setData(data);
                chart_28.animateY(1000);
                chart_28.invalidate();
                //chart_28.setVisibility(View.VISIBLE);
                */

            }
            PieDataSet dataSet = new PieDataSet(pieEntries, "emission");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);

            chart_28 = (PieChart) findViewById(R.id.singleDayChart);
            chart_28.setData(data);
            chart_28.animateY(1000);
            chart_28.invalidate();


        }
    }


}
