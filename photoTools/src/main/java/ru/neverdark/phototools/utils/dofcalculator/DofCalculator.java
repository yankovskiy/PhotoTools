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
package ru.neverdark.phototools.utils.dofcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.neverdark.phototools.utils.Constants;

/**
 * Class implements the DoF calculator
 */
public class DofCalculator {
    public class CalculationResult {

        private BigDecimal mHyperFocal;
        private BigDecimal mNearLimit;
        private BigDecimal mFarLimit;
        private BigDecimal mTotal;

        /**
         * Converts all results to the needs measure
         * 
         * @param measureUnit
         *            must be in Constants.METER, Constants.CM, Constants.INCH,
         *            Constants.FOOT
         */
        public void convertMeasureUnits(final int measureUnit) {
            double value = 1;

            switch (measureUnit) {
            case Constants.METER:
                value = M;
                break;
            case Constants.CM:
                value = CM;
                break;
            case Constants.INCH:
                value = IN;
                break;
            case Constants.FOOT:
                value = FOOT;
                break;
            }

            mHyperFocal = mHyperFocal.divide(new BigDecimal(value), 2,
                    RoundingMode.HALF_UP);
            mNearLimit = mNearLimit.divide(new BigDecimal(value), 2,
                    RoundingMode.HALF_UP);
            mFarLimit = mFarLimit.divide(new BigDecimal(value), 2,
                    RoundingMode.HALF_UP);
            mTotal = mTotal.divide(new BigDecimal(value), 2,
                    RoundingMode.HALF_UP);
        }

        /**
         * Formats input values for show infinity symbols
         * 
         * @param value
         *            value for format
         * @return "∞" if value == 0 or value.toString() in other case
         */
        public String format(BigDecimal value) {
            return value.floatValue() == 0 ? "∞" : value.toString();
        }

        /**
         * Gets far limit distance
         * 
         * @return the far limit distance
         */
        public BigDecimal getFarLimit() {
            return mFarLimit;
        }

        /**
         * Gets hyper focal distance
         * 
         * @return hyper focal distance
         */
        public BigDecimal getHyperFocal() {
            return mHyperFocal;
        }

        /**
         * Gets near limit distance
         * 
         * @return near limit distance
         */
        public BigDecimal getNearLimit() {
            return mNearLimit;
        }

        /**
         * Gets total depth of field distance
         * 
         * @return total depth of field distance
         */
        public BigDecimal getTotal() {
            return mTotal;
        }

        /**
         * Sets far limit distance
         * 
         * @param farLimit
         *            the far limit distance
         */
        public void setFarLimit(BigDecimal farLimit) {
            mFarLimit = farLimit;
        }

        /**
         * Sets hyper focal distance
         * 
         * @param hyperFocal
         *            the hyperfocal distance
         */
        public void setHyperFocal(BigDecimal hyperFocal) {
            this.mHyperFocal = hyperFocal;
        }

        /**
         * Sets near limit distance
         * 
         * @param nearLimit
         *            the near limit distance
         */
        public void setNearLimit(BigDecimal nearLimit) {
            this.mNearLimit = nearLimit;
        }

        /**
         * Sets total depth of field distance
         * 
         * @param total
         *            the total depth of field distance
         */
        public void setTotal(BigDecimal total) {
            this.mTotal = total;
        }
    }

    private static final double CM = 10;
    private static final double M = 1000;
    private static final double IN = 25.4;
    private static final double FOOT = 304.8;

    /** F-stop. e.g.: 1.4, 1.8, 2, 2.8, 4, 5.6, etc */
    private BigDecimal mAperture;

    /** Focus length, in millimeters */
    private BigDecimal mFocusLength;

    /** Circle of confusion for the camera, in millimeters */
    private BigDecimal mCoc;

    /** Hyper focal distance */
    private BigDecimal mHyperFocalDistance;

    /** Near depth of field limit */
    private BigDecimal mNearLimitDistance;

    /** Far depth of field limit */
    private BigDecimal mFarLimtDistance;

    /** Subject distance */
    private BigDecimal mSubjectDistance;
    /** Total depth of view distance */
    private BigDecimal mTotal;

    /**
     * Class constructor
     * 
     * @param aperture
     *            - aperture
     * @param focusLength
     *            - focusLength in mm
     * @param coc
     *            - circle of confusion in mm
     * @param subjectDistance
     *            subject distance in meters
     * @param calcMeasureUnit
     *            measure unit for subject distance must be in Constants.METER,
     *            Constants.CM, Constants.INCH, Constants.FOOT
     */
    public DofCalculator(BigDecimal aperture, BigDecimal focusLength,
            BigDecimal coc, BigDecimal subjectDistance,
            final int calcMeasureUnit) {
        double value = 1;

        switch (calcMeasureUnit) {
        case Constants.METER:
            value = M;
            break;
        case Constants.CM:
            value = CM;
            break;
        case Constants.INCH:
            value = IN;
            break;
        case Constants.FOOT:
            value = FOOT;
            break;
        }

        mAperture = aperture;
        mFocusLength = focusLength;
        mCoc = coc;
        // convert to mm
        mSubjectDistance = subjectDistance.multiply(new BigDecimal(value));
    }

    /**
     * Calculates near limit, far limit, total depth of view, hyper focal and
     * store result into CalculationResult object
     * 
     * @param resultMeasureUnit
     *            measure unit for resulting must be in Constants.METER,
     *            Constants.CM, Constants.INCH, Constants.FOOT
     * 
     * @return CalculationResult object
     */
    public CalculationResult calculate(final int resultMeasureUnit) {
        calculateHyperFocalDistance();
        calculateNearLimit();
        calculateFarLimit();
        calculateTotal();

        CalculationResult result = new CalculationResult();
        result.setHyperFocal(mHyperFocalDistance);
        result.setNearLimit(mNearLimitDistance);
        result.setFarLimit(mFarLimtDistance);
        result.setTotal(mTotal);
        result.convertMeasureUnits(resultMeasureUnit);

        return result;
    }

    /**
     * Calculates near depth of view limit
     */
    private void calculateFarLimit() {
        if (mSubjectDistance.floatValue() < mHyperFocalDistance.floatValue()) {
            BigDecimal numerator = mHyperFocalDistance
                    .multiply(mSubjectDistance);
            BigDecimal denominator = mHyperFocalDistance
                    .subtract(mSubjectDistance);
            mFarLimtDistance = numerator.divide(denominator,
                    RoundingMode.HALF_UP);
        } else {
            mFarLimtDistance = new BigDecimal("0");
        }
    }

    /**
     * Function calculate hyper focal distance
     */
    private void calculateHyperFocalDistance() {
        BigDecimal numerator = mFocusLength.pow(2);
        BigDecimal denominator = mAperture.multiply(mCoc);
        mHyperFocalDistance = numerator.divide(denominator,
                RoundingMode.HALF_UP);
    }

    /**
     * Calculates near depth of view limit
     */
    private void calculateNearLimit() {
        BigDecimal numerator = mHyperFocalDistance.multiply(mSubjectDistance);
        BigDecimal denominator = mHyperFocalDistance.add(mSubjectDistance);
        mNearLimitDistance = numerator
                .divide(denominator, RoundingMode.HALF_UP);
    }

    /**
     * Calculates total depth of view
     */
    private void calculateTotal() {
        if (mFarLimtDistance.floatValue() != 0) {
            mTotal = mFarLimtDistance.subtract(mNearLimitDistance);
        } else {
            mTotal = new BigDecimal("0");
        }
    }
}
