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

import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.UserCamerasAdapter;
import ru.neverdark.phototools.db.UserCamerasAdapter.OnEditAndRemoveListener;
import ru.neverdark.phototools.db.UserCamerasRecord;
import ru.neverdark.phototools.fragments.CameraEditorDialog.OnCameraEditorListener;
import ru.neverdark.phototools.fragments.DeleteConfirmationDialog.OnDeleteConfirmationListener;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.Log;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


/**
 * Implements dialog for camera management
 */
public class CameraManagementDialog extends UfoDialogFragment {
    /**
     * Click listener for "Add" button
     */
    private class AddCameraClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String cameraName = mModel.getText().toString().trim();
            if (cameraName.length() > 0) {
                if (mAdapter.isCameraExist(cameraName)) {
                    ShowMessageDialog dialog = ShowMessageDialog
                            .getInstance(getContext());
                    dialog.setMessages(R.string.error,
                            R.string.error_cameraAlreadyExist);
                    dialog.show(getFragmentManager(),
                            ShowMessageDialog.DIALOG_TAG);
                } else {
                    CameraEditorDialog dialog = CameraEditorDialog
                            .getInstance(getContext());
                    dialog.setCallback(new CameraEditorListener());
                    dialog.setActionType(CameraEditorDialog.ACTION_ADD);
                    dialog.setCameraName(cameraName);
                    dialog.show(getFragmentManager(),
                            CameraEditorDialog.DIALOG_TAG);
                }
            }
        }
    }

    /**
     * Listener for handling data from CameraEditorDialog
     */
    private class CameraEditorListener implements OnCameraEditorListener, OnCallback {
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

    /**
     * Called when user confirm delete record in the DeleteConfirmationDialog
     */
    private class DeleteConfirmationListener implements
            OnDeleteConfirmationListener {
        @Override
        public void onDeleteConfirmationHandler(Object deleteRecord) {
            UserCamerasRecord record = (UserCamerasRecord) deleteRecord;
            mAdapter.deleteCamera(record);
            mIsOperationSuccess = true;
        }
    }

    /**
     * Listener for handling "Back" button on the dialog
     */
    private class DialogKeyListener implements Dialog.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                OnCameraManagementListener callback = (OnCameraManagementListener) getCallback();
                if (mIsOperationSuccess && callback != null) {
                    callback.cameraManagementOnBack();
                }
            }
            return false;
        }
    }

    /**
     * Listener called for "Edit" or "Remove" buttons in the list
     */
    private class EditAndRemoveListener implements OnEditAndRemoveListener {
        @Override
        public void onEditHandler(UserCamerasRecord record) {
            CameraEditorDialog dialog = CameraEditorDialog
                    .getInstance(getContext());
            dialog.setCallback(new CameraEditorListener());
            dialog.setActionType(CameraEditorDialog.ACTION_EDIT);
            dialog.setDataForEdit(record);
            dialog.show(getFragmentManager(), CameraEditorDialog.DIALOG_TAG);
        }

        @Override
        public void onRemoveHandler(UserCamerasRecord record) {
            DeleteConfirmationDialog dialog = DeleteConfirmationDialog
                    .getInstance(getContext());
            dialog.setCallback(new DeleteConfirmationListener());
            dialog.setObjectForDelete(record);
            dialog.show(getFragmentManager(),
                    DeleteConfirmationDialog.DIALOG_TAG);
        }
    }

    /**
     * Interface provides callback method calls for "Back" button in the dialog
     */
    public interface OnCameraManagementListener {
        /**
         * Calls when user "Back" button
         */
        public void cameraManagementOnBack();
    }

    /**
     * Dialog tag for fragment manager
     */
    public static final String DIALOG_TAG = "cameraManagementDialog";

    /**
     * Creates dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static CameraManagementDialog getInstance(Context context) {
        CameraManagementDialog dialog = new CameraManagementDialog();
        dialog.setContext(context);
        return dialog;
    }

    private UserCamerasAdapter mAdapter;
    private ImageView mAddButton;
    private ListView mCameraList;

    private boolean mIsOperationSuccess = false;

    private EditText mModel;

    /**
     * Creates alert dialog
     */
    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.cameraManagementTitle);
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
            mAdapter = new UserCamerasAdapter(getContext());
            mAdapter.setCallback(new EditAndRemoveListener());
        }

        if (mAdapter.isDbOpen() == false) {
            mAdapter.openDb();
        }

        mAdapter.loadData();

        mCameraList.setAdapter(mAdapter);
    }


    @Override
    public void bindObjects() {
        setDialogView( View.inflate(getContext(), R.layout.user_camera_dialog, null));
        mAddButton = (ImageView) getDialogView().findViewById(R.id.userCamera_addButton);
        mCameraList = (ListView) getDialogView().findViewById(R.id.userCamera_list);
        mModel = (EditText) getDialogView().findViewById(R.id.userCamera_model);
    }

    @Override
    public void setListeners() {
        mAddButton.setOnClickListener(new AddCameraClickListener());
        mAddButton.setOnTouchListener(new ImageOnTouchListener());
        getAlertDialog().setOnKeyListener(new DialogKeyListener());
    }
}
