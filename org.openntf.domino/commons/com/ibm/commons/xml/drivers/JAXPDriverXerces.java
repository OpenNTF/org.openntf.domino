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

package com.ibm.commons.xml.drivers;

import javax.xml.transform.TransformerException;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.XResultUtils;
import com.ibm.commons.xml.xpath.NodeListImpl;
import com.ibm.commons.xml.xpath.XPathException;



/**
 * JAXP Driver using Xerces as the XPath engine.
 */
public class JAXPDriverXerces extends AbstractJAXPDriver {
    
    // =========================================================================================
    // XPATH
    // =========================================================================================

    // PHIL: temporary.
    // Not optimized method. Should move to a precompiled XPath object
    public Object createXPath(String xpath) throws XPathException {
        return xpath;
    }
    public XResult evaluateXPath(Node node, Object xpath, NamespaceContext nsContext) throws XPathException {
        try {
            PrefixResolver pr = nsContext!=null ? new NSResolver(nsContext) : null;
            XObject res = XPathAPI.eval(node,(String)xpath,pr);
            return convertResult(res);
        } catch(Exception e) {
            throw new XPathException(e,"Error while evaluating XPath {0}",xpath.toString()); // $NLS-AbstractXercesDriver.ErrorwhileevaluatingXPath0-1$
        }
    }
    public XResult evaluateXPath(NodeList nodeList, Object xpath, NamespaceContext nsContext) throws XPathException {
        try {
            if(nodeList.getLength()>0) {
                XResult result = null; 
                NodeListImpl nodes = null;
                PrefixResolver pr = nsContext!=null ? new NSResolver(nsContext) : null;
                for( int i=0; i<nodeList.getLength(); i++ ) {
                    XObject res = XPathAPI.eval(nodeList.item(i),(String)xpath,pr);
                    switch(res.getType()) {
                        case XObject.CLASS_BOOLEAN: {
                            if(nodes!=null) {
                                throw new XPathException(null,"XPath result cannot contain both values and nodes"); // $NLS-AbstractXercesDriver.AnXPathresultcannotcontainbothval-1$
                            }
                            if(result!=null) {
                                throw new XPathException(null,"XPath exception cannot return multiple values"); // $NLS-AbstractXercesDriver.AnXPathExceptioncannotreturnmulti-1$
                            }
                            result = new XResultUtils.BooleanValue(res.bool());
                        } break;
                        case XObject.CLASS_NUMBER: {
                            if(nodes!=null) {
                                throw new XPathException(null,"XPath result cannot contain both values and nodes"); // $NLS-AbstractXercesDriver.AnXPathresultcannotcontainbothval.1-1$
                            }
                            if(result!=null) {
                                throw new XPathException(null,"XPath exception cannot return multiple values"); // $NLS-AbstractXercesDriver.AnXPathExceptioncannotreturnmulti.1-1$
                            }
                            result = new XResultUtils.NumberValue(res.num());
                        } break;
                        case XObject.CLASS_STRING: {
                            if(nodes!=null) {
                                throw new XPathException(null,"XPath result cannot contain both values and nodes"); // $NLS-AbstractXercesDriver.AnXPathresultcannotcontainbothval.2-1$
                            }
                            if(result!=null) {
                                throw new XPathException(null,"XPath exception cannot return multiple values"); // $NLS-AbstractXercesDriver.AnXPathExceptioncannotreturnmulti.2-1$
                            }
                            result = new XResultUtils.StringValue(res.str());
                        } break;
                        case XObject.CLASS_NODESET: {
                            if(result!=null) {
                                throw new XPathException(null,"XPath result cannot contain both values and nodes"); // $NLS-AbstractXercesDriver.AnXPathresultcannotcontainbothval.3-1$
                            }
                            NodeList nl = res.nodelist();
                            int len = nl.getLength(); 
                            if(len>0) {
                                if(nodes==null) {
                                    nodes = new NodeListImpl();
                                }
                                for( int j=0; j<nl.getLength(); j++ ) {
                                    nodes.add(nl.item(j));
                                }
                            }
                        }
                    }
                }
                if(result!=null) {
                    return result;
                }
                if(nodes!=null) {
                    int len = nodes.getLength();
                    if(len==0) {
                        return XResultUtils.emptyResult;
                    } else if(len==1) {
                        return new XResultUtils.XMLNode(nodes.item(0));
                    } else { 
                        return new XResultUtils.XMLNodeList(nodes);
                    }
                }
            }
            return XResultUtils.emptyResult;
        } catch(Exception e) {
            throw new XPathException(e,"Error evaluating XPath {0}",xpath.toString()); // $NLS-AbstractXercesDriver.ErrorwhileevaluatingXPath0.1-1$
        }
    }

    private static class NSResolver implements PrefixResolver {
        NamespaceContext nsContext;
        NSResolver(NamespaceContext nsContext) {
            this.nsContext = nsContext;
        }
        public String getNamespaceForPrefix(String prefix) {
            return nsContext.getNamespaceURI(prefix);
        }
        public String getNamespaceForPrefix(String prefix, org.w3c.dom.Node context) {
            return nsContext.getNamespaceURI(prefix);
        }
        public boolean handlesNullPrefixes() {
            String nsUri = nsContext.getNamespaceURI("");
            return !StringUtil.isEmpty(nsUri);
        }
        
        // PHIL: not sure what to do here...
        public String getBaseIdentifier() {
            return "";
        }
    }

    private static XResult convertResult(XObject object) throws TransformerException {
        switch(object.getType()) {
            //case XObject.CLASS_UNKNOWN:
            //case XObject.CLASS_NULL:
            // break;
        
            case XObject.CLASS_BOOLEAN: {
                return new XResultUtils.BooleanValue(object.bool());
            }
            case XObject.CLASS_NUMBER: {
                return new XResultUtils.NumberValue(object.num());
            }
            case XObject.CLASS_STRING: {
                return new XResultUtils.StringValue(object.str());
            }
            case XObject.CLASS_NODESET: {
                final NodeList nl = object.nodelist();
                final int len = nl.getLength(); 
                if(len==0) {
                    return XResultUtils.emptyResult;
                } else if(len==1) {
                    return new XResultUtils.XMLNode(nl.item(0));
                } else { 
                    return new XResultUtils.XMLNodeList(nl);
                }
            }
            //case CLASS_RTREEFRAG: {
           //CLASS_UNRESOLVEDVARIABLE = 600;
        }
        return XResultUtils.emptyResult;
    }
}
