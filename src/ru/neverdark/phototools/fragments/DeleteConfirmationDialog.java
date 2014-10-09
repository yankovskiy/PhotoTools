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
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Implements delete confirmation dialog
 */
public class DeleteConfirmationDialog extends UfoDialogFragment {

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
            OnDeleteConfirmationListener callback = (OnDeleteConfirmationListener) getCallback();
            if (callback != null) {
                callback.onDeleteConfirmationHandler(mDeleteObject);
            }
        }
    }

    /**
     * Creates new dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    static public DeleteConfirmationDialog getInstance(Context context) {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
        dialog.setContext(context);
        return dialog;
    }

    private Object mDeleteObject;
    public static final String DIALOG_TAG = "deleteConfirmationDialog";

    /**
     * Creates alert dialog
     */
    @Override
    protected void createDialog() {
        super.createDialog();
        String message = String.format(Locale.US,
                getString(R.string.deleteConfirmation_dialog_message), mDeleteObject.toString());

        getAlertDialog().setTitle(R.string.deleteConfirmation_dialog_title);
        getAlertDialog().setMessage(message);
    }

    /**
     * Sets object for delete
     * 
     * @param deleteObject
     */
    public void setObjectForDelete(Object deleteObject) {
        mDeleteObject = deleteObject;
    }

    @Override
    public void bindObjects() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

}
