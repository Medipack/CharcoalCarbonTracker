package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.List;

public class unitConversion {

    //Set Index
    public static final int SET_ELECTRICITY = 0;
    public static final int SET_NATURAL_GAS = 1;
    public static final int SET_GASOLINE = 2;
    public static final int SET_DIESEL = 3;
    public static final int SET_ELECTRIC_C = 4;
    public static final int SET_BUSFARE = 5;
    public static final int SET_SKYTRAIN = 6;
    public static final int SET_WALK = 7;

    //Attributes
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
        unitName= "";
        electricFuelRate = 0;
        naturalGasRate = 0;
        gasolineRate = 0;
        dieselRate = 0;
        electricFuelRate = 0;
        busRate = 0;
        skytrainRate = 0;
        walkBikeRate = 0;
    }


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

    //Copy Constructor
    public unitConversion(unitConversion ref) {
        unitName = ref.getUnitName();
        electricityRate = ref.getElectricityRate();
        naturalGasRate = ref.getNaturalGasRate();
        gasolineRate = ref.getGasolineRate();
        dieselRate = ref.getDieselRate();
        electricFuelRate = ref.getElectricFuelRate();
        busRate = ref.getBusRate();
        skytrainRate = ref.getSkytrainRate();
        walkBikeRate = ref.getWalkBikeRate();
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

    public void setValues(String name, List<Double> unitList) {
        //set the values
        setUnitName(name);
        setElectricityRate(unitList.get(SET_ELECTRICITY));
        setNaturalGasRate(unitList.get(SET_NATURAL_GAS));
        setGasolineRate(unitList.get(SET_GASOLINE));
        setDieselRate(unitList.get(SET_DIESEL));
        setElectricFuelRate(unitList.get(SET_ELECTRIC_C));
        setBusRate(unitList.get(SET_BUSFARE));
        setSkytrainRate(unitList.get(SET_SKYTRAIN));
        setWalkBikeRate(unitList.get(SET_WALK));
    }
}
