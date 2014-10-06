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

import ru.neverdark.phototools.R;
import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;

/**
 * Implements alert dialog
 */
public class AlertSettingsDialog extends UfoDialogFragment {
    private class OnPositiveClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public static final String DIALOG_ID = "alertSettingsDialog";

    @Override
    public void bindObjects() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListeners() {
        mAlertDialog
                .setPositiveButton(R.string.sunset_alert_positive, new OnPositiveClickListener());
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        mAlertDialog.setTitle(R.string.sunset_alert_title);
        mAlertDialog.setMessage(R.string.sunset_alert_message);
    }

    public static AlertSettingsDialog getInstance(Context context) {
        AlertSettingsDialog dialog = new AlertSettingsDialog();
        dialog.mContext = context;
        return dialog;
    }
}
