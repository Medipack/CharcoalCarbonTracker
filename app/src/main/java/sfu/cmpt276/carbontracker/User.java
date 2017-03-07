package sfu.cmpt276.carbontracker;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Car> carList;
    private RouteList routeList;
    private List<Journey> journeyList;

    private User(){
        carList = new ArrayList<>();
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

    public void addCarToCarList(Car car){
        carList.add(car);
    }

    public void addRouteToRouteList(Route route){
        routeList.addRoute(route);
    }

    public void  addJourney( Car car, Route route){
        journeyList.add(new Journey(car, route));
    }

}