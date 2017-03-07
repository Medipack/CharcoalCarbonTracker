package sfu.cmpt276.carbontracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewVehicleFragment extends AppCompatDialogFragment {

    private final String TAG = "NewVehicleDialog";
    private Car car;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        car = new Car();
        // Create the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_vehicle, null);

        // Add button listener
        DialogInterface.OnClickListener addListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Add button clicked");
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

        populateSpinner(makeSpinner, getMakeList());

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                car.setMake(parent.getItemAtPosition(position).toString());
                populateSpinner(modelSpinner, getModelList(car.getMake()));
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                car.setModel(parent.getItemAtPosition(position).toString());
                populateSpinner(yearSpinner, getYearList(car.getMake(), car.getModel()));
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // Build the dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Add New Vehicle")
                .setView(view)
                .setPositiveButton("ADD", addListener)
                .setNegativeButton("CANCEL", cancelListener)
                .create();

    }

    private List<String> getMakeList()
    {
        User user = User.getInstance();
        CarDirectory directory = user.getMain();
        List<String> makeList = new ArrayList<>(directory.getMakeKeys());
        Collections.sort(makeList);
        return makeList;
    }

    private List<String> getModelList(String make)
    {
        User user = User.getInstance();
        CarDirectory directory = user.getMain();
        List<String> modelList = new ArrayList<>(directory.getModelKeys(make));
        Collections.sort(modelList);
        return modelList;
    }

    private List<String> getYearList(String make, String model)
    {
        User user = User.getInstance();
        CarDirectory directory = user.getMain();
        List<String> yearList = new ArrayList<>(directory.getYearKeys(make, model));
        Collections.sort(yearList, Collections.reverseOrder());
        return yearList;
    }

    private void populateSpinner(Spinner spinner, List<String> list) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
    }

}