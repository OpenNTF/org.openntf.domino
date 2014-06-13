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

import java.util.ArrayList;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.SystemCache;
import com.ibm.commons.xml.xpath.part.AttributePart;
import com.ibm.commons.xml.xpath.part.ElementPart;
import com.ibm.commons.xml.xpath.part.IndexedElementPart;
import com.ibm.commons.xml.xpath.part.Part;

/**
 * @author Mark Wallace
 * @author Philippe Riand
 */
abstract public class AbstractExpressionFactory implements XPathExpressionFactory {
    
    protected SystemCache _expressionCache;
    
    public static final String FORBIDDEN_IN_CREATE = "()[]{}*=";    //$NON-NLS-1$
    public static final String FORBIDDEN_IN_SIMPLE = "(){}*=";      //$NON-NLS-1$
    
    /**
     * Construct a AbstractXPathExpressionFactory.
     */
    public AbstractExpressionFactory() {
        this(null);
    }

    /**
     * Construct a AbstractXPathExpressionFactory and create a cache with the specified name
     * and a maximum size of 400.
     * 
     * @param name
     */
    public AbstractExpressionFactory(String name) {
        this(name, 400);
    }

    /**
     * Construct a AbstractXPathExpressionFactory and create a cache with the specified name
     * and the specified maximum size.
     * 
     * @param name
     * @param maxSize
     */
    public AbstractExpressionFactory(String name, int maxSize) {
        name = (name == null) ? getClass().getName() : name;
        _expressionCache = new SystemCache(name, 400, "ibm.xpath.cachesize"); // $NON-NLS-1$
    }
    
    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.XPathExpressionFactory#createExpression(java.lang.Object, java.lang.String, boolean)
     */
    public XPathExpression createExpression(Object data, String expression, boolean useCache) throws XPathException {
        // try the cache
        XPathExpression pathExpression;
        if(useCache) {
            pathExpression = (XPathExpression)_expressionCache.get(expression);
            if (pathExpression != null) {
                return pathExpression;
            }
        }

        // try to create a simple xpath if possible
        pathExpression = createSimpleExpression(expression);
        if (pathExpression == null) {
            // not a simple xpath so create a complex one
            pathExpression = createComplexExpression(expression);
        }

        // cache the created xpath expression
        if(useCache) {
            cacheExpression(expression, pathExpression);
        }

        return pathExpression;
    }

    /**
     * Create a simple XPathExpression object for the specified expression. This uses the
     * rules for identifying a simple Xpath expression:
     * 
     * <li>the expression does not contain any of the following characters (){}*=</li>
     * <li>the expression does not start with //</li>
     * <li>each part of the expression starts with /</li>
     * <li>each part starts with a valid XML start character</li>
     * <li>each part contains only valid XML characters</li>
     * 
     * @param expression
     * @return
     */
    protected XPathExpression createSimpleExpression(String expression) throws XPathException {
        // reserved key words
        if ("true".equals(expression)) { //$NON-NLS-1$
            return null;
        }
        if ("false".equals(expression)) { //$NON-NLS-1$
            return null;
        }

        // first check for forbidden characters
        char[] chars = expression.toCharArray();
        for (int index=0; index < chars.length; index++) {
            if (FORBIDDEN_IN_SIMPLE.indexOf(chars[index]) != -1) {
                return null;
            }
        }

        // next check that each part is simple
        boolean isFromRoot = false;
        ArrayList partList = new ArrayList();
        for (int index=0; index < chars.length; ) {
            // each part should start with a leading '/'
            // except the first one...
            if (index == 0) {
                if (chars[index]=='/') {
                    isFromRoot = true;
                    index++;
                    if (chars.length> index && chars[index]=='/' ) { // case of '//'
                        return null;
                    }
                }
            } 
            else {
                if (chars[index] != '/') {
                    return null;
                }
                index++;
            }
            
            // check if the path points to an attribute
            if (index < chars.length && chars[index] == '@') {
                index++;
                int start = index;
                // the first character must be valid
                if (index >= chars.length || !isXMLNameStartCharacter(chars[index])) {
                    return null;
                }
                index++;
                // get all the other characters
                while (index < chars.length && isXMLNameCharacter(chars[index])) {
                    index++;
                }
                String attrName = new String(chars, start, index-start);
                // and compose the attribute part
                AttributePart part = new AttributePart(attrName);
                partList.add(part);
                continue;
            }

            // else, check if it is an element
            if (index < chars.length && isXMLNameStartCharacter(chars[index])) {
                int start = index++;
                // get all the other characters
                while (index < chars.length && isXMLNameCharacter(chars[index]) && chars[index] != ':') {
                    index++;
                }
                String nsPrefix = null;
                String eltName = new String(chars, start, index-start);
                // check if it was a namespace prefix
                if (index < chars.length && chars[index]==':' ) {
                    index++;
                    nsPrefix = eltName;
                    start = index;
                    if (index >= chars.length || !isXMLNameStartCharacter(chars[index]) || 
                        chars[index]==':') {
                        // if "::" or not valid namespace character then it is not a simple path
                        return null;
                    }
                    index++;
                    // Get all the other characters
                    while (index < chars.length && isXMLNameCharacter(chars[index])) {
                        index++;
                    }
                    eltName = new String(chars, start, index-start);
                }
                // check if the element is indexed
                if (index<chars.length && chars[index] == '[') {
                    index++;
                    start = index;
                    while (index<chars.length && chars[index]!=']') {
                        index++;
                    }
                    String strIdx = (new String(chars, start, index-start)).trim();
                    if( StringUtil.isEmpty(strIdx) ) {
                        return null; // throw new InvalidXPath()
                    }
                    index++; // skip ']'
                    try {
                        int idx = Integer.parseInt(strIdx);
                        // and compose the element part
                        IndexedElementPart part = new IndexedElementPart(nsPrefix, eltName, idx);
                        partList.add(part);
                    } catch( Exception e ) {
                        return null;
                    }
                } 
                else {
                    // and compose the element part
                    ElementPart part = new ElementPart(nsPrefix, eltName);
                    partList.add(part);
                }
                continue;
            } 
            else if (index<chars.length && chars[index] == '.') {
                index++;
                if (index<chars.length && chars[index] == '.') {
                    index++;
                    // and compose the element part
                    ElementPart part = new ElementPart(null, "..");
                    partList.add(part);
                } 
                else {
                    // and compose the element part
                    ElementPart part = new ElementPart(null, ".");
                    partList.add(part);
                }
                continue;
            }

            // it is an error!
            if (index < chars.length)
                return null;
        }

        // create the simple xpath expression object
        Part[] parts = (Part[])partList.toArray(new Part[partList.size()]);
        return createSimpleExpression(expression, isFromRoot, parts);
    }
    
    /**
     * Create a simple XPathExpression object for the specified properties.
     * 
     * @param expression
     * @param isFromRoot
     * @param parts
     * @return
     */
    abstract protected XPathExpression createSimpleExpression(String expression, boolean isFromRoot, Part[] parts) throws XPathException;
    
    /**
     * Create a complex XPathExpression object for the specified expression.
     * This creates the expression using whateverthe underlying XPath engine is
     * appropriate for this expression.
     * 
     * @param expression
     * @return
     */
    abstract protected XPathExpression createComplexExpression(String expression) throws XPathException ;
    
    /**
     * This is a utility function for determining whether a specified 
     * character is a legal name start character according to the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML name start compliance.
     * @return <code>boolean</code> true if it's a name start character, false otherwise.
     */
    abstract protected boolean isXMLNameStartCharacter(char ch);

    /**
     * This is a utility function for determining whether a specified 
     * character is a name character according to the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML name compliance.
     * @return <code>boolean</code> true if it's a name character, false otherwise.
     */
    abstract protected boolean isXMLNameCharacter(char ch);

    /**
     * Cache the specified XPathExpression object as long as it is not a simple expression
     * with only one part.
     */
    protected void cacheExpression(String expression, XPathExpression pathExpression) {
        if (pathExpression == null)
            return;
        
// PHIL: cache them anyway
// Indexed XPath were heavily used in FlowBuilder, but Designer is not generating them anymore as
// it is now using XPathContext.        
//        if (pathExpression.isSimple()) {
//            AbstractSimpleExpression simpleExpression = (AbstractSimpleExpression)pathExpression;
//            if (!simpleExpression.isFromRoot() && (simpleExpression.getPartCount() < 2))
//                return;
//            // Only cache non indexed path
//            // This is because a lot of XPath in UI are build with such an index when applied in a 
//            // repeat context.
//            if(expression.indexOf('[')>=0) {
//              return;
//            }
//        }

        // cache the expression
        _expressionCache.put(expression, pathExpression);
    }
}
