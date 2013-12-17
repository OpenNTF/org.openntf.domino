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

package com.ibm.commons.util.io.base64;

/**
 * Ascii constants.
 * <p>Some useful constants.</p>
 * @ibm-not-published
 */
public interface Ascii {

    public static final byte CASE_OFFSET = 32;

    public static final byte A = 65;
    public static final byte B = 66;
    public static final byte C = 67;
    public static final byte D = 68;
    public static final byte E = 69;
    public static final byte F = 70;
    public static final byte G = 71;
    public static final byte H = 72;
    public static final byte I = 73;
    public static final byte J = 74;
    public static final byte K = 75;
    public static final byte L = 76;
    public static final byte M = 77;
    public static final byte N = 78;
    public static final byte O = 79;
    public static final byte P = 80;
    public static final byte Q = 81;
    public static final byte R = 82;
    public static final byte S = 83;
    public static final byte T = 84;
    public static final byte U = 85;
    public static final byte V = 86;
    public static final byte W = 87;
    public static final byte X = 88;
    public static final byte Y = 89;
    public static final byte Z = 90;

    public static final byte a = 97;
    public static final byte b = 98;
    public static final byte c = 99;
    public static final byte d = 100;
    public static final byte e = 101;
    public static final byte f = 102;
    public static final byte g = 103;
    public static final byte h = 104;
    public static final byte i = 105;
    public static final byte j = 106;
    public static final byte k = 107;
    public static final byte l = 108;
    public static final byte m = 109;
    public static final byte n = 110;
    public static final byte o = 111;
    public static final byte p = 112;
    public static final byte q = 113;
    public static final byte r = 114;
    public static final byte s = 115;
    public static final byte t = 116;
    public static final byte u = 117;
    public static final byte v = 118;
    public static final byte w = 119;
    public static final byte x = 120;
    public static final byte y = 121;
    public static final byte z = 122;

    public static final byte ZERO = 48;
    public static final byte ONE = 49;
    public static final byte TWO = 50;
    public static final byte THREE = 51;
    public static final byte FOUR = 52;
    public static final byte FIVE = 53;
    public static final byte SIX = 54;
    public static final byte SEVEN = 55;
    public static final byte EIGHT = 56;
    public static final byte NINE = 57;

    public static final byte TAB = 9;
    public static final byte LF = 10;
    public static final byte FF = 12;
    public static final byte CR = 13;

    public static final byte SPACE = 32;
    public static final byte PERIOD = 46;
    public static final byte SLASH = 47;
    public static final byte BACK_SLASH = 92;
    public static final byte COLON = 58;
    public static final byte SEMICOLON =  59;
    public static final byte EQUALS = 61;
    public static final byte QUOTE = 34;
    public static final byte LT = 60;
    public static final byte GT = 62;
    public static final byte OP = 40;
    public static final byte CP = 41;
    public static final byte AT = 64;
    public static final byte UNDERSCORE = 95;
    public static final byte STAR = 42;
    public static final byte PERCENT = 37;
    public static final byte HYPHEN = 45;
    public static final byte PLUS = 43;
    public static final byte OPEN_CURLY = 123;
    public static final byte CLOSE_CURLY = 125;
    public static final byte OPEN_ANGLE = 60;
    public static final byte CLOSE_ANGLE = 62;


    /** The CR/LF pair.*/
    public static final byte[] CRLF = { CR, LF };
}

