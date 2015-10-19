/*
 * © Copyright IBM Corp. 2013
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

import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_CORE;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_PW_STATS;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lotus.domino.DateTime;
import lotus.domino.Directory;
import lotus.domino.DirectoryNavigator;
import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.commons.json.JsonConstants;
import com.ibm.domino.commons.util.BackendUtil;
import com.ibm.domino.das.service.CoreService;
import com.ibm.domino.osgi.core.context.ContextInfo;

@Path(PATH_SEGMENT_CORE + "/" + PATH_SEGMENT_PW_STATS)
public class PasswordStatisticsResource {
    
    private static final String JSON_CHANGE_INTERVAL = "changeinterval"; // $NON-NLS-1$
    private static final String JSON_INTERNET_PASSWORD = "internetpassword"; // $NON-NLS-1$
    private static final String JSON_NOTES_PASSWORD = "notespassword"; // $NON-NLS-1$
    private static final String JSON_EXPIRE_DATE = "expiredate"; // $NON-NLS-1$
    private static final String JSON_CHANGE_DATE = "lastchangedate"; // $NON-NLS-1$
    private static final String JSON_EXPIRES = "expires"; // $NON-NLS-1$
    private static final String JSON_SYNC_WITH_NOTES = "syncwithnotes"; // $NON-NLS-1$
    private static final String JSON_CHANGE_OVER_HTTP = "changeoverhttp"; // $NON-NLS-1$
    
    private static final int PD_CHANGE_INTERVAL = 0;
    private static final int PD_NOTES_CHANGE_DATE = 1;
    private static final int PD_INTERNET_CHANGE_DATE = 2;

    private Vector<String> s_lookupItems = lookupItems();
    private long ONE_DAY = 1000L * 60 * 60 * 24;
    
    /**
     * Gets password statistics (change date, expire date, etc.)
     * 
     * @return
     */
    @GET
    public Response getPassword() {
        
        String jsonEntity = null;
        
        CoreService.verifyUserContext();
        
        try {
            Session session = ContextInfo.getUserSession();
            
            // Get the password stats
            
            JsonJavaObject obj = getPasswordStats(session);
            
            // Serialize the JSON
            
            StringBuilder sb = new StringBuilder();
            JsonGenerator.Generator generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
            generator.toJson(obj);
            jsonEntity = sb.toString();
        } 
        catch (NotesException e) {
            throw new WebApplicationException(CoreService.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        } 
        catch (JsonException e) {
            throw new WebApplicationException(CoreService.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        } 
        catch (IOException e) {
            throw new WebApplicationException(CoreService.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }
        
        ResponseBuilder builder = Response.ok();
        if ( jsonEntity != null ) {
            builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
        }
        
        Response response = builder.build();
        return response;
    }
    
    /**
     * Internal implementation of password statistics.
     * 
     * <p>TODO: Move this to com.ibm.domino.commons.model.
     * 
     * @param session
     * @return
     * @throws NotesException
     */
    private JsonJavaObject getPasswordStats(Session session) throws NotesException {

        JsonJavaObject obj = null;
        Document document = null;
        
        try {
            boolean notesPwdExpires = false;
            boolean internetPwdExpires = false;
            boolean internetPwdSync = false;
            boolean internetPwdChangeHttp = true; // Default is true
            int changeInterval = -1;

            // Get the effective user's name
            
            String user = session.getEffectiveUserName();
            Name name = session.createName(user);
    
            // Get the password data from the person record
            
            Object personData[] = getPersonRecordData(session, user);
            
            // Get the security policy for this user
            
            Vector values = null;
            document = session.getUserPolicySettings("", name.getAbbreviated(), Session.POLICYSETTINGS_SECURITY);
            
            if ( document == null ) {
                
                // There is no policy.  Use settings from person doc.
                //
                // Note these assumptions: 1) The person doc only affects Notes password expiration 
                // (not internet passwords), and 2) the Notes password expires only if the CheckPassword
                // item == "1" and the password change interval > 0.
                
                if ( personData != null && personData[PD_CHANGE_INTERVAL] != null ) {
                    changeInterval = (Integer)personData[PD_CHANGE_INTERVAL];
                    if ( changeInterval > 0 ) {
                        notesPwdExpires = true;
                    }
                }
            }
            else {
                
                // Get the expiration policy
                
                values = document.getItemValue("PwdExp"); // $NON-NLS-1$
                if ( values != null && values.size() > 0 && values.get(0) instanceof String ) {
                    String strValue = (String)values.get(0);
                    if ( "1".equals(strValue) ) {
                        notesPwdExpires = true;
                    }
                    else if ( "2".equals(strValue) ) {
                        internetPwdExpires = true;
                    }
                    else if ( "3".equals(strValue) ) {
                        notesPwdExpires = true;
                        internetPwdExpires = true;
                    }
                }
            
                // Get the change interval
                
                if ( notesPwdExpires || internetPwdExpires ) {
                    values = document.getItemValue("PasswordChangeInterval"); // $NON-NLS-1$
                    if ( values != null && values.size() > 0 && values.get(0) instanceof Double) {
                        Double dValue = (Double)values.get(0);
                        changeInterval = dValue.intValue();
                    }
                }
                
                // Get the other internet password settings
            
                values = document.getItemValue("PwdSync"); // $NON-NLS-1$
                if ( values != null && values.size() > 0 && values.get(0) instanceof String) {
                    String strValue = (String)values.get(0);
                    if ( "1".equals(strValue) ) {
                        internetPwdSync = true;
                    }
                }

                values = document.getItemValue("PwdAlwHTTP"); // $NON-NLS-1$
                if ( values != null && values.size() > 0 && values.get(0) instanceof String) {
                    String strValue = (String)values.get(0);
                    if ( "0".equals(strValue) ) {
                        internetPwdChangeHttp = false;
                    }
                }
            }
            
            // Create the JSON object
            
            obj = new JsonJavaObject();

            JsonJavaObject notes = new JsonJavaObject();
            notes.putBoolean(JSON_EXPIRES, notesPwdExpires);
            if ( personData != null && personData[PD_NOTES_CHANGE_DATE] != null ) {
                Date date = (Date)personData[PD_NOTES_CHANGE_DATE];
                notes.putString(JSON_CHANGE_DATE, JsonConstants.ISO8601_UTC.format(date));
                if ( notesPwdExpires && changeInterval > 0 ) {
                    Date expireDate = new Date(date.getTime() + (ONE_DAY*changeInterval));
                    notes.putString(JSON_EXPIRE_DATE, JsonConstants.ISO8601_UTC.format(expireDate));
                }
            }
            obj.putObject(JSON_NOTES_PASSWORD, notes);
            
            JsonJavaObject internet = new JsonJavaObject();
            internet.putBoolean(JSON_EXPIRES, internetPwdExpires);
            if ( personData != null && personData[PD_INTERNET_CHANGE_DATE] != null ) {
                Date date = (Date)personData[PD_INTERNET_CHANGE_DATE];
                internet.putString(JSON_CHANGE_DATE, JsonConstants.ISO8601_UTC.format(date));
                internet.putBoolean(JSON_SYNC_WITH_NOTES, internetPwdSync);
                internet.putBoolean(JSON_CHANGE_OVER_HTTP, internetPwdChangeHttp);
                if ( internetPwdExpires && changeInterval > 0 ) {
                    Date expireDate = new Date(date.getTime() + (ONE_DAY*changeInterval));
                    internet.putString(JSON_EXPIRE_DATE, JsonConstants.ISO8601_UTC.format(expireDate));
                }
            }
            obj.putObject(JSON_INTERNET_PASSWORD, internet);

            if ( changeInterval > 0 ) {
                obj.putInt(JSON_CHANGE_INTERVAL, changeInterval);
            }
        }
        finally {
            BackendUtil.safeRecycle(document);
        }

        return obj;
    }
    
    private static Vector<String> lookupItems() {
        Vector<String> lookupItems = new Vector<String>();
        
        lookupItems.addElement("FullName"); //$NON-NLS-1$
        lookupItems.addElement("ShortName"); //$NON-NLS-1$
        lookupItems.addElement("CheckPassword"); //$NON-NLS-1$
        lookupItems.addElement("PasswordChangeInterval"); //$NON-NLS-1$
        lookupItems.addElement("PasswordChangeDate"); //$NON-NLS-1$
        lookupItems.addElement("HTTPPasswordChangeDate"); //$NON-NLS-1$
        
        return lookupItems;
    }
    
    /**
     * Gets the last change dates from the person record.
     * 
     * @param session
     * @param canonicalName
     * @return
     * @throws NotesException
     */
    private Object[] getPersonRecordData(Session session, String canonicalName) throws NotesException {
        Object personData[] = null;
        Directory lookupDir = null;
        
        try {
            // Find the user
            
            DirectoryNavigator dirNav = null;
            lookupDir = session.getDirectory();
            if ( lookupDir != null ) {
                Vector<String> vName = new Vector<String>();
                vName.addElement(canonicalName);

                dirNav = lookupDir.lookupNames("($Users)", vName, s_lookupItems, true);  //$NON-NLS-1$
            }
            

            // Digest the results of the lookup
            
            Integer changeInterval = -1;
            DateTime notesChangeDate = null;
            DateTime internetChangeDate = null;
            if( dirNav != null && dirNav.getCurrentMatches() != 0 ){
                Vector value = null;
                value = dirNav.getFirstItemValue();
                String fullName = (String)value.elementAt(0);
                
                value = dirNav.getNextItemValue();
                String shortName = (String)value.elementAt(0);
                
                value = dirNav.getNextItemValue();
                String checkPassword = null;
                if ( value != null && value.size() > 0 && value.elementAt(0) instanceof String ) {
                    checkPassword = (String)value.elementAt(0);
                }
                
                value = dirNav.getNextItemValue();
                if ( "1".equals(checkPassword) && value != null && value.size() > 0 && value.elementAt(0) instanceof Double ) {
                    changeInterval = ((Double)value.elementAt(0)).intValue();
                }
                
                value = dirNav.getNextItemValue();
                if ( value != null && value.size() > 0  && value.elementAt(0) instanceof DateTime ) {
                    notesChangeDate = (DateTime)value.elementAt(0);
                }

                value = dirNav.getNextItemValue();
                if ( value != null && value.size() > 0  && value.elementAt(0) instanceof DateTime ) {
                    internetChangeDate = (DateTime)value.elementAt(0);
                }
            }

            // Create the return array
            
            if ( changeInterval > 0 || notesChangeDate != null || internetChangeDate != null ) {
                personData = new Object[3];
                if ( changeInterval != -1 ) {
                    personData[PD_CHANGE_INTERVAL] = changeInterval;
                }
                if ( notesChangeDate != null ) {
                    personData[PD_NOTES_CHANGE_DATE] = notesChangeDate.toJavaDate();
                }
                if ( internetChangeDate != null ) {
                    personData[PD_INTERNET_CHANGE_DATE] = internetChangeDate.toJavaDate();
                }
            }
        }
        finally {
            BackendUtil.safeRecycle(lookupDir);
        }
        
        return personData;
    }
}