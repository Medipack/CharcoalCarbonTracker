package sfu.cmpt276.carbontracker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.Database;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;
/* Activity that displays carbon footprint in form of table of journeys or pie graph*/

public class JourneyActivity extends AppCompatActivity {
    private static final String TAG = "JourneyActivity";

    private static final int EDIT_CODE = 1000;

    private JourneyDataSource db;
    private List<Journey> journeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        setUpTable();
        setupPieButton();
        setupDatabase();
        populateJourneyList();
        populateListView();
        registerJourneyListCallBack();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FullScreencall();
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

    private void setUpTable() {
        String[] values = getResources().getStringArray(R.array.unitNames);
        TextView carbonValue = (TextView) findViewById(R.id.journey_table_emission);
        int settings = User.getInstance().checkSetting();
        carbonValue.setText(values[settings]);
    }

    private void setupDatabase() {
        db = new JourneyDataSource(this);
    }

    private void populateJourneyList() {
        db.open();
        journeyList = db.getAllJourneys(this);
        db.close();
    }

    private void setupPieButton() {
        Button pieButton = (Button) findViewById(R.id.pieButton);
        pieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyActivity.this, PieGraphActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateListView() {
        //Build adapter
        ArrayAdapter<Journey> adapter = new JourneyListAdapter(this, journeyList);
        //Configure items;
        ListView list = (ListView) findViewById(R.id.listJourney);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void registerJourneyListCallBack() {
        ListView list = (ListView) findViewById(R.id.listJourney);
        registerListShortClick(list);
        registerListLongClick(list);
    }

    private void registerListLongClick(ListView list) {
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(JourneyActivity.this);
                alert.setMessage(R.string.deleteJourneyAlert);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User user = User.getInstance();
                        //user.getJourneyList().remove(index);
                        Journey journeyToDelete = journeyList.get(index);
                        deleteJourney(journeyToDelete);
                    }
                });
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                String msg = "This journey has been longClicked";
                Log.i(TAG, msg);
                alert.show();
                return true;
            }
        });
    }

    private void deleteJourney(Journey journey) {
        Database.getDB().deleteJourney(journey);
        populateJourneyList();
        populateListView();
    }

    private void registerListShortClick(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journey journey = journeyList.get(position);
                String msg = "You clicked a journey with: " + journey.getVehicleName() +
                        " and " + journey.getRouteName();
                Log.i(TAG, msg);
                Intent intent = new Intent(JourneyActivity.this, EditJourneyActivity.class);
                intent.putExtra("index", position);
                startActivityForResult(intent, EDIT_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (EDIT_CODE) : {
                if (resultCode == EDIT_CODE) {
                    populateListView();
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
