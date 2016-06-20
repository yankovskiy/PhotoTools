/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Geocoder {
    private Context mContext;

    /**
     * Constructor
     * 
     * @param context
     *            application context
     */
    public Geocoder(Context context) {
        mContext = context;
    }

    /**
     * Gets coordinate from location name
     * 
     * @param searchString
     *            user specify location name
     * @return coordinates for founded location or null if not found
     */
    public LatLng getFromLocation(String searchString) {
        LatLng coords = null;
        String locationInfo = getLocationInfo(searchString);

        if (locationInfo.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(locationInfo);
                JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                JSONObject jsonLocation = jsonArray.getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location");
                Double longitude = jsonLocation.getDouble("lng");
                Double latitude = jsonLocation.getDouble("lat");

                coords = new LatLng(latitude, longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return coords;
    }

    /**
     * Gets json for location
     * 
     * @param searchString
     *            location for search
     * @return json for location or empty string if connection problem
     */
    private String getLocationInfo(String searchString) {
        String query = null;
        try {
            query = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String url = String
                .format("https://maps.google.com/maps/api/geocode/json?address=%s&sensor=false",
                        query);

        return Common.getHttpContent(url);
    }

    /**
     * Checks connection status
     * 
     * @return true if device online, false in other case
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
