package sfu.cmpt276.carbontracker.carbonmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import sfu.cmpt276.carbontracker.carbonmodel.Car;

/**
 * Class to keep a record of all the car data read from the CSV
 */

public class CarDirectory{

    //Hashmap that separates EPA car data by make, model and year
    //Stores an undelimited string
    private HashMap<String, HashMap<String, HashMap<String, List<String>>>> mainList;

    //Constructor
    CarDirectory(InputStream input){
        mainList = new HashMap<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, Charset.forName("UTF-8"))
        );
        String line;
        try {
            while((line = reader.readLine())!=null){
                //Initialize a list
                List<String> tempList;
                tempList = readData(line);
                //Add unprocessed string
                tempList.add(line);
            }
        } catch (IOException e) {
            Log.wtf("carDirectory", "Input Exception");
            e.printStackTrace();
        }
    }

    //Key Getters
    public Set<String> getMakeKeys(){
        return mainList.keySet();
    }

    public Set<String> getModelKeys(String Make){
        Set<String> keys = mainList.get(Make).keySet();
        return keys;
    }

    public Set<String> getYearKeys(String make, String model){
        Set<String> keys = mainList.get(make).get(model).keySet();
        return keys;
    }

    //Creates a list of each line read into program
    //Takes at LEAST three tokens: Make, Model and Year
    public List<String> getDataList(String[] token){
        String make = token[0];
        String model = token[1];
        String year = token[2];
        List<String> stringList = mainList.get(make).get(model).get(year);
        return stringList;
    }

    //Creates a profile based on data read from line
    public Car makeProfile(String data){
        String[] token = getTokens(data);
        String make = token[0];
        String model = token[1];
        int year = Integer.valueOf(token[2]);
        String fuelType = token[3];
        String transmission = token[4];
        int cityCO2 = Integer.valueOf(token[5]);
        int hwyCO2 = Integer.valueOf(token[6]);
        double enginedisp = Double.valueOf(token[7]);
        //make new Car class
        Car temp = new Car(make, model, year, fuelType, transmission, cityCO2, hwyCO2, enginedisp);
        return temp;
    }

    //gives a list of cars objects
    //Accepts a csv string that has at least a Make, Model and Year
    public List<Car> carList(String data){
        String[] tokens = getTokens(data);
        List<String> dataList = getDataList(tokens);
        List<Car> list = new ArrayList<Car>();
        for (int i = 0; i<dataList.size(); i++){
            list.add(makeProfile(dataList.get(i)));
        }
        return list;
    }

    //Readers
    //Reads the data and opens up appropriate section
    private List<String> readData(String line) {
        List<String> tempList;//split up the line into tokens
        String[] token = getTokens(line);
        HashMap<String, HashMap<String, List<String>>> tempModel = getModelMap(token[0]);
        HashMap<String, List<String>> tempYear = getYearMap(token[1], tempModel);
        //Check if there is an entry
        checkYearDup(token[2], tempYear);
        tempList = tempYear.get(token[2]);
        return tempList;
    }

    //Reads the make and retrieves appropriate model hashmap
    private HashMap<String, HashMap<String, List<String>>> getModelMap(String key) {
        //Read the make
        //Check for duplicate keys, move onto next phase if true;
        checkMakeDup(key);
        //Copy a pointer to Model hashmap
        return mainList.get(key);
    }

    //Reads the mdoel and retrieves appropriate year hashmap
    private HashMap<String, List<String>> getYearMap(String key, HashMap<String, HashMap<String, List<String>>> tempModel) {
        //Read the model
        //Check for duplicate keys
        checkModelDup(key, tempModel);
        //Copy a pointer to Year hashmap
        return tempModel.get(key);
    }

    //Utility
    @NonNull
    private String[] getTokens(String data) {
        return data.split(",");
    }

    //Duplicate Checkers
    //checks for duplicate entries for year
    private void checkYearDup(String key, HashMap<String, List<String>> tempYear) {
        if(!tempYear.containsKey(key)){
            //Add a new entry for the Year
            tempYear.put(key, new ArrayList<String>());
        }
    }

    //checks for duplicate entries for model
    private void checkModelDup(String key, HashMap<String, HashMap<String, List<String>>> tempModel) {
        if(!tempModel.containsKey(key)){
            //Add a new entry for the Model
            tempModel.put(key, new HashMap<String, List<String>>());
        }
    }

    //checks for duplicate entries for make
    private void checkMakeDup(String key) {
        if(!mainList.containsKey(key)){
            //Add an entry for the Make
            mainList.put(key, new HashMap<String, HashMap<String, List<String>>>());
        }
    }
}
