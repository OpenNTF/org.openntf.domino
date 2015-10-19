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

import static com.ibm.domino.commons.json.JsonConstants.HREF_PROP;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_IMA_SETTINGS;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_NONCE;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_PW_STATS;
import static com.ibm.domino.das.service.CoreService.PATH_SEGMENT_STATS;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.commons.model.IImaSettingsProvider;
import com.ibm.domino.commons.model.ProviderFactory;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.CoreService;

@Path("core") // $NON-NLS-1$
public class CoreRootResource {
    
    @GET
    public Response getResources(@Context UriInfo uriInfo) {
        String jsonEntity = null;
        
        DAS_LOGGER.traceEntry(this, "getResources"); // $NON-NLS-1$
        
        try {
            URI baseUri = UriHelper.copy(uriInfo.getAbsolutePath(), true);
            List<JsonJavaObject> links = new ArrayList<JsonJavaObject>();

            JsonJavaObject pwstats = new JsonJavaObject();
            pwstats.putJsonProperty("rel", PATH_SEGMENT_PW_STATS); // $NON-NLS-1$
            pwstats.putJsonProperty(HREF_PROP, UriHelper.appendPathSegment(baseUri, PATH_SEGMENT_PW_STATS).toString());
            links.add(pwstats);

            IImaSettingsProvider provider = ProviderFactory.getImaSettingsProvider();
            if ( provider != null ) {
                JsonJavaObject imasettings = new JsonJavaObject();
                imasettings.putJsonProperty("rel", PATH_SEGMENT_IMA_SETTINGS); // $NON-NLS-1$
                imasettings.putJsonProperty(HREF_PROP, UriHelper.appendPathSegment(baseUri, PATH_SEGMENT_IMA_SETTINGS).toString());
                links.add(imasettings);
            }

            JsonJavaObject nonce = new JsonJavaObject();
            nonce.putJsonProperty("rel", PATH_SEGMENT_NONCE); // $NON-NLS-1$
            nonce.putJsonProperty(HREF_PROP, UriHelper.appendPathSegment(baseUri, PATH_SEGMENT_NONCE).toString());
            links.add(nonce);

            JsonJavaObject stats = new JsonJavaObject();
            stats.putJsonProperty("rel", PATH_SEGMENT_STATS); // $NON-NLS-1$
            stats.putJsonProperty(HREF_PROP, UriHelper.appendPathSegment(baseUri, PATH_SEGMENT_STATS).toString());
            links.add(stats);

            JsonJavaObject obj = new JsonJavaObject();
            obj.putJsonProperty("links", links); // $NON-NLS-1$
            
            StringBuilder sb = new StringBuilder();
            JsonGenerator.Generator generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
            generator.toJson(obj);
            jsonEntity = sb.toString();
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
        DAS_LOGGER.traceExit(this, "getResources", response); // $NON-NLS-1$

        return response;
    }
}
