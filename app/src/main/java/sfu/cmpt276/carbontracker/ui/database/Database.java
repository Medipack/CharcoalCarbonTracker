package sfu.cmpt276.carbontracker.ui.database;

import android.content.Context;

import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Route;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;

/*
    Database singleton class. Used to maintain lists in User class.
    Add/Edit/Delete items in Databases from here.
 */
public class Database {

    protected static final int DATABASE_VERSION = 25; // Increment if any databases changed. Wipes ALL databases

    private static Database instance = new Database();

    private Context context;

    private VehicleDataSource vehicle_db;
    private RouteDataSource route_db;
    private JourneyDataSource journey_db;
    private UtilityDataSource utility_db;

    private boolean vehiclelist_initialized = false;
    private boolean routelist_initialized = false;
    private boolean journeylist_initialized = false;
    private boolean utilitylist_initalized = false;

    private Database() {
        User user = User.getInstance();
    }

    public static Database getDB() {
        return instance;
    }

    public void initializeDatabase(Context context) {
        this.context = context;
        vehicle_db = new VehicleDataSource(context);
        route_db = new RouteDataSource(context);
        journey_db = new JourneyDataSource(context);
        utility_db = new UtilityDataSource(context);

        populateUserLists();
    }

    private void populateUserLists() {
        populateVehicleList(false);
        populateRouteList(false);
        populateJourneyList(false);
        populateUtilityList(false);
    }

    private void populateUtilityList(boolean override) {
        if(!utilitylist_initalized || override) {
            utility_db.open();
            User user = User.getInstance();
            for(Utility utility : utility_db.getAllUtilities())
                user.addUtilityToUtilityList(utility);
            utility_db.close();
            utilitylist_initalized = true;
        }
    }

    private void populateJourneyList(boolean override) {
        if(!journeylist_initialized || override) {
            journey_db.open();
            User user = User.getInstance();
            for(Journey journey : journey_db.getAllJourneys(context))
                user.addJourney(journey);
            journey_db.close();
            journeylist_initialized = true;
        }
    }

    private void populateRouteList(boolean override) {
        if(!routelist_initialized || override) {
            route_db.open();
            User user = User.getInstance();
            for(Route route : route_db.getAllRoutes())
                user.addRouteToRouteList(route);
            route_db.close();
            routelist_initialized = true;
        }
    }

    private void populateVehicleList(boolean override) {
        if(!vehiclelist_initialized || override) {
            addConstantsToVehicleDatabase();
            vehicle_db.open();
            User user = User.getInstance();
            for (Vehicle car : vehicle_db.getAllCars())
                user.addCarToCarList(car);
            vehicle_db.close();
            vehiclelist_initialized = true;
        }
    }


    private void addConstantsToVehicleDatabase() {
        vehicle_db.open();
        if(vehicle_db.getCarById(0) == null) {
            vehicle_db.updateVehicle(User.BUS);
            vehicle_db.updateVehicle(User.BIKE);
            vehicle_db.updateVehicle(User.SKYTRAIN);
        }
        vehicle_db.close();
    }

    // *** Vehicle *** //

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicle_db.open();
        vehicle = vehicle_db.insertCar(vehicle);
        vehicle_db.close();
        User.getInstance().addCarToCarList(vehicle);
        return vehicle;
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicle_db.open();
        vehicle_db.updateVehicle(vehicle);
        vehicle_db.close();

        User.getInstance().clearVehicleList();
        User.getInstance().clearJourneyList();
        populateVehicleList(true);
        populateJourneyList(true);
    }

    // *** Route *** //

    public Route addRoute(Route route) {
        route_db.open();
        route = route_db.insertRoute(route);
        route_db.close();
        User.getInstance().addRouteToRouteList(route);
        return route;
    }

    public void updateRoute(Route route) {
        route_db.open();
        route_db.updateRoute(route);
        route_db.close();

        User.getInstance().clearRouteList();
        User.getInstance().clearJourneyList();
        populateRouteList(true);
        populateJourneyList(true);
    }

    // *** Journey *** //

    public Journey addJourney(Journey journey) {
        journey_db.open();
        journey = journey_db.insertJourney(journey, context);
        journey_db.close();
        User.getInstance().addJourney(journey);
        return journey;
    }

    public void updateJourney(Journey journey) {
        journey_db.open();
        journey_db.updateJourney(journey);
        journey_db.close();

        User.getInstance().clearJourneyList();
        populateJourneyList(true);
    }

    public void deleteJourney(Journey journey) {
        journey_db.open();
        journey_db.deleteJourney(journey);
        journey_db.close();

        User.getInstance().clearJourneyList();
        populateJourneyList(true);
    }

    // *** Utility *** //

    public Utility addUtility(Utility utility) {
        utility_db.open();
        utility = utility_db.insertUtility(utility);
        utility_db.close();
        User.getInstance().addUtilityToUtilityList(utility);
        return utility;
    }

    public void updateUtility(Utility utility) {
        utility_db.open();
        utility_db.updateUtility(utility);
        utility_db.close();

        User.getInstance().clearUtilityList();
        populateUtilityList(true);
    }

    public void deleteUtility(Utility utility) {
        utility_db.open();
        utility_db.deleteUtility(utility);
        utility_db.close();

        User.getInstance().clearUtilityList();
        populateUtilityList(true);
    }
}
