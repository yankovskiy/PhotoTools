package ru.neverdark.sunmooncalc;

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
        return (date.getTimeInMillis() / DAY_MS - 0.5 + J1970);
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

    private Coords sunCoords(double d) {
        final double M = solarMeanAnomaly(d);
        final double L = eclipticLongitude(M);

        Coords ret = new Coords();
        ret.dec = declination(L, 0);
        ret.ra = rightAscension(L, 0);

        return ret;
    }

    private Position getPosition(Calendar date, LatLng latLng) {
        final double lw = RAD * -latLng.longitude;
        final double phi = RAD * latLng.latitude;
        final double d = toDays(date);
        final Coords c = sunCoords(d);
        final double H = siderealTime(d, lw) - c.ra;

        Position ret = new Position();
        ret.azimuth = azimuth(H, phi, c.dec);
        ret.altitude = altitude(H, phi, c.dec);

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

    Times getTimes(Calendar date, LatLng latLng) {
        final double lw = RAD * -latLng.longitude;
        final double phi = RAD * latLng.latitude;
        final double d = toDays(date);
        final double n = julianCycle(d, lw);
        final double ds = approxTransit(0, lw, n);
        final double M = solarMeanAnomaly(ds);
        final double L = eclipticLongitude(M);
        final double dec = declination(L, 0);
        final double Jnoon = solarTransitJ(ds, M, L);

        Times times = new Times();
        times.setSolarNoon(fromJulian(Jnoon));
        times.setNadir(fromJulian(Jnoon - 0.5));

        for (Map.Entry entry : times.getTimes().entrySet()) {
            double angle = (Double) entry.getKey();
            Time time = (Time) entry.getValue();

            double Jset = getSetJ(angle * RAD, lw, phi, dec, n, M, L);
            double Jrise = Jnoon - (Jset - Jnoon);

            time.morning = fromJulian(Jrise);
            time.evening = fromJulian(Jset);
        }

        return times;
    }
}
