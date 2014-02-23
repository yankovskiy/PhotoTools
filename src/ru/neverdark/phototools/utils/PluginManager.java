package ru.neverdark.phototools.utils;

import java.util.ArrayList;
import java.util.List;

import ru.neverdark.phototools.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Singleton class for plug-in management
 */
public class PluginManager {
    private static PluginManager mPluginManager;
    private static final String PLUGINS_LIST = "ru.neverdark.phototools.PLUGINS";
    /**
     * Creates new class object or gets old object if exists
     * 
     * @param context
     *            application context
     * @return class object
     */
    public static PluginManager getInstance(Context context) {
        if (mPluginManager == null) {
            mPluginManager = new PluginManager();
            mPluginManager.mContext = context;
        }

        return mPluginManager;
    }
    private Context mContext;

    private final List<MainMenuItem> mList;

    /**
     * Constructor
     */
    private PluginManager() {
        mList = new ArrayList<MainMenuItem>();
    }

    /**
     * Creates main menu item
     * 
     * @param title
     *            menu item title
     * @param pluginPackage
     *            name of plug-in package
     *            @param appName application name
     * @return menu item
     */
    private MainMenuItem createMenuItem(String title, String pluginPackage, Drawable icon, String appName) {
        MainMenuItem item = new MainMenuItem();
        item.setIsPlugin(true);
        item.setTitle(title);
        item.setPluginPackage(pluginPackage);
        item.setIcon(icon);
        item.setAppName(appName);
        return item;
    }

    /**
     * Gets main menu items contains plugins record
     * 
     * @return
     */
    public List<MainMenuItem> getMenuItems() {
        return mList;
    }

    /**
     * Runs plug-in by name of plug-in package
     * 
     * @param pluginPackage
     *            name of plug-in package
     */
    public void runPlugin(String pluginPackage) {
        Intent call = new Intent(PLUGINS_LIST);
        call.setClassName(pluginPackage,
                pluginPackage.concat(".PluginActivity"));
        mContext.startActivity(call);
    }

    /**
     * Scans device for installed plugins
     */
    public PluginManager scan() {
        mList.clear();

        Intent intent = new Intent(PLUGINS_LIST);
        List<ResolveInfo> list = mContext.getPackageManager()
                .queryIntentActivities(intent, 0);
        String title;
        String pluginPackage;
        Drawable icon;
        String appName;
        
        for (ResolveInfo item : list) {
            pluginPackage = item.activityInfo.packageName;
            
            try {
                Resources res = mContext.getPackageManager().getResourcesForApplication(pluginPackage);
                int id = res.getIdentifier("app_title", "string", pluginPackage);
                
                appName = item.loadLabel(mContext.getPackageManager()).toString();
                icon = item.loadIcon(mContext.getPackageManager());
                title = res.getString(id);
                mList.add(createMenuItem(title, pluginPackage, icon, appName));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return this;
    }
}
