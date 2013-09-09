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

import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * Text utilities.
 * @ibm-api
 */
public final class TextUtil {

    /**
     * Add a string before each row of an existing string.
     * <p>
     * This function may be used by code generators in order to properly indent
     * code. To be consistent with Reader's behavior, a line is considered to be
     * terminated by any one of a line feed ('\n'), a carriage return ('\r) or
     * a carriage return immediately followed by a linefeed. In each case, the end
     * of line characters are preserved as they are.
     * </p>
     * @ibm-api
     */
    public static String indentString( String s, String indent ) {
        if( !StringUtil.isEmpty(s) && !StringUtil.isEmpty(indent) ) {
            FastStringBuffer buf = new FastStringBuffer();
            for( int i=0; i<s.length(); ) {
                // Add the indent string
                buf.append( indent );

                // And each character up to the end of the row
                while( i<s.length() ) {
                    char c = s.charAt(i++);
                    buf.append(c);

                    // The line ends with a single '\n'
                    if( c=='\n' ) {
                        break;
                    }

                    // The line ends with a single '\r', possibly followed by a '\n'
                    if( c=='\r' ) {
                        if( i<s.length()-1 ) {
                            c = s.charAt(i+1);
                            if( c=='\n' ) {
                                // Delegates the end to the '\n'
                                continue;
                            }
                        }
                        break;
                    }
                }
            }
            return buf.toString();
        }
        return s;
    }

    /**
     * Convert a Java source string to a regular string.
     * <p>
     * This replace all the escape sequences by the actual characters.
     * </p>
     * @ibm-api
     */
    public static String fromJavaString( String s ) {
        FastStringBuffer b = new FastStringBuffer();
        int len = s.length();
        for( int i=0; i<len; ) {
            char c = s.charAt(i++);
            if( c=='\\' ) {
                if( i<len ) {
                    c = s.charAt(i++);
                    switch(c) {
                        case 'b':   b.append( "\b" );  break; //$NON-NLS-1$
                        case 't':   b.append( "\t" );  break; //$NON-NLS-1$
                        case 'n':   b.append( "\n" );  break; //$NON-NLS-1$
                        case 'f':   b.append( "\f" );  break; //$NON-NLS-1$
                        case 'r':   b.append( "\r" );  break; //$NON-NLS-1$
                        case '\'':  b.append( "\'" );  break; //$NON-NLS-1$
                        case '\"':  b.append( "\"" );  break; //$NON-NLS-1$
                        case '\\':  b.append( "\\" );  break; //$NON-NLS-1$
                        case 'u':   {
                            int v=0;
                            for( int j=0; j<4; j++ ) {
                                if( i<len ) {
                                    c = s.charAt(i++);
                                    v=v*16+StringUtil.hexValue(c);
                                }
                            }
                            b.append((char)v);
                        } break;
                        default: {
                            b.append(c);
                        }
                    }
                }
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Format a string to be a JavaSource string.
     * <p>
     * This replace all the special characters by escape sequences.
     * </p>
     * @ibm-api
     */
    public static String toJavaString(String s, boolean addQuotes) {
        FastStringBuffer b = new FastStringBuffer();
        b.appendJavaString(s,addQuotes);
        return b.toString();
    }

    /**
     * Converts a XML string with internal entities into a Java string.
     * <p>
     * The entities parsed are
     * <UL>
     *  <LI><PRE>&amp;</PRE>
     *  <LI><PRE>&apos;</PRE>
     *  <LI><PRE>&gt;</PRE>
     *  <LI><PRE>&lt;</PRE>
     *  <LI><PRE>&quot;</PRE>
     * </UL>
     * </p>
     * @param s the XML string
     * @return the converted string
     * @ibm-api
     */
    public static String fromXMLString( String s ) {
    	if (s == null) {
    		return null;
    	}
        FastStringBuffer b = null;
        int pos = 0; int length = s.length();
        while(true) {
            int nextEntity = s.indexOf('&',pos);
            if( nextEntity<0 ) {
                nextEntity = length;
            }
            // Copy the part of the string
            if( nextEntity>pos ) {
                if( b==null ) {
                    b = new FastStringBuffer();
                }
                b.append(s, pos, nextEntity);
                pos = nextEntity;
            }
            if( pos<length ) {
                if( b==null ) {
                    b = new FastStringBuffer();
                }
                pos++; // Skip the leading '&'
                if( s.startsWith("amp;",pos) ) { //$NON-NLS-1$
                    b.append('&'); pos+=4;
                } else {
                    if( s.startsWith("apos;",pos) ) { //$NON-NLS-1$
                        b.append('\''); pos+=5;
                    } else {
                        if( s.startsWith("gt;",pos) ) { //$NON-NLS-1$
                            b.append('>'); pos+=3;
                        } else {
                            if( s.startsWith("lt;",pos) ) { //$NON-NLS-1$
                                b.append('<'); pos+=3;
                            } else {
                                if( s.startsWith("quot;",pos) ) { //$NON-NLS-1$
                                    b.append('\"'); pos+=5;
                                } else {
                                    if( s.startsWith("#",pos) ) { //$NON-NLS-1$
                                        pos++;
                                        int val = 0;
                                        do {
                                            int ch = s.charAt(pos++);
                                            if(ch==';') {
                                                break;
                                            }
                                            if(ch>='0' && ch<='9') {
                                                val = val*10 + (ch-'0');
                                            }                                               
                                        } while(true);
                                        b.append((char)val);
                                    } else {
                                    throw new AbstractRuntimeException( null, "Unknown entity", s ); //$NON-NLS-1$
                                }
                            }
                        }
                    }
                }
                }
            } else {
                return b!=null ? b.toString() : s;
            }
        }
    }

    /**
     * Converts a Java string to an XML one, with internal entities into a Java string.
     * <p>
     * The entities parsed are
     * <UL>
     *  <LI><PRE>&</PRE>
     *  <LI><PRE>'</PRE>
     *  <LI><PRE>></PRE>
     *  <LI><PRE><</PRE>
     *  <LI><PRE>"</PRE>
     * </UL>
     * </p>
     * @param s the Java string
     * @return the converted string
     * @ibm-api
     */
    public static String toXMLString(String s) {
        if( s==null ) {
            return null;
        }
        FastStringBuffer b = null;

        char[] chars = s.toCharArray();
        int length = chars.length;
        for( int i=0; i<length; i++ ) {
            char c = chars[i];

            // Is it a specific entity ?
            switch(c) {
                case '&':
                case '\'':
                case '>':
                case '<':
                case '\"': {
                    if( b==null ) {
                        b = new FastStringBuffer();
                        b.append(s, 0, i);
                    }
                    if( c=='&' )  { b.append( "&amp;" ); break; } //$NON-NLS-1$
                    if( c=='\'' ) { b.append( "&apos;" ); break; } //$NON-NLS-1$
                    if( c=='>' )  { b.append( "&gt;" ); break; } //$NON-NLS-1$
                    if( c=='<' )  { b.append( "&lt;" ); break; } //$NON-NLS-1$
                    if( c=='\"' ) { b.append( "&quot;" ); break; } //$NON-NLS-1$
                } break;
                default: {
                    if( b!=null ) {
                        b.append(c);
                    }
                }
            }
        }

        return b!=null ? b.toString() : s;
    }

    /**
     * Converts a Java string to an XML one, with internal entities into a Java string.
     * The entities parsed are
     * <p>
     * <UL>
     *  <LI><PRE>&</PRE>
     *  <LI><PRE>'</PRE>
     *  <LI><PRE>></PRE>
     *  <LI><PRE><</PRE>
     *  <LI><PRE>"</PRE>
     * </UL>
     * </p>
     * @param s the Java string
     * @param ascii ensure that all the character are in the ascii range, else they will be replaced by an entity
     * @return the converted string
     * @ibm-api
     */
    public static String toXMLString(String s, boolean ascii) {
        if( s==null ) {
            return null;
        }
        FastStringBuffer b = new FastStringBuffer(s.length()+256);
        char[] chars = s.toCharArray();
        int length = chars.length;
        for( int i=0; i<length; i++ ) {
            char c = chars[i];

            // Is it a specific entity ?
            switch(c) {
                case '&':
                case '\'':
                case '>':
                case '<':
                case '\"': {
                    if( c=='&' )  { b.append( "&amp;" ); break; } //$NON-NLS-1$
                    if( c=='\'' ) { b.append( "&apos;" ); break; } //$NON-NLS-1$
                    if( c=='>' )  { b.append( "&gt;" ); break; } //$NON-NLS-1$
                    if( c=='<' )  { b.append( "&lt;" ); break; } //$NON-NLS-1$
                    if( c=='\"' ) { b.append( "&quot;" ); break; } //$NON-NLS-1$
                } break;
                default: {
                    if( ascii && (c>127) ) {
                        b.append("&#"+Integer.toString((int)c)+";");
                    } else {
                        b.append(c);
                    }
                }
            }
        }

        return b.toString();
    }
    
// TESTS....    
//    public static final void main(String[] args) {
//        try {
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            while(true) {
//                System.out.println("Enter XML text (quit to leave):"); // $NON-NLS-1$
//                String text = "";
//                while(true) {
//                    String s = in.readLine();
//                    if(s.equals("quit")) { // $NON-NLS-1$
//                        System.exit(0);
//                    }
//                    if(s.equals("")) {
//                        break;
//                    }
//                    if(text.length()>0) {
//                        text = text + "\n"; // $NON-NLS-1$
//                    }
//                    text = text + s;
//                }
//                text = StringUtil.replace(text, '\n', ' ');
//                text = StringUtil.replace(text, '\n', '\0');
//                String xml = toXMLString(text,false);
//                System.out.println("");
//                System.out.println(xml);
//                System.out.println("");
//                System.out.println("");
//            }
//        } catch(Throwable t) {
//            t.printStackTrace();
//        }
//    }
}