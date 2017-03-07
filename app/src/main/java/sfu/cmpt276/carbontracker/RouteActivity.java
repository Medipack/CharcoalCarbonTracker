package sfu.cmpt276.carbontracker;


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



public class RouteActivity extends AppCompatActivity {

    private final String TAG = "RouteActivity";


    private String nameSaved;
    private int citySaved;
    private int highwaySaved;

    private int route_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        setupAddRoute();

        //addTestRoute();
        populateRouteList();

        registerClickCallback();
    }

    private void addTestRoute(){
        User user = User.getInstance();
        user.getRouteList().addRoute(new Route("shopping", 6, 5));
    }

    private class RouteListAdapter extends ArrayAdapter<Route> implements RouteListener{

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
                            //Toast.makeText(RouteActivity.this, "save: " + nameSaved, Toast.LENGTH_SHORT).show();
                            String str_citySaved = routeCity.getText().toString();
                            citySaved = Integer.parseInt(str_citySaved);
                            String str_highwaySaved = routeHighway.getText().toString();
                            highwaySaved = Integer.parseInt(str_highwaySaved);

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
            }
        });
    }

    private void registerClickCallback(){
        ListView listRoute = (ListView) findViewById(R.id.routeList);
        //edit + delete
        listRoute.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
                            int editCitySaved = Integer.parseInt(str_editCitySaved);
                            String str_editHighwaySaved = editHighway.getText().toString();
                            int editHighwaySaved = Integer.parseInt(str_editHighwaySaved);

                            if (editCitySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive city distance", Toast.LENGTH_SHORT).show();
                            }
                            else if (editHighwaySaved == 0) {
                                Toast.makeText(RouteActivity.this, "Please enter an positive highway distance", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Route editRoute = new Route(editNameSaved, editCitySaved, editHighwaySaved);
                                User.getInstance().getRouteList().editRoute(editRoute, route_position);
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
                        Route hideRoute = myRouteList.getRoute(route_position);
                        myRouteList.removeRoute(route_position);
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
}

