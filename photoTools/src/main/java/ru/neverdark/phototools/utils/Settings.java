package ru.neverdark.phototools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.neverdark.phototools.R;

/**
 * Created by ufo on 18.05.17.
 */

public class Settings {
    /**
     * Checks value for 24-hour mode
     *
     * @param context application context
     * @return true for enabled 24-hour mode
     */
    public static boolean is24HourMode(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(
                context.getString(R.string.pref_24HourMode), false);
    }
}
