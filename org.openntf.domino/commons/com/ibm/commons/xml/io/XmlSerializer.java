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

package com.ibm.commons.xml.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;

import org.w3c.dom.Document;

import com.ibm.commons.xml.XMLException;

/**
 * XML serialization methods.
 */
public class XmlSerializer {

	public static void writeDOMObject(ObjectOutput os, Document document)
			throws XMLException, IOException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static Document readDOMObject(ObjectInput is) throws IOException,
			XMLException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static Document readDOMObject(ObjectInput is, Document doc)
			throws IOException, XMLException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static void serialize(OutputStream os, Document doc)
			throws XMLException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static Document deserialize(InputStream is) throws XMLException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static Document deserialize(InputStream is, Document doc)
			throws XMLException {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

	public static void main(String[] args) {
		throw new UnsupportedOperationException(
				"Only available from domino/xpages deployments");
	}

}