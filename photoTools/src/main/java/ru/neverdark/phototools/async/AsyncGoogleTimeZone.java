package ru.neverdark.phototools.async;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.TimeZone;

import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.GoogleTimeZone;

/**
 * Class for asynchronous determining timezone from Google servers
 */

public class AsyncGoogleTimeZone extends AsyncTask<Void, Void, Integer>{
    private final OnGoogleTimezoneListener mCallback;
    private final Calendar mCalendar;
    private final LatLng mLocation;
    private TimeZone mTimeZone;

    /**
     * Interface for UI interaction
     */
    public interface OnGoogleTimezoneListener {
        void onGetResultSuccess(TimeZone tz);
        void onGetResultFail();
        void onPreExecute();
        void onPostExecute();
    }

    /**
     * Constructor
     * @param calendar calendar contains date for determining tz
     * @param location location for determining tz
     * @param callback callback listener
     */
    public AsyncGoogleTimeZone(Calendar calendar, LatLng location, OnGoogleTimezoneListener callback) {
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
            mCallback.onGetResultSuccess(mTimeZone);
        } else if (status == Constants.STATUS_FAIL) {
            mCallback.onGetResultFail();
        }
        mCallback.onPostExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        GoogleTimeZone gtz = new GoogleTimeZone();
        mTimeZone = gtz.getTimeZone(mCalendar, mLocation);

        if (mTimeZone != null) {
            return Constants.STATUS_SUCCESS;
        } else {
            return Constants.STATUS_FAIL;
        }
    }
}
