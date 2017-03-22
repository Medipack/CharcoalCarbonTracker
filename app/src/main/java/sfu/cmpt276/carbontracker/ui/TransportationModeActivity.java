package sfu.cmpt276.carbontracker.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Car;
import sfu.cmpt276.carbontracker.carbonmodel.CarListener;

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
    }

    private void setUpBikeButton() {
        Button bikeButton = (Button) findViewById(R.id.addBikeButton);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                 Car bike = new Car();
                bike.setTransport_mode(Car.WALK_BIKE);
                bike.setNickname("Bike");
                user.setCurrentJourneyCar(bike);
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
                Car skytrain = new Car();
                skytrain.setTransport_mode(Car.SKYTRAIN);
                skytrain.setNickname("Skytrain");
                user.setCurrentJourneyCar(skytrain);
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
                Car bus = new Car("Bus", 89, 89);
                bus.setTransport_mode(Car.BUS);
                user.setCurrentJourneyCar(bus);
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

        List<Car> carArrayList = user.getCarList();

        carArrayList.add(new Car("My fav car", "Lamborghini", "Diablo", 1999));
        carArrayList.add(new Car("The fun car", "Porsche", "911", 2017));
        carArrayList.add(new Car("The Ancient One", "Honda", "Civic", 1985));
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
        ArrayAdapter<Car> carListAdapter = new CarListAdapter(TransportationModeActivity.this);
        User.getInstance().setCarListener((CarListener) carListAdapter);
        ListView carList = (ListView) findViewById(R.id.carListView);
        carList.setAdapter(carListAdapter);
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

    private void registerListViewClickCallback() {
        ListView list = (ListView) findViewById(R.id.carListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has selected a vehicle
                Car car = User.getInstance().getCarList().get(i);
                Log.i(TAG, "User selected vehicle " + car.getShortDecription());

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
                Log.i(TAG, "User long pressed on " + car.getShortDecription());

                launchNewVehicleDialog(i);

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