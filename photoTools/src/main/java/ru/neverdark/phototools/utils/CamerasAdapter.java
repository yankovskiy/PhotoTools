package ru.neverdark.phototools.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.ui.ImageOnTouchListener;
import ru.neverdark.phototools.utils.dofcalculator.CameraData;

public class CamerasAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final CameraData.Vendor mVendor;
    private final int mResource;
    private final OnButtonsClickListener mCallback;

    public interface OnButtonsClickListener {
        public void onCopyClick(String camera);

        public void onRemoveClick(String camera);

        public void onEditClick(String camera);
    }

    private static class RowHolder {
        TextView mCameraModel;
        ImageView mActionButton;
        ImageView mEditButton;
    }

    public CamerasAdapter(Context context, CameraData.Vendor vendor, OnButtonsClickListener callback) {
        super(context, R.layout.camera_list_item,
                new ArrayList<>(Arrays.asList(CameraData.getCameraByVendor(vendor, context)))
        );
        mContext = context;
        mVendor = vendor;
        mResource = R.layout.camera_list_item;
        mCallback = callback;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RowHolder holder = null;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResource, parent, false);
            holder = new RowHolder();
            holder.mCameraModel = (TextView) row.findViewById(R.id.camera_list_item_model);
            holder.mActionButton = (ImageView) row.findViewById(R.id.camera_list_item_button);
            holder.mEditButton = (ImageView) row.findViewById(R.id.camera_list_edit_button);
            row.setTag(holder);
        } else {
            holder = (RowHolder) row.getTag();
        }

        int drawableResource = R.drawable.ic_content_copy_white_24dp;
        String camera = getItem(position);
        if (mVendor == CameraData.Vendor.USER) {
            drawableResource = R.drawable.ic_delete_white_24dp;
            holder.mActionButton.setOnClickListener(new CameraClickListener(camera, Constants.REMOVE_CAMERA));
            holder.mEditButton.setVisibility(View.VISIBLE);
            holder.mEditButton.setOnClickListener(new CameraClickListener(camera, Constants.EDIT_CAMERA));
            holder.mEditButton.setOnTouchListener(new ImageOnTouchListener());
        } else {
            holder.mActionButton.setOnClickListener(new CameraClickListener(camera, Constants.COPY_CAMERA));
            holder.mEditButton.setVisibility(View.GONE);
        }

        Drawable buttonImage = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            buttonImage = mContext.getResources().getDrawable(drawableResource);
        } else {
            buttonImage = mContext.getDrawable(drawableResource);
        }
        holder.mActionButton.setImageDrawable(buttonImage);
        holder.mCameraModel.setText(camera);
        holder.mActionButton.setOnTouchListener(new ImageOnTouchListener());
        return row;
    }

    private class CameraClickListener implements View.OnClickListener {
        private final String mCamera;
        private final int mAction;

        public CameraClickListener(String camera, int action) {
            this.mCamera = camera;
            this.mAction = action;
        }

        @Override
        public void onClick(View view) {
            if (mAction == Constants.COPY_CAMERA) {
                mCallback.onCopyClick(mCamera);
            } else if (mAction == Constants.REMOVE_CAMERA) {
                mCallback.onRemoveClick(mCamera);
            } else if (mAction == Constants.EDIT_CAMERA) {
                mCallback.onEditClick(mCamera);
            }
        }
    }
}
