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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_DOC_COLLECTION;
import static com.ibm.domino.das.service.DataService.STAT_DOC_COLLECTION;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.rest.RestParameterConstants.*;
import static com.ibm.domino.services.rest.RestServiceConstants.ITEM_FORM;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonDocumentCollectionContent;
import com.ibm.domino.services.content.JsonDocumentContent;
import com.ibm.domino.services.util.JsonWriter;

@Path(PARAM_DATA + PARAM_SEPERATOR + PARAM_DOCUMENTS)
public class DocumentCollectionResource extends AbstractDasResource{

    /**
     * Gets a list of documents in a database.
     * 
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)    
    public Response getDocuments(@Context final UriInfo uriInfo,
            @QueryParam(PARAM_COMPACT) final boolean compact,
            @QueryParam(PARAM_DOC_SEARCH) final String search,
            @QueryParam(PARAM_DOC_SEARCHMAXDOCS) final String searchMaxDocs,
            @QueryParam(PARAM_DOC_SINCE) final String since) {
        
        DAS_LOGGER.traceEntry(this, "getDocuments"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOC_COLLECTION, STAT_DOC_COLLECTION);

        StreamingOutput streamJsonEntity = new StreamingOutput(){

            //@Override
            public void write(OutputStream outputStream) throws IOException {
                try {
                    URI baseURI = UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                    baseURI = UriHelper.appendPathSegment(baseURI,PARAM_UNID);
                    
                    //Database database = getDatabase(DB_ACCESS_VIEWS_DOCS);
                    Database database = getDatabase(DB_ACCESS_VIEWS);
                    if ( database == null ) {
                        // This resource should always have a database context
                        throw new WebApplicationException(ErrorHelper.createErrorResponse("URI path must include a database.", Response.Status.BAD_REQUEST)); // $NLX-DocumentCollectionResource.URIpathmustincludeadatabase-1$
                    }
                    // Seach MAX Documents.                 
                    int max = 0;
                    if (StringUtil.isNotEmpty(searchMaxDocs)) { 
                        max = getParameterInt(PARAM_DOC_SEARCHMAXDOCS, searchMaxDocs);
                    }
                    
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    JsonWriter jwriter = new JsonWriter(streamWriter, compact);
                    JsonDocumentCollectionContent content = new JsonDocumentCollectionContent(database, baseURI.toString()+"/", search, since, max);
                    content.writeDocumentCollection(jwriter);
                    
                    streamWriter.close();
                }
                catch (ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
                }
            }
        };

        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        
        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "getDocuments", response); // $NON-NLS-1$

        return response;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDocument(String requestEntity,
            @Context final UriInfo uriInfo,
            @QueryParam(PARAM_DOC_FORM) String form,
            @QueryParam(PARAM_DOC_COMPUTEWITHFORM) String computeWithForm, 
//          @QueryParam(PARAM_DOC_SEND) String send,
//          @QueryParam(PARAM_DOC_ATTACHFORM) String attachForm,
            @QueryParam(PARAM_DOC_PARENTID) String parentId) {

        DAS_LOGGER.traceEntry(this, "postDocument"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOC_COLLECTION, STAT_DOC_COLLECTION);

        URI location = null;
        Document document = null;
        Document parent = null;

        try {
            Database database = this.getDatabase(DB_ACCESS_VIEWS_DOCS);
            document = database.createDocument();
            JsonDocumentContent content = new JsonDocumentContent(document);
            JsonJavaObject jsonItems;
            JsonJavaFactory factory = JsonJavaFactory.instanceEx;
            try {
                StringReader reader = new StringReader(requestEntity);
                try {
                    jsonItems = (JsonJavaObject) JsonParser.fromJson(factory, reader);
                }
                finally {
                    reader.close();
                }
            }
            catch (Exception ex) {
                throw new ServiceException(ex, "Error while parsing the JSON content"); // $NLX-DocumentCollectionResource.ErrorwhileparsingtheJSONcontent-1$
            }
            // Handle parameters.
            if (StringUtil.isNotEmpty(form)) {
                document.replaceItemValue(ITEM_FORM, form);
            }
            content.updateFields(jsonItems, false);
            if (StringUtil.isNotEmpty(parentId)) {
                parent = database.getDocumentByUNID(parentId);
                document.makeResponse(parent);
            }
            if (StringUtil.isNotEmpty(computeWithForm) && computeWithForm.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0) {
                document.computeWithForm(true, true);
            }
            document.save();
            
//          if (send != null && send.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0) {
//              boolean bAttachForm = attachForm != null && attachForm.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0;
//              document.send(bAttachForm);
//          }

            URI baseURI = uriInfo.getAbsolutePath(); 
            baseURI = UriHelper.appendPathSegment(baseURI,PARAM_UNID);
            location = UriHelper.appendPathSegment(baseURI,document.getUniversalID());
        }
        catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }
        catch (ServiceException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }
        finally {
            if ( document != null ) {
                try {
                    document.recycle();
                } catch (NotesException e) {
                    // Ignore!
                }
                document = null;
            }            
            if ( parent != null ) {
                try {
                    parent.recycle();
                } catch (NotesException e) {
                    // Ignore!
                }
                parent = null;
            }
        }

        ResponseBuilder builder = Response.created(location);
        // builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();
        DAS_LOGGER.traceExit(this, "postDocument", response); // $NON-NLS-1$

        return response;
    }
    
}