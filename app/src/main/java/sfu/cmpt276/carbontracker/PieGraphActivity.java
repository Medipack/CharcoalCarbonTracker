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
/*  Displays Pie chart of journey emissions
* */
public class PieGraphActivity extends AppCompatActivity {
    List journeyList = User.getInstance().getJourneyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_graph);

        setupPieChart();
    }

    private void setupPieChart() {
        String emission[] = new String[journeyList.size()];
        List<PieEntry> pieEntries = new ArrayList<>();


        for(int i=0; i<journeyList.size();i++){
            String car = User.getInstance().getJourneyList().get(i).getVehicleName();

            double emissionTemp = User.getInstance().getJourneyList().get(i).getCarbonEmitted();
            String str_emissionTemp = String.valueOf(emissionTemp);
            emission[i] = str_emissionTemp;
            float temp = Float.valueOf(emission[i]);
            pieEntries.add(new PieEntry(temp, car));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "emission");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //get the chart:
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
