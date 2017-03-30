package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.icu.text.UnicodeSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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
import sfu.cmpt276.carbontracker.carbonmodel.Route;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class SingleDayActivity extends AppCompatActivity {
    private String[] charts = {"SINGLE DAY", "LAST 28 DAYS", "LAST 365 DAYS"};
    List journeyList = User.getInstance().getJourneyList();
    String str_date;
    Date singleDate;
    Date startDate;
    Date endDate;

    long oneDay = 1000 * 60 * 60 * 24;

    double emissionCar;
    double emissionBus;
    double emissionBike;
    double emissionSkytrain;
    double emissionRoute;

    float tempCar;
    float tempBus;
    float tempSkytrain;
    float tempBike;
    float tempRoute;

    String vehicleCar;
    String vehicleBus;
    String vehicleSkytrain;
    String vehicleBike;

    int car_exist;
    int bus_exist;
    int skytrain_exist;
    int bike_exist;

    Double emissionShare;
    Date journeyDate;
    Utility utility;
    Utility utility_28;
    Utility utility_365;

    int dateIndex;

    long tempDiff_gas;
    long min_gas;
    long min = 5;       //check overlap
    long tempDiff_elec;
    long min_elec;

    int chart_position;

    PieChart chart_single;
    PieChart chart_28;
    PieChart chart_365;

    Switch modeSwitch;

    final Calendar cal_current = Calendar.getInstance();
    int curr_year = cal_current.get(Calendar.YEAR) - 1900;
    int curr_month = cal_current.get(Calendar.MONTH);
    int curr_day = cal_current.get(Calendar.DAY_OF_MONTH);
    Date currentDate = new Date(curr_year, curr_month, curr_day);
    Date before_28 = new Date(currentDate.getTime() - 28 * oneDay);      //before 28 is the 28 days ago
    Date before_365 = new Date(currentDate.getTime() - 365 * oneDay);      ////before 365 is the 365 days ago

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    String str_before = formatter.format(before_28);
    String str_journey;
    String str_current = formatter.format(currentDate);
    //String str_single = formatter.format(singleDate);

    int routeSize = User.getInstance().getRouteList().countRoutes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);
        setupSpinner();
        setupChart();
        setupSwitch();
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

    private void setupSwitch() {
        modeSwitch = (Switch) findViewById(R.id.modeSwitch);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(SingleDayActivity.this, "route", Toast.LENGTH_SHORT).show();

                    //single day pie graph - [Route]
                    if (chart_position == 0) {
                        List<PieEntry> pieEntries_route_single = new ArrayList<>();
                        for (int r = 0; r < routeSize; r++) {
                            for (int i = 0; i < User.getInstance().getJourneyList().size(); i++) {
                                //if single day equals to the journey list
                                Journey journey = User.getInstance().getJourneyList().get(i);
                                Route route = User.getInstance().getRouteList().getRoute(r);
                                String str_single = formatter.format(singleDate);
                                if (Objects.equals(str_single, str_journey)) {
                                    emissionRoute = journey.getCarbonEmitted();
                                    String str_emissionRoute = String.valueOf(emissionRoute);
                                    tempRoute = Float.valueOf(str_emissionRoute);
                                    pieEntries_route_single.add(new PieEntry(tempRoute, route.getRouteName()));
                                }
                                PieDataSet dataSet = new PieDataSet(pieEntries_route_single, "emission");
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                PieData data = new PieData(dataSet);
                                //get the chart:
                                chart_single = (PieChart) findViewById(R.id.singleDayChart);
                                chart_single.setData(data);
                                chart_single.animateY(1000);
                                chart_single.invalidate();
                            }
                        }
                    }

                    //28 pie graph - [Route]
                    if (chart_position == 1 && routeSize != 0) {
                        List<PieEntry> pieEntries_route_28 = new ArrayList<>();
                        //journey part - [Route]
                        for (int r = 0; r < routeSize; r++) {
                            //check whether in last 28 days
                            for (int j = 0; j < User.getInstance().getJourneyList().size(); j++) {
                                Journey journey = User.getInstance().getJourneyList().get(j);
                                Route route = User.getInstance().getRouteList().getRoute(r);
                                if (Objects.equals(User.getInstance().getRouteList().getRoute(r).getRouteName(), journey.getRouteName())) {
                                    //Toast.makeText(SingleDayActivity.this, "Yeah!", Toast.LENGTH_SHORT).show();
                                    emissionRoute = journey.getCarbonEmitted();
                                    String str_emissionRoute = String.valueOf(emissionRoute);
                                    tempRoute = Float.valueOf(str_emissionRoute);
                                    pieEntries_route_28.add(new PieEntry(tempRoute, route.getRouteName()));
                                }
                                PieDataSet dataSet = new PieDataSet(pieEntries_route_28, "emission");
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                PieData data = new PieData(dataSet);
                                //get the chart:
                                chart_single = (PieChart) findViewById(R.id.singleDayChart);
                                chart_single.setData(data);
                                chart_single.animateY(1000);
                                chart_single.invalidate();
                            }
                        }
                    }

                    //365 pie graph - [Route]
                    else if (chart_position == 2 && routeSize != 0) {
                        List<PieEntry> pieEntries_route_365 = new ArrayList<>();
                        //journey part - [Route]
                        for (int r = 0; r < routeSize; r++) {
                            //check whether in last 365 days
                            for (int j = 0; j < User.getInstance().getJourneyList().size(); j++) {
                                Journey journey = User.getInstance().getJourneyList().get(j);
                                Route route = User.getInstance().getRouteList().getRoute(r);
                                if (Objects.equals(User.getInstance().getRouteList().getRoute(r).getRouteName(), journey.getRouteName())) {
                                    //Toast.makeText(SingleDayActivity.this, "Yeah!", Toast.LENGTH_SHORT).show();
                                    emissionRoute = journey.getCarbonEmitted();
                                    String str_emissionRoute = String.valueOf(emissionRoute);
                                    float temp1 = Float.valueOf(str_emissionRoute);
                                    pieEntries_route_365.add(new PieEntry(temp1, route.getRouteName()));
                                }

                                PieDataSet dataSet = new PieDataSet(pieEntries_route_365, "emission");
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                PieData data = new PieData(dataSet);
                                //get the chart:
                                chart_single = (PieChart) findViewById(R.id.singleDayChart);
                                chart_single.setData(data);
                                chart_single.animateY(1000);
                                chart_single.invalidate();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(SingleDayActivity.this, "mode", Toast.LENGTH_SHORT).show();

                    if(chart_position == 0){
                        //chart_single.setVisibility(View.VISIBLE);
                        //chart_28.setVisibility(View.INVISIBLE);
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
                                        }
                                    }
                                }

                                //electricity
                                else {
                                    //selected date in the period of one date
                                    if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                                        //dateIndex = i;
                                        emissionShare =utility.getPerDayUsage();
                                        String str_singleEmission = String.valueOf(emissionShare);
                                        float temp = Float.valueOf(str_singleEmission);
                                        pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
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
                                        }
                                    }
                                }
                            }
                        }

                        //one day - journey part
                        String emission[] = new String[journeyList.size()];
                        for(int k=0; k<journeyList.size();k++){
                            Journey journey = User.getInstance().getJourneyList().get(k);
                            journeyDate = journey.getDate();
                            str_journey = formatter.format(journeyDate);
                            String str_journey = formatter.format(journeyDate);
                            String str_single = formatter.format(singleDate);
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
                    }

                    //28 pie graph
                    else if(chart_position == 1){
                        //chart_single.setVisibility(View.INVISIBLE);
                        //chart_28.setVisibility(View.VISIBLE);
                        List<PieEntry> pieEntries_28 = new ArrayList<>();

                        //28 pie graph - journey part
                        for(int k=0;k<journeyList.size();k++){
                            Journey journey = User.getInstance().getJourneyList().get(k);
                            journeyDate = journey.getDate();
                            str_journey = formatter.format(journeyDate);


                            if(Objects.equals(str_before, str_journey) || Objects.equals(str_current, str_journey) ||
                                    journeyDate.getTime() >= before_28.getTime() || journeyDate.getTime() <= currentDate.getTime()){
                                String vehicle = journey.getVehicleName();
                                if(vehicle.equals("Car")){
                                    car_exist = 1;
                                    vehicleCar = vehicle;
                                    emissionCar = emissionCar + journey.getCarbonEmitted();

                                    String str_emissionCar = String.valueOf(emissionCar);
                                    tempCar = Float.valueOf(str_emissionCar);
                                }

                                else if(vehicle.equals("Bus")){
                                    bus_exist = 1;
                                    vehicleBus = vehicle;
                                    emissionBus = emissionBus + journey.getCarbonEmitted();

                                    String str_emissionBus = String.valueOf(emissionBus);
                                    tempBus = Float.valueOf(str_emissionBus);
                                }

                                else if(vehicle.equals("Skytrain")){
                                    bus_exist = 1;
                                    vehicleSkytrain = vehicle;
                                    emissionSkytrain = emissionSkytrain + journey.getCarbonEmitted();

                                    String str_emissionSkytrain = String.valueOf(emissionSkytrain);
                                    tempSkytrain = Float.valueOf(str_emissionSkytrain);
                                }

                                else if(vehicle.equals("Bike")){
                                    bus_exist = 1;
                                    vehicleBike = vehicle;
                                    emissionBike = emissionBike + journey.getCarbonEmitted();

                                    String str_emissionBike = String.valueOf(emissionBike);
                                    tempBike = Float.valueOf(str_emissionBike);
                                }
                            }
                        }

                        if(car_exist == 1){
                            pieEntries_28.add(new PieEntry(tempCar, vehicleCar));
                        }
                        if(bus_exist == 1){
                            pieEntries_28.add(new PieEntry(tempBus, vehicleBus));
                        }
                        if(skytrain_exist == 1){
                            pieEntries_28.add(new PieEntry(tempSkytrain, vehicleSkytrain));
                        }
                        if(bike_exist == 1){
                            pieEntries_28.add(new PieEntry(tempBike, vehicleBike));
                        }



                        //28 pie graph - utility part
                        //natural gas + electricity
                        for(int n=0;n<User.getInstance().getUtilityList().countUtility();n++) {
                            utility_28 = User.getInstance().getUtilityList().getUtility(n);
                            startDate = utility_28.getStartDate();
                            endDate = utility_28.getEndDate();

                            //the period of bill in the last 28 days
                            if (startDate.getTime() >= before_28.getTime() && endDate.getTime() <= currentDate.getTime()) {
                                //get total gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //startDate - 28 days - endDate (current)
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() == currentDate.getTime()) {
                                //get the 28 days gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 28;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //start date before 28 days, end date in the 28 days
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() >= before_28.getTime()) {
                                //calculate the days between end date and 28 days before
                                long periodEnd = (endDate.getTime() - before_28.getTime()) / oneDay;
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //the period not in the last 28 days
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() < before_28.getTime()) {
                                emissionShare = 0.0;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                        }

                        PieDataSet dataSet = new PieDataSet(pieEntries_28, "emission");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        PieData data = new PieData(dataSet);

                        chart_28 = (PieChart) findViewById(R.id.singleDayChart);
                        chart_28.setData(data);
                        chart_28.animateY(1000);
                        chart_28.invalidate();
                    }

                    //365
                    else if(chart_position == 2){
                        //chart_single.setVisibility(View.INVISIBLE);
                        //chart_365.setVisibility(View.VISIBLE);
                        List<PieEntry> pieEntries_365 = new ArrayList<>();

                        //365 pie graph - journey part
                        for(int k=0;k<journeyList.size();k++){
                            Journey journey = User.getInstance().getJourneyList().get(k);
                            journeyDate = journey.getDate();
                            str_journey = formatter.format(journeyDate);

                            if(Objects.equals(str_before, str_journey) || Objects.equals(str_current, str_journey) ||
                                    journeyDate.getTime() >= before_365.getTime() || journeyDate.getTime() <= currentDate.getTime()){
                                String vehicle = journey.getVehicleName();
                                if(vehicle.equals("Car")){
                                    car_exist = 1;
                                    vehicleCar = vehicle;
                                    emissionCar = emissionCar + journey.getCarbonEmitted();

                                    String str_emissionCar = String.valueOf(emissionCar);
                                    tempCar = Float.valueOf(str_emissionCar);
                                }

                                else if(vehicle.equals("Bus")){
                                    bus_exist = 1;
                                    vehicleBus = vehicle;
                                    emissionBus = emissionBus + journey.getCarbonEmitted();

                                    String str_emissionBus = String.valueOf(emissionBus);
                                    tempBus = Float.valueOf(str_emissionBus);
                                }

                                else if(vehicle.equals("Skytrain")){
                                    bus_exist = 1;
                                    vehicleSkytrain = vehicle;
                                    emissionSkytrain = emissionSkytrain + journey.getCarbonEmitted();

                                    String str_emissionSkytrain = String.valueOf(emissionSkytrain);
                                    tempSkytrain = Float.valueOf(str_emissionSkytrain);
                                }

                                else if(vehicle.equals("Bike")){
                                    bus_exist = 1;
                                    vehicleBike = vehicle;
                                    emissionBike = emissionBike + journey.getCarbonEmitted();

                                    String str_emissionBike = String.valueOf(emissionBike);
                                    tempBike = Float.valueOf(str_emissionBike);
                                }
                            }
                        }

                        if(car_exist == 1){
                            pieEntries_365.add(new PieEntry(tempCar, vehicleCar));
                        }
                        if(bus_exist == 1){
                            pieEntries_365.add(new PieEntry(tempBus, vehicleBus));
                        }
                        if(skytrain_exist == 1){
                            pieEntries_365.add(new PieEntry(tempSkytrain, vehicleSkytrain));
                        }
                        if(bike_exist == 1){
                            pieEntries_365.add(new PieEntry(tempBike, vehicleBike));
                        }

                        //365 pie graph - utility part
                        //natural gas + electricity
                        for(int n=0;n<User.getInstance().getUtilityList().countUtility();n++) {
                            utility_365 = User.getInstance().getUtilityList().getUtility(n);
                            startDate = utility_365.getStartDate();
                            endDate = utility_365.getEndDate();

                            //the period of bill in the last 365 days
                            if (startDate.getTime() >= before_365.getTime() && endDate.getTime() <= currentDate.getTime()) {
                                //get total gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //startDate - 365 days - endDate (current)
                            else if (startDate.getTime() < before_365.getTime() && endDate.getTime() == currentDate.getTime()) {
                                //get the 365 days gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 365;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //start date before 365 days, end date in the 365 days
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() >= before_28.getTime()) {
                                //calculate the days between end date and 28 days before
                                long periodEnd = (endDate.getTime() - before_28.getTime()) / oneDay;
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }

                            //the period not in the last 365 days
                            else if (startDate.getTime() < before_365.getTime() && endDate.getTime() < before_365.getTime()) {
                                emissionShare = 0.0;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);

                                if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                                }
                                else{
                                    pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                        }

                        PieDataSet dataSet = new PieDataSet(pieEntries_365, "emission");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        PieData data = new PieData(dataSet);

                        chart_365 = (PieChart) findViewById(R.id.singleDayChart);
                        chart_365.setData(data);
                        chart_365.animateY(1000);
                        chart_365.invalidate();
                    }
                }
            }
        });
    }


    private void setupChart() {
        if(chart_position == 0){
            //chart_single.setVisibility(View.VISIBLE);
            //chart_28.setVisibility(View.INVISIBLE);
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
                            }
                        }
                    }

                    //electricity
                    else {
                        //selected date in the period of one date
                        if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                            //dateIndex = i;
                            emissionShare =utility.getPerDayUsage();
                            String str_singleEmission = String.valueOf(emissionShare);
                            float temp = Float.valueOf(str_singleEmission);
                            pieEntries.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
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
                            }
                        }
                    }
                }
            }

            //one day - journey part
            String emission[] = new String[journeyList.size()];
            for(int k=0; k<journeyList.size();k++){
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);
                String str_journey = formatter.format(journeyDate);
                String str_single = formatter.format(singleDate);
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
        }

        //28 pie graph - [mode]
        else if(chart_position == 1){
            //chart_single.setVisibility(View.INVISIBLE);
            //chart_28.setVisibility(View.VISIBLE);
            List<PieEntry> pieEntries_28 = new ArrayList<>();

            //28 pie graph - journey part
            for(int k=0;k<journeyList.size();k++){
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);


                if(Objects.equals(str_before, str_journey) || Objects.equals(str_current, str_journey) ||
                        journeyDate.getTime() >= before_28.getTime() || journeyDate.getTime() <= currentDate.getTime()){
                    String vehicle = journey.getVehicleName();
                    Toast.makeText(this, "" + vehicle, Toast.LENGTH_SHORT).show();
                    if(vehicle.equals("Car")){
                        car_exist = 1;
                        vehicleCar = vehicle;
                        emissionCar = emissionCar + journey.getCarbonEmitted();

                        String str_emissionCar = String.valueOf(emissionCar);
                        tempCar = Float.valueOf(str_emissionCar);
                    }

                    else if(vehicle.equals("Bus")){
                        bus_exist = 1;
                        vehicleBus = vehicle;
                        emissionBus = emissionBus + journey.getCarbonEmitted();

                        String str_emissionBus = String.valueOf(emissionBus);
                        tempBus = Float.valueOf(str_emissionBus);
                    }

                    else if(vehicle.equals("Skytrain")){
                        bus_exist = 1;
                        vehicleSkytrain = vehicle;
                        emissionSkytrain = emissionSkytrain + journey.getCarbonEmitted();

                        String str_emissionSkytrain = String.valueOf(emissionSkytrain);
                        tempSkytrain = Float.valueOf(str_emissionSkytrain);
                    }

                    else if(vehicle.equals("Bike")){
                        bus_exist = 1;
                        vehicleBike = vehicle;
                        emissionBike = emissionBike + journey.getCarbonEmitted();

                        String str_emissionBike = String.valueOf(emissionBike);
                        tempBike = Float.valueOf(str_emissionBike);
                    }
                }
            }

            if(car_exist == 1){
                pieEntries_28.add(new PieEntry(tempCar, vehicleCar));
            }
            if(bus_exist == 1){
                pieEntries_28.add(new PieEntry(tempBus, vehicleBus));
            }
            if(skytrain_exist == 1){
                pieEntries_28.add(new PieEntry(tempSkytrain, vehicleSkytrain));
            }
            if(bike_exist == 1){
                pieEntries_28.add(new PieEntry(tempBike, vehicleBike));
            }

            //28 pie graph - utility part
            //natural gas + electricity
            for(int n=0;n<User.getInstance().getUtilityList().countUtility();n++) {
                utility_28 = User.getInstance().getUtilityList().getUtility(n);
                startDate = utility_28.getStartDate();
                endDate = utility_28.getEndDate();

                //the period of bill in the last 28 days
                if (startDate.getTime() >= before_28.getTime() && endDate.getTime() <= currentDate.getTime()) {
                    //get total gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //startDate - 28 days - endDate (current)
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() == currentDate.getTime()) {
                    //get the 28 days gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 28;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //start date before 28 days, end date in the 28 days
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() >= before_28.getTime()) {
                    //calculate the days between end date and 28 days before
                    long periodEnd = (endDate.getTime() - before_28.getTime()) / oneDay;
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //the period not in the last 28 days
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() < before_28.getTime()) {
                    emissionShare = 0.0;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
            }

            PieDataSet dataSet = new PieDataSet(pieEntries_28, "emission");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);

            chart_28 = (PieChart) findViewById(R.id.singleDayChart);
            chart_28.setData(data);
            chart_28.animateY(1000);
            chart_28.invalidate();
        }

        //365
        else if(chart_position == 2){
            //chart_single.setVisibility(View.INVISIBLE);
            //chart_365.setVisibility(View.VISIBLE);
            List<PieEntry> pieEntries_365 = new ArrayList<>();

            //365 pie graph - journey part
            for(int k=0;k<journeyList.size();k++){
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);

                if(Objects.equals(str_before, str_journey) || Objects.equals(str_current, str_journey) ||
                        journeyDate.getTime() >= before_365.getTime() || journeyDate.getTime() <= currentDate.getTime()){
                    String vehicle = journey.getVehicleName();
                    Toast.makeText(this, "" + vehicle, Toast.LENGTH_SHORT).show();
                    if(vehicle.equals("Car")){
                        car_exist = 1;
                        vehicleCar = vehicle;
                        emissionCar = emissionCar + journey.getCarbonEmitted();

                        String str_emissionCar = String.valueOf(emissionCar);
                        tempCar = Float.valueOf(str_emissionCar);
                    }

                    else if(vehicle.equals("Bus")){
                        bus_exist = 1;
                        vehicleBus = vehicle;
                        emissionBus = emissionBus + journey.getCarbonEmitted();

                        String str_emissionBus = String.valueOf(emissionBus);
                        tempBus = Float.valueOf(str_emissionBus);
                    }

                    else if(vehicle.equals("Skytrain")){
                        bus_exist = 1;
                        vehicleSkytrain = vehicle;
                        emissionSkytrain = emissionSkytrain + journey.getCarbonEmitted();

                        String str_emissionSkytrain = String.valueOf(emissionSkytrain);
                        tempSkytrain = Float.valueOf(str_emissionSkytrain);
                    }

                    else if(vehicle.equals("Bike")){
                        bus_exist = 1;
                        vehicleBike = vehicle;
                        emissionBike = emissionBike + journey.getCarbonEmitted();

                        String str_emissionBike = String.valueOf(emissionBike);
                        tempBike = Float.valueOf(str_emissionBike);
                    }
                }
            }

            if(car_exist == 1){
                pieEntries_365.add(new PieEntry(tempCar, vehicleCar));
            }
            if(bus_exist == 1){
                pieEntries_365.add(new PieEntry(tempBus, vehicleBus));
            }
            if(skytrain_exist == 1){
                pieEntries_365.add(new PieEntry(tempSkytrain, vehicleSkytrain));
            }
            if(bike_exist == 1){
                pieEntries_365.add(new PieEntry(tempBike, vehicleBike));
            }

            //365 pie graph - utility part
            //natural gas + electricity
            for(int n=0;n<User.getInstance().getUtilityList().countUtility();n++) {
                utility_365 = User.getInstance().getUtilityList().getUtility(n);
                startDate = utility_365.getStartDate();
                endDate = utility_365.getEndDate();

                //the period of bill in the last 365 days
                if (startDate.getTime() >= before_365.getTime() && endDate.getTime() <= currentDate.getTime()) {
                    //get total gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //startDate - 365 days - endDate (current)
                else if (startDate.getTime() < before_365.getTime() && endDate.getTime() == currentDate.getTime()) {
                    //get the 365 days gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 365;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //start date before 365 days, end date in the 365 days
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() >= before_28.getTime()) {
                    //calculate the days between end date and 28 days before
                    long periodEnd = (endDate.getTime() - before_28.getTime()) / oneDay;
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }

                //the period not in the last 365 days
                else if (startDate.getTime() < before_365.getTime() && endDate.getTime() < before_365.getTime()) {
                    emissionShare = 0.0;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);

                    if(User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    }
                    else{
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
            }

            PieDataSet dataSet = new PieDataSet(pieEntries_365, "emission");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);

            chart_365 = (PieChart) findViewById(R.id.singleDayChart);
            chart_365.setData(data);
            chart_365.animateY(1000);
            chart_365.invalidate();
        }
    }
}
