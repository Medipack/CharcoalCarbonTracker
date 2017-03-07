package sfu.cmpt276.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {
    int testNum[] = {3, 5, 6, 7, 8, 13};
    String testName[] = {"three", "five", "six", "seven", "eight", "thirteen"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        setupPieChart();
    }

    private void setupPieChart() {
        //populating a list of PieEntries:
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i < testNum.length; i++){
            pieEntries.add(new PieEntry(testNum[i], testName[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "test");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //get the chart:
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
