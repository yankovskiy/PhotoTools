package ru.neverdark.phototools;

import ru.neverdark.phototools.fragments.AboutFragment;
import ru.neverdark.phototools.fragments.DofFragment;
import ru.neverdark.phototools.fragments.EvpairsFragment;
import ru.neverdark.phototools.log.Log;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Activity for showing fragments: DoF, EV Pairs, About
 */
public class DetailsActivity extends SherlockFragmentActivity {
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
            showFragment();
        }
    }
    
    /**
     * Shows fragment by index delivered from MainActivity
     */
    private void showFragment() {
        Log.message("Enter");
        int index = getIntent().getIntExtra(Constants.SHOWN_INDEX, 0);
        Log.variable("index = ", String.valueOf(index));
        switch (index) {
        case Constants.DOF_CHOICE:
            DofFragment dofFragment = new DofFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, dofFragment).commit();
            break;
        case Constants.EV_CHOICE:
            EvpairsFragment evFragment = new EvpairsFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, evFragment).commit();
            break;
        case Constants.ABOUT_CHOICE:
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, aboutFragment).commit();
            break;
        }

    }
}
