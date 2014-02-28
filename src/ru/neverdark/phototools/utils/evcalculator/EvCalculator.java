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
package ru.neverdark.phototools.utils.evcalculator;

import java.util.ArrayList;
import java.util.Arrays;

import ru.neverdark.phototools.utils.Log;


public class EvCalculator {

    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;
    private int mIndex;
    private EvMathTable mEvTable;
    
    
    public static final int FULL_STOP = 0;
    public static final int HALF_STOP = 1;
    public static final int THIRD_STOP = 2;
    
    /** For error handling. Must not equ with any of exposition pairs. */
    public static final int INVALID_INDEX = -100;
    
    private String ISO_LIST[];
    private Double ISO_VALUE_LIST[];
    private Double APERTURE_VALUE_LIST[];
    private Double SHUTTER_VALUE_LIST[];
    
    private String SHUTTER_SPEED_LIST[];
        
    private String APERTURE_LIST[];
    
    private int EV_TABLE[][];
    
    private String SHUTTERS_TABLE[][];
    
    
    public void prepare(int currentAperturePosition,
            int currentIsoPosition, int currentShutterSpeedPosition,
            int newAperturePosition, int newIsoPostion,
            int newShutterSpeedPosition) {

        mCurrentAperturePosition = currentAperturePosition;
        mCurrentIsoPosition = currentIsoPosition;
        mCurrentShutterSpeedPosition = currentShutterSpeedPosition;

        mNewAperturePosition = newAperturePosition - 1;
        mNewIsoPostion = newIsoPostion -1;
        mNewShutterSpeedPosition = newShutterSpeedPosition  - 1;

        Log.variable("mCurrentAperturePosition", String.valueOf(mCurrentAperturePosition));
        Log.variable("mCurrentIsoPosition", String.valueOf(mCurrentIsoPosition));
        Log.variable("mCurrentShutterSpeedPosition", String.valueOf(mCurrentShutterSpeedPosition));
        
        Log.variable("mNewAperturePosition", String.valueOf(mNewAperturePosition));
        Log.variable("mNewIsoPostion", String.valueOf(mNewIsoPostion));
        Log.variable("mNewShutterSpeedPosition", String.valueOf(mNewShutterSpeedPosition));
        
        mIndex = INVALID_INDEX;
    }
    
    /**
     * Inits local arrays by EV step
     * @param evStep ev step
     */
    public void initArrays(final int evStep) {
        switch (evStep) {
        case FULL_STOP:
            //mEvTable = new EvMathTable();
            Log.message("EV FULL");
            break;
        case HALF_STOP:
            //mEvTable = new EvMathTable();
            Log.message("Ev HALF");
            break;
        case THIRD_STOP:
            //mEvTable = new  EvMathTable();
            Log.message("Ev THIRD");
            break;
        }
        
        mEvTable = new  EvMathTable();
        ISO_LIST = mEvTable.getIsoList();
        ISO_VALUE_LIST = mEvTable.getISOValues(evStep);
        APERTURE_VALUE_LIST = mEvTable.getApertureValues(evStep);
        SHUTTER_VALUE_LIST = mEvTable.getShutterValues(evStep);
        SHUTTER_SPEED_LIST = mEvTable.getShutterList();
        APERTURE_LIST = mEvTable.getApertureList();
        EV_TABLE = mEvTable.getEvTable();
        SHUTTERS_TABLE = mEvTable.getShutterTable();
    }
    
    /**
     * Gets ISO list
     * @return array contains possible ISO
     */
    public String[] getIsoList() {
    	ArrayList<String> isos = new ArrayList<String>();
    	int index = 0;
    	for (index = 0; index < ISO_VALUE_LIST.length ; index++ ) {
    		isos.add(cleanNumberToString(ISO_VALUE_LIST[index]));
    	}
        return isos.toArray(new String[isos.size()]);
    }
    
    /**
     * Gets aperture list 
     * @return array contains possible apertures
     */
    public String[] getApertureList() {
    	ArrayList<String> apertures = new ArrayList<String>();
    	int index = 0;
    	for (index = 0; index < APERTURE_VALUE_LIST.length ; index++ ) {
    		apertures.add("f/"+cleanNumberToString(APERTURE_VALUE_LIST[index]));
    	}
        return apertures.toArray(new String[apertures.size()]);
    }
    
    /**
     * Gets shutter list
     * @return array contains possible shutter speed
     */
    public String[] getShutterList() {
    	ArrayList<String> shutters = new ArrayList<String>();
    	int index = 0;
    	int flag = 0;
    	for (index = 0; index < SHUTTER_VALUE_LIST.length ; index++ ) {
    		switch(flag){
    		case 0: {
	    			if(SHUTTER_VALUE_LIST[index]==1)
	    				flag=1;
	    			else {
	    				shutters.add("1/"+cleanNumberToString(1/SHUTTER_VALUE_LIST[index])+" sec");
	    				break;
	    			}
	    		}
    		case 1: {
	    			if(SHUTTER_VALUE_LIST[index]>=60)
	    				flag=2;
	    			else {
	    				shutters.add(cleanNumberToString(SHUTTER_VALUE_LIST[index])+" sec");
	    				break;
	    			}
	    		}
    		default: shutters.add(cleanNumberToString((Double)(SHUTTER_VALUE_LIST[index]/60))+" min"); break;
    		}
    	}
        return shutters.toArray(new String[shutters.size()]);
    }
    
    private String cleanNumberToString(Double number){
    	Double cleanerNumber = Math.round(number*100)/100.0;
    	String numberToReturn = "";
    	if( cleanerNumber%1 == 0)
    		numberToReturn = String.format("%d",cleanerNumber.intValue());
		else
			numberToReturn = String.format("%s",cleanerNumber);
    	return numberToReturn;
    }
    
    /**
     * Function calculates the required value based on indices obtained in the class constructor.
     * @return index for the empty spinner or INVALID_INDEX on error
     */
    public int calculate() {
    	int apertureDifference = 0;
    	int isoDifference = 0;
    	int shutterDifference = 0;
    	
        /*if (mNewAperturePosition == -1) {
        	apertureDifference = calculateApertureDifference();
        }
        
        if (mNewIsoPostion == -1) {
        	isoDifference = calculateIsoDifference();
        }
        
        if (mNewShutterSpeedPosition == -1) {
        	shutterDifference = calculateShutterDifference();
        	
        mCurrentAperturePosition = currentAperturePosition;
        mCurrentIsoPosition = currentIsoPosition;
        mCurrentShutterSpeedPosition = currentShutterSpeedPosition;
        }*/
        
    	Log.variable("mCurrentAperturePosition", String.valueOf(mCurrentAperturePosition));
    	Log.variable("mCurrentAperturePosition Value", String.valueOf(APERTURE_VALUE_LIST[mCurrentAperturePosition]));
        Log.variable("mCurrentIsoPosition", String.valueOf(mCurrentIsoPosition));
        Log.variable("mCurrentIsoPosition Value", String.valueOf(ISO_VALUE_LIST[mCurrentIsoPosition]));
        Log.variable("mCurrentShutterSpeedPosition", String.valueOf(mCurrentShutterSpeedPosition));
        Log.variable("mCurrentShutterSpeedPosition Value", String.valueOf(SHUTTER_VALUE_LIST[mCurrentShutterSpeedPosition]));
        
        Log.variable("mNewAperturePosition", String.valueOf(mNewAperturePosition));
        if(mNewAperturePosition>=0)
        	Log.variable("mNewAperturePosition Value", String.valueOf(APERTURE_VALUE_LIST[mNewAperturePosition]));
        Log.variable("mNewIsoPostion", String.valueOf(mNewIsoPostion));
        if(mNewIsoPostion>=0)
        	Log.variable("mNewIsoPostion Value", String.valueOf(ISO_VALUE_LIST[mNewIsoPostion]));
        Log.variable("mNewShutterSpeedPosition", String.valueOf(mNewShutterSpeedPosition));
        if(mNewShutterSpeedPosition>=0)
        	Log.variable("mNewShutterSpeedPosition Value", String.valueOf(SHUTTER_VALUE_LIST[mNewShutterSpeedPosition]));

        /*if(mNewAperturePosition<0) {
        	double isoStopDifference = calculateIsoDifference();
        	double shutterStopDifference = calculateShutterDifference();
        }*/
        mIndex++;
        Log.variable("mIndex", String.valueOf(mIndex));
        
        return mIndex;
    }
    
    /**
     * Function calculates the aperture
     */
    private void calculateAperture() {
        int isoNewIndex = getIsoNewIndex();
        String shutter = SHUTTER_SPEED_LIST[mNewShutterSpeedPosition];
        
        Log.variable("shutter", String.valueOf(shutter));
        Log.variable("isoNewIndex", String.valueOf(isoNewIndex));

        if (isoNewIndex != INVALID_INDEX) {
            for (int i = 0; i < SHUTTERS_TABLE[isoNewIndex].length; i++) {
                if (shutter.equals(SHUTTERS_TABLE[isoNewIndex][i])) {
                    mIndex = i;
                    Log.variable("mIndex", String.valueOf(mIndex));
                    break;
                }
            }
        }
    }
    
    private void calculateApertureDifference() {
        int isoNewIndex = getIsoNewIndex();
        String shutter = SHUTTER_SPEED_LIST[mNewShutterSpeedPosition];
        
        Log.variable("shutter", String.valueOf(shutter));
        Log.variable("isoNewIndex", String.valueOf(isoNewIndex));

        if (isoNewIndex != INVALID_INDEX) {
            for (int i = 0; i < SHUTTERS_TABLE[isoNewIndex].length; i++) {
                if (shutter.equals(SHUTTERS_TABLE[isoNewIndex][i])) {
                    mIndex = i;
                    Log.variable("mIndex", String.valueOf(mIndex));
                    break;
                }
            }
        }
    }
    
    /**
     * Function calculates the shutter speed
     */
    private void calculateShutterSpeed() {
        int apertureNewColumnNumber = mNewAperturePosition;
        int isoNewIndex = getIsoNewIndex();
        Log.variable("isoNewIndex", String.valueOf(isoNewIndex));
        
        if (isoNewIndex != INVALID_INDEX) {
            String shutter = SHUTTERS_TABLE[isoNewIndex][apertureNewColumnNumber];
            mIndex = Arrays.asList(SHUTTER_SPEED_LIST).indexOf(shutter);
            
            Log.variable("shutter", shutter);
            Log.variable("mIndex", String.valueOf(mIndex));
        }
    }

    /**
     * Function calculates the ISO
     */
    private void calculateIso() {
        int i;
        int ev = getEv();
        int isoLine = INVALID_INDEX;
        int apertureNewColumnIndex = mNewAperturePosition;
        String shutter = SHUTTER_SPEED_LIST[mNewShutterSpeedPosition];

        Log.variable("ev", String.valueOf(ev));
        Log.variable("apertureNewColumnIndex", String.valueOf(apertureNewColumnIndex));
        Log.variable("shutter", shutter);
        
        if (ev != INVALID_INDEX) {
            for (i = 0; i < SHUTTERS_TABLE.length; i++) {
                if (shutter.equals(SHUTTERS_TABLE[i][apertureNewColumnIndex])) {
                    isoLine = i;
                    break;
                }
            }
            
            Log.variable("isoLine", String.valueOf(isoLine));
            
            if (isoLine != INVALID_INDEX) {
                for (i = 0; i < ISO_LIST.length - 1; i++) {
                    if (EV_TABLE[isoLine][i] == ev) {
                        mIndex = i;
                        Log.variable("mIndex", String.valueOf(mIndex));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Function gets the number of exposure pairs that matches the specified parameters
     * 
     * @return number pf exposure pair or INVLAID_INDEX if EV not found
     */
    private int getEv() {
        int ev = INVALID_INDEX;
        int isoCurrentColumnNumber = mCurrentIsoPosition;
        int apertureCurrentColumnNumber = mCurrentAperturePosition;
        String currentShutterSpeed = SHUTTER_SPEED_LIST[mCurrentShutterSpeedPosition];

        Log.variable("currentShutterSpeed", currentShutterSpeed);
        
        for (int i = 0; i < SHUTTERS_TABLE.length; i++) {
            if (currentShutterSpeed
                    .equals(SHUTTERS_TABLE[i][apertureCurrentColumnNumber])) {
                ev = EV_TABLE[i][isoCurrentColumnNumber];
                Log.variable("ev", String.valueOf(ev));
                break;
            }
        }

        return ev;
    }
    
    /**
     * Function determines the index of the ISO
     * 
     * @return ISO index or INVALID_INDEX if ISO not found
     */
    private int getIsoNewIndex() {
        int ev = getEv();
        int index = INVALID_INDEX;
        int isoNewColumnNumber = mNewIsoPostion;

        Log.variable("ev", String.valueOf(ev));
        
        if (ev != INVALID_INDEX) {
            for (int i = 0; i < SHUTTERS_TABLE.length; i++) {
                if (ev == EV_TABLE[i][isoNewColumnNumber]) {
                    index = i;
                    Log.variable("index", String.valueOf(index));
                    break;
                }
            }
        }

        return index;
    }
    
}
