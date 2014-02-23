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

import ru.neverdark.phototools.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Array adapter for plug-in
 */
public class PluginAdapter extends ArrayAdapter<MainMenuItem> {

    private final List<MainMenuItem> mObjects;
    private final int mResource;
    private final Context mContext;
    private final boolean mIsInstalled;

    private static class RowHolder {
        private ImageView mPluginIcon;
        private TextView mPluginLabel;
        private ImageView mPluginRemove;
    }

    /**
     * Constructor
     * 
     * @param context
     *            application context
     * @param resource
     *            resource id
     * @param objects
     *            plug-in list
     * @param isInstalled
     *            true for list installed plug-in
     */
    public PluginAdapter(Context context, int resource,
            List<MainMenuItem> objects, boolean isInstalled) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mObjects = objects;
        mIsInstalled = isInstalled;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        RowHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResource, parent, false);
            holder = new RowHolder();

            if (mIsInstalled) {
                holder.mPluginIcon = (ImageView) row
                        .findViewById(R.id.plugin_icon);
                holder.mPluginLabel = (TextView) row
                        .findViewById(R.id.plugin_label);
                holder.mPluginRemove = (ImageView) row
                        .findViewById(R.id.plugin_remove);
            } else {
                holder.mPluginIcon = (ImageView) row
                        .findViewById(R.id.plugin_available_icon);
                holder.mPluginLabel = (TextView) row
                        .findViewById(R.id.plugin_available_label);
            }

            row.setTag(holder);
        } else {
            holder = (RowHolder) row.getTag();
        }

        MainMenuItem item = mObjects.get(position);
        holder.mPluginIcon.setImageDrawable(item.getIcon());
        holder.mPluginLabel.setText(item.getAppName());

        if (mIsInstalled) {
            holder.mPluginRemove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeApplication(mObjects.get(position).getPluginPackage());
                }
            });
        }

        return row;
    }

    /**
     * Shows uninstall dialog
     * 
     * @param pluginPackage
     *            package name for uninstall
     */
    private void removeApplication(String pluginPackage) {
        Uri packageURI = Uri.parse("package:".concat(pluginPackage));
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        mContext.startActivity(uninstallIntent);
    }
}
