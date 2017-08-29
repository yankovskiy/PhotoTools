/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
 ******************************************************************************/
package ru.neverdark.phototools.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.async.AsyncGoogleTimeZone;
import ru.neverdark.phototools.fragments.DateDialog.OnDateChangeListener;
import ru.neverdark.phototools.fragments.LocationSelectionDialog.OnLocationListener;
import ru.neverdark.phototools.fragments.ZonePickerDialog.OnTimeZonePickerListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GeoLocationService;
import ru.neverdark.phototools.utils.GeoLocationService.GeoLocationBinder;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.sunmooncalc.SunriseSunsetCalculator;

/**
 * Fragment contains sunrise / sunset UI
 */
public class SunsetFragment extends UfoFragment {
    private static final int SUNSET_ACCESS_FINE_LOCATION = 1;
    private EditText mEditTextDate;
    private Context mContext;
    private View mView;
    private Button mButtonCalculate;
    private EditText mEditTextLocation;
    private EditText mEditTextTimeZone;
    private LatLng mLocation;
    private TimeZone mTimeZone;
    private long mSelectionId;
    private GeoLocationService mGeoLocationService;
    private boolean mBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.message("Enter");
            GeoLocationBinder binder = (GeoLocationBinder) service;
            mGeoLocationService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.message("Enter");
            mBound = false;
        }
    };
    private TextView mSunriseResult;
    private TextView mSunsetResult;
    private TextView mAstroDawnResult;
    private TextView mAstroDuskResult;
    private TextView mNauticalDawnResult;
    private TextView mNauticalDuskResult;
    private TextView mCivilDawnResult;
    private TextView mCivilDuskResult;
    private TextView mNightResult;
    private TextView mSolarNoonResult;
    private TextView mGoldenHourDawnResult;
    private TextView mGoldenHourDuskResult;

    private LinearLayout mLinearLayoutCalculationResult;
    private String mLocationName;
    private int mTimeZoneMethod;
    private Calendar mCalendar;


    @Override
    public void bindObjects() {

    }

    @Override
    public void setListeners() {

    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        Log.message("Enter");
        mEditTextDate = (EditText) mView.findViewById(R.id.sunset_editText_date);
        mButtonCalculate = (Button) mView.findViewById(R.id.sunset_button_calculate);
        mEditTextLocation = (EditText) mView.findViewById(R.id.sunset_editText_location);
        mEditTextTimeZone = (EditText) mView.findViewById(R.id.sunset_editText_timeZone);

        mSunriseResult = (TextView) mView.findViewById(R.id.sunset_label_sunriseResult);
        mSunsetResult = (TextView) mView.findViewById(R.id.sunset_label_sunsetResult);

        mAstroDawnResult = (TextView) mView.findViewById(R.id.sunset_label_astroDawnResult);
        mAstroDuskResult = (TextView) mView.findViewById(R.id.sunset_label_astroDuskResult);

        mNauticalDawnResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalDawnResult);
        mNauticalDuskResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalDuskResult);

        mCivilDawnResult = (TextView) mView.findViewById(R.id.sunset_label_civilDawnResult);
        mCivilDuskResult = (TextView) mView.findViewById(R.id.sunset_label_civilDuskResult);

        mNightResult = (TextView) mView.findViewById(R.id.sunset_label_nightResult);
        mSolarNoonResult = (TextView) mView.findViewById(R.id.sunset_label_solarNoonResult);

        mGoldenHourDawnResult = (TextView) mView.findViewById(R.id.sunset_label_goldenHourDawnResult);
        mGoldenHourDuskResult = (TextView) mView.findViewById(R.id.sunset_label_goldenHourDuskResult);

        mLinearLayoutCalculationResult = (LinearLayout) mView
                .findViewById(R.id.sunsnet_LinearLayout_calculationResult);
    }

    /**
     * Binds to GeoLocationService
     */
    private void bindToGeoService() {
        Log.message("Enter");
        Intent serivceIntent = new Intent(mContext, GeoLocationService.class);
        mContext.bindService(serivceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private String generateTzLabel() {
        ZonePickerDialog.GMT gmt = ZonePickerDialog.getGMTOffset(mTimeZone, mCalendar.getTimeInMillis());
        return String.format("%s (%s)", mTimeZone.getID(), gmt.getGMT());
    }

    private static final String TAG = "SunsetFragment";

    /**
     * Calculates sunset and sunrise
     */
    private void calculateSunset() {
        Log.message("Enter");

        try {
            if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                if (!getCurrentLocation()) {
                    throw new LocationNotDetermineException();
                }

                String timeZone;

                if (mTimeZoneMethod == Constants.AUTO_TIMEZONE_METHOD) {
                    setDeviceTimeZone();
                    timeZone = String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel());
                    mEditTextTimeZone.setText(timeZone);
                }
            }

            if (mTimeZone == null) {
                showTimeZoneSelectionDialog();
                return;
            }

            Calendar calendar = Calendar.getInstance(mTimeZone);
            Common.copyCalendarWithoutTz(mCalendar, calendar);

            SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(mLocation, calendar);
            setVisibleCalculculationResult(calculator);
        } catch (LocationNotDetermineException e) {
            ShowMessageDialog dialog = ShowMessageDialog.getInstance(mContext);
            dialog.setMessages(R.string.error, R.string.error_locationNotDetermine);
            dialog.show(getFragmentManager(), ShowMessageDialog.DIALOG_TAG);
        }
    }

    /**
     * Gets current location
     *
     * @return true of location gets successful
     */
    private boolean getCurrentLocation() {
        boolean success = false;
        Log.message("Enter");

        if (Common.isHavePermissions(mContext)) {
            if (mGeoLocationService.canGetLocation()) {
                mLocation = new LatLng(mGeoLocationService.getLatitude(), mGeoLocationService.getLongitude());
                Log.variable("latitude", String.valueOf(mLocation.latitude));
                Log.variable("longitude", String.valueOf(mLocation.longitude));
                success = (mLocation.latitude != 0.0 || mLocation.longitude != 0.0);
            }
        }

        return success;
    }

    /**
     * Gets Timezone from Google Online Map
     */
    private void getTimeZoneFromGoogle() {
        Log.message("Enter");

        AsyncGoogleTimeZone gtz = new AsyncGoogleTimeZone(mCalendar, mLocation, new AsyncGoogleTimeZone.OnGoogleTimezoneListener() {
            @Override
            public void onGetResultSuccess(TimeZone tz) {
                mTimeZone = tz;
                showInformationMesssage(R.string.sunset_information_timeZoneSuccess);
                String timeZone = String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel());
                mEditTextTimeZone.setText(timeZone);

            }

            @Override
            public void onGetResultFail() {
                mTimeZone = null;
                showInformationMesssage(R.string.sunset_information_timeZoneFail);
            }

            @Override
            public void onPreExecute() {
                showInformationMesssage(R.string.sunset_information_timeZoneStart);
            }

            @Override
            public void onPostExecute() {

            }
        });
        gtz.execute();
    }

    /**
     * Handles current location selection
     */
    private void handleCurrentLocation() {
        Log.message("Enter");
        mSelectionId = Constants.LOCATION_CURRENT_POSITION_CHOICE;
        if (mTimeZoneMethod == Constants.AUTO_TIMEZONE_METHOD) {
            setDeviceTimeZone();
            mEditTextTimeZone.setText(String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel()));
        }
        setTextLocation();
    }

    /**
     * Handles custom location on the map
     *
     * @param locationRecord object contains row from database
     */
    private void handleCustomLocation(final LocationRecord locationRecord) {
        Log.message("Enter");

        mLocation = new LatLng(locationRecord.latitude, locationRecord.longitude);
        mLocationName = locationRecord.locationName;
        mSelectionId = locationRecord._id;

        if (mTimeZoneMethod == Constants.AUTO_TIMEZONE_METHOD) {
            getTimeZoneFromGoogle();
        }
        setTextLocation();
    }

    /**
     * Handles point of map selection
     */
    private void handlePointOnMap() {
        Log.message("Enter");
        if (isGoogleServiceAvailabe()) {
            if(!getCurrentLocation()) {
                mLocation = new LatLng(0, 0);
            }
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);

            mapIntent.putExtra(Constants.TIMEZONE, mTimeZone);
            mapIntent.putExtra(Constants.CALENDAR, mCalendar);
            mapIntent.putExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_ADD);
            mapIntent.putExtra(Constants.LOCATION_LATITUDE, mLocation.latitude);
            mapIntent.putExtra(Constants.LOCATION_LONGITUDE, mLocation.longitude);

            startActivityForResult(mapIntent, Constants.LOCATION_POINT_ON_MAP_CHOICE);
        }
    }

    /**
     * Inits date for current date
     */
    private void initDate() {
        Log.message("Enter");
        mCalendar = Calendar.getInstance();
    }

    /**
     * Checks for availabe Google Service If service is not available, or
     * service is not up to date
     *
     * @return true if Google service is available
     */
    private boolean isGoogleServiceAvailabe() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(mContext);
        boolean isAvailable = false;
        if (status == ConnectionResult.SUCCESS) {
            isAvailable = true;
        } else {
            Dialog dialog = api.getErrorDialog(getActivity(), status, 1);
            dialog.show();
        }

        return isAvailable;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.message("Enter");
        if (mGeoLocationService == null && Common.isHavePermissions(mContext)) {
            bindToGeoService();
        }

        if (requestCode == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            if (resultCode == Activity.RESULT_OK) {
                mLocation = new LatLng(
                        data.getDoubleExtra(Constants.LOCATION_LATITUDE, 0.0),
                        data.getDoubleExtra(Constants.LOCATION_LONGITUDE, 0.0));
                mLocationName = data.getStringExtra(Constants.LOCATION_NAME);
                mSelectionId = data.getLongExtra(Constants.LOCATION_RECORD_ID,
                        Constants.LOCATION_POINT_ON_MAP_CHOICE);

                if (mTimeZoneMethod == Constants.AUTO_TIMEZONE_METHOD) {
                    getTimeZoneFromGoogle();
                }

                setTextLocation();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.sunset_fragment, container, false);
        mContext = mView.getContext();

        bindObjectsToResources();

        setOnClickListeners(mEditTextDate);
        setOnClickListeners(mButtonCalculate);
        setOnClickListeners(mEditTextLocation);
        setOnClickListeners(mEditTextTimeZone);

        setEditTextLongClick(mEditTextDate);
        setEditTextLongClick(mEditTextLocation);
        setEditTextLongClick(mEditTextTimeZone);

        initDate();
        setTextLocation();

        updateDate();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Common.isHavePermissions(mContext)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SUNSET_ACCESS_FINE_LOCATION);
        } else {
            bindToGeoService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SUNSET_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bindToGeoService();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        Log.message("Enter");
        super.onPause();
        unbindFromGeoService();
    }


    @Override
    public void onResume() {
        Log.message("Enter");
        super.onResume();
    }

    /**
     * Sets default timezone
     */
    private void setDeviceTimeZone() {
        mTimeZone = TimeZone.getDefault();
    }

    /**
     * Sets long click listener for one EditText
     *
     * @param editText EditText for sets long click listener
     */
    private void setEditTextLongClick(final EditText editText) {
        Log.message("Enter");
        editText.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                Log.message("Enter");
                switch (arg0.getId()) {
                    case R.id.sunset_editText_date:
                        initDate();
                        updateDate();
                        break;
                    case R.id.sunset_editText_location:
                        handleCurrentLocation();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Sets onClickListeners for views
     *
     * @param view view for set onClickListener
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
                    case R.id.sunset_editText_timeZone:
                        showTimeZoneMethodDialog();
                        break;

                    case R.id.sunset_button_calculate:
                        if (mSelectionId > Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                            calculateSunset();
                        } else {
                            if (Common.isHavePermissions(mContext)) {
                                    /* if we have coordinates, calculation sunset / sunrise */
                                if (mGeoLocationService.canGetLocation()) {
                                    calculateSunset();
                                    /*
                                     * we dont't have coordinates and we try getting
                                     * current location, show alert dialog
                                     */
                                } else {
                                    showSettingsAlert();
                                }
                            } else {
                                ConfirmDialog dialog = ConfirmDialog.getInstance(mContext);
                                dialog.setTitle(R.string.attention);
                                dialog.setMessage(R.string.sunset_current_location_error);
                                dialog.setCallback(new SunsetCurrentLocationErrorListener());
                                dialog.show(getFragmentManager(), ConfirmDialog.DIALOG_ID);
                            }
                        }
                        break;
                    case R.id.sunset_editText_location:
                        showLocationSelectionDialog();
                        break;
                }
            }

            class SunsetCurrentLocationErrorListener implements OnCallback, ConfirmDialog.OnPositiveClickListener {
                @Override
                public void onPositiveClickHandler() {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SUNSET_ACCESS_FINE_LOCATION);
                }
            }
        });
    }

    /**
     * Shows dialog for choosing time zone picker method
     */
    private void showTimeZoneMethodDialog() {
        TimeZonePickerMethodDialog dialog = TimeZonePickerMethodDialog.getInstance(mContext);
        dialog.setCallback(new TimeZonePickerMethodListener());
        dialog.show(getFragmentManager(), TimeZonePickerMethodDialog.DIALOG_ID);
    }

    /**
     * Sets text location method
     */
    private void setTextLocation() {
        Log.message("Enter");
        if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
            mEditTextLocation.setText(R.string.locationSelection_label_currentLocation);
        } else if (mSelectionId == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            mEditTextLocation.setText(R.string.locationSelection_label_pointOnMap);
        } else {
            mEditTextLocation.setText(mLocationName);
        }
    }

    /**
     * Sets visible calculation result scrollView
     *
     * @param calculator
     */
    private void setVisibleCalculculationResult(SunriseSunsetCalculator calculator) {
        mNightResult.setText(calculator.getNight());
        mAstroDawnResult.setText(calculator.getAstroDawn());

        mSunriseResult.setText(calculator.getSunrise());
        mSunsetResult.setText(calculator.getSunset());

        mCivilDawnResult.setText(calculator.getDawn());
        mCivilDuskResult.setText(calculator.getDusk());

        mNauticalDawnResult.setText(calculator.getNauticalDawn());
        mNauticalDuskResult.setText(calculator.getNauticalDusk());


        mAstroDuskResult.setText(calculator.getAstroDusk());
        mSolarNoonResult.setText(calculator.getSolarNoon());

        mGoldenHourDawnResult.setText(calculator.getGoldenHourDawn());
        mGoldenHourDuskResult.setText(calculator.getGoldenHourDusk());

        mLinearLayoutCalculationResult.setVisibility(View.VISIBLE);
    }

    /**
     * Shows date picker dialog
     */
    private void showDatePicker() {
        Log.message("Enter");
        DateDialog dateFragment = DateDialog.getInstance(mContext);
        dateFragment.setDate(mCalendar);
        dateFragment.setCallback(new DateChangeHandler());
        dateFragment.show(getFragmentManager(), DateDialog.DIALOG_ID);
    }

    /**
     * Shows information message
     *
     * @param resourceId string Id contains information message
     */
    private void showInformationMesssage(int resourceId) {
        Toast.makeText(mContext, resourceId, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows location selection dialog
     */
    private void showLocationSelectionDialog() {
        Log.message("Enter");
        LocationSelectionDialog locationFragment = LocationSelectionDialog.getInstance(mContext);
        locationFragment.setCallback(new LocationEditListener());
        locationFragment.show(getFragmentManager(), LocationSelectionDialog.DIALOG_TAG);
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     */
    private void showSettingsAlert() {
        Log.message("Enter");
        AlertSettingsDialog alertFragment = AlertSettingsDialog.getInstance(mContext);
        alertFragment.show(getFragmentManager(), AlertSettingsDialog.DIALOG_ID);
    }

    /**
     * Shows TimeZone selection dialog
     */
    private void showTimeZoneSelectionDialog() {
        Log.enter();
        ZonePickerDialog dialog = ZonePickerDialog.getInstance(mContext);
        dialog.setCallback(new TimeZonePickerListener());
        dialog.show(getFragmentManager(), ZonePickerDialog.DIALOG);
    }

    /**
     * Unbinds from GeoLocationService
     */
    private void unbindFromGeoService() {
        Log.message("Enter");
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }

    /**
     * Updates dates into the mEditTextDate
     */
    private void updateDate() {
        Log.message("Enter");
        /* formating date for system locale */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", java.util.Locale.getDefault());
        mEditTextDate.setText(sdf.format(mCalendar.getTime()));
    }

    private class TimeZonePickerListener implements OnTimeZonePickerListener, OnCallback {

        @Override
        public void onTimeZonePickerHandler(TimeZone tz) {
            mTimeZone = tz;
            //calculateSunset();
            mEditTextTimeZone.setText(generateTzLabel());
            mTimeZoneMethod = Constants.MANUAL_TIMEZONE_METHOD;
        }
    }

    private class DateChangeHandler implements OnDateChangeListener, OnCallback {
        @Override
        public void dateChangeHandler(int year, int month, int day) {
            mCalendar.set(year, month, day);
            updateDate();
        }
    }

    private class LocationEditListener implements OnLocationListener, OnCallback {

        @Override
        public void onEditLocationHandler(LocationRecord record) {
            Log.message("Enter");
            if (isGoogleServiceAvailabe()) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_EDIT);
                mapIntent.putExtra(Constants.LOCATION_LATITUDE, record.latitude);
                mapIntent.putExtra(Constants.LOCATION_LONGITUDE, record.longitude);
                mapIntent.putExtra(Constants.LOCATION_RECORD_ID, record._id);
                mapIntent.putExtra(Constants.LOCATION_NAME, record.locationName);
                mapIntent.putExtra(Constants.LOCATION_MAP_TYPE, record.mapType);
                mapIntent.putExtra(Constants.LOCATION_CAMERA_ZOOM, record.cameraZoom);
                startActivityForResult(mapIntent, Constants.LOCATION_POINT_ON_MAP_CHOICE);
            }
        }

        @Override
        public void onSelectLocationHandler(LocationRecord record) {
            Log.message("Enter");
            if (record._id == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                handleCurrentLocation();
            } else if (record._id == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
                handlePointOnMap();
            } else {
                Log.variable("mSelectionId", String.valueOf(mSelectionId));
                handleCustomLocation(record);
            }
        }

    }

    private class LocationNotDetermineException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 5446852956370468838L;

    }

    private class TimeZonePickerMethodListener implements TimeZonePickerMethodDialog.OnTimeZonePickerMethodListener, OnCallback {
        @Override
        public void onMethodClick(int position) {
            if (position != mTimeZoneMethod && position == Constants.AUTO_TIMEZONE_METHOD) {
                mTimeZoneMethod = position;
                if (mSelectionId != Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                    mEditTextTimeZone.setText(getString(R.string.sunset_label_auto));
                    getTimeZoneFromGoogle();
                } else {
                    setDeviceTimeZone();
                    mEditTextTimeZone.setText(String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel()));
                }
            } else if (position == Constants.MANUAL_TIMEZONE_METHOD) {
                showTimeZoneSelectionDialog();
            }
        }
    }

}
