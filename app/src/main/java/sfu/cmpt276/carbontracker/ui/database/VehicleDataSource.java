package sfu.cmpt276.carbontracker.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.User;

/**
 * A data source from the Car Database
 */
public class VehicleDataSource {

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

    public VehicleDataSource(Context context) {
        dbHelper = new CarDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues vehicleToContentValues(Vehicle vehicle, boolean includeId) {
        ContentValues values = new ContentValues();
        if(includeId) {
            values.put(CarDatabaseHelper.COLUMN_ID, vehicle.getId());
        }
        values.put(CarDatabaseHelper.COLUMN_ISACTIVE, User.booleanToInt(vehicle.getActive()));
        values.put(CarDatabaseHelper.COLUMN_NICKNAME, vehicle.getNickname());
        values.put(CarDatabaseHelper.COLUMN_TRANSPORTMODE, vehicle.getTransport_mode());
        values.put(CarDatabaseHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(CarDatabaseHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(CarDatabaseHelper.COLUMN_FUELTYPE, vehicle.getFuelType());
        values.put(CarDatabaseHelper.COLUMN_TRANSMISSION, vehicle.getTransmission());
        values.put(CarDatabaseHelper.COLUMN_YEAR, vehicle.getYear());
        values.put(CarDatabaseHelper.COLUMN_CITYCO2, vehicle.getCityCO2());
        values.put(CarDatabaseHelper.COLUMN_HWYCO2, vehicle.getHwyCO2());
        values.put(CarDatabaseHelper.COLUMN_ENGINEDISPL, vehicle.getEngineDispl());
        return values;
    }

    public void updateVehicle(Vehicle vehicle) {
        ContentValues values = vehicleToContentValues(vehicle, true);
        db.replace(CarDatabaseHelper.TABLE_CARS, null, values);
    }

    public Vehicle insertCar(Vehicle vehicle) {
        ContentValues values = vehicleToContentValues(vehicle, false);

        long insertId = db.insert(CarDatabaseHelper.TABLE_CARS, null, values);

        Cursor cursor = db.query(CarDatabaseHelper.TABLE_CARS, columns,
                CarDatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Vehicle newCar = cursorToVehicle(cursor);
        cursor.close();
        return newCar;
    }

    public void deleteCar(Vehicle car) {
        long id = car.getId();
        Log.i(VehicleDataSource.class.getName(), "Car id " + id + " \"" + car.toString() + "\" deleted from Car database");
        db.delete(CarDatabaseHelper.TABLE_CARS, CarDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Vehicle> getAllCars() {
        List<Vehicle> cars = new ArrayList<>();

        Cursor cursor = db.query(CarDatabaseHelper.TABLE_CARS, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Vehicle car = cursorToVehicle(cursor);
            cars.add(car);
            cursor.moveToNext();
        }

        cursor.close();
        return cars;
    }

    private Vehicle cursorToVehicle(Cursor cursor) {
        Vehicle vehicle = new Vehicle();

        vehicle.setId(cursor.getInt(0));
        vehicle.setActive(User.intToBoolean(cursor.getInt(1)));
        vehicle.setNickname(cursor.getString(2));
        vehicle.setTransport_mode(cursor.getString(3));
        vehicle.setMake(cursor.getString(4));
        vehicle.setModel(cursor.getString(5));
        vehicle.setFuelType(cursor.getString(6));
        vehicle.setTransmission(cursor.getString(7));
        vehicle.setYear(cursor.getInt(8));
        vehicle.setCityCO2(cursor.getInt(9));
        vehicle.setHwyCO2(cursor.getInt(10));
        vehicle.setEngineDispl(cursor.getDouble(11));

        return vehicle;
    }

    public Vehicle getCarById(int id) {
        Vehicle car = null;

        try (Cursor cursor = db.rawQuery("SELECT * FROM "
                + CarDatabaseHelper.TABLE_CARS + " WHERE "
                + CarDatabaseHelper.COLUMN_ID + " = ?", new String[]{"" + id})) {
            if (cursor.moveToFirst()) {
                car = cursorToVehicle(cursor);
            }
        }

        return car;
    }
}
