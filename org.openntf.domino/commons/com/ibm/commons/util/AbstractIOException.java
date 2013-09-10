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

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Define a global exception.
 * @ibm-api
 */
public class AbstractIOException extends IOException implements IExceptionEx {

    private static final long serialVersionUID = 253893307550263241L;
    private static boolean hasCause = false;
    
    static {
        // Check if we are running on JDK 1.4 or above for exception chaining
        Class<Throwable> c = Throwable.class;
        try {
            c.getMethod("initCause", new Class[]{Throwable.class}); //$NON-NLS-1$
            hasCause = true;
        } catch( NoSuchMethodException ex ) {}
    }
    
    private Throwable causeMember = this;
	
    /**
     * @ibm-api
     */
    public AbstractIOException(Throwable nextException) {
        this(nextException, nextException==null?"":nextException.getMessage() ); //$NON-NLS-1$
    }
    /**
     * @ibm-api
     */
    public AbstractIOException(Throwable nextException, String msg) {
        super(StringUtil.format(msg));
        initCause(nextException);
    }
    /**
     * @ibm-api
     */
    public AbstractIOException( Throwable nextException, String msg, Object p1 ) {
        this( nextException,format(msg, p1, null, null, null, null) );
    }
    /**
     * @ibm-api
     */
    public AbstractIOException( Throwable nextException, String msg, Object p1, Object p2 ) {
        this( nextException,format(msg, p1, p2, null, null, null) );
    }
    /**
     * @ibm-api
     */
    public AbstractIOException( Throwable nextException, String msg, Object p1, Object p2, Object p3 ) {
        this( nextException,format(msg, p1, p2, p3, null, null) );
    }
    /**
     * @ibm-api
     */
    public AbstractIOException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4 ) {
        this( nextException,format(msg, p1, p2, p3, p4, null) );
    }
    /**
     * @ibm-api
     */
    public AbstractIOException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4, Object p5 ) {
        this( nextException,format(msg, p1, p2, p3, p4, p5) );
    }
    
    private static String format( String msg, Object p1, Object p2, Object p3, Object p4, Object p5 ) {
        return StringUtil.format(msg, p1, p2, p3, p4, p5);
    }

    /**
     * @ibm-api
     */
    public static Throwable initCause(Throwable ext, Throwable cause) {
        if(hasCause) {
            ext.initCause(cause);
        } else {
            if(ext instanceof AbstractIOException) {
                ((AbstractIOException)ext).initCause(cause);
            }
        }
        return ext;
    }

    /**
     * @ibm-api
     */
    public Throwable initCause(Throwable cause) {
        if(hasCause) {
            return super.initCause(cause);
        } else {
            if(this.causeMember!=this) {
                throw new IllegalStateException("Can't overwrite the cause of exception"); //$NLS-AbstractIOException.TException.OverwriteCause.Exception-1$
            }
            if(cause==this) {
                throw new IllegalArgumentException("Cannot assign self as the cause of exception"); //$NLS-AbstractIOException.TException.AssignSelfAsCause.Exception-1$
            }
            this.causeMember = cause;
            return this;
        }
    }
    
    /**
     * @ibm-api
     */
    public static Throwable getCause(Throwable ext) {
        if(hasCause) {
            return ext.getCause();
        } else {
            if(ext instanceof AbstractIOException) {
                ((AbstractIOException)ext).getCause();
            }
            return null;
        }
    }
    
    /**
     * @ibm-api
     */
    public Throwable getCause() {
        if(hasCause) {
            return super.getCause();
        } else {
            return causeMember==this ? null : causeMember;
        }
    }
        
    /**
     * @ibm-api
     */
    public void printStackTrace() { 
        if(hasCause) {
            super.printStackTrace();
        } else {
            printStackTrace(System.err);
        }
    }

    /**
     * @ibm-api
     */
    public void printStackTrace(PrintStream s) {
        if(hasCause) {
            super.printStackTrace(s);
        } else {
            synchronized(s) {
                for( Throwable t=this; t!=null; ) {
                    if(t!=this) {
                        s.println("Caused by:"); //$NON-NLS-1$
                    }
                    if( t instanceof AbstractIOException ) {
                        AbstractIOException te = (AbstractIOException)t; 
                        te.superPrintStackTrace(s);
                        t = te.getCause();
                    } else {
                        t.printStackTrace(s);
                        t = null;
                    }
                }
            }
        }
    }
    private void superPrintStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    /**
     * @ibm-api
     */
    public void printStackTrace(PrintWriter w) {
        if(hasCause) {
            super.printStackTrace(w);
        } else {
            synchronized(w) {
                for( Throwable t=this; t!=null; ) {
                    if(t!=this) {
                        w.println("Caused by:"); //$NON-NLS-1$
                    }
                    if( t instanceof AbstractIOException ) {
                        AbstractIOException te = (AbstractIOException)t; 
                        te.superPrintStackTrace(w);
                        t = te.getCause();
                    } else {
                        t.printStackTrace(w);
                        t = null;
                    }
                }
            }
        }
    }
    private void superPrintStackTrace(PrintWriter w) {
        super.printStackTrace(w);
    }

    /**
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
