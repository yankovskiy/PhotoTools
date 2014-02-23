/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
