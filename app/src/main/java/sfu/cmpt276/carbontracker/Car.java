package sfu.cmpt276.carbontracker;

/**
 * Class to hold profile data for cars
 */

public class Car {
    public static final String Default = "NA";
    private String nickname;
    private String model;
    private String make;
    private String fuelType;
    private String transmission;
    private int year;
    private int cityCO2;
    private int hwyCO2;
    private int engineDispl;

    //Constructor
    public Car(){
        nickname = Default;
        model = Default;
        make = Default;
        transmission = Default;
        year = 0;
        cityCO2 = 0;
        hwyCO2 = 0;
        engineDispl = 0;
    }

    public Car(String nickname, String model, String make, int year, int cityCO2, int hwyCO2){
        this.nickname = nickname;
        this.model = model;
        this.make = make;
        this.year = year;
        this.cityCO2 = cityCO2;
        this.hwyCO2 = hwyCO2;
    }

    //Getter
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

    public int getCityCO2() {
        return cityCO2;
    }

    public int getHwyCO2() {
        return hwyCO2;
    }

    public int getEngineDispl() {
        return engineDispl;
    }

    //Setter
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        this.cityCO2 = cityCO2;
    }

    public void setHwyCO2(int hwyCO2) {
        this.hwyCO2 = hwyCO2;
    }

    public void setEngineDispl(int engineDispl) {
        this.engineDispl = engineDispl;
    }
}


