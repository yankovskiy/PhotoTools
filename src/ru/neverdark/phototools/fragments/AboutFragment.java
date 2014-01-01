/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import com.actionbarsherlock.app.SherlockFragment;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Log;
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

        TextView labelLicense = (TextView) view.findViewById(R.id.about_label_licenseInformation);
        labelLicense.setText(Html
                .fromHtml(getString(R.string.about_label_licenseInformation)));
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
