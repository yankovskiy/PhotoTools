package ru.neverdark.phototools.utils;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Adapter for main menu list
 */
public class MainMenuAdapter extends ArrayAdapter<MainMenuItem> {

    /**
     * Constructor
     * 
     * @param context
     *            application context
     * @param resource
     *            resource ID for a layout file containing a layout to use when
     *            instantiating views
     * @param textViewResource
     *            id of the TextView within the layout resource to be populated
     * @param objects
     *            objects to represent in the ListView
     */
    public MainMenuAdapter(Context context, int resource, int textViewResource,
            List<MainMenuItem> objects) {
        super(context, resource, textViewResource, objects);
        setNotifyOnChange(true);
    }

}
