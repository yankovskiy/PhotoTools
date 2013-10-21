/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.db;

import ru.neverdark.phototools.utils.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * A class for working with Locations table
 * 
 */
public class LocationsDbAdapter {
    /* Locations table fields */
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOCATION_NAME = "location_name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LAST_ACCESS = "last_access";
    private static final String TABLE_NAME = "locations";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    public LocationsDbAdapter(Context context) {
        Log.message("Enter");
        mContext = context;
    }

    /**
     * Closes database
     */
    public void close() {
        Log.message("Enter");
        mDatabaseHelper.close();
    }

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
    private ContentValues CreateContentValues(String locationName,
            double latitude, double longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOCATION_NAME, locationName);
        contentValues.put(KEY_LATITUDE, latitude);
        contentValues.put(KEY_LONGITUDE, longitude);
        contentValues.put(KEY_LAST_ACCESS, "date('now')");

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
        ContentValues insertValues = CreateContentValues(locationName,
                latitude, longitude);

        return mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    /**
     * Deletes location by recordId
     * 
     * @param recordId
     *            recordId in Location table
     * @return true if delete success or false in othe case
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
        String[] columns = { KEY_ROWID, KEY_LOCATION_NAME, KEY_LATITUDE, KEY_LONGITUDE };
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
        String[] columns = { KEY_ROWID, KEY_LOCATION_NAME, KEY_LATITUDE,
                KEY_LONGITUDE };
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = { String.valueOf(recordId) };
        Cursor cursor = mDatabase.query(true, TABLE_NAME, columns, where,
                whereArgs, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
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
        Cursor cursor = mDatabase.query(true, TABLE_NAME,
                new String[] { KEY_ROWID }, where, whereArgs, null, null, null,
                null, null);

        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        return exists;
    }

    /**
     * Opens database
     * 
     * @return LocationDbAdapter object for feature operations with table
     * @throws SQLException
     */
    public LocationsDbAdapter open() throws SQLException {
        Log.message("Enter");
        mDatabaseHelper = new DatabaseHelper(mContext);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        return this;
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
        contentValues.put(KEY_LAST_ACCESS, "datetime('now')");
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

        ContentValues updateValues = CreateContentValues(locationName,
                latitude, longitude);
        mDatabase.update(TABLE_NAME, updateValues, where, whereArgs);
    }
}
