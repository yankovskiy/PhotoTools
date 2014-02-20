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
     * Gets the name of plug-in package
     * @return name of plug-in package
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
     * Checks record as plug-in
     * @return true for record as plug-in
     */
    public boolean isPlugin() {
        return mIsPlugin;
    }

    /**
     * Mark current record as plug-in
     * @param isPlugin true for plug-in record
     */
    public void setIsPlugin(boolean isPlugin) {
        this.mIsPlugin = isPlugin;
    }

    /**
     * Sets the name of plug-in package
     * @param pluginPackage name of plug-in package
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
