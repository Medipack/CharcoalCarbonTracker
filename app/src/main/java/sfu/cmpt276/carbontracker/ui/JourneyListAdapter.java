package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;

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

    @NonNull
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String str_date = sdf.format(thisJourney.getDate());
        date.setText(str_date);
        //Nickname

        //check transportation mode
        Vehicle thisVehicle = thisJourney.getVehicle();
        String transportationMode =thisJourney.getVehicle().getTransport_mode();
        if (transportationMode == Vehicle.BUS) {
            thisVehicle.setNickname(context.getString(R.string.bus));
        } else if (transportationMode == Vehicle.SKYTRAIN) {
            thisVehicle.setNickname(context.getString(R.string.skytrain));
        } else if (transportationMode == Vehicle.WALK_BIKE) {
            thisVehicle.setNickname(context.getString(R.string.walk_bike));
        }


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

        ImageView iconImg = (ImageView) journeyView.findViewById(R.id.car_icon);
        int iconID =  thisJourney.getVehicle().getIconID();
        TypedArray icons = context.getResources().obtainTypedArray(R.array.iconArray);
        if(iconID > -1 && iconImg != null)
            iconImg.setImageDrawable(icons.getDrawable(iconID));

        return journeyView;
    }
}
