package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.DialogInterface;

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Constants;

public class ConfirmActionDialog extends UfoDialogFragment {
    public static final String DIALOG_ID = "confirmActionDialog";
    private int mAction;
    private OnPositiveClickListener mCallback;
    private String mCamera;

    public static ConfirmActionDialog getInstance(Context context, int action, String camera, OnPositiveClickListener callback) {
        ConfirmActionDialog dialog = new ConfirmActionDialog();
        dialog.setContext(context);
        dialog.mAction = action;
        dialog.mCamera = camera;
        dialog.mCallback = callback;
        return dialog;
    }

    @Override
    public void bindObjects() {

    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialogConfirmCreate_positive,
                new PositiveClickListener());
        getAlertDialog().setNegativeButton(R.string.dialogConfirmCreate_negative,
                new CancelClickListener());
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        int dialogTitleId = 0;
        int dialogMessageId = 0;

        if (mAction == Constants.COPY_CAMERA) {
            dialogTitleId = R.string.confirm_action_copy_title;
            dialogMessageId = R.string.confirm_action_copy_message;
        } else if (mAction == Constants.REMOVE_CAMERA) {
            dialogTitleId = R.string.confirm_action_remove_title;
            dialogMessageId = R.string.confirm_action_remove_message;
        }

        getAlertDialog().setTitle(dialogTitleId);
        getAlertDialog().setMessage(String.format(getString(dialogMessageId), mCamera));
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(String camera, int action);
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mCallback.onPositiveClick(mCamera, mAction);
        }
    }
}
