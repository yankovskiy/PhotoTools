package ru.neverdark.phototools.fragments;

import ru.neverdark.phototools.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class CameraManagementDialog extends SherlockDialogFragment {
    private class AddCameraClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        }
    }

    private Context mContext;
    private View mView;
    private ImageView mAddButton;
    private ListView mCameraList;
    private EditText mModel;
    private AlertDialog.Builder mAlertDialog;
    public static final String DIALOG_TAG = "cameraManagementDialog";
    
    public static CameraManagementDialog getInstance(Context context) {
        CameraManagementDialog dialog = new CameraManagementDialog();
        dialog.mContext = context;
        return dialog;
    }
    
    private void bindObjectToResource() {
        mView = View.inflate(mContext, R.layout.user_camera_dialog, null);
        mAddButton = (ImageView) mView.findViewById(R.id.userCamera_addButton);
        mCameraList = (ListView) mView.findViewById(R.id.userCamera_list);
        mModel = (EditText) mView.findViewById(R.id.userCamera_model);
    }
    
    private void createDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(mView);
        mAlertDialog.setTitle(R.string.cameraManagementTitle);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindObjectToResource();
        createDialog();
        
        return mAlertDialog.create();
    }
    
    public void setOnClickListener() {
        mAddButton.setOnClickListener(new AddCameraClickListener());
    }
}
