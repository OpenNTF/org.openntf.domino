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
 * Created on May 29, 2005
 */
package com.ibm.commons.xml;

import java.util.Iterator;

/**
 * @author Mark Wallace
 * @fbscript
 */
public interface NamespaceContext {

    static final String XML_NS_PREFIX 			= "xml";		//$NON-NLS-1$
    static final String XMLNS_ATTRIBUTE 		= "xmlns";		//$NON-NLS-1$
    
    static final String XML_NS_URI 				= "http://www.w3.org/XML/1998/namespace"; 	//$NON-NLS-1$ 
    static final String XMLNS_ATTRIBUTE_NS_URI 	= "http://www.w3.org/2000/xmlns/"; 			//$NON-NLS-1$
    
    /**
     * Get the Namespace URI for the specified prefix in this namespace context.
     * @fbscript
     */
    public String getNamespaceURI(String prefix);
    
    /**
     * Get the prefix for the specified Namespace URI in this namespace context.
     * @fbscript
     */
    public String getPrefix(String namespaceURI);

    /**
     * Get all prefixes for the specified Namespace URI in this namespace context.
     * @fbscript
     */
    public Iterator getPrefixes(String namespaceURI);
    
    /**
     * Get all prefixes in this namespace context.
     * @fbscript
     */
    public Iterator getPrefixes();
    
}
