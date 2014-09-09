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

import java.util.ArrayList;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.LocationsTable;
import ru.neverdark.phototools.fragments.DeleteConfirmationDialog.OnDeleteConfirmationListener;
import ru.neverdark.phototools.utils.CancelClickListener;
import ru.neverdark.phototools.utils.LocationAdapter;
import ru.neverdark.phototools.utils.LocationAdapter.LocationImageChangeListener;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class LocationSelectionDialog extends SherlockDialogFragment implements
        LocationImageChangeListener {

    /**
     * Implements listener for "ok" button on the delete confirmation dialog
     */
    private class DeleteConfirmationListener implements
            OnDeleteConfirmationListener {
        @Override
        public void onDeleteConfirmationHandler(Object deleteRecord) {
            LocationRecord record = (LocationRecord) deleteRecord;

            mDbAdapter.getLocations().deleteLocation(record._id);
            mAdapter.remove(record);
            mAdapter.notifyDataSetChanged();
        }
    }

    public static LocationSelectionDialog getInstance(Context context) {
        LocationSelectionDialog dialog = new LocationSelectionDialog();
        dialog.mContext = context;
        return dialog;
    }
    private View mView;
    private ListView mListView;
    private ArrayList<LocationRecord> mArrayList;
    private DbAdapter mDbAdapter;
    private Cursor mCursor;
    private LocationAdapter mAdapter;

    private Context mContext;
    
    public static final String DIALOG_TAG = "locationSelectionDialog";
    
    private AlertDialog.Builder mAlertDialog;
    
    /**
     * Binds classes objects to resources
     */
    private void bindObjectsToResources() {
        mView = View.inflate(mContext, R.layout.activity_location_selection, null);
        mListView = (ListView) mView
                .findViewById(R.id.locationSelection_listView);
    }

    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.locationSelection_label_selectLocation);
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

        mCursor = mDbAdapter.getLocations().fetchAllLocations();
        final int KEY_ROWID = mCursor
                .getColumnIndex(LocationsTable.KEY_ROWID);
        final int KEY_LOCATION_NAME = mCursor
                .getColumnIndex(LocationsTable.KEY_LOCATION_NAME);
        final int KEY_LATITUDE = mCursor
                .getColumnIndex(LocationsTable.KEY_LATITUDE);
        final int KEY_LONGITUDE = mCursor
                .getColumnIndex(LocationsTable.KEY_LONGITUDE);

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
        bindObjectsToResources();
        createDialog();
        setClickListeners();
        
        return mAlertDialog.create();
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
        this.dismiss();

        LocationRecord record = mAdapter.getItem(position);

        ((SunsetFragment) getTargetFragment()).handleEditCustomLocation(record);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.neverdark.phototools.utils.LocationAdapter.LocationImageChangeListener
     * #onLocationImageRemove(int)
     */
    public void onLocationImageRemove(int position) {
        Log.message("Enter");

        DeleteConfirmationDialog dialog = DeleteConfirmationDialog.getInstance(mContext);
        dialog.setCallback(new DeleteConfirmationListener());
        dialog.setObjectForDelete(mAdapter.getItem(position));
        dialog.show(getFragmentManager(), DeleteConfirmationDialog.DIALOG_TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.message("Enter");

        if (mDbAdapter != null) {
            mDbAdapter.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.message("Enter");
        if (mDbAdapter == null) {
            mDbAdapter = new DbAdapter(mView.getContext());
        }

        mDbAdapter.open();

        fillData();

    }

    private void setClickListeners() {
        setOnItemClickListener(this);
        mAlertDialog.setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
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

                LocationRecord record = mAdapter.getItem(position);

                mDbAdapter.getLocations().udateLastAccessTime(record._id);

                ((SunsetFragment) getTargetFragment())
                        .handleLocationSelection(record);

            }
        });
    }
}
