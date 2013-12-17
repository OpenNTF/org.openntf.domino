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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A Base64 content transfer encoding filter stream.
 * <p>
 * From RFC 2045, section 6.8:
 * <p>
 * The Base64 Content-Transfer-Encoding is designed to represent
 * arbitrary sequences of octets in a form that need not be humanly
 * readable.  The encoding and decoding algorithms are simple, but the
 * encoded data are consistently only about 33 percent larger than the
 * unencoded data.
 * @ibm-api
 */
public class Base64InputStream extends FilterInputStream {

    private byte[] _buffer;
    private int _buflen;
    private int _index;
    private byte[] _decodeBuf;

    /**
     * Constructs an input stream that decodes an underlying Base64-encoded
     * stream.
     * @param in the Base64-encoded stream
     * @ibm-api
     */
    public Base64InputStream(InputStream in) {
        super(in);
        _buflen = 0;
        _index = 0;
        _decodeBuf = new byte[4];
        _buffer = new byte[3];
    }

    /**
     * Reads the next byte of data from the input stream.
     * @throws IOException IO Exception occurred
     * @return next byte in data stream
     */
    public int read() throws IOException {
        if (_index >= _buflen) {
            decode();
            if (_buflen == 0) {
                return -1;
            }
            _index = 0;
        }
        return _buffer[_index++] & 0xFF;
    }

    /**
     * Reads up to len bytes of data from the input stream into an array of 
     * bytes.
     * @param b buffer to put data
     * @param off offset to start of buffer
     * @param len number of bytes from buffer
     * @throws IOException IO Exception occurred
     * @return number of bytes read
     */
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            int l = 0; 
            for (; l < len; l++) {
                int ch = read();
                if (ch == -1) {
                    if (l == 0) {
                        return -1;
                    }
                    else {
                        break;
                    }
                }
                b[off+l] = (byte)ch;
            }
            return l;
        } catch (IOException ioe) {
            return -1;
        }
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from this
     * input stream without blocking by the next caller of a method for this 
     * input stream.
     * @throws IOException IO Exception occurred
     * @return number of bytes that can be read
     */
    public int available() throws IOException {
        return(in.available() * 3) / 4 + (_buflen-_index);
    }

    /**
     * Decode Base64 encoded buffer
     * @throws IOException IO Exception occurred
     */
    private void decode() throws IOException {
        _buflen = 0;

        // Loop until we hit EOF or non-line-termination char
        int ch = Ascii.LF;
        while (ch == Ascii.LF || ch == Ascii.CR){
            ch = in.read();
            if (ch == -1) {
                return;
            }
        }

        _decodeBuf[0] = (byte)ch;
        int j = 3, l;
        for (int k = 1; (l = in.read(_decodeBuf, k, j)) != j; k += l) {
            if (l == -1) {
                throw new IOException("Base64 encoding error"); // $NLS-Base64InputStream.Base64encodingerror-1$
            }
            j -= l;
        }

        byte b0 = IoConstants.B64_DST_MAP[_decodeBuf[0] & 0xFF];
        byte b2 = IoConstants.B64_DST_MAP[_decodeBuf[1] & 0xFF];
        _buffer[_buflen++] = (byte)(b0<<2 & 0xfc | b2 >>> 4 & 0x3);
        if (_decodeBuf[2] != Ascii.EQUALS) {
            b0 = b2;
            b2 = IoConstants.B64_DST_MAP[_decodeBuf[2] & 0xFF];
            _buffer[_buflen++] = (byte)(b0 << 4 & 0xf0 | b2 >>> 2 & 0xf);
            if (_decodeBuf[3] != Ascii.EQUALS) {
                byte b1 = b2;
                b2 = IoConstants.B64_DST_MAP[_decodeBuf[3] & 0xFF];
                _buffer[_buflen++] = (byte)(b1 << 6 & 0xc0 | b2 & 0x3f);
            }
        }
    }

}
