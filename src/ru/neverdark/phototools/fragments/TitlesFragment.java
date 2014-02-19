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

import java.util.ArrayList;
import java.util.List;

import ru.neverdark.phototools.DetailsActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.MainMenuAdapter;
import ru.neverdark.phototools.utils.MainMenuItem;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

/**
 * Fragment for navigation list
 */
public class TitlesFragment extends SherlockListFragment {
    private boolean mDualPane;
    private int mCurrentCheckPosition = 0;
    private SherlockFragmentActivity mActivity;
    private MainMenuAdapter mAdapter;

    /**
     * Opens market detail application page for donate app
     */
    private void gotoDonate() {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri
                .parse("market://details?id=ru.neverdark.phototoolsdonate"));
        startActivity(marketIntent);
    }

    /**
     * Creates main menu item
     * 
     * @param title
     *            item title
     * @param isPlugin
     *            true if item is plugin
     * @param recordId
     *            record id for local list, or 0 (zero) for plugin
     * @param pluginPackage
     *            plugin package name for plugin or null for local list
     * @return menu item
     */
    private MainMenuItem createMainMenuItem(String title, boolean isPlugin,
            byte recordId, String pluginPackage) {
        MainMenuItem item = new MainMenuItem();
        item.setTitle(title);
        item.setIsPlugin(isPlugin);
        item.setRecordId(recordId);
        item.setPluginPackage(pluginPackage);
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
        list.add(createMainMenuItem(getString(R.string.main_button_dofcalc),
                false, Constants.DOF_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_evpairs),
                false, Constants.EV_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_sunset),
                false, Constants.SUNSET_CHOICE, null));

        // second part of the menu
        // loads installed plugin list
        List<MainMenuItem> pluginList = getPluginsList();
        if (pluginList != null) {
            for (MainMenuItem item : pluginList) {
                list.add(item);
            }
        }
        // third part of the menu
        list.add(createMainMenuItem(getString(R.string.main_button_plugins),
                false, Constants.PLUGIN_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_rateMe),
                false, Constants.RATE_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_feedback),
                false, Constants.FEEDBACK_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_donate),
                false, Constants.DONATE_CHOICE, null));
        list.add(createMainMenuItem(getString(R.string.main_button_about),
                false, Constants.ABOUT_CHOICE, null));

        return list;
    }

    /**
     * Gets list of installed plugins
     * 
     * @return list of installed plugins
     */
    private List<MainMenuItem> getPluginsList() {
        // TODO получение списка установленных плагинов
        return null;
    }

    /**
     * Opens market detail application page
     */
    private void gotoMarket() {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri
                .parse("market://details?id=ru.neverdark.phototools"));
        startActivity(marketIntent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onActivityCreated(savedInstanceState);

        mActivity = getSherlockActivity();
        mAdapter = new MainMenuAdapter(mActivity, R.layout.menu_item,
                R.id.menuItem_label_title, buildMainMenuList());

        setListAdapter(mAdapter);

        View detailsFrame = mActivity.findViewById(R.id.main_detailFragment);
        mDualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mCurrentCheckPosition = savedInstanceState.getInt(
                    Constants.CURRENT_CHOICE, 0);
        }

        if (mDualPane) {
            getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            showDetails(mCurrentCheckPosition);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
     * , android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position,
            long id) {
        Log.message("Enter");
        MainMenuItem menuItem = mAdapter.getItem(position);
        if (menuItem.isPlugin()) {
            // TODO: запустить плагин
        } else {
            showDetails(menuItem.getRecordId());
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

        outState.putInt(Constants.CURRENT_CHOICE, mCurrentCheckPosition);
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
        Fragment details = getFragmentManager().findFragmentById(
                R.id.main_detailFragment);

        switch (index) {
        case Constants.DOF_CHOICE:
            if ((details instanceof DofFragment) == false) {
                details = new DofFragment();
                isOperationNeed = true;
                mActivity
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            break;
        case Constants.EV_CHOICE:
            if ((details instanceof EvpairsFragment) == false) {
                details = new EvpairsFragment();
                isOperationNeed = true;
                mActivity
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            break;
        case Constants.SUNSET_CHOICE:
            if ((details instanceof SunsetFragment) == false) {
                details = new SunsetFragment();
                isOperationNeed = true;
                mActivity
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            break;
        case Constants.ABOUT_CHOICE:
            if ((details instanceof AboutFragment) == false) {
                details = new AboutFragment();
                isOperationNeed = true;
                mActivity
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            break;
        }

        if (isOperationNeed == true) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_detailFragment, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
     * Sets current position for navigation list
     * 
     * @param index
     */
    private void setCurentCheckPosition(int index) {
        mCurrentCheckPosition = index;
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
        intent.setClass(getActivity(), DetailsActivity.class);
        intent.putExtra(Constants.SHOWN_INDEX, index);
        startActivity(intent);
    }

    /**
     * Shows fragment if application runned on the Tablet or activity for other
     * case
     */
    private void showDetails(int index) {
        Log.message("Enter");

        if (index == Constants.RATE_CHOICE) {
            gotoMarket();
        } else if (index == Constants.DONATE_CHOICE) {
            gotoDonate();
        } else if (index == Constants.FEEDBACK_CHOICE) {
            sendEmail();
        } else {
            if (mDualPane == true) {
                getListView().setItemChecked(index, true);
                replaceFragment(index);
            } else {
                showActivity(index);
            }

            setCurentCheckPosition(index);
        }
    }

}
