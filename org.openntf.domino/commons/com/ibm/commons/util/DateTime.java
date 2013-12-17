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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Some date-time converter utilities.
 * @ibm-not-published 
 */
public final class DateTime {

    public static final String SHORT_DATE       = "<date>"; //$NON-NLS-1$
    public static final String LONG_DATE        = "<long date>"; //$NON-NLS-1$
    public static final String SHORT_TIME       = "<time>"; //$NON-NLS-1$
    public static final String LONG_TIME        = "<long time>"; //$NON-NLS-1$
    public static final String SHORT_DATETIME   = "<date and time>"; //$NON-NLS-1$
    public static final String LONG_DATETIME    = "<long date and time>"; //$NON-NLS-1$
    public static final String FULL_DATE       = "<full>"; //$NON-NLS-1$
    public static final String MEDIUM_DATE       = "<medium>"; //$NON-NLS-1$

    /**
     * Convert a date object to a string using a SimpleDate format.
     * @param date the date to format
     * @param format the string format to use
     * @ibm-api
     */
    public static String formatDateTime( Date date, String format ) {
        return formatDateTime(date, format, Locale.getDefault(), TimeZone.getDefault());
    }
    /**
     * Convert a date object to a string using a default format.
     * @param date the date to format
     * @ibm-api
     */
    public static String formatDateTime( Date date) {
        return formatDateTime(date, null, Locale.getDefault(), TimeZone.getDefault());
    }
    /**
     * Convert a date object to a string using a default format.
     * @param date the date to format
     * @param loc the locale to use
     * @ibm-api
     */
    public static String formatDateTime( Date date, Locale loc) {
        return formatDateTime(date, null, loc, TimeZone.getDefault());
    }
    /**
     * Convert a date object to a string using a default format.
     * @param date the date to format
     * @param format the string format to use
     * @param loc the locale to use
     * @ibm-api
     */
    public static String formatDateTime(Date date, String format, Locale loc) {
        return formatDateTime(date, format, loc, TimeZone.getDefault());
    }
    /**
     * Format the date with the given pattern.
     * If the pattern is one of the predefined pattern, it may be impacted by the locale
     * (ex : dd/MM/yy in france and MM/dd/yy in the US)
     * The given date is converted to the given TZ
     * The TZ is also useful to display the timezone code for <long time> and <long date and time>
     * that contains a 3-letters code for the tz.
     * @param date the date to format
     * @param format the string format to use
     * @param loc the locale to use
     * @param tz the time zone to use
     * @ibm-api
     */
    public static String formatDateTime(Date date, String format, Locale loc, TimeZone tz) {
        if (date==null){
            return null;
        }
        if (loc==null) {
            loc = Locale.getDefault();
        }
        if (tz==null) {
            tz = TimeZone.getDefault();
        }
        if( !StringUtil.isEmpty(format) ) {
            if (format.charAt(0)=='<') {
                if( format.equals(SHORT_DATE) ) {
                    // tz is unuseful
                    return getShortDateFormatter(loc, TimeZone.getDefault()).format(date);
                } else if( format.equals(LONG_DATE) ) {
                    // tz is unuseful
                    return getLongDateFormatter(loc, TimeZone.getDefault()).format(date);
                } else if( format.equals(SHORT_TIME) ) {
                    return getShortTimeFormatter(loc, tz).format(date);
                } else if( format.equals(LONG_TIME) ) {
                    return getLongTimeFormatter(loc, tz).format(date);
                } else if( format.equals(SHORT_DATETIME) ) {
                    return getShortDatetimeFormatter(loc, tz).format(date);
                } else if( format.equals(LONG_DATETIME) ) {
                    return getLongDatetimeFormatter(loc, tz).format(date);
                }
            } else {
                java.text.DateFormat fmt = new java.text.SimpleDateFormat(format, loc);
                if (format!=null
                    && (   format.indexOf('h')!=-1
                        || format.indexOf('H')!=-1
                        || format.indexOf('m')!=-1
                        || format.indexOf('s')!=-1)) {
                    fmt.setTimeZone(tz);
                }
                return fmt.format(date);
            }
        }
        return getDefaultDateFormatter(loc).format(date);
    }


    /**
     * transform a complete pattern (following java standard) into a simplified one.
     * (tolerance on the separators... )
     */
    private static String getSimplifiedDateFormat(String javafmt) {
        if( !StringUtil.isEmpty(javafmt) ) {
            String fmt = formatCache.get(javafmt);
            if( fmt==null ) {
                StringBuilder b = new StringBuilder();
                // special case for MMMM or MMM
                javafmt = StringUtil.replace(javafmt, "MMMM", "J"); //$NON-NLS-1$ //$NON-NLS-2$
                javafmt = StringUtil.replace(javafmt, "MMM", "j"); //$NON-NLS-1$ //$NON-NLS-2$

                char lastch=0;
                for( int i=0; i<javafmt.length(); i++ ){
                    char ch=javafmt.charAt(i);
                    if( ch!=lastch ) {
                        switch(ch) {
                            case 'y':            b.append('Y'); break;
                            case 'M':            b.append('M'); break;
                            case 'J':            b.append('J'); break;
                            case 'j':            b.append('j'); break;
                            case 'd':            b.append('D'); break;
                            case 'h': case 'H':  b.append('H'); break;
                            case 'm':            b.append('N'); break;
                            case 's':            b.append('S'); break;
                            case 'z':            b.append('Z'); break;
                            case '/':
                            case '.':
                            case '-':  b.append('/'); break;
                            case ':':  b.append(':'); break;
                        }
                    }
                    lastch=ch;
                }
                fmt = b.toString();
                formatCache.put(javafmt,fmt);
            }
            return fmt;
        }
        return ""; //$NON-NLS-1$
    }
    /**
     * A cache for the formats
     */
    private static HashMap<String, String> formatCache = new HashMap<String, String>();

    /**
     * Getting a Date from a String. (the string represents a date in JVM TZ)
     * @param text the text of the date
     * @param format the string format to use
     * @ibm-api
     */
    public static Date parseDate( String text, String format) throws ParseException {
        return parseDate(text, format, TimeZone.getDefault(), Locale.getDefault());
    }
    /**
     * Getting a Date from a String. (the string represents a date in JVM TZ)
     * @param text the text of the date
     * @param format the string format to use
     * @param tz the time zone to use
     * @ibm-api
     */
    public static Date parseDate( String text, String format, TimeZone tz ) throws ParseException {
        return parseDate(text, format, tz, Locale.getDefault());
    }
    /**
     * Getting a Date from a String. (the string represents a date in JVM TZ)
     * @param text the text of the date
     * @param format the string format to use
     * @param loc the locale to use
     * @ibm-api
     */
    public static Date parseDate( String text, String format, Locale loc ) throws ParseException {
        return parseDate(text, format, TimeZone.getDefault(), loc);
    }

    /**
     * Getting a Date from a String
     * Warning ! here format must be an explicit format
     * The date to parse can contain a TZ code (if pattern contains 'z').
     * If there is no code, it is supposed to be in the given TZ.
     * BUT, the returned date is in the JVM TZ.
     * Note : Locale is only useful if the pattern contains MMM or MMMM, to interpret the name of the month
     * Note : If the format does not contain time (only date), there is no conversion to the given tz
     * @param text the text of the date
     * @param format the string format to use
     * @param loc the locale to use
     * @param tz the time zone to use
     * @ibm-api
     */
    public static Date parseDate( String text, String format, TimeZone tz, Locale loc ) throws ParseException {
        if (text==null){
            return null;
        }
        if (tz==null) {
            tz = TimeZone.getDefault();
        }
        if (loc==null) {
            loc = Locale.getDefault();
        }
        String dt = text.toLowerCase().trim();
        if( StringUtil.isEmpty(dt) ) {
            return null;
        }
        // Get the simplified format
        String fmt = getSimplifiedDateFormat(format);
        // Parse the date content
        int year=-1, month=-1, day=-1, hour=-1, minute=-1, second=-1, ampm=-1;
//        String tzCode;
        int pos = 0;
        boolean hasDate=false; boolean hasTime=false;
        // nb of characters for the year
        int nbCharForYear = 0;

        // if short name of month ends with a '.' : should be ignored
        int point = dt.indexOf('.');
        if (point!=-1 && (dt.charAt(point-1)>='a' && dt.charAt(point-1)<='z')) {
            dt = dt.substring(0, point) + dt.substring(point+1);
        }

        for( int fmtpos=0; pos<dt.length(); ) {
            int nbChar = 0;
            char c = dt.charAt(pos++);
            nbChar++;

            if( c>='0' && c<='9' ) {
            // try to read a number
                int num = c-'0';
                while( pos<dt.length() ) {
                    c = dt.charAt(pos);
                    if( c>='0' && c<='9' ) {
                        num = num*10 + (c-'0');
                        pos++;
                        nbChar++;
                    } else {
                        break;
                    }
                }
                if( fmtpos<fmt.length() ) {
                    switch( fmt.charAt(fmtpos) ) {
                        case 'Y':   year   = num; hasDate=true; nbCharForYear=nbChar; break;
                        case 'M':   month  = num; hasDate=true; break;
                        case 'J':   month  = num; hasDate=true; break;
                        case 'j':   month  = num; hasDate=true; break;
                        case 'D':   day    = num; hasDate=true; break;
                        case 'H':   hour   = num; hasTime=true; break;
                        case 'N':   minute = num; hasTime=true; break;
                        case 'S':   second = num; hasTime=true; break;
                    }
                    fmtpos++;
                } else {
                    throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                }
            } else if( Character.isLetter(c)  /*c>='a' && c<='z'*/)  {
            // try to read a word (ex : AM, PM, name of a month, 3-letters for tz... )
            	StringBuilder b = new StringBuilder();
                b.append(c);
                while( pos<dt.length() ) {
                    c = dt.charAt(pos);
                    if( Character.isLetter(c) )  {
                        b.append(c);
                        pos++;
                    } else {
                        break;
                    }
                }
                String ss = b.toString();
                if( ss.equals("am") ) { //$NON-NLS-1$
                    if( ampm>0 ) throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                    ampm=1;
                } else if( ss.equals("pm") ) { //$NON-NLS-1$
                    if( ampm>0 ) throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                    ampm=2;
                } else if( fmtpos<fmt.length() && (fmt.charAt(fmtpos)=='J' || fmt.charAt(fmtpos)=='j') ) {
                    // check if ss is a name of month
                    int i = computeMonth(ss, loc);
                    if (i!=-1) {
                        month = i;
                        fmtpos++;
                    } else {
                        throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                    }
                } else if( fmtpos<fmt.length() && fmt.charAt(fmtpos)=='Z' ) {
                	// PHIL: should be tested
                    // check if ss is a TZ code
                	boolean found = false;
                	String[] ids = TimeZone.getAvailableIDs();
                	for( int i=0; i<ids.length; i++ ) {
                		if( StringUtil.equalsIgnoreCase(ss,ids[i])) {
                            tz = TimeZone.getTimeZone(ids[i]);
                            fmtpos++;
                            found = true;
                            break;
                		}
                	}
                	if(!found) {
                		throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                	}
//                    TStringArrayMatching strArray = new TStringArrayMatching(TimeZone.getAvailableIDs());
//                    if (strArray.containsIgnoreCase(ss)==TStringArrayMatching.COMPLETE) {
//                        tz = TimeZone.getTimeZone(strArray.getExactString(ss));
//                        fmtpos++;
//                    } else {
//                        throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
//                    }
                } else {
                    throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                }
            } else if( c=='/' || c=='.' || c=='-' ) {
                if( fmtpos>=fmt.length() || fmt.charAt(fmtpos)!='/' ) {
                    throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                }
                fmtpos++;
            } else if( c==':' ) {
                if( fmtpos>=fmt.length() || fmt.charAt(fmtpos)!=':' ) {
                    throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
                }
                fmtpos++;
            } else if( c==' ' || c==',') {
                // Ignore
            } else {
                throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
            }
        }

        // Check the date part
        if( hasDate ) {
            if (nbCharForYear<=2) {
                year = convertYearIfInf100(year);
            }
            int maxDay;
            if((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
                maxDay = 30;
            } else if(month == 2) {
                maxDay = (  ((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0) ) ) ? 29 : 28 );
            } else {
                maxDay = 31;
            }
            if(    (day<1 || day>maxDay)
                || (month<1 || month>12)
                || (year<=0) ) {
                throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
            }
        } else {
            day = month = year = 0;
        }

        // Check the time part
        if( hasTime ) {

            // Warning about US conventions for hours !!!
            if (ampm==1 && hour==12) {
                hour = 0; // 00:00 (midnight) is coded by 12:00 AM
            } else if (ampm==2 && hour!=12) {
                // no change if hour is 12, because 12:00 (noon) is coded by 12:00 PM
                hour +=12;
            } else if (ampm==-1 && hour==24) {
                hour = 0; // 24:00 is accepted, it means 00:00
            }

            if( second==-1 ) second=0;
            // hour must be between 0 and 23 !
            if(    (hour<0 || hour>23)
                || (minute<0 || minute>59)
                || (second!=-1 && (second<0 || second>59)) ) {
                throw new ParseException( StringUtil.format("Invalid date '{0}'", text), pos ); //$NLS-DateTime.TFormatter.InvalidDate.Exception-1$
            }
        } else {
            hour = minute = second = 0;
        }

        // Valid!

        // case has only time : 1st january 1970
        if (!hasDate && hasTime) {
            year = 1970;
            month = 1;
            day = 1;
        }

        if( hasDate || hasTime ) {

            // case the date comes from another time zone (and hasTime)
            if (hasTime && ! tz.hasSameRules(TimeZone.getDefault())) {
                TGregorianCalendar cal = getCalendar();
                try {
                    cal.setTimeZone(tz);
                    cal.set(year, month-1, day, hour, minute, second);
                    cal.set(GregorianCalendar.MILLISECOND,0);
                    return cal.getTime();
                }finally{
                    recycleCalendar(cal,true);
                }
            }
            // case the date can be returned without conversion
            else {
                TGregorianCalendar cal = getCalendar();
                try {
                    cal.set(year, month-1, day, hour, minute, second);
                    cal.set(GregorianCalendar.MILLISECOND,0);
                    return cal.getTime();
                }finally{
                    recycleCalendar(cal,false);
                }
            }
        }
        return null;
    }

    /**
     * Returns an int to represent the given month (ex : 1 for January)
     * Returns -1 if the given string doesn't match any month
     * Try to match with complete name and then with short names
     */
    private static int computeMonth(String ss, Locale loc) {
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        String[] monthsArray = sym.getMonths();
        String[] shortMonthsArray = sym.getShortMonths();
        for (int i=0; i<monthsArray.length; i++) {
            if (StringUtil.equalsIgnoreCase(monthsArray[i], ss)) {
                return i+1;
            }
        }
        if (ss.endsWith(".")) { //$NON-NLS-1$
            ss = ss.substring(0, ss.length()-1);
        }
        for (int i=0; i<shortMonthsArray.length; i++) {
            if (shortMonthsArray[i].endsWith(".")) { //$NON-NLS-1$
                shortMonthsArray[i] = shortMonthsArray[i].substring(0, shortMonthsArray[i].length()-1);
            }
            if (StringUtil.equalsIgnoreCase(shortMonthsArray[i], ss)) {
                return i+1;
            }
        }
        return -1;
    }

//    private int computeMonth(String ss) {
//        return computeMonth(ss, Locale.getDefault());
//    }

    // NEVER USED !!!!!
    // Getting a Date from a String
//    public static java.util.Date getDateTime(String dateString,String format, Locale loc) {
//        if (loc==null) {
//            loc = Locale.getDefault();
//        }
//        if( StringUtil.isEmpty(dateString) ) {
//            return null;
//        }
//        try{
//            if( format.equals(SHORT_DATE) ) {
//                return getShortDateFormatter().parse(dateString);
//            }
//            if( format.equals(LONG_DATE) ) {
//                return getLongDatetimeFormatter().parse(dateString);
//            }
//            java.text.DateFormat fmt = new java.text.SimpleDateFormat(format);
//            return fmt.parse(dateString);
//        }catch(Exception e){
//            com.ibm.workplace.designer.util.TDiag.exception(e);
//            return null;
//        }
//    }

    /**
     * Convert a date from a user timezone to the JVM timezeone.
     * @param date the date to convert
     * @param userTZ the user timezone
     * @return the converted date
     * @ibm-api
     */
    public static Date convertDateIntoJVMTimeZone(Date date, TimeZone userTZ) {
        return timeZoneConversion(date, userTZ, TimeZone.getDefault());
    }
    /**
     * Convert a date from the JVM timezeone to a user timezone.
     * @param date the date to convert
     * @param userTZ the user timezone
     * @return the converted date
     * @ibm-api
     */
    public static Date convertDateFromJVMTimeZone(Date date, TimeZone userTZ) {
        return timeZoneConversion(date, TimeZone.getDefault(), userTZ);
    }

    /**
     * Convert a date from a source timezone to a target timezone.
     * @param date the date to convert
     * @param source the source timezone
     * @param target the target timezone
     * @return the converted date
     * @ibm-api
     */
    public static Date timeZoneConversion(Date date, TimeZone source, TimeZone target) {
        if( !source.equals(target) ) {
            // creation of a calendar for the date in the SOURCE tz
            TGregorianCalendar calSource=getCalendar();
            // creation of a calendar for the same 'absolute time' in TARGET tz
            TGregorianCalendar calTarget=getCalendar();
            try {
                calSource.setTimeZone(source);
                calSource.setTime(date);
                // offset to convert :  source -> GMT
                int zoneOffsetSource=-calSource.get(Calendar.ZONE_OFFSET);
                int dstOffsetSource=-calSource.get(Calendar.DST_OFFSET);

                calTarget.setTimeZone(target);
                calTarget.setMillis(calSource.getMillis());
                // offset to convert : GMT -> JVM tz
                int zoneOffsetTarget=calTarget.get(Calendar.ZONE_OFFSET);
                int dstOffsetTarget=calTarget.get(Calendar.DST_OFFSET);
                long totalOffset= ((long)zoneOffsetSource)+dstOffsetSource+zoneOffsetTarget+dstOffsetTarget;
                // obtain a new date in JVM tz
                date=new Date(date.getTime() + totalOffset);
            } finally {
                recycleCalendar(calSource,true);
                recycleCalendar(calTarget,true);
            }
        }
        return date;
    }

    /**
     * Convert a java.sql.Timestamp from a user timezone to the JVM timezeone.
     * @param date the date to convert
     * @param userTZ the user timezone
     * @return the converted date
     * @ibm-api
     */
    public static java.sql.Timestamp convertTimestampIntoJVMTimeZone(java.sql.Timestamp date, TimeZone userTZ) {
        return timeZoneTimestampConversion(date, userTZ, TimeZone.getDefault());
    }
    /**
     * Convert a java.sql.Timestamp from the JVM timezeone to a user timezone.
     * @param date the date to convert
     * @param userTZ the user timezone
     * @return the converted date
     * @ibm-api
     */
    public static java.sql.Timestamp convertTimestampFromJVMTimeZone(java.sql.Timestamp date, TimeZone userTZ) {
        return timeZoneTimestampConversion(date, TimeZone.getDefault(), userTZ);
    }

    /**
     * Convert a java.sql.Timestamp from a source timezeone to a target timezone.
     * @param date the date to convert
     * @param source the source timezone
     * @param target the target timezone
     * @return the converted date
     * @ibm-api
     */
    public static java.sql.Timestamp timeZoneTimestampConversion(java.sql.Timestamp date, TimeZone source, TimeZone target) {
        if( !source.equals(target) ) {
            // creation of a calendar for the date in the SOURCE tz
            TGregorianCalendar calSource=getCalendar();
            // creation of a calendar for the same 'absolute time' in TARGET tz
            TGregorianCalendar calTarget=getCalendar();
            try {
                calSource.setTimeZone(source);
                calSource.setTime(date);
                // offset to convert :  source -> GMT
                int zoneOffsetSource=-calSource.get(Calendar.ZONE_OFFSET);
                int dstOffsetSource=-calSource.get(Calendar.DST_OFFSET);

                calTarget.setTimeZone(target);
                calTarget.setMillis(calSource.getMillis());
                // offset to convert : GMT -> JVM tz
                int zoneOffsetTarget=calTarget.get(Calendar.ZONE_OFFSET);
                int dstOffsetTarget=calTarget.get(Calendar.DST_OFFSET);
                long totalOffset= ((long)zoneOffsetSource)+dstOffsetSource+zoneOffsetTarget+dstOffsetTarget;
                // obtain a new date in JVM tz
                date=new java.sql.Timestamp(date.getTime()+totalOffset);
            } finally {
                recycleCalendar(calSource,true);
                recycleCalendar(calTarget,true);
            }
        }
        return date;
    }



    /**
     * Get the real date format string.
     * Return the actual format from the abreviated form.
     * @param format the date formt
     * @param loc the Locale to use
     * @return the actual date format 
     * @ibm-api
     */
    public static String getFormatString( String format, Locale loc ) {
        if (loc==null) {
            loc = Locale.getDefault();
        }
        // if it is a predefined format (one of the 6 constants)
        if( !StringUtil.isEmpty(format) && format.charAt(0)=='<') {
            java.text.DateFormat fmt = null;
            if( format.equals(SHORT_DATE) ) {
                fmt = getShortDateFormatter(loc);
            } else if( format.equals(LONG_DATE) ) {
                fmt = getLongDateFormatter(loc);
            } else if( format.equals(SHORT_TIME) ) {
                fmt = getShortTimeFormatter(loc);
            } else if( format.equals(LONG_TIME) ) {
                fmt = getLongTimeFormatter(loc);
            } else if( format.equals(SHORT_DATETIME) ) {
                fmt = getShortDatetimeFormatter(loc);
            } else if( format.equals(LONG_DATETIME) ) {
                fmt = getLongDatetimeFormatter(loc);
            }
           
            if( fmt!=null && fmt instanceof SimpleDateFormat ) {
                return ((SimpleDateFormat)fmt).toPattern();
            }
        }
        // in other cases, return the format string itself
        return format;
    }

    /**
     * Get the real date format string.
     * Return the actual format from the abreviated form.
     * @param format the date formt
     * @param loc the Locale to use
     * @param tZ the Timezone to use
     * @return the actual date format 
     * @ibm-api
     */
    public static String getFormatString( String format, Locale loc,TimeZone tZ ) {
        if (loc==null) {
            loc = Locale.getDefault();
        }
        // if it is a predefined format (one of the 6 constants)
        if( !StringUtil.isEmpty(format) && format.charAt(0)=='<') {
            java.text.DateFormat fmt = null;
            if( format.equals(SHORT_DATE) ) {
                fmt = getShortDateFormatter(loc,tZ);
            } else if( format.equals(LONG_DATE) ) {
                fmt = getLongDateFormatter(loc,tZ);
            } else if( format.equals(SHORT_TIME) ) {
                fmt = getShortTimeFormatter(loc,tZ);
            } else if( format.equals(LONG_TIME) ) {
                fmt = getLongTimeFormatter(loc,tZ);
            } else if( format.equals(SHORT_DATETIME) ) {
                fmt = getShortDatetimeFormatter(loc,tZ);
            } else if( format.equals(LONG_DATETIME) ) {
                fmt = getLongDatetimeFormatter(loc,tZ);
            }
            else if( format.equals(FULL_DATE) ) {
                fmt = getFullDateFormatter(loc,tZ);
            }
            else if( format.equals(MEDIUM_DATE) ) {
                fmt = getMediumDateFormatter(loc,tZ);
            }
            if( fmt!=null && fmt instanceof SimpleDateFormat ) {
                return ((SimpleDateFormat)fmt).toPattern();
            }
        }
        // in other cases, return the format string itself
        return format;
    }

    /**
     * Get the real date format string.
     * Return the actual format from the abreviated form.
     * @param format the date formt
     * @return the actual date format 
     * @ibm-api
     */
    public static String getFormatString( String format) {
        return getFormatString(format, Locale.getDefault());
    }



    // DATE
    /**
     * Return the default formatter for a date.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDateFormatter() {
        return getShortDateFormatter();
    }
    /**
     * Return the default short formatter for a date.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDateFormatter() {
        return DateFormat.getDateInstance(DateFormat.SHORT);
    }
    /**
     * Return the default long formatter for a date.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDateFormatter() {
        return DateFormat.getDateInstance(DateFormat.LONG);
    }
    /**
     * Return the default formatter for a date.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDateFormatter(Locale loc) {
        return getShortDateFormatter(loc);
    }
    /**
     * Return the default short formatter for a date.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDateFormatter(Locale loc) {
        return DateFormat.getDateInstance(DateFormat.SHORT, loc);
    }
    /**
     * Return the default long  formatter for a date.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDateFormatter(Locale loc) {
        return DateFormat.getDateInstance(DateFormat.LONG, loc);
    }
    /**
     * Return the default formatter for a date.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDateFormatter(Locale loc, TimeZone tz) {
        return getShortDateFormatter(loc, tz);
    }
    /**
     * Return the default short formatter for a date.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    /**
     * Return the default long formatter for a date.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }
    
    /**
     * Return the default full formatter for a date.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getFullDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.FULL, loc);
        df.setTimeZone(tz);
        return df;
    }
    
    /**
     * Return the default medium formatter for a date.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getMediumDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.MEDIUM, loc);
        df.setTimeZone(tz);
        return df;
    }

    // TIME
    /**
     * Return the default formatter for a time.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultTimeFormatter() {
        return getShortTimeFormatter();
    }
    /**
     * Return the default short formatter for a time.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortTimeFormatter() {
        return DateFormat.getTimeInstance(DateFormat.SHORT);
    }
    /**
     * Return the default long formatter for a time.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongTimeFormatter() {
        return DateFormat.getTimeInstance(DateFormat.LONG);
    }
    /**
     * Return the default formatter for a time.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultTimeFormatter(Locale loc) {
        return getShortTimeFormatter(loc);
    }
    /**
     * Return the default short formatter for a time.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortTimeFormatter(Locale loc) {
        return DateFormat.getTimeInstance(DateFormat.SHORT, loc);
    }
    /**
     * Return the default long formatter for a time.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongTimeFormatter(Locale loc) {
        return DateFormat.getTimeInstance(DateFormat.LONG, loc);
    }
    /**
     * Return the default formatter for a time.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultTimeFormatter(Locale loc, TimeZone tz) {
        return getShortTimeFormatter(loc);
    }
    /**
     * Return the default short formatter for a time.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortTimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getTimeInstance(DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    /**
     * Return the default long formatter for a time.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongTimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }

    // DATETIME
    /**
     * Return the default formatter for a timestamp.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDatetimeFormatter() {
        return getShortDatetimeFormatter();
    }
    /**
     * Return the default short formatter for a timestamp.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDatetimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    }
    /**
     * Return the default long formatter for a timestamp.
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDatetimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
    }
    /**
     * Return the default formatter for a timestamp.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDatetimeFormatter(Locale loc) {
        return getShortDatetimeFormatter(loc);
    }
    /**
     * Return the default short formatter for a timestamp.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDatetimeFormatter(Locale loc) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, loc);
    }
    /**
     * Return the default long formatter for a timestamp.
     * @param loc the Locale to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDatetimeFormatter(Locale loc) {
        return DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG, loc);
    }
    /**
     * Return the default formatter for a timestamp.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getDefaultDatetimeFormatter(Locale loc, TimeZone tz) {
        return getShortDatetimeFormatter(loc);
    }
    /**
     * Return the default short formatter for a timestamp.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getShortDatetimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    /**
     * Return the default long formatter for a timestamp.
     * @param loc the Locale to use
     * @param tz the Timezone to use
     * @return the resulting DateFormat
     * @ibm-api
     */
    public static final DateFormat getLongDatetimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }
    
    
    // ==== DATE UTILITIES ===========================================================================
    
    public static final int limitDate = 39;
    
    /**
     * If a year is composed by only 2 characters, add 1900 or 2000.
     * If a year is composed by more than 2 characters : no change
     * @ibm-api
     */
    public static int convertYear(String st) {
        int result = Integer.parseInt(st);
        if (st.length()<=2) {
            return convertYearIfInf100(result);
        }
        return result;
    }
    /**
     * Add 1900 or 2000 to the given year, if the given int is between 0 and 99.
     * (else the year is returned without any change)
     * This method shoud be called for a year value that comes from a 2-characters long string
     * @ibm-api
     */
    public static int convertYearIfInf100(int year) {
        if( year>=0 && year<=limitDate ) {
            year+=2000;
        }
        if( year>limitDate && year<=99 ) {
            year+=1900;
        }
        return year;
    }

    /**
     * java.util.Date gregorian structure representation.
     * @ibm-not-published
     */
    public static class DateStruct {
        public DateStruct() {
            this( System.currentTimeMillis() );
        }
        /**
         * @param year - the normal year (ex : 2003)
         * @param month - the month between 1-12.
         */
        public DateStruct( int year, int month, int day, int hour, int minute, int second, int millis ) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.millis = millis;
        }
        public DateStruct( int year, int month, int day, int hour, int minute, int second ) {
            this( year, month, day, hour, minute, second, 0 );
        }
        public DateStruct( java.util.Date dt ) {
            this( dt.getTime() );
        }
        public DateStruct( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                this.year = gregorianCalendar.get(Calendar.YEAR);
                this.month = gregorianCalendar.get(Calendar.MONTH)+1;
                this.day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                this.hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
                this.minute = gregorianCalendar.get(Calendar.MINUTE);
                this.second = gregorianCalendar.get(Calendar.SECOND);
                this.millis = gregorianCalendar.get(Calendar.MILLISECOND);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
//        public String toISOString() {
//            return   TString.format( "{0}-{1}-{2}",
//                        TString.toString(year),
//                        TString.toString(month),
//                        TString.toString(day))
//                    + TString.format( " {0}:{1}:{2}.{3}",
//                        TString.toString(hour),
//                        TString.toString(minute),
//                        TString.toString(second)
//                        TString.toString(millis) );
//        }
        public java.util.Date createDate() {
            return createDate(year,month,day,hour,minute,second,millis);
        }
        public static final java.util.Date createDate( int year, int month, int day, int hour, int minute, int second ) {
            return createDate(year,month,day,hour,minute,second,0);
        }
        public static final java.util.Date createDate( int year, int month, int day, int hour, int minute, int second, int millis ) {
            return new java.util.Date( createDateAsLong(year,month,day,hour,minute,second,millis) );
        }
        /**
         *
         * @param y The year (already adjusted, no need to add 1900 or 2000)
         */
        public static final long createDateAsLong( int y, int m, int d, int h, int n, int s, int ms ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.set(y,m-1,d,h,n,s);
                gregorianCalendar.set(GregorianCalendar.MILLISECOND,ms);
                return gregorianCalendar.getMillis();
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        /**
         *
         * @param y The year (already adjusted, no need to add 1900 or 2000)
         */
        public static final long createDateAsLong( int y, int m, int d, int h, int n, int s ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.set(y,m-1,d,h,n,s);
                gregorianCalendar.set(GregorianCalendar.MILLISECOND,0);
                return gregorianCalendar.getMillis();
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public String toString() {
            return toString( year, month, day, hour, minute, second, millis );
        }
        public static String toString( long ms ) {
            return new DateStruct(ms).toString();
        }
        public static String toString( java.util.Date dt ) {
            // return TFormatter.getDefaultDateFormatter().format(dt);
            return new DateStruct(dt).toString();
        }
        public static String toString(int year, int month, int day, int hour, int minute, int second, int millis) {
            return   StringUtil.format( "{0}-{1}-{2}", //$NON-NLS-1$
                        StringUtil.toString(year),
                        StringUtil.toString(month),
                        StringUtil.toString(day))
                    + StringUtil.format( " {0}:{1}:{2}", //.{3}", //$NON-NLS-1$
                        StringUtil.toString(hour),
                        StringUtil.toString(minute),
                        StringUtil.toString(second) );
//                        StringUtil.toString(millis));
        }
        public static int getYear( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.YEAR);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getYear( java.util.Date dt ) {
            return getYear(dt.getTime());
        }
        public static int getMonth( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.MONTH)+1;
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getMonth( java.util.Date dt ) {
            return getMonth(dt.getTime());
        }
        public static int getDay( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getDay( java.util.Date dt ) {
            return getDay(dt.getTime());
        }
        public static int getHour( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.HOUR_OF_DAY);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getHour( java.util.Date dt ) {
            return getHour(dt.getTime());
        }
        public static int getMinute( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.MINUTE);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getMinute( java.util.Date dt ) {
            return getMinute(dt.getTime());
        }
        public static int getSecond( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.SECOND);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getSecond( java.util.Date dt ) {
            return getSecond(dt.getTime());
        }
        public static int getMilliSecond( long dt ) {
            TGregorianCalendar gregorianCalendar = getCalendar();
            try {
                gregorianCalendar.setMillis(dt);
                return gregorianCalendar.get(Calendar.MILLISECOND);
            } finally {
                recycleCalendar(gregorianCalendar);
            }
        }
        public static int getMilliSecond( java.util.Date dt ) {
            return getMilliSecond(dt.getTime());
        }
        public int day;
        public int month;
        public int year;
        public int hour;
        public int minute;
        public int second;
        public int millis;
    }
    
    /*
     * We need to derive the gregorian calendar because getTimeInMillis() &
     * setTimeInMillis() are protected (?!?) in the Calendar class.
     */
    public static final class TGregorianCalendar extends GregorianCalendar {
        private static final long serialVersionUID = 1L;
        public final long getMillis() {
            return getTimeInMillis();
        }
        public final void setMillis(long ms) {
            setTimeInMillis(ms);
        }
    }

    private static TGregorianCalendar[] gregorianCalendars = new TGregorianCalendar[16];
    private static int gcCount = 0;

    /**
     * To obtain a TGregorianCalendar.
     * Remember to call recycleCalendar when object is no more used.
     * @ibm-not-published
     */
    public static TGregorianCalendar getCalendar() {
        synchronized(gregorianCalendars) {
            if( gcCount==0 ) {
                return new TGregorianCalendar();
            }
            return gregorianCalendars[--gcCount];
        }
    }
    
    /**
     * @ibm-not-published
     */
    public static void recycleCalendar( TGregorianCalendar cal ) {
        recycleCalendar(cal, true); // TODO Mark: Check this with Phil
    }

    /**
     * @ibm-not-published
     */
    public static void recycleCalendar( TGregorianCalendar cal, boolean resetTZ ) {
    	if(resetTZ) {
    		cal.setTimeZone(TimeZone.getDefault());
    	}
        synchronized(gregorianCalendars) {
            if( gcCount<gregorianCalendars.length ) {
                gregorianCalendars[gcCount++] = cal;
            }
        }
    }
}
