package ru.neverdark.phototools.dofcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class implements the DoF calculator
 */
public class DofCalculator {
    /** F-stop. e.g.: 1.4, 1.8, 2, 2.8, 4, 5.6, etc*/
    private BigDecimal mAperture;

    /** Focus length, in millimeters */
    private BigDecimal mFocusLength;

    /** Circle of confusion for the camera, in millimeters */
    private BigDecimal mCoc;

    /**
     * Class constructor
     * @param aperture - aperture 
     * @param focusLength - focusLength in mm
     * @param coc - circle of confusion in mm
     */
    public DofCalculator(BigDecimal aperture, BigDecimal focusLength, BigDecimal coc) {
        mAperture = aperture;
        mFocusLength = focusLength;
        mCoc = coc;
    }
    
    /**
     * Function calculate hyper focal distance
     * @return hyper focal distance
     */
    public BigDecimal calculateHyperFocalDistance() {
        BigDecimal numerator = mFocusLength.pow(2);
        BigDecimal denominator = mAperture.multiply(mCoc);
        BigDecimal result = numerator.divide(denominator, RoundingMode.HALF_UP);
        
        /* Into meters*/
        result = result.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
        return result;
    }
}
