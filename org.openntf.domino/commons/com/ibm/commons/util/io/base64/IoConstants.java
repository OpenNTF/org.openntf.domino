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
 * Useful constants for base64 package.
 * @ibm-not-published
 */
public class IoConstants {

    /** 
     * RFC822 requires that header lines are limited to 998 octets (excluding CRLF) 
     */
    public static final int MAX_RFC822_LINE_LENGTH = 998;
    
    /** 
     * RFC 2822 requires that body content lines can never be more than 998 octets (excluding CRLF) 
     */
    public static final int MAX_RFC2822_LINE_LENGTH = 998;
    
    /** 
     * Map used for Base64 encoding 
     */
    public static final char[] B64_SRC_MAP = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };

    /** 
     * Map used for Base64 decoding 
     */
    public static final byte[] B64_DST_MAP = new byte[256];

    /** 
     * Char array used in decimal to hexidecimal conversion. 
     */
    public static final char[] HEX_CHARS = { '0','1','2','3','4','5','6', '7',
                                             '8','9','A','B','C','D', 'E','F'};
    
    /** Construct mapping from source to destination maps */
    static {
        for (int i = 0; i < B64_DST_MAP.length; i++) {
            B64_DST_MAP[i] = -1;
        }
        for (int i = 0; i < B64_SRC_MAP.length; i++) {
            B64_DST_MAP[B64_SRC_MAP[i]] = (byte)i;
        }
    }
}
