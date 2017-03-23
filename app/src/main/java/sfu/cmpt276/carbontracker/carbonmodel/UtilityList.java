package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.ui.BillActivity;

/**
 * /**
 * List Class to hold data on montly utility, as per data entered by user based on fortisbc or bc hydro bill
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
                        + " people in home\n" + "people in home share: " + utility.getPeopleShare() +
                        "g\nemission per day per person: " + utility.getPerDayUsage() + "g";
            }

            else{
                descriptions[i] = utility.getUtility_type() + ": \nfrom " + utility.getStartDate() + "\n" + "to " + utility.getEndDate()
                        + "\n" + utility.getDaysInPeriod() + " days in total\n" + utility.getElectricUsed() + " used by " + utility.getNumberOfPeople()
                        + " people in home\n" + "people in home share: " + utility.getPeopleShare()
                        + "g\nemission per day person: " + utility.getPerDayUsage() + "g";
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

