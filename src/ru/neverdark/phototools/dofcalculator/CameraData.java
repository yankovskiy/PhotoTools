package ru.neverdark.phototools.dofcalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		NIKON, 
		PENTAX, 
		SONY
	}

	/** camera model */
	private String mModel;
	/** circle of confusion */
	private BigDecimal mCoc;
	/** camera vendor */
	private Vendor mVendor;

	/** DATABASE contains all cameras */
	private static final Map<Vendor, List<CameraData>> DATABASE = new HashMap<Vendor, List<CameraData>>();

	static {
		List<CameraData> canonCameras = new ArrayList<CameraData>();
		canonCameras.add(new CameraData(Vendor.CANON, "7D", new BigDecimal("0.012")));
		DATABASE.put(Vendor.CANON, canonCameras);
		
		List<CameraData> nikonCameras = new ArrayList<CameraData>();
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3S", new BigDecimal("0.025")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		
		List<CameraData> pentaxCameras = new ArrayList<CameraData>();
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5", new BigDecimal("0.014")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		
		List<CameraData> sonyCameras = new ArrayList<CameraData>();
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A290", new BigDecimal("0.014")));
		DATABASE.put(Vendor.SONY, sonyCameras);
	}
	
	/**
	 * Function get cameras list by vendor
	 * @param vendor - camera vendor
	 * @return list contain cameras model for specified vendor
	 */
	public static List<CameraData> getCameraByVendor(Vendor vendor) {
        return Collections.unmodifiableList(DATABASE.get(vendor));
    }

	/**
	 * Function get circle of confusion for special camera
	 * @param vendor - camera vendor
	 * @param camera - camera model
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
	 * @return vendor array
	 */
	public static Vendor[] getVendors() {
		return Vendor.values();
	}

	/**
	 * Class constructor
	 * @param vendor - camera vendor
	 * @param model - camera model
	 * @param coc - circle of confusion
	 */
	public CameraData(Vendor vendor, String model, BigDecimal coc) {
		mModel = model;
		mCoc = coc;
		mVendor = vendor;
	}
	
	/**
	 * Function get circle of confusion
	 * @return circle of confusion
	 */
	public BigDecimal getCoc() {
		return mCoc;
	}
	
    /**
     * Function get camera model
     * @return camera model
     */
    public String getModel() {
		return mModel;
	}
    
    /**
     * Function get camera vendor
     * @return camera vendor
     */
    public Vendor getVendor() {
		return mVendor;
	}
}
