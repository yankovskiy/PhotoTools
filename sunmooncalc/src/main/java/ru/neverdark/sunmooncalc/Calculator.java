package ru.neverdark.sunmooncalc;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Map;

/**
 * Основан на JS калькуляторе от Владимира Агафонкина
 * @link https://github.com/yankovskiy/suncalc
 */
class Calculator {
    private static final long DAY_MS = 1000 * 60 * 60 * 24;
    private static final int J1970 = 2440588;
    private static final int J2000 = 2451545;
    private static final double RAD = Math.PI / 180;
    private static final double E = RAD * 23.4397; // obliquity of the Earth
    private static final double J0 = 0.0009;

    private double toJulian(Calendar date) {
        double first = (double)date.getTimeInMillis() / DAY_MS;
        return (first - 0.5 + J1970);
    }

    private Calendar fromJulian(double j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) ((j + 0.5 - J1970) * DAY_MS));
        return calendar;
    }

    private double toDays(Calendar calendar) {
        return toJulian(calendar) - J2000;
    }

    private double rightAscension(double l, double b) {
        return Math.atan2(Math.sin(l) * Math.cos(E) - Math.tan(b) * Math.sin(E), Math.cos(l));
    }

    private double declination(double l, double b) {
        return Math.asin(Math.sin(b) * Math.cos(E) + Math.cos(b) * Math.sin(E) * Math.sin(l));
    }

    private double azimuth(double H, double phi, double dec) {
        return Math.atan2(Math.sin(H), Math.cos(H) * Math.sin(phi) - Math.tan(dec) * Math.cos(phi));
    }

    private double altitude(double H, double phi, double dec) {
        return Math.asin(Math.sin(phi) * Math.sin(dec) + Math.cos(phi) * Math.cos(dec) * Math.cos(H));
    }

    private double siderealTime(double d, double lw) {
        return RAD * (280.16 + 360.9856235 * d) - lw;
    }

    private double astroRefraction(double h) {
        // the following formula works for positive altitudes only.
        // if h = -0.08901179 a div/0 would occur.
        if (h < 0) {
            h = 0;
        }
        // formula 16.4 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
        // 1.02 / tan(h + 10.26 / (h + 5.10)) h in degrees, result in arc minutes -> converted to rad:
        return 0.0002967 / Math.tan(h + 0.00312536 / (h + 0.08901179));
    }

    private double solarMeanAnomaly(double ds) {
        return RAD * (357.5291 + 0.98560028 * ds);
    }

    private double eclipticLongitude(double M) {
        // equation of center
        final double C = RAD * (1.9148 * Math.sin(M) + 0.02 * Math.sin(2 * M) + 0.0003 * Math.sin(3 * M));
        // perihelion of the Earth
        final double P = RAD * 102.9372;
        return M + C + P + Math.PI;
    }

    private SunCoords sunCoords(double d) {
        final double M = solarMeanAnomaly(d);
        final double L = eclipticLongitude(M);

        SunCoords ret = new SunCoords();
        ret.dec = declination(L, 0);
        ret.ra = rightAscension(L, 0);

        return ret;
    }

    private static final String TAG = "Calculator";

    SunPosition getSunPosition(Calendar date, LatLng latLng) {
        Log.v(TAG, "getSunPosition: date = " + date);
        Log.v(TAG, "getSunPosition: lat = " + latLng.latitude);
        Log.v(TAG, "getSunPosition: lng = " + latLng.longitude);
        final double lw = RAD * -latLng.longitude;
        final double phi = RAD * latLng.latitude;
        Log.v(TAG, "getSunPosition: lw = " + lw);
        Log.v(TAG, "getSunPosition: phi = " + phi);
        final double d = toDays(date);
        Log.v(TAG, "getSunPosition: d = " + d);
        final SunCoords c = sunCoords(d);
        Log.v(TAG, "getSunPosition: c.ra = " + c.ra);
        Log.v(TAG, "getSunPosition: c.dec = " + c.dec);
        final double H = siderealTime(d, lw) - c.ra;
        Log.v(TAG, "getSunPosition: H = " + H);

        SunPosition ret = new SunPosition();
        ret.azimuth = azimuth(H, phi, c.dec) + Math.PI;
        ret.altitude = altitude(H, phi, c.dec) * 180 / Math.PI;

        Log.v(TAG, "getSunPosition: azimuth = " + ret.azimuth);
        Log.v(TAG, "getSunPosition: altitude = " + ret.altitude);
        return ret;
    }

    private double julianCycle(double d, double lw) {
        return Math.round(d - J0 - lw / (2 * Math.PI));
    }

    private double approxTransit(double Ht, double lw, double n) {
        return J0 + (Ht + lw) / (2 * Math.PI) + n;
    }

    private double solarTransitJ(double ds, double M, double L) {
        return J2000 + ds + 0.0053 * Math.sin(M) - 0.0069 * Math.sin(2 * L);
    }

    private double hourAngel(double h, double phi, double dec) {
        return Math.acos((Math.sin(h) - Math.sin(phi) * Math.sin(dec)) / (Math.cos(phi) * Math.cos(dec)));
    }

    private double getSetJ(double h, double lw, double phi, double dec, double n, double M, double L) {
        final double w = hourAngel(h, phi, dec);
        final double a = approxTransit(w, lw, n);
        return solarTransitJ(a, M, L);
    }

    SunTimes getSunTimes(Calendar date, LatLng latLng) {
        final double lw = RAD * -latLng.longitude;
        final double phi = RAD * latLng.latitude;
        final double d = toDays(date);
        final double n = julianCycle(d, lw);
        final double ds = approxTransit(0, lw, n);
        final double M = solarMeanAnomaly(ds);
        final double L = eclipticLongitude(M);
        final double dec = declination(L, 0);
        final double Jnoon = solarTransitJ(ds, M, L);

        SunTimes sunTimes = new SunTimes();
        sunTimes.setSolarNoon(fromJulian(Jnoon));
        sunTimes.setNadir(fromJulian(Jnoon - 0.5));

        for (Map.Entry entry : sunTimes.getTimes().entrySet()) {
            double angle = (Double) entry.getKey();
            SunTimes.Time time = (SunTimes.Time) entry.getValue();

            double Jset = getSetJ(angle * RAD, lw, phi, dec, n, M, L);
            double Jrise = Jnoon - (Jset - Jnoon);

            time.morning = fromJulian(Jrise);
            time.evening = fromJulian(Jset);
        }

        return sunTimes;
    }

    private MoonCoords moonCoords(double d) {
        // ecliptic longitude
        final double L = RAD * (218.316 + 13.176396 * d);
        // mean anomaly
        final double M = RAD * (134.963 + 13.064993 * d);
        // mean distance
        final double F = RAD * (93.272 + 13.229350 * d);
        // longitude
        final double l =  L + RAD * 6.289 * Math.sin(M);
        // latitude
        final double b = RAD * 5.128 * Math.sin(F);
        // distance to the moon in km
        final double dt = 385001 - 20905 * Math.cos(M);

        MoonCoords ret = new MoonCoords();
        ret.ra = rightAscension(l, b);
        ret.dec = declination(l, b);
        ret.dist = dt;

        return ret;
    }

    MoonPosition getMoonPosition(Calendar date, double lat, double lng) {
        final double lw = RAD * -lng;
        final double phi = RAD * lat;
        final double d = toDays(date);
        final MoonCoords c = moonCoords(d);
        final double H = siderealTime(d, lw) - c.ra;
        double h = altitude(H, phi, c.dec);
        // formula 14.1 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
        final double pa = Math.atan2(Math.sin(H), Math.tan(phi) * Math.cos(c.dec) - Math.sin(c.dec) * Math.cos(H));

        h += astroRefraction(h); // altitude correction for refraction

        MoonPosition ret = new MoonPosition();
        ret.azimuth = azimuth(H, phi, c.dec);
        ret.altitude = h;
        ret.distance = c.dist;
        ret.parallacticAngle = pa;

        return ret;
    }

    MoonIllumination getMoonIllumination(Calendar date) {
        final double d = toDays(date);
        final SunCoords s = sunCoords(d);
        final MoonCoords m = moonCoords(d);
        final int sdist = 149598000; // distance from Earth to Sun in km
        final double phi = Math.acos(Math.sin(s.dec) * Math.sin(m.dec) + Math.cos(s.dec) * Math.cos(m.dec) * Math.cos(s.ra - m.ra));
        final double inc = Math.atan2(sdist * Math.sin(phi), m.dist - sdist * Math.cos(phi));
        final double angle = Math.atan2(Math.cos(s.dec) * Math.sin(s.ra - m.ra), Math.sin(s.dec) * Math.cos(m.dec) -
                Math.cos(s.dec) * Math.sin(m.dec) * Math.cos(s.ra - m.ra));

        MoonIllumination ret = new MoonIllumination();
        ret.fraction = (1 + Math.cos(inc)) / 2;
        ret.phase = 0.5 + 0.5 * inc * (angle < 0 ? -1 : 1) / Math.PI;
        ret.angle = angle;

        return ret;
    }

    private Calendar hoursLater(Calendar date, double h) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) (date.getTimeInMillis() + h * DAY_MS / 24));
        return calendar;
    }

    MoonTimes getMoonTimes(Calendar date, double lat, double lng) {
        Calendar t = (Calendar) date.clone();
        t.set(Calendar.HOUR_OF_DAY, 0);
        t.set(Calendar.MINUTE, 0);
        t.set(Calendar.SECOND, 0);
        t.set(Calendar.MILLISECOND, 0);

        final double hc = 0.133 * RAD;
        double h0 = getMoonPosition(t, lat, lng).altitude - hc;
        double h1, h2, rise = 0, set = 0, a, b, xe, ye = 0, d, roots, x1 = 0, x2 = 0, dx;

        // go in 2-hour chunks, each time seeing if a 3-point quadratic curve crosses zero (which means rise or set)
        for (int i = 1; i <= 24; i += 2) {
            h1 = getMoonPosition(hoursLater(t, i), lat, lng).altitude - hc;
            h2 = getMoonPosition(hoursLater(t, i + 1), lat, lng).altitude - hc;

            a = (h0 + h2) / 2 - h1;
            b = (h2 - h0) / 2;
            xe = -b / (2 * a);
            ye = (a * xe + b) * xe + h1;
            d = b * b - 4 * a * h1;
            roots = 0;

            if (d >= 0) {
                dx = Math.sqrt(d) / (Math.abs(a) * 2);
                x1 = xe - dx;
                x2 = xe + dx;
                if (Math.abs(x1) <= 1) roots++;
                if (Math.abs(x2) <= 1) roots++;
                if (x1 < -1) x1 = x2;
            }

            if (roots == 1) {
                if (h0 < 0) rise = i + x1;
                else set = i + x1;

            } else if (roots == 2) {
                rise = i + (ye < 0 ? x2 : x1);
                set = i + (ye < 0 ? x1 : x2);
            }

            if (rise != 0 && set != 0) break;

            h0 = h2;
        }

        MoonTimes ret = new MoonTimes();
        if (rise != 0) {
            ret.rise = hoursLater(t, rise);
        }

        if (set != 0) {
            ret.set = hoursLater(t, set);
        }

        if (rise == 0 & set == 0) {
            if (ye > 0) {
                ret.alwaysUp = true;
            } else {
                ret.alwaysDown = true;
            }
        }

        return ret;
    }

}
