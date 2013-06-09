package ru.neverdark.phototools;

public class EvpairsCalculator {

	private int mCurrentAperturePosition;
	private int mCurrentIsoPosition;
	private int mCurrentShutterSpeedPosition;
	private int mNewAperturePosition;
	private int mNewIsoPostion;
	private int mNewShutterSpeedPosition;
	private int mIndex;
	
	public static final int INVALID_INDEX = -1;
	
	public static final String ISO_LIST[] = { "", "25", "50", "100", "200", "400", "800",
			"1600", "3200", "6400", "12800" };
	
	public static final String SHUTTER_SPEED_LIST[]  = { "", "512 min",
		"256 min", "64 min", "32 min", "16 min", "8 min", "4 min", "2 min",
		"1 min", "30 sec", "15 sec", "8 sec", "4 sec", "2 sec", "1 sec",
		"1/2 sec", "1/4 sec", "1/8 sec", "1/15 sec", "1/30 sec",
		"1/60 sec", "1/125 sec", "1/250 sec", "1/500 sec", "1/1000 sec",
		"1/2000 sec", "1/4000 sec", "1/8000 sec" };
		
	public static final String APERTURE_LIST[] = { "", "1.4", "2.0", "2.8",
		"4", "5.6", "8", "11", "16", "22", "32" };
	
	private final int EV_TABLE[][] = {
		{ -16, -17, -18, -19, -20, -21, -22, -23, -24, -25 },
		{ -15, -16, -17, -18, -19, -20, -21, -22, -23, -24 },
		{ -14, -15, -16, -17, -18, -19, -20, -21, -22, -23 },
		{ -13, -14, -15, -16, -17, -18, -19, -20, -21, -22 },
		{ -12, -13, -14, -15, -16, -17, -18, -19, -20, -21 },
		{ -11, -12, -13, -14, -15, -16, -17, -18, -19, -20 },
		{ -10, -11, -12, -13, -14, -15, -16, -17, -18, -19 },
		{ -9, -10, -11, -12, -13, -14, -15, -16, -17, -18 },
		{ -8, -9, -10, -11, -12, -13, -14, -15, -16, -17 },
		{ -7, -8, -9, -10, -11, -12, -13, -14, -15, -16 },
		{ -6, -7, -8, -9, -10, -11, -12, -13, -14, -15 },
		{ -5, -6, -7, -8, -9, -10, -11, -12, -13, -14 },
		{ -4, -5, -6, -7, -8, -9, -10, -11, -12, -13 },
		{ -3, -4, -5, -6, -7, -8, -9, -10, -11, -12 },
		{ -2, -3, -4, -5, -6, -7, -8, -9, -10, -11 },
		{ -1, -2, -3, -4, -5, -6, -7, -8, -9, -10 },
		{ 0, -1, -2, -3, -4, -5, -6, -7, -8, -9 },
		{ 1, 0, -1, -2, -3, -4, -5, -6, -7, -8 },
		{ 2, 1, 0, -1, -2, -3, -4, -5, -6, -7 },
		{ 3, 2, 1, 0, -1, -2, -3, -4, -5, -6 },
		{ 4, 3, 2, 1, 0, -1, -2, -3, -4, -5 },
		{ 5, 4, 3, 2, 1, 0, -1, -2, -3, -4 },
		{ 6, 5, 4, 3, 2, 1, 0, -1, -2, -3 },
		{ 7, 6, 5, 4, 3, 2, 1, 0, -1, -2 },
		{ 8, 7, 6, 5, 4, 3, 2, 1, 0, -1 },
		{ 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 },
		{ 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 },
		{ 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 },
		{ 12, 11, 10, 9, 8, 7, 6, 5, 4, 3 },
		{ 13, 12, 11, 10, 9, 8, 7, 6, 5, 4 },
		{ 14, 13, 12, 11, 10, 9, 8, 7, 6, 5 },
		{ 15, 14, 13, 12, 11, 10, 9, 8, 7, 6 },
		{ 16, 15, 14, 13, 12, 11, 10, 9, 8, 7 },
		{ 17, 16, 15, 14, 13, 12, 11, 10, 9, 8 },
		{ 18, 17, 16, 15, 14, 13, 12, 11, 10, 9 },
		{ 19, 18, 17, 16, 15, 14, 13, 12, 11, 10 },
		{ 20, 19, 18, 17, 16, 15, 14, 13, 12, 11 },
		{ 21, 20, 19, 18, 17, 16, 15, 14, 13, 12 },
		{ 22, 21, 20, 19, 18, 17, 16, 15, 14, 13 },
		{ 23, 22, 21, 20, 19, 18, 17, 16, 15, 14 } };
	
	private final String SHUTTERS_TABLE[][] = {
		{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "512 min", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "256 min", "512 min", "0", "0", "0", "0", "0", "0", "0", "0" },
		{ "128 min", "256 min", "512 min", "0", "0", "0", "0", "0", "0", "0" },
		{ "64 min", "128 min", "256 min", "512 min", "0", "0", "0", "0", "0", "0" },
		{ "32 min", "64 min", "128 min", "256 min", "512 min", "0", "0", "0", "0", "0" },
		{ "16 min", "32 min", "64 min", "128 min", "256 min", "512 min", "0", "0", "0", "0" },
		{ "8 min", "16 min", "32 min", "64 min", "128 min", "256 min", "512 min", "0", "0", "0" },
		{ "4 min", "8 min", "16 min", "32 min", "64 min", "128 min", "256 min", "512 min", "0", "0" },
		{ "2 min", "4 min", "8 min", "16 min", "32 min", "64 min", "128 min", "256 min", "512 min", "0" },
		{ "1 min", "2 min", "4 min", "8 min", "16 min", "32 min", "64 min", "128 min", "256 min", "512 min" },
		{ "30 sec", "1 min", "2 min", "4 min", "8 min", "16 min", "32 min", "64 min", "128 min", "256 min" },
		{ "15 sec", "30 sec", "1 min", "2 min", "4 min", "8 min", "16 min", "32 min", "64 min", "128 min" },
		{ "8 sec", "15 sec", "30 sec", "1 min", "2 min", "4 min", "8 min", "16 min", "32 min", "64 min" },
		{ "4 sec", "8 sec", "15 sec", "30 sec", "1 min", "2 min", "4 min", "8 min", "16 min", "32 min" },
		{ "2 sec", "4 sec", "8 sec", "15 sec", "30 sec", "1 min", "2 min", "4 min", "8 min", "16 min" },
		{ "1 sec", "2 sec", "4 sec", "8 sec", "15 sec", "30 sec", "1 min", "2 min", "4 min", "8 min" },
		{ "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec", "15 sec", "30 sec", "1 min", "2 min", "4 min" },
		{ "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec", "15 sec", "30 sec", "1 min", "2 min" },
		{ "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec", "15 sec", "30 sec", "1 min" },
		{ "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec", "15 sec", "30 sec" },
		{ "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec", "15 sec" },
		{ "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec", "8 sec" },
		{ "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec", "4 sec" },
		{ "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec", "2 sec" },
		{ "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec", "1 sec" },
		{ "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec", "1/2 sec" },
		{ "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec", "1/4 sec" },
		{ "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec", "1/8 sec" },
		{ "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec", "1/15 sec" },
		{ "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec", "1/30 sec" },
		{ "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec", "1/60 sec" },
		{ "0", "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec", "1/125 sec" },
		{ "0", "0", "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec", "1/250 sec" },
		{ "0", "0", "0", "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec", "1/500 sec" },
		{ "0", "0", "0", "0", "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec", "1/1000 sec" },
		{ "0", "0", "0", "0", "0", "0", "0", "1/8000 sec", "1/4000 sec", "1/2000 sec" } };
	
	
	public EvpairsCalculator(int currentAperturePosition,
			int currentIsoPosition, int currentShutterSpeedPosition,
			int newAperturePosition, int newIsoPostion,
			int newShutterSpeedPosition) {

		mCurrentAperturePosition = currentAperturePosition;
		mCurrentIsoPosition = currentIsoPosition;
		mCurrentShutterSpeedPosition = currentShutterSpeedPosition;
		
		mNewAperturePosition = newAperturePosition;
		mNewIsoPostion = newIsoPostion;
		mNewShutterSpeedPosition = newShutterSpeedPosition;
		
		mIndex = INVALID_INDEX;
	}
	
	public int calculate() {				
		if (mNewAperturePosition == 0) {
			calculateAperture();
		} else if (mNewIsoPostion == 0) {
			calculateIso();
		} else {
			calculateShutterSpeed();
		}
		
		return mIndex;
	}
	
	private void calculateAperture() {
		
	}
	
	private void calculateShutterSpeed() {
		
	}
	
	private void calculateIso() {
		
	}
}
