/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
public class DbAdapter {
    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    private LocationsTable mLocations;
    private UserCamerasTable mUserCameras;

    public DbAdapter(Context context) {
        Log.message("Enter");
        mContext = context;
        mLocations = new LocationsTable();
        mUserCameras = new UserCamerasTable();
    }

    /**
     * Closes database
     */
    public void close() {
        Log.message("Enter");
        mDatabaseHelper.close();
    }

    /**
     * Gets object for locations table
     * 
     * @return
     */
    public LocationsTable getLocations() {
        return mLocations;
    }

    /**
     * Gets object for user_cameras table
     * @return
     */
    public UserCamerasTable getUserCameras() {
        return mUserCameras;
    }
    
    /**
     * Returns true if the database is currently open.
     * 
     * @return true if the database is currently open.
     */
    public boolean isOpen() {
        return mDatabase.isOpen();
    }

    /**
     * Opens database
     * 
     * @return LocationDbAdapter object for feature operations with table
     * @throws SQLException
     */
    public DbAdapter open() throws SQLException {
        Log.message("Enter");
        mDatabaseHelper = new DatabaseHelper(mContext);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        mLocations.setDatabase(mDatabase);
        mUserCameras.setDatabase(mDatabase);
        return this;
    }

}
