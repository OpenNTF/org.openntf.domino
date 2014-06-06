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

package com.ibm.commons.xml;


/**
 * XML Character utilities.
 * This class is similar to the one available in Apache Xerces.
 */
public class XMLChar {
    
    /**
     * Returns true if the specified character is a supplemental character.
     *
     * @param c The character to check.
     */
    public static boolean isSupplemental(int c) {
        return org.apache.xml.utils.XMLChar.isSupplemental(c);
    }

    /**
     * Returns true the supplemental character corresponding to the given
     * surrogates.
     *
     * @param h The high surrogate.
     * @param l The low surrogate.
     */
    public static int supplemental(char h, char l) {
        return org.apache.xml.utils.XMLChar.supplemental(h,l);
    }

    /**
     * Returns the high surrogate of a supplemental character
     *
     * @param c The supplemental character to "split".
     */
    public static char highSurrogate(int c) {
        return org.apache.xml.utils.XMLChar.highSurrogate(c);
    }

    /**
     * Returns the low surrogate of a supplemental character
     *
     * @param c The supplemental character to "split".
     */
    public static char lowSurrogate(int c) {
        return org.apache.xml.utils.XMLChar.lowSurrogate(c);
    }

    /**
     * Returns whether the given character is a high surrogate
     *
     * @param c The character to check.
     */
    public static boolean isHighSurrogate(int c) {
        return org.apache.xml.utils.XMLChar.isHighSurrogate(c);
    }

    /**
     * Returns whether the given character is a low surrogate
     *
     * @param c The character to check.
     */
    public static boolean isLowSurrogate(int c) {
        return org.apache.xml.utils.XMLChar.isLowSurrogate(c);
    }


    /**
     * Returns true if the specified character is valid. This method
     * also checks the surrogate character range from 0x10000 to 0x10FFFF.
     * <p>
     * If the program chooses to apply the mask directly to the
     * <code>CHARS</code> array, then they are responsible for checking
     * the surrogate character range.
     *
     * @param c The character to check.
     */
    public static boolean isValid(int c) {
        return org.apache.xml.utils.XMLChar.isValid(c);
    } 

    /**
     * Returns true if the specified character is invalid.
     *
     * @param c The character to check.
     */
    public static boolean isInvalid(int c) {
        return org.apache.xml.utils.XMLChar.isInvalid(c);
    }

    /**
     * Returns true if the specified character can be considered content.
     *
     * @param c The character to check.
     */
    public static boolean isContent(int c) {
        return org.apache.xml.utils.XMLChar.isContent(c);
    } 

    /**
     * Returns true if the specified character can be considered markup.
     * Markup characters include '&lt;', '&amp;', and '%'.
     *
     * @param c The character to check.
     */
    public static boolean isMarkup(int c) {
        return org.apache.xml.utils.XMLChar.isMarkup(c);
    }

    /**
     * Returns true if the specified character is a space character
     * as defined by production [3] in the XML 1.0 specification.
     *
     * @param c The character to check.
     */
    public static boolean isSpace(int c) {
        return org.apache.xml.utils.XMLChar.isSpace(c);
    }

    /**
     * Returns true if the specified character is a valid name start
     * character as defined by production [5] in the XML 1.0
     * specification.
     *
     * @param c The character to check.
     */
    public static boolean isNameStart(int c) {
        return org.apache.xml.utils.XMLChar.isNameStart(c);
    }

    /**
     * Returns true if the specified character is a valid name
     * character as defined by production [4] in the XML 1.0
     * specification.
     *
     * @param c The character to check.
     */
    public static boolean isName(int c) {
        return org.apache.xml.utils.XMLChar.isName(c);
    }

    /**
     * Returns true if the specified character is a valid NCName start
     * character as defined by production [4] in Namespaces in XML
     * recommendation.
     *
     * @param c The character to check.
     */
    public static boolean isNCNameStart(int c) {
        return org.apache.xml.utils.XMLChar.isNCNameStart(c);
    }

    /**
     * Returns true if the specified character is a valid NCName
     * character as defined by production [5] in Namespaces in XML
     * recommendation.
     *
     * @param c The character to check.
     */
    public static boolean isNCName(int c) {
        return org.apache.xml.utils.XMLChar.isNCName(c);
    } 

    /**
     * Returns true if the specified character is a valid Pubid
     * character as defined by production [13] in the XML 1.0
     * specification.
     *
     * @param c The character to check.
     */
    public static boolean isPubid(int c) {
        return org.apache.xml.utils.XMLChar.isPubid(c);
    }

    /**
     * Check to see if a string is a valid Name according to [5]
     * in the XML 1.0 Recommendation
     *
     * @param name string to check
     * @return true if name is a valid Name
     */
    public static boolean isValidName(String name) {
        return org.apache.xml.utils.XMLChar.isValidName(name);
    }
    

    /**
     * Check to see if a string is a valid NCName according to [4]
     * from the XML Namespaces 1.0 Recommendation
     *
     * @param name string to check
     * @return true if name is a valid NCName
     */
    public static boolean isValidNCName(String ncName) {
        return org.apache.xml.utils.XMLChar.isValidNCName(ncName);
    }

    /**
     * Check to see if a string is a valid Nmtoken according to [7]
     * in the XML 1.0 Recommendation
     *
     * @param nmtoken string to check
     * @return true if nmtoken is a valid Nmtoken 
     */
    public static boolean isValidNmtoken(String nmtoken) {
        return org.apache.xml.utils.XMLChar.isValidNmtoken(nmtoken);
    }

    /**
     * Returns true if the encoding name is a valid IANA encoding.
     * This method does not verify that there is a decoder available
     * for this encoding, only that the characters are valid for an
     * IANA encoding name.
     *
     * @param ianaEncoding The IANA encoding name.
     */
    public static boolean isValidIANAEncoding(String ianaEncoding) {
        return org.apache.xml.utils.XMLChar.isValidIANAEncoding(ianaEncoding);
    }

    /**
     * Returns true if the encoding name is a valid Java encoding.
     * This method does not verify that there is a decoder available
     * for this encoding, only that the characters are valid for an
     * Java encoding name.
     *
     * @param javaEncoding The Java encoding name.
     */
    public static boolean isValidJavaEncoding(String javaEncoding) {
        return org.apache.xml.utils.XMLChar.isValidJavaEncoding(javaEncoding);
    }
    
   /**
     * Simple check to determine if qname is legal. If it returns false
     * then <param>str</param> is illegal; if it returns true then 
     * <param>str</param> is legal.
     */
// Currently Linux JDK doesn't feature this    
//    public static boolean isValidQName(String str) {
//        return org.apache.xml.utils.XMLChar.isValidQName(str);
//    }      
}
