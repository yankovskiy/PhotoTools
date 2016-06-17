/*******************************************************************************
 * Copyright (C) 2013-2016 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
 * Класс для хранения позиций колес в EV-калькуляторах
 */
public class EvWheelsData {
    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterPosition;

    private int mNewAperturePosition;
    private int mNewIsoPosition;
    private int mNewShutterPosition;

    public EvWheelsData() {

    }

    public EvWheelsData(int allPositions) {
        mCurrentAperturePosition = allPositions;
        mCurrentIsoPosition = allPositions;
        mCurrentShutterPosition = allPositions;

        mNewAperturePosition = allPositions;
        mNewIsoPosition = allPositions;
        mNewShutterPosition = allPositions;
    }

    public int getCurrentAperturePosition() {
        return mCurrentAperturePosition;
    }

    public void setCurrentAperturePosition(int currentAperturePosition) {
        this.mCurrentAperturePosition = currentAperturePosition;
    }

    public int getCurrentIsoPosition() {
        return mCurrentIsoPosition;
    }

    public void setCurrentIsoPosition(int currentIsoPosition) {
        this.mCurrentIsoPosition = currentIsoPosition;
    }

    public int getCurrentShutterPosition() {
        return mCurrentShutterPosition;
    }

    public void setCurrentShutterPosition(int currentShutterPosition) {
        this.mCurrentShutterPosition = currentShutterPosition;
    }

    public int getNewAperturePosition() {
        return mNewAperturePosition;
    }

    public void setNewAperturePosition(int newAperturePosition) {
        this.mNewAperturePosition = newAperturePosition;
    }

    public int getNewIsoPosition() {
        return mNewIsoPosition;
    }

    public void setNewIsoPosition(int newIsoPosition) {
        this.mNewIsoPosition = newIsoPosition;
    }

    public int getNewShutterPosition() {
        return mNewShutterPosition;
    }

    public void setNewShutterPosition(int newShutterPosition) {
        this.mNewShutterPosition = newShutterPosition;
    }
}
