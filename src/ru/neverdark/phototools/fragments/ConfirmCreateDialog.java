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

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Implements confirm creation dialog
 */
public class ConfirmCreateDialog extends UfoDialogFragment {

    private class ConfirmCreateError extends Exception {
        private static final long serialVersionUID = 748326812444861397L;
        private int mMessageId;

        public ConfirmCreateError(int messageId) {
            mMessageId = messageId;
        }

        public void showErrorMessage() {
            Toast.makeText(getContext(), mMessageId, Toast.LENGTH_LONG).show();
        }
    }

    private class IsSaveClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            setLocationNameVisible(isSaveChecked());
        }
    }

    public interface OnConfirmDialogHandler {
        public void handleConfirmDialog(String locationName);
    }

    private class PositiveClickListener implements android.content.DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String locationName = null;

            try {
                if (isSaveChecked()) {

                    locationName = mEditText_locationName.getText().toString();

                    if (locationName.length() == 0) {
                        throw new ConfirmCreateError(
                                R.string.dialogConfirmCreate_error_emptyLocationName);
                        /* if not edit mode - check for location exist */
                    } else if (mIsEdit == false) {

                        DbAdapter dbAdapter = new DbAdapter(getContext());
                        dbAdapter.open();
                        boolean isExists = dbAdapter.getLocations().isLocationExists(locationName);

                        dbAdapter.close();

                        if (isExists == true) {
                            throw new ConfirmCreateError(
                                    R.string.dialogConfirmCreate_error_alreayExist);
                        }
                    }
                } else {
                    locationName = null;
                }

                OnConfirmDialogHandler callback = (OnConfirmDialogHandler) getCallback();
                if (callback != null) {
                    callback.handleConfirmDialog(locationName);
                }
            } catch (ConfirmCreateError error) {
                error.showErrorMessage();
            }

            dialog.dismiss();
        }
    }

    public static final String DIALOG_ID = "confirmCreateDialog";

    /**
     * Creates and return new ConfirmCreateFragment object
     * 
     * @param context
     *            application context
     * @return ConfirmCreateFragment object
     */
    public static ConfirmCreateDialog getInstance(Context context) {
        Log.message("Enter");
        ConfirmCreateDialog dialog = new ConfirmCreateDialog();
        dialog.setContext(context);
        return dialog;
    }
    
    private CheckBox mCheckBox_isSave;
    
    private EditText mEditText_locationName;

    private String mLocationName;
    private int mAction;
    private boolean mIsEdit;

    /**
     * Shows error message by resourceId
     * 
     * @param resourceId
     *            string resource id contains error message
     */

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getSherlockActivity(), R.layout.dialog_confirm_create, null));

        mCheckBox_isSave = (CheckBox) getDialogView().findViewById(
                R.id.dialogConfirmCreate_checkbox_isSave);
        mEditText_locationName = (EditText) getDialogView().findViewById(
                R.id.dialogConfirmCreate_editText);
    }
    @Override
    protected void createDialog() {
        super.createDialog();

        if (mAction == Constants.LOCATION_ACTION_EDIT) {
            initEditMode();
        }

        getAlertDialog().setTitle(R.string.dialogConfirmCreate_title);
        getAlertDialog().setMessage(R.string.dialogConfirmCreate_message);
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

    public void setAction(int action) {
        mAction = action;
        mIsEdit = (action == Constants.LOCATION_ACTION_EDIT);
    }

    @Override
    public void setListeners() {
        mCheckBox_isSave.setOnClickListener(new IsSaveClickListener());
        getAlertDialog().setPositiveButton(R.string.dialogConfirmCreate_positive,
                new PositiveClickListener());
        getAlertDialog().setNegativeButton(R.string.dialogConfirmCreate_negative,
                new CancelClickListener());
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
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
}
