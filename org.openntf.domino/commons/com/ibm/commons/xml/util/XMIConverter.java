/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.commons.xml.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.ibm.commons.util.DateTime;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.XMLException;

/**
 * XML value conversion using XMI format.
 */
public class XMIConverter {


    // ====================================================================================
    // Conversion helpers
    // ====================================================================================

    // --- char converters methods ---
    public static char parseChar(String s) {
        return s.length()>0?s.charAt(0):'\0';
    }

    public static char parseChar(String s, String def) {
        return parseChar(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(char value) {
        return value!=0?new String(new char[] {
            value}):""; //$NON-NLS-1$
    }

    // --- Byte converters methods ---
    public static byte parseByte(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Byte.parseByte(s);
            } catch(NumberFormatException e) {}
            return(byte)Double.parseDouble(s);
        }
        return 0;
    }

    public static byte parseByte(String s, String def) {
        return parseByte(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(byte value) {
        return StringUtil.toString(value);
    }

    // --- Short converters methods ----
    public static short parseShort(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Short.parseShort(s);
            } catch(NumberFormatException e) {}
            return(short)Double.parseDouble(s);
        }
        return 0;
    }

    public static short parseShort(String s, String def) {
        return parseShort(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(short value) {
        return StringUtil.toString(value);
    }

    // --- Integer converters methods ----
    public static int parseInteger(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Integer.parseInt(s);
            } catch(NumberFormatException e) {}
            return(int)Double.parseDouble(s);
        }
        return 0;
    }

    public static int parseInteger(String s, String def) {
        return parseInteger(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(int value) {
        return StringUtil.toString(value);
    }

    // --- Long converters methods ----
    public static long parseLong(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Long.parseLong(s);
            } catch(NumberFormatException e) {}
            return(long)Double.parseDouble(s);
        }
        return 0;
    }

    public static long parseLong(String s, String def) {
        return parseLong(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(long value) {
        return StringUtil.toString(value);
    }

    // --- Float converters methods ----
    public static float parseFloat(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Float.parseFloat(s);
            } catch(NumberFormatException e) {
                // check if the string is not NaN because IBM JDK 1.3 throw an exception in this case
                // instead of returning Float.NaN
                if (StringUtil.equals(s, "NaN")){ // $NON-NLS-1$
                    return Float.NaN;
                }
            }
            return(float)Double.parseDouble(s);
        }
        return 0;
    }

    public static float parseFloat(String s, String def) {
        return parseFloat(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(float value) {
        return StringUtil.toString(value);
    }

    // --- Double converters methods ----
    public static double parseDouble(String s) {
        if(!StringUtil.isEmpty(s)) {
            try {
                return Double.parseDouble(s);
            } catch(NumberFormatException e) {
                // check if the string is not NaN because IBM JDK 1.3 throw an exception in this case
                // instead of returning Float.NaN
                if (StringUtil.equals(s, "NaN")){ // $NON-NLS-1$
                    return Double.NaN;
                }
            }
        }
        return 0;
    }

    public static double parseDouble(String s, String def) {
        return parseDouble(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(double value) {
        // PHIL: try that?
        //return DToA.toFixed(double,100)
        String s = Double.toString(value);
        if(s.indexOf('e')>0||s.indexOf('E')>0) {
            return new java.math.BigDecimal(s).toString();
        }
        return s;
    }

    // --- BigInteger converters methods ----
    public static java.math.BigInteger parseBigInteger(String s) {
        return StringUtil.isEmpty(s)?new java.math.BigInteger("0"):new java.math.BigInteger(s); //$NON-NLS-1$
    }

    public static java.math.BigInteger parseBigInteger(String s, String def) {
        return parseBigInteger(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(java.math.BigInteger value) {
        return StringUtil.toString(value);
    }

    // --- BigDecimal converters methods ----
    public static java.math.BigDecimal parseBigDecimal(String s) {
        return StringUtil.isEmpty(s)?new java.math.BigDecimal("0.0"):new java.math.BigDecimal(s); //$NON-NLS-1$
    }

    public static java.math.BigDecimal parseBigDecimal(String s, String def) {
        return parseBigDecimal(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(java.math.BigDecimal value) {
        if(value!=null) {
            // PHIL: Is there a way to optimize that?
            String s = value.toString();
            int trailingZero = 0;
            int scale = value.scale();
            for(int i = 0; i<scale; i++) {
                if(s.charAt(s.length()-i-1)!='0') {
                    break;
                }
                trailingZero++;
            }
            if(trailingZero>0) {
                return s.substring(0, s.length()-trailingZero);
            }
            return s;
        }
        return ""; //$NON-NLS-1$
    }

    // --- Boolean converters methods ----
    public static boolean parseBoolean(String s) {
        if(StringUtil.isEmpty(s)) {
            return false;
        }
        if(s.equals("true")) //$NON-NLS-1$
            return true;
        if(s.equals("false")) //$NON-NLS-1$
            return false;
        throw new NumberFormatException(StringUtil.format("Invalid Boolean format", s)); // $NLS-XMIConverter.InvalidBooleanformat-1$
    }

    public static boolean parseBoolean(String s, String def) {
        return parseBoolean(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(boolean value) {
        return value?"true":"false"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static java.util.Date parseUtilDate(String s) {
        String CURRENT_DATE_TIME ="now"; //$NON-NLS-1$

        if(StringUtil.isEmpty(s)) {
            return null;
        }
        if(StringUtil.equalsIgnoreCase(s, CURRENT_DATE_TIME)) {
        	java.util.Date nowResult = new java.util.Date();
        	return nowResult;
        }
        // Try an XMI parsing
        try {
            java.util.Date result = (java.util.Date)readXMIDate(s, java.util.Date.class);
            if(result!=null) {
                return result;
            }
        } catch(Exception e) {
        	//need to check for a bare Time format
        	try {
        		long resultTime = readXMIDateStrict(s, false, false);
        		if (resultTime > 0) {
        			java.util.Date nowResult = new java.util.Date();
        			return nowResult;
        		}
        	}
        	catch (Exception eTime){}
        }
        return null;
    }

    public static java.sql.Date parseDate(String s) {
        if(StringUtil.isEmpty(s)) {
            return null;
        }
        // Try an XMI parsing
        try {
            java.sql.Date result = (java.sql.Date)readXMIDate(s, java.sql.Date.class);
            if(result!=null) {
                return result;
            }
        } catch(Exception e) {}
        return null;
    }

    public static java.sql.Date parseDate(String s, String def) {
        return parseDate(StringUtil.isEmpty(s)?def:s);
    }

    public static String toString(java.util.Date value) {
        if(value==null)
            return ""; //$NON-NLS-1$
        return composeDate(value.getTime());
    }

    public static java.sql.Time parseTime(String s) {
        if(StringUtil.isEmpty(s)) {
            return null;
        }
        // Try a SOAP parsing
        try {
            java.sql.Time result = (java.sql.Time)readXMIDate(s, java.sql.Time.class);
            if(result!=null) {
                return result;
            }
        } catch(Exception e) {}
        return null;
    }

    public static java.sql.Time parseTime(String s, String def) {
        return parseTime(StringUtil.isEmpty(s)?def:s);
    }

    public static java.sql.Timestamp parseTimestamp(String s) {
        if(StringUtil.isEmpty(s)) {
            return null;
        }
        // Try a SOAP parsing
        try {
            java.sql.Timestamp result = (java.sql.Timestamp)readXMIDate(s, java.sql.Timestamp.class);
            if(result!=null) {
                return result;
            }
        } catch(Exception e) {}
        return null;
    }

    public static java.sql.Timestamp parseTimestamp(String s, String def) {
        return parseTimestamp(StringUtil.isEmpty(s)?def:s);
    }

    /**
     * Parse an XMI  date
     * Returns a date in XSP server time zone (if the XML doc contains
     * tz indication different from the server, the date is converted. )
     */
    public static java.util.Date readXMIDate(String s, Class javaClass) throws XMLException {
        long xmlDate = readXMIDate(s);
        if(xmlDate!=Long.MIN_VALUE) {
            if(javaClass==java.util.Date.class) {
                return new java.util.Date(xmlDate);
            }
            if(javaClass==java.sql.Date.class) {
                //return new TDateUtilities.SQLDateStruct(xmlDate).createDate();
                return new java.sql.Date(xmlDate);
            }
            if(javaClass==java.sql.Time.class) {
                //return new TDateUtilities.SQLTimeStruct(xmlDate).createTime();
                return new java.sql.Time(xmlDate);
            }
            if(javaClass==java.sql.Timestamp.class) {
                //return new TDateUtilities.SQLDatetimeStruct(xmlDate, nanos).createDatetime();
                return new java.sql.Timestamp(xmlDate);
            }
        }
        return null;
    }

    public static long readXMIDate(String s) throws XMLException {
        return readXMIDateStrict(s, true);
    }
    
    public static long readXMIDateStrict(String s, boolean strict) throws XMLException {
        return readXMIDateStrict(s, strict, false);
    }
    public static long readXMIDateStrict(String s, boolean strict, boolean ignoreTz) throws XMLException {
        boolean tzIndication = false;
        boolean tzSignPlus = false; // time zone offset sign
        int h2 = 0, m2 = 0; // time zone offset
        // The date use the format
        // YYYY-MM-DDTHH:NN:SS
        // could be followed by "Z" or " +or- HH:mm" (time zone offset) $NLS-XMIConverter.orHHmm-2$
        DateStringParser parser = new DateStringParser(s);

        // If it starts with 'T', then this is just a time
        int year = 1970;
        int month = 1;
        int day = 1;
        if(!parser.startsWith('T')) {
            // Begin by the date, without any leading char
            year = parser.matchInteger(4);
            if(year==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
            if(!parser.match('-')) {
                throwBadDateException(s); 
            }
    
            month = parser.matchInteger(2);
            if(month==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
            if(!parser.match('-')) {
                throwBadDateException(s); 
            }
    
            day = parser.matchInteger(2);
            if(day==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
        } else if (strict) {
            throwBadDateException(s); 
        }

        int hour = 12;
        int minute = 0;
        int second = 0;
        if (parser.match('T')) {
            hour = parser.matchInteger(2);
            if(hour==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
            if(!parser.match(':')) {
                throwBadDateException(s); 
            }
    
            minute = parser.matchInteger(2);
            if(minute==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
            if(!parser.match(':')) {
                throwBadDateException(s); 
            }
    
            second = parser.matchInteger(2);
            if(second==Integer.MIN_VALUE) {
                throwBadDateException(s); 
            }
    
            int nanos = 0;
            if(parser.match('.')) {
                int ptr = parser.getCurrentPosition();
                int frac = parser.getNextInteger();
                if(frac==Integer.MIN_VALUE) {
                    throwBadDateException(s); 
                }
                int ndigits = parser.getCurrentPosition()-ptr;
                if(ndigits<6) {
                    nanos = frac*pow10[ndigits];
                }
            }
            // Get the time diff
            if(parser.match('Z')) {
                tzIndication = true; // h2=0 and m2=0
            } else {
                tzSignPlus = parser.match('+');
                if(parser.match('-')||tzSignPlus) {
                    tzIndication = true;
                    h2 = parser.matchInteger(2);
                    if(h2==Integer.MIN_VALUE) {
                        throwBadDateException(s); 
                    }
                    // this is optional
                    parser.match(':');
                    m2 = parser.matchInteger(2);
                    if(m2==Integer.MIN_VALUE) {
                        throwBadDateException(s); 
                    }
                }
            }
        }
        else if (strict) {
            throwBadDateException(s); 
        }

        // The date must be finished
        if(!parser.isEOF()) {
            throwBadDateException(s); 
        }

        DateTime.TGregorianCalendar gregorianCalendar = DateTime.getCalendar();
        try {
            gregorianCalendar.set(year,month-1,day,hour,minute,second);
            gregorianCalendar.set(GregorianCalendar.MILLISECOND,0);
            long xmlDate = gregorianCalendar.getMillis();
            
            // if the time zone indication in the XML is DIFFERENT from XSP server TZ :
            // the date must be converted
            if(tzIndication && !ignoreTz) {
                long xmlOffsetindication;
                if(tzSignPlus) {
                    xmlOffsetindication = h2*60*60*1000+m2*60*1000;
                } else {
                    xmlOffsetindication = -h2*60*60*1000-m2*60*1000;
                }
                long serverOffsetIndication = TimeZone.getDefault().getRawOffset();
                // conversion if offsets are not the same
                if(xmlOffsetindication!=serverOffsetIndication) {
                    xmlDate = xmlDate-xmlOffsetindication+serverOffsetIndication;
                }
            }

            return xmlDate;
        } finally {
            DateTime.recycleCalendar(gregorianCalendar,false);
        }
    }
    private static void throwBadDateException(String dateString) throws XMLException {
        throw new XMLException(null,"Invalid XMI date format {0}",dateString); // $NLS-XMIConverter.InvalidXMIdateformat0-1$
    }
    
    private static int[] pow10 = {
        100000, 10000, 1000, 100, 10, 1};

    // Compose an XMI date
    public static String composeDate(long date) {
        DateTime.TGregorianCalendar gregorianCalendar = DateTime.getCalendar();
        try {
            gregorianCalendar.setMillis(date);
            int year = gregorianCalendar.get(Calendar.YEAR);
            int month = gregorianCalendar.get(Calendar.MONTH)+1;
            int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            int hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = gregorianCalendar.get(Calendar.MINUTE);
            int second = gregorianCalendar.get(Calendar.SECOND);
            //int millis = gregorianCalendar.get(Calendar.MILLISECOND);
            return composeDate(year, month, day, hour, minute, second);
        } finally {
            DateTime.recycleCalendar(gregorianCalendar,false);
        }
    }

    // Compose an XMI date
    public static String composeDate(int year, int month, int day, int hour, int minute, int second) {
        return composeDate(year, month, day, hour, minute, second, true);
    }

    // Compose an XMI date
    public static String composeDate(int year, int month, int day, int hour, int minute, int second, boolean addTZIndication) {
        StringBuffer sb = new StringBuffer();
        appendInt(sb, year, 4);
        sb.append('-');
        appendInt(sb, month, 2);
        sb.append('-');
        appendInt(sb, day, 2);
        sb.append('T');
        appendInt(sb, hour, 2);
        sb.append(':');
        appendInt(sb, minute, 2);
        sb.append(':');
        appendInt(sb, second, 2);
        if(addTZIndication) {
            // append the offset of XSP server time zone
            sb.append(composeServerTZOffset());
        }

        return sb.toString();
    }

    /**
     * Returns the offset indication (formatting to XML standard) for XSP server.
     * It could be "Z" if the server is in UTC, or "-05:00" if the server is in NY for ex.
     */
    public static String composeServerTZOffset() {
        // append the offset of XSP server time zone
        int offset = TimeZone.getDefault().getRawOffset();
        if(offset==0) { // UTC time
            return "Z"; //$NON-NLS-1$
        } else { // local time
            StringBuffer sb = new StringBuffer();
            int h = (int)offset/ (60*60*1000);
            int min = (int) (offset-h*60*60*1000)/ (60*1000);
            if(offset>0) {
                sb.append('+');
                appendInt(sb, h, 2);
                sb.append(':');
                appendInt(sb, min, 2);
            } else if(offset<0) {
                sb.append('-');
                appendInt(sb, -h, 2);
                sb.append(':');
                appendInt(sb, -min, 2);
            }
            return sb.toString();
        }
    }

    private static void appendInt(StringBuffer sb, int v, int size) {
        String s = Integer.toString(v);
        switch(size-s.length()) {
            case 1:
                sb.append("0"); //$NON-NLS-1$
                break;
            case 2:
                sb.append("00"); //$NON-NLS-1$
                break;
            case 3:
                sb.append("000"); //$NON-NLS-1$
                break;
        }
        sb.append(s);
    }
}
