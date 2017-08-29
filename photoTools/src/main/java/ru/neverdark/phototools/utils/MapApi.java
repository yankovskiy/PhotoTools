package ru.neverdark.phototools.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.*;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ru.neverdark.phototools.R;

/**
 * API for Google Map
 */

public class MapApi implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {
    private static final String MAP_LATITUDE = "map_latitude";
    private static final String MAP_LONGITUDE = "map_longitude";
    private static final String MAP_ZOOM = "map_zoom";
    private static final String MAP_TYPE = "map_type";
    private static final float FIND_ZOOM = 14f;
    private static final float MAP_CAMERA_ZOOM = 17.0f;
    private static final float MINIMUM_ZOOM = 5.0f;
    private static final String MAP_PREFS = "map_prefs";
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Context mContext;
    private OnMapApiListener mCallback;
    private Polyline mAzimuthLine;
    private Polyline mSunsetAzimuthLine;
    private Polyline mSunriseAzimuthLine;
    private double mAltitude;
    private double mAzimuth;
    private double mSunsetAzimuth;
    private double mSunriseAzimuth;
    private float mOldZoom;

    private static final String TAG = "MapApi";

    public void setAzimuthData(double altitude, double azimuth, double sunsetAzimuth, double sunriseAzimuth) {
        Log.v(TAG, "setAzimuthData altitude: " + altitude);
        Log.v(TAG, "setAzimuthData azimuth : " + azimuth);
        Log.v(TAG, "setAzimuthData sunsetAzimuth: " + sunsetAzimuth);
        Log.v(TAG, "setAzimuthData sunriseAzimuth: " + sunriseAzimuth);

        mAltitude = altitude;
        mAzimuth = azimuth;
        mSunsetAzimuth = sunsetAzimuth;
        mSunriseAzimuth = sunriseAzimuth;
    }

    public void drawAzimuth() {
        if (getCameraZoom() >= MINIMUM_ZOOM) {
            LatLng pos = getMarkerPosition();
            double size = calculateLineLength();

            if (mAzimuthLine != null) {
                mAzimuthLine.remove();
            }

            if (mSunsetAzimuthLine != null) {
                mSunsetAzimuthLine.remove();
            }

            if (mSunriseAzimuthLine != null) {
                mSunriseAzimuthLine.remove();
            }

            if (mAltitude > 0) {
                PolylineOptions options = polylineDraw(
                        pos,
                        mAzimuth,
                        size,
                        Settings.getSunLineColor(mContext),
                        5
                );
                mAzimuthLine = mGoogleMap.addPolyline(options);
            }

            if (Settings.isSunsetShow(mContext)) {
                PolylineOptions lsunsetAzimuth = polylineDraw(
                        pos,
                        mSunsetAzimuth,
                        size,
                        Settings.getSunsetLineColor(mContext),
                        5
                );
                mSunsetAzimuthLine = mGoogleMap.addPolyline(lsunsetAzimuth);
            }

            if (Settings.isSunriseShow(mContext)) {
                PolylineOptions lsunriseAzimuth = polylineDraw(
                        pos,
                        mSunriseAzimuth,
                        size,
                        Settings.getSunriseLineColor(mContext),
                        5
                );

                mSunriseAzimuthLine = mGoogleMap.addPolyline(lsunriseAzimuth);
            }
        } else {
            Common.showMessage(mContext, R.string.error_zoomToSmall);
        }
    }

    /**
     * Creates polyline object for drawing azimuth
     *
     * @param location start point
     * @param azimuth  solar azimuth angle
     * @param size     line length for drawing
     * @param color    line color
     * @param width    line width
     * @return polyline object for drawing azimuth
     */
    private PolylineOptions polylineDraw(LatLng location, double azimuth, double size, int color,
                                         int width) {
        PolylineOptions options = new PolylineOptions();
        options.add(location);
        options.add(getDestLatLng(location, azimuth, size));
        options.width(width);
        options.color(color);

        return options;
    }

    private LatLng getDestLatLng(LatLng location, double azimuth,
                                 double distance) {
        double lat2 = location.latitude + distance * Math.cos(azimuth);
        double lng2 = location.longitude + distance * Math.sin(azimuth);

        return new LatLng(lat2, lng2);
    }

    private double calculateLineLength() {
        double size = mGoogleMap.getProjection().getVisibleRegion().farLeft.longitude -
                mGoogleMap.getProjection().getVisibleRegion().nearRight.longitude;
        return Math.abs(size);
    }

    @Override
    public void onCameraIdle() {
        if (mMarker != null) {
            if (mOldZoom != getCameraZoom()) {
                mOldZoom = getCameraZoom();
                drawAzimuth();
            }
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        mOldZoom = getCameraZoom();
    }

    public interface OnMapApiListener {
        void onMapReady();
        void onMapLongClick(LatLng latLng);
    }

    public void setMapType(int mapType) {
        mGoogleMap.setMapType(mapType);
    }

    public int getMapType() {
        return mGoogleMap.getMapType();
    }

    public LatLng getMarkerPosition() {
        return mMarker.getPosition();
    }

    public static MapApi getInstance(Context context, OnMapApiListener callback) {
        MapApi api = new MapApi();
        api.mContext = context;
        api.mCallback = callback;
        return api;
    }

    private void clearMap() {
        if (mMarker != null) {
            mGoogleMap.clear();
            mMarker = null;
        }
    }

    public float getCameraZoom() {
        return mGoogleMap.getCameraPosition().zoom;
    }

    public void moveCamera(LatLng coordinates) {
        float zoom = FIND_ZOOM;
        if (getCameraZoom() < FIND_ZOOM) {
            zoom = getCameraZoom();
        }
        moveCamera(coordinates, zoom);
    }

    public void moveCamera(LatLng coordinates, float zoom) {
        moveCamera(coordinates, zoom, true);
    }

    public void moveCamera(LatLng coordinates, float zoom, boolean isAnimated) {
        clearMap();
        CameraPosition position = new CameraPosition.Builder().target(coordinates).zoom(zoom).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
        if (isAnimated) {
            mGoogleMap.animateCamera(update);
        } else {
            mGoogleMap.moveCamera(update);
        }
    }

    public boolean isHaveMarker() {
        return mMarker != null;
    }

    public void setMarker(LatLng point) {
        if (mMarker != null) {
            clearMap();
        }
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(point));
    }

    public void saveState() {
        if (Constants.PAID) {
            SharedPreferences prefs = mContext.getSharedPreferences(MAP_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(MAP_TYPE, getMapType());
            editor.putFloat(MAP_ZOOM, getCameraZoom());
            editor.putFloat(MAP_LATITUDE, (float) getCameraTarget().latitude);
            editor.putFloat(MAP_LONGITUDE, (float) getCameraTarget().longitude);
            editor.apply();
        }
    }

    public LatLng getCameraTarget() {
        return mGoogleMap.getCameraPosition().target;
    }

    @SuppressWarnings("MissingPermission")
    public void setMyLocationEnabled(boolean isEnable) {
        mGoogleMap.setMyLocationEnabled(isEnable);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mCallback.onMapReady();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mCallback.onMapLongClick(latLng);
    }

    public void prepareMap(Intent intent) {
        double latitude = intent.getDoubleExtra(Constants.LOCATION_LATITUDE, 0);
        double longitude = intent.getDoubleExtra(Constants.LOCATION_LONGITUDE, 0);
        float zoom = MAP_CAMERA_ZOOM;
        int mapType = GoogleMap.MAP_TYPE_NORMAL;

        if (intent.getByteExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_ADD) == Constants.LOCATION_ACTION_EDIT) {
            if (Constants.PAID) {
                zoom = intent.getFloatExtra(Constants.LOCATION_CAMERA_ZOOM, zoom);
                mapType = intent.getIntExtra(Constants.LOCATION_MAP_TYPE, mapType);
            }
        } else {
            if (Constants.PAID) {
                SharedPreferences prefs = mContext.getSharedPreferences(MAP_PREFS, Context.MODE_PRIVATE);
                latitude = (double) prefs.getFloat(MAP_LATITUDE, (float) latitude);
                longitude = (double) prefs.getFloat(MAP_LONGITUDE, (float) longitude);
                zoom = prefs.getFloat(MAP_ZOOM, zoom);
                mapType = prefs.getInt(MAP_TYPE, mapType);
            }
        }

        setMapType(mapType);

        if ((latitude != 0) || (longitude != 0)) {
            moveCamera(new LatLng(latitude, longitude), zoom, false);
        }
    }
}
