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

import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.utils.Log;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Dialog fragment with DatePicker
 */
public class DateDialog extends UfoDialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String DIALOG_ID = "dateDialog";
    private Calendar mCalendar;

    public static DateDialog getInstance(Context context) {
        DateDialog dialog = new DateDialog();
        dialog.setContext(context);
        return dialog;
    }
    
    public void setDate(Calendar calendar) {
        mCalendar = calendar;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");
        return new DatePickerDialog(getContext(), this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }
    
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        Log.message("Enter");
        OnDateChangeListener callback = (OnDateChangeListener) getCallback();
        if (callback != null) {
            callback.dateChangeHandler(year, monthOfYear, dayOfMonth);
        }
    }

    @Override
    public void bindObjects() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListeners() {
        // TODO Auto-generated method stub

    }

    public interface OnDateChangeListener {
        void dateChangeHandler(int year, int month, int day);
    }
}
