package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class JourneyEmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_emission);

        saveJourneyToList();
        setupTextViews();

        setupShowTableButton();
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
        Car car = journey.getCar();
        Route route = journey.getRoute();
        //todo change the grid data to include the extra car data & add params to format grid
        String[] gridData = {
                getResources().getString(R.string.car_description), "",
                getResources().getString(R.string.car_name), car.getNickname(),
                getResources().getString(R.string.make),  car.getMake(),
                getResources().getString(R.string.model), car.getModel(),
                getResources().getString(R.string.fuel_type), car.getFuelType(),
                getResources().getString(R.string.transmission), car.getTransmission(),
                getResources().getString(R.string.year), String.valueOf(car.getYear()),
                getResources().getString(R.string.displacement), String.valueOf(new BigDecimal(car.getEngineDispl()).setScale(2, RoundingMode.HALF_UP).doubleValue()),
                "", "",
                getResources().getString(R.string.route_info), "",
                getResources().getString(R.string.route_name), route.getRouteName(),
                getResources().getString(R.string.city_distance), String.valueOf(route.getRouteDistanceCity()),
                getResources().getString(R.string.highway_distance), String.valueOf(route.getRouteDistanceHighway()),
                "", "",
                getResources().getString(R.string.journey_info), "",
                getResources().getString(R.string.total_distance), String.valueOf(journey.getTotalDistance()),
                getResources().getString(R.string.date), String.valueOf(journey.getDate()),
                getResources().getString(R.string.emission), String.valueOf(journey.getCarbonEmitted()),
        };

        GridView journeyGrid = (GridView) findViewById(R.id.journeyGRid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gridData);
        journeyGrid.setHorizontalSpacing(0);
        journeyGrid.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        setResult(User.ACTIITY_FINISHED_REQUESTCODE);
        finish();
    }

}
