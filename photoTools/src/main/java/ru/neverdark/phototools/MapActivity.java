/**
 * ****************************************************************************
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
 * ****************************************************************************
 */
package ru.neverdark.phototools;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.fragments.ConfirmCreateDialog;
import ru.neverdark.phototools.fragments.ConfirmCreateDialog.OnConfirmDialogHandler;
import ru.neverdark.phototools.fragments.DateTimeDialog;
import ru.neverdark.phototools.fragments.ShowMessageDialog;
import ru.neverdark.phototools.utils.AsyncGeoCoder;
import ru.neverdark.phototools.utils.AsyncGeoCoder.OnGeoCoderListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;

public class MapActivity extends AppCompatActivity implements OnMapLongClickListener, DateTimeDialog.OnDateTimeChange {

    private static final String MAP_LATITUDE = "map_latitude";
    private static final String MAP_LONGITUDE = "map_longitude";
    private static final String MAP_ZOOM = "map_zoom";
    private static final String MAP_TYPE = "map_type";
    private static final String SHOW_HINT = "map_show_hint";
    MapActivity mCurrentActivity;
    private GoogleMap mMap;
    private MenuItem mMenuItemDone;
    private Marker mMarker;
    private LatLng mMarkerPosition;
    private int mAction;
    private long mRecordId;
    private String mLocationName;
    private Context mContext;
    private MenuItem mMenuItemSearch;
    private boolean mIsVisible = false;
    private static final int MAP_ACCESS_FINE_LOCATION = 1;
    private Calendar mCalendar;

    /**
     * Inits process for searching coordinates by address
     *
     * @param query address for searching in GeoCoder
     */
    public void initSearchProcess(String query) {
        AsyncGeoCoder geo = new AsyncGeoCoder(mContext);
        geo.setSearchString(query);
        geo.setCallback(new GeoCoderListener());
        geo.execute();
    }

    private void showErrorDialog(String errorMessage) {
        ShowMessageDialog dialog = ShowMessageDialog.getInstance(mContext);
        dialog.setMessages(R.string.error, errorMessage);
        dialog.show(getSupportFragmentManager(), ShowMessageDialog.DIALOG_TAG);
    }

    private void showErrorDialog(int resourceId) {
        ShowMessageDialog dialog = ShowMessageDialog.getInstance(mContext);
        dialog.setMessages(R.string.error, resourceId);
        dialog.show(getSupportFragmentManager(), ShowMessageDialog.DIALOG_TAG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MAP_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    /**
     * Inits Google Map
     */
    private void initMap() {
        Log.message("Enter");
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        mMap.setOnMapLongClickListener(mCurrentActivity);
                        /* gets current coord if have */
                        Intent intent = getIntent();
                        Double latitude = intent.getDoubleExtra(Constants.LOCATION_LATITUDE, 0);
                        Double longitude = intent.getDoubleExtra(Constants.LOCATION_LONGITUDE, 0);

                        /* gets action */
                        mAction = intent.getByteExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_ADD);

                        float zoom = Constants.MAP_CAMERA_ZOOM;
                        int mapType = GoogleMap.MAP_TYPE_NORMAL;
                        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

                        if (mAction == Constants.LOCATION_ACTION_EDIT) {
                            mRecordId = intent.getLongExtra(Constants.LOCATION_RECORD_ID,
                                    Constants.LOCATION_POINT_ON_MAP_CHOICE);
                            mLocationName = intent.getStringExtra(Constants.LOCATION_NAME);
                            mMarkerPosition = new LatLng(latitude, longitude);
                            setMarkerAndRecalc();

                            if (Constants.PAID) {
                                zoom = intent.getFloatExtra(Constants.LOCATION_CAMERA_ZOOM, Constants.MAP_CAMERA_ZOOM);
                                mapType = intent.getIntExtra(Constants.LOCATION_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);

                                Log.variable("zoom", String.valueOf(zoom));
                                Log.variable("mapType", String.valueOf(mapType));

                                if (zoom == 0) {
                                    zoom = Constants.MAP_CAMERA_ZOOM;
                                }
                                if (mapType == 0) {
                                    mapType = GoogleMap.MAP_TYPE_NORMAL;
                                }
                            }
                        } else {
                            if (Constants.PAID) {
                                // Если нет сохраненных данных используем данные с GPS

                                latitude = (double) prefs.getFloat(MAP_LATITUDE, latitude.floatValue());
                                longitude = (double) prefs.getFloat(MAP_LONGITUDE, longitude.floatValue());
                                zoom = prefs.getFloat(MAP_ZOOM, zoom);
                                mapType = prefs.getInt(MAP_TYPE, mapType);
                            }
                        }

                        mMap.setMapType(mapType);
                        /* checks for coordinates was received */
                        if ((latitude != 0) || (longitude != 0)) {
                            CameraPosition currentPosition = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude)).zoom(zoom)
                                    .build();
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
                        }

                        if (prefs.getBoolean(SHOW_HINT, true)) {
                            final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),
                                    R.string.long_tap_for_choose_location,
                                    Snackbar.LENGTH_INDEFINITE);
                            snack.setAction(R.string.hide, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(SHOW_HINT, false);
                                    editor.apply();
                                    snack.dismiss();
                                }
                            });
                            snack.show();
                        }

                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        } else {
                            requirePerms();
                        }
                    }
                });
    }

    private void requirePerms() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onPause() {
        if (Constants.PAID) {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(MAP_TYPE, mMap.getMapType());
            editor.putFloat(MAP_ZOOM, mMap.getCameraPosition().zoom);
            editor.putFloat(MAP_LATITUDE, (float) mMap.getCameraPosition().target.latitude);
            editor.putFloat(MAP_LONGITUDE, (float) mMap.getCameraPosition().target.longitude);
            editor.apply();
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        setTheme(R.style.MapTheme);
        setContentView(R.layout.map_activity);
        mCurrentActivity = this;
        mCalendar = Calendar.getInstance();
        initMap();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.enter();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_actions, menu);
        mMenuItemSearch = menu.findItem(R.id.map_action_search);
        mMenuItemDone = menu.findItem(R.id.map_button_done);
        mMenuItemDone.setVisible(mIsVisible);

        if (Constants.PAID) {
            SearchView mSearchView = new SearchView(mContext);
            mSearchView.setQueryHint(getString(R.string.search_hint));
            mSearchView.setOnQueryTextListener(new QueryTextListener());
            mMenuItemSearch.setActionView(mSearchView);

        } else {
            mMenuItemSearch.setActionView(null);
        }

        return true;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.message("Enter");
        mMarkerPosition = point;
        setMarkerAndRecalc();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_button_done:
                showConfirmDialog();
                return true;
            case R.id.map_type_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;

            case R.id.map_type_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.map_type_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.map_type_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.map_action_search:
                if (!Constants.PAID) {
                    Common.gotoDonate(mContext);
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.map_button_changeTime:
                showDateTimeDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows dialog for change date and time
     */
    private void showDateTimeDialog() {
        DateTimeDialog dialog = DateTimeDialog.getInstance(mCalendar, this);
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * Saves data to the database
     *
     * @param locationName location name for save into database
     */
    private void saveDataToDatabase(String locationName) {
        Log.message("Enter");

        DbAdapter dbAdapter = new DbAdapter(getApplicationContext()).open();

        if (mAction == Constants.LOCATION_ACTION_ADD) {
            mRecordId = dbAdapter.getLocations().createLocation(locationName,
                    mMarkerPosition.latitude, mMarkerPosition.longitude, mMap.getMapType(), mMap.getCameraPosition().zoom);
        } else if (mAction == Constants.LOCATION_ACTION_EDIT) {
            dbAdapter.getLocations().updateLocation(mRecordId, locationName,
                    mMarkerPosition.latitude, mMarkerPosition.longitude, mMap.getMapType(), mMap.getCameraPosition().zoom);
        }

        dbAdapter.close();
    }

    /**
     * Sets visible property for Done button
     *
     * @param isVisible true for Done button visible, false for invisible
     */
    private void setButtonVisible(final boolean isVisible) {
        Log.message("Enter");
        if (mMenuItemDone != null) {
            mMenuItemDone.setVisible(isVisible);
        }
        mIsVisible = isVisible;

    }

    /**
     * Sets marker to new position and recalculate procedure
     */
    private void setMarkerAndRecalc() {
        Log.message("Enter");
        setMarker();
        recalculate();
        setButtonVisible(true);
    }

    /**
     * Sets marker to the long tap position If marker already exists - remove
     * old marker and set new marker in new position
     */
    private void setMarker() {
    /* If we have marker - destroy */
        if (mMarker != null) {
            mMap.clear();
        }
        mMarker = mMap.addMarker(new MarkerOptions().position(mMarkerPosition));
    }

    /**
     * Shows confirmation dialog
     */
    private void showConfirmDialog() {
        Log.message("Enter");
        ConfirmCreateDialog confirmDialog = ConfirmCreateDialog.getInstance(mContext);
        confirmDialog.setCallback(new ConfirmCreateDialogHandler());
        confirmDialog.setAction(mAction);
        confirmDialog.setLocationName(mLocationName);
        confirmDialog.show(getSupportFragmentManager(), ConfirmCreateDialog.DIALOG_ID);
    }

    @Override
    public void onDateTimeChange(Calendar calendar) {
        mCalendar = calendar;

        /* if marker already setted run recalclate procedure */
        if (mMarker != null) {
            recalculate();
        }
    }

    /**
     * Recalculates all data
     */
    private void recalculate() {
        // TODO написать код для пересчета
    }

    private class ConfirmCreateDialogHandler implements OnConfirmDialogHandler, OnCallback {

        @Override
        public void handleConfirmDialog(String locationName) {
            Log.message("Enter");

            Intent intent = new Intent();
            intent.putExtra(Constants.LOCATION_LATITUDE, mMarkerPosition.latitude);
            intent.putExtra(Constants.LOCATION_LONGITUDE, mMarkerPosition.longitude);

            if (locationName != null) {
                saveDataToDatabase(locationName);
                intent.putExtra(Constants.LOCATION_NAME, locationName);
            } else {
                mRecordId = Constants.LOCATION_POINT_ON_MAP_CHOICE;
            }

            intent.putExtra(Constants.LOCATION_RECORD_ID, mRecordId);

            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private class GeoCoderListener implements OnGeoCoderListener {

        @Override
        public void onGetResultFail() {
            showErrorDialog(R.string.error_geoCoderNotAvailable);
        }

        @Override
        public void onGetResultSuccess(LatLng coordinates, String searchString) {
            if (coordinates != null) {

                float zoom = mMap.getCameraPosition().zoom;

                // move camera to saved position
                CameraPosition currentPosition = new CameraPosition.Builder().target(coordinates)
                        .zoom(zoom).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
            } else {
                String errorMessage = String.format(getString(R.string.error_notFound),
                        searchString);
                showErrorDialog(errorMessage);
            }
        }

    }

    private class QueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            MenuItemCompat.collapseActionView(mMenuItemSearch);
            initSearchProcess(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

    }

}
