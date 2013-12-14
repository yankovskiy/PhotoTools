/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParserException;

import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * The class displaying a list of time zones that match a filter string such as
 * "Africa", "Europe", etc. Choosing an item from the list will set the time
 * zone. Pressing Back without choosing from the list will not result in a
 * change in the time zone setting.
 * 
 * The class based on the ZonePicker from Android 4.1.2_r2.1
 */
public class ZonePicker extends SherlockDialogFragment {
    public static final String DIALOG = "ZonePicker";

    private static final String KEY_ID = "id";
    private static final String KEY_DISPLAYNAME = "name";
    private static final String KEY_GMT = "gmt";
    private static final String KEY_OFFSET = "offset";
    private static final String XMLTAG_TIMEZONE = "timezone";

    private static final int HOUR = 3600000;

    private View mView;
    private ListView mListView;

    /**
     * Adds timezone to time zone list
     * 
     * @param data
     *            time zone list
     * @param id
     *            time zone id
     * @param displayName
     *            time zone name
     * @param date
     *            current date
     */
    private void addItem(List<HashMap<String, Object>> data, String id,
            String displayName, long date) {
        Log.enter();
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_ID, id);
        map.put(KEY_DISPLAYNAME, displayName);

        final TimeZone tz = TimeZone.getTimeZone(id);
        final int offset = tz.getOffset(date);
        final int p = Math.abs(offset);
        final StringBuilder name = new StringBuilder();
        name.append("GMT");

        if (offset < 0) {
            name.append('-');
        } else {
            name.append('+');
        }

        name.append(p / HOUR);
        name.append(":");

        int min = p / 60000;
        min %= 60;

        if (min < 10) {
            name.append('0');
        }
        name.append(min);

        map.put(KEY_GMT, name.toString());
        map.put(KEY_OFFSET, offset);

        data.add(map);
    }

    /**
     * Binds classes objects to resources
     */
    private void bindObjectToResource() {
        mView = View.inflate(getSherlockActivity(),
                R.layout.dialog_timezone_picker, null);
        mListView = (ListView) mView.findViewById(R.id.timeZonePicker_listView);
    }

    /**
     * Constructs an adapter with TimeZone list. Sorted by TimeZone in default.
     * 
     * @param sortedByName
     *            use Name for sorting the list.
     */
    private SimpleAdapter constructTimeZoneAdapter(Context context, int layoutId) {
        final String[] from = new String[] { KEY_DISPLAYNAME, KEY_GMT };
        final int[] to = new int[] { R.id.timeZone_label_displayName,
                R.id.timeZone_label_gmt };
        final List<HashMap<String, Object>> list = getZones(context);
        final SimpleAdapter adapter = new SimpleAdapter(context, list,
                layoutId, from, to);

        return adapter;
    }

    /**
     * Gets time zone list from xml
     * 
     * @param context
     *            application context
     * @return time zone list
     */
    private List<HashMap<String, Object>> getZones(Context context) {
        final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        final long date = Calendar.getInstance().getTimeInMillis();

        try {
            XmlResourceParser xrp = context.getResources().getXml(
                    R.xml.timezones);
            while (xrp.next() != XmlResourceParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                        return data;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
                    String id = xrp.getAttributeValue(0);
                    String displayName = xrp.nextText();
                    addItem(data, id, displayName, date);
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
            xrp.close();
        } catch (XmlPullParserException e) {
            Log.message("XmlPullParserException exception");
        } catch (IOException e) {
            Log.message("IOException exception");
        }

        return data;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.enter();
        bindObjectToResource();

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                getSherlockActivity());
        dialog.setView(mView);
        dialog.setTitle(R.string.zonePicker_title);

        SimpleAdapter adapter = constructTimeZoneAdapter(getSherlockActivity(),
                R.layout.time_zone_row);
        mListView.setAdapter(adapter);
        setOnItemClickListener(this);

        return dialog.create();
    }

    /**
     * Sets on item click listener for ListView
     * 
     * @param dialog
     *            dialog called this function
     */
    private void setOnItemClickListener(final SherlockDialogFragment dialog) {
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked,
                    int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) mListView
                        .getItemAtPosition(position);
                TimeZone tz = TimeZone.getTimeZone(obj.get(KEY_ID).toString());

                dialog.dismiss();
                ((SunsetFragment) getTargetFragment()).setZoneAndCalculate(tz);
            }
        });
    }
}
