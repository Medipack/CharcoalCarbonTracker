package sfu.cmpt276.carbontracker.carbonmodel;

public class unitConversion {

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
    public unitConversion(String name,
                          double electricity,
                          double natGas,
                          double gas,
                          double diesel,
                          double elecFuel,
                          double busFare,
                          double skyTrainFare,
                          double walk){
        unitName = name;
        electricityRate = electricity;
        naturalGasRate = natGas;
        gasolineRate = gas;
        dieselRate = diesel;
        electricFuelRate = elecFuel;
        busRate = busFare;
        skytrainRate = skyTrainFare;
        walkBikeRate = walk;
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
