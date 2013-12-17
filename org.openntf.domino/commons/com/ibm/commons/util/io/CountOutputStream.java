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
 * Stream that counts a number of bytes emitted.
 * 
 * @ibm-api
 */
public class CountOutputStream extends OutputStream {

	private long count;
	
	/**
	 * @ibm-api
	 */
    public CountOutputStream() {
    }

    /** 
     * @ibm-api
     * @return the number of bytes added to the stream
     */
    public long getCount() {
    	return count;
    }
    
    public void write(int b) throws IOException {
    	count++;
    }

    public void write(byte[] b, int off, int len) throws IOException {
    	count += len;
    }
}
