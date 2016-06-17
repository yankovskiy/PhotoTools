/*******************************************************************************
 * Copyright (C) 2014 Rudy Dordonne (rudy@itu.dk).
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
 * <p/>
 * Modifications:
 * Artem Yankovskiy (artemyankovskiy@gmail.com)
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
    private static final String EV_COMPENSATION_LIST[] = {"-10 EV", "-9 ⅔ EV",
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
            "+8 ½ EV", "+8 ⅔ EV", "+9 EV", "+9 ⅓ EV", "+9 ½ EV", "+9 ⅔ EV", "+10 EV"};

    private static final String EV_FULL_COMPENSATION_LIST[] = {
            "-60 EV",
            "-59 ⅔ EV", "-59 ½ EV", "-59 ⅓ EV", "-59 EV", "-58 ⅔ EV", "-58 ½ EV", "-58 ⅓ EV",
            "-58 EV", "-57 ⅔ EV", "-57 ½ EV", "-57 ⅓ EV", "-57 EV", "-56 ⅔ EV", "-56 ½ EV",
            "-56 ⅓ EV", "-56 EV", "-55 ⅔ EV", "-55 ½ EV", "-55 ⅓ EV", "-55 EV", "-54 ⅔ EV",
            "-54 ½ EV", "-54 ⅓ EV", "-54 EV", "-53 ⅔ EV", "-53 ½ EV", "-53 ⅓ EV", "-53 EV",
            "-52 ⅔ EV", "-52 ½ EV", "-52 ⅓ EV", "-52 EV", "-51 ⅔ EV", "-51 ½ EV", "-51 ⅓ EV",
            "-51 EV", "-50 ⅔ EV", "-50 ½ EV", "-50 ⅓ EV", "-50 EV", "-49 ⅔ EV", "-49 ½ EV",
            "-49 ⅓ EV", "-49 EV", "-48 ⅔ EV", "-48 ½ EV", "-48 ⅓ EV", "-48 EV", "-47 ⅔ EV",
            "-47 ½ EV", "-47 ⅓ EV", "-47 EV", "-46 ⅔ EV", "-46 ½ EV", "-46 ⅓ EV", "-46 EV",
            "-45 ⅔ EV", "-45 ½ EV", "-45 ⅓ EV", "-45 EV", "-44 ⅔ EV", "-44 ½ EV", "-44 ⅓ EV",
            "-44 EV", "-43 ⅔ EV", "-43 ½ EV", "-43 ⅓ EV", "-43 EV", "-42 ⅔ EV", "-42 ½ EV",
            "-42 ⅓ EV", "-42 EV", "-41 ⅔ EV", "-41 ½ EV", "-41 ⅓ EV", "-41 EV", "-40 ⅔ EV",
            "-40 ½ EV", "-40 ⅓ EV", "-40 EV", "-39 ⅔ EV", "-39 ½ EV", "-39 ⅓ EV", "-39 EV",
            "-38 ⅔ EV", "-38 ½ EV", "-38 ⅓ EV", "-38 EV", "-37 ⅔ EV", "-37 ½ EV", "-37 ⅓ EV",
            "-37 EV", "-36 ⅔ EV", "-36 ½ EV", "-36 ⅓ EV", "-36 EV", "-35 ⅔ EV", "-35 ½ EV",
            "-35 ⅓ EV", "-35 EV", "-34 ⅔ EV", "-34 ½ EV", "-34 ⅓ EV", "-34 EV", "-33 ⅔ EV",
            "-33 ½ EV", "-33 ⅓ EV", "-33 EV", "-32 ⅔ EV", "-32 ½ EV", "-32 ⅓ EV", "-32 EV",
            "-31 ⅔ EV", "-31 ½ EV", "-31 ⅓ EV", "-31 EV", "-30 ⅔ EV", "-30 ½ EV", "-30 ⅓ EV",
            "-30 EV", "-29 ⅔ EV", "-29 ½ EV", "-29 ⅓ EV", "-29 EV", "-28 ⅔ EV", "-28 ½ EV",
            "-28 ⅓ EV", "-28 EV", "-27 ⅔ EV", "-27 ½ EV", "-27 ⅓ EV", "-27 EV", "-26 ⅔ EV",
            "-26 ½ EV", "-26 ⅓ EV", "-26 EV", "-25 ⅔ EV", "-25 ½ EV", "-25 ⅓ EV", "-25 EV",
            "-24 ⅔ EV", "-24 ½ EV", "-24 ⅓ EV", "-24 EV", "-23 ⅔ EV", "-23 ½ EV", "-23 ⅓ EV",
            "-23 EV", "-22 ⅔ EV", "-22 ½ EV", "-22 ⅓ EV", "-22 EV", "-21 ⅔ EV", "-21 ½ EV",
            "-21 ⅓ EV", "-21 EV", "-20 ⅔ EV", "-20 ½ EV", "-20 ⅓ EV", "-20 EV", "-19 ⅔ EV",
            "-19 ½ EV", "-19 ⅓ EV", "-19 EV", "-18 ⅔ EV", "-18 ½ EV", "-18 ⅓ EV", "-18 EV",
            "-17 ⅔ EV", "-17 ½ EV", "-17 ⅓ EV", "-17 EV", "-16 ⅔ EV", "-16 ½ EV", "-16 ⅓ EV",
            "-16 EV", "-15 ⅔ EV", "-15 ½ EV", "-15 ⅓ EV", "-15 EV", "-14 ⅔ EV", "-14 ½ EV",
            "-14 ⅓ EV", "-14 EV", "-13 ⅔ EV", "-13 ½ EV", "-13 ⅓ EV", "-13 EV", "-12 ⅔ EV",
            "-12 ½ EV", "-12 ⅓ EV", "-12 EV", "-11 ⅔ EV", "-11 ½ EV", "-11 ⅓ EV",
            "-11 EV", "-10 ⅔ EV", "-10 ½ EV", "-10 ⅓ EV", "-10 EV", "-9 ⅔ EV", "-9 ½ EV", "-9 ⅓ EV",
            "-9 EV", "-8 ⅔ EV", "-8 ½ EV", "-8 ⅓ EV", "-8 EV", "-7 ⅔ EV", "-7 ½ EV", "-7 ⅓ EV",
            "-7 EV", "-6 ⅔ EV", "-6 ½ EV", "-6 ⅓ EV", "-6 EV", "-5 ⅔ EV", "-5 ½ EV", "-5 ⅓ EV",
            "-5 EV", "-4 ⅔ EV", "-4 ½ EV", "-4 ⅓ EV", "-4 EV", "-3 ⅔ EV", "-3 ½ EV", "-3 ⅓ EV",
            "-3 EV", "-2 ⅔ EV", "-2 ½ EV", "-2 ⅓ EV", "-2 EV", "-1 ⅔ EV", "-1 ½ EV", "-1 ⅓ EV",
            "-1 EV", "-⅔ EV", "-½ EV", "-⅓ EV",
            "0 EV", "⅓ EV", "½ EV", "⅔ EV", "1 EV", "1 ⅓ EV", "1 ½ EV", "1 ⅔ EV", "2 EV", "2 ⅓ EV",
            "2 ½ EV", "2 ⅔ EV", "3 EV", "3 ⅓ EV", "3 ½ EV", "3 ⅔ EV", "4 EV", "4 ⅓ EV", "4 ½ EV",
            "4 ⅔ EV", "5 EV", "5 ⅓ EV", "5 ½ EV", "5 ⅔ EV", "6 EV", "6 ⅓ EV", "6 ½ EV", "6 ⅔ EV",
            "7 EV", "7 ⅓ EV", "7 ½ EV", "7 ⅔ EV", "8 EV", "8 ⅓ EV", "8 ½ EV", "8 ⅔ EV", "9 EV",
            "9 ⅓ EV", "9 ½ EV", "9 ⅔ EV", "10 EV", "10 ⅓ EV", "10 ½ EV", "10 ⅔ EV", "11 EV",
            "11 ⅓ EV", "11 ½ EV", "11 ⅔ EV", "12 EV", "12 ⅓ EV", "12 ½ EV", "12 ⅔ EV", "13 EV",
            "13 ⅓ EV", "13 ½ EV", "13 ⅔ EV", "14 EV", "14 ⅓ EV", "14 ½ EV", "14 ⅔ EV", "15 EV",
            "15 ⅓ EV", "15 ½ EV", "15 ⅔ EV", "16 EV", "16 ⅓ EV", "16 ½ EV", "16 ⅔ EV", "17 EV",
            "17 ⅓ EV", "17 ½ EV", "17 ⅔ EV", "18 EV", "18 ⅓ EV", "18 ½ EV", "18 ⅔ EV", "19 EV",
            "19 ⅓ EV", "19 ½ EV", "19 ⅔ EV", "20 EV", "20 ⅓ EV", "20 ½ EV", "20 ⅔ EV", "21 EV",
            "21 ⅓ EV", "21 ½ EV", "21 ⅔ EV", "22 EV", "22 ⅓ EV", "22 ½ EV", "22 ⅔ EV", "23 EV",
            "23 ⅓ EV", "23 ½ EV", "23 ⅔ EV", "24 EV", "24 ⅓ EV", "24 ½ EV", "24 ⅔ EV", "25 EV",
            "25 ⅓ EV", "25 ½ EV", "25 ⅔ EV", "26 EV", "26 ⅓ EV", "26 ½ EV", "26 ⅔ EV", "27 EV",
            "27 ⅓ EV", "27 ½ EV", "27 ⅔ EV", "28 EV", "28 ⅓ EV", "28 ½ EV", "28 ⅔ EV", "29 EV",
            "29 ⅓ EV", "29 ½ EV", "29 ⅔ EV", "30 EV", "30 ⅓ EV", "30 ½ EV", "30 ⅔ EV", "31 EV",
            "31 ⅓ EV", "31 ½ EV", "31 ⅔ EV", "32 EV", "32 ⅓ EV", "32 ½ EV", "32 ⅔ EV", "33 EV",
            "33 ⅓ EV", "33 ½ EV", "33 ⅔ EV", "34 EV", "34 ⅓ EV", "34 ½ EV", "34 ⅔ EV", "35 EV",
            "35 ⅓ EV", "35 ½ EV", "35 ⅔ EV", "36 EV", "36 ⅓ EV", "36 ½ EV", "36 ⅔ EV", "37 EV",
            "37 ⅓ EV", "37 ½ EV", "37 ⅔ EV", "38 EV", "38 ⅓ EV", "38 ½ EV", "38 ⅔ EV", "39 EV",
            "39 ⅓ EV", "39 ½ EV", "39 ⅔ EV", "40 EV", "40 ⅓ EV", "40 ½ EV", "40 ⅔ EV", "41 EV",
            "41 ⅓ EV", "41 ½ EV", "41 ⅔ EV", "42 EV", "42 ⅓ EV", "42 ½ EV", "42 ⅔ EV", "43 EV",
            "43 ⅓ EV", "43 ½ EV", "43 ⅔ EV", "44 EV", "44 ⅓ EV", "44 ½ EV", "44 ⅔ EV", "45 EV",
            "45 ⅓ EV", "45 ½ EV", "45 ⅔ EV", "46 EV", "46 ⅓ EV", "46 ½ EV", "46 ⅔ EV", "47 EV",
            "47 ⅓ EV", "47 ½ EV", "47 ⅔ EV", "48 EV", "48 ⅓ EV", "48 ½ EV", "48 ⅔ EV", "49 EV",
            "49 ⅓ EV", "49 ½ EV", "49 ⅔ EV", "50 EV", "50 ⅓ EV", "50 ½ EV", "50 ⅔ EV", "51 EV",
            "51 ⅓ EV", "51 ½ EV", "51 ⅔ EV", "52 EV", "52 ⅓ EV", "52 ½ EV", "52 ⅔ EV", "53 EV",
            "53 ⅓ EV", "53 ½ EV", "53 ⅔ EV", "54 EV", "54 ⅓ EV", "54 ½ EV", "54 ⅔ EV", "55 EV",
            "55 ⅓ EV", "55 ½ EV", "55 ⅔ EV", "56 EV", "56 ⅓ EV", "56 ½ EV", "56 ⅔ EV", "57 EV",
            "57 ⅓ EV", "57 ½ EV", "57 ⅔ EV", "58 EV", "58 ⅓ EV", "58 ½ EV", "58 ⅔ EV", "59 EV",
            "59 ⅓ EV", "59 ½ EV", "59 ⅔ EV", "60 EV"
    };

    /**
     * Table containing all possible ISO values.
     */
    private static final double ISO_VALUE_LIST[] = {25, 32, 35, 40, 50, 64,
            70, 80, 100, 125, 140, 160, 200, 250, 280, 320, 400, 500, 560, 640,
            800, 1000, 1100, 1250, 1600, 2000, 2200, 2500, 3200, 4000, 4400,
            5000, 6400, 8000, 8800, 10000, 12800, 16000, 17600, 20000, 25600,
            32000, 35200, 40000, 51200, 64000, 70400, 80000, 102400, 128000,
            140800, 160000, 204800};

    /**
     * Table containing all possible shutter speed values.
     */
    private static final double SHUTTER_VALUE_LIST[] = {1 / 8000.0,
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
            7680, 9660, 10860, 12180, 15360, 19380, 21720, 24360, 30720};

    /**
     * Table containing all possible aperture values.
     */
    private static final double APERTURE_VALUE_LIST[] = {360, 320, 300, 290,
            256, 230, 215, 200, 180, 160, 150, 145, 128, 115, 110, 100, 90, 80,
            76, 72, 64, 57, 54, 50, 45, 40, 38, 36, 32, 28, 27, 25, 22, 20, 19,
            18, 16, 14, 13.5, 12.5, 11, 10, 9.5, 9, 8, 7, 6.8, 6.3, 5.6, 5,
            4.8, 4.5, 4, 3.6, 3.4, 3.2, 2.8, 2.5, 2.4, 2.3, 2, 1.8, 1.7, 1.6,
            1.4, 1.25, 1.2, 1.12, 1};

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

    public static String[] getFullEvCompensationValues(int stopDistribution) {
        return getValues(EV_FULL_COMPENSATION_LIST, stopDistribution);
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
