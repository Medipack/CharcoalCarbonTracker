package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold data on montly utility, as per data entered by user based on fortisbc or bc hydro bill
 */

public class Utility {

    public static final double ELECTRICITY = 9000; //amount of CO2 per GWh
    public static final double NATURAL_GAS = 56.1; //amount of CO2 per GJ

    private String utility_type;
    private Date startDate;
    private Date endDate;


    private double electricUsed;     //in kWh
    private double naturalGasUsed;  // in GJ
    private int numberOfPeople;     //number of people in household
    private int daysInPeriod;       //number of days in period based on start and end date

    private double averageKWhCurrent;  //average daily electricity used current
    private double averageKWhPrevious; //average daily electricity used previous year (same billing period)
    private double averageGJCurrent;   //average daily gas used current
    private double averageGJPrevious;  //average daily electricity used previous year (same billing period)

    public Utility()
    {
        startDate = new Date();
        endDate = new Date();
        electricUsed = 0;
        naturalGasUsed = 0;
        numberOfPeople = 1;
        daysInPeriod = 0;
        averageKWhCurrent = 0;
        averageKWhPrevious = 0;
    }

    public Utility(String utility_type, Date startDate, Date endDate, double utilityUsed, int numberOfPeople, int daysInPeriod, double averageCurrent, double averagePrevious)
    {
        this.utility_type = utility_type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
        this.daysInPeriod = daysInPeriod;

        if(utility_type.equals("electricity"))
        {
            this.electricUsed = utilityUsed;
            this.averageKWhCurrent = averageCurrent;
            this.averageKWhPrevious = averagePrevious;
        }
        else if(utility_type.equals("gas"))
        {
            this.naturalGasUsed = utilityUsed;
            this.averageGJCurrent = averageCurrent;
            this.averageGJPrevious = averagePrevious;
        }

    }

    public Utility(String utility_type, double utilityUsed, int numberOfPeople, int daysInPeriod, double averageCurrent, double averagePrevious){
        this.utility_type = utility_type;
        this.numberOfPeople = numberOfPeople;
        this.daysInPeriod = daysInPeriod;
        this.naturalGasUsed = utilityUsed;
        this.averageKWhCurrent = averageCurrent;
        this.averageKWhPrevious = averagePrevious;

    }



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getElectricUsed() {
        return electricUsed;
    }

    public void setElectricUsed(double electricUsed) {
        this.electricUsed = electricUsed;
    }

    public double getNaturalGasUsed() {
        return naturalGasUsed;
    }

    public void setNaturalGasUsed(double naturalGasUsed) {
        this.naturalGasUsed = naturalGasUsed;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getDaysInPeriod() {
        return daysInPeriod;
    }

    public void setDaysInPeriod(int daysInPeriod) {
        this.daysInPeriod = daysInPeriod;
    }

    public double getAverageKWhCurrent() {
        return averageKWhCurrent;
    }

    public void setAverageKWhCurrent(double averageKWhCurrent) {
        this.averageKWhCurrent = averageKWhCurrent;
    }

    public double getAverageKWhPrevious() {
        return averageKWhPrevious;
    }

    public void setAverageKWhPrevious(double averageKWhPrevious) {
        this.averageKWhPrevious = averageKWhPrevious;
    }

    public double getAverageGJCurrent() {
        return averageGJCurrent;
    }

    public void setAverageGJCurrent(double averageGJCurrent) {
        this.averageGJCurrent = averageGJCurrent;
    }

    public double getAverageGJPrevious() {
        return averageGJPrevious;
    }

    public void setAverageGJPrevious(double averageGJPrevious) {
        this.averageGJPrevious = averageGJPrevious;
    }

    public String getUtility_type(){
        return utility_type;
    }

    public double getPerDayUsed(){
        if(utility_type.equals("electricity")) {
            return electricUsed / daysInPeriod;
        }
        else{
            return naturalGasUsed / daysInPeriod;
        }
    }

    public double getPeopleShare(){
        if(utility_type.equals("electricity")) {
            return electricUsed / numberOfPeople;
        }
        else{
            return naturalGasUsed / numberOfPeople;
        }
    }

    public double getPerPersonEmission(){
        if(utility_type.equals("electricity")){
            return 9000 * 1000 * (averageKWhCurrent - averageKWhPrevious)/1000000 / numberOfPeople;   // g per gwh per person
        }
        else{
            return 56.1 * (averageGJCurrent - averageGJPrevious) / numberOfPeople;                 //g per GJ per person
        }
    }

    public double getPerDayUsage(){
        if(utility_type.equals("electricity")){
            return 9000 * 1000 * (averageKWhCurrent - averageKWhPrevious)/1000000 / daysInPeriod;   // g per gwh per day
        }
        else{
            return 56.1 * (averageGJCurrent - averageGJPrevious) / daysInPeriod;                 //g per GJ per day
        }
    }
}
