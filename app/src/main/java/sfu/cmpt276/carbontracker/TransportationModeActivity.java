package sfu.cmpt276.carbontracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


class TestCarClass {
    String nickname;
    String make;
    String model;
    int year;

    TestCarClass(String nickname, String make, String model, int year){
        this.nickname = nickname;
        this.make = make;
        this.model = model;
        this.year = year;
    }
}

public class TransportationModeActivity extends AppCompatActivity {

    private final String TAG = "TransportationActivity";

    private ArrayList<TestCarClass> carArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_mode);

        setUpAddVehicleButton();
        setUpCarListView();
        registerListViewClickCallback();

        addTestVehicleToArray();
    }


    private void addTestVehicleToArray() {
        carArrayList.add(new TestCarClass("My fav car", "Lamborghini", "Diablo", 1999));
        carArrayList.add(new TestCarClass("The fun car", "Porsche", "911", 2017));
        carArrayList.add(new TestCarClass("The Ancient One", "Honda", "Civic", 1985));
    }

    private class CarListAdapter extends ArrayAdapter<TestCarClass> {

        CarListAdapter(Context context) {
            super(context, R.layout.car_listview_item, carArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Ensure we have a view (could have been passed a null)
            View itemView = convertView;
            if(itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.car_listview_item, parent, false);
            }

            // Get the current car
            TestCarClass car = carArrayList.get(position);

            // Fill the TextView
            TextView description = (TextView) itemView.findViewById(R.id.car_description);
            description.setText(car.nickname + ": " + car.make + " " + car.model + " (" + car.year + ")");

            return itemView;
        }
    }

    private void setUpCarListView() {
        ArrayAdapter<TestCarClass> carListAdapter = new CarListAdapter(TransportationModeActivity.this);
        ListView carList = (ListView) findViewById(R.id.carListView);
        carList.setAdapter(carListAdapter);
    }

    private void setUpAddVehicleButton() {
        Button addVehicleButton = (Button) findViewById(R.id.addVehicleButton);

        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Add Vehicle Button clicked");
            }
        });
    }

    private void registerListViewClickCallback() {
        ListView list = (ListView) findViewById(R.id.carListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has selected a vehicle
                TestCarClass car = carArrayList.get(i);
                Log.i(TAG, "User selected a vehicle");

                Toast.makeText(TransportationModeActivity.this,
                        "Selected " + car.make + " " + car.model,
                        Toast.LENGTH_SHORT).show();

                // todo go to route activity
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has long pressed to edit a vehicle
                TestCarClass car = carArrayList.get(i);
                Log.i(TAG, "User long pressed on a vehicle");

                Toast.makeText(TransportationModeActivity.this,
                        "Long pressed on " + car.make + " " + car.model,
                        Toast.LENGTH_SHORT).show();

                // todo edit selected vehicle
                return true;
            }
        });
    }
}
