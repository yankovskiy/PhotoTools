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

import java.util.ArrayList;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.ui.ImageOnTouchListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A class provide adapter for locations
 */
public class LocationAdapter extends ArrayAdapter<LocationRecord> {
    /**
     * Interface for LocationImageChangeListener
     */
    public interface LocationImageChangeListener {
        /**
         * Listener for handling Edit image clicked
         * 
         * @param position
         *            position in ListView
         */
        public void onLocationImageEdit(int position);

        /**
         * Listener for handling Remove image clicked
         * 
         * @param position
         *            position in ListView
         */
        public void onLocationImageRemove(int position);
    }

    private LocationImageChangeListener mCallback;

    private Context mContext;
    private int mResource;

    private ArrayList<LocationRecord> mObjects = new ArrayList<LocationRecord>();

    public LocationAdapter(Context context, int resource,
            ArrayList<LocationRecord> objects,
            LocationImageChangeListener callback) {
        super(context, resource, objects);
        Log.message("Enter");
        mContext = context;
        mResource = resource;
        mObjects = objects;
        mCallback = callback;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.message("Enter");
        View row = convertView;
        LocationHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResource, parent, false);
            holder = new LocationHolder();
            holder.locationRow_image_edit = (ImageView) row
                    .findViewById(R.id.locationRow_image_edit);
            holder.locationRow_image_remove = (ImageView) row
                    .findViewById(R.id.locationRow_image_remove);
            holder.locationRow_label = (TextView) row
                    .findViewById(R.id.locationRow_label);
            row.setTag(holder);
        } else {
            holder = (LocationHolder) row.getTag();
        }

        LocationRecord record = mObjects.get(position);
        holder.locationRow_label.setText(record.locationName);

        /* if is not current location and not point on map */
        if (position > Constants.LOCATION_POINT_ON_MAP_CHOICE) {
            holder.locationRow_image_edit.setVisibility(View.VISIBLE);
            holder.locationRow_image_remove.setVisibility(View.VISIBLE);

            holder.locationRow_image_edit
                    .setOnTouchListener(new ImageOnTouchListener());
            holder.locationRow_image_remove
                    .setOnTouchListener(new ImageOnTouchListener());

            holder.locationRow_image_edit
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.message("Enter");
                            mCallback.onLocationImageEdit(position);
                        }
                    });

            holder.locationRow_image_remove
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.message("Enter");
                            mCallback.onLocationImageRemove(position);
                        }
                    });

        } else {
            holder.locationRow_image_edit.setVisibility(View.GONE);
            holder.locationRow_image_remove.setVisibility(View.GONE);
        }

        return row;
    }

    static class LocationHolder {
        ImageView locationRow_image_remove;
        ImageView locationRow_image_edit;
        TextView locationRow_label;
    }
}
