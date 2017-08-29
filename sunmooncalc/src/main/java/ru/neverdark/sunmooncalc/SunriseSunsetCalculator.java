package ru.neverdark.sunmooncalc;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ufo on 25.04.17.
 */

public class SunriseSunsetCalculator {
    private final SunTimes mSunTimes;
    private final TimeZone mTimeZone;
    private final SunPosition mSunPosition;
    private final SunPosition mSunsetPosition;
    private final SunPosition mSunrisePosition;

    private String getDate(Calendar from, Calendar to) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.US);
        dateFmt.setTimeZone(mTimeZone);
        return String.format("%s - %s",
                isValidDate(from)? dateFmt.format(from.getTime()) : "n/a",
                isValidDate(to)? dateFmt.format(to.getTime()) : "n/a");
    }

    public String getDate(Calendar cal) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.US);
        return String.format("%s", isValidDate(cal)? dateFmt.format(cal.getTime()) : "n/a");
    }

    private boolean isValidDate(Calendar date) {
        int year = date.get(Calendar.YEAR);
        return year != 1970;
    }

    private static final String TAG = "SunriseSunsetCalculator";

    public SunriseSunsetCalculator(LatLng location, Calendar calendar) {
        Calculator calc = new Calculator();
        mTimeZone = calendar.getTimeZone();
        mSunTimes = calc.getSunTimes(calendar, location);
        mSunPosition = calc.getSunPosition(calendar, location);
        mSunsetPosition = calc.getSunPosition(mSunTimes.getSunset(), location);
        mSunrisePosition = calc.getSunPosition(mSunTimes.getSunriseEnd(), location);
    }

    public double getAzimuth() {
        return mSunPosition.azimuth;
    }

    public double getAltitude() {
        return mSunPosition.altitude;
    }

    public double getSunsetAzimuth() {
        return mSunsetPosition.azimuth;
    }

    public double getSunriseAzimuth() {
        return mSunrisePosition.azimuth;
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
