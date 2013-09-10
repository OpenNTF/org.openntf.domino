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

import java.io.IOException;

/**
 * Encodes and decodes to and from Base64 notation.
 * @ibm-api
 */
public class Base64 {

	/**
	 * Decode a base64 string to an ascii string.
	 * @param base64str the string to decode
	 * @return
	 * @ibm-api
	 */
    public static String decode(String base64str) {
    	try {
    		StringInputStream bais = new StringInputStream(base64str);
	        Base64.InputStream b64 = new Base64.InputStream(bais);
	        StringBuffer buf = new StringBuffer();
	        int byt;
	        while( (byt = b64.read()) >= 0)  {
	            buf.append((char) byt);
	        }
	        return buf.toString();
    	} catch(IOException ex) {
    		ex.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * Encode a string to base64. 
     * @param str the string to encode
     * @return
     * @ibm-api
     */
    public static String encode(String str) {
    	try {
    		StringBuidlerOutputStream baos = new StringBuidlerOutputStream(str.length()*3/2);
	        Base64.OutputStream b64 = new Base64.OutputStream(baos);

	        int len = str.length();
    		for( int i=0; i<len; i++) {
    			int c = ((int)str.charAt(i)) & 0x00FF;
    			b64.write(c);
    		}
	        b64.flushBuffer();

	        return baos.builder.toString();
    	} catch(IOException ex) {
    		ex.printStackTrace();
    		return null;
    	}
    }

    public static class StringInputStream extends java.io.InputStream {

    	private String str;
    	private int ptr;
    	
    	public StringInputStream(String str) {
    		this.str = str;
    	}

        public int read() throws IOException {
        	if(ptr<str.length()) {
        		return str.charAt(ptr++);
        	}
        	return -1;
        }
    }
    
    public static class StringBufferOutputStream extends java.io.OutputStream {
    	private StringBuffer buffer;
    	public StringBufferOutputStream(int size) {
    		this.buffer = new StringBuffer(size);
    	}
    	public StringBuffer getStringBuffer() {
    		return buffer;
    	}
        public void write(int b) throws IOException {
        	buffer.append((char)b);
        }
    }
    
    public static class StringBuidlerOutputStream extends java.io.OutputStream {
    	private StringBuilder builder;
    	public StringBuidlerOutputStream(int size) {
    		this.builder = new StringBuilder(size);
    	}
    	public StringBuilder getStringBuilder() {
    		return builder;
    	}
        public void write(int b) throws IOException {
        	builder.append((char)b);
        }
    }
    
	
    public static class OutputStream extends Base64OutputStream {
        public OutputStream( java.io.OutputStream out ) {
        	super(out);
        }
        public void flushBuffer() throws IOException {
        	flush();
        }
    }
	
    public static class InputStream extends Base64InputStream {
        public InputStream( java.io.InputStream in ) {
        	super(in);
        }
    }
}
