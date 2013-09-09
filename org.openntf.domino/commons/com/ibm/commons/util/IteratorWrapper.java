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
 * Iterator wrapper.
 * <p>
 * This wraps an iterator and alos the object it returns to be wraped.
 * </p>
 * @ibm-api
 */
public abstract class IteratorWrapper<T> implements Iterator {

    private Iterator<?> it;

    public IteratorWrapper( Iterator<?> it ) {
        this.it = it;
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public T next() {
        Object o = it.next();
        return o!=null ? wrap(o) : null;
    }

    public void remove() {
        it.remove();
    }

    protected abstract T wrap( Object o );
}
