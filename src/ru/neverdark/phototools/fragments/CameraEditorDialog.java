/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.UserCamerasRecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Implements dialog for additing/editig cameras
 */
public class CameraEditorDialog extends SherlockDialogFragment {
    /**
     * Custom exception for showing dialog with error message
     */
    private class CameraEditorDialogException extends Exception {
        private static final long serialVersionUID = -3433785378864484422L;

        /**
         * Constructor
         * 
         * @param resourceId
         *            string Id contains error message
         */
        public CameraEditorDialogException(int resourceId) {
            ShowMessageDialog dialog = ShowMessageDialog.getInstance(mContext);
            dialog.setMessages(R.string.error, resourceId);
            dialog.show(getFragmentManager(), ShowMessageDialog.DIALOG_TAG);
        }
    }

    /**
     * Listener for handling changed state "Automatic calculation"
     */
    private class CheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            disableCustomCoc(isChecked);
        }
    }

    /**
     * Listener for "Cancel" button
     */
    private class NegativeClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    /**
     * Interface provides callback methods call for add or edit camera
     */
    public interface OnCameraEditorListener {
        /**
         * Called for add camera
         * 
         * @param record
         *            record contains data for add camera
         */
        public void onAddCamera(UserCamerasRecord record);

        /**
         * Called for edit camera
         * 
         * @param record
         *            record contains data for edit camera
         */
        public void onEditCamera(UserCamerasRecord record);
    }

    /**
     * Listener for "Ok" button
     */
    private class PositiveClickListener implements View.OnClickListener {

        /**
         * Checks for all data is filled
         * 
         * @return true if all data is fileld
         */
        private boolean isDataFilled() {
            boolean isFilled;

            isFilled = mResolutionWidth.getText().toString().trim().length() > 0;

            if (isFilled) {
                isFilled = mResolutionHeight.getText().toString().trim()
                        .length() > 0;
            }

            if (isFilled) {
                isFilled = mSensorWidth.getText().toString().trim().length() > 0;
            }

            if (isFilled) {
                isFilled = mSensorHeight.getText().toString().trim().length() > 0;
            }

            return isFilled;
        }

        @Override
        public void onClick(View v) {
            String cameraName = mCameraModel.getText().toString().trim();

            try {
                if (cameraName.length() == 0) {
                    throw new CameraEditorDialogException(
                            R.string.error_cameraNameEmpty);
                }

                if (isDataFilled() == false) {
                    throw new CameraEditorDialogException(
                            R.string.error_notAllValuesFilled);
                }

                if (mAutoCalcCoc.isChecked() == false
                        && mCoc.getText().toString().trim().length() == 0) {
                    throw new CameraEditorDialogException(
                            R.string.error_cocNotFilled);
                }

                boolean exist = false;

                if ((mActionType == ACTION_ADD)
                        || (mActionType == ACTION_EDIT && cameraName
                                .equals(mUserCamerasRecord.getCameraName()) == false)) {
                    DbAdapter dbAdapter = new DbAdapter(mContext).open();
                    exist = dbAdapter.getUserCameras()
                            .isCameraExist(cameraName);
                    dbAdapter.close();
                }

                if (exist) {
                    throw new CameraEditorDialogException(
                            R.string.error_cameraAlreadyExist);
                }

                dismiss();

                if (mCallback != null) {
                    fillUserCamerasRecord(mUserCamerasRecord);

                    if (mActionType == ACTION_ADD) {
                        mCallback.onAddCamera(mUserCamerasRecord);
                    } else if (mActionType == ACTION_EDIT) {
                        mCallback.onEditCamera(mUserCamerasRecord);
                    }
                }
            } catch (CameraEditorDialogException e) {

            }
        }
    }

    /**
     * Action for add new camera
     */
    public static final int ACTION_ADD = 0;
    /**
     * Action for edit exist camera
     */
    public static final int ACTION_EDIT = 1;
    /**
     * Dialog tag for fragment manager
     */
    public static final String DIALOG_TAG = "cameraEditorDialog";

    /**
     * Creates new dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static CameraEditorDialog getInstance(Context context) {
        CameraEditorDialog dialog = new CameraEditorDialog();
        dialog.mContext = context;
        return dialog;
    }

    private int mActionType;
    private AlertDialog.Builder mAlertDialog;
    private CheckBox mAutoCalcCoc;
    private OnCameraEditorListener mCallback;
    private EditText mCameraModel;
    private String mCameraName;
    private EditText mCoc;
    private Context mContext;
    private EditText mResolutionHeight;
    private EditText mResolutionWidth;
    private EditText mSensorHeight;
    private EditText mSensorWidth;
    private UserCamerasRecord mUserCamerasRecord;

    private View mView;

    /**
     * Binds objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.camera_editor_dialog, null);
        mResolutionWidth = (EditText) mView
                .findViewById(R.id.cameraEditor_resolutionWidth);
        mResolutionHeight = (EditText) mView
                .findViewById(R.id.cameraEditor_resolutionHeight);
        mSensorWidth = (EditText) mView
                .findViewById(R.id.cameraEditor_sensorWidth);
        mSensorHeight = (EditText) mView
                .findViewById(R.id.cameraEditor_sensorHeight);
        mCameraModel = (EditText) mView
                .findViewById(R.id.cameraEditor_cameraName);
        mCoc = (EditText) mView.findViewById(R.id.cameraEditor_coc);
        mAutoCalcCoc = (CheckBox) mView
                .findViewById(R.id.cameraEditor_autoCalc);
    }

    /**
     * Creates alert dialog
     */
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);

        if (mActionType == ACTION_ADD) {
            mAlertDialog.setTitle(R.string.userCamera_addCamera);
            disableCustomCoc(true);
            mCameraModel.setText(mCameraName);
            mAutoCalcCoc.setChecked(true);
            mUserCamerasRecord = new UserCamerasRecord();
        } else if (mActionType == ACTION_EDIT) {
            mAlertDialog.setTitle(R.string.userCamera_editCamera);

            boolean disableCoc = !mUserCamerasRecord.isCustomCoc();
            String cameraName = mUserCamerasRecord.getCameraName();
            String resolutionWidth = String.valueOf(mUserCamerasRecord
                    .getResolutionWidth());
            String resolutionHeight = String.valueOf(mUserCamerasRecord
                    .getResolutionHeight());
            String sensorWidth = String.valueOf(mUserCamerasRecord
                    .getSensorWidth());
            String sensorHeight = String.valueOf(mUserCamerasRecord
                    .getSensorHeight());
            String coc = String.valueOf(mUserCamerasRecord.getCoc());

            disableCustomCoc(disableCoc);
            mCameraModel.setText(cameraName);
            mResolutionWidth.setText(resolutionWidth);
            mResolutionHeight.setText(resolutionHeight);
            mSensorWidth.setText(sensorWidth);
            mSensorHeight.setText(sensorHeight);
            mCoc.setText(coc);
            mAutoCalcCoc.setChecked(disableCoc);
        }
    }

    /**
     * Disable EditText field for custom circle of confusion
     * 
     * @param isDisable
     *            true for disable
     */
    private void disableCustomCoc(boolean isDisable) {
        mCoc.setEnabled(!isDisable);
    }

    /**
     * Fills record for future processing
     * 
     * @param record
     *            record for filled
     */
    private void fillUserCamerasRecord(UserCamerasRecord record) {
        String cameraName = mCameraModel.getText().toString().trim();
        boolean isCustomCoc = !mAutoCalcCoc.isChecked();
        int resolutionWidth = Integer.valueOf(mResolutionWidth.getText()
                .toString().trim());
        int resolutionHeight = Integer.valueOf(mResolutionHeight.getText()
                .toString().trim());
        float sensorWidth = Float.valueOf(mSensorWidth.getText().toString()
                .trim());
        float sensorHeight = Float.valueOf(mSensorHeight.getText().toString()
                .trim());

        if (mAutoCalcCoc.isChecked() == false) {
            float coc = Float.valueOf(mCoc.getText().toString().trim());
            record.setCoc(coc);
        }

        record.setCameraName(cameraName);
        record.setIsCustomCoc(isCustomCoc);
        record.setResolutionHeight(resolutionHeight);
        record.setResolutionWidth(resolutionWidth);
        record.setSensorHeight(sensorHeight);
        record.setSensorWidth(sensorWidth);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjectToResource();
        createDialog();
        setListeners();

        return mAlertDialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d
                    .getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new PositiveClickListener());
        }
    }

    /**
     * Sets action type
     * 
     * @param actionType
     *            must be ACTION_ADD or ACTION_EDIT
     */
    public void setActionType(int actionType) {
        mActionType = actionType;
    }

    /**
     * Sets callback object for handling add or edit camera
     * 
     * @param callback
     *            callback object
     */
    public void setCallback(OnCameraEditorListener callback) {
        mCallback = callback;
    }

    /**
     * Sets camera name. Function called only for Add new camera
     * 
     * @param cameraName
     *            name of camera
     */
    public void setCameraName(String cameraName) {
        mCameraName = cameraName;
    }

    /**
     * Sets data for edit
     * 
     * @param record
     *            contains data for editing
     */
    public void setDataForEdit(UserCamerasRecord record) {
        mUserCamerasRecord = record;
    }

    /**
     * Sets listeners for objects
     */
    private void setListeners() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok, null);
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel,
                new NegativeClickListener());
        mAutoCalcCoc.setOnCheckedChangeListener(new CheckedChangeListener());

    }
}
