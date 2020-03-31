/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.annotations.MixedFramedVertexList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphFactory;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractCollectionResource;
import org.openntf.domino.rest.service.Headers;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.httpmethod.PATCH;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

@Path(Routes.ROOT + "/" + Routes.FRAMES + "/" + Routes.NAMESPACE_PATH_PARAM)
public class FramedCollectionResource extends AbstractCollectionResource {

	public FramedCollectionResource(final ODAGraphService service) {
		super(service);
	}

	private ResponseBuilder getBuilder(final String jsonEntity, final Date lastMod, final boolean includeEtag, final Request request) {
		String etagSource = DominoUtils.md5(jsonEntity);
		EntityTag etag = new EntityTag(etagSource);
		ResponseBuilder berg = null;
		if (request != null) {
			berg = request.evaluatePreconditions(etag);
		}

		if (berg == null) {
			berg = Response.ok();
			if (includeEtag) {
				berg.tag(etag);
			}
			berg.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
			berg.lastModified(lastMod);
			CacheControl cc = new CacheControl();
			cc.setMustRevalidate(true);
			cc.setPrivate(true);
			cc.setMaxAge(86400);
			cc.setNoTransform(true);
			berg.cacheControl(cc);
		} else {
			// System.out.println("TEMP DEBUG got a hit for etag " +
			// etagSource);
		}
		return berg;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFramedObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace,
			@Context final Request request) throws JsonException, IOException {
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, true);
		try {
			if (pm.getTypes() != null) {
				List<CharSequence> types = pm.getTypes();
				List<CharSequence> filterkeys = pm.getFilterKeys();
				List<CharSequence> filtervalues = pm.getFilterValues();
				List<CharSequence> partialkeys = pm.getPartialKeys();
				List<CharSequence> partialvalues = pm.getPartialValues();
				List<CharSequence> startskeys = pm.getStartsKeys();
				List<CharSequence> startsvalues = pm.getStartsValues();

				if (types.size() == 0) {
					writer.outNull();
				} else if (types.size() == 1) {
					long startTime = new Date().getTime();
					CharSequence typename = types.get(0);

					Iterable<?> elements = null;
					if (filterkeys != null) {
						elements = graph.getFilteredElements(typename.toString(), filterkeys, filtervalues);
					} else if (partialkeys != null) {
						elements = graph.getFilteredElementsPartial(typename.toString(), partialkeys, partialvalues);
					} else if (startskeys != null) {
						elements = graph.getFilteredElementsStarts(typename.toString(), startskeys, startsvalues);
					} else {
						elements = graph.getElements(typename.toString());
					}

					if (elements instanceof FramedEdgeList) {
						List<?> result = sortAndLimitList((List<?>) elements, pm);
						writer.outArrayLiteral(result);
					} else if (elements instanceof FramedVertexList) {
						List<?> result = sortAndLimitList((List<?>) elements, pm);
						writer.outArrayLiteral(result);
					} else {
						List<Object> maps = new ArrayList<Object>();
						for (Object element : elements) {
							maps.add(element);
						}
						writer.outArrayLiteral(maps);
					}
					long endTime = new Date().getTime();
					System.out.println("TEMP DEBUG Output for " + typename + " took " + (endTime-startTime) + "ms");
				} else {
					MixedFramedVertexList vresult = null;
					FramedEdgeList eresult = null;
					for (CharSequence typename : types) {
						Iterable<?> elements = null;
						if (filterkeys != null) {
							elements = graph.getFilteredElements(typename.toString(), filterkeys, filtervalues);
						} else if (partialkeys != null) {
							elements = graph.getFilteredElementsPartial(typename.toString(), partialkeys,
									partialvalues);
						} else if (startskeys != null) {
							elements = graph.getFilteredElementsStarts(typename.toString(), startskeys, startsvalues);
						} else {
							elements = graph.getElements(typename.toString());
						}
						/*
						 * if (elements != null) { System.out.
						 * println("TEMP DEBUG found elements for type " +
						 * typename.toString()); } else {
						 * System.out.println("TEMP DEBUG NO elements for type "
						 * + typename.toString()); }
						 */
						if (elements instanceof FramedVertexList) {
							if (vresult == null) {
								vresult = new MixedFramedVertexList(graph, null, (FramedVertexList) elements);
							} else {
								vresult.addAll((List<?>) elements);
							}
						} else if (elements instanceof FramedEdgeList) {
							if (eresult == null) {
								eresult = (FramedEdgeList) elements;
							} else {
								eresult.addAll((FramedEdgeList) elements);
							}
						}

					}
					if (vresult != null) {
						List<?> result = sortAndLimitList(vresult, pm);
						writer.outArrayLiteral(result);
					}
					if (eresult != null) {
						List<?> result = sortAndLimitList(eresult, pm);
						writer.outArrayLiteral(result);
					}
				}

			} else {
				// System.out.println("TEMP DEBUG: ID was null therefore we
				// can't report...");
				Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
				jsonMap.put("namespace", namespace);
				jsonMap.put("status", "active");
				writer.outObject(jsonMap);
			}
		} catch (UserAccessException uae) {
			throw new WebApplicationException(ErrorHelper.createErrorResponse(uae, Response.Status.UNAUTHORIZED));
		} catch (Exception e) {
			throw new WebApplicationException(
					ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, new Date(), true, request);
		Response response = berg.build();
		return response;
	}

	private List<?> sortAndLimitList(final List<?> elements, final ParamMap pm) {
		if (elements instanceof FramedEdgeList) {
			FramedEdgeList<?> result = (FramedEdgeList<?>) elements;
			if (pm.getOrderBys() != null) {
				result = result.sortBy(pm.getOrderBys(), pm.getDescending());
			}
			if (pm.getStart() >= 0) {
				if (pm.getCount() > 0) {
					result = (FramedEdgeList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount());
				} else {
					result = (FramedEdgeList<?>) result.subList(pm.getStart(), result.size());
				}
			}
			return result;
		} else if (elements instanceof FramedVertexList) {
			FramedVertexList<?> result = (FramedVertexList<?>) elements;
			if (pm.getOrderBys() != null) {
				result = result.sortBy(pm.getOrderBys(), pm.getDescending());
			}
			if (pm.getStart() >= 0) {
				if (pm.getCount() > 0) {
					result = (FramedVertexList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount());
				} else {
					result = (FramedVertexList<?>) result.subList(pm.getStart(), result.size());
				}
			}
			return result;
		} else if (elements instanceof MixedFramedVertexList) {
			MixedFramedVertexList result = (MixedFramedVertexList) elements;
			if (pm.getOrderBys() != null) {
				result = result.sortBy(pm.getOrderBys(), pm.getDescending());
			}
			if (pm.getStart() > 0) {
				if (pm.getCount() > 0) {
					result = (MixedFramedVertexList) result.subList(pm.getStart(), pm.getStart() + pm.getCount());
				} else {
					result = (MixedFramedVertexList) result.subList(pm.getStart(), result.size());
				}
			}
			return result;
		} else
			throw new IllegalArgumentException(
					"Cannot sort a list of type " + (elements == null ? "null" : elements.getClass().getName()));
		// writer.outArrayLiteral(result);
	}

	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFramedObject(final String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace, @Context final Request request)
					throws JsonException, IOException {
		// org.apache.wink.common.internal.registry.metadata.ResourceMetadataCollector
		// rc;
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, true);
		// System.out.println("TEMP DEBUG Starting new POST...");
		JsonJavaObject jsonItems = null;
		List<Object> jsonArray = null;
		JsonGraphFactory factory = JsonGraphFactory.instance;
		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				Object jsonRaw = JsonParser.fromJson(factory, reader);
				if (jsonRaw instanceof JsonJavaObject) {
					jsonItems = (JsonJavaObject) jsonRaw;
				} else if (jsonRaw instanceof List) {
					jsonArray = (List<Object>) jsonRaw;
					// System.out.println("TEMP DEBUG processing a POST with an
					// array of size " + jsonArray.size());
				} else {
					// System.out.println("TEMP DEBUG Got an unexpected object
					// from parser "
					// + (jsonRaw == null ? "null" :
					// jsonRaw.getClass().getName()));
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
		boolean committed = true;
		Map<Object, Object> results = new LinkedHashMap<Object, Object>();
		if (jsonArray != null) {
			writer.startArray();
			for (Object raw : jsonArray) {
				if (raw instanceof JsonJavaObject) {
					writer.startArrayItem();
					committed = processJsonObject((JsonJavaObject) raw, graph, writer, results);
					writer.endArrayItem();
				} else {
					System.err.println("Raw array member isn't a JsonJavaObject. It's a "
							+ (raw == null ? "null" : raw.getClass().getName()));
				}
			}
			writer.endArray();
		} else if (jsonItems != null) {
			committed = processJsonObject(jsonItems, graph, writer, results);
		} else {
			// System.out.println("TEMP DEBUG Nothing to POST. No JSON items
			// found.");
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, new Date(), false, request);
		Response response = berg.build();
		if (!committed) {
			graph.rollback();
		}
		return response;
	}

	private boolean processJsonObject(final JsonJavaObject jsonItems, final DFramedTransactionalGraph graph, final JsonGraphWriter writer,
			final Map<Object, Object> results) {
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		String rawType = jsonItems.getAsString("@type");
		String label = jsonItems.getAsString("@label");
		Object rawId = jsonItems.get("@id");
		boolean commit = true;
		if (rawType != null && rawType.length() > 0) {
			try {
				rawType = rawType.trim();
				Class<?> type = graph.getTypeRegistry().findClassByName(rawType);
				if (VertexFrame.class.isAssignableFrom(type)) {
					VertexFrame parVertex = (VertexFrame) graph.addVertex(null, type);
					String resultId = parVertex.asVertex().getId().toString();
					results.put(rawId, resultId);
					try {
						JsonFrameAdapter adapter = new JsonFrameAdapter(graph, parVertex, null, true);
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
									}
								}
							}
							if (!cisMap.isEmpty()) {
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
						}
						writer.outObject(parVertex);
					} catch (Exception e) {
						throw new WebApplicationException(
								ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
					}
				}
			} catch (IllegalArgumentException iae) {
				throw new RuntimeException(iae);
			}
		} else if (label != null && label.length() > 0) {
			// System.out.println("TEMP DEBUG adding an inline edge...");
			String inid = null;
			String outid = null;
			JsonJavaObject in = jsonItems.getAsObject("@in");
			if (in != null) {
				Object rawinid = in.get("@id");
				if (rawinid instanceof Double) {
					inid = String.valueOf(results.get(rawinid)).trim();
					// System.out.println("in id is an integer. It resolves to "
					// + inid);
					in.put("@id", inid);
				} else {
					inid = String.valueOf(rawinid).trim();
					// System.out.println("in id is not an integer. It's a " +
					// rawinid.getClass().getName() + ": "
					// + String.valueOf(rawinid));
				}
			}
			JsonJavaObject out = jsonItems.getAsObject("@out");
			if (out != null) {
				Object rawoutid = out.get("@id");
				if (rawoutid instanceof Double) {
					outid = String.valueOf(results.get(rawoutid));
					// System.out.println("out id is an integer. It resolves to
					// " + outid);
					out.put("@id", outid);
				} else {
					outid = String.valueOf(rawoutid).trim();
					// System.out.println("out id is not an integer. It's a " +
					// rawoutid.getClass().getName() + ": "
					// + String.valueOf(rawoutid));
				}
			}
			NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(inid);
			Object element = graph.getElement(nc, null);
			if (element instanceof VertexFrame) {
				VertexFrame parVertex = (VertexFrame) element;
				Map<CaseInsensitiveString, Method> adders = graph.getTypeRegistry().getAdders(parVertex.getClass());
				CaseInsensitiveString rawLabel = new CaseInsensitiveString(label);
				Method method = adders.get(rawLabel);
				if (method == null) {
					rawLabel = new CaseInsensitiveString(label + "In");
					method = adders.get(rawLabel);
				}
				if (method != null) {
					NoteCoordinate othernc = NoteCoordinate.Utils.getNoteCoordinate(outid);
					Object otherElement = graph.getElement(othernc, null);
					if (otherElement instanceof VertexFrame) {
						VertexFrame otherVertex = (VertexFrame) otherElement;
						try {
							Object result = method.invoke(parVertex, otherVertex);
							if (result == null) {
								System.out.println("Invokation of method " + method.getName() + " on a vertex of type "
										+ DGraphUtils.findInterface(parVertex) + " with an argument of type "
										+ DGraphUtils.findInterface(otherVertex)
										+ " resulted in null when we expected an Edge");
							}
							JsonFrameAdapter adapter = new JsonFrameAdapter(graph, (EdgeFrame) result, null, true);
							Iterator<String> frameProperties = adapter.getJsonProperties();
							while (frameProperties.hasNext()) {
								CaseInsensitiveString key = new CaseInsensitiveString(frameProperties.next());
								if (!key.startsWith("@")) {
									Object value = cisMap.get(key);
									if (value != null) {
										adapter.putJsonProperty(key.toString(), value);
										cisMap.remove(key);
									}
								}
							}
							for (CaseInsensitiveString cis : cisMap.keySet()) {
								if (!cis.startsWith("@")) {
									Object value = cisMap.get(cis);
									if (value != null) {
										adapter.putJsonProperty(cis.toString(), value);
									}
								}
							}
							writer.outObject(result);
						} catch (IllegalArgumentException iae) {
							Exception e = new RuntimeException("Invokation of method " + method.getName() + " on a vertex of type "
									+ DGraphUtils.findInterface(parVertex) + " with an argument of type "
									+ DGraphUtils.findInterface(otherVertex)
									+ " resulted in an exception", iae);
							throw new WebApplicationException(
									ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
						} catch (Exception e) {
							throw new WebApplicationException(
									ErrorHelper.createErrorResponse(e, Response.Status.INTERNAL_SERVER_ERROR));
						}
					} else {
						Factory.println("otherElement is not a VertexFrame. It's a "
								+ (otherElement == null ? "null" : DGraphUtils.findInterface(otherElement).getName()));
					}
				} else {
					Class<?>[] interfaces = element.getClass().getInterfaces();
					String intList = "";
					for (Class<?> inter : interfaces) {
						intList = intList + inter.getName() + ", ";
					}
					String methList = "";
					for (CaseInsensitiveString key : adders.keySet()) {
						methList = methList + key.toString() + ", ";
					}
					Factory.println("No method found for " + rawLabel + " on element " + intList + ": "
							+ ((VertexFrame) element).asVertex().getId() + " methods " + methList);
				}
			} else {
				org.openntf.domino.utils.Factory
				.println("element is not a VertexFrame. It's a " + element.getClass().getName());
			}
		} else {
			System.err.println("Cannot POST without an @type in the JSON");
		}
		if (commit) {
			graph.commit();
		}
		return commit;
	}

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
		Response result = null;
		DFramedTransactionalGraph<?> graph = this.getGraph(namespace);
		JsonJavaObject jsonItems = null;
		List<Object> jsonArray = null;
		JsonGraphFactory factory = JsonGraphFactory.instance;
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, true);

		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				Object jsonRaw = JsonParser.fromJson(factory, reader);
				if (jsonRaw instanceof JsonJavaObject) {
					jsonItems = (JsonJavaObject) jsonRaw;
				} else if (jsonRaw instanceof List) {
					jsonArray = (List) jsonRaw;
				}
			} catch (Exception e) {
				e.printStackTrace();
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
					processJsonUpdate((JsonJavaObject) raw, graph, writer, pm, request.getMethod());
					writer.endArrayItem();
				} else {
					System.err.println("Expected a JsonJavaObject but instead we got a " + raw.getClass().getName());
				}
			}
			writer.endArray();
		} else if (jsonItems != null) {
			processJsonUpdate(jsonItems, graph, writer, pm, request.getMethod());
		}

		String jsonEntity = sw.toString();
		ResponseBuilder berg = getBuilder(jsonEntity, new Date(), false, request);
		Response response = berg.build();
		return response;
	}

	private void processJsonUpdate(final JsonJavaObject jsonItems, final DFramedTransactionalGraph graph, final JsonGraphWriter writer,
			final ParamMap pm, final String method) throws JsonException, IOException {
		boolean commit = true;
		boolean isPut = "put".equalsIgnoreCase(method);
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		String id = jsonItems.getAsString("@id");
		JsonFrameAdapter adapter = null;
		NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
		Object element = graph.getElement(nc, null);
		if (element instanceof EdgeFrame) {
			adapter = new JsonFrameAdapter(graph, (EdgeFrame) element, null, true);
		} else if (element instanceof VertexFrame) {
			adapter = new JsonFrameAdapter(graph, (VertexFrame) element, null, true);
		} else if (element == null) {
			if ("post".equalsIgnoreCase(method)) {
				throw new RuntimeException("Cannot force a metaversalid through REST API: " + id);
			} else {
				throw new RuntimeException("Element id " + id + " was not found in the graph");
			}
		} else {
			throw new RuntimeException("TODO"); // TODO
		}
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
				CaseInsensitiveString key = new CaseInsensitiveString(frameProperties.next());
				if (!key.startsWith("@")) {
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
		writer.outObject(element);
		if (commit) {
			graph.commit();
		} else {
			graph.rollback();
		}

	}

}
