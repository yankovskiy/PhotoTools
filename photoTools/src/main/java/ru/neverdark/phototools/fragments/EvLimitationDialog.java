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

import java.util.List;

import kankan.wheel.widget.WheelView;
import ru.neverdark.phototools.R;
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import ru.neverdark.phototools.utils.evcalculator.EvData;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

public class EvLimitationDialog extends UfoDialogFragment {
    public static final String DIALOG_TAG = "evLimitationDialog";
    private Limit mLimitData;
    private WheelView mWheelMinAperture;
    private WheelView mWheelMinIso;
    private WheelView mWheelMinShutter;
    private WheelView mWheelMaxAperture;
    private WheelView mWheelMaxIso;
    private WheelView mWheelMaxShutter;

    /**
     * Creates dialog
     *
     * @param context
     *            application context
     * @return dialog
     */
    public static EvLimitationDialog getInstance(Context context) {
        EvLimitationDialog dialog = new EvLimitationDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.ev_limitation_dialog, null));
        mWheelMinAperture = (WheelView) getDialogView().findViewById(R.id.evLimitation_minAperture);
        mWheelMaxAperture = (WheelView) getDialogView().findViewById(R.id.evLimitation_maxAperture);
        mWheelMinIso = (WheelView) getDialogView().findViewById(R.id.evLimitation_minIso);
        mWheelMaxIso = (WheelView) getDialogView().findViewById(R.id.evLimitation_maxIso);
        mWheelMinShutter = (WheelView) getDialogView().findViewById(R.id.evLimitation_minShutter);
        mWheelMaxShutter = (WheelView) getDialogView().findViewById(R.id.evLimitation_maxShutter);
    }

    /**
     * Creates alert dialog
     */
    @Override
    protected void createDialog() {
        super.createDialog();
        loadWheelsData();
        getAlertDialog().setTitle(R.string.evLimitation);
    }

    public boolean isDataValid() {
        int minAperture = mWheelMinAperture.getCurrentItem();
        int maxAperture = mWheelMaxAperture.getCurrentItem();
        int minIso = mWheelMinIso.getCurrentItem();
        int maxIso = mWheelMaxIso.getCurrentItem();
        int minShutter = mWheelMinShutter.getCurrentItem();
        int maxShutter = mWheelMaxShutter.getCurrentItem();

        return ((minAperture < maxAperture) && (minIso < maxIso) && (minShutter < maxShutter));
    }

    /**
     * Loads data to wheels
     */
    private void loadWheelsData() {
        final int textSize = R.dimen.wheelTextSize;
        EvCalculator data = new EvCalculator();
        data.initArrays(EvData.THIRD_STOP);

        final List<String> apertures = data.getApertureList();
        final List<String> isos = data.getIsoList();
        final List<String> shutters = data.getShutterList();

        Common.setWheelAdapter(getContext(), mWheelMaxAperture, apertures, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMinAperture, apertures, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMaxIso, isos, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMinIso, isos, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMaxShutter, shutters, textSize, false);
        Common.setWheelAdapter(getContext(), mWheelMinShutter, shutters, textSize, false);

        if (mLimitData != null) {
            mWheelMinAperture.setCurrentItem(mLimitData.getMinAperture());
            mWheelMaxAperture.setCurrentItem(mLimitData.getMaxAperture());
            mWheelMinIso.setCurrentItem(mLimitData.getMinIso());
            mWheelMaxIso.setCurrentItem(mLimitData.getMaxIso());
            mWheelMinShutter.setCurrentItem(mLimitData.getMinShutter());
            mWheelMaxShutter.setCurrentItem(mLimitData.getMaxShutter());
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
        mLimitData.setMinIso(mWheelMinIso.getCurrentItem());
        mLimitData.setMaxIso(mWheelMaxIso.getCurrentItem());
        mLimitData.setMinShutter(mWheelMinShutter.getCurrentItem());
        mLimitData.setMaxShutter(mWheelMaxShutter.getCurrentItem());
    }

    /**
     * Interface provides callback method calls for OK button in the dialog
     */
    public interface OnEvLimitationListener {
        /**
         * Calls when user tap OK button
         *
         * @param data for limit values in the wheels
         */
        void onEvLimitationHandler(Limit data);
    }

    /**
     * Listener for handle OK dialog button
     */
    private class PositiveClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            OnEvLimitationListener callback = (OnEvLimitationListener) getCallback();
            if (callback != null) {
                if (isDataValid()) {
                    storeWheelsData();
                    callback.onEvLimitationHandler(mLimitData);
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
