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

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.fragments.DateDialog.OnDateChangeListener;
import ru.neverdark.phototools.fragments.LocationSelectionDialog.OnLocationListener;
import ru.neverdark.phototools.fragments.ZonePickerDialog.OnTimeZonePickerListener;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GeoLocationService;
import ru.neverdark.phototools.utils.GeoLocationService.GeoLocationBinder;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;

/**
 * Fragment contains sunrise / sunset UI
 */
public class SunsetFragment extends UfoFragment {
    private EditText mEditTextDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Context mContext;
    private View mView;
    private Button mButtonCalculate;
    private EditText mEditTextLocation;
    private EditText mEditTextTimeZone;
    private double mLatitude;
    private double mLongitude;
    private boolean mIsVisibleResult = false;
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
    private String mOfficialSunrise;
    private TextView mOfficialSunriseResult;
    private String mOfficialSunset;
    private TextView mOfficialSunsetResult;
    private String mAstroSunrise;
    private TextView mAstroSunriseResult;
    private String mAstroSunset;
    private TextView mAstroSunsetResult;
    private String mNauticalSunrise;
    private TextView mNauticalSunriseResult;
    private String mNauticalSunset;
    private TextView mNauticalSunsetResult;
    private String mCivilSunrise;
    private TextView mCivilSunriseResult;
    private String mCivilSunset;
    private TextView mCivilSunsetResult;
    private LinearLayout mLinearLayoutCalculationResult;
    private String mLocationName;
    private int mTimeZoneMethod;
    private View mRowOfficialSunrise;
    private View mRowOfficialSunset;
    private View mRowAstroSunrise;
    private View mRowAstroSunset;
    private View mRowNauticalSunrise;
    private View mRowNauticalSunset;
    private View mRowCivilSunrise;
    private View mRowCivilSunset;

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

        mOfficialSunriseResult = (TextView) mView.findViewById(R.id.sunset_label_sunriseResult);
        mOfficialSunsetResult = (TextView) mView.findViewById(R.id.sunset_label_sunsetResult);

        mAstroSunriseResult = (TextView) mView.findViewById(R.id.sunset_label_astrolSunriseResult);
        mAstroSunsetResult = (TextView) mView.findViewById(R.id.sunset_label_astrolSunsetResult);

        mNauticalSunriseResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunriseResult);
        mNauticalSunsetResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunsetResult);

        mCivilSunriseResult = (TextView) mView.findViewById(R.id.sunset_label_civilSunriseResult);
        mCivilSunsetResult = (TextView) mView.findViewById(R.id.sunset_label_civilSunsetResult);

        mRowOfficialSunrise = mView.findViewById(R.id.sunset_row_sunrise);
        mRowOfficialSunset = mView.findViewById(R.id.sunset_row_sunset);

        mRowAstroSunrise = mView.findViewById(R.id.sunset_row_astrolSunrise);
        mRowAstroSunset = mView.findViewById(R.id.sunset_row_astrolSunset);

        mRowNauticalSunrise = mView.findViewById(R.id.sunset_row_nauticalSunrise);
        mRowNauticalSunset = mView.findViewById(R.id.sunset_row_nauticalSunset);

        mRowCivilSunrise = mView.findViewById(R.id.sunset_row_civilSunrise);
        mRowCivilSunset = mView.findViewById(R.id.sunset_row_civilSunset);

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay);
        ZonePickerDialog.GMT gmt = ZonePickerDialog.getGMTOffset(mTimeZone, calendar.getTimeInMillis());
        return String.format("%s (%s)", mTimeZone.getID(), gmt.getGMT());
    }

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

            Location location = new Location(mLatitude, mLongitude);
            SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, mTimeZone);
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay);

            mOfficialSunrise = calculator.getOfficialSunriseForDate(calendar);
            mOfficialSunset = calculator.getOfficialSunsetForDate(calendar);

            mAstroSunrise = calculator.getAstronomicalSunriseForDate(calendar);
            mAstroSunset = calculator.getAstronomicalSunsetForDate(calendar);

            mNauticalSunrise = calculator.getNauticalSunriseForDate(calendar);
            mNauticalSunset = calculator.getNauticalSunsetForDate(calendar);

            mCivilSunrise = calculator.getCivilSunriseForDate(calendar);
            mCivilSunset = calculator.getCivilSunsetForDate(calendar);

            mIsVisibleResult = true;
            setVisibleCalculculationResult();
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

        if (mGeoLocationService.canGetLocation()) {
            mLatitude = mGeoLocationService.getLatitude();
            mLongitude = mGeoLocationService.getLongitude();
            Log.variable("mlatitude", String.valueOf(mLatitude));
            Log.variable("mlongitude", String.valueOf(mLongitude));
            success = (mLatitude != 0.0 || mLongitude != 0.0);
        }

        return success;
    }

    /**
     * Gets Timezone from Google Online Map
     */
    private void getTimeZoneFromGoogle() {
        Log.message("Enter");
        showInformationMesssage(R.string.sunset_information_timeZoneStart);
        TimeZoneFromGoogle timeZoneFromGoogle = new TimeZoneFromGoogle();
        timeZoneFromGoogle.execute();
    }

    /**
     * Handles current location selection
     */
    private void handleCurrentLocation() {
        Log.message("Enter");
        // mSelectionId = Constants.LOCATION_CURRENT_POSITION_CHOICE;
        if (mTimeZoneMethod == Constants.AUTO_TIMEZONE_METHOD) {
            setDeviceTimeZone();
            mEditTextTimeZone.setText(String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel()));
        }
        setTextLocation();
    }

    /**
     * Handles custom location on the map
     *
     * @param locationRecord
     *            object contains row from database
     */
    private void handleCustomLocation(final LocationRecord locationRecord) {
        Log.message("Enter");

        mLatitude = locationRecord.latitude;
        mLongitude = locationRecord.longitude;
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
            getCurrentLocation();
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);

            mapIntent.putExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_ADD);
            mapIntent.putExtra(Constants.LOCATION_LATITUDE, mLatitude);
            mapIntent.putExtra(Constants.LOCATION_LONGITUDE, mLongitude);

            startActivityForResult(mapIntent, Constants.LOCATION_POINT_ON_MAP_CHOICE);
        }
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

    /**
     * Checks for availabe Google Service If service is not available, or
     * service is not up to date
     *
     * @return true if Google service is available
     */
    private boolean isGoogleServiceAvailabe() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        boolean isAvailable = false;
        if (status == ConnectionResult.SUCCESS) {
            isAvailable = true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 1);
            dialog.show();
        }

        return isAvailable;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.message("Enter");
        if (requestCode == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            if (resultCode == Activity.RESULT_OK) {
                mLatitude = data.getDoubleExtra(Constants.LOCATION_LATITUDE, 0.0);
                mLongitude = data.getDoubleExtra(Constants.LOCATION_LONGITUDE, 0.0);
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
        setOnClickListeners(mRowAstroSunrise);
        setOnClickListeners(mRowAstroSunset);
        setOnClickListeners(mRowCivilSunrise);
        setOnClickListeners(mRowCivilSunset);
        setOnClickListeners(mRowNauticalSunrise);
        setOnClickListeners(mRowNauticalSunset);
        setOnClickListeners(mRowOfficialSunrise);
        setOnClickListeners(mRowOfficialSunset);
        setOnClickListeners(mEditTextTimeZone);

        setEditTextLongClick(mEditTextDate);
        setEditTextLongClick(mEditTextLocation);
        setEditTextLongClick(mEditTextTimeZone);

        initDate();
        setTextLocation();

        updateDate();
        setVisibleCalculculationResult();

        return mView;
    }

    @Override
    public void onPause() {
        Log.message("Enter");
        super.onPause();
        unbindFromGeoService();
    }

    @Override
    public void onStart() {
        Log.message("Enter");
        super.onStart();
        bindToGeoService();
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
     * @param editText
     *            EditText for sets long click listener
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
                    case R.id.sunset_row_astrolSunrise:
                        showInformationDialog(Constants.INFORMATION_ASTRO_SUNRISE);
                        break;
                    case R.id.sunset_row_astrolSunset:
                        showInformationDialog(Constants.INFORMATION_ASTRO_SUNSET);
                        break;
                    case R.id.sunset_row_civilSunrise:
                        showInformationDialog(Constants.INFORMATION_CIVIL_SUNRISE);
                        break;
                    case R.id.sunset_row_civilSunset:
                        showInformationDialog(Constants.INFORMATION_CIVIL_SUNSET);
                        break;
                    case R.id.sunset_row_nauticalSunrise:
                        showInformationDialog(Constants.INFORMATION_NAUTICAL_SUNRISE);
                        break;
                    case R.id.sunset_row_nauticalSunset:
                        showInformationDialog(Constants.INFORMATION_NAUTICAL_SUNSET);
                        break;
                    case R.id.sunset_row_sunrise:
                        showInformationDialog(Constants.INFORMATION_SUNRISE);
                        break;
                    case R.id.sunset_row_sunset:
                        showInformationDialog(Constants.INFORMATION_SUNSET);
                        break;
                    case R.id.sunset_editText_timeZone:
                        showTimeZoneMethodDialog();
                        break;

                    case R.id.sunset_button_calculate:

                        if (mSelectionId > Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                            calculateSunset();
                        } else {
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
                        }
                        break;
                    case R.id.sunset_editText_location:
                        showLocationSelectionDialog();
                        break;
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
     */
    private void setVisibleCalculculationResult() {
        if (mIsVisibleResult) {
            setVisibleForResultLine(mRowOfficialSunrise, !mOfficialSunrise.equals("99:99"));
            setVisibleForResultLine(mRowOfficialSunset, !mOfficialSunset.equals("99:99"));
            setVisibleForResultLine(mRowCivilSunrise, !mCivilSunrise.equals("99:99"));
            setVisibleForResultLine(mRowCivilSunset, !mCivilSunset.equals("99:99"));
            setVisibleForResultLine(mRowNauticalSunrise, !mNauticalSunrise.equals("99:99"));
            setVisibleForResultLine(mRowNauticalSunset, !mNauticalSunset.equals("99:99"));
            setVisibleForResultLine(mRowAstroSunrise, !mAstroSunrise.equals("99:99"));
            setVisibleForResultLine(mRowAstroSunset, !mAstroSunset.equals("99:99"));

            mOfficialSunriseResult.setText(mOfficialSunrise);
            mOfficialSunsetResult.setText(mOfficialSunset);

            mCivilSunriseResult.setText(mCivilSunrise);
            mCivilSunsetResult.setText(mCivilSunset);

            mNauticalSunriseResult.setText(mNauticalSunrise);
            mNauticalSunsetResult.setText(mNauticalSunset);

            mAstroSunriseResult.setText(mAstroSunrise);
            mAstroSunsetResult.setText(mAstroSunset);

            mLinearLayoutCalculationResult.setVisibility(View.VISIBLE);
        } else {
            mLinearLayoutCalculationResult.setVisibility(View.GONE);
        }
    }

    private void setVisibleForResultLine(View resultLine, boolean isVisible) {
        resultLine.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }

    /**
     * Shows date picker dialog
     */
    private void showDatePicker() {
        Log.message("Enter");
        DateDialog dateFragment = DateDialog.getInstance(mContext);
        dateFragment.setDate(mYear, mMonth, mDay);
        dateFragment.setCallback(new DateChangeHandler());
        dateFragment.show(getFragmentManager(), DateDialog.DIALOG_ID);
    }

    /**
     * Shows information dialog with description for sunset/sunrise type
     *
     * @param messageId
     *            Id message for displaying
     */
    private void showInformationDialog(final int messageId) {
        Log.message("Enter");
        InfoFragmentDialog infoFragment = InfoFragmentDialog.getInstance(mContext);
        infoFragment.setMessageId(messageId);
        infoFragment.show(getFragmentManager(), InfoFragmentDialog.DIALOG_TAG);
    }

    /**
     * Shows information message
     *
     * @param resourceId
     *            string Id contains information message
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
     * */
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
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(mYear, mMonth, mDay);
        /* formating date for system locale */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", java.util.Locale.getDefault());
        mEditTextDate.setText(sdf.format(localCalendar.getTime()));
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
            mYear = year;
            mMonth = month;
            mDay = day;
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
            mSelectionId = record._id;

            if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                handleCurrentLocation();
            } else if (mSelectionId == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
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

    /**
     * Thread for getting timeZone from Google Online Map If Internet connection
     * does not available we have use devices TimeZone
     */
    private class TimeZoneFromGoogle extends AsyncTask<Void, Void, Integer> {
        private final int STATUS_SUCCESS = 0;
        private final int STATUS_FAIL = 1;

        @Override
        protected Integer doInBackground(Void... arg0) {
            Log.message("Enter");
            int status = STATUS_FAIL;

            /* we have internet, download json from timeZone google service */
            if (isOnline()) {
                Log.message("Get Time Zone from Google");
                String json = readTimeZoneJson();
                Log.variable("json", json);

                /* JSON data not empty, parse it */
                if (json.length() != 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String timeZoneId = jsonObject.getString("timeZoneId");
                        String rawOffset = jsonObject.getString("rawOffset");
                        Log.variable("timeZoneId", timeZoneId);
                        Log.variable("rawOffset", rawOffset);
                        mTimeZone = TimeZone.getTimeZone(timeZoneId);
                        mTimeZone.setRawOffset(Integer.valueOf(rawOffset) * 1000);
                        status = STATUS_SUCCESS;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Log.message("Device offline.");
            }

            return status;
        }

        /**
         * Checks connection status
         *
         * @return true if device online, false in other case
         */
        private boolean isOnline() {
            Log.message("Enter");
            ConnectivityManager cm = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case STATUS_SUCCESS:
                    showInformationMesssage(R.string.sunset_information_timeZoneSuccess);
                    String timeZone = String.format("%s: %s", getString(R.string.sunset_label_auto), generateTzLabel());
                    mEditTextTimeZone.setText(timeZone);
                    break;
                case STATUS_FAIL:
                    mTimeZone = null;
                    showInformationMesssage(R.string.sunset_information_timeZoneFail);
                    break;
            }
        }

        /**
         * Reads TimeZone from Google Json
         *
         * @return TimeZone JSON from Google Json or empty if cannot determine
         */
        private String readTimeZoneJson() {
            Log.message("Enter");
            StringBuilder builder = new StringBuilder();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.set(mYear, mMonth, mDay);
            /* Gets desired time as seconds since midnight, January 1, 1970 UTC */
            Long timestamp = calendar.getTimeInMillis() / 1000;

            String url_format = "https://maps.googleapis.com/maps/api/timezone/json?location=%f,%f&timestamp=%d";
            String url = String.format(Locale.US, url_format, mLatitude, mLongitude, timestamp);
            Log.variable("url", url);

            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.message("Download fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
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
