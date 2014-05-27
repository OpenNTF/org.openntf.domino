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

import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.util.EmptyIterator;
import com.ibm.commons.util.IteratorWrapper;
import com.ibm.commons.util.SingleValueIterator;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.util.XMIConverter;


/**
 * XResult utilities.
 */
public final class XResultUtils {
    
    public static Empty emptyResult = new Empty();
    
    private static Object[] EMPTY_NODES  = new Object[0];
    private static String[] EMPTY_VALUES = new String[0];

    public static void dump(XResult result, PrintStream ps) {
        dump(result,ps,null);
    }
    public static void dump(XResult result, PrintStream ps, NamespaceContext ns) {
        try {
            ps.print("Dumping XResult: "); // $NON-NLS-1$
            switch(result.getValueType()) {
                case XResult.TYPE_EMPTY: {
                    ps.println(" Empty"); // $NON-NLS-1$
                } break;
                case XResult.TYPE_STRING: {
                    ps.println(StringUtil.format(" String, {0}",result.getStringValue())); // $NON-NLS-1$
                } break;
                case XResult.TYPE_BOOLEAN: {
                    ps.println(StringUtil.format(" Boolean, {0}",Boolean.toString(result.getBooleanValue()))); // $NON-NLS-1$
                } break;
                case XResult.TYPE_NUMBER: {
                    ps.println(StringUtil.format(" Double, {0}",Double.toString(result.getNumberValue()))); // $NON-NLS-1$
                } break;
                case XResult.TYPE_SINGLENODE: {
                    ps.println(StringUtil.format(" Single Node, {0}",getNodePath((Node)result.getSingleNode(),ns))); // $NON-NLS-1$
                } break;
                case XResult.TYPE_MULTIPLENODES: {
                    ps.println(StringUtil.format(" Multiple Node")); // $NON-NLS-1$
                    for( Iterator it=result.getNodeIterator(); it.hasNext(); ) {
                        Node n = (Node)it.next();
                        ps.println(StringUtil.format("     * {0}",getNodePath(n,ns)));
                    }
                } break;
                case XResult.TYPE_DATE: {
                    ps.println(StringUtil.format(" Date, {0}",result.getStringValue())); // $NON-NLS-1$
                } break;
            }
        } catch(XMLException ex) {
        }
    }
    private static String getNodePath(Node node, NamespaceContext ns) {
        if(ns==null) {
            ns = DOMUtil.getSelectionNamespaces(node.getOwnerDocument());
        }
        StringBuffer b = new StringBuffer();
        addNodePath(b,node,ns);
        String text = DOMUtil.getText(node);
        b.append("(");
        b.append(text);
        b.append(")");
        return b.toString();
    }
    private static void addNodePath(StringBuffer b, Node node, NamespaceContext ns) {
        Node p = node.getParentNode();
        if( p!=null ) {
            addNodePath(b,p,ns);
            b.append("/");
        }
        if( node.getNodeType()==Node.ELEMENT_NODE || node.getNodeType()==Node.ATTRIBUTE_NODE) {
            String uri = node.getNamespaceURI();
            if( StringUtil.isNotEmpty(uri) ) {
                String pre = ns!=null ? ns.getPrefix(uri) : node.getPrefix();
                if( StringUtil.isNotEmpty(pre) ) {
                    b.append(pre);
                    b.append(':');
                }
            }
            String localName = node.getLocalName();
            if( node.getNodeType()==Node.ATTRIBUTE_NODE ) {
                b.append('@');
            }
            b.append(localName);
        }
    }
    
    private static abstract class AbstractResult implements XResult {
    }

        
    private static class Empty extends AbstractResult {
        
        public Empty() {
        }
        public boolean equals(Object o) {
            return o instanceof Empty;
        }
        public int getValueType() {
            return TYPE_EMPTY;
        }
        public boolean isValue() {
            return false;
        }
        public boolean isEmpty() {
            return true;
        }
        public boolean isMultiple() {
            return false;
        }
        public String getStringValue() {
            return null;
        }
        public double getNumberValue() {
            return 0.0;
        }
        public boolean getBooleanValue() {
            return false;
        }
        public Date getDateValue() throws XMLException {
        	return null;
        }
        public Object getSingleNode() {
            return null;
        }
        public Iterator getNodeIterator() {
            return EmptyIterator.getInstance();
        }
        public Object[] getNodes() {
            return EMPTY_NODES;
        }
        public Iterator getValueIterator() {
            return EmptyIterator.getInstance();
        }
        public String[] getValues() {
            return EMPTY_VALUES;
        }
        
        public String toString() {
            return null;
        }
    }

    private static abstract class ValueResult extends AbstractResult {
        public Object getSingleNode() {
            return null;
        }
        public Iterator getNodeIterator() {
            return EmptyIterator.getInstance();
        }
        public Object[] getNodes() {
            return EMPTY_NODES;
        }
        public Iterator getValueIterator() {
            return new SingleValueIterator(stringValue());
        }
        public String[] getValues() {
            return new String[]{stringValue()};
        }
        public String getStringValue() {
            return stringValue();
        }
        public Date getDateValue() throws XMLException {
        	return XMIConverter.parseDate(getStringValue());
        }
        public String toString() {
            return stringValue();
        }
        abstract String stringValue(); 
    }
    public static class StringValue extends ValueResult {
        
        private String value;
        
        public StringValue(String value) {
            this.value = value;
        }
        public boolean equals(Object o) {
            if( o instanceof StringValue ) {
                StringValue r = (StringValue)o;
                return StringUtil.equals(value, r.value);
            }
            return false;
        }
        public int getValueType() {
            return TYPE_STRING;
        }
        public boolean isValue() {
            return true;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean isMultiple() {
            return false;
        }
        protected String stringValue() {
            return value;
        }
        public double getNumberValue() throws XMLException {
            try {
                return Double.parseDouble(value);
            } catch(Exception e) {
                throw new XMLException(e,"Error getting number value"); // $NLS-XResultUtils.Errorwhilegettingnumbervalue-1$
            }
        }
        public boolean getBooleanValue() {
            return value.equals("true"); // $NON-NLS-1$
        }
    }

    public static class NumberValue extends ValueResult {
        
        private double value;
        
        public NumberValue(double value) {
            this.value = value;
        }
        public boolean equals(Object o) {
            if( o instanceof NumberValue ) {
                NumberValue r = (NumberValue)o;
                return value==r.value;
            }
            return false;
        }
        public int getValueType() {
            return TYPE_NUMBER;
        }
        public boolean isValue() {
            return true;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean isMultiple() {
            return false;
        }
        protected String stringValue() {
            return Double.toString(value);
        }
        public double getNumberValue() {
            return value;
        }
        public boolean getBooleanValue() {
            return value!=0.0;
        }
    }

    public static class BooleanValue extends ValueResult {
        
        private boolean value;
        
        public BooleanValue(boolean value) {
            this.value = value;
        }
        public boolean equals(Object o) {
            if( o instanceof BooleanValue ) {
                BooleanValue r = (BooleanValue)o;
                return value==r.value;
            }
            return false;
        }
        public int getValueType() {
            return TYPE_BOOLEAN;
        }
        public boolean isValue() {
            return true;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean isMultiple() {
            return false;
        }
        protected String stringValue() {
            return value ? "true" : "false"; // $NON-NLS-2$ $NON-NLS-1$
        }
        public double getNumberValue() {
            return value ? 1.0 : 0.0;
        }
        public boolean getBooleanValue() {
            return value;
        }
    }

    public static abstract class SingleNode extends AbstractResult {
        
        private Object node;
        
        public SingleNode(Object node) {
            this.node = node;
        }
        public boolean equals(Object o) {
            if( o instanceof SingleNode ) {
                SingleNode r = (SingleNode)o;
                return node==r.node;
            }
            return false;
        }
        public int getValueType() {
            return TYPE_SINGLENODE;
        }
        public boolean isValue() {
            return false;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean isMultiple() {
            return false;
        }
        public String getStringValue() {
            return getText(node);
        }
        public double getNumberValue() throws XMLException {
            try {
                String str = getText(node);
                return Double.parseDouble(str);
            } catch(Exception e) {
                throw new XMLException(e,"Error getting number value"); // $NLS-XResultUtils.Errorwhilegettingnumbervalue.1-1$
            }
        }
        public boolean getBooleanValue() {
            String str = getText(node);
            return str.equals("true"); // $NON-NLS-1$
        }
        public Date getDateValue() {
            String str = getText(node);
            return XMIConverter.parseDate(str);
        }
        public Object getSingleNode() {
            return node;
        }
        public Iterator getNodeIterator() {
            return new SingleValueIterator(node);
        }
        public Object[] getNodes() {
            return new Object[]{node};
        }
        public Iterator getValueIterator() {
            return new SingleValueIterator(getText(node));
        }
        public String[] getValues() {
            return new String[]{getText(node)};
        }
        public String toString() {
            return getStringValue();
        }
        
        protected abstract String getText(Object node);
    }

    public static abstract class MultipleNode extends AbstractResult {
        
        public MultipleNode() {
        }
        public int getValueType() {
            return TYPE_MULTIPLENODES;
        }
        public boolean isValue() {
            return false;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean isMultiple() {
            return true;
        }
        public double getNumberValue() throws XMLException {
            throw new XMLException(null,"Cannot get single value from a node list"); // $NLS-XResultUtils.Cannotgetsinglevaluefromanodelist.1-1$
        }
        public boolean getBooleanValue() throws XMLException {
            throw new XMLException(null,"Cannot get single value from a node list"); // $NLS-XResultUtils.Cannotgetsinglevaluefromanodelist-1$
        }
        public Date getDateValue() throws XMLException {
            throw new XMLException(null,"Cannot get single value from a node list"); // $NLS-XResultUtils.Cannotgetsinglevaluefromanodelist-1$
        }
        public Object getSingleNode() throws XMLException {
            throw new XMLException(null,"Cannot get single node from a node list"); // $NLS-XResultUtils.Cannotgetsinglenodefromanodelist-1$
        }

        protected abstract String getText(Object node);
        public abstract Iterator getNodeIterator();

        public Iterator getValueIterator() {
            return new IteratorWrapper(getNodeIterator()) {
                protected Object wrap( Object o ) {
                    return getText(o);
                }
            };
        }
        
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Iterator i = getNodeIterator(); i.hasNext(); ) {
                sb.append(DOMUtil.getTextValue((Node)i.next()));
                if (i.hasNext()) sb.append(","); //$NON-NLS-1$
            }
            return sb.toString();
        }
    }
    
    public static class XMLNode extends SingleNode {
        public XMLNode(Node node) {
            super(node);
        }
        protected String getText(Object node) {
            return DOMUtil.getTextValue((Node)node);
        }
    }
    
    public static class XMLNodeList extends MultipleNode {
        private NodeList nodeList;
        public XMLNodeList(NodeList nodeList) {
            this.nodeList = nodeList;
        }
        public boolean equals(Object o) {
            if( o instanceof XMLNodeList ) {
                XMLNodeList r = (XMLNodeList)o;
                if(nodeList.getLength()==r.nodeList.getLength()) {
                    for( int i=0; i<nodeList.getLength(); i++ ) {
                        Node n1 = nodeList.item(i);
                        Node n2 = r.nodeList.item(i);
                        if(n1!=n2) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        protected String getText(Object node) {
            return DOMUtil.getTextValue((Node)node);
        }
        public String getStringValue() {
            StringBuffer buffer = new StringBuffer();
            int length = nodeList.getLength();
            for(int i=0; i<length; i++) {
                buffer.append(DOMUtil.getTextValue(nodeList.item(i)));
                if (i+1<length) {
                    buffer.append(XResult.DELIM);
                }
            }
            return buffer.toString();
        }
        public Iterator getNodeIterator() {
            return new Iterator() {
                private int current;
                private int len = nodeList.getLength(); 

                public boolean hasNext() {
                    return current<len;
                }
                public Object next() {
                    if( current<len ) {
                        return nodeList.item(current++);
                    }
                    throw new NoSuchElementException();
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
        public Object[] getNodes() {
            Object[] result = new Object[nodeList.getLength()];
            for( int i=0; i<result.length; i++) {
                result[i] = nodeList.item(i);
            }
            return result;
        }
        public String[] getValues() {
            String[] result = new String[nodeList.getLength()];
            for( int i=0; i<result.length; i++) {
                result[i] = getText(nodeList.item(i));
            }
            return result;
        }
    }
}
