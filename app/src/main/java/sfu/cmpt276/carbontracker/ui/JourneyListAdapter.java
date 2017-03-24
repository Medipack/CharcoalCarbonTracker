package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;

/**
 * A custom array adapter for the purposes of creating a multicolumn Listview
 */
class JourneyListAdapter extends ArrayAdapter<Journey> {
    private List<Journey> journeyList;
    private Context context;

    public JourneyListAdapter(Context context, List<Journey> journeyList) {
        super(context, R.layout.journeys, journeyList);
        this.context = context;
        this.journeyList = journeyList;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View journeyView = convertView;
        if (journeyView == null) {
            journeyView = LayoutInflater.from(context).inflate(R.layout.journeys, parent, false);
        }
        //Find the journey
        Journey thisJourney = journeyList.get(position);
        //Initialize TextViews
        TextView date = (TextView) journeyView.findViewById(R.id.viewDate);
        TextView vehicle = (TextView) journeyView.findViewById(R.id.viewVehicle);
        TextView route = (TextView) journeyView.findViewById(R.id.viewRoute);
        TextView distance = (TextView) journeyView.findViewById(R.id.viewDistance);
        TextView emission = (TextView) journeyView.findViewById(R.id.viewEmission);
        //Fill the view
        //Date
        @SuppressLint
                ("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String str_date = sdf.format(thisJourney.getDate());
        date.setText(str_date);
        //Nickname
        String nickname = thisJourney.getVehicle().getNickname();
        vehicle.setText(nickname);
        //Route
        String routeName = thisJourney.getRoute().getRouteName();
        route.setText(routeName);
        //Distance
        String totalDistance = Double.toString(thisJourney.getTotalDistance());
        distance.setText(totalDistance);
        //Emissions
        String totalEmissions = Double.toString(thisJourney.getCarbonEmitted());
        emission.setText(totalEmissions);

        return journeyView;
    }
}
