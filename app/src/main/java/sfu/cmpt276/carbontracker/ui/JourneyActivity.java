package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
/* Activity that displays carbon footprint in form of table of journeys or pie graph*/

public class JourneyActivity extends AppCompatActivity {
    public static final String MY_APP = "MyApp";
    List<Journey> JourneyList = User.getInstance().getJourneyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        setupPieButton();
        populateListView();
        registerJourneyListCallBack();
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
        ArrayAdapter<Journey> adapter = new JourneyListAdapter(this);
        //Configure items;
        ListView list = (ListView) findViewById(R.id.listJourney);
        list.setAdapter(adapter);
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
                Journey journey = JourneyList.get(position);
                String msg = "This journey has been longClicked";
                Toast.makeText(JourneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.i(MY_APP, msg);
                return true;
            }
        });
    }

    private void registerListShortClick(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journey journey = JourneyList.get(position);
                String msg = "You clicked a journey with: " + journey.getVehicleName() +
                        " and " + journey.getRouteName();
                Toast.makeText(JourneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.i(MY_APP, msg);
            }
        });
    }
}
