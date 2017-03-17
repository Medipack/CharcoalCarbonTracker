package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;

public class SelectTransportationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_transportation);

        setupCarMode();
        setTitle();
    }

    private void setTitle() {
        TextView test = (TextView) findViewById(R.id.select);
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Albondigas.ttf");
        test.setTypeface(face);
    }

    private void setupCarMode() {
        Button carButton = (Button) findViewById(R.id.carMode);
        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTransportationActivity.this, TransportationModeActivity.class);
                startActivity(intent);
            }
        });
    }

}
