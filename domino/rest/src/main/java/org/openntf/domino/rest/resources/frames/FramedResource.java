/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.rest.resources.frames;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.contributor.DocumentBackupContributor;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DEdgeEntryList.KeyNotFoundException;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphFactory;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.Headers;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.TypeUtils;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.httpmethod.PATCH;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

@SuppressWarnings({ "rawtypes", "unchecked", "nls" })
@Path(Routes.ROOT + "/" + Routes.FRAMED + "/" + Routes.NAMESPACE_PATH_PARAM)
public class FramedResource extends AbstractResource {

	public FramedResource(final ODAGraphService service) {
		super(service);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFramedObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace,
			@Context final Request request) throws JsonException, IOException {
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		if (pm.getVersion() != null) {
			System.out.println("TEMP DEBUG Version number parameter detected " + pm.getVersion().get(0));
		}
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);
		Date lastModified = new Date();
		boolean getLastMod = false;
		try {
			if (pm.get(Parameters.ID) != null) {
				List<String> ids = pm.get(Parameters.ID);
				if (ids.size() == 0) {
					writer.outNull();
				} else if (ids.size() == 1) {
					String id = ids.get(0).trim();
					NoteCoordinate nc = null;
					if (id.startsWith("E")) {
						nc = ViewEntryCoordinate.Utils.getViewEntryCoordinate(id);
					} else if (id.startsWith("V")) {
						nc = ViewEntryCoordinate.Utils.getViewEntryCoordinate(id);
					} else {
						nc = NoteCoordinate.Utils.getNoteCoordinate(id);
						getLastMod = true;
						// System.out.println("TEMP DEBUG isIcon: " +
						// String.valueOf(nc.isIcon()));
					}
					if (nc == null) {
						// writer.outStringProperty("message", "NoteCoordinate
						// is null for id " + id);
					}
					if (graph == null) {
						// writer.outStringProperty("message", "Graph is null
						// for namespace " + namespace);
					}
					NoteCoordinate versionNC = null;
					if (pm.getVersion() != null) {
						String versionString = pm.getVersion().get(0).toString();
						System.out.println("Version parameter detected: " + versionString);
						SimpleDateFormat sdf = TypeUtils.getDefaultDateFormat();
						Date versionDate = sdf.parse(versionString);
						try {
							Session sess = Factory.getSession(SessionType.CURRENT);
							Document doc = sess.getDocumentByMetaversalID(nc.toString());
							Database db = doc.getAncestorDatabase();

							List<DocumentBackupContributor> contributors = Factory.findApplicationServices(DocumentBackupContributor.class);
							if(contributors != null) {
								for(DocumentBackupContributor contributor : contributors) {
									Optional<Document> versionDoc = contributor.createSidecarDocument(db, doc.getUniversalID(), versionDate);
									if(versionDoc.isPresent()) {
										versionNC = versionDoc.get().getNoteCoordinate();
										break;
									}
								}
							}
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
					Object elem = null;
					if (versionNC != null) {
						elem = graph.getElement(versionNC, null);
						//						System.out.println("Got an element from graph with id " + ((Element)elem).getId());
					} else {
						elem = graph.getElement(nc, null);
					}
					if (elem == null) {
						// builder = Response.status(Status.NOT_FOUND);
						// writer.outStringProperty("currentUsername",
						// Factory.getSession(SessionType.CURRENT).getEffectiveUserName());
						// throw new IllegalStateException();
						Response response = ErrorHelper.createErrorResponse(
								"Graph element not found for id " + String.valueOf(id), Response.Status.NOT_FOUND);
						throw new WebApplicationException(response);

					}
					try {
						if (elem instanceof DVertexFrame && getLastMod) {
							lastModified = ((DVertexFrame) elem).getModified();
						}
						if (elem instanceof DEdgeFrame && getLastMod) {
							lastModified = ((DEdgeFrame) elem).getModified();
						}
					} catch (UserAccessException uae) {
						return ErrorHelper
								.createErrorResponse("User " + Factory.getSession(SessionType.CURRENT).getEffectiveUserName()
										+ " is not authorized to access this resource", Response.Status.UNAUTHORIZED);
					} catch (Exception e) {
						throw new WebApplicationException(
								ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
					}
					writer.outObject(elem);
				} else {
					List<Object> maps = new ArrayList<Object>();
					for (String id : ids) {
						NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
						maps.add(graph.getElement(nc, null));
					}
					writer.outArrayLiteral(maps);
				}

			} else if (pm.getKeys() != null) {
				Class<?> type = null;
				if (pm.getTypes() != null) {
					List<CharSequence> types = pm.getTypes();
					String typename = types.get(0).toString();
					type = graph.getTypeRegistry().findClassByName(typename);
				}
				DKeyResolver resolver = graph.getKeyResolver(type);
				List<CharSequence> keys = pm.getKeys();
				if (keys.size() == 0) {
					writer.outNull();
				} else if (keys.size() == 1) {
					CharSequence id = keys.get(0);
					NoteCoordinate nc = resolver.resolveKey(type, URLDecoder.decode(String.valueOf(id), "UTF-8"));
					Object elem = null;
					if (nc != null) {
						// writer.outStringProperty("message", "NoteCoordinate
						// is null for id " + id);
						try {
							elem = graph.getElement(nc);
						} catch (Exception e) {
							// NOOP NTF - this is possible and not an error condition. That's why we have .handleMissingKey.
						}
					}
					if (elem == null) {
						elem = resolver.handleMissingKey(type, id);
						if (elem == null) {
							Response response = ErrorHelper.createErrorResponse("Graph element not found for key " + id,
									Response.Status.NOT_FOUND);
							throw new WebApplicationException(response);
						}
					}
					if (elem instanceof Vertex) {
						// System.out.println("TEMP DEBUG Framing a vertex of
						// type "
						// + elem.getClass().getName());
						VertexFrame vf = (VertexFrame) graph.frame((Vertex) elem, type);
						if (vf instanceof DVertexFrame) {
							lastModified = ((DVertexFrame) vf).getModified();
						}
						writer.outObject(vf);
					} else if (elem instanceof Edge) {
						EdgeFrame ef = (EdgeFrame) graph.frame((Edge) elem, type);
						if (ef instanceof DEdgeFrame) {
							lastModified = ((DEdgeFrame) ef).getModified();
						}
						writer.outObject(ef);
					}
				} else {
					List<Object> maps = new ArrayList<Object>();
					for (CharSequence id : keys) {
						NoteCoordinate nc = resolver.resolveKey(type, id);
						maps.add(graph.getElement(nc, null));
					}
					writer.outArrayLiteral(maps);
				}

				graph.rollback();
			} else {
				MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
				for (@SuppressWarnings("unused") String key : mvm.keySet()) {
					// System.out.println("TEMP DEBUG: " + key + ": " +
					// mvm.getFirst(key));
				}
				Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
				jsonMap.put("namespace", namespace);
				jsonMap.put("status", "active");
				writer.outObject(jsonMap);
			}

		} catch (UserAccessException uae) {
			return ErrorHelper
					.createErrorResponse("User " + Factory.getSession(SessionType.CURRENT).getEffectiveUserName()
							+ " is not authorized to access this resource", Response.Status.UNAUTHORIZED);
		} catch (KeyNotFoundException knfe) {
			ResponseBuilder rb = Response.noContent();
			return rb.build();
		} catch (Exception e) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, lastModified, true, request);
		Response response = berg.build();
		return response;
	}

	private ResponseBuilder getBuilder(final String jsonEntity, final Date lastMod, final boolean includeEtag, final Request request) {
		String etagSource = DominoUtils.md5(jsonEntity);
		EntityTag etag = new EntityTag(etagSource);
		ResponseBuilder berg = null;
		if (request != null) {
			berg = request.evaluatePreconditions(etag);
		}
		if (berg == null) {
			// System.out.println("TEMP DEBUG creating a new builder");
			berg = Response.ok();
			if (includeEtag) {
				berg.tag(etag);
			}
			berg.type(MediaType.APPLICATION_JSON_TYPE);
			berg.entity(jsonEntity);
			berg.lastModified(lastMod);
			CacheControl cc = new CacheControl();
			cc.setMustRevalidate(true);
			cc.setPrivate(true);
			cc.setMaxAge(86400);
			cc.setNoTransform(true);
			berg.cacheControl(cc);
		}
		return berg;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putDocumentByUnid(final String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace,
			@HeaderParam(Headers.IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince, @Context final Request request)
					throws JsonException, IOException {
		ParamMap pm = Parameters.toParamMap(uriInfo);
		Response response = updateFrameByMetaid(requestEntity, namespace, ifUnmodifiedSince, pm, true, request);

		return response;
	}

	// @OPTIONS
	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response patchDocumentByUnid(final String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace,
			@HeaderParam(Headers.IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince, @Context final Request request)
					throws JsonException, IOException {
		ParamMap pm = Parameters.toParamMap(uriInfo);
		Response response = updateFrameByMetaid(requestEntity, namespace, ifUnmodifiedSince, pm, false, request);
		return response;
	}

	protected Response updateFrameByMetaid(final String requestEntity, final String namespace, final String ifUnmodifiedSince,
			final ParamMap pm, final boolean isPut, final Request request) throws JsonException, IOException {
		@SuppressWarnings("unused")
		Response result = null;
		DFramedTransactionalGraph<?> graph = this.getGraph(namespace);
		JsonJavaObject jsonItems = null;
		List<Object> jsonArray = null;
		// ResponseBuilder builder = null;
		JsonGraphFactory factory = JsonGraphFactory.instance;
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);

		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				Object jsonRaw = JsonParser.fromJson(factory, reader);
				if (jsonRaw instanceof JsonJavaObject) {
					jsonItems = (JsonJavaObject) jsonRaw;
				} else if (jsonRaw instanceof List) {
					jsonArray = (List) jsonRaw;
				} else if (jsonRaw instanceof String) {
					//NTF reparse
					reader = new StringReader((String)jsonRaw);
					Object jsonRaw2 = JsonParser.fromJson(factory, reader);
					if (jsonRaw2 instanceof JsonJavaObject) {
						jsonItems = (JsonJavaObject) jsonRaw2;
					} else if (jsonRaw2 instanceof List) {
						jsonArray = (List) jsonRaw2;
					} else {
						System.out.println("ALERT Got a jsonRaw2 of type " + (jsonRaw2 !=null?jsonRaw2.getClass().getName():"null") + ".  Value is: " + String.valueOf(jsonRaw));
					}
				}

			} catch (UserAccessException uae) {
				throw new WebApplicationException(ErrorHelper
						.createErrorResponse("User " + Factory.getSession(SessionType.CURRENT).getEffectiveUserName()
								+ " is not authorized to update this resource", Response.Status.UNAUTHORIZED));
			} catch (Exception e) {
				throw new WebApplicationException(
						ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));

			} finally {
				reader.close();
			}
		} catch (Exception ex) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

		}
		if (jsonArray != null) {
			writer.startArray();
			for (Object raw : jsonArray) {
				if (raw instanceof JsonJavaObject) {
					writer.startArrayItem();
					processJsonUpdate((JsonJavaObject) raw, graph, writer, pm, isPut);
					writer.endArrayItem();
				}
			}
			writer.endArray();
		} else if (jsonItems != null) {
			processJsonUpdate(jsonItems, graph, writer, pm, isPut);
		} else {
			System.out.println("ALERT! Received a JSON object that was not expected!");
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, new Date(), false, request);
		Response response = berg.build();
		return response;
	}

	@SuppressWarnings("unlikely-arg-type")
	private void processJsonUpdate(final JsonJavaObject jsonItems, final DFramedTransactionalGraph graph, final JsonGraphWriter writer,
			final ParamMap pm, final boolean isPut) throws JsonException, IOException {
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		List<String> ids = pm.get(Parameters.ID);
		boolean commit = true;
		if (ids.size() == 0) {
			// TODO no id
		} else {
			JsonFrameAdapter adapter = null;
			for (String id : ids) {
				NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
				Object element = graph.getElement(nc, null);
				if (element instanceof EdgeFrame) {
					adapter = new JsonFrameAdapter(graph, (EdgeFrame) element, null, false);
				} else if (element instanceof VertexFrame) {
					adapter = new JsonFrameAdapter(graph, (VertexFrame) element, null, false);
				} else if (element == null) {
					throw new RuntimeException("Cannot force a metaversalid through REST API. Requested URL: "
							+ ODAGraphService.getCurrentRequest().getRequestURI());
				} else {
					throw new RuntimeException(
							"TODO. Requested URL: " + ODAGraphService.getCurrentRequest().getRequestURI()); // TODO
				}
				Iterator<String> frameProperties = adapter.getJsonProperties();
				CaseInsensitiveString actionName = null;
				CaseInsensitiveString preactionName = null;
				List<Object> actionArguments = null;
				for (CaseInsensitiveString cis : cisMap.keySet()) {
					if (cis.equals("%preaction")) {
						preactionName = new CaseInsensitiveString(String.valueOf(cisMap.get(cis)));
					}
					if (cis.equals("%args")) {
						Object result = cisMap.get(cis);
						if (result instanceof List) {
							actionArguments = (List) result;
						}
					}
				}
				if (preactionName != null) {
					if (actionArguments != null) {
						commit = adapter.runAction(preactionName, actionArguments);
					} else {
						commit = adapter.runAction(preactionName);
					}
				}
				if (commit) {
					while (frameProperties.hasNext()) {
						CaseInsensitiveString key = new CaseInsensitiveString(frameProperties.next());
						if (!key.startsWith("@") && !key.startsWith("%")) {
							Object value = cisMap.get(key);
							if (value != null) {
								adapter.putJsonProperty(key.toString(), value);
								cisMap.remove(key);
							} else if (isPut) {
								adapter.putJsonProperty(key.toString(), value);
							}
						}
					}
					for (CaseInsensitiveString cis : cisMap.keySet()) {
						if (cis.equals("%action")) {
							actionName = new CaseInsensitiveString(String.valueOf(cisMap.get(cis)));
						} else if (!cis.startsWith("@") && !cis.startsWith("%")) {
							Object value = cisMap.get(cis);
							if (value != null) {
								adapter.putJsonProperty(cis.toString(), value);
							}
						}
					}
					adapter.updateReadOnlyProperties();
					if (actionName != null) {
						if (actionArguments != null) {
							commit = adapter.runAction(actionName, actionArguments);
						} else {
							commit = adapter.runAction(actionName);
						}
					}
				}
				writer.outObject(element);
			}
			if (commit) {
				graph.commit();
			} else {
				graph.rollback();
			}
		}
	}

	@SuppressWarnings("resource")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFramedObject(final String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace, @Context final Request request)
					throws JsonException, IOException {
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);

		@SuppressWarnings("unused")
		JsonGraphFactory factory = JsonGraphFactory.instance;

		Map<String, String> report = new HashMap<String, String>();

		List<String> ids = pm.get(Parameters.ID);
		if (ids.size() == 0) {
			throw new WebApplicationException(ErrorHelper.createErrorResponse("No id specified for DELETE",
					Response.Status.INTERNAL_SERVER_ERROR));
		} else {
			for (String id : ids) {
				try {
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
					Object element = graph.getElement(nc, null);
					if (element instanceof Element) {
						((Element) element).remove();
					} else if (element instanceof VertexFrame) {
						graph.removeVertexFrame((VertexFrame) element);
					} else if (element instanceof EdgeFrame) {
						graph.removeEdgeFrame((EdgeFrame) element);
					} else {
						if (element != null) {
							throw new WebApplicationException(ErrorHelper.createErrorResponse(
									"Graph returned unexpected object type " + element.getClass().getName(),
									Response.Status.INTERNAL_SERVER_ERROR));
						}
					}
					report.put(id, "deleted");
				} catch (Exception e) {
					throw new WebApplicationException(
							ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));

				}
			}
			graph.commit();
		}
		try {
			writer.outObject(report);
		} catch (JsonException e) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));

		} catch (IOException e) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		}

		jsonEntity = sw.toString();
		ResponseBuilder bob = getBuilder(jsonEntity, new Date(), false, null);
		Response response = bob.build();
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFramedObject(final String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace, @Context final Request request)
					throws JsonException, IOException {
		// Factory.println("Processing a POST for " + namespace);
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, false);

		JsonJavaObject jsonItems = null;
		List<Object> jsonArray = null;
		JsonGraphFactory factory = JsonGraphFactory.instance;
		Object jsonRaw = null;
		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				jsonRaw = JsonParser.fromJson(factory, reader);
				if (jsonRaw instanceof JsonJavaObject) {
					jsonItems = (JsonJavaObject) jsonRaw;
				} else if (jsonRaw instanceof List) {
					jsonArray = (List) jsonRaw;
				} else if (jsonRaw instanceof String) {
					//NTF reparse
					reader = new StringReader((String)jsonRaw);
					Object jsonRaw2 = JsonParser.fromJson(factory, reader);
					if (jsonRaw2 instanceof JsonJavaObject) {
						jsonItems = (JsonJavaObject) jsonRaw2;
					} else if (jsonRaw2 instanceof List) {
						jsonArray = (List) jsonRaw2;
					} else {
						System.out.println("ALERT Got a jsonRaw2 of type " + (jsonRaw2 !=null?jsonRaw2.getClass().getName():"null") + ".  Value is: " + String.valueOf(jsonRaw));
					}
				}
			} catch (Exception e) {
				throw new WebApplicationException(
						ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
			} finally {
				reader.close();
			}
		} catch (Exception ex) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
		}
		// Map<Object, Object> results = new LinkedHashMap<Object, Object>();
		if (jsonArray != null) {
			writer.startArray();
			for (Object raw : jsonArray) {
				if (raw instanceof JsonJavaObject) {
					writer.startArrayItem();
					processJsonObject((JsonJavaObject) raw, graph, writer, pm/* , results */);
					writer.endArrayItem();
				}
			}
			writer.endArray();
		} else if (jsonItems != null) {
			processJsonObject(jsonItems, graph, writer, pm/* , results */);
		} else {
			System.out.println("ALERT Got a jsonRaw of type " + (jsonRaw !=null?jsonRaw.getClass().getName():"null") + ".  Value is: " + String.valueOf(requestEntity));
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, new Date(), false, null);
		Response response = berg.build();
		return response;
	}

	@SuppressWarnings({ "resource", "unlikely-arg-type" })
	private void processJsonObject(final JsonJavaObject jsonItems, final DFramedTransactionalGraph graph, final JsonGraphWriter writer,
			final ParamMap pm/* , Map<Object, Object> resultMap */) {
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		List<String> ids = pm.get(Parameters.ID);
		boolean commit = true;
		if (ids == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "Cannot POST to frame without an id parameter");
			try {
				writer.outObject(map);
			} catch (JsonException e) {
				throw new WebApplicationException(
						ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
			} catch (IOException e) {
				throw new WebApplicationException(
						ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
			}
		} else {
			if (ids.size() == 0) {
				System.out.println("Cannot POST to frame without an id parameter");
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "Cannot POST to frame without an id parameter");
				try {
					writer.outObject(map);
				} catch (JsonException e) {
					throw new WebApplicationException(
							ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
				} catch (IOException e) {
					throw new WebApplicationException(
							ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
				}
			} else {
				for (String id : ids) {
					//					System.out.println("TEMP DEBUG POSTing to " + id);
					Class<?> type = null;
					if (pm.getTypes() != null) {
						List<CharSequence> types = pm.getTypes();
						String typename = types.get(0).toString();
						type = graph.getTypeRegistry().findClassByName(typename);
					}
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
					Object element = graph.getElement(nc, type);
					if (element instanceof VertexFrame) {
						VertexFrame parVertex = (VertexFrame) element;
						Map<CaseInsensitiveString, Method> adders = graph.getTypeRegistry()
								.getAdders(parVertex.getClass());
						CaseInsensitiveString rawLabel = new CaseInsensitiveString(jsonItems.getAsString("@label"));
						Method method = adders.get(rawLabel);
						if (method == null) {
							method = adders.get(rawLabel + "In");
						}
						if (method != null) {
							String rawId = jsonItems.getAsString("@id");
							NoteCoordinate othernc = NoteCoordinate.Utils.getNoteCoordinate(rawId);
							Object otherElement = graph.getElement(othernc, null);
							if (otherElement instanceof VertexFrame) {
								VertexFrame otherVertex = (VertexFrame) otherElement;
								try {
									Object result = method.invoke(parVertex, otherVertex);
									if (result == null) {
										System.out.println("Invokation of method " + method.getName()
										+ " on a vertex of type " + DGraphUtils.findInterface(parVertex)
										+ " with an argument of type " + DGraphUtils.findInterface(otherVertex)
										+ " resulted in null when we expected an Edge");
									}
									JsonFrameAdapter adapter = new JsonFrameAdapter(graph, (EdgeFrame) result, null,
											false);
									Iterator<String> frameProperties = adapter.getJsonProperties();
									CaseInsensitiveString actionName = null;
									CaseInsensitiveString preactionName = null;
									for (CaseInsensitiveString cis : cisMap.keySet()) {
										if (cis.equals("%preaction")) {
											preactionName = new CaseInsensitiveString(String.valueOf(cisMap.get(cis)));
										}
									}
									if (preactionName != null) {
										commit = adapter.runAction(preactionName);
									}
									if (commit) {
										while (frameProperties.hasNext()) {
											CaseInsensitiveString key = new CaseInsensitiveString(
													frameProperties.next());
											if (!key.startsWith("@")) {
												Object value = cisMap.get(key);
												if (value != null) {
													adapter.putJsonProperty(key.toString(), value);
													cisMap.remove(key);
												}
											}
										}
										for (CaseInsensitiveString cis : cisMap.keySet()) {
											if (cis.equals("%action")) {
												actionName = new CaseInsensitiveString(String.valueOf(cisMap.get(cis)));
											} else if (!cis.startsWith("@")) {
												Object value = cisMap.get(cis);
												if (value != null) {
													adapter.putJsonProperty(cis.toString(), value);
												}
											}
										}
										adapter.updateReadOnlyProperties();
										if (actionName != null) {
											commit = adapter.runAction(actionName);
										}
									}
									writer.outObject(result);
								} catch (Exception e) {
									throw new WebApplicationException(
											ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
								}
							} else {
								throw new WebApplicationException(
										ErrorHelper.createErrorResponse("otherElement is not a VertexFrame. It's a " + (otherElement == null
										? "null" : DGraphUtils.findInterface(otherElement).getName()), Response.Status.INTERNAL_SERVER_ERROR));
							}
						} else {
							Class[] interfaces = element.getClass().getInterfaces();
							String intList = "";
							for (Class inter : interfaces) {
								intList = intList + inter.getName() + ", ";
							}
							String methList = "";
							for (CaseInsensitiveString key : adders.keySet()) {
								methList = methList + key.toString() + ", ";
							}
							throw new WebApplicationException(
									ErrorHelper.createErrorResponse("No method found for " + rawLabel + " on element " + intList + ": "
											+ ((VertexFrame) element).asVertex().getId() + " methods " + methList, Response.Status.INTERNAL_SERVER_ERROR));
						}
					} else {
						throw new WebApplicationException(ErrorHelper.createErrorResponse("Element is null",
								Response.Status.INTERNAL_SERVER_ERROR));
					}
				}
			}
			if (commit) {
				graph.commit();
			} else {
				graph.rollback();
			}
		}
	}
}
