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

package com.ibm.commons.xml.drivers;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.ibm.commons.xml.Format;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XPathContext;



/**
 * Standard JAXP driver.
 */
public abstract class AbstractJAXPDriver extends AbstractDriver {
    
    private DOMImplementation domImplementation;
    private DOMImplementationLS domImplementationLS;
    
    public AbstractJAXPDriver() {
    	try {
    		DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
    		domImplementation = reg.getDOMImplementation("XML 3.0"); //$NON-NLS-1$
    	} catch(Throwable t) {
    		throw new RuntimeException(t);
    	}
    	if(domImplementation==null) {
    		throw new RuntimeException("Error while initializing the JAXP driver"); // $NLS-AbstractJAXPDriver.ErrorwhileinitializingtheJAXPdriv-1$
    	}
    	if(!(domImplementation instanceof DOMImplementationLS)) {
    		throw new RuntimeException("DOMImplementation does not implement Load/Save"); // $NLS-AbstractJAXPDriver.DOMImplementationdoesnotimplement-1$
    	}
    	domImplementationLS = (DOMImplementationLS)domImplementation;
    }
    
    public DOMImplementation getDOMImplementation() {
        return domImplementation;
    }
    
    protected DocumentBuilderFactory createDocumentBuilderFactory(boolean resolveEntities, boolean validate) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        dbFactory.setExpandEntityReferences(resolveEntities);
    	dbFactory.setValidating(validate);
        return dbFactory;
    }
    
    // =========================================================================================
    // NamespaceContext Access
    // =========================================================================================
    
    private static final String USERDATA_KEY = "com.ibm.xml.context"; //$NON-NLS-1$
    	
    private static class UserData implements Serializable {
		private static final long serialVersionUID = 1L;
		private transient NamespaceContext nsContext;
        private transient XPathContext xpContext;
    }
    
    private UserData getUserData(Document doc) {
        UserData data = (UserData)doc.getUserData(USERDATA_KEY);
        if(data==null) {
            data = new UserData();
            doc.setUserData(USERDATA_KEY,data,null); // No user handler
        }
        return data;
    }
    
    public XPathContext getXPathContext(Document doc) {
        UserData data = getUserData(doc);
        return data.xpContext;
    }
    
    public void pushXPathContext(Document doc, String xpath) throws XMLException {
        UserData data = getUserData(doc);
        data.xpContext = new XPathContext(doc,data.xpContext,xpath);
    }
    
    public void popXPathContext(Document doc) throws XMLException {
        UserData data = getUserData(doc);
        data.xpContext = data.xpContext.getParent();
    }

    public NamespaceContext getNamespaceContext(Document doc) {
        UserData data = getUserData(doc);
        return data.nsContext;
    }
    
    public void setNamespaceContext(Document doc, NamespaceContext ns) {
        UserData data = getUserData(doc);
        data.nsContext = ns;
    }
    
    public void serialize(OutputStream os, Node node, Format format) throws XMLException {
        try {
        	LSSerializer ser = createLSSerializer(format);
        	LSOutput out = domImplementationLS.createLSOutput();
        	out.setByteStream(os);
        	ser.write(node,out);
        } catch(Exception e) {
            throw new XMLException(e,"Error while converting XML document to string"); // $NLS-AbstractXercesDriver.ErrorwhileconvertingXMLdocumentto-1$
        }
    }

    public void serialize(Writer w, Node node, Format format) throws XMLException {
        try {
        	LSSerializer ser = createLSSerializer(format);
        	LSOutput out = domImplementationLS.createLSOutput();
        	out.setCharacterStream(w);
        	ser.write(node,out);
        } catch(Exception e) {
            throw new XMLException(e,"Error while converting XML document to string"); // $NLS-AbstractXercesDriver.ErrorwhileconvertingXMLdocumentto.1-1$
        }
    }

    private LSSerializer createLSSerializer(Format format) {
    	LSSerializer ser = domImplementationLS.createLSSerializer();
    	DOMConfiguration c = ser.getDomConfig();
    	c.setParameter("format-pretty-print", format.indent!=0); //$NON-NLS-1$
    	c.setParameter("xml-declaration", !format.xmlDecl); //$NON-NLS-1$
    	return ser;
    }
}
