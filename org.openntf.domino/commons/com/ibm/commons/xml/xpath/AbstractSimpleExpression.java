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
import java.util.Iterator;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.xpath.part.Part;
import com.ibm.commons.xml.xpath.xml.Utils;

/**
 * @author Mark Wallace
 * @author Eugene Konstantinov
 * @author Philippe Riand
 */
abstract public class AbstractSimpleExpression extends AbstractExpression {

    protected boolean _isFromRoot;

    protected Part[] _parts;
    
    /**
     * Construct a SimpleXPathExpression with the specified properties
     * 
     * @param expression
     * @param fromRoot
     * @param parts
     */
    public AbstractSimpleExpression(String expression, boolean isFromRoot,
            Part[] parts) {
        super(expression);

        _isFromRoot = isFromRoot;
        _parts = parts;
    }

    /**
     * Return true if this expression is relative to the root, otherwise return
     * false.
     * 
     * @return true if this expression is relative to the root
     */
    public boolean isFromRoot() {
        return _isFromRoot;
    }

    /**
     * Return the count of Part objects associated with this
     * SimpleXPathExpression.
     * 
     * @return the count of Part objects
     */
    public int getPartCount() {
        return (_parts != null) ? _parts.length : 0;
    }

    /**
     * Return the nth Part object associated with this SimpleXPathExpression.
     * 
     * @param index
     * @return the nth Part object
     */
    public Part getPart(int index) {
        return (_parts != null) ? _parts[index] : null;
    }

    //
    // Implementation of XPathExpression
    //

    /**
     * 
     * @see com.ibm.xfaces.xpath.XPathExpression#isSimple()
     */
    public boolean isSimple() {
        return true;
    }

    /**
     * Evaluates this simple xpath expression.
     * <p>
     * The algorithm for evaluation iteratively step through each part of the
     * xpath calling evaluatePart()
     * 
     * @param node
     *            the node evaluation start from
     * @see com.ibm.xfaces.xpath.XPathExpression#isSimple()
     * @see #evaluatePart(Object, Part)
     */
    protected XResult doEval(Object node, NamespaceContext namespaceContext) throws XPathException {
        try {
            if(isFromRoot()) {
                node = getRootNode(node);
            }
            
            namespaceContext = resolveNamespaceContext(node,namespaceContext);
            
            int count = (_parts != null) ? _parts.length : 0; 

            if (count== 0 && StringUtil.isEmpty(getExpression())) {
                throw new XPathException(null,
                        "Incorrect XPATH: Empty text was provided"); // $NLS-AbstractSimpleExpression.IncorrectXPATHEmptytextwasprovide-1$
            }

            for(int pos = 0; pos < count && node != null; pos++) {
                node = evaluatePart(node, _parts[pos], namespaceContext);
            }
            return wrapUp(node);
        } catch (Exception e) {
//            System.err.println("Evaluation error for: " + getExpression()
//                    + " Root cause:" + e.getMessage());
            throw new XPathException(e);
        }
    }

    /**
     * Sets up the new value of the node
     * 
     * @param node
     *            the node the new value is being set
     * @param value
     *            the new value for the node
     * @param autoCreate
     *            set to <code>true</code> to indicate that node must be
     *            created if it doesnot exist
     * @see com.ibm.xfaces.xpath.XPathExpression#setValue(Object, Object,
     *      boolean)
     */
    protected void doSetValue(Object node, Object value, NamespaceContext namespaceContext, boolean autoCreate) throws XPathException {
        if(isFromRoot()) {
            node = getRootNode(node);
        }
        Object curObject = node;
        Object prevObject = null;
        try {
            if (!(isValid(node))) {
                throw new XPathException(null,"Incorrect node type for evaluation:" // $NLS-AbstractSimpleExpression.Incorrectnodetypeforevaluation-1$
                        + node.getClass());
            }
            if (getPartCount() == 0 && StringUtil.isEmpty(getExpression())) {
                throw new XPathException(null,
                        "Incorrect XPATH: Empty text was provided"); // $NLS-AbstractSimpleExpression.IncorrectXPATHEmptytextwasprovide.1-1$
            }
            namespaceContext = resolveNamespaceContext(node,namespaceContext);
            
            // evaluate all parts and create any that are missing if
            // auto create has been set to true
            Part currentPart = null;
            int currentPos = 0;
            while (getPartCount() > currentPos) {
                currentPart = getPart(currentPos);
                currentPos++;
                prevObject = curObject;
                curObject = evaluatePart(curObject, currentPart, namespaceContext);

                if (curObject == null) {
                    if (autoCreate) {
                        curObject = createPart(prevObject, currentPart, namespaceContext);
                    } 
                    else {
                        throw new XPathException(null,"Evaluation error for " // $NLS-AbstractSimpleExpression.Evaluationerrorfor.1-1$
                                + currentPart.toString() + " of the XPATH:" // $NLS-AbstractSimpleExpression.oftheXPATH-1$
                                + getExpression());
                    }
                }
                if (!isValid(curObject)) {
                    throw new XPathException(null,
                            "Incorrect node type for evaluation:" // $NLS-AbstractSimpleExpression.Incorrectnodetypeforevaluation.1-1$
                                    + node.getClass());
                }
            }

            String strValue = Utils.getAsString(value);
            setNodeValue(curObject, strValue);
        } 
        catch (Exception e) {
//            System.err.println("Evaluation error for : " + getExpression()
//                    + " Root cause:" + e.getMessage());
            throw new XPathException(e);
        }
    }

    /**
     * Creates node or nodes
     * 
     * @param node
     *            the node the evaluation starts from
     * @return the inner most node created (corresponds to the very last part of
     *         xpath expression) or null
     * @see com.ibm.xfaces.xpath.XPathExpression#doCreateNodes(Object, Object)
     */
    protected  Object doCreateNodes(Object node, NamespaceContext namespaceContext)
            throws XPathException {
        if(isFromRoot()) {
            node = getRootNode(node);
        }

        ArrayList<Object> created = new ArrayList<Object>();
        Object curObject = node;
        try {
            if (!(isValid(node))) {
                throw new XPathException(null,"Incorrect node type for evaluation:" // $NLS-AbstractSimpleExpression.Incorrectnodetypeforevaluation.2-1$
                        + node.getClass());
            }
            if (getPartCount() == 0 && StringUtil.isEmpty(getExpression())) {
                throw new XPathException(null,
                        "Incorrect XPATH: Empty text was provided"); // $NLS-AbstractSimpleExpression.IncorrectXPATHEmptytextwasprovide.2-1$
            }
            namespaceContext = resolveNamespaceContext(node,namespaceContext);
            
            Part currentPart = null;
            int currentPos = 0;
            while (getPartCount() > currentPos) {
                currentPart = getPart(currentPos);
                currentPos++;
                Object evalObject = evaluatePart(curObject, currentPart, namespaceContext);
                if (evalObject == null) {
                    evalObject = createPart(curObject, currentPart, namespaceContext);
                    // keep record created nodes
                    if (evalObject != null) {
                        created.add(evalObject);
                    }
                }
                curObject = evalObject;
                continue;
            }
            return curObject;
        } catch (Exception e) {
            // release any created DataObjects in an event of an error
            for (Iterator<Object> iter = created.iterator(); iter.hasNext();) {
                deletePart(iter.next(), null);
            }
            throw new XPathException(e);
        }
    }

    
    /**
     * Get the root element for a particular node.
     * This method is called by absolute XPath evaluation. At leat, this method should
     * return the node itself if a root node doesn't make sense.
     * @param node
     * @return
     */
    public abstract Object getRootNode(Object node);
    
    /**
     * Creates node for the respective xpath part
     * 
     * @param node
     *            the node that is parent for the newly created node
     * @param part
     *            the part of the xpath that the current node is being created
     * @return the newly create node or <code>null</code>
     */
    abstract protected Object createPart(Object node, Part part, NamespaceContext namespaceContext) throws XPathException;

    /**
     * Sets a new value to a node
     * 
     * @param node
     *            the node whisch value is being set
     * @param part
     *            the part that corresponds to the node which value is being set
     * @param value
     *            the new value for the node
     */
    abstract protected void setNodeValue(Object node, Object value) throws XPathException;

    /**
     * Deletes node
     * 
     * @param node
     *            the node that is being deleted
     * @param part
     *            the part that corresponds to the node
     */
    abstract protected void deletePart(Object node, Part part);

    /**
     * Evaluates the part of xpath expression
     * 
     * @param node
     *            the parent node
     * @param part
     *            the part of the xpath to be evaluated
     * @return the node that corresponds with the part of the xpath
     * @throws XPathException
     */
    abstract protected Object evaluatePart(Object node, Part part, NamespaceContext namespaceContext)
            throws XPathException;

    /**
     * Returns wrapper over the node to extract value of the given node.
     * <p>
     * This wrapper object must override <code>toString()</code>to return
     * <code>String</code> representation of the node's value
     * 
     * @param node
     *            the context node which value we trying to resolve
     * @return the object that wrapps up the node
     */
    abstract public XResult wrapUp(Object node) throws XPathException;

}
