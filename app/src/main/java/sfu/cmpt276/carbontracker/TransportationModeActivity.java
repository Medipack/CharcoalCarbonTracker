package sfu.cmpt276.carbontracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TransportationModeActivity extends AppCompatActivity {

    private final String TAG = "TransportationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_mode);
        setUpAddVehicleButton();
        setUpCarListView();
        registerListViewClickCallback();

        //addTestVehicleToArray();
        setupCarDirectory();
    }

    private void setupCarDirectory() {
        User user = User.getInstance();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("vehicles",
                        "raw", this.getPackageName()));
        user.setUpDirectory(inputStream);
    }


    private void addTestVehicleToArray() {
        User user = User.getInstance();

        List<Car> carArrayList = user.getCarList();

        carArrayList.add(new Car("My fav car", "Lamborghini", "Diablo", 1999));
        carArrayList.add(new Car("The fun car", "Porsche", "911", 2017));
        carArrayList.add(new Car("The Ancient One", "Honda", "Civic", 1985));
    }

    private class CarListAdapter extends ArrayAdapter<Car> implements CarListener {

        CarListAdapter(Context context) {
            super(context, R.layout.car_listview_item, User.getInstance().getCarList());
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            // Ensure we have a view (could have been passed a null)
            View itemView = convertView;
            if(itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.car_listview_item, parent, false);
            }


            User user = User.getInstance();
            // Get the current car
            Car car = user.getCarList().get(position);

            // Fill the TextView
            TextView description = (TextView) itemView.findViewById(R.id.car_description);
            description.setText(car.getShortDecription());

            return itemView;
        }

        @Override
        public void carListWasEdited() {
            Log.i(TAG, "Car List changed, updating listview");
            notifyDataSetChanged();
        }
    }

    private void setUpCarListView() {
        ArrayAdapter<Car> carListAdapter = new CarListAdapter(TransportationModeActivity.this);
        User.getInstance().setCarListener((CarListener) carListAdapter);
        ListView carList = (ListView) findViewById(R.id.carListView);
        carList.setAdapter(carListAdapter);
    }

    private void launchNewVehicleDialog(){
        FragmentManager manager = getSupportFragmentManager();
        NewVehicleFragment dialog = new NewVehicleFragment();
        dialog.show(manager, "NewVehicleDialog");
    }


    private void setUpAddVehicleButton() {
        Button addVehicleButton = (Button) findViewById(R.id.addVehicleButton);

        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Add Vehicle Button clicked");
                launchNewVehicleDialog();
            }
        });
    }

    private void registerListViewClickCallback() {
        ListView list = (ListView) findViewById(R.id.carListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has selected a vehicle
                Car car = User.getInstance().getCarList().get(i);
                Log.i(TAG, "User selected vehicle \"" + car.getNickname()
                            + "\" " + car.getMake() + " " + car.getModel());

                // Set current Journey to use the selected car
                User.getInstance().setCurrentJourneyCar(car);

                Intent intent = new Intent(TransportationModeActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has long pressed to edit a vehicle
                Car car = User.getInstance().getCarList().get(i);
                Log.i(TAG, "User long pressed on a vehicle");

                Toast.makeText(TransportationModeActivity.this,
                        "Long pressed on " + car.getMake() + " " + car.getModel(),
                        Toast.LENGTH_SHORT).show();

                // todo edit selected vehicle
                return true;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == User.ACTIITY_FINISHED_REQUESTCODE) {
            finish();
        }
    }
}
