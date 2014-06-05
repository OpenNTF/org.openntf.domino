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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XPathContext;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.XResultUtils;
import com.ibm.commons.xml.xpath.AbstractSimpleExpression;
import com.ibm.commons.xml.xpath.NodeListImpl;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.part.AttributePart;
import com.ibm.commons.xml.xpath.part.IndexedElementPart;
import com.ibm.commons.xml.xpath.part.Part;

/**
 * @author Mark Wallace
 * @author Eugene Konstantinov
 * @author Philippe Riand
 */

public class XmlSimpleExpression extends AbstractSimpleExpression {

    public XmlSimpleExpression(String expression, boolean isFromRoot,
            Part[] parts) {
        super(expression, isFromRoot, parts);
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
     * @see com.ibm.commons.xml.xpath.AbstractSimpleExpression#eval(java.lang.Object, com.ibm.commons.xml.NamespaceContext)
     */
    protected XResult doEval(Object node, NamespaceContext namespaceContext) throws XPathException {
        if (!isFromRoot() && node instanceof Document) {
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
            throw new XPathException(new NullPointerException("Cannot evaluate an XPath on a null object")); // $NLS-XmlSimpleExpression.CannotevaluateanXPathonanullobjec-1$
        }
        return super.doEval(node, namespaceContext);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.commons.xml.xpath.AbstractSimpleExpression#setValue(java.lang.Object, java.lang.Object, com.ibm.commons.xml.NamespaceContext, boolean)
     */
    protected  void doSetValue(Object node, Object value, NamespaceContext namespaceContext, boolean autoCreate) throws XPathException {
        if (node instanceof Document) {
            Document document = (Document)node;
            XPathContext pathContext = DOMUtil.getXPathContext(document);
            if (pathContext != null) {
                node = pathContext.getContextNodes();
                
                // create the nodes if auto creating
                if (node == null && autoCreate) {
                    try {
                        pathContext.createNodes();
                        node = ((NodeList)pathContext.getContextNodes()).item(0);
                    }
                    catch (XMLException xe) {
                        throw new XPathException(xe);
                    }
                }
            }
        }
        super.doSetValue(node, value, namespaceContext, autoCreate);
    }

    /* (non-Javadoc)
     * @see com.ibm.xfaces.xpath.XPathExpression#isReadOnly(java.lang.Object)
     */
    public boolean isReadOnly(Object node) {
        return false;
    }

    public NamespaceContext resolveNamespaceContext(Object node, NamespaceContext ns) {
        if(node instanceof Node) {
            return Utils.resolveNamespaceContext((Node)node,ns);
        }
        if(node instanceof NodeList) {
            NodeList l = (NodeList)node;
            if(l.getLength()>0) {
                return Utils.resolveNamespaceContext(l.item(0),ns);
            }
        }
        return null;
    }
    
    public Object getRootNode(Object node) {
        if(node instanceof Node) {
            Node n = (Node)node;
            return DOMUtil.getOwnerDocument(n);
        }
        if(node instanceof NodeList) {
            NodeList nl = (NodeList)node;
            if(nl.getLength()>0) {
                return DOMUtil.getOwnerDocument(nl.item(0));
            }
        }
        return node;
    }

    /**
     * Creates a new node
     * 
     * @param node
     *            the parent node for the node beign created
     * @param part
     *            the part of the XPath expression that describes the created
     *            node
     * 
     * @return a created node or <code>null</code> if an event of failure
     * @see AbstractSimpleExpression#createPart(Object, Part)
     */
    protected Object createPart(Object node, Part part, NamespaceContext namespaceContext) throws XPathException {
        if (node instanceof NodeListImpl) {
            NodeListImpl nodeList = (NodeListImpl)node;
            if (nodeList.getLength() == 1) {
                node = nodeList.get(0);
            }
        }
        if (node == null || !(node instanceof Node)) {
            throw new XPathException(null,"Error creating part:" + part.toString() // $NLS-XmlSimpleExpression.ErrorcreatePART-1$
                    + " Node must be of the type " + Node.class // $NLS-XmlSimpleExpression.Nodemustbeofthetype-1$
                    + " but it was :" // $NLS-XmlSimpleExpression.BUTitwas-1$
                    + ((node == null) ? null : node.getClass()));
        }
        Node nodeObj = (Node) node;
        
        Document document = DOMUtil.getOwnerDocument(nodeObj);
        Node newNode = null;
        if (part instanceof AttributePart) {
            if (StringUtil.isEmpty(part.getPrefix())) {
                newNode = document.createAttribute(part.getName());
                nodeObj.getAttributes().setNamedItem(newNode);
            } else {
                String nsURI = getNamespaceURI(namespaceContext, part.getPrefix());
                newNode = document.createAttributeNS(nsURI, part.getName());
                newNode.setPrefix(part.getPrefix());
                nodeObj.getAttributes().setNamedItem(newNode);
                setNamespaceAttr(newNode);
            }
        } else {
            if (StringUtil.isEmpty(part.getPrefix())) {
                newNode = document.createElement(part.getName());
                nodeObj.appendChild(newNode);
            } else {
                String nsURI = getNamespaceURI(namespaceContext, part.getPrefix());
                newNode = document.createElementNS(nsURI, part.getName());
                newNode.setPrefix(part.getPrefix());
                nodeObj.appendChild(newNode);
                setNamespaceAttr(newNode);
            }
        }

        return newNode;
    }

    /**
     * Removes node from its parent
     * 
     * @param node
     *            the node to be removed
     * @param part
     *            not used
     * @see AbstractSimpleExpression#deletePart(Object, Part)
     */
    protected void deletePart(Object node, Part part) {
        if (node instanceof NodeListImpl) {
            NodeListImpl nodeList = (NodeListImpl)node;
            if (nodeList.getLength() == 1) {
                node = nodeList.get(0);
            }
        }
        if (node instanceof Node) {
            Node nodeObj = (Node) node;
            Node parentNode = nodeObj.getParentNode();
            if (parentNode != null) {
                parentNode.removeChild(nodeObj);
            }
        }
    }

    /**
     * Evaluates the part of XPath expression
     * 
     * @param node
     *            evaluation starts from. It can be either a Node or a NodeListImpl
     * @param part
     *            not used
     * @see AbstractSimpleExpression#evaluatePart(Object, Part)
     */
    protected Object evaluatePart(Object node, Part part, NamespaceContext namespaceContext) throws XPathException {
        if(node==null) {
            return null;
        }
        
        // In case of a list, then applies the part on each element
        if( node instanceof NodeListImpl ) {
            NodeListImpl result = new NodeListImpl(16);
            for (Iterator iter = ((NodeListImpl) node).iterator(); iter.hasNext();) {
                Object res = evaluatePart(iter.next(), part, namespaceContext);
                if (res instanceof NodeListImpl) {
                    result.addAll((NodeListImpl) res);
                } else if (res != null) {
                    result.add(res);
                }
            }
            return (result.isEmpty()) ? null : result;
        }

        Node nodeObj = (Node) node;
        
        if (part.getName().equals(".")) {
            return nodeObj;
        }
        
        if (part.getName().equals("..")) {
            return nodeObj.getParentNode();
        }
        
        if (part instanceof AttributePart) {
            if (node instanceof Element) {
                if (StringUtil.isEmpty(part.getPrefix())) {
                    return ((Element)node).getAttributeNode(part.getName());
                } else {
                    String nsURI = getNamespaceURI(namespaceContext,part.getPrefix());
                    return ((Element)node).getAttributeNodeNS(nsURI, part.getName());
                }
            }
            return null;
        }

        // process child nodes
        if (node instanceof Document) {
            Document doc = (Document) node;
            Element elem = doc.getDocumentElement();
            if (elem != null && equalsName(namespaceContext, part, elem)) {
                return elem;
            }
            return null;
        }
        
        int index = 0;
        if (part instanceof IndexedElementPart) {
            index = ((IndexedElementPart) part).getIndex();
            if (index < 1) {
                String message = "Invalid predicate {0}, XPath predicates are 1-based."; // $NLS-XmlSimpleExpression.Invalidpredicate0XPathpredicatesa-1$
                message = StringUtil.format(message, new Object[] { Integer.valueOf(index) });
                throw new IllegalArgumentException(message);
            }
        }

        NodeList content = nodeObj.getChildNodes();
        Node resultObject = null;
        NodeListImpl resultList = null;
        int found = 0;
        int len = content.getLength();
        for (int i = 0; i < len; i++) {
            Node childNode = content.item(i);
            if (equalsName(namespaceContext, part, childNode)) {
                found++;
                if(index==0 || index==found) {
                    if(resultObject==null) {
                        resultObject = childNode; 
                    } else {
                        if(resultList==null) {
                            resultList = new NodeListImpl();
                            resultList.add(resultObject);
                        }
                        resultList.add(childNode);
                    }
                }
            }
        }
        if (resultList!=null) {
            return resultList;
        }
        return resultObject;
    }

    /**
     * Sets up a node's value. If node is the element type it will atempt to set
     * the TEXT node value instead. Otherwise, it sets up the value of the node
     * itself
     * 
     * @param node
     *            to be set with a new value
     * @param part
     *            not used
     * @param value
     *            String representing a new value
     * 
     * @see AbstractSimpleExpression#setPart(Object, Part, Object)
     */
    protected void setNodeValue(Object node, Object value) throws XPathException {
        if (node == null) {
            throw new XPathException(null, "Cannot set a value on an null XPath result" ); // $NLS-XmlSimpleExpression.CannotsetavalueonannullXPathresul-1$
        }
        
        if (node instanceof NodeListImpl) {
            NodeListImpl nodeList = (NodeListImpl)node;
            if (nodeList.getLength() == 1) {
                node = nodeList.get(0);
            }
            else {
                throw new XPathException(null, "Cannot set a value on a list of elements" ); // $NLS-XmlSimpleExpression.Cannotsetavalueonalistofelements-1$
            }
        }
        
        Node nodeObj = (Node) node;
        String strValue = (value == null) ? "" : value.toString(); //$NON-NLS-1$
        DOMUtil.setTextValue(nodeObj, strValue);
    }
    
    /**
     * Returns <code>true</code> if node is of the correct type for
     * evaluation. The valid types are:
     * <p>
     * <ul>
     * <li>org.w3c.dom.Node</li>
     * <li>NodeListImpl</li>
     * </ul>
     * 
     * 
     * @param node
     *            the node to be check for validity
     * @return true if node of the correct type or false otherwise
     * @see com.ibm.xfaces.xpath.XPathExpression#isValid(java.lang.Object)
     */
    public boolean isValid(Object node) {
        if ( (node instanceof Node) || (node instanceof NodeListImpl)) {
            return true;
        }
        return false;
    }

    /**
     * Returns wrapper over the node that knows how to extract value.
     * <p>
     * This method should return a <code>com.ibm.xfaces.xpath.XNodeList</code>if node is instance of <code>org.w3c.dom.Node</code>
     * or else it returns the node back presuming the node is already wrapped up e.g. <code>org.apache.xpath.objects.XObject</code>
     * @param node
     *            node which is being wrapped up
     * @return wrapper object
     * @see com.ibm.xfaces.xpath.XPathExpression#valueOf(Object)
     * @see com.ibm.xfaces.xpath.xml.NodeListImpl
     */
    public XResult wrapUp(Object node) throws XPathException {
        if(node == null ) {
            return XResultUtils.emptyResult;
        }
        
        if (node instanceof Node){
            return new XResultUtils.XMLNode((Node)node);
        }
        
        if( node instanceof NodeList ) {
            NodeList nl = (NodeList)node;
            int len = nl.getLength();
            if(len==0) {
                return XResultUtils.emptyResult;
            } else if(len==1) {
                return new XResultUtils.XMLNode(nl.item(0));
            } else {
                return new XResultUtils.XMLNodeList((NodeList)node);
            }
        }
        

        throw new XPathException(null,"Internal error while executing simple path. Result:",node.toString()); // $NLS-XmlSimpleExpression.Internalerrorwhileexecutingsimple-1$
    }

    //
    // Utility methods
    //
    
    protected void setNamespaceAttr(Node node) {
        if (node == null) {
            return;
        }
        try {
            Document document = node.getOwnerDocument();
            Element docElement = document.getDocumentElement();
            if (docElement == null) {
                return;
            }
            
            // add namespace ffrom new node
            String prefix = node.getPrefix();
            String namespaceURI = node.getNamespaceURI();
            String qualifiedName = "xmlns:" + prefix; // $NON-NLS-1$
            
            if (!docElement.hasAttribute(qualifiedName)) {
                Attr attr = document.createAttributeNS(
                         "http://www.w3.org/2000/xmlns/", //$NON-NLS-1$
                         qualifiedName); //$NON-NLS-1$
                attr.setNodeValue(namespaceURI);
                docElement.setAttributeNode(attr);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected Document getDocument(Object node) throws XMLException {
        if (node instanceof Document) {
            return (Document)node;
        }
        return ((Node)node).getOwnerDocument();
    }

    protected boolean equalsName(NamespaceContext namespaceContext, int idx, Node elem) {
        if (idx >= getPartCount())
            return false;
        else
            return equalsName(namespaceContext,getPart(idx), elem);
    }

    protected static String getNamespaceURI(NamespaceContext namespaceContext, String nsPrefix) {
        if (namespaceContext != null) {
            return namespaceContext.getNamespaceURI(nsPrefix);
        }
        return null;
    }

    protected static boolean equalsName(NamespaceContext namespaceContext, Part part, Node elem) {
        if (elem == null) {
            return false;
        } 
        
        String localName = elem.getLocalName();
        if (localName == null) {
            return StringUtil.equals(part.getName(), elem.getNodeName());
        }
        if (StringUtil.equals(part.getName(), localName)) {
            String elemNsURI = elem.getNamespaceURI();
            if (namespaceContext != null
                    && !StringUtil.isEmpty(part.getPrefix())) {
                String partNsURI = getNamespaceURI(namespaceContext,part.getPrefix());
                return StringUtil.equals(partNsURI, elemNsURI);
            } else {
                return true;
            }
        }
        return false;
    }
}
