package ru.neverdark.phototools;

import java.math.BigDecimal;
import java.util.List;

import ru.neverdark.phototools.dofcalculator.CameraData;
import ru.neverdark.phototools.dofcalculator.DofCalculator;
import ru.neverdark.phototools.dofcalculator.FStop;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DofActivity extends Activity {

    /**
     * Store calculation result
     */
    private BigDecimal mCalculationResult;
    
    /**
     * Calculate hyperfocal distance
     */
    private void calculate() {
        BigDecimal aperture = getSelectedAperture();
        BigDecimal focusLength = getFocalLengthValue();
        BigDecimal coc = CameraData.getCocForCamera(getSelectedVendor(), getSelectedCamera());
        DofCalculator dofCalculator = new DofCalculator(aperture, focusLength, coc);
        mCalculationResult = dofCalculator.calculateHyperFocalDistance();
    }
    
    /**
     * Function display calculation result in the dof_label_hyperFocal TextView
     */
    private void displayCalculationResult() {
        String result = getString(R.string.dof_label_hyperFocal);
        result += " " + mCalculationResult.toString();
        result += " " + getString(R.string.dof_label_meters);
        
        getHyperFocalResultLabel().setText(result);
    }
    
    /**
     * Function get spinner contains aperture
     * @return Spinner if found or null in other case
     */
    private Spinner getAperturesSpinner() {
        return (Spinner) findViewById(R.id.dof_spinner_apertures);
    }
    
    /**
     * Function get spinner contains cameras models
     * @return Spinner if found or null in other case
     */
    private Spinner getCamerasSpinner() {
        return (Spinner) findViewById(R.id.dof_spinner_cameras);
    }
    
    /**
     * Function get EditText for Focal length
     * @return EditText if found or null in other case
     */
    private EditText getFocalLengthTextEdit() {
         return (EditText) findViewById(R.id.dof_editText_focalLength);
    }

    /**
     * Function get value of focal length from EditText
     * @return focal length
     */
    private BigDecimal getFocalLengthValue() { 
        String tmp = getFocalLengthTextEdit().getText().toString();
        return (new BigDecimal(tmp));
    }

    /**
     * Function get TextView for calculation results
     * @return The TextView if found or null in other case
     */
    private TextView getHyperFocalResultLabel() {
        return (TextView) findViewById(R.id.dof_label_hyperFocalResult);
    }

    /**
     * Function get selected aperture value from spinner
     * @return aperture value
     */
    private BigDecimal getSelectedAperture() {
        return ((FStop) getAperturesSpinner().getSelectedItem()).getValue();
    }

    /**
     * Function get selected camera model from spinner
     * @return camera model
     */
    private String getSelectedCamera() {
        return (String) getCamerasSpinner().getSelectedItem();
    }
    
    /**
     * Function get selected camera vendor
     * @return camera vendor
     */
    private CameraData.Vendor getSelectedVendor() {
        return (CameraData.Vendor) getVendorsSpinner().getSelectedItem();
    }
    
    /**
     * Function get spinner contains cameras vendor
     * @return spinner if found or null in other case
     */
    private Spinner getVendorsSpinner() {
        return (Spinner) findViewById(R.id.dof_spinner_vendors);
    }    
    
    /**
     * Function for fill apertures spinner
     */
    private void initAperturesSpinner() {
        ArrayAdapter<FStop> apertureAdapter = new ArrayAdapter<FStop>(this, android.R.layout.simple_spinner_item);
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getAperturesSpinner().setAdapter(apertureAdapter);

        for (FStop fstop : FStop.getAllFStops()) {
            apertureAdapter.add(fstop);
        }
    }
    
    /**
     * Function for fill camera spinner
     */
    private void initCamerasSpinner() {
        ArrayAdapter<String> cameraArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        cameraArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCamerasSpinner().setAdapter(cameraArrayAdapter);
    }
    
    /**
     * Function for init vendor spinner.
     * 
     * Function implements handler for OnItemSelected event for vendor spinner.
     * If user select any items in vendor spinner - cameras spinner reload data for new vendor.
     */
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
    
    /**
     * Function checks focal length for valid values
     * @return true if valid or false in other case
     */
    private boolean isFocalLengthValid() {
        boolean isValid = false;
        String focalLength = getFocalLengthTextEdit().getText().toString();
        
        if (focalLength.length() > 0) {
            BigDecimal tmp = new BigDecimal(focalLength);
            if (tmp.intValue() > 0 && tmp.intValue() <= 500) {
                isValid = true;
            }
        }
        
        return isValid;
    }
    
    /**
     * Function called when a view has been clicked.
     * 
     * @param v - The view that was clicked.
     */
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dof_button_calculate:
            if (isFocalLengthValid()) {
                calculate();
                displayCalculationResult();
            } else {
                showError(R.string.dof_error_emptyFocalLength);
            }
            break;
        }
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dof);
        
        initAperturesSpinner();
        initCamerasSpinner();
        initVendorSpinner();        
    }
    
    /**
     * Function reload data in cameras spinner for special vendor
     * @param vendor - vendor of cameras
     */
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
        
        getCamerasSpinner().setSelection(0);
    }
    
    /**
     * Function show error message
     * @param resourceId - string id for error contains error message
     */
    private void showError(int resourceId) {
        Toast.makeText(
                this,
                getString(resourceId),
                Toast.LENGTH_LONG).show();
    }

}
