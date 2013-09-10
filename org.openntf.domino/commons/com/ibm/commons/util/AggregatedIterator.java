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
 * Aggregated iterator.
 * <p>
 * This Iterator aggregates the content of 2 iterators.
 * </p>
 * @ibm-api
 */
public final class AggregatedIterator<T> implements Iterator<T> {

	private Iterator<T> it1;
	private Iterator<T> it2;

	/**
	 * @ibm-api
	 */
	public AggregatedIterator(Iterator<T> it1, Iterator<T> it2) {
		this.it1 = it1;
		this.it2 = it2;
	}

	/**
	 * @ibm-api
	 */
    public boolean hasNext() {
    	if(it1.hasNext()) {
    		return true;
    	}
    	if(it2.hasNext()) {
    		return true;
    	}
    	return false;
    }

	/**
	 * @ibm-api
	 */
    public T next() {
    	if(it1.hasNext()) {
    		return it1.next();
    	}
    	if(it2.hasNext()) {
    		return it2.next();
    	}
        throw new NoSuchElementException();
    }

	/**
	 * @ibm-api
	 */
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
