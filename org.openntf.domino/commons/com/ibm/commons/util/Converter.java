/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Some data converter utilities.
 * @ibm-api
 */
public final class Converter {

    /*------------------------ General methods----------------------------------------------*/

    /**
     * Returns the decimal separator in this locale.
     * @ibm-api
     */
    public static char getDecimalSeparator(Locale loc){
        return new DecimalFormatSymbols(loc).getDecimalSeparator();
    }

    /**
     * Returns the grouping separator (to separate every 3 digits
     * in integer part of a number) in this locale.
     * @ibm-api
     */
    public static char getGroupingSeparator(Locale loc){
        return new DecimalFormatSymbols(loc).getGroupingSeparator();
    }

    /**
     * Returns the default locale of the JVM.
     * @fbscript
     * @ibm-api
     */
    public static Locale getJVMLocale(){
        return Locale.getDefault();
    }

    /**
     * Returns the default time zone of the JVM.
     * @fbscript
     * @ibm-api
     */
    public static TimeZone getJVMTimeZone() {
        return TimeZone.getDefault();
    }
    
    
    
    /*------------------------ Integer Conversion ------------------------------------------*/


    /**
     * @ibm-api
     */
    public static long parseInteger(String src) {
        if(StringUtil.isEmpty(src) ) {
            return 0;
        }
        return Long.parseLong(src);
    }
    
    /**
     * @ibm-api
     */
    public static long parseInteger(String src, Locale loc) throws ParseException{
        if (loc==null) {
            loc = Locale.getDefault();
        }
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        nf.setParseIntegerOnly(true);
        return nf.parse(src).longValue();
    }
    
    /**
     * @ibm-api
     */
    public static String formatInteger( long l ) {
        return formatInteger(l, null, null, null);
    }
    /**
     * @ibm-api
     */
    public static String formatInteger( long l, String format ) {
        return formatInteger(l, format, null, null);
    }
    /**
     * @ibm-api
     */
    public static String formatInteger( long l, Locale locale ) {
        return formatInteger(l, null, null, locale);
    }
    /**
     * @ibm-api
     */
    public static String formatInteger( long l, String format, String emptyFormat ) {
        return formatInteger(l, format, emptyFormat, null);
    }
    /**
     * @ibm-api
     */
    public static String formatInteger( long l, String format, String emptyFormat, Locale loc ) {
        if (loc==null) {
            loc = Locale.getDefault();
        }
        if( l==0.0 && !StringUtil.isEmpty(emptyFormat) ) {
            return emptyFormat;
        }
        NumberFormat fmt=null;
        if (!StringUtil.isEmpty(format)){
            fmt = new DecimalFormat(format, new DecimalFormatSymbols(loc));
        } else {
            fmt = NumberFormat.getNumberInstance(loc);
        }
        if (fmt!=null) {
            return fmt.format(l);
        } else {
            //should never append
            return Long.toString(l);
        }
    }

    
    /*------------------------ Double Conversion -------------------------------------------*/

    /**
     * @ibm-api
     */
    public static double parseDecimal(String src) {
        if(StringUtil.isEmpty(src) ) {
            return 0;
        }
        return Double.parseDouble(src);
    }
    /**
     * @ibm-api
     */
    public static double parseDecimal(String src, Locale loc) throws ParseException{
        if (loc==null) {
            loc = Locale.getDefault();
        }
        return NumberFormat.getInstance(loc).parse(src).floatValue();
    }

    // Getting a Decimal from a String (accept comma or point in some locales)
    /**
     * @ibm-api
     */
    public static double parseFloatWithDecimalSeparatorTolerance(String doubleString, Locale loc) {
        doubleString = removeThousandSeparator(doubleString, loc);
        doubleString = replaceCommaByPoint(doubleString, loc);
        return parseDecimal(doubleString);
    }

    /**
     * If a decimal number contains a comma and no point, and if the given
     * locale uses a comma for decimal separator, the comma is replaced by
     * a point.
     * @ibm-api
     */
    public static String replaceCommaByPoint(String doubleString, Locale loc) {
        DecimalFormatSymbols symb = new DecimalFormatSymbols(loc);
        if (symb.getDecimalSeparator()==',') {
            // comma just 1 time, and no point
            if (   doubleString.indexOf(',')>=0
                && doubleString.indexOf(',')==doubleString.lastIndexOf(',')
                && doubleString.indexOf('.')==-1 ) {
                return doubleString.replace(',','.');
            }
        }
        return doubleString;
    }

    /**
     * If a decimal number contains thousand separator (example : a coma in US),
     * they are removed, so that the string can be correctly interpreted by Double.parseDouble
     * @ibm-api
     */
    public static String removeThousandSeparator(String doubleString, Locale loc) {
        DecimalFormatSymbols symb = new DecimalFormatSymbols(loc);
        char thoSep = symb.getGroupingSeparator();
        if (thoSep=='.') { // don't remove thousand separator if it is a '.'
            return doubleString;
        }
        if (thoSep==160) { // insecable white space
            thoSep=' ';
        }
        return StringUtil.replace(doubleString, String.valueOf(thoSep), null);
    }
    
    /**
     * @ibm-api
     */
    public static String formatDecimal( double d ) {
        return formatDecimal(d, null, null, null);
    }
    /**
     * @ibm-api
     */
    public static String formatDecimal( double d, Locale locale ) {
        return formatDecimal(d, null, null, locale);
    }
    /**
     * @ibm-api
     */
    public static String formatDecimal( double d, String format ) {
        return formatDecimal(d, format, null, null);
    }
    /**
     * @ibm-api
     */
    public static String formatDecimal( double d, String format, String emptyFormat ) {
        return formatDecimal(d, format, emptyFormat, null);
    }
    /**
     * @ibm-api
     */
    public static String formatDecimal(double d, String format, String emptyFormat, Locale loc) {
        if (loc==null) {
            loc = Locale.getDefault();
        }
        if( d==0.0 && !StringUtil.isEmpty(emptyFormat) ) {
            return emptyFormat;
        }
        NumberFormat fmt=null;
        if (!StringUtil.isEmpty(format)){
            fmt = new DecimalFormat(format, new DecimalFormatSymbols(loc));
        } else {
            fmt = NumberFormat.getNumberInstance(loc);
        }
        if (fmt!=null) {
            return fmt.format(d);
        } else {
            //should never append
            return Double.toString(d);
        }
    }
    
    
    /*------------------------ Boolean Conversion ------------------------------------------*/

    /**
     * @ibm-api
     */
    public static String formatBoolean(boolean value) {
        return formatBoolean(value,null);
    }

    /**
     * @ibm-api
     */
    public static String formatBoolean(boolean value, Locale locale) {
        return value ? "True" : "False"; // $NLS-Converter.true-1$ $NLS-Converter.false-2$
    }

    /**
     * @ibm-api
     */
    public static boolean parseBoolean(String value) {
        return StringUtil.equals(value,"True"); // $NLS-Converter.true.1-1$
    }

    /**
     * @ibm-api
     */
    public static boolean parseBooleanLocale(String value) {
        return parseBoolean(value);
    }

    /**
     * @ibm-api
     */
    public static boolean parseBooleanLocale(String value, Locale locale) {
        return parseBoolean(value);
    }
}
