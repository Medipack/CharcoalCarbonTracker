package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by apple on 2017-03-17.
 */

public class UtilityList {
    private List<Utility> utilities = new ArrayList<>();

    public List<Utility> getUtilities(){
        return utilities;
    }

    public void addUtility(Utility utility){
        utilities.add(utility);
    }

    //edit the utility
    public void editUtility(Utility utility, int index){
        validateIndexWithException(index);

        utilities.remove(index);
        utilities.add(index, utility);
    }
    //remove utility
    public void removeUtility(int index){
        utilities.remove(index);
    }
    //count size of the utility
    public int countUtility(){
        return utilities.size();
    }
    //get utility in the utility list
    public Utility getUtility(int index){
        validateIndexWithException(index);
        return utilities.get(index);
    }
    //utility information in the list
    public String[] getUtilityDescription(){
        String[] descriptions = new String[countUtility()];
        for(int i=0; i<countUtility();i++) {
            Utility utility = getUtility(i);
            if (utility.getUtility_type().equals("gas")) {
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n" + utility.getNaturalGasUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "current average is: " + utility.getAverageGJCurrent() + "GJ, previous average is: " + utility.getAverageGJPrevious()
                        +  "GJ\npeople in home share: " + utility.getPeopleShare() + "\nemission per person: " + utility.getPerPersonEmission()
                        + "g\nemission per day: " + utility.getPerDayUsage() + "g";
            }

            else{
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n" + utility.getElectricUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "current average is: " + utility.getAverageKWhCurrent() + "kwh, previous average is: " + utility.getAverageKWhPrevious()
                        +  "kwh\npeople in home share: " + utility.getPeopleShare() + "\nemission per person: " + utility.getPerPersonEmission()
                        + "g\nemission per day: " + utility.getPerDayUsage() + "g";
            }
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if(index<0 || index>=countUtility()){
            throw new IllegalArgumentException();

        }
    }

//Graph helper methods//
/*


    public int getDaysOfUtilityInPeriod(int daysInPeriod)
    {
        Date periodStartDate = getPeriodStartDate(daysInPeriod); //period start is current date - daysinPeriod, e.g. today - 28 days
        Date periodEndDate = new Date(); //current date


    }

    public double getTotalElectricEmissions(int daysInPeriod)
    {
        double co2ElectricityTotal = 0;
        for(Utility utility : utilities)
        {
            if(utility.getUtility_type().equals(Utility.ELECTRICITY_NAME )) {
                co2ElectricityTotal += utility.getPerPersonEmission();
            }
        }
        return co2ElectricityTotal;
    }

    public double getTotalGasEmissions(int daysInPeriod)
    {
        double co2GasTotal = 0;

        for(Utility utility : utilities)
        {
            if(utility.getUtility_type().equals(Utility.GAS_NAME)) {
                co2GasTotal += utility.getPerPersonEmission();
            }
        }
        return co2GasTotal;
    }*/

}

