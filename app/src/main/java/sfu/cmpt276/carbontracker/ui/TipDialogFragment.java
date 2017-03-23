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

/*fragment to show user tips*/
public class TipDialogFragment extends AppCompatDialogFragment {

    User user = User.getInstance();
    TextView tipsCount;
    TextView tipsText;
    int index = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> vehicleTips = Arrays.asList(getResources().getStringArray(R.array.vehicle_tips));
        List<String> utilityTips = Arrays.asList(getResources().getStringArray(R.array.electric_tips));
        User.getInstance().compareEmissions(vehicleTips, utilityTips);
        //create view to show
        View tipView = LayoutInflater.from(getActivity())
                .inflate(R.layout.tips_popup_layout, null);
        tipsCount = (TextView) tipView.findViewById(R.id.tips_tip_count);
        tipsText = (TextView) tipView.findViewById(R.id.tips_tipLocation);
        //create button listeners
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("APP", "clicked the dialog!");
            }
        };
        setTips();
        setTipPage();

        Button back = (Button) tipView.findViewById(R.id.tips_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index>0) {
                    index--;
                    setTipPage();
                    setTips();
                }
            }
        });

        Button forward = (Button) tipView.findViewById(R.id.tips_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index<user.getTips().size() -1) {
                    index++;
                    setTipPage();
                    setTips();
                }
            }
        });

        //build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.some_environmentally_conscious_tips))
                .setView(tipView)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

    private void setTipPage() {
        int i = index;
        i++;
        String tipPage = getString(R.string.d_1_of_d_2);
        String tipPageFormat = String.format(tipPage, i, user.getTips().size());
        tipsCount.setText(tipPageFormat);
    }

    private void setTips() {
        boolean isVehicle = user.vehicleMostEmissions();
        List<String> vehicleTips = Arrays.asList(getResources().getStringArray(R.array.vehicle_tips));
        int vehicleTipCount = vehicleTips.size();
        List<String> utilityTips = Arrays.asList(getResources().getStringArray(R.array.electric_tips));
        int utilityTipCount = utilityTips.size();
        double emissions;
        String tip = user.getTips().get(index);
        String formatTip = "";
            if (!isVehicle) {
                if (index < utilityTipCount) {
                    emissions = user.topUtilityEmissions();
                }else {
                    emissions = user.topUtilityEmissions();
                }
                formatTip = String.format(tip, emissions);
                tipsText.setText(formatTip);
            } else {
                if (index < vehicleTipCount) {
                    emissions = user.topVehicleEmmissions();
                }else {
                    emissions = user.topUtilityEmissions();
                }
                formatTip = String.format(tip, emissions);
                tipsText.setText(formatTip);
            }
        }
    }
