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
import android.widget.TabHost;
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

    private TabHost mTabHost;

    private EvCalculator mEvCalculator;

    private String STEP_INDEX = "ev_stepIndex";
    private String CURRENT_APERTURE_INDEX = "ev_currentAperture";
    private String CURRENT_ISO_INDEX = "ev_currentIso";
    private String CURRENT_SHUTTER_INDEX = "ev_currentShutter";
    private String NEW_APERTURE_INDEX = "ev_newAperture";
    private String NEW_ISO_INDEX = "ev_newIso";
    private String NEW_SHUTTER_INDEX = "ev_newShutter";

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

        mTabHost = (TabHost) mView.findViewById(android.R.id.tabhost);

        mActivity = getSherlockActivity();
    }

    /**
     * Builds tabs for small devices
     */
    private void buildTabs() {
        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("tag1");

        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.evpairs_label_currentValues));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.evpairs_label_newValues));
        mTabHost.addTab(spec);
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
     * @return true if layout contains tabs
     */
    private boolean isTabPresent() {
        return (mTabHost != null);
    }

    /**
     * Load saved values from shared prefs
     */
    private void loadValues() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        mStepIndex = preferenced.getInt(STEP_INDEX, EvData.FULL_STOP);

        mCurrentAperturePosition = preferenced
                .getInt(CURRENT_APERTURE_INDEX, 0);
        mCurrentIsoPosition = preferenced.getInt(CURRENT_ISO_INDEX, 0);
        mCurrentShutterSpeedPosition = preferenced.getInt(
                CURRENT_SHUTTER_INDEX, 0);

        mNewAperturePosition = preferenced.getInt(NEW_APERTURE_INDEX, 0);
        mNewIsoPostion = preferenced.getInt(NEW_ISO_INDEX, 0);
        mNewShutterSpeedPosition = preferenced.getInt(NEW_SHUTTER_INDEX, 0);

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
        
        if (isTabPresent() == true) {
            buildTabs();
        }

        mEvCalculator = new EvCalculator();

        loadValues();
        updateStepButtons();
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
                case R.id.ev_button_calculate:
                    calculate();
                    break;
                case R.id.ev_button_clear:
                    clearNewValues();
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

        mView.findViewById(R.id.ev_button_calculate).setOnClickListener(
                clickListener);
        mView.findViewById(R.id.ev_button_clear).setOnClickListener(
                clickListener);
        mLabelStepFull.setOnClickListener(clickListener);
        mLabelStepHalf.setOnClickListener(clickListener);
        mLabelStepThird.setOnClickListener(clickListener);
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
