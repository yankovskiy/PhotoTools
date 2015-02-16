package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;

public class PhotohuntDialog extends UfoDialogFragment{
    private class NegativeClickListener implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mHideMenu.isChecked()) {
                mCallback = (OnHidePhotohuntMenu) getCallback();
                if (mCallback != null) {
                    mCallback.hidePhotohuntMenu();
                }
            }
            dialog.dismiss();
        }

    }

    private class PositiveClickListenet implements OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mHideMenu.isChecked()) {
                mCallback = (OnHidePhotohuntMenu) getCallback();
                if (mCallback != null) {
                    mCallback.hidePhotohuntMenu();
                }
            }
            dialog.dismiss();
            
            String url = "market://details?id=ru.neverdark.photohunt";
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse(url));
            startActivity(marketIntent);
        }

    }
    
    public interface OnHidePhotohuntMenu {
        public void hidePhotohuntMenu();
    }
    
    private OnHidePhotohuntMenu mCallback;

    public static String DIALOG_ID = "photohuntDialog";
    private CheckBox mHideMenu;

    @Override
    public void setListeners() {
        getAlertDialog().setPositiveButton(R.string.dialog_button_ok, new PositiveClickListenet());
        getAlertDialog().setNegativeButton(R.string.dialog_button_cancel, new NegativeClickListener());
    }
    
    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.photohunt_dialog, null));
        mHideMenu = (CheckBox) getDialogView().findViewById(R.id.photohunt_hide);
    }
    
    public static PhotohuntDialog getInstance(Context context) {
        PhotohuntDialog dialog = new PhotohuntDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.photohunt_title);
        getAlertDialog().setMessage(R.string.photohunt_message);
    }
}
