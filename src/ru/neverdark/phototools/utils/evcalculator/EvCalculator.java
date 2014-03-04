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
 *     
 * 	Modification:
 * 		2014/03/03 Rudy Dordonne (rudy@itu.dk)	
 ******************************************************************************/
package ru.neverdark.phototools.utils.evcalculator;

import java.util.ArrayList;
import ru.neverdark.phototools.utils.Log;


public class EvCalculator {

    private int mCurrentAperturePosition;
    private int mCurrentIsoPosition;
    private int mCurrentShutterSpeedPosition;
    private int mNewAperturePosition;
    private int mNewIsoPostion;
    private int mNewShutterSpeedPosition;
    private int mIndex;
    private int stopDistribution;
    
    private Double ISO_VALUE_LIST[];
    private Double APERTURE_VALUE_LIST[];
    private Double SHUTTER_VALUE_LIST[];

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
        
        mIndex = EvMathTable.INVALID_INDEX;
    }
    
    /**
     * Inits local arrays by EV step
     * @param evStep ev step
     */
    public void initArrays(final int evStep) {
    	stopDistribution=evStep;
        ISO_VALUE_LIST = EvMathTable.getISOValues(evStep);
        APERTURE_VALUE_LIST = EvMathTable.getApertureValues(evStep);
        SHUTTER_VALUE_LIST = EvMathTable.getShutterValues(evStep);
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
    	String element = "";
    	for (index = 0; index < SHUTTER_VALUE_LIST.length ; index++ ) {
    		if(SHUTTER_VALUE_LIST[index]<1.0)
		    	element = "1/"+cleanNumberToString(1/SHUTTER_VALUE_LIST[index])+" sec";
	    				
    		else if(SHUTTER_VALUE_LIST[index]>=1 && SHUTTER_VALUE_LIST[index]<60.0)
	    		element = cleanNumberToString(SHUTTER_VALUE_LIST[index])+" sec";

    		else if(SHUTTER_VALUE_LIST[index]>=60.0)
	    		element = cleanNumberToString((Double)(SHUTTER_VALUE_LIST[index]/60))+" min";

    		shutters.add(element);
    	}
        return shutters.toArray(new String[shutters.size()]);
    }
    
	/**
	 * @param number
	 * @return
	 */
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
    	int wIndex = 1;

        
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

       if(mNewAperturePosition<0) {
        	double isoStopDifference = calculateIsoDifference();
        	Log.variable("isoStopDifference Value", String.valueOf(isoStopDifference));
        	double shutterStopDifference = calculateShutterDifference();
        	Log.variable("shutterStopDifference Value", String.valueOf(shutterStopDifference));
        	double expectedApertureStopDifference =  isoStopDifference + shutterStopDifference;
        	Log.variable("expectedApertureStopDifference Value", String.valueOf(expectedApertureStopDifference));
        	wIndex += (int) Math.round(((double)(expectedApertureStopDifference * stopDistribution)));
        	Log.variable("wIndex Value", String.valueOf(wIndex));
        	mIndex = mCurrentAperturePosition + wIndex;
        	if(mIndex>APERTURE_VALUE_LIST.length)
        		mIndex=APERTURE_VALUE_LIST.length;
        } else if(mNewIsoPostion<0) {
        	double apertureStopDifference = calculateApertureDifference();
        	Log.variable("apertureStopDifference Value", String.valueOf(apertureStopDifference));
        	double shutterStopDifference = calculateShutterDifference();
        	Log.variable("shutterStopDifference Value", String.valueOf(shutterStopDifference));
        	double expectedIsoStopDifference =  apertureStopDifference + shutterStopDifference;
        	Log.variable("expectedIsoStopDifference Value", String.valueOf(expectedIsoStopDifference));
        	wIndex += (int) Math.round(((double)(expectedIsoStopDifference * stopDistribution)));
        	Log.variable("wIndex Value", String.valueOf(wIndex));
        	mIndex = mCurrentIsoPosition + wIndex;
        	if(mIndex>ISO_VALUE_LIST.length)
        		mIndex=ISO_VALUE_LIST.length;
        } else if(mNewShutterSpeedPosition<0) {
        	double apertureStopDifference = calculateApertureDifference();
        	Log.variable("apertureStopDifference Value", String.valueOf(apertureStopDifference));
        	double isoStopDifference = calculateIsoDifference();
        	Log.variable("isoStopDifference Value", String.valueOf(isoStopDifference));
        	double expectedShutterStopDifference =  apertureStopDifference + isoStopDifference;
        	Log.variable("expectedShutterStopDifference Value", String.valueOf(expectedShutterStopDifference));
        	wIndex += (int) Math.round(((double)(expectedShutterStopDifference * stopDistribution)));
        	Log.variable("wIndex Value", String.valueOf(wIndex));
        	mIndex = mCurrentShutterSpeedPosition + wIndex;
        	if(mIndex>SHUTTER_VALUE_LIST.length)
        		mIndex=SHUTTER_VALUE_LIST.length;
        }
       
       	if(mIndex<1)
       		mIndex=1;
        Log.variable("mIndex", String.valueOf(mIndex));
        
        return mIndex;
    }

    /**
     * @return
     */
    private double calculateIsoDifference(){    	
    	return calculateDifference(ISO_VALUE_LIST[mCurrentIsoPosition], ISO_VALUE_LIST[mNewIsoPostion], 2.0);
    }
    
    private double calculateShutterDifference(){
    	return calculateDifference(SHUTTER_VALUE_LIST[mCurrentShutterSpeedPosition], SHUTTER_VALUE_LIST[mNewShutterSpeedPosition], 2.0);
    }
    
    private double calculateApertureDifference() {	    
    	return calculateDifference(APERTURE_VALUE_LIST[mNewAperturePosition], APERTURE_VALUE_LIST[mCurrentAperturePosition], Math.sqrt(2));
    }
    
    /**
     * @param currentValue
     * @param newValue
     * @param base
     * @return
     */
    private double calculateDifference(double currentValue, double newValue, double base){
    	double difference = 0;
    	if(currentValue < newValue)
    		difference = (Math.log(newValue/currentValue)/Math.log(base));
    	else
    		difference = - (Math.log(currentValue/newValue)/Math.log(base));
    	
    	return difference;
    }
}
