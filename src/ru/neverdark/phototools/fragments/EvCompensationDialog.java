package ru.neverdark.phototools.fragments;

import ru.neverdark.phototools.R;
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
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO закончили скроллить, нужно взять значение и отобразить в TextView
        }

    }

    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            if (mCallback != null) {
                
                mCallback.onEvCompensationHandler(getCompensationIndex());
            }
        }

        private int getCompensationIndex() {
            return mSeekBar.getProgress();
        }

    }

    public static final String DIALOG_TAG = "evCompensationDialog";
    
    private Context mContext;
    private View mView;
    private SeekBar mSeekBar;
    private TextView mLabelValue;
    
    private int mStep;
    private int mCompensationIndex;
    
    private AlertDialog.Builder mAlertDialog;
    
    public interface OnEvCompensationListener {
        public void onEvCompensationHandler(int compensationIndex);
    }
    
    private OnEvCompensationListener mCallback;
    
    /**
     * Binds objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.ev_compensation_dialog, null);
        mSeekBar = (SeekBar) mView.findViewById(R.id.ev_compensation_seekBar);
        mLabelValue = (TextView) mView.findViewById(R.id.ev_compensation_label_value);
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
    }
    
    public void setCompensationIndex(int compensationIndex) {
        mCompensationIndex = compensationIndex;
    }
    
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.evCompensation_title);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjectToResource();
        createDialog();
        setListeners();

        return mAlertDialog.create();
    }

    private void setListeners() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }
}
