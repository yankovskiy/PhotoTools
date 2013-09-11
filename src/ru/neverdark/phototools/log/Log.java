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
package ru.neverdark.phototools.log;

public class Log {
    /** true if DEBUG enabled or false if DEBUG disable */
    private static final boolean DEBUG = true;

    /**
     * Function logged message to the LogCat as information message
     * 
     * @param message
     *            message for logging
     */
    public static void message(String message) {
        if (DEBUG == true) {
            log(message);
        }
    }

    /**
     * Function logged values to the LogCat as information message
     * 
     * @param variable
     *            variable name for logging
     * @param value
     *            value of the variable
     */
    public static void variable(String variable, String value) {
        if (DEBUG == true) {
            String message = variable + " = " + value;
            log(message);
        }
    }
    
    /**
     * Logs message with class name, method name and line number
     * @param message message for logging
     */
    private static void log(String message) {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        String APP = trace[2].getClassName() + "." + trace[2].getMethodName() + ":" + trace[2].getLineNumber();
        android.util.Log.i(APP, message);
    }
}
