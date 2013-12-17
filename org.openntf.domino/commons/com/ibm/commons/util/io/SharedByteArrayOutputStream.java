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

import java.io.ByteArrayOutputStream;


/**
 * The standard byte array makes a copy of its internal byte buffer when
 * <code>toByteArray()</code> is called. This class provides direct access to
 * the internal byte array.
 * 
 * @ibm-not-published
 *
 */
public class SharedByteArrayOutputStream extends ByteArrayOutputStream {

    /**
     * Constructor
     */
    public SharedByteArrayOutputStream() {
        super();
    }

    /**
     * Constructor
     * 
     * @param size - initial size in bytes
     */
    public SharedByteArrayOutputStream(int size) {
        super(size);
    }
    
    /**
     * Get the underlying byte array for stream.
     * 
     * Callers need to be <u>very</u> careful with this...
     * 
     * @return byte[]
     */
    public byte[] getByteArray() {
        return this.buf;
    }
}

