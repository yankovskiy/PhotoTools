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
package ru.neverdark.phototools.utils;

/**
 * Constants for PhotoTools application
 */
public class Constants {
    public static final boolean PAID = true;
    
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
    
    public static final String DATE_YEAR = "year";
    public static final String DATE_MONTH = "month";
    public static final String DATE_DAY = "day";
    public static final String DATE_PICKER_DIALOG = "datePickerDialog";
    
    public static final byte LOCATION_CURRENT_POSITION_CHOICE = 0;
    public static final byte LOCATION_POINT_ON_MAP_CHOICE = 1;
    
    public static final byte DIALOG_FRAGMENT = 0;
    public static final byte DELETE_DIALOG_FRAGMENT = 1;
    
    public static final String LOCATION_LATITUDE = "ru.neverdark.phototools.locationLatitude";
    public static final String LOCATION_LONGITUDE = "ru.neverdark.phototools.locationLongitude";
    public static final String LOCATION_SELECTION_ID = "locationSelectionId";
    public static final String LOCATION_TIMEZONE = "timeZone";
    
    public static final byte LOCATION_ACTION_ADD = 0;
    public static final byte LOCATION_ACTION_EDIT = 1;
    
    public static final String MAP_ACTION_DATA = "mapAction";
    public static final String MAP_RECORD_ID = "mapRecordId";
    public static final String MAP_LOCATION_NAME = "mapLocationName";
    
    public static final String LOCATION_ACTION_DATA = "locationAction";
    public static final String LOCATION_ACTION = "ru.neverdark.phototools.locationAction";
    public static final String LOCATION_RECORD_ID = "ru.neverdark.phototools.recordId";
    public static final String LOCATION_NAME = "ru.neverdark.phototools.locationName";
    public static final String LOCATION_NAME_DATA = "locationName";
    
    public static final String LOCATION_OFFICIAL_SUNRISE = "locaionOfficialSunrise";
    public static final String LOCATION_OFFICIAL_SUNSET = "locationOfficialSunset";
    public static final String LOCATION_ASTRO_SUNRISE = "locationAstroSunrise";
    public static final String LOCATION_ASTRO_SUNSET = "locationAstroSunset";
    public static final String LOCATION_NAUTICAL_SUNRISE = "locationNauticalSunrise";
    public static final String LOCATION_NAUTICAL_SUNSET = "locationNauticalSunset";
    public static final String LOCATION_CIVIL_SUNRISE = "locationCivilSunrise";
    public static final String LOCATION_CIVIL_SUNSET = "locationCivilSunset";
    public static final String LOCATION_IS_VISIVLE_RESULT = "locationIsVisibleResult";
    
    public static final float MAP_CAMERA_ZOOM = 17.0f; 
    
    public static final String MAP_MARKER_POSITION = "mapMarkerPosition";
    
    public static final int INFORMATION_SUNRISE = 0;
    public static final int INFORMATION_SUNSET = 1;
    public static final int INFORMATION_ASTRO_SUNRISE = 2;
    public static final int INFORMATION_ASTRO_SUNSET = 3;
    public static final int INFORMATION_NAUTICAL_SUNRISE = 4;
    public static final int INFORMATION_NAUTICAL_SUNSET = 5;
    public static final int INFORMATION_CIVIL_SUNRISE = 6;
    public static final int INFORMATION_CIVIL_SUNSET = 7;
    
    public static final String INFORMATION_MESSAGE_ID = "informationMessageId";
    
    public static final String DELETE_LOCATION_NAME = "deleteLocationName";
    public static final String DELETE_LOCATION_POSITION = "deleteLocationPosition";
    
    public static final String CONFIRM_CREATION_ISVISIBLE = "isVisible";
    public static final String CONFIRM_CREATION_ISEDIT = "isEdit";
    
    public final static int METER = 0;
    public final static int CM = 1;
    public final static int FOOT = 2;
    public final static int INCH = 3;
    
    public final static int TABLET_SW = 600;
}
