package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;
import sfu.cmpt276.carbontracker.ui.database.UtilityDataSource;

/* Activity to display 28 day bar graph and 365 day bar graph*/

public class MultiDayGraphs extends AppCompatActivity {

    private static final int DAYS_IN_4_WEEKS = 28;
    private static final int DAYS_IN_YEAR = 365;
    private static final int MONTH_COUNT = 12;

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
        if (days == DAYS_IN_4_WEEKS) {
            create28DayGraph();
        } else if (days == DAYS_IN_YEAR) {
            create365DayGraph();
        }
    }

    private void create365DayGraph() {
        BarChart chart = (BarChart) findViewById(R.id.barChart);
        BarDataSet busSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet, carSet;

        XAxis xAxis = chart.getXAxis();
        String[] xAxisValues = {"jan", "feb", "march", "april", "may", "june", "july", "august", "sept", "dec", "oct", "nov"};

        List<BarEntry> busEntries = new ArrayList<>();
        List<BarEntry> skytrainEntries = new ArrayList<>();
        List<BarEntry> walk_bikeEntries = new ArrayList<>();
        List<BarEntry> carEntries = new ArrayList<>();
        List<BarEntry> electricityEntries = new ArrayList<>();
        List<BarEntry> naturalGasEntries = new ArrayList<>();

        for (int m = 0; m < MONTH_COUNT; m++) //iterate through 12 months, from 0 to 11
        {
            float busEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.BUS);
            float skytrainEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.SKYTRAIN);
            float walk_bikeEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.WALK_BIKE);

            busEntries.add(new BarEntry(m, busEmissions));
            skytrainEntries.add(new BarEntry(m, skytrainEmissions));
            walk_bikeEntries.add(new BarEntry(m, walk_bikeEmissions));

            List<Float> temp_yValues = new ArrayList<>();
            Map<Vehicle, Float> carMap = GraphHelper.getVehicleEmissionTotalsFromJourneysInMonth(m);
            carMap.size();
            for (Map.Entry<Vehicle, Float> entry : carMap.entrySet()) {
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            float[] yvalues = new float[temp_yValues.size()];
            for (int u = 0; u < temp_yValues.size(); u++) {
                yvalues[u] = temp_yValues.get(u);
            }
            carEntries.add(new BarEntry(m, yvalues));

            float electricEmissions = GraphHelper.getUtilityEmissionsForMonthForUtilityType(Utility.ELECTRICITY_NAME, m);
            float gasEmissions = GraphHelper.getUtilityEmissionsForMonthForUtilityType(Utility.GAS_NAME, m);
            electricityEntries.add(new BarEntry(m, electricEmissions));
            naturalGasEntries.add(new BarEntry(m, gasEmissions));

        }
        busSet = new BarDataSet(busEntries, "bus");
        carSet = new BarDataSet(carEntries, "cars");
        skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
        walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");
        electricSet = new BarDataSet(electricityEntries, "electricity");
        naturalGasSet = new BarDataSet(naturalGasEntries, "natural gas");

        int[] carColors = getApplicationContext().getResources().getIntArray(R.array.carColors);
        carSet.setColors(carColors);
        busSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.BusColor));
        skytrainSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.TrainColor));
        walk_bikeSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.Walk_bikeColor));
        electricSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.ElectricColor));
        naturalGasSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.NaturalGasColor));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        xAxis.setValueFormatter(new XAxisVaueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(MONTH_COUNT);
        BarData data = new BarData(busSet, carSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        chart.invalidate(); // refresh
    }

    private void create28DayGraph() {
        BarChart chart = (BarChart) findViewById(R.id.barChart);
        BarDataSet busSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet, carSet;
        List<BarEntry> busEntries = new ArrayList<>();
        List<BarEntry> skytrainEntries = new ArrayList<>();
        List<BarEntry> walk_bikeEntries = new ArrayList<>();
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> electricityEntries = new ArrayList<>();
        List<BarEntry> naturalGasEntries = new ArrayList<>();

        XAxis xAxis = chart.getXAxis();
        String[] xAxisValues = new String[DAYS_IN_4_WEEKS];

        List<Date> dateList = GraphHelper.getDateList(DAYS_IN_4_WEEKS);
        float c = 0;
        for (int i = dateList.size() - 1; i >= 0; i--) {
            for (Map.Entry<String, Float> entry : GraphHelper.getDailyTotalUtilityEmissions(dateList.get(i)).entrySet()) {
                Float emissionTotal = entry.getValue();
                if (entry.getKey().equals(Utility.ELECTRICITY_NAME))
                    electricityEntries.add(new BarEntry(c, emissionTotal));
                else
                    naturalGasEntries.add(new BarEntry(c, emissionTotal));
            }

            List<Float> temp_yValues = new ArrayList<>();
            List<Journey> journeys = GraphHelper.getJourneysForTransportModeOnDate(dateList.get(i), Vehicle.CAR);
            Map<Vehicle, Float> carMap = GraphHelper.getCarEmissionTotalsFromJourneys(journeys);
           // carMap.size();
            for (Map.Entry<Vehicle, Float> entry : carMap.entrySet()) {
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            float[] yvalues = new float[temp_yValues.size()];
            for (int u = 0; u < temp_yValues.size(); u++) {
                yvalues[u] = temp_yValues.get(u);
            }
            entries.add(new BarEntry(c, yvalues));

            busEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.BUS)));
            skytrainEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.SKYTRAIN)));
            walk_bikeEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.WALK_BIKE)));
            Date date = dateList.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date formatDate = sdf.parse(sdf.format(date));
                xAxisValues[i] = String.valueOf(sdf.format(formatDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c++;
        }
        busSet = new BarDataSet(busEntries, "bus");
        carSet = new BarDataSet(entries, "cars");
        skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
        walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");
        electricSet = new BarDataSet(electricityEntries, "electricity");
        naturalGasSet = new BarDataSet(naturalGasEntries, "natural gas");

        int[] carColors = getApplicationContext().getResources().getIntArray(R.array.carColors);
        carSet.setColors(carColors);
        busSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.BusColor));
        skytrainSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.TrainColor));
        walk_bikeSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.Walk_bikeColor));
        electricSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.ElectricColor));
        naturalGasSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.NaturalGasColor));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        xAxis.setValueFormatter(new XAxisVaueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(DAYS_IN_4_WEEKS);
        BarData data = new BarData(busSet, carSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        chart.invalidate(); // refresh
    }
}
//xAxisValueFormatter as per MP Charts Wiki
class XAxisVaueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public XAxisVaueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }

}
