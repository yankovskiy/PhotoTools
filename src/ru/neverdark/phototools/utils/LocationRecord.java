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
 * A class contains one record from Locations table
 */
public class LocationRecord {
    public long _id;
    public String locationName;
    public double latitude;
    public double longitude;

    public LocationRecord(long _id, String locationName, double latitude,
            double longitude) {
        super();
        this._id = _id;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public LocationRecord() {
        super();
    }
    
    @Override
    public String toString() {
        return locationName;
    }

}
