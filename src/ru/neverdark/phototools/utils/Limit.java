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
    private int mMinAperture;
    private int mMaxAperture;
    
    private int mMinIso;
    private int mMaxIso;
    
    private int mMinShutter;
    private int mMaxShutter;
    
    private int mMinFocalLength;
    private int mMaxFocalLength;
    
    private int mMinSubjectDistance;
    private int mMaxSubjectDistance;
    
    public int getMinAperture() {
        return mMinAperture;
    }
    public void setMinAperture(int minAperture) {
        this.mMinAperture = minAperture;
    }
    public int getMaxAperture() {
        return mMaxAperture;
    }
    public void setMaxAperture(int maxAperture) {
        this.mMaxAperture = maxAperture;
    }
    public int getMinIso() {
        return mMinIso;
    }
    public void setMinIso(int minIso) {
        this.mMinIso = minIso;
    }
    public int getMaxIso() {
        return mMaxIso;
    }
    public void setMaxIso(int maxIso) {
        this.mMaxIso = maxIso;
    }
    public int getMinShutter() {
        return mMinShutter;
    }
    public void setMinShutter(int minShutter) {
        this.mMinShutter = minShutter;
    }
    public int getMaxShutter() {
        return mMaxShutter;
    }
    public void setMaxShutter(int maxShutter) {
        this.mMaxShutter = maxShutter;
    }
    public int getMinFocalLength() {
        return mMinFocalLength;
    }
    public void setMinFocalLength(int minFocalLength) {
        this.mMinFocalLength = minFocalLength;
    }
    public int getMaxFocalLength() {
        return mMaxFocalLength;
    }
    public void setMaxFocalLength(int maxFocalLength) {
        this.mMaxFocalLength = maxFocalLength;
    }
    public int getMinSubjectDistance() {
        return mMinSubjectDistance;
    }
    public void setMinSubjectDistance(int minSubjectDistance) {
        this.mMinSubjectDistance = minSubjectDistance;
    }
    public int getMaxSubjectDistance() {
        return mMaxSubjectDistance;
    }
    public void setMaxSubjectDistance(int maxSubjectDistance) {
        this.mMaxSubjectDistance = maxSubjectDistance;
    }
}
