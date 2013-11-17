package ru.neverdark.phototools.utils.evcalculator;

/**
 * Class for EV table with step in 1/2 stop
 */
public class EvHalfTable implements EvTable{

    private final String ISO_LIST[] = {};
    private final String SHUTTER_LIST[] = {};
    private final String APERTURE_LIST[] = {};
    private final int EV_TABLE[][] = {{}};
    private final String SHUTTERS_TABLE[][] = {{}};
            
    @Override
    public String[] getIsoList() {
        return ISO_LIST;
    }

    @Override
    public String[] getApertureList() {
        return APERTURE_LIST;
    }

    @Override
    public String[] getShutterList() {
        return SHUTTER_LIST;
    }

    @Override
    public int[][] getEvTable() {
        return EV_TABLE;
    }

    @Override
    public String[][] getShutterTable() {
        return SHUTTERS_TABLE;
    }

}
