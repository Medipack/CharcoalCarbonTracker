package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold data on montly utility, as per data entered by user based on fortisbc or bc hydro bill
 */

public class Utility {

    public static final double ELECTRICITY = 9000; //amount of CO2 per GWh
    public static final double NATURAL_GAS = 56.1; //amount of CO2 per GJ

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

    Utility()
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

    Utility(Date startDate, Date endDate, double electricUsed, double naturalGasUsed, int numberOfPeople, int daysInPeriod, double averageKWhCurrent, double averageKWhPrevious, double meterReading)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.electricUsed = electricUsed;
        this.naturalGasUsed = naturalGasUsed;
        this.numberOfPeople = numberOfPeople;
        this.daysInPeriod = daysInPeriod;
        this.averageKWhCurrent = averageKWhCurrent;
        this.averageKWhPrevious = averageKWhPrevious;

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
}
