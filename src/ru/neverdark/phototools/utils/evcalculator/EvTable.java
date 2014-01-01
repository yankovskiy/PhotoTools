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
package ru.neverdark.phototools.utils.evcalculator;

/**
 * Interface for exposure pare table
 */
public interface EvTable {
    /**
     * Gets ISO list
     * @return array contains possible ISO
     */
    public String[] getIsoList();
    
    /**
     * Gets aperture list 
     * @return array contains possible apertures
     */
    public String[] getApertureList();
    
    /**
     * Gets shutter list
     * @return array contains possible shutter speed
     */
    public String[] getShutterList();
    
    /**
     * Gets EV number table
     * @return two-dimensional array contains EV number
     */
    public int[][] getEvTable();
    
    /**
     * Gets table contains shutter speed
     * @return two-dimensional array contains shutter speed
     */
    public String[][] getShutterTable();
}
