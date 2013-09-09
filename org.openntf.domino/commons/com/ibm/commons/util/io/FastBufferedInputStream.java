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

/**
 * Fast buffered input stream.
 * <p>
 * This implementation is faster than the standard BufferedInputStream
 * </p>
 * @ibm-api
 */
public class FastBufferedInputStream extends InputStream {

    private static final int DEFAULT_BUFFER_SIZE = 16384;

    private static final boolean FORCE_READ = false;
    
    private InputStream is;
    private byte[] buffer;
    private int count;
    private int pos;
    
    /** @ibm-api */
    public static InputStream get(InputStream source) {
        return source!=null ? new FastBufferedInputStream(source)
                            : null;
    }

    // Constructors
    /** @ibm-api */
    public FastBufferedInputStream(InputStream is, int size) {
        this.is = is;
        this.buffer = new byte[size];
    }

    /** @ibm-api */
    public FastBufferedInputStream(InputStream is) {
        this(is,DEFAULT_BUFFER_SIZE);
    }
    
    /** @ibm-api */
    public boolean isEOF() throws IOException {
        if( pos==count ) {
            count = is.read( buffer, 0, buffer.length );
            pos = 0;
            if( count<0 ) {
                count = 0;
                return true;
            }
        }
        return false;
    }

    /** @ibm-api */
    public int read() throws IOException {
        if( pos==count ) {
            count = is.read( buffer, 0, buffer.length );
            pos = 0;
            if( count<0 ) {
                count = 0;
                return -1;
            }
        }
        return buffer[pos++] & 0xFF;
    }

    /** @ibm-api */
    public int read(byte[] array) throws IOException {
        return read(array,0,array.length);
    }

    /** @ibm-api */
    public int read(byte[] array, int off, int length) throws IOException {
    	if(FORCE_READ) {
	    	int read = 0;
			while(read<length) {
				int r = _read(array,off,length);
				if(r<0) {
					return read>0 ? read : r;
				}
				off += r;
				length -= r;
				read += r;
			}
			return read;
    	} else {
    		return _read(array,off,length);
    	}
    }
    private int _read(byte[] array, int off, int length) throws IOException {
        int avail = count-pos;
        if( avail==0 ) {
            avail = count = is.read( buffer, 0, buffer.length );
            pos = 0;
            if( count<0 ) {
                count = 0;
                return -1;
            }
        }
        int toRead = length<avail ? length : avail;
        System.arraycopy(buffer,pos,array,off,toRead);
        pos += toRead;
        return toRead;
    }

    /** @ibm-api */
    public long skip(long n) throws IOException {
    	if(FORCE_READ) {
	    	long skip = 0;
			while(skip<n) {
				long r = _skip(n);
				if(r<0) {
					return skip>0 ? skip : r;
				}
				n -= r;
				skip += r;
			}
			return skip;
    	} else {
    		return _skip(n);
    	}
    }
    private long _skip(long n) throws IOException {
        int avail = count-pos;
        if( avail>0 ) {
            if( n<(long)avail ) {
                pos += n;
                return n;
            }
            pos = count;
            return avail;
        }
        return is.skip(n);
    }

    /** @ibm-api */
    public int available() throws IOException {
        return is.available() + (count-pos);
    }

    /** @ibm-api */
    public void close() throws IOException {
        is.close();
    }

    /** @ibm-api */
    public void mark(int int0) {
    }

    /** @ibm-api */
    public void reset() throws IOException {
    }

    /** @ibm-api */
    public boolean markSupported() {
        return false;
    }
}
