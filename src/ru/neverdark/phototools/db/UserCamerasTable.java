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

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class for user_cameras table
 */
public class UserCamerasTable {
    public static final String KEY_CAMERA_NAME = "camera_name";
    public static final String KEY_COC = "coc";
    public static final String KEY_IS_CUSTOM_COC = "is_custom_coc";
    public static final String KEY_RESOLUTION_HEIGHT = "resolution_height";
    public static final String KEY_RESOLUTION_WIDTH = "resolution_width";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SENSOR_HEIGHT = "sensor_height";
    public static final String KEY_SENSOR_WIDTH = "sensor_width";
    public static final String TABLE_NAME = "user_cameras";
    private SQLiteDatabase mDatabase;

    /**
     * Creates camera in database
     * 
     * @param cameraName
     *            name of the camera
     * @param resolutionWidth
     *            maximum resolution width
     * @param resolutionHeight
     *            maximum resolution height
     * @param sensorWidth
     *            sensor width
     * @param sensorHeight
     *            sensor height
     * @param coc
     *            circle of confusion
     * @param isCustomCoc
     *            true for use circle of confusion from database (no calc)
     */
    public void createCamera(String cameraName, int resolutionWidth,
            int resolutionHeight, float sensorWidth, float sensorHeight,
            float coc, boolean isCustomCoc) {
        ContentValues insertValues = createContentValues(cameraName,
                resolutionWidth, resolutionHeight, sensorWidth, sensorHeight,
                coc, isCustomCoc);

        mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    /**
     * Creates ContentValues for insert or update to database
     * 
     * @param cameraName
     *            name of the camera
     * @param resolutionWidth
     *            maximum resolution width
     * @param resolutionHeight
     *            maximum resolution height
     * @param sensorWidth
     *            sensor width
     * @param sensorHeight
     *            sensor height
     * @param coc
     *            circle of confusion
     * @param isCustomCoc
     *            true for use circle of confusion from database (no calc)
     */
    private ContentValues createContentValues(String cameraName,
            int resolutionWidth, int resolutionHeight, float sensorWidth,
            float sensorHeight, float coc, boolean isCustomCoc) {
        ContentValues values = new ContentValues();
        values.put(KEY_CAMERA_NAME, cameraName);
        values.put(KEY_RESOLUTION_WIDTH, resolutionWidth);
        values.put(KEY_RESOLUTION_HEIGHT, resolutionHeight);
        values.put(KEY_SENSOR_WIDTH, sensorWidth);
        values.put(KEY_SENSOR_HEIGHT, sensorHeight);
        values.put(KEY_COC, coc);
        values.put(KEY_IS_CUSTOM_COC, isCustomCoc);

        return values;
    }

    /**
     * Deletes camera from database
     * 
     * @param recordId
     *            record id for delete
     */
    public void deleteCamera(long recordId) {
        String where = KEY_ROWID.concat(" = ?");
        String[] whereArgs = { String.valueOf(recordId) };
        mDatabase.delete(TABLE_NAME, where, whereArgs);
    }

    /**
     * Fetches all record from user_cameras table
     * 
     * @param list
     *            list for save data
     */
    public void fetchAllCameras(final List<UserCamerasRecord> list) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null,
                null, null);

        if (cursor.getCount() > 0) {
            list.clear();

            int id = cursor.getColumnIndex(KEY_ROWID);
            int cameraName = cursor.getColumnIndex(KEY_CAMERA_NAME);
            int coc = cursor.getColumnIndex(KEY_COC);
            int isCustomCoc = cursor.getColumnIndex(KEY_IS_CUSTOM_COC);
            int resolutionWidth = cursor.getColumnIndex(KEY_RESOLUTION_WIDTH);
            int resolutionHeight = cursor.getColumnIndex(KEY_RESOLUTION_HEIGHT);
            int sensorWidth = cursor.getColumnIndex(KEY_SENSOR_WIDTH);
            int sensorHeight = cursor.getColumnIndex(KEY_SENSOR_HEIGHT);

            while (cursor.moveToNext()) {
                UserCamerasRecord record = new UserCamerasRecord();
                record.setRecordId(cursor.getLong(id));
                record.setCameraName(cursor.getString(cameraName));
                record.setCoc(cursor.getFloat(coc));
                record.setIsCustomCoc(cursor.getInt(isCustomCoc) > 0);
                record.setResolutionHeight(cursor.getInt(resolutionHeight));
                record.setResolutionWidth(cursor.getInt(resolutionWidth));
                record.setSensorHeight(cursor.getFloat(sensorHeight));
                record.setSensorWidth(cursor.getFloat(sensorWidth));
                list.add(record);
            }
        }
        cursor.close();
    }

    /**
     * Checks table for exist camera
     * 
     * @param cameraName
     *            camera name for check
     * @return true if record exist
     */
    public boolean isCameraExist(String cameraName) {
        boolean exists = false;
        String where = KEY_CAMERA_NAME.concat(" = ?");
        String[] whereArgs = { cameraName };
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[] { KEY_ROWID },
                where, whereArgs, null, null, null);

        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        return exists;
    }

    /**
     * Sets database object for operations
     * 
     * @param database
     *            database object
     */
    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }

    /**
     * Updates camera in database
     * 
     * @param recordId
     *            record id for update
     * @param cameraName
     *            name of the camera
     * @param resolutionWidth
     *            maximum resolution width
     * @param resolutionHeight
     *            maximum resolution height
     * @param sensorWidth
     *            sensor width
     * @param sensorHeight
     *            sensor height
     * @param coc
     *            circle of confusion
     * @param isCustomCoc
     *            true for use circle of confusion from database (no calc)
     */
    public void updateCamera(long recordId, String cameraName,
            int resolutionWidth, int resolutionHeight, float sensorWidth,
            float sensorHeight, float coc, boolean isCustomCoc) {
        String where = KEY_ROWID.concat(" = ?");
        String[] whereArgs = { String.valueOf(recordId) };

        ContentValues updateValues = createContentValues(cameraName,
                resolutionWidth, resolutionHeight, sensorWidth, sensorHeight,
                coc, isCustomCoc);
        mDatabase.update(TABLE_NAME, updateValues, where, whereArgs);
    }
}
