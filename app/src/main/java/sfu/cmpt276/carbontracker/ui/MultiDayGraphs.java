package sfu.cmpt276.carbontracker.ui;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import sfu.cmpt276.carbontracker.R;
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

    private void setupChart(int days) {
        if(days == 28)
        {
            LineChart chart = new LineChart(getApplicationContext());
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.chartLayout);
            layout.addView(chart);
        }
    }




}
