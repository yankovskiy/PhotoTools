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
package ru.neverdark.phototools.fragments;

import java.util.Arrays;
import java.util.List;

import kankan.wheel.widget.WheelView;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import ru.neverdark.phototools.utils.evcalculator.EvData;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EvLimitationDialog extends SherlockDialogFragment {
    /**
     * Interface provides callback method calls for OK button in the dialog
     */
    public interface OnEvLimitationListener {
        /**
         * Calls when user tap OK button
         * 
         * @param data
         *            for limit values in the wheels
         */
        public void onEvLimitationHandler(Limit data);
    }

    /**
     * Listener for handle OK dialog button
     */
    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mCallback != null) {
                storeWheelsData();

                mCallback.onEvLimitationHandler(mLimitData);
            }
        }
    }

    public static final String DIALOG_TAG = "evLimitationDialog";

    /**
     * Creates dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static EvLimitationDialog getInstance(Context context) {
        EvLimitationDialog dialog = new EvLimitationDialog();
        dialog.mContext = context;
        return dialog;
    }

    private Context mContext;

    private OnEvLimitationListener mCallback;

    private Limit mLimitData;

    private WheelView mWheelMinAperture;
    private WheelView mWheelMinIso;
    private WheelView mWheelMinShutter;
    private WheelView mWheelMaxAperture;
    private WheelView mWheelMaxIso;
    private WheelView mWheelMaxShutter;

    private AlertDialog.Builder mAlertDialog;

    private View mView;

    /**
     * Binds objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.ev_limitation_dialog, null);
        mWheelMinAperture = (WheelView) mView
                .findViewById(R.id.evLimitation_minAperture);
        mWheelMaxAperture = (WheelView) mView
                .findViewById(R.id.evLimitation_maxAperture);
        mWheelMinIso = (WheelView) mView.findViewById(R.id.evLimitation_minIso);
        mWheelMaxIso = (WheelView) mView.findViewById(R.id.evLimitation_maxIso);
        mWheelMinShutter = (WheelView) mView
                .findViewById(R.id.evLimitation_minShutter);
        mWheelMaxShutter = (WheelView) mView
                .findViewById(R.id.evLimitation_maxShutter);
    }

    /**
     * Creates alert dialog
     */
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.evLimitation);
    }

    /**
     * Loads data to wheels
     */
    private void loadWheelsData() {
        final int textSize = R.dimen.wheelTextSize;
        EvCalculator data = new EvCalculator();
        data.initArrays(EvData.FULL_STOP);

        final List<String> apertures = Arrays.asList(data.getApertureList());
        final List<String> isos = Arrays.asList(data.getIsoList());
        final List<String> shutters = Arrays.asList(data.getShutterList());

        Common.setWheelAdapter(mContext, mWheelMaxAperture, apertures,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMinAperture, apertures,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMaxIso, isos, textSize, false);
        Common.setWheelAdapter(mContext, mWheelMinIso, isos, textSize, false);
        Common.setWheelAdapter(mContext, mWheelMaxShutter, shutters, textSize,
                false);
        Common.setWheelAdapter(mContext, mWheelMinShutter, shutters, textSize,
                false);

        if (mLimitData != null) {
            int minApertureIndex = apertures.indexOf(mLimitData
                    .getMinAperture());
            int maxApertureIndex = apertures.indexOf(mLimitData
                    .getMaxAperture());
            int minIsoIndex = isos.indexOf(mLimitData.getMinIso());
            int maxIsoIndex = isos.indexOf(mLimitData.getMaxIso());
            int minShutterIndex = shutters.indexOf(mLimitData.getMinShutter());
            int maxShutterIndex = shutters.indexOf(mLimitData.getMaxShutter());

            mWheelMinAperture.setCurrentItem(minApertureIndex);
            mWheelMaxAperture.setCurrentItem(maxApertureIndex);
            mWheelMinIso.setCurrentItem(minIsoIndex);
            mWheelMaxIso.setCurrentItem(maxIsoIndex);
            mWheelMinShutter.setCurrentItem(minShutterIndex);
            mWheelMaxShutter.setCurrentItem(maxShutterIndex);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjectToResource();
        loadWheelsData();
        createDialog();
        setListeners();

        return mAlertDialog.create();
    }

    /**
     * Sets callback object for handling OK button on the dialog
     * 
     * @param callback
     *            callback object
     */
    public void setCallback(OnEvLimitationListener callback) {
        mCallback = callback;
    }

    /**
     * Sets limitation data for loading
     * 
     * @param data
     *            data for loading or null for default value
     */
    public void setLimitData(Limit data) {
        mLimitData = data;
    }

    /**
     * Sets listeners for objects
     */
    private void setListeners() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok,
                new PositiveClickListener());
    }

    /**
     * Stores wheels data to Limit object
     */
    private void storeWheelsData() {
        if (mLimitData == null) {
            mLimitData = new Limit();
        }

        mLimitData.setMinAperture((String) mWheelMinAperture.getSelectedItem());
        mLimitData.setMaxAperture((String) mWheelMaxAperture.getSelectedItem());
        mLimitData.setMinIso((String) mWheelMinIso.getSelectedItem());
        mLimitData.setMaxIso((String) mWheelMaxIso.getSelectedItem());
        mLimitData.setMinShutter((String) mWheelMinShutter.getSelectedItem());
        mLimitData.setMaxShutter((String) mWheelMaxShutter.getSelectedItem());
    }
}
