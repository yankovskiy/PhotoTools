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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.dofcalculator.Array;
import ru.neverdark.phototools.utils.dofcalculator.CameraData;
import ru.neverdark.phototools.utils.dofcalculator.DofCalculator;
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
 * Fragment contains Depth of Field calculator UI
 */
public class DofFragment extends SherlockFragment {

    private View mView;

    private WheelView mWheelVendor;
    private WheelView mWheelCamera;
    private WheelView mWheelAperture;
    private WheelView mWheelFocalLength;
    private WheelView mWheelSubjectDistance;
    private WheelView mWheelMeasureUnit;

    private TextView mLabelM;
    private TextView mLabelCm;
    private TextView mLabelFt;
    private TextView mLabelIn;

    private TextView mLabelNearLimitResult;
    private TextView mLabelFarLimitResult;
    private TextView mLabelHyperFocalResult;
    private TextView mLabelTotalResutl;

    private final static String VENDOR = "dof_vendor";
    private final static String CAMERA = "dof_camera";
    private final static String APERTURE = "dof_aperture";
    private final static String FOCAL_LENGTH = "dof_focalLength";
    private final static String SUBJECT_DISTANCE = "dof_subjectDistance";
    private final static String MEASURE_UNIT = "dof_measureUnit";
    private final static String MEASURE_RESULT_UNIT = "dof_measureResultUnit";
    private final static String NEAR_LIMIT = "dof_nearLimit";
    private final static String FAR_LIMIT = "dof_farLimit";
    private final static String HYPERFOCAL = "dof_hyperfocal";
    private final static String TOTAL = "dof_total";



    private int mVendorId;
    private int mCameraId;

    private int mMeasureResultUnit;

    /**
     * Loads arrays to wheels
     */
    private void arrayToWheels() {
        final String measureUnits[] = getResources().getStringArray(
                R.array.dof_measureUnits);

        setWheelAdapter(mWheelAperture, Array.APERTURE_LIST);
        setWheelAdapter(mWheelFocalLength, Array.FOCAL_LENGTH);
        setWheelAdapter(mWheelMeasureUnit, measureUnits);
        setWheelAdapter(mWheelSubjectDistance, Array.SUBJECT_DISTANCE);
    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        mWheelVendor = (WheelView) mView.findViewById(R.id.dof_wheel_vendor);
        mWheelCamera = (WheelView) mView.findViewById(R.id.dof_wheel_camera);
        mWheelAperture = (WheelView) mView
                .findViewById(R.id.dof_wheel_aperture);
        mWheelFocalLength = (WheelView) mView
                .findViewById(R.id.dof_wheel_focalLength);
        mWheelSubjectDistance = (WheelView) mView
                .findViewById(R.id.dof_wheel_subjectDistance);
        mWheelMeasureUnit = (WheelView) mView
                .findViewById(R.id.dof_wheel_measureUnit);

        mLabelM = (TextView) mView.findViewById(R.id.dof_label_m);
        mLabelCm = (TextView) mView.findViewById(R.id.dof_label_cm);
        mLabelFt = (TextView) mView.findViewById(R.id.dof_label_ft);
        mLabelIn = (TextView) mView.findViewById(R.id.dof_label_in);

        mLabelNearLimitResult = (TextView) mView
                .findViewById(R.id.dof_label_nearLimitResult);
        mLabelFarLimitResult = (TextView) mView
                .findViewById(R.id.dof_label_farLimitResult);
        mLabelHyperFocalResult = (TextView) mView
                .findViewById(R.id.dof_label_hyperFocalResult);
        mLabelTotalResutl = (TextView) mView
                .findViewById(R.id.dof_label_totalResult);
    }

    /**
     * @return aperture value from aperture wheel
     */
    private BigDecimal getAperture() {
        BigDecimal aperture = new BigDecimal(
                Array.SCIENTIFIC_ARERTURES[mWheelAperture.getCurrentItem()]);
        Log.variable("aperture", aperture.toString());
        return aperture;
    }

    /**
     * @ return cirle of confusion for selected camera
     */
    private BigDecimal getCoc() {
        CameraData.Vendor vendor = (CameraData.Vendor) mWheelVendor
                .getSelectedItem();
        String camera = (String) mWheelCamera.getSelectedItem();
        BigDecimal coc = CameraData.getCocForCamera(vendor, camera);
        Log.variable("camera", camera);
        Log.variable("vendor", vendor.toString());
        Log.variable("coc", coc.toString());
        return coc;
    }

    /**
     * @return focal length from wheel
     */
    private BigDecimal getFocalLength() {
        String focal = (String) mWheelFocalLength.getSelectedItem();
        focal = focal.replace(" mm", "");
        Log.variable("focal", focal);
        return new BigDecimal(focal);
    }

    /**
     * @return measure unit from wheel
     */
    private int getMeasureUnit() {
        int measureUnit = mWheelMeasureUnit.getCurrentItem();
        Log.variable("measureUnit", String.valueOf(measureUnit));
        return measureUnit;
    }

    /**
     * @return subject distance from wheel
     */
    private BigDecimal getSubjectDistance() {
        String distance = (String) mWheelSubjectDistance.getSelectedItem();
        Log.variable("distance", distance);
        return new BigDecimal(distance);
    }

    /**
     * Loads cameras data to wheels
     */
    private void loadCamerasDataToWheel() {
        Log.enter();

        final CameraData.Vendor vendors[] = CameraData.getVendors();
        ArrayWheelAdapter<CameraData.Vendor> adapter = new ArrayWheelAdapter<CameraData.Vendor>(
                getSherlockActivity(), vendors);
        mWheelVendor.setViewAdapter(adapter);
        adapter.setTextSize(15);
        mWheelVendor.setCurrentItem(mVendorId);

        populateCameraByVendor();
    }

    /**
     * Loads data from shared preferences
     */
    private void loadSavedData() {
        Log.enter();
        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);
        mVendorId = prefs.getInt(VENDOR, 0);
        mCameraId = prefs.getInt(CAMERA, 0);

        mLabelFarLimitResult.setText(prefs.getString(FAR_LIMIT, ""));
        mLabelHyperFocalResult.setText(prefs.getString(HYPERFOCAL, ""));
        mLabelNearLimitResult.setText(prefs.getString(NEAR_LIMIT, ""));
        mLabelTotalResutl.setText(prefs.getString(TOTAL, ""));

        mWheelAperture.setCurrentItem(prefs.getInt(APERTURE, 0));
        mWheelFocalLength.setCurrentItem(prefs.getInt(FOCAL_LENGTH, 0));
        mWheelMeasureUnit.setCurrentItem(prefs.getInt(MEASURE_UNIT, 0));
        mWheelSubjectDistance.setCurrentItem(prefs.getInt(SUBJECT_DISTANCE, 0));

        mMeasureResultUnit = prefs.getInt(MEASURE_RESULT_UNIT, Constants.METER);
    }

    /**
     * Handler for clicking event on the measure unit buttons
     */
    private void measureButtonsHandler() {
        Log.enter();

        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                case R.id.dof_label_m:
                    mMeasureResultUnit = Constants.METER;
                    break;

                case R.id.dof_label_cm:
                    mMeasureResultUnit = Constants.CM;
                    break;

                case R.id.dof_label_ft:
                    mMeasureResultUnit = Constants.FOOT;
                    break;

                case R.id.dof_label_in:
                    mMeasureResultUnit = Constants.INCH;
                    break;
                }

                updateMeasureButtons();
                recalculate();
            }
        };

        mLabelM.setOnClickListener(clickListener);
        mLabelCm.setOnClickListener(clickListener);
        mLabelFt.setOnClickListener(clickListener);
        mLabelIn.setOnClickListener(clickListener);
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
        long start = Log.enter();
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_dof, container, false);

        bindObjectsToResources();
        arrayToWheels();
        loadSavedData();
        loadCamerasDataToWheel();
        vendorSelectionHandler();
        measureButtonsHandler();
        wheelsHandler();
        updateMeasureButtons();

        Log.exit(start);
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();

        saveData();
    }

    /**
     * Populates camera models by selected vendor
     */
    private void populateCameraByVendor() {
        CameraData.Vendor vendor = (CameraData.Vendor) mWheelVendor
                .getSelectedItem();
        String cameras[] = CameraData.getCameraByVendor(vendor);

        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
                getSherlockActivity(), cameras);
        mWheelCamera.setViewAdapter(adapter);
        adapter.setTextSize(12);
        mWheelCamera.setCurrentItem(mCameraId);
    }

    /**
     * Recalculation and show result
     */
    private void recalculate() {
        Log.enter();

        BigDecimal aperture = getAperture();
        BigDecimal focusLength = getFocalLength();
        BigDecimal coc = getCoc();
        int measureUnit = getMeasureUnit();
        BigDecimal subjectDistance = getSubjectDistance();

        DofCalculator dofCalc = new DofCalculator(aperture, focusLength, coc,
                subjectDistance, measureUnit);
        DofCalculator.CalculationResult calculationResult = dofCalc
                .calculate(mMeasureResultUnit);

        mLabelFarLimitResult.setText(calculationResult.format(calculationResult
                .getFarLimit()));
        mLabelNearLimitResult.setText(calculationResult
                .format(calculationResult.getNearLimit()));
        mLabelHyperFocalResult.setText(calculationResult
                .format(calculationResult.getHyperFocal()));
        mLabelTotalResutl.setText(calculationResult.format(calculationResult
                .getTotal()));
    }

    /**
     * Saves data to shared preferences
     */
    private void saveData() {
        Log.enter();
        SharedPreferences preferenced = getActivity().getPreferences(
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferenced.edit();

        editor.putInt(VENDOR, mWheelVendor.getCurrentItem());
        editor.putInt(CAMERA, mWheelCamera.getCurrentItem());
        editor.putInt(APERTURE, mWheelAperture.getCurrentItem());
        editor.putInt(FOCAL_LENGTH, mWheelFocalLength.getCurrentItem());
        editor.putInt(SUBJECT_DISTANCE, mWheelSubjectDistance.getCurrentItem());
        editor.putInt(MEASURE_UNIT, mWheelMeasureUnit.getCurrentItem());
        editor.putInt(MEASURE_RESULT_UNIT, mMeasureResultUnit);
        editor.putString(NEAR_LIMIT, mLabelNearLimitResult.getText().toString());
        editor.putString(FAR_LIMIT, mLabelFarLimitResult.getText().toString());
        editor.putString(HYPERFOCAL, mLabelHyperFocalResult.getText()
                .toString());
        editor.putString(TOTAL, mLabelTotalResutl.getText().toString());

        editor.commit();
    }

    /**
     * Sets adapter for wheel
     * 
     * @param wheel
     *            object for sets adapter
     * @param values
     *            values for adapter
     */
    private void setWheelAdapter(WheelView wheel, String values[]) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
                getSherlockActivity(), values);
        adapter.setTextSize(15);
        wheel.setViewAdapter(adapter);
    }

    /**
     * Updates measure buttons
     */
    private void updateMeasureButtons() {
        Common.setBg(mLabelM, R.drawable.right_stroke);
        Common.setBg(mLabelCm, R.drawable.right_stroke);
        Common.setBg(mLabelFt, R.drawable.right_stroke);
        Common.setBg(mLabelIn, R.drawable.right_stroke);

        switch (mMeasureResultUnit) {
        case Constants.METER:
            Common.setBg(mLabelM, R.drawable.left_blue_button);
            break;

        case Constants.CM:
            Common.setBg(mLabelCm, R.drawable.middle_blue_button);
            break;

        case Constants.FOOT:
            Common.setBg(mLabelFt, R.drawable.middle_blue_button);
            break;

        case Constants.INCH:
            Common.setBg(mLabelIn, R.drawable.right_blue_button);
            break;
        }
    }

    /**
     * Handler for changing vendor.
     * 
     * When user change vendor in the wheel - we reload cameras wheel
     */
    private void vendorSelectionHandler() {
        Log.enter();
        OnWheelScrollListener listener = new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // nothing

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mCameraId = 0;
                populateCameraByVendor();
                recalculate();
            }
        };

        mWheelVendor.addScrollingListener(listener);
    }

    /**
     * Handler for swipe event handle from all wheels except vendor
     */
    private void wheelsHandler() {
        Log.enter();
        OnWheelScrollListener listener = new OnWheelScrollListener() {

            @Override
            public void onScrollingFinished(WheelView wheel) {
                recalculate();
            }

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // nothing
            }
        };

        mWheelAperture.addScrollingListener(listener);
        mWheelFocalLength.addScrollingListener(listener);
        mWheelMeasureUnit.addScrollingListener(listener);
        mWheelSubjectDistance.addScrollingListener(listener);
        mWheelCamera.addScrollingListener(listener);
    }
}
