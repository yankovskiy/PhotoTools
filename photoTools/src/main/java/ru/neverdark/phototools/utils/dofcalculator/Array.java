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
package ru.neverdark.phototools.utils.dofcalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Array {
    public final static double SCIENTIFIC_ARERTURES[] = { 1.0000000000,
            1.1224620483, 1.2599210499, 1.4142135624, 1.5874010520,
            1.7817974363, 2.0000000000, 2.2449240966, 2.5198420998,
            2.8284271247, 3.1748021039, 3.5635948726, 4.0000000000,
            4.4898481932, 5.0396841996, 5.6568542495, 6.3496042079,
            7.1271897451, 8.0000000000, 8.9796963865, 10.0793683992,
            11.3137084990, 12.6992084157, 14.2543794902, 16.0000000000,
            17.9593927730, 20.1587367983, 22.6274169980, 25.3984168315,
            28.5087589805, 32.0000000000, 35.9187855459, 40.3174735966,
            45.2548339959, 50.7968336630, 57.0175179610, 64.0000000000,
            71.8375710918, 80.6349471933, 90.5096679919, 101.5936673260,
            114.0350359220, 128.0000000000, 143.6751421836, 161.2698943865,
            181.0193359838, 203.1873346519, 228.0700718439, 256.0000000000,
            287.3502843672, 322.5397887731, 362.0386719675 };

    public static final String APERTURE_LIST[] = { "f/1", "f/1.12", "f/1.25",
            "f/1.4", "f/1.6", "f/1.8", "f/2.0", "f/2.3", "f/2.5", "f/2.8",
            "f/3.2", "f/3.6", "f/4", "f/4.5", "f/5", "f/5.6", "f/6.3", "f/7",
            "f/8", "f/9", "f/10", "f/11", "f/12.5", "f/14", "f/16", "f/18",
            "f/20", "f/22", "f/25", "f/28", "f/32", "f/36", "f/40", "f/45",
            "f/50", "f/57", "f/64", "f/72", "f/80", "f/90", "f/100", "f/115",
            "f/128", "f/145", "f/160", "f/180", "f/200", "f/230", "f/256",
            "f/290", "f/320", "f/360" };

    public static final String FOCAL_LENGTH[] = { "3 mm", "4 mm", "5 mm",
            "6 mm", "7 mm", "8 mm", "9 mm", "10 mm", "11 mm", "12 mm", "13 mm",
            "14 mm", "15 mm", "16 mm", "17 mm", "18 mm", "19 mm", "20 mm",
            "21 mm", "22 mm", "23 mm", "24 mm", "25 mm", "26 mm", "27 mm",
            "28 mm", "29 mm", "30 mm", "31 mm", "32 mm", "33 mm", "34 mm",
            "35 mm", "36 mm", "37 mm", "38 mm", "39 mm", "40 mm", "41 mm",
            "42 mm", "43 mm", "45 mm", "46 mm", "47 mm", "48 mm", "50 mm",
            "51 mm", "52 mm", "53 mm", "54 mm", "55 mm", "56 mm", "58 mm",
            "59 mm", "60 mm", "61 mm", "63 mm", "65 mm", "66 mm", "69 mm",
            "70 mm", "72 mm", "75 mm", "76 mm", "80 mm", "85 mm", "86 mm",
            "89 mm", "90 mm", "95 mm", "100 mm", "101 mm", "102 mm", "103 mm",
            "104 mm", "105 mm", "106 mm", "108 mm", "110 mm", "111 mm",
            "114 mm", "115 mm", "117 mm", "120 mm", "121 mm", "123 mm",
            "125 mm", "127 mm", "130 mm", "135 mm", "140 mm", "145 mm",
            "150 mm", "152 mm", "155 mm", "160 mm", "165 mm", "168 mm",
            "170 mm", "180 mm", "190 mm", "200 mm", "203 mm", "205 mm",
            "210 mm", "215 mm", "240 mm", "250 mm", "254 mm", "255 mm",
            "260 mm", "270 mm", "280 mm", "300 mm", "305 mm", "320 mm",
            "350 mm", "355 mm", "360 mm", "375 mm", "380 mm", "400 mm",
            "420 mm", "450 mm", "480 mm", "500 mm", "600 mm", "610 mm",
            "720 mm", "800 mm", "1000 mm", "1200 mm", "1500 mm", "2000 mm" };

    public static final String SUBJECT_DISTANCE[] = { "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "20", "30", "40", "50", "60", "70", "80",
            "90", "100", "110", "120", "130", "140", "150", "160", "170",
            "180", "190", "200", "210", "220", "230", "240", "250", "260",
            "270", "280", "290", "300", "310", "320", "330", "340", "350",
            "360", "370", "380", "390", "400", "410", "420", "430", "440",
            "450", "460", "470", "480", "490", "500", "510", "520", "530",
            "540", "550", "560", "570", "580", "590", "600", "610", "620",
            "630", "640", "650", "660", "670", "680", "690", "700", "710",
            "720", "730", "740", "750", "760", "770", "780", "790", "800",
            "810", "820", "830", "840", "850", "860", "870", "880", "890",
            "900", "910", "920", "930", "940", "950", "960", "970", "980",
            "990", "1000" };

    public static List<String> getValues(String array[], int minIndex, int maxIndex) {
        List<String> list = new ArrayList<>();

        list.addAll(Arrays.asList(array).subList(minIndex, maxIndex + 1));
        
        return list;
    }

    public static int getApertureIndex(String aperture) {
        for (int index = 0; index < APERTURE_LIST.length; index++) {
            if (APERTURE_LIST[index].equals(aperture)) {
                return index;
            }
        }
        
        return -1;
    }
}
