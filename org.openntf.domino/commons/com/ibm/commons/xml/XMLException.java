/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.commons.xml;

import com.ibm.commons.util.AbstractException;

/**
 * XML Exception. A standard Exception implementation that allows for Exception chaining.
 * 
 * @ibm-api
 */
public class XMLException extends AbstractException  {

    /**
     * 
     */
    private static final long serialVersionUID = 801731453454574527L;
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     */
    public XMLException(Throwable nextException) {
        super(nextException);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     */
    public XMLException(Throwable nextException, String msg) {
        super(nextException,msg);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     * @param p1
     *          a parameter to be added to the message
     */
    public XMLException( Throwable nextException, String msg, Object p1 ) {
        super(nextException,msg,p1);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     * @param p1
     *          a parameter to be added to the message
     * @param p2
     *          a parameter to be added to the message
     */
    public XMLException( Throwable nextException, String msg, Object p1, Object p2 ) {
        super(nextException,msg,p1,p2);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     * @param p1
     *          a parameter to be added to the message
     * @param p2
     *          a parameter to be added to the message
     * @param p3
     *          a parameter to be added to the message
     */
    public XMLException( Throwable nextException, String msg, Object p1, Object p2, Object p3 ) {
        super(nextException,msg,p1,p2,p3);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     * @param p1
     *          a parameter to be added to the message
     * @param p2
     *          a parameter to be added to the message
     * @param p3
     *          a parameter to be added to the message
     * @param p4
     *          a parameter to be added to the message
     */
    public XMLException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4 ) {
        super(nextException,msg,p1,p2,p3,p4);
    }
    /**
     * Constructs a new instance of this object.
     * 
     * @param nextException
     *          an earlier exception that was caught which is to be chained for use higher in the exception hierarchy
     * @param msg
     *          a message that is to be printed with this exception
     * @param p1
     *          a parameter to be added to the message
     * @param p2
     *          a parameter to be added to the message
     * @param p3
     *          a parameter to be added to the message
     * @param p4
     *          a parameter to be added to the message
     * @param p5
     *          a parameter to be added to the message
     */
    public XMLException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4, Object p5 ) {
        super(nextException,msg,p1,p2,p3,p4,p5);
    }
}
