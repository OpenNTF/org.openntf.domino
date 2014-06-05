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

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.NodeListImpl;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;


/**
 * XPath context associated to an XML document.
 */
public class XPathContext {

    private Document _doc;
    private XPathContext _parent;
    
    private XPathExpression _xPathExpression;
    private NodeListImpl _contextNodes;
    private boolean _isAbsolute;

    private Node _filteredContextNodes;
    
    public XPathContext(Document doc, XPathContext parent, String path) throws XMLException {
        _doc = doc;
        _parent = parent;
        _xPathExpression = DOMUtil.createXPath(path);
        _isAbsolute = _xPathExpression.isFromRoot();
        compute();
    }
    
    public Document getDocument() {
        return _doc;
    }
    
    public String getExpression() {
        return _xPathExpression.getExpression();
    }
    
    public void compute() throws XPathException {
        // compute the result of this xpath against the parent results, or the main document
        XResult r = null;
        if(!_isAbsolute && _parent!=null) {
            NodeListImpl parentNodes = _parent._contextNodes;
            if (parentNodes == null) {
                return;
            }
            r = _xPathExpression.eval(parentNodes, null);
        } 
        else {
            if (_doc.getDocumentElement() == null) {
                return;
            }
            r = _xPathExpression.eval(_doc, null);
        }
        
        // and fill the current nodes
        for( Iterator it=r.getNodeIterator(); it.hasNext(); ) {
            if(_contextNodes==null) {
                _contextNodes = new NodeListImpl();
            }
            _contextNodes.add(it.next());
        }
    }
    
    public XPathContext getParent() {
        return _parent;
    }
    
    public Object getContextNodes() {
        if (_filteredContextNodes!=null) {
            return _filteredContextNodes;
        }
        return _contextNodes;
    }

    public int getContextNodesLength() {
        return (_contextNodes == null) ? 0 : _contextNodes.getLength();
    }

    public void setFilterIndex(int index) throws XMLException {
        if(index<0) {
            this._filteredContextNodes = null;
        } else {
            index--; // Make it zero based 
            if(index<0) {
                throw new XMLException(null,"XPath indexes start at 1"); // $NLS-XPathContext.XPathindexesstartat1-1$
            }
            if(_contextNodes==null) {
                throw new XMLException(null,"Invalid XPath context: context is empty"); // $NLS-XPathContext.InvalidXPathcontextcontextisempty-1$
            }
            if(index>=_contextNodes.getLength()) {
                throw new XMLException(null,"Invalid XPath context: index {0} too big",Integer.toString(index)); // $NLS-XPathContext.InvalidXPathcontextindex0toobig-1$
            }
            this._filteredContextNodes = _contextNodes.item(index);
        }
    }
    
    public void setFilterNode(Node node) {
        this._filteredContextNodes = node;
    }
    
    public Node getUniqueContextNode() throws XMLException {
        if(_filteredContextNodes!=null) {
            return _filteredContextNodes;
        }
        if(_contextNodes==null) {
            throw new XMLException(null,"Invalid XPath context: context is empty"); // $NLS-XPathContext.InvalidXPathcontextcontextisempty.1-1$
        }
        if(_contextNodes.getLength()>1) {
            throw new XMLException(null,"Invalid XPath context: context contains more than one node"); // $NLS-XPathContext.InvalidXPathcontextcontextcontain-1$
        }
        return _contextNodes.item(0);
    }
    
    public void createNodes() throws XMLException {
        if(_contextNodes==null) {
            Node ctx = _doc;

            // Ensure that the parent is created
            // Uniquely if we are not absolute
            if(!_isAbsolute && _parent!=null) {
                _parent.createNodes();
                ctx = _parent.getUniqueContextNode();
            }
            
            // And evaluate the XPath
            Object o = _xPathExpression.createNodes(ctx, null);
            if(o!=null) {
                _contextNodes = new NodeListImpl();
                _contextNodes.add(o);
            }
        }
    }
}
