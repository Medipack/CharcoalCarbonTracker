package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017-03-17.
 */

public class UtilityList {
    private List<Utility> utilities = new ArrayList<>();

    public List<Utility> getUtilities(){
        return utilities;
    }

    public void addUtility(Utility newUtility){
        utilities.add(newUtility);
    }

    public void editUtility(Utility editUtility, int index){
        validateIndexWithException(index);
        utilities.remove(index);
        utilities.add(index, editUtility);
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
                        + "\n" + utility.getDaysInPeriod() + " days in total\n$" + utility.getNaturalGasUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "current average is: " + utility.getAverageGJCurrent() + "GJ, previous average is: " + utility.getAverageGJPrevious()
                        +  "GJ\npeople in home share: $" + utility.getPeopleShare() + "\nemission per person: " + utility.getPerPersonEmission()
                        + "g\nemission per day: " + utility.getPerDayUsage() + "g";
            }

            else{
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n$" + utility.getElectricUsed() + " used by " + utility.getNumberOfPeople()
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

}

