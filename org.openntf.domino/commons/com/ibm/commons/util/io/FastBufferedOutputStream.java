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
import java.io.OutputStream;

/**
 * Fast buffered output stream.
 * <p>
 * This implementation is faster than the standard BufferedOutputStream
 * </p>
 * @ibm-api
 */
public class FastBufferedOutputStream extends OutputStream {

    private static final int DEFAULT_BUFFER_SIZE = 16384;

    private OutputStream os;
    private byte[] buffer;
    private int bufferLength;
    private int pos;

    /** @ibm-api */
    public static OutputStream get(OutputStream source) {
        return source!=null ? new FastBufferedOutputStream(source)
                            : null;
    }

    /** @ibm-api */
    public FastBufferedOutputStream( OutputStream os, int size ) {
        this.os = os;
        this.bufferLength = size;
        this.buffer = new byte[size];
    }

    /** @ibm-api */
    public FastBufferedOutputStream( OutputStream os ) {
        this(os,DEFAULT_BUFFER_SIZE);
    }

    /** @ibm-api */
    public void write(int b) throws IOException {
        if( pos==bufferLength ) {
            os.write(buffer,0,bufferLength);
            pos = 0;
        }
        buffer[pos++] = (byte)b;
    }

    /** @ibm-api */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /** @ibm-api */
    public void write(byte b[], int off, int len) throws IOException {
        while(len>0) {
            if( pos==bufferLength ) {
                os.write(buffer,0,bufferLength);
                pos = 0;
            }
            int avail = bufferLength-pos;
            int toWrite = len>avail ? avail : len;
            System.arraycopy(b,off,buffer,pos,toWrite);
            pos += toWrite;
            off += toWrite;
            len -= toWrite;
        }
    }

    /** @ibm-api */
    public void flush() throws IOException {
        if( pos>0 ) {
            os.write(buffer,0,pos);
            pos = 0;
        }
        os.flush();
    }

    /** @ibm-api */
    public void close() throws IOException {
        if( pos>0 ) {
            os.write(buffer,0,pos);
            pos = 0;
        }
        os.close();
    }
}
