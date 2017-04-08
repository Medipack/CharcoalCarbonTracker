package sfu.cmpt276.carbontracker.ui;

import android.graphics.Typeface;
import android.os.Build;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.UtilityList;
import sfu.cmpt276.carbontracker.ui.database.UtilityDataSource;
/*Main utility activity to show utilities and allow use to add new ones*/
public class UtilityActivity extends AppCompatActivity {
    private ListView list;
    private int edit_position;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        populateListView();
        setupAddBtn();
        registerClickCallback();
        FullScreencall();
        setupBillListFonts();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupBillListFonts() {
        TextView carbon = (TextView) findViewById(R.id.listTitle);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/AlexBook.otf");
        carbon.setTypeface(face);

    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void tipDialogue() {
        if (!User.getInstance().getJourneyList().isEmpty() || !User.getInstance().getUtilityList().getUtilities().isEmpty()) {
            FragmentManager manager = getSupportFragmentManager();
            TipDialogFragment tipDialog = new TipDialogFragment();
            tipDialog.show(manager, "TipsDialog");
        }
    }

    private void setupAddBtn() {
        Button addBtn = (Button) findViewById(R.id.addBillList);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = BillActivity.makeIntent(UtilityActivity.this);
                startActivity(intent);
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,                       // Context for the activity
                R.layout.bill_item,          // Layout to use (create)
                getUtilityDescriptions(User.getInstance().getUtilityList())
        );            // Items to be displayed

        // Configure the list view
        list = (ListView) findViewById(R.id.utilityList);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
        User.getInstance().resetTips();
        tipDialogue();

    }

    private void registerClickCallback() {
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                edit_position = position;
                mode = 10;
                Intent intent = BillActivity.makeIntent(UtilityActivity.this);
                intent.putExtra("pos", edit_position);
                intent.putExtra("mode", mode);
                startActivity(intent);
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public String[] getUtilityDescriptions(UtilityList utilityList)
    {
        String[] descriptions = new String[utilityList.countUtility()];
        for(int i=0; i<utilityList.countUtility();i++) {
            Utility utility = utilityList.getUtility(i);
            if (utility.getUtility_type().equals("gas")) {
                descriptions[i] = utility.getUtility_type() + getString(R.string.fromUtilityList) + utility.getStartDate() +  getString(R.string.toUtilityList) + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + getString(R.string.daysTotalUtilityList) + utility.getNaturalGasUsed() + getString(R.string.usedByUtilityList) + utility.getNumberOfPeople()
                        + getString(R.string.peopleUtilityList) + getString(R.string.currentAvgUtilityList) + utility.getAverageGJCurrent() + getString(R.string.previousAvgGj) + utility.getAverageGJPrevious()
                        +  getString(R.string.peopleShareUtilityList) + utility.getPeopleShare() + getString(R.string.perPersonEmissionUtilityList) + utility.getPerPersonEmission()
                        + getString(R.string.perDayEmissionUtilityList) + utility.getPerDayUsage() + getString(R.string.gUtilityList);
            }

            else{
                descriptions[i] = utility.getUtility_type() + getString(R.string.fromUtilityList) + utility.getStartDate() + getString(R.string.toUtilityList) + utility.getEndDate()
                        + getString(R.string.newLine) + utility.getDaysInPeriod() + getString(R.string.daysTotalUtilityList) + utility.getElectricUsed() + getString(R.string.usedByUtilityList) + utility.getNumberOfPeople()
                        + getString(R.string.peopleUtilityList) + getString(R.string.currentAvgUtilityList) + utility.getAverageKWhCurrent() + getString(R.string.previousAvgKwhUtilityList) + utility.getAverageKWhPrevious()
                        +  getString(R.string.peopleShareKwh) + utility.getPeopleShare() + getString(R.string.perPersonEmissionUtilityList) + utility.getPerPersonEmission()
                        + getString(R.string.perDayEmissionUtilityList) + utility.getPerDayUsage() + getString(R.string.gUtilityList);
            }
        }
        return descriptions;
    }

}
