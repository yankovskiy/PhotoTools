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

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.ui.NothingSelectedSpinnerAdapter;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.view.View.OnLongClickListener;

/**
 * Fragment contains EV Pairs calculator UI
 */
public class EvpairsFragment extends SherlockFragment {
    private View mView;

    private Spinner mSpinner_currentAperture;
    private Spinner mSpinner_currentIso;
    private Spinner mSpinner_currentShutterSpeed;
    private Spinner mSpinner_newAperture;
    private Spinner mSpinner_newIso;
    private Spinner mSpinner_newShutterSpeed;
    private Spinner mSpinner_step;

    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;

    private EvCalculator mEvCalculator;

    private String STEP_INDEX = "STEP_INDEX";
    
    private String CURRENT_APERTURE_INDEX = "CURRENT_APERTURE_INDEX";
    private String CURRENT_ISO_INDEX = "CURRENT_ISO_INDEX";
    private String CURRENT_SHUTTER_INDEX = "CURRENT_SHUTTER_INDEX";
    
    private String NEW_APERTURE_INDEX = "NEW_APERTURE_INDEX";
    private String NEW_ISO_INDEX = "NEW_ISO_INDEX";
    private String NEW_SHUTTER_INDEX = "NEW_SHUTTER_INDEX";
    

    /** how much allow filled fields (new fields) */
    private static final byte MAXIMUM_ALLOWED_FILLED_FIELDS = 2;

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
                    fillEmptySpinner(index);
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.evpairs_error_calculationProblem),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        getSelectedItemsPositions();
        
        outState.putInt(CURRENT_APERTURE_INDEX, mCurrentAperturePosition);
        outState.putInt(CURRENT_ISO_INDEX, mCurrentIsoPosition);
        outState.putInt(CURRENT_SHUTTER_INDEX, mCurrentShutterSpeedPosition);
        
        outState.putInt(NEW_APERTURE_INDEX, mNewAperturePosition);
        outState.putInt(NEW_ISO_INDEX, mNewIsoPostion);
        outState.putInt(NEW_SHUTTER_INDEX, mNewShutterSpeedPosition);
    }
    
    
    /**
     * Clears new values
     */
    private void clearNewValues() {
        mSpinner_newAperture.setSelection(0);
        mSpinner_newIso.setSelection(0);
        mSpinner_newShutterSpeed.setSelection(0);
    }

    /**
     * Function determine empty spinner and sets selelection for him.
     * 
     * @param index
     *            - item index getting from calculations
     */
    private void fillEmptySpinner(int index) {
        Spinner spinner;

        if (mNewAperturePosition == 0) {
            spinner = (Spinner) getActivity().findViewById(
                    R.id.evpairs_spinner_newAperture);
        } else if (mNewIsoPostion == 0) {
            spinner = (Spinner) getActivity().findViewById(
                    R.id.evpairs_spinner_newIso);
        } else {
            spinner = (Spinner) getActivity().findViewById(
                    R.id.evpairs_spinner_newShutterSpeed);
        }

        spinner.setSelection(index);
    }

    /**
     * Function gets selected items positions for spinner
     */
    private void getSelectedItemsPositions() {
        mCurrentAperturePosition = mSpinner_currentAperture
                .getSelectedItemPosition();
        mCurrentIsoPosition = mSpinner_currentIso.getSelectedItemPosition();
        mCurrentShutterSpeedPosition = mSpinner_currentShutterSpeed
                .getSelectedItemPosition();
        mNewAperturePosition = mSpinner_newAperture.getSelectedItemPosition();
        mNewIsoPostion = mSpinner_newIso.getSelectedItemPosition();
        mNewShutterSpeedPosition = mSpinner_newShutterSpeed
                .getSelectedItemPosition();
    }
    
    /**
     * Sets selected items position for spinner
     */
    private void setSelectedItemsPositions() {
        mSpinner_currentAperture.setSelection(mCurrentAperturePosition);
        mSpinner_currentIso.setSelection(mCurrentIsoPosition);
        mSpinner_currentShutterSpeed.setSelection(mCurrentShutterSpeedPosition);
        
        mSpinner_newAperture.setSelection(mNewAperturePosition);
        mSpinner_newIso.setSelection(mNewIsoPostion);
        mSpinner_newShutterSpeed.setSelection(mNewShutterSpeedPosition);
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
    public void loadStep() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        int stepIndex = preferenced.getInt(STEP_INDEX, EvCalculator.FULL_STOP);
        mSpinner_step.setSelection(stepIndex);
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

        /* Create link between resource and object */
        mSpinner_currentAperture = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_currentAperture);
        mSpinner_currentIso = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_currentIso);
        mSpinner_currentShutterSpeed = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_currentShutterSpeed);

        mSpinner_newAperture = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_newAperture);
        mSpinner_newIso = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_newIso);
        mSpinner_newShutterSpeed = (Spinner) mView
                .findViewById(R.id.evpairs_spinner_newShutterSpeed);

        mSpinner_step = (Spinner) mView.findViewById(R.id.evpairs_spinner_step);

        mEvCalculator = new EvCalculator();

        /* Load data from arrays */
        ArrayAdapter<String> adapter_step = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.evpairs_label_steps));
        adapter_step
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_step.setAdapter(adapter_step);
        loadStep();
        
        mSpinner_step.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {
                updateStep(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        /* restore spinner position after device rotation */
        /*
        if (savedInstanceState != null) {
            
            mCurrentAperturePosition = savedInstanceState.getInt(CURRENT_APERTURE_INDEX);
            mCurrentIsoPosition = savedInstanceState.getInt(CURRENT_ISO_INDEX);
            mCurrentShutterSpeedPosition = savedInstanceState.getInt(CURRENT_SHUTTER_INDEX);
            
            mNewAperturePosition = savedInstanceState.getInt(NEW_APERTURE_INDEX);
            mNewIsoPostion = savedInstanceState.getInt(NEW_ISO_INDEX);
            mNewShutterSpeedPosition = savedInstanceState.getInt(NEW_SHUTTER_INDEX);
            
            setSelectedItemsPositions();
            
        }
*/
        setClickListener();
        setLongClickListeners();

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
    public void saveStep() {
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);
        int stepIndex = mSpinner_step.getSelectedItemPosition();

        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(STEP_INDEX, stepIndex);

        editor.commit();
    }

    /**
     * Sets OnClickListener to buttons on the fragment
     */
    private void setClickListener() {
        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                case R.id.evpairs_button_calculate:
                    calculate();
                    break;
                case R.id.evpairs_button_clear:
                    clearNewValues();
                    break;
                }
            }
        };

        mView.findViewById(R.id.evpairs_button_calculate).setOnClickListener(
                clickListener);
        mView.findViewById(R.id.evpairs_button_clear).setOnClickListener(
                clickListener);
    }

    /**
     * Sets long click listener for one spinner
     * 
     * @param spinner
     *            spinner for sets long click listener
     */
    private void setLongClick(final Spinner spinner) {
        spinner.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                spinner.setSelection(0);
                return true;
            }
        });
    }

    /**
     * Sets long click listeners for all spinner
     */
    private void setLongClickListeners() {
        setLongClick(mSpinner_currentAperture);
        setLongClick(mSpinner_currentIso);
        setLongClick(mSpinner_currentShutterSpeed);
        setLongClick(mSpinner_newAperture);
        setLongClick(mSpinner_newIso);
        setLongClick(mSpinner_newShutterSpeed);
    }

    /**
     * Sets NothingSelectedSpinnerAdapter for spinner
     * 
     * @param spinnerAdapter
     *            wrapped adapter
     * @param spinner
     *            spinner for sets adapter
     * @param layoutId
     *            layout id for nothing selected spinner
     */
    private void setSpinnerAdapter(SpinnerAdapter adapter, Spinner spinner,
            int layoutId) {
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(adapter, layoutId,
                getActivity()));
    }

    /**
     * Update steps
     * 
     * @param step
     *            step for expsoure (FULL_STOP, HALF_STOP, THIRD_STOP)
     */
    public void updateStep(final int step) {
        Log.enter();

        mEvCalculator.initArrays(step);

        ArrayAdapter<String> adapter_currentAperture = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getApertureList());
        adapter_currentAperture
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentAperture, mSpinner_currentAperture,
                R.layout.aperture_spinner);

        ArrayAdapter<String> adapter_currentIso = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getIsoList());
        adapter_currentIso
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentIso, mSpinner_currentIso,
                R.layout.iso_spinner);

        ArrayAdapter<String> adapter_currentShutterSpeed = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getShutterList());
        adapter_currentShutterSpeed
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentShutterSpeed,
                mSpinner_currentShutterSpeed, R.layout.shutter_spinner);

        ArrayAdapter<String> adapter_newAperture = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getApertureList());
        adapter_newAperture
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newAperture, mSpinner_newAperture,
                R.layout.aperture_spinner);

        ArrayAdapter<String> adapter_newIso = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getIsoList());
        adapter_newIso
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newIso, mSpinner_newIso, R.layout.iso_spinner);

        ArrayAdapter<String> adapter_newShutterSpeed = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                mEvCalculator.getShutterList());
        adapter_newShutterSpeed
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newShutterSpeed, mSpinner_newShutterSpeed,
                R.layout.shutter_spinner);
    }
}
