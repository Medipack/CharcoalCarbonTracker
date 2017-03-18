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

    public void removeUtility(int index){
        utilities.remove(index);
    }

    public int countUtility(){
        return utilities.size();
    }

    public Utility getUtility(int index){
        validateIndexWithException(index);
        return utilities.get(index);
    }

    public String[] getUtilityDescription(){
        String[] descriptions = new String[countUtility()];
        for(int i=0; i<countUtility();i++) {
            Utility utility = getUtility(i);
            if (utility.getUtility_type().equals("gas")) {
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n" + utility.getNaturalGasUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "current average is: " + utility.getAverageGJCurrent() + ", previous average is: " + utility.getAverageGJPrevious()
                        +  "\nper day: " + utility.getPerDayUsed() + "\npeople in home share: " + utility.getPeopleShare() + "\nemission per person: " + utility.getPerPersonEmission()
                        + "\nemission per day: " + utility.getPerDayUsage();
            }

            else{
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n" + utility.getElectricUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "current average is: " + utility.getAverageKWhCurrent() + ", previous average is: " + utility.getAverageKWhPrevious()
                        +  "\nper day: " + utility.getPerDayUsed() + "\npeople in home share: " + utility.getPeopleShare() + "\nemission: " + utility.getPerPersonEmission()
                        + "\ndaily usage: " + utility.getPerDayUsage();
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

