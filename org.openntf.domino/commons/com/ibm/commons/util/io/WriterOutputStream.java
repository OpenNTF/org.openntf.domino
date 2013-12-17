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
import java.io.Writer;


/**
 * Converts from a Writer to an OutputStream.
 * @ibm-api
 */
public class WriterOutputStream extends OutputStream {

    private Writer writer;
    private String encoding;

	/**
     * @ibm-api
	 */
    public WriterOutputStream( Writer writer, String encoding ) {
        this.writer = writer;
        this.encoding = encoding;
    }

	/**
     * @ibm-api
	 */
    public WriterOutputStream( Writer writer ) {
        this( writer, null );
    }

	/**
     * @ibm-api
	 */
    public void write( int b ) throws IOException {
        writer.write( b );
    }

	/**
     * @ibm-api
	 */
    public void write( byte b[], int off, int len ) throws IOException {
        if( encoding!=null ) {
            writer.write( new String( b, off, len, encoding ) );
        } else {
            writer.write( new String( b, off, len ) );
        }
    }

	/**
     * @ibm-api
	 */
    public void write( byte b[] ) throws IOException {
        if( encoding!=null ) {
            writer.write( new String( b, encoding ) );
        } else {
            writer.write( new String( b ) );
        }
    }

	/**
     * @ibm-api
	 */
    public void close() throws IOException {
        writer.close();
    }

	/**
     * @ibm-api
	 */
    public void flush() throws IOException {
        writer.flush();
    }
}

