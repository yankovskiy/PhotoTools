/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import kankan.wheel.widget.WheelView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
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
    private View mView;

    private WheelView mWheel_currentAperture;
    private WheelView mWheel_currentIso;
    private WheelView mWheel_currentShutter;
    private WheelView mWheel_newAperture;
    private WheelView mWheel_newIso;
    private WheelView mWheel_newShutter;

    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;

    private TextView mLabelStepFull;
    private TextView mLabelStepHalf;
    private TextView mLabelStepThird;

    private EvCalculator mEvCalculator;

    private String STEP_INDEX = "ev_stepIndex";

    /** how much allow filled fields (new fields) */
    private static final byte MAXIMUM_ALLOWED_FILLED_FIELDS = 2;

    private int mStepIndex;

    private SherlockFragmentActivity mActivity;

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

        mActivity = getSherlockActivity();
    }

    /**
     * Calculates EV pairs
     */
    private void calculate() {
        getSelectedItemsPositions();
        if (isRequiredFieldsFilled() == true) {
            if (isOnlyOneFieldEmpty() == true) {
                mEvCalculator.prepare(mCurrentAperturePosition,
                        mCurrentIsoPosition, mCurrentShutterSpeedPosition,
                        mNewAperturePosition, mNewIsoPostion,
                        mNewShutterSpeedPosition);

                int index = mEvCalculator.calculate();

                if (index != EvCalculator.INVALID_INDEX) {
                    fillEmptyWheel(index);
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.evpairs_error_calculationProblem),
                            Toast.LENGTH_SHORT).show();
                }
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
     * Verifies that all required fields are filled. Function displays an error
     * message if not all required fields have been filled.
     * 
     * @return true if all fields are filled or false in other case
     */
    private boolean isRequiredFieldsFilled() {
        boolean isFilled = false;

        if (mCurrentAperturePosition == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.evpairs_error_emptyCurrentAperture),
                    Toast.LENGTH_SHORT).show();
        } else if (mCurrentIsoPosition == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.evpairs_error_emptyCurrentIso),
                    Toast.LENGTH_SHORT).show();
        } else if (mCurrentShutterSpeedPosition == 0) {
            Toast.makeText(getActivity(),
                    getString(R.string.evpairs_error_emptyCurrentShutterSpeed),
                    Toast.LENGTH_SHORT).show();
        } else {
            isFilled = true;
        }

        return isFilled;
    }

    /**
     * Load saved steps from shared prefs
     */
    private void loadStep() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        mStepIndex = preferenced.getInt(STEP_INDEX, EvCalculator.FULL_STOP);
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

        mEvCalculator = new EvCalculator();

        /* Load data from arrays */
        loadStep();
        updateStepButtons();

        setClickListener();

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveStep();
    }

    /**
     * Save selected step into shared prefs
     */
    private void saveStep() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(STEP_INDEX, mStepIndex);

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
                case R.id.ev_button_calculate:
                    calculate();
                    break;
                case R.id.ev_button_clear:
                    clearNewValues();
                    break;
                case R.id.ev_label_stepFull:
                    mStepIndex = EvCalculator.FULL_STOP;
                    updateStepButtons();
                    break;
                case R.id.ev_label_stepHalf:
                    mStepIndex = EvCalculator.HALF_STOP;
                    updateStepButtons();
                    break;
                case R.id.ev_label_stepThird:
                    mStepIndex = EvCalculator.THIRD_STOP;
                    updateStepButtons();
                    break;
                }
            }
        };

        mView.findViewById(R.id.ev_button_calculate).setOnClickListener(
                clickListener);
        mView.findViewById(R.id.ev_button_clear).setOnClickListener(
                clickListener);
        mLabelStepFull.setOnClickListener(clickListener);
        mLabelStepHalf.setOnClickListener(clickListener);
        mLabelStepThird.setOnClickListener(clickListener);
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

    /**
     * Update steps
     * 
     */
    private void updateStep() {
        Log.enter();

        final int textSize = R.dimen.wheelTextSize;
        mEvCalculator.initArrays(mStepIndex);

        Common.setWheelAdapter(mActivity, mWheel_currentAperture,
                mEvCalculator.getApertureList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_currentIso,
                mEvCalculator.getIsoList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_currentShutter,
                mEvCalculator.getShutterList(), textSize, true);

        Common.setWheelAdapter(mActivity, mWheel_newAperture,
                mEvCalculator.getApertureList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_newIso,
                mEvCalculator.getIsoList(), textSize, true);
        Common.setWheelAdapter(mActivity, mWheel_newShutter,
                mEvCalculator.getShutterList(), textSize, true);
    }

    private void updateStepButtons() {
        Common.setBg(mLabelStepFull, R.drawable.right_stroke);
        Common.setBg(mLabelStepHalf, R.drawable.right_stroke);
        Common.setBg(mLabelStepThird, R.drawable.right_stroke);

        switch (mStepIndex) {
        case EvCalculator.FULL_STOP:
            Common.setBg(mLabelStepFull, R.drawable.left_blue_button);
            break;
        case EvCalculator.HALF_STOP:
            Common.setBg(mLabelStepHalf, R.drawable.middle_blue_button);
            break;
        case EvCalculator.THIRD_STOP:
            Common.setBg(mLabelStepThird, R.drawable.right_blue_button);
            break;
        }

        updateStep();
        setAllWheelsToFirstPos();
    }
}
