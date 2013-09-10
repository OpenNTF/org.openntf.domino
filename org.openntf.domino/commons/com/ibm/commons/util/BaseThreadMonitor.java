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
 * Helper class to permit the synchronous call of a method hosted in a secondary
 * thread, useful for library that make mandatory the use of a specific thread
 * class in order to call their functions.</P>
 * This monitor class is the link between the calling and the called thread.
 * <PRE>
 *  Calling thread
 *        |
 *        |  call()    Called Thread
 *        +------>-----------+
 *                           |
 *        +------<-----------+
 *        |
 *        V
 * </PRE>
 * <B>Monitor used :</B><BR>
 * The monitor class has the same methods than the callee object but use the
 * intermediate BaseMethod object and in fact run the <CODE>call()</CODE> method
 * of the monitor. The thread synchronization is ensured by the monitor's methods.
 * <PRE>
 *   class TCalledThread extends BaseThreadMonitor {
 *       public int myMethod(TCallee callee, int param) {
 *           TMyMethod method = new TMyMethod(callee,param);
 *           call( method );
 *           return method.result;
 *       }
 *    }
 * </PRE>
 * <B>Calling thread use :</B><BR>
 * The call a method must be done within a inherited BaseMethod class, in its
 * <CODE>execute()</CODE> method. The BaseMethod object is constructed with all
 * the method parameters, whose need to be stored in order to be used when the
 * method is called.<BR>
 * <PRE>
 *   class TMyMethod implements BaseThreadMonitor.BaseMethod {
 *        TMyMethod(TCallee callee, int param) {
 *            this.callee = callee;
 *            this.param = param;
 *        }
 *        public void execute() {
 *            result = callee.myMethod(param);
 *        }
 *        TCallee callee;
 *        int result;
 *        int param;
 *    }
 * </PRE>
 * Call from the calling thread :
 * <PRE>
 *   int myResult = myMonitor.myMethod(myParam);
 * </PRE>
 * <B>Called thread use :</B><BR>
 * The called thread must wait for an incomming event and when this event occurs
 * it gets the current BaseMethod object and call its <CODE>execute()</CODE> method.
 * This is done by calling the <CODE>run()</CODE> method of the ThreadMonitor.
 * The result in then stored in an internal temporary field which can be read by
 * the calling thread.
 * <PRE>
 *   class TCalledThread extends Thread {
 *        public TCalledThread(TMyMonitor monitor) {
 *            this.monitor = monitor;
 *        }
 *        public void run() {
 *            // Run some initializations
 *            ....
 *            // Run an infinite loop
 *            while(true) {
 *                monitor.run();
 *            }
 *        }
 *        TMyMonitor monitor;
 *    }
 * </PRE>
 * @ibm-not-published
 */
public class BaseThreadMonitor {

    /**
     * Method call interface.
     */
    public static abstract class BaseMethod {
        /**
         * Execute the method.
         */
        public abstract void execute() throws Exception;

        /**
         * Filled if an exception is thrown.
         */
        public Exception exception;
    }


    /**
     * Thread monitor constructor.
     */
    public BaseThreadMonitor() {
    }

    /**
     * Call a method located in the called thread.
     * @param method the method to call. It must own its parameters as well as
     *        its result.
     * @return the method called (=parameter)
     */
    public synchronized BaseMethod call( BaseMethod method ) throws Exception {
    	synchronized (methodLock) {
    		 // Set the method to call
            this.method = method;
            this.method.exception = null;
            //System.out.println("requesting to execute "+method);
            methodLock.notify(); //in case the monitor was waiting for the request
            while (this.method != null) {
    			try {
    				methodLock.wait();
                } catch( InterruptedException e ) {}
    		}
    	}
    	//System.out.println("got the result for "+method);
        if (method.exception != null) {
        	throw method.exception;
        }
        return method;
    }

	protected Object beforeCallingMethod( BaseMethod method ) {
        return null;
    }

    protected void afterCallingMethod( BaseMethod method, Object param ) {
    }

    int i = 0;
    /**
     * Run a synchronous method.
     * This method is called by the called thread, in order to wait for a new
     * request and call the desired method.
     */
    public void run() {
        // Wait for a method call
    	waitTillMethodRequested();
        //System.out.println("executing "+method);
        Object param = beforeCallingMethod(method);
        try {
            // And call it
            method.execute();

        } catch (Exception e) {
        	method.exception = e;
        } finally {
            afterCallingMethod(method,param);
        }
        synchronized (methodLock) {
        	//System.out.println("done executing "+method);
        	method = null;
        	methodLock.notify();
        }
    }

    private void waitTillMethodRequested() {
    	synchronized(methodLock) {
    		while (method == null) {
    			//System.out.println("waiting for a new request");
    			try {
    				methodLock.wait();
                } catch( InterruptedException e ) {}
    		}
    	}
    }
    
    /*
     *
     */
    private BaseMethod method;
    Object methodLock = new Object ();
}

