package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupNewJourneyBtn();
        setupCarbonTotalsBtn();
    }

    private void setupNewJourneyBtn()
    {
        Button newJourneyBtn = (Button)findViewById(R.id.createJourneyBtn);
        newJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TransportationModeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCarbonTotalsBtn()
    {
        Button newJourneyBtn = (Button)findViewById(R.id.viewCarbonTotalsBtn);
        newJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CarbonFootprintActivity.class);
                startActivity(intent);
            }
        });
    }

}
