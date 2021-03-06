package sfu.cmpt276.carbontracker.carbonmodel;
/*Class to hold route data for journeys*/
public class Route {
    private int id = -1;
    private boolean isActive;
    private String routeName;
    private double routeDistanceCity;
    private double routeDistanceHighway;

    public Route() {
        isActive = false;
        routeName = "";
        routeDistanceCity = 0;
        routeDistanceHighway = 0;
    }

    public Route(String name, double distanceC, double distanceH) {
        isActive = false;
        routeName = name;
        routeDistanceCity = distanceC;
        routeDistanceHighway = distanceH;

    }

    //return the name
    public String getRouteName() {
        return routeName;
    }

    //set the name
    public void setRouteName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        routeName = name;
    }

    //return the distance of city
    public double getRouteDistanceCity() {
        return routeDistanceCity;
    }

    //return the distance of highway
    public double getRouteDistanceHighway() {
        return routeDistanceHighway;
    }

    //set the distance of city
    public void setRouteDistanceCity(double distanceC) {
        if (distanceC < 0) {
            throw new IllegalArgumentException();
        }
        routeDistanceCity = distanceC;
    }

    //set the distance of highway
    public void setRouteDistanceHighway(double distanceH) {
        if (distanceH < 0) {
            throw new IllegalArgumentException();

        }
        routeDistanceHighway = distanceH;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}