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

import java.text.*;
import java.util.*;

/**
 * Default formatter objects.
 * This class simply exports some common default values that can be used by another classes.
 *
 * A lot of methods in this class are published in I18n class : look at I18n for more
 * detailed documentation.
 * 
 * @ibm-not-published
 * @deprecated
 */
public final class FormatUtil {
    
    private static final String INVALID_DATE = "Invalid date '{0}'"; // $NLS-FormatUtil.Invaliddate0-1$
    
    /* -------------------------- NUMBERS ----------------------------------------- */

    // Getting an Integer from a String (no locale / no tolerance)
    // only strings like '30000' are ok.
    public static long getInteger( String integerString) {
        if(StringUtil.isEmpty(integerString) ) {
            return 0;
        }
        return Long.parseLong(integerString);
    }

    // Getting an Integer from a String (with locale)
    // localized strings are accepted (ex : 30 000 in france)
    public static long parseInt(String src, Locale loc) throws ParseException{
        if (loc==null) {
            loc = Locale.getDefault();
        }
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        nf.setParseIntegerOnly(true);
        return nf.parse(src).longValue();
    }

    // Getting a Decimal from a String (no locale / no tolerance)
    public static double getDecimal(String doubleString) {
        if(StringUtil.isEmpty(doubleString) ) {
            return 0;
        }
        return Double.parseDouble(doubleString);
    }
    // Getting a Decimal from a String (accept comma or point in some locales)
    public static double parseFloatWithDecimalSeparatorTolerance(String doubleString, Locale loc) {
        doubleString = removeThousandSeparator(doubleString, loc);
        doubleString = replaceCommaByPoint(doubleString, loc);
        return getDecimal(doubleString);
    }

    /**
     * If a decimal number contains a comma and no point, and if the given
     * locale uses a comma for decimal separator, the comma is replaced by
     * a point.
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


    // Getting a Decimal from a String (with locale)
    public static double parseFloat(String src, Locale loc) throws ParseException{
        if (loc==null) {
            loc = Locale.getDefault();
        }
        return NumberFormat.getInstance(loc).parse(src).floatValue();
    }



    // Formatting an integer
    public static String formatInteger( long l, String format, String emptyFormat ) {
        return formatInteger(l, format, emptyFormat, Locale.getDefault());
    }
    // Formatting an integer
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


    // Formatting a decimal
    public static String formatDecimal( double d, String format, String emptyFormat ) {
        return formatDecimal(d, format, emptyFormat, Locale.getDefault());
    }
    // Formatting a decimal
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



    /* -------------------------- DATE ----------------------------------------- */

    public static final String SHORT_DATE       = "<date>"; //$NON-NLS-1$
    public static final String LONG_DATE        = "<long date>"; //$NON-NLS-1$
    public static final String SHORT_TIME       = "<time>"; //$NON-NLS-1$
    public static final String LONG_TIME        = "<long time>"; //$NON-NLS-1$
    public static final String SHORT_DATETIME   = "<date and time>"; //$NON-NLS-1$
    public static final String LONG_DATETIME    = "<long date and time>"; //$NON-NLS-1$

    // Formatting a date
    public static String formatDateTime( Date date, String format ) {
        return formatDateTime(date, format, Locale.getDefault(), TimeZone.getDefault());
    }
    public static String formatDateTime( Date date) {
        return formatDateTime(date, null, Locale.getDefault(), TimeZone.getDefault());
    }
    public static String formatDateTime( Date date, Locale loc) {
        return formatDateTime(date, null, loc, TimeZone.getDefault());
    }
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
     */
    public static String formatDateTime(Date date, String format, Locale loc, TimeZone tz) {
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
                FastStringBuffer b = new FastStringBuffer();
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
     * a cache for the formats
     */
    private static Map<String, String> formatCache = new HashMap<String, String>();

    /**
     * Getting a Date from a String. (the string represents a date in server TZ)
     */
    public static Date parseDate( String text, String format) throws ParseException {
        return parseDate(text, format, TimeZone.getDefault(), Locale.getDefault());
    }
    /**
     * Getting a Date from a String.
     */
    public static Date parseDate( String text, String format, TimeZone tz ) throws ParseException {
        return parseDate(text, format, tz, Locale.getDefault());
    }
    /**
     * Getting a Date from a String.
     */
    public static Date parseDate( String text, String format, Locale loc ) throws ParseException {
        return parseDate(text, format, TimeZone.getDefault(), loc);
    }

    /**
     * Getting a Date from a String
     * Warning ! here format must be an explicit format
     * The date to parse can contain a TZ code (if pattern contains 'z').
     * If there is no code, it is supposed to be in the given TZ.
     * BUT, the returned date is in the server TZ.
     * Note : Locale is only useful if the pattern contains MMM or MMMM, to interpret the name of the month
     * Note : If the format does not contain time (only date), there is no conversion to the given tz
     */
    public static Date parseDate( String text, String format, TimeZone tz, Locale loc ) throws ParseException {
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
                    throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
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
                    if( ampm>0 ) throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                    ampm=1;
                } else if( ss.equals("pm") ) { //$NON-NLS-1$
                    if( ampm>0 ) throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                    ampm=2;
                } else if( fmtpos<fmt.length() && (fmt.charAt(fmtpos)=='J' || fmt.charAt(fmtpos)=='j') ) {
                    // check if ss is a name of month
                    int i = computeMonth(ss, loc);
                    if (i!=-1) {
                        month = i;
                        fmtpos++;
                    } else {
                        throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                    }
                } else if( fmtpos<fmt.length() && fmt.charAt(fmtpos)=='Z' ) {
                    // check if ss is a TZ code
                    TStringArrayMatching strArray = new TStringArrayMatching(TimeZone.getAvailableIDs());
                    if (strArray.containsIgnoreCase(ss)==TStringArrayMatching.COMPLETE) {
                        tz = TimeZone.getTimeZone(strArray.getExactString(ss));
                        fmtpos++;
                    } else {
                        throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                    }
                } else {
                    throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                }
            } else if( c=='/' || c=='.' || c=='-' ) {
                if( fmtpos>=fmt.length() || fmt.charAt(fmtpos)!='/' ) {
                    throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                }
                fmtpos++;
            } else if( c==':' ) {
                if( fmtpos>=fmt.length() || fmt.charAt(fmtpos)!=':' ) {
                    throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
                }
                fmtpos++;
            } else if( c==' ' || c==',') {
                // Ignore
            } else {
                throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
            }
        }

        // Check the date part
        if( hasDate ) {
            if (nbCharForYear<=2) {
                year = DateTime.convertYearIfInf100(year);
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
                throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
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
                throw new ParseException( StringUtil.format(INVALID_DATE, text), pos );
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
                DateTime.TGregorianCalendar cal = DateTime.getCalendar();
                try {
                    cal.setTimeZone(tz);
                    cal.set(year, month-1, day, hour, minute, second);
                    return cal.getTime();
                }finally{
                    DateTime.recycleCalendar(cal);
                }
            }
            // case the date can be returned without conversion
            else {
                return new DateTime.DateStruct(year, month, day, hour, minute, second).createDate();
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

    public static Date convertDateIntoServerTimeZone(Date date, TimeZone userTZ) {
        return timeZoneConversion(date, userTZ, TimeZone.getDefault());
    }
    public static Date convertDateIntoUserTimeZone(Date date, TimeZone userTZ) {
        return timeZoneConversion(date, TimeZone.getDefault(), userTZ);
    }

    public static Date timeZoneConversion(Date date, TimeZone source, TimeZone target) {
        if( !source.equals(target) ) {
            // creation of a calendar for the date in the SOURCE tz
            DateTime.TGregorianCalendar calSource=DateTime.getCalendar();
            // creation of a calendar for the same 'absolute time' in TARGET tz
            DateTime.TGregorianCalendar calTarget=DateTime.getCalendar();
            try {
                calSource.setTimeZone(source);
                calSource.setTime(date);
                // offset to convert :  source -> GMT
                int zoneOffsetSource=-calSource.get(Calendar.ZONE_OFFSET);
                int dstOffsetSource=-calSource.get(Calendar.DST_OFFSET);

                calTarget.setTimeZone(target);
                calTarget.setMillis(calSource.getMillis());
                // offset to convert : GMT -> XSP server tz
                int zoneOffsetTarget=calTarget.get(Calendar.ZONE_OFFSET);
                int dstOffsetTarget=calTarget.get(Calendar.DST_OFFSET);
                long totalOffset=((long)zoneOffsetSource)+dstOffsetSource+zoneOffsetTarget+dstOffsetTarget;
                // obtain a new date in xsp server tz
                date=new Date(date.getTime()+totalOffset);
            } finally {
                DateTime.recycleCalendar(calSource);
                DateTime.recycleCalendar(calTarget);
            }
        }
        return date;
    }

    public static java.sql.Timestamp convertTimestampIntoServerTimeZone(java.sql.Timestamp date, TimeZone userTZ) {
        return timeZoneTimestampConversion(date, userTZ, TimeZone.getDefault());
    }
    public static java.sql.Timestamp convertTimestampIntoUserTimeZone(java.sql.Timestamp date, TimeZone userTZ) {
        return timeZoneTimestampConversion(date, TimeZone.getDefault(), userTZ);
    }

    public static java.sql.Timestamp timeZoneTimestampConversion(java.sql.Timestamp date, TimeZone source, TimeZone target) {
        if( !source.equals(target) ) {
            // creation of a calendar for the date in the SOURCE tz
            DateTime.TGregorianCalendar calSource=DateTime.getCalendar();
            // creation of a calendar for the same 'absolute time' in TARGET tz
            DateTime.TGregorianCalendar calTarget=DateTime.getCalendar();
            try {
                calSource.setTimeZone(source);
                calSource.setTime(date);
                // offset to convert :  source -> GMT
                int zoneOffsetSource=-calSource.get(Calendar.ZONE_OFFSET);
                int dstOffsetSource=-calSource.get(Calendar.DST_OFFSET);

                calTarget.setTimeZone(target);
                calTarget.setMillis(calSource.getMillis());
                // offset to convert : GMT -> XSP server tz
                int zoneOffsetTarget=calTarget.get(Calendar.ZONE_OFFSET);
                int dstOffsetTarget=calTarget.get(Calendar.DST_OFFSET);
                long totalOffset=((long)zoneOffsetSource)+dstOffsetSource+zoneOffsetTarget+dstOffsetTarget;
                // obtain a new date in xsp server tz
                date=new java.sql.Timestamp(date.getTime()+totalOffset);
            } finally {
                DateTime.recycleCalendar(calSource);
                DateTime.recycleCalendar(calTarget);
            }
        }
        return date;
    }



    /**
     * Get the real format string
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
     * Get the real format string
     */
    public static String getFormatString( String format) {
        return getFormatString(format, Locale.getDefault());
    }



    // DATE
    public static final DateFormat getDefaultDateFormatter() {
        return getShortDateFormatter();
    }
    public static final DateFormat getShortDateFormatter() {
        return DateFormat.getDateInstance(DateFormat.SHORT);
    }
    public static final DateFormat getLongDateFormatter() {
        return DateFormat.getDateInstance(DateFormat.LONG);
    }
    public static final DateFormat getDefaultDateFormatter(Locale loc) {
        return getShortDateFormatter(loc);
    }
    public static final DateFormat getShortDateFormatter(Locale loc) {
        return DateFormat.getDateInstance(DateFormat.SHORT, loc);
    }
    public static final DateFormat getLongDateFormatter(Locale loc) {
        return DateFormat.getDateInstance(DateFormat.LONG, loc);
    }
    public static final DateFormat getDefaultDateFormatter(Locale loc, TimeZone tz) {
        return getShortDateFormatter(loc, tz);
    }
    public static final DateFormat getShortDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    public static final DateFormat getLongDateFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateInstance(DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }

    // TIME
    public static final DateFormat getDefaultTimeFormatter() {
        return getShortTimeFormatter();
    }
    public static final DateFormat getShortTimeFormatter() {
        return DateFormat.getTimeInstance(DateFormat.SHORT);
    }
    public static final DateFormat getLongTimeFormatter() {
        return DateFormat.getTimeInstance(DateFormat.LONG);
    }
    public static final DateFormat getDefaultTimeFormatter(Locale loc) {
        return getShortTimeFormatter(loc);
    }
    public static final DateFormat getShortTimeFormatter(Locale loc) {
        return DateFormat.getTimeInstance(DateFormat.SHORT, loc);
    }
    public static final DateFormat getLongTimeFormatter(Locale loc) {
        return DateFormat.getTimeInstance(DateFormat.LONG, loc);
    }
    public static final DateFormat getDefaultTimeFormatter(Locale loc, TimeZone tz) {
        return getShortTimeFormatter(loc);
    }
    public static final DateFormat getShortTimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getTimeInstance(DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    public static final DateFormat getLongTimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }

    // DATETIME
    public static final DateFormat getDefaultDatetimeFormatter() {
        return getShortDatetimeFormatter();
    }
    public static final DateFormat getShortDatetimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    }
    public static final DateFormat getLongDatetimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
    }
    public static final DateFormat getDefaultDatetimeFormatter(Locale loc) {
        return getShortDatetimeFormatter(loc);
    }
    public static final DateFormat getShortDatetimeFormatter(Locale loc) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, loc);
    }
    public static final DateFormat getLongDatetimeFormatter(Locale loc) {
        return DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG, loc);
    }
    public static final DateFormat getDefaultDatetimeFormatter(Locale loc, TimeZone tz) {
        return getShortDatetimeFormatter(loc);
    }
    public static final DateFormat getShortDatetimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df =  DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, loc);
        df.setTimeZone(tz);
        return df;
    }
    public static final DateFormat getLongDatetimeFormatter(Locale loc, TimeZone tz) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, loc);
        df.setTimeZone(tz);
        return df;
    }
    
}
