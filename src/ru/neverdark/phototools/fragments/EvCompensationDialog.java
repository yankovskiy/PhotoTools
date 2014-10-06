package ru.neverdark.phototools.fragments;

import ru.neverdark.phototools.R;
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.evcalculator.EvData;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EvCompensationDialog extends SherlockDialogFragment {
    private class SeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            mLabelValue.setText(mCompensationList[progress]);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mCallback != null) {
                mCallback.onEvCompensationHandler(getShiftIndex());
            }
        }
    }

    private int getShiftIndex() {
        return getShiftFromPosition(mSeekBar.getProgress());
    }

    public static final String DIALOG_TAG = "evCompensationDialog";

    private Context mContext;
    private View mView;
    private SeekBar mSeekBar;
    private TextView mLabelValue;

    private int mStep;
    private int mShiftIndex = EvData.INVALID_INDEX;
    private String[] mCompensationList;

    private AlertDialog.Builder mAlertDialog;

    public interface OnEvCompensationListener {
        public void onEvCompensationHandler(int compensationShift);
    }

    private OnEvCompensationListener mCallback;

    private int mZeroPosition;

    /**
     * Binds objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.ev_compensation_dialog, null);
        mSeekBar = (SeekBar) mView.findViewById(R.id.ev_compensation_seekBar);
        mLabelValue = (TextView) mView
                .findViewById(R.id.ev_compensation_label_value);
    }

    public static EvCompensationDialog getInstance(Context context) {
        EvCompensationDialog dialog = new EvCompensationDialog();
        dialog.mContext = context;
        return dialog;
    }

    public void setCallback(OnEvCompensationListener callback) {
        mCallback = callback;
    }

    public void setStep(int step) {
        mStep = step;
        mCompensationList = EvData.getEvCompensationValues(mStep);
    }

    public void setShiftIndex(int shiftIndex) {
        mShiftIndex = shiftIndex;
    }

    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.evCompensation_title);
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
        bindObjectToResource();
        createDialog();
        setListeners();
        initCompensation();

        return mAlertDialog.create();
    }

    private void setListeners() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok,
                new PositiveClickListener());
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    private int getShiftFromPosition(int positionIndex) {
        int shift = 0;

        if (positionIndex != mZeroPosition) {
            shift = positionIndex - mZeroPosition;
        }

        Log.variable("shift", String.valueOf(shift));
        return shift;
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
}
