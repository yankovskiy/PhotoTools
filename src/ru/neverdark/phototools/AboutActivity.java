package ru.neverdark.phototools;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.app.Activity;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		/* For clickable links */
		TextView labelAuthor = (TextView) findViewById(R.id.about_label_author);
		labelAuthor.setText(Html.fromHtml(getString(R.string.about_label_author)));
		labelAuthor.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView labelLicense = (TextView) findViewById(R.id.about_label_license);
		labelLicense.setText(Html.fromHtml(getString(R.string.about_label_license)));
		labelLicense.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
