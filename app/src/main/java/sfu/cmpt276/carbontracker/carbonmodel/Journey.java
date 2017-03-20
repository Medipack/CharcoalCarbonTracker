package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold Journey data and calculate carbon emission for journey. Journey is a combination of specific car, route, and date.
 */

public class Journey {
    private static final double GASOLINE = 2.34849; //kg of co2 per litre
    private static final double DIESEL = 2.6839881; //kg of co2 per litre
    private static final double ELECTRIC = 0; //kg of co2 per gallon
    private static final double BUS = 0.089; //kg of co2 per KM of travel
    private static final double WALK_BIKE = 0; //kg of co2 per KM of travel
    private static final double SKYTRAIN = 0; //kg of co2 per KM of travel todo: verify skytrain emisisons

    private Car car;
    private Route route;
    private Date date;
    private double totalDistance;
    private double carbonEmitted;

    Journey() {
        car = new Car(); //initializes car as new car with default values
        route = new Route(); //initializes route as a new route with default values
        date = new Date(); //sets date to current date
        totalDistance = calculateTotalDistance();
        carbonEmitted = 0;
    }

    private double calculateTotalDistance() {
        return route.getRouteDistanceCity() + route.getRouteDistanceHighway();
    }

    Journey(Car car, Route route) {
        this.car = car;
        this.route = route;
        date = new Date(); //sets date as current date
        totalDistance = route.getRouteDistanceCity() + route.getRouteDistanceHighway();
        carbonEmitted = calculateCarbonEmission();
    }

    public double calculateCarbonEmission() //returns kg of co2 for journey
    {
        double co2 = 0;
        if(car.getTransport_mode().equals(Car.CAR)) {
            double cityLitres = route.getRouteDistanceCity() / car.getCityCO2(); //cityCO2 is in km per litre, so divide distance by this to get litres used
            double highwayLitres = route.getRouteDistanceHighway() / car.getHwyCO2();
            if (car.getFuelType().contains("Gasoline")) //checks for all gasoline types
                co2 = GASOLINE;
            else if (car.getFuelType().equals("Diesel"))
                co2 = DIESEL;
            else
                co2 = ELECTRIC;
            return Math.round(co2 * cityLitres + co2 * highwayLitres);
        }
        else {
            if (car.getTransport_mode().equals(Car.BUS)) {
                co2 = BUS;
            } else if (car.getTransport_mode().equals(Car.WALK_BIKE)) {
                co2 = WALK_BIKE;
            } else if (car.getTransport_mode().equals(Car.SKYTRAIN)) {
                co2 = SKYTRAIN;
            }
            return co2 * totalDistance;
        }
    }

    public String getRouteName() { //needed to populate table in total Emissions Screen
        return route.getRouteName();
    }

    public String getVehicleName() { //needed to populate table in total emissions screen
        return car.getNickname();
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getCarbonEmitted() {
        return carbonEmitted;
    }

    public void setCarbonEmitted(double carbonEmitted) {
        this.carbonEmitted = carbonEmitted;
    }
    public void resetCarbonEmitted() {
        this.carbonEmitted = calculateCarbonEmission();
    }

    //needed for editing cars/routes=>alters journeys
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Route getRoute() {
        return route;
    }

    //Add route and resets total distance and carbon emitted
    public void setRoute(Route route) {
        this.route = route;
        totalDistance = calculateTotalDistance();
        carbonEmitted = calculateCarbonEmission();
    }

}
