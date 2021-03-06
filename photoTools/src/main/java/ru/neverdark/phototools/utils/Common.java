/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import ru.neverdark.phototools.BuildConfig;
import ru.neverdark.phototools.R;

public class Common {
    /**
     * Проверяет наличие прав на доступ к геолокации
     * @param context контекст приложения
     * @return true Если права на доступ к геолокации есть
     */
    public static boolean isHavePermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static String getHttpContent(String httpUrl) {
        URL url;
        StringBuilder builder = new StringBuilder();
        HttpsURLConnection urlConnection = null;
        try {
            url = new URL(httpUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream content = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.message("Download fail");
            }
        } catch (IOException e) {
            Log.message("IOException");
            builder = new StringBuilder();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return builder.toString();
    }

    /**
     * Sets background from drawable resource
     *
     * @param view
     *            view object for set drawable resource
     * @param resourceId
     *            drawable resource
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setBg(View view, final int resourceId) {
        Drawable res = view.getResources().getDrawable(resourceId);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            view.setBackgroundDrawable(res);
        } else {
            view.setBackground(res);
        }
    }

    /**
     * Sets adapter for wheel
     *
     * @param context
     *            application context
     * @param wheel
     *            object for sets adapter
     * @param values
     *            values for adapter
     * @param textSizeResourceId
     *            text size resource id for wheels
     * @param isFirstEmpty
     *            true if need first empty item
     */
    public static void setWheelAdapter(Context context, WheelView wheel,
                                       List<String> values, int textSizeResourceId, boolean isFirstEmpty) {
        String[] localValues = values.toArray(new String[values.size()]);
        setWheelAdapter(context, wheel, localValues, textSizeResourceId,
                isFirstEmpty);
    }

    /**
     * Sets adapter for wheel
     *
     * @param context
     *            application context
     * @param wheel
     *            object for sets adapter
     * @param values
     *            values for adapter
     * @param textSizeResourceId
     *            text size resource id for wheels
     * @param isFirstEmpty
     *            true if need first empty item
     */
    public static void setWheelAdapter(Context context, WheelView wheel,
                                       String values[], int textSizeResourceId, boolean isFirstEmpty) {
        AbstractWheelTextAdapter adapter;
        int textSize = (int) (context.getResources().getDimension(
                textSizeResourceId) / context.getResources()
                .getDisplayMetrics().density);
        Log.variable("textSize", String.valueOf(textSize));

        if (!isFirstEmpty) {
            adapter = new ArrayWheelAdapter<>(context, values);
        } else {
            adapter = new WheelAdapter<>(context, values);
        }

        adapter.setTextSize(textSize);
        wheel.setViewAdapter(adapter);
    }

    /**
     * Opens market detail application page for donate app
     *
     * @param context application context
     */
    public static void gotoDonate(Context context) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, "Goto Market", Toast.LENGTH_LONG).show();
        } else {
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri
                    .parse("market://details?id=ru.neverdark.phototoolsdonate"));
            try {
                context.startActivity(marketIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.error_googlePlayNotFound, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks connection status
     *
     * @return true if device online, false in other case
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * Copy source calendar data except timezone to the target calendar
     * @param sourceCalendar source Calendar object
     * @param targetCalendar target Calendar object
     */
    public static void copyCalendarWithoutTz(Calendar sourceCalendar, Calendar targetCalendar) {
        targetCalendar.set(Calendar.YEAR, sourceCalendar.get(Calendar.YEAR));
        targetCalendar.set(Calendar.MONTH, sourceCalendar.get(Calendar.MONTH));
        targetCalendar.set(Calendar.DAY_OF_MONTH, sourceCalendar.get(Calendar.DAY_OF_MONTH));
        targetCalendar.set(Calendar.HOUR_OF_DAY, sourceCalendar.get(Calendar.HOUR_OF_DAY));
        targetCalendar.set(Calendar.MINUTE, sourceCalendar.get(Calendar.MINUTE));
        targetCalendar.set(Calendar.SECOND, sourceCalendar.get(Calendar.SECOND));
    }

    public static void showMessage(Context context, int messsageId) {
        Toast.makeText(context, messsageId, Toast.LENGTH_LONG).show();
    }

    public static class MinMaxValues {
        private String mMinValue;
        private String mMaxValue;

        public static MinMaxValues getMinMax(WheelView wheel) {
            MinMaxValues minMax = new MinMaxValues();
            int count = wheel.getViewAdapter().getItemsCount();
            minMax.mMinValue = (String) wheel.getViewAdapter()
                    .getItemByIndex(0);
            minMax.mMaxValue = (String) wheel.getViewAdapter().getItemByIndex(
                    count - 1);

            return minMax;
        }

        public String getMinValue() {
            return mMinValue;
        }

        public String getMaxValue() {
            return mMaxValue;
        }
    }
}
