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
package ru.neverdark.phototools.utils;

/**
 * Constants for PhotoTools application
 */
public class Constants {
    public static final String CURRENT_CHOICE = "currentChoice";
    public static final String SHOWN_INDEX = "detailsIndex";
    
    public static final byte DOF_CHOICE = 0;
    public static final byte EV_CHOICE = 1;
    public static final byte SUNSET_CHOICE = 2;
    public static final byte RATE_CHOICE = 3;
    public static final byte FEEDBACK_CHOICE = 4;
    public static final byte ABOUT_CHOICE = 5;
    
    public static final String DATE_YEAR = "year";
    public static final String DATE_MONTH = "month";
    public static final String DATE_DAY = "day";
    public static final String DATE_PICKER_DIALOG = "datePickerDialog";
    
    public static final byte LOCATION_CURRENT_POSITION_CHOICE = 0;
    public static final byte LOCATION_POINT_ON_MAP_CHOICE = 1;
    
    public static final String LOCATION_SELECTION_DIALOG = "locationSelectionDialog";
    
    public static final byte DIALOG_FRAGMENT = 0;
    
    public static final String LOCATION_LATITUDE = "ru.neverdark.phototools.locationLatitude";
    public static final String LOCATION_LONGITUDE = "ru.neverdark.phototools.locationLongitude";
    public static final String LOCATION_IS_SAVE = "ru.neverdark.phototools.isSaveLocation";
    public static final String LOCATION_SELECTION_ID = "locationSelectionId";
    public static final String LOCATION_TIMEZONE = "timeZone"; 
    
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
}
