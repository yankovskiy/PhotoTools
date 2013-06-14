package ru.neverdark.phototools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Function called when a view has been clicked.
	 * 
	 * @param v - The view that was clicked.
	 */
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.main_button_about:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.main_button_dofcalc:
			intent = new Intent(this, DofActivity.class);
			startActivity(intent);
			break;
		case R.id.main_button_evpairs:
			intent = new Intent(this, EvpairsActivity.class);
			startActivity(intent);
			break;
		case R.id.main_button_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
	}
}
