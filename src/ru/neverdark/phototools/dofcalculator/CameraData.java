package ru.neverdark.phototools.dofcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1Ds Mark II", new BigDecimal("36"), new BigDecimal("4992")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1Ds Mark III", new BigDecimal("36"), new BigDecimal("5616")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D X", new BigDecimal("36"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D C", new BigDecimal("36"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D", new BigDecimal("28.7"), new BigDecimal("2464")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D Mark II", new BigDecimal("28.7"), new BigDecimal("3504")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D Mark II N", new BigDecimal("28.7"), new BigDecimal("3504")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D Mark III", new BigDecimal("28.1"), new BigDecimal("3888")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1D Mark IV", new BigDecimal("27.9"), new BigDecimal("4896")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "5D", new BigDecimal("35.8"), new BigDecimal("4368")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "5D Mark II", new BigDecimal("36"), new BigDecimal("5616")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "5D Mark III", new BigDecimal("36"), new BigDecimal("5760")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "7D", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "6D", new BigDecimal("35.8"), new BigDecimal("5472")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "D30", new BigDecimal("22.7"), new BigDecimal("2160")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "D60", new BigDecimal("22.7"), new BigDecimal("3072")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "10D", new BigDecimal("22.7"), new BigDecimal("3072")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "20D", new BigDecimal("22.5"), new BigDecimal("3520")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "30D", new BigDecimal("22.5"), new BigDecimal("3504")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "40D", new BigDecimal("22.2"), new BigDecimal("3888")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "50D", new BigDecimal("22.3"), new BigDecimal("4752")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "60D", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "300D / Rebel / Kiss", new BigDecimal("22.7"), new BigDecimal("3072")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "350D / Rebel XT / Kiss N", new BigDecimal("22.2"), new BigDecimal("3456")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "400D / Rebel XTi / Kiss X", new BigDecimal("22.2"), new BigDecimal("3888")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "450D / Rebel XSi / Kiss X2", new BigDecimal("22.2"), new BigDecimal("4272")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "500D / Rebel T1i / Kiss X3", new BigDecimal("22.3"), new BigDecimal("4752")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "550D / Rebel T2i / Kiss X4", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "600D / Rebel T3i / Kiss X5", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "650D / Rebel T4i / Kiss X6i", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "700D / Rebel T5i / Kiss X7i", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "100D / Rebel SL1 / Kiss X7", new BigDecimal("22.3"), new BigDecimal("5184")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1000D / Rebel XS / Kiss F", new BigDecimal("22.2"), new BigDecimal("3888")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		canonCameras.add(new CameraData(Vendor.CANON, "1100D / Rebel T3 / Kiss X50", new BigDecimal("22.2"), new BigDecimal("4272")));
		DATABASE.put(Vendor.CANON, canonCameras);				
		
		List<CameraData> nikonCameras = new ArrayList<CameraData>();
		nikonCameras.add(new CameraData(Vendor.NIKON, "D1", new BigDecimal("23.7"), new BigDecimal("2000")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D1X", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D2X", new BigDecimal("23.7"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D2Xs", new BigDecimal("23.7"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3X", new BigDecimal("35.9"), new BigDecimal("6048")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D1H", new BigDecimal("23.7"), new BigDecimal("2000")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D2H", new BigDecimal("23.3"), new BigDecimal("2464")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D2HS", new BigDecimal("23.3"), new BigDecimal("2464")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3", new BigDecimal("36"), new BigDecimal("4256")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3S", new BigDecimal("36"), new BigDecimal("4256")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D4", new BigDecimal("36"), new BigDecimal("4928")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D700", new BigDecimal("36"), new BigDecimal("4256")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D800 / D800E", new BigDecimal("35.9"), new BigDecimal("7360")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D100", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D200", new BigDecimal("23.7"), new BigDecimal("3872")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D300", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D300S", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D600", new BigDecimal("35.9"), new BigDecimal("6016")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D70", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D70s", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D80", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D90", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D7000", new BigDecimal("23.6"), new BigDecimal("4928")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D7100", new BigDecimal("23.5"), new BigDecimal("6000")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D50", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D40X", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D60", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D5000", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D5100", new BigDecimal("23.6"), new BigDecimal("4928")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D5200", new BigDecimal("23.5"), new BigDecimal("6000")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D40", new BigDecimal("23.7"), new BigDecimal("3008")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3000", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3100", new BigDecimal("23.1"), new BigDecimal("4608")));
		DATABASE.put(Vendor.NIKON, nikonCameras);		
		nikonCameras.add(new CameraData(Vendor.NIKON, "D3200", new BigDecimal("23.2"), new BigDecimal("6016")));
		DATABASE.put(Vendor.NIKON, nikonCameras);				
		
		List<CameraData> pentaxCameras = new ArrayList<CameraData>();
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K100D", new BigDecimal("23.5"), new BigDecimal("3008")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K110D", new BigDecimal("23.5"), new BigDecimal("3008")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K10D", new BigDecimal("23.5"), new BigDecimal("3872")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K200D", new BigDecimal("23.5"), new BigDecimal("3872")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K20D", new BigDecimal("23.4"), new BigDecimal("4672")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-m / K2000", new BigDecimal("23.5"), new BigDecimal("3872")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-7", new BigDecimal("23.4"), new BigDecimal("4672")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-x", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-r", new BigDecimal("23.6"), new BigDecimal("4288")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5", new BigDecimal("23.7"), new BigDecimal("4928")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-01", new BigDecimal("23.7"), new BigDecimal("4928")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-30", new BigDecimal("23.7"), new BigDecimal("4928")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		pentaxCameras.add(new CameraData(Vendor.PENTAX, "K-5 II", new BigDecimal("23.7"), new BigDecimal("4928")));
		DATABASE.put(Vendor.PENTAX, pentaxCameras);		
		
		List<CameraData> sonyCameras = new ArrayList<CameraData>();
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A900", new BigDecimal("35.9"), new BigDecimal("6048")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A850", new BigDecimal("35.9"), new BigDecimal("6048")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A100", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A700", new BigDecimal("23.5"), new BigDecimal("4288")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A200", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A300", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A350", new BigDecimal("23.6"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A230", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A330", new BigDecimal("23.6"), new BigDecimal("3872")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A380", new BigDecimal("23.6"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A500", new BigDecimal("23.5"), new BigDecimal("4277")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A550", new BigDecimal("23.5"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A450", new BigDecimal("23.5"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A290", new BigDecimal("23.6"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A390", new BigDecimal("23.6"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A560", new BigDecimal("23.5"), new BigDecimal("4592")));
		DATABASE.put(Vendor.SONY, sonyCameras);
		sonyCameras.add(new CameraData(Vendor.SONY, "DSLR-A580", new BigDecimal("23.5"), new BigDecimal("4912")));
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
	 * @param sensorWidth - sensor width
	 * @param maxWidthResolution - maximum width resolution in pixel  
	 */
	public CameraData(Vendor vendor, String model, BigDecimal sensorWidth, BigDecimal maxWidthResolution) {
		BigDecimal pixelSize = sensorWidth.divide(maxWidthResolution, 10, RoundingMode.HALF_UP);
		mCoc = pixelSize.multiply(MULTIPLY_FACTOR);
		mModel = model;		
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
