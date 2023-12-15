/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
		return new XMLNode(((Document) node_).getDocumentElement());
	}

	public void loadURL(final String urlString) throws SAXException, IOException, ParserConfigurationException {
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		node_ = getBuilder().parse((InputStream) conn.getContent());
	}

	public void loadInputStream(final InputStream is) throws SAXException, IOException, ParserConfigurationException {
		node_ = getBuilder().parse(is);
	}

	public void loadString(final String s) throws SAXException, IOException, ParserConfigurationException {
		loadInputStream(new ByteArrayInputStream(s.getBytes("UTF-8"))); //$NON-NLS-1$
	}

	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setValidating(false);
		// fac.setNamespaceAware(true);
		return fac.newDocumentBuilder();
	}

	public static String escapeXPathValue(final String input) {
		return input.replace("'", "\\'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String toString() {
		try {
			return getXml(null);
		} catch (IOException e) {
			return e.getMessage();
		}
	}
}