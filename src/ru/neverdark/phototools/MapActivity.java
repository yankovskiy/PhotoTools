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
package ru.neverdark.phototools;

import ru.neverdark.phototools.db.LocationsDbAdapter;
import ru.neverdark.phototools.fragments.ConfirmCreateFragment;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends SherlockFragmentActivity implements
        OnMapLongClickListener {

    private GoogleMap mMap;
    private Button mButtonDone;
    private Marker mMarker;
    private LatLng mMarkerPosition;
    private int mAction;
    private long mRecordId;

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        Log.message("Enter");
        mButtonDone = (Button) findViewById(R.id.map_button_done);
    }

    /**
     * Handles getting information from Confirmation dialog
     * 
     * @param locationName
     *            location name
     */
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

    /**
     * Inits Google Map
     */
    private void initMap(Bundle savedInstanceState) {
        Log.message("Enter");
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

            mMap.setMyLocationEnabled(true);
            mMap.setOnMapLongClickListener(this);
        }

        if (savedInstanceState == null) {
            /* gets current coord if have */
            Intent intent = getIntent();
            Double latitude = intent.getDoubleExtra(
                    Constants.LOCATION_LATITUDE, 0);
            Double longitude = intent.getDoubleExtra(
                    Constants.LOCATION_LONGITUDE, 0);

            /* gets action */
            mAction = intent.getIntExtra(Constants.LOCATION_ACTION,
                    Constants.LOCATION_ACTION_ADD);

            if (mAction == Constants.LOCATION_ACTION_EDIT) {
                mRecordId = intent.getLongExtra(Constants.LOCATION_RECORD_ID,
                        Constants.LOCATION_POINT_ON_MAP_CHOICE);
            }

            /* checks for coordinates was received */
            if ((latitude != 0) || (longitude != 0)) {
                CameraPosition currentPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(Constants.MAP_CAMERA_ZOOM).build();
                mMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(currentPosition));
            }
        } else {
            mMarkerPosition = savedInstanceState
                    .getParcelable(Constants.MAP_MARKER_POSITION);
            mAction = savedInstanceState.getInt(Constants.MAP_ACTION_DATA);
            if (mMarkerPosition != null) {
                setMarker();
            }
        }
    }

    /**
     * OnClick handler for views
     * 
     * @param view
     *            for handle onClick event
     */
    public void onClick(View view) {
        Log.message("Enter");

        showConfirmDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bindObjectsToResources();
        initMap(savedInstanceState);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.message("Enter");
        mMarkerPosition = point;
        setMarker();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.MAP_MARKER_POSITION, mMarkerPosition);
        outState.putInt(Constants.MAP_ACTION_DATA, mAction);
    }

    /**
     * Saves data to the database
     * 
     * @param locationName
     *            location name for save into database
     */
    private void saveDataToDatabase(String locationName) {
        Log.message("Enter");

        LocationsDbAdapter dbAdapter = new LocationsDbAdapter(
                getApplicationContext());
        dbAdapter.open();

        if (mAction == Constants.LOCATION_ACTION_ADD) {
            mRecordId = dbAdapter.createLocation(locationName,
                    mMarkerPosition.latitude, mMarkerPosition.longitude);
        } else if (mAction == Constants.LOCATION_ACTION_EDIT) {
            dbAdapter.updateLocation(mRecordId, locationName,
                    mMarkerPosition.latitude, mMarkerPosition.longitude);
        }

        dbAdapter.close();
    }

    /**
     * Sets visible property for Done button
     * 
     * @param isVisible
     *            true for Done button visible, false for invisible
     */
    private void setButtonVisible(final boolean isVisible) {
        Log.message("Enter");
        int visible;

        if (isVisible) {
            visible = View.VISIBLE;
        } else {
            visible = View.INVISIBLE;
        }

        mButtonDone.setVisibility(visible);
    }

    /**
     * Sets marker to the long tap position If marker already exists - remove
     * old marker and set new marker in new position
     */
    private void setMarker() {
        Log.message("Enter");

        /* If we have marker - destroy */
        if (mMarker != null) {
            mMap.clear();
        }
        mMarker = mMap.addMarker(new MarkerOptions().position(mMarkerPosition));
        setButtonVisible(true);

    }

    /**
     * Shows confirmation dialog
     */
    private void showConfirmDialog() {
        Log.message("Enter");
        ConfirmCreateFragment confirmDialog = new ConfirmCreateFragment();
        confirmDialog.show(getSupportFragmentManager(),
                Constants.CONFIRM_DIALOG);
    }

}
