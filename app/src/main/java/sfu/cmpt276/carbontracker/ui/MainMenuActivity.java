package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.unitConversion;
import sfu.cmpt276.carbontracker.ui.database.Database;

/*  Menu Activity displays main menu
* */
public class MainMenuActivity extends AppCompatActivity {

    protected static final String ELECTRCITY = "Electricity";
    protected static final String NATURAL_GAS = "Natural Gas";
    protected static final String GASOLINE = "Gasoline";
    protected static final String DIESEL = "Diesel";
    protected static final String ELECTRIC_FUEL = "Electric Car";
    protected static final String BUS = "Bus";
    protected static final String WALK = "Walking";
    protected static final String SKYTRAIN = "Skytrain";

    public static final String DEFAULT_NAME = "CO2";
    public static final double ELECTRICITY_CO2 = 0.009; //kg of CO2 per KWh
    public static final double NATURAL_GAS_CO2 = 56.1; //kg of CO2 per GJ
    public static final double GASOLINE_CO2 = 2.34849; //kg of co2 per litre
    public static final double DIESEL_CO2 = 2.6839881; //kg of co2 per litre
    public static final double ELECTRIC_FUEL_CO2 = 0; //kg of co2 per gallon
    public static final double BUS_CO2 = 0.089; //kg of co2 per KM of travel
    public static final double WALK_BIKE_CO2 = 0; //kg of co2 per KM of travel
    public static final double SKYTRAIN_CO2 = 0; //kg of co2 per KM of travel todo: verify skytrain emisisons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Database.getDB().initializeDatabase(this);
        setStartingUnitSettings();

        setupMainDirectory();
        setupNewJourneyBtn();
        setupCarbonTotalsBtn();

        setupCarbon();
        setupUtility();
        setupGraph();
        FullScreencall();
    }

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

    private void setStartingUnitSettings() {
        SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.clear();
        int startSettings = settings.getInt("position", 0);
        User.getInstance().setUnitChanged(startSettings);
        unitConversion units = savedUnitPrefs(settings);
        User.getInstance().setUnits(units);
    }

    @NonNull
    private unitConversion savedUnitPrefs(SharedPreferences settings) {
        String name = settings.getString("Name", DEFAULT_NAME);
        double electricityRates = (double) settings.getFloat(ELECTRCITY, (float) ELECTRICITY_CO2);
        double naturalGasRates = (double) settings.getFloat(NATURAL_GAS, (float) NATURAL_GAS_CO2);
        double gasolineRates = (double) settings.getFloat(GASOLINE, (float) GASOLINE_CO2);
        double dieselRates = (double) settings.getFloat(DIESEL, (float) DIESEL_CO2);
        double electricityFuelRates = (double) settings.getFloat(ELECTRIC_FUEL, (float) ELECTRIC_FUEL_CO2);
        double busRates = (double) settings.getFloat(BUS, (float) BUS_CO2);
        double skyTrainRates = (double) settings.getFloat(SKYTRAIN, (float) SKYTRAIN_CO2);
        double walkRates = (double) settings.getFloat(WALK, (float) WALK_BIKE_CO2);
        return new unitConversion(name,
                                    electricityRates,
                                    naturalGasRates,
                                    gasolineRates,
                                    dieselRates,
                                    electricityFuelRates,
                                    busRates,
                                    skyTrainRates,
                                    walkRates);
    }

    private void setupCarbon() {
        TextView carbon = (TextView) findViewById(R.id.carbon);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Super.ttf");
        carbon.setTypeface(face);

    }

    private void setupMainDirectory(){
        if(User.getInstance().directoryNotSetup()){
            InputStream input = getResources().openRawResource(R.raw.vehicles);
            User.getInstance().setUpDirectory(input);
        }
    }

    private void setupNewJourneyBtn()
    {
        Button newJourneyBtn = (Button)findViewById(R.id.createJourneyBtn);
        newJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().createNewCurrentJourney();
                Intent intent = new Intent(MainMenuActivity.this, TransportationModeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCarbonTotalsBtn()
    {
        Button carbonTotalsBtn = (Button)findViewById(R.id.viewCarbonTotalsBtn);
        carbonTotalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUtility() {
        Button utilityBtn = (Button)findViewById(R.id.createUtilityBtn);
        utilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupGraph() {
        Button showGraphBtn = (Button) findViewById(R.id.showGraphBtn);
        showGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            //case R.id.about:
        }
        return super.onOptionsItemSelected(item);
    }




}