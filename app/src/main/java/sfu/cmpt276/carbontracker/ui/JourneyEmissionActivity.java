package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Route;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
/*activity to display journey emission*/
public class JourneyEmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_emission);

        saveJourneyToList();
        setupTextViews();

        setupShowTableButton();
        setuptxt();
        tipsDialog();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        loadCurrentIcon();
    }

    private void loadCurrentIcon() {
        Button currentIcon= (Button)findViewById(R.id.currentIconImg);
        int iconID = User.getInstance().getCurrentJourney().getVehicle().getIconID();
        TypedArray icons = getResources().obtainTypedArray(R.array.iconArray);
        currentIcon.setBackground(icons.getDrawable(iconID));
    }

    private void tipsDialog() {
        if (!User.getInstance().getJourneyList().isEmpty() || !User.getInstance().getUtilityList().getUtilities().isEmpty()) {
            FragmentManager manager = getSupportFragmentManager();
            TipDialogFragment tipDialog = new TipDialogFragment();
            tipDialog.show(manager, "TipsDialog");
        }
    }

    private void setuptxt() {
        TextView txt = (TextView) findViewById(R.id.journeyTitleTxt);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Peter.ttf");
        txt.setTypeface(face);
    }

    private void setupShowTableButton() {
        Button show = (Button) findViewById(R.id.journeyBtn);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyEmissionActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveJourneyToList() {
        //User.getInstance().addJourney(User.getInstance().getCurrentJourney());
    }

    private void setupTextViews(){
        Journey journey = User.getInstance().getCurrentJourney();
        Vehicle vehicle = journey.getVehicle();
        Route route = journey.getRoute();
        //todo change the grid data to include the extra vehicle data & add params to format grid
        String[] values = getResources().getStringArray(R.array.unitNames);
        String[] gridData = {
                getResources().getString(R.string.car_description), "",
                getResources().getString(R.string.car_name), vehicle.getNickname(),
                getResources().getString(R.string.make),  vehicle.getMake(),
                getResources().getString(R.string.model), vehicle.getModel(),
                getResources().getString(R.string.fuel_type), vehicle.getFuelType(),
                getResources().getString(R.string.transmission), vehicle.getTransmission(),
                getResources().getString(R.string.year), String.valueOf(vehicle.getYear()),
                getResources().getString(R.string.displacement), String.valueOf(new BigDecimal(vehicle.getEngineDispl()).setScale(2, RoundingMode.HALF_UP).doubleValue()),
                "", "",
                getResources().getString(R.string.route_info), "",
                getResources().getString(R.string.route_name), route.getRouteName(),
                getResources().getString(R.string.city_distance), String.valueOf(route.getRouteDistanceCity()),
                getResources().getString(R.string.highway_distance), String.valueOf(route.getRouteDistanceHighway()),
                "", "",
                getResources().getString(R.string.journey_info), "",
                getResources().getString(R.string.total_distance), String.valueOf(journey.getTotalDistance()),
                getResources().getString(R.string.date), String.valueOf(journey.getDate()),
                values[User.getInstance().checkSetting()], String.valueOf(journey.getCarbonEmitted()),
        };

        GridView journeyGrid = (GridView) findViewById(R.id.journeyGRid);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, gridData);
        journeyGrid.setHorizontalSpacing(0);
        journeyGrid.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        setResult(User.ACTIVITY_FINISHED_REQUESTCODE);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            setResult(User.ACTIVITY_FINISHED_REQUESTCODE);
            finish();
        return super.onOptionsItemSelected(item);
    }

}
