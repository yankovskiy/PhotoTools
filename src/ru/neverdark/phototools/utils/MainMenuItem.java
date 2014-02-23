package ru.neverdark.phototools.utils;

import android.graphics.drawable.Drawable;

/**
 * Class contains one record for main menu
 */
public class MainMenuItem {
    private Drawable mIcon;
    private boolean mIsPlugin;
    private String mPluginPackage;
    private byte mRecordId;
    private String mTitle;
    private String mAppName;

    /**
     * Constructor
     */
    public MainMenuItem() {
        setIsPlugin(false);
    }

    /**
     * Gets icon for plug-in
     * 
     * @return icon
     */
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Gets the name of plug-in package
     * 
     * @return name of plug-in package
     */
    public String getPluginPackage() {
        return mPluginPackage;
    }

    /**
     * Gets record id
     * 
     * @return record id
     */
    public byte getRecordId() {
        return mRecordId;
    }

    /**
     * Gets record title
     * 
     * @return record title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Checks record as plug-in
     * 
     * @return true for record as plug-in
     */
    public boolean isPlugin() {
        return mIsPlugin;
    }

    /**
     * Sets icon for plug-in
     * 
     * @param icon
     *            for plug-in
     */
    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    /**
     * Mark current record as plug-in
     * 
     * @param isPlugin
     *            true for plug-in record
     */
    public void setIsPlugin(boolean isPlugin) {
        this.mIsPlugin = isPlugin;
    }

    /**
     * Sets the name of plug-in package
     * 
     * @param pluginPackage
     *            name of plug-in package
     */
    public void setPluginPackage(String pluginPackage) {
        this.mPluginPackage = pluginPackage;
    }

    /**
     * Sets record id
     * 
     * @param recordId
     *            record id
     */
    public void setRecordId(byte recordId) {
        this.mRecordId = recordId;
    }

    /**
     * Sets title for record
     * 
     * @param title
     *            title
     */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    /**
     * Gets application name
     * @return application name
     */
    public String getAppName() {
        return mAppName;
    }

    /**
     * Sets application name
     * @param appName application name
     */
    public void setAppName(String appName) {
        this.mAppName = appName;
    }
}
