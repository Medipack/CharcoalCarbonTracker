package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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


//Activity to allow the user to set the language

public class SettingsActivity extends AppCompatActivity {

    //Units Index
    public static  final int CO2 = 0;
    public  static final  int MONEY = 1;
    public  static final  int TREES = 2;

    //Hash Keys
    protected static final String ELECTRCITY = "Electricity";
    protected static final String NATURAL_GAS = "Natural Gas";
    protected static final String GASOLINE = "Gasoline";
    protected static final String DIESEL = "Diesel";
    protected static final String ELECTRIC_FUEL = "Electric Car";
    protected static final String BUS = "Bus";
    protected static final String WALK = "Walking";
    protected static final String SKYTRAIN = "Skytrain";

    //Names
    public static final String $_NAME = "Cost";
    public static final String CO2_NAME = "CO2";
    public static final String TREE_NAME = "Trees";

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
    int settingsChanged;
    HashMap<String, HashMap<String, String>> unitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsChanged = User.getInstance().checkSetting();
        temp = new unitConversion();
        //initialize the hash map
        unitMap = new HashMap<>();
        //Set up the file
        InputStream input = getResources().openRawResource(R.raw.units);
        //read the file
        readTheFile(unitMap, input);
        //initialize the toggle
        initializeTheSpinner(unitMap);
        //initialize the apply button
        setUpApplyBtn();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FullScreencall();
    }

    //Sets the view to fullscreen
    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setUpApplyBtn() {
        Button applyBtn = (Button) findViewById(R.id.settings_apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToPreferences();
                User.getInstance().setUnits(temp);
                User.getInstance().setUnitChanged(settingsChanged);
                finish();
            }
        });
    }

    private void saveToPreferences() {
        SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("position", settingsChanged);
        editor.putString("Name", temp.getUnitName());
        editor.putFloat(ELECTRCITY, (float) temp.getElectricityRate());
        editor.putFloat(NATURAL_GAS, (float) temp.getNaturalGasRate());
        editor.putFloat(GASOLINE, (float) temp.getGasolineRate());
        editor.putFloat(DIESEL, (float) temp.getDieselRate());
        editor.putFloat(ELECTRIC_FUEL, (float) temp.getElectricFuelRate());
        editor.putFloat(BUS, (float) temp.getBusRate());
        editor.putFloat(SKYTRAIN, (float) temp.getSkytrainRate());
        editor.putFloat(WALK, (float) temp.getWalkBikeRate());
        editor.commit();
    }

    private void initializeTheSpinner(final HashMap<String, HashMap<String, String>> unitMap) {
        final Spinner unitSpin = (Spinner) findViewById(R.id.settings_unitsToggle);
        //populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SettingsActivity.this, R.array.unitNames, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpin.setAdapter(adapter);
        //setDefault
        unitSpin.setSelection(User.getInstance().checkSetting());
        //set Functions
        unitSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == CO2){
                    List<Double> unitList = getHashedValues(CO2_NAME,unitMap);
                    temp.setValues(CO2_NAME,unitList);
                    settingsChanged = position;
                }else if (position == MONEY){
                    List<Double> unitList = getHashedValues($_NAME,unitMap);
                    temp.setValues($_NAME,unitList);
                    settingsChanged = position;
                }else if (position == TREES) {
                    List<Double> unitList = getHashedValues(TREE_NAME, unitMap);
                    temp.setValues(TREE_NAME, unitList);
                    settingsChanged = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                unitSpin.setSelection(settingsChanged);
            }
        });
    }

    @NonNull
    private List<Double> getHashedValues(String name, HashMap<String, HashMap<String, String>> unitMap) {
        //extract values from map
        HashMap<String, String> tempMap = unitMap.get(name);
        List<Double> unitList = new ArrayList<Double>();
        List<String> keyList = getKeys();
        for (String key: keyList){
            double value = valueOf(tempMap.get(key));
            unitList.add(value);
        }
        return unitList;
    }

    private List<String> getKeys() {
        List<String> keyList = new ArrayList<>();
        keyList.add(ELECTRCITY);
        keyList.add(NATURAL_GAS);
        keyList.add(GASOLINE);
        keyList.add(DIESEL);
        keyList.add(ELECTRIC_FUEL);
        keyList.add(BUS);
        keyList.add(SKYTRAIN);
        keyList.add(WALK);
        return keyList;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
