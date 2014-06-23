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

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.util.XMIConverter;


/**
 * W3C DOM Utilities.
 * Utility functions to access the content of a DOM with strong typed methods.
 */
public class DOMAccessor {
	
    // ======================================================================
    // Integer value getter/setter
    // ======================================================================

    /**
     * Get an integer value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as an long integer
     * @throws XMLException if an error occurred
     */
    public static long getIntValue(Node node, String xpath) throws XMLException {
    	return getIntValue(node,xpath,null);
    }
    
    /**
     * Get an integer value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as an long integer
     * @throws XMLException if an error occurred
     */
    public static long getIntValue(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getStringValue();
        return XMIConverter.parseLong(r);
    }

    /**
     * Get an array of integer values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the values as an long integer array
     * @throws XMLException if an error occurred
     */
    public static long[] getIntValues(Node node, String xpath) throws XMLException {
    	return getIntValues(node,xpath,null);
    }
    
    /**
     * Get an array of integer values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the values as an long integer array
     * @throws XMLException if an error occurred
     */
    public static long[] getIntValues(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String[] r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getValues();
    	long[] res = new long[r.length];
    	for( int i=0; i<r.length; i++ ) {
    		res[i] = XMIConverter.parseLong(r[i]);
    	}
    	return res;
    }

    /**
     * Get the double value of the given element.
     * @param node the source node
     * @return the value as a double
     * @throws XMLException if an error occurred
     */
    public static long getIntValue(Node node) throws XMLException {
        return XMIConverter.parseLong(DOMUtil.getTextValue(node));
    }
    
    /**
     * Get the integer value of the given element.
     * @param node the source node
     * @param def default value to return if node value is empty
     * @return the value as an integer
     * @throws XMLException
     */
    public static double getDoubleValue(Node node, int def) throws XMLException {
        String s = DOMUtil.getTextValue(node);
        return StringUtil.isEmpty(s) ? def : XMIConverter.parseInteger(s);
    }

    /**
     * Set the given value (as an integer) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setIntValue(Node node, String xpath, long value) throws XMLException {
    	setIntValue(node,xpath,value,null);
    }
    
    /**
     * Set the given value (as an integer) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setIntValue(Node node, String xpath, long value, NamespaceContext selectionNS) throws XMLException {
        DOMUtil.setValue(node,xpath,XMIConverter.toString(value),selectionNS);
    }


    /**
     * Set the given value (as an integer) to the given element.
     * @param node the source node
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setIntValue(Node node, long value) throws XMLException {
    	DOMUtil.setTextValue(node,XMIConverter.toString(value));
    }


    // ======================================================================
    // Double value getter/setter
    // ======================================================================

    /**
     * Get a double value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a double
     * @throws XMLException if an error occurred
     */
    public static double getDoubleValue(Node node, String xpath) throws XMLException {
        return getDoubleValue(node, xpath,null);
    }
    
    /**
     * Get a double value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a double
     * @throws XMLException if an error occurred
     */
    public static double getDoubleValue(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getStringValue();
        return XMIConverter.parseDouble(r);
    }

    /**
     * Get a double array of values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the values as a double array
     * @throws XMLException if an error occurred
     */
    public static double[] getDoubleValues(Node node, String xpath) throws XMLException {
        return getDoubleValues(node, xpath, null);
    }

    /**
     * Get a double array of values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the values as a double array
     * @throws XMLException if an error occurred
     */
    public static double[] getDoubleValues(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String[] r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getValues();
    	double[] res = new double[r.length];
    	for( int i=0; i<r.length; i++ ) {
    		res[i] = XMIConverter.parseDouble(r[i]);
    	}
    	return res;
    }

    /**
     * Get the double value of the given element.
     * @param node the source node
     * @return the value as a double
     * @throws XMLException if an error occurred
     */
    public static double getDoubleValue(Node node) throws XMLException {
        return XMIConverter.parseDouble(DOMUtil.getTextValue(node));
    }
    
    /**
     * Get the double value of the given element.
     * @param node the source node
     * @param def default value to return if node value is empty
     * @return the value as a double
     * @throws XMLException
     */
    public static double getDoubleValue(Node node, double def) throws XMLException {
        String s = DOMUtil.getTextValue(node);
        return StringUtil.isEmpty(s) ? def : XMIConverter.parseDouble(s);
    }

    /**
     * Set the given value (as a double) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setDoubleValue(Node node, String xpath, double value) throws XMLException {
        setDoubleValue(node, xpath, value, null);
    }
    
    /**
     * Set the given value (as a double) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setDoubleValue(Node node, String xpath, double value, NamespaceContext selectionNS) throws XMLException {
        DOMUtil.setValue(node,xpath,XMIConverter.toString(value),selectionNS);
    }

    /**
     * Set the given value (as a double) to the given element.
     * @param node the source node
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setDoubleValue(Node node, double value) throws XMLException {
    	DOMUtil.setTextValue(node,XMIConverter.toString(value));
    }



    // ======================================================================
    // Boolean value getter/setter
    // ======================================================================

    /**
     * Get a boolean value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static boolean getBooleanValue(Node node, String xpath) throws XMLException {
        return getBooleanValue(node, xpath, null);
    }
    
    /**
     * Get a boolean value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static boolean getBooleanValue(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getStringValue();
        return XMIConverter.parseBoolean(r);
    }

    /**
     * Get a boolean array of values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the values as a boolean array
     * @throws XMLException if an error occurred
     */
    public static boolean[] getBooleanValues(Node node, String xpath) throws XMLException {
    	return getBooleanValues(node,xpath,null);
    }
    
    /**
     * Get a boolean array of values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the values as a boolean array
     * @throws XMLException if an error occurred
     */
    public static boolean[] getBooleanValues(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String[] r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getValues();
    	boolean[] res = new boolean[r.length];
    	for( int i=0; i<r.length; i++ ) {
    		res[i] = XMIConverter.parseBoolean(r[i]);
    	}
    	return res;
    }

    /**
     * Get the boolean value of the given element.
     * @param node the source node
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static boolean getBooleanValue(Node node) throws XMLException {
        return XMIConverter.parseBoolean(DOMUtil.getTextValue(node));
    }

    /**
     * Set the given value (as a boolean) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setBooleanValue(Node node, String xpath, boolean value) throws XMLException {
        setBooleanValue(node, xpath, value, null);
    }
    
    /**
     * Set the given value (as a boolean) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setBooleanValue(Node node, String xpath, boolean value, NamespaceContext selectionNS) throws XMLException {
    	DOMUtil.setValue(node,xpath,XMIConverter.toString(value),selectionNS);
    }

    /**
     * Set the given value (as a boolean) to the given element.
     * @param node the source node
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setBooleanValue(Node node, boolean value) throws XMLException {
    	DOMUtil.setTextValue(node,XMIConverter.toString(value));
    }



    // ======================================================================
    // Date value getter/setter
    // ======================================================================

    /**
     * Get a date value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a date
     * @throws XMLException if an error occurred
     */
    public static java.util.Date getDateValue(Node node, String xpath) throws XMLException {
        return getDateValue(node, xpath, null);
    }

    /**
     * Get a date value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a date
     * @throws XMLException if an error occurred
     */
    public static java.util.Date getDateValue(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getStringValue();
        return XMIConverter.parseDate(r);
    }

    /**
     * Get an array of date values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the values as a date array
     * @throws XMLException if an error occurred
     */
    public static java.util.Date[] getDateValues(Node node, String xpath) throws XMLException {
        return getDateValues(node, xpath, null);
    }
    
    /**
     * Get an array of date values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the values as a date array
     * @throws XMLException if an error occurred
     */
    public static java.util.Date[] getDateValues(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String[] r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getValues();
    	java.util.Date[] res = new java.util.Date[r.length];
    	for( int i=0; i<r.length; i++ ) {
    		res[i] = XMIConverter.parseDate(r[i]);
    	}
    	return res;
    }

    /**
     * Get the date value of the given element.
     * @param node the source node
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static java.util.Date getDateValue(Node node) throws XMLException {
        return XMIConverter.parseDate(DOMUtil.getTextValue(node));
    }
    
    /**
     * Get the date value of the given element.
     * 
     * @param node the source node
     * @param def default to return if node value is empty
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static java.util.Date getDateValue(Node node, java.util.Date def) throws XMLException {
        java.util.Date date = getDateValue(node);
        return date == null ? def : date;
    }

    /**
     * Set the given value (as a date) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setDateValue(Node node, String xpath, java.util.Date value) throws XMLException {
        setDateValue(node, xpath, value, null);
    }
    
    /**
     * Set the given value (as a date) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setDateValue(Node node, String xpath, java.util.Date value, NamespaceContext selectionNS) throws XMLException {
        DOMUtil.setValue(node,xpath,XMIConverter.toString(value),selectionNS);
    }

    /**
     * Set the given value (as a date) to the given element.
     * @param node the source node
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setDateValue(Node node, java.util.Date value) throws XMLException {
    	DOMUtil.setTextValue(node,XMIConverter.toString(value));
    }


    // ======================================================================
    // String value getter/setter
    // ======================================================================

    /**
     * Get a string value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a string
     * @throws XMLException if an error occurred
     */
    public static String getStringValue(Node node, String xpath) throws XMLException {
        return getStringValue(node, xpath, null);
    }
    
    /**
     * Get a string value from the first XPath matching element.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a string
     * @throws XMLException if an error occurred
     */
    public static String getStringValue(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getStringValue();
        return r;
    }

    /**
     * Get an array of string values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @return the value as a string array
     * @throws XMLException if an error occurred
     */
    public static String[] getStringValues(Node node, String xpath) throws XMLException {
        return getStringValues(node, xpath, null);
    }
    
    /**
     * Get an array of string values from an XPath.
     * @param node the source node
     * @param xpath the XPath to evaluate
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @return the value as a string array
     * @throws XMLException if an error occurred
     */
    public static String[] getStringValues(Node node, String xpath, NamespaceContext selectionNS) throws XMLException {
    	String[] r = DOMUtil.evaluateXPath(node,xpath,selectionNS).getValues();
    	return r;
    }

    /**
     * Get the string value of the given element.
     * @param node the source node
     * @return the value as a boolean
     * @throws XMLException if an error occurred
     */
    public static String getStringValue(Node node) throws XMLException {
    	return DOMUtil.getTextValue(node);
    }
    
    /**
     * Get the string value of the given element.
     * 
     * @param node the source node
     * @param def  the default to return if the node value is empty
     * @return the value as a String
     * @throws XMLException if an error occurred
     */
    public static String getStringValueWithDefault(Node node, String def) throws XMLException {
        String s = DOMUtil.getTextValue(node);
        return StringUtil.isEmpty(s) ? def : s;
    }

    /**
     * Set the given value (as a string) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setStringValue(Node node, String xpath, String value) throws XMLException {
        setStringValue(node, xpath, value, null);
    }
    
    /**
     * Set the given value (as a string) to the first XPath matching element.
     * That method first evaluates the XPath and, if it returns an existing nodes, then
     * it updates it value. If not, then it tries to add a new node corresponding to that
     * XPath. This operation is possible only if the XPath is simple, thus in a form
     * like <code>node[/node...]</code>.
     * @param node the source node
     * @param xpath the XPath to use
     * @param value the value to set
     * @param selectionNS the NamespacContext used to resolve namespaces during XPath evaluation
     * @throws XMLException if an error occurred
     */
    public static void setStringValue(Node node, String xpath, String value, NamespaceContext selectionNS) throws XMLException {
        DOMUtil.setValue(node,xpath,value,selectionNS);
    }

    /**
     * Set the given value (as a string) to the given element.
     * @param node the source node
     * @param value the value to set
     * @throws XMLException if an error occurred
     */
    public static void setStringValue(Node node, String value) throws XMLException {
    	DOMUtil.setTextValue(node,value);
    }
}
