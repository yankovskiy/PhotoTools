package ru.neverdark.phototools;

import ru.neverdark.phototools.fragments.DofFragment;
import ru.neverdark.phototools.log.Log;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailsActivity extends SherlockFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
            DofFragment details = new DofFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
