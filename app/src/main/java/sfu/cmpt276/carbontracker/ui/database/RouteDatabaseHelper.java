package sfu.cmpt276.carbontracker.ui.database;

// Written with help from tutorial at http://www.vogella.com/tutorials/AndroidSQLite/article.html

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper for Route class
 */
class RouteDatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_ROUTES = "routes";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NICKNAME = "nickname";
    static final String COLUMN_CITY_DISTANCE = "city_distance";
    static final String COLUMN_HWY_DISTANCE = "hwy_distance";

    private static final String DATABASE_NAME = "routes.db";
    private static final int DATABASE_VERSION = 1; // update if Route class is ever changed

    // SQL command to create the database
    private static final String DATABASE_CREATE = "create table " +
            TABLE_ROUTES + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NICKNAME + " text not null, " +
            COLUMN_CITY_DISTANCE + " double not null, " +
            COLUMN_HWY_DISTANCE + " double not null);";

    public RouteDatabaseHelper(Context context){
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        onCreate(db);
    }
}
