package ru.neverdark.phototools.utils.evcalculator;

/**
 * Interface for exposure pare table
 */
public interface EvTable {
    /**
     * Gets ISO list
     * @return array contains possible ISO
     */
    public String[] getIsoList();
    
    /**
     * Gets aperture list 
     * @return array contains possible apertures
     */
    public String[] getApertureList();
    
    /**
     * Gets shutter list
     * @return array contains possible shutter speed
     */
    public String[] getShutterList();
    
    /**
     * Gets EV number table
     * @return two-dimensional array contains EV number
     */
    public int[][] getEvTable();
    
    /**
     * Gets table contains shutter speed
     * @return two-dimensional array contains shutter speed
     */
    public String[][] getShutterTable();
}
