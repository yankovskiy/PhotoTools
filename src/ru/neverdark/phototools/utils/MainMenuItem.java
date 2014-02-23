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
