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
import ru.neverdark.phototools.db.UserCamerasAdapter;
import ru.neverdark.phototools.db.UserCamerasAdapter.OnEditAndRemoveListener;
import ru.neverdark.phototools.db.UserCamerasRecord;
import ru.neverdark.phototools.fragments.CameraEditorDialog.OnCameraEditorListener;
import ru.neverdark.phototools.fragments.DeleteConfirmationDialog.OnDeleteConfirmationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class CameraManagementDialog extends SherlockDialogFragment {
    private class AddCameraClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String cameraName = mModel.getText().toString().trim();
            if (cameraName.length() > 0) {
                if (mAdapter.isCameraExist(cameraName)) {
                    ShowMessageDialog dialog = ShowMessageDialog
                            .getInstance(mContext);
                    dialog.setMessages(R.string.error,
                            R.string.error_cameraAlreadyExist);
                    dialog.show(getFragmentManager(),
                            ShowMessageDialog.DIALOG_TAG);
                } else {
                    CameraEditorDialog dialog = CameraEditorDialog
                            .getInstance(mContext);
                    dialog.setCallback(new CameraEditorListener());
                    dialog.setActionType(CameraEditorDialog.ACTION_ADD);
                    dialog.setCameraName(cameraName);
                    dialog.show(getFragmentManager(),
                            CameraEditorDialog.DIALOG_TAG);
                }
            }
        }
    }

    private class CameraEditorListener implements OnCameraEditorListener {
        @Override
        public void onAddCamera(UserCamerasRecord record) {
            mAdapter.createCamera(record);
            mModel.setText("");
            mIsOperationSuccess = true;
        }

        @Override
        public void onEditCamera(UserCamerasRecord record) {
            mAdapter.updateCamera(record);
            mIsOperationSuccess = true;
        }
    }

    public interface OnCameraManagementListener {
        public void cameraManagementOnBack();
    }

    private class DeleteConfirmationListener implements
            OnDeleteConfirmationListener {
        @Override
        public void onDeleteConfirmationHandler(Object deleteRecord) {
            UserCamerasRecord record = (UserCamerasRecord) deleteRecord;
            mAdapter.deleteCamera(record);
            mIsOperationSuccess = true;
        }
    }

    private class DialogKeyListener implements Dialog.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mIsOperationSuccess && mCallback != null) {
                    mCallback.cameraManagementOnBack();
                }
            }
            return false;
        }
    }
    private class EditAndRemoveListener implements OnEditAndRemoveListener {
        @Override
        public void onEditHandler(UserCamerasRecord record) {
            CameraEditorDialog dialog = CameraEditorDialog
                    .getInstance(mContext);
            dialog.setCallback(new CameraEditorListener());
            dialog.setActionType(CameraEditorDialog.ACTION_EDIT);
            dialog.setDataForEdit(record);
            dialog.show(getFragmentManager(), CameraEditorDialog.DIALOG_TAG);
        }

        @Override
        public void onRemoveHandler(UserCamerasRecord record) {
            DeleteConfirmationDialog dialog = DeleteConfirmationDialog
                    .getInstance(mContext);
            dialog.setCallback(new DeleteConfirmationListener());
            dialog.setObjectForDelete(record);
            dialog.show(getFragmentManager(),
                    DeleteConfirmationDialog.DIALOG_TAG);
        }
    }
    public static final String DIALOG_TAG = "cameraManagementDialog";
    public static CameraManagementDialog getInstance(Context context) {
        CameraManagementDialog dialog = new CameraManagementDialog();
        dialog.mContext = context;
        return dialog;
    }
    private UserCamerasAdapter mAdapter;
    private ImageView mAddButton;
    private AlertDialog.Builder mAlertDialog;
    private OnCameraManagementListener mCallback;
    private ListView mCameraList;
    private Context mContext;

    private boolean mIsOperationSuccess = false;

    private EditText mModel;

    private View mView;

    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.user_camera_dialog, null);
        mAddButton = (ImageView) mView.findViewById(R.id.userCamera_addButton);
        mCameraList = (ListView) mView.findViewById(R.id.userCamera_list);
        mModel = (EditText) mView.findViewById(R.id.userCamera_model);
    }

    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.cameraManagementTitle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjectToResource();
        createDialog();
        setListeners();

        return mAlertDialog.create();
    }

    @Override
    public void onPause() {
        Log.enter();

        super.onPause();
        if (mAdapter != null) {
            mAdapter.closeDb();
        }
    }

    @Override
    public void onResume() {
        Log.enter();

        super.onResume();
        if (mAdapter == null) {
            mAdapter = new UserCamerasAdapter(mContext);
            mAdapter.setCallback(new EditAndRemoveListener());
        }

        if (mAdapter.isDbOpen() == false) {
            mAdapter.openDb();
        }

        mAdapter.loadData();

        mCameraList.setAdapter(mAdapter);
    }

    public void setCallback(OnCameraManagementListener callback) {
        mCallback = callback;
    }

    private void setListeners() {
        mAddButton.setOnClickListener(new AddCameraClickListener());
        mAddButton.setOnTouchListener(new ImageOnTouchListener());
        mAlertDialog.setOnKeyListener(new DialogKeyListener());
    }
}
