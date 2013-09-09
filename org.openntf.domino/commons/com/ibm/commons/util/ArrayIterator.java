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

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array iterator.
 * <p>
 * This Iterator iterated the content of a Java array.
 * </p>
 * @ibm-api
 */
public class ArrayIterator<T> implements Iterator<T> {

    private Object array;
    private int lastItem;
    private int current;

	/**
	 * @ibm-api
	 */
    public ArrayIterator( Object array ) {
        this.array = array;
        this.lastItem = Array.getLength(array);
    }

	/**
	 * @ibm-api
	 */
    public ArrayIterator( Object array, int first, int last ) {
        this.array = array;
        this.current = first>=0 ? first : 0;
        this.lastItem = last<=Array.getLength(array) ? last : Array.getLength(array);
    }

	/**
	 * @ibm-api
	 */
    public boolean hasNext() {
        return current<lastItem;
    }

	/**
	 * @ibm-api
	 */
    public T next() {
        if( current<lastItem ) {
            return (T)Array.get(array, current++);
        }
        throw new NoSuchElementException( "No more elements in the array iterator" ); //$NLS-ArrayIterator.ArrayIterator.NoElements.Exception-1$
    }

	/**
	 * @ibm-api
	 */
    public void remove() {
        throw new UnsupportedOperationException( "Cannot remove items on array iterator" ); //$NLS-ArrayIterator.ArrayIterator.CannotRemoveItems.Exception-1$
    }
}
