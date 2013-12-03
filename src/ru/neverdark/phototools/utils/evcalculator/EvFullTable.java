package ru.neverdark.phototools.utils.evcalculator;

/**
 * Class for EV table with step in 1 stop
 */
public class EvFullTable implements EvTable{

    private static final String ISO_LIST[] = {"25", "50", "100", "200", "400", "800",
            "1600", "3200", "6400", "12800", "25600", "51200", "102400", "204800"};
    
    private static final String SHUTTER_LIST[] = {"512 min",
            "256 min", "128 min", "64 min", "32 min", "16 min", "8 min", "4 min", "2 min",
            "1 min", "30 sec", "15 sec", "8 sec", "4 sec", "2 sec", "1 sec",
            "1/2 sec", "1/4 sec", "1/8 sec", "1/15 sec", "1/30 sec",
            "1/60 sec", "1/125 sec", "1/250 sec", "1/500 sec", "1/1000 sec",
            "1/2000 sec", "1/4000 sec", "1/8000 sec" };
    
    private static final String APERTURE_LIST[] = {"1", "1.4", "2.0", "2.8",
            "4", "5.6", "8", "11", "16", "22", "32", "45", "64", "90", "128", "180", "256", "360" };
    
    private static final int EV_TABLE[][] = {
        { 0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13},
        { 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12},
        { 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11},
        { 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10},
        { 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9},
        { 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8},
        { 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7},
        { 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6},
        { 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5},
        { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4},
        { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3},
        { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2},
        { 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1},
        { 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
        { 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
        { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2},
        { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3},
        { 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4},
        { 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5},
        { 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6},
        { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7},
        { 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8},
        { 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9},
        { 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10},
        { 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11},
        { 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12},
        { 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13},
        { 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14},
        { 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15},
        { 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16},
        { 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17},
        { 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18},
        { 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19},
        { 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20},
        { 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21},
        { 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22},
        { 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23},
        { 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24},
        { 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25},
        { 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26},
        { 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27},
        { 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28},
        { 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29},
        { 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30},
        { 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31},
        { 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32} };
    
    private static final String SHUTTERS_TABLE[][] = {
        {"512 min","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"256 min","512 min","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0","0","0"},
        {"8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0","0"},
        {"4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0","0"},
        {"2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0","0"},
        {"1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0","0"},
        {"30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0","0"},
        {"15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0","0"},
        {"8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0","0"},
        {"4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0","0"},
        {"2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0","0"},
        {"1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0","0"},
        {"1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min","0"},
        {"1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min","512 min"},
        {"1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min","256 min"},
        {"1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min","128 min"},
        {"1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min","64 min"},
        {"1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min","32 min"},
        {"1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min","16 min"},
        {"1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min","8 min"},
        {"1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min","4 min"},
        {"1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min","2 min"},
        {"1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec","1 min"},
        {"1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec","30 sec"},
        {"1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec","15 sec"},
        {"0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec","8 sec"},
        {"0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec","4 sec"},
        {"0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec","2 sec"},
        {"0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec","1 sec"},
        {"0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec","1/2 sec"},
        {"0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec","1/4 sec"},
        {"0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec","1/8 sec"},
        {"0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec","1/15 sec"},
        {"0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec","1/30 sec"},
        {"0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec","1/60 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec","1/125 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec","1/250 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec","1/500 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec","1/1000 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec","1/2000 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec","1/4000 sec"},
        {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1/8000 sec"} };
            
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
