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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_DB_COLLECTION;
import static com.ibm.domino.das.service.DataService.STAT_DB_COLLECTION;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.osgi.core.context.ContextInfo;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonDatabaseCollectionContent;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.acl.NoAccessSignal;

/**
 * DbCollectionResource
 */
//@Workspace(workspaceTitle = WORKSPACE_TITLE, collectionTitle = "Events")
@Path("data") // $NON-NLS-1$
public class DbCollectionResource {
	
	private static final String ENV_VAR_ALLOW_ANONYMOUS = "DataServiceAllowAnonymous"; // $NON-NLS-1$

    /**
     * Gets a list of databases on the server.
     * 
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDbs(@Context final UriInfo uriInfo,
            @QueryParam(PARAM_COMPACT) final boolean compact) {

        DAS_LOGGER.traceEntry(this, "getDbs"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DB_COLLECTION, STAT_DB_COLLECTION);
        
        StreamingOutput streamJsonEntity = null;

        try {
	        boolean authenticated = false;
	    	Session session = ContextInfo.getUserSession();
	    	if ( session != null ) {
		    	String userName = session.getEffectiveUserName();
		    	if ( userName != null && !userName.equals("Anonymous")) { // $NON-NLS-1$
		    		authenticated = true;
		    	}
	    	}
	    	
	    	// SPR #RGAU8TYHMG: Don't allow anonymous requests -- unless admin opts in.
	    	
	    	if ( !authenticated ) {
	    		String var = session.getEnvironmentString(ENV_VAR_ALLOW_ANONYMOUS, true);
	    		if ( !"1".equals(var) ) { // $NON-NLS-1$
	    			throw new NoAccessSignal("Need user context"); // $NLX-DbCollectionResource.Needusercontext-1$
	    		}
	    	}
	
	    	// Stream the output 
	    	
	        streamJsonEntity = new StreamingOutput(){
	
	            //@Override
	            public void write(OutputStream outputStream) throws IOException {
	                try {
	                    Database database = ContextInfo.getUserDatabase();
	                    if ( database != null ) {
	                        // This resource should never have a database context
	                        throw new WebApplicationException(ErrorHelper.createErrorResponse("URI path must not include a database.", Response.Status.BAD_REQUEST)); // $NLX-DbCollectionResource.URIpathmustnotincludeadatabase-1$
	                    }
	                    
	        	    	// Split URI into base and path segments
                        URI uri = UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                        uri = UriHelper.appendPathSegment(uri, "collections"); // $NON-NLS-1$
                        String baseUri = uri.getHost();
                        if(baseUri == null){
                            baseUri = "/";  // $NON-NLS-1$
                        }
                        String resourcePath = uri.getPath();
                        
	                    // Write JSON content
	                    
	                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
	                    JsonWriter jwriter = new JsonWriter(streamWriter, compact);
	                    Session session = ContextInfo.getUserSession();
	                    JsonDatabaseCollectionContent content = new JsonDatabaseCollectionContent(session, baseUri, resourcePath);
	                    content.writeDatabaseCollection(jwriter);
	                    
	                    streamWriter.close();
	                }
	                catch (ServiceException e) {
	                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
	                } 
	                
	            }
	            
	        };
        }
        catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		}
        
        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "getDbs", response); // $NON-NLS-1$

        return response;
    }
    
}