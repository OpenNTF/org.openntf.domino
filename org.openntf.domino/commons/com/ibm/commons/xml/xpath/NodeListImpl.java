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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * NodeListImpl is the wrapper over the <code>org.w3c.dom.Node</code> implemented on
 * top of an <code>ArrayList</code>, to return the node value via overriden 
 * <code>toString()</code>.
 * <p>
 * This method returns the node value except the case when the node of the
 * <code>Node.ELEMENT_NODE</code> type. In this event it attempt to find the
 * child text node and return the text node value. If there is no text it will
 * return the value of the node
 * <p>
 * 
 * @author Eugene Konstantinov
 * @author Philippe Riand
 * 
 */
public class NodeListImpl extends ArrayList implements NodeList {
	
	private static final long serialVersionUID = -1732976747721986301L;

	public NodeListImpl() {
	}

	public NodeListImpl(int initialCapacity) {
		super(initialCapacity);
	}

	public Node item(int index) {
	    // WHITNEY
        // per the contract of NodeList, this method must return null, rather than throwing 
        // an exception when an item is outside of the index.
	    Node item = null;
        
        try {
            item = (Node) get(index);
        } catch (IndexOutOfBoundsException exception) {
            // exepected under normal conditions, no handling necessary
        }
        
        return item;
	}

	public int getLength() {
		return size();
	}

	public String toString() {
		if (size() > 0) {
			Object node = get(0);
			if (node instanceof Node) {
				if (((Node) node).getNodeType() == Node.ELEMENT_NODE) {
					Node firstChild = ((Node) node).getFirstChild(); 
					if (firstChild != null) 
						return firstChild.getNodeValue();
				}
				else {
					return ((Node) node).getNodeValue();
				}
			}
		}
		return "";
	}
	
	public boolean equals(Object obj) {
		return toString().equals(obj);
	}	
}
