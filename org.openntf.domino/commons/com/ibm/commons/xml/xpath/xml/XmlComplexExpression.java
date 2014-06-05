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
 * Created on June 14, 2005
 * 
 */
package com.ibm.commons.xml.xpath.xml;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XPathContext;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.drivers.XMLParserDriver;
import com.ibm.commons.xml.xpath.AbstractExpression;
import com.ibm.commons.xml.xpath.XPathException;

/**
 * @author Mark Wallace
 * @author Eugene Konstantinov
 */
public class XmlComplexExpression extends AbstractExpression {

    private XMLParserDriver domDriver;
    private Object compiledXPath;
    
    /**
     * @param expression
     */
    public XmlComplexExpression(XMLParserDriver domDriver, String expression, Object compiledXPath) {
        super(expression);
        this.domDriver = domDriver;
        this.compiledXPath = compiledXPath;
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.XPathExpression#supportsXPathContext()
     */
    public boolean supportsXPathContext() {
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.XPathExpression#pushXPathContext(java.lang.Object)
     */
    public void pushXPathContext(Object node) throws XPathException {
        try {
            Document document = getDocument(node);
            DOMUtil.pushXPathContext(document, getExpression());
        }
        catch (XMLException xe) {
            throw new XPathException(xe);
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.XPathExpression#popXPathContext(java.lang.Object)
     */
    public void popXPathContext(Object node) throws XPathException {
        try {
            Document document = getDocument(node);
            XPathContext context = DOMUtil.getXPathContext(document);
            if (context != null && context.getExpression().equals(getExpression())) {
                DOMUtil.popXPathContext(document);
            }
        }
        catch (XMLException xe) {
            throw new XPathException(xe);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.AbstractExpression#resolveNamespaceContext(java.lang.Object, com.ibm.commons.xml.NamespaceContext)
     */
    public NamespaceContext resolveNamespaceContext(Object node, NamespaceContext ns) {
        return Utils.resolveNamespaceContext((Node)node, ns);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.XPathExpression#isValid(java.lang.Object)
     */
    public boolean isValid(Object node) {
        return false;
    }

    public Object valueOf(Object node) {
        return node;
    }

    /* (non-Javadoc)
     * @see com.ibm.xfaces.xpath.XPathExpression#isReadOnly(java.lang.Object)
     */
    public boolean isReadOnly(Object node) {
        return false;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xfaces.xpath.XPathExpression#isSimple()
     */
    public boolean isSimple() {
        return false;
    }

    public boolean isFromRoot() {
        return getExpression().startsWith("/");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xfaces.xpath.XPathExpression#eval(java.lang.Object)
     */
    protected XResult doEval(Object node, NamespaceContext namespaceContext) throws XPathException {
        if (node instanceof Document) {
            Document document = (Document)node;
            XPathContext pathContext = DOMUtil.getXPathContext(document);
            if (pathContext != null) {
                node = pathContext.getContextNodes();
                // create the context nodes if they don't exist
                if (node == null) {
                	try {
                		pathContext.createNodes();
                	}
                	catch (XMLException xe) {
                		throw new XPathException(xe);
                	}
                    node = pathContext.getContextNodes();
                }
            }
        }
        if (node == null) {
            throw new XPathException(new NullPointerException("Cannot evaluate an XPath on a null object")); // $NLS-XmlComplexExpression.CannotevaluateanXPathonanullobjec-1$
        }
        // WARN: Xerces is having Node also implementing NodeList
        // We first check Node to be sure.
        if (node instanceof Node) {
            Node nodeObj = (Node)node;
            namespaceContext = resolveNamespaceContext(nodeObj,namespaceContext);
            return domDriver.evaluateXPath(nodeObj,compiledXPath,namespaceContext);
        }
        if (node instanceof NodeList) {
            NodeList nodeList = (NodeList)node;
            namespaceContext = resolveNamespaceContext(nodeList.item(0),namespaceContext);
            XResult r = domDriver.evaluateXPath(nodeList,compiledXPath,namespaceContext);
            return r;
        }
        throw new XPathException(null,"Try to evaluate an XPath on a object that is not a node or a node list"); // $NLS-XmlComplexExpression.TrytoevaluateanXPathonaobjectthat-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xfaces.xpath.XPathExpression#createNodes(java.lang.Object,
     *      java.lang.Object)
     */
    protected Object doCreateNodes(Object node, NamespaceContext namespaceContext)
            throws XPathException {     
        throw new XPathException(null,"CREATE nodes not supported by complex expressions."); // $NLS-XmlComplexExpression.CREATEnodesnotsupportedbycomplexe-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xfaces.xpath.XPathExpression#setValue(java.lang.Object,
     *      java.lang.Object)
     */
    protected void doSetValue(Object node, Object value, NamespaceContext nsContext, boolean autoCreate) throws XPathException {
        if (node == null) {
            throw new XPathException(new NullPointerException("Cannot set a value on a null object")); // $NLS-XmlComplexExpression.Cannotsetavalueonanullobject-1$
        }
        if (!(node instanceof Node)) {
            throw new XPathException(null,"Try to evaluate to set a value on a object that is not a node"); // $NLS-XmlComplexExpression.Trytoevaluatetosetavalueonaobject-1$
        }

        Node nodeObj = (Node)node;
        nsContext = resolveNamespaceContext(nodeObj,nsContext);
        
        XResult r = domDriver.evaluateXPath(nodeObj,compiledXPath,nsContext);
        if(r.isEmpty()) {
            throw new XPathException(null,"Cannot create XPath {0}",getExpression()); // $NLS-XmlComplexExpression.CannotcreateXPath0-1$
        }
        if(r.isValue()) {
            throw new XPathException(null,"Cannot set a value on a value result, XPath={0}",getExpression()); // $NLS-XmlComplexExpression.CannotsetavalueonavalueresultXPat-1$
        }
        String strValue = Utils.getAsString(value);
        for( Iterator it=r.getNodeIterator(); it.hasNext(); ) {
            Object n = (Object)it.next();
            if( n instanceof Node ) {
                DOMUtil.setTextValue((Node)n, strValue);
            }
        }
    }

    
    protected Document getDocument(Object node) throws XMLException {
        if (node instanceof Document) {
            return (Document)node;
        }
        return ((Node)node).getOwnerDocument();
    }
    
}
