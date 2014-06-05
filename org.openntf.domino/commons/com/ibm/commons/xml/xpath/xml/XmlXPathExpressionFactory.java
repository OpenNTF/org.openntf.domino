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

/*
 * Created on May 30, 2005
 * 
 */
package com.ibm.commons.xml.xpath.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.drivers.XMLParserDriver;
import com.ibm.commons.xml.xpath.AbstractExpressionFactory;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.commons.xml.xpath.part.Part;

/**
 * @author Mark Wallace
 * @author Eugene Konstantinov
 */
public class XmlXPathExpressionFactory extends AbstractExpressionFactory {

	protected XMLParserDriver _domDriver;
	
	public XmlXPathExpressionFactory(XMLParserDriver domDriver) {
		_domDriver = domDriver;
	}
	
	public XmlXPathExpressionFactory() {
		_domDriver = DOMUtil.getParserDriver();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xfaces.xpath.AbstractExpressionFactory#isValidDataContext(java.lang.Object)
	 */
	public boolean isValidDataContext(Object data) {
		return (data instanceof Node || 
				data instanceof Document || 
				data instanceof XResult);
	}

	/* (non-Javadoc)
	 * @see com.ibm.commons.xml.xpath.AbstractExpressionFactory#createExpression(java.lang.Object, java.lang.String, boolean)
	 */
	public XPathExpression createExpression(Object data, String expression, boolean usecache) throws XPathException {
		return super.createExpression(data, expression, usecache);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xfaces.xpath.AbstractExpressionFactory#createSimpleExpression(java.lang.String,
	 *      boolean, com.ibm.xfaces.xpath.Part[])
	 */
	protected XPathExpression createSimpleExpression(String expression,
			boolean isFromRoot, Part[] parts) throws XPathException {
		return new XmlSimpleExpression(expression, isFromRoot, parts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xfaces.xpath.AbstractExpressionFactory#createComplexExpression(java.lang.String)
	 */
	protected XPathExpression createComplexExpression(String expression)
			throws XPathException {
		Object compiledXPath = _domDriver.createXPath(expression);
		return new XmlComplexExpression(_domDriver,expression,compiledXPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xfaces.xpath.AbstractExpressionFactory#isXMLNameStartCharacter(char)
	 */
	protected boolean isXMLNameStartCharacter(char ch) {
		boolean result = false;
		result = (Character.isLetterOrDigit(ch) || ch == '.' || ch=='-' || ch=='_' || ch==':') ? true:false;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xfaces.xpath.AbstractExpressionFactory#isXMLNameCharacter(char)
	 */
	protected boolean isXMLNameCharacter(char ch) {
		boolean result = false;
		result = (Character.isLetterOrDigit(ch) || ch == '.' || ch=='-' || ch=='_' || ch==':') ? true:false;
		return result;
	}

}
