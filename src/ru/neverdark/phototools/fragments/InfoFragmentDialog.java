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
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Implements information dialog
 * 
 */
public class InfoFragmentDialog extends SherlockDialogFragment {
    public static final String DIALOG_TAG = "infoFragmentDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");
        String title = "";
        String message = "";
        int messageId = getArguments().getInt(Constants.INFORMATION_MESSAGE_ID);

        switch (messageId) {
        case Constants.INFORMATION_SUNRISE:
            title = getString(R.string.information_title_sunrise);
            message = getString(R.string.information_message_sunrise);
            break;
        case Constants.INFORMATION_SUNSET:
            title = getString(R.string.information_title_sunset);
            message = getString(R.string.information_message_sunset);
            break;
        case Constants.INFORMATION_ASTRO_SUNRISE:
            title = getString(R.string.information_title_astroSunrise);
            message = getString(R.string.information_message_astroSunrise);
            break;
        case Constants.INFORMATION_ASTRO_SUNSET:
            title = getString(R.string.information_title_astroSunset);
            message = getString(R.string.information_message_astroSunset);
            break;
        case Constants.INFORMATION_NAUTICAL_SUNRISE:
            title = getString(R.string.information_title_nauticalSunrise);
            message = getString(R.string.information_message_nauticalSunrise);
            break;
        case Constants.INFORMATION_NAUTICAL_SUNSET:
            title = getString(R.string.information_title_nauticalSunset);
            message = getString(R.string.information_message_nauticalSunset);
            break;
        case Constants.INFORMATION_CIVIL_SUNRISE:
            title = getString(R.string.information_title_civilSunrise);
            message = getString(R.string.information_message_civilSunrise);
            break;
        case Constants.INFORMATION_CIVIL_SUNSET:
            title = getString(R.string.information_title_civilSunset);
            message = getString(R.string.information_message_civilSunset);
            break;
        }

        Log.variable("title", title);
        Log.variable("message", message);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.dialog_button_close,
                new CancelClickListener());

        // Creating Alert Message
        return alertDialog.create();
    }
}
