package sfu.cmpt276.carbontracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to keep a record of all the car data read from the CSV
 */

public class carDirectory extends Car{

    private List mainList = new ArrayList<Car>();

    carDirectory(){

    }
    public Car getCar(int index){
       return (Car) mainList.get(index);
    }
    public void addCar(Car newCar){
        mainList.add(newCar);
    }

}
