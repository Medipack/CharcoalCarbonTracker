package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;

/**
 * Class containing methods to get graph data
 */

public class GraphHelper {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    ///////365 day graph methods///////
    //Returns total emissions for a particular transport mode during a particular month
    public static float getJourneyEmissionsForMonthForTransportType(int m, String transportModeWanted) {
        float totalEmissions = 0;
        for(Journey journey: User.getInstance().getJourneyList()) //for each journey in the journey list
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.get(Calendar.MONTH) == m; //if the journey is in the desired month
            boolean isWantedTransportMode = journey.getVehicle().getTransport_mode().equals(transportModeWanted); //if journey is desired transport mode
            if(isInMonth && isWantedTransportMode)
            {
                totalEmissions += journey.getCarbonEmitted(); //total up emissions for journeys of specific transport mode during specific month
            }
        }
        return totalEmissions;
    }
    //returns total emissions for a particular utility type during a particular month
    public static float getUtilityEmissionsForMonthForUtilityType(String utilityType, int m)
    {
        float totalEmissions = 0;
        for(Utility utility: User.getInstance().getUtilityList().getUtilities()) //for all known utilities in utility list
        {
            List<Date> utilityDates = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utility.getStartDate()); //get start date of utility

            while(calendar.getTime().before(utility.getEndDate())) //for each date from start to end of utility
            {
                boolean isInMonth = calendar.get(Calendar.MONTH) == m;
                boolean isWantedUtilityType = utilityType.equals(utility.getUtility_type());
                if (isInMonth && isWantedUtilityType) { //if utility is during the particular month, and of the particular type
                    totalEmissions += utility.getPerDayUsage(); //add to total emissions
                }
                utilityDates.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1); //increment date for while loop
            }

        }
        return totalEmissions;
    }
    //returns map of total emissions for all cars during a particular month, keys are unique cars, values are total emissions
    public static Map<Vehicle, Float> getCarEmissionTotalsFromJourneysInMonth(int month)
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.get(Calendar.MONTH) == month;
            boolean isCar = journey.getVehicle().getTransport_mode().equals(Vehicle.CAR);
            if(isInMonth && isCar)
            {
                journeyList.add(journey);
            }
        }
        Map<Vehicle, Float> map = new HashMap<>();
        for(Journey journey: journeyList)
        {
            Vehicle vehicle = journey.getVehicle();
            if(!map.containsKey(vehicle))
            {
                map.put(vehicle, (float)journey.getCarbonEmitted());
            }
            else
            {
                float temp = map.get(vehicle);
                temp += journey.getCarbonEmitted();
                map.put(vehicle, temp);
            }
        }
        return map;
    }

///////methods for 28 day graph///////
    //returns total emissions for a given transport mode on a given date
    public static double getTotalEmissionsForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        double totalEmissions = 0;
        for(Journey journey: getJourneysForTransportModeOnDate(dateWanted, transportModeWanted))
        {
            totalEmissions += journey.getCarbonEmitted();
        }

        return totalEmissions;
    }
    //returns list of dates during the period we want (e.g. last 28 days)
    public static List<Date> getDateList(int daysInPeriod) //gets dates in period
    {
        List<Date> dateList = new ArrayList<>(daysInPeriod);
        for (int i=0; i < daysInPeriod; i++) {
            Calendar cal = Calendar.getInstance(); // this would default to now
            cal.add(Calendar.DAY_OF_YEAR, -i);
            dateList.add(cal.getTime());
        }
        return dateList;
    }
    //returns list of journeys for given transport mode on given date
    public static List<Journey> getJourneysForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Vehicle car = journey.getVehicle();
            Date journeyDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
            try {
                journeyDateWithoutTime = sdf.parse(sdf.format(journey.getDate()));
                dateWantedWithoutTime = sdf.parse(sdf.format(dateWanted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean isWantedTransportMode = car.getTransport_mode().equals(transportModeWanted);
            boolean isInDate = journeyDateWithoutTime.equals(dateWantedWithoutTime);
            if(isWantedTransportMode && isInDate)
            {
                journeyList.add(journey);
            }
        }

        return journeyList;
    }


    /*private List<Journey> getJourneysForTransportationModeInMonth(Calendar calendar, String transportModeWanted) //
    {
        List<Journey> mainJourneyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Car car = journey.getCar();
            Calendar journeyCal = Calendar.getInstance();
            journeyCal.setTime(journey.getDate());

            boolean isWantedTransportMode = car.getTransport_mode().equals(transportModeWanted);
            boolean isSameMonth = calendar.MONTH == journeyCal.MONTH;
            if(isWantedTransportMode && isSameMonth)
            {
                mainJourneyList.add(journey);
            }
        }

        return mainJourneyList;
    }*/

    public static Map<Vehicle, Float> getCarEmissionTotalsFromJourneys(List<Journey> journeyList)
    {
        Map<Vehicle, Float> map = new HashMap<>();
        for(Journey journey: journeyList)
        {
            if(!map.containsKey(journey.getVehicle()))
            {
                map.put(journey.getVehicle(), (float)journey.getCarbonEmitted());
            }
            else
            {
                float temp = map.get(journey.getVehicle());
                temp += journey.getCarbonEmitted();
                map.put(journey.getVehicle(), temp);
            }
        }
        return map;
    }
    //returns map of emissions for utilities on given date, where key is utility type and value is total emissions
    public static Map<String, Float> getDailyTotalUtilityEmissions(Date dateWanted) //return total of daily averages for electric and natural gas as separate keys-value pairs in map
    {
        Map<String, Float> map = new HashMap<>();
        map.put(Utility.ELECTRICITY_NAME, 0f); //set for electricity emission totals
        map.put(Utility.GAS_NAME, 0f); //set for gas emission totals

        for(Utility utility: User.getInstance().getUtilityList().getUtilities())  //each utility known
        {
            Date utilityStartDateWithoutTime = new Date();
            Date utilityEndDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
            try {
                utilityStartDateWithoutTime = sdf.parse(sdf.format(utility.getStartDate()));
                utilityEndDateWithoutTime = sdf.parse(sdf.format(utility.getEndDate()));
                dateWantedWithoutTime = sdf.parse(sdf.format(dateWanted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean isInDate =  dateWantedWithoutTime.after(utilityStartDateWithoutTime) && dateWantedWithoutTime.before(utilityEndDateWithoutTime);
            if(isInDate)
            {
                float tempTotal = map.get(utility.getUtility_type());
                tempTotal += utility.getPerDayUsage();
                map.put(utility.getUtility_type(), tempTotal);
            }
        }
        return map;
    }
}

