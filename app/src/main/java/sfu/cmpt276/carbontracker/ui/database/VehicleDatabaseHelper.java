package sfu.cmpt276.carbontracker.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

/**
 * Database Helper for Car class
 */
class VehicleDatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_VEHICLES = "vehicles";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_ISACTIVE = "isactive";
    static final String COLUMN_NICKNAME = "nickname";
    static final String COLUMN_TRANSPORTMODE = "transportmode";
    static final String COLUMN_MAKE = "make";
    static final String COLUMN_MODEL = "model";
    static final String COLUMN_FUELTYPE = "fueltype";
    static final String COLUMN_TRANSMISSION = "transmission";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_CITYCO2 = "cityco2";
    static final String COLUMN_HWYCO2 = "hwyco2";
    static final String COLUMN_ENGINEDISPL = "enginedispl";
    static final String COLUMN_ICON_ID = "iconID";

    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = Database.DATABASE_VERSION; // update if Car class is ever changed

    private final Context createContext;

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table " +
            TABLE_VEHICLES + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ISACTIVE + " integer not null, " +
            COLUMN_NICKNAME + " text not null, " +
            COLUMN_TRANSPORTMODE + " text not null, " +
            COLUMN_MAKE + " text not null, " +
            COLUMN_MODEL + " text not null, " +
            COLUMN_FUELTYPE + " text not null, " +
            COLUMN_TRANSMISSION + " text not null, " +
            COLUMN_YEAR + " integer not null, " +
            COLUMN_CITYCO2 + " integer not null, " +
            COLUMN_HWYCO2 + " integer not null, " +
            COLUMN_ENGINEDISPL + " double not null, " +
            COLUMN_ICON_ID + " integer not null);";

    public VehicleDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        createContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(VehicleDatabaseHelper.class.getName(), "Upgrading + " + DATABASE_NAME + " database from version "
                + oldVersion + " to " + newVersion + ", old data will be destroyed");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);

        JourneyDatabaseHelper journey_db = new JourneyDatabaseHelper(createContext);
        journey_db.onUpgrade(db, 0,0);

        onCreate(db);
    }
}
