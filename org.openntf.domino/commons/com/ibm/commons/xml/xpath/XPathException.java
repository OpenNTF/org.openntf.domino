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

package com.ibm.commons.xml.xpath;

import com.ibm.commons.xml.XMLException;


/**
 * CML exception.
 * @author Philippe Riand
 */
public class XPathException extends XMLException  {

    /**
     *
     */
    public XPathException(Throwable nextException) {
        super(nextException);
    }
    public XPathException(Throwable nextException, String msg) {
        super(nextException,msg);
    }
    public XPathException( Throwable nextException, String msg, Object p1 ) {
        super(nextException,msg,p1);
    }
    public XPathException( Throwable nextException, String msg, Object p1, Object p2 ) {
        super(nextException,msg,p1,p2);
    }
    public XPathException( Throwable nextException, String msg, Object p1, Object p2, Object p3 ) {
        super(nextException,msg,p1,p2,p3);
    }
    public XPathException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4 ) {
        super(nextException,msg,p1,p2,p3,p4);
    }
    public XPathException( Throwable nextException, String msg, Object p1, Object p2, Object p3, Object p4, Object p5 ) {
        super(nextException,msg,p1,p2,p3,p4,p5);
    }
}
