package sfu.cmpt276.carbontracker.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;


public class TipDialogFragment extends AppCompatDialogFragment {

    User user = User.getInstance();
    int index = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> vehicleTips = Arrays.asList(getResources().getStringArray(R.array.vehicle_tips));
        List<String> utilityTips = Arrays.asList(getResources().getStringArray(R.array.electric_tips));
        User.getInstance().compareEmissions(vehicleTips, utilityTips);
        //create view to show
        View tipView = LayoutInflater.from(getActivity())
                .inflate(R.layout.tips_popup_layout, null);
        //create button listeners
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("APP", "clicked the dialog!");
            }
        };
        setTips(tipView);
        setTipPage(tipView);

        Button back = (Button) tipView.findViewById(R.id.tips_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index>0) {
                    index--;
                    setTipPage(v);
                    setTips(v);
                }
            }
        });

        Button forward = (Button) tipView.findViewById(R.id.tips_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index<user.getTips().size()) {
                    index++;
                    setTipPage(v);
                    setTips(v);
                }
            }
        });

        //build the alert dialog
        return new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.some_environmentally_conscious_tips)).setView(tipView).setPositiveButton(android.R.string.ok, listener).create();
    }

    private void setTipPage(View tipView) {
        int i = index;
        i++;
        TextView tipsCount = (TextView) tipView.findViewById(R.id.tips_tip_count);
        String tipPage = getString(R.string.d_1_of_d_2);
        String tipPageFormat = String.format(tipPage, i, user.getTips().size());
        tipsCount.setText(tipPageFormat);
    }

    private void setTips(View tipView) {
        TextView tipsText = (TextView) tipView.findViewById(R.id.tips_tipLocation);
        boolean isVehicle = user.vehicleMostEmissions();
        String tip = user.getTips().get(index);
        String formatTip = "";
        if (tip.contains("%d")) {
            if (!isVehicle) {
                formatTip = String.format(tip, user.topUtilityEmissions());
                tipsText.setText(formatTip);
            } else {
                formatTip = String.format(tip, user.topVehicleEmmissions());
                tipsText.setText(formatTip);
            }
        }
        tipsText.setText(tip);
    }
}
