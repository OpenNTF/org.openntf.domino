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

package com.ibm.domino.das.service;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.apache.wink.common.WinkApplication;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.commons.model.IGatekeeperProvider;
import com.ibm.domino.commons.model.ProviderFactory;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.das.utils.ScnContext;
import com.ibm.domino.das.utils.StatsContext;
import com.ibm.domino.osgi.core.context.ContextInfo;
import com.ibm.xsp.acl.NoAccessSignal;

public class RestService extends WinkApplication {
    
    public static final String SERVICE_PATH = "/api";//$NON-NLS-1$
    public static final String URL_PARAM_OWNER = "owner"; //$NON-NLS-1$
    
    public RestService() {
        super();
    }
    
    /**
     * Frees resources associated with this service.
     * 
     * <p>This method is called once as the DAS servlet itself is being destroyed.
     * A subclass can override this method to free resources.  A subclass should
     * free resources quickly.  Otherwise, this method can block shutdown of 
     * the HTTP server.
     */
    public void destroy() {
    }

    public static Response createErrorResponse(String message, Response.Status status) {

        return (ErrorHelper.createErrorResponse(message, status));
    }
    

    public static Response createErrorResponse(Exception e, Response.Status status) {

        return (ErrorHelper.createErrorResponse(e, status));
    }
    

    public static Response createErrorResponse(Throwable e) {
        return (ErrorHelper.createErrorResponse(e));
    }
    
    /**
     * Gets the database from the request context.
     * 
     * @return
     */
    public static Database getUserDatabase() {
        
        Database database = ContextInfo.getUserDatabase();
        if ( database == null ) {
            throw new WebApplicationException(createErrorResponse("No database context.",  // $NLX-RestService.Nodatabasecontext-1$
                        Response.Status.NOT_FOUND));
        }
        
        return database;
    }

    /**
     * Verifies the current request has a database context.
     */
    public static void verifyDatabaseContext() {
        getUserDatabase();
    }

    /**
     * Verifies the current request has NO database context.
     */
    public static void verifyNoDatabaseContext() {
        
        Database database = ContextInfo.getUserDatabase();
        if ( database != null ) {
            throw new WebApplicationException(createErrorResponse("Database context not allowed.",  // $NLX-RestService.Databasecontextnotallowed-1$
                        Response.Status.NOT_FOUND));
        }
    }
    
    /**
     * Verifies the current request has a user context (not Anonymous).
     */
    public static void verifyUserContext() {
        
        String user = null;
        Session session = ContextInfo.getUserSession();
        if ( session != null ) {
            try {
                user = session.getEffectiveUserName();
            } 
            catch (NotesException e) {
                // Ignore this
            }
        }
        
        if ( user == null || user.equals("Anonymous")) { // $NON-NLS-1$
            throw new NoAccessSignal("Need user context"); // $NLX-RestService.Needusercontext-1$
        }

    }

    /**
     * Parses a query parameter with an integer value.
     * 
     * @param paramName
     * @param paramValue
     * @param bHex
     * @return
     */
    public static int getParameterInt(String paramName, String paramValue, boolean bHex) {
        int iParam;        
        try {
            if(bHex && paramValue.toLowerCase().startsWith("0x")) // $NON-NLS-1$
                iParam = Integer.parseInt(paramValue.substring(2), 16);
            else
                iParam = Integer.parseInt(paramValue);
        } catch (NumberFormatException nfe) {
            String msg = MessageFormat.format("Invalid {0} parameter: {1}", paramName, paramValue); // $NLX-RestService.Invalid0parameter1-1$
            throw new WebApplicationException(
                    createErrorResponse(new Exception(msg, nfe), Response.Status.BAD_REQUEST));
        }

    return iParam;
    }
 
    /**
     * Parses a query parameter.  The value is expected to be a list of 
     * comma-separated strings.
     * 
     * <p>Warning:  This method returns a list of lower case strings.
     * 
     * @param paramName
     * @param paramValue
     * @return
     */
    public static ArrayList<String> getParameterStringList(String paramName, String paramValue) {
        // Check the format of parameter list
        Pattern pattern = Pattern.compile("(\\s*[\\w-]+\\s*,)*\\s*([\\w-]+)\\s*"); // $NON-NLS-1$
        Matcher matcher = pattern.matcher(paramValue);
        if(!matcher.matches()){
            String msg = MessageFormat.format("Invalid {0} parameter: {1} ", paramName, paramValue); // $NLX-RestService.Invalid0parameter1.1-1$
            throw new WebApplicationException(
                    createErrorResponse(new Exception(msg), Response.Status.BAD_REQUEST));            
        }
        
        // If match, at least have one field
        ArrayList<String> vaueList = new ArrayList<String>();
        String[] valueArray = paramValue.split(",");
        for(int i = 0; i< valueArray.length; i++){
            vaueList.add(valueArray[i].trim().toLowerCase());
        }
        
        return vaueList;
    }
    
    /**
     * Helper method to adapt an "on-premise" URI to SCN.
     * 
     * @param uri
     * @return
     */
    public static URI adaptUriToScn(URI uri) {
        URI adapted = uri;
        
        if ( ScnContext.getCurrentInstance().isHideDbPath() ) {
            
            // Remove database from path
            String sUri = uri.toString();
            int index = sUri.indexOf(SERVICE_PATH + "/");
            if ( index != -1 ) {
                sUri = sUri.substring(index);

                if ( uri.isAbsolute() ) {
                    // SPR# WJBJ9MZ9U9: Since SSL is terminated at the F5, we can't use
                    // uri.getScheme() and uri.getAuthority() here.  Hard code the protocol 
                    // to https and port to 443.
                    String protocol = "https";  // $NON-NLS-1$
                    String authority = uri.getHost() + ":443";
                    
                    // Add protocol, server & port back to the URI
                    sUri = protocol + "://" + authority + sUri;
                }
            }

            // Add owner parameter (if necessary)
            String ownerId = ScnContext.getCurrentInstance().getOwnerId();
            if ( StringUtil.isNotEmpty(ownerId) ) {
                sUri += "?" + URL_PARAM_OWNER + "=" + ownerId;
            }
            
            adapted = UriHelper.create(sUri, false);
        }
        
        return adapted;
    }

    /**
     * Common bookkeeping before handling a request.
     * 
     * <p>A resource class can call this method before doing request-specific processing.
     * Currently, this method sets the statistics category and/or checks gatekeeper.
     * In the future, other pre-processing may be added.
     * 
     * @param feature
     * @param category
     */
    public static void beforeRequest(int feature, String category) {

        if ( category != null ) {
            StatsContext.getCurrentInstance().setRequestCategory(category);
        }
    
        if ( feature != 0 ) {
            ScnContext ctx = ScnContext.getCurrentInstance();
            String customerId = ctx.getCustomerId();
            String userId = ctx.getUserId();
            
            IGatekeeperProvider provider = ProviderFactory.getGatekeeperProvider();
            if ( ! provider.isFeatureEnabled(feature, customerId, userId) ) {
                String msg = MessageFormat.format("Feature {0} is disabled.", feature); // $NLX-RestService.Feature0isdisabled-1$
                throw new WebApplicationException(ErrorHelper.createErrorResponse(msg, Response.Status.FORBIDDEN));
            }
        }
    }
    
}