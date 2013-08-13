package ru.neverdark.phototools.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.evcalculator.EvpairsCalculator;
import ru.neverdark.phototools.ui.NothingSelectedSpinnerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
    
    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;
        
    /** how much allow filled fields (new fields) */
    private static final byte MAXIMUM_ALLOWED_FILLED_FIELDS = 2;
    
    /**
     * Function determine empty spinner and sets selelection for him.
     * @param index - item index getting from calculations
     */
    private void fillEmptySpinner(int index) {
        Spinner spinner;

        if (mNewAperturePosition == 0) {
            spinner = (Spinner) getActivity().findViewById(R.id.evpairs_spinner_newAperture);
        } else if (mNewIsoPostion == 0) {
            spinner = (Spinner) getActivity().findViewById(R.id.evpairs_spinner_newIso);
        } else {
            spinner = (Spinner) getActivity().findViewById(R.id.evpairs_spinner_newShutterSpeed);
        }

        spinner.setSelection(index);
    }
    
    /**
     * Function gets selected items positions for spinner
     */
    private void getSelectedItemsPositions() {
        mCurrentAperturePosition = mSpinner_currentAperture.getSelectedItemPosition();
        mCurrentIsoPosition = mSpinner_currentIso.getSelectedItemPosition();
        mCurrentShutterSpeedPosition = mSpinner_currentShutterSpeed.getSelectedItemPosition();
        mNewAperturePosition = mSpinner_newAperture.getSelectedItemPosition();
        mNewIsoPostion = mSpinner_newIso.getSelectedItemPosition();
        mNewShutterSpeedPosition = mSpinner_newShutterSpeed.getSelectedItemPosition();
    }

    /**
     * Verifies that only one new fields are empty.
     * Function display an error message if new fields does not contain only one empty field
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
     * Verifies that all required fields are filled.
     * Function displays an error message if not all required fields have been filled.
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
     * Clears new values
     */
    private void clearNewValues() {
        mSpinner_newAperture.setSelection(0);
        mSpinner_newIso.setSelection(0);
        mSpinner_newShutterSpeed.setSelection(0);
    }

    /**
     * Calculates EV pairs
     */
    private void calculate() {
        getSelectedItemsPositions();
        if (isRequiredFieldsFilled() == true) {
            if (isOnlyOneFieldEmpty() == true) {
                EvpairsCalculator evCalc = new EvpairsCalculator(
                        mCurrentAperturePosition, mCurrentIsoPosition,
                        mCurrentShutterSpeedPosition, mNewAperturePosition,
                        mNewIsoPostion, mNewShutterSpeedPosition);
                int index = evCalc.calculate();

                if (index != EvpairsCalculator.INVALID_INDEX) {
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

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_evpairs, container, false);
        
        /* Create link between resource and object */
        mSpinner_currentAperture = (Spinner) mView.findViewById(R.id.evpairs_spinner_currentAperture);
        mSpinner_currentIso = (Spinner) mView.findViewById(R.id.evpairs_spinner_currentIso);
        mSpinner_currentShutterSpeed = (Spinner) mView.findViewById(R.id.evpairs_spinner_currentShutterSpeed);

        mSpinner_newAperture = (Spinner) mView.findViewById(R.id.evpairs_spinner_newAperture);
        mSpinner_newIso = (Spinner) mView.findViewById(R.id.evpairs_spinner_newIso);
        mSpinner_newShutterSpeed = (Spinner) mView.findViewById(R.id.evpairs_spinner_newShutterSpeed);
        
        
        /* Load data from arrays */
        ArrayAdapter<String> adapter_currentAperture = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.APERTURE_LIST);
        adapter_currentAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentAperture, mSpinner_currentAperture, R.layout.aperture_spinner);
        
        ArrayAdapter<String> adapter_currentIso = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.ISO_LIST);
        adapter_currentIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentIso, mSpinner_currentIso, R.layout.iso_spinner);
        
        ArrayAdapter<String> adapter_currentShutterSpeed = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.SHUTTER_SPEED_LIST);
        adapter_currentShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_currentShutterSpeed, mSpinner_currentShutterSpeed, R.layout.shutter_spinner);

        ArrayAdapter<String> adapter_newAperture = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.APERTURE_LIST);
        adapter_newAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newAperture, mSpinner_newAperture, R.layout.aperture_spinner);
        
        ArrayAdapter<String> adapter_newIso = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.ISO_LIST);
        adapter_newIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newIso, mSpinner_newIso, R.layout.iso_spinner);
        
        ArrayAdapter<String> adapter_newShutterSpeed = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                EvpairsCalculator.SHUTTER_SPEED_LIST);
        adapter_newShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinnerAdapter(adapter_newShutterSpeed, mSpinner_newShutterSpeed, R.layout.shutter_spinner);
        
        setClickListener();
        setLongClickListeners();
        return mView;
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
     * Sets long click listener for one spinner
     * @param spinner spinner for sets long click listener
     */
    private void setLongClick(final Spinner spinner) {
        spinner.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                spinner.setSelection(0);
                return true;
            }
        });
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

        mView.findViewById(R.id.evpairs_button_calculate).setOnClickListener(clickListener);
        mView.findViewById(R.id.evpairs_button_clear).setOnClickListener(clickListener);
    }
    
    /**
     * Sets NothingSelectedSpinnerAdapter for spinner
     * @param spinnerAdapter wrapped adapter 
     * @param spinner spinner for sets adapter
     * @param layoutId layout id for nothing selected spinner
     */
    private void setSpinnerAdapter(SpinnerAdapter adapter, Spinner spinner, int layoutId) {
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                      adapter,
                      layoutId,
                      getActivity()));
    }
}