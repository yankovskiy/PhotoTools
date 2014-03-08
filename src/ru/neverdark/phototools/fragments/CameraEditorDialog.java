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
import ru.neverdark.phototools.db.UserCamerasRecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class CameraEditorDialog extends SherlockDialogFragment {
    private class NegativeClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public interface OnCameraEditorListener {
        public void onAddCamera(UserCamerasRecord record);
        public void onEditCamera(UserCamerasRecord record);
    }

    private class PositiveClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            // TODO проверка на существование записи
            // отображать диалог с ошибкой, если запись существует, дальнейшее выполнение кода при этом прекращать
            
            if (mCallback != null) {
                fillUserCamerasRecord(mUserCamerasRecord);

                if (mActionType == ACTION_ADD) {
                    mCallback.onAddCamera(mUserCamerasRecord);
                } else if (mActionType == ACTION_EDIT) {
                    mCallback.onEditCamera(mUserCamerasRecord);
                }
            }
        }
    }
    public static final String DIALOG_TAG = "cameraEditorDialog";
    public static final int ACTION_ADD = 0;
    public static final int ACTION_EDIT = 1;

    public static CameraEditorDialog getInstance(Context context) {
        CameraEditorDialog dialog = new CameraEditorDialog();
        dialog.mContext = context;
        return dialog;
    }
    private OnCameraEditorListener mCallback;
    private Context mContext;
    private View mView;
    private AlertDialog.Builder mAlertDialog;
    private String mCameraName;
    private UserCamerasRecord mUserCamerasRecord;
    private EditText mResolutionWidth;
    private EditText mResolutionHeight;
    private EditText mSensorWidth;
    private EditText mSensorHeight;
    private CheckBox mAudoCalcCoc;
    private EditText mCoc;

    private int mActionType;

    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.camera_editor_dialog, null);
        mResolutionWidth = (EditText) mView.findViewById(R.id.cameraEditor_resolutionWidth);
        mResolutionHeight = (EditText) mView.findViewById(R.id.cameraEditor_resolutionHeight);
        mSensorWidth = (EditText) mView.findViewById(R.id.cameraEditor_sensorWidth);
        mSensorHeight = (EditText) mView.findViewById(R.id.cameraEditor_sensorHeight);
        mCoc = (EditText) mView.findViewById(R.id.cameraEditor_coc);
        mAudoCalcCoc = (CheckBox) mView.findViewById(R.id.cameraEditor_autoCalc);
    }
    
    private void disableCustomCoc(boolean isDisable) {
        // TODO вызывать при создании диалога и при измененеии состояния чекбокса
        // для новой записи должен быть отключен
        mCoc.setEnabled(!isDisable);
    }

    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);

        if (mActionType == ACTION_ADD) {
            mAlertDialog.setTitle(R.string.userCamera_addCamera);
        } else if (mActionType == ACTION_EDIT) {
            mAlertDialog.setTitle(R.string.userCamera_editCamera);
        }
    }

    private void fillUserCamerasRecord(UserCamerasRecord record) {
        if (mActionType == ACTION_ADD) {
            record = new UserCamerasRecord();
        }
        // TODO заполнение record введенными данными
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        createDialog();
        bindObjectToResource();
        setOnClickListener();

        return mAlertDialog.create();
    }

    public void setActionType(int actionType) {
        mActionType = actionType;
    }

    public void setCallback(OnCameraEditorListener callback) {
        mCallback = callback;
    }

    public void setCameraName(String cameraName) {
        mCameraName = cameraName;
    }

    public void setDataForEdit(UserCamerasRecord record) {
        mUserCamerasRecord = record;
    }

    private void setOnClickListener() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok,
                new PositiveClickListener());
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel,
                new NegativeClickListener());
    }
}
