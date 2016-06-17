/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
 ******************************************************************************/
package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.EvWheelsData;
import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.evcalculator.EvCalculator;
import ru.neverdark.phototools.utils.evcalculator.EvData;

public class EvDiffFragment extends UfoFragment {
    private static final String PREF_IS_LIMIT = "evdiff_is_limit";
    private static final String PREF_MIN_APERTURE = "evdiff_min_aperture";
    private static final String PREF_MAX_APERTURE = "evdiff_max_aperture";
    private static final String PREF_MIN_ISO = "evdiff_min_iso";
    private static final String PREF_MAX_ISO = "evdiff_max_iso";
    private static final String PREF_MIN_SHUTTER = "evdiff_min_shutter";
    private static final String PREF_MAX_SHUTTER = "evdiff_max_shutter";
    private static final String PREF_EV_STEP = "evdiff_ev_step";
    private static final String PREF_CURRENT_APERTURE = "evdiff_current_aperture";
    private static final String PREF_CURRENT_ISO = "evdiff_current_iso";
    private static final String PREF_CURRENT_SHUTTER = "evdiff_current_shutter";
    private static final String PREF_NEW_APERTURE = "evdiff_new_aperture";
    private static final String PREF_NEW_ISO = "evdiff_new_iso";
    private static final String PREF_NEW_SHUTTER = "evdiff_new_shutter";
    private View mView;
    private Context mContext;
    private ImageView mLimitationButton;
    private TextView mFullStopButton;
    private TextView mHalfStopButton;
    private TextView mThirdStopButton;
    private TextView mCalculationResultLabel;
    private WheelView mCurrentApertureWheel;
    private WheelView mCurrentIsoWheel;
    private WheelView mCurrentShutterSpeedWheel;
    private WheelView mNewApertureWheel;
    private WheelView mNewIsoWheel;
    private WheelView mNewShutterSpeedWheel;
    private Limit mLimit = new Limit();
    private EvCalculator mEvCalculator = new EvCalculator();

    @Override
    public void bindObjects() {
        mLimitationButton = (ImageView) mView.findViewById(R.id.evdiff_limitation);
        mFullStopButton = (TextView) mView.findViewById(R.id.evdiff_label_stepFull);
        mHalfStopButton = (TextView) mView.findViewById(R.id.evdiff_label_stepHalf);
        mThirdStopButton = (TextView) mView.findViewById(R.id.evdiff_label_stepThird);
        mCalculationResultLabel = (TextView) mView.findViewById(R.id.evdiff_result);
        mCurrentApertureWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_currentAperture);
        mCurrentIsoWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_currentIso);
        mCurrentShutterSpeedWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_currentShutter);
        mNewApertureWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_newAperture);
        mNewIsoWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_newIso);
        mNewShutterSpeedWheel = (WheelView) mView.findViewById(R.id.evdiff_wheel_newShutter);

        mCurrentApertureWheel.setCyclic(true);
        mCurrentIsoWheel.setCyclic(true);
        mCurrentShutterSpeedWheel.setCyclic(true);
        mNewApertureWheel.setCyclic(true);
        mNewIsoWheel.setCyclic(true);
        mNewShutterSpeedWheel.setCyclic(true);
    }

    @Override
    public void setListeners() {
        // Обработчик тапов по кнопкам
        ButtonClickListener listener = new ButtonClickListener();
        mLimitationButton.setOnClickListener(listener);
        mLimitationButton.setOnTouchListener(new ImageOnTouchListener());
        mFullStopButton.setOnClickListener(listener);
        mHalfStopButton.setOnClickListener(listener);
        mThirdStopButton.setOnClickListener(listener);

        // Обработчик колес
        mCurrentApertureWheel.addScrollingListener(new WheelScrollListener());
        mCurrentIsoWheel.addScrollingListener(new WheelScrollListener());
        mCurrentShutterSpeedWheel.addScrollingListener(new WheelScrollListener());
        mNewApertureWheel.addScrollingListener(new WheelScrollListener());
        mNewIsoWheel.addScrollingListener(new WheelScrollListener());
        mNewShutterSpeedWheel.addScrollingListener(new WheelScrollListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.evdiff_fragment, container, false);
        mContext = mView.getContext();

        bindObjects();
        setListeners();

        initWheels();
        return mView;
    }

    /**
     * Получение позиций всех колес
     *
     * @return объект содержащий позиции всех колес
     */
    private EvWheelsData getWheelsPositions() {
        EvWheelsData evWheelsData = new EvWheelsData();

        evWheelsData.setCurrentAperturePosition(mCurrentApertureWheel.getCurrentItem());
        evWheelsData.setCurrentIsoPosition(mCurrentIsoWheel.getCurrentItem());
        evWheelsData.setCurrentShutterPosition(mCurrentShutterSpeedWheel.getCurrentItem());

        evWheelsData.setNewAperturePosition(mNewApertureWheel.getCurrentItem());
        evWheelsData.setNewIsoPosition(mNewIsoWheel.getCurrentItem());
        evWheelsData.setNewShutterPosition(mNewShutterSpeedWheel.getCurrentItem());

        return evWheelsData;
    }

    private void initWheels() {
        EvWheelsData evWheelsData = loadData();
        updateStepButtons(mLimit.getEvStep(), evWheelsData);
    }

    /**
     * Загрузка сохраненных ранее позиций колес, результатов расчета, значений ограничителей
     *
     * @return объект содержащий позиции всех колес
     */
    private EvWheelsData loadData() {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        mLimit.setEvStep(prefs.getInt(PREF_EV_STEP, EvData.FULL_STOP));

        if (prefs.getBoolean(PREF_IS_LIMIT, false)) {
            mLimit.setMinAperture(prefs.getInt(PREF_MIN_APERTURE, 0));
            mLimit.setMaxAperture(prefs.getInt(PREF_MAX_APERTURE, 0));
            mLimit.setMinIso(prefs.getInt(PREF_MIN_ISO, 0));
            mLimit.setMaxIso(prefs.getInt(PREF_MAX_ISO, 0));
            mLimit.setMinShutter(prefs.getInt(PREF_MIN_SHUTTER, 0));
            mLimit.setMaxShutter(prefs.getInt(PREF_MAX_SHUTTER, 0));
        }

        EvWheelsData data = new EvWheelsData();
        data.setCurrentAperturePosition(prefs.getInt(PREF_CURRENT_APERTURE, 0));
        data.setCurrentIsoPosition(prefs.getInt(PREF_CURRENT_ISO, 0));
        data.setCurrentShutterPosition(prefs.getInt(PREF_CURRENT_SHUTTER, 0));

        data.setNewAperturePosition(prefs.getInt(PREF_NEW_APERTURE, 0));
        data.setNewIsoPosition(prefs.getInt(PREF_NEW_ISO, 0));
        data.setNewShutterPosition(prefs.getInt(PREF_NEW_SHUTTER, 0));

        return data;
    }

    /**
     * Сохранение позиций колес, результатов расчета, значений ограничителей
     */
    private void saveData() {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(PREF_IS_LIMIT, mLimit.isLimit());
        EvWheelsData data = getWheelsPositions();

        if (mLimit.isLimit()) {
            editor.putInt(PREF_MIN_APERTURE, mLimit.getMinAperture());
            editor.putInt(PREF_MAX_APERTURE, mLimit.getMaxAperture());
            editor.putInt(PREF_MIN_ISO, mLimit.getMinIso());
            editor.putInt(PREF_MAX_ISO, mLimit.getMaxIso());
            editor.putInt(PREF_MIN_SHUTTER, mLimit.getMinShutter());
            editor.putInt(PREF_MAX_SHUTTER, mLimit.getMaxShutter());
        }

        editor.putInt(PREF_EV_STEP, mLimit.getEvStep());
        editor.putInt(PREF_CURRENT_APERTURE, data.getCurrentAperturePosition());
        editor.putInt(PREF_CURRENT_ISO, data.getCurrentIsoPosition());
        editor.putInt(PREF_CURRENT_SHUTTER, data.getCurrentShutterPosition());
        editor.putInt(PREF_NEW_APERTURE, data.getNewAperturePosition());
        editor.putInt(PREF_NEW_ISO, data.getNewIsoPosition());
        editor.putInt(PREF_NEW_SHUTTER, data.getNewShutterPosition());

        editor.apply();
    }

    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }

    /**
     * Обновление подсветки кнопки шага экспозиции. Вызывается при клике на соответствующую кнопку.
     * Также колеса заполняет нужным набором данных с заданным шагом и сбрасывают свои позиции на
     * первый элемент
     *
     * @param step         шаг изменения экпозиции FULL_STOP, HALF_STOP, THIRD_STOP
     * @param evWheelsData объект содержащий позиции всех колес
     * @see EvData
     */
    private void updateStepButtons(int step, EvWheelsData evWheelsData) {
        Common.setBg(mFullStopButton, R.drawable.right_stroke);
        Common.setBg(mHalfStopButton, R.drawable.right_stroke);
        Common.setBg(mThirdStopButton, R.drawable.right_stroke);

        switch (step) {
            case EvData.FULL_STOP:
                Common.setBg(mFullStopButton, R.drawable.left_blue_button);
                break;
            case EvData.HALF_STOP:
                Common.setBg(mHalfStopButton, R.drawable.middle_blue_button);
                break;
            case EvData.THIRD_STOP:
                Common.setBg(mThirdStopButton, R.drawable.right_blue_button);
                break;
        }

        mLimit.setEvStep(step);
        loadDataToWheels();
        setWheelsPos(evWheelsData);
        recalculate();
    }

    /**
     * Обновление набора данных в колесах
     */
    private void loadDataToWheels() {
        final int textSize = R.dimen.wheelTextSize;
        if (mLimit.isLimit()) {
            mEvCalculator.initArrays(mLimit.getEvStep(), mLimit);
        } else {
            mEvCalculator.initArrays(mLimit.getEvStep());
        }

        Common.setWheelAdapter(mContext, mCurrentApertureWheel,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mContext, mCurrentIsoWheel,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mContext, mCurrentShutterSpeedWheel,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);

        Common.setWheelAdapter(mContext, mNewApertureWheel,
                mEvCalculator.getApertureList(), textSize, false);
        Common.setWheelAdapter(mContext, mNewIsoWheel,
                mEvCalculator.getIsoList(), textSize, false);
        Common.setWheelAdapter(mContext, mNewShutterSpeedWheel,
                mEvCalculator.getShutterList(), R.dimen.wheelSutterTextSize,
                false);
    }

    /**
     * Установка значений всех колес
     *
     * @param evWheelsData объект содержащий новые позиции для всех колес
     */
    private void setWheelsPos(EvWheelsData evWheelsData) {
        mCurrentApertureWheel.setCurrentItem(evWheelsData.getCurrentAperturePosition());
        mCurrentIsoWheel.setCurrentItem(evWheelsData.getCurrentIsoPosition());
        mCurrentShutterSpeedWheel.setCurrentItem(evWheelsData.getCurrentShutterPosition());
        mNewApertureWheel.setCurrentItem(evWheelsData.getNewAperturePosition());
        mNewIsoWheel.setCurrentItem(evWheelsData.getNewIsoPosition());
        mNewShutterSpeedWheel.setCurrentItem(evWheelsData.getNewShutterPosition());
    }

    /**
     * Отображение диалогового окна для ограничений списка возможных значений
     */
    private void showLimitationDialog() {
        if (Constants.PAID) {
            EvLimitationDialog dialog = EvLimitationDialog
                    .getInstance(mContext);
            dialog.setCallback(new EvDiffLimitationListener());
            dialog.setLimitData(mLimit);
            dialog.show(getFragmentManager(), EvLimitationDialog.DIALOG_TAG);
        } else {
            Common.gotoDonate(mContext);
        }
    }

    /**
     * Пересчитать результат
     */
    private void recalculate() {
        EvWheelsData data = getWheelsPositions();
        mEvCalculator.prepare(data.getCurrentAperturePosition(), data.getCurrentIsoPosition(), data.getCurrentShutterPosition(),
                data.getNewAperturePosition(), data.getNewIsoPosition(), data.getNewShutterPosition(), EvData.INVALID_INDEX, EvData.INVALID_INDEX);
        String result = mEvCalculator.calculateEvDiff();
        mCalculationResultLabel.setText(result);
    }

    /**
     * Обработка нажатий на кнопки
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.evdiff_limitation:
                    showLimitationDialog();
                    break;
                case R.id.evdiff_label_stepFull:
                    updateStepButtons(EvData.FULL_STOP, new EvWheelsData(0));
                    break;
                case R.id.evdiff_label_stepHalf:
                    updateStepButtons(EvData.HALF_STOP, new EvWheelsData(0));
                    break;
                case R.id.evdiff_label_stepThird:
                    updateStepButtons(EvData.THIRD_STOP, new EvWheelsData(0));
                    break;
            }
        }
    }

    private class WheelScrollListener implements OnWheelScrollListener {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            recalculate();
        }
    }

    private class EvDiffLimitationListener implements EvLimitationDialog.OnEvLimitationListener, OnCallback {
        @Override
        public void onEvLimitationHandler(Limit data) {
            mLimit = data;
            loadDataToWheels();
            setWheelsPos(new EvWheelsData(0));
            recalculate();
        }
    }
}
