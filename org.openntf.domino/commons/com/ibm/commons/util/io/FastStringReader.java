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

/**
 * Fast buffered reader.
 * <p>
 * This implementation is faster than the standard BufferedReader
 * </p>
 * @ibm-api
 */
public class FastStringReader extends Reader {

	private String s;
	private int pos;
	private int length;
	
    /** @ibm-api */
    public FastStringReader(String s) {
    	this.s = s;
    	this.length = s.length();
    }
    
    /** @ibm-api */
    public int read() throws IOException {
    	if(pos<length) {
    		return s.charAt(pos++);
    	}
    	return -1;
    }
    
    /** @ibm-api */
    public int read(char cbuf[], int off, int len) throws IOException {
    	if(pos<length) {
    		int count = Math.min(len,length-pos);
    		s.getChars(pos, pos+count, cbuf, off);
    		pos += count;
    		return count;
    	}
    	return -1;
    }

    /** @ibm-api */
    public long skip(long n) throws IOException {
    	int skip = Math.min(pos+(int)n,length)-pos;
    	pos += skip;
    	return skip;
    }

    /** @ibm-api */
    public boolean ready() throws IOException {
    	return true;
    }
    
    /** @ibm-api */
    public void close() throws java.io.IOException {
    	// Nothing here...
    }
}
