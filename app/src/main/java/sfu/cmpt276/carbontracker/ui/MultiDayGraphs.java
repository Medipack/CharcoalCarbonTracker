package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;
import sfu.cmpt276.carbontracker.ui.database.UtilityDataSource;

/* Activity to display 28 day bar graph and 365 day bar graph*/

public class MultiDayGraphs extends AppCompatActivity {

    private static final int DAYS_IN_4_WEEKS = 28;
    private static final int DAYS_IN_YEAR = 365;
    private static final int MONTH_COUNT = 12;

    private List<Utility> mainUtilityList;
    private List<Journey> mainJourneyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_day_graphs);

        populateUtilityList();
        populateJourneyList();

        Intent intent = getIntent();
        setupChart(intent.getIntExtra("days", 0));
    }

    private void populateJourneyList() {
        mainJourneyList = new ArrayList<>();
        JourneyDataSource db = new JourneyDataSource(this);
        db.open();
        mainJourneyList = db.getAllJourneys(this);
        db.close();
        Set<Journey> journeySet = new HashSet<>();
        for(Journey journey : mainJourneyList)
            journeySet.add(journey);
        mainJourneyList.clear();
        mainJourneyList.addAll(journeySet);
    }

    private void populateUtilityList() {
        mainUtilityList = new ArrayList<>();
        UtilityDataSource db = new UtilityDataSource(this);
        db.open();
        mainUtilityList = db.getAllUtilities();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupChart(getIntent().getIntExtra("days", 0));
    }

    private void setupChart(int days) {
        if(days == DAYS_IN_4_WEEKS)
        {
            create28DayGraph();
        }
        else if(days == DAYS_IN_YEAR)
        {
            create365DayGraph();
        }
    }

    private void create365DayGraph() {
        BarChart chart = (BarChart) findViewById(R.id.barChart);
        BarDataSet busSet, skytrainSet, walk_bikeSet, electricSet, naturalGasSet, carSet;

        XAxis xAxis = chart.getXAxis();
        String[] xAxisValues = {"jan", "feb","march","april","may","june","july","august","sept","dec","oct","nov"};

        List<BarEntry> busEntries = new ArrayList<>();
        List<BarEntry> skytrainEntries = new ArrayList<>();
        List<BarEntry> walk_bikeEntries = new ArrayList<>();
        List<BarEntry> carEntries = new ArrayList<>();
        List<BarEntry> electricityEntries = new ArrayList<>();
        List<BarEntry> naturalGasEntries = new ArrayList<>();

        for(int m = 0; m < MONTH_COUNT; m++) //iterate through 12 months
        {
            List<Float> temp_yValues = new ArrayList<>();

            float busEmissions = getJourneyEmissionsForMonthForTransportType(m, Vehicle.BUS);
            float skytrainEmissions = getJourneyEmissionsForMonthForTransportType(m, Vehicle.SKYTRAIN);
            float walk_bikeEmissions = getJourneyEmissionsForMonthForTransportType(m, Vehicle.WALK_BIKE);

            busEntries.add(new BarEntry(m, busEmissions));
            skytrainEntries.add(new BarEntry(m, skytrainEmissions));
            walk_bikeEntries.add(new BarEntry(m, walk_bikeEmissions));

            Map<Vehicle, Float> carMap = getVehicleEmissionTotalsFromJourneysInMonth(m);
            carMap.size();
            for(Map.Entry<Vehicle, Float> entry : carMap.entrySet()) {
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            float[] yvalues = new float[temp_yValues.size()];
            for(int u = 0; u < temp_yValues.size(); u++)
            {
                yvalues[u] = temp_yValues.get(u);
                carEntries.add(new BarEntry(m, temp_yValues.get(u)));
            }

            float electricEmissions = getUtilityEmissionsForMonthForUtilityType(Utility.ELECTRICITY_NAME, m);
            float gasEmissions = getUtilityEmissionsForMonthForUtilityType(Utility.GAS_NAME, m);
            electricityEntries.add(new BarEntry(m, electricEmissions));
            naturalGasEntries.add(new BarEntry(m, gasEmissions));

        }
        busSet = new BarDataSet(busEntries, "bus");
        carSet = new BarDataSet(carEntries, "cars");
        skytrainSet = new BarDataSet(skytrainEntries, "skytrain");
        walk_bikeSet = new BarDataSet(walk_bikeEntries, "walk/bike");
        electricSet = new BarDataSet(electricityEntries, "electricity");
        naturalGasSet = new BarDataSet(naturalGasEntries, "natural gas");

        int[] carColors = {getResources().getColor(R.color.CarColor1), getResources().getColor(R.color.CarColor2), getResources().getColor(R.color.CarColor3), getResources().getColor(R.color.CarColor4), getResources().getColor(R.color.CarColor5)};
        carSet.setColors(carColors);
        busSet.setColor(getResources().getColor(R.color.BusColor));
        skytrainSet.setColor(getResources().getColor(R.color.TrainColor));
        walk_bikeSet.setColor(getResources().getColor(R.color.Walk_bikeColor));
        electricSet.setColor(getResources().getColor(R.color.ElectricColor));
        naturalGasSet.setColor(getResources().getColor(R.color.NaturalGasColor));

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

        List<Date> dateList = getDateList(DAYS_IN_4_WEEKS);
        float c = 0;
        for(int i = dateList.size()-1; i >= 0; i--)
        {
            for(Map.Entry<String, Float> entry: getDailyTotalUtilityEmissions(dateList.get(i)).entrySet())
            {
                Float emissionTotal = entry.getValue();
                if(entry.getKey().equals(Utility.ELECTRICITY_NAME))
                    electricityEntries.add(new BarEntry(c, emissionTotal));
                else
                    naturalGasEntries.add(new BarEntry(c, emissionTotal));
            }

            List<Float> temp_yValues = new ArrayList<>();
            List<Journey> journeys = getJourneysForTransportModeOnDate(dateList.get(i), Vehicle.CAR);
            Map<Vehicle, Float> carMap = getCarEmissionTotalsFromJourneys(journeys);
            carMap.size();
            for(Map.Entry<Vehicle, Float> entry : carMap.entrySet()) {
                Float emissionTotal = entry.getValue();
                temp_yValues.add(emissionTotal);
            }
            float[] yvalues = new float[temp_yValues.size()];
            for(int u = 0; u < temp_yValues.size(); u++)
            {
                yvalues[u] = temp_yValues.get(u);
                entries.add(new BarEntry(c, temp_yValues.get(u)));
            }

            busEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.BUS)));
            skytrainEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.SKYTRAIN)));
            walk_bikeEntries.add(new BarEntry(c, (float)getTotalEmissionsForTransportModeOnDate(dateList.get(i), Vehicle.WALK_BIKE)));
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

        int[] carColors = {getResources().getColor(R.color.CarColor1), getResources().getColor(R.color.CarColor2), getResources().getColor(R.color.CarColor3), getResources().getColor(R.color.CarColor4), getResources().getColor(R.color.CarColor5)};
        carSet.setColors(carColors);
        busSet.setColor(getResources().getColor(R.color.BusColor));
        skytrainSet.setColor(getResources().getColor(R.color.TrainColor));
        walk_bikeSet.setColor(getResources().getColor(R.color.Walk_bikeColor));
        electricSet.setColor(getResources().getColor(R.color.ElectricColor));
        naturalGasSet.setColor(getResources().getColor(R.color.NaturalGasColor));

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

    private float getJourneyEmissionsForMonthForTransportType(int m, String transportModeWanted) {
        float totalEmissions = 0;
        for(Journey journey: mainJourneyList)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.MONTH == m;
            boolean isWantedTransportMode = journey.getVehicle().getTransport_mode().equals(transportModeWanted);
            if(isInMonth && isWantedTransportMode)
            {
                totalEmissions += journey.getCarbonEmitted();
            }
        }
        return totalEmissions;
    }

    private float getUtilityEmissionsForMonthForUtilityType(String utilityType, int m)
    {
        float totalEmissions = 0;
        for(Utility utility: mainUtilityList)
        {
            List<Date> utilityDates = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utility.getStartDate());

            while(calendar.getTime().before(utility.getEndDate()))
            {
                utilityDates.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }

            for(Date date: utilityDates) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                boolean isInMonth = cal.MONTH == m;
                boolean isWantedUtilityType = utilityType.equals(utility.getUtility_type());
                if (isInMonth && isWantedUtilityType) {
                    totalEmissions += utility.getPerDayUsage();
                }
            }
        }
        return totalEmissions;
    }

    private Map<Vehicle, Float> getVehicleEmissionTotalsFromJourneysInMonth(int month)
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: mainJourneyList)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.MONTH == month;
            boolean isCar = journey.getVehicle().getTransport_mode().equals(Vehicle.CAR);
            if(isInMonth && isCar)
            {
                journeyList.add(journey);
            }
        }
        Map<Vehicle, Float> map = new HashMap<>();
        for(Journey journey: journeyList)
        {
            Vehicle vehicle = journey.getVehicle();
            if(!map.containsKey(vehicle))
            {
                map.put(vehicle, (float)journey.getCarbonEmitted());
            }
            else
            {
                float temp = map.get(vehicle);
                temp += journey.getCarbonEmitted();
                map.put(vehicle, temp);
            }
        }
        return map;
    }


    private double getTotalEmissionsForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        double totalEmissions = 0;
        for(Journey journey: getJourneysForTransportModeOnDate(dateWanted, transportModeWanted))
        {
            totalEmissions += journey.getCarbonEmitted();
        }

        return totalEmissions;
    }

    private List<Date> getDateList(int daysInPeriod) //gets dates in period
    {
        List<Date> dateList = new ArrayList<>(daysInPeriod);
        for (int i=0; i < daysInPeriod; i++) {
            Calendar cal = Calendar.getInstance(); // this would default to now
            cal.add(Calendar.DAY_OF_YEAR, -i);
            dateList.add(cal.getTime());
        }
        return dateList;
    }

    private List<Journey> getJourneysForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: mainJourneyList)
        {
            Vehicle car = journey.getVehicle();
            Date journeyDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                journeyDateWithoutTime = sdf.parse(sdf.format(journey.getDate()));
                dateWantedWithoutTime = sdf.parse(sdf.format(dateWanted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean isWantedTransportMode = car.getTransport_mode().equals(transportModeWanted);
            boolean isInDate = journeyDateWithoutTime.equals(dateWantedWithoutTime);
            if(isWantedTransportMode && isInDate)
            {
                journeyList.add(journey);
            }
        }

        return journeyList;
    }


    /*private List<Journey> getJourneysForTransportationModeInMonth(Calendar calendar, String transportModeWanted) //
    {
        List<Journey> mainJourneyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Car car = journey.getCar();
            Calendar journeyCal = Calendar.getInstance();
            journeyCal.setTime(journey.getDate());

            boolean isWantedTransportMode = car.getTransport_mode().equals(transportModeWanted);
            boolean isSameMonth = calendar.MONTH == journeyCal.MONTH;
            if(isWantedTransportMode && isSameMonth)
            {
                mainJourneyList.add(journey);
            }
        }

        return mainJourneyList;
    }*/

    private Map<Vehicle, Float> getCarEmissionTotalsFromJourneys(List<Journey> journeyList)
    {
        Map<Vehicle, Float> map = new HashMap<>();
        for(Journey journey: journeyList)
        {
            if(!map.containsKey(journey.getVehicle()))
            {
                map.put(journey.getVehicle(), (float)journey.getCarbonEmitted());
            }
            else
            {
                float temp = map.get(journey.getVehicle());
                temp += journey.getCarbonEmitted();
                map.put(journey.getVehicle(), temp);
            }
        }
        return map;
    }

    private Map<String, Float> getDailyTotalUtilityEmissions(Date dateWanted) //return total of daily averages for electric and natural gas as separate keys-value pairs in map
    {
        Map<String, Float> map = new HashMap<>();
        map.put(Utility.ELECTRICITY_NAME, 0f); //set for electricity emission totals
        map.put(Utility.GAS_NAME, 0f); //set for gas emission totals

        for(Utility utility: mainUtilityList)  //each utility known
        {
            Date utilityStartDateWithoutTime = new Date();
            Date utilityEndDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                utilityStartDateWithoutTime = sdf.parse(sdf.format(utility.getStartDate()));
                utilityEndDateWithoutTime = sdf.parse(sdf.format(utility.getEndDate()));
                dateWantedWithoutTime = sdf.parse(sdf.format(dateWanted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean isInDate =  dateWantedWithoutTime.after(utilityStartDateWithoutTime) && dateWantedWithoutTime.before(utilityEndDateWithoutTime);
            if(isInDate)
            {
                float tempTotal = map.get(utility.getUtility_type());
                tempTotal += utility.getPerDayUsage();
                map.put(utility.getUtility_type(), tempTotal);
            }
        }
        return map;
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
