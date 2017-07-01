package ru.neverdark.phototools.utils;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class for determine internet timezone by date and location from Google servers
 */

public class GoogleTimeZone {

    /**
     * Gets timezone from Google servers
     * @param calendar calendar contains date for pick tz
     * @param location location for pick tz
     * @return picked timezone or null for error
     */
    public TimeZone getTimeZone(Calendar calendar, LatLng location) {
        String data = readTimeZoneJson(calendar, location);
        if (data.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String timeZoneId = jsonObject.getString("timeZoneId");
                String rawOffset = jsonObject.getString("rawOffset");

                TimeZone tz = TimeZone.getTimeZone(timeZoneId);
                tz.setRawOffset(Integer.valueOf(rawOffset) * 1000);
                return tz;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Reads TimeZone from Google Json
     *
     * @return TimeZone JSON from Google Json or empty if cannot determine
     */
    private String readTimeZoneJson(Calendar calendar, LatLng location) {
        Log.message("Enter");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Common.copyCalendarWithoutTz(calendar, cal);

            /* Gets desired time as seconds since midnight, January 1, 1970 UTC */
        Long timestamp = cal.getTimeInMillis() / 1000;

        String url_format = "https://maps.googleapis.com/maps/api/timezone/json?location=%f,%f&timestamp=%d";
        String url = String.format(Locale.US, url_format, location.latitude, location.longitude, timestamp);

        return Common.getHttpContent(url);
    }
}
