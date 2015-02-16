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
package ru.neverdark.abs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockDialogFragment;

public abstract class UfoDialogFragment extends SherlockDialogFragment implements CommonApi {
    private AlertDialog.Builder mAlertDialog;
    private View mView;
    private OnCallback mCallback;
    private Context mContext;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjects();
        createDialog();
        setListeners();

        return mAlertDialog.create();
    }
    
    protected void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        if (mView != null) {
            mAlertDialog.setView(mView);
        }
    }
    
    public void setCallback(OnCallback callback) {
        mCallback = callback;
    }
    
    protected OnCallback getCallback() {
        return mCallback;
    }
    
    protected View getDialogView() {
        return mView;
    }
    
    protected Context getContext() {
        return mContext;
    }
    
    protected AlertDialog.Builder getAlertDialog() {
        return mAlertDialog;
    }
    
    protected void setContext(Context context) {
        mContext = context;
    }
    
    protected void setDialogView(View view) {
        mView = view;
    }
}
