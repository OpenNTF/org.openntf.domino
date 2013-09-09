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
 * A simple semaphore implementation based on work by Doug Lea.
 * @ibm-not_published
 */
public class Semaphore {

    private long _permits;

    /**
     * Create a semaphore with a specified number of permits
     * 
     * @param initialPermits
     */
    public Semaphore(long initialPermits) {
        _permits = initialPermits;
    }

    /**
     * Acquire a permite waiting indefinitely until acquired
     * or the thread is interuppted.
     * 
     * @throws InterruptedException
     */
    public void acquire() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        synchronized (this) {
            try {
                while (_permits<=0)
                    wait();
                --_permits;
            }
            catch (InterruptedException ex) {
                notify();
                throw ex;
            }
        }
    }


    /**
     * Try to acquire a permit waiting for a specified number of millis.
     * 
     * @param msecs
     * @return
     * @throws InterruptedException
     */
    public boolean acquire(long msecs) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();

        synchronized (this) {
            if (_permits>0) {
                --_permits;
                return true;
            }
            else if (msecs<=0)
                return false;
            else {
                try {
                    long startTime = System.currentTimeMillis();
                    long waitTime = msecs;

                    for (;;) {
                        wait(waitTime);
                        if (_permits>0) {
                            --_permits;
                            return true;
                        }
                        else {
                            waitTime = msecs-(System.currentTimeMillis()-startTime);
                            if (waitTime<=0)
                                return false;
                        }
                    }
                }
                catch (InterruptedException ex) {
                    notify();
                    throw ex;
                }
            }
        }
    }


    /**
     * Release a permit
     */
    public synchronized void release() {
        ++_permits;
        notify();
    }




    /**
     * Release a specified number of permits
     * 
     * @param n
     */
    public synchronized void release(long n) {
        if (n<0)
            throw new IllegalArgumentException("Negative argument"); // $NLS-Semaphore.Negativeargument-1$

        _permits += n;
        for (long i = 0; i<n; ++i)
            notify();
    }


    /**
     * Returns the current number of available permits
     * 
     * @return
     */
    public synchronized long permits() {
        return _permits;
    }
}
