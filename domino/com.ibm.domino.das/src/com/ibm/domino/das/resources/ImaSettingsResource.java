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
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_IMA_SETTINGS;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ibm.commons.util.io.base64.Base64;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.commons.json.JsonConstants;
import com.ibm.domino.commons.model.IImaSettingsProvider;
import com.ibm.domino.commons.model.ImaSettings;
import com.ibm.domino.commons.model.ModelException;
import com.ibm.domino.commons.model.ProviderFactory;
import com.ibm.domino.das.service.CoreService;
import com.ibm.domino.osgi.core.context.ContextInfo;

@Path(PATH_SEGMENT_CORE + "/" + PATH_SEGMENT_IMA_SETTINGS)
public class ImaSettingsResource {
    
    private static final String JSON_FREEBUSY = "freebusy"; // $NON-NLS-1$
    private static final String JSON_PW_CHANGE = "passwordchange"; // $NON-NLS-1$
    
    /**
     * Gets the IBM mail add-in settings
     * 
     * @return
     */
    @GET
    public Response getImaSettings() {
        
        String jsonEntity = null;
        
        DAS_LOGGER.traceEntry(this, "getImaSettings"); // $NON-NLS-1$
        
        IImaSettingsProvider provider = ProviderFactory.getImaSettingsProvider();
        if ( provider == null ) {
            throw new WebApplicationException(CoreService.createErrorResponse("No IMA settings resource.", Response.Status.NOT_FOUND)); // $NLX-ImaSettingsResource.NoIMAsettingsresource-1$
        }
        
        // Anonymous access not allowed
        CoreService.verifyUserContext();

        try {
            ImaSettings settings = provider.getSettings(ContextInfo.getUserSession());

            // Get the IMA settings
            
            JsonJavaObject obj = toJsonObject(settings);
            
            // Serialize the JSON
            
            StringBuilder sb = new StringBuilder();
            JsonGenerator.Generator generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
            generator.toJson(obj);
            jsonEntity = sb.toString();
        } 
        catch (ModelException e) {
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
            builder.type(MediaType.APPLICATION_JSON).entity(jsonEntity);
        }
        
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "getImaSettings", response); // $NON-NLS-1$

        return response;
    }

    /**
     * Converts the IMA settings to a JSON object.
     * 
     * @param settings
     * @return
     */
    private JsonJavaObject toJsonObject(ImaSettings settings) {
        JsonJavaObject obj = new JsonJavaObject();
        
        JsonJavaObject freebusy = new JsonJavaObject();
        if ( settings.getFbUrl() != null ) {
            freebusy.putString(JsonConstants.HREF_PROP, settings.getFbUrl());
        }
        if ( freebusy.size() > 0 ) {
            obj.putObject(JSON_FREEBUSY, freebusy);
        }
        
        JsonJavaObject pw = new JsonJavaObject();
        if ( settings.getPwChangeUrl() != null ) {
            pw.putString(JsonConstants.HREF_PROP, settings.getPwChangeUrl());
        }
        if ( pw.size() > 0 ) {
            obj.putObject(JSON_PW_CHANGE, pw);
        }
        
        return obj;
    }
}