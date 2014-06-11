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

package com.ibm.commons.xml;

import java.util.Date;
import java.util.Iterator;

/**
 * XPath result.
 * This interface encapsulate the result of an XPath engine.
 * @author Philippe Riand
 */
public interface XResult {
	
	public static final int TYPE_EMPTY         = 0;
	public static final int TYPE_STRING        = 1;
	public static final int TYPE_BOOLEAN       = 2;
	public static final int TYPE_NUMBER        = 3;
	public static final int TYPE_SINGLENODE    = 4;
	public static final int TYPE_MULTIPLENODES = 5;
    public static final int TYPE_DATE          = 6;
    
    public static final char DELIM = ' '; // delimiter for multiple values for XForms compatability
	
	/**
	 * Get the XPath result type.
	 * The returned value is one of:
	 * <li>
	 *   <ul>TYPE_EMPTY: an empty result set
	 *   <ul>TYPE_STRING: a single string value
	 *   <ul>TYPE_BOOLEAN: a single boolean value
	 *   <ul>TYPE_NUMBER: a single number value
	 *   <ul>TYPE_SINGLENODE: a single node
	 *   <ul>TYPE_MULTIPLENODES: a list of nodes
	 * </li>
	 * @return
	 */
	public int getValueType();
	
	/**
	 * Check if the result is a value or a Node.
	 * @return
	 */
	public boolean isValue();
	
	/**
	 * Check if the result is empty.
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * Check if the result contains multiple values.
	 * @return
	 */
	public boolean isMultiple();
	
	/**
	 * Converts the current result to a string.
	 * @return
	 * @throws XMLException if the result is not a value or a single node
	 */
	public String getStringValue() throws XMLException;
	
	/**
	 * Converts the current result to a number.
	 * @return
	 * @throws XMLException if the result is not a value or a single node
	 */
	public double getNumberValue() throws XMLException;
	
	/**
	 * Converts the current result to a boolean.
	 * @return
	 * @throws XMLException if the result is not a value or a single node
	 */
	public boolean getBooleanValue() throws XMLException;
	
	/**
	 * Converts the current result to a Date.
	 * @return
	 * @throws XMLException if the result is not a value or a single node
	 */
	public Date getDateValue() throws XMLException;
	
	/**
	 * Get the result as a single node.
	 * @return
	 * @throws XMLException if the result is not a single node
	 */
	public Object getSingleNode() throws XMLException;
	
	public Iterator getNodeIterator();
	public Object[] getNodes();
	
	public Iterator getValueIterator();
	public String[] getValues();
}
