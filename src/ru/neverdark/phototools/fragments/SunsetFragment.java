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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GeoLocationService;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.GeoLocationService.GeoLocationBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

/**
 * Fragment contains sunrise / sunset UI
 */
public class SunsetFragment extends SherlockFragment {
    /**
     * Thread for getting timeZone from Google Online Map If Internet connection
     * does not available we have use devices TimeZone
     */
    private class Task implements Runnable {

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
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        }

        /**
         * Reads TimeZone from Google Json
         * 
         * @return TimeZone JSON from Google Json or empty if cannot determine
         */
        private String readTimeZoneJson() {
            Log.message("Enter");
            StringBuilder builder = new StringBuilder();
            Calendar calendar = Calendar.getInstance(TimeZone
                    .getTimeZone("GMT"));
            calendar.set(mYear, mMonth, mDay);
            /* Gets desired time as seconds since midnight, January 1, 1970 UTC */
            Long timestamp = calendar.getTimeInMillis() / 1000;

            String url_format = "https://maps.googleapis.com/maps/api/timezone/json?location=%f,%f&timestamp=%d&sensor=false";
            String url = String.format(Locale.US, url_format, mLatitude,
                    mLongitude, timestamp);
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
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.message("Download fail");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        public void run() {
            Log.message("Enter");

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
                        Log.variable("timeZoneId", timeZoneId);
                        mTimeZone = TimeZone.getTimeZone(timeZoneId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Log.message("Device offline. Use device timezone");
                setDefaultTimeZone();
            }
        }
    }

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

    private Context mContext;
    private View mView;
    private static EditText mEditTextDate;
    private Button mButtonCalculate;
    private EditText mEditTextLocation;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private double mLatitude;
    private double mLongitude;
    private boolean mIsVisibleResult = false;

    private TimeZone mTimeZone;
    private long mSelectionId;
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

    private Intent mSerivceIntent;

    private GeoLocationService mGeoLocationService;

    private boolean mBound = false;
    private String mOfficialSunrise;
    private TextView mOfficialSunriseResult;
    private TextView mLabelOfficialSunrise;
    private String mOfficialSunset;
    private TextView mOfficialSunsetResult;
    private TextView mLabelOfficialSunset;
    private String mAstroSunrise;
    private TextView mAstroSunriseResult;
    private TextView mLabelAstroSunrise;
    private String mAstroSunset;
    private TextView mAstroSunsetResult;
    private TextView mLabelAstroSunset;
    private String mNauticalSunrise;
    private TextView mNauticalSunriseResult;
    private TextView mLabelNauticalSunrise;
    private String mNauticalSunset;
    private TextView mNauticalSunsetResult;
    private TextView mLabelNauticalSunset;
    private String mCivilSunrise;
    private TextView mCivilSunriseResult;
    private TextView mLabelCivilSunrise;
    private String mCivilSunset;
    private TextView mCivilSunsetResult;
    private TextView mLabelCivilSunset;
    private LinearLayout mLinearLayoutCalculationResult;
    private TextView mSunsetTimeZoneResult;
    private String mLocationName;

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

        mOfficialSunriseResult = (TextView) mView
                .findViewById(R.id.sunset_label_sunriseResult);
        mOfficialSunsetResult = (TextView) mView
                .findViewById(R.id.sunset_label_sunsetResult);

        mAstroSunriseResult = (TextView) mView
                .findViewById(R.id.sunset_label_astrolSunriseResult);
        mAstroSunsetResult = (TextView) mView
                .findViewById(R.id.sunset_label_astrolSunsetResult);

        mNauticalSunriseResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunriseResult);
        mNauticalSunsetResult = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunsetResult);

        mCivilSunriseResult = (TextView) mView
                .findViewById(R.id.sunset_label_civilSunriseResult);
        mCivilSunsetResult = (TextView) mView
                .findViewById(R.id.sunset_label_civilSunsetResult);

        mLabelOfficialSunrise = (TextView) mView
                .findViewById(R.id.sunset_label_sunrise);
        mLabelOfficialSunset = (TextView) mView
                .findViewById(R.id.sunset_label_sunset);

        mLabelAstroSunrise = (TextView) mView
                .findViewById(R.id.sunset_label_astrolSunrise);
        mLabelAstroSunset = (TextView) mView
                .findViewById(R.id.sunset_label_astrolSunset);

        mLabelNauticalSunrise = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunrise);
        mLabelNauticalSunset = (TextView) mView
                .findViewById(R.id.sunset_label_nauticalSunset);

        mLabelCivilSunrise = (TextView) mView
                .findViewById(R.id.sunset_label_civilSunrise);
        mLabelCivilSunset = (TextView) mView
                .findViewById(R.id.sunset_label_civilSunset);

        mLinearLayoutCalculationResult = (LinearLayout) mView
                .findViewById(R.id.sunsnet_LinearLayout_calculationResult);

        mSunsetTimeZoneResult = (TextView) mView
                .findViewById(R.id.sunset_label_timeZoneResult);
    }

    /**
     * Binds to GeoLocationService
     */
    private void bindToGeoService() {
        Log.message("Enter");
        mSerivceIntent = new Intent(mContext, GeoLocationService.class);
        mContext.bindService(mSerivceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * Calculates sunset and sunrise
     */
    private void calculateSunset() {
        Log.message("Enter");

        if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
            setDefaultTimeZone();
            getCurrentLocation();
        }

        if (mTimeZone == null) {
            setDefaultTimeZone();
        }

        Location location = new Location(mLatitude, mLongitude);
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(
                location, mTimeZone);
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
     * Gets Timezone from Google Online Map
     */
    private void getTimeZoneFromGoogle() {
        Log.message("Enter");
        new Thread(new Task()).start();
    }

    /**
     * Handles current location selection
     */
    private void handleCurrentLocation() {
        Log.message("Enter");
        //mSelectionId = Constants.LOCATION_CURRENT_POSITION_CHOICE;
        setTextLocation();
    }

    /**
     * Handling location selection
     * 
     * @param locationRecord object contains row from database
     */
    public void handleLocationSelection(final LocationRecord locationRecord) {
        Log.message("Enter");
        mSelectionId = locationRecord._id;
        
        if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
            handleCurrentLocation();
        } else if (mSelectionId == Constants.LOCATION_POINT_ON_MAP_CHOICE){
            handlePointOnMap();
        } else {
            Log.variable("mSelectionId", String.valueOf(mSelectionId));
            handleCustomLocation(locationRecord);
        }
    }

    /**
     * Handles custom location on the map
     * @param locationRecord object contains row from database
     */
    private void handleCustomLocation(final LocationRecord locationRecord) {
        Log.message("Enter");
        
        mLatitude = locationRecord.latitude;
        mLongitude = locationRecord.longitude;
        mLocationName = locationRecord.locationName;
        mSelectionId = locationRecord._id;
        setTextLocation();
    }
    
    /**
     * Handles edit custom location. Opens map with marker and name from selected location
     * @param locationRecord object contains row from database
     */
    public void handleEditCustomLocation(final LocationRecord locationRecord) {
        Log.message("Enter");
        Intent mapIntent = new Intent(getActivity(), MapActivity.class);
        mapIntent.putExtra(Constants.LOCATION_ACTION, Constants.LOCATION_ACTION_EDIT);
        mapIntent.putExtra(Constants.LOCATION_LATITUDE, locationRecord.latitude);
        mapIntent.putExtra(Constants.LOCATION_LONGITUDE, locationRecord.longitude);
        mapIntent.putExtra(Constants.LOCATION_RECORD_ID, locationRecord._id);
        mapIntent.putExtra(Constants.LOCATION_NAME, locationRecord.locationName);
        
        startActivityForResult(mapIntent, Constants.LOCATION_POINT_ON_MAP_CHOICE);
    }
    
    /**
     * Handles point of map selection
     */
    private void handlePointOnMap() {
        Log.message("Enter");
        getCurrentLocation();
        Intent mapIntent = new Intent(getActivity(), MapActivity.class);

        mapIntent.putExtra(Constants.LOCATION_ACTION,
                Constants.LOCATION_ACTION_ADD);
        mapIntent.putExtra(Constants.LOCATION_LATITUDE, mLatitude);
        mapIntent.putExtra(Constants.LOCATION_LONGITUDE, mLongitude);

        startActivityForResult(mapIntent,
                Constants.LOCATION_POINT_ON_MAP_CHOICE);
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
     * Set "" (empty string) for null string object
     * 
     * @param str
     *            string
     * @return empty string if null, or return string in other case
     */
    private String nullToEmpty(final String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.message("Enter");
        // TODO сделать проверку на редактирование
        if (requestCode == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            if (resultCode == Activity.RESULT_OK) {
                mLatitude = data.getDoubleExtra(Constants.LOCATION_LATITUDE,
                        0.0);
                mLongitude = data.getDoubleExtra(Constants.LOCATION_LONGITUDE,
                        0.0);
                mLocationName = data.getStringExtra(Constants.LOCATION_NAME);
                mSelectionId = data.getLongExtra(Constants.LOCATION_RECORD_ID,
                        Constants.LOCATION_POINT_ON_MAP_CHOICE);

                getTimeZoneFromGoogle();

                setTextLocation();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_sunset, container, false);
        mContext = mView.getContext();

        bindObjectsToResources();

        setOnClickListeners(mEditTextDate);
        setOnClickListeners(mButtonCalculate);
        setOnClickListeners(mEditTextLocation);
        setOnClickListeners(mLabelAstroSunrise);
        setOnClickListeners(mLabelAstroSunset);
        setOnClickListeners(mLabelCivilSunrise);
        setOnClickListeners(mLabelCivilSunset);
        setOnClickListeners(mLabelNauticalSunrise);
        setOnClickListeners(mLabelNauticalSunset);
        setOnClickListeners(mLabelOfficialSunrise);
        setOnClickListeners(mLabelOfficialSunset);

        setEditTextLongClick(mEditTextDate);
        setEditTextLongClick(mEditTextLocation);

        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble(
                    Constants.LOCATION_LATITUDE, 0.0);
            mLongitude = savedInstanceState.getDouble(
                    Constants.LOCATION_LONGITUDE, 0.0);
            mSelectionId = savedInstanceState.getLong(
                    Constants.LOCATION_SELECTION_ID,
                    Constants.LOCATION_CURRENT_POSITION_CHOICE);
            Log.variable("mSelectionId", String.valueOf(mSelectionId));
            String timeZoneId = savedInstanceState
                    .getString(Constants.LOCATION_TIMEZONE);
            if (timeZoneId == null) {
                timeZoneId = TimeZone.getDefault().getID();
            }
            mTimeZone = TimeZone.getTimeZone(timeZoneId);

            mIsVisibleResult = savedInstanceState.getBoolean(
                    Constants.LOCATION_IS_VISIVLE_RESULT, false);
            mAstroSunrise = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_ASTRO_SUNRISE));
            mAstroSunset = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_ASTRO_SUNSET));
            mCivilSunrise = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_CIVIL_SUNRISE));
            mCivilSunset = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_CIVIL_SUNSET));
            mNauticalSunrise = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_NAUTICAL_SUNRISE));
            mNauticalSunset = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_NAUTICAL_SUNSET));
            mOfficialSunrise = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_OFFICIAL_SUNRISE));
            mOfficialSunset = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_OFFICIAL_SUNSET));
            mLocationName = nullToEmpty(savedInstanceState
                    .getString(Constants.LOCATION_NAME_DATA));

        } else {
            initDate();
            setTextLocation();
        }
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
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);
        outState.putDouble(Constants.LOCATION_LATITUDE, mLatitude);
        outState.putDouble(Constants.LOCATION_LONGITUDE, mLongitude);
        outState.putLong(Constants.LOCATION_SELECTION_ID, mSelectionId);
        Log.variable("mSelectionId", String.valueOf(mSelectionId));
        
        if (mTimeZone != null) {
            outState.putString(Constants.LOCATION_TIMEZONE, mTimeZone.getID());
        }

        outState.putBoolean(Constants.LOCATION_IS_VISIVLE_RESULT,
                mIsVisibleResult);
        outState.putString(Constants.LOCATION_ASTRO_SUNRISE, mAstroSunrise);
        outState.putString(Constants.LOCATION_ASTRO_SUNSET, mAstroSunset);
        outState.putString(Constants.LOCATION_CIVIL_SUNRISE, mCivilSunrise);
        outState.putString(Constants.LOCATION_CIVIL_SUNSET, mCivilSunset);
        outState.putString(Constants.LOCATION_NAUTICAL_SUNRISE,
                mNauticalSunrise);
        outState.putString(Constants.LOCATION_NAUTICAL_SUNSET, mNauticalSunset);
        outState.putString(Constants.LOCATION_OFFICIAL_SUNRISE,
                mOfficialSunrise);
        outState.putString(Constants.LOCATION_OFFICIAL_SUNSET, mOfficialSunset);
        outState.putString(Constants.LOCATION_NAME_DATA, mLocationName);
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
    private void setDefaultTimeZone() {
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
                case R.id.sunset_label_astrolSunrise:
                    showInformationDialog(Constants.INFORMATION_ASTRO_SUNRISE);
                    break;
                case R.id.sunset_label_astrolSunset:
                    showInformationDialog(Constants.INFORMATION_ASTRO_SUNSET);
                    break;
                case R.id.sunset_label_civilSunrise:
                    showInformationDialog(Constants.INFORMATION_CIVIL_SUNRISE);
                    break;
                case R.id.sunset_label_civilSunset:
                    showInformationDialog(Constants.INFORMATION_CIVIL_SUNSET);
                    break;
                case R.id.sunset_label_nauticalSunrise:
                    showInformationDialog(Constants.INFORMATION_NAUTICAL_SUNRISE);
                    break;
                case R.id.sunset_label_nauticalSunset:
                    showInformationDialog(Constants.INFORMATION_NAUTICAL_SUNSET);
                    break;
                case R.id.sunset_label_sunrise:
                    showInformationDialog(Constants.INFORMATION_SUNRISE);
                    break;
                case R.id.sunset_label_sunset:
                    showInformationDialog(Constants.INFORMATION_SUNSET);
                    break;

                case R.id.sunset_button_calculate:
                    /* if we have coordinates, calculation sunset / sunrise */
                    if (mGeoLocationService.canGetLocation()) {
                        calculateSunset();
                        /*
                         * we dont't have coordinates and we try getting current
                         * location, show alert dialog
                         */
                    } else if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
                        showSettingsAlert();
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
     * Sets text location method
     */
    private void setTextLocation() {
        Log.message("Enter");
        if (mSelectionId == Constants.LOCATION_CURRENT_POSITION_CHOICE) {
            mEditTextLocation
                    .setText(R.string.locationSelection_label_currentLocation);
        } else if (mSelectionId == Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            mEditTextLocation
                    .setText(R.string.locationSelection_label_pointOnMap);
        } else {
            mEditTextLocation.setText(mLocationName);
        }
    }

    /**
     * Sets visible calculation result scrollView
     */
    private void setVisibleCalculculationResult() {
        if (mIsVisibleResult) {
            mOfficialSunriseResult.setText(mOfficialSunrise);
            mOfficialSunsetResult.setText(mOfficialSunset);

            mCivilSunriseResult.setText(mCivilSunrise);
            mCivilSunsetResult.setText(mCivilSunset);

            mNauticalSunriseResult.setText(mNauticalSunrise);
            mNauticalSunsetResult.setText(mNauticalSunset);

            mAstroSunriseResult.setText(mAstroSunrise);
            mAstroSunsetResult.setText(mAstroSunset);

            mSunsetTimeZoneResult.setText(mTimeZone.getID());

            mLinearLayoutCalculationResult.setVisibility(View.VISIBLE);
        } else {
            mLinearLayoutCalculationResult.setVisibility(View.GONE);
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
     * Shows information dialog with description for sunset/sunrise type
     * 
     * @param messageId
     *            Id message for displaying
     */
    private void showInformationDialog(final int messageId) {
        Log.message("Enter");
        InfoFragment infoFragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INFORMATION_MESSAGE_ID, messageId);
        infoFragment.setArguments(bundle);
        infoFragment.show(getFragmentManager(), Constants.INFORMATION_DIALOG);
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

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     * */
    private void showSettingsAlert() {
        Log.message("Enter");
        AlertFragment alertFragment = new AlertFragment();
        alertFragment.show(getFragmentManager(), Constants.ALERT_DIALOG);
    }

    /**
     * Unbinds from GeoLocationService
     */
    private void unbindFromGeoService() {
        Log.message("Enter");
        if (mBound == true) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }
}
