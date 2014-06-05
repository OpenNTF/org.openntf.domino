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
 * Created on June 14, 2005
 * 
 */
package com.ibm.commons.xml.xpath.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.util.XMIConverter;

/**
 * Some utilities for XML XPath evaluation
 * @author Philippe Riand
 */
public class Utils {

	/**
	 * Look for the best NamespaceContext to use.
	 * @param node the source node
	 * @param ns the namespace context passed from the caller
	 * @return
	 */
	public static NamespaceContext resolveNamespaceContext(Node node, NamespaceContext nsContext) {
		if (nsContext != null) {
			return nsContext;
		}
		if (node==null) {
			return null;
		}
		
		Document document = DOMUtil.getOwnerDocument(node);
		nsContext = DOMUtil.getSelectionNamespaces(document);
		if (nsContext != null) {
			return nsContext;
		}
	
		// create a namespace context based on the root element of the document
		nsContext = getNamespaceContext(document.getDocumentElement());
		return nsContext;
	}
	
    /**
     * Return the NamespaceContext based on this XML element.
     * @return
     */
    public static NamespaceContext getNamespaceContext(final Element docElement) {
    	return new NamespaceContext() {

			public String getNamespaceURI(String prefix) {
                if (docElement == null) {
                    return null;
                }
				return docElement.getAttribute(getNamespaceAttribute(prefix));
			}

			public String getPrefix(String namespaceURI) {
                if (docElement == null) {
                    return null;
                }
				NamedNodeMap attrs = docElement.getAttributes();
				for (int i=0; i<attrs.getLength(); i++) {
					Attr attr = (Attr)attrs.item(i);
					if (namespaceURI.equals(attr.getNodeValue())) {
						return attr.getLocalName();
					}
				}
				return null;
			}

			public Iterator getPrefixes(String namespaceURI) {
                if (docElement == null) {
                    return Collections.EMPTY_LIST.iterator();
                }
				List prefixes = new ArrayList();
				NamedNodeMap attrs = docElement.getAttributes();
				for (int i=0; i<attrs.getLength(); i++) {
					Attr attr = (Attr)attrs.item(i);
					if (namespaceURI.equals(attr.getNodeValue())) {
						prefixes.add(attr.getLocalName());
					}
				}
				return prefixes.iterator();
			}

			public Iterator getPrefixes() {
                if (docElement == null) {
                    return Collections.EMPTY_LIST.iterator();
                }
				List prefixes = new ArrayList();
				NamedNodeMap attrs = docElement.getAttributes();
				for (int i=0; i<attrs.getLength(); i++) {
					Attr attr = (Attr)attrs.item(i);
					if (isNamespaceAttribute(attr.getName())) {
						prefixes.add(attr.getLocalName());
					}
				}
				return prefixes.iterator();
			}
    		
    	};
    }
    
	static public String getNamespaceAttribute(String prefix) {
		if (StringUtil.isEmpty(prefix)) {
			return "xmlns"; //$NON-NLS-1$
		}
		return "xmlns:" + prefix; //$NON-NLS-1$
	}

	static public boolean isNamespaceAttribute(String name) {
		return name.startsWith("xmlns:"); //$NON-NLS-1$
	}
	
    static public String getAsString(Object value) {
    	if (value instanceof String) {
            return (String)value;
        }
        if (value instanceof Date) {
            return XMIConverter.toString((Date)value);
        }
        if (value instanceof Number) {
	        if (value instanceof BigDecimal) {
	            return XMIConverter.toString((BigDecimal)value);
	        }
	        if (value instanceof BigInteger) {
	            return XMIConverter.toString((BigInteger)value);
	        }
	        if (value instanceof Integer) {
	            return XMIConverter.toString(((Integer)value).intValue());
	        }
	        if (value instanceof Double) {
	            return XMIConverter.toString(((Double)value).doubleValue());
	        }
	        if (value instanceof Long) {
	            return XMIConverter.toString(((Long)value).longValue());
	        }
	        if (value instanceof Float) {
	            return XMIConverter.toString(((Float)value).floatValue());
	        }
	        if (value instanceof Short) {
	            return XMIConverter.toString(((Short)value).shortValue());
	        }
	        if (value instanceof Byte) {
	            return XMIConverter.toString(((Byte)value).byteValue());
	        }
        }
        if (value instanceof Character) {
            return XMIConverter.toString(((Character)value).charValue());
        }
		if (value instanceof Collection) {
			Collection collection = (Collection)value;
			StringBuffer buffer = new StringBuffer();
			for (Iterator i = collection.iterator(); i.hasNext(); ) {
				buffer.append(getAsString(i.next()));
				if (i.hasNext()) {
					buffer.append(" "); //$NON-NLS-1$
				}
			}
			return buffer.toString();
		}
		if (value instanceof Object[]) {
			Object[] array = (Object[])value;
			StringBuffer buffer = new StringBuffer();
			for (int i=0; i<array.length; i++) {
				buffer.append(getAsString(array[i]));
				if (i+1 < array.length) {
					buffer.append(" "); //$NON-NLS-1$
				}
			}
			return buffer.toString();
		}
        return (value == null) ? "" : value.toString(); //$NON-NLS-1$
    }

}
