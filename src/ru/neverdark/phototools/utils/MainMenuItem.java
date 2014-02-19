package ru.neverdark.phototools.utils;

/**
 * Class contains one record for main menu
 */
public class MainMenuItem {
    private boolean mIsPlugin;
    private String mPluginPackage;
    private byte mRecordId;
    private String mTitle;
    
    /**
     * Constructor
     */
    public MainMenuItem() {
        setIsPlugin(false);
    }

    /**
     * Gets the name of plugin package
     * @return name of plugin package
     */
    public String getPluginPackage() {
        return mPluginPackage;
    }

    /**
     * Gets record id
     * @return record id
     */
    public byte getRecordId() {
        return mRecordId;
    }

    /**
     * Gets record title
     * @return record title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Checks record as plugin
     * @return true for record as plugin
     */
    public boolean isPlugin() {
        return mIsPlugin;
    }

    /**
     * Mark current record as plugin
     * @param isPlugin true for plugin-record
     */
    public void setIsPlugin(boolean isPlugin) {
        this.mIsPlugin = isPlugin;
    }

    /**
     * Sets the name of plugin package
     * @param pluginPackage name of plugin package
     */
    public void setPluginPackage(String pluginPackage) {
        this.mPluginPackage = pluginPackage;
    }

    /**
     * Sets record id
     * @param recordId record id
     */
    public void setRecordId(byte recordId) {
        this.mRecordId = recordId;
    }

    /**
     * Sets title for record
     * @param title title
     */
    public void setTitle(String title) {
        this.mTitle = title;
    }
    
    @Override
    public String toString() {
        return mTitle;
    }
}
