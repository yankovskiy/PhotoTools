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
package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.neverdark.phototools.BuildConfig;
import ru.neverdark.phototools.DetailsActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Common;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.MainMenuAdapter;
import ru.neverdark.phototools.utils.MainMenuItem;
import ru.neverdark.phototools.utils.PluginManager;

/**
 * Fragment for navigation list
 */
public class TitlesFragment extends Fragment {
    private boolean mDualPane;
    private int mCurrentRecordId = 0;
    private Context mContext;
    private MainMenuAdapter mAdapter;
    private ListView mList;

    /**
     * Creates main menu item
     * 
     * @param title
     *            item title
     * @param recordId
     *            record id for local list, or 0 (zero) for plug-in
     * @return menu item
     */
    private MainMenuItem createMainMenuItem(String title, byte recordId) {
        MainMenuItem item = new MainMenuItem();
        item.setTitle(title);
        item.setIsPlugin(false);
        item.setRecordId(recordId);
        return item;
    }

    /**
     * Builds list for main menu
     * 
     * @return list
     */
    private List<MainMenuItem> buildMainMenuList() {

        List<MainMenuItem> list = new ArrayList<MainMenuItem>();

        // first part of the menu
        list.add(createMainMenuItem(getString(R.string.main_button_dofcalc), Constants.DOF_CHOICE));
        list.add(createMainMenuItem(getString(R.string.main_button_evpairs), Constants.EV_CHOICE));
        list.add(createMainMenuItem(getString(R.string.main_button_sunset), Constants.SUNSET_CHOICE));

        // second part of the menu
        // loads installed plugin list
        PluginManager.getInstance(mContext).scan();

        for (MainMenuItem item : getPluginsList()) {
            list.add(item);
        }

        // third part of the menu
        list.add(createMainMenuItem(getString(R.string.main_button_plugins),
                Constants.PLUGIN_CHOICE));
        list.add(createMainMenuItem(getString(R.string.main_button_rateMe), Constants.RATE_CHOICE));
        list.add(createMainMenuItem(getString(R.string.main_button_feedback),
                Constants.FEEDBACK_CHOICE));

        
        if (Constants.PAID == false) {
            list.add(createMainMenuItem(getString(R.string.main_button_donate),
                    Constants.DONATE_CHOICE));
        }

        list.add(createMainMenuItem(getString(R.string.main_button_about), Constants.ABOUT_CHOICE));

        return list;
    }

    /**
     * Gets list of installed plug-in
     * 
     * @return list of installed plug-in
     */
    private List<MainMenuItem> getPluginsList() {
        return PluginManager.getInstance(mContext).getMenuItems();
    }

    /**
     * Opens market detail application page
     */
    private void gotoMarket() {
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Goto market", Toast.LENGTH_LONG).show();
        } else {
            String url = "market://details?id=".concat(mContext.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse(url));
            startActivity(marketIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.titles_fragment, container, false);
        mList = (ListView) view.findViewById(R.id.titles_list);
        mList.setOnItemClickListener(new TitleClickListener());

        mContext = view.getContext();

        if (savedInstanceState != null) {
            mCurrentRecordId = savedInstanceState.getInt(Constants.CURRENT_CHOICE, 0);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailsFrame = getActivity().findViewById(R.id.main_detailFragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        Log.variable("mDualPane", String.valueOf(detailsFrame == null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        Log.enter();
        super.onResume();

        rebuildList();
        if (mDualPane) {
            mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            int position = findPositionById(mCurrentRecordId);
            showDetails(position, mCurrentRecordId);
        }
    }

    /**
     * Rebuild menu list
     */
    private void rebuildList() {
        if (mAdapter != null) {
            mAdapter.clear();
        }

        mAdapter = new MainMenuAdapter(mContext, R.layout.menu_item, R.id.menuItem_label_title,
                buildMainMenuList());

        mList.setAdapter(mAdapter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onStop() {
        Log.enter();
        super.onStop();

        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.CURRENT_CHOICE, mCurrentRecordId);
    }

    /**
     * Replace current fragment to other
     * 
     * @param index
     *            index fragment
     */
    private void replaceFragment(int index) {
        Log.message("Enter");
        boolean isOperationNeed = false;

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Fragment details = getFragmentManager().findFragmentById(R.id.main_detailFragment);

        switch (index) {
        case Constants.DOF_CHOICE:
            if ((details instanceof DofFragment) == false) {
                details = new DofFragment();
                isOperationNeed = true;
            }
            break;
        case Constants.EV_CHOICE:
            if ((details instanceof EvpairsFragment) == false) {
                details = new EvpairsFragment();
                isOperationNeed = true;
            }
            break;
        case Constants.SUNSET_CHOICE:
            if ((details instanceof SunsetFragment) == false) {
                details = new SunsetFragment();
                isOperationNeed = true;
            }
            break;
        case Constants.ABOUT_CHOICE:
            if ((details instanceof AboutFragment) == false) {
                details = new AboutFragment();
                isOperationNeed = true;
            }
            break;
        case Constants.PLUGIN_CHOICE:
            if ((details instanceof PluginsFragment) == false) {
                details = new PluginsFragment();
                isOperationNeed = true;
            }
            break;
        }

        if (isOperationNeed == true) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_detailFragment, details);
            ft.commit();
        }
    }

    /**
     * Sends email to application author
     */
    private void sendEmail() {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("plain/text");
        mailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { getString(R.string.titles_emailAuthor) });
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        startActivity(Intent.createChooser(mailIntent,
                getString(R.string.titles_selectEmailApplication)));
    }

    /**
     * Sets current record id for menu list
     * 
     * @param recordId
     */
    private void setCurentRecordId(int recordId) {
        mCurrentRecordId = recordId;
    }

    /**
     * Shows activity by index
     * 
     * @param index
     *            activity index for shown
     */
    private void showActivity(int index) {
        Log.message("Enter");
        Intent intent = new Intent();
        intent.setClass(mContext, DetailsActivity.class);
        intent.putExtra(Constants.SHOWN_INDEX, index);
        startActivity(intent);
    }

    /**
     * Shows fragment if application runned on the Tablet or activity for other
     * case
     * 
     * @param index
     *            index selected menu item
     * @param recordId
     *            record id for selected item
     */
    private void showDetails(int index, int recordId) {
        Log.message("Enter");

        if (recordId == Constants.RATE_CHOICE) {
            gotoMarket();
        } else if (recordId == Constants.DONATE_CHOICE) {
            Common.gotoDonate(mContext);
        } else if (recordId == Constants.FEEDBACK_CHOICE) {
            sendEmail();
        } else {
            if (mDualPane == true) {
                mList.setItemChecked(index, true);
                replaceFragment(recordId);
            } else {
                showActivity(recordId);
            }

            setCurentRecordId(recordId);
        }
    }

    /**
     * Finds item position in the menu list by id
     * 
     * @param recordId
     *            record id
     * @return item position
     */
    private int findPositionById(int recordId) {
        for (int position = 0; position < mAdapter.getCount(); position++) {
            if (mAdapter.getItem(position).getRecordId() == recordId) {
                return position;
            }
        }

        return 0;
    }


    private class TitleClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MainMenuItem menuItem = mAdapter.getItem(i);
            if (menuItem.isPlugin()) {
                PluginManager.getInstance(mContext).runPlugin(menuItem.getPluginPackage());
            } else {
                showDetails(i, menuItem.getRecordId());
            }
        }
    }
}
