package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;

public class CarbonFootprintActivity extends AppCompatActivity {

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
        User.getInstance().addJourney(User.getInstance().getCurrentJourney());
    }

    private void setupDebugTextView(){
        Journey selectedJourney = User.getInstance().getCurrentJourney();

        String debug = "Selected Journey: "
                + "\n- Route \"" + selectedJourney.getRouteName() + "\""
                + "\n- Vehicle \"" + selectedJourney.getVehicle().getShortDecription() + "\"" + selectedJourney.getTotalDistance()
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
