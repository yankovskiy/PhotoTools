package ru.neverdark.phototools;

import java.util.List;

import ru.neverdark.phototools.dofcalculator.CameraData;
import ru.neverdark.phototools.dofcalculator.FStop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;

public class DofActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dof);
		
		initAperturesSpinner();
		initCamerasSpinner();
		initVendorSpinner();
		
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dof_button_calculate:
			break;
		}
	}
	
	private Spinner getVendorsSpinner() {
		return (Spinner) findViewById(R.id.dof_spinner_vendors);
	}
	
	private Spinner getCamerasSpinner() {
		return (Spinner) findViewById(R.id.dof_spinner_cameras);
	}
	
	private Spinner getAperturesSpinner() {
		return (Spinner) findViewById(R.id.dof_spinner_apertures);
	}	
	
	private void initVendorSpinner() {
		Spinner vendorSpinner = getVendorsSpinner();

        ArrayAdapter<CameraData.Vendor> vendorAdapter = new ArrayAdapter<CameraData.Vendor>(this, android.R.layout.simple_spinner_item);
        vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(vendorAdapter);
        
        for(CameraData.Vendor vendor : CameraData.getVendors()) {
        	vendorAdapter.add(vendor);
        }
        vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {                
                CameraData.Vendor vendor = (CameraData.Vendor) adapterView.getAdapter().getItem(pos);
                populateCameraByVendor(vendor);                
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });        
	}
	
	private void initCamerasSpinner() {
        ArrayAdapter<String> cameraArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        cameraArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCamerasSpinner().setAdapter(cameraArrayAdapter);
	}
	
	@SuppressWarnings("unchecked")
	private void populateCameraByVendor(CameraData.Vendor vendor) {
		List<CameraData> cameraList = CameraData.getCameraByVendor(vendor);
		
        // Clear the current camera values
        ArrayAdapter<String> cameraSpinnerAdapter = (ArrayAdapter<String>) getCamerasSpinner().getAdapter();
        cameraSpinnerAdapter.clear();

        // Re-populate with cameras for manufacturer
        for (CameraData cameraData : cameraList) {
            cameraSpinnerAdapter.add(cameraData.getModel());
        }
	}
	
	private void initAperturesSpinner() {
        ArrayAdapter<FStop> apertureAdapter = new ArrayAdapter<FStop>(this, android.R.layout.simple_spinner_item);
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getAperturesSpinner().setAdapter(apertureAdapter);

        for (FStop fstop : FStop.getAllFStops()) {
            apertureAdapter.add(fstop);
        }
	}

}
