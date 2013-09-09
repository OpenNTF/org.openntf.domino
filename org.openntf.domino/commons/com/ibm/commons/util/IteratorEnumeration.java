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

import java.util.Enumeration;
import java.util.Iterator;


/**
 * Iterator wrapped as an enumeration.
 * @ibm-api 
 */
public class IteratorEnumeration<T> implements Enumeration<T> {

	private Iterator<T> it;

	/**
	 * Create an enumeration from an Iterator.
	 * @ibm-api
	 */ 
    public IteratorEnumeration(Iterator<T> it) {
    	this.it = it;
    }

    public boolean hasMoreElements() {
        return it.hasNext();
    }

    public T nextElement() {
        return it.next();
    }
}
