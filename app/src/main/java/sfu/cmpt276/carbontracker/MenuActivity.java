package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
/*  Menu Activity displays main menu
* */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupMainDirectory();
        setupNewJourneyBtn();
        setupCarbonTotalsBtn();
    }

    private void setupMainDirectory(){
        if(User.getInstance().directoryNotSetup()){
            InputStream input = getResources().openRawResource(R.raw.vehicles);
            User.getInstance().setUpDirectory(input);
        }
    }

    private void setupNewJourneyBtn()
    {
        Button newJourneyBtn = (Button)findViewById(R.id.createJourneyBtn);
        newJourneyBtn.setBackgroundResource(R.mipmap.menu1);
        newJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().createNewCurrentJourney();
                Intent intent = new Intent(MenuActivity.this, TransportationModeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCarbonTotalsBtn()
    {
        Button carbonTotalsBtn = (Button)findViewById(R.id.viewCarbonTotalsBtn);
        carbonTotalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

}