/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Implements delete confirmation dialog
 */
public class ConfirmDeleteFragment extends SherlockDialogFragment {
    private String mLocationName;
    
    public ConfirmDeleteFragment(String locationName) {
        super();
        this.mLocationName = locationName;
    }
    
    public ConfirmDeleteFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");
        
        if (savedInstanceState != null) {
            mLocationName = savedInstanceState.getString(Constants.DELETE_LOCATION_NAME);
        }
        
        String message = String.format(Locale.US, getString(R.string.sunset_deleteConfirm_message), mLocationName);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(R.string.sunset_deleteConfirm_title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.sunset_deleteConfirm_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.sunset_deleteConfirm_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        return alertDialog.create();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DELETE_LOCATION_NAME, mLocationName);
    }
}
