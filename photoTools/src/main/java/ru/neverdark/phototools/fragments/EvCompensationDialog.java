/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EvCompensationDialog extends UfoDialogFragment {
    public interface OnEvCompensationListener {
        public void onEvCompensationHandler(int compensationShift);
    }

    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            OnEvCompensationListener callback = (OnEvCompensationListener) getCallback();
            if (callback != null) {
                callback.onEvCompensationHandler(getShiftIndex());
            }
        }
    }

    private class SeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mLabelValue.setText(mCompensationList[progress]);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    public static final String DIALOG_TAG = "evCompensationDialog";

    public static EvCompensationDialog getInstance(Context context) {
        EvCompensationDialog dialog = new EvCompensationDialog();
        dialog.setContext(context);
        return dialog;
    }
    private SeekBar mSeekBar;

    private TextView mLabelValue;
    private int mStep;
    private int mShiftIndex = EvData.INVALID_INDEX;

    private String[] mCompensationList;

    private int mZeroPosition;

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.ev_compensation_dialog, null));
        mSeekBar = (SeekBar) getDialogView().findViewById(R.id.ev_compensation_seekBar);
        mLabelValue = (TextView) getDialogView().findViewById(R.id.ev_compensation_label_value);
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.evCompensation_title);
    }

    private int getPositionFromShift(int shift) {
        int position = 0;

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
        initCompensation();
        return dialog;
    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    public void setShiftIndex(int shiftIndex) {
        mShiftIndex = shiftIndex;
    }

    public void setStep(int step) {
        mStep = step;
        mCompensationList = EvData.getEvCompensationValues(mStep);
    }
}
