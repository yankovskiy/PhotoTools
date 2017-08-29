package ru.neverdark.phototools.async;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GoogleTimeZone;
import ru.neverdark.sunmooncalc.SunriseSunsetCalculator;

/**
 * Async calculate sun parameters
 */
public class AsyncCalculator extends AsyncTask<Void, Void, Integer>{
    private final OnCalculatorListener mCallback;
    private final Calendar mCalendar;
    private final LatLng mLocation;
    private SunriseSunsetCalculator mSunSetCalc;

    /**
     * Interface for UI interaction
     */
    public interface OnCalculatorListener {
        void onGetResultFail();
        void onPreExecute();
        void onPostExecute();
        void onGetResultSuccess(SunriseSunsetCalculator calculator);
    }

    private static final String TAG = "AsyncCalculator";

    @Override
    protected Integer doInBackground(Void... params) {
        int status = Constants.STATUS_SUCCESS;

        GoogleTimeZone gtz = new GoogleTimeZone();
        TimeZone tz = gtz.getTimeZone(mCalendar, mLocation);

        if (tz == null) {
            status = Constants.STATUS_FAIL;
        }

        if (status != Constants.STATUS_FAIL) {
            Calendar calendar = Calendar.getInstance(tz);
            Common.copyCalendarWithoutTz(mCalendar, calendar);
            mSunSetCalc = new SunriseSunsetCalculator(mLocation, calendar);
        }

        return status;
    }

    public AsyncCalculator(Calendar calendar, LatLng location, OnCalculatorListener callback) {
        super();
        mCallback = callback;
        mCalendar = calendar;
        mLocation = location;
    }

    @Override
    protected void onPreExecute() {
        mCallback.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer status) {
        if (status == Constants.STATUS_SUCCESS) {
            mCallback.onGetResultSuccess(mSunSetCalc);
        } else if (status == Constants.STATUS_FAIL) {
            mCallback.onGetResultFail();
        }
        mCallback.onPostExecute();
    }
}
