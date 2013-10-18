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
import android.content.Context;
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
     * Closes database
     */
    public void close() {
        Log.message("Enter");
        mDatabaseHelper.close();
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
        // TODO
        return -1;
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
        // TODO
    }

    /**
     * Deletes location by recordId
     * 
     * @param recordId
     *            recordId in Location table
     * @return true if delete success or false in othe case
     */
    public boolean deleteLocation(long recordId) {
        // TODO
        return false;
    }

    /**
     * Updates last_access field for current timestamp
     * 
     * @param recordId
     *            if of record in Location table
     */
    public void udateLastAccessTime(long recordId) {
        // TODO
    }

    /**
     * Checks Location table for location with the same name
     * 
     * @param locationName
     *            location name for checks
     * @return true if location exist, false if location not found in database
     */
    public boolean isLocationExists(String locationName) {
        // TODO
        return false;
    }
}
