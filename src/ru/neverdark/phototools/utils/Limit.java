/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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

public class Limit {
    private String mMinAperture;
    private String mMaxAperture;
    
    private String mMinIso;
    private String mMaxIso;
    
    private String mMinShutter;
    private String mMaxShutter;
    
    private String mMinFocalLength;
    private String mMaxFocalLength;
    
    private String mMinSubjectDistance;
    private String mMaxSubjectDistance;
    
    public String getMinAperture() {
        return mMinAperture;
    }
    public void setMinAperture(String minAperture) {
        this.mMinAperture = minAperture;
    }
    public String getMaxAperture() {
        return mMaxAperture;
    }
    public void setMaxAperture(String maxAperture) {
        this.mMaxAperture = maxAperture;
    }
    public String getMinIso() {
        return mMinIso;
    }
    public void setMinIso(String minIso) {
        this.mMinIso = minIso;
    }
    public String getMaxIso() {
        return mMaxIso;
    }
    public void setMaxIso(String maxIso) {
        this.mMaxIso = maxIso;
    }
    public String getMinShutter() {
        return mMinShutter;
    }
    public void setMinShutter(String minShutter) {
        this.mMinShutter = minShutter;
    }
    public String getMaxShutter() {
        return mMaxShutter;
    }
    public void setMaxShutter(String maxShutter) {
        this.mMaxShutter = maxShutter;
    }
    public String getMinFocalLength() {
        return mMinFocalLength;
    }
    public void setMinFocalLength(String minFocalLength) {
        this.mMinFocalLength = minFocalLength;
    }
    public String getMaxFocalLength() {
        return mMaxFocalLength;
    }
    public void setMaxFocalLength(String maxFocalLength) {
        this.mMaxFocalLength = maxFocalLength;
    }
    public String getMinSubjectDistance() {
        return mMinSubjectDistance;
    }
    public void setMinSubjectDistance(String minSubjectDistance) {
        this.mMinSubjectDistance = minSubjectDistance;
    }
    public String getMaxSubjectDistance() {
        return mMaxSubjectDistance;
    }
    public void setMaxSubjectDistance(String maxSubjectDistance) {
        this.mMaxSubjectDistance = maxSubjectDistance;
    }
}
