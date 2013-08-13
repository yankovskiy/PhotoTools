package ru.neverdark.phototools.fragments;

import java.math.BigDecimal;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.dofcalculator.CameraData;
import ru.neverdark.phototools.dofcalculator.DofCalculator;
import ru.neverdark.phototools.dofcalculator.FStop;
import ru.neverdark.phototools.log.Log;
import ru.neverdark.phototools.ui.NothingSelectedSpinnerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment contains Depth of Field calculator UI
 */
public class DofFragment extends SherlockFragment {

    /**
     * Store oldest vendors spinner position
     */
    private int mOldVendorPosition = -1;
    
    /**
     * Names for save preferences
     */
    private final String APERTURE_INDEX = "APERTURE_INDEX";
    private final String CAMERA_INDEX = "CAMERA_INDEX";
    private final String VENDOR_INDEX = "VENDOR_INDEX";
    
    /**
     * Names for save state for reload TextView text when activity recreated (rotate screen)
     */
    private final String STATE_HYPER_FOCAL = "hyperFocal";
    private final String STATE_FAR_LIMIT = "farLimit";
    private final String STATE_NEAR_LIMIT = "nearLimit";
    private final String STATE_TOTAL = "total"; 
    
    /**
     * Calculate hyperfocal distance
     * @return CalculationResult object
     */
    private DofCalculator.CalculationResult calculate() {
        BigDecimal aperture = getSelectedAperture();
        BigDecimal focusLength = getFocalLengthValue();
        BigDecimal coc = CameraData.getCocForCamera(getSelectedVendor(), getSelectedCamera());
        BigDecimal subjectDistance = getSubjectDistanceValue();
        DofCalculator dofCalculator = new DofCalculator(aperture, focusLength, coc, subjectDistance);
        return dofCalculator.calculate();
    }
    
    /**
     * Function display calculation result in the dof_label_hyperFocal TextView
     * @param calculationResult CalculationResult object when stores the result of the calculations
     */
    private void displayCalculationResult(DofCalculator.CalculationResult calculatioResult) {
        String meters = " " + getString(R.string.dof_label_meters); 
        String tmp;
        
        tmp = getString(R.string.dof_label_hyperFocal) + " "
                + calculatioResult.getHyperFocal().toString()
                + meters;
        getHyperFocalResultLabel().setText(tmp);

        tmp = getString(R.string.dof_label_nearLimit) + " "
                + calculatioResult.getNearLimit().toString()
                + meters;
        getNearLimitResultLabel().setText(tmp);

        tmp = getString(R.string.dof_label_farLimit) + " "
                + calculatioResult.format(calculatioResult.getFarLimit())
                + meters;
        getFarLimitResultLabel().setText(tmp);

        tmp = getString(R.string.dof_label_total) + " "
                + calculatioResult.format(calculatioResult.getTotal())
                + meters;
        getTotalResultLabel().setText(tmp);
    }
    
    /**
     * Function get spinner contains aperture
     * @return Spinner if found or null in other case
     */
    private Spinner getAperturesSpinner() {
        return (Spinner) getActivity().findViewById(R.id.dof_spinner_apertures);
    }
    
    /**
     * Function get spinner contains cameras models
     * @return Spinner if found or null in other case
     */
    private Spinner getCamerasSpinner() {
        return (Spinner) getActivity().findViewById(R.id.dof_spinner_cameras);
    }
    /**
     * Gets TextView for far limit results
     * @return The TextView if found or null in other case
     */
    private TextView getFarLimitResultLabel() {
        return (TextView) getActivity().findViewById(R.id.dof_label_farLimit);
    }
    
    /**
     * Function get EditText for Focal length
     * @return EditText if found or null in other case
     */
    private EditText getFocalLengthEditText() {
         return (EditText) getActivity().findViewById(R.id.dof_editText_focalLength);
    }
    
    /**
     * Function get value of focal length from EditText
     * @return focal length
     */
    private BigDecimal getFocalLengthValue() { 
        String tmp = getFocalLengthEditText().getText().toString();
        return (new BigDecimal(tmp));
    }

    /**
     * Function get TextView for calculation results
     * @return The TextView if found or null in other case
     */
    private TextView getHyperFocalResultLabel() {
        return (TextView) getActivity().findViewById(R.id.dof_label_hyperFocalResult);
    }
    
    /**
     * Gets TextView for near limit results
     * @return The TextView if found or null in other case
     */
    private TextView getNearLimitResultLabel() {
        return (TextView) getActivity().findViewById(R.id.dof_label_nearLimit);
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
     * Gets EditText for subject distance
     * @return EditText if found or null in other case
     */
    private EditText getSubjectDistanceEditText() {
        return (EditText) getActivity().findViewById(R.id.dof_editText_subjectDistance);
    }
    
    /**
     * Gets value from subject distance EditText
     * @return value from subject distance EditText
     */
    private BigDecimal getSubjectDistanceValue() {
        String tmp = getSubjectDistanceEditText().getText().toString();
        return (new BigDecimal(tmp));
    }
    

    /**
     * Gets TextView for total results
     * @return The TextView if found or null in other case
     */
    private TextView getTotalResultLabel() {
        return (TextView) getActivity().findViewById(R.id.dof_label_total);
    }

    /**
     * Function get spinner contains cameras vendor
     * @return spinner if found or null in other case
     */
    private Spinner getVendorsSpinner() {
        return (Spinner) getActivity().findViewById(R.id.dof_spinner_vendors);
    }
    
    /**
     * Function for fill apertures spinner
     */
    private void initAperturesSpinner() {
        Spinner spinner = getAperturesSpinner();
        ArrayAdapter<FStop> apertureAdapter = new ArrayAdapter<FStop>(getActivity(), android.R.layout.simple_spinner_item);
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(apertureAdapter);

        for (FStop fstop : FStop.getAllFStops()) {
            apertureAdapter.add(fstop);
        }
        
        setLongClick(spinner);
        setSpinnerAdapter(apertureAdapter, spinner, R.layout.aperture_spinner);
        
    }
    
    /**
     * Function for fill camera spinner
     */
    private void initCamerasSpinner() {
        ArrayAdapter<String> cameraArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
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

        ArrayAdapter<CameraData.Vendor> vendorAdapter = new ArrayAdapter<CameraData.Vendor>(getActivity(), android.R.layout.simple_spinner_item);
        vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(vendorAdapter);
        
        for(CameraData.Vendor vendor : CameraData.getVendors()) {
            vendorAdapter.add(vendor);
        }
        vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != mOldVendorPosition) {
                    if (mOldVendorPosition != -1) {
                        CameraData.Vendor vendor = (CameraData.Vendor) adapterView.getAdapter().getItem(pos);
                        populateCameraByVendor(vendor);
                        getCamerasSpinner().setSelection(0);
                    }
                    mOldVendorPosition = pos;
                }
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
        String focalLength = getFocalLengthEditText().getText().toString();
        
        if (focalLength.length() > 0) {
            BigDecimal tmp = new BigDecimal(focalLength);
            if (tmp.intValue() > 0 && tmp.intValue() <= 500) {
                isValid = true;
            }
        }
        
        return isValid;
    }
    
    /**
     * Checks subject distance for valid values
     * @return true if valid or false in other case
     */
    private boolean isSubjectDistanceValid() {
        boolean isValid = false;
        String subjectDistance = getSubjectDistanceEditText().getText().toString();
        
        if (subjectDistance.length() > 0) {
            try {
                BigDecimal tmp = new BigDecimal(subjectDistance);
                if (tmp.floatValue() > 0 && tmp.floatValue() <= 500) {
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                Log.message("Exception in isSubjectDistanceValid");
            }
        }
        
        return isValid;
    }
    
    /**
     * Load positions for spinners from SharedPreferences
     */
    private void loadSpinnersPositions() {
        SharedPreferences preferenced = getActivity().getPreferences(Context.MODE_PRIVATE);
        int vendorIndex = preferenced.getInt(VENDOR_INDEX, 0);
        int cameraIndex = preferenced.getInt(CAMERA_INDEX, 0);
        int apertureIndex = preferenced.getInt(APERTURE_INDEX, 0);
        
        /* checks for no more than the number of elements in the arrays */
        /* if more than number of elements in the array - set index to zero */
        if (vendorIndex > getVendorsSpinner().getCount() - 1) {
            vendorIndex = 0;
        }
        
        getVendorsSpinner().setSelection(vendorIndex);

        populateCameraByVendor(CameraData.Vendor.values()[vendorIndex]);

        if (cameraIndex > getCamerasSpinner().getCount() -1) {
            cameraIndex = 0;
        }
        
        getCamerasSpinner().setSelection(cameraIndex);
        
        if (apertureIndex > getAperturesSpinner().getCount() - 1) {
            apertureIndex = 0;
        }
        
        getAperturesSpinner().setSelection(apertureIndex);
    }
    

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_dof, container, false);
        Button calculateButton = (Button) view.findViewById(R.id.dof_button_calculate);
        if (calculateButton != null) {
            calculateButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if (isApertureSelected()) {
                        if (isFocalLengthValid()) {
                            if (isSubjectDistanceValid()) {
                                DofCalculator.CalculationResult calculationResult = calculate();
                                displayCalculationResult(calculationResult);
                            } else {
                                showError(R.string.dof_error_incorrectSubjectDistance);
                            }
                        } else {
                            showError(R.string.dof_error_emptyFocalLength);
                        }
                    } else {
                        showError(R.string.dof_error_apertureNotSelected);
                    }
                }
            });
        }
        
        
        return view;
    }
    
    /**
     * Checks aperture spinner for selected values
     * @return true if aperture selected or false in other case
     */
    private boolean isApertureSelected() {
        Log.message("Enter");
        return getAperturesSpinner().getSelectedItemPosition() > 0;
    }
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        /* saving spinner position */
        saveSpinnerPositions();
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onActivityCreated(savedInstanceState);
        
        initAperturesSpinner();
        initCamerasSpinner();
        initVendorSpinner();
        
        if (savedInstanceState != null) {
            getHyperFocalResultLabel().setText(savedInstanceState.getString(STATE_HYPER_FOCAL));
            getNearLimitResultLabel().setText(savedInstanceState.getString(STATE_NEAR_LIMIT));
            getFarLimitResultLabel().setText(savedInstanceState.getString(STATE_FAR_LIMIT));
            getTotalResultLabel().setText(savedInstanceState.getString(STATE_TOTAL));
        }
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    public void onResume() {
        super.onResume();
        /* load spinner positions */
        loadSpinnersPositions();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.message("Enter");
        savedInstanceState.putString(STATE_HYPER_FOCAL, getHyperFocalResultLabel().getText().toString());
        savedInstanceState.putString(STATE_FAR_LIMIT, getFarLimitResultLabel().getText().toString());
        savedInstanceState.putString(STATE_NEAR_LIMIT, getNearLimitResultLabel().getText().toString());
        savedInstanceState.putString(STATE_TOTAL, getTotalResultLabel().getText().toString());
        
        super.onSaveInstanceState(savedInstanceState);
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
    }

    /**
     * Save positions spinner positions to SharedPreferenced
     */
    private void saveSpinnerPositions() {
        SharedPreferences preferenced = getActivity().getPreferences(Context.MODE_PRIVATE);
        int vendorIndex = getVendorsSpinner().getSelectedItemPosition();
        int cameraIndex = getCamerasSpinner().getSelectedItemPosition();
        int apertureIndex = getAperturesSpinner().getSelectedItemPosition();
        
        SharedPreferences.Editor editor = preferenced.edit();
        editor.putInt(VENDOR_INDEX, vendorIndex);
        editor.putInt(CAMERA_INDEX, cameraIndex);
        editor.putInt(APERTURE_INDEX, apertureIndex);
        editor.commit();
    }
    
    /**
     * Function show error message
     * @param resourceId - string id for error contains error message
     */
    private void showError(int resourceId) {
        Toast.makeText(
                getActivity(),
                getString(resourceId),
                Toast.LENGTH_LONG).show();
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
