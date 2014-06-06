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

package com.sun.org.apache.xerces.internal.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Sun implementation classes mock-up, for compilation
 * purposes.
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {

	@Override
	public Object getAttribute(String arg0) throws IllegalArgumentException {
		return null;
	}

	@Override
	public boolean getFeature(String arg0) throws ParserConfigurationException {
		return false;
	}

	@Override
	public DocumentBuilder newDocumentBuilder()
			throws ParserConfigurationException {
		return null;
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
			throws IllegalArgumentException {
	}

	@Override
	public void setFeature(String arg0, boolean arg1)
			throws ParserConfigurationException {
	}

}
