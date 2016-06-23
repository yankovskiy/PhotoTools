/*******************************************************************************
 * Copyright (C) 2014-2015 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;

public class ConfirmDialog extends UfoDialogFragment {
    public static final String DIALOG_ID = "confirmDialog";
    private int mTitleId;
    private int mMessageId;
    private String mMessage;

    public static ConfirmDialog getInstance(Context context) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    public void bindObjects() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        getAlertDialog().setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

    public void setMessages(int titleId, int messageId) {
        mTitleId = titleId;
        mMessageId = messageId;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        if (mTitleId != 0) {
            getAlertDialog().setTitle(mTitleId);
        }
        if (mMessageId != 0) {
            getAlertDialog().setMessage(mMessageId);
        } else if (mMessage != null) {
            getAlertDialog().setMessage(mMessage);
        }
    }

    public void setMessage(int messageId) {
        mMessageId = messageId;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setTitle(int titleId) {
        mTitleId = titleId;
    }

    public interface OnPositiveClickListener {
        public void onPositiveClickHandler();
    }

    private class PositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            OnPositiveClickListener callback = (OnPositiveClickListener) getCallback();
            if (callback != null) {
                callback.onPositiveClickHandler();
            }
        }
    }
}
