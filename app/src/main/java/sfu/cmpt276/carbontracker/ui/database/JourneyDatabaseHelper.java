package sfu.cmpt276.carbontracker.ui.database;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper for Journey class
 */
class JourneyDatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_JOURNEYS = "journeys";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_CAR_ID = "car_id";
    static final String COLUMN_ROUTE_ID = "route_id";
    static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "journeys.db";
    private static final int DATABASE_VERSION = Database.DATABASE_VERSION; // update if Journey class is ever changed

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table " +
            TABLE_JOURNEYS + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CAR_ID + " integer not null, " +
            COLUMN_ROUTE_ID + " integer not null, " +
            COLUMN_DATE + " long not null);";

    public JourneyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RouteDatabaseHelper.class.getName(), "Upgrading + " + DATABASE_NAME + " database from version "
                + oldVersion + " to " + newVersion + ", old data will be destroyed");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS);
        onCreate(db);
    }
}
