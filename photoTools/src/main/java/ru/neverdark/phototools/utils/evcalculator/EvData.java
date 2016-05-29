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
 *     Modifications:
 *      Artem Yankovskiy (artemyankovskiy@gmail.com)
 *     
 ******************************************************************************/

package ru.neverdark.phototools.utils.evcalculator;

import java.util.ArrayList;
import java.util.Arrays;

import ru.neverdark.phototools.utils.Log;

public class EvData {

    public static final int FULL_STOP = 1;
    public static final int HALF_STOP = 2;
    public static final int THIRD_STOP = 3;
    public static final int INVALID_INDEX = -100;

    /**
     * Table containing all possible exposure compensation values.
     */
    private static final String EV_COMPENSATION_LIST[] = { "-10 EV", "-9 ⅔ EV",
            "-9 ½ EV", "-9 ⅓ EV", "-9 EV", "-8 ⅔ EV", "-8 ½ EV", "-8 ⅓ EV",
            "-8 EV", "-7 ⅔ EV", "-7 ½ EV", "-7 ⅓ EV", "-7 EV", "-6 ⅔ EV",
            "-6 ½ EV", "-6 ⅓ EV", "-6 EV", "-5 ⅔ EV", "-5 ½ EV", "-5 ⅓ EV",
            "-5 EV", "-4 ⅔ EV", "-4 ½ EV", "-4 ⅓ EV", "-4 EV", "-3 ⅔ EV",
            "-3 ½ EV", "-3 ⅓ EV", "-3 EV", "-2 ⅔ EV", "-2 ½ EV", "-2 ⅓ EV",
            "-2 EV", "-1 ⅔ EV", "-1 ½ EV", "-1 ⅓ EV", "-1 EV", "-⅔ EV",
            "-½ EV", "-⅓ EV", "0 EV", "+⅓ EV", "+½ EV", "+⅔ EV", "+1 EV", "+1 ⅓ EV",
            "+1 ½ EV", "+1 ⅔ EV", "+2 EV", "+2 ⅓ EV", "+2 ½ EV", "+2 ⅔ EV", "+3 EV",
            "+3 ⅓ EV", "+3 ½ EV", "+3 ⅔ EV", "+4 EV", "+4 ⅓ EV", "+4 ½ EV", "+4 ⅔ EV",
            "+5 EV", "+5 ⅓ EV", "+5 ½ EV", "+5 ⅔ EV", "+6 EV", "+6 ⅓ EV", "+6 ½ EV",
            "+6 ⅔ EV", "+7 EV", "+7 ⅓ EV", "+7 ½ EV", "+7 ⅔ EV", "+8 EV", "+8 ⅓ EV",
            "+8 ½ EV", "+8 ⅔ EV", "+9 EV", "+9 ⅓ EV", "+9 ½ EV", "+9 ⅔ EV", "+10 EV" };

    /**
     * Table containing all possible ISO values.
     */
    private static final double ISO_VALUE_LIST[] = { 25, 32, 35, 40, 50, 64,
            70, 80, 100, 125, 140, 160, 200, 250, 280, 320, 400, 500, 560, 640,
            800, 1000, 1100, 1250, 1600, 2000, 2200, 2500, 3200, 4000, 4400,
            5000, 6400, 8000, 8800, 10000, 12800, 16000, 17600, 20000, 25600,
            32000, 35200, 40000, 51200, 64000, 70400, 80000, 102400, 128000,
            140800, 160000, 204800 };

    /**
     * Table containing all possible shutter speed values.
     */
    private static final double SHUTTER_VALUE_LIST[] = { 1 / 8000.0,
            1 / 6400.0, 1 / 6000.0, 1 / 5000.0, 1 / 4000.0, 1 / 3200.0,
            1 / 3000.0, 1 / 2500.0, 1 / 2000.0, 1 / 1600.0, 1 / 1500.0,
            1 / 1250.0, 1 / 1000.0, 1 / 800.0, 1 / 750.0, 1 / 640.0, 1 / 500.0,
            1 / 400.0, 1 / 350.0, 1 / 320.0, 1 / 250.0, 1 / 200.0, 1 / 180.0,
            1 / 160.0, 1 / 125.0, 1 / 100.0, 1 / 90.0, 1 / 80.0, 1 / 60.0,
            1 / 50.0, 1 / 45.0, 1 / 40.0, 1 / 30.0, 1 / 25.0, 1 / 23.0,
            1 / 20.0, 1 / 15.0, 1 / 13.0, 1 / 11.0, 1 / 10.0, 1 / 8.0, 1 / 6.0,
            1 / 6.0, 1 / 5.0, 1 / 4.0, 1 / 3.0, 1 / 3.0, 1 / 2.5, 1 / 2.0,
            1 / 1.6, 1 / 1.5, 1 / 1.3, 1, 1.3, 1.5, 1.6, 2, 2.5, 3, 3, 4, 5, 6,
            6, 8, 10, 11, 13, 15, 20, 23, 25, 30, 40, 45, 50, 60, 78, 84, 96,
            120, 150, 168, 192, 240, 300, 342, 378, 480, 606, 678, 762, 960,
            1212, 1356, 1524, 1920, 2418, 2700, 3048, 3840, 4836, 5460, 6120,
            7680, 9660, 10860, 12180, 15360, 19380, 21720, 24360, 30720 };

    /**
     * Table containing all possible aperture values.
     */
    private static final double APERTURE_VALUE_LIST[] = { 360, 320, 300, 290,
            256, 230, 215, 200, 180, 160, 150, 145, 128, 115, 110, 100, 90, 80,
            76, 72, 64, 57, 54, 50, 45, 40, 38, 36, 32, 28, 27, 25, 22, 20, 19,
            18, 16, 14, 13.5, 12.5, 11, 10, 9.5, 9, 8, 7, 6.8, 6.3, 5.6, 5,
            4.8, 4.5, 4, 3.6, 3.4, 3.2, 2.8, 2.5, 2.4, 2.3, 2, 1.8, 1.7, 1.6,
            1.4, 1.25, 1.2, 1.12, 1 };

    /**
     * Extract Aperture values belonging to the given stop distribution.
     *
     * @param stopDistribution stop value for gets aperture list
     * @return Aperture values matching the chosen distribution.
     */
    public static Double[] getApertureValues(int stopDistribution) {
        return getValues(APERTURE_VALUE_LIST, stopDistribution);
    }

    public static Double[] getApertureValues(int stopDistribution,
            int minApertureIndex, int maxApertureIndex) {
        return getValues(APERTURE_VALUE_LIST, stopDistribution,
                minApertureIndex, maxApertureIndex, true);
    }

    public static String[] getEvCompensationValues(int stopDistribution) {
        return getValues(EV_COMPENSATION_LIST, stopDistribution);
    }

    public static Double[] getISOValues(int stopDistribution, int minIsoIndex,
            int maxIsoIndex) {
        return getValues(ISO_VALUE_LIST, stopDistribution, minIsoIndex,
                maxIsoIndex, false);
    }

    /**
     * Extract ISO values belonging to the given stop distribution.
     *
     * @param stopDistribution stop value for get iso list
     * @return Iso values matching the chosen distribution.
     */
    public static Double[] getISOValues(int stopDistribution) {
        return getValues(ISO_VALUE_LIST, stopDistribution);
    }

    /**
     * Extract Shutter speed values belonging to the given stop distribution.
     *
     * @param stopDistribution stop value for get iso list
     * @return Shutter speed values matching the chosen distribution.
     */
    public static Double[] getShutterValues(int stopDistribution) {
        return getValues(SHUTTER_VALUE_LIST, stopDistribution);
    }

    public static Double[] getShutterValues(int stopDistribution,
            int minShutterIndex, int maxShutterIndex) {
        return getValues(SHUTTER_VALUE_LIST, stopDistribution, minShutterIndex,
                maxShutterIndex, false);
    }

    /**
     * Extracts from a full list of values the ones which belong to the given
     * stop distribution.
     *
     * @param listOfValues full list of value
     * @param stopDistribution stop value for get part of full list
     * @return the subset of values contained in listOfValues which belong to
     *         the chosen distribution.
     */
    private static Double[] getValues(double[] listOfValues,
            int stopDistribution) {
        ArrayList<Double> fullValues = new ArrayList<>();
        int index;
        int limit = listOfValues.length;

        for (index = 0; index < limit; index++) {
            if (selectValues(index, stopDistribution)) {
                fullValues.add(0, listOfValues[index]);
            }
        }

        return fullValues.toArray(new Double[fullValues.size()]);
    }

    private static String[] getValues(String[] listOfValues,
            int stopDistribution) {
        ArrayList<String> fullValues = new ArrayList<>();
        int index;
        int limit = listOfValues.length;

        for (index = 0; index < limit; index++) {
            if (selectValues(index, stopDistribution)) {
                fullValues.add(listOfValues[index]);
            }
        }

        return fullValues.toArray(new String[fullValues.size()]);
    }

    private static Double[] getValues(double[] listOfValues,
            int stopDistribution, int minIndex, int maxIndex, boolean isAperture) {
        Double[] values = getValues(listOfValues, stopDistribution);

        ArrayList<Double> fullValues = new ArrayList<>();

        if (stopDistribution == THIRD_STOP) {
            fullValues.addAll(Arrays.asList(values).subList(minIndex, maxIndex + 1));
        } else {
            Double[] thirdValues = getValues(listOfValues, THIRD_STOP);

            for (Double value : values) {
                if (isAperture) {
                    if (value >= thirdValues[minIndex]
                            && value <= thirdValues[maxIndex]) {
                        fullValues.add(value);
                    }
                } else {
                    if (value <= thirdValues[minIndex]
                            && value >= thirdValues[maxIndex]) {
                        fullValues.add(value);
                    }
                }
            }
        }

        Log.variable("Length = ", String.valueOf(fullValues.size()));
        return fullValues.toArray(new Double[fullValues.size()]);
    }

    /**
     * Defines whether or not the given index belongs to the chosen
     * distribution.
     *
     * @param index element index in full list
     * @param stopDistribution stop value for given index
     * @return boolean true if we need choose item
     */
    private static boolean selectValues(int index, int stopDistribution) {
        boolean selection;
        switch (stopDistribution) {
        case FULL_STOP:
            selection = (index % 4 == 0);
            break;
        case HALF_STOP:
            selection = (index % 2 == 0);
            break;
        case THIRD_STOP:
            selection = (index % 4 != 2);
            break;
        default:
            selection = false;
        }

        return selection;
    }
}
