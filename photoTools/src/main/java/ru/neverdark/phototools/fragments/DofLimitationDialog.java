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
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.dofcalculator.Array;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

public class DofLimitationDialog extends UfoDialogFragment {

    public static final String DIALOG_TAG = "dofLimitationDialog";
    private Limit mLimitData;
    private WheelView mWheelMinAperture;
    private WheelView mWheelMaxAperture;
    private WheelView mWheelMinFocalLength;
    private WheelView mWheelMaxFocalLength;
    private WheelView mWheelMinSubjectDistance;
    private WheelView mWheelMaxSubjectDistance;

    /**
     * Creates dialog
     *
     * @param context
     *            application context
     * @return dialog
     */
    public static DofLimitationDialog getInstance(Context context) {
        DofLimitationDialog dialog = new DofLimitationDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.dof_limitation_dialog, null));
        mWheelMinAperture = (WheelView) getDialogView()
                .findViewById(R.id.dofLimitation_minAperture);
        mWheelMaxAperture = (WheelView) getDialogView()
                .findViewById(R.id.dofLimitation_maxAperture);
        mWheelMinFocalLength = (WheelView) getDialogView().findViewById(
                R.id.dofLimitation_minFocalLength);
        mWheelMaxFocalLength = (WheelView) getDialogView().findViewById(
                R.id.dofLimitation_maxFocalLength);
        mWheelMinSubjectDistance = (WheelView) getDialogView().findViewById(
                R.id.dofLimitation_minSubjectDistance);
        mWheelMaxSubjectDistance = (WheelView) getDialogView().findViewById(
                R.id.dofLimitation_maxSubjectDistance);
    }

    /**
     * Creates alert dialog
     */
    @Override
    protected void createDialog() {
        super.createDialog();
        loadWheelsData();
        getAlertDialog().setTitle(R.string.dofLimitation);
    }

    /**
     * Checks input data for valid values
     *
     * @return true if input data is valid
     */
    private boolean isDataValid() {
        int minAperture = mWheelMinAperture.getCurrentItem();
        int maxAperture = mWheelMaxAperture.getCurrentItem();
        int minFocalLength = mWheelMinFocalLength.getCurrentItem();
        int maxFocalLength = mWheelMaxFocalLength.getCurrentItem();
        int minSubjectDistance = mWheelMinSubjectDistance.getCurrentItem();
        int maxSubjectDistance = mWheelMaxSubjectDistance.getCurrentItem();

        return ((minAperture < maxAperture) && (minFocalLength < maxFocalLength) && (minSubjectDistance < maxSubjectDistance));
    }

    /**
     * Loads data to wheels
     */
    private void loadWheelsData() {
        final int textSize = R.dimen.wheelTextSize;
        final List<String> apertures = Arrays.asList(Array.APERTURE_LIST);
        final List<String> focalLengths = Arrays.asList(Array.FOCAL_LENGTH);
        final List<String> subjectDistance = Arrays.asList(Array.SUBJECT_DISTANCE);

        Common.setWheelAdapter(getContext(), mWheelMinAperture, apertures, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMaxAperture, apertures, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMinFocalLength, focalLengths, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMaxFocalLength, focalLengths, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMinSubjectDistance, subjectDistance, textSize,
                false);
        Common.setWheelAdapter(getContext(), mWheelMaxSubjectDistance, subjectDistance, textSize,
                false);

        if (mLimitData != null) {
            mWheelMinAperture.setCurrentItem(mLimitData.getMinAperture());
            mWheelMaxAperture.setCurrentItem(mLimitData.getMaxAperture());
            mWheelMinFocalLength.setCurrentItem(mLimitData.getMinFocalLength());
            mWheelMaxFocalLength.setCurrentItem(mLimitData.getMaxFocalLength());
            mWheelMinSubjectDistance.setCurrentItem(mLimitData.getMinSubjectDistance());
            mWheelMaxSubjectDistance.setCurrentItem(mLimitData.getMaxSubjectDistance());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new PositiveClickListener());
        }
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

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, null);
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

    /**
     * Stores wheels data to Limit object
     */
    private void storeWheelsData() {
        if (mLimitData == null) {
            mLimitData = new Limit();
        }

        mLimitData.setMinAperture(mWheelMinAperture.getCurrentItem());
        mLimitData.setMaxAperture(mWheelMaxAperture.getCurrentItem());
        mLimitData.setMinFocalLength(mWheelMinFocalLength.getCurrentItem());
        mLimitData.setMaxFocalLength(mWheelMaxFocalLength.getCurrentItem());
        mLimitData.setMinSubjectDistance(mWheelMinSubjectDistance.getCurrentItem());
        mLimitData.setMaxSubjectDistance(mWheelMaxSubjectDistance.getCurrentItem());
    }

    /**
     * Interface provides callback method calls for OK button in the dialog
     */
    public interface OnDofLimitationListener {
        /**
         * Calls when user tap OK button
         *
         * @param data for limit values in the wheels
         */
        void onDofLimitationHandler(Limit data);
    }

    /**
     * Listener for handle OK dialog button
     */
    private class PositiveClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            OnDofLimitationListener callback = (OnDofLimitationListener) getCallback();
            if (callback != null) {
                if (isDataValid()) {
                    storeWheelsData();
                    callback.onDofLimitationHandler(mLimitData);
                    dismiss();
                } else {
                    ShowMessageDialog errorDialog = ShowMessageDialog.getInstance(getContext());
                    errorDialog.setMessages(R.string.error, R.string.error_minMaxIncorrect);
                    errorDialog.show(getFragmentManager(), ShowMessageDialog.DIALOG_TAG);
                }
            }
        }
    }
}
