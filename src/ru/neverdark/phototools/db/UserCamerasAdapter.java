/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
package ru.neverdark.phototools.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.ui.ImageOnTouchListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ArrayAdapter for "user_cameras" table
 */
public class UserCamerasAdapter extends ArrayAdapter<UserCamerasRecord> {
    /**
     * Click listener for "Edit" button
     */
    private class EditClickListener implements OnClickListener {
        private UserCamerasRecord mRecord;

        /**
         * Constructor
         * 
         * @param record
         *            record for edit
         */
        public EditClickListener(UserCamerasRecord record) {
            mRecord = record;
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onEditHandler(mRecord);
            }
        }
    }

    /**
     * Interface provide callback methods called for handling "Edit" and
     * "Remove" buttons on the list
     */
    public interface OnEditAndRemoveListener {
        /**
         * Called when user press "Edit" button
         * 
         * @param record
         *            for edition
         */
        public void onEditHandler(UserCamerasRecord record);

        /**
         * Called when user press "Remove" button
         * 
         * @param record
         *            for remove
         */
        public void onRemoveHandler(UserCamerasRecord record);
    }

    /**
     * Click listener for "Remove" button
     */
    private class RemoveClickListener implements OnClickListener {
        private UserCamerasRecord mRecord;

        /**
         * Constructor
         * 
         * @param record
         *            record for remove
         */
        public RemoveClickListener(UserCamerasRecord record) {
            mRecord = record;
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onRemoveHandler(mRecord);
            }
        }
    }

    /**
     * Single row in the list
     */
    private static class RowHolder {
        private TextView mCameraCoc;
        private TextView mCameraModel;
        private TextView mCameraResolution;
        private TextView mCameraSensor;
        private ImageView mEditButton;
        private ImageView mRemoveButton;
    }
    private OnEditAndRemoveListener mCallback;
    private Context mContext;
    private DbAdapter mDbAdapter;
    private List<UserCamerasRecord> mObjects;

    private int mResource;

    /**
     * Constructor
     * 
     * @param context
     *            application context
     */
    public UserCamerasAdapter(Context context) {
        this(context, R.layout.user_camera_row,
                new ArrayList<UserCamerasRecord>());
    }

    /**
     * Constructor
     * 
     * @param context
     *            application context
     * @param resource
     *            resource ID for a layout file containing a TextView to use
     *            when instantiating views.
     * @param list
     *            objects to represent in the ListView
     */
    private UserCamerasAdapter(Context context, int resource,
            List<UserCamerasRecord> list) {
        super(context, resource, list);
        mObjects = list;
        mResource = resource;
        mContext = context;
        mDbAdapter = new DbAdapter(mContext);
    }

    /**
     * Closes database
     */
    public void closeDb() {
        mDbAdapter.close();
    }

    /**
     * Creates camera
     * 
     * @param record
     *            a record contains data for new camera
     */
    public void createCamera(UserCamerasRecord record) {
        mDbAdapter.getUserCameras().createCamera(record.getCameraName(),
                record.getResolutionWidth(), record.getResolutionHeight(),
                record.getSensorWidth(), record.getSensorHeight(),
                record.getCoc(), record.isCustomCoc());
        mDbAdapter.getUserCameras().fetchAllCameras(mObjects);

        notifyDataSetChanged();
    }

    /**
     * Deletes camera
     * 
     * @param record
     *            record for remove
     */
    public void deleteCamera(UserCamerasRecord record) {
        long recordId = record.getRecordId();

        remove(record);
        mDbAdapter.getUserCameras().deleteCamera(recordId);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResource, parent, false);

            holder = new RowHolder();
            holder.mCameraCoc = (TextView) row
                    .findViewById(R.id.userCamera_label_coc);
            holder.mCameraModel = (TextView) row
                    .findViewById(R.id.userCamera_label_model);
            holder.mCameraResolution = (TextView) row
                    .findViewById(R.id.userCamera_label_resolution);
            holder.mCameraSensor = (TextView) row
                    .findViewById(R.id.userCamera_label_sensor);
            holder.mEditButton = (ImageView) row
                    .findViewById(R.id.userCamera_edit);
            holder.mRemoveButton = (ImageView) row
                    .findViewById(R.id.userCamera_remove);
            row.setTag(holder);
        } else {
            holder = (RowHolder) row.getTag();
        }

        UserCamerasRecord record = getItem(position);
        String model = record.getCameraName();
        String coc = String.format(Locale.US, "%s: %s",
                mContext.getString(R.string.userCamera_label_coc),
                record.getCoc());
        String resolution = String.format(Locale.US, "%s: %d x %d",
                mContext.getString(R.string.userCameraRow_resolution),
                record.getResolutionWidth(), record.getResolutionHeight());
        String sensor = String.format(Locale.US, "%s: %s x %s",
                mContext.getString(R.string.userCameraRow_sensor),
                record.getSensorWidth(), record.getSensorHeight());

        holder.mCameraCoc.setText(coc);
        holder.mCameraModel.setText(model);
        holder.mCameraResolution.setText(resolution);
        holder.mCameraSensor.setText(sensor);
        holder.mEditButton.setOnClickListener(new EditClickListener(record));
        holder.mRemoveButton
                .setOnClickListener(new RemoveClickListener(record));
        holder.mEditButton.setOnTouchListener(new ImageOnTouchListener());
        holder.mRemoveButton.setOnTouchListener(new ImageOnTouchListener());

        return row;
    }

    /**
     * Checks table for exists camera
     * 
     * @param cameraName
     *            camera name for check
     * @return true if camera exist
     */
    public boolean isCameraExist(String cameraName) {
        return mDbAdapter.getUserCameras().isCameraExist(cameraName);
    }

    /**
     * Checks database state
     * 
     * @return true if database open
     */
    public boolean isDbOpen() {
        return mDbAdapter.isOpen();
    }

    /**
     * Loads data from database to objects
     */
    public void loadData() {
        mDbAdapter.getUserCameras().fetchAllCameras(mObjects);
        notifyDataSetChanged();
    }

    /**
     * Opens database
     */
    public void openDb() {
        mDbAdapter.open();
    }

    /**
     * Sets callback object for handling "Edit" and "Remove" buttons on the list
     * 
     * @param callback
     *            object for handling "Edit" and "Remove" buttons on the list
     */
    public void setCallback(OnEditAndRemoveListener callback) {
        mCallback = callback;
    }

    /**
     * Updates camera information
     * 
     * @param record
     *            a record contains data for update camera
     */
    public void updateCamera(UserCamerasRecord record) {
        mDbAdapter.getUserCameras()
                .updateCamera(record.getRecordId(), record.getCameraName(),
                        record.getResolutionWidth(),
                        record.getResolutionHeight(), record.getSensorWidth(),
                        record.getSensorHeight(), record.getCoc(),
                        record.isCustomCoc());
        mDbAdapter.getUserCameras().fetchAllCameras(mObjects);

        notifyDataSetChanged();
    }
}
