/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
 *     
 * 	Modification:
 * 		2014/03/03 Rudy Dordonne (rudy@itu.dk)
 ******************************************************************************/
package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.fragments.EvCompensationDialog.OnEvCompensationListener;
import ru.neverdark.phototools.fragments.EvLimitationDialog.OnEvLimitationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.EvWheelsData;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import ru.neverdark.phototools.utils.evcalculator.EvData;

/**
 * Fragment contains EV Pairs calculator UI
 */
public class EvpairsFragment extends UfoFragment {
    private static final String LIMIT_APERTURE_MIN = "ev_aperture_min_limit";
    private static final String LIMIT_APERTURE_MAX = "ev_aperture_max_limit";
    private static final String LIMIT_ISO_MIN = "ev_iso_min_limit";
    private static final String LIMIT_ISO_MAX = "ev_iso_max_limit";
    private static final String LIMIT_SHUTTER_MIN = "ev_shutter_min_limit";
    private static final String LIMIT_SHUTTER_MAX = "ev_shutter_max_limit";
    private static final String IS_LIMIT_PRESENT = "ev_is_limit_present";
    private static final String CALCULATE_INDEX = "ev1_calculateIndex";
    private static final String NEW_APERTURE_INDEX = "ev1_newAperture";
    private static final String NEW_ISO_INDEX = "ev1_newIso";
    private static final String NEW_SHUTTER_INDEX = "ev1_newShutter";
    private static final String STEP_INDEX = "ev1_stepIndex";
    private static final String EV_COMPENSATION_SHIFT = "ev_compensation_shift";
    private Context mContext;
    private boolean mIsLimit = false;
    private String CURRENT_APERTURE_INDEX = "ev1_currentAperture";
    private String CURRENT_ISO_INDEX = "ev1_currentIso";
    private String CURRENT_SHUTTER_INDEX = "ev1_currentShutter";
    private int mCalculateIndex;
    private EvCalculator mEvCalculator;
    private TextView mLabelAperture;
    private TextView mLabelIso;
    private TextView mLabelShutter;
    private TextView mLabelStepFull;
    private TextView mLabelStepHalf;
    private TextView mLabelStepThird;
    private ImageView mEvLimit;
    private ImageView mEvCompensation;
    private int mStepIndex;
    private View mView;
    private WheelView mWheel_currentAperture;
    private WheelView mWheel_currentIso;
    private WheelView mWheel_currentShutter;
    private WheelView mWheel_newAperture;
    private WheelView mWheel_newIso;
    private WheelView mWheel_newShutter;
    private Limit mLimit;
    private int mEvCompensationShift;

    @Override
    public void bindObjects() {

    }

    @Override
    public void setListeners() {

    }

    /**
     * Gets wheels index for calculates
     *
     * @return wheels index for calculates
     */
    private int getCalculateIndex() {
        return mCalculateIndex;
    }

    /**
     * Sets wheels by index for calculation
     *
     * @param calculateIndex
     *            index of wheels
     */
    private void setCalculateIndex(int calculateIndex) {
        mCalculateIndex = calculateIndex;
    }

    private int getStepIndex() {
        return mStepIndex;
    }

    private void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

    private Limit getLimit() {
        return mLimit;
    }

    private void setLimit(Limit limit) {
        mLimit = limit;
    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        mWheel_currentAperture = (WheelView) mView
                .findViewById(R.id.ev_wheel_currentAperture);
        mWheel_currentIso = (WheelView) mView
                .findViewById(R.id.ev_wheel_currentIso);
        mWheel_currentShutter = (WheelView) mView
                .findViewById(R.id.ev_wheel_currentShutter);
        mWheel_newAperture = (WheelView) mView
                .findViewById(R.id.ev_wheel_newAperture);
        mWheel_newIso = (WheelView) mView.findViewById(R.id.ev_wheel_newIso);
        mWheel_newShutter = (WheelView) mView
                .findViewById(R.id.ev_wheel_newShutter);

        mLabelStepFull = (TextView) mView.findViewById(R.id.ev_label_stepFull);
        mLabelStepHalf = (TextView) mView.findViewById(R.id.ev_label_stepHalf);
        mLabelStepThird = (TextView) mView
                .findViewById(R.id.ev_label_stepThird);

        mLabelAperture = (TextView) mView
                .findViewById(R.id.ev_label_newAperture);
        mLabelIso = (TextView) mView.findViewById(R.id.ev_label_newIso);
        mLabelShutter = (TextView) mView.findViewById(R.id.ev_label_newShutter);

        mEvLimit = (ImageView) mView.findViewById(R.id.ev_limitation);
        mEvCompensation = (ImageView) mView.findViewById(R.id.ev_compensation);
    }

    private void fillCalculatedWheel(final int index) {
        int calculateIndex = getCalculateIndex();
        if (calculateIndex == EvCalculator.CALCULATE_APERTURE) {
            mWheel_newAperture.setCurrentItem(index);
        } else if (calculateIndex == EvCalculator.CALCULATE_ISO) {
            mWheel_newIso.setCurrentItem(index);
        } else {
            mWheel_newShutter.setCurrentItem(index);
        }
    }

    /**
     * Function gets selected items positions for spinner
     */
    private EvWheelsData getSelectedItemsPositions() {
        EvWheelsData evWheelsData = new EvWheelsData();

        evWheelsData.setCurrentAperturePosition(mWheel_currentAperture
                .getCurrentItem());
        evWheelsData.setCurrentIsoPosition(mWheel_currentIso.getCurrentItem());
        evWheelsData.setCurrentShutterPosition(mWheel_currentShutter
                .getCurrentItem());

        evWheelsData.setNewAperturePosition(mWheel_newAperture.getCurrentItem());
        evWheelsData.setNewIsoPosition(mWheel_newIso.getCurrentItem());
        evWheelsData.setNewShutterPosition(mWheel_newShutter.getCurrentItem());

        return evWheelsData;
    }

    /**
     * Load saved values from shared prefs
     *
     * @return saved positions for wheels
     */
    private EvWheelsData loadSavedData() {
        EvWheelsData evWheelsData = new EvWheelsData();

        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        setEvCompensationShift(prefs.getInt(EV_COMPENSATION_SHIFT, 0));
        setStepIndex(prefs.getInt(STEP_INDEX, EvData.FULL_STOP));
        setCalculateIndex(prefs.getInt(CALCULATE_INDEX,
                EvCalculator.CALCULATE_SHUTTER));

        evWheelsData.setCurrentAperturePosition(prefs.getInt(
                CURRENT_APERTURE_INDEX, 0));
        evWheelsData.setCurrentIsoPosition(prefs.getInt(CURRENT_ISO_INDEX, 0));
        evWheelsData.setCurrentShutterPosition(prefs.getInt(CURRENT_SHUTTER_INDEX,
                0));

        evWheelsData.setNewAperturePosition(prefs.getInt(NEW_APERTURE_INDEX, 0));
        evWheelsData.setNewIsoPosition(prefs.getInt(NEW_ISO_INDEX, 0));
        evWheelsData.setNewShutterPosition(prefs.getInt(NEW_SHUTTER_INDEX, 0));

        mIsLimit = prefs.getBoolean(IS_LIMIT_PRESENT, false);

        if (mIsLimit) {
            mLimit = new Limit();
            mLimit.setMinAperture(prefs.getInt(LIMIT_APERTURE_MIN, -1));
            mLimit.setMaxAperture(prefs.getInt(LIMIT_APERTURE_MAX, -1));
            mLimit.setMinIso(prefs.getInt(LIMIT_ISO_MIN, -1));
            mLimit.setMaxIso(prefs.getInt(LIMIT_ISO_MAX, -1));
            mLimit.setMinShutter(prefs.getInt(LIMIT_SHUTTER_MIN, -1));
            mLimit.setMaxShutter(prefs.getInt(LIMIT_SHUTTER_MAX, -1));
        }

        return evWheelsData;
    }

    private void lockCalculateWheel() {
        int calculateIndex = getCalculateIndex();

        mWheel_newAperture
                .setEnabled(calculateIndex != EvCalculator.CALCULATE_APERTURE);
        mWheel_newIso.setEnabled(calculateIndex != EvCalculator.CALCULATE_ISO);
        mWheel_newShutter
                .setEnabled(calculateIndex != EvCalculator.CALCULATE_SHUTTER);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.evpairs_fragment, container, false);
        mContext = mView.getContext();
        bindObjectsToResources();
        setCyclicToWheels();

        mEvCalculator = new EvCalculator();

        EvWheelsData evWheelsData = loadSavedData();
        updateStepButtons();
        updateCalculateButtons();
        lockCalculateWheel();
        setOldWheelPositions(evWheelsData);

        setClickListener();
        wheelsHandler();
        recalculate();

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveValues();
    }

    /**
     * Calculates EV pairs
     */
    private void recalculate() {
        EvWheelsData evWheelsData = getSelectedItemsPositions();

        mEvCalculator.prepare(evWheelsData.getCurrentAperturePosition(),
                evWheelsData.getCurrentIsoPosition(),
                evWheelsData.getCurrentShutterPosition(),
                evWheelsData.getNewAperturePosition(),
                evWheelsData.getNewIsoPosition(),
                evWheelsData.getNewShutterPosition(), getCalculateIndex(),
                getEvCompensationShift());

        int index = mEvCalculator.calculate();
        boolean isError = false;

        if (index != EvData.INVALID_INDEX) {
            fillCalculatedWheel(index);
        } else {
            isError = true;
        }

        setErrorHighlight(isError);
    }

    /**
     * Save values into shared prefs
     */
    private void saveValues() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        EvWheelsData evWheelsData = getSelectedItemsPositions();

        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(EV_COMPENSATION_SHIFT, getEvCompensationShift());
        editor.putInt(STEP_INDEX, getStepIndex());
        editor.putInt(CALCULATE_INDEX, getCalculateIndex());

        editor.putInt(CURRENT_APERTURE_INDEX,
                evWheelsData.getCurrentAperturePosition());
        editor.putInt(CURRENT_ISO_INDEX, evWheelsData.getCurrentIsoPosition());
        editor.putInt(CURRENT_SHUTTER_INDEX,
                evWheelsData.getCurrentShutterPosition());

        editor.putInt(NEW_APERTURE_INDEX, evWheelsData.getNewAperturePosition());
        editor.putInt(NEW_ISO_INDEX, evWheelsData.getNewIsoPosition());
        editor.putInt(NEW_SHUTTER_INDEX, evWheelsData.getNewShutterPosition());

        mIsLimit = (getLimit() != null);
        editor.putBoolean(IS_LIMIT_PRESENT, mIsLimit);

        if (mIsLimit) {
            editor.putInt(LIMIT_APERTURE_MIN, mLimit.getMinAperture());
            editor.putInt(LIMIT_APERTURE_MAX, mLimit.getMaxAperture());
            editor.putInt(LIMIT_ISO_MIN, mLimit.getMinIso());
            editor.putInt(LIMIT_ISO_MAX, mLimit.getMaxIso());
            editor.putInt(LIMIT_SHUTTER_MIN, mLimit.getMinShutter());
            editor.putInt(LIMIT_SHUTTER_MAX, mLimit.getMaxShutter());
        }

        editor.apply();
    }

    private int getEvCompensationShift() {
        return mEvCompensationShift;
    }

    private void setEvCompensationShift(int shift) {
        mEvCompensationShift = shift;
        Log.variable("mEvCompensationShift", String.valueOf(mEvCompensationShift));
    }

    /**
     * Sets position to first for all wheels
     */
    private void setAllWheelsToFirstPos() {
        setWheelToFirstPos(mWheel_currentAperture);
        setWheelToFirstPos(mWheel_currentIso);
        setWheelToFirstPos(mWheel_currentShutter);

        setWheelToFirstPos(mWheel_newAperture);
        setWheelToFirstPos(mWheel_newIso);
        setWheelToFirstPos(mWheel_newShutter);
    }

    /**
     * Sets OnClickListener to buttons on the fragment
     */
    private void setClickListener() {
        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                case R.id.ev_label_newAperture:
                    setCalculateIndex(EvCalculator.CALCULATE_APERTURE);
                    updateCalculateButtons();
                    lockCalculateWheel();
                    recalculate();
                    break;
                case R.id.ev_label_newIso:
                    setCalculateIndex(EvCalculator.CALCULATE_ISO);
                    updateCalculateButtons();
                    lockCalculateWheel();
                    recalculate();
                    break;
                case R.id.ev_label_newShutter:
                    setCalculateIndex(EvCalculator.CALCULATE_SHUTTER);
                    updateCalculateButtons();
                    lockCalculateWheel();
                    recalculate();
                    break;
                case R.id.ev_label_stepFull:
                    setStepIndex(EvData.FULL_STOP);
                    setEvCompensationShift(0);
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepHalf:
                    setStepIndex(EvData.HALF_STOP);
                    setEvCompensationShift(0);
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepThird:
                    setStepIndex(EvData.THIRD_STOP);
                    setEvCompensationShift(0);
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_limitation:
                    showLimitationDialog();
                    break;
                case R.id.ev_compensation:
                    showEvCompensationDialog();
                    break;
                }
            }
        };

        mLabelStepFull.setOnClickListener(clickListener);
        mLabelStepHalf.setOnClickListener(clickListener);
        mLabelStepThird.setOnClickListener(clickListener);

        mLabelAperture.setOnClickListener(clickListener);
        mLabelIso.setOnClickListener(clickListener);
        mLabelShutter.setOnClickListener(clickListener);

        mEvLimit.setOnClickListener(clickListener);
        mEvLimit.setOnTouchListener(new ImageOnTouchListener());

        mEvCompensation.setOnClickListener(clickListener);
        mEvCompensation.setOnTouchListener(new ImageOnTouchListener());
    }

    private void showEvCompensationDialog() {
        EvCompensationDialog dialog = EvCompensationDialog.getInstance(mContext);
        dialog.setCallback(new EvCompensationListener());
        dialog.setStep(getStepIndex());
        dialog.setShiftIndex(getEvCompensationShift());
        dialog.show(getFragmentManager(), EvCompensationDialog.DIALOG_TAG);
    }

    /**
     * Sets wheels to cyclic
     */
    private void setCyclicToWheels() {
        mWheel_currentAperture.setCyclic(true);
        mWheel_currentIso.setCyclic(true);
        mWheel_currentShutter.setCyclic(true);

        mWheel_newAperture.setCyclic(true);
        mWheel_newIso.setCyclic(true);
        mWheel_newShutter.setCyclic(true);
    }

    /**
     * Sets red highlights for current item in calculated wheel
     *
     * @param isError
     *            true for highlights red or false for back to normal
     */
    private void setErrorHighlight(boolean isError) {
        int normal = R.drawable.wheel_val;
        int error = R.drawable.wheel_val_err;

        mWheel_newAperture.setCurrentDrawable(normal);
        mWheel_newIso.setCurrentDrawable(normal);
        mWheel_newShutter.setCurrentDrawable(normal);

        if (isError) {
            switch (getCalculateIndex()) {
            case EvCalculator.CALCULATE_APERTURE:
                mWheel_newAperture.setCurrentDrawable(error);
                break;
            case EvCalculator.CALCULATE_ISO:
                mWheel_newIso.setCurrentDrawable(error);
                break;
            case EvCalculator.CALCULATE_SHUTTER:
                mWheel_newShutter.setCurrentDrawable(error);
                break;
            }
        }
    }

    /**
     * Sets old wheel positions
     */
    private void setOldWheelPositions(EvWheelsData evWheelsData) {
        mWheel_currentAperture.setCurrentItem(evWheelsData
                .getCurrentAperturePosition());
        mWheel_currentIso.setCurrentItem(evWheelsData.getCurrentIsoPosition());
        mWheel_currentShutter.setCurrentItem(evWheelsData
                .getCurrentShutterPosition());

        mWheel_newAperture.setCurrentItem(evWheelsData.getNewAperturePosition());
        mWheel_newIso.setCurrentItem(evWheelsData.getNewIsoPosition());
        mWheel_newShutter.setCurrentItem(evWheelsData.getNewShutterPosition());
    }

    /**
     * Sets position to first
     *
     * @param wheel
     *            wheel for setting first position
     */
    private void setWheelToFirstPos(WheelView wheel) {
        wheel.setCurrentItem(0);
    }

    private void showLimitationDialog() {
        if (Constants.PAID) {
            EvLimitationDialog dialog = EvLimitationDialog
                    .getInstance(mContext);
            dialog.setCallback(new EvLimitationListener());
            dialog.setLimitData(getLimit());
            dialog.show(getFragmentManager(), EvLimitationDialog.DIALOG_TAG);
        } else {
            Common.gotoDonate(mContext);
        }
    }

    private void updateCalculateButtons() {
        Common.setBg(mLabelAperture, R.drawable.right_stroke);
        Common.setBg(mLabelIso, R.drawable.right_stroke);
        Common.setBg(mLabelShutter, R.drawable.right_stroke);

        switch (getCalculateIndex()) {
        case EvCalculator.CALCULATE_APERTURE:
            Common.setBg(mLabelAperture, R.drawable.left_green_button);
            break;
        case EvCalculator.CALCULATE_ISO:
            Common.setBg(mLabelIso, R.drawable.middle_green_button);
            break;
        case EvCalculator.CALCULATE_SHUTTER:
            Common.setBg(mLabelShutter, R.drawable.right_green_button);
            break;
        }
    }

    /**
     * Update steps
     *
     */
    private void updateStep() {
        Log.enter();

        final int textSize = R.dimen.wheelTextSize;
        if (mIsLimit) {
            mEvCalculator.initArrays(getStepIndex(), getLimit());
        } else {
            mEvCalculator.initArrays(getStepIndex());
        }

        Common.setWheelAdapter(mContext, mWheel_currentAperture,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mContext, mWheel_currentIso,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mContext, mWheel_currentShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);

        Common.setWheelAdapter(mContext, mWheel_newAperture,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mContext, mWheel_newIso,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mContext, mWheel_newShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);
    }

    private void updateStepButtons() {
        Common.setBg(mLabelStepFull, R.drawable.right_stroke);
        Common.setBg(mLabelStepHalf, R.drawable.right_stroke);
        Common.setBg(mLabelStepThird, R.drawable.right_stroke);

        switch (getStepIndex()) {
        case EvData.FULL_STOP:
            Common.setBg(mLabelStepFull, R.drawable.left_green_button);
            break;
        case EvData.HALF_STOP:
            Common.setBg(mLabelStepHalf, R.drawable.middle_green_button);
            break;
        case EvData.THIRD_STOP:
            Common.setBg(mLabelStepThird, R.drawable.right_green_button);
            break;
        }

        updateStep();
    }

    private void wheelsHandler() {
        mWheel_currentAperture.addScrollingListener(new WheelScrollListener());
        mWheel_currentIso.addScrollingListener(new WheelScrollListener());
        mWheel_currentShutter.addScrollingListener(new WheelScrollListener());
        mWheel_newAperture.addScrollingListener(new WheelScrollListener());
        mWheel_newIso.addScrollingListener(new WheelScrollListener());
        mWheel_newShutter.addScrollingListener(new WheelScrollListener());
    }

    private class EvCompensationListener implements OnEvCompensationListener, OnCallback {

        @Override
        public void onEvCompensationHandler(int compensationShift) {
            setEvCompensationShift(compensationShift);
            recalculate();
        }

    }

    /**
     * Listener for OK button in the DofLimitationDialog
     */
    private class EvLimitationListener implements OnEvLimitationListener, OnCallback {

        @Override
        public void onEvLimitationHandler(Limit data) {
            setLimit(data);

            EvWheelsData evWheelsData = new EvWheelsData();
            evWheelsData.setCurrentAperturePosition(0);
            evWheelsData.setNewAperturePosition(0);
            evWheelsData.setCurrentIsoPosition(0);
            evWheelsData.setNewIsoPosition(0);
            evWheelsData.setCurrentShutterPosition(0);
            evWheelsData.setNewShutterPosition(0);

            mIsLimit = true;

            updateStep();
            setOldWheelPositions(evWheelsData);
            recalculate();
        }
    }

    private class WheelScrollListener implements OnWheelScrollListener {

        @Override
        public void onScrollingFinished(WheelView wheel) {
            recalculate();
        }

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

    }

}
