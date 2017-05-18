package ru.neverdark.phototools.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.Calendar;

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Settings;

/**
 * Created by ufo on 16.05.17.
 */

public class DateTimeDialog extends AppCompatDialogFragment {
    private Calendar mCalendar;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private TabHost mTabHost;
    private OnDateTimeChange mCallback;

    public static DateTimeDialog getInstance(Calendar calendar, OnDateTimeChange callback) {
        DateTimeDialog dialog = new DateTimeDialog();
        dialog.mCalendar = calendar;
        dialog.mCallback = callback;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.date_time_dialog, null);
        dialog.setView(view);

        bindObjects(view);
        createButtons(dialog);
        buildTabs();

        initDate();

        return dialog.create();
    }

    private void set24HourMode(boolean is24HourMode) {
        mTimePicker.setIs24HourView(is24HourMode);
    }

    private void hideCalendar() {
        mDatePicker.setCalendarViewShown(false);
    }

    private void initDate() {
        set24HourMode(Settings.is24HourMode(getContext()));
        hideCalendar();
        initDateTime(mCalendar);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new ResetDateTimeClickListener());
    }

    private void buildTabs() {
        mTabHost.setup();

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("time");
        tabSpec.setContent(R.id.dialog_timePicker);
        tabSpec.setIndicator(getString(R.string.dateTimeDialog_timeTab));
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec("date");
        tabSpec.setContent(R.id.dialog_datePicker);
        tabSpec.setIndicator(getString(R.string.dateTimeDialog_dateTab));
        mTabHost.addTab(tabSpec);
    }

    private void createButtons(AlertDialog.Builder dialog) {
        dialog.setNeutralButton(R.string.dialog_button_reset, null);
        dialog.setPositiveButton(R.string.dialog_button_ok, new PositiveClickListener());
        dialog.setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

    private void bindObjects(View view) {
        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_timePicker);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_datePicker);
        mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
    }

    private void initDateTime(Calendar calendar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
        } else {
            mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }
        mDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    public interface OnDateTimeChange {
        void onDateTimeChange(Calendar calendar);
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mTimePicker.clearFocus();
            mDatePicker.clearFocus();

            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth();
            int day = mDatePicker.getDayOfMonth();
            int hour;
            int minute;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = mTimePicker.getHour();
                minute = mTimePicker.getMinute();
            } else {
                hour = mTimePicker.getCurrentHour();
                minute = mTimePicker.getCurrentMinute();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);

            if (mCallback != null) {
                mCallback.onDateTimeChange(calendar);
            }
        }
    }

    private class ResetDateTimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            fixValues();
            initDateTime(Calendar.getInstance());
        }

        private void fixValues() {
            // bug is not reproducible in APIs 24 and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return;
            try {
                int hour, minute;
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    hour = mTimePicker.getHour();
                    minute = mTimePicker.getMinute();
                } else {
                    hour = mTimePicker.getCurrentHour();
                    minute = mTimePicker.getCurrentMinute();
                }

                Field mDelegateField = mTimePicker.getClass().getDeclaredField("mDelegate");
                mDelegateField.setAccessible(true);
                Class<?> clazz;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    clazz = Class.forName("android.widget.TimePickerClockDelegate");
                } else {
                    clazz = Class.forName("android.widget.TimePickerSpinnerDelegate");
                }
                Field mInitialHourOfDayField = clazz.getDeclaredField("mInitialHourOfDay");
                Field mInitialMinuteField = clazz.getDeclaredField("mInitialMinute");
                mInitialHourOfDayField.setAccessible(true);
                mInitialMinuteField.setAccessible(true);
                mInitialHourOfDayField.setInt(mDelegateField.get(mTimePicker), hour);
                mInitialMinuteField.setInt(mDelegateField.get(mTimePicker), minute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
