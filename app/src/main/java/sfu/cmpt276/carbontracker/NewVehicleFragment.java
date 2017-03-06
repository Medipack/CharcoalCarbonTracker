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

public class NewVehicleFragment extends AppCompatDialogFragment {

    private final String TAG = "NewVehicleDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Don't allow user to cancel by tapping outside of dialog
        setCancelable(false);

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

        // Build the dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Add New Vehicle")
                .setView(view)
                .setPositiveButton("ADD", addListener)
                .setNegativeButton("CANCEL", cancelListener)
                .create();
    }
}