package sfu.cmpt276.carbontracker;

import org.junit.Test;

import sfu.cmpt276.carbontracker.carbonmodel.Car;

import static org.junit.Assert.assertEquals;

public class TestCarClass {
    @Test
    public void CO2Conversion() throws Exception {
        Car car = new Car();

        int mpg = 10;

        car.setCityCO2(mpg);

        double DELTA = 1e-4;

        assertEquals(4.25144, car.getCityCO2(), DELTA);
    }

    


}