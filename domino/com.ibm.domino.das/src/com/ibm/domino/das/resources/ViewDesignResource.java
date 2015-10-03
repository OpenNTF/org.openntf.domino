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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_VIEW_DESIGN;
import static com.ibm.domino.das.service.DataService.STAT_VIEW_DESIGN;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_COMPACT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonViewDesignContent;
import com.ibm.domino.services.util.JsonWriter;

@Path("data/collections/{key}/{value}/design") // $NON-NLS-1$
public class ViewDesignResource extends ViewBaseResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewDesign(@PathParam("key") final String keyName, // $NON-NLS-1$
            @PathParam("value") final String keyValue, // $NON-NLS-1$
            @QueryParam(PARAM_COMPACT) final boolean compact) {
        
        DAS_LOGGER.traceEntry(this, "getViewDesign"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_DESIGN, STAT_VIEW_DESIGN);
        
        final Database database = getDatabase(DB_ACCESS_VIEWS);
        if ( database == null ) {
            // This resource should always have a database context
            throw new WebApplicationException(ErrorHelper.createErrorResponse("URI path must include a database.", Response.Status.NOT_FOUND)); // $NLX-ViewDesignResource.URIpathmustincludeadatabase-1$
        }
        
        StreamingOutput streamJsonEntity = new StreamingOutput(){

            //@Override
            public void write(OutputStream outputStream) throws IOException {
                View view = null;

                try {                   
                    view = getCurrentView(keyName, keyValue, database);
                    view.setAutoUpdate(false);
                    
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    JsonWriter jwriter = new JsonWriter(streamWriter, compact);
                    JsonViewDesignContent content = new JsonViewDesignContent(view);
                    content.writeViewDesign(jwriter);
                    
                    streamWriter.close();
                }
                catch(NotesException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
                }
                catch(ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
                }
                finally {
                    if (view != null) {
                        try {
                            view.recycle();
                            view = null;
                        } catch(NotesException ex) {
                            DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewDesignResource.Exceptioncaughtandignored-1$
                        }
                    }
                }
            }
        };
        
        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "getViewDesign", response); // $NON-NLS-1$

        return response;
    }


}
