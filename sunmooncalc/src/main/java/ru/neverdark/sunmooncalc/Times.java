package ru.neverdark.sunmooncalc;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ufo on 26.04.17.
 */

class Times {
    final static double SUNRISE = -0.833;
    final static double SUNSET = SUNRISE;
    final static double SUNRISE_END = -0.3;
    final static double SUNSET_START = SUNRISE_END;
    final static double DAWN = -6.0;
    final static double DUSK = DAWN;
    final static double NAUTICAL_DAWN = -12.0;
    final static double NAUTICAL_DUSK = NAUTICAL_DAWN;
    final static double NIGHT_END = -18.0;
    final static double NIGHT = NIGHT_END;
    final static double GOLDEN_HOUR_END = 6.0;
    final static double GOLDEN_HOUR = GOLDEN_HOUR_END;

    private final Map<Double, Time> mTimes = new HashMap<>();
    private Calendar solarNoon;
    private Calendar nadir;

    Times() {
        mTimes.put(SUNRISE, new Time());            // sunrise & sunset
        mTimes.put(SUNRISE_END, new Time());        // sunriseEnd & sunsetStart
        mTimes.put(DAWN, new Time());               // dawn & dusk
        mTimes.put(NAUTICAL_DAWN, new Time());      // nauticalDawn && nauticalDusk
        mTimes.put(NIGHT_END, new Time());          // nightEnd && night
        mTimes.put(GOLDEN_HOUR_END, new Time());    // goldenHourEnd && goldenHour
    }

    Calendar getSolarNoon() {
        return solarNoon;
    }

    Calendar getNadir() {
        return nadir;
    }

    Calendar getSunrise() {
        return mTimes.get(SUNRISE).morning;
    }

    Calendar getSunset() {
        return mTimes.get(SUNSET).evening;
    }

    Calendar getSunriseEnd() {
        return mTimes.get(SUNRISE_END).morning;
    }

    Calendar getSunsetStart() {
        return mTimes.get(SUNSET_START).evening;
    }

    Calendar getDawn() {
        return mTimes.get(DAWN).morning;
    }

    Calendar getDusk() {
        return mTimes.get(DUSK).evening;
    }

    Calendar getNauticalDawn() {
        return mTimes.get(NAUTICAL_DAWN).morning;
    }

    Calendar getNauticalDusk() {
        return mTimes.get(NAUTICAL_DUSK).evening;
    }

    Calendar getNightEnd() {
        return mTimes.get(NIGHT_END).morning;
    }

    Calendar getNight() {
        return mTimes.get(NIGHT).evening;
    }

    Calendar getGoldenHourEnd() {
        return mTimes.get(GOLDEN_HOUR_END).morning;
    }

    Calendar getGoldenHour() {
        return mTimes.get(GOLDEN_HOUR).evening;
    }

    void setSolarNoon(Calendar solarNoon) {
        this.solarNoon = solarNoon;
    }

    void setNadir(Calendar nadir) {
        this.nadir = nadir;
    }

    Map<Double, Time> getTimes() {
        return mTimes;
    }
}
