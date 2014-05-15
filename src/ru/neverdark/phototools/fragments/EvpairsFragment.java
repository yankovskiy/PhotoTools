/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.fragments.EvLimitationDialog.OnEvLimitationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Common.MinMaxValues;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import ru.neverdark.phototools.utils.evcalculator.EvData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment contains EV Pairs calculator UI
 */
public class EvpairsFragment extends SherlockFragment {
    /**
     * Listener for OK button in the DofLimitationDialog
     */
    private class EvLimitationListener implements OnEvLimitationListener {

        @Override
        public void onEvLimitationHandler(Limit data) {
            setLimit(data);
            MinMaxValues minMaxApertures = MinMaxValues.getMinMax(mWheel_currentAperture);
            MinMaxValues minMaxIsos = MinMaxValues.getMinMax(mWheel_currentIso);
            MinMaxValues minMaxShutters = MinMaxValues.getMinMax(mWheel_currentShutter);

            SavedData savedData = getSelectedItemsPositions();
            
            if (minMaxApertures.getMinValue().equals(data.getMinAperture()) == false || 
                    minMaxApertures.getMaxValue().equals(data.getMaxAperture()) == false) {
                savedData.setCurrentAperturePosition(0);
                savedData.setNewAperturePosition(0);
            } 
            
            if (minMaxIsos.getMinValue().equals(data.getMinIso()) == false || 
                    minMaxIsos.getMaxValue().equals(data.getMaxIso()) == false) {
                savedData.setCurrentIsoPosition(0);
                savedData.setNewIsoPosition(0);
            }
            
            if (minMaxShutters.getMinValue().equals(data.getMinAperture()) == false ||
                    minMaxShutters.getMaxValue().equals(data.getMaxShutter()) == false) {
                savedData.setCurrentShutterPosition(0);
                savedData.setNewShutterPosition(0);
            }
            
            /*
             * TODO добавить загрузку необходимого набора данных
             * добавить позиционирование на новых элементах
             * добавить перерасчет
             */
        }
    }

    private class WheelScrollListener implements OnWheelScrollListener {

        @Override
        public void onScrollingFinished(WheelView wheel) {
            recalculate();
        }

        @Override
        public void onScrollingStarted(WheelView wheel) {
            // TODO Auto-generated method stub

        }

    }

    private static final String LIMIT_APERTURE_MIN = "ev_aperture_min_limit";
    private static final String LIMIT_APERTURE_MAX = "ev_aperture_max_limit";
    private static final String LIMIT_ISO_MIN = "ev_iso_min_limit";
    private static final String LIMIT_ISO_MAX = "ev_iso_max_limit";
    private static final String LIMIT_SHUTTER_MIN = "ev_shutter_min_limit";
    private static final String LIMIT_SHUTTER_MAX = "ev_shutter_max_limit";

    
    private static final String CALCULATE_INDEX = "ev1_calculateIndex";

    private static final String NEW_APERTURE_INDEX = "ev1_newAperture";
    private static final String NEW_ISO_INDEX = "ev1_newIso";
    private static final String NEW_SHUTTER_INDEX = "ev1_newShutter";

    private static final String STEP_INDEX = "ev1_stepIndex";

    private String CURRENT_APERTURE_INDEX = "ev1_currentAperture";
    private String CURRENT_ISO_INDEX = "ev1_currentIso";
    private String CURRENT_SHUTTER_INDEX = "ev1_currentShutter";

    private SherlockFragmentActivity mActivity;

    private int mCalculateIndex;
    
    /**
     * Gets wheels index for calculates
     * @return
     */
    private int getCalculateIndex() {
        return mCalculateIndex;
    }
    
    /**
     * Sets wheels by index for calculation
     * @param calculateIndex index of wheels
     */
    private void setCalculateIndex(int calculateIndex) {
        mCalculateIndex = calculateIndex;
    }

    private EvCalculator mEvCalculator;

    private TextView mLabelAperture;
    private TextView mLabelIso;
    private TextView mLabelShutter;
    private TextView mLabelStepFull;
    private TextView mLabelStepHalf;
    private TextView mLabelStepThird;

    private ImageView mEvLimit;

    private int mStepIndex;
    
    private void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }
    
    private int getStepIndex() {
        return mStepIndex;
    }

    private View mView;

    private WheelView mWheel_currentAperture;
    private WheelView mWheel_currentIso;
    private WheelView mWheel_currentShutter;

    private WheelView mWheel_newAperture;
    private WheelView mWheel_newIso;
    private WheelView mWheel_newShutter;

    private Limit mLimit;
    
    private void setLimit(Limit limit) {
        mLimit = limit;
    }
    
    private Limit getLimit() {
        return mLimit;
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

        mActivity = getSherlockActivity();
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
    private SavedData getSelectedItemsPositions() {
        SavedData savedData = new SavedData();
        
        savedData.setCurrentAperturePosition(mWheel_currentAperture.getCurrentItem());
        savedData.setCurrentIsoPosition(mWheel_currentIso.getCurrentItem());
        savedData.setCurrentShutterPosition(mWheel_currentShutter.getCurrentItem());

        savedData.setNewAperturePosition(mWheel_newAperture.getCurrentItem());
        savedData.setNewIsoPosition(mWheel_newIso.getCurrentItem());
        savedData.setNewShutterPosition(mWheel_newShutter.getCurrentItem());
        
        return savedData;
    }

    private class SavedData {
        private int mCurrentAperturePosition;
        private int mCurrentIsoPosition;
        private int mCurrentShutterPosition;
        
        private int mNewAperturePosition;
        private int mNewIsoPosition;
        private int mNewShutterPosition;
        
        public int getCurrentAperturePosition() {
            return mCurrentAperturePosition;
        }
        public void setCurrentAperturePosition(int currentAperturePosition) {
            this.mCurrentAperturePosition = currentAperturePosition;
        }
        public int getCurrentIsoPosition() {
            return mCurrentIsoPosition;
        }
        public void setCurrentIsoPosition(int currentIsoPosition) {
            this.mCurrentIsoPosition = currentIsoPosition;
        }
        public int getCurrentShutterPosition() {
            return mCurrentShutterPosition;
        }
        public void setCurrentShutterPosition(int currentShutterPosition) {
            this.mCurrentShutterPosition = currentShutterPosition;
        }
        public int getNewAperturePosition() {
            return mNewAperturePosition;
        }
        public void setNewAperturePosition(int newAperturePosition) {
            this.mNewAperturePosition = newAperturePosition;
        }
        public int getNewIsoPosition() {
            return mNewIsoPosition;
        }
        public void setNewIsoPosition(int newIsoPosition) {
            this.mNewIsoPosition = newIsoPosition;
        }
        public int getNewShutterPosition() {
            return mNewShutterPosition;
        }
        public void setNewShutterPosition(int newShutterPosition) {
            this.mNewShutterPosition = newShutterPosition;
        }
    }
    
    /**
     * Load saved values from shared prefs
     * @return saved positions for wheels
     */
    private SavedData loadSavedData() {
        SavedData savedData = new SavedData();
        
        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        setStepIndex(prefs.getInt(STEP_INDEX, EvData.FULL_STOP));
        setCalculateIndex(prefs.getInt(CALCULATE_INDEX,
                EvCalculator.CALCULATE_SHUTTER));

        savedData.setCurrentAperturePosition(prefs.getInt(CURRENT_APERTURE_INDEX, 0));
        savedData.setCurrentIsoPosition(prefs.getInt(CURRENT_ISO_INDEX, 0));
        savedData.setCurrentShutterPosition(prefs.getInt(CURRENT_SHUTTER_INDEX, 0));

        savedData.setNewAperturePosition(prefs.getInt(NEW_APERTURE_INDEX, 0));
        savedData.setNewIsoPosition(prefs.getInt(NEW_ISO_INDEX, 0));
        savedData.setNewShutterPosition(prefs.getInt(NEW_SHUTTER_INDEX, 0));
        
        // TODO добавить загрузку минимальных и максимальных значений в колесах
        
        return savedData;
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
        mView = inflater.inflate(R.layout.activity_evpairs, container, false);
        bindObjectsToResources();
        setCyclicToWheels();

        mEvCalculator = new EvCalculator();

        SavedData savedData = loadSavedData();
        updateStepButtons();
        updateCalculateButtons();
        lockCalculateWheel();
        setOldWheelPositions(savedData);

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
        SavedData savedData = getSelectedItemsPositions();

        mEvCalculator.prepare(savedData.getCurrentAperturePosition(), savedData.getCurrentIsoPosition(),
                savedData.getCurrentShutterPosition(), savedData.getNewAperturePosition(),
                savedData.getNewIsoPosition(), savedData.getNewShutterPosition(), getCalculateIndex());

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

        SavedData savedData = getSelectedItemsPositions();

        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(STEP_INDEX, getStepIndex());
        editor.putInt(CALCULATE_INDEX, getCalculateIndex());

        editor.putInt(CURRENT_APERTURE_INDEX, savedData.getCurrentAperturePosition());
        editor.putInt(CURRENT_ISO_INDEX, savedData.getCurrentIsoPosition());
        editor.putInt(CURRENT_SHUTTER_INDEX, savedData.getCurrentShutterPosition());

        editor.putInt(NEW_APERTURE_INDEX, savedData.getNewAperturePosition());
        editor.putInt(NEW_ISO_INDEX, savedData.getNewIsoPosition());
        editor.putInt(NEW_SHUTTER_INDEX, savedData.getNewShutterPosition());

        // TODO добавить сохранение минимальных и максимальных значений в колесах
        editor.commit();
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
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepHalf:
                    setStepIndex(EvData.HALF_STOP);
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepThird:
                    setStepIndex(EvData.THIRD_STOP);
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_limitation:
                    showLimitationDialog();
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
    private void setOldWheelPositions(SavedData savedData) {
        mWheel_currentAperture.setCurrentItem(savedData.getCurrentAperturePosition());
        mWheel_currentIso.setCurrentItem(savedData.getCurrentIsoPosition());
        mWheel_currentShutter.setCurrentItem(savedData.getCurrentShutterPosition());

        mWheel_newAperture.setCurrentItem(savedData.getNewAperturePosition());
        mWheel_newIso.setCurrentItem(savedData.getNewIsoPosition());
        mWheel_newShutter.setCurrentItem(savedData.getNewShutterPosition());
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
                    .getInstance(mActivity);
            dialog.setCallback(new EvLimitationListener());
            dialog.setLimitData(getLimit());
            dialog.show(getFragmentManager(), EvLimitationDialog.DIALOG_TAG);
        } else {
            ShowMessageDialog dialog = ShowMessageDialog.getInstance(mActivity);
            dialog.setMessages(R.string.error,
                    R.string.error_availableOnlyInPaid);
            dialog.show(getFragmentManager(), ShowMessageDialog.DIALOG_TAG);
        }
    }

    private void updateCalculateButtons() {
        Common.setBg(mLabelAperture, R.drawable.right_stroke);
        Common.setBg(mLabelIso, R.drawable.right_stroke);
        Common.setBg(mLabelShutter, R.drawable.right_stroke);

        switch (getCalculateIndex()) {
        case EvCalculator.CALCULATE_APERTURE:
            Common.setBg(mLabelAperture, R.drawable.left_blue_button);
            break;
        case EvCalculator.CALCULATE_ISO:
            Common.setBg(mLabelIso, R.drawable.middle_blue_button);
            break;
        case EvCalculator.CALCULATE_SHUTTER:
            Common.setBg(mLabelShutter, R.drawable.right_blue_button);
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
        mEvCalculator.initArrays(getStepIndex());

        Common.setWheelAdapter(mActivity, mWheel_currentAperture,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_currentIso,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_currentShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);

        Common.setWheelAdapter(mActivity, mWheel_newAperture,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_newIso,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_newShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);
    }

    private void updateStepButtons() {
        Common.setBg(mLabelStepFull, R.drawable.right_stroke);
        Common.setBg(mLabelStepHalf, R.drawable.right_stroke);
        Common.setBg(mLabelStepThird, R.drawable.right_stroke);

        switch (getStepIndex()) {
        case EvData.FULL_STOP:
            Common.setBg(mLabelStepFull, R.drawable.left_blue_button);
            break;
        case EvData.HALF_STOP:
            Common.setBg(mLabelStepHalf, R.drawable.middle_blue_button);
            break;
        case EvData.THIRD_STOP:
            Common.setBg(mLabelStepThird, R.drawable.right_blue_button);
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
}
