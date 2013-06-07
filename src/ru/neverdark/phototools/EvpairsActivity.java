package ru.neverdark.phototools;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;


public class EvpairsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evpairs);
				

		/* Create link between resource and object */
		Spinner spinner_currentAperture = (Spinner) findViewById(R.id.evpairs_spinner_currentAperture);			
		Spinner spinner_currentIso = (Spinner) findViewById(R.id.evpairs_spinner_currentIso);
		Spinner spinner_currentShutterSpeed = (Spinner) findViewById(R.id.evpairs_spinner_currentShutterSpeed);

		Spinner spinner_newAperture = (Spinner) findViewById(R.id.evpairs_spinner_newAperture);
		Spinner spinner_newIso = (Spinner) findViewById(R.id.evpairs_spinner_newIso);
		Spinner spinner_newShutterSpeed = (Spinner) findViewById(R.id.evpairs_spinner_newShutterSpeed);
		
		
		/* Load data from arrays */			
		ArrayAdapter<String> adapter_currentAperture = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.APERTURE_LIST);
		adapter_currentAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_currentAperture.setAdapter(adapter_currentAperture);
		
		ArrayAdapter<String> adapter_currentIso = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.ISO_LIST);
		adapter_currentIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_currentIso.setAdapter(adapter_currentIso);
		
		ArrayAdapter<String> adapter_currentShutterSpeed = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.SHUTTER_SPEED_LIST);
		adapter_currentShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_currentShutterSpeed.setAdapter(adapter_currentShutterSpeed);

		ArrayAdapter<String> adapter_newAperture = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.APERTURE_LIST);
		adapter_newAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_newAperture.setAdapter(adapter_newAperture);
		
		ArrayAdapter<String> adapter_newIso = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.ISO_LIST);
		adapter_newIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_newIso.setAdapter(adapter_newIso);
		
		ArrayAdapter<String> adapter_newShutterSpeed = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				EvpairsCalculator.SHUTTER_SPEED_LIST);
		adapter_newShutterSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_newShutterSpeed.setAdapter(adapter_newShutterSpeed);		
	}
}
