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

import kankan.wheel.widget.WheelView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment contains EV Pairs calculator UI
 */
public class EvpairsFragment extends SherlockFragment {
    private static final int CALCULATE_APERTURE = 0;
    private static final int CALCULATE_ISO = 1;
    private static final int CALCULATE_SHUTTER = 2;
    
    private static final String CALCULATE_INDEX = "ev_calculateIndex";
    
    /** how much allow filled fields (new fields) */
    private static final byte MAXIMUM_ALLOWED_FILLED_FIELDS = 2;
    
    private static final String NEW_APERTURE_INDEX = "ev_newAperture";
    private static final String NEW_ISO_INDEX = "ev_newIso";
    private static final String NEW_SHUTTER_INDEX = "ev_newShutter";
    
    private static final String STEP_INDEX = "ev_stepIndex";
    
    private String CURRENT_APERTURE_INDEX = "ev_currentAperture";
    private String CURRENT_ISO_INDEX = "ev_currentIso";
    private String CURRENT_SHUTTER_INDEX = "ev_currentShutter";

    private SherlockFragmentActivity mActivity;
    
    private int mCalculateIndex;
    
    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    
    private EvCalculator mEvCalculator;
    
    private TextView mLabelAperture;
    private TextView mLabelIso;
    private TextView mLabelShutter;
    private TextView mLabelStepFull;
    private TextView mLabelStepHalf;
    private TextView mLabelStepThird;

    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;
    
    private int mStepIndex;
    
    private View mView;
    
    private WheelView mWheel_currentAperture;
    private WheelView mWheel_currentIso;
    private WheelView mWheel_currentShutter;
    
    private WheelView mWheel_newAperture;
    private WheelView mWheel_newIso;
    private WheelView mWheel_newShutter;

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

        mActivity = getSherlockActivity();
    }

    /**
     * Calculates EV pairs
     */
    private void calculate() {
        getSelectedItemsPositions();

        if (isOnlyOneFieldEmpty() == true) {
            mEvCalculator.prepare(mCurrentAperturePosition,
                    mCurrentIsoPosition, mCurrentShutterSpeedPosition,
                    mNewAperturePosition, mNewIsoPostion,
                    mNewShutterSpeedPosition);

            int index = mEvCalculator.calculate();

            if (index != EvData.INVALID_INDEX) {
                fillEmptyWheel(index);
            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.evpairs_error_calculationProblem),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Clears new values
     */
    private void clearNewValues() {
        mWheel_newAperture.setCurrentItem(0);
        mWheel_newIso.setCurrentItem(0);
        mWheel_newShutter.setCurrentItem(0);
    }

    /**
     * Function determine empty wheel and sets selelection for him.
     * 
     * @param index
     *            - item index getting from calculations
     */
    private void fillEmptyWheel(final int index) {
        WheelView wheel;

        if (mNewAperturePosition == 0) {
            wheel = (WheelView) mView.findViewById(R.id.ev_wheel_newAperture);
        } else if (mNewIsoPostion == 0) {
            wheel = (WheelView) mView.findViewById(R.id.ev_wheel_newIso);
        } else {
            wheel = (WheelView) mView.findViewById(R.id.ev_wheel_newShutter);
        }

        wheel.setCurrentItem(index);
    }

    /**
     * Function gets selected items positions for spinner
     */
    private void getSelectedItemsPositions() {
        mCurrentAperturePosition = mWheel_currentAperture.getCurrentItem();
        mCurrentIsoPosition = mWheel_currentIso.getCurrentItem();
        mCurrentShutterSpeedPosition = mWheel_currentShutter.getCurrentItem();

        mNewAperturePosition = mWheel_newAperture.getCurrentItem();
        mNewIsoPostion = mWheel_newIso.getCurrentItem();
        mNewShutterSpeedPosition = mWheel_newShutter.getCurrentItem();
    }

    /**
     * Verifies that only one new fields are empty. Function display an error
     * message if new fields does not contain only one empty field
     * 
     * @return true if new fields contain only one empty field
     */
    private boolean isOnlyOneFieldEmpty() {
        boolean isOnlyOneEmpty = false;
        byte notEmptyCount = 0;

        if (mNewAperturePosition > 0) {
            notEmptyCount++;
        }

        if (mNewIsoPostion > 0) {
            notEmptyCount++;
        }

        if (mNewShutterSpeedPosition > 0) {
            notEmptyCount++;
        }

        if (notEmptyCount != MAXIMUM_ALLOWED_FILLED_FIELDS) {
            Toast.makeText(getActivity(),
                    getString(R.string.evpairs_error_onlyOneFieldMustBeEpmty),
                    Toast.LENGTH_SHORT).show();
        } else {
            isOnlyOneEmpty = true;
        }

        return isOnlyOneEmpty;
    }

    /**
     * Load saved values from shared prefs
     */
    private void loadValues() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        mStepIndex = preferenced.getInt(STEP_INDEX, EvData.FULL_STOP);
        mCalculateIndex = preferenced
                .getInt(CALCULATE_INDEX, CALCULATE_SHUTTER);

        mCurrentAperturePosition = preferenced
                .getInt(CURRENT_APERTURE_INDEX, 0);
        mCurrentIsoPosition = preferenced.getInt(CURRENT_ISO_INDEX, 0);
        mCurrentShutterSpeedPosition = preferenced.getInt(
                CURRENT_SHUTTER_INDEX, 0);

        mNewAperturePosition = preferenced.getInt(NEW_APERTURE_INDEX, 0);
        mNewIsoPostion = preferenced.getInt(NEW_ISO_INDEX, 0);
        mNewShutterSpeedPosition = preferenced.getInt(NEW_SHUTTER_INDEX, 0);
    }

    private void lockCalculateWheel() {
        mWheel_newAperture.setEnabled(mCalculateIndex != CALCULATE_APERTURE);
        mWheel_newIso.setEnabled(mCalculateIndex != CALCULATE_ISO);
        mWheel_newShutter.setEnabled(mCalculateIndex != CALCULATE_SHUTTER);
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

        loadValues();
        updateStepButtons();
        updateCalculateButtons();
        lockCalculateWheel();
        setOldWheelPositions();

        setClickListener();

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveValues();
    }

    /**
     * Save values into shared prefs
     */
    private void saveValues() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        getSelectedItemsPositions();

        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(STEP_INDEX, mStepIndex);
        editor.putInt(CALCULATE_INDEX, mCalculateIndex);

        editor.putInt(CURRENT_APERTURE_INDEX, mCurrentAperturePosition);
        editor.putInt(CURRENT_ISO_INDEX, mCurrentIsoPosition);
        editor.putInt(CURRENT_SHUTTER_INDEX, mCurrentShutterSpeedPosition);

        editor.putInt(NEW_APERTURE_INDEX, mNewAperturePosition);
        editor.putInt(NEW_ISO_INDEX, mNewIsoPostion);
        editor.putInt(NEW_SHUTTER_INDEX, mNewShutterSpeedPosition);

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
                    mCalculateIndex = CALCULATE_APERTURE;
                    updateCalculateButtons();
                    lockCalculateWheel();
                    break;
                case R.id.ev_label_newIso:
                    mCalculateIndex = CALCULATE_ISO;
                    updateCalculateButtons();
                    lockCalculateWheel();
                    break;
                case R.id.ev_label_newShutter:
                    mCalculateIndex = CALCULATE_SHUTTER;
                    updateCalculateButtons();
                    lockCalculateWheel();
                    break;
                case R.id.ev_label_stepFull:
                    mStepIndex = EvData.FULL_STOP;
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepHalf:
                    mStepIndex = EvData.HALF_STOP;
                    updateStepButtons();
                    setAllWheelsToFirstPos();
                    break;
                case R.id.ev_label_stepThird:
                    mStepIndex = EvData.THIRD_STOP;
                    updateStepButtons();
                    setAllWheelsToFirstPos();
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
     * Sets old wheel positions
     */
    private void setOldWheelPositions() {
        mWheel_currentAperture.setCurrentItem(mCurrentAperturePosition);
        mWheel_currentIso.setCurrentItem(mCurrentIsoPosition);
        mWheel_currentShutter.setCurrentItem(mCurrentShutterSpeedPosition);

        mWheel_newAperture.setCurrentItem(mNewAperturePosition);
        mWheel_newIso.setCurrentItem(mNewIsoPostion);
        mWheel_newShutter.setCurrentItem(mNewShutterSpeedPosition);
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

    private void updateCalculateButtons() {
        Common.setBg(mLabelAperture, R.drawable.right_stroke);
        Common.setBg(mLabelIso, R.drawable.right_stroke);
        Common.setBg(mLabelShutter, R.drawable.right_stroke);

        switch (mCalculateIndex) {
        case CALCULATE_APERTURE:
            Common.setBg(mLabelAperture, R.drawable.left_blue_button);
            break;
        case CALCULATE_ISO:
            Common.setBg(mLabelIso, R.drawable.middle_blue_button);
            break;
        case CALCULATE_SHUTTER:
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
        mEvCalculator.initArrays(mStepIndex);

        Common.setWheelAdapter(mActivity, mWheel_currentAperture,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_currentIso,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mActivity, mWheel_currentShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);

        Common.setWheelAdapter(mActivity, mWheel_newAperture,
                mEvCalculator.getApertureList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_newIso,
                mEvCalculator.getIsoList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_newShutter,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                true);
    }

    private void updateStepButtons() {
        Common.setBg(mLabelStepFull, R.drawable.right_stroke);
        Common.setBg(mLabelStepHalf, R.drawable.right_stroke);
        Common.setBg(mLabelStepThird, R.drawable.right_stroke);

        switch (mStepIndex) {
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
}
