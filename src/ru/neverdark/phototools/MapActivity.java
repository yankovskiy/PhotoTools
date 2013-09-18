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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bindObjectsToResources();
        initMap(savedInstanceState);
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

            /* checks for coordinates was received */
            if ((latitude != 0) || (longitude != 0)) {
                CameraPosition currentPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(Constants.MAP_CAMERA_ZOOM).build();
                mMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(currentPosition));
            }
        } else {
            mMarkerPosition = savedInstanceState.getParcelable(Constants.MAP_MARKER_POSITION);
            if (mMarkerPosition != null) {
                setMarker();
            }
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.MAP_MARKER_POSITION, mMarkerPosition);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.message("Enter");
        mMarkerPosition = point;
        setMarker();
    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        Log.message("Enter");
        mButtonDone = (Button) findViewById(R.id.map_button_done);
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
     * OnClick handler for views
     * 
     * @param view
     *            for handle onClick event
     */
    public void onClick(View view) {
        Log.message("Enter");
        Intent intent = new Intent();
        intent.putExtra(Constants.LOCATION_LATITUDE, mMarkerPosition.latitude);
        intent.putExtra(Constants.LOCATION_LONGITUDE, mMarkerPosition.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
    

}
