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
package ru.neverdark.phototools.utils;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Service for determine your Geo Location
 */
public class GeoLocationService extends Service implements LocationListener {

    /**
     * Class for local service binding
     */
    public class GeoLocationBinder extends Binder {
        /**
         * Gets GeoLocationService object for manipulate from our program
         * 
         * @return GeoLocationService object
         */
        public GeoLocationService getService() {
            Log.message("Enter");
            return GeoLocationService.this;
        }
    }

    private final IBinder mBinder = new GeoLocationBinder();
    private boolean mIsGpsEnabled;
    private boolean mIsNetworkEnabled;

    private boolean mCanGetLocation;
    private double mLatitude;

    private double mLongitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private LocationManager mLocationManager;

    public GeoLocationService() {
        Log.message("Enter");
        mIsGpsEnabled = false;
        mIsNetworkEnabled = false;
        mCanGetLocation = false;
        mLatitude = 0;
        mLongitude = 0;
    }

    /**
     * Checks WIFI / GPS enabled
     * 
     * @return true if WIFI / GPS enabled or false in other case
     */
    public boolean canGetLocation() {
        return mCanGetLocation;
    }

    /**
     * Gets current latitude
     * 
     * @return current latitude or zero (0) if not determine
     */
    public double getLatitude() {
        Log.message("Enter");
        return mLatitude;
    }

    /**
     * Gets location from specified provider
     * 
     * @param provider
     *            Must be GPS_PROVIDER or NETWORK_PROVIDER
     */
    private void getLocationFromProvider(final String provider) {
        Log.message("Enter");
        Log.variable("provider", provider);
        mLocationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (mLocationManager != null) {
            Location location;

            if (provider == LocationManager.GPS_PROVIDER && mIsNetworkEnabled) {
                /* we sets coordinates from network until getting coordinates from GPS*/
                Log.message("Used network coordinates");
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                location = mLocationManager.getLastKnownLocation(provider);
            }

            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
            }
        }
    }

    /**
     * Gets current longitude
     * 
     * @return current longitude or zero (0) if not determine
     */
    public double getLongitude() {
        Log.message("Enter");
        return mLongitude;
    }

    /**
     * Inits Geo location service
     */
    private void initGeoLocationService() {
        Log.message("Enter");
        try {
            mLocationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);
            // getting GPS status
            mIsGpsEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting Network status
            mIsNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            mCanGetLocation = (mIsGpsEnabled || mIsNetworkEnabled);

            if (mCanGetLocation == true) {
                /* First. Get location from GPS if possible */
                if (mIsGpsEnabled == true) {
                    getLocationFromProvider(LocationManager.GPS_PROVIDER);
                } else { /* GPS disabled. Get location from network */
                    getLocationFromProvider(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            Log.message("Exception in initGeoLocation");
            Log.message(e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.message("Enter");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.message("Enter");
        super.onCreate();
        initGeoLocationService();
    }

    @Override
    public void onDestroy() {
        Log.message("Enter");
        stopGeoLocationService();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        Log.message("Enter");
        mLatitude = arg0.getLatitude();
        mLongitude = arg0.getLongitude();
    }

    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    /**
     * Stop using get location listener
     */
    private void stopGeoLocationService() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

}
