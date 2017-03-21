package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;

public class CarbonFootprintActivity extends AppCompatActivity {

    private final String TAG = "CarbonFootprintActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint);

        saveJourneyToList();
        setupDebugTextView();

        setupJump();
    }

    private void setupJump() {
        Button jump = (Button) findViewById(R.id.jumpToJ);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarbonFootprintActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveJourneyToList() {
        Log.i(TAG, "Saving Current Journey to Database");

        Journey journeyToSave = User.getInstance().getCurrentJourney();

        JourneyDataSource db = new JourneyDataSource(this);
        db.open();
        journeyToSave = db.insertJourney(journeyToSave, this);
        db.close();

        User.getInstance().addJourney(journeyToSave);
    }

    private void setupDebugTextView(){
        Journey selectedJourney = User.getInstance().getCurrentJourney();

        String debug = "Selected Journey: "
                + "\n- Route \"" + selectedJourney.getRouteName() + "\""
                + "\n- Car \"" + selectedJourney.getCar().getShortDecription() + "\"" + selectedJourney.getTotalDistance()
                + " " + selectedJourney.getCarbonEmitted();

        TextView textView = (TextView) findViewById(R.id.selectedJourney);
        textView.setText(debug);
    }


    @Override
    public void onBackPressed() {
        setResult(User.ACTIITY_FINISHED_REQUESTCODE);
        finish();
    }

}
