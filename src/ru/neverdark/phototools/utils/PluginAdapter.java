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
 * Array adapter for plugin
 */
public class PluginAdapter extends ArrayAdapter<MainMenuItem> {

    private final List<MainMenuItem> mObjects;
    private final int mResource;
    private final Context mContext;
    
    private static class RowHolder {
        private ImageView mPluginIcon;
        private TextView mPluginLabel;
        private ImageView mPluginRemove;
    }
    
    /**
     * Constructor
     * @param context application context
     * @param resource resource id
     * @param objects plug-in list
     */
    public PluginAdapter(Context context, int resource,
            List<MainMenuItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mObjects = objects;
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
            holder.mPluginIcon = (ImageView) row.findViewById(R.id.plugin_icon);
            holder.mPluginLabel = (TextView) row.findViewById(R.id.plugin_label);
            holder.mPluginRemove = (ImageView) row.findViewById(R.id.plugin_remove);
            row.setTag(holder);
        } else {
            holder = (RowHolder) row.getTag();
        }

        MainMenuItem item = mObjects.get(position);
        holder.mPluginIcon.setImageDrawable(item.getIcon());
        holder.mPluginLabel.setText(item.getAppName());
        
        holder.mPluginRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeApplication(mObjects.get(position).getPluginPackage());
            }
        });
        
        return row;
    }

    /**
     * Shows uninstall dialog
     * @param pluginPackage package name for uninstall
     */
    private void removeApplication(String pluginPackage) {
        Uri packageURI = Uri.parse("package:".concat(pluginPackage));
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        mContext.startActivity(uninstallIntent);
    }
}
