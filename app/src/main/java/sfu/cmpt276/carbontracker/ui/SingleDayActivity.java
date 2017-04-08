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
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    private String[] charts = {getString(R.string.single_day), getString(R.string.last_28_days), getString(R.string.last_year)};
    List journeyList = User.getInstance().getJourneyList();
    String str_date;
    Date singleDate;
    Date startDate;
    Date endDate;
    long oneDay = 1000 * 60 * 60 * 24;
    double emissionCar_single;
    double emissionBus_single;
    double emissionBike_single;
    double emissionSkytrain_single;
    double emissionRoute_single;
    double emissionCar_28;
    double emissionBus_28;
    double emissionBike_28;
    double emissionSkytrain_28;
    double emissionRoute_28;
    double emissionCar_365;
    double emissionBus_365;
    double emissionBike_365;
    double emissionSkytrain_365;
    double emissionRoute_365;
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
    int times_single;
    int times_28;
    int times_365;
    int times_route;
    final Calendar cal_current = Calendar.getInstance();
    int curr_year = cal_current.get(Calendar.YEAR) - 1900;
    int curr_month = cal_current.get(Calendar.MONTH);
    int curr_day = cal_current.get(Calendar.DAY_OF_MONTH);
    Date currentDate = new Date(curr_year, curr_month, curr_day);
    Date before_28 = new Date(currentDate.getTime() - 28 * oneDay);      //before 28 is the 28 days ago
    Date before_365 = new Date(currentDate.getTime() - 365 * oneDay);      ////before 365 is the 365 days ago
    SimpleDateFormat formatter = new SimpleDateFormat(MM_DD_YYYY);
    String str_before = formatter.format(before_28);
    String str_before365 = formatter.format(before_365);
    String str_journey;
    String str_current = formatter.format(currentDate);
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
                    List<PieEntry> pieEntries_route = new ArrayList<>();
                    if (chart_position == 0) {
                        times_route = times_route + 1;
                        //avoid adding additional data if click one more times
                        //journey part - [Route]
                        for (int r = 0; r < routeSize; r++) {
                            for (int j = 0; j < User.getInstance().getJourneyList().size(); j++) {
                                Journey journey = User.getInstance().getJourneyList().get(j);
                                journeyDate = journey.getDate();
                                str_journey = formatter.format(journeyDate);
                                String str_single = formatter.format(singleDate);
                                Route route = User.getInstance().getRouteList().getRoute(r);
                                //check whether in the selected day
                                if(Objects.equals(str_single, str_journey)) {
                                    if (Objects.equals(User.getInstance().getRouteList().getRoute(r).getRouteName(), journey.getRouteName())) {
                                        emissionRoute_single = journey.getCarbonEmitted();
                                        String str_emissionRoute = String.valueOf(emissionRoute_single);
                                        tempRoute = Float.valueOf(str_emissionRoute);
                                        pieEntries_route.add(new PieEntry(tempRoute, route.getRouteName()));
                                    }
                                }
                            }
                        }
                        Intent intent = getIntent();
                        str_date = intent.getStringExtra("date string");
                        //pie graph list
                        try {
                            DateFormat formatter;
                            formatter = new SimpleDateFormat(MM_DD_YYYY);
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
                                        pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
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
                                            pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                        }
                                    }
                                }
                                //electricity
                                else {
                                    //selected date in the period of one date
                                    if (singleDate.getTime() >= startDate.getTime() && singleDate.getTime() <= endDate.getTime()) {
                                        emissionShare =utility.getPerDayUsage();
                                        String str_singleEmission = String.valueOf(emissionShare);
                                        float temp = Float.valueOf(str_singleEmission);
                                        pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
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
                                            pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //28 pie graph - [Route]
                    if (chart_position == 1) {
                        times_route = times_route + 1;
                        //journey part - [Route]
                        for (int r = 0; r < routeSize; r++) {
                            for (int j = 0; j < User.getInstance().getJourneyList().size(); j++) {
                                Journey journey = User.getInstance().getJourneyList().get(j);
                                journeyDate = journey.getDate();
                                str_journey = formatter.format(journeyDate);
                                //check whether in last 28 days
                                if (journeyDate.getTime() >= before_28.getTime()) {
                                    Toast.makeText(SingleDayActivity.this, "yea", Toast.LENGTH_SHORT).show();
                                    Route route = User.getInstance().getRouteList().getRoute(r);
                                    if (Objects.equals(User.getInstance().getRouteList().getRoute(r).getRouteName(), journey.getRouteName())) {
                                        if (times_route <= 1) {
                                            emissionRoute_28 = emissionRoute_28 + journey.getCarbonEmitted();
                                        }
                                        String str_emissionRoute = String.valueOf(emissionRoute_28);
                                        tempRoute = Float.valueOf(str_emissionRoute);
                                        pieEntries_route.add(new PieEntry(tempRoute, route.getRouteName()));
                                    }
                                }
                            }
                        }
                        for (int n = 0; n < User.getInstance().getUtilityList().countUtility(); n++) {
                            utility_28 = User.getInstance().getUtilityList().getUtility(n);
                            startDate = utility_28.getStartDate();
                            endDate = utility_28.getEndDate();
                            //the period of bill in the last 28 days
                            if (startDate.getTime() >= before_28.getTime() && endDate.getTime() <= currentDate.getTime()) {
                                //get total gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //startDate - 28 days - endDate (current)
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() == currentDate.getTime()) {
                                //get the 28 days gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 28;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //start date before 28 days, end date in the 28 days
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() >= before_28.getTime()) {
                                //calculate the days between end date and 28 days before
                                long periodEnd = (endDate.getTime() - before_28.getTime()) / oneDay;
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //the period not in the last 28 days
                            else if (startDate.getTime() < before_28.getTime() && endDate.getTime() < before_28.getTime()) {
                                emissionShare = 0.0;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                        }
                    }
                    //365 pie graph - [Route]
                    else if (chart_position == 2) {
                        times_route = times_route + 1;
                        //journey part - [Route]
                        for (int r = 0; r < routeSize; r++) {
                            for (int j = 0; j < User.getInstance().getJourneyList().size(); j++) {
                                Journey journey = User.getInstance().getJourneyList().get(j);
                                journeyDate = journey.getDate();
                                str_journey = formatter.format(journeyDate);
                                //check whether in last 365 days
                                if (journeyDate.getTime() >= before_365.getTime()) {
                                    Route route = User.getInstance().getRouteList().getRoute(r);
                                    if (Objects.equals(User.getInstance().getRouteList().getRoute(r).getRouteName(), journey.getRouteName())) {
                                        if (times_route <= 1) {
                                            emissionRoute_365 = emissionRoute_365 + journey.getCarbonEmitted();
                                            String str_emissionRoute = String.valueOf(emissionRoute_365);
                                            tempRoute = Float.valueOf(str_emissionRoute);
                                            pieEntries_route.add(new PieEntry(tempRoute, route.getRouteName()));
                                        }
                                    }
                                }
                            }
                        }
                        for (int n = 0; n < User.getInstance().getUtilityList().countUtility(); n++) {
                            utility_365 = User.getInstance().getUtilityList().getUtility(n);
                            startDate = utility_365.getStartDate();
                            endDate = utility_365.getEndDate();
                            //the period of bill in the last 365 days
                            if (startDate.getTime() >= before_365.getTime() && endDate.getTime() <= currentDate.getTime()) {
                                //get total gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //startDate - 365 days - endDate (current)
                            else if (startDate.getTime() < before_365.getTime() && endDate.getTime() == currentDate.getTime()) {
                                //get the 365 days gas used
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 365;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //start date before 365 days, end date in the 365 days
                            else if (startDate.getTime() < before_365.getTime() && endDate.getTime() >= before_365.getTime()) {
                                //calculate the days between end date and 365 days before
                                long periodEnd = (endDate.getTime() - before_365.getTime()) / oneDay;
                                emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                            //the period not in the last 365 days
                            else if (startDate.getTime() < before_365.getTime() && endDate.getTime() < before_365.getTime()) {
                                emissionShare = 0.0;
                                String str_singleEmission = String.valueOf(emissionShare);
                                float temp = Float.valueOf(str_singleEmission);
                                if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                                    pieEntries_route.add(new PieEntry(temp, Utility.GAS_NAME));
                                } else {
                                    pieEntries_route.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                                }
                            }
                        }
                    }
                    //show pie graph - single + 28 + 365 - [Route]
                    PieDataSet dataSet = new PieDataSet(pieEntries_route, getString(R.string.emission));
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    PieData data = new PieData(dataSet);
                    //get the chart:
                    chart_single = (PieChart) findViewById(R.id.singleDayChart);
                    chart_single.setData(data);
                    chart_single.animateY(1000);
                    chart_single.invalidate();
                }
                else{
                    Toast.makeText(SingleDayActivity.this, getString(R.string.mode), Toast.LENGTH_SHORT).show();
                    setupChart();
                }
            }
        });
    }
    //set default part - [transportation]
    private void setupChart() {
        if(chart_position == 0){
            times_single = times_single + 1;
            Intent intent = getIntent();
            str_date = intent.getStringExtra("date string");
            //pie graph list
            List<PieEntry> pieEntries = new ArrayList<>();
            try {
                DateFormat formatter;
                formatter = new SimpleDateFormat(MM_DD_YYYY);
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
            for (int k = 0; k < journeyList.size(); k++) {
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);
                String str_single = formatter.format(singleDate);
                if (Objects.equals(str_single, str_journey)) {
                    String vehicle = journey.getVehicleName();
                    if (vehicle.equals("Bus")) {
                        bus_exist = 1;
                        vehicleBus = vehicle;
                        if(times_single <= 1) {
                            emissionBus_single = emissionBus_single + journey.getCarbonEmitted();
                        }
                        String str_emissionBus = String.valueOf(emissionBus_single);
                        tempBus = Float.valueOf(str_emissionBus);
                    }
                    else if (vehicle.equals("Skytrain")) {
                        bus_exist = 1;
                        vehicleSkytrain = vehicle;
                        if(times_single <= 1) {
                            emissionSkytrain_single = emissionSkytrain_single + journey.getCarbonEmitted();
                        }
                        String str_emissionSkytrain = String.valueOf(emissionSkytrain_single);
                        tempSkytrain = Float.valueOf(str_emissionSkytrain);
                    }
                    else if (vehicle.equals("Bike")) {
                        bus_exist = 1;
                        vehicleBike = vehicle;
                        if(times_single <= 1) {
                            emissionBike_single = emissionBike_single + journey.getCarbonEmitted();
                        }
                        String str_emissionBike = String.valueOf(emissionBike_single);
                        tempBike = Float.valueOf(str_emissionBike);
                    }
                    //car
                    else {
                        vehicleCar = vehicle;
                        emissionCar_single = journey.getCarbonEmitted();
                        String str_emissionCar = String.valueOf(emissionCar_single);
                        tempCar = Float.valueOf(str_emissionCar);
                        pieEntries.add(new PieEntry(tempCar, vehicleCar));
                    }
                }
            }
            if (bus_exist == 1) {
                pieEntries.add(new PieEntry(tempBus, vehicleBus));
            }
            if (skytrain_exist == 1) {
                pieEntries.add(new PieEntry(tempSkytrain, vehicleSkytrain));
            }
            if (bike_exist == 1) {
                pieEntries.add(new PieEntry(tempBike, vehicleBike));
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
        //28 pie graph - default
        else if(chart_position == 1) {
            List<PieEntry> pieEntries_28 = new ArrayList<>();
            times_28 = times_28 + 1;
            //none extra addition
            //if (times_28 <= 1) {
            //28 pie graph - journey part
            for (int k = 0; k < journeyList.size(); k++) {
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);
                if (journeyDate.getTime() >= before_28.getTime()) {
                    String vehicle = journey.getVehicleName();
                    if (vehicle.equals("Bus")) {
                        bus_exist = 1;
                        vehicleBus = vehicle;
                        if(times_28 <= 1) {
                            emissionBus_28 = emissionBus_28 + journey.getCarbonEmitted();
                        }
                        String str_emissionBus = String.valueOf(emissionBus_28);
                        tempBus = Float.valueOf(str_emissionBus);
                    }
                    else if (vehicle.equals("Skytrain")) {
                        bus_exist = 1;
                        vehicleSkytrain = vehicle;
                        if(times_28 <= 1) {
                            emissionSkytrain_28 = emissionSkytrain_28 + journey.getCarbonEmitted();
                        }
                        String str_emissionSkytrain = String.valueOf(emissionSkytrain_28);
                        tempSkytrain = Float.valueOf(str_emissionSkytrain);
                    }
                    else if (vehicle.equals("Bike")) {
                        bus_exist = 1;
                        vehicleBike = vehicle;
                        if(times_28 <= 1) {
                            emissionBike_28 = emissionBike_28 + journey.getCarbonEmitted();
                        }
                        String str_emissionBike = String.valueOf(emissionBike_28);
                        tempBike = Float.valueOf(str_emissionBike);
                    }
                    //car
                    else {
                        vehicleCar = vehicle;
                        emissionCar_28 = journey.getCarbonEmitted();
                        String str_emissionCar = String.valueOf(emissionCar_28);
                        tempCar = Float.valueOf(str_emissionCar);
                        pieEntries_28.add(new PieEntry(tempCar, vehicleCar));
                    }
                }
            }
            if (bus_exist == 1) {
                pieEntries_28.add(new PieEntry(tempBus, vehicleBus));
            }
            if (skytrain_exist == 1) {
                pieEntries_28.add(new PieEntry(tempSkytrain, vehicleSkytrain));
            }
            if (bike_exist == 1) {
                pieEntries_28.add(new PieEntry(tempBike, vehicleBike));
            }
            //28 pie graph - utility part
            //natural gas + electricity
            for (int n = 0; n < User.getInstance().getUtilityList().countUtility(); n++) {
                utility_28 = User.getInstance().getUtilityList().getUtility(n);
                startDate = utility_28.getStartDate();
                endDate = utility_28.getEndDate();
                //the period of bill in the last 28 days
                if (startDate.getTime() >= before_28.getTime() && endDate.getTime() <= currentDate.getTime()) {
                    //get total gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
                //startDate - 28 days - endDate (current)
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() == currentDate.getTime()) {
                    //get the 28 days gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 28;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
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
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
                        pieEntries_28.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
                //the period not in the last 28 days
                else if (startDate.getTime() < before_28.getTime() && endDate.getTime() < before_28.getTime()) {
                    emissionShare = 0.0;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_28.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
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
            //}
        }
        //365
        else if(chart_position == 2) {
            List<PieEntry> pieEntries_365 = new ArrayList<>();
            times_365 = times_365 + 1;
            //365 pie graph - journey part
            //if (times_365 <= 1) {
            for (int k = 0; k < journeyList.size(); k++) {
                Journey journey = User.getInstance().getJourneyList().get(k);
                journeyDate = journey.getDate();
                str_journey = formatter.format(journeyDate);
                if (journeyDate.getTime() >= before_365.getTime()) {
                    String vehicle = journey.getVehicleName();
                    Toast.makeText(this, "" + vehicle, Toast.LENGTH_SHORT).show();
                    if (vehicle.equals("Bus")) {
                        bus_exist = 1;
                        vehicleBus = vehicle;
                        if (times_365 <= 1) {
                            emissionBus_365 = emissionBus_365 + journey.getCarbonEmitted();
                        }
                        String str_emissionBus = String.valueOf(emissionBus_365);
                        tempBus = Float.valueOf(str_emissionBus);
                    } else if (vehicle.equals("Skytrain")) {
                        bus_exist = 1;
                        vehicleSkytrain = vehicle;
                        if(times_365 <= 1) {
                            emissionSkytrain_365 = emissionSkytrain_365 + journey.getCarbonEmitted();
                        }
                        String str_emissionSkytrain = String.valueOf(emissionSkytrain_365);
                        tempSkytrain = Float.valueOf(str_emissionSkytrain);
                    } else if (vehicle.equals("Bike")) {
                        bus_exist = 1;
                        vehicleBike = vehicle;
                        if(times_365 <= 1) {
                            emissionBike_365 = emissionBike_365 + journey.getCarbonEmitted();
                        }
                        String str_emissionBike = String.valueOf(emissionBike_365);
                        tempBike = Float.valueOf(str_emissionBike);
                    }
                    //car
                    else {
                        vehicleCar = vehicle;
                        emissionCar_365 = journey.getCarbonEmitted();
                        String str_emissionCar = String.valueOf(emissionCar_365);
                        tempCar = Float.valueOf(str_emissionCar);
                        pieEntries_365.add(new PieEntry(tempCar, vehicleCar));
                    }
                }
            }
            if (car_exist == 1) {
                pieEntries_365.add(new PieEntry(tempCar, vehicleCar));
            }
            if (bus_exist == 1) {
                pieEntries_365.add(new PieEntry(tempBus, vehicleBus));
            }
            if (skytrain_exist == 1) {
                pieEntries_365.add(new PieEntry(tempSkytrain, vehicleSkytrain));
            }
            if (bike_exist == 1) {
                pieEntries_365.add(new PieEntry(tempBike, vehicleBike));
            }
            //365 pie graph - utility part
            //natural gas + electricity
            for (int n = 0; n < User.getInstance().getUtilityList().countUtility(); n++) {
                utility_365 = User.getInstance().getUtilityList().getUtility(n);
                startDate = utility_365.getStartDate();
                endDate = utility_365.getEndDate();
                //the period of bill in the last 365 days
                if (startDate.getTime() >= before_365.getTime() && endDate.getTime() <= currentDate.getTime()) {
                    //get total gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * User.getInstance().getUtilityList().getUtility(n).getDaysInPeriod();
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
                //startDate - 365 days - endDate (current)
                else if (startDate.getTime() < before_365.getTime() && endDate.getTime() == currentDate.getTime()) {
                    //get the 365 days gas used
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * 365;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
                //start date before 365 days, end date in the 365 days
                else if (startDate.getTime() < before_365.getTime() && endDate.getTime() >= before_365.getTime()) {
                    //calculate the days between end date and 28 days before
                    long periodEnd = (endDate.getTime() - before_365.getTime()) / oneDay;
                    emissionShare = User.getInstance().getUtilityList().getUtility(n).getPerDayUsage() * periodEnd;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
                        pieEntries_365.add(new PieEntry(temp, Utility.ELECTRICITY_NAME));
                    }
                }
                //the period not in the last 365 days
                else if (startDate.getTime() < before_365.getTime() && endDate.getTime() < before_365.getTime()) {
                    emissionShare = 0.0;
                    String str_singleEmission = String.valueOf(emissionShare);
                    float temp = Float.valueOf(str_singleEmission);
                    if (User.getInstance().getUtilityList().getUtility(n).getUtility_type().equals(Utility.GAS_NAME)) {
                        pieEntries_365.add(new PieEntry(temp, Utility.GAS_NAME));
                    } else {
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
            //}
        }
    }
}
