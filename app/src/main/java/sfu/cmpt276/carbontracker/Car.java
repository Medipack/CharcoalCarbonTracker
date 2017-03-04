package sfu.cmpt276.carbontracker;

/**
 * Class to hold profile data for cars
 */

public class Car {
    //Constants
    private static final String Default = "NA";
    private static final double MILES_TO_METERS = 1609.34;
    private static final double GAL_TO_LITRES = 3.78541;
    //Attributes
    private Boolean isActive;
    private String nickname;
    private String model;
    private String make;
    private String fuelType;
    private String transmission;
    private int year;
    private double cityCO2;
    private double hwyCO2;
    private double engineDispl;

    //Constructor
    public Car(){
        isActive = false;
        nickname = Default;
        model = Default;
        make = Default;
        transmission = Default;
        year = 0;
        cityCO2 = 0;
        hwyCO2 = 0;
        engineDispl = 0;
    }

    public Car(String nickname, String model, String make, int year, int cityCO2, int hwyCO2, double engineDispl){
        this.nickname = nickname;
        this.model = model;
        this.make = make;
        this.year = year;
        setCityCO2(cityCO2);
        setHwyCO2(hwyCO2);
        setEngineDispl(engineDispl);
    }

    //Getter

    public Boolean checkActive() {
        return isActive;
    }

    public String getNickname() {
        return nickname;
    }
    public String getModel() {
        return model;
    }
    public String getMake() {
        return make;
    }
    public int getYear() {
        return year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public double getCityCO2() {
        return cityCO2;
    }

    public double getHwyCO2() {
        return hwyCO2;
    }

    public double getEngineDispl() {
        return engineDispl;
    }

    //Setter


    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setCityCO2(int cityCO2) {
        double CO2 = toMeters(cityCO2);
        this.cityCO2 = CO2;
    }

    public void setHwyCO2(int hwyCO2) {
        double CO2 = toMeters(hwyCO2);
        this.hwyCO2 = CO2;
    }

    public void setEngineDispl(double engineDispl) {
        double liters = toLitres(engineDispl);
        this.engineDispl = liters;
    }

    //Helper functions
    double toMeters(int miles){
        double meters = miles/MILES_TO_METERS;
        return meters;
    }

    double toLitres(double gallons){
        double litres = gallons/GAL_TO_LITRES;
        return litres;
    }
}


