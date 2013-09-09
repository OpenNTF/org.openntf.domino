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
 * Null OutputStream.
 * <p>
 * This output stream eats all the data.
 * </p>
 * @ibm-api
 */
public class NullOutputStream extends OutputStream {

    // An OutputStream that writes nothing

    public NullOutputStream() {
    }
    public void write(byte b[], int off, int len) throws IOException {
    }
    public void write(byte b[]) throws IOException {
    }
    public void write(int b) throws java.io.IOException {
    }
}
