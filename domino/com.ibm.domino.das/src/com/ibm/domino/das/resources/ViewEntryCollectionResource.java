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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_VIEW_ENTRIES;
import static com.ibm.domino.das.service.DataService.STAT_VIEW_ENTRIES;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.HttpServiceConstants.HEADER_CONTENT_RANGE;
import static com.ibm.domino.services.HttpServiceConstants.HEADER_RANGE;
import static com.ibm.domino.services.HttpServiceConstants.HEADER_RANGE_ITEMS;
import static com.ibm.domino.services.rest.RestParameterConstants.*;
import static com.ibm.domino.services.rest.RestServiceConstants.ITEM_FORM;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.services.Loggers;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonViewEntryCollectionContent;
import com.ibm.domino.services.rest.das.RestDocumentNavigator;
import com.ibm.domino.services.rest.das.RestDocumentNavigatorFactory;
import com.ibm.domino.services.rest.das.view.RestViewNavigator;
import com.ibm.domino.services.rest.das.view.RestViewNavigatorFactory;
import com.ibm.domino.services.rest.das.view.ViewParameters;
import com.ibm.domino.services.rest.das.view.impl.DefaultViewParameters;
import com.ibm.domino.services.util.JsonWriter;

@Path(PARAM_DATA + PARAM_SEPERATOR + PARAM_COLLECTIONS + "/{key}/{value}") // $NON-NLS-1$
public class ViewEntryCollectionResource extends ViewBaseResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewEntries(@Context final UriInfo uriInfo,
            @PathParam("key") final String keyName, // $NON-NLS-1$
            @PathParam("value") final String keyValue, // $NON-NLS-1$
            @HeaderParam(HEADER_RANGE) final String range,
            @QueryParam(PARAM_COMPACT) final boolean compact,
            @QueryParam(PARAM_VIEW_START) final String start,
            @QueryParam(PARAM_VIEW_COUNT) final String count,
            @QueryParam(PARAM_VIEW_STARTINDEX) final String startIndex,
            @QueryParam(PARAM_VIEW_PAGESIZE) final String pageSize,
            @QueryParam(PARAM_VIEW_PAGEINDEX) final String pageIndex,
            @QueryParam(PARAM_VIEW_SEARCH) final String search,
            @QueryParam(PARAM_VIEW_SEARCHMAXDOCS) final String searchMaxDocs,
            @QueryParam(PARAM_VIEW_SORTCOLUMN) final String sortColumn,
            @QueryParam(PARAM_VIEW_SORTORDER) final String sortOrder,
            @QueryParam(PARAM_VIEW_STARTKEYS) final String startKeys,
            @QueryParam(PARAM_VIEW_SYSTEMCOLUMNS) final String systemColumns,
            @QueryParam(PARAM_VIEW_KEYS) final String keys,
            @QueryParam(PARAM_VIEW_KEYSEXACTMATCH) final String keysExactMatch,
            @QueryParam(PARAM_VIEW_EXPANDLEVEL) final String expandLevel,
            @QueryParam(PARAM_VIEW_CATEGORY) final String categoryFilter,
            @QueryParam(PARAM_VIEW_PARENTID) final String parentId,
            @QueryParam(PARAM_VIEW_ENTRYCOUNT) final String entryCount) {

        DAS_LOGGER.traceEntry(this, "getViewEntries"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_ENTRIES, STAT_VIEW_ENTRIES);
                
        final Database database = this.getDatabase(DB_ACCESS_VIEWS);
        if ( database == null ) {
            // This resource should always have a database context
            throw new WebApplicationException(ErrorHelper.createErrorResponse("URI path must include a database.", Response.Status.NOT_FOUND)); // $NLX-ViewEntryCollectionResource.URIpathmustincludeadatabase-1$
        }   

        final ResponseBuilder builder = Response.ok();
        
        abstract class StreamingOutputImpl implements StreamingOutput {
            
            Response response = null;
            
            public void setResponse(Response response) {
                this.response = response;
            }
            
        }
        
        StreamingOutputImpl streamJsonEntity = new StreamingOutputImpl() {
            
            //@Override
            public void write(OutputStream outputStream) throws IOException {
                
                View view = null;
                int iStart = 0;
                int iCount = DEFAULT_VIEW_COUNT;
                
                try { 
                    view = getCurrentView(keyName, keyValue, database);
                    view.setAutoUpdate(false);          

                    // Handle parameters.           
                    DefaultViewParameters parameters =  new DefaultViewParameters();
                    int iGlobalValues = DefaultViewParameters.GLOBAL_ALL;
                    int iSystemColumns = DefaultViewParameters.SYSCOL_ALL;
                    
                    parameters.setDefaultColumns(true); 
                    URI baseUri= UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                    baseUri = UriHelper.appendPathSegment(baseUri, PARAM_UNID);
                    
                    URI baseDocumentUri= UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                    baseDocumentUri = UriHelper.trimAtLast(baseDocumentUri, PARAM_SEPERATOR + PARAM_COLLECTIONS);
                    baseDocumentUri = UriHelper.appendPathSegment(baseDocumentUri, PARAM_DOCUMENTS);
                    baseDocumentUri = UriHelper.appendPathSegment(baseDocumentUri, PARAM_UNID);
                    
                    // Header submitted by the client:
                    //    Range: items=0-24
                    if(StringUtil.isNotEmpty(range) && range.startsWith(HEADER_RANGE_ITEMS)) {
                        int pos = HEADER_RANGE_ITEMS.length();
                        int sep = range.indexOf('-',pos);
                        iStart = Integer.valueOf(range.substring(pos,sep));
                        int last = Integer.valueOf(range.substring(sep+1));
                        iCount = last-iStart+1;
                    } 
                    else { 
                        // If the header does not contain range information, we still look at the url.
                        // Currently the plan is to support both iStart and iCount and page, ps and si.             
                        if (StringUtil.isNotEmpty(start)) {
                            iStart = getParameterInt(PARAM_VIEW_START, start);
                        }               
                        if (StringUtil.isNotEmpty(count)) {
                            iCount = getParameterInt(PARAM_VIEW_COUNT, count);              
                        } 
                        // The following three parameters page, ps and si map to iStart and iCount.
                        // iCount = ps
                        // iStart = ps * page + si
                        if (StringUtil.isNotEmpty(pageSize)) {
                            iCount = getParameterInt(PARAM_VIEW_PAGESIZE, pageSize);        
                        }           
                        if (StringUtil.isNotEmpty(pageIndex)) {
                            int page = getParameterInt(PARAM_VIEW_PAGEINDEX, pageIndex);        
                            iStart = page * iCount;
                        }           
                        if (StringUtil.isNotEmpty(startIndex)) {
                            try {
                                int si = getParameterInt(PARAM_VIEW_STARTINDEX, startIndex);        
                                iStart += si;
                            } catch (NumberFormatException nfe) {}
                        } 
                    }
                    
                    // SPR# KHRL9NZTRZ: Limit the page size
                    int maxCount = DataService.getMaxViewEntries();
                    if ( iCount > maxCount ) {
                        String msg = StringUtil.format("Limit exceeded.  Cannot read more than {0} entries.", maxCount); // $NLX-ViewEntryCollectionResource.LimitexceededCannotreadmorethan0e-1$
                        throw new WebApplicationException(
                                ErrorHelper.createErrorResponse(msg, Response.Status.BAD_REQUEST));
                    }
                    
                    // We passed all the checks. Set the start index and count
                    parameters.setStart(iStart);
                    parameters.setCount(iCount);
                    
                    if (StringUtil.isNotEmpty(search)) {            
                        parameters.setFtSearch(search);
                    }
                    if (StringUtil.isNotEmpty(searchMaxDocs)) { 
                        parameters.setFtMaxDocs(getParameterInt(PARAM_VIEW_SEARCHMAXDOCS, searchMaxDocs));
                    }
                    if (StringUtil.isNotEmpty(sortColumn)) {
                        parameters.setSortColumn(sortColumn);
                    }
                    if (StringUtil.isNotEmpty(sortOrder)) {
                        parameters.setSortOrder(sortOrder);
                    }
                    if (StringUtil.isNotEmpty(startKeys)) {
                        parameters.setStartKey(startKeys);
                    }
                    if (StringUtil.isNotEmpty(keys)) {
                        parameters.setKeys(keys);
                    }
                    if (StringUtil.isNotEmpty(keysExactMatch)) {
                        parameters.setKeysExactMatch(keysExactMatch.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0);
                    }
                    if (StringUtil.isNotEmpty(expandLevel)) {
                        parameters.setMaxLevel(getParameterInt(PARAM_VIEW_EXPANDLEVEL, expandLevel));
                    } else {
                        parameters.setMaxLevel(Integer.MAX_VALUE);
                    }
                    if (StringUtil.isNotEmpty(categoryFilter)) {
                        parameters.setCategoryFilter(categoryFilter);
                    }
                    if (StringUtil.isNotEmpty(parentId)) {
                        parameters.setParentId(parentId);
                    }                    
                    if (StringUtil.isNotEmpty(entryCount) && entryCount.compareToIgnoreCase(PARAM_VALUE_FALSE) == 0) {
                        iGlobalValues &= ~ViewParameters.GLOBAL_TOPLEVEL;
                    }
                    if (StringUtil.isNotEmpty(systemColumns)) { 
                        iSystemColumns = getParameterInt(PARAM_VIEW_SYSTEMCOLUMNS, systemColumns, true);
                    }
                    
                    parameters.setGlobalValues(iGlobalValues);
                    parameters.setSystemColumns(iSystemColumns);

                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    JsonWriter jsonWriter = new JsonWriter(streamWriter, compact);
                    JsonViewEntryCollectionContent content = new JsonViewEntryCollectionContent(view, baseUri.toString(), baseDocumentUri.toString());
                    String header = content.getContentRangeHeader(parameters);
                    if (header != null) {
                        response.getMetadata().add(HEADER_CONTENT_RANGE, header);
                    }
                    content.writeViewEntryCollection(jsonWriter, parameters);
                    streamWriter.close();           

                } catch (NotesException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
                } catch (ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
                } finally {
                    if (view != null) {
                        try {
                            view.recycle();
                            view = null;
                        } catch(NotesException ex) {
                            DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewEntryCollectionResource.Exceptioncaughtandignored-1$
                        }
                    }
                }
            }
        };
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        Response response = builder.build();
        streamJsonEntity.setResponse(response);
        return response;
    }
    
    @POST   
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postViewEntry(@Context final UriInfo uriInfo,
            @PathParam("key") final String keyName, // $NON-NLS-1$
            @PathParam("value") final String keyValue, // $NON-NLS-1$
            String requestEntity, 
            @QueryParam(PARAM_VIEW_FORM) String form,
            @QueryParam(PARAM_VIEW_COMPUTEWITHFORM) String computeWithForm,
            @QueryParam(PARAM_VIEW_PARENTID) String parentId) {

        DAS_LOGGER.traceEntry(this, "postViewEntry");        // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_ENTRIES, STAT_VIEW_ENTRIES);
    
        URI location = null;
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
                throw new ServiceException(ex, "Error while parsing the JSON content"); // $NLX-ViewEntryCollectionResource.ErrorwhileparsingtheJSONcontent-1$
            }
            
            Document document = null;
            RestDocumentNavigator docNav = null;
            try {
                DefaultViewParameters parameters =  new DefaultViewParameters();
                parameters.setGlobalValues(DefaultViewParameters.GLOBAL_ALL);
                parameters.setSystemColumns(DefaultViewParameters.SYSCOL_ALL);
                parameters.setDefaultColumns(true); 
                
                // Get a view navigator to get access to the columns
                RestViewNavigator viewNav = RestViewNavigatorFactory.createNavigatorForDesign(view,parameters);

                // Get a document docNav
                docNav = RestDocumentNavigatorFactory.createNavigator(view, parameters);

                // Create a new document.
                docNav.createDocument();
                document = docNav.getDocument();
                
                JsonViewEntryCollectionContent content = new JsonViewEntryCollectionContent(view);
                content.updateFields(viewNav, docNav, jsonItems);
                
                // Handle parameters.
                if (StringUtil.isNotEmpty(form)) {
                    document.replaceItemValue(ITEM_FORM, form);
                }           
                if (StringUtil.isNotEmpty(parentId)) {
                    Document parent = null;
                    try {
                        parent = database.getDocumentByUNID(parentId);
                        document.makeResponse(parent);
                    }
                    catch (NotesException e) {
                        throw new ServiceException(e, "Error creating document."); // $NLX-ViewEntryCollectionResource.Errorcreatingdocument-1$
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
                if (StringUtil.isNotEmpty(computeWithForm)&& computeWithForm.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0) {
                    document.computeWithForm(true, true);       
                } 
                document.save();
        
                if (view.isFolder())
                    document.putInFolder(view.getName());
        
                URI baseDocumentUri= uriInfo.getAbsolutePath();
                baseDocumentUri = UriHelper.trimAtLast(baseDocumentUri, PARAM_COLLECTIONS);
                baseDocumentUri = UriHelper.appendPathSegment(baseDocumentUri, PARAM_DOCUMENTS);
                baseDocumentUri = UriHelper.appendPathSegment(baseDocumentUri, PARAM_UNID);

                location = UriHelper.appendPathSegment(baseDocumentUri, document.getUniversalID());        
                
            } catch(Throwable ex) {
                if(ex instanceof ServiceException) {
                    throw (ServiceException)ex;
                }
                throw new ServiceException(ex,"Error while creating document."); // $NLX-ViewEntryCollectionResource.Errorwhilecreatingdocument-1$
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
                    DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewEntryCollectionResource.Exceptioncaughtandignored.1-1$
                }
            }
        }
    
        ResponseBuilder builder = Response.created(location);
//      builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "postViewEntry", response); // $NON-NLS-1$

        return response;
    }
    
    @PUT    
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putFolderOperations(
            String requestEntity, 
            @PathParam("key") final String keyName, // $NON-NLS-1$
            @PathParam("value") final String keyValue) { // $NON-NLS-1$

        DAS_LOGGER.traceEntry(this, "putFolderOperations"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_VIEW_ENTRIES, STAT_VIEW_ENTRIES);
        
        View view = null;
        
        try { 
            // Open the folder
            
            Database database = this.getDatabase(DB_ACCESS_VIEWS);
            view = getCurrentView(keyName, keyValue, database);
            
            // Make sure it really is a folder
            
            if ( ! view.isFolder() ) {
                throw new WebApplicationException(ErrorHelper.createErrorResponse("Cannot perform folder operations on a view.", Response.Status.BAD_REQUEST)); // $NLX-ViewEntryCollectionResource.Cannotperformfolderoperationsonav-1$
            }
            
            // Parse the JSON input
            
            JsonJavaObject jsonOperations = null;
            try {
                StringReader reader = new StringReader(requestEntity);
                try {
                    jsonOperations = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, reader);
                } 
                finally {
                    reader.close();
                }
            } 
            catch(Exception ex) {
                throw new ServiceException(ex, "Error while parsing the JSON content"); // $NLX-ViewEntryCollectionResource.ErrorwhileparsingtheJSONcontent.1-1$
            }

            // Delegate the work
            
            JsonViewEntryCollectionContent content = new JsonViewEntryCollectionContent(view);
            content.updateFolder(jsonOperations);
        }
        catch (ServiceException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        } catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
        }
        finally {
            if (view != null) {
                try {
                    view.recycle();
                    view = null;
                } catch(NotesException ex) {
                    DAS_LOGGER.warn(ex, "Exception caught and ignored."); // $NLW-ViewEntryCollectionResource.Exceptioncaughtandignored.2-1$
                }
            }
        }
        
        ResponseBuilder builder = Response.ok();
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "putFolderOperations", response); // $NON-NLS-1$

        return response;
    }
        
}