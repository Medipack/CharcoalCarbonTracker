package sfu.cmpt276.carbontracker.carbonmodel;

/**
 * Class to hold data on car information, as per data entered by user
 */

import android.content.res.Resources;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.security.AccessController.getContext;

public class Car implements Cloneable, Serializable{
    //Constants
    public static final String CAR = "car";
    public static final String BUS = "bus";
    public static final String SKYTRAIN = "skytrain";
    public static final String WALK_BIKE = "walk/bike";

    private static final String DEFAULT_NICKNAME = "";
    private static final String DEFAULT_DESCRIPTION = "N/A";
    private static final double MILES_TO_KM = 1.60934;
    private static final double GAL_TO_LITRES = 3.78541;

    private static final double MPG_TO_KML = 0.425144;
    //Attributes
    private int id = -1;
    private boolean isActive;
    private String nickname;
    private String transport_mode;
    private String make;
    private String model;
    private String fuelType;
    private String transmission;
    private int year;
    private double cityCO2;
    private double hwyCO2;
    private double engineDispl;

    //Constructor
    public Car(){
        isActive = false;
        nickname = DEFAULT_NICKNAME;
        model = DEFAULT_DESCRIPTION;
        make = DEFAULT_DESCRIPTION;
        transmission = DEFAULT_DESCRIPTION;
        fuelType = DEFAULT_DESCRIPTION;
        year = 0;
        cityCO2 = 0;
        hwyCO2 = 0;
        engineDispl = 0;
        transport_mode = CAR;
    }

    //Constructor for public modes of transportation
    public Car(int id, String nickname, double cityCO2, double hwyCO2, String transport_mode){
        setId(id);
        isActive = false;
        this.nickname = nickname;
        model = DEFAULT_DESCRIPTION;
        make = DEFAULT_DESCRIPTION;
        transmission = DEFAULT_DESCRIPTION;
        fuelType = DEFAULT_DESCRIPTION;
        year = 0;
        this.cityCO2 = cityCO2;
        this.hwyCO2 = hwyCO2;
        engineDispl = 0;
        setTransport_mode(transport_mode);
    }

    public Car(String nickname, String make, String model, int year){
        isActive = false;
        this.nickname = nickname;
        this.make = make;
        this.model = model;
        this.year = year;
        transport_mode = CAR;
    }

    public Car(String nickname, String model, String make, int year, String fuelType, String transmission, int cityCO2, int hwyCO2, double engineDispl){
        isActive = false;
        this.nickname = nickname;
        this.model = model;
        this.make = make;
        this.year = year;
        this.fuelType = fuelType;
        this.transmission = transmission;
        setCityCO2(cityCO2);
        setHwyCO2(hwyCO2);
        setEngineDispl(engineDispl);
        transport_mode = CAR;
    }

    public Car(String model, String make, int year, String fuelType, String transmission, int cityCO2, int hwyCO2, double engineDispl){
        isActive = false;
        this.nickname = DEFAULT_NICKNAME;
        this.model = model;
        this.make = make;
        this.year = year;
        this.fuelType = fuelType;
        this.transmission = transmission;
        setCityCO2(cityCO2);
        setHwyCO2(hwyCO2);
        setEngineDispl(engineDispl);
        transport_mode = CAR;
    }

    //Getter

    public int getId() {
        return id;
    }

    public String getShortDecription(){
        String description = make + " " + model + " (" + year + ")";

        if(nickname == null || nickname.equals(DEFAULT_NICKNAME))
            return description;
        else
            return nickname + ": " + description;
    }

    public String getLongDescription(){
        //todo add transmission type, fuel type, etc
        String description =  getShortDecription() + " " + getTransmissionFuelTypeDispacementDescription();

        if(nickname == null || nickname.equals(DEFAULT_NICKNAME))
            return description;
        else
            return nickname + ": " + description;
    }

    public String getTransmissionFuelTypeDispacementDescription(){
        return fuelType +  " : " + transmission + " (" + new BigDecimal(engineDispl).setScale(2, RoundingMode.HALF_UP).doubleValue() +"L)" ;
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

    public void setId(int id) {
        this.id = id;
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
        double CO2 = toLitres(toKM(cityCO2));
        this.cityCO2 = CO2;
    }

    public void setHwyCO2(int hwyCO2) {
        double CO2 = toLitres(toKM(hwyCO2));
        this.hwyCO2 = CO2;
    }

    public void setEngineDispl(double engineDispl) {
        //double liters = toLitres(engineDispl);
        this.engineDispl = engineDispl;
    }

    //Helper functions
    double toKM(int miles){
        double meters = miles * MILES_TO_KM;
        return meters;
    }

    double toLitres(double gallons){
        double litres = gallons/GAL_TO_LITRES;
        return litres;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTransport_mode() {
        return transport_mode;
    }

    public void setTransport_mode(String transport_mode) {
        this.transport_mode = transport_mode;
    }

    @Override
    public String toString() {
        return getShortDecription();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}


