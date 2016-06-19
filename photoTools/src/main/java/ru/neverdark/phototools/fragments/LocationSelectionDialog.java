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

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.LocationsTable;
import ru.neverdark.phototools.fragments.DeleteConfirmationDialog.OnDeleteConfirmationListener;
import ru.neverdark.phototools.utils.LocationAdapter;
import ru.neverdark.phototools.utils.LocationAdapter.OnLocationChangeListener;
import ru.neverdark.phototools.utils.LocationRecord;
import ru.neverdark.phototools.utils.Log;

public class LocationSelectionDialog extends UfoDialogFragment {

    public static final String DIALOG_TAG = "locationSelectionDialog";
    private ListView mLocationsList;
    private ArrayList<LocationRecord> mArrayList;
    private DbAdapter mDbAdapter;
    private LocationAdapter mAdapter;

    public static LocationSelectionDialog getInstance(Context context) {
        LocationSelectionDialog dialog = new LocationSelectionDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.location_selection_dialog, null));
        mLocationsList = (ListView) getDialogView().findViewById(R.id.locationSelection_listView);
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.locationSelection_label_selectLocation);
    }

    /**
     * Fills list view
     */
    private void fillData() {
        Log.message("Enter");

        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        } else {
            mArrayList.clear();
        }

        loadStaticData();
        loadDynamicData();

        if (mAdapter == null) {
            mAdapter = new LocationAdapter(getContext(), R.layout.location_row, mArrayList,
                    new LocationChangeListener());
        }

        mLocationsList.setAdapter(mAdapter);
    }

    /**
     * Loads dynamic data from database to the ListView
     */
    private void loadDynamicData() {
        Log.message("Enter");

        Cursor cursor = mDbAdapter.getLocations().fetchAllLocations();
        final int KEY_ROWID = cursor.getColumnIndex(LocationsTable.KEY_ROWID);
        final int KEY_LOCATION_NAME = cursor.getColumnIndex(LocationsTable.KEY_LOCATION_NAME);
        final int KEY_LATITUDE = cursor.getColumnIndex(LocationsTable.KEY_LATITUDE);
        final int KEY_LONGITUDE = cursor.getColumnIndex(LocationsTable.KEY_LONGITUDE);
        final int KEY_CAMERA_ZOOM = cursor.getColumnIndex(LocationsTable.KEY_CAMERA_ZOOM);
        final int KEY_MAP_TYPE = cursor.getColumnIndex(LocationsTable.KEY_MAP_TYPE);

        while (cursor.moveToNext()) {
            LocationRecord dbRecord = new LocationRecord();
            dbRecord._id = cursor.getLong(KEY_ROWID);
            dbRecord.locationName = cursor.getString(KEY_LOCATION_NAME);
            dbRecord.latitude = cursor.getDouble(KEY_LATITUDE);
            dbRecord.longitude = cursor.getDouble(KEY_LONGITUDE);
            dbRecord.cameraZoom = cursor.getFloat(KEY_CAMERA_ZOOM);
            dbRecord.mapType = cursor.getInt(KEY_MAP_TYPE);
            mArrayList.add(dbRecord);
        }

        cursor.close();
    }

    /**
     * Loads static data to the ListView
     */
    private void loadStaticData() {
        Log.message("Enter");
        mArrayList.add(new LocationRecord(0,
                getString(R.string.locationSelection_label_currentLocation), 0, 0));
        mArrayList.add(new LocationRecord(1,
                getString(R.string.locationSelection_label_pointOnMap), 0, 0));
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
            mDbAdapter = new DbAdapter(getContext());
        }

        mDbAdapter.open();

        fillData();

    }

    @Override
    public void setListeners() {
        mLocationsList.setOnItemClickListener(new LocationClickListener());
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
    }

    public interface OnLocationListener {
        void onEditLocationHandler(LocationRecord record);

        void onSelectLocationHandler(LocationRecord record);
    }

    /**
     * Implements listener for "ok" button on the delete confirmation dialog
     */
    private class DeleteConfirmationListener implements OnDeleteConfirmationListener, OnCallback {
        @Override
        public void onDeleteConfirmationHandler(Object deleteRecord) {
            LocationRecord record = (LocationRecord) deleteRecord;

            mDbAdapter.getLocations().deleteLocation(record._id);
            mAdapter.remove(record);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class LocationChangeListener implements OnLocationChangeListener {

        @Override
        public void onLocationEditHandler(int position) {
            Log.message("Enter");
            Log.variable("position", String.valueOf(position));
            getDialog().dismiss();

            LocationRecord record = mAdapter.getItem(position);

            OnLocationListener callback = (OnLocationListener) getCallback();
            if (callback != null) {
                callback.onEditLocationHandler(record);
            }
        }

        @Override
        public void onLocationRemoveHandler(int position) {
            Log.message("Enter");

            DeleteConfirmationDialog dialog = DeleteConfirmationDialog.getInstance(getContext());
            dialog.setCallback(new DeleteConfirmationListener());
            dialog.setObjectForDelete(mAdapter.getItem(position));
            dialog.show(getFragmentManager(), DeleteConfirmationDialog.DIALOG_TAG);
        }

    }

    private class LocationClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
            Log.message("Enter");
            getDialog().dismiss();

            LocationRecord record = mAdapter.getItem(position);

            mDbAdapter.getLocations().udateLastAccessTime(record._id);

            OnLocationListener callback = (OnLocationListener) getCallback();
            if (callback != null) {
                callback.onSelectLocationHandler(record);
            }
        }

    }
}
