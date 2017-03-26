package sfu.cmpt276.carbontracker.ui.database;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper for Utility class
 */
class UtilityDatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_UTILITIES = "utilities";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_ISACTIVE = "isactive";
    static final String COLUMN_UTILITY_TYPE = "utility_type";
    static final String COLUMN_START_DATE = "start_date";
    static final String COLUMN_END_DATE = "end_date";
    static final String COLUMN_ELECTRIC_USED = "electric_used";
    static final String COLUMN_NATURALGAS_USED = "naturalgas_used";
    static final String COLUMN_NUMBER_OF_PEOPLE = "num_people";
    static final String COLUMN_DAYS_IN_PERIOD = "days_in_period";
    static final String COLUMN_AVG_KWH_CURRENT = "avg_kwh_current";
    static final String COLUMN_AVG_KWH_PREV = "avg_kwh_prev";
    static final String COLUMN_AVG_GJ_CURRENT = "avg_gj_current";
    static final String COLUMN_AVG_GJ_PREV = "avg_gj_prev";


    private static final String DATABASE_NAME = "utilities.db";
    private static final int DATABASE_VERSION = Database.DATABASE_VERSION; // update if utility class is ever changed

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table " +
            TABLE_UTILITIES + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ISACTIVE + " integer not null, " +
            COLUMN_UTILITY_TYPE + " text not null, " +
            COLUMN_START_DATE + " long not null, " +
            COLUMN_END_DATE + " long not null, " +
            COLUMN_ELECTRIC_USED + " double not null, " +
            COLUMN_NATURALGAS_USED + " double not null, " +
            COLUMN_NUMBER_OF_PEOPLE + " integer not null, " +
            COLUMN_DAYS_IN_PERIOD + " integer not null, " +
            COLUMN_AVG_KWH_CURRENT + " double not null, " +
            COLUMN_AVG_KWH_PREV + " double not null, " +
            COLUMN_AVG_GJ_CURRENT + " double not null, " +
            COLUMN_AVG_GJ_PREV + " double not null);";

    public UtilityDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UtilityDatabaseHelper.class.getName(), "Upgrading + " + DATABASE_NAME + " database from version "
                + oldVersion + " to " + newVersion + ", old data will be destroyed");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILITIES);

        onCreate(db);
    }
}
