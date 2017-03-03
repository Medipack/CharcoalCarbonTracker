package com.example.apple.carbontracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/2/28.
 */

public class routeList {
    private List<route> routes = new ArrayList<>();

    public void addRoute(route newRoute){
        routes.add(newRoute);
    }

    public void editRoute(route editRoute, int index){
        validateIndexWithException(index);
        routes.remove(index);
        routes.add(index, editRoute);
    }

    public int countRoutes(){
        return routes.size();
    }

    public route getRoute(int index){
        validateIndexWithException(index);
        return routes.get(index);
    }

    public String[] getRouteDescription(){
        String[] descriptions = new String[countRoutes()];
        for(int i=0; i<countRoutes();i++){
            route routes = getRoute(i);
            descriptions[i] = routes.getRouteName() + ":" + "City Distance: " + routes.getRouteDistanceCity()
                    + ", " + "Highway Distance: " + routes.getRouteDistanceHighway() + ".";
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if(index<0 || index>=countRoutes()){
            throw new IllegalArgumentException();
        }
    }
}
