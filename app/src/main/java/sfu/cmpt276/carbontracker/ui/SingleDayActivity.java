package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.DataOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class SingleDayActivity extends AppCompatActivity {
    List journeyList = User.getInstance().getJourneyList();
    String str_date;
    Date singleDate;
    Date startDate;
    Date endDate;

    Double emissionShare;
    Date journeyDate;
    Utility utility;

    int dateIndex;

    long tempDiff_gas;
    long min_gas;

    long tempDiff_elec;
    long min_elec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);
        setupSingleDayPieGraph();
    }
    private void setupSingleDayPieGraph() {
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
                        //dateIndex = i;
                        emissionShare =utility.getPerDayUsage();
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


                        emissionShare = User.getInstance().getUtilityList().getUtility(dateIndex).getPerDayUsage();
                        String str_singleEmission = String.valueOf(emissionShare);
                        float temp = Float.valueOf(str_singleEmission);
                        pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                        Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
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

                        emissionShare = User.getInstance().getUtilityList().getUtility(dateIndex).getPerDayUsage();
                        String str_singleEmission = String.valueOf(emissionShare);
                        float temp = Float.valueOf(str_singleEmission);
                        pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                        Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            emissionShare = User.getInstance().getUtilityList().getUtility(dateIndex).getPerDayUsage();
            String str_singleEmission = String.valueOf(emissionShare);
            float temp = Float.valueOf(str_singleEmission);
            pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
            Toast.makeText(this, "" + emissionShare, Toast.LENGTH_SHORT).show();

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
            else{
                Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show();
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
    }
}
