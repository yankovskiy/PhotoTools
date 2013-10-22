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
package ru.neverdark.phototools.fragments;

import java.util.ArrayList;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.LocationAdapter;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class LocationSelectionFragment extends SherlockDialogFragment {

    private View mView;
    private ListView mListView;

    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        mListView = (ListView) mView
                .findViewById(R.id.locationSelection_listView);
    }

    /**
     * Fills list view
     */
    private void fillData() {
        Log.message("Enter");

        ArrayList<LocationRecord> arrayList = new ArrayList<LocationRecord>();
        arrayList.add(new LocationRecord(0,
                getString(R.string.locationSelection_label_currentLocation), 0,
                0));
        arrayList.add(new LocationRecord(1,
                getString(R.string.locationSelection_label_pointOnMap), 0, 0));

        LocationAdapter adapter = new LocationAdapter(mView.getContext(),
                R.layout.location_row, arrayList);

        mListView.setAdapter(adapter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.message("Enter");
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(R.string.locationSelection_label_selectLocation);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.message("Enter");
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.activity_location_selection,
                container, false);

        bindObjectsToResources();
        setOnItemClickListener(this);
        fillData();
        return mView;
    }

    /**
     * Sets on item click listener for ListView
     * 
     * @param dialog
     *            dialog object for closing after handling event
     */
    public void setOnItemClickListener(final SherlockDialogFragment dialog) {
        Log.message("Enter");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked,
                    int position, long id) {
                Log.message("Enter");
                dialog.dismiss();
                ((SunsetFragment) getTargetFragment())
                        .handleLocationSelection(position);

            }
        });
    }

}
