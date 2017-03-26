package sfu.cmpt276.carbontracker;

import org.junit.Test;

import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;

import static org.junit.Assert.assertEquals;

public class TestCarClass {
    @Test
    public void CO2Conversion() throws Exception {
        Vehicle vehicle = new Vehicle();

        int mpg = 10;

        vehicle.setCityCO2(mpg);

        double DELTA = 1e-4;

        assertEquals(4.25144, vehicle.getCityCO2(), DELTA);
    }

    


}