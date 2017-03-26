package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.VehicleDirectory;
import sfu.cmpt276.carbontracker.carbonmodel.VehicleListener;
import sfu.cmpt276.carbontracker.ui.database.Database;

/* Fragment for adding a new vehicle to vehicle list when creating a journey
* */
public class NewVehicleFragment extends AppCompatDialogFragment {

    private final int DEFAULT_EDIT_CAR_POSITION = -1;
    private final String TAG = "NewVehicleDialog";
    private Vehicle vehicle;
    private List<Vehicle> detailedVehicleList;
    private VehicleListener detailedVehicleListener;

    private boolean editing = false;
    private int editCarPosition = DEFAULT_EDIT_CAR_POSITION;

    private DetailedVehicleAdapter detailedCarArrayAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        vehicle = new Vehicle();

        if(getArguments() != null)
            editCarPosition = getArguments().getInt(TransportationModeActivity.EDIT_VEHICLE_REQUEST, DEFAULT_EDIT_CAR_POSITION); // defaults to -1

        if(editCarPosition != DEFAULT_EDIT_CAR_POSITION)
        {
            vehicle = User.getInstance().getVehicleList().get(editCarPosition);
            Log.i(TAG, "Editing vehicle " + vehicle.getShortDecription());
            editing = true;
        }

        // Create the view
        @SuppressLint
                ("InflateParams") final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_vehicle, null);

        detailedVehicleList = new ArrayList<>();

        detailedCarArrayAdapter = new DetailedVehicleAdapter(getActivity());
        detailedVehicleListener = detailedCarArrayAdapter;
        ListView detailedCarListView = (ListView) view.findViewById(R.id.detailedCarList);
        detailedCarListView.setAdapter(detailedCarArrayAdapter);

        detailedCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // User has selected a vehicle
                detailedVehicleList.get(i);
                Log.i(TAG, "User selected vehicle \"" + vehicle.getNickname()
                        + "\" " + vehicle.getMake() + " " + vehicle.getModel());
                detailedCarArrayAdapter.setSelectedIndex(i);
                detailedCarArrayAdapter.notifyDataSetChanged();
            }
        });


        // Add/Save button listener
        DialogInterface.OnClickListener addListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vehicle = detailedCarArrayAdapter.getSelectedCar();
                EditText nickname = (EditText) view.findViewById(R.id.name);
                vehicle.setNickname(String.valueOf(nickname.getText()).trim());

                if(editing) {
                    editExistingCar(editCarPosition, vehicle);

                } else {
                    addNewCar(vehicle);
                }
            }
        };

        // Delete button listener
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hideCar(editCarPosition);
            }
        };

        // Use button listener
        DialogInterface.OnClickListener useListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Use button clicked");
                vehicle = detailedCarArrayAdapter.getSelectedCar();
                EditText nickname = (EditText) view.findViewById(R.id.name);
                vehicle.setNickname(String.valueOf(nickname.getText()).trim());

                // Set current Journey to use the selected car
                useNewCar(vehicle);

                Intent intent = new Intent(getActivity(), RouteActivity.class);
                startActivityForResult(intent, 0);
            }
        };

        // Cancel button listener
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Cancel button clicked");
            }
        };

        final Spinner makeSpinner = (Spinner)view.findViewById(R.id.make);
        final Spinner modelSpinner = (Spinner)view.findViewById(R.id.model);
        final Spinner yearSpinner = (Spinner)view.findViewById(R.id.year);

        populateSpinner(makeSpinner, getMakeList(), vehicle.getMake());

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                vehicle.setMake(parent.getItemAtPosition(position).toString());
                populateSpinner(modelSpinner, getModelList(vehicle.getMake()), String.valueOf(vehicle.getModel()));
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                vehicle.setModel(parent.getItemAtPosition(position).toString());
                populateSpinner(yearSpinner, getYearList(vehicle.getMake(), vehicle.getModel()), String.valueOf(vehicle.getYear()));
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                vehicle.setYear(Integer.parseInt(parent.getItemAtPosition(position).toString()));
                List<Vehicle> vehicleList = getCarList(vehicle.getMake(), vehicle.getModel(), String.valueOf(vehicle.getYear()));
                detailedVehicleList.clear();
                detailedVehicleList.addAll(vehicleList);
                detailedVehicleListener.vehicleListWasEdited();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if(editing){
            // Build the dialog
            final String title;
            if(vehicle.getNickname().equals(new Vehicle().getNickname()))
                title = "Edit Vehicle";
            else {
                title = "Edit \"" + vehicle.getNickname() + "\"";
                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(vehicle.getNickname());
            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setView(view)
                    .setNeutralButton("DELETE", deleteListener)
                    .setPositiveButton("SAVE", addListener)
                    .setNegativeButton("CANCEL", cancelListener)
                    .create();
        } else {
            // Build the dialog
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Add New Vehicle")
                    .setView(view)
                    .setPositiveButton("ADD", addListener)
                    .setNeutralButton("USE", useListener)
                    .setNegativeButton("CANCEL", cancelListener)
                    .create();
        }
    }

    private class DetailedVehicleAdapter extends ArrayAdapter<Vehicle> implements VehicleListener {

        private int selectedIndex = 0;

        DetailedVehicleAdapter(Context context) {
            super(context, R.layout.car_listview_item_dialog, detailedVehicleList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            // Ensure we have a view (could have been passed a null)
            View itemView = convertView;
            if(itemView == null) {
                itemView = LayoutInflater.from(NewVehicleFragment.this.getContext()).inflate(R.layout.car_listview_item_dialog, parent, false);
            }

            // Get the current vehicle
            Vehicle vehicle = detailedVehicleList.get(position);

            // Fill the TextView
            final RadioButton selected = (RadioButton) itemView.findViewById(R.id.selectedRadioButton);
            selected.setText(vehicle.getTransmissionFuelTypeDispacementDescription());

            // Set the radiobutton

            if(position == selectedIndex) {
                selected.setChecked(true);
            }
            else
                selected.setChecked(false);

            return itemView;
        }

        public void setSelectedIndex(int index){
            selectedIndex = index;
        }

        public Vehicle getSelectedCar(){
            return detailedVehicleList.get(selectedIndex);
        }

        @Override
        public void vehicleListWasEdited() {
            Log.i(TAG, "Vehicle List changed, updating listview");
            selectedIndex = 0;
            notifyDataSetChanged();
        }
    }

    @NonNull
    private List<Vehicle> getCarList(String make, String model, String year) {
        String data = make+","+model+","+year;
        return User.getInstance().getMain().carList(data);
    }

    private List<String> getMakeList()
    {
        User user = User.getInstance();
        VehicleDirectory directory = user.getMain();
        List<String> makeList = new ArrayList<>(directory.getMakeKeys());
        Collections.sort(makeList);
        return makeList;
    }

    private List<String> getModelList(String make)
    {
        User user = User.getInstance();
        VehicleDirectory directory = user.getMain();
        List<String> modelList = new ArrayList<>(directory.getModelKeys(make));
        Collections.sort(modelList);
        return modelList;
    }

    private List<String> getYearList(String make, String model)
    {
        User user = User.getInstance();
        VehicleDirectory directory = user.getMain();
        List<String> yearList = new ArrayList<>(directory.getYearKeys(make, model));
        Collections.sort(yearList, Collections.reverseOrder());
        return yearList;
    }

    private void populateSpinner(Spinner spinner, List<String> list, String compareValue) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
        if(compareValue != null)
        {
            int position = adapter.getPosition(compareValue);
            spinner.setSelection(position);
        }
    }

    // *** Database related methods *** //
    // *** Add / Edit / Delete helper functions *** //

    private void hideCar(int editCarPosition) {
        Vehicle car = User.getInstance().getVehicleList().get(editCarPosition);
        Log.i(TAG, "Delete button clicked, hiding " + car);
        car.setActive(false);

        Database.getDB().updateVehicle(car);
    }

    private void editExistingCar(int editCarPosition, Vehicle car) {
        Log.i(TAG, "Save edit button clicked");
        // Pass old car id to the new car
        int oldCarId = User.getInstance().getVehicleList().get(editCarPosition).getId();
        car.setId(oldCarId);
        car.setActive(true);

        Database.getDB().updateVehicle(car);

        User.getInstance().editCarFromCarList(editCarPosition, car);
    }

    private void addNewCar(Vehicle car) {
        Log.i(TAG, "Add button clicked");
        car.setActive(true);

        Database.getDB().addVehicle(car);
    }

    private void useNewCar(Vehicle car) {
        Log.i(TAG, "Use button clicked");
        car.setActive(false);

        car = Database.getDB().addVehicle(car);
        User.getInstance().setCurrentJourneyCar(car);
    }

    // *** end of database related methods *** //
}
