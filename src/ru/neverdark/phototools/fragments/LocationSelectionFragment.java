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
import ru.neverdark.phototools.db.LocationsDbAdapter;
import ru.neverdark.phototools.utils.LocationAdapter;
import ru.neverdark.phototools.utils.LocationAdapter.LocationImageChangeListener;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class LocationSelectionFragment extends SherlockDialogFragment implements
        LocationImageChangeListener {

    private View mView;
    private ListView mListView;
    private ArrayList<LocationRecord> mArrayList;
    private LocationsDbAdapter mDbAdapter;
    private Cursor mCursor;
    private LocationAdapter mAdapter;

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

        if (mArrayList == null) {
            mArrayList = new ArrayList<LocationRecord>();
        } else {
            mArrayList.clear();
        }

        loadStaticData();
        loadDynamicData();

        if (mAdapter == null) {
            mAdapter = new LocationAdapter(mView.getContext(),
                    R.layout.location_row, mArrayList, this);
        }

        mListView.setAdapter(mAdapter);
    }

    /**
     * Loads dynamic data from database to the ListView
     */
    private void loadDynamicData() {
        Log.message("Enter");

        mCursor = mDbAdapter.fetchAllLocations();
        final int KEY_ROWID = mCursor
                .getColumnIndex(LocationsDbAdapter.KEY_ROWID);
        final int KEY_LOCATION_NAME = mCursor
                .getColumnIndex(LocationsDbAdapter.KEY_LOCATION_NAME);
        final int KEY_LATITUDE = mCursor
                .getColumnIndex(LocationsDbAdapter.KEY_LATITUDE);
        final int KEY_LONGITUDE = mCursor
                .getColumnIndex(LocationsDbAdapter.KEY_LONGITUDE);

        while (mCursor.moveToNext()) {
            LocationRecord dbRecord = new LocationRecord();
            dbRecord._id = mCursor.getLong(KEY_ROWID);
            dbRecord.locationName = mCursor.getString(KEY_LOCATION_NAME);
            dbRecord.latitude = mCursor.getDouble(KEY_LATITUDE);
            dbRecord.longitude = mCursor.getDouble(KEY_LONGITUDE);
            mArrayList.add(dbRecord);
            dbRecord = null;
        }

        mCursor.close();
    }

    /**
     * Loads static data to the ListView
     */
    private void loadStaticData() {
        Log.message("Enter");
        mArrayList.add(new LocationRecord(0,
                getString(R.string.locationSelection_label_currentLocation), 0,
                0));
        mArrayList.add(new LocationRecord(1,
                getString(R.string.locationSelection_label_pointOnMap), 0, 0));
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

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.message("Enter");
        if (mDbAdapter == null) {
            mDbAdapter = new LocationsDbAdapter(mView.getContext());
        }

        mDbAdapter.open();

        fillData();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.message("Enter");

        if (mDbAdapter != null) {
            mDbAdapter.close();
        }
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.neverdark.phototools.utils.LocationAdapter.LocationImageChangeListener
     * #onLocationImageEdit(int)
     */
    @Override
    public void onLocationImageEdit(int position) {
        Log.message("Enter");
        Log.variable("position", String.valueOf(position));

    }

    /* (non-Javadoc)
     * @see ru.neverdark.phototools.utils.LocationAdapter.LocationImageChangeListener#onLocationImageRemove(int)
     */
    public void onLocationImageRemove(int position) {
        Log.message("Enter");
        Log.variable("position", String.valueOf(position));

        ConfirmDeleteFragment deleteFragment = ConfirmDeleteFragment
                .NewInstance(mAdapter.getItem(position).locationName, position);
        deleteFragment
                .setTargetFragment(this, Constants.DELETE_DIALOG_FRAGMENT);
        deleteFragment.show(getFragmentManager(), Constants.DELETE_DIALOG);
    }

    /**
     * Delete selected location from database and listView
     * @param position location position for delete
     */
    public void deleteLocation(final int position) {
        Log.message("Enter");
        Log.variable("Position", String.valueOf(position));

        LocationRecord record = mAdapter.getItem(position);

        mDbAdapter.deleteLocation(record._id);
        mAdapter.remove(record);
        mAdapter.notifyDataSetChanged();

    }
}
