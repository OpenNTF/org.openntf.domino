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
 * Read write lock acquisition.
 * <p>
 * Locking strategies can be defined by overriding some of the protected
 * methods (who has priority, how many concurrent thread can run simultaneously...).
 * Also note that JRE 5 now has a concurrent package which can be used to replace
 * this class.
 * </p>
 * <p>
 * Inspired from 'Concurrent programming in Java, 2nd edition'
 * </p>
 * @ibm-api
 */
public class ThreadLockManager {

    protected int activeReaders = 0;    // Threads executing read
    protected int activeWriters = 0;    // always 0 or one

    protected int waitingReaders = 0;   // Threads not yet in read
    protected int waitingWriters = 0;   // same for write

    protected boolean allowReader(Object param) {
        //TDiag.trace( "{0}: allowReader()={1}, waitingWriters={2}, activeWriters={3}", Thread.currentThread().getName(), TString.toString(waitingWriters==0 && activeWriters==0),TString.toString(waitingWriters),TString.toString(activeWriters) );
        return waitingWriters==0 && activeWriters==0;
    }

    protected boolean allowWriter(Object param) {
        //TDiag.trace( "{0}: allowWriter()={1}, activeReaders={2}, activeWriters={3}", Thread.currentThread().getName(), TString.toString(activeReaders==0 && activeWriters==0),TString.toString(activeReaders),TString.toString(activeWriters) );
        return activeReaders==0 && activeWriters==0;
    }

    protected synchronized void beforeRead(Object param) throws InterruptedException {
        waitingReaders++;
        //Thread.currentThread().dumpStack();
        while( !allowReader(param) ) {
            try {
                //TDiag.trace("Thread {0} = waiting for read", Thread.currentThread().toString() );
                waitForRead(param);
            } catch( InterruptedException e ) {
                waitingReaders--;
                throw e;
            }
        }
        waitingReaders--;
        //TDiag.trace("Thread {0} = aquire read", Thread.currentThread().toString() );
        activeReaders++;
    }
    protected void waitForRead(Object param) throws InterruptedException {
    	wait();
    }

    protected synchronized boolean tryBeforeRead(Object param) throws InterruptedException {
        waitingReaders++;
        try {
            if( allowReader(param) ) {
                //TDiag.trace("Thread {0} = aquire read", Thread.currentThread().toString() );
                activeReaders++;
                return true;
            }
        } finally {
            waitingReaders--;
        }
        return false;
    }

    protected synchronized void afterRead(Object param) {
        activeReaders--;
        //TDiag.trace("Thread {0} = relax read", Thread.currentThread().toString() );
        notifyAll();
    }

    protected synchronized void beforeWrite(Object param) throws InterruptedException {
        waitingWriters++;
        //Thread.currentThread().dumpStack();
        while( !allowWriter(param) ) {
            try {
                //TDiag.trace("Thread {0} = waiting for write", Thread.currentThread().toString() );
                waitForWrite(param);
            } catch( InterruptedException e ) {
                waitingWriters--;
                throw e;
            }
        }
        waitingWriters--;
        activeWriters++;
        //TDiag.trace("Thread {0} = aquire write", Thread.currentThread().toString() );
    }
    protected void waitForWrite(Object param) throws InterruptedException {
    	wait();
    }

    protected synchronized boolean tryBeforeWrite(Object param) throws InterruptedException {
        waitingWriters++;
        try {
            if( allowWriter(param) ) {
                //TDiag.trace("Thread {0} = aquire read", Thread.currentThread().toString() );
                activeWriters++;
                return true;
            }
        } finally {
            waitingWriters--;
        }
        return false;
    }

    protected synchronized void afterWrite(Object param) {
        activeWriters--;
        //TDiag.trace("Thread {0} = relax write", Thread.currentThread().toString() );
        notifyAll();
    }


    // ========================================================================
    // Simple read/write actions
    // ========================================================================

    public void read() throws InterruptedException {
    	read(null);
    }
    public void read(Object param) throws InterruptedException {
        beforeRead(param);
        try {
            doRead();
        } finally {
            afterRead(param);
        }
    }

    public void write() throws InterruptedException {
    	write(null);
    }
    public void write(Object param) throws InterruptedException {
        beforeWrite(param);
        try {
            doWrite();
        } finally {
            afterWrite(param);
        }
    }

    protected void doRead()  {};   // Read action implemented in subclasses
    protected void doWrite() {};  // Write action implemented in subclasses


    // ========================================================================
    // Read and write lock objects
    // ========================================================================

    private class RLock implements ThreadLock {
    	private Object param;
        private RLock(Object param) {this.param=param;}

        public boolean acquire() throws InterruptedException {
            beforeRead(param);
            return true;
        }
        public boolean tryAcquire() throws InterruptedException {
            return tryBeforeRead(param);
        }
        public void release() {
            afterRead(param);
        }
    	public boolean isLocked() {
    		return allowReader(param);
    	}
    }

    private class WLock implements ThreadLock {
    	private Object param;
        private WLock(Object param) { this.param=param; }

        public boolean acquire() throws InterruptedException {
            beforeWrite(param);
            return true;
        }
        public boolean tryAcquire() throws InterruptedException {
            return tryBeforeWrite(param);
        }
        public void release() {
            afterWrite(param);
        }
    	public boolean isLocked() {
    		return allowWriter(param);
    	}
    }

    private final RLock rLock = new RLock(null);
    private final WLock wLock = new WLock(null);

    /**
     * Get a lock used to read data.
     * @ibm-api
     */
    public ThreadLock getReadLock() {
        return rLock;
    }

    /**
     * Get a lock used to read data.
     * @ibm-api
     */
    public ThreadLock getReadLock(Object param) {
        return new RLock(param);
    }

    /**
     * Get a lock used to write data.
     * @ibm-api
     */
    public ThreadLock getWriteLock() {
        return wLock;
    }
    
    /**
     * Get a lock used to write data.
     * @ibm-api
     */
    public ThreadLock getWriteLock(Object param) {
        return new WLock(param);
    }
}
