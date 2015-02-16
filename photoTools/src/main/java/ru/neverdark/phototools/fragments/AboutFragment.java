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

import ru.neverdark.abs.UfoFragment;
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
public class AboutFragment extends UfoFragment {
    private View mView;
    private TextView mAuthor;
    private TextView mLicense;
    private TextView mVersion;

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_about, container, false);

        /* For clickable links */
        bindObjects();

        mAuthor.setText(Html.fromHtml(getString(R.string.about_label_author)));
        mAuthor.setMovementMethod(LinkMovementMethod.getInstance());
        mLicense.setText(Html.fromHtml(getString(R.string.about_label_licenseInformation)));
        mLicense.setMovementMethod(LinkMovementMethod.getInstance());

        String version = getString(R.string.about_label_version);
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            version += " " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.message("NameNotFoundException appeared");
        }
        mVersion.setText(version);

        return mView;
    }

    @Override
    public void bindObjects() {
        mAuthor = (TextView) mView.findViewById(R.id.about_label_author);
        mLicense = (TextView) mView.findViewById(R.id.about_label_licenseInformation);
        mVersion = (TextView) mView.findViewById(R.id.about_label_version);
    }

    @Override
    public void setListeners() {
        // TODO Auto-generated method stub

    }
}
