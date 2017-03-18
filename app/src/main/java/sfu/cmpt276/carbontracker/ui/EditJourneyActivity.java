package sfu.cmpt276.carbontracker.ui;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sfu.cmpt276.carbontracker.R;

public class EditJourneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);

        setupSelectModeTxt();
        setUpSpinner();



    }

    private void setupSelectModeTxt() {
        TextView selectTxt = (TextView) findViewById(R.id.edit_journey_selectTransportationMode);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Peter.ttf");
        selectTxt.setTypeface(face);
    }

    private void setUpSpinner() {
    }
}
