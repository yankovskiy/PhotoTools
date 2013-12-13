package ru.neverdark.phototools.utils;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class Common {
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
     * @param activity
     *            application activity
     * @param wheel
     *            object for sets adapter
     * @param values
     *            values for adapter
     * @param resourceId
     *            text size resource id for wheels
     * @param isFirstEmpty
     *            true if need first empty item
     */
    public static void setWheelAdapter(SherlockFragmentActivity activity,
            WheelView wheel, String values[], int textSizeResourceId,
            boolean isFirstEmpty) {
        AbstractWheelTextAdapter adapter;
        int textSize = (int) activity.getResources().getDimension(
                textSizeResourceId);
        
        if (isFirstEmpty == false) {
            adapter = new ArrayWheelAdapter<String>(activity, values);
        } else {
            adapter = new WheelAdapter<String>(activity, values);
        }
        
        adapter.setTextSize(textSize);
        wheel.setViewAdapter(adapter);
    }
}
