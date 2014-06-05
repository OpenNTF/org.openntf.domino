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
/*
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 20 Apr 2009
* DOMImplementationImpl.java
*/

package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 * 20 Apr 2009
 * 
 * Unit: DOMImplementationImpl.java
 */
public class DOMImplementationImpl implements DOMImplementation {

    /* (non-Javadoc)
     * @see org.w3c.dom.DOMImplementation#createDocument(java.lang.String, java.lang.String, org.w3c.dom.DocumentType)
     */
    public Document createDocument(String arg0, String arg1, DocumentType arg2)
            throws DOMException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.w3c.dom.DOMImplementation#createDocumentType(java.lang.String, java.lang.String, java.lang.String)
     */
    public DocumentType createDocumentType(String arg0, String arg1, String arg2)
            throws DOMException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.w3c.dom.DOMImplementation#getFeature(java.lang.String, java.lang.String)
     */
    public Object getFeature(String arg0, String arg1) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.w3c.dom.DOMImplementation#hasFeature(java.lang.String, java.lang.String)
     */
    public boolean hasFeature(String arg0, String arg1) {
        return false;
    }

    /**
     * @return
     */
    public static DOMImplementation getDOMImplementation() {
        return null;
    }

}
