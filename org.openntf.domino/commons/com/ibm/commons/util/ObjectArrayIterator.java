/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator on an object array.
 * @ibm-not-published 
 */
public class ObjectArrayIterator implements Iterator<Object> {

    private Object[] array;
    private int lastItem;
    private int current;

    public ObjectArrayIterator( Object[] array ) {
        this.array = array;
        this.lastItem = array.length;
    }

    public ObjectArrayIterator( Object[] array, int first, int last ) {
        this.array = array;
        this.current = first>=0 ? first : 0;
        this.lastItem = last<=array.length ? last : array.length;
    }

    public boolean hasNext() {
        return current<lastItem;
    }

    public Object next() {
        if( current<lastItem ) {
            return array[current++];
        }
        throw new NoSuchElementException( "No more elements in the array iterator" ); //$NLS-ObjectArrayIterator.ArrayIterator.NoElements.Exception-1$
    }

    public void remove() {
        throw new UnsupportedOperationException( "Cannot remove items on array iterator" ); //$NLS-ObjectArrayIterator.ArrayIterator.CannotRemoveItems.Exception-1$
    }
}
