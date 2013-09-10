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
 * Iterator for a single value.
 * @ibm-api
 */
public class SingleValueIterator<T> implements Iterator<T> {

    private T value;

    public SingleValueIterator( T value ) {
        this.value = value;
    }

    public boolean hasNext() {
        return value!=null;
    }

    public T next() {
        if( value!=null ) {
            Object result = value;
            value = null;
            return (T)result;
        }
        throw new NoSuchElementException("No more elements in iterator");  // $NLS-SingleValueIterator.Nomoreelementiniterator-1$
    }

    public void remove() {
        throw new NoSuchElementException("Iterator does not support remove() method");  // $NLS-SingleValueIterator.Iteratordoesnotsupportremovemetho-1$
    }
}
