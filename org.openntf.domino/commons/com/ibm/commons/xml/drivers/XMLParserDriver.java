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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;

import com.ibm.commons.xml.*;
import com.ibm.commons.xml.xpath.XPathException;



/**
 * XML Parser driver.
 * This driver encapsulate the actual parser, and the non standard methods.
 * 
 * @ibm-api
 */
public interface XMLParserDriver {
	/**
	 * Returns the W3C DomImplementation
	 * @return
	 */
	public DOMImplementation getDOMImplementation();
	/**
	 * Creates a DOM Document object given the uri, qualified name, and Document type.
	 * 
	 * @param namespaceURI
	 *         a fully qualified namespace URI
	 * @param qualifiedName
	 *         The qualified name of the document element to be created or null.
	 * @param doctype
	 *         The type of document to be created or null. When doctype is not null, its Node.ownerDocument attribute is set to the document being created.
	 * @return
	 *         A new Document object with its document element. If the NamespaceURI, qualifiedName, and doctype are null, the returned Document is empty with no document element.
	 *         
	 * @throws XMLException
	 *         throws an XML exception when invalid input is given
	 */
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws XMLException;
    /**
     * Creates an empty DocumentType node. Entity declarations and notations are not made available. 
     * Entity reference expansions and default attribute additions do not occur!
     * 
     * @param qualifiedName
     *          the qualified name of the document type
     * @param publicId
     *          the public identifier
     * @param systemId
     *          the system identifier
     *          
     * @return
     *          a new document type based on the parameters provided
     *          
     * @throws XMLException
     *          throws an XMLException if the parameters contain illegal XML input
     */
    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws XMLException;
    /**
     * Creates a W3C DOM Document based on the inputstream provided. 
     * 
     * @param is
     *          an input stream which contains a DOM document
     * @param ignoreBlanks
     *          ignore blanks (spaces and tabs) in the provided stream
     * @param resolveEntities
     *          resolve entities in the provided stream
     * @param validate
     *          validate the resulting document
     *          
     * @return
     *          a document built from the provided input stream
     *          
     * @throws XMLException
     *          throws an exception if the provided inputstream contains illegal XML markup
     */
    public Document parse(InputStream is, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException;
    /**
     * Creates a W3C DOM Document based on the Reader provided. 
     * 
     * @param reader
     *          an reader which contains a DOM document
     * @param ignoreBlanks
     *          ignore blanks (spaces and tabs) in the provided reader
     * @param resolveEntities
     *          resolve entities in the provided stream
     * @param validate
     *          validate the resulting document
     *          
     * @return
     *          a document built from the provided reader
     *          
     * @throws XMLException
     *          throws an exception if the provided reader contains illegal XML markup
     */
    public Document parse(Reader reader, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException;
    /**
     * Saves the provided node to the given stream, using the given formatter.
     * 
     * @param os
     *          the stream where the DOM Node is to be saved (serialized) to
     * @param node
     *          a DOM Node that is to be serialized
     * @param format
     *          a formatter that is used to format the Node when serialized
     *          
     * @throws XMLException
     *          throws an exception if the node is illegal or if the stream is not accessible
     */
    public void serialize(OutputStream os, Node node, Format format) throws XMLException;
    /**
     * Saves the provided node to the given stream, using the given formatter.
     * 
     * @param os
     *          the stream where the DOM Node is to be saved (serialized) to
     * @param node
     *          a DOM Node that is to be serialized
     * @param format
     *          a formatter that is used to format the Node when serialized
     *          
     * @throws XMLException
     *          throws an exception if the node is illegal or if the stream is not accessible
     */
    public void serialize(Writer writer, Node node, Format format) throws XMLException;
    /**
     * Creates an XPage expression based on the provided String
     * 
     * @param xpath
     *          the XPath we wish to encapsulate
     *          
     * @return
     *          an object describing the provided xpath
     *          
     * @throws XPathException
     */
    public Object createXPath(String xpath) throws XPathException;
    /**
     * Evaluate the xpath relative to the given node and namespace context
     * @param node
     *          a node that we wish to evaluate using the given XPath
     * @param xpath
     *          a namespace context to be used when evaluting the XPath
     * @param nsContext
     * 
     * @return
     *          an object based on the given XPath
     *          
     * @throws XPathException
     */
    public XResult evaluateXPath(Node node, Object xpath, NamespaceContext nsContext) throws XPathException;
    /**
     * Evaluate the xpath relative to the given NodeList and namespace context
     * @param nodeList
     *          a node list that we wish to evaluate using the given XPath
     * @param xpath
     *          a namespace context to be used when evaluting the XPath
     * @param nsContext
     * 
     * @return
     *          an object based on the given XPath
     *          
     * @throws XPathException
     */
    public XResult evaluateXPath(NodeList nodeList, Object xpath, NamespaceContext nsContext) throws XPathException;
    /**
     * Returns the XPath context associated to an XML document
     * @param doc
     *          a DOM Document
     * @return
     */
    public XPathContext getXPathContext(Document doc);
    /**
     * Push an XPath context to an existing document.
     * An XML document contains a base XPath that is used when evaluating XPath against a
     * node. This base XPath is composed by the aggregating all the XPath contexts
     * 
     * @param doc
     *          the document where the xpath is to be set
     * @param xpath
     *          the xpath to set
     * @throws XMLException
     */
    public void pushXPathContext(Document doc, String xpath) throws XMLException;
    /**
     * Remove the latest XPath context that has been set to the document
     * An XML document contains a base XPath that is used when evaluating XPath against a
     * node. This base XPath is composed by the aggregating all the XPath contexts
     * pushed to the document.
     * 
     * @param doc 
     *          the source document
     * 
     * @throws XMLException
     */
    public void popXPathContext(Document doc) throws XMLException;
    /**
     * Get the selection NamespaceContext use during the XPath evaluation on the document.
     * 
     * @param doc
     *          the source document
     *          
     * @return
     *          a NamespaceContext
     */
    public NamespaceContext getNamespaceContext(Document doc);
    /**
     * Set the namespaces map use during the XPath evaluation on the document.
     * 
     * @param doc 
     *          the source document
     * @param selectionNS 
     *          the NamespaceContext used during XPath evaluation on the document in order to resolve namespaces.
     */
    public void setNamespaceContext(Document doc, NamespaceContext ns);
    /**
     * Returns a DocumentTraversal object that can be used on the provided document object
     * 
     * @param doc
     * 
     * @return
     */
    public DocumentTraversal getDocumentTraversal(Document doc);
}
