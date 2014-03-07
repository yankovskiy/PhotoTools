package ru.neverdark.phototools.fragments;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.db.UserCamerasAdapter;
import ru.neverdark.phototools.db.UserCamerasAdapter.OnEditAndRemoveListener;
import ru.neverdark.phototools.db.UserCamerasRecord;
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
    private class EditAndRemoveListener implements OnEditAndRemoveListener {

        @Override
        public void onEditHandler(UserCamerasRecord record) {
            // TODO Auto-generated method stub
            // отобразить диалог для редактирования записи
        }

        @Override
        public void onRemoveHandler(UserCamerasRecord record) {
            // TODO Auto-generated method stub
            // отобразить диалог для подтверждения удаления записи
        }

    }

    private class AddCameraClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String cameraName = mModel.getText().toString().trim();
            if (cameraName.length() > 0) {
                if (mAdapter.isCameraExist(cameraName)) {
                    // TODO
                    // отобразить диалог о том, что камера уже существует
                } else {
                    // TODO
                    // отобразить диалог для добавления камеры
                }
            }
        }
    }

    private Context mContext;
    private View mView;
    private ImageView mAddButton;
    private ListView mCameraList;
    private EditText mModel;
    private AlertDialog.Builder mAlertDialog;
    public static final String DIALOG_TAG = "cameraManagementDialog";
    private UserCamerasAdapter mAdapter;
    
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
        setOnClickListener();
        
        return mAlertDialog.create();
    }
    
    private void setOnClickListener() {
        mAddButton.setOnClickListener(new AddCameraClickListener());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null) {
            mAdapter = new UserCamerasAdapter(mContext);
            mAdapter.setCallback(new EditAndRemoveListener());
        }
        
        mAdapter.openDb();
        mAdapter.loadData();
        
        mCameraList.setAdapter(mAdapter);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.closeDb();
        }
    }
}
