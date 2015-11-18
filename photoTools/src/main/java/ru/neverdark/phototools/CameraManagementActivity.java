package ru.neverdark.phototools;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;

import ru.neverdark.abs.OnCallback;
import ru.neverdark.abs.UfoFragment;
import ru.neverdark.abs.UfoFragmentActivity;
import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.UserCamerasRecord;
import ru.neverdark.phototools.fragments.CameraEditorDialog;
import ru.neverdark.phototools.fragments.ConfirmActionDialog;
import ru.neverdark.phototools.fragments.ConfirmActionDialog.OnPositiveClickListener;
import ru.neverdark.phototools.fragments.ShowMessageDialog;
import ru.neverdark.phototools.utils.CamerasAdapter;
import ru.neverdark.phototools.utils.Constants;
import ru.neverdark.phototools.utils.Log;
import ru.neverdark.phototools.utils.dofcalculator.CameraData;

public class CameraManagementActivity extends UfoFragmentActivity {
    private Context mContext;
    private Spinner mVendors;
    private MenuItem mMenuAddCamera;
    private ListView mCamerasList;

    @Override
    public void bindObjects() {
        mVendors = (Spinner) getSupportActionBar().getCustomView().findViewById(R.id.camera_spinner);
        mCamerasList = (ListView) findViewById(R.id.camera_management_list);
    }

    @Override
    public void setListeners() {
        mVendors.setOnItemSelectedListener(new CamerasSelectedListener());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setTheme(R.style.Theme_AppCompat);
        setContentView(R.layout.camera_management_activity);
        getSupportActionBar().setCustomView(R.layout.camera_spinner);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);
        bindObjects();
        loadData();
        setListeners();
    }

    private void loadData() {
        ArrayAdapter<CameraData.Vendor> vendors = new ArrayAdapter<CameraData.Vendor>(mContext, android.R.layout.simple_spinner_item, CameraData.Vendor.values());
        mVendors.setAdapter(vendors);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.camera_management_add:
                CameraEditorDialog dialog = CameraEditorDialog
                        .getInstance(mContext);
                dialog.setCallback(new CameraEditorListener());
                dialog.setActionType(CameraEditorDialog.ACTION_ADD);
                dialog.show(getSupportFragmentManager(), CameraEditorDialog.DIALOG_TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private CamerasAdapter mAdapter;

    private class CamerasSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            updateCameraList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void updateCameraList() {
        CameraData.Vendor vendor = (CameraData.Vendor) mVendors.getSelectedItem();
        if (mMenuAddCamera != null) {

            mMenuAddCamera.setVisible(vendor == CameraData.Vendor.USER);
        }

        mAdapter = new CamerasAdapter(mContext, vendor, new ButtonsClickListener());
        mCamerasList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.enter();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera_management, menu);
        mMenuAddCamera = menu.findItem(R.id.camera_management_add);

        return true;
    }

    private class ButtonsClickListener implements CamerasAdapter.OnButtonsClickListener {
        private final OnPositiveClickListener mPositiveClickListener = new OnPositiveClickListener() {
            @Override
            public void onPositiveClick(String camera, int action) {
                DbAdapter dbAdapter = new DbAdapter(mContext).open();
                CameraData.Vendor vendor = (CameraData.Vendor) mVendors.getSelectedItem();

                if (action == Constants.COPY_CAMERA) {
                    String cameraName = vendor.toString().concat(" ").concat(camera);

                    if (dbAdapter.getUserCameras().isCameraExist(cameraName)) {
                        ShowMessageDialog dialog = ShowMessageDialog
                                .getInstance(mContext);
                        dialog.setMessages(R.string.error,
                                R.string.error_cameraAlreadyExist);
                        dialog.show(getSupportFragmentManager(),
                                ShowMessageDialog.DIALOG_TAG);
                    } else {
                        BigDecimal coc = CameraData.getCocForCamera(vendor, camera);
                        dbAdapter.getUserCameras().createCamera(cameraName, 0, 0, 0, 0, coc.floatValue(), true);
                        Toast.makeText(mContext, R.string.camera_copied, Toast.LENGTH_LONG).show();
                    }
                } else if (action == Constants.REMOVE_CAMERA) {
                    mAdapter.remove(camera);
                    mAdapter.notifyDataSetChanged();
                    dbAdapter.getUserCameras().deleteCameraByName(camera);
                }
                dbAdapter.close();
            }
        };

        @Override
        public void onCopyClick(String camera) {
            ConfirmActionDialog dialog = ConfirmActionDialog.getInstance(mContext, Constants.COPY_CAMERA,
                    camera, mPositiveClickListener);
            dialog.show(getSupportFragmentManager(), ConfirmActionDialog.DIALOG_ID);
        }

        @Override
        public void onRemoveClick(String camera) {
            ConfirmActionDialog dialog = ConfirmActionDialog.getInstance(mContext, Constants.REMOVE_CAMERA,
                    camera, mPositiveClickListener);
            dialog.show(getSupportFragmentManager(), ConfirmActionDialog.DIALOG_ID);
        }

        @Override
        public void onEditClick(String camera) {
            DbAdapter dbAdapter = new DbAdapter(mContext).open();
            UserCamerasRecord record = dbAdapter.getUserCameras().getCameraByName(camera);
            dbAdapter.close();
            CameraEditorDialog dialog = CameraEditorDialog
                    .getInstance(mContext);
            dialog.setCallback(new CameraEditorListener());
            dialog.setActionType(CameraEditorDialog.ACTION_EDIT);
            dialog.setDataForEdit(record);
            dialog.show(getSupportFragmentManager(), CameraEditorDialog.DIALOG_TAG);
        }
    }

    private class CameraEditorListener implements CameraEditorDialog.OnCameraEditorListener, OnCallback {
        @Override
        public void onAddCamera(UserCamerasRecord record) {
            DbAdapter dbAdapter = new DbAdapter(mContext).open();
            dbAdapter.getUserCameras().createCamera(record);
            dbAdapter.close();
            updateCameraList();
        }

        @Override
        public void onEditCamera(UserCamerasRecord record) {
            DbAdapter dbAdapter = new DbAdapter(mContext).open();
            dbAdapter.getUserCameras().updateCamera(record);
            dbAdapter.close();
            updateCameraList();
        }
    }
}
