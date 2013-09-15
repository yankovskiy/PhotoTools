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

import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GeoLocationService;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.GeoLocationService.GeoLocationBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
        Log.message("Enter");
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

    private Context context;
    private View mView;
    private static EditText mEditTextDate;
    private Button mButtonCalculate;
    private EditText mEditTextLocation;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private double mLatitude;
    private double mLongitude;

    private int mSelectionId;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.message("Enter");
            GeoLocationBinder binder = (GeoLocationBinder) service;
            mGeoLocationService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.message("Enter");
            bound = false;
        }
    };
    private Intent mSerivceIntent;

    private GeoLocationService mGeoLocationService;

    private boolean bound = false;

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
     * Binds to GeoLocationService
     */
    private void bindToGeoService() {
        Log.message("Enter");
        mSerivceIntent = new Intent(context, GeoLocationService.class);
        context.bindService(mSerivceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * Calculates sunset and sunrise
     */
    private void calculateSunset() {
        Log.message("Enter");
    }

    /**
     * Gets current location 
     */
    private void getCurrentLocation() {
        Log.message("Enter");
        
        if (mGeoLocationService.canGetLocation()) {
            mLatitude = mGeoLocationService.getLatitude();
            mLongitude = mGeoLocationService.getLongitude();
            Log.variable("mlatitude", String.valueOf(mLatitude));
            Log.variable("mlongitude", String.valueOf(mLongitude));
        } 
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
     * Handling location selection
     * 
     * @param locationSelectionId
     *            location selection id item
     */
    public void handleLocationSelection(final int locationSelectionId) {
        Log.message("Enter");
        getCurrentLocation();
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
     * Handles point of map selection
     */
    private void handlePointOnMap() {
        Log.message("Enter");
        Intent mapIntent = new Intent(getActivity(), MapActivity.class);

        mapIntent.putExtra(Constants.LOCATION_LATITUDE, mLatitude);
        mapIntent.putExtra(Constants.LOCATION_LONGITUDE, mLongitude);

        startActivityForResult(mapIntent, Constants.LOCATION_POINT_ON_MAP_CHOICE);
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
        context = mView.getContext();

        bindObjectsToResources();
        setOnClickListeners(mEditTextDate);
        setOnClickListeners(mButtonCalculate);
        setOnClickListeners(mEditTextLocation);

        if (savedInstanceState != null) {
            mLatitude  = savedInstanceState.getDouble(Constants.LOCATION_LATITUDE, 0.0);
            mLongitude = savedInstanceState.getDouble(Constants.LOCATION_LONGITUDE, 0.0);
            Log.variable("mLatitude", String.valueOf(mLatitude));
            Log.variable("mLongitude", String.valueOf(mLongitude));
        } else {
            initDate();
            setTextLocation();
        }
        updateDate();

        return mView;
    }

    @Override
    public void onPause() {
        Log.message("Enter");
        super.onPause();
        unbindFromGeoService();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);
        outState.putDouble(Constants.LOCATION_LATITUDE, mLatitude);
        outState.putDouble(Constants.LOCATION_LONGITUDE, mLongitude);
        Log.variable("mLatitude", String.valueOf(mLatitude));
        Log.variable("mLongitude", String.valueOf(mLongitude));
    }

    @Override
    public void onStart() {
        Log.message("Enter");
        super.onStart();
        bindToGeoService();
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
                    if (mGeoLocationService.canGetLocation()) {
                        calculateSunset();
                    } else {
                        showSettingsAlert();
                    }
                    break;
                case R.id.sunset_editText_location:
                    showLocationSelectionDialog();
                }
            }
        });
    }

    /**
     * Sets text location method
     */
    private void setTextLocation() {
        Log.message("Enter");
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
     * Shows location selection dialog
     */
    private void showLocationSelectionDialog() {
        Log.message("Enter");
        LocationSelectionFragment locationFragment = new LocationSelectionFragment();
        locationFragment.setTargetFragment(this, Constants.DIALOG_FRAGMENT);
        locationFragment.show(getFragmentManager(),
                Constants.LOCATION_SELECTION_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.message("Enter");
        if (requestCode == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            if (resultCode == Activity.RESULT_OK) {
                mLatitude = data.getDoubleExtra(Constants.LOCATION_LATITUDE, 0.0);
                mLongitude = data.getDoubleExtra(Constants.LOCATION_LONGITUDE, 0.0);
                Log.variable("mLatitude", String.valueOf(mLatitude));
                Log.variable("mLongitude", String.valueOf(mLongitude));
                mSelectionId = Constants.LOCATION_POINT_ON_MAP_CHOICE;
                setTextLocation();
            }
        }
    }
    
    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     * */
    private void showSettingsAlert() {
        Log.message("Enter");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.sunset_alert_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.sunset_alert_message);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.sunset_alert_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.sunset_alert_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Unbinds from GeoLocationService
     */
    private void unbindFromGeoService() {
        Log.message("Enter");
        if (bound == true) {
            context.unbindService(mServiceConnection);
            bound = false;
        }
    }
}
