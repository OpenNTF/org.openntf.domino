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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_VIEW_COLLECTION;
import static com.ibm.domino.das.service.DataService.STAT_VIEW_COLLECTION;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_COMPACT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import lotus.domino.Database;

import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonViewCollectionContent;
import com.ibm.domino.services.util.JsonWriter;

@Path("data/collections") // $NON-NLS-1$
public class ViewCollectionResource extends AbstractDasResource{

    /**
     * Gets a list of views and folders in a database.
     * 
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViews(@Context final UriInfo uriInfo,
            @QueryParam(PARAM_COMPACT) final boolean compact) {
        
        DAS_LOGGER.traceEntry(this, "getViews"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_COLLECTION, STAT_VIEW_COLLECTION);

        StreamingOutput streamJsonEntity = new StreamingOutput(){

            //@Override
            public void write(OutputStream outputStream) throws IOException {
                try {
                    URI baseURI = UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                    
                    Database database = getDatabase(DB_ACCESS_VIEWS);
                    if ( database == null ) {
                        // This resource should always have a database context
                        throw new WebApplicationException(ErrorHelper.createErrorResponse("URI path must include a database.", Response.Status.BAD_REQUEST)); // $NLX-ViewCollectionResource.URIpathmustincludeadatabase-1$
                    }
                    
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    JsonWriter jwriter = new JsonWriter(streamWriter, compact);
                    JsonViewCollectionContent content = new JsonViewCollectionContent(database, baseURI.toString());
                    content.writeViewCollection(jwriter);
                    
                    streamWriter.close();
                }
                catch (ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
        };

        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "getViews", response); // $NON-NLS-1$

        return response;
    }
    
}