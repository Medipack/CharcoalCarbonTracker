package sfu.cmpt276.carbontracker.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.CarDataSource;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.VehicleListener;

/* Displays list of vehicles, allows for adding, editing, deleting cars */
public class TransportationModeActivity extends AppCompatActivity {

    private final String TAG = "TransportationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_mode);
        setupCarDirectory();
        //addTestVehicleToArray();
        setupSelectModeTxt();
        setUpAddVehicleButton();
        setUpBikeButton();
        setUpSkytrainButton();
        setUpBusButton();
        setUpCarListView();
        registerListViewClickCallback();

        setupCarDirectory();

        setupSelectModeTxt();

        populateCarListFromDatabase();
    }

    private void populateCarListFromDatabase() {
        CarDataSource dataSource = new CarDataSource(this);
        dataSource.open();
        dataSource.updateCar(User.BUS);
        dataSource.updateCar(User.BIKE);
        dataSource.updateCar(User.SKYTRAIN);

        dataSource.close();
        // Check if car list already populated from database
        // This prevents duplicate entries from re-opening this activity
        if (!User.getInstance().isCarListPopulatedFromDatabase()) {
            CarDataSource db = new CarDataSource(this);
            db.open();

            List<Car> cars = db.getAllCars();
            User user = User.getInstance();
            for (Car car : cars) {
                user.addCarToCarList(car);
            }
            User.getInstance().setCarListPopulatedFromDatabase();
        }
    }

    private void setUpBikeButton() {
        Button bikeButton = (Button) findViewById(R.id.addBikeButton);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                //Car bike = new Car();
                //bike.setTransport_mode(Car.WALK_BIKE);
                //bike.setNickname("Bike");
                user.setCurrentJourneyCar(User.BIKE);
                Intent intent = new Intent(TransportationModeActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setUpSkytrainButton() {
        Button SkytrainButton = (Button) findViewById(R.id.addSkytrainButton);
        SkytrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                //Car skytrain = new Car();
                //skytrain.setTransport_mode(Car.SKYTRAIN);
                //skytrain.setNickname("Skytrain");
                user.setCurrentJourneyCar(User.SKYTRAIN);
                Intent intent = new Intent(TransportationModeActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setUpBusButton() {
        Button BusButton = (Button) findViewById(R.id.addBusButton);
        BusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                //Car bus = new Car("Bus", 89, 89, Car.BUS);
                //bus.setTransport_mode(Car.BUS);
                user.setCurrentJourneyCar(User.BUS);
                Intent intent = new Intent(TransportationModeActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        });
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

        List<Vehicle> vehicleArrayList = user.getVehicleList();

        vehicleArrayList.add(new Vehicle("My fav car", "Lamborghini", "Diablo", 1999));
        vehicleArrayList.add(new Vehicle("The fun car", "Porsche", "911", 2017));
        vehicleArrayList.add(new Vehicle("The Ancient One", "Honda", "Civic", 1985));
    }

    private void setupSelectModeTxt() {
        TextView selectTxt = (TextView) findViewById(R.id.selectMode);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Peter.ttf");
        selectTxt.setTypeface(face);
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

    private void setUpCarListView() {
        ArrayAdapter<Vehicle> carListAdapter = new VehicleListAdapter(TransportationModeActivity.this);
        User.getInstance().setVehicleListener((VehicleListener) carListAdapter);
        ListView carList = (ListView) findViewById(R.id.carListView);
        carList.setAdapter(carListAdapter);
    }

    private class VehicleListAdapter extends ArrayAdapter<Vehicle> implements VehicleListener {
        private List<Vehicle> vehicleList;

        VehicleListAdapter(Context context) {
            super(context, R.layout.car_listview_item, User.getInstance().getVehicleList());
            vehicleList = User.getInstance().getVehicleList();
            vehicleListWasEdited();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            View itemView;
            Vehicle car = vehicleList.get(position);
            if (car.getActive()) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.car_listview_item, parent, false);
            } else {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_null, parent, false);
            }

            User user = User.getInstance();
            // Get the current vehicle
            Vehicle vehicle = user.getVehicleList().get(position);
            // Fill the TextView
            TextView description = (TextView) itemView.findViewById(R.id.car_description);
            if(description != null)
                description.setText(vehicle.getShortDecription());

            return itemView;
        }

        @Nullable
        @Override
        public Car getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public void carListWasEdited() {
            Log.i(TAG, "Vehicle List changed, updating listview");
            notifyDataSetChanged();
        }
    }

    private void registerListViewClickCallback() {
        ListView list = (ListView) findViewById(R.id.carListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has selected a vehicle
                Vehicle vehicle = User.getInstance().getVehicleList().get(i);
                Log.i(TAG, "User selected vehicle " + vehicle.getShortDecription());

                // Set current Journey to use the selected vehicle
                User.getInstance().setCurrentJourneyCar(vehicle);

                Intent intent = new Intent(TransportationModeActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has long pressed to edit a vehicle
                Vehicle vehicle = User.getInstance().getVehicleList().get(i);
                Log.i(TAG, "User long pressed on " + vehicle.getShortDecription());

                int index = User.getInstance().getVehicleList().indexOf(vehicle);

                launchNewVehicleDialog(index);

                return true;
            }
        });
    }

    private void launchNewVehicleDialog(){
        FragmentManager manager = getSupportFragmentManager();
        NewVehicleFragment dialog = new NewVehicleFragment();
        dialog.show(manager, "NewVehicleDialog");
    }

    private void launchNewVehicleDialog(int carPosition){
        FragmentManager manager = getSupportFragmentManager();
        NewVehicleFragment dialog = new NewVehicleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("car", carPosition);
        dialog.setArguments(bundle);
        dialog.show(manager, "NewVehicleDialog");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == User.ACTIITY_FINISHED_REQUESTCODE) {
            finish();
        }
    }

}