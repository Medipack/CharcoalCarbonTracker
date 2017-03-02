package sfu.cmpt276.carbontracker;

/**
 * Created by Medipack on 2017-03-01.
 */

public class Car {
    public static final String Default = "NA";
    private String nickname;
    private String model;
    private String make;
    private int year;
    private double efficiency;

    //Constructor
    public Car(){
        nickname = Default;
        model = Default;
        make = Default;
        year = 0;
        efficiency = 0;
    }

    public Car(String nickname, String model, String make, int year, double efficiency){
        this.nickname = nickname;
        this.model = model;
        this.make = make;
        this.year = year;
        this.efficiency = efficiency;
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

    public double getEfficiency() {
        return efficiency;
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

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}


