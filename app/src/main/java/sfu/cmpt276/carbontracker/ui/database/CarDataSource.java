package sfu.cmpt276.carbontracker.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Car;
import sfu.cmpt276.carbontracker.carbonmodel.User;

/**
 * A data source from the Car Database
 */
public class CarDataSource {

    private SQLiteDatabase db;
    private CarDatabaseHelper dbHelper;

    private String[] columns = {
            CarDatabaseHelper.COLUMN_ID,
            CarDatabaseHelper.COLUMN_ISACTIVE,
            CarDatabaseHelper.COLUMN_NICKNAME,
            CarDatabaseHelper.COLUMN_TRANSPORTMODE,
            CarDatabaseHelper.COLUMN_MAKE,
            CarDatabaseHelper.COLUMN_MODEL,
            CarDatabaseHelper.COLUMN_FUELTYPE,
            CarDatabaseHelper.COLUMN_TRANSMISSION,
            CarDatabaseHelper.COLUMN_YEAR,
            CarDatabaseHelper.COLUMN_CITYCO2,
            CarDatabaseHelper.COLUMN_HWYCO2,
            CarDatabaseHelper.COLUMN_ENGINEDISPL};

    public CarDataSource(Context context) {
        dbHelper = new CarDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues carToContentValues(Car car, boolean includeId) {
        ContentValues values = new ContentValues();

            if(includeId)
                values.put(CarDatabaseHelper.COLUMN_ID, car.getId());
        values.put(CarDatabaseHelper.COLUMN_ISACTIVE, User.booleanToInt(car.getActive()));
        values.put(CarDatabaseHelper.COLUMN_NICKNAME, car.getNickname());
        values.put(CarDatabaseHelper.COLUMN_TRANSPORTMODE, car.getTransport_mode());
        values.put(CarDatabaseHelper.COLUMN_MAKE, car.getMake());
        values.put(CarDatabaseHelper.COLUMN_MODEL, car.getModel());
        values.put(CarDatabaseHelper.COLUMN_FUELTYPE, car.getFuelType());
        values.put(CarDatabaseHelper.COLUMN_TRANSMISSION, car.getTransmission());
        values.put(CarDatabaseHelper.COLUMN_YEAR, car.getYear());
        values.put(CarDatabaseHelper.COLUMN_CITYCO2, car.getCityCO2());
        values.put(CarDatabaseHelper.COLUMN_HWYCO2, car.getHwyCO2());
        values.put(CarDatabaseHelper.COLUMN_ENGINEDISPL, car.getEngineDispl());

        return values;
    }

    public void updateCar(Car car) {
        ContentValues values = carToContentValues(car, true);
        db.replace(CarDatabaseHelper.TABLE_CARS, null, values);
    }

    public Car insertCar(Car car) {
        ContentValues values = carToContentValues(car, false);

        long insertId = db.insert(CarDatabaseHelper.TABLE_CARS, null, values);

        Cursor cursor = db.query(CarDatabaseHelper.TABLE_CARS, columns,
                CarDatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Car newCar = cursorToCar(cursor);
        cursor.close();
        return newCar;
    }

    public void deleteCar(Car car) {
        long id = car.getId();
        Log.i(CarDataSource.class.getName(), "Car id " + id + " \"" + car.toString() + "\" deleted from Car database");
        db.delete(CarDatabaseHelper.TABLE_CARS, CarDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();

        Cursor cursor = db.query(CarDatabaseHelper.TABLE_CARS, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Car car = cursorToCar(cursor);
            cars.add(car);
            cursor.moveToNext();
        }

        cursor.close();
        return cars;
    }

    private Car cursorToCar(Cursor cursor) {
        Car car = new Car();

        car.setId(cursor.getInt(0));
        car.setActive(User.intToBoolean(cursor.getInt(1)));
        car.setNickname(cursor.getString(2));
        car.setTransport_mode(cursor.getString(3));
        car.setMake(cursor.getString(4));
        car.setModel(cursor.getString(5));
        car.setFuelType(cursor.getString(6));
        car.setTransmission(cursor.getString(7));
        car.setYear(cursor.getInt(8));
        car.setCityCO2(cursor.getInt(9));
        car.setHwyCO2(cursor.getInt(10));
        car.setEngineDispl(cursor.getDouble(11));

        return car;
    }

    public Car getCarById(int id) {
        Car car = null;

        try (Cursor cursor = db.rawQuery("SELECT * FROM "
                + CarDatabaseHelper.TABLE_CARS + " WHERE "
                + CarDatabaseHelper.COLUMN_ID + " = ?", new String[]{"" + id})) {
            if (cursor.moveToFirst()) {
                car = cursorToCar(cursor);
            }
        }

        return car;
    }
}
