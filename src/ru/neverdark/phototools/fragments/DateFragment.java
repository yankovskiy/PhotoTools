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

import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Dialog fragment with DatePicker
 */
public class DateFragment extends SherlockDialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");
        int year = getArguments().getInt(Constants.DATE_YEAR);
        int month = getArguments().getInt(Constants.DATE_MONTH);
        int day = getArguments().getInt(Constants.DATE_DAY);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        Log.message("Enter");
        SunsetFragment.setDate(year, monthOfYear, dayOfMonth);
    }
}
