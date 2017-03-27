package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;

/* Activity to display 28 day bar graph and 365 day bar graph*/

public class MultiDayGraphs extends AppCompatActivity {

    private static final int DAYS_IN_4_WEEKS = 28;
    private static final int DAYS_IN_YEAR = 365;
    private static final int MONTH_COUNT = 12;
    private static final float DAILY_EMISSIONS_PER_CAPITA = 40.5479452055f; //14800kg / 365days

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

        //create bar chart, sets, entries, and x-axis labels
        CombinedChart chart = (CombinedChart) findViewById(R.id.barChart);
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
            //get emisisons for bus, skytrain, and walk/bike for particular month
            float busEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.BUS);
            float skytrainEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.SKYTRAIN);
            float walk_bikeEmissions = GraphHelper.getJourneyEmissionsForMonthForTransportType(m, Vehicle.WALK_BIKE);
            //add this months bus, skytrain, walk/bike barData to graph as new barEntry
            busEntries.add(new BarEntry(m, busEmissions));
            skytrainEntries.add(new BarEntry(m, skytrainEmissions));
            walk_bikeEntries.add(new BarEntry(m, walk_bikeEmissions));

            //get car emissions values, where each car has its own section of the carEntry
            List<Float> temp_yValues = new ArrayList<>(); //needed to add list of cars to a car entry
            Map<Vehicle, Float> carMap = GraphHelper.getCarEmissionTotalsFromJourneysInMonth(m); //getting the carmap
            carMap.size();
            for (Map.Entry<Vehicle, Float> entry : carMap.entrySet()) {  //for each entry in carmap, add the value to the temp_yvalues
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            //convert temp_yValues Float List to float array
            float[] yvalues = new float[temp_yValues.size()];
            for (int u = 0; u < temp_yValues.size(); u++) {
                yvalues[u] = temp_yValues.get(u);
            }
            carEntries.add(new BarEntry(m, yvalues)); //add this months car emissions totals as new barEntry

            //get utility emissions
            float electricEmissions = GraphHelper.getUtilityEmissionsForMonthForUtilityType(Utility.ELECTRICITY_NAME, m);
            float gasEmissions = GraphHelper.getUtilityEmissionsForMonthForUtilityType(Utility.GAS_NAME, m);
            //add this months utility emissions as new bar entry
            electricityEntries.add(new BarEntry(m, electricEmissions));
            naturalGasEntries.add(new BarEntry(m, gasEmissions));

        }

        //set all barData sets to include entries from all months and name sets
        busSet = new BarDataSet(busEntries, "bus");
        carSet = new BarDataSet(carEntries, "cars");
        skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
        walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");
        electricSet = new BarDataSet(electricityEntries, "electricity");
        naturalGasSet = new BarDataSet(naturalGasEntries, "natural gas");

        //set the colors from each set from the color.xml
        int[] carColors = getApplicationContext().getResources().getIntArray(R.array.carColors);
        carSet.setColors(carColors);
        busSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.BusColor));
        skytrainSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.TrainColor));
        walk_bikeSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.Walk_bikeColor));
        electricSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.ElectricColor));
        naturalGasSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.NaturalGasColor));

        //format the y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        //format the x axis
        xAxis.setValueFormatter(new XAxisVaueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(MONTH_COUNT);
        //set the barData for the barChart and format
        BarData barData = new BarData(busSet, carSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet);

        CombinedData data = new CombinedData();
        data.setData(barData);
        chart.setData(data);
        //chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        chart.invalidate(); // refresh
    }

    private void create28DayGraph() {
        //create bar chart, sets, entries, and x-axis labels
        CombinedChart chart = (CombinedChart) findViewById(R.id.barChart);
        BarDataSet busSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet, carSet;
        List<BarEntry> busEntries = new ArrayList<>();
        List<BarEntry> skytrainEntries = new ArrayList<>();
        List<BarEntry> walk_bikeEntries = new ArrayList<>();
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> electricityEntries = new ArrayList<>();
        List<BarEntry> naturalGasEntries = new ArrayList<>();

        XAxis xAxis = chart.getXAxis();
        String[] xAxisValues = new String[DAYS_IN_4_WEEKS];

        List<Date> dateList = GraphHelper.getDateList(DAYS_IN_4_WEEKS); //gets list of dates in last 28 days
        float c = 0;
        for (int i = dateList.size() - 1; i >= 0; i--) { //iterate through last 28 days
            //get utility map and add emission totals for each utility on particular date to the entry list
            for (Map.Entry<String, Float> entry : GraphHelper.getDailyTotalUtilityEmissions(dateList.get(i)).entrySet()) {
                Float emissionTotal = entry.getValue();
                if (entry.getKey().equals(Utility.ELECTRICITY_NAME))
                    electricityEntries.add(new BarEntry(c, emissionTotal));
                else
                    naturalGasEntries.add(new BarEntry(c, emissionTotal));
            }

            List<Float> temp_yValues = new ArrayList<>(); //needed for individual cars to be added as one to the entries
            List<Journey> journeys = GraphHelper.getJourneysForTransportModeOnDate(dateList.get(i), Vehicle.CAR); //get car journeys on date
            Map<Vehicle, Float> carMap = GraphHelper.getCarEmissionTotalsFromJourneys(journeys); //get carmap for those journeys
           // carMap.size();
            for (Map.Entry<Vehicle, Float> entry : carMap.entrySet()) { //for each car, get its emissions total on date and add to temp_yvalues
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            float[] yvalues = new float[temp_yValues.size()]; //convert temp_yValues Float List to float array
            for (int u = 0; u < temp_yValues.size(); u++) {
                yvalues[u] = temp_yValues.get(u);
            }
            entries.add(new BarEntry(c, yvalues)); //enter car barData for particular date to entries

            //add bus, skytrain, walk/bike emissions to their appropriate entry lists for particular date
            busEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.BUS)));
            skytrainEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.SKYTRAIN)));
            walk_bikeEntries.add(new BarEntry(c, (float) GraphHelper.getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.WALK_BIKE)));

            //format each of the 28 days and add to xaxis-values list to be used as labels
            Date date = dateList.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date formatDate = sdf.parse(sdf.format(date));
                xAxisValues[(int)c] = String.valueOf(sdf.format(formatDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c++;
        }

        //set the sets to include all entries for the 28 days and name the sets
        busSet = new BarDataSet(busEntries, "bus");
        carSet = new BarDataSet(entries, "cars");
        skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
        walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");
        electricSet = new BarDataSet(electricityEntries, "electricity");
        naturalGasSet = new BarDataSet(naturalGasEntries, "natural gas");

        //set the set colors using the color.xml
        int[] carColors = getApplicationContext().getResources().getIntArray(R.array.carColors);
        carSet.setColors(carColors);
        busSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.BusColor));
        skytrainSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.TrainColor));
        walk_bikeSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.Walk_bikeColor));
        electricSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.ElectricColor));
        naturalGasSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.NaturalGasColor));

        //format y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        //format x axis
        xAxis.setValueFormatter(new XAxisVaueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(DAYS_IN_4_WEEKS);

        //set bar chart barData and format chart
        BarData barData = new BarData(busSet, carSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet);

        CombinedData data = new CombinedData();
        data.setData(barData);
        chart.setData(data);
        //chart.setFitBars(true); // make the x-axis fit exactly all bars
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
