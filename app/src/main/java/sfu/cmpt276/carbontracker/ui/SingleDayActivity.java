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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class SingleDayActivity extends AppCompatActivity {
    List journeyList = User.getInstance().getJourneyList();

    String str_date;
    Date singleDate;
    Date startDate;
    Date endDate;

    Date compareStartDate;
    Date compareEndDate;

    Double emissionShare;

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
        Toast.makeText(this, "" + singleDate.getTime(), Toast.LENGTH_SHORT).show();



        int size = User.getInstance().getUtilityList().countUtility();
        for (int i = 0; i < size; i++) {
            Utility utility = User.getInstance().getUtilityList().getUtility(i); //get the bill in list
            startDate = utility.getStartDate();
            endDate = utility.getEndDate();
            //gas
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
                        compareEndDate = endDate;

                    }

                    //selected date earlier than the start date of bill
                    //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                    else if (singleDate.getTime() < startDate.getTime()) {
                        compareStartDate = startDate;
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
                        compareEndDate = endDate;
                    }

                    //selected date earlier than the start date of bill
                    //e.g. choose: Mar3, bill[i]: Mar8-Mar29
                    else if (singleDate.getTime() < startDate.getTime()) {
                        compareStartDate = startDate;
                    }

                }
            }

        /*
        //journey part
        String emission[] = new String[journeyList.size()];
        for(int i=0; i<journeyList.size();i++){
            String car = User.getInstance().getJourneyList().get(i).getVehicleName();

            double emissionTemp = User.getInstance().getJourneyList().get(i).getCarbonEmitted();
            String str_emissionTemp = String.valueOf(emissionTemp);
            emission[i] = str_emissionTemp;
            float temp = Float.valueOf(emission[i]);
            pieEntries.add(new PieEntry(temp, car));
        }
        */


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