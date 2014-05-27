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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ibm.commons.util.EmptyIterator;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.FastBufferedOutputStream;
import com.ibm.commons.xml.drivers.AbstractJAXPDriver;
import com.ibm.commons.xml.drivers.JAXPDriverSun;
import com.ibm.commons.xml.drivers.JAXPDriverXerces;
import com.ibm.commons.xml.drivers.XMLParserDriver;
import com.ibm.commons.xml.drivers.XercesDriver;
import com.ibm.commons.xml.drivers.XercesSunDriver;
import com.ibm.commons.xml.util.XMIConverter;
import com.ibm.commons.xml.xpath.NodeListImpl;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.commons.xml.xpath.XPathExpressionFactory;
import com.ibm.commons.xml.xpath.xml.XmlXPathExpressionFactory;

/**
 * W3C DOM Utilities.<br/><br/>
 * 
 * A set of utility methods that allow clients to perform various DOM
 * manipulations.
 * 
 * @ibm-api
 */
public class DOMUtil {
    
    // ======================================================================
    // DOM common functions
    // ======================================================================
        
    private static XMLParserDriver s_parserDriver;
    private static XmlXPathExpressionFactory s_xpathFactory;
    
    static {
        //s_parserDriver = new XercesDriver();
        loadDriver();
        s_xpathFactory = new XmlXPathExpressionFactory(s_parserDriver);
    }
    private static void loadDriver() {
		DocumentBuilder builder = null;
    	try {
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		builder = factory.newDocumentBuilder();
    	} catch(Throwable ex) {
    	}
    	
    	// If Xerces if available in the path, using it
		if(builder.getClass().getName().startsWith("org.apache.xerces")) { //$NON-NLS-1$
			s_parserDriver = new XercesDriver();
			return; 
		}

    	// If the Sun version of Xerces is available, then use the full Sun Xerces driver
		if(builder.getClass().getName().startsWith("com.sun.org.apache.xerces")) { //$NON-NLS-1$
			s_parserDriver = new XercesSunDriver();
			return; 
		}

    	// Else use JAXP
    	// If the Xerces XPath API is available, then use it 
    	try {
    		Class.forName("org.apache.xpath.XPathAPI"); //$NON-NLS-1$
			s_parserDriver = new JAXPDriverXerces();
    	} catch(Throwable ex) {
    	}

    	// If the Sun Server XPath API is available, then use it
    	try {
    		Class.forName("com.sun.org.apache.xpath.internal.XPath"); //$NON-NLS-1$
			s_parserDriver = new JAXPDriverSun();
    	} catch(Throwable ex) {
    	}
    	
    	// Else forget about XPath evaluation
    	// Just use a JAXP driver
        s_parserDriver = new AbstractJAXPDriver() {
            public Object createXPath(String xpath) throws XPathException {
            	throw noXpathAvail();
            }
            public XResult evaluateXPath(Node node, Object xpath, NamespaceContext nsContext) throws XPathException {
            	throw noXpathAvail();
            }
            public XResult evaluateXPath(NodeList nodeList, Object xpath, NamespaceContext nsContext) throws XPathException {
            	throw noXpathAvail();
            }
        };
    }
    private static XPathException noXpathAvail() {
    	return new XPathException(null,"XPath engine is not available");//$NLS-DOMUtil.XPathengineisnotavailable-1$
    }
    
    /**
     * Get the internal XML parser driver used.
     * This object is not meant to be used directly. Use DOMUtil high level methods instead.
     * @return the XML parser driver
     */
    public static XMLParserDriver getParserDriver() {
        return s_parserDriver;
    }
    
    /**
     * Get the internal XPath factory used.
     * This object is not meant to be used directly. Use DOMUtil high level methods instead.
     * @return the XPath factory
     */
    public static XPathExpressionFactory getXPathExpressionFactory() {
        return s_xpathFactory;
    }
    
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws XMLException {
    	return s_parserDriver.createDocument(namespaceURI,qualifiedName,doctype);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(String namespaceURI, String qualifiedName) throws XMLException {
        return createDocument(namespaceURI,qualifiedName,null);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(DocumentType docType) throws XMLException {
        return createDocument(null,null,docType);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument() throws XMLException {
        return createDocument(null,null,null);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(InputStream is, boolean ignoreBlanks) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks,true,false);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(InputStream is, boolean ignoreBlanks, boolean resolveEntities) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks, resolveEntities, false);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(InputStream is, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks, resolveEntities, validate);
    }
        
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(InputStream is) throws XMLException {
        return createDocument(is,true);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(Reader is, boolean ignoreBlanks) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks,true,false);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(Reader is, boolean ignoreBlanks, boolean resolveEntities) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks,resolveEntities,false);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(Reader is, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        return s_parserDriver.parse(is,ignoreBlanks,resolveEntities,validate);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(Reader is) throws XMLException {
        return createDocument(is,true);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(String xmlText, boolean ignoreBlanks) throws XMLException {
    	if(StringUtil.isEmpty(xmlText)) {
    		return createDocument();
    	}
        return createDocument(new StringReader(xmlText),ignoreBlanks);
    }
    
    /**
     * Create a new W3C Document.
     * @return the newly created document
     */
    public static Document createDocument(String xmlText) throws XMLException {
        return createDocument(xmlText,true);
    }
    
    
    /**
     * Create a new W3C DocumentType.
     * @return the newly created document
     */
    public static DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws XMLException {
        return s_parserDriver.createDocumentType(qualifiedName, publicId, systemId);
    }

    
    /**
     * Returns the root element of a DOM.
     * This is for compatibility only.
     * @param document the source document
     * @return the DOM root element
     * @deprecated use document.getDocumentElement() instead
     */
    public static Element rootElement( Document document ) {
        return document.getDocumentElement();
    }

    /**
     * Returns whether  a DOM has a root.
     * @param document the source document
     * @return true if a root element exists, false otherwise
     */
    public static boolean hasRootElement( Document document ) {
        if(document!=null) {
            Element tmp = document.getDocumentElement();
            if ( tmp == null ) return false;
            return true;
        }
        return false;
    }
    
    /**
     * Check if an element has some children.
     * This is for compatibility only.
     * @param element the element to check
     * @return true if the element has at least one child
     * @deprecated use element.hasChildNode() instead
     */
    public static boolean hasChildren( Element element ) {
        if(element!=null) {
            return element.hasChildNodes();
        }
        return false;
    }

    
    // ======================================================================
    // Some useful DOM utilities
    // ======================================================================

    /**
     * Remove all the children from a node.
     * This method is surprisingly absent from the Node interface.
     * @param node the node to remove the children 
     */
    public static void removeChildren(Node node) {
        if(node!=null) {
            while(node.hasChildNodes()) {
                node.removeChild(node.getFirstChild());
            }
        }
    }

    /**
     * Remove the content of an existing document.
     * @param doc the document to clear
     * @return the empty 
     */
    public static Document emptyDocument(Document doc) {
        Element rootNode = doc.getDocumentElement();
        if(rootNode!=null) {
            doc.removeChild(rootNode);
        }
        return doc;
    }
    
    // ======================================================================
    // Text routines
    // ======================================================================
    
    /**
     * Get the text associated with a node.
     * If case of an Element, then it concatenate all the Text parts into one big.
     * @return the text associated to the node
     */
    public static String getTextValue(Node node) {
        if(node!=null) {
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                if(node.hasChildNodes()) {
                    NodeList l = node.getChildNodes();
                    int len = l.getLength(); 
                    if(len==1) {
                        Node child = l.item(0);
                        if( child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE ) {
                            return child.getNodeValue();
                        }
                        return null;
                    } else {
                        StringBuilder b = new StringBuilder(128);
                        for( int i=0; i<len; i++ ) {
                            Node child = l.item(i);
                            if( child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE ) {
                                String s = child.getNodeValue();
                                if(s!=null) {
                                    b.append(s);
                                }
                            }
                        }
                        return b.toString();
                    }
                }
            } 
            else if (node.getNodeType()==Node.TEXT_NODE || 
                    node.getNodeType()==Node.CDATA_SECTION_NODE ||
                    node.getNodeType()==Node.ATTRIBUTE_NODE) {
                return node.getNodeValue();
            }
        }
        return null;
    }

    /**
     * Set the text associated with a node.
     * If case of an Element, it finds the first occurence of text and change its content. If 
     * none is available, then it add a new one at the end of the content. For the other nodes, 
     * it then calls setNodeValue()
     * @return the text associated to the node
     */
    public static void setTextValue(Node node, String value) {
        if(node!=null) {
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        ((Text)child).setNodeValue(value);
                        return;
                    }
                    if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        ((Text)child).setNodeValue(value);
                        return;
                    }
                    child = child.getNextSibling();
                    if (child == null) {
                        break;
                    }
                }
                if(node.hasChildNodes()) {
                    removeChildren(node);
                }
                Text textNode = node.getOwnerDocument().createTextNode(value);
                node.appendChild(textNode);
            } else {
                node.setNodeValue(value);
            }
        }
    }
    public static void setTextValue(Node node, String value, boolean cdata) {
        if(node!=null) {
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE && !cdata) {
                        ((Text)child).setNodeValue(value);
                        return;
                    }
                    if (child.getNodeType() == Node.CDATA_SECTION_NODE && cdata) {
                        ((Text)child).setNodeValue(value);
                        return;
                    }
                }
                if(node.hasChildNodes()) {
                    removeChildren(node);
                }
                Node textNode = cdata ? node.getOwnerDocument().createCDATASection(value) : node.getOwnerDocument().createTextNode(value);
                node.appendChild(textNode);
            } else {
                node.setNodeValue(value);
            }
        }
    }

    
    // ======================================================================
    // Some Java iterator
    // ======================================================================
    
    /**
     * Get the list of the children Element.
     * Only the elements are extracted here.
     * @return an iterator on the children elements
     */
    public static Iterator getChildren(Node node) {
        if(node!=null) {
            return new NodeListIterator(node.getChildNodes(),NodeListIterator.ELEMENT_FILTER);
        }
        return EmptyIterator.getInstance();
    }
    
    
    
    // ======================================================================
    // XSL Tranformer
    // ======================================================================

//    /**
//     * Applies a XSL transformation (defined in .xsp file, which name is 'xslName') to
//     * the given source document.
//     * The result of XSL transformation is returned.
//     * @param the source document
//     * @param xslName the XSL name to apply
//     * @return the result document
//     * @throws XMLException if an error occurred
//     */
//    public static org.jdom.Document xslTransform( org.jdom.Document source, String xslName ) throws XMLException {
//        return JDOM.xslTransform(source,xslName);
//    }
//
//    // ======================================================================
//    // XML load/save
//    // ======================================================================
//
//    /**
//     * Reads a JDOM object from a given XML resource and write it into the given source document.
//     * A resource is a file within the application directory (ex: c:\FlowBuilder20), and is relative
//     * to that directory. This works when running in an expanded directory as well as when running
//     * from a .war file, even non expanded.<br>
//     * Note that the file separator is a slash '/',regardless the platform where it is running.
//     * @param source the document to fill with the data
//     * @param resourceName the resource name to load
//     * @throws XMLException if an error occurred
//     */
//    public static void loadResource( org.jdom.Document source, String resourceName ) throws XMLException {
//        try {
//            com.ibm.workplace.designer.Application.get().getVFS().getFile(resourceName).readXML(new ExistingDocumentFactory(source));
//        } catch( VFSException e ) {
//            throw new XMLException( e, "Error while loading XML resource '{0}'", resourceName ); //$NLS-DOMUtil.JDom.Loading.Exception-1$
//        }
//    }
//
//    /**
//     * Writes the given JDOM document into a file.
//     * A resource is a file within the application directory (ex: c:\FlowBuilder20), and is relative
//     * to that directory. This works when running in an expanded directory as well as when running
//     * from a .war file, even non expanded. In case of a war file, the value is written to a temporary
//     * directory. It is lost when the application is redeployed!<br>
//     * Note that the file separator is a slash '/',regardless the platform where it is running.
//     * @param source the document to save
//     * @param resourceName the resource name to write
//     * @throws XMLException if an error occurred
//     */
//    public static void saveResource( org.jdom.Document source, String resourceName ) throws XMLException {
//        try {
//            com.ibm.workplace.designer.Application.get().getVFS().getFile(resourceName).writeXML(source,false);
//        } catch( VFSException e ) {
//            throw new XMLException( e, "Error while saving XML resource '{0}'", resourceName ); //$NLS-DOMUtil.JDom.Saving.Exception-1$
//        }
//    }
//
//    /**
//     * Writes the given JDOM document in a file , with a compact format (no indent, no
//     * empty lines) if the <code>compact</code> parameter is true.
//     * A resource is a file within the application directory (ex: c:\FlowBuilder20), and is relative
//     * to that directory. This works when running in an expanded directory as well as when running
//     * from a .war file, even non expanded. In case of a war file, the value is written to a temporary
//     * directory. It is lost when the application is redeployed!<br>
//     * Note that the file separator is a slash '/',regardless the platform where it is running.
//     * @param source the document to save
//     * @param resourceName the resource name to write
//     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
//     * @throws XMLException if an error occurred
//     */
//    public static void saveResource( org.jdom.Document source, String resourceName, boolean compact ) throws XMLException {
//        try {
//            com.ibm.workplace.designer.Application.get().getVFS().getFile(resourceName).writeXML(source,compact);
//        } catch( VFSException e ) {
//            throw new XMLException( e, "Error while saving XML resource '{0}'", resourceName ); //$NLS-DOMUtil.JDom.Saving.Exception-1$
//        }
//    }
//
//    /**
//     * Reads a JDOM object from a given XML file, and write it into the given source Document.
//     * The file name is using the underlying OS conventions. Note that, for accessing a file
//     * located in your FlowBuilder directory, it is better to use <code>loadResource()</code>,
//     * which also works when the application is deployed as a war file.
//     * @param source the document to fill with the data
//     * @param fileName the file name to load
//     * @throws XMLException if an error occurred
//     */
//    public static void loadFile( org.jdom.Document source, String fileName ) throws XMLException {
//        Document d = JDOM.readFileOrURL(fileName,new ExistingDocumentFactory(source));
//    }
//
//    /**
//     * Writes the given document into the given file.
//     * The file name is using the underlying OS conventions. Note that, for accessing a file
//     * located in your FlowBuilder directory, it is better to use <code>saveResource()</code>,
//     * which also works when the application is deployed as a war file.
//     * @param source the document to save
//     * @param fileName the file name to load
//     * @throws XMLException if an error occurred
//     */
//    public static void saveFile( org.jdom.Document source, String fileName ) throws XMLException {
//        JDOM.save(new File(fileName),source,false);
//    }
//
//    /**
//     * Writes the given document into the given file, with a compact format (no indent, no
//     * empty lines) if the <code>compact</code> parameter is true.
//     * Writes the given document into the given file.
//     * The file name is using the underlying OS conventions. Note that, for accessing a file
//     * located in your FlowBuilder directory, it is better to use <code>saveResource()</code>,
//     * which also works when the application is deployed as a war file.
//     * @param source the document to save
//     * @param fileName the file name to load
//     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
//     * @throws XMLException if an error occurred
//     */
//    public static void saveFile( org.jdom.Document source, String fileName, boolean compact ) throws XMLException {
//        JDOM.save(new File(fileName),source,compact);
//    }
//
//

    // ======================================================================
    // Serialization
    // ======================================================================
//jtw-add
    /**
     * Serialize a node to a file.
     * @param file the java.io.File
     * @param node the source node
     * @param format the desired output format
     */
    public static void serialize( File file, Node node, Format format) throws XMLException {
        try {
            OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
            try {
                serialize(os, node, format);
            } finally {
                os.close();
            }
        } catch(IOException e) {
//TODO - after resource bundle clarification            throw new XMLException(e, "Error while saving XML to file '{0}'", file.getPath()); //$NLS-DOMUtil.JDOMUtil.XmlSavingToFile.Exception-1$
            throw new XMLException(e,"JDOMUtil.XmlSavingToFile.Exception"+file.getPath()); // $NON-NLS-1$
        }
    }
    /**
     * Serialize a node to a stream.
     * @param os the output stream
     * @param node the source node
     * @param format the desired output format
     */
    public static void serialize( OutputStream os, Node node, Format format ) throws XMLException {
            s_parserDriver.serialize(os,node,format);
    }

    
    /**
     * Serialize a node to a stream.
     * The serialization is done using UTF-8 encoding.
     * @param os the output stream
     * @param node the source node
     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
     * @param xmldecl if true, include the XML declaration statement
     */
    public static void serialize( OutputStream os, Node node, boolean compact, boolean xmldecl ) throws XMLException {
        Format fmt = new Format(
                compact ? 0 : 2,
                xmldecl,
                "UTF-8" // $NON-NLS-1$
        );
        serialize(os,node,fmt);
    }

    /**
     * Serialize a node to a writer.
     * @param w the writer
     * @param node the source node
     * @param format the desired output format
     */
    public static void serialize( Writer w, Node node, Format format ) throws XMLException {
        s_parserDriver.serialize(w,node,format);
    }

    /**
     * Serialize a node to a writer.
     * The serialization is done using UTF-8 encoding.
     * @param w the writer
     * @param node the source node
     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
     * @param xmldecl if true, include the XML declaration statement
     */
    public static void serialize( Writer w, Node node, boolean compact, boolean xmldecl ) throws XMLException {
        Format fmt = new Format(
                compact ? 0 : 2,
                xmldecl,
                "UTF-8" // $NON-NLS-1$
        );
        s_parserDriver.serialize(w,node,fmt);
    }
    
    
    // ======================================================================
    // String conversion
    // ======================================================================

    /**
     * Method to convert a DOM document into a string.
     * <p> If <code>xmlDecl</code> is false, the resulting string
     * won't contain an XML declaration. (<code>&lt;?xml version="1.0"?&gt;</code>)
     * <p> If <code>compact</code> is true, the resulting string will be compacted
     * (no indent, no empty lines).
     * @param node the source XML node
     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
     * @param xmldecl if true, include the XML declaration statement
     * @return the document as a string
     * @throws XMLException if an error occurred
     */
    public static String getXMLString(Node node, Format format) throws XMLException {
        StringWriter w = new StringWriter();
        serialize(w,node,format);
        return w.toString();
    }

    /**
     * Method to convert a DOM document into a string.
     * <p> If <code>xmlDecl</code> is false, the resulting string
     * won't contain an XML declaration. (<code>&lt;?xml version="1.0"?&gt;</code>)
     * <p> If <code>compact</code> is true, the resulting string will be compacted
     * (no indent, no empty lines).
     * @param node the source XML node
     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
     * @param xmldecl if true, include the XML declaration statement
     * @return the document as a string
     * @throws XMLException if an error occurred
     */
    public static String getXMLString(Node node, boolean compact, boolean xmldecl) throws XMLException {
        StringWriter w = new StringWriter();
        serialize(w,node,compact,xmldecl);
        return w.toString();
    }

    /**
     * Method to convert a DOM document into a string with no XML declaration line.
     * <p> If <code>compact</code> is true, the resulting string will be compacted
     * (no indent, no empty lines).
     * @param node the source XML node
     * @param compact if true, the document does'nt contains any formatting text (blanks, new lines..)
     * @return the document as a string
     * @throws XMLException if an error occurred
     */
    public static String getXMLString(Node node, boolean compact) throws XMLException {
        return getXMLString(node,compact,false);
    }

    /**
     * Method to convert a DOM document into a string with no XML declaration line.
     * The resulting string is not compacted (indent, blank lines)
     * @param node the source XML node
     * @return the document as a string
     * @throws XMLException if an error occurred
     */
    public static String getXMLString(Node node) throws XMLException {
        return getXMLString(node,false,false);
    }

    /**
     * Reads the given string to update the document content.
     * The string is parsed to an XML DOM, which is then assigned to the document
     * @param doc the XML node to update
     * @param s the XML string to parse
     * @throws XMLException if an error occurred
     */
    public static void setXMLString(Document target, String s) throws XMLException {
        Document source = createDocument(s);
        setDocument(source, target);
    }

    /**
     * Clear all of the content from the specified document
     * @param doc
     */
    public static void clearDocument(Document doc) {
        if (doc == null) {
            return;
        }
        while (doc.hasChildNodes()) {
            doc.removeChild(doc.getFirstChild());
        }
    }

    /**
     * Set the content of the target document from the sourec document.
     * @param source
     * @param target
     */
    public static void setDocument(Document source, Document target) {
        if (source == null || target == null) {
            return;
        }
        clearDocument(target);
        Node node = target.importNode(source.getDocumentElement(), true);
        target.appendChild(node);
    }

    // ======================================================================
    // XPath access
    // ======================================================================
        
    /**
     * Push an XPath context to an existing document.
     * An XML document contains a base XPath that is used when evaluating XPath against a
     * node. This base XPath is composed by the aggregating all the XPath contexts
     * pushed to the document.
     * @param doc the source document
     * @param xPathContext the XPath context to push to the document
     */
    public static void pushXPathContext(Document doc, String xPathContext) throws XMLException {
        s_parserDriver.pushXPathContext(doc, xPathContext);
    }

    /**
     * Remove the latest XPath context that has been set to the document
     * An XML document contains a base XPath that is used when evaluating XPath against a
     * node. This base XPath is composed by the aggregating all the XPath contexts
     * pushed to the document.
     * @param doc the source document
     */
    public static void popXPathContext(Document doc) throws XMLException {
        s_parserDriver.popXPathContext(doc);
    }    

    /**
     * Get the current XPath context result that is associated to a document.
     * The result is either a Node or a NodeList.
     * @param doc the source document
     */
    public static Object getContextNodes(Document doc) {
        XPathContext ctx = s_parserDriver.getXPathContext(doc);
        return ctx!=null ? ctx.getContextNodes() : null;
    }
    
    /**
     * Get the current XPath contextthat is associated to a document.
     * @param doc the source document
     */
    public static XPathContext getXPathContext(Document doc) {
        return s_parserDriver.getXPathContext(doc);
    }

    /**
     * Set the filter index on the current XPath context that is associated with the 
     * specified document.
     * @param doc
     * @param index
     * @throws XMLException
     */
    public static void setFilterIndex(Document doc, int index) throws XMLException {
        XPathContext ctx = s_parserDriver.getXPathContext(doc);
        if(ctx==null) {
            throw new XMLException(null,"No current context defined for the document"); // $NLS-DOMUtil.Nocurrentcontextdefinedforthedocu-1$
        }
        ctx.setFilterIndex(index);
    }
    
    /**
     * Set the namespaces map use during the XPath evaluation on the document.
     * @param doc the source document
     * @param selectionNS the NamespaceContext used during XPath evaluation on the document,
     * in order to resolve namespaces.
     */
    public static void setSelectionNamespaces(Document doc, NamespaceContext selectionNS) {
        // Delegate to the driver
        s_parserDriver.setNamespaceContext(doc,selectionNS);
    }

    /**
     * Get the selection NamespaceContext use during the XPath evaluation on the document.
     * @param doc the source document
     */
    public static NamespaceContext getSelectionNamespaces(Document doc) {
        // Delegate to the driver
        return s_parserDriver.getNamespaceContext(doc);
    }

    /**
     * Debugging function for NamespaceContext.
     * @param namespaceContext the namespace context to dump.
     * @return a string representing the namespace context
     */
   public static String getNamespaceContextAsString(NamespaceContext namespaceContext) {
       StringBuffer b = new StringBuffer();
       boolean first = true;
       for( Iterator it=namespaceContext.getPrefixes(); it.hasNext(); ) {
           String s = it.next().toString();
           if(!first) {
               b.append("\n"); // $NON-NLS-1$
           } else {
               first = false;
           }
           b.append(s);
           b.append("=");
           b.append(namespaceContext.getNamespaceURI(s));
       }
       return b.toString();
   }
   
    /**
     * Create an XPath expression object.
     * This object can be safetly reused for subsequent evaluations.
     * WARN: This object does not take care of the Document XPath context. To take 
     * advantage of this context, use one of the string based functions.
     * @param xpathExpr the xpath expression
     * @param useCache indicate if the xpath should be cached
     * @return the XPath expression
     * @throws XMLException
     */
    public static XPathExpression createXPath(String xpathExpr, boolean useCache) throws XMLException {
        return s_xpathFactory.createExpression(null, xpathExpr, useCache);
    }

    /**
     * Create an XPath expression object.
     * This object can be safetly reused for subsequent evaluations.
     * WARN: This object does not take care of the Document XPath context. To take 
     * advantage of this context, use one of the string based functions.
     * @param xpathExpr the xpath expression
     * @return the XPath expression
     * @throws XMLException
     */
    public static XPathExpression createXPath(String xpathExpr) throws XMLException {
        return s_xpathFactory.createExpression(null, xpathExpr, true);
    }

    /**
     * Evaluate an XPath expression.
     * This object can be safetly reused for subsequent evaluations.
     * @param node the node source of the XPath
     * @param xpathExpr the xpath expression
     * @param nsContext the namespace context to use by the engine
     * @param useCache indicate if the xpath should be cached
     * @return the result of the XPath evaluation
     * @throws XMLException
     */
    public static XResult evaluateXPath(Node node, String xpathExpr, NamespaceContext nsContext, boolean useCache) throws XMLException {
        XPathExpression expr = createXPath(xpathExpr,useCache);
        return expr.eval(node,nsContext);
    }

    /**
     * Evaluate an XPath expression.
     * This object can be safetly reused for subsequent evaluations.
     * @param node the node source of the XPath
     * @param xpathExpr the xpath expression
     * @param nsContext the namespace context to use by the engine
     * @return the result of the XPath evaluation
     * @throws XMLException
     */
    public static XResult evaluateXPath(Node node, String xpathExpr, NamespaceContext nsContext) throws XMLException {
        return evaluateXPath(node, xpathExpr, nsContext, true);
    }

    /**
     * Evaluate an XPath expression.
     * This object can be safetly reused for subsequent evaluations.
     * @param node the node source of the XPath
     * @param xpathExpr the xpath expression
     * @return the result of the XPath evaluation
     * @throws XMLException
     */
    public static XResult evaluateXPath(Node node, String xpathExpr) throws XMLException {
        return evaluateXPath(node, xpathExpr, null, true);
    }

    /**
     * Evaluate an XPath expression.
     * This object can be safetly reused for subsequent evaluations.
     * @param node the node source of the XPath
     * @param xpathExpr the xpath expression
     * @param useCache indicate if the xpath should be cached
     * @return the result of the XPath evaluation
     * @throws XMLException
     */
    public static XResult evaluateXPath(Node node, String xpathExpr, boolean useCache) throws XMLException {
        return evaluateXPath(node, xpathExpr, null, useCache);
    }


    /**
     * Create a node based on an XPath expression object.
     * @param node the node to start from
     * @param xpathExpr the xpath expression
     * @param useCache indicate if the xpath should be cached
     * @return the XPath expression
     * @throws XMLException
     */
    public static Object createNodes(Node node, String xpathExpr, boolean useCache) throws XMLException {
        XPathExpression xp = createXPath(xpathExpr);
        // If we start from the document, we should take care of the XPath context
        if(!xp.isFromRoot() && (node instanceof Document)) {
            Document doc = (Document)node;
            XPathContext ctx = s_parserDriver.getXPathContext(doc);
            if(ctx!=null) {
                // Ensure that the intermediate context are created
                ctx.createNodes();
                // And evaluate from this context
                return xp.createNodes(ctx.getUniqueContextNode(),null);
            }
        }
        // Else, start from the current node
        return xp.createNodes(node,null);
    }

    /**
     * Create a node based on an XPath expression object.
     * @param node the node to start from
     * @param xpathExpr the xpath expression
     * @param useCache indicate if the xpath should be cached
     * @return the XPath expression
     * @throws XMLException
     */
    public static Object createNodes(Node node, String xpathExpr) throws XMLException {
        return createNodes(node,xpathExpr,true);
    }

    /**
     * Create an element with the specified name and add it as a child of the specified parent.
     * @param document
     * @param parent
     * @param name
     * @return
     */
    public static Element createElement(Document document, Element parent, String name) {
        Element element = document.createElement(name);
        parent.appendChild(element);
        return element;
    }

    /**
     * Create an element with the specified name and add it as a child of the specified parent.
     * @param document
     * @param name
     * @return
     */
    public static Element createElement(Document document, String name) {
        Element element = document.createElement(name);
        document.appendChild(element);
        return element;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    // Value extraction
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Evaluate the given XPath expression on the given document, and returns an array with
     * all elements and attributes that match this XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the array of XML nodes that match the XPath
     * @throws XMLException if an error occurred
     */
    public static Object[] nodes(Node node, String xpath) throws XMLException {
        XResult r = evaluateXPath(node,xpath);
        return r.getNodes();
    }

    /**
     * Evaluate the given XPath expression on the given document, and returns the
     * first element or attribute that match this XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the resulting object
     * @throws XMLException if an error occurred
     */
    public static Object node(Node node, String xpath) throws XMLException {
        XResult r = evaluateXPath(node,xpath);
        return r.getSingleNode();
    }

    /**
     * Evaluate the given XPath expression on the given document, and returns an array with
     * all elements and attributes that match this XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the array of XML nodes that match the XPath
     * @throws XMLException if an error occurred
     */
    public static Object[] nodes(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
        XResult r = evaluateXPath(node,xpath,selectionNS);
        return r.getNodes();
    }

    /**
     * Evaluate the given XPath expression on the given document, and returns the
     * first element or attribute that match this XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the resulting object
     * @throws XMLException if an error occurred
     */
    public static Object node(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
        XResult r = evaluateXPath(node,xpath,selectionNS);
        return r.getSingleNode();
    }


    ///////////////////////////////////////////////////////////////////////////
    // Value extraction
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get a string value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a string
     * @throws XMLException if an error occurred
     */
    public static String value(Node node, String xpath) throws XMLException {
        XResult r = evaluateXPath(node,xpath);
        return r.getStringValue();
    }

    /**
     * Get a string value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a string
     * @throws XMLException if an error occurred
     */
    public static String value(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
        XResult r = evaluateXPath(node,xpath,selectionNS);
        return r.getStringValue();
    }

    /**
     * Get the string values from the XPath matching elements.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the values as a string array
     * @throws XMLException if an error occurred
     */
    public static String[] values(Node node, String xpath) throws XMLException {
        XResult r = evaluateXPath(node,xpath);
        return r.getValues();
    }
    
    /**
     * Get the string values from the XPath matching elements.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the values as a string array
     * @throws XMLException if an error occurred
     */
    public static String[] values(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
        XResult r = evaluateXPath(node,xpath,selectionNS);
        return r.getValues();
    }


    /**
     * Set the value of the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param path the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setValue(Node node, String path, String value) throws XMLException {
        setValue(node,path,value,null);
    }
    
    /**
     * Set the value of the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param path the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setValue(Node node, String path, String value, NamespaceContext selectionNS) throws XMLException {
        XPathExpression xp = createXPath(path);
        // If we start from the document, we should take care of the XPath context
        if(!xp.isFromRoot() && (node instanceof Document)) {
            Document doc = (Document)node;
            XPathContext ctx = s_parserDriver.getXPathContext(doc);
            if(ctx!=null) {
                // Ensure that the intermediate context are created
                ctx.createNodes();
                // And evaluate from this context
                xp.setValue(ctx.getUniqueContextNode(),value,selectionNS,true);
                return;
            }
        }
        // Else, start from the current node
        xp.setValue(node,value,selectionNS,true);
    }

    
    // ======================================================================
    // Element accessor
    // ======================================================================

    /**
     * Find a element by name.
     * @return the element found, or null if it is not available
     */
    public static Element getFirstElementByName( Element parent, String name ){
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String localName = e.getLocalName();
    	        if (localName == null) {
    	            if( name.equals(e.getNodeName()) ) {
    	            	return e;
    	            }
    	        }
    		}
    	}
    	return null;
    }

    /**
     * Find a element by name.
     * @return the element found, or null if it is not available
     */
    public static Element getFirstElementByNameNS( Element parent, String namespaceURI, String name ){
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String localName = e.getLocalName();
    	        if (localName == null) {
    	            if( name.equals(e.getNodeName()) ) {
    	            	return e;
    	            }
    	        }
    	        if (namespaceURI!=null && name.equals(localName)) {
    	            String elemNsURI = e.getNamespaceURI();
    	            if (namespaceURI.equals(elemNsURI)) {
    	            	return e;
    	            }
    	        }
    		}
    	}
    	return null;
    }
    
    /**
     * Find a element by name.
     * @return the element found, or null if it is not available
     */
    public static NodeList getElementsByName( Element parent, String name ){
    	NodeListImpl result = new NodeListImpl(); 
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String localName = e.getLocalName();
    	        if (localName == null) {
    	            if( name.equals(e.getNodeName()) ) {
    	            	result.add(e);
    	            }
    	        }
    		}
    	}
    	return result;
    }

    /**
     * Find a element by name.
     * @return the element found, or null if it is not available
     */
    public static NodeList getElementsByNameNS( Element parent, String namespaceURI, String name ){
    	NodeListImpl result = new NodeListImpl(); 
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String localName = e.getLocalName();
    	        if (localName == null) {
    	            if( name.equals(e.getNodeName()) ) {
    	            	result.add(e);
    	            }
    	        }
    	        if (namespaceURI!=null && name.equals(localName)) {
    	            String elemNsURI = e.getNamespaceURI();
    	            if (namespaceURI.equals(elemNsURI)) {
    	            	result.add(e);
    	            }
    	        }
    		}
    	}
    	return result;
    }

    /**
     * Inserts the node newChild after the existing child node refChild.
	 * Inserts the node newChild after the existing child node refChild. If refChild is null, 
	 * insert newChild at the end of the list of children.
	 * If newChild is a DocumentFragment object, all of its children are inserted, 
	 * in the same order, after refChild. If the newChild is already in the tree, 
	 * it is first removed.
	 * @return The node being inserted     
     */
    public static Node insertAfter(Node parent, Node newChild, Node refChild) {
    	if(refChild!=null) {
    		Node next = refChild.getNextSibling();
    		return parent.insertBefore(newChild, next);
    	}
		return parent.insertBefore(newChild, refChild);
    }

    /**
     * Move an element prior to his previous sibling.
     * If the element doesn't have a previous sibling, then nothing is moved
     * @param node the node to move up
     * @return true if the element was moved
     */
    public static boolean moveNodeUp(Node node) {
    	Node parent = node.getParentNode();
    	Node previous = node.getPreviousSibling();
    	if(previous!=null) {
    		parent.removeChild(node);
    		parent.insertBefore(node, previous);
    		return true;
    	}
    	return false;
    }

    /**
     * Move an element prior to his previous sibling.
     * If the element doesn't have a previous sibling, then nothing is moved
     * @param node the node to move down
     * @return true if the element was moved
     */
    public static boolean moveNodeDown(Node node) {
    	Node parent = node.getParentNode();
    	Node next = node.getNextSibling();
    	if(next!=null) {
    		parent.removeChild(node);
    		insertAfter(parent, node, next);
    		return true;
    	}
    	return false;
    }
    
    // ======================================================================
    // Attributes accessors
    // ======================================================================

    /**
     * Determines if an Attribute is a namespace attribute.
     */
    public static boolean isNamespaceAttribute( Attr attribute ){
        boolean rtnVal = false;
        String prefix = attribute.getPrefix();
        if ( StringUtil.equals( prefix, "xmlns") == true) { // $NON-NLS-1$
            rtnVal = true;
        }
        return rtnVal;
    }

    /**
     * Gets the DOM attribute of <code>element</code> with the given name.
     * It returns the first attribute that matches the name, regardless the case.
     * This is particularly useful when dealing with an HTML DOM, as HTML is not
     * case sensitive.
     * @param element the source element
     * @param attributeName the attribute name
     * @return the XML attribute if any, null otherwise
     */
    public static Attr getAttributeIgnoreCase(Element element, String attributeName) {
        NamedNodeMap nm = element.getAttributes();
        for(int i=0; i<nm.getLength(); i++ ) {
            Attr a = (Attr)nm.item(i);
            if(a.getName().equalsIgnoreCase(attributeName)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Removes the DOM attribute from <code>element</code> with the given name.
     * It removes the first attribute that matches the name, regardless the case.
     * This is particularly useful when dealing with an HTML DOM, as HTML is not
     * case sensitive.
     * @param element the source element
     * @param attributeName the attribute name
     */
    public static void removeAttributeIgnoreCase(Element element, String attributeName) {
        Attr a = getAttributeIgnoreCase(element,attributeName);
        if(a!=null) {
            element.removeAttributeNode(a);
        }
    }

    /**
     * Gets the value of DOM attribute of <code>element</code> with the given name
     * It returns the first attribute value that matches the name, regardless the case.
     * This is particularly useful when dealing with an HTML DOM, as HTML is not
     * case sensitive.
     * @param element the source element
     * @param attributeName the attribute name
     * @return the XML attribute if any, null otherwise
     */
    public static String getAttributeValueIgnoreCase(Element element, String attributeName) {
        Attr a = getAttributeIgnoreCase(element,attributeName);
        if(a!=null) {
            a.getValue();
        }
        return null;
    }

    /**
     * Return the owner document for the specified node.
     * 
     * @param node
     * @return
     */
    public static Document getOwnerDocument(Node node) {
        if (node instanceof Document) {
            return (Document)node;
        }
        return node.getOwnerDocument();
    }

    /**
     * Traverse a node, and parent nodes, for a namespace URI. If the namespace
     * URI is located, the corresponding name is returned. If the namespace
     * uri is not located (i.e., does not exist), it is created as an attribute
     * node of the document root node.
     * @param parentNode - node from which to begin the search
     * @param preferredPrefix - Prefix to use if the namespace attribute must be created. 
     * @param nsUri - the namesapce URI to search for
     * @return the located/created namespace prefix
     */
    public static String getNamespacePrefix(Node parentNode, String preferredPrefix, String nsUri) {
        //traverse the list of namespaces
        Node currentNode = parentNode;
        do{
            NamedNodeMap nodeMap = currentNode.getAttributes();
            for ( int i = 0 ; nodeMap != null && i < nodeMap.getLength(); i++ ){
                Node item = nodeMap.item( i );
                String itemName = item.getNodeName();
                if ( itemName.startsWith( "xmlns" ) ){ // $NON-NLS-1$
                    String uri = getTextValue( item );
                    if ( nsUri.equals( uri ) ){
                        return itemName.substring( itemName.indexOf(":") + 1);
                    }
                }
            }
        }while( (currentNode = currentNode.getParentNode()) != null );
        
        // If the namespace does not exist, then create it
        Element root = parentNode.getOwnerDocument().getDocumentElement();
        if(root!=null) {
            for( int i=1; ; i++) {
                String p = preferredPrefix;
                if( i>1 ) {
                    p += i;
                }
                if( root.getAttributeNode("xmlns:"+p)==null) { // $NON-NLS-1$
                    root.setAttribute("xmlns:"+p,nsUri); // $NON-NLS-1$
                    return p;
                }
            }
        } 
        return "";
    }


    // ======================================================================
    // clone method
    // ======================================================================
    /**
     * Clone a Document. This method returns a deep copy of the input
     * Document.
     * @param inDoc the input document
     * @return the cloned Document
     */
    public static Document clone( Document inDoc ) {
            Document rtnVal = (Document)inDoc.cloneNode(true);
            return ( rtnVal );
    }
    
    // ======================================================================
    // jdom compatibility methods
    // ======================================================================
    /**
     * Returns the textual content of the named child element, or null if
     * there's no such child.
     * See: org.jdom.Element 'getChildText()'
     * @param element to search for children
     * @return java.lang.String
     */
    public static String getChildText(Element element, String nodeName) {
        NodeList ndlist = element.getChildNodes();
        Node nd;
        String rtnVal = null;
        for(int i=0; i<ndlist.getLength(); i++ ) {
            nd = (Node)ndlist.item(i);
            if ( nd.getNodeType() == Node.ELEMENT_NODE &&
                 nd.getNodeName().equals( nodeName )){
                rtnVal = getText(nd); 
                break;
            }
        }
        return rtnVal;
    }

    //=========================================================================
    // utiltiy methods
    //=========================================================================
    /**
     * Returns the textual content directly held under this element as a
     * string. The call does not recurse into child elements. If no 
     * textual value exists for the element, an empty string is returned.
     * @param n
     * @return
     */
    public static String getText(Node n) {
        String rtnVal = "";
        NodeList nlist = n.getChildNodes();
        Node currentNode;
        for ( int i = 0 ; i < nlist.getLength() ; i++ ){
            currentNode = nlist.item(i);
            if ( currentNode.getNodeType() == Node.TEXT_NODE ){
                rtnVal = currentNode.getNodeValue();
            }
        }
        return rtnVal;
    }

    /**
     * This returns the attribute value for the attribute of the input element
     * with the given name and within no namespace. Returns null if there is
     * no such attribute, and the empty string if the attribute value is empty.
     * @param e the Node
     * @param key the attribute name
     * @return
     */
    public static String getAttributeValue( Element e, String key ){
        String value = null;
        NamedNodeMap map = e.getAttributes();
        Node keyNode = map.getNamedItem(key);
        if (keyNode != null) {
            value = keyNode.getNodeValue();
        }
        return value;
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name and default (null) namespace. The parent element
     * is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addNode(Element parent, String elementName){
        addNode( parent, elementName, null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name and namespace. The parent element
     * is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addNode(Element parent, String elementName, String namespaceURI){
        Document doc = parent.getOwnerDocument();
        Element elt = doc.createElementNS(namespaceURI, elementName);
        parent.appendChild(elt);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, BigDecimal value, and a defult (null) namespace. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addBigDecimal(Element parent, String elementName, BigDecimal value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, BigDecimal value, and a defult (null) namespace. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addBigDecimal(Element parent, String elementName, BigDecimal value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, boolean value, and namespace. The parent elenent is assumed
     * to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addBoolean(Element parent, String elementName, boolean value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }
    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, boolean value, and namespace. The parent elenent is assumed to be attached
     * to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addBoolean(Element parent, String elementName, boolean value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sql.Date value, and the default (null) namespace. The
     * parent element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addDate(Element parent, String elementName, Date value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sql.Date value, and namespace. The parent element is
     * assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addDate(Element parent, String elementName, Date value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, double value, and default (null) namespace. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addDouble(Element parent, String elementName, double value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, double value, and namespace. The parent element is assumed
     * to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addDouble(Element parent, String elementName, double value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, int value, and the default (null) namespace. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addInteger(Element parent, String elementName, int value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, int value, and namespace. The parent element is assumed to be
     * attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addInteger(Element parent, String elementName, int value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, long value, and the default (null) namespace. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addLong(Element parent, String elementName, long value) {
        addString(parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, long value, and namespace. The parent element is assumed
     * to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addLong(Element parent, String elementName, long value, String namespace) {
        addString(parent, elementName, XMIConverter.toString(value), namespace);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, String value, and default (null) namespace. The parent element
     * is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addString(Element parent, String elementName, String value){
        addString( parent, elementName, value, null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, String value, and namespace. The parent element
     * is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addString(Element parent, String elementName, String value, String namespaceURI){
        Document doc = parent.getOwnerDocument();
        Element elt = doc.createElementNS(namespaceURI, elementName);
        elt.appendChild(doc.createTextNode(value));
        parent.appendChild(elt);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sqlTime value, and default (null) namespace. The
     * parent element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addTime(Element parent, String elementName, Time value){
        addString( parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sqlTime value, and namespaceURI. The parent element
     * is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addTime(Element parent, String elementName, Time value, String namespaceURI){
        addString( parent, elementName, XMIConverter.toString(value), namespaceURI);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sql.Timestamp value, and default (null) namespace.
     * The parent element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addTimestamp(Element parent, String elementName, Timestamp value){
        addString( parent, elementName, XMIConverter.toString(value), null);
    }

    /**
     * Add a child element to a parent element. The child element will have the
     * indicated name, java.sql.Timestamp value, and namespaceURI. The parent
     * element is assumed to be attached to a Document.
     * @param parent the parent element
     * @param elementName the Element name
     * @param value the Element value
     */
    public static void addTimestamp(Element parent, String elementName, Timestamp value, String namespaceURI){
        addString( parent, elementName, XMIConverter.toString(value), namespaceURI);
    }

    /**
     * Create a java.util.List contain the child nodes of the specified 
     * element.
     * @param elt
     * @return
     */
    public static List <Node> getChildrenAsList(Element elt) {
        List <Node> rtnVal = new ArrayList <Node> ();
        NodeList nlist = elt.getChildNodes();
        for (int i=0 ; i<nlist.getLength() ; i++) {
            rtnVal.add(i, (Node)nlist.item(i));
        }
        return rtnVal;
    }
    
    /**
     * Return the source reference id for the specified node. 
     * @param node
     * @return
     */
    public static String getSourceReferenceId(Node node) {
        if (node instanceof Element) {
            Element element = (Element)node;
            String id = element.getAttribute("id"); //$NON-NLS-1$
            if (StringUtil.isEmpty(id)) {
                return createSourceReferenceId(element);
            }
            return id;
        }
        if (node instanceof Attr) {
            Attr attr = (Attr)node;
            Element element = attr.getOwnerElement();
            String sourceId = getSourceReferenceId(element);
            return sourceId + "/@" + attr.getName(); //$NON-NLS-1$
        }
        if (node.getNodeType() == Node.CDATA_SECTION_NODE
                || node.getNodeType() == Node.TEXT_NODE) {
            
            // See these on the xpath for a text or cdata:
            //   http://www.w3.org/TR/xpath#section-Text-Nodes
            //   http://www.w3.org/TR/xpath#node-tests
            // Note if you have an element containing text, a cdata, more text
            // then all of these will correspond to the same xpath expression
            // ending in text(), because xpath always treats adjacent text and
            // cdatas as if they were merged.
            
            Node parent = node.getParentNode();
            String parentPath = getSourceReferenceId(parent);
            if( parentPath.lastIndexOf('/') != parentPath.length() - 1){
                // make it end in '/'
                parentPath += '/';
            }
            
            int textsCount = 0;
            boolean lastWasText = false;
            // the current node's index in the list of it's parent's child texts
            int nodeTextListIndex = -1; 
            // for all siblings 
            //  find if more than one texts (each text includes merged cdatas)
            //  calculate the current node's 1-base index (cannot be 0)
            NodeList children = parent.getChildNodes();
            int len = children.getLength();
            boolean siblingIsText;
            for (int i = 0; i < len; i++) {
                Node sibling = children.item(i);
                
                siblingIsText = Node.TEXT_NODE == sibling.getNodeType()
                        || Node.CDATA_SECTION_NODE == sibling.getNodeType();
                if( siblingIsText ){
                    if( ! lastWasText ){
                        textsCount++;
                        if( textsCount > 1 && -1 != nodeTextListIndex ){
                            break;
                        }
                    }
                    if( node == sibling ){
                        nodeTextListIndex = textsCount;
                        if( textsCount > 1 && -1 != nodeTextListIndex ){
                            break;
                        }
                    }
                }
                lastWasText = siblingIsText;
            }
            
            String path;
            if( textsCount == 1 ){
                // text() resolves to the only text child
                path = parentPath + "text()"; // $NON-NLS-1$
            }else{
                // text() resolves to the set of text children
                path = parentPath + "text()["+nodeTextListIndex+"]"; // $NON-NLS-1$
            }
            return path;
        }
        if( node instanceof Document ){
            return "/";
        }
        return null;
    }
    
    private static String createSourceReferenceId(Element elt) {
        StringBuffer buffer = new StringBuffer();
        while (StringUtil.isEmpty(elt.getAttribute("id"))) { //$NON-NLS-1$
            insertElementReferenceId(buffer, elt);
            if (elt.getParentNode() == elt.getOwnerDocument()) {
                break;
            }
            elt = (Element)elt.getParentNode();
        }
        buffer.insert(0, elt.getAttribute("id")); //$NON-NLS-1$
        return buffer.toString();
    }
    
    private static void insertElementReferenceId(StringBuffer buffer, Element elt) {
        int predicate = 1;
        Node sibling = elt;
        while ((sibling = sibling.getPreviousSibling()) != null) {
            if (elt.getNodeName().equals(sibling.getNodeName())) {
                predicate++;
            }
        }
        buffer.insert(0, "]"); //$NON-NLS-1$
        buffer.insert(0, predicate);
        buffer.insert(0, "["); //$NON-NLS-1$
        buffer.insert(0, elt.getNodeName());
        buffer.insert(0, "/"); //$NON-NLS-1$
    }
    
    /**
     * Behaves like getElementsByTagNameNS() but only return nodes that are the children
     * of a specified element.
     * 
     * @param element - element to find the children of, may not be null.
     * @param uri - only return children matching this namespace uri.
     * @param tag - only return children matching this tag name. 
     * @return NodeList - matching elements that are children of element.
     */
    public static NodeList getChildElementsByTagNameNS(Element element, String uri, String tag) {
        NodeList found = element.getElementsByTagNameNS(uri, tag);
        
        NodeListImpl children = new NodeListImpl();
        
        for (int i = 0; i < found.getLength(); i++) {
            Node node = found.item(i);
            
            Node parentNode = node.getParentNode();
            
            if (parentNode.equals(element)) {
                children.add(node);
            }
        }
        
        return children;
    }
    
    
    /**
     * Behaves like getElementsByTagName() but only return nodes that are the children
     * of a specified element.
     * 
     * @param element - element to find the children of, may not be null.
     * @param tag - only return children matching this tag name. 
     * @return NodeList - matching elements that are children of element.
     */
    public static NodeList getChildElementsByTagName(Element element, String tag) {
        NodeList found = element.getElementsByTagName(tag);
        
        NodeListImpl children = new NodeListImpl();
        
        for (int i = 0; i < found.getLength(); i++) {
            Node node = found.item(i);
            
            Node parentNode = node.getParentNode();
            
            if (parentNode.equals(element)) {
                children.add(node);
            }
        }
        
        return children;
    }
    
    /**
     * compare two nodes. Needed to write my own because of problems in the implementation
     * of the WST Range.compareBoundaryPoints();
     * 
     * @param node1
     * @param offset1
     * @param node2
     * @param offset2
     * @return less than zero if node1 < nodes2, 0 if node1 == node2, greater than zero if node1 > node2
     */
    public static int compareLocations(Node node1, int offset1, Node node2, int offset2) {
        
        if (node1 == node2) {
            return offset1 - offset2;
        }
        
        // need  to find a common parent of the two edit parts,
        // then compare the respective ancestors of that 
        // commons part. The ancestor that is first in the list
        // is considers less than the other.
        List parents1 = getParents(node1);
        List parents2 = getParents(node2);
        
        // insert the parts into the parents list
        parents1.add(0, node1);
        parents2.add(0, node2);

        // add effective nodes to the list
        if (node1.hasChildNodes()) {
            parents1.add(0, node1.getChildNodes().item(offset1));
        }

        if (node2.hasChildNodes()) {
            parents2.add(0, node2.getChildNodes().item(offset2));
        }

        Node commonParent = null;
        int cpIndex1 = 1;   // index of common parent in parents1
        int cpIndex2 = 0;   // index of common parent in parents2
        
        // find a common parent by searching parents2 for items
        // in parent1.
        // skip the first element, so we don't get
        // a part on the mark as the common parent.
        for (Iterator iter = parents1.listIterator(cpIndex1); iter.hasNext();) {
            Node node = (Node) iter.next();
            int index = parents2.indexOf(node);
            if (index > 0) {            // don't allow the part at the mark to be a parent
                commonParent = node;
                cpIndex2 = index;
                break;
            }
            cpIndex1++;
        }
        
        // this can happen when the document is in flux.
        if (commonParent == null) {
            return -1;
        }
        
        // get the direct childs of the common parent and
        // compare their position in the flow.
        NodeList cpChildren = commonParent.getChildNodes();
        
        Node cpChild1 = (Node) parents1.get(cpIndex1 - 1); // child of common parent representing node1
        Node cpChild2 = (Node) parents2.get(cpIndex2 - 1); // child of common parent representing node2
        if (cpChild1 == cpChild2) {
            return offset1 - offset2;
        }
        else if (cpChild1 == null) {    // cpChild1 is the last child
            return 1;
        }
        else if (cpChild2 == null) {    // cpChild2 is the last child
            return -1;
        }
        
        int indexCpChild1 = getOffsetInParent(cpChild1);
        for (int i = (indexCpChild1 + 1); i < cpChildren.getLength(); i++) {
            if (cpChildren.item(i) == cpChild2) {
                return -1;  // node1 comes before node2 (less than)
            }
        }
        
        // node2 not found in the above loop so it must have
        // come before node1 in the list. This means node1 is
        // greater than node2.
        return 1;
    }
    
    /**
     * 
     * @param node
     * @return a list of parent nodes
     */
    private static List getParents(Node node) {
        List list = new ArrayList();
        node = node.getParentNode();
        while (node != null) {
            list.add(node);
            node = node.getParentNode();
        }
        
        return list;
    }
    
    
    
    /**
     * Convert the parent, offset into the child node. 
     * Node without child are just returned.
     *  
     * @param node
     * @param offset
     * @return Node
     */
    public static Node getNode(Node node, int offset) {
        if (node.getNodeType() != Node.TEXT_NODE) {
            return node.getChildNodes().item(offset);
        }
        
        return node;
    }

    /**
     * Helper method to get the offset of a given node with respect to its
     * parent.
     * 
     * @param node -
     *            may not be null
     * @return int - zero-based offset of node in its container or -1 if the
     *         node as no container.
     */
    public static int getOffsetInParent(Node node) {
        Node parent = node.getParentNode();
        if (parent != null) {
            NodeList children = parent.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = (Node) children.item(i);
                if (child == node) {
                    return i;
                }
            }
            throw new IllegalStateException(); // how could the child not be
            // found?
        }
    
        return -1;
    }
    
    /**
     * Find all element of a given name that is a child or subchild to n levels of a given parent element.
     * If an a child element has the given tag name, none of its child nodes that may also have that tag
     * name will be added to the NodeList. So essentially only the first child elelent with the given name
     * will be returned from each branch.  
     * For example a panel within a panel, only the parent panel would be returned. 
     * @return the elements found, or null if none are found or if there is an error. 
     */
    public static NodeList getAllChildElementsByName( Element parent, String name ){
    	NodeListImpl result = new NodeListImpl(); 
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String nodeName = e.getNodeName();
    	        if( name.equals(nodeName) ) {
    	            	result.add(e);
    	        }
    	        else{
    	        	NodeList nl = getAllChildElementsByName((Element)n, name);
    	        	if(nl.getLength()>0){
    	        		result = concatNodeLists(result, nl);
    	        	}
    	        }
    		}
    	}
    	return result;
    }
    
    /**
     * This method will return a nodelist containing all the nodes from 2 given nodeLists. 
     * @param nl1
     * @param nl2
     * @return
     */
    public static NodeListImpl concatNodeLists(NodeList nl1, NodeList nl2){
    	NodeListImpl results = new NodeListImpl();
    	for(int i=0; i<nl1.getLength(); i++){
    		results.add(nl1.item(i));
    	}
    	for(int i=0; i<nl2.getLength(); i++){
    		results.add(nl2.item(i));
    	}
    	return results;
    }
    
    /**
     * Find the first element that is a child or subchild under a given Element parent. So essentially
     * returning the first occurance of a given node anywhere in the parent elements child subtree.
     * 
     * @return the element found, or null if it is not available
     */
    public static Element getFirstElementAtAnyDepthByNodeName( Element parent, String name ){
    	NodeList l = parent.getChildNodes();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n.getNodeType()==Node.ELEMENT_NODE) {
    			Element e = (Element)n;
    	        String nodeName = e.getNodeName();
    	        if( name.equals(nodeName) ) {
    	            	return e;
    	        }
    	        else{
    	        	Element returnElement = getFirstElementAtAnyDepthByNodeName(e,name);
    	        	if(null != returnElement){
    	        		return returnElement;
    	        	}
    	        }
    		}
    	}
    	return null;
    }

    
    /**
     * Find all children and subChildren of the given parent to n sub levels. This will include text nodes!
     * 
     * @return NodeList of all child elements of the parent
     */
    public static NodeList getChildNodesToNLevels( Node parent){
    	NodeList l = parent.getChildNodes();
    	NodeListImpl returnList = new NodeListImpl();
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		returnList.add(n);
    		NodeList children = getChildNodesToNLevels(n);
    	    if(null != children){
    	    	returnList = concatNodeLists(returnList, children);
    		}
    	}
    	return returnList;
    }
    
    
    /**
     * Search through all child nodes and sub child nodes of the given parent node, and return true, if the given child node is ever found. 
     * 
     * @return NodeList of all child elements of the parent
     */
    public static boolean findChildAtAnyLevel( Node parent, Node child){
    	NodeList l = parent.getChildNodes();
    	boolean returnBoolean = false;
    	int count = l.getLength();
    	for( int i=0; i<count; i++ ) {
    		Node n = l.item(i);
    		if(n == child){
    			returnBoolean = true;
    		}
    		else{
	    		returnBoolean = returnBoolean || findChildAtAnyLevel(n, child);
	    	    
    		}
    		if(returnBoolean){
    			return true;
    		}
    	}
    	return returnBoolean;
    }
}
