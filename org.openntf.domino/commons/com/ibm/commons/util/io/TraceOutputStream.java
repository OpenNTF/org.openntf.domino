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
 * This class is simply a wrapper to an original stream that traces the bytes written.
 * <p>
 * It is useful to display each byte that is written to an OutputStream without
 * consuming them.
 * </p>
 * @ibm-api
 */
public class TraceOutputStream extends OutputStream {

	/**
     * @ibm-api
	 */
    public TraceOutputStream(OutputStream outputStream, OutputStream trace, boolean close) {
        this.outputStream=outputStream;
        this.trace=trace;
        this.close=close;
    }

	/**
     * @ibm-api
	 */
    public void close() throws java.io.IOException {
        outputStream.close();
        if( close ) {
            trace.close();
        } else {
            trace.flush();
        }
    }

	/**
     * @ibm-api
	 */
    public void flush() throws java.io.IOException {
        outputStream.flush();
    }

	/**
     * @ibm-api
	 */
    public void write(int b) throws IOException {
        outputStream.write(b);
        trace.write(b);
        //trace.flush();
    }

	/**
     * @ibm-api
	 */
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b,off,len);
        trace.write(b,off,len);
        //trace.flush();
    }

	/**
     * @ibm-api
	 */
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        trace.write(b);
        //trace.flush();
    }


    private OutputStream outputStream;
    private OutputStream trace;
    private boolean close;
}
