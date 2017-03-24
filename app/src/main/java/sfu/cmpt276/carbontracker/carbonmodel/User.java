package sfu.cmpt276.carbontracker.carbonmodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/*Singleton class holding list of known cars, list of known routes, and list of known journeys*/
public class User {

    public static final int ACTIITY_FINISHED_REQUESTCODE = 1000;

    public static final Vehicle BUS = new Vehicle(0, "Bus", 89, 89, Vehicle.BUS);
    public static final Vehicle BIKE = new Vehicle(1, "Bike", 0, 0, Vehicle.WALK_BIKE);
    public static final Vehicle SKYTRAIN = new Vehicle(2, "Skytrain", 89, 89, Vehicle.SKYTRAIN);

    private VehicleListener vehicleListener;
    private RouteListener routeListener;

    private List<Vehicle> vehicleList;
    private RouteList routeList;
    private List<Journey> journeyList;
    private VehicleDirectory mainDirectory;
    private UtilityList utilityList;
    private Journey currentJourney;
    private List<String> tips;

    private boolean carListPopulatedFromDatabase = false;
    private boolean routeListPopulatedFromDatabase = false;
    private boolean utilityListPopulatedFromDatabase = false;

    private User(){
        vehicleList = new ArrayList<>();
        routeList = new RouteList();
        currentJourney = new Journey();
        journeyList = new ArrayList<Journey>();
        utilityList = new UtilityList();
        tips = new ArrayList<String>();
    }

    private static User instance = new User();

    // *** Getters *** //

    public static User getInstance() {
        return instance;
    }

    public List<Vehicle> getVehicleList(){
        return vehicleList;
    }

    public RouteList getRouteList(){
        return routeList;
    }

    public List<Journey> getJourneyList() {
        return journeyList;
    }

    public VehicleDirectory getMain(){
        return mainDirectory;
    }

    public UtilityList getUtilityList() {
        return utilityList;
    }


    // *** Current Journey *** //

    public void createNewCurrentJourney(){
        currentJourney = new Journey();
    }

    public void setCurrentJourney(Journey currentJourney){
        this.currentJourney = currentJourney;
    }

    public void setCurrentJourneyCar(Vehicle vehicle){
        currentJourney.setVehicle(vehicle);
    }

    public void setCurrentJourneyRoute(Route route){
        currentJourney.setRoute(route);
    }
    public void resetCurrentJourneyEmission(){
        currentJourney.resetCarbonEmitted();
    }

    public Journey getCurrentJourney(){
        return currentJourney;
    }


    public Vehicle getCarFromCarList(int index) {
        return vehicleList.get(index);
    }

    // *** Modify lists *** //

    public void addCarToCarList(Vehicle vehicle){
        vehicleList.add(vehicle);
        notifyListenerCarWasEdited();
    }

    public void editCarFromCarList(int index, Vehicle newVehicle){
        Vehicle oldVehicle = vehicleList.get(index);
        for(Journey journey : journeyList) {
            if(journey.getVehicle() == oldVehicle) {
                journey.setVehicle(newVehicle);
                journey.resetCarbonEmitted();
            }
        }
        vehicleList.set(index, newVehicle);
        notifyListenerCarWasEdited();
    }

    public void removeCarFromCarList(Vehicle car){
        /*
        Vehicle vehicle = vehicleList.get(index);
        for(Journey journey : journeyList){
            if(journey.getVehicle() == vehicle){
                Vehicle newVehicle = new Vehicle();
                try{
                    newVehicle = (Vehicle) vehicle.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                journey.setVehicle(newVehicle);
                journey.resetCarbonEmitted();
            }
        }
        vehicleList.remove(car);
        */
        notifyListenerCarWasEdited();
    }

    public void editRouteFromRouteList(int index, Route newRoute){
        Route oldRoute = routeList.getRoute(index);
        for(Journey journey : journeyList) {
            if(journey.getRoute() == oldRoute) {
                journey.setRoute(newRoute);
                journey.resetCarbonEmitted();
            }
        }
        routeList.editRoute(newRoute, index);
        notifyListenerRouteWasEdited();
    }


    public void addRouteToRouteList(Route route){
        routeList.addRoute(route);
        notifyListenerRouteWasEdited();
    }

    public void removeRouteFromRouteList(int index){
        /*
        Route route = routeList.getRoute(index);
        for(Journey journey : journeyList){
            if(journey.getRoute() == route){
                Route newRoute = new Route();
                try{
                    newRoute = (Route) route.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                journey.setRoute(newRoute);
                journey.resetCarbonEmitted();
            }
        }
        routeList.removeRoute(index);
        */
        notifyListenerRouteWasEdited();
    }

    public void addJourney(Vehicle vehicle, Route route) {
        journeyList.add(new Journey(vehicle, route));
    }

    public void addJourney(Journey journey){
        journeyList.add(journey);
    }

    //edit utility list
    public void EditUtilityIntoUtilityList(int index, Utility newUtility){
        utilityList.editUtility(newUtility, index);
    }

    //get utility date
    public Date UtilityStartDate(int index){
        return utilityList.getUtility(index).getStartDate();
    }

    public Date UtilityEndDate(int index){
        return utilityList.getUtility(index).getEndDate();
    }


    public void addUtilityToUtilityList(Utility utility) {
        utilityList.addUtility(utility);
    }

    public Journey getJourney(int position){
        return journeyList.get(position);
    }

    // *** Directory *** //

    public void setUpDirectory(InputStream input){
        mainDirectory = new VehicleDirectory(input);
    }

    public boolean directoryNotSetup(){
        return mainDirectory == null;
    }

    // *** Event Listeners *** //

    public void setVehicleListener(VehicleListener listener){
        vehicleListener = listener;
    }

    public void setRouteListener(RouteListener listener){
        routeListener = listener;
    }

    private void notifyListenerRouteWasEdited(){
        if(routeListener != null)
            routeListener.routeListWasEdited();
        else
            throw new ExceptionInInitializerError("No one is listening to route list");
    }

    private void notifyListenerCarWasEdited(){
        if(vehicleListener != null)
            vehicleListener.carListWasEdited();
        else
            throw new ExceptionInInitializerError("No one is listening to car list");
    }

    // *** Tips *** //
    public double topVehicleEmmissions(){

        double vehicleEmissions = 0;
        if (!journeyList.isEmpty()) {
            for (int i = 0; i < journeyList.size(); i++) {
                double carbonEmitted = journeyList.get(i).getCarbonEmitted();
                if (carbonEmitted > vehicleEmissions) {
                    vehicleEmissions = carbonEmitted;
                }
            }
        }
        return vehicleEmissions;
    }

    public double topUtilityEmissions(){
        double utilityEmissions = 0;
        for(Utility utility : utilityList.getUtilities()){
            double carbonEmitted = utility.getPerPersonEmission();
            if (carbonEmitted > utilityEmissions) {
                utilityEmissions = carbonEmitted;
            }
        }
        return utilityEmissions;
    }

    //returns true if vehicle has themost emissions, returns false if it's utilities
    public boolean vehicleMostEmissions(){
        boolean vehicle = false;
        double vehicleEmissions = topVehicleEmmissions();
        double utilityEmissions = topUtilityEmissions();
        if(vehicleEmissions>utilityEmissions){
            vehicle = true;
        }else if(utilityEmissions>vehicleEmissions) {
            vehicle = false;
        }
        return vehicle;
    }

    public void resetTips(){
        tips.clear();
    }

    public void compareEmissions(List vehicleTips, List utilityTips){
        if(vehicleMostEmissions()){
            shuffleTips(vehicleTips, utilityTips);
            vehiclesFirst(vehicleTips, utilityTips);
        }else if(!vehicleMostEmissions()){
            shuffleTips(vehicleTips, utilityTips);
            utilitiesFirst(vehicleTips, utilityTips);
        }else{
            shuffleTips(vehicleTips, utilityTips);
            utilitiesFirst(vehicleTips, utilityTips);
        }
    }

    private void utilitiesFirst(List vehicleTips, List utilityTips) {
        tips.addAll(utilityTips);
        tips.addAll(vehicleTips);
    }

    private void vehiclesFirst(List vehicleTips, List utilityTips) {
        tips.addAll(vehicleTips);
        tips.addAll(utilityTips);
    }

    private void shuffleTips(List vehicleTips, List utilityTips) {
        Collections.shuffle(vehicleTips);
        Collections.shuffle(utilityTips);
    }

    public List<String> getTips(){
        return tips;
    }
    // *** Database *** //

    public boolean isCarListPopulatedFromDatabase() {
        return carListPopulatedFromDatabase;
    }

    public void setCarListPopulatedFromDatabase() {
        carListPopulatedFromDatabase = true;
    }

    public boolean isRouteListPopulatedFromDatabase() {
        return routeListPopulatedFromDatabase;
    }

    public void setRouteListPopulatedFromDatabase() {
        routeListPopulatedFromDatabase = true;
    }

    public boolean isUtilityListPopulatedFromDatabase() {
        return utilityListPopulatedFromDatabase;
    }

    public void setUtilityListPopulatedFromDatabase() {
        utilityListPopulatedFromDatabase = true;
    }

    public static int booleanToInt(boolean value) {
        return (value) ? 1 : 0;
    }

    public static boolean intToBoolean(int value) {
        return value > 0;
    }
}
