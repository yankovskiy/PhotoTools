/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
package ru.neverdark.phototools.utils;

import ru.neverdark.phototools.R;

import com.google.android.gms.maps.model.LatLng;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Class provides an asynchronous Geocoder functionality
 */
public class AsyncGeoCoder extends AsyncTask<Void, Void, Integer> {

    /**
     * The interface provides callback methods for handle finishing Geocoder
     * process
     */
    public interface OnGeoCoderListener {
        /**
         * Called when result from Geocoder is not success
         */
        public void onGetResultFail();

        /**
         * Called when result from Geocoder is success
         * @param coordinates coordinates for founded location
         * @param searchString string for search
         */
        public void onGetResultSuccess(LatLng coordinates, String searchString);
    }

    private OnGeoCoderListener mCallback;
    private Context mContext;
    private ProgressDialog mDialog;
    private String mSearchString;
    private LatLng mCoordinates;

    /**
     * Constructor
     * 
     * @param context
     *            application context
     */
    public AsyncGeoCoder(Context context) {
        mContext = context;
    }

    /**
     * Creates and shows progress dialog
     */
    private void createDialog() {
        Log.enter();
        mDialog = new ProgressDialog(mContext);
        mDialog.setCancelable(false);
        mDialog.setTitle(R.string.progress_dialog_title);
        mDialog.setMessage(mContext.getString(R.string.progress_dialog_message));
        mDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.enter();
        int result = Constants.STATUS_FAIL;
        
        Geocoder geocoder = new Geocoder(mContext);
        
        if (geocoder.isOnline()) {
            mCoordinates = geocoder.getFromLocation(mSearchString);
            result = Constants.STATUS_SUCCESS;
        }
        
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        Log.enter();
        mDialog.dismiss();

        if (mCallback != null) {
            if (result.equals(Constants.STATUS_SUCCESS)) {
                mCallback.onGetResultSuccess(mCoordinates, mSearchString);
            } else {
                mCallback.onGetResultFail();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        Log.enter();
        createDialog();
    }

    /**
     * Sets object for calling callback function after completing Geocoder
     * process
     * 
     * @param callback
     */
    public void setCallback(OnGeoCoderListener callback) {
        mCallback = callback;
    }

    /**
     * Sets strings for searching in Geocoder by address
     * 
     * @param searchString
     *            string for search
     */
    public void setSearchString(String searchString) {
        mSearchString = searchString;
    }

}
