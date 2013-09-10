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
import java.io.OutputStream;

/**
 * This class is simply a wrapper to an original stream that traces the bytes read.
 * <p>
 * It is useful to display each byte that is read from an InputStream without
 * consuming them.
 * </p>
 * @ibm-api
 */
public class TraceInputStream extends InputStream {

	/**
     * @ibm-api
	 */
    public TraceInputStream(InputStream inputStream, OutputStream trace, boolean close) {
        this.inputStream=inputStream;
        this.trace=trace;
        this.close=close;
    }

	/**
     * @ibm-api
	 */
    public void close() throws java.io.IOException {
        inputStream.close();
        if( close ) {
            trace.close();
        } else {
            trace.flush();
        }
    }

	/**
     * @ibm-api
	 */
    public int read() throws IOException {
        int b = inputStream.read();
        if( b>=0 ) {
            trace.write(b);
            //trace.flush();
        }
        return b;
    }

	/**
     * @ibm-api
	 */
    public int read(byte b[], int off, int len) throws IOException {
        int r = inputStream.read( b, off, len );
        if( r>0 ) {
            trace.write(b,off,r);
            //trace.flush();
        }
        return r;
    }

    private InputStream inputStream;
    private OutputStream trace;
    private boolean close;
}
