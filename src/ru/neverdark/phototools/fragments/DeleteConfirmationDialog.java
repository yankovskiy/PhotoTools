/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import java.util.Locale;

import ru.neverdark.phototools.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Implements delete confirmation dialog
 */
public class DeleteConfirmationDialog extends SherlockDialogFragment {

    /**
     * Click listener for "Cancel" button
     */
    private class NegativeClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    /**
     * The interface for processing the delete record action
     */
    public interface OnDeleteConfirmationListener {
        /**
         * Handler for processing delete record action
         * 
         * @param deleteRecord
         *            the record for deletion
         */
        public void onDeleteConfirmationHandler(Object deleteRecord);
    }

    /**
     * Click listener for "Ok" button
     */
    private class PositiveClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (mCallback != null) {
                mCallback.onDeleteConfirmationHandler(mDeleteObject);
            }
        }
    }
    
    /**
     * Creates new dialog
     * @param context application context
     * @return dialog
     */
    static public DeleteConfirmationDialog getInstance(Context context) {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
        dialog.mContext = context;
        return dialog;
    }
    
    private Context mContext;
    private Object mDeleteObject;
    private AlertDialog.Builder mAlertDialog;
    public static final String DIALOG_TAG = "deleteConfirmationDialog";
    private OnDeleteConfirmationListener mCallback;

    /**
     * Creates alert dialog
     */
    private void createDialog() {
        String message = String.format(Locale.US,
                getString(R.string.deleteConfirmation_dialog_message),
                mDeleteObject.toString());

        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle(R.string.deleteConfirmation_dialog_title);
        mAlertDialog.setMessage(message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        createDialog();
        setClickListener();

        return mAlertDialog.create();
    }

    /**
     * Sets callback for handling delete record
     * @param callback
     */
    public void setCallback(OnDeleteConfirmationListener callback) {
        mCallback = callback;
    }

    /**
     * Sets click listener for dialog buttons
     */
    private void setClickListener() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok,
                new PositiveClickListener());
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel,
                new NegativeClickListener());
    }

    /**
     * Sets object for delete
     * @param deleteObject
     */
    public void setObjectForDelete(Object deleteObject) {
        mDeleteObject = deleteObject;
    }

}
