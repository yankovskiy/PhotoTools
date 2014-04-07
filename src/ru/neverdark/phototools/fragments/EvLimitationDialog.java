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

import ru.neverdark.phototools.utils.Limit;
import android.content.Context;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EvLimitationDialog extends SherlockDialogFragment {
    /**
     * Interface provides callback method calls for OK button in the dialog
     */
    public interface OnEvLimitationListener {
        /**
         * Calls when user tap OK button
         * 
         * @param data
         *            for limit values in the wheels
         */
        public void onEvLimitationHandler(Limit data);
    }
    public static final String DIALOG_TAG = "evLimitationDialog";
    /**
     * Creates dialog
     * 
     * @param context
     *            application context
     * @return dialog
     */
    public static EvLimitationDialog getInstance(Context context) {
        EvLimitationDialog dialog = new EvLimitationDialog();
        dialog.mContext = context;
        return dialog;
    }
    private Context mContext;

    private OnEvLimitationListener mCallback;

    private Limit mLimitData;

    /**
     * Sets callback object for handling OK button on the dialog
     * 
     * @param callback
     *            callback object
     */
    public void setCallback(OnEvLimitationListener callback) {
        mCallback = callback;
    }

    /**
     * Sets limitation data for loading
     * 
     * @param data
     *            data for loading or null for default value
     */
    public void setLimitData(Limit data) {
        mLimitData = data;
    }
}
