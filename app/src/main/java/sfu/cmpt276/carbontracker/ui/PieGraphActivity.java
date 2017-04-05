package sfu.cmpt276.carbontracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;

/*  Displays Pie chart of journey emissions
* */
public class PieGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_graph);

        setupPieChart();
    }

    private void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

        for(Journey journey : User.getInstance().getJourneyList()){
            String car = journey.getVehicle().getShortDecription();

            float emission = (float) journey.getCarbonEmitted();

            pieEntries.add(new PieEntry(emission, car));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, getString(R.string.emission1));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //get the chart:
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
