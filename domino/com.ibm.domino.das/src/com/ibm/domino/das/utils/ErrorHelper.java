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
package com.ibm.domino.das.utils;

import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_CODE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_DATA;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_MESSAGE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_TEXT;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lotus.domino.NotesException;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;
import com.ibm.domino.services.util.JsonWriter;


/** Resource handler class 
 * @ibm-not-published
 * **/

public class ErrorHelper {
 
    
    public static final LogMgr DAS_LOGGER = Log.load("com.ibm.domino.das");  //$NON-NLS-1$
    
    /**
     * Creates a JSON response with the specified message in the body and 
     * the specified status code.
     * 
     * @param message
     * @param status
     * @return
     */
    public static Response createErrorResponse(String message, Response.Status status) {
        return createErrorResponse(message, status, null);
    }

    public static Response createErrorResponse(String message, Response.Status status, Map<String, Object> extraProps) {
        DAS_LOGGER.traceEntry("", "createErrorResponse");  //$NON-NLS-1$ $NON-NLS-2$
        ResponseBuilder builder = Response.status(status);
        builder.type(MediaType.APPLICATION_JSON);
        String jsonEntity = null;
        StringWriter writer = new StringWriter();
        Boolean compact = false;
        JsonWriter jWriter = new JsonWriter(writer, compact);
        
        try {
            try {
                jWriter.startObject();
                writeProperty(jWriter, ATTR_CODE, status.getStatusCode());
                writeProperty(jWriter, ATTR_TEXT, status.getReasonPhrase());
                
                // Write extra properties
                
                writeExtraProperties(jWriter, extraProps);
                
                // Write message
                
                if  (message != null)
                    writeProperty(jWriter, "message", message); // $NON-NLS-1$
            } 
            finally {
                jWriter.endObject();
            }
            StringBuffer buffer = writer.getBuffer();
            jsonEntity = buffer.toString();
            builder.entity(jsonEntity);
        }
        catch (IOException ex) {
            DAS_LOGGER.warn(ex, "Error creating response.");  //$NON-NLS-1$
        }
        Response response = builder.build();
        DAS_LOGGER.traceExit("", "createErrorResponse", response); //$NON-NLS-1$ $NON-NLS-2$
        
        return response;
    }
    
    /**
     * Creates a JSON response with an exception stack trace in the body and
     * the specified status code.
     * 
     * @param e
     * @param status
     * @return
     */
    public static Response createErrorResponse(Exception e, Response.Status status) {
        return createErrorResponse(e, status, null);
    }

    public static Response createErrorResponse(Exception e, Response.Status status, Map<String, Object> extraProps) {
        
        DAS_LOGGER.traceEntry("", "createErrorResponse");  //$NON-NLS-1$ $NON-NLS-2$
        ResponseBuilder builder = Response.status(status);
        builder.type(MediaType.APPLICATION_JSON);
        String jsonEntity = null;
        StringWriter writer = new StringWriter();
        Boolean compact = false;
        JsonWriter jWriter = new JsonWriter(writer, compact);
        
        try {
            try {
                jWriter.startObject();
                writeProperty(jWriter, ATTR_CODE, status.getStatusCode());
                writeProperty(jWriter, ATTR_TEXT, status.getReasonPhrase());
                
                // Write extra properties
                
                writeExtraProperties(jWriter, extraProps);
                
                // Write the exception
                
                writeException(jWriter, e);
            } 
            finally {
                jWriter.endObject();
            }
            StringBuffer buffer = writer.getBuffer();
            jsonEntity = buffer.toString();
            builder.entity(jsonEntity);
        }
        catch (IOException ex) {
            DAS_LOGGER.warn(ex, "Error creating response."); // $NLW-ErrorHelper.ErrorCreatingResponse-1$
        }
        Response response = builder.build();
        DAS_LOGGER.traceExit("", "createErrorResponse", response); //$NON-NLS-1$ $NON-NLS-2$
        
        return response;
    }
 

    public static Response createErrorResponse(Throwable e) {
        String message = null;
        
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            e.printStackTrace(new PrintWriter(os, true));
            os.flush();
            message = os.toString();
        }
        catch (IOException ioe) {
            message = e.getMessage();
        }
        
        return createErrorResponse(message, Response.Status.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Writes a JSON int property.
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */     
    public static void writeProperty(JsonWriter jwriter, String propName, int propValue) throws IOException {
        jwriter.startProperty(propName);
        jwriter.outIntLiteral(propValue);
        jwriter.endProperty();
    }
    
    /**
     * Writes a JSON string property.
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */     
    public static void writeProperty(JsonWriter jwriter, String propName, String propValue) throws IOException {
        jwriter.startProperty(propName);
        jwriter.outStringLiteral(propValue);
        jwriter.endProperty();
    }
    
    /**
     * Writes a JSON exception property.
     * 
     * @param jwriter
     * @param throwable
     * @throws IOException
     */     
    public static void writeException(JsonWriter jwriter, Throwable throwable) throws IOException {
        
        if (throwable == null) {
            return;
        }
        
        // message
        jwriter.startProperty(ATTR_MESSAGE);        
        String message = ""; 
        Throwable t = throwable;
        while ((message == null || message.length() == 0) && t != null)
        {
            if (t instanceof NotesException)
                message = ((NotesException)t).text;
            else
                message = t.getMessage();

            t = t.getCause();               
        }
        if (message == null)
            jwriter.outStringLiteral("");  //$NON-NLS-1$
        else
            jwriter.outStringLiteral(message);
        
        jwriter.endProperty();
        
        // type
        writeProperty(jwriter, ATTR_TYPE, ATTR_TEXT);
        
        // data
        jwriter.startProperty(ATTR_DATA);
        if ( ScnContext.getCurrentInstance().isScn() ) {
            Throwable cause = throwable.getCause();
            String data = null;
            if ( cause == null ) {
                data = getExceptionSummary(throwable);
            }
            else {
                data = MessageFormat.format("{0} caused by {1}",  // $NLX-ErrorHelper.0causedby1-1$
                        getExceptionSummary(throwable), getExceptionSummary(cause));
            }
            jwriter.outStringLiteral(data);
        }
        else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);      
            jwriter.outStringLiteral(sw.toString());
        }
        jwriter.endProperty();
    }

    private static void writeExtraProperties(JsonWriter jWriter, Map<String, Object> extraProps) throws IOException {
        if ( extraProps != null ) {
            Set<String> keys = extraProps.keySet();
            if ( keys != null ) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object obj = extraProps.get(key);
                    if ( obj instanceof String ) {
                        writeProperty(jWriter, key, (String)obj);
                    }
                    else if ( obj instanceof Integer ) {
                        writeProperty(jWriter, key, (Integer)obj);
                    }
                }
            }
        }
        
    }
    
    private static String getExceptionSummary(Throwable t) {
        String message = null;
        if (t instanceof NotesException)
            message = ((NotesException)t).text;
        else
            message = t.getMessage();
        
        return MessageFormat.format("{0} [{1}]", t.getClass().getName(), 
                    message == null ? "" : message);
    }
}
