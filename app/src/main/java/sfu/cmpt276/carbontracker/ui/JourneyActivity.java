package sfu.cmpt276.carbontracker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

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
                alert.setMessage("Are you sure you want to delete this journey?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User user = User.getInstance();
                        //user.getJourneyList().remove(index);
                        Journey journeyToDelete = journeyList.get(index);
                        deleteJourney(journeyToDelete);
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
}
