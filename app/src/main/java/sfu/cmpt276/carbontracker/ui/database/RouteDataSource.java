package sfu.cmpt276.carbontracker.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sfu.cmpt276.carbontracker.carbonmodel.Route;

/**
 * A data source from the Route Database
 */
public class RouteDataSource {

    private SQLiteDatabase db;
    private RouteDatabaseHelper dbHelper;

    private String[] columns = {
            RouteDatabaseHelper.COLUMN_ID,
            RouteDatabaseHelper.COLUMN_NICKNAME,
            RouteDatabaseHelper.COLUMN_CITY_DISTANCE,
            RouteDatabaseHelper.COLUMN_HWY_DISTANCE
   };

    public RouteDataSource(Context context) {
        dbHelper = new RouteDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Route insertRoute(Route route) {
        ContentValues values = new ContentValues();

        values.put(RouteDatabaseHelper.COLUMN_NICKNAME, route.getRouteName());
        values.put(RouteDatabaseHelper.COLUMN_CITY_DISTANCE, route.getRouteDistanceCity());
        values.put(RouteDatabaseHelper.COLUMN_HWY_DISTANCE, route.getRouteDistanceHighway());

        long insertId = db.insert(RouteDatabaseHelper.TABLE_ROUTES, null, values);

        Cursor cursor = db.query(RouteDatabaseHelper.TABLE_ROUTES, columns,
                RouteDatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Route newRoute = cursorToRoute(cursor);
        cursor.close();
        return newRoute;
    }

    public void deleteRoute(Route route) {
        long id = route.getId();
        Log.i(RouteDataSource.class.getName(), "Route id " + id + " \"" + route.toString() + "\" deleted from Route database");
        db.delete(RouteDatabaseHelper.TABLE_ROUTES, RouteDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();

        Cursor cursor = db.query(RouteDatabaseHelper.TABLE_ROUTES, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Route route = cursorToRoute(cursor);
            routes.add(route);
            cursor.moveToNext();
        }

        cursor.close();
        return routes;
    }

    private Route cursorToRoute(Cursor cursor) {
        Route route = new Route();

        route.setId(cursor.getInt(0));
        route.setRouteName(cursor.getString(1));
        route.setRouteDistanceCity(cursor.getDouble(2));
        route.setRouteDistanceHighway(cursor.getDouble(3));

        return route;
    }

    public Route getRouteById(int id) {
        Route route = null;

        try (Cursor cursor = db.rawQuery("SELECT * FROM "
                + RouteDatabaseHelper.TABLE_ROUTES + " WHERE "
                + RouteDatabaseHelper.COLUMN_ID + " = ?", new String[]{"" + id})) {
            if (cursor.moveToFirst()) {
                route = cursorToRoute(cursor);
            }
        }

        return route;
    }
}
