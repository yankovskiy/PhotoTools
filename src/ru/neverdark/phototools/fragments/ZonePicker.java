package ru.neverdark.phototools.fragments;

import android.content.Context;

import com.actionbarsherlock.app.SherlockDialogFragment;


/**
 * Implements dialog for selection time zone
 */
public class ZonePicker extends SherlockDialogFragment {
    public static final String DIALOG = "ZonePicker";
    
    private static final String KEY_DISPLAYNAME = "name";
    private static final String KEY_GMT = "gmt";
    
    private static final int HOUR = 3600000;
    
    private Context mContext;
    
    
}
