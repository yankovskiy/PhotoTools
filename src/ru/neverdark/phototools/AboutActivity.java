package ru.neverdark.phototools;

import ru.neverdark.phototools.log.Log;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AboutActivity extends Activity {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /* For clickable links */
        TextView labelAuthor = (TextView) findViewById(R.id.about_label_author);
        labelAuthor.setText(Html
                .fromHtml(getString(R.string.about_label_author)));
        labelAuthor.setMovementMethod(LinkMovementMethod.getInstance());

        TextView labelLicense = (TextView) findViewById(R.id.about_label_license);
        labelLicense.setText(Html
                .fromHtml(getString(R.string.about_label_license)));
        labelLicense.setMovementMethod(LinkMovementMethod.getInstance());

        TextView labelVersion = (TextView) findViewById(R.id.about_label_version);
        String version = getString(R.string.about_label_version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version += " "+ packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.message("NameNotFoundException appeared");
        }          
        labelVersion.setText(version);
    }
}
