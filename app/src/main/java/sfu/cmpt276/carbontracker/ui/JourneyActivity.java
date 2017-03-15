package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
/* Activity that displays carbon footprint in form of table of journeys or pie graph*/

public class JourneyActivity extends AppCompatActivity {
    List JourneyList = User.getInstance().getJourneyList();
    //Journey journey = User.getInstance().getCurrentJourney();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        setupTable();
        setupPieButton();
    }

    private void setupTable() {
        TableLayout journeyTable = (TableLayout) findViewById(R.id.table);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(0, 0, 0, 0);

        for (Journey journey : User.getInstance().getJourneyList()) {

            Log.i("TABLETEST", journey.getRouteName());

            String carName = journey.getCar().getNickname();
            String routeName = journey.getRouteName();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            String str_date = sdf.format(journey.getDate());
            double distance = journey.getRoute().getRouteDistanceCity() + journey.getRoute().getRouteDistanceHighway();
            String str_distance = String.format("%.2f", distance);
            double emission = journey.getCarbonEmitted();
            String str_emission = String.format("%.2f", emission);

            TextView textDate = new TextView(this);
            textDate.setText(str_date);
            textDate.setPadding(0,0,0,0);

            TextView textCar = new TextView(this);
            textCar.setText(carName);
            textCar.setPadding(0,0,0,0);

            TextView textRoute = new TextView(this);
            textRoute.setText(routeName);
            textRoute.setPadding(0,0,0,0);

            TextView textDistance = new TextView(this);
            textDistance.setText(str_distance);
            textDistance.setPadding(0,0,0,0);

            TextView textEmission = new TextView(this);
            textEmission.setText(str_emission);
            textEmission.setPadding(0,0,0,0);

            TableRow row = new TableRow(this);

            row.addView(textDate, lp);
            row.addView(textCar, lp);
            row.addView(textRoute, lp);
            row.addView(textDistance, lp);
            row.addView(textEmission, lp);
            journeyTable.addView(row);
        }

    }

    private void setupPieButton() {
        Button pieButton = (Button) findViewById(R.id.pieButton);
        pieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyActivity.this, PieGraphActivity.class);
                startActivity(intent);
            }
        });
    }
}
