package sfu.cmpt276.carbontracker.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;

/**
 * a custom array adapter for the purposes of creating a multicolumn Listview
 */
class JourneyListAdapter extends ArrayAdapter<Journey> {
    private JourneyActivity journeyActivity;

    public JourneyListAdapter(JourneyActivity journeyActivity) {
        super(journeyActivity, R.layout.journeys, journeyActivity.JourneyList);
        this.journeyActivity = journeyActivity;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View journeyView = convertView;
        if (journeyView == null) {
            journeyView = journeyActivity.getLayoutInflater().inflate(R.layout.journeys, parent, false);
        }
        //populate the list
        //find the journey
        Journey thisJourney = User.getInstance().getJourneyList().get(position);
        //Initialize TextViews
        TextView date = (TextView) journeyView.findViewById(R.id.viewDate);
        TextView vehicle = (TextView) journeyView.findViewById(R.id.viewVehicle);
        TextView route = (TextView) journeyView.findViewById(R.id.viewRoute);
        TextView distance = (TextView) journeyView.findViewById(R.id.viewDistance);
        TextView emission = (TextView) journeyView.findViewById(R.id.viewEmission);
        //fill the view
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String str_date = sdf.format(thisJourney.getDate());
        date.setText(str_date);
        String nickname = thisJourney.getCar().getNickname();
        vehicle.setText(nickname);
        String routeName = thisJourney.getRoute().getRouteName();
        route.setText(routeName);
        String totalDistance = Double.toString(thisJourney.getTotalDistance());
        distance.setText(totalDistance);
        String totalEmissions = Double.toString(thisJourney.getCarbonEmitted());
        emission.setText(totalEmissions);

        return journeyView;
    }
}
