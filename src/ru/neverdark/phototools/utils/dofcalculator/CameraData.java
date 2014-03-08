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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import ru.neverdark.phototools.db.DbAdapter;
import ru.neverdark.phototools.db.UserCamerasRecord;

/**
 * Class CameraData
 * Contains data models, vendor and circle of confusion for each camera models
 */
public class CameraData {
    /**
     * Camera vendors enum
     */
    public static enum Vendor {
        CANON, 
        FUJIFILM,
        NIKON, 
        OLYMPUS,
        PENTAX, 
        SONY,
        USER
    }
    /** Multiply factor for CoC calculation */
    private final BigDecimal MULTIPLY_FACTOR = new BigDecimal("3.065333276");
    /** camera model */
    private String mModel;
    /** circle of confusion = pixel size multiply to MULTIPLY_FACTOR */
    private BigDecimal mCoc;
    /** camera vendor */
    private Vendor mVendor;

    /** DATABASE contains all cameras */
    private static final Map<Vendor, List<CameraData>> DATABASE = new HashMap<Vendor, List<CameraData>>();

    static {
        List<CameraData> canonCameras = new ArrayList<CameraData>();
        canonCameras.add(new CameraData(Vendor.CANON, "1Ds", new BigDecimal("35.8"), new BigDecimal("4064")));
        canonCameras.add(new CameraData(Vendor.CANON, "1Ds Mark II", new BigDecimal("36"), new BigDecimal("4992")));
        canonCameras.add(new CameraData(Vendor.CANON, "1Ds Mark III", new BigDecimal("36"), new BigDecimal("5616")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D X", new BigDecimal("36"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D C", new BigDecimal("36"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D", new BigDecimal("28.7"), new BigDecimal("2464")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D Mark II", new BigDecimal("28.7"), new BigDecimal("3504")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D Mark II N", new BigDecimal("28.7"), new BigDecimal("3504")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D Mark III", new BigDecimal("28.1"), new BigDecimal("3888")));
        canonCameras.add(new CameraData(Vendor.CANON, "1D Mark IV", new BigDecimal("27.9"), new BigDecimal("4896")));
        canonCameras.add(new CameraData(Vendor.CANON, "5D", new BigDecimal("35.8"), new BigDecimal("4368")));
        canonCameras.add(new CameraData(Vendor.CANON, "5D Mark II", new BigDecimal("36"), new BigDecimal("5616")));
        canonCameras.add(new CameraData(Vendor.CANON, "5D Mark III", new BigDecimal("36"), new BigDecimal("5760")));
        canonCameras.add(new CameraData(Vendor.CANON, "7D", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "6D", new BigDecimal("35.8"), new BigDecimal("5472")));
        canonCameras.add(new CameraData(Vendor.CANON, "D30", new BigDecimal("22.7"), new BigDecimal("2160")));
        canonCameras.add(new CameraData(Vendor.CANON, "D60", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData(Vendor.CANON, "10D", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData(Vendor.CANON, "20D", new BigDecimal("22.5"), new BigDecimal("3520")));
        canonCameras.add(new CameraData(Vendor.CANON, "30D", new BigDecimal("22.5"), new BigDecimal("3504")));
        canonCameras.add(new CameraData(Vendor.CANON, "40D", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData(Vendor.CANON, "50D", new BigDecimal("22.3"), new BigDecimal("4752")));
        canonCameras.add(new CameraData(Vendor.CANON, "60D", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "70D", new BigDecimal("22.5"), new BigDecimal("5472")));
        canonCameras.add(new CameraData(Vendor.CANON, "300D / Rebel / Kiss", new BigDecimal("22.7"), new BigDecimal("3072")));
        canonCameras.add(new CameraData(Vendor.CANON, "350D / Rebel XT / Kiss N", new BigDecimal("22.2"), new BigDecimal("3456")));
        canonCameras.add(new CameraData(Vendor.CANON, "400D / Rebel XTi / Kiss X", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData(Vendor.CANON, "450D / Rebel XSi / Kiss X2", new BigDecimal("22.2"), new BigDecimal("4272")));
        canonCameras.add(new CameraData(Vendor.CANON, "500D / Rebel T1i / Kiss X3", new BigDecimal("22.3"), new BigDecimal("4752")));
        canonCameras.add(new CameraData(Vendor.CANON, "550D / Rebel T2i / Kiss X4", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "600D / Rebel T3i / Kiss X5", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "650D / Rebel T4i / Kiss X6i", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "700D / Rebel T5i / Kiss X7i", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "100D / Rebel SL1 / Kiss X7", new BigDecimal("22.3"), new BigDecimal("5184")));
        canonCameras.add(new CameraData(Vendor.CANON, "1000D / Rebel XS / Kiss F", new BigDecimal("22.2"), new BigDecimal("3888")));
        canonCameras.add(new CameraData(Vendor.CANON, "1100D / Rebel T3 / Kiss X50", new BigDecimal("22.2"), new BigDecimal("4272")));
        DATABASE.put(Vendor.CANON, canonCameras);
        
        List<CameraData> fujiCameras = new ArrayList<CameraData>();
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "Fujix DS-560", new BigDecimal("11"), new BigDecimal("1280")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "Fujix DS-565", new BigDecimal("11"), new BigDecimal("1280")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix IS Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix S1 Pro", new BigDecimal("23"), new BigDecimal("3040")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix S2 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix S3 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix S3 Pro UVIR", new BigDecimal("23"), new BigDecimal("4256")));
        fujiCameras.add(new CameraData(Vendor.FUJIFILM, "FinePix S5 Pro", new BigDecimal("23"), new BigDecimal("4256")));
        DATABASE.put(Vendor.FUJIFILM, fujiCameras);
        
        List<CameraData> nikonCameras = new ArrayList<CameraData>();
        nikonCameras.add(new CameraData(Vendor.NIKON, "D1", new BigDecimal("23.7"), new BigDecimal("2000")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D1X", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D2X", new BigDecimal("23.7"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D2Xs", new BigDecimal("23.7"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3X", new BigDecimal("35.9"), new BigDecimal("6048")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D1H", new BigDecimal("23.7"), new BigDecimal("2000")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D2H", new BigDecimal("23.3"), new BigDecimal("2464")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D2HS", new BigDecimal("23.3"), new BigDecimal("2464")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3S", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D4", new BigDecimal("36"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D700", new BigDecimal("36"), new BigDecimal("4256")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D800 / D800E", new BigDecimal("35.9"), new BigDecimal("7360")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D100", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D200", new BigDecimal("23.7"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D300", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D300S", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D600", new BigDecimal("35.9"), new BigDecimal("6016")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D70", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D70s", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D80", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D90", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D7000", new BigDecimal("23.6"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D7100", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D50", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D40X", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D60", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D5000", new BigDecimal("23.6"), new BigDecimal("4288")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D5100", new BigDecimal("23.6"), new BigDecimal("4928")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D5200", new BigDecimal("23.5"), new BigDecimal("6000")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D40", new BigDecimal("23.7"), new BigDecimal("3008")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3000", new BigDecimal("23.6"), new BigDecimal("3872")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3100", new BigDecimal("23.1"), new BigDecimal("4608")));
        nikonCameras.add(new CameraData(Vendor.NIKON, "D3200", new BigDecimal("23.2"), new BigDecimal("6016")));
        DATABASE.put(Vendor.NIKON, nikonCameras);

        List<CameraData> olympusCameras = new ArrayList<CameraData>();
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-1", new BigDecimal("17.3"), new BigDecimal("2560")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-10", new BigDecimal("11"), new BigDecimal("2240")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-20", new BigDecimal("11"), new BigDecimal("2560")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-3", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-30", new BigDecimal("17.3"), new BigDecimal("4032")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-300", new BigDecimal("17.3"), new BigDecimal("3264")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-330", new BigDecimal("17.3"), new BigDecimal("3136")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-400", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-410", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-420", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-450", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-5", new BigDecimal("17.3"), new BigDecimal("4032")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-500", new BigDecimal("17.3"), new BigDecimal("3264")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-510", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-520", new BigDecimal("17.3"), new BigDecimal("3648")));
        olympusCameras.add(new CameraData(Vendor.OLYMPUS, "E-620", new BigDecimal("17.3"), new BigDecimal("4032")));
        DATABASE.put(Vendor.OLYMPUS, olympusCameras);
        
        List<CameraData> pentaxCameras = new ArrayList<CameraData>();
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K100D", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K110D", new BigDecimal("23.5"), new BigDecimal("3008")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K10D", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K200D", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K20D", new BigDecimal("23.4"), new BigDecimal("4672")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-m / K2000", new BigDecimal("23.5"), new BigDecimal("3872")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-7", new BigDecimal("23.4"), new BigDecimal("4672")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-x", new BigDecimal("23.6"), new BigDecimal("4288")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-r", new BigDecimal("23.6"), new BigDecimal("4288")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-01", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-30", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5 II", new BigDecimal("23.7"), new BigDecimal("4928")));
        pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-3", new BigDecimal("23.5"), new BigDecimal("6016")));
        DATABASE.put(Vendor.PENTAX, pentaxCameras);
        
        List<CameraData> sonyCameras = new ArrayList<CameraData>();
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A900", new BigDecimal("35.9"), new BigDecimal("6048")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A850", new BigDecimal("35.9"), new BigDecimal("6048")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A100", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A700", new BigDecimal("23.5"), new BigDecimal("4288")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A200", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A300", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A350", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A230", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A330", new BigDecimal("23.6"), new BigDecimal("3872")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A380", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A500", new BigDecimal("23.5"), new BigDecimal("4277")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A550", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A450", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A290", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A390", new BigDecimal("23.6"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A560", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A580", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A33", new BigDecimal("23.5"), new BigDecimal("4592")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A35", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A37", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A55", new BigDecimal("23.5"), new BigDecimal("4912")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A57", new BigDecimal("23.5"), new BigDecimal("5456")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A65", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A77", new BigDecimal("23.5"), new BigDecimal("6000")));
        sonyCameras.add(new CameraData(Vendor.SONY, "SLT-A99", new BigDecimal("35.8"), new BigDecimal("6000")));
        DATABASE.put(Vendor.SONY, sonyCameras);
        
        List<CameraData> userCameras = new ArrayList<CameraData>();
        DATABASE.put(Vendor.USER, userCameras);
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
        ArrayList<String> list = new ArrayList<String>();

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
        List<UserCamerasRecord> list = new ArrayList<UserCamerasRecord>();
        DbAdapter adapter = new DbAdapter(context).open();
        adapter.getUserCameras().fetchAllCameras(list);
        adapter.close();
        
        DATABASE.get(Vendor.USER).clear();
        Vendor vendor = Vendor.USER;
        
        for(UserCamerasRecord record: list) {
            String model = record.getCameraName();
            
            if (record.isCustomCoc() == false) {
                BigDecimal sensorWidth = new BigDecimal(record.getSensorWidth());
                BigDecimal resolutionWidth = new BigDecimal(record.getResolutionWidth());
                
                DATABASE.get(vendor).add(new CameraData(vendor, model, sensorWidth, resolutionWidth));
            } else {
                BigDecimal coc = new BigDecimal(record.getCoc());
                
                DATABASE.get(vendor).add(new CameraData(vendor, model, coc));
            }
        }
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
                return cameraData.getCoc();
            }
        }
        return null;
    }

    /**
     * Function get vendors array
     * 
     * @return vendor array
     */
    public static Vendor[] getVendors() {
        return Vendor.values();
    }

    /**
     * Class constructor
     * 
     * @param vendor
     *            - camera vendor
     * @param model
     *            - camera model
     * @param sensorWidth
     *            - sensor width
     * @param maxWidthResolution
     *            - maximum width resolution in pixel
     */
    public CameraData(Vendor vendor, String model, BigDecimal sensorWidth,
            BigDecimal maxWidthResolution) {
        BigDecimal pixelSize = sensorWidth.divide(maxWidthResolution, 10,
                RoundingMode.HALF_UP);
        mCoc = pixelSize.multiply(MULTIPLY_FACTOR);
        mModel = model;
        mVendor = vendor;
    }
    
    /**
     * Class constructor
     * 
     * @param vendor
     *            camera vendor
     * @param model
     *            camera model
     * @param coc
     *            custom circle of confusion
     */
    public CameraData(Vendor vendor, String model, BigDecimal coc) {
        mModel = model;
        mVendor = vendor;
        mCoc = coc;
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
     * Function get camera vendor
     * 
     * @return camera vendor
     */
    public Vendor getVendor() {
        return mVendor;
    }
}
