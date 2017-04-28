package ru.neverdark.sunmooncalc;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ufo on 25.04.17.
 */

/*
    [-0.833, 'sunrise',       'sunset'      ],
    [  -0.3, 'sunriseEnd',    'sunsetStart' ],
    [    -6, 'dawn',          'dusk'        ],
    [   -12, 'nauticalDawn',  'nauticalDusk'],
    [   -18, 'nightEnd',      'night'       ],
    [     6, 'goldenHourEnd', 'goldenHour'  ]
 */

/*
00:00—04:24 — night
04:24—05:05 — astronomical twilight
05:05—05:42 — nautical twilight
05:42—06:13 — civil twilight
06:13—06:16 — sunrise
06:16—20:06 — daylight
20:06—20:09 — sunset
20:09—20:39 — civil twilight
20:39—21:17 — nautical twilight
21:17—21:58 — astronomical twilight
21:58—00:00 — night
 */
public class SunriseSunsetCalculator {
    private final Times mTimes;

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
        mTimes = calc.getTimes(calendar, location);
    }

    public String getNight() {
        return getDate(mTimes.getNight(), mTimes.getNightEnd());
    }

    public String getAstroDawn() {
        return getDate(mTimes.getNightEnd(), mTimes.getNauticalDawn());
    }

    public String getNauticalDawn() {
        return getDate(mTimes.getNauticalDawn(), mTimes.getDawn());
    }

    public String getDawn() {
        return getDate(mTimes.getDawn(), mTimes.getSunrise());
    }

    public String getSunrise() {
        return getDate(mTimes.getSunrise(), mTimes.getSunriseEnd());
    }

    public String getGoldenHourDawn() {
        return getDate(mTimes.getSunriseEnd(), mTimes.getGoldenHourEnd());
    }

    public String getSolarNoon() {
        SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.US);
        return isValidDate(mTimes.getSolarNoon())? dateFmt.format(mTimes.getSolarNoon().getTime()) : "n/a";
    }

    public String getGoldenHourDusk() {
        return getDate(mTimes.getGoldenHour(), mTimes.getSunsetStart());
    }

    public String getSunset() {
        return getDate(mTimes.getSunsetStart(), mTimes.getSunset());
    }

    public String getDusk() {
        return getDate(mTimes.getSunset(), mTimes.getDusk());
    }

    public String getNauticalDusk() {
        return getDate(mTimes.getDusk(), mTimes.getNauticalDusk());
    }

    public String getAstroDusk() {
        return getDate(mTimes.getNauticalDusk(), mTimes.getNight());
    }
}
