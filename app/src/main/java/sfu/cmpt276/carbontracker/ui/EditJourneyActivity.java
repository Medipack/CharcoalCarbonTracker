package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Route;
import sfu.cmpt276.carbontracker.carbonmodel.RouteList;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.Database;

/* Activity to allow user to edit journey*/
public class EditJourneyActivity extends AppCompatActivity {

    private static final int EDIT_CODE = 1000;
    public static final String TAG = "MyApp";
    public static final String MM_DD_YY = "MM/dd/yy";
    private Calendar calendar = Calendar.getInstance();
    private String nameSaved;
    private double citySaved;
    private double highwaySaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);

        setupSelectModeTxt();
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        setUpCalendar(index);
        setUpRouteSpinner(index);
        setUpAddRouteButton(index);
        setUpCarSpinner(index);
        setUpAddCar();
        setUpChangeModeToCar(index);
        setUpChangeModeToBus(index);
        setUpChangeModeToSkytrain(index);
        setUpChangeModeToBike(index);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FullScreencall();
    }


    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setUpChangeModeToBike(final int index) {
        Button bikeButton = (Button) findViewById(R.id.journey_edit_bikeButton);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJourneyEdits(index, Vehicle.WALK_BIKE);
                Log.i(TAG, "clicked");
                setResult(EDIT_CODE);
                finish();
            }
        });
    }

    private void setUpChangeModeToSkytrain(final int index) {
        Button skytrainButton = (Button) findViewById(R.id.journey_edit_skytrainButton);
        skytrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJourneyEdits(index, Vehicle.SKYTRAIN);
                Log.i(TAG, "clicked");
                setResult(EDIT_CODE);
                finish();
            }
        });
    }

    private void setupSelectModeTxt() {
        TextView selectTxt = (TextView) findViewById(R.id.edit_journey_selectTransportationMode);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Peter.ttf");
        selectTxt.setTypeface(face);
    }

    private void setUpCalendar(final int index) {
        User user = User.getInstance();
        Date journeyDate = user.getJourney(index).getDate();
        calendar.setTime(journeyDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        EditText calendarDate = (EditText) findViewById(R.id.edit_journey_editable_date);
        calendarDate.setShowSoftInputOnFocus(false);
        calendarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditJourneyActivity.this, date, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH)).show();
            }
        });
        updateDate();
    }

    private void setUpChangeModeToBus(final int index) {
        Button busButton = (Button) findViewById(R.id.journey_edit_busButton);
        busButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJourneyEdits(index, Vehicle.BUS);
                Log.i(TAG, "clicked");
                setResult(EDIT_CODE);
                finish();
            }
        });
    }

    private void updateDate(){
        EditText calendarDate = (EditText) findViewById(R.id.edit_journey_editable_date);
        String calendarFormat = MM_DD_YY;
        SimpleDateFormat sdf = new SimpleDateFormat(calendarFormat);
        String journeyDate_str = sdf.format(calendar.getTime());
        calendarDate.setText(journeyDate_str);
    }

    private void setUpRouteSpinner(int index) {
        //Create a String list
        RouteList routeList = User.getInstance().getRouteList();
        List<String> list = getRouteNames(routeList);
        Spinner routeSpin = (Spinner) findViewById(R.id.routeList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        routeSpin.setAdapter(adapter);
        if (index >= 0) {
            Route originalRoute = User.getInstance().getJourney(index).getRoute();
            int routeIndex = routeList.getRoutes().indexOf(originalRoute);
            routeSpin.setSelection(routeIndex);
        }
    }

    private void setUpAddRouteButton(final int index) {
        Button addRoute = (Button) findViewById(R.id.journey_edit_addRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog viewDialog = new Dialog(EditJourneyActivity.this);
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
                        if (routeName.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.nameError,
                                    Toast.LENGTH_SHORT).show();
                        } else if (routeCity.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.cityDistanceError,
                                    Toast.LENGTH_SHORT).show();
                        } else if (routeHighway.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.hwyDistanceError,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            nameSaved = routeName.getText().toString();
                            String str_citySaved = routeCity.getText().toString();
                            citySaved = Double.valueOf(str_citySaved);
                            String str_highwaySaved = routeHighway.getText().toString();
                            highwaySaved = Double.valueOf(str_highwaySaved);

                            if (citySaved == 0) {
                                Toast.makeText(EditJourneyActivity.this, R.string.positiveCityDistance, Toast.LENGTH_SHORT).show();
                            } else if (highwaySaved == 0) {
                                Toast.makeText(EditJourneyActivity.this, R.string.positiveHwyDistance, Toast.LENGTH_SHORT).show();
                            } else {
                                Route newRoute = new Route(nameSaved, citySaved, highwaySaved);
                                Database.getDB().addRoute(newRoute);
                                setUpRouteSpinner(index);
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
                        if (routeName.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.nameError,
                                    Toast.LENGTH_SHORT).show();
                        } else if (routeCity.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.cityDistanceError,
                                    Toast.LENGTH_SHORT).show();
                        } else if (routeHighway.length() == 0) {
                            Toast.makeText(EditJourneyActivity.this,
                                    R.string.hwyDistanceError,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            nameSaved = routeName.getText().toString();
                            String str_citySaved = routeCity.getText().toString();
                            citySaved = Double.valueOf(str_citySaved);
                            String str_highwaySaved = routeHighway.getText().toString();
                            highwaySaved = Double.valueOf(str_highwaySaved);

                            if (citySaved == 0) {
                                Toast.makeText(EditJourneyActivity.this,  R.string.positiveCityDistance, Toast.LENGTH_SHORT).show();
                            } else if (highwaySaved == 0) {
                                Toast.makeText(EditJourneyActivity.this, R.string.positiveHwyDistance, Toast.LENGTH_SHORT).show();
                            } else {
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

                                Database.getDB().addJourney(User.getInstance().getCurrentJourney());

                                Intent intent = new Intent(EditJourneyActivity.this, JourneyEmissionActivity.class);
                                startActivityForResult(intent, 0);

                                viewDialog.cancel();
                            }
                        }
                    }
                });
            }
        });
    }

    private void setUpCarSpinner(int index) {
        //Create a String list
        List<Vehicle> vehicleList = User.getInstance().getVehicleList();
        List<String> list = getCarNames(vehicleList);
        Spinner routeSpin = (Spinner) findViewById(R.id.edit_journey_car_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.notifyDataSetChanged();
        routeSpin.setAdapter(adapter);
        if (index >= 0) {
            Vehicle originalCar = User.getInstance().getJourney(index).getVehicle();
            int routeIndex = vehicleList.indexOf(originalCar);
            routeSpin.setSelection(routeIndex);
        }
    }

    private void setUpAddCar() {
        Button editVehicle = (Button) findViewById(R.id.edit_journey_addNewCar);
        editVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewVehicleDialog();
                setUpCarSpinner(-1);
            }
        });
    }

    private void setUpChangeModeToCar(final int index) {
        Button changeVehicle = (Button) findViewById(R.id.journey_edit_carButton);
        changeVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJourneyEdits(index, Vehicle.CAR);
                Log.i(TAG, "clicked");
                setResult(EDIT_CODE);
                finish();
            }
        });
    }

    private void setJourneyEdits(int index, String transportationMode) {
        User user = User.getInstance();
        Date selectedDate = getSelectedDate();
        Route selectedRoute = getSelectedRoute();
        Journey editedJourney = user.getJourneyList().get(index);
        if (transportationMode.equals(Vehicle.CAR)){
            Vehicle selectedVehicle = getCarSelection();
            editedJourney.setVehicle(selectedVehicle);
        }else {
            Vehicle vehicle = user.getVehicleList().get(index);
            if (transportationMode == Vehicle.BUS) {
                vehicle = User.BUS;
            } else if (transportationMode == Vehicle.SKYTRAIN) {
                vehicle = User.SKYTRAIN;
            } else if (transportationMode == Vehicle.WALK_BIKE) {
                vehicle = User.BIKE;
            }
            editedJourney.setVehicle(vehicle);
        }
        editedJourney.setCarbonEmitted(editedJourney.calculateCarbonEmission());
        editedJourney.setDate(selectedDate);
        editedJourney.setRoute(selectedRoute);

        Database.getDB().updateJourney(editedJourney);
    }

    private Date getSelectedDate() {
        return calendar.getTime();
    }

    private Vehicle getCarSelection() {
        User user = User.getInstance();
        Spinner carSpin = (Spinner) findViewById(R.id.edit_journey_car_spinner);
        List<Vehicle> vehicleList = user.getVehicleList();
        return vehicleList.get(carSpin.getSelectedItemPosition());
    }

    private Route getSelectedRoute() {
        User user = User.getInstance();
        Spinner routeSpin = (Spinner) findViewById(R.id.routeList);
        RouteList routeList = user.getRouteList();
        return routeList.getRoute(routeSpin.getSelectedItemPosition());
    }

    @NonNull
    private List<String> getRouteNames(RouteList routeList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < routeList.countRoutes(); i++) {
            list.add(i, routeList.getRoute(i).getRouteName());
        }
        return list;
    }

    @NonNull
    private List<String> getCarNames(List<Vehicle> vehicles) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            list.add(i, vehicles.get(i).getNickname());
        }
        return list;
    }

    private void launchNewVehicleDialog(){
        FragmentManager manager = getSupportFragmentManager();
        NewVehicleFragment dialog = new NewVehicleFragment();
        dialog.show(manager, "NewVehicleDialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}