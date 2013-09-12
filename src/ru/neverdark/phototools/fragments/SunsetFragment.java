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
package ru.neverdark.phototools.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.neverdark.phototools.Constants;
import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.log.Log;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Fragment contains sunrise / sunset UI
 */
public class SunsetFragment extends SherlockFragment {
    private View mView;
    private static EditText mEditTextDate;
    private Button mButtonCalculate;
    private EditText mEditTextLocation;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private int mSelectionId;

    /**
     * Sets date and update EditText
     * 
     * @param year
     *            year
     * @param month
     *            month of year
     * @param day
     *            day of month
     */
    public static void setDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        updateDate();
    }

    /**
     * Updates dates into the mEditTextDate
     */
    private static void updateDate() {
        Log.message("Enter");
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(mYear, mMonth, mDay);
        /* formating date for system locale */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy",
                java.util.Locale.getDefault());
        mEditTextDate.setText(sdf.format(localCalendar.getTime()));
    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        Log.message("Enter");
        mEditTextDate = (EditText) mView
                .findViewById(R.id.sunset_editText_date);
        mButtonCalculate = (Button) mView
                .findViewById(R.id.sunset_button_calculate);
        mEditTextLocation = (EditText) mView
                .findViewById(R.id.sunset_editText_location);
    }

    /**
     * Inits date for current date
     */
    private void initDate() {
        Log.message("Enter");
        Calendar localCalendar = Calendar.getInstance();
        mYear = localCalendar.get(Calendar.YEAR);
        mMonth = localCalendar.get(Calendar.MONTH);
        mDay = localCalendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_sunset, container, false);

        bindObjectsToResources();
        setOnClickListeners(mEditTextDate);
        setOnClickListeners(mButtonCalculate);
        setOnClickListeners(mEditTextLocation);

        if (savedInstanceState != null) {
            // Load date
            // Load location

        } else {
            initDate();
            setTextLocation();
        }
        updateDate();

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.message("Enter");

    }

    /**
     * Sets onClickListeners for views
     * 
     * @param view
     *            view for set onClickListener
     */
    private void setOnClickListeners(View view) {
        Log.message("Enter");
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.message("Enter");
                switch (arg0.getId()) {
                case R.id.sunset_editText_date:
                    showDatePicker();
                    break;
                case R.id.sunset_button_calculate:
                    calculateSunset();
                    break;
                case R.id.sunset_editText_location:
                    showLocationSelectionDialog();
                }
            }
        });
    }

    /**
     * Calculates sunset and sunrise
     */
    private void calculateSunset() {
        Log.message("Enter");
    }

    /**
     * Shows date picker dialog
     */
    private void showDatePicker() {
        Log.message("Enter");
        DateFragment dateFragment = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATE_YEAR, mYear);
        bundle.putInt(Constants.DATE_MONTH, mMonth);
        bundle.putInt(Constants.DATE_DAY, mDay);
        dateFragment.setArguments(bundle);
        dateFragment.show(getFragmentManager(), Constants.DATE_PICKER_DIALOG);
    }

    /**
     * Shows map for selection position
     */
    private void showMap() {
        Log.message("Enter");
        Location location = getCurrentLocation();

        Intent mapIntent = new Intent(getActivity(), MapActivity.class);

        /*
         * If current location was determine we put coordinates for display
         * current location on the map.
         */
        if (location != null) {
            mapIntent.putExtra(Constants.LOCATION_LATITUDE,
                    location.getLatitude());
            mapIntent.putExtra(Constants.LOCATION_LONGITUDE,
                    location.getLongitude());
        }
        startActivity(mapIntent);
    }

    /**
     * Shows location selection dialog
     */
    private void showLocationSelectionDialog() {
        LocationSelectionFragment locationFragment = new LocationSelectionFragment();
        locationFragment.setTargetFragment(this, Constants.DIALOG_FRAGMENT);
        locationFragment.show(getFragmentManager(),
                Constants.LOCATION_SELECTION_DIALOG);
    }

    /**
     * Handles current location selection
     */
    private void handleCurrentLocation() {
        Log.message("Enter");
        mSelectionId = Constants.LOCATION_CURRENT_POSITION_CHOICE;
        setTextLocation();
    }

    /**
     * Handles point of map selection
     */
    private void handlePointOnMap() {
        Log.message("Enter");
        showMap();
        mSelectionId = Constants.LOCATION_POINT_ON_MAP_CHOICE;
        setTextLocation();
    }

    /**
     * Handling location selection
     * 
     * @param locationSelectionId
     *            location selection id item
     */
    public void handleLocationSelection(final int locationSelectionId) {
        Log.message("Enter");

        switch (locationSelectionId) {
        case Constants.LOCATION_CURRENT_POSITION_CHOICE:
            handleCurrentLocation();
            break;
        case Constants.LOCATION_POINT_ON_MAP_CHOICE:
            handlePointOnMap();
            break;
        default:
            break;
        }
    }

    /**
     * Gets current location
     * 
     * @return Location object with current location on null if not determine
     */
    private Location getCurrentLocation() {
        Log.message("Enter");
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        ;
        Location location = null;

        try {
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            location = locationManager.getLastKnownLocation(provider);

        } catch (NullPointerException e) {
            Log.message("LOCATION_SERVICE not found.");
            Log.message(e.getMessage());
        }

        return location;
    }

    /**
     * Sets text location method
     */
    private void setTextLocation() {
        switch (mSelectionId) {
        case Constants.LOCATION_CURRENT_POSITION_CHOICE:
            mEditTextLocation
                    .setText(R.string.locationSelection_label_currentLocation);
            break;
        case Constants.LOCATION_POINT_ON_MAP_CHOICE:
            mEditTextLocation
                    .setText(R.string.locationSelection_label_pointOnMap);
            break;
        default:
            break;
        }
    }

}
