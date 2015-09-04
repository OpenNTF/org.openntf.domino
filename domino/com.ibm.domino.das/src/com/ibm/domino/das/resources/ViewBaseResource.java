/*
 * © Copyright IBM Corp. 2011
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

package com.ibm.domino.das.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lotus.domino.Database;
import lotus.domino.View;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.das.utils.ErrorHelper;

public class ViewBaseResource extends AbstractDasResource{

    public static final String UNID = "unid"; // $NON-NLS-1$
    public static final String NAME = "name"; // $NON-NLS-1$

    public View getCurrentView(final String keyName, final String keyValue, final Database database) {

        View view = null;
        if ( UNID.equalsIgnoreCase(keyName) ) {
            // Find the view by UNID
            view = getCurrentViewByUnid(database, keyValue);
        }
        else if( NAME.equalsIgnoreCase(keyName)){
            // Find the view by name
            view = getCurrentViewByName(database, keyValue);
        }
        else {
            // This resource should always have a database context
            String msg = StringUtil.format("The query method {0} not supported", keyName); // $NLX-ViewBaseResource.Thequerymethod0notsupported-1$
            throw new WebApplicationException(ErrorHelper.createErrorResponse(msg, Response.Status.NOT_FOUND));
        }
        if ( view == null ) {
            // This resource should always have a database context
            throw new WebApplicationException(ErrorHelper.createErrorResponse("The URI must refer to an existing view.", Response.Status.NOT_FOUND)); // $NLX-ViewBaseResource.TheURImustrefertoanexistingview-1$
        }
        return view;
    }
        
}