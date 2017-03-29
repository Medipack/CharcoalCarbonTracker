package sfu.cmpt276.carbontracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.unitConversion;

public class SettingsActivity extends AppCompatActivity {

    protected static final String ELECTRCITY = "Electricity";
    protected static final String NATURAL_GAS = "Natural Gas";
    protected static final String GASOLINE = "Gasoline";
    protected static final String DIESEL = "Diesel";
    protected static final String ELECTRIC_FUEL = "Electric Car";
    protected static final String BUS = "Bus";
    protected static final String WALK = "Walking";
    protected static final String SKYTRAIN = "Skytrain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        User user = User.getInstance();
        unitConversion units = user.getUnits();
        //initialize the hash map
        HashMap<String, HashMap<String, String>> unitMap = new HashMap<>();
        //Set up the file
        InputStream input = getResources().openRawResource(R.raw.units);
        //read the file
        readTheFile(unitMap, input);
        //initialize the toggle
        ToggleButton unitToggle = (ToggleButton) findViewById(R.id.settings_unitsToggle);
        //initialize the language spinner
        Spinner languageSpin = (Spinner) findViewById(R.id.settings_languageSpinner);
        //initialize the apply button
        Button applyBtn = (Button) findViewById(R.id.settings_apply);


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
        tempMap.put(ELECTRCITY, tempList.get(1));
        tempMap.put(NATURAL_GAS, tempList.get(2));
        tempMap.put(GASOLINE, tempList.get(3));
        tempMap.put(DIESEL, tempList.get(4));
        tempMap.put(ELECTRIC_FUEL, tempList.get(5));
        tempMap.put(BUS, tempList.get(6));
        tempMap.put(WALK, tempList.get(7));
        tempMap.put(SKYTRAIN, tempList.get(8));
        unitMap.put(tempList.get(0), tempMap);
    }

    private List<String> readData(String line) {
        List<String> list = new ArrayList<>();
        String [] data = line.split(",");
        for (String token: data){
            list.add(token);
        }
        return list;
    }
}
