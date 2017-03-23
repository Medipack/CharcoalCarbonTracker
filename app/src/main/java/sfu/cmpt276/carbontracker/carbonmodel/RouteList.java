package sfu.cmpt276.carbontracker.carbonmodel;

/**
 * Class to hold data on route list, as per data entered by user
 */

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Route;


public class RouteList {
    private List<Route> routes = new ArrayList<>();

    public List<Route> getRoutes(){
        return routes;
    }

    public void addRoute(Route newRoute){
        routes.add(newRoute);
    }

    public void editRoute(Route editRoute, int index){
        validateIndexWithException(index);
        routes.remove(index);
        routes.add(index, editRoute);
    }
    public void removeRoute(int index){
        routes.remove(index);
    }

    public int countRoutes(){
        return routes.size();
    }


    public Route getRoute(int index){
        validateIndexWithException(index);
        return routes.get(index);
    }

    public String[] getRouteDescription(){
        String[] descriptions = new String[countRoutes()];
        for(int i=0; i<countRoutes();i++){
            Route routes = getRoute(i);
            descriptions[i] = "Name: " + routes.getRouteName() + "\n" + "City Distance: " + routes.getRouteDistanceCity()
                    + "\n" + "Highway Distance: " + routes.getRouteDistanceHighway();
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if(index<0 || index>=countRoutes()){
            throw new IllegalArgumentException();

        }
    }
}
