package sfu.cmpt276.carbontracker.carbonmodel;

/**
 * Created by apple on 2017-03-15.
 */

public class Gas {
    private boolean isActive;
    private double gasAmountUsed;
    private int gasNumShared;
    private double gasShared;

    public Gas() {
        isActive = false;
        gasAmountUsed = 0;
        gasNumShared = 0;
    }

    public Gas(double gas_amount, int gas_num){
        gasAmountUsed = gas_amount;
        gasNumShared = gas_num;
    }

    public double getGasAmountUsed(){
        return gasAmountUsed;
    }

    public int getGasNumShared(){
        return gasNumShared;
    }

    public double getGasShared(){
        gasShared = getGasAmountUsed()/getGasNumShared();
        return gasShared;
    }
}
