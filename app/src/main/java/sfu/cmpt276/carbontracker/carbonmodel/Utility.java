package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold data on montly utility, as per data entered by user based on fortisbc or bc hydro bill
 */

public class Utility {
    private Date startDate;
    private Date endDate;
    private double electricUsed;
    private double naturalGasUsed;
    private int numberOfPeople;
    private int daysInPeriod;
    private double averageKWhCurrent;
    private double averageKWhPrevious;
    private double meterReading;

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
        meterReading = 0;
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
        this.meterReading = meterReading;

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

    public double getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(double meterReading) {
        this.meterReading = meterReading;
    }
}
