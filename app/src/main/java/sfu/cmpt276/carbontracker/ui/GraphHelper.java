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
 * Created by Jariniya on 3/26/2017.
 */

public class GraphHelper {

    public static float getJourneyEmissionsForMonthForTransportType(int m, String transportModeWanted) {
        float totalEmissions = 0;
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.MONTH == m;
            boolean isWantedTransportMode = journey.getVehicle().getTransport_mode().equals(transportModeWanted);
            if(isInMonth && isWantedTransportMode)
            {
                totalEmissions += journey.getCarbonEmitted();
            }
        }
        return totalEmissions;
    }

    public static float getUtilityEmissionsForMonthForUtilityType(String utilityType, int m)
    {
        float totalEmissions = 0;
        for(Utility utility: User.getInstance().getUtilityList().getUtilities())
        {
            List<Date> utilityDates = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utility.getStartDate());

            while(calendar.getTime().before(utility.getEndDate()))
            {
                utilityDates.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }

            for(Date date: utilityDates) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                boolean isInMonth = cal.MONTH == m;
                boolean isWantedUtilityType = utilityType.equals(utility.getUtility_type());
                if (isInMonth && isWantedUtilityType) {
                    totalEmissions += utility.getPerDayUsage();
                }
            }
        }
        return totalEmissions;
    }

    public static Map<Vehicle, Float> getCarEmissionTotalsFromJourneysInMonth(int month)
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(journey.getDate());
            boolean isInMonth = calendar.MONTH == month;
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


    public static double getTotalEmissionsForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        double totalEmissions = 0;
        for(Journey journey: getJourneysForTransportModeOnDate(dateWanted, transportModeWanted))
        {
            totalEmissions += journey.getCarbonEmitted();
        }

        return totalEmissions;
    }

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

    public static List<Journey> getJourneysForTransportModeOnDate(Date dateWanted, String transportModeWanted) //
    {
        List<Journey> journeyList = new ArrayList<>();
        for(Journey journey: User.getInstance().getJourneyList())
        {
            Vehicle car = journey.getVehicle();
            Date journeyDateWithoutTime = new Date();
            Date dateWantedWithoutTime = new Date();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

