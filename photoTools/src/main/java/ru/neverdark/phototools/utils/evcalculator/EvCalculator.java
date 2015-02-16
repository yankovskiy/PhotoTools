/*******************************************************************************
 * Copyright (C) 2014 Rudy Dordonne (rudy@itu.dk).
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
 *     
 *     Modification:
 *      Artem Yankovskiy (artemyankovskiy@gmail.com)
 *     
 ******************************************************************************/
package ru.neverdark.phototools.utils.evcalculator;

import java.util.ArrayList;
import java.util.List;

import ru.neverdark.phototools.utils.Limit;
import ru.neverdark.phototools.utils.Log;

public class EvCalculator {
    public static final int CALCULATE_APERTURE = 0;
    public static final int CALCULATE_ISO = 1;
    public static final int CALCULATE_SHUTTER = 2;

    private int mCalculateIndex;
    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;
    private int mIndex;
    private int mStopDistribution;
    private int mCompensationShift;

    private Double ISO_VALUE_LIST[];
    private Double APERTURE_VALUE_LIST[];
    private Double SHUTTER_VALUE_LIST[];

    public void prepare(int currentAperturePosition, int currentIsoPosition,
            int currentShutterSpeedPosition, int newAperturePosition,
            int newIsoPostion, int newShutterSpeedPosition, int calculateIndex, int compensationShift) {

        mCurrentAperturePosition = currentAperturePosition;
        mCurrentIsoPosition = currentIsoPosition;
        mCurrentShutterSpeedPosition = currentShutterSpeedPosition;

        mNewAperturePosition = newAperturePosition;
        mNewIsoPostion = newIsoPostion;
        mNewShutterSpeedPosition = newShutterSpeedPosition;

        mCalculateIndex = calculateIndex;
        mCompensationShift = compensationShift;

        Log.variable("mCurrentAperturePosition",
                String.valueOf(mCurrentAperturePosition));
        Log.variable("mCurrentIsoPosition", String.valueOf(mCurrentIsoPosition));
        Log.variable("mCurrentShutterSpeedPosition",
                String.valueOf(mCurrentShutterSpeedPosition));

        Log.variable("mNewAperturePosition",
                String.valueOf(mNewAperturePosition));
        Log.variable("mNewIsoPostion", String.valueOf(mNewIsoPostion));
        Log.variable("mNewShutterSpeedPosition",
                String.valueOf(mNewShutterSpeedPosition));

        mIndex = EvData.INVALID_INDEX;
    }

    /**
     * Inits local arrays by EV step
     * 
     * @param evStep
     *            ev step
     */
    public void initArrays(final int evStep) {
        mStopDistribution = evStep;
        ISO_VALUE_LIST = EvData.getISOValues(evStep);
        APERTURE_VALUE_LIST = EvData.getApertureValues(evStep);
        SHUTTER_VALUE_LIST = EvData.getShutterValues(evStep);
    }

    public void initArrays(final int evStep, Limit limit) {
        int minApertureIndex = limit.getMinAperture();
        int maxApertureIndex = limit.getMaxAperture();
        int minIsoIndex = limit.getMinIso();
        int maxIsoIndex = limit.getMaxIso();
        int minShutterIndex = limit.getMinShutter();
        int maxShutterIndex = limit.getMaxShutter();
        
        mStopDistribution = evStep;
        APERTURE_VALUE_LIST = EvData.getApertureValues(mStopDistribution,
                minApertureIndex, maxApertureIndex);
        ISO_VALUE_LIST = EvData.getISOValues(mStopDistribution, minIsoIndex,
                maxIsoIndex);
        SHUTTER_VALUE_LIST = EvData.getShutterValues(mStopDistribution,
                minShutterIndex, maxShutterIndex);
    }

    /**
     * Gets effective ISO values list to be displayed
     * 
     * @return list contains possible ISO values
     */
    public List<String> getIsoList() {
        List<String> isos = new ArrayList<String>();
        int index = 0;

        for (index = 0; index < ISO_VALUE_LIST.length; index++) {
            isos.add(cleanNumberToString(ISO_VALUE_LIST[index]));
        }

        return isos;
    }

    /**
     * Gets effective aperture values and formats them list to be displayed
     * 
     * @return list contains possible aperture values
     */
    public List<String> getApertureList() {
        List<String> apertures = new ArrayList<String>();
        int index = 0;

        for (index = 0; index < APERTURE_VALUE_LIST.length; index++) {
            apertures.add(new String("f/")
                    .concat(cleanNumberToString(APERTURE_VALUE_LIST[index])));
        }

        return apertures;
    }

    /**
     * Gets effective shutter values list and formats them to be displayed
     * 
     * @return list contains possible shutter speed values
     */
    public List<String> getShutterList() {
        List<String> shutters = new ArrayList<String>();
        int index = 0;
        String element = "";

        for (index = 0; index < SHUTTER_VALUE_LIST.length; index++) {
            if (SHUTTER_VALUE_LIST[index] < 1.0) {
                element = new String("1/").concat(
                        cleanNumberToString(1 / SHUTTER_VALUE_LIST[index]))
                        .concat(" sec");
            } else if (SHUTTER_VALUE_LIST[index] >= 1
                    && SHUTTER_VALUE_LIST[index] < 60.0) {
                element = cleanNumberToString(SHUTTER_VALUE_LIST[index])
                        .concat(" sec");
            } else if (SHUTTER_VALUE_LIST[index] >= 60.0) {
                element = cleanNumberToString(
                        (Double) (SHUTTER_VALUE_LIST[index] / 60)).concat(
                        " min");
            }

            shutters.add(element);
        }

        return shutters;
    }

    /**
     * Formats numbers to be properly displayed.
     * 
     * @param number
     * @return Formated String
     */
    private String cleanNumberToString(Double number) {
        Double cleanerNumber = Math.round(number * 100) / 100.0;
        String numberToReturn = "";

        if (cleanerNumber % 1 == 0) {
            numberToReturn = String.format("%d", cleanerNumber.intValue());
        } else {
            numberToReturn = String.format("%s", cleanerNumber);
        }

        return numberToReturn;
    }

    /**
     * Function calculates the required value based on indices obtained in the
     * class constructor.
     * 
     * @return index for the empty spinner
     */
    public int calculate() {
        int wIndex = 0;
        mIndex = -mCompensationShift;
        
        if (mCalculateIndex == CALCULATE_APERTURE) {
            double isoStopDifference = calculateIsoDifference();
            double shutterStopDifference = calculateShutterDifference();
            double expectedApertureStopDifference = isoStopDifference
                    + shutterStopDifference;

            wIndex += (int) Math
                    .round(((double) (expectedApertureStopDifference * mStopDistribution)));
            mIndex += mCurrentAperturePosition + wIndex;

            if (mIndex >= APERTURE_VALUE_LIST.length) {
                mIndex = EvData.INVALID_INDEX;
            }
        } else if (mCalculateIndex == CALCULATE_ISO) {
            double apertureStopDifference = calculateApertureDifference();
            double shutterStopDifference = calculateShutterDifference();
            double expectedIsoStopDifference = apertureStopDifference
                    + shutterStopDifference;

            wIndex += (int) Math
                    .round(((double) (expectedIsoStopDifference * mStopDistribution)));
            mIndex += mCurrentIsoPosition + wIndex;

            if (mIndex >= ISO_VALUE_LIST.length) {
                mIndex = EvData.INVALID_INDEX;
            }

        } else if (mCalculateIndex == CALCULATE_SHUTTER) {
            double apertureStopDifference = calculateApertureDifference();
            double isoStopDifference = calculateIsoDifference();
            double expectedShutterStopDifference = apertureStopDifference
                    + isoStopDifference;

            wIndex += (int) Math
                    .round(((double) (expectedShutterStopDifference * mStopDistribution)));
            mIndex += mCurrentShutterSpeedPosition + wIndex;

            Log.variable("mIndex", String.valueOf(mIndex));
            Log.variable("SHUTTER_VALUE_LIST.length",
                    String.valueOf(SHUTTER_VALUE_LIST.length));

            if (mIndex >= SHUTTER_VALUE_LIST.length) {
                mIndex = EvData.INVALID_INDEX;
            }
        }

        
        
        if (mIndex < 0) {
            mIndex = EvData.INVALID_INDEX;
        }

        Log.variable("mIndex", String.valueOf(mIndex));

        return mIndex;
    }

    /**
     * Calculate the stop difference between current and new ISO values.
     * 
     * @return ISO stop difference between current and new ISO values.
     */
    private double calculateIsoDifference() {
        return calculateDifference(ISO_VALUE_LIST[mCurrentIsoPosition],
                ISO_VALUE_LIST[mNewIsoPostion], 2.0);
    }

    /**
     * Calculate the stop difference between current and new shutter values.
     * 
     * @return Shutter stop difference between current and new shutter values.
     */
    private double calculateShutterDifference() {
        return calculateDifference(
                SHUTTER_VALUE_LIST[mCurrentShutterSpeedPosition],
                SHUTTER_VALUE_LIST[mNewShutterSpeedPosition], 2.0);
    }

    /**
     * Calculate the stop difference between current and new aperture values.
     * 
     * @return Aperture stop difference between current and new aperture values.
     */
    private double calculateApertureDifference() {
        return calculateDifference(APERTURE_VALUE_LIST[mNewAperturePosition],
                APERTURE_VALUE_LIST[mCurrentAperturePosition], Math.sqrt(2));
    }

    /**
     * Calculate the stop difference between two parameters.
     * 
     * @param currentValue
     * @param newValue
     * @param base
     * @return Stop difference between the two parameters according to the base
     *         for calculation.
     */
    private double calculateDifference(double currentValue, double newValue,
            double base) {
        double difference = 0;

        if (currentValue < newValue) {
            difference = (Math.log(newValue / currentValue) / Math.log(base));
        } else {
            difference = -(Math.log(currentValue / newValue) / Math.log(base));
        }

        return difference;
    }

}
