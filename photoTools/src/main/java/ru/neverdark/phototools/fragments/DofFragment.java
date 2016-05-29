/**
 * ****************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************
 */
package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.phototools.CameraManagementActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.fragments.DofLimitationDialog.OnDofLimitationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Common.MinMaxValues;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.dofcalculator.Array;
import ru.neverdark.phototools.utils.dofcalculator.CameraData;
import ru.neverdark.phototools.utils.dofcalculator.CameraData.Vendor;
import ru.neverdark.phototools.utils.dofcalculator.DofCalculator;

/**
 * Fragment contains Depth of Field calculator UI
 */
public class DofFragment extends UfoFragment {

    private final static String APERTURE = "dof_aperture";
    private final static String CAMERA = "dof_camera2";
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
    private boolean mFirst;
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
    private View mView;
    private WheelView mWheelAperture;
    private WheelView mWheelCamera;
    private WheelView mWheelFocalLength;
    private WheelView mWheelMeasureUnit;
    private WheelView mWheelSubjectDistance;
    private Limit mLimit;

    @Override
    public void bindObjects() {
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
    }

    @Override
    public void setListeners() {
        mCameraManagement.setOnClickListener(new ButtonClickListener());
        mCameraManagement.setOnTouchListener(new ImageOnTouchListener());

        mDofLimit.setOnClickListener(new ButtonClickListener());
        mDofLimit.setOnTouchListener(new ImageOnTouchListener());
    }

    /**
     * Loads arrays to wheels
     *
     * @param savedData saved position for wheels
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
            apertureList = new ArrayList<>(
                    Arrays.asList(Array.APERTURE_LIST));
            focalLengthList = new ArrayList<>(
                    Arrays.asList(Array.FOCAL_LENGTH));
            subjectDistance = new ArrayList<>(
                    Arrays.asList(Array.SUBJECT_DISTANCE));
        }

        Common.setWheelAdapter(mContext, mWheelAperture, apertureList,
                commonWheelSize, false);

        Common.setWheelAdapter(mContext, mWheelFocalLength, focalLengthList,
                commonWheelSize, false);

        Common.setWheelAdapter(mContext, mWheelMeasureUnit, measureUnits,
                measureWheelTextSize, false);

        Common.setWheelAdapter(mContext, mWheelSubjectDistance,
                subjectDistance, commonWheelSize, false);

        mWheelAperture.setCurrentItem(savedData.getAperturePosition());
        mWheelFocalLength.setCurrentItem(savedData.getFocalLengthPosition());
        mWheelMeasureUnit.setCurrentItem(savedData.getMeasureUnitPosition());
        mWheelSubjectDistance.setCurrentItem(savedData
                .getSubjectDistancePosition());
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
        String camera = (String) mWheelCamera.getSelectedItem();
        BigDecimal coc = CameraData.getCocForCamera(Vendor.USER, camera);
        Log.variable("camera", camera);

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
     * Loads data from shared preferences
     *
     * @return saved position for wheels
     */
    private SavedData loadSavedData() {
        Log.enter();
        SavedData savedData = new SavedData();

        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);
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
        mView = inflater.inflate(R.layout.dof_fragment, container, false);
        mContext = mView.getContext();

        bindObjects();

        setCyclicToWheels();
        SavedData savedData = loadSavedData();
        arrayToWheels(savedData);
        populateCameraByVendor();
        measureButtonsHandler();
        wheelsHandler();
        updateMeasureButtons();
        recalculate();
        setListeners();
        mFirst = true;
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
    public void onActivityCreated(Bundle savedInstanceData) {
        super.onActivityCreated(savedInstanceData);
        ViewTarget target = new ViewTarget(R.id.dof_cameraManagement, getActivity());
        new ShowcaseView.Builder(getActivity())
                .setTarget(target)
                .setContentTitle(R.string.dof_sc_title)
                .setContentText(R.string.dof_sc_message)
                .hideOnTouchOutside()
                .singleShot(Constants.DOF_CHOICE)
                .setStyle(R.style.SVCustom)
                .withMaterialShowcase()
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.enter();
        // populate and recalc only if resume from camera management
        if (mFirst) {
            mFirst = false;
        } else {
            populateCameraByVendor();
            recalculate();
        }
    }

    /**
     * Populates camera models by selected vendor
     */
    private void populateCameraByVendor() {
        String cameras[] = CameraData.getCameraByVendor(Vendor.USER, mContext);
        final int textSize = R.dimen.wheelCameraTextSize;

        Common.setWheelAdapter(mContext, mWheelCamera, cameras, textSize,
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
        editor.apply();
    }

    /**
     * Sets wheels to cyclic
     */
    private void setCyclicToWheels() {
        mWheelAperture.setCyclic(true);
        mWheelFocalLength.setCyclic(true);
        mWheelSubjectDistance.setCyclic(true);
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

    /**
     * Click listener for "Camera management" and "Limit" button
     */
    private class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dof_cameraManagement:
                    /*
                    CameraManagementDialog cameraDialog = CameraManagementDialog
                            .getInstance(mContext);
                    cameraDialog.setCallback(new CameraManagementListener());
                    cameraDialog.show(getFragmentManager(),
                            CameraManagementDialog.DIALOG_TAG);
                            */
                    Intent intent = new Intent();
                    intent.setClass(mContext, CameraManagementActivity.class);
                    startActivity(intent);
                    break;
                case R.id.dof_limitation:
                    showLimitataionDialog();
                    break;
            }
        }
    }

    /**
     * Listener for handling OK button in the DofLimitationDialog
     */
    private class DofLimitationListener implements OnDofLimitationListener, OnCallback {
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

            if (!minMaxAperture.getMinValue().equals(minAperture)
                    || !minMaxAperture.getMaxValue().equals(maxAperture)) {
                savedData.setAperturePosition(0);
            } else {
                savedData.setAperturePosition(mWheelAperture.getCurrentItem());
            }

            if (!minMaxFocalLength.getMinValue().equals(minFocal)
                    || !minMaxFocalLength.getMaxValue().equals(maxFocal)) {
                savedData.setFocalLengthPosition(0);
            } else {
                savedData.setFocalLengthPosition(mWheelFocalLength
                        .getCurrentItem());
            }

            if (!minMaxSubjectDistance.getMinValue().equals(minSubjectDistance)
                    || !minMaxSubjectDistance.getMaxValue().equals(
                    maxSubjectDistance)) {
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
}
