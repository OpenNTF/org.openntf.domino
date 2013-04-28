/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jgallagher
 * 
 */
public class XMLNode implements Map<String, Object>, Serializable {
	private static final long serialVersionUID = 2304991412510751453L;

	protected org.w3c.dom.Node node = null;
	private transient XPath xPath = null;
	private Map<String, Object> getResults = new HashMap<String, Object>();

	protected XMLNode() {
	}

	public XMLNode(org.w3c.dom.Node node) {
		this.node = node;
	}

	public XMLNode selectSingleNode(String xpathString) throws XPathExpressionException {
		List<XMLNode> result = this.selectNodes(xpathString);
		return result.size() == 0 ? null : result.get(0);
	}

	public List<XMLNode> selectNodes(String xpathString) throws XPathExpressionException {

		NodeList nodes = (NodeList) this.getXPath().compile(xpathString).evaluate(node, XPathConstants.NODESET);
		List<XMLNode> result = new XMLNodeList(nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			result.add(new XMLNode(nodes.item(i)));
		}

		return result;
	}

	public String getAttribute(String attribute) {
		if (this.node == null) {
			return "";
		}
		NamedNodeMap attributes = this.node.getAttributes();
		Node attr = attributes.getNamedItem(attribute);
		if (attr == null) {
			return "";
		}
		return attr.getTextContent();
	}

	public void setAttribute(String attribute, String value) {
		Node attr = this.node.getAttributes().getNamedItem(attribute);
		if (attr == null) {
			attr = getDocument().createAttribute(attribute);
		}
		attr.setNodeValue(value);
		this.node.getAttributes().setNamedItem(attr);
	}

	public String getText() {
		if (node == null) {
			return "";
		}
		return node.getTextContent();
	}

	public void setText(String text) {
		if (node == null) {
			return;
		}
		node.setTextContent(text);
	}

	public String getTextContent() {
		return this.getText();
	}

	public void setTextContent(String textContent) {
		this.setText(textContent);
	}

	public String getNodeValue() {
		if (node == null) {
			return "";
		}
		return node.getNodeValue();
	}

	public void setNodeValue(String value) {
		if (node == null) {
			return;
		}
		node.setNodeValue(value);
	}

	public XMLNode addChildElement(String elementName) {
		Node node = this.getDocument().createElement(elementName);
		this.node.appendChild(node);
		return new XMLNode(node);
	}

	public XMLNode insertChildElementBefore(String elementName, XMLNode refNode) {
		Node node = this.getDocument().createElement(elementName);
		this.node.insertBefore(node, refNode.getNode());
		return new XMLNode(node);
	}

	public XMLNode getFirstChild() {
		Node node = this.getNode().getFirstChild();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public XMLNode getParentNode() {
		Node node = this.getNode().getParentNode();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public void removeChild(XMLNode childNode) {
		this.getNode().removeChild(childNode.getNode());
	}

	public XMLNode getNextSibling() {
		Node node = this.getNode().getNextSibling();
		if (node != null) {
			return new XMLNode(node);
		}
		return null;
	}

	public void appendChild(XMLNode node) {
		this.getNode().appendChild(node.getNode());
	}

	public void insertBefore(XMLNode newChild, XMLNode refChild) {
		this.getNode().insertBefore(newChild.getNode(), refChild.getNode());
	}

	public org.w3c.dom.Node getNode() {
		return this.node;
	}

	public Object get(Object arg0) {
		String path = String.valueOf(arg0);

		if (path.equals("nodeValue")) {
			return this.getNode().getNodeValue();
		} else if (path.equals("textContent")) {
			return this.getNode().getTextContent();
		}

		if (!this.getResults.containsKey(path)) {
			try {
				List<XMLNode> nodes = this.selectNodes(path);
				if (nodes.size() == 1) {
					// this.getResults.put(path, nodes.get(0).getNode());
					this.getResults.put(path, nodes.get(0));
				} else {
					this.getResults.put(path, nodes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.getResults.get(path);
	}

	public String getXml() throws IOException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat();
			format.setLineWidth(200);
			format.setIndenting(true);
			format.setIndent(2);
			XMLSerializer serializer = new XMLSerializer(bos, format);
			serializer.serialize((Element) this.node);
			return new String(bos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private XPath getXPath() {
		if (this.xPath == null) {
			xPath = XPathFactory.newInstance().newXPath();
		}
		return this.xPath;
	}

	private Document getDocument() {
		return this.node.getOwnerDocument();
	}

	public void clear() {
	}

	public boolean containsKey(Object arg0) {
		return false;
	}

	public boolean containsValue(Object arg0) {
		return false;
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return null;
	}

	public boolean isEmpty() {
		return false;
	}

	public Set<String> keySet() {
		return null;
	}

	public Object put(String arg0, Object arg1) {
		if (arg0.equals("nodeValue")) {
			this.getNode().setNodeValue(String.valueOf(arg1));
			return arg1;
		} else if (arg0.equals("textContent")) {
			this.getNode().setNodeValue(String.valueOf(arg1));
			return arg1;
		}
		return null;
	}

	public void putAll(Map<? extends String, ? extends Object> arg0) {
	}

	public Object remove(Object arg0) {
		return null;
	}

	public int size() {
		return 0;
	}

	public Collection<Object> values() {
		return null;
	}
}