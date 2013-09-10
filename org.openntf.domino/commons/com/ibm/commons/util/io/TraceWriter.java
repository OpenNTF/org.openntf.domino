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
import java.io.Writer;

/**
 * This class is simply a wrapper to an original stream that traces the characters written.
 * <p>
 * It is useful to display each byte that is written to a Writer without
 * consuming them.
 * </p>
 * @ibm-api
 */
public class TraceWriter extends Writer {

	/**
     * @ibm-api
	 */
    public TraceWriter(Writer writer, Writer trace) {
        this.writer=writer;
        this.trace=trace;
    }

	/**
     * @ibm-api
	 */
    public void close() throws java.io.IOException {
        writer.close();
        trace.flush();
        // Don't close the trace writer, as it may be still used outside
    }

	/**
     * @ibm-api
	 */
    public void write(char cbuf[], int off, int len) throws IOException {
        writer.write(cbuf,off,len);
        trace.write(cbuf,off,len);
        trace.flush();
    }

	/**
     * @ibm-api
	 */
    public void flush() throws java.io.IOException {
        writer.flush();
        trace.flush();
    }


    private Writer writer;
    private Writer trace;

}
