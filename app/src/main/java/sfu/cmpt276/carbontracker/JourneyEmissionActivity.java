package sfu.cmpt276.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JourneyEmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_emission);

        saveJourneyToList();
        setupTextViews();
    }

    private void saveJourneyToList() {
        User.getInstance().addJourney(User.getInstance().getCurrentJourney());
    }

    private void setupTextViews(){
        Journey journey = User.getInstance().getCurrentJourney();
        Car car = journey.getCar();
        Route route = journey.getRoute();

        TextView
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
