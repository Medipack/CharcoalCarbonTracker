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

/**
 * A data source from the Car Database
 */
public class CarDataSource {

    private SQLiteDatabase db;
    private CarDatabaseHelper dbHelper;

    private String[] columns = {
            CarDatabaseHelper.COLUMN_ID,
            CarDatabaseHelper.COLUMN_CAR };

    public CarDataSource(Context context) {
        dbHelper = new CarDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Car insertCar(Car car) {
        ContentValues values = new ContentValues();

        values.put(CarDatabaseHelper.COLUMN_CAR, car.getMake());
        //todo add ALL values of car

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

    public Car cursorToCar(Cursor cursor) {
        Car car = new Car();

        car.setId(cursor.getInt(0));
        car.setMake(cursor.getString(1));

        return car;
    }
}
