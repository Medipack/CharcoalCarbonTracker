package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmissionActivity extends AppCompatActivity {
    private String emissionName;
    private int emissionCity;
    private int emissionHighway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission);

        setupInfo();
        setupBack();

    }



    private void setupInfo() {
        Intent intent = getIntent();
        emissionName = intent.getStringExtra("route name");
        emissionHighway = intent.getIntExtra("highway distance", 0);
        emissionCity = intent.getIntExtra("city distance", 0);

        TextView showRouteName = (TextView) findViewById(R.id.showRouteName);
        showRouteName.setText(emissionName);

    }
    //TODO not sure back to where
    private void setupBack() {
        Button back = (Button) findViewById(R.id.backEmission);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmissionActivity.this, RouteActivity.class);
                startActivity(intent);
            }
        });
    }



}
