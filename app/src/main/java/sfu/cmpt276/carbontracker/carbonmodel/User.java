package sfu.cmpt276.carbontracker.carbonmodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*Singleton class holding list of known cars, list of known routes, and list of known journeys*/
public class User {

    public static final int ACTIITY_FINISHED_REQUESTCODE = 1000;

    private VehicleListener vehicleListener;
    private RouteListener routeListener;

    private List<Vehicle> vehicleList;
    private RouteList routeList;
    private List<Journey> journeyList;
    private VehicleDirectory mainDirectory;
    private UtilityList utilityList;
    private Journey currentJourney;

    private User(){
        vehicleList = new ArrayList<>();
        routeList = new RouteList();
        currentJourney = new Journey();
        journeyList = new ArrayList<Journey>();
        utilityList = new UtilityList();
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

    public void removeCarFromCarList(int index){
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
        vehicleList.remove(index);
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
        notifyListenerRouteWasEdited();
    }

    public void addJourney(Vehicle vehicle, Route route){
        journeyList.add(new Journey(vehicle, route));
    }

    public void addJourney(Journey journey){
        journeyList.add(journey);
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


}
