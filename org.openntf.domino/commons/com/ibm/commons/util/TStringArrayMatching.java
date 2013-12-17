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


/**
 * A simple pattern matching that checks if a string is contained within a string array.
 */
public class TStringArrayMatching {

    public static final int COMPLETE    = 0;
    public static final int INCOMPLETE  = 1;
    public static final int UNMATCH     = 2;
    public static final int ERROR       = 3;
    
    /**
     * Constructor.
     */
    public TStringArrayMatching( String[] stringArray ) {
        if( stringArray==null ) {
            throw new IllegalArgumentException();
        }
        this.stringArray = stringArray;
    }

    /**
     * Get the string array to match.
     * @return the string array
     */
    public String[] getStringArray() {
        return stringArray;
    }

    /**
     * Set the string list to match.
     * @param stringList the string list
     */
    public void setStringArray( String[] stringArray ) {
        this.stringArray = stringArray;
    }

    /**
     * Check if a string is contained within the array.
     */
    public int match( String string ) {
        if( string==null ) {
            throw new IllegalArgumentException();
        }
        int result = UNMATCH;
        for( int i=0; i<stringArray.length; i++ ) {
            if( string.equals(stringArray[i]) ) {
                return COMPLETE;
            }
            if( string.startsWith(stringArray[i]) ) {
                result = result<INCOMPLETE ? result : INCOMPLETE;
            }
        }
        return result;
    }

    /**
     * Check if a string is contained within the array, without take care
     * of uppercase and lowercase.
     */
    public int containsIgnoreCase( String string ) {
        if( string==null ) {
            throw new IllegalArgumentException();
        }
        for( int i=0; i<stringArray.length; i++ ) {
            if( string.equalsIgnoreCase(stringArray[i]) ) {
                return COMPLETE;
            }
        }
        return UNMATCH;
    }

    /**
     * Returns the string in the array that corresponds to the given string
     * (without take care of upper and lowercase).
     * Returns null if string not found.
     */
    public String getExactString(String string) {
        if( string==null ) {
            throw new IllegalArgumentException();
        }
        for( int i=0; i<stringArray.length; i++ ) {
            if( string.equalsIgnoreCase(stringArray[i]) ) {
                return stringArray[i];
            }
        }
        return null;
    }

    /*
     *
     */
    private String[] stringArray;
}

