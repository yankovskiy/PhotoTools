package ru.neverdark.phototools;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;


public class EvpairsActivity extends Activity {

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
	
	private static final byte MAXIMUM_ALLOWED_EMPTY_FIELDS = 2;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evpairs);		
		
		/* Create link between resource and object */
		mSpinner_currentAperture = (Spinner) findViewById(R.id.evpairs_spinner_currentAperture);			
		mSpinner_currentIso = (Spinner) findViewById(R.id.evpairs_spinner_currentIso);
		mSpinner_currentShutterSpeed = (Spinner) findViewById(R.id.evpairs_spinner_currentShutterSpeed);

		mSpinner_newAperture = (Spinner) findViewById(R.id.evpairs_spinner_newAperture);
		mSpinner_newIso = (Spinner) findViewById(R.id.evpairs_spinner_newIso);
		mSpinner_newShutterSpeed = (Spinner) findViewById(R.id.evpairs_spinner_newShutterSpeed);			
		
		
		/* Load data from arrays */			
		ArrayAdapter<String> adapter_currentAperture = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.APERTURE_LIST);
		adapter_currentAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_currentAperture.setAdapter(adapter_currentAperture);
		
		ArrayAdapter<String> adapter_currentIso = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.ISO_LIST);
		adapter_currentIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_currentIso.setAdapter(adapter_currentIso);
		
		ArrayAdapter<String> adapter_currentShutterSpeed = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.SHUTTER_SPEED_LIST);
		adapter_currentShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_currentShutterSpeed.setAdapter(adapter_currentShutterSpeed);

		ArrayAdapter<String> adapter_newAperture = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.APERTURE_LIST);
		adapter_newAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_newAperture.setAdapter(adapter_newAperture);
		
		ArrayAdapter<String> adapter_newIso = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.ISO_LIST);
		adapter_newIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_newIso.setAdapter(adapter_newIso);
		
		ArrayAdapter<String> adapter_newShutterSpeed = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.SHUTTER_SPEED_LIST);
		adapter_newShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_newShutterSpeed.setAdapter(adapter_newShutterSpeed);
				
	}
	
	public void onClick(View v) {
		getSelectedItemsPositions();
		if (isRequiredFieldsFilled() == true) {
			if (isOnlyOneFieldEmpty() == true) {
				EvpairsCalculator evCalc = new EvpairsCalculator(
						mCurrentAperturePosition, mCurrentIsoPosition,
						mCurrentShutterSpeedPosition, mNewAperturePosition,
						mNewIsoPostion, mNewShutterSpeedPosition);
				int index = evCalc.calculate();

				if (index != EvpairsCalculator.INVALID_INDEX) {
					fillValueByIndex(index);
				} else {
					Toast.makeText(
							this,
							getString(R.string.evpairs_error_calculationProblem),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void fillValueByIndex(int index) {
		Spinner spinner;

		if (mNewAperturePosition == 0) {
			spinner = (Spinner) findViewById(R.id.evpairs_spinner_newAperture);
		} else if (mNewIsoPostion == 0) {
			spinner = (Spinner) findViewById(R.id.evpairs_spinner_newIso);
		} else {
			spinner = (Spinner) findViewById(R.id.evpairs_spinner_newShutterSpeed);
		}

		spinner.setSelection(index);
	}
	
	/*
	 * Verifies that all required fields are filled.
	 * Function displays an error message if not all required fields have been filled.
	 * 
	 * @return true if all fields are filled or false in other case
	 */
	private boolean isRequiredFieldsFilled() {
		boolean isFilled = false;

		if (mCurrentAperturePosition == 0) {
			Toast.makeText(this,
					getString(R.string.evpairs_error_emptyCurrentAperture),
					Toast.LENGTH_SHORT).show();
		} else if (mCurrentIsoPosition == 0) {
			Toast.makeText(this,
					getString(R.string.evpairs_error_emptyCurrentIso),
					Toast.LENGTH_SHORT).show();
		} else if (mCurrentShutterSpeedPosition == 0) {
			Toast.makeText(this,
					getString(R.string.evpairs_error_emptyCurrentShutterSpeed),
					Toast.LENGTH_SHORT).show();
		} else {
			isFilled = true;
		}

		return isFilled;
	}
	
	/*
	 * Verifies that only one new fields are empty.
	 * Function display an error message if new fields does not contain only one empty field
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

		if (notEmptyCount != MAXIMUM_ALLOWED_EMPTY_FIELDS) {
			Toast.makeText(this,
					getString(R.string.evpairs_error_onlyOneFieldMustBeEpmty),
					Toast.LENGTH_SHORT).show();
		} else {
			isOnlyOneEmpty = true;
		}

		return isOnlyOneEmpty;
	}
	
	/*
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
	
	

}
