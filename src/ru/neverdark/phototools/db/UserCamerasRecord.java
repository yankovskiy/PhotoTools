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

/**
 * Stores one record from user_cameras
 */
public class UserCamerasRecord {
    private String mCameraName;
    private float mCoc;
    boolean mIsCustomCoc;
    long mRecordId;
    private int mResolutionHeight;
    private int mResolutionWidth;
    private float mSensorHeight;
    private float mSensorWidth;

    /**
     * Gets camera name
     * 
     * @return camera name
     */
    public String getCameraName() {
        return mCameraName;
    }

    /**
     * Gets circle of confusion
     * 
     * @return circle of confusion
     */
    public float getCoc() {
        return mCoc;
    }

    /**
     * Gets record id
     * 
     * @return record id
     */
    public long getRecordId() {
        return mRecordId;
    }

    /**
     * Gets maximum height resolution
     * 
     * @return maximum height resolution
     */
    public int getResolutionHeight() {
        return mResolutionHeight;
    }

    /**
     * Gets maximum width resolution
     * 
     * @return maximum width resolution
     */
    public int getResolutionWidth() {
        return mResolutionWidth;
    }

    /**
     * Gets sensor height
     * 
     * @return sensor height
     */
    public float getSensorHeight() {
        return mSensorHeight;
    }

    /**
     * Gets sensor width
     * 
     * @return sensor width
     */
    public float getSensorWidth() {
        return mSensorWidth;
    }

    /**
     * Checks is need use custom coc
     * 
     * @return true if need use custom coc
     */
    public boolean isCustomCoc() {
        return mIsCustomCoc;
    }

    /**
     * Sets camera name
     * 
     * @param cameraName
     *            camera name
     */
    public void setCameraName(String cameraName) {
        this.mCameraName = cameraName;
    }

    /**
     * Sets circle of confusion
     * 
     * @param coc
     *            circle of confusion
     */
    public void setCoc(float coc) {
        this.mCoc = coc;
    }

    /**
     * Sets is need use custom coc
     * 
     * @param isCustomCoc
     *            true for use custom coc
     */
    public void setIsCustomCoc(boolean isCustomCoc) {
        this.mIsCustomCoc = isCustomCoc;
    }

    /**
     * Sets record id
     * 
     * @param recordId
     *            record id
     */
    public void setRecordId(long recordId) {
        this.mRecordId = recordId;
    }

    /**
     * Sets maximum height resolution
     * 
     * @param resolutionHeight
     *            maximum height resolution
     */
    public void setResolutionHeight(int resolutionHeight) {
        this.mResolutionHeight = resolutionHeight;
    }

    /**
     * Sets maximum width resolution
     * 
     * @param resolutionWidth
     *            maximum width resolution
     */
    public void setResolutionWidth(int resolutionWidth) {
        this.mResolutionWidth = resolutionWidth;
    }

    /**
     * Sets sensor height
     * 
     * @param sensorHeight
     *            sensor height
     */
    public void setSensorHeight(float sensorHeight) {
        this.mSensorHeight = sensorHeight;
    }

    /**
     * Sets sensor width
     * 
     * @param sensorWidth
     *            sensor width
     */
    public void setSensorWidth(float sensorWidth) {
        this.mSensorWidth = sensorWidth;
    }
}
