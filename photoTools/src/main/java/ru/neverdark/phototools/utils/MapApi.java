package ru.neverdark.phototools.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * API for Google Map
 */

public class MapApi implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private static final String MAP_LATITUDE = "map_latitude";
    private static final String MAP_LONGITUDE = "map_longitude";
    private static final String MAP_ZOOM = "map_zoom";
    private static final String MAP_TYPE = "map_type";
    private static final float FIND_ZOOM = 14f;
    private static final float MAP_CAMERA_ZOOM = 17.0f;
    private static final String MAP_PREFS = "map_prefs";
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Context mContext;
    private OnMapApiListener mCallback;

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
