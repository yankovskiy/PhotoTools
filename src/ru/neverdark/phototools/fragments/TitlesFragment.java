/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import ru.neverdark.phototools.DetailsActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;

/**
 * Fragment for navigation list
 */
public class TitlesFragment extends SherlockListFragment {
    private boolean mDualPane;
    private int mCurrentCheckPosition = 0;

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
        final String[] TITLES = getResources().getStringArray(
                R.array.main_menuTitles);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.menu_item, R.id.menuItem_label_title, TITLES));

        View detailsFrame = getActivity()
                .findViewById(R.id.main_detailFragment);
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
        showDetails(position);
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
