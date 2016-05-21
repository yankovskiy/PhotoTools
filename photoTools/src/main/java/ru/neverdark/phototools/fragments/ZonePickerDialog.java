/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ru.neverdark.abs.UfoDialogFragment;
import ru.neverdark.phototools.R;
import ru.neverdark.phototools.utils.Log;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * The class displaying a list of time zones that match a filter string such as
 * "Africa", "Europe", etc. Choosing an item from the list will set the time
 * zone. Pressing Back without choosing from the list will not result in a
 * change in the time zone setting.
 * 
 * The class based on the ZonePicker from Android 4.1.2_r2.1
 */
public class ZonePickerDialog extends UfoDialogFragment {

    public static class GMT {
        private int mOffset;
        private String mGMT;

        public void setOffset(int offset) {
            mOffset = offset;
        }

        public void setGMT(String GMT) {
            mGMT = GMT;
        }

        public String getGMT() {
            return mGMT;
        }

        public int getOffset() {
            return mOffset;
        }
    }

    private class TimeZoneClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            HashMap<String, Object> obj = (HashMap<String, Object>) mListView
                    .getItemAtPosition(position);
            TimeZone tz = TimeZone.getTimeZone(obj.get(KEY_ID).toString());

            getDialog().dismiss();
            OnTimeZonePickerListener callback = (OnTimeZonePickerListener) getCallback();
            if (callback != null) {
                callback.onTimeZonePickerHandler(tz);
            }

        }

    }
    
    public interface OnTimeZonePickerListener {
        public void onTimeZonePickerHandler(TimeZone tz);
    }

    public static final String DIALOG = "ZonePicker";

    private static final String KEY_ID = "id";
    private static final String KEY_DISPLAYNAME = "name";
    private static final String KEY_GMT = "gmt";
    private static final String KEY_OFFSET = "offset";
    private static final String XMLTAG_TIMEZONE = "timezone";

    private static final int HOUR = 3600000;

    public static ZonePickerDialog getInstance(Context context) {
        ZonePickerDialog dialog = new ZonePickerDialog();
        dialog.setContext(context);
        return dialog;
    }

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
    private void addItem(List<HashMap<String, Object>> data, String id, String displayName,
            long date) {
        Log.enter();
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_ID, id);
        map.put(KEY_DISPLAYNAME, displayName);

        final TimeZone tz = TimeZone.getTimeZone(id);

        GMT gmt = getGMTOffset(tz, date);
        map.put(KEY_GMT, gmt.getGMT());
        map.put(KEY_OFFSET, gmt.getOffset());

        data.add(map);
    }

    public static GMT getGMTOffset(TimeZone tz, long date) {
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

        GMT gmt = new GMT();
        gmt.setGMT(name.toString());
        gmt.setOffset(offset);
        return gmt;
    }

    @Override
    public void bindObjects() {
        setDialogView(View.inflate(getContext(), R.layout.dialog_timezone_picker, null));
        mListView = (ListView) getDialogView().findViewById(R.id.timeZonePicker_listView);
    }

    /**
     * Constructs an adapter with TimeZone list. Sorted by TimeZone in default.
     */
    private SimpleAdapter constructTimeZoneAdapter(Context context, int layoutId) {
        final String[] from = new String[] { KEY_DISPLAYNAME, KEY_GMT };
        final int[] to = new int[] { R.id.timeZone_label_displayName, R.id.timeZone_label_gmt };
        final List<HashMap<String, Object>> list = getZones(context);
        final SimpleAdapter adapter = new SimpleAdapter(context, list, layoutId, from, to);

        return adapter;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        getAlertDialog().setTitle(R.string.zonePicker_title);
        SimpleAdapter adapter = constructTimeZoneAdapter(getContext(),
                R.layout.time_zone_row);
        mListView.setAdapter(adapter);
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
            XmlResourceParser xrp = context.getResources().getXml(R.xml.timezones);
            while (xrp.next() != XmlPullParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlPullParser.END_TAG) {
                while (xrp.getEventType() != XmlPullParser.START_TAG) {
                    if (xrp.getEventType() == XmlPullParser.END_DOCUMENT) {
                        return data;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
                    String id = xrp.getAttributeValue(0);
                    String displayName = xrp.nextText();
                    addItem(data, id, displayName, date);
                }
                while (xrp.getEventType() != XmlPullParser.END_TAG) {
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
    public void setListeners() {
        mListView.setOnItemClickListener(new TimeZoneClickListener());
    }
}
