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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_VIEW_ENTRY;
import static com.ibm.domino.das.service.DataService.STAT_VIEW_ENTRY;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_VIEW_COMPUTEWITHFORM;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_VIEW_FORM;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_VIEW_PARENTID;
import static com.ibm.domino.services.rest.RestServiceConstants.ITEM_FORM;

import java.io.StringReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.services.Loggers;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonViewEntryCollectionContent;
import com.ibm.domino.services.rest.das.RestDocumentNavigator;
import com.ibm.domino.services.rest.das.RestDocumentNavigatorFactory;
import com.ibm.domino.services.rest.das.view.RestViewNavigator;
import com.ibm.domino.services.rest.das.view.RestViewNavigatorFactory;
import com.ibm.domino.services.rest.das.view.impl.DefaultViewParameters;

@Path("data/collections/{key}/{value}/unid/{docunid}") // $NON-NLS-1$
public class ViewEntryResource extends ViewBaseResource {
    

    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putViewEntry(@PathParam("key") String keyName, // $NON-NLS-1$
            @PathParam("value") String keyValue, // $NON-NLS-1$
            @PathParam("docunid") String docUnid, // $NON-NLS-1$
            String requestEntity, 
            @QueryParam(PARAM_VIEW_FORM) String form,
            @QueryParam(PARAM_VIEW_COMPUTEWITHFORM) String computeWithForm,
            @QueryParam(PARAM_VIEW_PARENTID) String parentId) {

        DAS_LOGGER.traceEntry(this, "postViewEntry");        // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_ENTRY, STAT_VIEW_ENTRY);
        
        View view = null;
        
        try { 
            Database database = this.getDatabase(DB_ACCESS_VIEWS);
            view = getCurrentView(keyName, keyValue, database);

            JsonJavaObject jsonItems;
            JsonJavaFactory factory = JsonJavaFactory.instanceEx;
            try {
                StringReader reader = new StringReader(requestEntity);
                try {
                    jsonItems = (JsonJavaObject)JsonParser.fromJson(factory, reader);
                } finally {
                    reader.close();
                }
            } catch(Exception ex) {
                throw new ServiceException(ex, "Error while parsing the JSON content"); // $NLX-ViewEntryResource.ErrorwhileparsingtheJSONcontent-1$
            }
                        
            RestDocumentNavigator docNav = null;
            Document document = null;
            try {
                DefaultViewParameters parameters =  new DefaultViewParameters();
                parameters.setGlobalValues(DefaultViewParameters.GLOBAL_ALL);
                parameters.setSystemColumns(DefaultViewParameters.SYSCOL_ALL);
                parameters.setDefaultColumns(true); 
                                
                // Get a view navigator to get access to the columns
                RestViewNavigator viewNav = RestViewNavigatorFactory.createNavigatorForDesign(view,parameters);
                try {
                    // Get a document docNav
                    docNav = RestDocumentNavigatorFactory.createNavigator(view, parameters);
                    docNav.openDocument(docUnid);
                    document = docNav.getDocument();
                } catch (ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.NOT_FOUND));
                }
                
                JsonViewEntryCollectionContent content = new JsonViewEntryCollectionContent(view);
                content.updateFields(viewNav, docNav, jsonItems);

                // Handle parameters.
                if (StringUtil.isNotEmpty(form)) {
                    document.replaceItemValue(ITEM_FORM, form);
                }           
                if (StringUtil.isNotEmpty(computeWithForm)&& computeWithForm.compareToIgnoreCase("true") == 0) { // $NON-NLS-1$
                    document.computeWithForm(true, true);       
                } 
                if (StringUtil.isNotEmpty(parentId)) {
                    Document parent = null;
                    try {
                        parent = database.getDocumentByUNID(parentId);
                        document.makeResponse(parent);
                    }
                    catch (NotesException e) {
                        throw new ServiceException(e, "Error creating document."); // $NLX-ViewEntryResource.Errorcreatingdocument-1$
                    } finally {
                        if ( parent != null ) {
                            try {
                                parent.recycle();
                            } catch (NotesException e) {
                                Loggers.SERVICES_LOGGER.traceDebug("Exception thrown on recycle.", e); // $NON-NLS-1$
                            }
                            parent = null;
                        }
                    }
                }       
                document.save();                

            } catch(Throwable ex) {
                if(ex instanceof ServiceException) {
                    throw (ServiceException)ex;
                }
                throw new ServiceException(ex,"Error while updating document."); // $NLX-ViewEntryResource.Errorwhileupdatingdocument-1$
            } finally {
                // The call to docNav.recycle will recycle the document.
                if (docNav != null)
                    docNav.recycle();
            }
            
        } 
        catch (ServiceException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        } 
        finally {
            if (view != null) {
                try {
                    view.recycle();
                    view = null;
                } catch(NotesException ex) {
                    DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewEntryResource.Exceptioncaughtandignored-1$
                }
            }
        }

        ResponseBuilder builder = Response.ok();
//      builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "postViewEntry", response); // $NON-NLS-1$

        return response;
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteViewEntry(@PathParam("key") String keyName,  // $NON-NLS-1$
            @PathParam("value") String keyValue, // $NON-NLS-1$
            @PathParam("docunid") String docUnid) { // $NON-NLS-1$

        DAS_LOGGER.traceEntry(this, "deleteViewEntry"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_ENTRY, STAT_VIEW_ENTRY);

        View view = null;
        Document document = null;

        try {   
            Database database = this.getDatabase(DB_ACCESS_VIEWS_DOCS);
            // We get the view to verify the access control.
            view = getCurrentView(keyName, keyValue, database);
            document = database.getDocumentByUNID(docUnid);         
        } catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.NOT_FOUND));
        }
        finally {
            if (view != null) {
                try {
                    view.recycle();
                    view = null;
                } catch(NotesException ex) {
                    DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewEntryResource.Exceptioncaughtandignored.1-1$
                }
            }
        }
        
        try {           
            if(!document.remove(true)) {
                throw new WebApplicationException(ErrorHelper.createErrorResponse("Document is not deleted because another user modified it.", Response.Status.CONFLICT)); // $NLX-ViewEntryResource.Documentisnotdeletedbecauseanothe-1$
            }               
        } catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }

        ResponseBuilder builder = Response.ok();
//      builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "deleteViewEntry", response); // $NON-NLS-1$

        return response;
    }
    
//  @DELETE
//  public Response deleteViewEntry(@PathParam("key") String keyName, @PathParam("value") String keyValue,
//          @PathParam("docunid") String docUnid,
//          @Context javax.servlet.http.HttpServletRequest request,
//          @Context javax.servlet.http.HttpServletResponse response) {
//      
//      DAS_LOGGER.traceEntry(this, "deleteViewEntry");
//      processRequest(keyName, keyValue, request, response);
//      DAS_LOGGER.traceExit(this, "deleteViewEntry", response);
//
//      return null;
//  }
    
//  @PUT
//  public Response putViewEntry(@PathParam("key") String keyName, @PathParam("value") String keyValue,
//          @PathParam("docunid") String docUnid,
//          @Context javax.servlet.http.HttpServletRequest request,
//          @Context javax.servlet.http.HttpServletResponse response) {
//      
//      DAS_LOGGER.traceEntry(this, "putViewEntry");
//      processRequest(keyName, keyValue, request, response);
//      DAS_LOGGER.traceExit(this, "putViewEntry", response);
//
//      return null;
//  }
    
}