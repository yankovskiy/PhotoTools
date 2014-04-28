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
import ru.neverdark.phototools.utils.dofcalculator.Array;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class DofLimitationDialog extends SherlockDialogFragment {

    /**
     * Interface provides callback method calls for OK button in the dialog
     */
    public interface OnDofLimitationListener {
        /**
         * Calls when user tap OK button
         * 
         * @param data
         *            for limit values in the wheels
         */
        public void onDofLimitationHandler(Limit data);
    }

    /**
     * Listener for handle OK dialog button
     */
    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mCallback != null) {
                // TODO: контроль введенных значений 
                // минимальные значения не должны быть больше, либо равны максимальным
                // в случае ошибки показывать соответствующий диалог, текущий при этом не закрывать
                storeWheelsData();

                mCallback.onDofLimitationHandler(mLimitData);
            }
        }
    }

    public static final String DIALOG_TAG = "dofLimitationDialog";

    /**
     * Creates dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static DofLimitationDialog getInstance(Context context) {
        DofLimitationDialog dialog = new DofLimitationDialog();
        dialog.mContext = context;
        return dialog;
    }

    private Context mContext;

    private OnDofLimitationListener mCallback;

    private Limit mLimitData;

    private WheelView mWheelMinAperture;
    private WheelView mWheelMaxAperture;
    private WheelView mWheelMinFocalLength;
    private WheelView mWheelMaxFocalLength;
    private WheelView mWheelMinSubjectDistance;
    private WheelView mWheelMaxSubjectDistance;

    private AlertDialog.Builder mAlertDialog;

    private View mView;

    /**
     * Binds objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.dof_limitation_dialog, null);
        mWheelMinAperture = (WheelView) mView
                .findViewById(R.id.dofLimitation_minAperture);
        mWheelMaxAperture = (WheelView) mView
                .findViewById(R.id.dofLimitation_maxAperture);
        mWheelMinFocalLength = (WheelView) mView
                .findViewById(R.id.dofLimitation_minFocalLength);
        mWheelMaxFocalLength = (WheelView) mView
                .findViewById(R.id.dofLimitation_maxFocalLength);
        mWheelMinSubjectDistance = (WheelView) mView
                .findViewById(R.id.dofLimitation_minSubjectDistance);
        mWheelMaxSubjectDistance = (WheelView) mView
                .findViewById(R.id.dofLimitation_maxSubjectDistance);
    }

    /**
     * Creates alert dialog
     */
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.dofLimitation);
    }

    /**
     * Loads data to wheels
     */
    private void loadWheelsData() {
        final int textSize = R.dimen.wheelTextSize;
        final List<String> apertures = Arrays.asList(Array.APERTURE_LIST);
        final List<String> focalLengths = Arrays.asList(Array.FOCAL_LENGTH);
        final List<String> subjectDistance = Arrays
                .asList(Array.SUBJECT_DISTANCE);

        Common.setWheelAdapter(mContext, mWheelMinAperture, apertures,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMaxAperture, apertures,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMinFocalLength, focalLengths,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMaxFocalLength, focalLengths,
                textSize, false);
        Common.setWheelAdapter(mContext, mWheelMinSubjectDistance,
                subjectDistance, textSize, false);
        Common.setWheelAdapter(mContext, mWheelMaxSubjectDistance,
                subjectDistance, textSize, false);

        if (mLimitData != null) {
            int minApertureIndex = apertures.indexOf(mLimitData
                    .getMinAperture());
            int maxApertureIndex = apertures.indexOf(mLimitData
                    .getMaxAperture());
            int minFocalLengthIndex = focalLengths.indexOf(mLimitData
                    .getMinFocalLength());
            int maxFocalLengthIndex = focalLengths.indexOf(mLimitData
                    .getMaxFocalLength());
            int minSubjectDistanceIndex = subjectDistance.indexOf(mLimitData
                    .getMinSubjectDistance());
            int maxSubjectDistanceIndex = subjectDistance.indexOf(mLimitData
                    .getMaxSubjectDistance());

            mWheelMinAperture.setCurrentItem(minApertureIndex);
            mWheelMaxAperture.setCurrentItem(maxApertureIndex);
            mWheelMinFocalLength.setCurrentItem(minFocalLengthIndex);
            mWheelMaxFocalLength.setCurrentItem(maxFocalLengthIndex);
            mWheelMinSubjectDistance.setCurrentItem(minSubjectDistanceIndex);
            mWheelMaxSubjectDistance.setCurrentItem(maxSubjectDistanceIndex);
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
    public void setCallback(OnDofLimitationListener callback) {
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
        mLimitData.setMinFocalLength((String) mWheelMinFocalLength
                .getSelectedItem());
        mLimitData.setMaxFocalLength((String) mWheelMaxFocalLength
                .getSelectedItem());
        mLimitData.setMinSubjectDistance((String) mWheelMinSubjectDistance
                .getSelectedItem());
        mLimitData.setMaxSubjectDistance((String) mWheelMaxSubjectDistance
                .getSelectedItem());
    }
}
