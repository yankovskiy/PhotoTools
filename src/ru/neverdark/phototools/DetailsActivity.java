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
package ru.neverdark.phototools;

import ru.neverdark.phototools.fragments.AboutFragment;
import ru.neverdark.phototools.fragments.DofFragment;
import ru.neverdark.phototools.fragments.EvpairsFragment;
import ru.neverdark.phototools.fragments.SunsetFragment;
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
        Log.variable("index", String.valueOf(index));
        switch (index) {
        case Constants.DOF_CHOICE:
            DofFragment dofFragment = new DofFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, dofFragment).commit();
            break;
        case Constants.EV_CHOICE:
            EvpairsFragment evFragment = new EvpairsFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, evFragment).commit();
            break;
        case Constants.SUNSET_CHOICE:
            SunsetFragment sunsetFragment = new SunsetFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, sunsetFragment).commit();
            break;
        case Constants.ABOUT_CHOICE:
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, aboutFragment).commit();
            break;
        }

    }
}
