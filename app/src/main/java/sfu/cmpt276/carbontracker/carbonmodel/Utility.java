package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.Date;

/**
 * Class to hold data on montly utility, as per data entered by user based on fortisbc or bc hydro bill
 */

public class Utility {

    public static final double ELECTRICITY = 0.009; //kg of CO2 per KWh
    public static final double NATURAL_GAS = 56.1; //kg of CO2 per GJ
    public static final String ELECTRICITY_NAME = "electricity";
    public static final String GAS_NAME = "gas";

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

    }
    //Utility
    public Utility(String utility_type, Date startDate, Date endDate, double utilityUsed, int numberOfPeople, int daysInPeriod)
    {
        this.utility_type = utility_type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
        this.daysInPeriod = daysInPeriod;

        if(utility_type.equals("electricity"))
        {
            this.electricUsed = utilityUsed;

        }
        else if(utility_type.equals("gas"))
        {
            this.naturalGasUsed = utilityUsed;
        }

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

    public String getUtility_type(){
        return utility_type;
    }
    public void setUtility_type(String utility_type){

        this.utility_type = utility_type;
    }

    //emission per people share
    public double getPeopleShare(){
        if(utility_type.equals(ELECTRICITY_NAME)) {
            return electricUsed / numberOfPeople;
        }
        else{
            return naturalGasUsed / numberOfPeople;
        }
    }

    //emission per person per day
    public double getPerDayUsage(){
        if(utility_type.equals(ELECTRICITY_NAME)){
            return ELECTRICITY  * electricUsed / daysInPeriod / numberOfPeople;   // kg per kwh per day
        }
        else{
            return NATURAL_GAS * naturalGasUsed / daysInPeriod / numberOfPeople;                 //kg per GJ per day
        }
    }
}
