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

    public static boolean isAltitudeShow(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(context.getString(R.string.pref_showAltitude),
                false);
    }

    public static boolean isSunsetShow(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(context.getString(R.string.pref_showSunsetAzimuth),
                true);
    }

    public static boolean isSunriseShow(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(context.getString(R.string.pref_showSunriseAzimuth),
                true);
    }

    public static int getSunLineColor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(
                context.getString(R.string.pref_sunColor),
                context.getResources().getColor(R.color.sun_color)
        );
    }

    public static int getSunsetLineColor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(
                context.getString(R.string.pref_sunsetColor),
                context.getResources().getColor(R.color.sunset_color)
        );
    }

    public static int getSunriseLineColor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(
                context.getString(R.string.pref_sunriseColor),
                context.getResources().getColor(R.color.sunrise_color)
        );
    }
}
