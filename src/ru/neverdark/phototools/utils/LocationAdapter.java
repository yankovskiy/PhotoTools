/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
    private Context mContext;
    private int mResource;
    private ArrayList<LocationRecord> mObjects = new ArrayList<LocationRecord> ();
    
    public LocationAdapter(Context context, int resource,
            ArrayList<LocationRecord> objects) {
        super(context, resource, objects);
        Log.message("Enter");
        mContext = context;
        mResource = resource;
        mObjects = objects;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.message("Enter");
        // TODO
        View row = convertView;
        LocationHolder holder = null;
        
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(mResource, parent, false);
            holder = new LocationHolder();
            holder.locationRow_image_edit = (ImageView) row.findViewById(R.id.locationRow_image_edit);
            holder.locationRow_image_remove = (ImageView) row.findViewById(R.id.locationRow_image_remove);
            holder.locationRow_label = (TextView) row.findViewById(R.id.locationRow_label);
            row.setTag(holder);
        } else {
            holder = (LocationHolder) row.getTag();
        }
        
        LocationRecord record = mObjects.get(position);
        holder.locationRow_label.setText(record.locationName);
        
        holder.locationRow_image_edit.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.message("Enter");
                // TODO edit
            }
        });
        
        holder.locationRow_image_remove.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.message("Enter");
                // TODO remove
                
            }
        });
        
        return row;
    }

    static class LocationHolder {
        ImageView locationRow_image_remove;
        ImageView locationRow_image_edit;
        TextView locationRow_label;
    }
}
