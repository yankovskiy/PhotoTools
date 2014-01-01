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

import ru.neverdark.phototools.MapActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.LocationsDbAdapter;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Implements confirm creation dialog
 */
public class ConfirmCreateFragment extends SherlockDialogFragment {
    /**
     * Creates and return new ConfirmCreateFragment object
     * 
     * @param action
     *            LOCATION_ACTION_ADD or LOCATION_ACTION_EDIT
     * @param locationName
     *            the name of edited location
     * @return ConfirmCreateFragment object
     */
    public static ConfirmCreateFragment NewInstance(int action,
            String locationName) {
        Log.message("Enter");
        ConfirmCreateFragment dialog = new ConfirmCreateFragment();
        dialog.mAction = action;
        dialog.mLocationName = locationName;
        dialog.mIsEdit = (action == Constants.LOCATION_ACTION_EDIT);
        
        return dialog;
    }
    
    private CheckBox mCheckBox_isSave;
    private View mView;
    private EditText mEditText_locationName;
    private AlertDialog.Builder mAlertDialog;
    private boolean mIsVisible;
    private String mLocationName;

    private int mAction;
    private boolean mIsEdit;

    /**
     * Binds classes objects to resources
     */
    private void bindObjectToResource() {
        Log.message("Enter");
        mView = View.inflate(getSherlockActivity(),
                R.layout.dialog_confirm_create, null);

        mCheckBox_isSave = (CheckBox) mView
                .findViewById(R.id.dialogConfirmCreate_checkbox_isSave);
        mEditText_locationName = (EditText) mView
                .findViewById(R.id.dialogConfirmCreate_editText);
    }

    /**
     * Inits edit mode. Load location name and set checkbox to save state
     */
    private void initEditMode() {
        Log.message("Enter");
        setLocationNameVisible(true);
        mCheckBox_isSave.setChecked(true);
        mEditText_locationName.setText(mLocationName);
    }

    /**
     * Returns true if "Is Save" checkbox checked
     * 
     * @return true if "Is Save" checkbox checked, false in other case
     */
    private boolean isSaveChecked() {
        return mCheckBox_isSave.isChecked();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");

        bindObjectToResource();

        if (mAction == Constants.LOCATION_ACTION_EDIT) {
            initEditMode();
        }

        mAlertDialog = new AlertDialog.Builder(getActivity());
        mAlertDialog.setView(mView);

        mAlertDialog.setTitle(R.string.dialogConfirmCreate_title);

        mAlertDialog.setMessage(R.string.dialogConfirmCreate_message);
        setOnClickListeners();

        if (savedInstanceState != null) {
            mIsVisible = savedInstanceState
                    .getBoolean(Constants.CONFIRM_CREATION_ISVISIBLE);
            setLocationNameVisible(mIsVisible);
            mIsEdit = savedInstanceState.getBoolean(Constants.CONFIRM_CREATION_ISEDIT);
        } 
        // Showing Alert Message
        return mAlertDialog.create();
    }

    @Override
    public void onSaveInstanceState(Bundle putInstanseState) {
        super.onSaveInstanceState(putInstanseState);
        mIsVisible = isSaveChecked();
        putInstanseState.putBoolean(Constants.CONFIRM_CREATION_ISVISIBLE,
                mIsVisible);
        putInstanseState.putBoolean(Constants.CONFIRM_CREATION_ISEDIT, mIsEdit);
    }

    /**
     * Sets visible or invisible state for Location Name field
     * 
     * @param isVisible
     *            - true for Location Name visible
     */
    private void setLocationNameVisible(boolean isVisible) {
        if (isVisible) {
            mEditText_locationName.setVisibility(View.VISIBLE);
        } else {
            mEditText_locationName.setVisibility(View.GONE);
        }
    }

    /**
     * Sets onClickListeners for UI objects
     */
    private void setOnClickListeners() {
        // On pressing Yes button
        mAlertDialog.setPositiveButton(R.string.dialogConfirmCreate_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String locationName = null;
                        boolean isError = false;

                        if (isSaveChecked()) {
                            locationName = mEditText_locationName.getText()
                                    .toString();

                            if (locationName.length() == 0) {
                                showErrorMessage(R.string.dialogConfirmCreate_error_emptyLocationName);
                                isError = true;
                                /* if not edit mode - check for location exist */
                            } else if (mIsEdit == false){

                                LocationsDbAdapter dbAdapter = new LocationsDbAdapter(
                                        mView.getContext());
                                dbAdapter.open();
                                boolean isExists = dbAdapter
                                        .isLocationExists(locationName);

                                dbAdapter.close();

                                if (isExists == true) {
                                    showErrorMessage(R.string.dialogConfirmCreate_error_alreayExist);
                                    isError = true;
                                }

                            }
                        } else {
                            locationName = null;
                        }

                        if (isError == false) {
                            ((MapActivity) getActivity())
                                    .handleConfirmDialog(locationName);
                        }

                        dialog.dismiss();

                    }
                });

        // on pressing No button
        mAlertDialog.setNegativeButton(R.string.dialogConfirmCreate_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        mCheckBox_isSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationNameVisible(isSaveChecked());
            }
        });
    }

    /**
     * Shows error message by resourceId
     * 
     * @param resourceId
     *            string resource id contains error message
     */
    private void showErrorMessage(int resourceId) {
        Toast.makeText(mView.getContext(), resourceId, Toast.LENGTH_LONG)
                .show();
    }
}
