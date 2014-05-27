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

import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ranges.Range;

/**
 *      
 * Iterator over the objects in a Range.
 * 
 * @author dloverin
 * Jun 9, 2006
 * 
 */
public class RangeIterator implements ListIterator {

    private Node _currentNode;  // hold place for traversal
    private int _currentOffset; // current offset in container of _currentNode
    private Node _commonAncestor; // derived from _range
    private Range _range;
    private Map _descentPath;       // descent from the common ancestor to the starting container
    private int _currentIndex;      // index of iterator
    private boolean _firstChild;    // if true start at first child, not common ancestor
    
    /**
     * NOT READY FOR USE YET. PLEASE DO NOT USE!
     * 
     * Create a new iterator that will return each node encounters while
     * walking the dom tree from the beginning of the range to the end of
     * the range. 
     * The walk of the range begins at the common ancestor of the range endpoints
     * and continues in a depth first traversal of the tree.
     * 
     * Parent containers are included in the iteration after reaching the last child in
     * a container.
     * 
     * @param range
     */
    public RangeIterator(Range range) {
        this(range, false);
    }
    
    
    /**
     * Same iterator with an option to start the iterator at the range's first node.
     * @param range - range must be normalized.
     * @param firstChild - if true start the the range's first child instead of
     *              the common ancestor.
     */
    public RangeIterator(Range range, boolean firstChild) {
        if (range == null || range.getStartContainer() == null ||
            range.getEndContainer() == null) {
            throw new NullPointerException();
        }
        
        _range = range;
        _firstChild = firstChild;
        initIterator();
    }
    
    public boolean hasNext() {
        
        if (_commonAncestor == null) {
            return false;
        }
        
        if (_currentNode == null) {
            return true;
        }
        
        if (isAtEndOfRange(_currentNode, _currentOffset)) {
            return false;
        }
        
        return true;
    }

    public Object next() {
        
        if (_commonAncestor == null) {
            throw new NoSuchElementException();
        }
 
        if (_currentNode == null) {
            if (_firstChild) {
                _currentNode = _range.getStartContainer();
                if (_currentNode.hasChildNodes()) {
                    _currentNode = _currentNode.getChildNodes().item(_range.getStartOffset());
                    _currentOffset = 0;
                }
                else {
                    _currentOffset = _range.getStartOffset();
                }
            }
            else {
                _currentNode = _commonAncestor;
                _currentOffset = ((Integer)_descentPath.get(_commonAncestor)).intValue();
            }

            _currentIndex++;
            return _currentNode;
        }
        
        if (isAtEndOfRange(_currentNode, _currentOffset)) {
            throw new NoSuchElementException();
        }
   
        if (_currentNode.hasChildNodes()) {
            Integer offsetInt = (Integer)_descentPath.get(_currentNode);
            if (offsetInt == null) {
                _currentNode = _currentNode.getFirstChild();
                _currentIndex++;
                _currentOffset = 0;
                return _currentNode;
            }
            else {
                Node tempNode = _currentNode.getChildNodes().item(offsetInt.intValue());
                if (tempNode != null) {
                    _currentNode = tempNode;
                    _currentIndex++;
                    offsetInt = (Integer)_descentPath.get(_currentNode);
                    _currentOffset = (offsetInt == null) ? 0 : offsetInt.intValue();
                    return _currentNode;
                }
            }
        }

        // when we are at the end of the sibling for a node,
        // go to the parent and get the next sibing in the parent.
        Node nextNode = _currentNode.getNextSibling();
        _currentOffset++;
        while (nextNode == null) {
            _currentNode = _currentNode.getParentNode();
            
            if (_currentNode == _commonAncestor) { 
                throw new NoSuchElementException();
            }

            nextNode = _currentNode.getNextSibling();
            _currentOffset = 0;
        }
        
        _currentNode = nextNode;
        _currentIndex++;
        
        return _currentNode;
    }

     
    /**
     * currently not supported
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * init state of this iterator
     *
     */
    private void initIterator() {
        _currentIndex = 0;
        
        // init the common ascestor
        _commonAncestor = _range.getCommonAncestorContainer();
        
        // this can hapend when an element has been removed from the model
        // but the changes have not been flushed.
        if (_commonAncestor == null) {
            return;
        }
        
        // create a map of nodes to child index to create a path
        // to descend the tree from the common ancestor to the 
        // starting container
        _descentPath = new HashMap();
        Node node = _range.getStartContainer();
        int offset = _range.getStartOffset();
        Node ancestorParent = _commonAncestor.getParentNode();
        while (node != ancestorParent) {
            _descentPath.put(node, Integer.valueOf(offset));
            offset = getOffsetInContainer(node);
            node = node.getParentNode();
        }
        
    }
    
    /**
     * Helper method to get the offset of a given node with respect to its
     * parent.
     * 
     * @param node -
     *            may not be null
     * @return int - zero-based offset of node in its container or -1 if the
     *         node as no container.
     */
    public static int getOffsetInContainer(Node node) {
        Node parent = node.getParentNode();
        if (parent != null) {
            NodeList children = parent.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = (Node) children.item(i);
                if (child == node) {
                    return i;
                }
            }
            throw new IllegalStateException(); // how could the child not be found?
        }

        return -1;
    }


    /**
     * NOT SUPPORTED
     * @param arg0
     */
    public void add(Object arg0) {
        throw new UnsupportedOperationException();
    }


    public boolean hasPrevious() {
        return _currentIndex == 0 ? true : false;
    }


    public int nextIndex() {
        return _currentIndex + 1;
    }


    public Object previous() {
        if (_currentIndex == 0) {
            throw new NoSuchElementException();
        }
        
        // return the current node
        Node retNode = _currentNode;
        
        // update the position of the current node for the next operation
        Node prev = _currentNode.getPreviousSibling();
        if (prev == null) {
            _currentNode = _currentNode.getParentNode();
        }
        else {
            // get the last child of this node if it has children
            if (prev.hasChildNodes()) {
                NodeList children = prev.getChildNodes();
                _currentNode = children.item(children.getLength() - 1);
            }
            else {
                _currentNode = prev;
            }

        }
        
        _currentIndex--;
        
        return retNode;
    }


    public int previousIndex() {
        return _currentIndex - 1;
    }


    /**
     * NOT SUPPORTED
     * 
     * @param arg0
     */
    public void set(Object arg0) {
        throw new UnsupportedOperationException();        
    }   

    
    /**
     * Test if node is at the end of the range of this iterator.
     * @param node
     * @return true node is the last node in the range.
     */
    private boolean isAtEndOfRange(Node node, int offset) {

        // don't compare offsets on the text node since we are jumping node to node.
        if (node.getNodeType() == Node.TEXT_NODE && node == _range.getEndContainer()) {
            return true;
        }
        
        int result = DOMUtil.compareLocations(node, offset, _range.getEndContainer(), _range.getEndOffset());
        if (result >= 0) {
            return true;
        }
 
        Node currentNode = node;
        Node endNode = _range.getEndContainer();

        if (currentNode.hasChildNodes()) {
            return false;
        }
        
        // check if all the parents between the current node and the end node are at their last child.
        // if so then we are done.
        Node parent = currentNode.getParentNode();
        while (parent != null) {
            NodeList children = parent.getChildNodes();
            if (children.item(children.getLength() - 1) != currentNode) {
                break;
            }
            
            if (parent == endNode) {
                return true;    // found no nodes to iterate to
            }
            
            currentNode = parent;
            parent = currentNode.getParentNode();
        }
        
        return false;

    }
    
    
    
    /**
     * Map a node and offset into an actual node.
     * 
     * @param node
     * @param offset
     * @return
     */
//    private Node getEffectiveNode(Node node, int offset) {
//        if (node.hasChildNodes()) {
//            NodeList children = node.getChildNodes();
//            if (offset == children.getLength()) {
//                offset--;
//            }
//            node = children.item(offset);
//        }
//        
//        return node;
//    }
//    
}
