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

import static com.ibm.domino.commons.model.IGatekeeperProvider.FEATURE_REST_API_DATA_DOCUMENT;
import static com.ibm.domino.das.service.DataService.STAT_DOCUMENT;
import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;
import static com.ibm.domino.services.HttpServiceConstants.*;
import static com.ibm.domino.services.rest.RestParameterConstants.*;
import static com.ibm.domino.services.rest.RestServiceConstants.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.apache.http.impl.cookie.DateParseException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.commons.util.UriHelper;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.httpmethod.PATCH;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.JsonDocumentContent;
import com.ibm.domino.services.rest.das.document.DocumentParameters;
import com.ibm.domino.services.util.JsonWriter;

@Path(PARAM_DATA + PARAM_SEPERATOR + PARAM_DOCUMENTS + PARAM_SEPERATOR + PARAM_UNID + PARAM_SEPERATOR + UNID_RESOURCE_PATH)
public class DocumentResource extends AbstractDasResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocumentByUnid(@Context final UriInfo uriInfo,
            @PathParam(PARAM_UNID) final String unid,
            @HeaderParam(HEADER_IF_MODIFIED_SINCE) final String ifModifiedSince,            
            @QueryParam(PARAM_COMPACT) final boolean compact,
            @QueryParam(PARAM_DOC_STRONGTYPE)final boolean strongType,
            @QueryParam(PARAM_DOC_HIDDEN)final boolean hidden,
            @QueryParam(PARAM_DOC_MARKREAD) final String markRead,
            @QueryParam(PARAM_DOC_MULTIPART) final String multipart,
            @QueryParam(PARAM_DOC_LOWERCASEFIELDS) final boolean lowercaseItems,
            @QueryParam(PARAM_DOC_FIELDS) final String items){

        DAS_LOGGER.traceEntry(this, "getDocumentByUnid"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOCUMENT, STAT_DOCUMENT);

        final ResponseBuilder builder = Response.ok();
        
        abstract class StreamingOutputImpl implements StreamingOutput {
            
            Response response = null;
            
            public void setResponse(Response response) {
                this.response = response;
            }
            
        }
        
        StreamingOutputImpl streamJsonEntity = new StreamingOutputImpl() {
        
            // @Override
            public void write(OutputStream outputStream) throws IOException {
            	Document document = null;
            	
                try {
                    Database database = getDatabase(DB_ACCESS_VIEWS_DOCS);
                   
                    try {
                        document = database.getDocumentByUNID(unid);
                    }
                    catch (NotesException e) {
                        throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.NOT_FOUND));
                    }
                    
                    URI baseUri = UriHelper.copy(uriInfo.getAbsolutePath(),DataService.isUseRelativeUrls());
                    
                    //Unid don't contain special characters, so don't need encode
                    baseUri = UriHelper.trimAtLast(baseUri, PARAM_SEPERATOR + unid);
                                        
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    
                    String lastModifiedHeader = ifModifiedSince(document, ifModifiedSince); 
                    if (lastModifiedHeader != null) {
                    	response.getMetadata().add(HEADER_LAST_MODIFIED, lastModifiedHeader);
                    }
                    
                    JsonWriter jsonWriter = new JsonWriter(streamWriter, compact);
                    JsonDocumentContent content = new JsonDocumentContent(document);
                    
                    int sysItems = DocumentParameters.SYS_ITEM_ALL;
                    String rtType = TYPE_MULTIPART;
                    
                    // Handle parameters
                    if (!hidden) {
                        sysItems &= ~DocumentParameters.SYS_ITEM_HIDDEN;
                    }                        
                    if (multipart != null && multipart.compareToIgnoreCase(PARAM_VALUE_FALSE) == 0) {
                    	rtType = TYPE_RICHTEXT;  // Deprecated
                    } 
                    
                    List<String> defItemFilter = null;
                    if ( StringUtil.isNotEmpty(items) ) {
                        defItemFilter = DataService.getParameterStringList(PARAM_DOC_FIELDS, items);
                    }

                    try {
                                       	
                        content.writeDocumentAsJson(jsonWriter, sysItems, true, 
                                    defItemFilter, lowercaseItems, null, 
                                    strongType, rtType, baseUri.toString());
                    }
                    finally {
                        jsonWriter.flush();
                    }                   
                    
                    // Handle parameters.
                    if (markRead == null || markRead.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0) {
                        document.markRead();
                    }
                    else if (markRead.compareToIgnoreCase(PARAM_VALUE_FALSE) != 0) {
                        throw new WebApplicationException(ErrorHelper.createErrorResponse("Invalid parameter.", Response.Status.BAD_REQUEST)); // $NLX-DocumentResource.Invalidparameter-1$
                    }
                    
                    streamWriter.close();

                }
                catch (NotesException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
                }
                catch (ServiceException e) {
                    throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
                }
                finally {
                	if (document != null) {
                		try {
        					document.recycle();
        				} catch (NotesException e) {
        					// Ignore
        				}
                	}
                }
            }
        };        
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(streamJsonEntity);
        Response response = builder.build();      
        streamJsonEntity.setResponse(response);
        DAS_LOGGER.traceExit(this, "getDocumentByUnid", response); // $NON-NLS-1$
        return response;
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putDocumentByUnid(String requestEntity, 
            @HeaderParam(HEADER_IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince, 
            @PathParam(PARAM_UNID) String unid,
            @QueryParam(PARAM_DOC_FORM) String form,
            @QueryParam(PARAM_DOC_COMPUTEWITHFORM) String computeWithForm) {

        DAS_LOGGER.traceEntry(this, "putDocumentByUnid"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOCUMENT, STAT_DOCUMENT);

        Response response = updateDocumentByUnid(requestEntity, ifUnmodifiedSince, unid, form, computeWithForm, true);

        DAS_LOGGER.traceExit(this, "putDocumentByUnid", response); // $NON-NLS-1$

        return response;
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchDocumentByUnid(String requestEntity, 
            @HeaderParam(HEADER_IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince, 
            @PathParam(PARAM_UNID) String unid,
            @QueryParam(PARAM_DOC_FORM) String form,
            @QueryParam(PARAM_DOC_COMPUTEWITHFORM) String computeWithForm) {

        DAS_LOGGER.traceEntry(this, "patchDocumentByUnid"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOCUMENT, STAT_DOCUMENT);

        Response response = updateDocumentByUnid(requestEntity, ifUnmodifiedSince, unid, form, computeWithForm, false);

        DAS_LOGGER.traceExit(this, "patchDocumentByUnid", response); // $NON-NLS-1$

        return response;
    }

    @DELETE
    public Response deleteDocumentByUnid(
    		@HeaderParam(HEADER_IF_UNMODIFIED_SINCE) String ifUnmodifiedSince, 
    		@PathParam(PARAM_UNID) String unid) {

        DAS_LOGGER.traceEntry(this, "deleteDocumentByUnid"); // $NON-NLS-1$
        DataService.beforeRequest(FEATURE_REST_API_DATA_DOCUMENT, STAT_DOCUMENT);

        // String jsonEntity = null;

        try {
            Database database = this.getDatabase(DB_ACCESS_VIEWS_DOCS);
            Document document = null;
            try {
                document = database.getDocumentByUNID(unid);
            }
            catch (NotesException e) {
                throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.NOT_FOUND));
            }
            ifUnmodifiedSince(document, ifUnmodifiedSince); 
            if(!document.remove(true)) {
                throw new WebApplicationException(ErrorHelper.createErrorResponse("Document is not deleted because another user modified it.", Response.Status.CONFLICT)); // $NLX-DocumentResource.Documentisnotdeletedbecauseanothe-1$
            }
        }
        catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }
        ResponseBuilder builder = Response.ok();
        // builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();
        
        DAS_LOGGER.traceExit(this, "deleteDocumentByUnid", response); // $NON-NLS-1$

        return response;
    }

    public Response updateDocumentByUnid(String requestEntity, 
            @HeaderParam(HEADER_IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince, 
            @PathParam(PARAM_UNID) String unid,
            @QueryParam(PARAM_DOC_FORM) String form,
            @QueryParam(PARAM_DOC_COMPUTEWITHFORM) String computeWithForm, 
            boolean put) {
        
        String jsonEntity = null;
        Document document = null;

        try {
            // Write JSON content
            Database database = this.getDatabase(DB_ACCESS_VIEWS_DOCS);
            try {
                document = database.getDocumentByUNID(unid);
            }
            catch (NotesException e) {
                throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.NOT_FOUND));
            }
            ifUnmodifiedSince(document, ifUnmodifiedSince); 
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
                throw new ServiceException(ex, "Error while parsing the JSON content"); // $NLX-DocumentResource.ErrorwhileparsingtheJSONcontent-1$
            }
            // Handle parameters.
            if (StringUtil.isNotEmpty(form)) {
                document.replaceItemValue(ITEM_FORM, form);
            }
            content.updateFields(jsonItems, put);
            if (computeWithForm != null && computeWithForm.compareToIgnoreCase(PARAM_VALUE_TRUE) == 0) {
                document.computeWithForm(true, true);
            }
            document.save();
        }
        catch (NotesException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }
        catch (ServiceException e) {
            throw new WebApplicationException(ErrorHelper.createErrorResponse(e, Response.Status.BAD_REQUEST));
        }
        finally {
        	if (document != null) {
        		try {
					document.recycle();
				} catch (NotesException e) {
					// Ignore
				}
        	}
        }

        ResponseBuilder builder = Response.ok();
        builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);

        Response response = builder.build();

        return response;
    }

	static private void ifUnmodifiedSince(Document document,
			final String ifUnmodifiedSince) throws NotesException {
		DateTime lastModifiedDateTime = document.getLastModified();
		
		if (lastModifiedDateTime != null) {
			Date lastModifiedDate = lastModifiedDateTime.toJavaDate();
			if (lastModifiedDate != null) {
				// Formats the given date according to the RFC 1123 pattern. 
				String lastModifiedHeader = org.apache.http.impl.cookie.DateUtils.formatDate(lastModifiedDate);
				if (lastModifiedHeader != null) {
					if (ifUnmodifiedSince != null) {
						if (!ifUnmodifiedSince.equalsIgnoreCase(lastModifiedHeader)) {							
            				try {
            					Date ifUnmodifiedSinceDate = org.apache.http.impl.cookie.DateUtils.parseDate(ifUnmodifiedSince);
            					if (lastModifiedDate.after(ifUnmodifiedSinceDate) ) {
            						throw new WebApplicationException(Response.Status.PRECONDITION_FAILED); 
            					}
            				}
            				catch (DateParseException e) {
            					throw new WebApplicationException(Response.Status.PRECONDITION_FAILED); 
            				}
						}
					}                    			
				}
			}
		}
	}


	static private String ifModifiedSince(Document document, final String ifModifiedSince)
			throws NotesException {
		String lastModifiedHeader = null;
		DateTime lastModifiedDateTime = document.getLastModified();
		if (lastModifiedDateTime != null) {
			Date lastModifiedDate = lastModifiedDateTime.toJavaDate();
			if (lastModifiedDate != null) {
				// Formats the given date according to the RFC 1123 pattern. 
				lastModifiedHeader = org.apache.http.impl.cookie.DateUtils.formatDate(lastModifiedDate);
				if (lastModifiedHeader != null) {					
					if (ifModifiedSince != null) {
						if (ifModifiedSince.equalsIgnoreCase(lastModifiedHeader)) {
							throw new WebApplicationException(Response.Status.NOT_MODIFIED); 
						}
						try {
							Date ifModifiedSinceDate = org.apache.http.impl.cookie.DateUtils.parseDate(ifModifiedSince);
							if (ifModifiedSinceDate.equals(lastModifiedDate) || ifModifiedSinceDate.after(lastModifiedDate)) {
								throw new WebApplicationException(Response.Status.NOT_MODIFIED); 
							}
						}
						catch (DateParseException e) {
							// If we can not parse the If-Modified-Since then continue.
							DAS_LOGGER.info("Can not parse the If-Modified-Since header."); // $NON-NLS-1$
						}
					}                    			
				}
			}
		}		
		return lastModifiedHeader;
	}

}