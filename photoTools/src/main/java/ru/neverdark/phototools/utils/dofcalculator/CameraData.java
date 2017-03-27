/*******************************************************************************
 * Copyright (C) 2013-2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
 ******************************************************************************/
package ru.neverdark.phototools.utils.dofcalculator;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.UserCamerasRecord;
import ru.neverdark.phototools.utils.Log;

/**
 * Class CameraData
 * Contains data models, vendor and circle of confusion for each camera models
 */
public class CameraData {
    /**
     * DATABASE contains all cameras
     */
    private static final Map<Vendor, List<CameraData>> DATABASE = new HashMap<>();

    static {
        List<CameraData> canonCameras = new ArrayList<>();
        canonCameras.add(new CameraData("1Ds", new BigDecimal("35.8"), new BigDecimal("4064")));
        canonCameras.add(new CameraData("1Ds Mark II", new BigDecimal("36"), new BigDecimal("4992")));
        canonCameras.add(new CameraData("1Ds Mark III", new BigDecimal("36"), new BigDecimal("5616")));
        canonCameras.add(new CameraData("1D X", new BigDecimal("36"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("1D C", new BigDecimal("36"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("1D X Mark II", new BigDecimal("35.9"), new BigDecimal("5472")));
        canonCameras.add(new CameraData("1D", new BigDecimal("28.7"), new BigDecimal("2464")));
        canonCameras.add(new CameraData("1D Mark II", new BigDecimal("28.7"), new BigDecimal("3504")));
        canonCameras.add(new CameraData("1D Mark II N", new BigDecimal("28.7"), new BigDecimal("3504")));
        canonCameras.add(new CameraData("1D Mark III", new BigDecimal("28.1"), new BigDecimal("3888")));
        canonCameras.add(new CameraData("1D Mark IV", new BigDecimal("27.9"), new BigDecimal("4896")));
        canonCameras.add(new CameraData("5D", new BigDecimal("35.8"), new BigDecimal("4368")));
        canonCameras.add(new CameraData("5D Mark II", new BigDecimal("36"), new BigDecimal("5616")));
        canonCameras.add(new CameraData("5D Mark III", new BigDecimal("36"), new BigDecimal("5760")));
        canonCameras.add(new CameraData("5D Mark IV", new BigDecimal("36"), new BigDecimal("6720")));
        canonCameras.add(new CameraData("5DS / 5D R", new BigDecimal("36"), new BigDecimal("8688")));
        canonCameras.add(new CameraData("7D", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("7D Mark II", new BigDecimal("22.3"), new BigDecimal("5472")));
        canonCameras.add(new CameraData("6D", new BigDecimal("35.8"), new BigDecimal("5472")));
        canonCameras.add(new CameraData("D30", new BigDecimal("22.7"), new BigDecimal("2160")));
        canonCameras.add(new CameraData("D60", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData("10D", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData("20D", new BigDecimal("22.5"), new BigDecimal("3520")));
        canonCameras.add(new CameraData("30D", new BigDecimal("22.5"), new BigDecimal("3504")));
        canonCameras.add(new CameraData("40D", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData("50D", new BigDecimal("22.3"), new BigDecimal("4752")));
        canonCameras.add(new CameraData("60D", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("70D", new BigDecimal("22.5"), new BigDecimal("5472")));
        canonCameras.add(new CameraData("80D", new BigDecimal("22.5"), new BigDecimal("6000")));
        canonCameras.add(new CameraData("300D / Rebel / Kiss", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData("350D / Rebel XT / Kiss N", new BigDecimal("22.2"), new BigDecimal("3456")));
        canonCameras.add(new CameraData("400D / Rebel XTi / Kiss X", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData("450D / Rebel XSi / Kiss X2", new BigDecimal("22.2"), new BigDecimal("4272")));
        canonCameras.add(new CameraData("500D / Rebel T1i / Kiss X3", new BigDecimal("22.3"), new BigDecimal("4752")));
        canonCameras.add(new CameraData("550D / Rebel T2i / Kiss X4", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("600D / Rebel T3i / Kiss X5", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("650D / Rebel T4i / Kiss X6i", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("700D / Rebel T5i / Kiss X7i", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("750D / Rebel T6i / Kiss X8i", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonCameras.add(new CameraData("800D / Rebel T7i / Kiss X9i", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonCameras.add(new CameraData("760D / Rebel T6s / 8000D", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonCameras.add(new CameraData("77D / EOS 9000D", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonCameras.add(new CameraData("100D / Rebel SL1 / Kiss X7", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("1000D / Rebel XS / Kiss F", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData("1100D / Rebel T3 / Kiss X50", new BigDecimal("22.2"), new BigDecimal("4272")));
        canonCameras.add(new CameraData("1200D/EOS Rebel T5/EOS Kiss X70", new BigDecimal("22.2"), new BigDecimal("5184")));
        canonCameras.add(new CameraData("1300D/EOS Rebel T6/EOS Kiss X80", new BigDecimal("22.2"), new BigDecimal("5184")));
        DATABASE.put(Vendor.CANON, canonCameras);

        List<CameraData> canonMilcCameras = new ArrayList<>();
        canonMilcCameras.add(new CameraData("M", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonMilcCameras.add(new CameraData("M2", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonMilcCameras.add(new CameraData("M3", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonMilcCameras.add(new CameraData("M5", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonMilcCameras.add(new CameraData("M6", new BigDecimal("22.3"), new BigDecimal("6000")));
        canonMilcCameras.add(new CameraData("M10", new BigDecimal("22.3"), new BigDecimal("5184")));
        DATABASE.put(Vendor.CANON_MILC, canonMilcCameras);

        List<CameraData> fujiCameras = new ArrayList<>();
        fujiCameras.add(new CameraData("Fujix DS-560", new BigDecimal("11"), new BigDecimal("1280")));
        fujiCameras.add(new CameraData("Fujix DS-565", new BigDecimal("11"), new BigDecimal("1280")));
        fujiCameras.add(new CameraData("FinePix IS Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData("FinePix S1 Pro", new BigDecimal("23"), new BigDecimal("3040")));
        fujiCameras.add(new CameraData("FinePix S2 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData("FinePix S3 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData("FinePix S3 Pro UVIR", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData("FinePix S5 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        DATABASE.put(Vendor.FUJIFILM, fujiCameras);

        List<CameraData> fujifilmMilc = new ArrayList<>();
        fujifilmMilc.add(new CameraData("X-T1", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-T2", new BigDecimal("23.6"), new BigDecimal("6000")));
        fujifilmMilc.add(new CameraData("X-T10", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-T20", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-Pro1", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-Pro2", new BigDecimal("23.6"), new BigDecimal("6000")));
        fujifilmMilc.add(new CameraData("X-E1", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-E2", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-E2S", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-M1", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-A1", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-A2", new BigDecimal("23.6"), new BigDecimal("4896")));
        fujifilmMilc.add(new CameraData("X-A3", new BigDecimal("23.6"), new BigDecimal("6000")));
        fujifilmMilc.add(new CameraData("X-A10", new BigDecimal("23.6"), new BigDecimal("4896")));
        DATABASE.put(Vendor.FUJIFILM_MILC, fujifilmMilc);

        List<CameraData> nikonCameras = new ArrayList<>();
        nikonCameras.add(new CameraData("D1", new BigDecimal("23.7"), new BigDecimal("2000")));
        nikonCameras.add(new CameraData("D1X", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D2X", new BigDecimal("23.7"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D2Xs", new BigDecimal("23.7"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D3X", new BigDecimal("35.9"), new BigDecimal("6048")));
        nikonCameras.add(new CameraData("D1H", new BigDecimal("23.7"), new BigDecimal("2000")));
        nikonCameras.add(new CameraData("D2H", new BigDecimal("23.3"), new BigDecimal("2464")));
        nikonCameras.add(new CameraData("D2HS", new BigDecimal("23.3"), new BigDecimal("2464")));
        nikonCameras.add(new CameraData("D3", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData("D3S", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData("D4", new BigDecimal("36"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData("D4S", new BigDecimal("36"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData("D5", new BigDecimal("36"), new BigDecimal("5568")));
        nikonCameras.add(new CameraData("D700", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData("D750", new BigDecimal("36"), new BigDecimal("6016")));
        nikonCameras.add(new CameraData("D800 / D800E", new BigDecimal("35.9"), new BigDecimal("7360")));
        nikonCameras.add(new CameraData("D810 / D810A", new BigDecimal("35.9"), new BigDecimal("7360")));
        nikonCameras.add(new CameraData("DF", new BigDecimal("36"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData("D100", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D200", new BigDecimal("23.7"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData("D300", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D300S", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D500", new BigDecimal("23.6"), new BigDecimal("5568")));
        nikonCameras.add(new CameraData("D600", new BigDecimal("35.9"), new BigDecimal("6016")));
        nikonCameras.add(new CameraData("D610", new BigDecimal("35.9"), new BigDecimal("6016")));
        nikonCameras.add(new CameraData("D70", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D70s", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D80", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData("D90", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D7000", new BigDecimal("23.6"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData("D7100", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D7200", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D50", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D40X", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData("D60", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData("D5000", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData("D5100", new BigDecimal("23.6"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData("D5200", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D5300", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D5500", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D5600", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D40", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData("D3000", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData("D3100", new BigDecimal("23.1"), new BigDecimal("4608")));
        nikonCameras.add(new CameraData("D3200", new BigDecimal("23.2"), new BigDecimal("6016")));
        nikonCameras.add(new CameraData("D3300", new BigDecimal("23.2"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData("D3400", new BigDecimal("23.5"), new BigDecimal("6000")));
        DATABASE.put(Vendor.NIKON, nikonCameras);

        List<CameraData> nikonMilc = new ArrayList<>();
        nikonMilc.add(new CameraData("1 AW1", new BigDecimal("13.2"), new BigDecimal("4608")));
        nikonMilc.add(new CameraData("1 V1", new BigDecimal("13.2"), new BigDecimal("3906")));
        nikonMilc.add(new CameraData("1 V2", new BigDecimal("13.2"), new BigDecimal("4608")));
        nikonMilc.add(new CameraData("1 V3", new BigDecimal("13.2"), new BigDecimal("5232")));
        nikonMilc.add(new CameraData("1 J1", new BigDecimal("13.2"), new BigDecimal("3872")));
        nikonMilc.add(new CameraData("1 J2", new BigDecimal("13.2"), new BigDecimal("3872")));
        nikonMilc.add(new CameraData("1 J3", new BigDecimal("13.2"), new BigDecimal("4608")));
        nikonMilc.add(new CameraData("1 J4", new BigDecimal("13.2"), new BigDecimal("5232")));
        nikonMilc.add(new CameraData("1 J5", new BigDecimal("13.2"), new BigDecimal("5568")));
        nikonMilc.add(new CameraData("1 S1", new BigDecimal("13.2"), new BigDecimal("3872")));
        nikonMilc.add(new CameraData("1 S2", new BigDecimal("13.2"), new BigDecimal("4592")));
        DATABASE.put(Vendor.NIKON_MILC, nikonMilc);

        List<CameraData> olympusCameras = new ArrayList<>();
        olympusCameras.add(new CameraData("E-1", new BigDecimal("17.3"), new BigDecimal("2560")));
        olympusCameras.add(new CameraData("E-10", new BigDecimal("11"), new BigDecimal("2240")));
        olympusCameras.add(new CameraData("E-20", new BigDecimal("11"), new BigDecimal("2560")));
        olympusCameras.add(new CameraData("E-3", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-30", new BigDecimal("17.3"), new BigDecimal("4032")));
        olympusCameras.add(new CameraData("E-300", new BigDecimal("17.3"), new BigDecimal("3264")));
        olympusCameras.add(new CameraData("E-330", new BigDecimal("17.3"), new BigDecimal("3136")));
        olympusCameras.add(new CameraData("E-400", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-410", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-420", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-450", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-5", new BigDecimal("17.3"), new BigDecimal("4032")));
        olympusCameras.add(new CameraData("E-500", new BigDecimal("17.3"), new BigDecimal("3264")));
        olympusCameras.add(new CameraData("E-510", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-520", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData("E-620", new BigDecimal("17.3"), new BigDecimal("4032")));
        DATABASE.put(Vendor.OLYMPUS, olympusCameras);

        List<CameraData> pentaxCameras = new ArrayList<>();
        pentaxCameras.add(new CameraData("645D", new BigDecimal("44"), new BigDecimal("7264")));
        pentaxCameras.add(new CameraData("645Z", new BigDecimal("44"), new BigDecimal("8256")));
        pentaxCameras.add(new CameraData("K-1", new BigDecimal("35.9"), new BigDecimal("7360")));
        pentaxCameras.add(new CameraData("K-3", new BigDecimal("23.5"), new BigDecimal("6016")));
        pentaxCameras.add(new CameraData("K-3 II", new BigDecimal("23.5"), new BigDecimal("6016")));
        pentaxCameras.add(new CameraData("KP", new BigDecimal("23.5"), new BigDecimal("6016")));
        pentaxCameras.add(new CameraData("K-7", new BigDecimal("23.4"), new BigDecimal("4672")));
        pentaxCameras.add(new CameraData("K-5", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData("K-5 II", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData("*ist D", new BigDecimal("23.7"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("K10D", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData("K20D", new BigDecimal("23.4"), new BigDecimal("4672")));
        pentaxCameras.add(new CameraData("K100D", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("K200D", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData("K-30", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData("K-50", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData("K-70", new BigDecimal("23.5"), new BigDecimal("6000")));
        pentaxCameras.add(new CameraData("K-S1", new BigDecimal("23.5"), new BigDecimal("5472")));
        pentaxCameras.add(new CameraData("K-S2", new BigDecimal("23.5"), new BigDecimal("5472")));
        pentaxCameras.add(new CameraData("*ist DS", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("*ist DS2", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("K-r", new BigDecimal("23.6"), new BigDecimal("4288")));
        pentaxCameras.add(new CameraData("K-500", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData("*ist DL", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("DL2", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("K110D", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData("K-m / K2000", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData("K-x", new BigDecimal("23.6"), new BigDecimal("4288")));
        pentaxCameras.add(new CameraData("K-01", new BigDecimal("23.7"), new BigDecimal("4928")));
        DATABASE.put(Vendor.PENTAX, pentaxCameras);

        List<CameraData> pentaxMilcCameras = new ArrayList<>();
        pentaxMilcCameras.add(new CameraData("Q", new BigDecimal("6.17"), new BigDecimal("4000")));
        pentaxMilcCameras.add(new CameraData("Q10", new BigDecimal("6.17"), new BigDecimal("4000")));
        pentaxMilcCameras.add(new CameraData("Q7", new BigDecimal("7.44"), new BigDecimal("4000")));
        pentaxMilcCameras.add(new CameraData("Q-S1", new BigDecimal("7.44"), new BigDecimal("4000")));
        DATABASE.put(Vendor.PENTAX_MILC, pentaxMilcCameras);

        List<CameraData> samsungMilc = new ArrayList<>();
        samsungMilc.add(new CameraData("NX1", new BigDecimal("23.5"), new BigDecimal("6480")));
        samsungMilc.add(new CameraData("NX10", new BigDecimal("23.4"), new BigDecimal("4592")));
        samsungMilc.add(new CameraData("NX11", new BigDecimal("23.4"), new BigDecimal("4592")));
        samsungMilc.add(new CameraData("NX20", new BigDecimal("23.4"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX30", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX100", new BigDecimal("23.4"), new BigDecimal("4592")));
        samsungMilc.add(new CameraData("NX200", new BigDecimal("23.4"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX210", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX300", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX300M", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX500", new BigDecimal("23.5"), new BigDecimal("6480")));
        samsungMilc.add(new CameraData("Galaxy NX", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX2000", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX3000", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX3300", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX5", new BigDecimal("23.5"), new BigDecimal("4592")));
        samsungMilc.add(new CameraData("NX1000", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX1100", new BigDecimal("23.5"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX mini", new BigDecimal("13.2"), new BigDecimal("5472")));
        samsungMilc.add(new CameraData("NX mini2", new BigDecimal("13.2"), new BigDecimal("5472")));
        DATABASE.put(Vendor.SAMSUNG_MILC, samsungMilc);

        List<CameraData> sonyCameras = new ArrayList<>();
        sonyCameras.add(new CameraData("DSLR-A900", new BigDecimal("35.9"), new BigDecimal("6048")));
        sonyCameras.add(new CameraData("DSLR-A850", new BigDecimal("35.9"), new BigDecimal("6048")));
        sonyCameras.add(new CameraData("DSLR-A100", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData("DSLR-A700", new BigDecimal("23.5"), new BigDecimal("4288")));
        sonyCameras.add(new CameraData("DSLR-A200", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData("DSLR-A300", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData("DSLR-A350", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A230", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData("DSLR-A330", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData("DSLR-A380", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A500", new BigDecimal("23.5"), new BigDecimal("4277")));
        sonyCameras.add(new CameraData("DSLR-A550", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A450", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A290", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A390", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A560", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("DSLR-A580", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData("SLT-A33", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData("SLT-A35", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData("SLT-A37", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData("SLT-A55", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData("SLT-A57", new BigDecimal("23.5"), new BigDecimal("5456")));
        sonyCameras.add(new CameraData("SLT-A58", new BigDecimal("23.5"), new BigDecimal("5456")));
        sonyCameras.add(new CameraData("SLT-A65", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData("SLT-A68", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData("SLT-A77", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData("SLT-A77 II", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData("SLT-A99", new BigDecimal("35.8"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData("SLT-A99 II", new BigDecimal("35.8"), new BigDecimal("7952")));
        DATABASE.put(Vendor.SONY, sonyCameras);

        List<CameraData> sonyMilcCameras = new ArrayList<>();
        sonyMilcCameras.add(new CameraData("α7S", new BigDecimal("35.8"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α7S II", new BigDecimal("35.6"), new BigDecimal("4240")));
        sonyMilcCameras.add(new CameraData("α7R", new BigDecimal("35.8"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α7R II", new BigDecimal("35.9"), new BigDecimal("7974")));
        sonyMilcCameras.add(new CameraData("α7", new BigDecimal("35.8"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α7 II", new BigDecimal("35.8"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("NEX-7", new BigDecimal("23.4"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("NEX-6", new BigDecimal("23.4"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("α6000", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α6300", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α6500", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("NEX-5 / NEX-5C", new BigDecimal("23.4"), new BigDecimal("4592")));
        sonyMilcCameras.add(new CameraData("NEX-5N", new BigDecimal("23.4"), new BigDecimal("4592")));
        sonyMilcCameras.add(new CameraData("NEX-5R", new BigDecimal("23.4"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("NEX-5T", new BigDecimal("23.4"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("NEX-3 / NEX-3C", new BigDecimal("23.4"), new BigDecimal("4592")));
        sonyMilcCameras.add(new CameraData("NEX-C3", new BigDecimal("23.4"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("NEX-F3", new BigDecimal("23.4"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("NEX-3N", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyMilcCameras.add(new CameraData("α5000", new BigDecimal("23.2"), new BigDecimal("5456")));
        sonyMilcCameras.add(new CameraData("α5100", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyMilcCameras.add(new CameraData("α3000", new BigDecimal("23.5"), new BigDecimal("5456")));
        sonyMilcCameras.add(new CameraData("QX1", new BigDecimal("23.2"), new BigDecimal("5456")));
        DATABASE.put(Vendor.SONY_MILC, sonyMilcCameras);

        /* user cameras */
        DATABASE.put(Vendor.USER, new ArrayList<CameraData>());
    }

    /** camera model */
    private String mModel;
    /** circle of confusion = pixel size multiply to MULTIPLY_FACTOR */
    private BigDecimal mCoc;

    /**
     * Class constructor
     *
     * @param model  camera model
     * @param coc    custom circle of confusion
     */
    public CameraData(String model, BigDecimal coc) {
        mModel = model;
        mCoc = coc;
    }

    /**
     * Class constructor
     *
     * @param model
     *            - camera model
     * @param sensorWidth
     *            - sensor width
     * @param maxWidthResolution
     *            - maximum width resolution in pixel
     */
    public CameraData(String model, BigDecimal sensorWidth,
                      BigDecimal maxWidthResolution) {
        BigDecimal pixelSize;
        if (maxWidthResolution.signum() == 1) {
            pixelSize = sensorWidth.divide(maxWidthResolution, 10,
                    RoundingMode.HALF_UP);
        } else {
            pixelSize = new BigDecimal("0.0");
        }
        /* Multiply factor for CoC calculation */
        BigDecimal MULTIPLY_FACTOR = new BigDecimal("3.065333276");
        mCoc = pixelSize.multiply(MULTIPLY_FACTOR);
        mModel = model;
    }

    /**
     * Function get circle of confusion for special camera
     *
     * @param vendor
     *            camera vendor
     * @param camera
     *            camera model
     * @return circle of confusion or null if camera not found
     */
    public static BigDecimal getCocForCamera(Vendor vendor, String camera) {
        List<CameraData> camerasForVendor = DATABASE.get(vendor);
        for (CameraData cameraData : camerasForVendor) {
            if (cameraData.getModel().equals(camera)) {
                BigDecimal coc = cameraData.getCoc();
                return coc.signum() != 0 ? coc: null;
            }
        }
        return null;
    }

    /**
     * Function get cameras array by vendor
     *
     * @param vendor
     *            camera vendor
     * @param context
     *            application context
     * @return array contain cameras model for specified vendor
     */
    public static String[] getCameraByVendor(Vendor vendor, Context context) {
        ArrayList<String> list = new ArrayList<>();

        if (vendor == Vendor.USER) {
            loadDataFromDb(context);
        }

        for (CameraData cameraData : DATABASE.get(vendor)) {
            list.add(cameraData.getModel());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Loads user cameras from database
     */
    private static void loadDataFromDb(Context context) {
        Log.enter();
        List<UserCamerasRecord> list = new ArrayList<>();
        DbAdapter adapter = new DbAdapter(context).open();
        adapter.getUserCameras().fetchAllCameras(list);
        adapter.close();

        DATABASE.get(Vendor.USER).clear();
        Vendor vendor = Vendor.USER;

        for (UserCamerasRecord record : list) {
            String model = record.getCameraName();

            if (!record.isCustomCoc()) {
                BigDecimal sensorWidth = new BigDecimal(record.getSensorWidth());
                BigDecimal resolutionWidth = new BigDecimal(
                        record.getResolutionWidth());

                DATABASE.get(vendor).add(
                        new CameraData(model, sensorWidth,
                                resolutionWidth));
            } else {
                BigDecimal coc = new BigDecimal(record.getCoc());

                DATABASE.get(vendor).add(new CameraData(model, coc));
            }
        }
    }

    /**
     * Function get circle of confusion
     *
     * @return circle of confusion
     */
    public BigDecimal getCoc() {
        return mCoc;
    }

    /**
     * Function get camera model
     *
     * @return camera model
     */
    public String getModel() {
        return mModel;
    }

    /**
     * Camera vendors enum
     */
    public enum Vendor {
        CANON("Canon DSLR"),
        CANON_MILC("Canon MILC"),
        FUJIFILM("Fujifilm DSLR"),
        FUJIFILM_MILC("Fujifilm MILC"),
        NIKON("Nikon DSLR"),
        NIKON_MILC("Nikon MILC"),
        OLYMPUS("Olympus DSLR"),
        PENTAX("Pentax DSLR"),
        PENTAX_MILC("Pentax MILC"),
        SAMSUNG_MILC("Samsung MILC"),
        SONY("Sony DSLR/SLT"),
        SONY_MILC("Sony MILC"),
        USER("My cameras");

        private final String text;

        /**
         * @param text text enum item
         */
        Vendor(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
