package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;

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

        Button tip = (Button) findViewById(R.id.tipButton);
        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupCarbon();
        setupUtility();
    }

    private void setupCarbon() {
        TextView carbon = (TextView) findViewById(R.id.carbon);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Super.ttf");
        carbon.setTypeface(face);

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

    private void setupUtility() {
        Button utilityBtn = (Button)findViewById(R.id.createUtilityBtn);
        utilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });
    }

}