/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.utils;

import ru.neverdark.phototools.BuildConfig;

/**
 * Constants for PhotoTools application
 */
public class Constants {
    public static final boolean PAID = BuildConfig.FLAVOR.equals("paid");

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAIL = 1;

    public static final String CURRENT_CHOICE = "currentChoice";
    public static final String SHOWN_INDEX = "detailsIndex";

    public static final byte DOF_CHOICE = 0;
    public static final byte EV_CHOICE = 1;
    public static final byte SUNSET_CHOICE = 2;
    public static final byte RATE_CHOICE = 3;
    public static final byte FEEDBACK_CHOICE = 4;
    public static final byte DONATE_CHOICE = 5;
    public static final byte ABOUT_CHOICE = 6;
    public static final byte PLUGIN_CHOICE = 7;
    public static final byte EVDIFF_CHOICE = 8;
    public static final byte SETTINGS_CHOICE = 9;

    public static final byte LOCATION_CURRENT_POSITION_CHOICE = 0;
    public static final byte LOCATION_POINT_ON_MAP_CHOICE = 1;

    public static final String LOCATION_LATITUDE = "ru.neverdark.phototools.locationLatitude";
    public static final String LOCATION_LONGITUDE = "ru.neverdark.phototools.locationLongitude";

    public static final byte LOCATION_ACTION_ADD = 0;
    public static final byte LOCATION_ACTION_EDIT = 1;

    public static final String LOCATION_ACTION = "ru.neverdark.phototools.locationAction";
    public static final String LOCATION_RECORD_ID = "ru.neverdark.phototools.recordId";
    public static final String LOCATION_NAME = "ru.neverdark.phototools.locationName";

    public static final float MAP_CAMERA_ZOOM = 17.0f;

    public static final int INFORMATION_SUNRISE = 0;
    public static final int INFORMATION_SUNSET = 1;
    public static final int INFORMATION_ASTRO_SUNRISE = 2;
    public static final int INFORMATION_ASTRO_SUNSET = 3;
    public static final int INFORMATION_NAUTICAL_SUNRISE = 4;
    public static final int INFORMATION_NAUTICAL_SUNSET = 5;
    public static final int INFORMATION_CIVIL_SUNRISE = 6;
    public static final int INFORMATION_CIVIL_SUNSET = 7;

    public static final int METER = 0;
    public static final int CM = 1;
    public static final int FOOT = 2;
    public static final int INCH = 3;

    public static final int COPY_CAMERA = 0;
    public static final int EDIT_CAMERA = 1;
    public static final int REMOVE_CAMERA = 2;

    public static final int AUTO_TIMEZONE_METHOD = 0;
    public static final int MANUAL_TIMEZONE_METHOD = 1;
    public static final String LOCATION_MAP_TYPE = "ru.neverdark.phototools.mapType";
    public static final String LOCATION_CAMERA_ZOOM = "ru.neverdark.phototools.cameraZoom";
}
