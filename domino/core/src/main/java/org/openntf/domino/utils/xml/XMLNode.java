/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openntf.domino.utils.DominoUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jgallagher
 *
 */
public class XMLNode implements Map<String, Object>, Serializable {
	private static final long serialVersionUID = 2304991412510751453L;
	private static TransformerFactory tFactory = TransformerFactory.newInstance();
	public static Transformer DEFAULT_TRANSFORMER = createTransformer(null);
	protected org.w3c.dom.Node node_ = null;
	private transient XPath xPath_ = null;
	private Map<String, Object> getResults_ = new HashMap<String, Object>();

	protected XMLNode() {
	}

	public XMLNode(final org.w3c.dom.Node node) {
		node_ = node;
	}

	public XMLNode selectSingleNode(final String xpathString) {
		XMLNodeList result = this.selectNodes(xpathString);
		return result.size() == 0 ? null : result.get(0);
	}

	public static Transformer createTransformer(final InputStream xsltStream) {
		Transformer transformer = null;
		try {
			if (xsltStream == null) {
				transformer = tFactory.newTransformer();
			} else {
				Source filter = new StreamSource(xsltStream);
				transformer = tFactory.newTransformer(filter);
			}
			// We don't want the XML declaration in front
			//transformer.setOutputProperty("omit-xml-declaration", "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); //$NON-NLS-1$ //$NON-NLS-2$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}

		return transformer;
	}

	public XMLNodeList selectNodes(final String xpathString) {
		try {
			NodeList nodes = (NodeList) this.getXPath().compile(xpathString).evaluate(node_, XPathConstants.NODESET);
			XMLNodeList result = new XMLNodeList(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {
				result.add(new XMLNode(nodes.item(i)));
			}

			return result;
		} catch (XPathExpressionException xee) {
			DominoUtils.handleException(xee);
			return null;
		}
	}

	public String getAttribute(final String attribute) {
		if (this.node_ == null) {
			return ""; //$NON-NLS-1$
		}
		NamedNodeMap attributes = this.node_.getAttributes();
		if (attributes == null) {
			return ""; //$NON-NLS-1$
		}
		Node attr = attributes.getNamedItem(attribute);
		if (attr == null) {
			return ""; //$NON-NLS-1$
		}
		return attr.getTextContent();
	}

	public void removeAttribute(final String attribute) {
		if (null != this.node_.getAttributes().getNamedItem(attribute)) {
			this.node_.getAttributes().removeNamedItem(attribute);
		}
	}

	public void setAttribute(final String attribute, final String value) {
		Node attr = this.node_.getAttributes().getNamedItem(attribute);
		if (attr == null) {
			attr = getDocument().createAttribute(attribute);
		}
		attr.setNodeValue(value == null ? "" : value); //$NON-NLS-1$
		this.node_.getAttributes().setNamedItem(attr);
	}

	public String getNodeName() {
		return node_.getNodeName();
	}

	public short getNodeType() {
		return node_.getNodeType();
	}

	public String getText() {
		if (node_ == null) {
			return ""; //$NON-NLS-1$
		}
		return node_.getTextContent();
	}

	public void setText(final String text) {
		if (node_ == null) {
			return;
		}
		node_.setTextContent(text);
	}

	public String getTextContent() {
		return this.getText();
	}

	public void setTextContent(final String textContent) {
		this.setText(textContent);
	}

	public String getNodeValue() {
		if (node_ == null) {
			return ""; //$NON-NLS-1$
		}
		return node_.getNodeValue();
	}

	public void setNodeValue(final String value) {
		if (node_ == null) {
			return;
		}
		node_.setNodeValue(value);
	}

	public XMLNode addChildElement(final String elementName) {
		Node node = this.getDocument().createElement(elementName);
		this.node_.appendChild(node);
		return new XMLNode(node);
	}

	public XMLNode insertChildElementBefore(final String elementName, final XMLNode refNode) {
		Node node = this.getDocument().createElement(elementName);
		this.node_.insertBefore(node, refNode.getNode());
		return new XMLNode(node);
	}

	public XMLNode getFirstChild() {
		Node node = this.getNode().getFirstChild();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public XMLNode getFirstChildElement() {
		Node node = this.getNode().getFirstChild();
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getNextSibling();
		}
		return node == null ? null : new XMLNode(node);
	}

	public XMLNode getParentNode() {
		Node node = this.getNode().getParentNode();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public void removeChild(final XMLNode childNode) {
		this.getNode().removeChild(childNode.getNode());
	}

	public XMLNodeList getChildNodes() {
		return new XMLNodeList(getNode().getChildNodes());
	}

	public void removeChildren() {
		for (XMLNode child : this.getChildNodes()) {
			removeChild(child);
		}
	}

	public XMLNode getNextSibling() {
		Node node = this.getNode().getNextSibling();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public XMLNode getNextSiblingElement() {
		Node node = this.getNode().getNextSibling();
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getNextSibling();
		}
		return node == null ? null : new XMLNode(node);
	}

	public void appendChild(final XMLNode node) {
		this.getNode().appendChild(node.getNode());
	}

	public void insertBefore(final XMLNode newChild, final XMLNode refChild) {
		this.getNode().insertBefore(newChild.getNode(), refChild.getNode());
	}

	public org.w3c.dom.Node getNode() {
		return this.node_;
	}

	@Override
	public Object get(final Object arg0) {
		String path = String.valueOf(arg0);

		if (path.equals("nodeValue")) { //$NON-NLS-1$
			return this.getNode().getNodeValue();
		} else if (path.equals("textContent")) { //$NON-NLS-1$
			return this.getNode().getTextContent();
		}

		if (!this.getResults_.containsKey(path)) {
			try {
				XMLNodeList nodes = this.selectNodes(path);
				if (nodes.size() == 1) {
					// this.getResults.put(path, nodes.get(0).getNode());
					this.getResults_.put(path, nodes.get(0));
				} else {
					this.getResults_.put(path, nodes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.getResults_.get(path);
	}

	/**
	 * Equivalent to {@link #getXml(Transformer)} passed null.
	 */
	public String getXml() throws IOException {
		return getXml(null);
	}

	public String getXml(Transformer transformer) throws IOException {
		try {
			if (transformer == null) {
				transformer = DEFAULT_TRANSFORMER;
			}
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(this.node_);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getXml(Transformer transformer, final Path out) throws IOException {
		try {
			if (transformer == null) {
				transformer = DEFAULT_TRANSFORMER;
			}

			// StreamResult xResult = new StreamResult(out); - This constructor has problems with german umlauts
			// See: http://comments.gmane.org/gmane.text.xml.saxon.help/6790
			StreamResult result = new StreamResult(out.toUri().toString());
			DOMSource source = new DOMSource(this.node_);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XPath getXPath() {
		if (this.xPath_ == null) {
			xPath_ = XPathFactory.newInstance().newXPath();
		}
		return this.xPath_;
	}

	private Document getDocument() {
		return this.node_.getOwnerDocument();
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean containsKey(final Object arg0) {
		return false;
	}

	@Override
	public boolean containsValue(final Object arg0) {
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Object put(final String arg0, final Object arg1) {
		if (arg0.equals("nodeValue")) { //$NON-NLS-1$
			this.getNode().setNodeValue(String.valueOf(arg1));
			return arg1;
		} else if (arg0.equals("textContent")) { //$NON-NLS-1$
			this.getNode().setNodeValue(String.valueOf(arg1));
			return arg1;
		}
		return null;
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> arg0) {
	}

	@Override
	public Object remove(final Object arg0) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}
}