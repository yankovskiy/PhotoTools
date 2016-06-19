/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.neverdark.phototools.utils.Log;

/**
 * A helper class to manage database creation and version management.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 3;
    private static final String CREATE_LOCATIONS_QUERY = "create table locations (_id integer primary key autoincrement, location_name text not null, latitude real not null, longitude real not null, last_access integer not null, map_type integer not null, camera_zoom real not null);";
    private static final String RESERVED_QUERY = "insert into locations values (1, 'reserved', 0, 0, 0, 0, 0);";

    private static final String CREATE_USER_CAMERAS_QUERY = "create table user_cameras (_id integer primary key autoincrement, camera_name text not null, resolution_width integer not null, resolution_height integer not null, sensor_width real not null, sensor_height real not null, coc real not null, is_custom_coc integer not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.message("Enter");
        database.execSQL(CREATE_LOCATIONS_QUERY);
        database.execSQL(RESERVED_QUERY);
        database.execSQL(CREATE_USER_CAMERAS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.message("Enter");
        if (oldVersion < newVersion) {
            switch (oldVersion) {
                case 1:
                    upgrade1to2(database);
                    break;
                case 2:
                    upgrade2to3(database);
                    break;
            }

            onUpgrade(database, ++oldVersion, newVersion);
        }
    }

    private void upgrade2to3(SQLiteDatabase database) {
        database.execSQL("alter table locations add column map_type integer not null default 0;");
        database.execSQL("alter table locations add column camera_zoom real not null default 0.0;");
    }

    private void upgrade1to2(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_CAMERAS_QUERY);
    }
}
