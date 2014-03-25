/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import ru.neverdark.phototools.R;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class Common {

    /**
     * Converts pixels to DP
     * 
     * @param context
     *            application context
     * @param pixelValue
     *            pixel value for convert
     * @return DP value
     */
    public static int convertPixelToDp(Context context, int pixelValue) {
        return (int) (pixelValue / context.getResources().getDisplayMetrics().density);
    }

    /**
     * Gets screen heights in DP
     * 
     * @param context
     *            application context
     * @return screen heights
     */
    public static int getHeightDp(Context context) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return convertPixelToDp(context, height);
    }

    /**
     * Gets screen width in DP
     * 
     * @param context
     *            application context
     * @return screen width
     */
    public static int getWidthDp(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return convertPixelToDp(context, width);
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
     * @param resourceId
     *            text size resource id for wheels
     * @param isFirstEmpty
     *            true if need first empty item
     */
    public static void setWheelAdapter(Context context,
            WheelView wheel, String values[], int textSizeResourceId,
            boolean isFirstEmpty) {
        AbstractWheelTextAdapter adapter;
        int textSize = (int) (context.getResources().getDimension(
                textSizeResourceId) / context.getResources()
                .getDisplayMetrics().density);
        Log.variable("textSize", String.valueOf(textSize));

        if (isFirstEmpty == false) {
            adapter = new ArrayWheelAdapter<String>(context, values);
        } else {
            adapter = new WheelAdapter<String>(context, values);
        }

        adapter.setTextSize(textSize);
        wheel.setViewAdapter(adapter);
    }

}
