/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.phototools.utils.Log;

/**
 * Class for tables "locations"
 */
public class LocationsTable {
    public static final String KEY_LAST_ACCESS = "last_access";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LOCATION_NAME = "location_name";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_MAP_TYPE = "map_type";
    public static final String KEY_CAMERA_ZOOM = "camera_zoom";
    private static final String TABLE_NAME = "locations";
    private SQLiteDatabase mDatabase;

    /**
     * Creates ContentValues for insert or update to database
     *
     * @param locationName location name
     * @param latitude     location latitude
     * @param longitude    location longitude
     * @return ContentValues object
     */
    private ContentValues createContentValues(String locationName,
                                              double latitude, double longitude,
                                              int mapType, float cameraZoom) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOCATION_NAME, locationName);
        contentValues.put(KEY_LATITUDE, latitude);
        contentValues.put(KEY_LONGITUDE, longitude);
        contentValues.put(KEY_LAST_ACCESS, getTimeStamp());
        contentValues.put(KEY_MAP_TYPE, mapType);
        contentValues.put(KEY_CAMERA_ZOOM, cameraZoom);

        return contentValues;
    }

    /**
     * Creates location in database
     *
     * @param locationName location name
     * @param latitude     location latitude
     * @param longitude    location longitude
     * @param mapType      type of the map
     * @param cameraZoom   camera zoom
     * @return recordId if success or -1 for fail
     */
    public long createLocation(String locationName, double latitude,
                               double longitude, int mapType, float cameraZoom) {
        Log.message("Enter");
        ContentValues insertValues = createContentValues(locationName,
                latitude, longitude, mapType, cameraZoom);

        return mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    /**
     * Deletes location by recordId
     *
     * @param recordId recordId in Location table
     * @return true if delete success or false in other case
     */
    public boolean deleteLocation(long recordId) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = {String.valueOf(recordId)};
        return mDatabase.delete(TABLE_NAME, where, whereArgs) > 0;
    }

    /**
     * Fetches all locations from database
     *
     * @return Cursor contains all records from Locations table
     */
    public Cursor fetchAllLocations() {
        Log.message("Enter");
        String[] columns = {KEY_ROWID, KEY_LOCATION_NAME, KEY_LATITUDE,
                KEY_LONGITUDE, KEY_MAP_TYPE, KEY_CAMERA_ZOOM};
        String where = KEY_ROWID + " > 1";
        return mDatabase.query(TABLE_NAME, columns, where, null, null, null,
                KEY_LAST_ACCESS + " DESC");
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
     * @param locationName location name for checks
     * @return true if location exist, false if location not found in database
     */
    public boolean isLocationExists(String locationName) {
        Log.message("Enter");
        boolean exists = false;
        String where = KEY_LOCATION_NAME + " = ?";
        String[] whereArgs = {locationName};
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{KEY_ROWID},
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
     * @param recordId if of record in Location table
     */
    public void udateLastAccessTime(long recordId) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = {String.valueOf(recordId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LAST_ACCESS, getTimeStamp());
        mDatabase.update(TABLE_NAME, contentValues, where, whereArgs);
    }

    /**
     * Updates location for new data
     *
     * @param recordId     id of record in Location table
     * @param locationName new location name
     * @param latitude     new location latitude
     * @param longitude    new location longitude
     * @param mapType      type of the map
     * @param cameraZoom   camera zoom
     */
    public void updateLocation(long recordId, String locationName,
                               double latitude, double longitude, int mapType, float cameraZoom) {
        Log.message("Enter");
        String where = KEY_ROWID + " = ?";
        String[] whereArgs = {String.valueOf(recordId)};

        ContentValues updateValues = createContentValues(locationName,
                latitude, longitude, mapType, cameraZoom);
        mDatabase.update(TABLE_NAME, updateValues, where, whereArgs);
    }

    /**
     * Sets database object for management
     *
     * @param database database object for management
     */
    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }
}
