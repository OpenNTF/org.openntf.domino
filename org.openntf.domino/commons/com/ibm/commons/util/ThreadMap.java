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

/**
 * Thread map, that maps an object to a thread.
 * This class hide how data are stored and may have different implementation,
 * depending on the JDK.
 * @deprecated
 * @ibm-not-published
 */
public class ThreadMap {

    private static class Item {
        private Item next;
        private Thread thread;
        private Object object;
        Item( Thread thread, Object object ) {
            this.thread = thread;
            this.object = object;
        }
    }

    private Item firstItem;

    public ThreadMap() {
    }
    
    public boolean hasItems() {
    	return firstItem!=null;
    }

    public synchronized void clear() {
        this.firstItem = null;
    }

    public synchronized void remove(Thread thread) {
        if( firstItem!=null ) {
            if( firstItem.thread==thread ) {
                firstItem = firstItem.next;
            } else {
                for( Item it=firstItem; it.next!=null; it=it.next ) {
                    if( it.next.thread==thread ) {
                        it.next = it.next.next;
                        break;
                    }
                }
            }
            return;
        }
    }

    public Object get(Thread thread) {
        for( Item it=firstItem; it!=null; it=it.next ) {
            if( it.thread==thread ) {
                return it.object;
            }
        }
        return null;
    }

    public synchronized void put( Thread thread, Object object ) {
        if( object!=null ) {
            for( Item it=firstItem; it!=null; it=it.next ) {
                if( it.thread==thread ) {
                    it.object = object;
                    return;
                }
            }
            Item item = new Item( thread, object );
            item.next = firstItem;
            firstItem = item;
        } else {
            remove(thread);
        }
    }
}
