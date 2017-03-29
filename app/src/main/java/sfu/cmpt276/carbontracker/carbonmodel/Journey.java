package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold Journey data and calculate carbon emission for journey. Journey is a combination of specific vehicle, route, and date.
 */

public class Journey {
//    private static final double GASOLINE = 2.34849; //kg of co2 per litre
//    private static final double DIESEL = 2.6839881; //kg of co2 per litre
//    private static final double ELECTRIC = 0; //kg of co2 per gallon
//    private static final double BUS = 0.089; //kg of co2 per KM of travel
//    private static final double WALK_BIKE = 0; //kg of co2 per KM of travel
//    private static final double SKYTRAIN = 0; //kg of co2 per KM of travel todo: verify skytrain emisisons

    private int id = -1;
    private Vehicle vehicle;
    private Route route;
    private Date date;
    private double totalDistance;
    private double carbonEmitted;

    public Journey() {
        vehicle = new Vehicle(); //initializes vehicle as new vehicle with default values
        route = new Route(); //initializes route as a new route with default values
        date = new Date(); //sets date to current date
        totalDistance = calculateTotalDistance();
        carbonEmitted = 0;
    }

    private double calculateTotalDistance() {
        return route.getRouteDistanceCity() + route.getRouteDistanceHighway();
    }

    Journey(Vehicle vehicle, Route route) {
        this.vehicle = vehicle;
        this.route = route;
        date = new Date(); //sets date as current date
        totalDistance = route.getRouteDistanceCity() + route.getRouteDistanceHighway();
        carbonEmitted = calculateCarbonEmission();
    }

    public double calculateCarbonEmission() //returns kg of co2 for journey
    {
        User user = User.getInstance();
        unitConversion unit = user.getUnits();
        double co2 = 0;
        if(vehicle.getTransport_mode().equals(Vehicle.CAR)) {
            double cityLitres = route.getRouteDistanceCity() / vehicle.getCityCO2(); //cityCO2 is in km per litre, so divide distance by this to get litres used
            double highwayLitres = route.getRouteDistanceHighway() / vehicle.getHwyCO2();
            if (vehicle.getFuelType().contains("Gasoline")) //checks for all gasoline types
                co2 = unit.getGasolineRate();
            else if (vehicle.getFuelType().equals("Diesel"))
                co2 = unit.getDieselRate();
            else
                co2 = unit.getElectricFuelRate();
            return Math.round(co2 * cityLitres + co2 * highwayLitres);
        }
        else {
            switch (vehicle.getTransport_mode()) {
                case Vehicle.BUS:
                    co2 = unit.getBusRate();
                    break;
                case Vehicle.WALK_BIKE:
                    co2 = unit.getWalkBikeRate();
                    break;
                case Vehicle.SKYTRAIN:
                    co2 = unit.getSkytrainRate();
                    break;
            }
            return co2 * totalDistance;
        }
    }

    public String getRouteName() { //needed to populate table in total Emissions Screen
        return route.getRouteName();
    }

    public String getVehicleName() { //needed to populate table in total emissions screen
        return vehicle.getNickname();
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
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
