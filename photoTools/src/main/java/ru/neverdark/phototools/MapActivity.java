/*
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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.phototools.async.AsyncCalculator;
import ru.neverdark.phototools.async.AsyncGeoCoder;
import ru.neverdark.phototools.async.AsyncGeoCoder.OnGeoCoderListener;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.fragments.ConfirmCreateDialog;
import ru.neverdark.phototools.fragments.ConfirmCreateDialog.OnConfirmDialogHandler;
import ru.neverdark.phototools.fragments.DateTimeDialog;
import ru.neverdark.phototools.fragments.ShowMessageDialog;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.MapApi;
import ru.neverdark.sunmooncalc.SunriseSunsetCalculator;

public class MapActivity extends AppCompatActivity implements MapApi.OnMapApiListener, DateTimeDialog.OnDateTimeChange {
    private static final String SHOW_HINT = "map_show_hint";
    private static final int MAP_ACCESS_FINE_LOCATION = 1;
    //private GoogleMap mMap;
    private MenuItem mMenuItemDone;
    //private Marker mMarker;
    private int mAction;
    private long mRecordId;
    private String mLocationName;
    private Context mContext;
    private MenuItem mMenuItemSearch;
    private boolean mIsVisible = false;
    private Calendar mCalendar;
    private MapApi mMapApi;
    private TimeZone mTimeZone;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAP_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationButton();
                }
                break;
        }
    }

    /**
     * Inits Google Map
     */
    private void initMap() {
        Log.message("Enter");
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(mMapApi);
    }

    private void enableMyLocationButton() {
        if (checkAndRequirePermission(Manifest.permission.ACCESS_FINE_LOCATION, MAP_ACCESS_FINE_LOCATION)) {
            mMapApi.setMyLocationEnabled(true);
        }
    }

    private boolean checkAndRequirePermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
    }

    @Override
    public void onPause() {
        mMapApi.saveState();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        setTheme(R.style.MapTheme);
        setContentView(R.layout.map_activity);
        mContext = this;
        mMapApi = MapApi.getInstance(mContext, this);
        initMap();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
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
    public void onMapReady() {
        Intent intent = getIntent();
        mMapApi.prepareMap(intent);

        mAction = intent.getByteExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_ADD);
        mTimeZone = (TimeZone) intent.getExtras().getSerializable(Constants.TIMEZONE);
        mCalendar = (Calendar) intent.getExtras().getSerializable(Constants.CALENDAR);
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        if (mAction == Constants.LOCATION_ACTION_EDIT) {
            mRecordId = intent.getLongExtra(Constants.LOCATION_RECORD_ID,
                    Constants.LOCATION_POINT_ON_MAP_CHOICE);
            mLocationName = intent.getStringExtra(Constants.LOCATION_NAME);
            setMarkerAndRecalc(mMapApi.getCameraTarget());
        }

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
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

        enableMyLocationButton();
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.message("Enter");
        setMarkerAndRecalc(point);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_button_done:
                showConfirmDialog();
                return true;
            case R.id.map_type_map:
                mMapApi.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.map_type_terrain:
                mMapApi.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.map_type_satellite:
                mMapApi.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.map_type_hybrid:
                mMapApi.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
                    mMapApi.getMarkerPosition().latitude,
                    mMapApi.getMarkerPosition().longitude,
                    mMapApi.getMapType(),
                    mMapApi.getCameraZoom());
        } else if (mAction == Constants.LOCATION_ACTION_EDIT) {
            dbAdapter.getLocations().updateLocation(mRecordId, locationName,
                    mMapApi.getMarkerPosition().latitude,
                    mMapApi.getMarkerPosition().longitude,
                    mMapApi.getMapType(),
                    mMapApi.getCameraZoom());
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
     *
     * @param point point on the map
     */
    private void setMarkerAndRecalc(LatLng point) {
        Log.message("Enter");
        mMapApi.setMarker(point);
        recalculate();
        setButtonVisible(true);
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
        if (mMapApi.isHaveMarker()) {
            recalculate();
        }
    }

    /**
     * Recalculates all data
     */
    private void recalculate() {
        AsyncCalculator calc = new AsyncCalculator(mCalendar, mMapApi.getMarkerPosition(), mTimeZone, new AsyncCalculator.OnCalculatorListener() {
            @Override
            public void onGetResultFail() {

            }

            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute() {

            }

            @Override
            public void onGetResultSuccess(SunriseSunsetCalculator calculator) {

            }
        });
        calc.execute();
    }

    private class ConfirmCreateDialogHandler implements OnConfirmDialogHandler, OnCallback {

        @Override
        public void handleConfirmDialog(String locationName) {
            Log.message("Enter");

            Intent intent = new Intent();
            intent.putExtra(Constants.LOCATION_LATITUDE, mMapApi.getMarkerPosition().latitude);
            intent.putExtra(Constants.LOCATION_LONGITUDE, mMapApi.getMarkerPosition().longitude);

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
                mMapApi.moveCamera(coordinates);
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
