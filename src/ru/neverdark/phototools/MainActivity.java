package ru.neverdark.phototools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {    	    	
    	Intent intent;
    	switch(v.getId()) {
    	case R.id.button_about:
    		intent = new Intent(this, AboutActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.button_dofcalc:
    		intent = new Intent(this, DofActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.button_evpairs:
    		intent = new Intent(this, EvpairsActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.button_settings:
    		intent = new Intent(this, SettingsActivity.class);
    		startActivity(intent);
    	}    	    	
    }   
}
