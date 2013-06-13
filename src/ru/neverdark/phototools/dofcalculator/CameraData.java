package ru.neverdark.phototools.dofcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraData {
	private String mModel;
	private double mCoc;
	private Vendor mVendor;

	public CameraData(Vendor vendor, String model, double coc) {
		mModel = model;
		mCoc = coc;
		mVendor = vendor;
	}

	public String getModel() {
		return mModel;
	}

	public double getCoc() {
		return mCoc;
	}
	
	public Vendor getVendor() {
		return mVendor;
	}

	public static enum Vendor {
		CANON, 
		NIKON, 
		PENTAX, 
		SONY
	}

	private static final Map<Vendor, List<CameraData>> DATABASE = new HashMap<Vendor, List<CameraData>>();

	static {
		List<CameraData> canonCameras = new ArrayList<CameraData>();
		canonCameras.add(new CameraData(Vendor.CANON, "7D", 0.19));
		DATABASE.put(Vendor.CANON, canonCameras);
		
		List<CameraData> pentaxCameras = new ArrayList<CameraData>();
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5", 0.17));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
	}
	
	public static Vendor[] getVendors() {
		return Vendor.values();
	}
	
    public static List<CameraData> getCameraByVendor(Vendor vendor) {
        return Collections.unmodifiableList(DATABASE.get(vendor));
    }
}
