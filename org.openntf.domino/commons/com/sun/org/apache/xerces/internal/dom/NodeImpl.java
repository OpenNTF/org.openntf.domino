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

package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;


/**
 * Sun implementation classes mock-up, for compilation
 * purposes.
 */
public class NodeImpl implements Node {
	// Non DOM
	public Object getUserData() {
		return null;
	}
	public void setUserData(Object data) {
	}
	
	// DOM
	public Node appendChild(Node newChild) throws DOMException {
		
		return null;
	}

	public Node cloneNode(boolean deep) {
		
		return null;
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		
		return 0;
	}

	public NamedNodeMap getAttributes() {
		
		return null;
	}

	public String getBaseURI() {
		
		return null;
	}

	public NodeList getChildNodes() {
		
		return null;
	}

	public Object getFeature(String feature, String version) {
		
		return null;
	}

	public Node getFirstChild() {
		
		return null;
	}

	public Node getLastChild() {
		
		return null;
	}

	public String getLocalName() {
		
		return null;
	}

	public String getNamespaceURI() {
		
		return null;
	}

	public Node getNextSibling() {
		
		return null;
	}

	public String getNodeName() {
		
		return null;
	}

	public short getNodeType() {
		
		return 0;
	}

	public String getNodeValue() throws DOMException {
		
		return null;
	}

	public Document getOwnerDocument() {
		
		return null;
	}

	public Node getParentNode() {
		
		return null;
	}

	public String getPrefix() {
		
		return null;
	}

	public Node getPreviousSibling() {
		
		return null;
	}

	public String getTextContent() throws DOMException {
		
		return null;
	}

	public Object getUserData(String key) {
		
		return null;
	}

	public boolean hasAttributes() {
		
		return false;
	}

	public boolean hasChildNodes() {
		
		return false;
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		
		return null;
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		
		return false;
	}

	public boolean isEqualNode(Node arg) {
		
		return false;
	}

	public boolean isSameNode(Node other) {
		
		return false;
	}

	public boolean isSupported(String feature, String version) {
		
		return false;
	}

	public String lookupNamespaceURI(String prefix) {
		
		return null;
	}

	public String lookupPrefix(String namespaceURI) {
		
		return null;
	}

	public void normalize() {
		
		
	}

	public Node removeChild(Node oldChild) throws DOMException {
		
		return null;
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		
		
	}

	public void setPrefix(String prefix) throws DOMException {
		
		
	}

	public void setTextContent(String textContent) throws DOMException {
		
		
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		
		return null;
	}
	

}
