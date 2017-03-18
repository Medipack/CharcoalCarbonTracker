package sfu.cmpt276.carbontracker.carbonmodel.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

/**
 * Database Helper for Car class
 */
public class CarDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAR = "car";

    private static final String DATABASE_NAME = "cars.db";
    private static final int DATABASE_VERSION = 1; // update if Car class is ever changed

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CARS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CAR
            + " text not null);";

    public CarDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CarDatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", old data will be destroyed");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
    }
}
