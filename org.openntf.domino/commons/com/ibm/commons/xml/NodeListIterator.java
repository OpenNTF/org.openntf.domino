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
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A JAVA2 collection iterator on DOM nodes.
 */
public class NodeListIterator implements Iterator {

    private NodeList list;
    private Filter filter;
    private int length;
    private int current;
    
    public static interface Filter {
        public boolean accept( Node node );
    }
    
    public static Filter ELEMENT_FILTER = new Filter() {
        public boolean accept( Node node ) {
            return node.getNodeType()==Node.ELEMENT_NODE;
        }
    };

    public NodeListIterator(NodeList list, Filter filter) {
        this.list = list;
        this.filter = filter;
        this.length = list.getLength();
        if(filter!=null) {
            this.current = Integer.MAX_VALUE;
            for( int i=0; i<length; i++ ) {
                Node n = list.item(i);
                if(filter.accept(n)) {
                    this.current = i; 
                    break;
                }
            }
        }
    }

    public NodeListIterator(NodeList list) {
        this(list,null);
    }

    public boolean hasNext() {
        return current<length;
    }

    public Object next() {
        if(current<length) {
            Node node = list.item(current++);
            if(filter!=null) {
                while( current<length && !filter.accept(list.item(current))) {
                    current++;
                }
            }
            return node;
        }
        throw new NoSuchElementException( "Node iterator is empty"); // $NLS-NodeListIterator.Nodeiteratorisempty-1$
    }

    public void remove() {
        throw new IllegalStateException( "Cannot remove an item in a Node iterator" ); // $NLS-NodeListIterator.CannotremoveaniteminaNodeiterator-1$
    }
}
