package sfu.cmpt276.carbontracker.carbonmodel;

public class unitConversion {

    protected static final double ELECTRICITY_CO2 = 0.009; //kg of CO2 per KWh
    protected static final double NATURAL_GAS_CO2 = 56.1; //kg of CO2 per GJ
    protected static final double GASOLINE_CO2 = 2.34849; //kg of co2 per litre
    protected static final double DIESEL_CO2 = 2.6839881; //kg of co2 per litre
    protected static final double ELECTRIC_FUEL_CO2 = 0; //kg of co2 per gallon
    protected static final double BUS_CO2 = 0.089; //kg of co2 per KM of travel
    protected static final double WALK_BIKE_CO2 = 0; //kg of co2 per KM of travel
    protected static final double SKYTRAIN_CO2 = 0; //kg of co2 per KM of travel todo: verify skytrain emisisons

    private String unitName;
    private double electricityRate;
    private double naturalGasRate;
    private double gasolineRate;
    private double dieselRate;
    private double electricFuelRate;
    private double busRate;
    private double skytrainRate;
    private double walkBikeRate;

    //Default constructor
    public unitConversion(){
        unitName = "CO2";
        electricityRate = ELECTRICITY_CO2;
        naturalGasRate = NATURAL_GAS_CO2;
        gasolineRate = GASOLINE_CO2;
        dieselRate = DIESEL_CO2;
        electricityRate = ELECTRIC_FUEL_CO2;
        busRate = BUS_CO2;
        skytrainRate = SKYTRAIN_CO2;
        walkBikeRate = WALK_BIKE_CO2;
    }

    //***Getters***

    public String getUnitName() {
        return unitName;
    }

    public double getElectricityRate() {
        return electricityRate;
    }

    public double getNaturalGasRate() {
        return naturalGasRate;
    }

    public double getGasolineRate() {
        return gasolineRate;
    }

    public double getDieselRate() {
        return dieselRate;
    }

    public double getElectricFuelRate() {
        return electricFuelRate;
    }

    public double getBusRate() {
        return busRate;
    }

    public double getSkytrainRate() {
        return skytrainRate;
    }

    public double getWalkBikeRate() {
        return walkBikeRate;
    }

    //***Setters***

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setElectricityRate(double electricityRate) {
        this.electricityRate = electricityRate;
    }

    public void setNaturalGasRate(double naturalGasRate) {
        this.naturalGasRate = naturalGasRate;
    }

    public void setGasolineRate(double gasolineRate) {
        this.gasolineRate = gasolineRate;
    }

    public void setDieselRate(double dieselRate) {
        this.dieselRate = dieselRate;
    }

    public void setElectricFuelRate(double electricFuelRate) {
        this.electricFuelRate = electricFuelRate;
    }

    public void setBusRate(double busRate) {
        this.busRate = busRate;
    }

    public void setSkytrainRate(double skytrainRate) {
        this.skytrainRate = skytrainRate;
    }

    public void setWalkBikeRate(double walkBikeRate) {
        this.walkBikeRate = walkBikeRate;
    }
}
