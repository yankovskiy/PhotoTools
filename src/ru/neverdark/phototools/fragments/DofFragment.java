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
 ******************************************************************************/
package ru.neverdark.phototools.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.fragments.CameraManagementDialog.OnCameraManagementListener;
import ru.neverdark.phototools.fragments.DofLimitationDialog.OnDofLimitationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Common.MinMaxValues;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.dofcalculator.Array;
import ru.neverdark.phototools.utils.dofcalculator.CameraData;
import ru.neverdark.phototools.utils.dofcalculator.DofCalculator;
import ru.neverdark.phototools.utils.dofcalculator.CameraData.Vendor;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment contains Depth of Field calculator UI
 */
public class DofFragment extends SherlockFragment {

    /**
     * Click listener for "Camera management" and "Limit" button
     */
    private class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.dof_cameraManagement:
                CameraManagementDialog cameraDialog = CameraManagementDialog
                        .getInstance(mContext);
                cameraDialog.setCallback(new CameraManagementListener());
                cameraDialog.show(getFragmentManager(),
                        CameraManagementDialog.DIALOG_TAG);
                break;
            case R.id.dof_limitation:
                showLimitataionDialog();
                break;
            }
        }
    }

    /**
     * Listener for handling "Back" button in the CameraManagementDialog
     */
    private class CameraManagementListener implements
            OnCameraManagementListener, OnCallback {
        @Override
        public void cameraManagementOnBack() {
            CameraData.Vendor vendor = (CameraData.Vendor) mWheelVendor
                    .getSelectedItem();
            if (vendor.equals(Vendor.USER)) {
                populateCameraByVendor();
                recalculate();
            }
        }
    }

    /**
     * Listener for handling OK button in the DofLimitationDialog
     */
    private class DofLimitationListener implements OnDofLimitationListener {
        @Override
        public void onDofLimitationHandler(Limit data) {
            mLimit = data;
            MinMaxValues minMaxAperture = MinMaxValues
                    .getMinMax(mWheelAperture);
            MinMaxValues minMaxFocalLength = MinMaxValues
                    .getMinMax(mWheelFocalLength);
            MinMaxValues minMaxSubjectDistance = MinMaxValues
                    .getMinMax(mWheelSubjectDistance);

            SavedData savedData = new SavedData();

            String minAperture = Array.APERTURE_LIST[data.getMinAperture()];
            String maxAperture = Array.APERTURE_LIST[data.getMaxAperture()];
            String minFocal = Array.FOCAL_LENGTH[data.getMinFocalLength()];
            String maxFocal = Array.FOCAL_LENGTH[data.getMaxFocalLength()];
            String minSubjectDistance = Array.SUBJECT_DISTANCE[data
                    .getMinSubjectDistance()];
            String maxSubjectDistance = Array.SUBJECT_DISTANCE[data
                    .getMaxSubjectDistance()];

            if (minMaxAperture.getMinValue().equals(minAperture) == false
                    || minMaxAperture.getMaxValue().equals(maxAperture) == false) {
                savedData.setAperturePosition(0);
            } else {
                savedData.setAperturePosition(mWheelAperture.getCurrentItem());
            }

            if (minMaxFocalLength.getMinValue().equals(minFocal) == false
                    || minMaxFocalLength.getMaxValue().equals(maxFocal) == false) {
                savedData.setFocalLengthPosition(0);
            } else {
                savedData.setFocalLengthPosition(mWheelFocalLength
                        .getCurrentItem());
            }

            if (minMaxSubjectDistance.getMinValue().equals(minSubjectDistance) == false
                    || minMaxSubjectDistance.getMaxValue().equals(
                            maxSubjectDistance) == false) {
                savedData.setSubjectDistancePosition(0);
            } else {
                savedData.setSubjectDistancePosition(mWheelSubjectDistance
                        .getCurrentItem());
            }

            savedData
                    .setMeasureUnitPosition(mWheelMeasureUnit.getCurrentItem());

            arrayToWheels(savedData);

            recalculate();
        }
    }

    private final static String APERTURE = "dof_aperture";

    private final static String CAMERA = "dof_camera";
    private final static String FAR_LIMIT = "dof_farLimit";

    private final static String FOCAL_LENGTH = "dof_focalLength";
    private final static String HYPERFOCAL = "dof_hyperfocal";
    private final static String MEASURE_RESULT_UNIT = "dof_measureResultUnit";
    private final static String MEASURE_UNIT = "dof_measureUnit";
    private final static String NEAR_LIMIT = "dof_nearLimit";
    private final static String SUBJECT_DISTANCE = "dof_subjectDistance";

    private static final String IS_LIMIT_PRESENT = "dof_is_limit_present";
    private static final String LIMIT_APERTURE_MIN = "dof_aperture_min_limit";
    private static final String LIMIT_APERTURE_MAX = "dof_aperture_max_limit";
    private static final String LIMIT_FOCAL_LENGTH_MIN = "dof_focal_length_min_limit";
    private static final String LIMIT_FOCAL_LENGTH_MAX = "dof_focal_length_max_limit";
    private static final String LIMIT_SUBJECT_DISTANCE_MIN = "dof_subject_distance_min_limit";
    private static final String LIMIT_SUBJECT_DISTANCE_MAX = "dof_subject_distance_max_limit";

    private final static String TOTAL = "dof_total";
    private final static String VENDOR = "dof_vendor";
    private SherlockFragmentActivity mActivity;
    private int mCameraId;

    private ImageView mCameraManagement;
    private ImageView mDofLimit;
    private Context mContext;
    private TextView mLabelCm;
    private TextView mLabelFarLimitResult;

    private TextView mLabelFt;

    private TextView mLabelHyperFocalResult;
    private TextView mLabelIn;
    private TextView mLabelM;
    private TextView mLabelNearLimitResult;
    private TextView mLabelTotalResutl;
    private int mMeasureResultUnit;
    private int mVendorId;
    private View mView;
    private WheelView mWheelAperture;
    private WheelView mWheelCamera;
    private WheelView mWheelFocalLength;

    private WheelView mWheelMeasureUnit;
    private WheelView mWheelSubjectDistance;

    private WheelView mWheelVendor;

    private Limit mLimit;

    /**
     * Loads arrays to wheels
     * 
     * @param savedData
     *            saved position for wheels
     */
    private void arrayToWheels(SavedData savedData) {
        final String measureUnits[] = getResources().getStringArray(
                R.array.dof_measureUnits);
        final int commonWheelSize = R.dimen.wheelTextSize;
        final int measureWheelTextSize = R.dimen.wheelMeasureUnitTextSize;

        List<String> apertureList;
        List<String> focalLengthList;
        List<String> subjectDistance;

        if (mLimit != null) {
            apertureList = Array.getValues(Array.APERTURE_LIST,
                    mLimit.getMinAperture(), mLimit.getMaxAperture());
            focalLengthList = Array.getValues(Array.FOCAL_LENGTH,
                    mLimit.getMinFocalLength(), mLimit.getMaxFocalLength());
            subjectDistance = Array.getValues(Array.SUBJECT_DISTANCE,
                    mLimit.getMinSubjectDistance(),
                    mLimit.getMaxSubjectDistance());
        } else {
            apertureList = new ArrayList<String>(
                    Arrays.asList(Array.APERTURE_LIST));
            focalLengthList = new ArrayList<String>(
                    Arrays.asList(Array.FOCAL_LENGTH));
            subjectDistance = new ArrayList<String>(
                    Arrays.asList(Array.SUBJECT_DISTANCE));
        }

        Common.setWheelAdapter(mActivity, mWheelAperture, apertureList,
                commonWheelSize, false);

        Common.setWheelAdapter(mActivity, mWheelFocalLength, focalLengthList,
                commonWheelSize, false);

        Common.setWheelAdapter(mActivity, mWheelMeasureUnit, measureUnits,
                measureWheelTextSize, false);

        Common.setWheelAdapter(mActivity, mWheelSubjectDistance,
                subjectDistance, commonWheelSize, false);

        mWheelAperture.setCurrentItem(savedData.getAperturePosition());
        mWheelFocalLength.setCurrentItem(savedData.getFocalLengthPosition());
        mWheelMeasureUnit.setCurrentItem(savedData.getMeasureUnitPosition());
        mWheelSubjectDistance.setCurrentItem(savedData
                .getSubjectDistancePosition());
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

        mCameraManagement = (ImageView) mView
                .findViewById(R.id.dof_cameraManagement);
        mDofLimit = (ImageView) mView.findViewById(R.id.dof_limitation);

        mActivity = getSherlockActivity();
    }

    /**
     * @return aperture value from aperture wheel
     */
    private BigDecimal getAperture() {
        BigDecimal aperture = new BigDecimal(
                Array.SCIENTIFIC_ARERTURES[Array
                        .getApertureIndex((String) mWheelAperture
                                .getSelectedItem())]);
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

        if (Log.DEBUG) {
            if (coc != null) {
                Log.variable("coc", coc.toString());
            } else {
                Log.variable("coc", "null");
            }
        }

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
        final int textSize = (int) (getResources().getDimension(
                R.dimen.wheelTextSize) / getResources().getDisplayMetrics().density);

        ArrayWheelAdapter<CameraData.Vendor> adapter = new ArrayWheelAdapter<CameraData.Vendor>(
                getSherlockActivity(), vendors);
        mWheelVendor.setViewAdapter(adapter);
        adapter.setTextSize(textSize);

        mWheelVendor.setCurrentItem(mVendorId);

        populateCameraByVendor();
    }

    private class SavedData {
        private int mAperturePosition;
        private int mFocalLengthPosition;
        private int mMeasureUnitPosition;
        private int mSubjectDistancePosition;

        public int getAperturePosition() {
            return mAperturePosition;
        }

        public void setAperturePosition(int aperturePosition) {
            this.mAperturePosition = aperturePosition;
        }

        public int getFocalLengthPosition() {
            return mFocalLengthPosition;
        }

        public void setFocalLengthPosition(int focalLengthPosition) {
            this.mFocalLengthPosition = focalLengthPosition;
        }

        public int getMeasureUnitPosition() {
            return mMeasureUnitPosition;
        }

        public void setMeasureUnitPosition(int measureUnitPosition) {
            this.mMeasureUnitPosition = measureUnitPosition;
        }

        public int getSubjectDistancePosition() {
            return mSubjectDistancePosition;
        }

        public void setSubjectDistancePosition(int subjectDistancePosition) {
            this.mSubjectDistancePosition = subjectDistancePosition;
        }
    }

    /**
     * Loads data from shared preferences
     * 
     * @return saved position for wheels
     */
    private SavedData loadSavedData() {
        Log.enter();
        SavedData savedData = new SavedData();

        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);
        mVendorId = prefs.getInt(VENDOR, 0);
        mCameraId = prefs.getInt(CAMERA, 0);

        savedData.setAperturePosition(prefs.getInt(APERTURE, 0));
        savedData.setFocalLengthPosition(prefs.getInt(FOCAL_LENGTH, 0));
        savedData.setMeasureUnitPosition(prefs.getInt(MEASURE_UNIT, 0));
        savedData.setSubjectDistancePosition(prefs.getInt(SUBJECT_DISTANCE, 0));

        mMeasureResultUnit = prefs.getInt(MEASURE_RESULT_UNIT, Constants.METER);

        if (prefs.getBoolean(IS_LIMIT_PRESENT, false)) {
            mLimit = new Limit();
            mLimit.setMinAperture(prefs.getInt(LIMIT_APERTURE_MIN, -1));
            mLimit.setMaxAperture(prefs.getInt(LIMIT_APERTURE_MAX, -1));
            mLimit.setMaxSubjectDistance(prefs.getInt(
                    LIMIT_SUBJECT_DISTANCE_MAX, -1));
            mLimit.setMinSubjectDistance(prefs.getInt(
                    LIMIT_SUBJECT_DISTANCE_MIN, -1));
            mLimit.setMaxFocalLength(prefs.getInt(LIMIT_FOCAL_LENGTH_MAX, -1));
            mLimit.setMinFocalLength(prefs.getInt(LIMIT_FOCAL_LENGTH_MIN, -1));
        }
        return savedData;
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
        mContext = mView.getContext();

        bindObjectsToResources();

        setCyclicToWheels();
        SavedData savedData = loadSavedData();
        arrayToWheels(savedData);
        loadCamerasDataToWheel();
        vendorSelectionHandler();
        measureButtonsHandler();
        wheelsHandler();
        updateMeasureButtons();
        recalculate();
        setClickListeners();

        Log.exit(start);
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.enter();

        saveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.enter();
    }

    /**
     * Populates camera models by selected vendor
     */
    private void populateCameraByVendor() {
        CameraData.Vendor vendor = (CameraData.Vendor) mWheelVendor
                .getSelectedItem();
        String cameras[] = CameraData.getCameraByVendor(vendor, mContext);
        final int textSize = R.dimen.wheelCameraTextSize;

        Common.setWheelAdapter(mActivity, mWheelCamera, cameras, textSize,
                false);
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

        /* if user cameras is empty coc is null */
        if (coc != null) {
            int measureUnit = getMeasureUnit();
            BigDecimal subjectDistance = getSubjectDistance();

            DofCalculator dofCalc = new DofCalculator(aperture, focusLength,
                    coc, subjectDistance, measureUnit);
            DofCalculator.CalculationResult calculationResult = dofCalc
                    .calculate(mMeasureResultUnit);

            mLabelFarLimitResult.setText(calculationResult
                    .format(calculationResult.getFarLimit()));
            mLabelNearLimitResult.setText(calculationResult
                    .format(calculationResult.getNearLimit()));
            mLabelHyperFocalResult.setText(calculationResult
                    .format(calculationResult.getHyperFocal()));
            mLabelTotalResutl.setText(calculationResult
                    .format(calculationResult.getTotal()));
        } else {
            mLabelFarLimitResult.setText("");
            mLabelNearLimitResult.setText("");
            mLabelHyperFocalResult.setText("");
            mLabelTotalResutl.setText("");
        }
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

        boolean isLimitPresent = (mLimit != null);
        if (isLimitPresent) {
            editor.putInt(LIMIT_APERTURE_MAX, mLimit.getMaxAperture());
            editor.putInt(LIMIT_APERTURE_MIN, mLimit.getMinAperture());
            editor.putInt(LIMIT_FOCAL_LENGTH_MAX, mLimit.getMaxFocalLength());
            editor.putInt(LIMIT_FOCAL_LENGTH_MIN, mLimit.getMinFocalLength());
            editor.putInt(LIMIT_SUBJECT_DISTANCE_MAX,
                    mLimit.getMaxSubjectDistance());
            editor.putInt(LIMIT_SUBJECT_DISTANCE_MIN,
                    mLimit.getMinSubjectDistance());
        }

        editor.putBoolean(IS_LIMIT_PRESENT, isLimitPresent);
        editor.commit();
    }

    private void setClickListeners() {
        mCameraManagement.setOnClickListener(new ButtonClickListener());
        mCameraManagement.setOnTouchListener(new ImageOnTouchListener());

        mDofLimit.setOnClickListener(new ButtonClickListener());
        mDofLimit.setOnTouchListener(new ImageOnTouchListener());
    }

    /**
     * Sets wheels to cyclic
     */
    private void setCyclicToWheels() {
        mWheelAperture.setCyclic(true);
        mWheelCamera.setCyclic(true);
        mWheelFocalLength.setCyclic(true);
        mWheelMeasureUnit.setCyclic(true);
        mWheelSubjectDistance.setCyclic(true);
        mWheelVendor.setCyclic(true);
    }

    private void showLimitataionDialog() {
        if (Constants.PAID) {
            DofLimitationDialog limitDialog = DofLimitationDialog
                    .getInstance(mContext);
            limitDialog.setCallback(new DofLimitationListener());
            limitDialog.setLimitData(mLimit);
            limitDialog.show(getFragmentManager(),
                    DofLimitationDialog.DIALOG_TAG);
        } else {
            Common.gotoDonate(mContext);
        }
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
            public void onScrollingFinished(WheelView wheel) {
                mCameraId = 0;
                populateCameraByVendor();
                recalculate();
            }

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // nothing

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
