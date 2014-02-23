package ru.neverdark.phototools.fragments;


import java.util.List;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.MainMenuItem;
import ru.neverdark.phototools.utils.PluginAdapter;
import ru.neverdark.phototools.utils.PluginManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragment;

public class PluginsFragment extends SherlockFragment{
    private View mView;
    private ListView mAvailableListView;
    private ListView mInstalledListView;
    private Context mContext;
    
    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_plugins, container, false);
        mContext = getSherlockActivity();
        
        bindObjectsToResources();
        
        buildTabs();
        
        return mView;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadDataToLists();
    }
    
    /**
     * Builds tabs
     */
    private void buildTabs() {
        TabHost mTabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
        
        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("tag1");

        spec.setContent(R.id.plugins_tab_availabe);
        spec.setIndicator(getString(R.string.plugins_available));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec("tag2");
        spec.setContent(R.id.plugins_tab_installed);
        spec.setIndicator(getString(R.string.plugins_installed));
        mTabHost.addTab(spec);
    }
    
    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        mAvailableListView = (ListView) mView.findViewById(R.id.plugins_listView_available);
        mInstalledListView = (ListView) mView.findViewById(R.id.plugins_listView_installed);
    }
    
    /**
     * Loads data to lists
     */
    private void loadDataToLists() {
        List<MainMenuItem> installed = PluginManager.getInstance(mContext).scan().getMenuItems();
        PluginAdapter adapter = new PluginAdapter(mContext, R.layout.plugin_item, installed);
        mInstalledListView.setAdapter(adapter);
    }
}
