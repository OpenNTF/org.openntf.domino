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

/**
 * A JAVA2 collection iterator that filters the items from another iterator.
 * @ibm-api
 */
public abstract class FilteredIterator<T> implements Iterator<T> {

    public FilteredIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    protected abstract boolean accept( Object object );

    public boolean hasNext() {
        if( !initialized ) {
            next();
            initialized = true;
        }
        return hasnext;
    }

    public T next() {
        T result = current;
        this.hasnext = false;
        while( !this.hasnext && iterator.hasNext() ) {
            current = iterator.next();
            if( accept(current) ) {
                this.hasnext = true;
            }
        }
        return result;
    }

    public void remove() {
        throw new IllegalStateException( "Filtered iterators cannot be used to remove an item" ); //$NLS-FilteredIterator.FilteredIterator.CannotRemoveItem.Exception-1$
    }

    private boolean initialized;
    private Iterator<T> iterator;
    private boolean hasnext;
    private T current;
}
