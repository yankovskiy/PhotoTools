package ru.neverdark.phototools.fragments;

import ru.neverdark.phototools.Constants;
import ru.neverdark.phototools.DetailsActivity;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.log.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;

public class TitlesFragment extends SherlockListFragment {
    boolean mDualPane;
    int mCurrentCheckPosition = 0;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onActivityCreated(savedInstanceState);
        final String[] TITLES = getResources().getStringArray(R.array.main_menuTitles);
        
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.menu_item,
                R.id.menuItem_label_title,
                TITLES));
        
        View detailsFrame = getActivity().findViewById(R.id.main_detailFragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        
        if (savedInstanceState != null) {
            mCurrentCheckPosition = savedInstanceState.getInt(Constants.CURRENT_CHOICE, 0);
        }
        
        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurrentCheckPosition);
        }
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.message("Enter");
        super.onSaveInstanceState(outState);
        
        outState.putInt(Constants.CURRENT_CHOICE, mCurrentCheckPosition);
    }
    
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Log.message("Enter");
        showDetails(position);
    }
    
    private void showDetails(int index) {
        Log.message("Enter");
        mCurrentCheckPosition = index;

        if (mDualPane == true) {
            getListView().setItemChecked(index, true);
            showFragment(index);
        } else {
            showActivity(index);
        }
    }
    
    private void showFragment(int index) {
        Log.message("Enter");
/*
        DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.main_detailFragment);
        if (details == null || details.getShownIndex() != index) {
            details = DetailsFragment.newInstance(index);
            
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_detailFragment, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
*/
    }
    
    private void showActivity(int index) {
        Log.message("Enter");
        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailsActivity.class);
        intent.putExtra(Constants.SHOWN_INDEX, index);
        startActivity(intent);
    }
    
}
