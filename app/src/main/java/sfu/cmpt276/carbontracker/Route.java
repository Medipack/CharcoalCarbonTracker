package sfu.cmpt276.carbontracker;

public class Route {
    private boolean isActive;
    private String routeName;
    private int routeDistanceCity;
    private int routeDistanceHighway;

    public Route() {
        isActive = false;
        routeName = "";
        routeDistanceCity = 0;
        routeDistanceHighway = 0;
    }

    public Route(int distanceC, int distanceH){
        isActive = false;
        routeDistanceCity = distanceC;
        routeDistanceHighway = distanceH;
    }

    public Route(String name, int distanceC, int distanceH){
        isActive = false;
        routeName = name;
        routeDistanceCity = distanceC;
        routeDistanceHighway = distanceH;

    }

    //return the name
    public String getRouteName(){
        return routeName;
    }

    //set the name
    public void setRouteName(String name){
        if(name==null){
            throw new IllegalArgumentException();
        }
        routeName = name;
    }

    //return the distance of city
    public int getRouteDistanceCity(){
        return routeDistanceCity;
    }

    //return the distance of highway
    public int getRouteDistanceHighway(){
        return routeDistanceHighway;
    }

    //set the distance of city
    public void setRouteDistanceCity(int distanceC){
        if(distanceC<0){
            throw new IllegalArgumentException();
        }
        routeDistanceCity = distanceC;
    }

    //set the distance of highway
    public void setRouteDistanceHighway(int distanceH){
        if(distanceH<0){
            throw new IllegalArgumentException();

        }
        routeDistanceHighway = distanceH;
    }
}