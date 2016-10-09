package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.httpmethod.PATCH;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.big.NoteCoordinate;
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
import org.openntf.domino.utils.Factory;

@Path(Routes.ROOT + "/" + Routes.FRAMES + "/" + Routes.NAMESPACE_PATH_PARAM)
public class FramedCollectionResource extends AbstractCollectionResource {

	public FramedCollectionResource(ODAGraphService service) {
		super(service);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFramedObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace)
			throws JsonException, IOException {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ResponseBuilder builder = null;
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
				} else {
					MixedFramedVertexList vresult = null;
					FramedEdgeList eresult = null;
					for (CharSequence typename : types) {
						Iterable<?> elements = null;
						if (filterkeys != null) {
							elements = graph.getFilteredElements(typename.toString(), filterkeys, filtervalues);
						} else if (partialkeys != null) {
							elements = graph
									.getFilteredElementsPartial(typename.toString(), partialkeys, partialvalues);
						} else if (startskeys != null) {
							elements = graph.getFilteredElementsStarts(typename.toString(), startskeys, startsvalues);
						} else {
							elements = graph.getElements(typename.toString());
						}
						/*	if (elements != null) {
								System.out.println("TEMP DEBUG found elements for type " + typename.toString());
							} else {
								System.out.println("TEMP DEBUG NO elements for type " + typename.toString());
							}*/
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
				jsonEntity = sw.toString();
			} else {
				// System.out.println("TEMP DEBUG: ID was null therefore we can't report...");

				MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
				for (String key : mvm.keySet()) {
					// System.out.println("TEMP DEBUG: " + key + ": " +
					// mvm.getFirst(key));
				}
				Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
				jsonMap.put("namespace", namespace);
				jsonMap.put("status", "active");
				writer.outObject(jsonMap);
				jsonEntity = sw.toString();
			}
			builder = Response.ok();
		} catch (Exception e) {
			writer.outObject(e);
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			jsonEntity = sw.toString();
		}

		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;
	}

	private List<?> sortAndLimitList(List<?> elements, ParamMap pm) {
		if (elements instanceof FramedEdgeList) {
			FramedEdgeList<?> result = (FramedEdgeList<?>) elements;
			if (pm.getOrderBys() != null) {
				result = result.sortBy(pm.getOrderBys(), pm.getDescending());
			}
			if (pm.getStart() > 0) {
				if (pm.getCount() > 0) {
					result = (FramedEdgeList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount() - 1);
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
			if (pm.getStart() > 0) {
				if (pm.getCount() > 0) {
					result = (FramedVertexList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount() - 1);
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
			throw new IllegalArgumentException("Cannot sort a list of type "
					+ (elements == null ? "null" : elements.getClass().getName()));
		// writer.outArrayLiteral(result);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFramedObject(String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace) throws JsonException, IOException {
		// org.apache.wink.common.internal.registry.metadata.ResourceMetadataCollector
		// rc;
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ResponseBuilder builder = null;
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
					jsonArray = (List) jsonRaw;
				} else {
					// System.out.println("TEMP DEBUG Got an unexpected object from parser "
					// + (jsonRaw == null ? "null" :
					// jsonRaw.getClass().getName()));
				}
				builder = Response.ok();
			} catch (Throwable t) {
				builder = Response.status(Status.INTERNAL_SERVER_ERROR);
				writer.outObject(t);
			} finally {
				reader.close();
			}
		} catch (Exception ex) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			writer.outObject(ex);
		}
		Map<Object, Object> results = new LinkedHashMap<Object, Object>();
		if (jsonArray != null) {
			writer.startArray();
			for (Object raw : jsonArray) {
				if (raw instanceof JsonJavaObject) {
					writer.startArrayItem();
					processJsonObject((JsonJavaObject) raw, graph, writer, results);
					writer.endArrayItem();
				}
			}
			writer.endArray();
		} else if (jsonItems != null) {
			processJsonObject(jsonItems, graph, writer, results);
		} else {
			// System.out.println("TEMP DEBUG Nothing to POST. No JSON items found.");
		}

		jsonEntity = sw.toString();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;
	}

	private void processJsonObject(JsonJavaObject jsonItems, DFramedTransactionalGraph graph, JsonGraphWriter writer,
			Map<Object, Object> results) {
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		String rawType = jsonItems.getAsString("@type");
		String label = jsonItems.getAsString("@label");
		Object rawId = jsonItems.get("@id");
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
						while (frameProperties.hasNext()) {
							CaseInsensitiveString key = new CaseInsensitiveString(frameProperties.next());
							if (!key.startsWith("@")) {
								Object value = cisMap.get(key);
								if (value != null) {
									adapter.putJsonProperty(key.toString(), value);
									cisMap.remove(key);
								}
							} else {
								// System.out.println("TEMP DEBUG Skipping property "
								// + key);
							}
						}
						if (!cisMap.isEmpty()) {
							for (CaseInsensitiveString cis : cisMap.keySet()) {
								if (!cis.startsWith("@")) {
									Object value = cisMap.get(cis);
									if (value != null) {
										adapter.putJsonProperty(cis.toString(), value);
									}
								} else {
									// System.out.println("TEMP DEBUG Skipping property "
									// + cis);
								}
							}
						}
						writer.outObject(parVertex);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (IllegalArgumentException iae) {
				throw new RuntimeException(iae);
			}
		} else if (label != null && label.length() > 0) {
			System.out.println("TEMP DEBUG adding an inline edge...");
			String inid = null;
			String outid = null;
			JsonJavaObject in = jsonItems.getAsObject("@in");
			if (in != null) {
				Object rawinid = in.get("@id");
				if (rawinid instanceof Double) {
					inid = String.valueOf(results.get(rawinid));
					System.out.println("in id is an integer. It resolves to " + inid);
					in.put("@id", inid);
				} else {
					inid = String.valueOf(rawinid);
					System.out.println("in id is not an integer. It's a " + rawinid.getClass().getName() + ": "
							+ String.valueOf(rawinid));
				}
			}
			JsonJavaObject out = jsonItems.getAsObject("@out");
			if (out != null) {
				Object rawoutid = out.get("@id");
				if (rawoutid instanceof Double) {
					outid = String.valueOf(results.get(rawoutid));
					System.out.println("out id is an integer. It resolves to " + outid);
					out.put("@id", outid);
				} else {
					outid = String.valueOf(rawoutid);
					System.out.println("out id is not an integer. It's a " + rawoutid.getClass().getName() + ": "
							+ String.valueOf(rawoutid));
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
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Factory.println("otherElement is not a VertexFrame. It's a "
								+ (otherElement == null ? "null" : DGraphUtils.findInterface(otherElement).getName()));
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
					Factory.println("No method found for " + rawLabel + " on element " + intList + ": "
							+ ((VertexFrame) element).asVertex().getId() + " methods " + methList);
				}
			} else {
				org.openntf.domino.utils.Factory.println("element is not a VertexFrame. It's a "
						+ element.getClass().getName());
			}
		} else {
			System.err.println("Cannot POST without an @type in the JSON");
		}
		graph.commit();
	}

	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response patchDocumentByUnid(String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace,
			@HeaderParam(Headers.IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince) throws JsonException, IOException {
		ParamMap pm = Parameters.toParamMap(uriInfo);
		Response response = updateFrameByMetaid(requestEntity, namespace, ifUnmodifiedSince, pm, false);
		return response;
	}

	protected Response updateFrameByMetaid(String requestEntity, String namespace, String ifUnmodifiedSince,
			ParamMap pm, boolean isPut) throws JsonException, IOException {
		Response result = null;
		DFramedTransactionalGraph<?> graph = this.getGraph(namespace);
		JsonJavaObject jsonItems = null;
		List<Object> jsonArray = null;
		ResponseBuilder builder = null;
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
				builder = Response.ok();
			} catch (Throwable t) {
				builder = Response.status(Status.INTERNAL_SERVER_ERROR);
				writer.outObject(t);
			} finally {
				reader.close();
			}
		} catch (Exception ex) {
			writer.outObject(ex);
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
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
		}

		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(sw.toString());

		result = builder.build();

		return result;
	}

	private void processJsonUpdate(JsonJavaObject jsonItems, DFramedTransactionalGraph graph, JsonGraphWriter writer,
			ParamMap pm, boolean isPut) throws JsonException, IOException {
		Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
		for (String jsonKey : jsonItems.keySet()) {
			CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
			cisMap.put(cis, jsonItems.get(jsonKey));
		}
		String id = jsonItems.getAsString("@id");
		JsonFrameAdapter adapter = null;
		NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
		Object element = graph.getElement(nc, null);
		if (element instanceof EdgeFrame) {
			adapter = new JsonFrameAdapter(graph, (EdgeFrame) element, null, true);
		} else if (element instanceof VertexFrame) {
			adapter = new JsonFrameAdapter(graph, (VertexFrame) element, null, true);
		} else if (element == null) {
			throw new RuntimeException("Cannot force a metaversalid through REST API: " + id);
		} else {
			throw new RuntimeException("TODO"); // TODO
		}
		Iterator<String> frameProperties = adapter.getJsonProperties();
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
			if (!cis.startsWith("@")) {
				Object value = cisMap.get(cis);
				if (value != null) {
					adapter.putJsonProperty(cis.toString(), value);
				}
			}
		}
		writer.outObject(element);
		graph.commit();

	}

}
