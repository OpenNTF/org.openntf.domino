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

package com.ibm.commons.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.ibm.commons.util.StringUtil;

/**
 * InputStream that reads its data from a reader.
 * @ibm-api
 */
public class ReaderInputStream extends InputStream {
    private Reader _in;
    private String _encoding = System.getProperty("file.encoding"); //$NON-NLS-1$
    private byte[] _slack;
    private int _begin;
    
    //private final static String s_streamClosedMessage = "ReaderInputStream.streamclosed.exception";  //$NON-NLS-1$
    
    //TODO: Problems with ResourceBundleHelper causing LinkageErrors.
    private final static String s_streamClosedMessage = "Stream closed."; // $NON-NLS-1$


    /**
     * Construct a <CODE>ReaderInputStream</CODE>
     * for the specified <CODE>Reader</CODE>.
     *
     * @param reader   <CODE>Reader</CODE>.  Must not be <code>null</code>.
     * @ibm-api
     */
    public ReaderInputStream(Reader reader) {
        _in = reader;
    }

    /**
     * Construct a <CODE>ReaderInputStream</CODE>
     * for the specified <CODE>Reader</CODE>,
     * with the specified encoding.
     *
     * @param reader     non-null <CODE>Reader</CODE>.
     * @param encoding   non-null <CODE>String</CODE> encoding.
     * @ibm-api
     */
    public ReaderInputStream(Reader reader, String encoding) {
        this(reader);
        
        if (StringUtil.isNotEmpty(encoding)) {
            _encoding = encoding;
        }
    }

    /**
     * Reads from the <CODE>Reader</CODE>, returning the same value.
     *
     * @return the value of the next character in the <CODE>Reader</CODE>.
     *
     * @exception IOException if the original <code>Reader</code> fails to be read
     * @ibm-api
     */
    public int read() throws IOException {
        if (_in == null) {
            throw new IOException(s_streamClosedMessage);
        }

        byte result;
        if (_slack != null && _begin < _slack.length) {
            result = _slack[_begin];
            if (++_begin == _slack.length) {
                _slack = null;
            }
        } else {
            byte[] buf = new byte[1];
            if (read(buf, 0, 1) <= 0) {
                result = -1;
            }
            result = buf[0];
        }

        if (result < -1) {
            result += 256;
        }

        return result;
    }

    /**
     * Reads from the <code>Reader</code> into a byte array
     *
     * @param b  the byte array to read into
     * @param off the offset in the byte array
     * @param len the length in the byte array to fill
     * @return the actual number read into the byte array, -1 at
     *         the end of the stream
     * @exception IOException if an error occurs
     * @ibm-api
     */
    public int read(byte[] b, int off, int len)
        throws IOException {
        if (_in == null) {
            throw new IOException(s_streamClosedMessage);
        }

        while (_slack == null) {
            char[] buf = new char[len];
            int n = _in.read(buf);
            if (n == -1) {
                return -1;
            }
            if (n > 0) {
                _slack = new String(buf, 0, n).getBytes(_encoding);
                _begin = 0;
            }
        }

        if (len > _slack.length - _begin) {
            len = _slack.length - _begin;
        }

        System.arraycopy(_slack, _begin, b, off, len);

        if ((_begin += len) >= _slack.length) {
            _slack = null;
        }

        return len;
    }

    /**
     * Marks the read limit of the StringReader.
     *
     * @param limit the maximum limit of bytes that can be read before the
     *              mark position becomes invalid
     * @ibm-api
     */
    public void mark(final int limit) {
        try {
            _in.mark(limit);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }


    /**
     * @return   the current number of bytes ready for reading
     * @exception IOException if an error occurs
     * @ibm-api
     */
    public int available() throws IOException {
        if (_in == null) {
            throw new IOException(s_streamClosedMessage);
        }
        if (_slack != null) {
            return _slack.length - _begin;
        }
        if (_in.ready()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return false - mark is not supported
     * @ibm-api
     */
    public boolean markSupported () {
        return false;   // would be imprecise
    }

    /**
     * Resets the StringReader.
     *
     * @exception IOException if the StringReader fails to be reset
     * @ibm-api
     */
    public void reset() throws IOException {
        if (_in == null) {
            throw new IOException(s_streamClosedMessage);
        }
        _slack = null;
        _in.reset();
    }

    /**
     * Closes the reader.
     *
     * @exception IOException if the original StringReader fails to be closed
     * @ibm-api
     */
    public void close() throws IOException {
        if (_in == null) {
            return;
        }
        _in.close();
        _slack = null;
        _in = null;
    }
}
