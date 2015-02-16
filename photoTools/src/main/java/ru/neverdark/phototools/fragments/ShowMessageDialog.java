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
import android.content.Context;

/**
 * Class for showing dialog with specified message and specified title
 */
public class ShowMessageDialog extends UfoDialogFragment {

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
        dialog.setContext(context);
        return dialog;
    }

    private int mTitleResourceId;
    private int mMessageResourceId;
    private String mMessage;

    @Override
    public void bindObjects() {
        // TODO Auto-generated method stub

    }

    /**
     * Creates alert dialog
     */
    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(mTitleResourceId);
        if (mMessage == null) {
            getAlertDialog().setMessage(mMessageResourceId);
        } else {
            getAlertDialog().setMessage(mMessage);
        }
    }

    @Override
    public void setListeners() {
        getAlertDialog().setNegativeButton(R.string.dialog_button_ok, new CancelClickListener());
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

    public void setMessages(int titleResourceId, String message) {
        mTitleResourceId = titleResourceId;
        mMessage = message;
    }
}
