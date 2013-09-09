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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


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
public class Base64OutputStream extends FilterOutputStream {

    private byte[] _buffer;
    private int _buflen;
    private int _count;
    private int _lineLength;

    /**
     * Default constructor.
     * @param out the underlying output stream to encode
     * @pre out != null
     * @post out != null
     */
    public Base64OutputStream(OutputStream out) {
        this(out, Integer.MAX_VALUE);
    }

    /**
     * Constructor.
     * @param out the underlying output stream to encode
     * @param lineLength the line length
     * @pre out != null
     * @pre lineLength > 0
     * @post out != null
     */
    public Base64OutputStream(OutputStream out, int lineLength) {
        super(out);
        _buflen = 0;
        _count = 0;
        _buffer = new byte[3];
        _lineLength = lineLength;
    }

    /**
     * Writes the specified byte to this output stream.
     * @param ch character to write/encode
     * @throws IOException IO Exception occurred
     * @pre out != null
     * @post out != null
     */
    public void write(int ch) throws IOException {
        _buffer[_buflen++] = (byte)ch;
        if (_buflen == 3) {
            encode();
            _buflen = 0;
        }
    }

    /**
     * Writes <code>b.length</code> bytes from the specified byte array 
     * to this output stream.
     * @param b buffer to write/encode
     * @throws IOException IO Exception occurred
     * @pre out != null
     * @post out != null
     */
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array 
     * starting at offset <code>off</code> to this output stream.
     * @param b buffer to write/encode
     * @param off offset to start of buffer to write/encode
     * @param len number of bytes from buffer to write/encode
     * @throws IOException IO Exception occurred
     * @pre off < b.length
     * @pre off+len < b.length
     * @pre out != null
     * @post out != null
     */
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(b[off+i]);
        }
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be
     * written out.
     * @throws IOException IO Exception occurred
     * @pre out != null
     * @post _buflen == 0
     */
    public void flush() throws IOException {
        if (_buflen > 0) {
            encode();
            _buflen = 0;
        }
        out.flush();
    }

    /**
     * Closes this output stream and releases any system resources 
     * associated with this stream.
     * @throws IOException IO Exception occurred
     * @pre out != null
     * @post $none
     */
    public void close() throws IOException {
        flush();
        out.close();
    }

    /** 
     * Encode current buffer bytes
     * @throws IOException IO Exception occurred
     * @pre out != null
     */

    private void encode() throws IOException {
        if ((_count + 4) > _lineLength) {
            out.write(Ascii.CR);
            out.write(Ascii.LF);
            _count = 0;
        }
        if (_buflen==1) {
            byte b = _buffer[0];
            int i = 0;
            out.write(IoConstants.B64_SRC_MAP[b>>>2 & 0x3f]);
            out.write(IoConstants.B64_SRC_MAP[(b<<4 & 0x30) + (i>>>4 & 0xf)]);
            out.write(Ascii.EQUALS);
            out.write(Ascii.EQUALS);
        }
        else if (_buflen==2) {
            byte b1 = _buffer[0], b2 = _buffer[1];
            int i = 0;
            out.write(IoConstants.B64_SRC_MAP[b1>>>2 & 0x3f]);
            out.write(IoConstants.B64_SRC_MAP[(b1<<4 & 0x30) + (b2>>>4 & 0xf)]);
            out.write(IoConstants.B64_SRC_MAP[(b2<<2 & 0x3c) + (i>>>6 & 0x3)]);
            out.write(Ascii.EQUALS);
        }
        else {
            byte b1 = _buffer[0], b2 = _buffer[1], b3 = _buffer[2];
            out.write(IoConstants.B64_SRC_MAP[b1>>>2 & 0x3f]);
            out.write(IoConstants.B64_SRC_MAP[(b1<<4 & 0x30) + (b2>>>4 & 0xf)]);
            out.write(IoConstants.B64_SRC_MAP[(b2<<2 & 0x3c) + (b3>>>6 & 0x3)]);
            out.write(IoConstants.B64_SRC_MAP[b3 & 0x3f]);
        }
        _count += 4;
    }

}
