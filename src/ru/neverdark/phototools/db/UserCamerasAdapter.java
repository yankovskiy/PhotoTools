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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserCamerasAdapter extends ArrayAdapter<UserCamerasRecord> {
    public interface OnEditAndRemoveListener {
        public void onEditHandler(UserCamerasRecord record);
        public void onRemoveHandler(UserCamerasRecord record);
    }
    
    private class RemoveClickListener implements OnClickListener {
        private UserCamerasRecord mRecord;

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

    private class EditClickListener implements OnClickListener {
        private UserCamerasRecord mRecord;

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

    private OnEditAndRemoveListener mCallback;
    private Context mContext;
    private List<UserCamerasRecord> mObjects;
    private int mResource;
    private DbAdapter mDbAdapter;

    private static class RowHolder {
        private TextView mCameraModel;
        private TextView mCameraResolution;
        private TextView mCameraSensor;
        private TextView mCameraCoc;
        private ImageView mRemoveButton;
        private ImageView mEditButton;
    }

    public UserCamerasAdapter(Context context) {
        this(context, R.layout.user_camera_row,
                new ArrayList<UserCamerasRecord>());
    }

    private UserCamerasAdapter(Context context, int resource,
            List<UserCamerasRecord> list) {
        super(context, resource, list);
        mObjects = list;
        mResource = resource;
        mContext = context;
        mDbAdapter = new DbAdapter(mContext);
        setNotifyOnChange(true);
    }

    public void openDb() {
        mDbAdapter.open();
    }

    public void closeDb() {
        mDbAdapter.close();
    }

    public void loadData() {
        mDbAdapter.getUserCameras().fetchAllCameras(mObjects);
        // notifyDataSetChanged();
    }

    public void deleteCamera(UserCamerasRecord record) {
        long recordId = record.getRecordId();

        remove(record);
        mDbAdapter.getUserCameras().deleteCamera(recordId);
        // notifyDataSetChanged();
    }

    public boolean isCameraExist(String cameraName) {
        return mDbAdapter.getUserCameras().isCameraExist(cameraName);
    }

    public void createCamera(UserCamerasRecord record) {
        mDbAdapter.getUserCameras().createCamera(record.getCameraName(),
                record.getResolutionWidth(), record.getResolutionHeight(),
                record.getSensorWidth(), record.getSensorHeight(),
                record.getCoc(), record.isCustomCoc());
        add(record);
        // notifyDataSetChanged();
    }

    public void updateCamera(UserCamerasRecord record) {
        mDbAdapter.getUserCameras()
                .updateCamera(record.getRecordId(), record.getCameraName(),
                        record.getResolutionWidth(),
                        record.getResolutionHeight(), record.getSensorWidth(),
                        record.getSensorHeight(), record.getCoc(),
                        record.isCustomCoc());
        mDbAdapter.getUserCameras().fetchAllCameras(mObjects);

        // notifyDataSetChanged();
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
        String coc = mContext.getString(R.string.userCamera_label_coc).concat(
                String.valueOf(record.getCoc()));
        String resolution = String.format(Locale.US, "%s %d %d",
                mContext.getString(R.string.userCamera_label_resolution),
                record.getResolutionWidth(), record.getResolutionHeight());
        String sensor = String.format(Locale.US, "%s %f %f",
                mContext.getString(R.string.userCamera_label_resolution),
                record.getResolutionWidth(), record.getResolutionHeight());

        holder.mCameraCoc.setText(coc);
        holder.mCameraModel.setText(model);
        holder.mCameraResolution.setText(resolution);
        holder.mCameraSensor.setText(sensor);
        holder.mEditButton.setOnClickListener(new EditClickListener(record));
        holder.mRemoveButton.setOnClickListener(new RemoveClickListener(record));

        return row;
    }
    
    public void setCallback(OnEditAndRemoveListener callback) {
        mCallback = callback;
    }
}
