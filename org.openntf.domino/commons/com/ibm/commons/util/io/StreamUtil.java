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
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.ibm.commons.util.FastStringBuffer;

/**
 * Utilities for working with streams
 * @ibm-api
 */
public class StreamUtil {

    /**
     * Call close on an output stream and ignore any exception.
     * @param s the stream to call .close() on
     * @return OuputStream null
     * @ibm-api
     */
    public static OutputStream close(OutputStream s) {
        if (s!=null) {
            try {
                s.close();
            }
            catch (IOException ioe) {
                /** ignore */
            }
        }
        return null;
    }

    /**
     * Call close on an input stream and ignore any exception.
     * @param s the stream to call .close() on
     * @return InputStream null
     * @ibm-api
     */
    public static InputStream close(InputStream s) {
        if (s!=null) {
            try {
                s.close();
            }
            catch (IOException ioe) {
                /** ignore */
            }
        }
        return null;
    }

    /**
     * Call close on a Writer and ignore any exception.
     * @param s the Writer to call .close() on
     * @return Writer null
     * @ibm-api
     */
    public static Writer close(Writer w) {
        if (w!=null) {
            try {
                w.close();
            }
            catch (IOException ioe) {
                /** ignore */
            }
        }
        return null;
    }

    /**
     * Call close on a Reader and ignore any exception.
     * @param s the Reader to call .close() on
     * @return Reader null
     * @ibm-api
     */
    public static Reader close(Reader r) {
        if (r!=null) {
            try {
                r.close();
            }
            catch (IOException ioe) {
                /** ignore */
            }
        }
        return null;
    }

    /**
     * Copy contents of one stream onto another
     * @param is input stream
     * @param os output stream
     * @ibm-api
     */
    public static long copyStream(InputStream is, OutputStream os) throws IOException {
        return copyStream(is, os, 8192);
    }

    /**
     * Copy contents of one stream onto another
     * @param is input stream
     * @param os output stream
     * @param bufferSize size of buffer to use for copy
     * 
     * Note: there are cases where InputStream.available() returns > 0 but in
     * actual fact the stream won't be able to read anything, so we need to
     * handle the fact that InputStream.read() may return -1
     * @ibm-api
     */
    public static long copyStream(InputStream is, OutputStream os, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		long totalBytes = 0;
		int readBytes;
		while( (readBytes = is.read(buffer))>0 ) {
			os.write(buffer, 0, readBytes);
			totalBytes += readBytes;
		}
		return totalBytes;
    }

    /**
     * Copy contents of one stream onto another
     * @param is input stream
     * @param os output stream
     * @ibm-api
     */
    public static long copyStream(Reader is, Writer os) throws IOException {
        return copyStream(is, os, 8192);
    }
    
    /**
     * Copy contents of one character stream onto another
     * @param is input stream
     * @param os output stream
     * @param bufferSize size of buffer to use for copy
     * 
     * Note: there are cases where InputStream.available() returns > 0 but in
     * actual fact the stream won't be able to read anything, so we need to
     * handle the fact that InputStream.read() may return -1
     * @ibm-api
     */
    public static long copyStream(Reader is, Writer os, int bufferSize) throws IOException {
		char[] buffer = new char[bufferSize];
		long totalBytes = 0;
		int readBytes;
		while( (readBytes = is.read(buffer))>0 ) {
			os.write(buffer, 0, readBytes);
			totalBytes += readBytes;
		}
		return totalBytes;
    }
    
    /**
     * Read a UTF string as written by writeUTF.
     * This handles the null strings
     * @ibm-api
     */
    public static String readUTF(ObjectInput in) throws IOException {
    	boolean isnull = in.readBoolean();
    	return isnull ? null : in.readUTF();
    }
    
    /**
     * Writes a UTF string to be read by readUTF().
     * This handles the null strings
     * @ibm-api
     */
    public static void writeUTF(ObjectOutput out, String s) throws IOException {
    	if(s==null) {
    		out.writeBoolean(true);
    	} else {
    		out.writeBoolean(false);
    		out.writeUTF(s);
    	}
    }
    
    /**
     * Read a string from a reader.
     * @ibm-api
     */
	public static String readString(Reader reader) throws IOException {
		FastStringBuffer sb = new FastStringBuffer();
		sb.load(reader);
		return sb.toString();
	}
    
    /**
     * Read a string from an input stream using the default encoding.
     * @ibm-api
     */
	public static String readString(InputStream is) throws IOException {
		FastStringBuffer sb = new FastStringBuffer();
		sb.load(new InputStreamReader(is));
		return sb.toString();
	}
    
    /**
     * Read a string from an input stream using a specific encoding.
     * @ibm-api
     */
	public static String readString(InputStream is, String encoding) throws IOException {
		FastStringBuffer sb = new FastStringBuffer();
		sb.load(new InputStreamReader(is,encoding));
		return sb.toString();
	}

    /**
     * Fill a buffer as much as we can from InputStream.
     * @ibm-api
     */
	public static int fillBuffer(byte[] buffer, InputStream is) throws IOException {
		int pos=0; int r;
		do {
			r = is.read(buffer,pos,buffer.length-pos);
			if(r>0) { pos+=r; }
		} while(r>0 && r<buffer.length);
		return pos;
	}
	
}
