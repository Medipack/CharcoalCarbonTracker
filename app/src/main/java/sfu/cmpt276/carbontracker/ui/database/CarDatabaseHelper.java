package sfu.cmpt276.carbontracker.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

/**
 * Database Helper for Car class
 */
class CarDatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_CARS = "cars";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NICKNAME = "nickname";
    static final String COLUMN_MAKE = "make";
    static final String COLUMN_MODEL = "model";
    static final String COLUMN_FUELTYPE = "fueltype";
    static final String COLUMN_TRANSMISSION = "transmission";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_CITYCO2 = "cityco2";
    static final String COLUMN_HWYCO2 = "hwyco2";
    static final String COLUMN_ENGINEDISPL = "enginedispl";

    private static final String DATABASE_NAME = "cars.db";
    private static final int DATABASE_VERSION = 3; // update if Car class is ever changed

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table " +
            TABLE_CARS + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NICKNAME + " text not null, " +
            COLUMN_MAKE + " text not null, " +
            COLUMN_MODEL + " text not null, " +
            COLUMN_FUELTYPE + " text not null, " +
            COLUMN_TRANSMISSION + " text not null, " +
            COLUMN_YEAR + " integer not null, " +
            COLUMN_CITYCO2 + " integer not null, " +
            COLUMN_HWYCO2 + " integer not null, " +
            COLUMN_ENGINEDISPL + " double not null);";

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
