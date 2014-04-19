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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.Format;
import com.ibm.commons.xml.XMLException;

/**
 * Abstract XML service class with utlity functions.
 */
public abstract class AbstractDriver implements XMLParserDriver {
        
    public AbstractDriver() {
    }
    
    
    // =========================================================================================
    // Document creation
    // =========================================================================================

    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws XMLException {
        try {
            // PHIL:
            // It seems that xerces is complaining when a a document is created with a doctype
            // and when the namespace URI is null.
            // We overcome this by creating a dummy root element that is then removed.
            if(doctype!=null) {
                if(namespaceURI==null && qualifiedName==null) {
                    Document doc = getDOMImplementation().createDocument(null,"ns",doctype); // $NON-NLS-1$
                    doc.removeChild(doc.getDocumentElement());
                    return doc;
                }
            }
            // Some parsers do *not* support creating a document with both being null
            // We delegate to JAXP in this case
            if(namespaceURI==null && qualifiedName==null) {
            	return createDocument();
            }
            return getDOMImplementation().createDocument(namespaceURI,qualifiedName,doctype);
        } catch(Exception e) {
            throw new XMLException(e,"Error while creating new document"); // $NLS-AbstractDriver.Errorwhilecreatingnewdocument-1$
        }
    }
    protected Document createDocument() throws Exception {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	return builder.newDocument();
    }
    
    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws XMLException {
        try {
            return getDOMImplementation().createDocumentType(qualifiedName,publicId,systemId);
        } catch(Exception e) {
            throw new XMLException(e,"Error while parsing XML stream"); // $NLS-AbstractDriver.ErrorwhileparsingXMLstream-1$
        }
    }

    
    // =========================================================================================
    // Document parsing 
    // =========================================================================================
    
    protected DocumentBuilderFactory createDocumentBuilderFactory(boolean resolveEntities, boolean validate) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        dbFactory.setExpandEntityReferences(resolveEntities);
    	dbFactory.setValidating(validate);
        return dbFactory;
    }
    
    public Document parse(InputStream is, boolean resolveEntities, boolean ignoreBlanks, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }
            return doc;
        } catch(Exception e) {
            throw new XMLException(e,"Error while parsing XML stream"); // $NLS-AbstractDriver.ErrorwhileparsingXMLstream.1-1$
        }
    }

    public Document parse(Reader reader, boolean resolveEntities, boolean ignoreBlanks, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(reader));
            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }
            return doc;
        } catch(Exception e) {
            throw new XMLException(e,"Error while parsing XML stream"); // $NLS-AbstractDriver.ErrorwhileparsingXMLstream.2-1$
        }
    }

    public static void removeEmtyTextNodes(Node node) {
        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();
        for(int i=length-1; i>=0; i--) {
            Node child = nodeList.item(i);
            if(child.getNodeType()==Node.TEXT_NODE) {
                Text txt = (Text) child;
                String data = txt.getData();
                if(StringUtil.isSpace(data)) {
                    node.removeChild(child);
                }
            } else {
                removeEmtyTextNodes(child);
            }
        }
    }
    
    
    // =========================================================================================
    // Document serialization
    // =========================================================================================
    
    public void serialize(OutputStream os, Node node, Format format) throws XMLException {
        StreamResult result = new StreamResult(os);
        serialize(node,result,format);
    }
    
    public void serialize(Writer writer, Node node, Format format) throws XMLException {
        StreamResult result = new StreamResult(writer);
        serialize(node,result,format);
    }
    
    private void serialize(Node node, StreamResult result, Format format) throws XMLException {
        try {
            if(format==null) {
                format = Format.defaultFormat;
            }
            DOMSource domSource = new DOMSource(node);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,format.indent>0?"yes":"no"); // $NON-NLS-1$ $NON-NLS-2$
            transformer.setOutputProperty(OutputKeys.ENCODING,format.encoding);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,format.xmlDecl?"no":"yes"); // $NON-NLS-1$ $NON-NLS-2$
            transformer.transform(domSource, result);
        } catch(Exception e) {
            throw new XMLException(e,"Error while converting XML document to string"); // $NLS-AbstractDriver.ErrorwhileconvertingXMLdocumentto-1$
        }
    }
    
    
    // =========================================================================================
    // Document traversal
    // =========================================================================================

    public DocumentTraversal getDocumentTraversal(Document doc) {
    	// That should work for almost all the drivers...
        return (DocumentTraversal)doc;
    }    
}
