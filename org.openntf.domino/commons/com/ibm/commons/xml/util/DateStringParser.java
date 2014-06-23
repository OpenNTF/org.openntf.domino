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

/**
 * String parser utility using to parse XMI date.
 */
public class DateStringParser {

    public DateStringParser(String s) {
        this.string = s;
        this.count = s.length();
    }

    public boolean isEOF() {
        return pointer>=count;
    }

    public int getCurrentPosition() {
        return pointer;
    }

    public boolean startsWith( char c ) {
        if(pointer<count) {
            return string.charAt(pointer)==c;
        }
        return false;
    }

    public boolean match( char c ) {
        if( startsWith(c) ) {
            pointer++;
            return true;
        }
        return false;
    }


    public int matchInteger(int len) {
        if(pointer+len>count) {
            return Integer.MIN_VALUE;
        }
        int ptr = pointer;
        // Extract the integer
        int v = 0;
        while( ptr<pointer+len ) {
            char c = string.charAt(ptr);
            if( Character.isDigit(c) ) {
                v = v*10 + (c-'0');
                ptr++;
            } else {
                return Integer.MIN_VALUE;
            }
        }
        pointer = ptr;
        return v;
    }

    public int getNextInteger() {
        int ptr = pointer;
        // Check if it is a negative number
        boolean negate=false;
        if( ptr<count && string.charAt(ptr)=='-' ) {
            negate = true;
            ptr++;
        }
        // Extract the integer
        int v = 0;
        while( ptr<count ) {
            char c = string.charAt(ptr);
            if( Character.isDigit(c) ) {
                v = v*10 + (c-'0');
                ptr++;
            } else {
                break;
            }
        }
        // Return the integer if exists, else the min integer valeu
        if( ptr>(pointer+(negate?1:0)) ) {
            pointer = ptr;
            return negate ? -v : v;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private String string;
    private int count;
    private int pointer;
}
