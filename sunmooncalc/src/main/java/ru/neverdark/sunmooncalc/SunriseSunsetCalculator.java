package ru.neverdark.sunmooncalc;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ufo on 25.04.17.
 */

public class SunriseSunsetCalculator {
    private final SunTimes mSunTimes;

    private String getDate(Calendar from, Calendar to) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.US);
        return String.format("%s - %s",
                isValidDate(from)? dateFmt.format(from.getTime()) : "n/a",
                isValidDate(to)? dateFmt.format(to.getTime()) : "n/a");
    }

    private boolean isValidDate(Calendar date) {
        int year = date.get(Calendar.YEAR);
        return year != 1970;
    }

    private static final String TAG = "SunriseSunsetCalculator";

    public SunriseSunsetCalculator(LatLng location, Calendar calendar) {
        Calculator calc = new Calculator();
        mSunTimes = calc.getSunTimes(calendar, location);
    }

    public String getNight() {
        return getDate(mSunTimes.getNight(), mSunTimes.getNightEnd());
    }

    public String getAstroDawn() {
        return getDate(mSunTimes.getNightEnd(), mSunTimes.getNauticalDawn());
    }

    public String getNauticalDawn() {
        return getDate(mSunTimes.getNauticalDawn(), mSunTimes.getDawn());
    }

    public String getDawn() {
        return getDate(mSunTimes.getDawn(), mSunTimes.getSunrise());
    }

    public String getSunrise() {
        return getDate(mSunTimes.getSunrise(), mSunTimes.getSunriseEnd());
    }

    public String getGoldenHourDawn() {
        return getDate(mSunTimes.getSunriseEnd(), mSunTimes.getGoldenHourEnd());
    }

    public String getSolarNoon() {
        SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.US);
        return isValidDate(mSunTimes.getSolarNoon())? dateFmt.format(mSunTimes.getSolarNoon().getTime()) : "n/a";
    }

    public String getGoldenHourDusk() {
        return getDate(mSunTimes.getGoldenHour(), mSunTimes.getSunsetStart());
    }

    public String getSunset() {
        return getDate(mSunTimes.getSunsetStart(), mSunTimes.getSunset());
    }

    public String getDusk() {
        return getDate(mSunTimes.getSunset(), mSunTimes.getDusk());
    }

    public String getNauticalDusk() {
        return getDate(mSunTimes.getDusk(), mSunTimes.getNauticalDusk());
    }

    public String getAstroDusk() {
        return getDate(mSunTimes.getNauticalDusk(), mSunTimes.getNight());
    }
}
