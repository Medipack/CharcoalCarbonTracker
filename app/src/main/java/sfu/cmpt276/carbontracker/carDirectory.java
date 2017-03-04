package sfu.cmpt276.carbontracker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to keep a record of all the car data read from the CSV
 */

public class carDirectory extends Car{

    //Hashmap that separates EPA car data by make, model and year
    //Stores an undelimited string
    private HashMap<String, HashMap<String, HashMap<String, List<String>>>> mainList;

    carDirectory(InputStream input){
        mainList = new HashMap<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, Charset.forName("UTF-8"))
        );
        String line;
        try {
            while((line = reader.readLine()) != null){
                //split up the line into tokens
                String[] tokens = line.split(",");
                //Read the data
                HashMap<String, HashMap<String, List<String>>> tempModel = getMakeHashMap(tokens[0]);
                HashMap<String, List<String>> tempYear = getModelHashMap(tokens[1], tempModel);
                List<String> tempList = getList(tokens[2], tempYear);
                //Add unprocessed string
                tempList.add(line);
            }
        } catch (IOException e) {
            Log.wtf("carDirectory", "Input Exception");
            e.printStackTrace();
        }
    }

    private HashMap<String, List<String>> getModelHashMap(String token, HashMap<String, HashMap<String, List<String>>> tempModel) {
        //Check for duplicate keys
        if(!tempModel.containsKey(token)){
            //Add an entry for the Model
            tempModel.put(token, new HashMap<String, List<String>>());
        }
        //Copy a pointer to Year hashmap
        return tempModel.get(token);
    }

    private HashMap<String, HashMap<String, List<String>>> getMakeHashMap(String token) {
        //Check for duplicate keys, move onto next phase if true;
        if(!mainList.containsKey(token)){
            //Add an entry for the Make
            mainList.put(token, new HashMap<String, HashMap<String, List<String>>>());
        }
        //Copy a pointer to Model hashmap
        return mainList.get(token);
    }

    private List<String> getList(String token, HashMap<String, List<String>> tempYear) {
        List<String> tempList;
        //Check if there is an entry
        if(!tempYear.containsKey(token)){
            //Add a new entry for the Year
            tempYear.put(token, new ArrayList<String>());
        }
        tempList = tempYear.get(token);
        return tempList;
    }
}
