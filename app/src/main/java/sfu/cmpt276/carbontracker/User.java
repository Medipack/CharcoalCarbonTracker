package sfu.cmpt276.carbontracker;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Car> carList;
    private RouteList routeList;
    //private List<Journey> journeyList;

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


}
