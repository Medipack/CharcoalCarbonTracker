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

import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

/**
 * A data source from the Utility Database
 */
public class UtilityDataSource {

    private SQLiteDatabase db;
    private UtilityDatabaseHelper dbHelper;

    private String[] columns = {
            UtilityDatabaseHelper.COLUMN_ID,
            UtilityDatabaseHelper.COLUMN_ISACTIVE,
            UtilityDatabaseHelper.COLUMN_UTILITY_TYPE,
            UtilityDatabaseHelper.COLUMN_START_DATE,
            UtilityDatabaseHelper.COLUMN_END_DATE,
            UtilityDatabaseHelper.COLUMN_ELECTRIC_USED,
            UtilityDatabaseHelper.COLUMN_NATURALGAS_USED,
            UtilityDatabaseHelper.COLUMN_NUMBER_OF_PEOPLE,
            UtilityDatabaseHelper.COLUMN_DAYS_IN_PERIOD,
            UtilityDatabaseHelper.COLUMN_AVG_KWH_CURRENT,
            UtilityDatabaseHelper.COLUMN_AVG_KWH_PREV,
            UtilityDatabaseHelper.COLUMN_AVG_GJ_CURRENT,
            UtilityDatabaseHelper.COLUMN_AVG_GJ_PREV
    };

    public UtilityDataSource(Context context) {
        dbHelper = new UtilityDatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues utilityToContentValues(Utility utility, boolean includeId) {
        ContentValues values = new ContentValues();

        if(includeId)
            values.put(UtilityDatabaseHelper.COLUMN_ID, utility.getId());
        values.put(UtilityDatabaseHelper.COLUMN_ISACTIVE, User.booleanToInt(utility.isActive()));
        values.put(UtilityDatabaseHelper.COLUMN_UTILITY_TYPE, utility.getUtility_type());
        values.put(UtilityDatabaseHelper.COLUMN_START_DATE, utility.getStartDate().getTime());
        values.put(UtilityDatabaseHelper.COLUMN_END_DATE, utility.getEndDate().getTime());
        values.put(UtilityDatabaseHelper.COLUMN_ELECTRIC_USED, utility.getElectricUsed());
        values.put(UtilityDatabaseHelper.COLUMN_NATURALGAS_USED, utility.getNaturalGasUsed());
        values.put(UtilityDatabaseHelper.COLUMN_NUMBER_OF_PEOPLE, utility.getNumberOfPeople());
        values.put(UtilityDatabaseHelper.COLUMN_DAYS_IN_PERIOD, utility.getDaysInPeriod());

        return values;
    }

    public void updateUtility(Utility utility) {
        ContentValues values = utilityToContentValues(utility, true);
        db.replace(UtilityDatabaseHelper.TABLE_UTILITIES, null, values);
    }

    public Utility insertUtility(Utility utility) {
        ContentValues values = utilityToContentValues(utility, false);

        long insertId = db.insert(UtilityDatabaseHelper.TABLE_UTILITIES, null, values);

        Cursor cursor = db.query(UtilityDatabaseHelper.TABLE_UTILITIES, columns,
                UtilityDatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Utility newUtility = cursorToUtility(cursor);
        cursor.close();
        return newUtility;
    }

    public void deleteUtility(Utility utility) {
        long id = utility.getId();
        Log.i(UtilityDataSource.class.getName(), "Utility id " + id + " \"" + utility.toString() + "\" deleted from Utility database");
        db.delete(UtilityDatabaseHelper.TABLE_UTILITIES, UtilityDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Utility> getAllUtilities() {
        List<Utility> utilities = new ArrayList<>();

        Cursor cursor = db.query(UtilityDatabaseHelper.TABLE_UTILITIES, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Utility utility = cursorToUtility(cursor);
            utilities.add(utility);
            cursor.moveToNext();
        }

        cursor.close();
        return utilities;
    }

    private Utility cursorToUtility(Cursor cursor) {
        Utility utility = new Utility();

        utility.setId(cursor.getInt(0));
        utility.setActive(User.intToBoolean(cursor.getInt(1)));
        utility.setUtility_type(cursor.getString(2));

        // Set Date in epoch time (long)
        long startDateInEpochTime = cursor.getLong(3);
        Date startDate = new Date(startDateInEpochTime);
        utility.setStartDate(startDate);

        // Set Date in epoch time (long)
        long endDateInEpochTime = cursor.getLong(4);
        Date endDate = new Date(endDateInEpochTime);
        utility.setEndDate(endDate);

        utility.setElectricUsed(cursor.getDouble(5));
        utility.setNaturalGasUsed(cursor.getDouble(6));
        utility.setNumberOfPeople(cursor.getInt(7));
        utility.setDaysInPeriod(cursor.getInt(8));


        return utility;
    }

    public Utility getUtilityById(int id) {
        Utility utility = null;

        try (Cursor cursor = db.rawQuery("SELECT * FROM "
                + UtilityDatabaseHelper.TABLE_UTILITIES + " WHERE "
                + UtilityDatabaseHelper.COLUMN_ID + " = ?", new String[]{"" + id})) {
            if (cursor.moveToFirst()) {
                utility = cursorToUtility(cursor);
            }
        }

        return utility;
    }
}
