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
import java.io.Reader;
import java.io.Writer;

/**
 * This class is simply a wrapper to an original stream that traces the characters read.
 * <p>
 * It is useful to display each character that is read from a Reader without
 * consuming them.
 * </p>
 * @ibm-api
 */
public class TraceReader extends Reader {

	/**
     * @ibm-api
	 */
    public TraceReader(Reader reader, Writer trace) {
        this.reader=reader;
        this.trace=trace;
    }

	/**
     * @ibm-api
	 */
    public void close() throws java.io.IOException {
        reader.close();
        trace.flush();
        // Don't close the trace writer, as it may be still used outside
    }

	/**
     * @ibm-api
	 */
    public int read() throws IOException {
        int c = reader.read();
        if( c>=0 ) {
            trace.write( (char)c );
        }
        return c;
    }

	/**
     * @ibm-api
	 */
    public int read(char cbuf[], int off, int len) throws IOException {
        int n=reader.read(cbuf,off,len);
        if (n>0) {
            trace.write(cbuf,off,n);    // it may read less than 'len' characters
        }
        return n;
    }

    private Reader reader;
    private Writer trace;
}
