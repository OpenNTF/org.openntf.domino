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
 * Created on May 29, 2005
 * 
 */
package com.ibm.commons.xml.xpath;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XResult;


/**
 * Defines the interface to an object which represents an XPath 1.0 expression which can be evaluated 
 * against a variety of different XML object models. 
 *  
 * @author Philippe Riand
 * @author Mark Wallace
 */
public interface XPathExpression {

    /**
     * Return the XPath expression
     * 
     * @return the XPath expression
     */
    public String getExpression();
    
    /**
     * Return true if this XPath expression is a candidate for simple expression 
     * evaluation and otherwise return false.
     * 
     * @return Return true if this is a simple XPath expression
     */
    public boolean isSimple();
    
    /**
     * Check is an XPath is absolute.
     * An absolute XPath is that doesn't case about the execution context, but
     * start from the root element (like the Document for DOM). The expression 
     * generally starts with a leading '/', like /A/B.
     * 
     * @return Return true if this is a simple XPath expression
     */
    public boolean isFromRoot();
    
    /**
     * Return true if the node is read only data.
     * 
     * @return true if the node is read only data.
     */
    public boolean isReadOnly(Object node);
    
    /**
     * Select all nodes that are selectable by this XPath expression. 
     * If multiple nodes match, multiple nodes will be returned. 
     * 
     * @param node the node or nodelist object for evaluation.
     * @param namespaceContext the namespaceContext to use for the evaluation
     * 
     * @return the node-set of all items selected by this XPath expression.
     */
    public XResult eval(Object node, NamespaceContext namespaceContext) throws XPathException;
    
    /**
     * Creates a new node by this XPath expression
     * 
     * @param node the node, node-set or Context object for insert
     * @return the inner most created node
     * @throws XPathException
     */
    public Object createNodes(Object node, NamespaceContext namespaceContext) throws XPathException;

    /**
     * Set the value of the node represented by this XPath expression, relative 
     * to the specified data context. 
     *
     * @param the node, node-set or Context object for update
     * @param the new value to set
     */
    public void setValue(Object node, Object value, NamespaceContext namespaceContext, boolean autoCreate) throws XPathException;
    
    /**
     * Return the type of the node represented by this XPath expression, relative 
     * to the current data node.
     * 
     * @return Type of the node.
     */
    public Class getType(Object node) throws XPathException;
    
    /**
     * @param node is valid object for evaluation
     * @return true if node is of the correct type for evaluation
     */
    public boolean isValid(Object node);
    
    /**
     * Return true if the expression supports using an XPath context.
     * 
     * @return
     */
    public boolean supportsXPathContext();
    
    /**
     * Push the current expression as an XPath context on the specified node.
     * @param node
     */
    public void pushXPathContext(Object node) throws XPathException;
    
    /**
     * Pop the current XPath expression from the specified node.
     * 
     * @param node
     * @return
     * @throws XPathException
     */
    public void popXPathContext(Object node) throws XPathException;
    
}
