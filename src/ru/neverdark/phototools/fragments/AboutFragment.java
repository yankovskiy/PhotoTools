package ru.neverdark.phototools.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.log.Log;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Contains About application fragment
 */
public class AboutFragment extends SherlockFragment {

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_about, container, false);
 

        /* For clickable links */
        TextView labelAuthor = (TextView) view.findViewById(R.id.about_label_author);
        labelAuthor.setText(Html
                .fromHtml(getString(R.string.about_label_author)));
        labelAuthor.setMovementMethod(LinkMovementMethod.getInstance());

        TextView labelLicense = (TextView) view.findViewById(R.id.about_label_license);
        labelLicense.setText(Html
                .fromHtml(getString(R.string.about_label_license)));
        labelLicense.setMovementMethod(LinkMovementMethod.getInstance());

        TextView labelVersion = (TextView) view.findViewById(R.id.about_label_version);
        String version = getString(R.string.about_label_version);
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version += " "+ packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.message("NameNotFoundException appeared");
        }          
        labelVersion.setText(version);
        
        return view;
    }
}
