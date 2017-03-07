package sfu.cmpt276.carbontracker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Car> carList;
    private RouteList routeList;
    private List<Journey> journeyList;
    private CarDirectory mainDirectory;

    private User(){
        carList = new ArrayList<>();
        routeList = new RouteList();
        journeyList = new ArrayList<Journey>();
    }

    private static User instance = new User();

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

    public void addCarToCarList(Car car){
        carList.add(car);
    }

    public void addRouteToRouteList(Route route){
        routeList.addRoute(route);
    }

    public void  addJourney( Car car, Route route){
        journeyList.add(new Journey(car, route));
    }

    public void setUpDirectory(InputStream input){
        mainDirectory = new CarDirectory(input);
    }
}
