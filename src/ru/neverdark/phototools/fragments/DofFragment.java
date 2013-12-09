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
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.dofcalculator.DofCalculator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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
        // TODO Получить значение диафрагмы из колеса
        return null;
    }

    /**
     * @ return cirle of confusion for selected camera
     */
    private BigDecimal getCoc() {
        // TODO получить кружок нерезкости у выбранной камеры
        return null;
    }

    /**
     * @return focus length from wheel
     */
    private BigDecimal getFocusLength() {
        // TODO получить значение фокусного расстояния из колеса
        return null;
    }

    /**
     * @return measure unit from wheel
     */
    private int getMeasureUnit() {
        // TODO получить единицу измерений дистанции фокусировки
        return 0;
    }

    /**
     * @return subject distance from wheel
     */
    private BigDecimal getSubjectDistance() {
        // TODO получить значение дистанции фокусировки
        return null;
    }

    /**
     * Loads cameras data to wheels
     */
    private void loadCamerasDataToWheel() {
        Log.enter();

        // TODO: загрузить список производителей
        // Выставить сохраненного ранее производителя
        // Загрузить модели камер производителя
        // Выставить сохраненную ранее модель камеры
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
        Log.enter();
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_dof, container, false);

        bindObjectsToResources();
        loadSavedData();
        loadCamerasDataToWheel();
        vendorSelectionHandler();
        measureButtonsHandler();
        wheelsHandler();
        updateMeasureButtons();

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();

        saveData();
    }

    /**
     * Recalculation and show result
     */
    private void recalculate() {
        Log.enter();

        BigDecimal aperture = getAperture();
        BigDecimal focusLength = getFocusLength();
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
        // TODO: при выборе нового вендора необходимо перезагружать список камер
        // от этого производителя. Курсор при этом сбрасывать на первую позицию
    }

    /**
     * Handler for swipe event handle from all wheels
     */
    private void wheelsHandler() {
        Log.enter();
        // TODO: при прекращении прокрутки любого колеса необходимо запускать
        // перерасчет результата учитывая выбранные единицы измерния для
        // результатов
    }
}
