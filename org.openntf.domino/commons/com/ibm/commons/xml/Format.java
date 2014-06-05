/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.commons.xml;


/**
 * Output format parameters.
 */
public class Format {

    public static final Format defaultFormat = new Format(2,true,"UTF-8"); // $NON-NLS-1$
    public static final Format serializationFormat = new Format(0,true,"UTF-8"); // $NON-NLS-1$

    public final int      indent;
    public final boolean  xmlDecl;
    public final String   encoding;

    public Format(int indent, boolean xmlDecl, String encoding) {
        this.indent = indent;
        this.xmlDecl = xmlDecl;
        this.encoding = encoding;
    }
    
}
