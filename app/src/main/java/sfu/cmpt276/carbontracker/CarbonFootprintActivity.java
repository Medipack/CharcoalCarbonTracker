package sfu.cmpt276.carbontracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CarbonFootprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_emission);

        saveJourneyToList();
        setupDebugTextView();
    }

    private void saveJourneyToList() {
        User.getInstance().addJourney(User.getInstance().getCurrentJourney());
    }

    private void setupDebugTextView(){
        Journey selectedJourney = User.getInstance().getCurrentJourney();

        String debug = "Selected Journey: "
                + "\n- Route \"" + selectedJourney.getRouteName() + "\""
                + "\n- Car \"" + selectedJourney.getCar().getShortDecription() + "\"";

        TextView textView = (TextView) findViewById(R.id.selectedJourney);
        textView.setText(debug);
    }


    @Override
    public void onBackPressed() {
        setResult(User.ACTIITY_FINISHED_REQUESTCODE);
        finish();
    }

}
