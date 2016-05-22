package ru.neverdark.phototools.fragments;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ru.neverdark.abs.CancelClickListener;
import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Log;

public class TimeZonePickerMethodDialog extends UfoDialogFragment {
    public static final String DIALOG_ID = "timeZonePickerMethodDialog";
    private ListView mPickerMethods;

    public interface OnTimeZonePickerMethodListener {
        void onMethodClick(int position);
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.timezone_picker_method_dialog, null));
        mPickerMethods = (ListView) getDialogView().findViewById(R.id.timeZonePickerMethod_listView);
    }

    @Override
    public void setListeners() {
        getAlertDialog()
                .setNegativeButton(R.string.dialog_button_cancel, new CancelClickListener());
        mPickerMethods.setOnItemClickListener(new MethodClickListener());
    }

    public static TimeZonePickerMethodDialog getInstance(Context context) {
        TimeZonePickerMethodDialog dialog = new TimeZonePickerMethodDialog();
        dialog.setContext(context);
        return dialog;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.sunset_timezone_detection_method);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.common_list_item, getContext().getResources().getStringArray(R.array.sunset_timeZonePickerMethods));
        mPickerMethods.setAdapter(adapter);
    }

    private class MethodClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            OnTimeZonePickerMethodListener callback = (OnTimeZonePickerMethodListener) getCallback();
            if (callback != null) {
                callback.onMethodClick(position);
            }
            dismiss();
        }
    }
}
