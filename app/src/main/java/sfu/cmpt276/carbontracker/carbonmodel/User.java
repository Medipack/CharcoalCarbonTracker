package sfu.cmpt276.carbontracker.carbonmodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*Singleton class holding list of known cars, list of known routes, and list of known journeys*/
public class User {

    public static final int ACTIITY_FINISHED_REQUESTCODE = 1000;

    private CarListener carListener;
    private RouteListener routeListener;

    private List<Car> carList;
    private RouteList routeList;
    private List<Journey> journeyList;
    private CarDirectory mainDirectory;

    private Journey currentJourney;

    private boolean carListPopulatedFromDatabase = false;

    private User(){
        carList = new ArrayList<>();
        routeList = new RouteList();
        currentJourney = new Journey();
        journeyList = new ArrayList<Journey>();
    }

    private static User instance = new User();

    // *** Getters *** //

    public static User getInstance() {
        return instance;
    }

    public List<Car> getCarList(){
        return carList;
    }

    public RouteList getRouteList(){
        return routeList;
    }

    public List<Journey> getJourneyList() {
        return journeyList;
    }

    public CarDirectory getMain(){
        return mainDirectory;
    }

    // *** Current Journey *** //

    public void createNewCurrentJourney(){
        currentJourney = new Journey();
    }

    public void setCurrentJourneyCar(Car car){
        currentJourney.setCar(car);
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

    public void addCarToCarList(Car car){
        carList.add(car);
        notifyListenerCarWasEdited();
    }

    public void editCarFromCarList(int index, Car newCar){
        Car oldCar = carList.get(index);
        for(Journey journey : journeyList) {
            if(journey.getCar() == oldCar) {
                journey.setCar(newCar);
                journey.resetCarbonEmitted();
            }
        }
        carList.set(index, newCar);
        notifyListenerCarWasEdited();
    }

    public void removeCarFromCarList(int index){
        Car car = carList.get(index);
        for(Journey journey : journeyList){
            if(journey.getCar() == car){
                Car newCar = new Car();
                try{
                    newCar = (Car) car.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                journey.setCar(newCar);
                journey.resetCarbonEmitted();
            }
        }
        carList.remove(index);
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

    public void addJourney(Car car, Route route){
        journeyList.add(new Journey(car, route));
    }

    public void addJourney(Journey journey){
        journeyList.add(journey);
    }

    // *** Directory *** //

    public void setUpDirectory(InputStream input){
        mainDirectory = new CarDirectory(input);
    }

    public boolean directoryNotSetup(){
        return mainDirectory == null;
    }

    // *** Event Listeners *** //

    public void setCarListener(CarListener listener){
        carListener = listener;
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
        if(carListener != null)
            carListener.carListWasEdited();
        else
            throw new ExceptionInInitializerError("No one is listening to car list");
    }

    // *** Database *** //

    public boolean isCarListPopulatedFromDatabase() {
        return carListPopulatedFromDatabase;
    }

    public void setCarListPopulatedFromDatabase() {
        carListPopulatedFromDatabase = true;
    }
}
