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


import java.util.Vector;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.das.servlet.DasServlet;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.osgi.core.context.ContextInfo;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

public class AbstractDasResource {

    /**
     * Special note id for accessing the icon note.
     */
    public static final String ICON_NOTEID = "FFFF0010"; // $NON-NLS-1$

    /**
     * Item name in the icon note to indicate that the database api is enabled.
     */
    public static final String ITEM_ALLOW_RESTDBAPI = "$AllowRESTDbAPI"; // $NON-NLS-1$
    
    // the following 3 are access levels for the db as specified by
    // ITEM_ALLOW_RESTDBAPI
    public static final int DB_ACCESS_NONE  = 0;
    public static final int DB_ACCESS_VIEWS = 1;
    public static final int DB_ACCESS_VIEWS_DOCS    = 2;
    
    // The following are for the view access check
    public static final String ITEM_WEB_FLAGS = "$WebFlags"; //$NON-NLS-1$
    private final static char WEBFLAG_RESTAPIALLOWED = 'A';  //$NON-NLS-1$  
    
    /**
     * Get the current database object with access control check
     * @return the database if allowed for access
     */
    protected Database getDatabase(int minAccessNeeded){
        Database database = ContextInfo.getUserDatabase();
        if ( database != null ) {
            //Verify current access to the database
            Document doc = null;
            try{
                doc = database.getDocumentByID(ICON_NOTEID);
                if (doc != null) {
                    Vector<?> values = doc.getItemValue(ITEM_ALLOW_RESTDBAPI);
                    int value = DB_ACCESS_NONE;
                    if ( values != null && values.size() > 0 ) {
                        Object obj = values.get(0);
                        if ( obj instanceof String ) {
                            // SPR# DDEY9N8Q62: The 8.5.2 client can incorrectly change the item
                            // value to a string.  Tolerate that and assume the greatest level
                            // of access.
                            if ( "1".equals(obj) ) {
                                value = DB_ACCESS_VIEWS_DOCS;
                            }
                        }
                        else {
                            value = doc.getItemValueInteger(ITEM_ALLOW_RESTDBAPI);
                        }
                    }

                    if ((value > 0) && (value >= minAccessNeeded)) { 
                        return database;
                    }
                    else if( value == DB_ACCESS_NONE) {
                        // This resource should never have a database context
                        throw new WebApplicationException(ErrorHelper.createErrorResponse("The Domino data service is not enabled for this database.", Response.Status.FORBIDDEN)); // $NLX-AbstractDasResource.DatabasenotallowedforWebAccess-1$
                    }
                    else {
                        if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                            DasServlet.DAS_LOGGER.traceDebug("Invalid access level value set in the icon document: {0}", value); // $NON-NLS-1$
                        }

                        throw new WebApplicationException(ErrorHelper.createErrorResponse("Cannot access resource",  Response.Status.FORBIDDEN)); // $NLX-AbstractDasResource.Cannotaccessresource-1$
                    }
                }
            }catch(NotesException e){
                if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                    DasServlet.DAS_LOGGER.traceDebug(e, "Error accessing the database icon document"); // $NON-NLS-1$
                }

                throw new WebApplicationException(ErrorHelper.createErrorResponse("", Response.Status.INTERNAL_SERVER_ERROR));
            }finally {
                if(doc!=null){
                    try {
                        doc.recycle();
                        doc = null;
                    } catch (NotesException e) {
                        DasServlet.DAS_LOGGER.warn("Error recycling the icon document", e) ; // $NLW-AbstractDasResource.errortorecycletheICONdocument-1$
                    }
                }
            }
            
        }
        return null;
    }

    /**
     * Get the current View object by its Unid with access control check
     * @return the view object for the specified Unid
     */
    protected View getCurrentViewByUnid(Database db, String unid){
        View expectedView = null;
        Vector<?> views = null;
        try {
            views = db.getViews();
            int size = views.size();
            //if (isUNID(unid))
            if (unid != null)
            {                               
                for (int i = 0; i < size; i++)
                {
                    View view = (View) views.elementAt(i);
                    if (view.getUniversalID().equalsIgnoreCase(unid)){
                        doViewAccessCheck(view);                    
                        expectedView = view;
                    }                   
                }
            }
        } catch (NotesException e) {
            if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                DasServlet.DAS_LOGGER.traceDebug(e, "Error accessing the view by UNID: {0}", unid); // $NON-NLS-1$
            }
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }finally{
            //recycle the view objects that are not matching
            if(views!=null){
                views.remove(expectedView);
                for (int i = 0; i < views.size(); i++)
                {
                    View view = (View) views.elementAt(i);
                    if(view!=null){
                        try {
                            view.recycle();
                            view = null;
                        } catch (NotesException e) {
                            DasServlet.DAS_LOGGER.warn("Error recycling the view", e) ; // $NLW-AbstractDasResource.errortorecycletheview-1$
                        }
                    }
                }
            }
        }        
        
        return expectedView;
    }
    
    /**
     * 
     * Get the current View object by its name or aliases with access control check
     * @return the view object for the specified name/alias
     */
    protected View getCurrentViewByName(Database db, String name){
        try {
            View view = db.getView(name);
            if ( view == null ) {
                // This resource should always have a database context
                throw new WebApplicationException(ErrorHelper.createErrorResponse("The URI must refer to an existing view.", Response.Status.NOT_FOUND)); // $NLX-AbstractDasResource.TheURImustrefertoanexistingview-1$
            }           
            doViewAccessCheck(view);                    
            return view;
        } catch (NotesException e) {
            if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                DasServlet.DAS_LOGGER.traceDebug(e, "Error accessing the view by name: {0}",name); // $NON-NLS-1$
            }
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }        
    }    

    /**
     * Converts a string parameter to an integer.
     * 
     * @param paramName
     * @param paramValue
     * @return int
     */
    protected int getParameterInt(String paramName, String paramValue) {
    	return getParameterInt(paramName, paramValue, false);
    }    
    
    /**
     * Converts a string parameter to an integer.
     * 
     * @param paramName
     * @param paramValue
     * @param bHex - Set to true if the values can be hex.
     * @return int
     */
    protected int getParameterInt(String paramName, String paramValue, boolean bHex) {
        int iParam;        
        try {
        	if(bHex && paramValue.toLowerCase().startsWith("0x")){ //$NON-NLS-1$
        		iParam = Integer.parseInt(paramValue.substring(2), 16);
        	}else{
        		iParam = Integer.parseInt(paramValue);
        	}
        } catch (NumberFormatException nfe) {
            String msg = StringUtil.format("Invalid parameter {0}: {1}", paramName, paramValue); // $NLX-AbstractDasResource.Invalidparameter01-1$
            throw new WebApplicationException(
                    ErrorHelper.createErrorResponse(
                            new Exception(msg, nfe), Response.Status.BAD_REQUEST));
        }
        if (iParam < 0) {
            String msg = StringUtil.format("Invalid parameter {0}: {1}", paramName, paramValue); // $NLX-AbstractDasResource.Invalidparameter01.1-1$
            throw new WebApplicationException(
                    ErrorHelper.createErrorResponse(msg, Response.Status.BAD_REQUEST));
            
        }
        return iParam;
    }
    
    /**
     * For each view, there is an new option named "Allow Domino Data Service operations" for the "Web Access"
     * If the box is checked, means the access for it is allowed, otherwise, forbidden
     * @param view
     */
    private void doViewAccessCheck(View view) {
        Document viewDoc = null;
        //Just pass all requests for the current view, 
        try {
            viewDoc = view.getParent().getDocumentByUNID(view.getUniversalID());
            
            if (viewDoc != null) {
                String flags = viewDoc.getItemValueString(ITEM_WEB_FLAGS);
                if (flags.indexOf(WEBFLAG_RESTAPIALLOWED) < 0) {
                    if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                        DasServlet.DAS_LOGGER.traceDebug("Error accessing view: {0}",view.toString()); // $NON-NLS-1$
                    }
                    String msg = StringUtil.format("The Domino data service is not enabled for this view [{0}].", view.getName()); // $NLX-AbstractDasResource.View0notallowedforWebAccess-1$
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(msg, Response.Status.FORBIDDEN));                       
                }
            }
        } catch (NotesException e) {
            if(DasServlet.DAS_LOGGER.isTraceDebugEnabled()){
                DasServlet.DAS_LOGGER.traceDebug(e, "Error accessing view: {0}",view.toString()); // $NON-NLS-1$
            }
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        } finally {
            if(viewDoc!=null){
                try {
                    viewDoc.recycle();
                    viewDoc = null;
                } catch (NotesException e) {
                    DasServlet.DAS_LOGGER.warn("Error recycling the view document", e) ; // $NLW-AbstractDasResource.errortorecycletheviewdocument-1$
                }
            }
        }        
    }    
    
}