package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.Database;

/*  Menu Activity displays main menu
* */
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Database.getDB().initializeDatabase(this);

        setupMainDirectory();
        setupNewJourneyBtn();
        setupCarbonTotalsBtn();

        setupCarbon();
        setupUtility();
        setupGraph();

        setupAboutButton();
    }

    private void setupAboutButton() {
        Button aboutBtn = (Button)findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().createNewCurrentJourney();
                Intent intent = new Intent(MainMenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });    }


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
                Intent intent = new Intent(MainMenuActivity.this, TransportationModeActivity.class);
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
                Intent intent = new Intent(MainMenuActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUtility() {
        Button utilityBtn = (Button)findViewById(R.id.createUtilityBtn);
        utilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupGraph() {
        Button showGraphBtn = (Button) findViewById(R.id.showGraphBtn);
        showGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

    }

}