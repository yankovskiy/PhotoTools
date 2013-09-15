/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
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
import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 6/01/11
 * Time: 2:16 PM
 */

public class FStop {
    /**
     * Function gets all FStops
     * @return FStop list
     */
    public static List<FStop> getAllFStops() {
        return DATABASE;
    }
    
    /**
     * Function gets the fstop value by name
     * @param value - aperture name
     * @return aperture value
     */
    public static FStop getFStop(String value) {
        for (FStop fStop : DATABASE) {
            if (fStop.getLabel().equals(value)) {
                return fStop;
            }
        }
        throw new IllegalArgumentException("Unknown fstop value: " + value);
    }

    /** aperture name */
    private final String mLabel;

    /** aperture value */
    private final BigDecimal mValue;

    /** DATABASE contains all FStops */
    private static final List<FStop> DATABASE = new ArrayList<FStop>();

    static {
        DATABASE.add(new FStop("1", new BigDecimal("1")));
        DATABASE.add(new FStop("1.2", new BigDecimal("1.189207")));
        DATABASE.add(new FStop("1.4", new BigDecimal("1.414214")));
        DATABASE.add(new FStop("1.6", new BigDecimal("1.587401")));
        DATABASE.add(new FStop("1.7", new BigDecimal("1.681793")));
        DATABASE.add(new FStop("1.8", new BigDecimal("1.781797")));
        DATABASE.add(new FStop("2", new BigDecimal("2.000000")));
        DATABASE.add(new FStop("2.2", new BigDecimal("2.244924")));
        DATABASE.add(new FStop("2.4", new BigDecimal("2.378414")));
        DATABASE.add(new FStop("2.5", new BigDecimal("2.519842")));
        DATABASE.add(new FStop("2.8", new BigDecimal("2.828427")));
        DATABASE.add(new FStop("3.2", new BigDecimal("3.174802")));
        DATABASE.add(new FStop("3.4", new BigDecimal("3.363586")));
        DATABASE.add(new FStop("3.6", new BigDecimal("3.563595")));
        DATABASE.add(new FStop("4", new BigDecimal("4.000000")));
        DATABASE.add(new FStop("4.5", new BigDecimal("4.489848")));
        DATABASE.add(new FStop("4.8", new BigDecimal("4.756828")));
        DATABASE.add(new FStop("5", new BigDecimal("5.039684")));
        DATABASE.add(new FStop("5.6", new BigDecimal("5.656854")));
        DATABASE.add(new FStop("6.4", new BigDecimal("6.349604")));
        DATABASE.add(new FStop("6.7", new BigDecimal("6.727171")));
        DATABASE.add(new FStop("7.1", new BigDecimal("7.127190")));
        DATABASE.add(new FStop("8", new BigDecimal("8.000000")));
        DATABASE.add(new FStop("9", new BigDecimal("8.979696")));
        DATABASE.add(new FStop("9.5", new BigDecimal("9.513657")));
        DATABASE.add(new FStop("10", new BigDecimal("10.07937")));
        DATABASE.add(new FStop("11", new BigDecimal("11.313708")));
        DATABASE.add(new FStop("12.7", new BigDecimal("12.699208")));
        DATABASE.add(new FStop("13.5", new BigDecimal("13.454343")));
        DATABASE.add(new FStop("14.3", new BigDecimal("14.254379")));
        DATABASE.add(new FStop("16", new BigDecimal("16.000000")));
        DATABASE.add(new FStop("18", new BigDecimal("17.959393")));
        DATABASE.add(new FStop("19", new BigDecimal("19.027314")));
        DATABASE.add(new FStop("20", new BigDecimal("20.158737")));
        DATABASE.add(new FStop("22", new BigDecimal("22.627417")));
        DATABASE.add(new FStop("25", new BigDecimal("25.398417")));
        DATABASE.add(new FStop("27", new BigDecimal("26.908685")));
        DATABASE.add(new FStop("28", new BigDecimal("28.508759")));
        DATABASE.add(new FStop("32", new BigDecimal("32")));
        DATABASE.add(new FStop("45", new BigDecimal("45.254834")));
        DATABASE.add(new FStop("64", new BigDecimal("64")));
    }

    /**
     * Constructor 
     * @param label - aperture name
     * @param value - aperture value
     */
    public FStop(String label, BigDecimal value) {
        mLabel = label;
        mValue = value;
    }

    /**
     * Function get the aperture name
     * @return
     */
    public String getLabel() {
        return mLabel;
    }
    
    /**
     * Function get the aperture value 
     * @return
     */
    public BigDecimal getValue() {
        return mValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getLabel();
    }
}
