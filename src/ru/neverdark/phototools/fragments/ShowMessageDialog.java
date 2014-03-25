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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Class for showing dialog with specified message and specified title
 */
public class ShowMessageDialog extends SherlockDialogFragment {
    private class PositiveClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public static final String DIALOG_TAG = "showMessageDialog";

    /**
     * Creates new dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static ShowMessageDialog getInstance(Context context) {
        ShowMessageDialog dialog = new ShowMessageDialog();
        dialog.mContext = context;
        return dialog;
    }

    private int mTitleResourceId;
    private int mMessageResourceId;
    private Context mContext;
    private AlertDialog.Builder mAlertDialog;

    /**
     * Creates alert dialog
     */
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle(mTitleResourceId);
        mAlertDialog.setMessage(mMessageResourceId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        createDialog();
        setClickListener();

        return mAlertDialog.create();
    }

    /**
     * Sets click listener for dialog buttons
     */
    private void setClickListener() {
        mAlertDialog.setPositiveButton(R.string.dialog_button_ok,
                new PositiveClickListener());
    }

    /**
     * Sets title and message for dialog
     * 
     * @param titleResourceId
     *            string title resource id
     * @param messageResourceId
     *            string message resource id
     */
    public void setMessages(int titleResourceId, int messageResourceId) {
        mTitleResourceId = titleResourceId;
        mMessageResourceId = messageResourceId;
    }
}
