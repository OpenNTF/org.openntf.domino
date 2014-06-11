/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author jgallagher
 * 
 */
public class XMLDocument extends XMLNode {
	private static final long serialVersionUID = -8106159267601656260L;

	public XMLDocument() {
	}

	public XMLDocument(final Node node) {
		super(node);
	}

	public XMLDocument(final String xml) throws SAXException, IOException, ParserConfigurationException {
		loadString(xml);
	}

	public XMLNode getDocumentElement() {
		return new XMLNode(((Document) this.node).getDocumentElement());
	}

	public void loadURL(final String urlString) throws SAXException, IOException, ParserConfigurationException {
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		this.node = getBuilder().parse((InputStream) conn.getContent());
	}

	public void loadInputStream(final InputStream is) throws SAXException, IOException, ParserConfigurationException {
		this.node = getBuilder().parse(is);
	}

	public void loadString(final String s) throws SAXException, IOException, ParserConfigurationException {
		loadInputStream(new ByteArrayInputStream(s.getBytes("UTF-8")));
	}

	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setValidating(false);
		// fac.setNamespaceAware(true);
		return fac.newDocumentBuilder();
	}

	public static String escapeXPathValue(final String input) {
		return input.replace("'", "\\'");
	}
}