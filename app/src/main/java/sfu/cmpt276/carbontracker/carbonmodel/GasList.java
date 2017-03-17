package sfu.cmpt276.carbontracker.carbonmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017-03-15.
 */

public class GasList {
    private List<Gas> gases = new ArrayList<>();
    public List<Gas> getGases(){
        return gases;
    }

    public void addGas(Gas newGas){
        gases.add(newGas);
    }

    public Gas getGas(int index){
        return gases.get(index);
    }

    public int countGas(){
        return gases.size();
    }

    public String[] getGasDescription(){
        String[] descriptions = new String[countGas()];
        for(int i=0; i<countGas();i++){
            Gas gas = getGas(i);
            descriptions[i] = gas.getGasAmountUsed() + "\n" + gas.getGasNumShared() + "\n" + gas.getGasShared();
        }
        return descriptions;
    }

}
