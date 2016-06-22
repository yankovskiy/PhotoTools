/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import ru.neverdark.phototools.R;
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.evcalculator.EvData;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EvCompensationDialog extends UfoDialogFragment {
    public static final String DIALOG_TAG = "evCompensationDialog";
    private SeekBar mSeekBar;
    private TextView mLabelValue;
    private int mShiftIndex = EvData.INVALID_INDEX;
    private String[] mCompensationList;
    private String[] mNdCompensationList;
    private int mZeroPosition;
    private CheckBox mIsNDFilter;
    private static final String IS_NDFILTER = "ev_compensation_is_nd";

    public static EvCompensationDialog getInstance(Context context) {
        EvCompensationDialog dialog = new EvCompensationDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.ev_compensation_dialog, null));
        mSeekBar = (SeekBar) getDialogView().findViewById(R.id.ev_compensation_seekBar);
        mLabelValue = (TextView) getDialogView().findViewById(R.id.ev_compensation_label_value);
        mIsNDFilter = (CheckBox) getDialogView().findViewById(R.id.ev_compensation_checkbox);
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.evCompensation_title);
    }

    private int getPositionFromShift(int shift) {
        int position;

        if (shift == 0) {
            position = mZeroPosition;
        } else {
            position = mZeroPosition + shift;
        }

        Log.variable("position", String.valueOf(position));
        return position;
    }

    private int getShiftFromPosition(int positionIndex) {
        int shift = 0;

        if (positionIndex != mZeroPosition) {
            shift = positionIndex - mZeroPosition;
        }

        Log.variable("shift", String.valueOf(shift));
        return shift;
    }

    private int getShiftIndex() {
        return getShiftFromPosition(mSeekBar.getProgress());
    }

    public void setShiftIndex(int shiftIndex) {
        mShiftIndex = shiftIndex;
    }

    private void initCompensation() {
        int length = mCompensationList.length - 1;
        mZeroPosition = length / 2;
        int position = mZeroPosition;
        mSeekBar.setMax(length);

        if (mShiftIndex != EvData.INVALID_INDEX) {
            position = getPositionFromShift(mShiftIndex);
        }

        mSeekBar.setProgress(position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        SharedPreferences prefs = getActivity().getPreferences(
                Context.MODE_PRIVATE);
        mIsNDFilter.setChecked(prefs.getBoolean(IS_NDFILTER, false));
        initCompensation();
        return dialog;
    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        getAlertDialog().setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        mIsNDFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateValueWithNd(mSeekBar.getProgress());
            }
        });
    }

    public void setStep(int step) {
        mCompensationList = EvData.getEvCompensationValues(step);
        mNdCompensationList = EvData.getEvNdCompensationValues(step);
    }

    public interface OnEvCompensationListener {
        void onEvCompensationHandler(int compensationShift);
    }

    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            OnEvCompensationListener callback = (OnEvCompensationListener) getCallback();
            if (callback != null) {
                callback.onEvCompensationHandler(getShiftIndex());
                SharedPreferences prefs = getActivity().getPreferences(
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(IS_NDFILTER, mIsNDFilter.isChecked());
                editor.apply();
            }
        }
    }

    private void updateValueWithNd(int progress) {
        String value = mCompensationList[progress];

        if (mIsNDFilter.isChecked()) {
            if (progress > mZeroPosition) {
                value = mNdCompensationList[progress];
            }
        }

        mLabelValue.setText(value);
    }

    private class SeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateValueWithNd(progress);
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }
}
