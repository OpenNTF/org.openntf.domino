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
 * Empty iterator.
 * @ibm-api
 */
public final class EmptyIterator implements Iterator {

    private static EmptyIterator emptyIterator=new EmptyIterator();

    /**
     * Return a shared instance of an rmpty iterator.
     * @ibm-api
     */
    public static EmptyIterator getInstance() {
        return emptyIterator;
    }

    public boolean hasNext() {
        return false;
    }

    public Object next() {
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
