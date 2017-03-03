package sfu.cmpt276.carbontracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

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

        addTestVehicleToArray();
    }

    private void addTestVehicleToArray() {
        carArrayList.add(new TestCarClass("My fav car", "Lamborghini", "Diablo", 1999));
        carArrayList.add(new TestCarClass("The fun car", "Porsche", "911", 2017));
        updateCarListView();
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

    private void updateCarListView() {
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
}
