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

import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.RestService;
import com.ibm.domino.das.servlet.DasServlet;
import com.ibm.domino.das.utils.ErrorHelper;

@Path("")
public class ApiRootResource {

    @GET
    public Response getServices(@Context final UriInfo uriInfo) {
        
        DAS_LOGGER.traceEntry(this, "getServices"); // $NON-NLS-1$
        
        String entity = null;
        
        RestService.verifyNoDatabaseContext();

        URI baseURI;
        try {
            baseURI = UriHelper.copy(uriInfo.getAbsolutePath(),true);
            entity = DasServlet.getServicesResponse(baseURI.toString());
        } 
        catch (IOException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        } 
        catch (JsonException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }
        
        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(entity);
        Response response = builder.build();
        
        DAS_LOGGER.traceExit(this, "getServices", response); // $NON-NLS-1$
        return response;
    }
}