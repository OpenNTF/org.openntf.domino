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

import java.io.PrintWriter;


/**
 * Abstract base class for global runtime exceptions.
 * This class is the main class for all the runtime exception. It provides several enhancements to 
 * the Java Exception class, by providing message formating with parameters. All its constructors
 * also have a first argument which is the root exception, which is an easy way for 
 * implementing the "exception chaining" pattern.
 * @ibm-api
 */
public class AbstractRuntimeException extends RuntimeException implements IExceptionEx {

    private static final long serialVersionUID = 8729376244073119553L;
    
    /**
     * Create a new exception.
     * The exception is not featuring a message
     * @param nextException the cause exception
     * @ibm-api
     */
    public AbstractRuntimeException(Throwable nextException) {
        this(nextException, nextException==null?"":nextException.getMessage() ); //$NON-NLS-1$
    }
    
    /**
     * Create a new exception.
     * The message is formatted using the StringUtil.format rules.
     * @param nextException the cause exception
     * @param msg the exception message
     * @ibm-api
     */
    public AbstractRuntimeException(Throwable nextException, String msg, Object...params) {
        super(StringUtil.format(msg,params));
        initCause(nextException);
    }

    /**
     * Init the exception cause.
     * The message is formatted using the StringUtil.format rules.
     * @param nextException the cause exception
     * @param msg the exception message
     * @ibm-api
     */
    public static Throwable initCause(Throwable ext, Throwable cause) {
        ext.initCause(cause);
        return ext;
    }
    
    /**
     * Get the exception cause.
     * The message is formatted using the StringUtil.format rules.
     * @param nextException the cause exception
     * @param msg the exception message
     * @ibm-api
     */
    public static Throwable getCause(Throwable ext) {
        return ext.getCause();
    }    

	/**
	 * Print some extra information.
	 * @param err
     * @ibm-api
	 */
	public void printExtraInformation(PrintWriter err) {
		// Nothing...
	}
    
/*    
    public static void main( String[] args ) {
        check(new NullPointerException()); 
        check(new TException(new NullPointerException())); 
        check(new TException(new TException(new NullPointerException()))); 
    }
    private static void check(Throwable t) {
        try {
            throw t;
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }
*/    
}
