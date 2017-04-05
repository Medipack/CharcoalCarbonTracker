package sfu.cmpt276.carbontracker.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.Route;

/**
 * A data source from the Journey Database
 */
public class JourneyDataSource {

    private SQLiteDatabase db;
    private JourneyDatabaseHelper dbHelper;

    private String[] columns = {
            JourneyDatabaseHelper.COLUMN_ID,
            JourneyDatabaseHelper.COLUMN_CAR_ID,
            JourneyDatabaseHelper.COLUMN_ROUTE_ID,
            JourneyDatabaseHelper.COLUMN_DATE,
            JourneyDatabaseHelper.COLUMN_DATE_ENTERED
    };

    public JourneyDataSource(Context context) {
        dbHelper = new JourneyDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues journeyToContentValues(Journey journey, boolean includeId) {
        ContentValues values = new ContentValues();

        if(includeId)
            values.put(JourneyDatabaseHelper.COLUMN_ID, journey.getId());
        values.put(JourneyDatabaseHelper.COLUMN_CAR_ID, journey.getVehicle().getId());
        values.put(JourneyDatabaseHelper.COLUMN_ROUTE_ID, journey.getRoute().getId());
        values.put(JourneyDatabaseHelper.COLUMN_DATE, journey.getDate().getTime());
        values.put(JourneyDatabaseHelper.COLUMN_DATE_ENTERED, (new Date()).getTime());

        return values;
    }

    public void updateJourney(Journey journey) {
        ContentValues values = journeyToContentValues(journey, true);
        db.replace(JourneyDatabaseHelper.TABLE_JOURNEYS, null, values);
    }

    public Journey insertJourney(Journey journey, Context context) {
        ContentValues values = journeyToContentValues(journey, false);

        long insertId = db.insert(JourneyDatabaseHelper.TABLE_JOURNEYS, null, values);

        Cursor cursor = db.query(JourneyDatabaseHelper.TABLE_JOURNEYS, columns,
                JourneyDatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Journey newJourney = cursorToJourney(cursor, context);
        cursor.close();
        return newJourney;
    }

    public void deleteJourney(Journey journey) {
        long id = journey.getId();
        Log.i(JourneyDataSource.class.getName(), "Journey id " + id + " \"" + journey.toString() + "\" deleted from Journey database");
        db.delete(JourneyDatabaseHelper.TABLE_JOURNEYS, JourneyDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Journey> getAllJourneys(Context context) {
        List<Journey> journeys = new ArrayList<>();

        VehicleDataSource car_db = new VehicleDataSource(context);
        RouteDataSource route_db = new RouteDataSource(context);

        Cursor cursor = db.query(JourneyDatabaseHelper.TABLE_JOURNEYS, columns, null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Journey journey = cursorToJourney(cursor, car_db, route_db);
            journeys.add(journey);
            cursor.moveToNext();
        }

        cursor.close();
        return journeys;
    }

    private Journey cursorToJourney(Cursor cursor, VehicleDataSource vehicle_db, RouteDataSource route_db) {
        Journey journey = new Journey();

        journey.setId(cursor.getInt(0));

        // Get Car from Database
        vehicle_db.open();
        Vehicle car = vehicle_db.getCarById(cursor.getInt(1));
        journey.setVehicle(car);
        vehicle_db.close();

        // Get Route from Database
        route_db.open();
        Route route = route_db.getRouteById(cursor.getInt(2));
        journey.setRoute(route);
        route_db.close();

        // Set Date in epoch time (long)
        long dateTime = cursor.getLong(3);
        Date date = new Date(dateTime);
        journey.setDate(date);

        // Set Date in epoch time (long)
        long dateEnteredTime = cursor.getLong(4);
        Date dateEntered = new Date(dateEnteredTime);
        journey.setDateEntered(dateEntered);

        return journey;
    }

    private Journey cursorToJourney(Cursor cursor, Context context) {
        VehicleDataSource car_db = new VehicleDataSource(context);
        RouteDataSource route_db = new RouteDataSource(context);

        return cursorToJourney(cursor, car_db, route_db);
    }
}
