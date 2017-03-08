package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.listener.PieRadarChartTouchListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        for (Journey journey : User.getInstance().getJourneyList()) {

            Log.i("TABLETEST", journey.getRouteName());

            //Toast.makeText(this, "" + JourneyList.size(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "" + JourneyList.get(0), Toast.LENGTH_SHORT).show();
            String carName = journey.getCar().getNickname();
            String routeName = journey.getRouteName();
            //String routeName = User.getInstance().getRouteList().getRoute(i).getRouteName();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            String str_date = sdf.format(journey.getDate());
            //double distance = journey.getTotalDistance();
            double distance = journey.getRoute().getRouteDistanceCity() + journey.getRoute().getRouteDistanceHighway();
            String str_distance = String.valueOf(distance);
            //double emission = journey.getCarbonEmitted();
            double emission = journey.getCarbonEmitted();
            String str_emission = String.valueOf(emission);

            TextView textDate = new TextView(this);
            textDate.setText(str_date + "  ");

            TextView textCar = new TextView(this);
            textCar.setText(carName + "  ");

            TextView textRoute = new TextView(this);
            textRoute.setText(routeName + "  ");

            TextView textDistance = new TextView(this);
            textDistance.setText(str_distance + "  ");

            TextView textEmission = new TextView(this);
            textEmission.setText(str_emission + "  ");

            TableRow row = new TableRow(this);

            row.addView(textDate);
            row.addView(textCar);
            row.addView(textRoute);
            row.addView(textDistance);
            row.addView(textEmission);
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
