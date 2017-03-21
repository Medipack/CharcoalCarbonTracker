package sfu.cmpt276.carbontracker.ui;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.RouteList;
import sfu.cmpt276.carbontracker.carbonmodel.RouteListener;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Route;

/*Displays know routes, allows for adding, editing, deleting routes*/

public class RouteActivity extends AppCompatActivity {

    private final String TAG = "RouteActivity";
    private int use_position;

    private String nameSaved;
    private double citySaved;
    private double highwaySaved;

    private int route_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        setupAddRoute();

        populateRouteList();

        registerClickCallback();
    }

    private class RouteListAdapter extends ArrayAdapter<Route> implements RouteListener {

        RouteListAdapter(Context context) {
            super(context, R.layout.route_item, User.getInstance().getRouteList().getRoutes());
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            // Ensure we have a view (could have been passed a null)
            View itemView = convertView;
            if(itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.route_item, parent, false);
            }
            User user = User.getInstance();
            // Get the current car
            Route route = user.getRouteList().getRoute(position);

            // Fill the TextView
            TextView description = (TextView) itemView.findViewById(R.id.routeDescription);
            description.setText(route.getRouteName());

            return itemView;
        }

        @Override
        public void routeListWasEdited() {
            Log.i(TAG, "Route List changed, updating listview");
            notifyDataSetChanged();
        }
    }

    private void populateRouteList() {
        ArrayAdapter<Route> routeListAdapter = new RouteListAdapter(RouteActivity.this);
        User.getInstance().setRouteListener((RouteListener) routeListAdapter);
        ListView list = (ListView) findViewById(R.id.routeList);
        list.setAdapter(routeListAdapter);
    }

    private void setupAddRoute() {
        Button addRoute = (Button)findViewById(R.id.addRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog viewDialog = new Dialog(RouteActivity.this);
                viewDialog.setContentView(R.layout.new_route_layout);
                viewDialog.show();

                final EditText routeName = (EditText) viewDialog.findViewById(R.id.routeName);
                final EditText routeCity = (EditText) viewDialog.findViewById(R.id.routeCity);
                final EditText routeHighway = (EditText) viewDialog.findViewById(R.id.routeHighway);

                Button saveButton = (Button) viewDialog.findViewById(R.id.saveRoute);
                Button useButton = (Button) viewDialog.findViewById(R.id.useRoute);
                Button cancelButton = (Button) viewDialog.findViewById(R.id.cancelRoute);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(routeName.length() == 0){
                            Toast.makeText(RouteActivity.this,
                                    "Please enter a name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (routeCity.length() == 0) {
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the city distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(routeHighway.length() == 0){
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the highway distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            nameSaved = routeName.getText().toString();
                            String str_citySaved = routeCity.getText().toString();
                            citySaved = Double.valueOf(str_citySaved);
                            String str_highwaySaved = routeHighway.getText().toString();
                            highwaySaved = Double.valueOf(str_highwaySaved);

                            if (citySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive city distance", Toast.LENGTH_SHORT).show();
                            }
                            else if (highwaySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive highway distance", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Route newRoute = new Route(nameSaved, citySaved, highwaySaved);
                                User.getInstance().getRouteList().addRoute(newRoute);
                                populateRouteList();
                                viewDialog.cancel();
                            }
                        }
                    }

                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewDialog.cancel();
                    }
                });

                useButton.setOnClickListener(new View.OnClickListener() {

                    Route newRoute = new Route();

                    @Override
                    public void onClick(View v) {
                        if(routeName.length() == 0){
                            Toast.makeText(RouteActivity.this,
                                    "Please enter a name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (routeCity.length() == 0) {
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the city distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(routeHighway.length() == 0){
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the highway distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            nameSaved = routeName.getText().toString();
                            String str_citySaved = routeCity.getText().toString();
                            citySaved = Double.valueOf(str_citySaved);
                            String str_highwaySaved = routeHighway.getText().toString();
                            highwaySaved = Double.valueOf(str_highwaySaved);

                            if (citySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive city distance", Toast.LENGTH_SHORT).show();
                            }
                            else if (highwaySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive highway distance", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                newRoute = new Route(nameSaved, citySaved, highwaySaved);
                                User.getInstance().setCurrentJourneyRoute(newRoute);

                                Log.i(TAG, "User selected route \"" + newRoute.getRouteName() + "\"");

                                // Set current Journey to use the selected route
                                User.getInstance().setCurrentJourneyRoute(newRoute);

                                Journey journey = User.getInstance().getCurrentJourney();
                                journey.setTotalDistance(citySaved + highwaySaved);
                                //double emission = journey.calculateCarbonEmission();
                                //journey.setCarbonEmitted(emission);
                                User.getInstance().resetCurrentJourneyEmission();

                                User.getInstance().addJourney(User.getInstance().getCurrentJourney());

                                Intent intent = new Intent(RouteActivity.this, JourneyEmissionActivity.class);
                                startActivityForResult(intent,0);

                                viewDialog.cancel();
                            }
                        }
                    }
                });
            }
        });
    }

    private void registerClickCallback(){
        ListView listRoute = (ListView) findViewById(R.id.routeList);

        listRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                use_position = i;
                // User has selected a route
                Route route = User.getInstance().getRouteList().getRoute(i);

                Log.i(TAG, "User selected route \"" + route.getRouteName() + "\"");

                // Set current Journey to use the selected route
                User.getInstance().setCurrentJourneyRoute(route);
                //Journey journey = User.getInstance().getCurrentJourney();
                //journey.setTotalDistance(citySaved + highwaySaved);
                //journey.setCarbonEmitted();

                Journey journey = User.getInstance().getCurrentJourney();
                journey.setTotalDistance(citySaved + highwaySaved);

                //double test = journey.getTotalDistance();
                //double emission = journey.calculateCarbonEmission();
                //journey.setCarbonEmitted(emission);
                User.getInstance().resetCurrentJourneyEmission();

                User.getInstance().addJourney(User.getInstance().getCurrentJourney());

                Intent intent = new Intent(RouteActivity.this, PickDateActivity.class);
                startActivityForResult(intent,0);
            }
        });

        //edit + delete
        listRoute.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                route_position = position;
                final Dialog editDialog = new Dialog(RouteActivity.this);
                editDialog.setContentView(R.layout.route_edit_delete_layout);
                editDialog.show();

                final EditText editName = (EditText) editDialog.findViewById(R.id.editName);
                final EditText editCity = (EditText) editDialog.findViewById(R.id.editCity);
                final EditText editHighway = (EditText) editDialog.findViewById(R.id.editHighway);

                Button editSave = (Button) editDialog.findViewById(R.id.editSave);
                Button editDelete = (Button) editDialog.findViewById(R.id.editDelete);
                Button editCancel = (Button) editDialog.findViewById(R.id.editCancel);

                Route editRoute = User.getInstance().getRouteList().getRoute(position);
                editName.setText(editRoute.getRouteName());
                editCity.setText(Double.toString(editRoute.getRouteDistanceCity()));
                editHighway.setText(Double.toString(editRoute.getRouteDistanceHighway()));

                //edit route
                editSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editName.length() == 0) {
                            Toast.makeText(RouteActivity.this,
                                    "Please enter a name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (editCity.length() == 0) {
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the city distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (editHighway.length() == 0) {
                            Toast.makeText(RouteActivity.this,
                                    "Please enter the highway distance",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String editNameSaved = editName.getText().toString();
                            String str_editCitySaved = editCity.getText().toString();
                            double editCitySaved = Double.valueOf(str_editCitySaved);
                            String str_editHighwaySaved = editHighway.getText().toString();
                            double editHighwaySaved = Double.valueOf(str_editHighwaySaved);

                            if (editCitySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive city distance", Toast.LENGTH_SHORT).show();
                            }
                            else if (editHighwaySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive highway distance", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Route editRoute = new Route(editNameSaved, editCitySaved, editHighwaySaved);
                                User.getInstance().editRouteFromRouteList(position, editRoute);
                                populateRouteList();
                                editDialog.cancel();
                            }
                        }
                    }
                });

                //delete route: need to hide!!!
                editDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RouteList myRouteList = User.getInstance().getRouteList();
                        //Route hideRoute = myRouteList.getRoute(route_position);
                        User.getInstance().removeRouteFromRouteList(position);
                        populateRouteList();
                        //myRouteList.addRoute(hideRoute);
                        editDialog.cancel();
                    }
                });
                //cancel edit
                editCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.cancel();
                    }
                });
                return true;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == User.ACTIITY_FINISHED_REQUESTCODE) {
            setResult(User.ACTIITY_FINISHED_REQUESTCODE);
            finish();
        }
    }
}

