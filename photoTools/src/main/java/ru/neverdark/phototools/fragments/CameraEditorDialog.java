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

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.UserCamerasRecord;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

/**
 * Implements dialog for additing/editig cameras
 */
public class CameraEditorDialog extends UfoDialogFragment {
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
    private int mActionType;
    private CheckBox mAutoCalcCoc;
    private EditText mCameraModel;
    private EditText mCoc;
    private EditText mResolutionHeight;
    private EditText mResolutionWidth;
    private EditText mSensorHeight;
    private EditText mSensorWidth;
    private UserCamerasRecord mUserCamerasRecord;

    /**
     * Creates new dialog
     *
     * @param context
     *            application context
     * @return dialog
     */
    public static CameraEditorDialog getInstance(Context context) {
        CameraEditorDialog dialog = new CameraEditorDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    protected void createDialog() {
        super.createDialog();

        if (mActionType == ACTION_ADD) {
            getAlertDialog().setTitle(R.string.userCamera_addCamera);
            disableCustomCoc(true);
            mAutoCalcCoc.setChecked(true);
            mUserCamerasRecord = new UserCamerasRecord();
        } else if (mActionType == ACTION_EDIT) {
            getAlertDialog().setTitle(R.string.userCamera_editCamera);

            boolean disableCoc = !mUserCamerasRecord.isCustomCoc();
            String cameraName = mUserCamerasRecord.getCameraName();
            String resolutionWidth = String.valueOf(mUserCamerasRecord.getResolutionWidth());
            String resolutionHeight = String.valueOf(mUserCamerasRecord.getResolutionHeight());
            String sensorWidth = String.valueOf(mUserCamerasRecord.getSensorWidth());
            String sensorHeight = String.valueOf(mUserCamerasRecord.getSensorHeight());
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
        int resolutionWidth = Integer.valueOf(mResolutionWidth.getText().toString().trim());
        int resolutionHeight = Integer.valueOf(mResolutionHeight.getText().toString().trim());
        float sensorWidth = Float.valueOf(mSensorWidth.getText().toString().trim());
        float sensorHeight = Float.valueOf(mSensorHeight.getText().toString().trim());

        if (!mAutoCalcCoc.isChecked()) {
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
    public void onStart() {
        super.onStart();

        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
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
     * Sets data for edit
     *
     * @param record
     *            contains data for editing
     */
    public void setDataForEdit(UserCamerasRecord record) {
        mUserCamerasRecord = record;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.camera_editor_dialog, null));
        mResolutionWidth = (EditText) getDialogView().findViewById(
                R.id.cameraEditor_resolutionWidth);
        mResolutionHeight = (EditText) getDialogView().findViewById(
                R.id.cameraEditor_resolutionHeight);
        mSensorWidth = (EditText) getDialogView().findViewById(R.id.cameraEditor_sensorWidth);
        mSensorHeight = (EditText) getDialogView().findViewById(R.id.cameraEditor_sensorHeight);
        mCameraModel = (EditText) getDialogView().findViewById(R.id.cameraEditor_cameraName);
        mCoc = (EditText) getDialogView().findViewById(R.id.cameraEditor_coc);
        mAutoCalcCoc = (CheckBox) getDialogView().findViewById(R.id.cameraEditor_autoCalc);
    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, null);
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
        mAutoCalcCoc.setOnCheckedChangeListener(new CheckedChangeListener());
    }

    /**
     * Interface provides callback methods call for add or edit camera
     */
    public interface OnCameraEditorListener {
        /**
         * Called for add camera
         *
         * @param record record contains data for add camera
         */
        void onAddCamera(UserCamerasRecord record);

        /**
         * Called for edit camera
         *
         * @param record record contains data for edit camera
         */
        void onEditCamera(UserCamerasRecord record);
    }

    /**
     * Custom exception for showing dialog with error message
     */
    private class CameraEditorDialogException extends Exception {
        private static final long serialVersionUID = -3433785378864484422L;
        private ShowMessageDialog mDialog;

        /**
         * Constructor
         *
         * @param resourceId string Id contains error message
         */
        public CameraEditorDialogException(int resourceId) {
            mDialog = ShowMessageDialog.getInstance(getContext());
            mDialog.setMessages(R.string.error, resourceId);
        }

        public void showDialog() {
            mDialog.show(getFragmentManager(), ShowMessageDialog.DIALOG_TAG);
        }
    }

    /**
     * Listener for handling changed state "Automatic calculation"
     */
    private class CheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            disableCustomCoc(isChecked);
        }
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
                isFilled = mResolutionHeight.getText().toString().trim().length() > 0;
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
                    throw new CameraEditorDialogException(R.string.error_cameraNameEmpty);
                }

                if (!isDataFilled()) {
                    throw new CameraEditorDialogException(R.string.error_notAllValuesFilled);
                }

                if (!mAutoCalcCoc.isChecked() && mCoc.getText().toString().trim().length() == 0) {
                    throw new CameraEditorDialogException(R.string.error_cocNotFilled);
                }

                boolean exist = false;

                if ((mActionType == ACTION_ADD)
                        || (mActionType == ACTION_EDIT && !cameraName.equals(mUserCamerasRecord
                        .getCameraName()))) {
                    DbAdapter dbAdapter = new DbAdapter(getContext()).open();
                    exist = dbAdapter.getUserCameras().isCameraExist(cameraName);
                    dbAdapter.close();
                }

                if (exist) {
                    throw new CameraEditorDialogException(R.string.error_cameraAlreadyExist);
                }

                dismiss();

                OnCameraEditorListener callback = (OnCameraEditorListener) getCallback();
                if (callback != null) {
                    fillUserCamerasRecord(mUserCamerasRecord);

                    if (mActionType == ACTION_ADD) {
                        callback.onAddCamera(mUserCamerasRecord);
                    } else if (mActionType == ACTION_EDIT) {
                        callback.onEditCamera(mUserCamerasRecord);
                    }
                }
            } catch (CameraEditorDialogException e) {
                e.showDialog();
            }
        }
    }
}
