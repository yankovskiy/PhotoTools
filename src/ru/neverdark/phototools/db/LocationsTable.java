package ru.neverdark.phototools.db;

import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.phototools.utils.Log;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationsTable {
    public static final String KEY_LAST_ACCESS = "last_access";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LOCATION_NAME = "location_name";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROWID = "_id";
    private static final String TABLE_NAME = "locations";
    private SQLiteDatabase mDatabase;

    /**
     * Creates ContentValues for insert or update to database
     * 
     * @param locationName
     *            location name
     * @param latitude
     *            location latitude
     * @param longitude
     *            location longitude
     * @return ContentValues object
     */
    private ContentValues createContentValues(String locationName,
            double latitude, double longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOCATION_NAME, locationName);
        contentValues.put(KEY_LATITUDE, latitude);
        contentValues.put(KEY_LONGITUDE, longitude);
        contentValues.put(KEY_LAST_ACCESS, getTimeStamp());

        return contentValues;
    }

    /**
     * Creates location in database
     * 
     * @param locationName
     *            location name
     * @param latitude
     *            location latitude
     * @param longitude
     *            location longitude
     * @return recordId if success or -1 for fail
     */
    public long createLocation(String locationName, double latitude,
            double longitude) {
        Log.message("Enter");
        ContentValues insertValues = createContentValues(locationName,
                latitude, longitude);

        return mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    /**
     * Deletes location by recordId
     * 
     * @param recordId
     *            recordId in Location table
     * @return true if delete success or false in other case
     */
    public boolean deleteLocation(long recordId) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = { String.valueOf(recordId) };
        return mDatabase.delete(TABLE_NAME, where, whereArgs) > 0;
    }

    /**
     * Fetches all locations from database
     * 
     * @return Cursor contains all records from Locations table
     */
    public Cursor fetchAllLocations() {
        Log.message("Enter");
        String[] columns = { KEY_ROWID, KEY_LOCATION_NAME, KEY_LATITUDE,
                KEY_LONGITUDE };
        String where = KEY_ROWID + " > 1";
        return mDatabase.query(TABLE_NAME, columns, where, null, null, null,
                KEY_LAST_ACCESS + " DESC");
    }

    /**
     * Fetches single location from database
     * 
     * @param recordId
     *            record Id for selection
     * @return Cursor contains record from Locations table
     */
    public Cursor fetchSingleLocation(long recordId) {
        Log.message("Enter");
        String[] columns = { KEY_ROWID, KEY_LOCATION_NAME, KEY_LATITUDE,
                KEY_LONGITUDE };
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = { String.valueOf(recordId) };
        Cursor cursor = mDatabase.query(TABLE_NAME, columns, where, whereArgs,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Gets current unix time
     * 
     * @return current unix time
     */
    private long getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        /* Gets desired time as seconds since midnight, January 1, 1970 UTC */
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * Checks Location table for location with the same name
     * 
     * @param locationName
     *            location name for checks
     * @return true if location exist, false if location not found in database
     */
    public boolean isLocationExists(String locationName) {
        Log.message("Enter");
        boolean exists = false;
        String where = KEY_LOCATION_NAME + " = ?";
        String[] whereArgs = { locationName };
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[] { KEY_ROWID },
                where, whereArgs, null, null, null);

        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        return exists;
    }

    /**
     * Updates last_access field for current timestamp
     * 
     * @param recordId
     *            if of record in Location table
     */
    public void udateLastAccessTime(long recordId) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = { String.valueOf(recordId) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LAST_ACCESS, getTimeStamp());
        mDatabase.update(TABLE_NAME, contentValues, where, whereArgs);
    }

    /**
     * Updates location for new data
     * 
     * @param recordId
     *            id of record in Location table
     * @param locationName
     *            new location name
     * @param latitude
     *            new location latitude
     * @param longitude
     *            new location longitude
     */
    public void updateLocation(long recordId, String locationName,
            double latitude, double longitude) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = { String.valueOf(recordId) };

        ContentValues updateValues = createContentValues(locationName,
                latitude, longitude);
        mDatabase.update(TABLE_NAME, updateValues, where, whereArgs);
    }

    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }
}
