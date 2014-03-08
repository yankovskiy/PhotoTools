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

import ru.neverdark.phototools.db.UserCamerasRecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class CameraEditorDialog extends SherlockDialogFragment{
    public static final String DIALOG_TAG = "cameraEditorDialog";
    public static final int ACTION_ADD = 0;
    public static final int ACTION_EDIT = 1;
    
    public interface OnCameraEditorListener {
        public void onEditCamera(UserCamerasRecord record);
        public void onAddCamera(UserCamerasRecord record);
    }
    
    private OnCameraEditorListener mCallback;
    private Context mContext;
    private View mView;
    private AlertDialog.Builder mAlertDialog;
    
    public void setCallback(OnCameraEditorListener callback) {
        mCallback = callback;
    }
    
    public static CameraEditorDialog getInstance(Context context) {
        CameraEditorDialog dialog = new CameraEditorDialog();
        dialog.mContext = context;
        return dialog;
    }
    
    private void bindObjectToResource() {
        // TODO
    }
    
    private void createDialog() {
        // TODO
    }
    
    private void setOnClickListener() {
        // TODO
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        createDialog();
        bindObjectToResource();
        setOnClickListener();
        
        return mAlertDialog.create();
    }

    public void setActionType(int actionType) {
        // TODO Auto-generated method stub
        
    }

    public void setDataForEdit(UserCamerasRecord record) {
        // TODO Auto-generated method stub
        
    }

    public void setCameraName(String cameraName) {
        // TODO Auto-generated method stub
        
    }
}
