package sfu.cmpt276.carbontracker.ui;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.unitConversion;

import static java.lang.Double.valueOf;

public class SettingsActivity extends AppCompatActivity {

    //Hash Keys
    protected static final String ELECTRCITY = "Electricity";
    protected static final String NATURAL_GAS = "Natural Gas";
    protected static final String GASOLINE = "Gasoline";
    protected static final String DIESEL = "Diesel";
    protected static final String ELECTRIC_FUEL = "Electric Car";
    protected static final String BUS = "Bus";
    protected static final String WALK = "Walking";
    protected static final String SKYTRAIN = "Skytrain";
    public static final String $_NAME = "$";
    public static final String CO2_NAME = "CO2";
    //Set Index
    public static final int SET_ELECTRICITY = 0;
    public static final int SET_NATURAL_GAS = 1;
    public static final int SET_GASOLINE = 2;
    public static final int SET_DIESEL = 3;
    public static final int SET_ELECTRIC_C = 4;
    public static final int SET_BUSFARE = 5;
    public static final int SET_SKYTRAIN = 6;
    public static final int SET_WALK = 7;
    //Hash Index
    public static final int HASH_NAME = 0;
    public static final int HASH_ELECTRICITY = 1;
    public static final int HASH_NATURAL_GAS = 2;
    public static final int HASH_GAS = 3;
    public static final int HASH_DIESEL = 4;
    public static final int HASH_ELEC_C = 5;
    public static final int HASH_BUS = 6;
    public static final int HASH_SKYTRAIN = 7;
    public static final int HASH_WALK = 8;

    unitConversion temp;
    Boolean settingsChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingsChanged = User.getInstance().checkDefault();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        temp = new unitConversion(User.getInstance().getUnits());
        //initialize the hash map
        final HashMap<String, HashMap<String, String>> unitMap = new HashMap<>();
        //Set up the file
        InputStream input = getResources().openRawResource(R.raw.units);


        //read the file
        readTheFile(unitMap, input);
        //initialize the toggle
        initializeTheToggle(unitMap);
        //initialize the language spinner
        Spinner languageSpin = (Spinner) findViewById(R.id.settings_languageSpinner);
        //initialize the apply button
        setUpApplyBtn();

    }

    private void setUpApplyBtn() {
        Button applyBtn = (Button) findViewById(R.id.settings_apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setUnits(temp);
                User.getInstance().setUnitChanged(settingsChanged);
                SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("unitSettings", settingsChanged);
                editor.commit();
            }
        });
    }

    private void initializeTheToggle(final HashMap<String, HashMap<String, String>> unitMap) {
        ToggleButton unitToggle = (ToggleButton) findViewById(R.id.settings_unitsToggle);
        //check if default
        unitToggle.setChecked(User.getInstance().checkDefault());
        unitToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    List<Double> unitList = getHashedValues(CO2_NAME,unitMap);
                    setValues(CO2_NAME,unitList);
                    settingsChanged = false;
                }else if (isChecked){
                    List<Double> unitList = getHashedValues($_NAME, unitMap);
                    setValues($_NAME, unitList);
                    settingsChanged = true;
                }
            }
        });
    }

    private void setValues(String name, List<Double> unitList) {
        //set the values
        temp.setUnitName(name);
        temp.setElectricityRate(unitList.get(SET_ELECTRICITY));
        temp.setNaturalGasRate(unitList.get(SET_NATURAL_GAS));
        temp.setGasolineRate(unitList.get(SET_GASOLINE));
        temp.setDieselRate(unitList.get(SET_DIESEL));
        temp.setElectricFuelRate(unitList.get(SET_ELECTRIC_C));
        temp.setBusRate(unitList.get(SET_BUSFARE));
        temp.setSkytrainRate(unitList.get(SET_SKYTRAIN));
        temp.setWalkBikeRate(unitList.get(SET_WALK));
    }

    @NonNull
    private List<Double> getHashedValues(String name, HashMap<String, HashMap<String, String>> unitMap) {
        //extract values from map
        HashMap<String, String> tempMap = unitMap.get(name);
        List<Double> unitList = new ArrayList<Double>();
        for (String key: tempMap.keySet()){
            double value = valueOf(tempMap.get(key));
            unitList.add(value);
        }
        return unitList;
    }

    private void readTheFile(HashMap<String, HashMap<String, String>> unitMap, InputStream input) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, Charset.forName("UTF-8"))
        );
        String line;

        try {
            while((line = reader.readLine())!=null){
                //Initialize a list
                List<String> tempList;
                //tokenize each line
                tempList = readData(line);
                //populate the hashmap
                populateMap(unitMap, tempList);
            }
        } catch (IOException e) {
            Log.wtf("", "Input Exception");
            e.printStackTrace();
        }
    }

    private void populateMap(HashMap<String, HashMap<String, String>> unitMap, List<String> tempList) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put(ELECTRCITY, tempList.get(HASH_ELECTRICITY));
        tempMap.put(NATURAL_GAS, tempList.get(HASH_NATURAL_GAS));
        tempMap.put(GASOLINE, tempList.get(HASH_GAS));
        tempMap.put(DIESEL, tempList.get(HASH_DIESEL));
        tempMap.put(ELECTRIC_FUEL, tempList.get(HASH_ELEC_C));
        tempMap.put(BUS, tempList.get(HASH_BUS));
        tempMap.put(SKYTRAIN, tempList.get(HASH_SKYTRAIN));
        tempMap.put(WALK, tempList.get(HASH_WALK));
        unitMap.put(tempList.get(HASH_NAME), tempMap);
    }

    private List<String> readData(String line) {
        List<String> list = new ArrayList<>();
        String [] data = line.split(",");
        Collections.addAll(list, data);
        return list;
    }
}
