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

package com.sun.org.apache.xml.internal.serialize;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * Sun implementation classes mock-up, for compilation purposes.
 */
public class XMLSerializer {

	public XMLSerializer(OutputFormat format) {
		throw new RuntimeException();
	}

	public void setOutputByteStream(java.io.OutputStream output) {
		throw new RuntimeException();
	}

	public void setOutputCharStream(java.io.Writer writer) {
		throw new RuntimeException();
	}

	public void serialize(Element elem) throws java.io.IOException {
		throw new RuntimeException();
	}

	public void serialize(DocumentFragment frag) throws java.io.IOException {
		throw new RuntimeException();
	}

	public void serialize(Document doc) throws java.io.IOException {
		throw new RuntimeException();
	}
}
