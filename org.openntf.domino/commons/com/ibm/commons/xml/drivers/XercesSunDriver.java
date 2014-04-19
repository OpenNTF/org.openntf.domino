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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import com.sun.org.apache.xerces.internal.dom.NodeImpl;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.XPathAPI;
import com.sun.org.apache.xpath.internal.objects.XObject;


import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.NullInputStream;
import com.ibm.commons.xml.Format;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XPathContext;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.XResultUtils;
import com.ibm.commons.xml.xpath.NodeListImpl;
import com.ibm.commons.xml.xpath.XPathException;



/**
 * Basic XERCES services.
 * This class encapsulate the basic document creation as well as the XPath service.
 */
public class XercesSunDriver extends AbstractDriver {
    
    private DOMImplementation domImplementation;
    
    public XercesSunDriver() {
        this.domImplementation = com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl.getDOMImplementation();
    }
    
    public DOMImplementation getDOMImplementation() {
        return domImplementation;
    }
    
    protected DocumentBuilderFactory createDocumentBuilderFactory(boolean resolveEntities, boolean validate) {
        DocumentBuilderFactory dbFactory = new DocumentBuilderFactoryImpl();
        dbFactory.setNamespaceAware(true);
        dbFactory.setExpandEntityReferences(resolveEntities);
    	dbFactory.setValidating(validate);
        return dbFactory;
    }
    
    // =========================================================================================
    // NamespaceContext Access
    // =========================================================================================
    
    private static class UserData {
        private NamespaceContext nsContext;
        private XPathContext xpContext;
    }
    
    private UserData getUserData(Document doc) {
        NodeImpl impl = (NodeImpl)doc;
        UserData data = (UserData)impl.getUserData();
        if(data==null) {
            data = new UserData();
            impl.setUserData(data);
        }
        return data;
    }
    
    public XPathContext getXPathContext(Document doc) {
        UserData data = getUserData(doc);
        return data.xpContext;
    }
    
    public void pushXPathContext(Document doc, String xpath) throws XMLException {
        UserData data = getUserData(doc);
        data.xpContext = new XPathContext(doc,data.xpContext,xpath);
    }
    
    public void popXPathContext(Document doc) throws XMLException {
        UserData data = getUserData(doc);
        data.xpContext = data.xpContext.getParent();
    }

    public NamespaceContext getNamespaceContext(Document doc) {
        UserData data = getUserData(doc);
        return data.nsContext;
    }
    
    public void setNamespaceContext(Document doc, NamespaceContext ns) {
        UserData data = getUserData(doc);
        data.nsContext = ns;
    }
    
    public void serialize(OutputStream os, Node node, Format format) throws XMLException {
        try {
            XMLSerializer ser = createXMLSerializer(node,format);
            ser.setOutputByteStream(os);
            serialize(ser,node);
        } catch(Exception e) {
            throw new XMLException(e,"Error while converting XML document to string"); // $NLS-AbstractXercesDriver.ErrorwhileconvertingXMLdocumentto-1$
        }
    }

    public void serialize(Writer w, Node node, Format format) throws XMLException {
        try {
            XMLSerializer ser = createXMLSerializer(node,format);
            ser.setOutputCharStream(w);
            serialize(ser,node);
        } catch(Exception e) {
            throw new XMLException(e,"Error while converting XML document to string"); // $NLS-AbstractXercesDriver.ErrorwhileconvertingXMLdocumentto.1-1$
        }
    }

    private XMLSerializer createXMLSerializer(Node node, Format fmt) {
        if(fmt==null) {
            fmt = Format.defaultFormat;
        }
        OutputFormat format = new OutputFormat(); //node.getOwnerDocument());
        format.setIndent(fmt.indent);
        format.setOmitXMLDeclaration(!fmt.xmlDecl);
        format.setEncoding(fmt.encoding);
        return new XMLSerializer(format);
    }

    private void serialize(XMLSerializer ser, Node node) throws IOException {
        if(node instanceof Document) {
            ser.serialize((Document)node);
        } else if(node instanceof Element) {
            ser.serialize((Element)node);
        } else if(node instanceof DocumentFragment) {
            ser.serialize((DocumentFragment)node);
        }
    }
    
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

    
    private final static EntityResolver NULL_ENTITY_RESOLVER = new EntityResolver() {
        public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
            return new InputSource(new NullInputStream());
        }
        
    };
    
    public Document parse(InputStream is, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            if (!resolveEntities) {
                docBuilder.setEntityResolver(NULL_ENTITY_RESOLVER);
            }
            
            Document doc = docBuilder.parse(is);

            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }

            return doc;
        }
        catch (Exception e) {
            throw new XMLException(e, "Error parsing XML stream"); // $NLS-XercesDriver.ErrorwhileparsingXMLstream-1$
        }
    }
    
    public Document parse(Reader reader, boolean ignoreBlanks, boolean resolveEntities, boolean validate) throws XMLException {
        try {
            DocumentBuilderFactory dbFactory = createDocumentBuilderFactory(resolveEntities,validate);
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            
            if (!resolveEntities) {
                docBuilder.setEntityResolver(NULL_ENTITY_RESOLVER);
            }
            
            Document doc = docBuilder.parse(new InputSource(reader));

            if (ignoreBlanks) {
                removeEmtyTextNodes(doc);
            }

            return doc;
        }
        catch (Exception e) {
            throw new XMLException(e, "Error parsing XML stream"); // $NLS-XercesDriver.ErrorwhileparsingXMLstream.1-1$
        }
    }
}
