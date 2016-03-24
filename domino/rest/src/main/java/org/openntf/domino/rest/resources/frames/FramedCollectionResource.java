package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.tinkerpop.frames.VertexFrame;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphFactory;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractCollectionResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.types.CaseInsensitiveString;

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
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true);

		if (pm.getTypes() != null) {
			List<CaseInsensitiveString> types = pm.getTypes();
			List<CaseInsensitiveString> filterkeys = pm.getFilterKeys();
			List<CaseInsensitiveString> filtervalues = pm.getFilterValues();
			List<CaseInsensitiveString> partialkeys = pm.getPartialKeys();
			List<CaseInsensitiveString> partialvalues = pm.getPartialValues();

			if (types.size() == 0) {
				writer.outNull();
			} else if (types.size() == 1) {
				CaseInsensitiveString typename = types.get(0);

				Iterable<?> elements = null;
				if (filterkeys != null) {
					elements = graph.getFilteredElements(typename.toString(), filterkeys, filtervalues);
				} else if (partialkeys != null) {
					elements = graph.getFilteredElementsPartial(typename.toString(), partialkeys, partialvalues);
				} else {
					// System.out.println("TEMP DEBUG Getting elements for type "
					// + typename);
					elements = graph.getElements(typename.toString());
				}

				if (elements instanceof FramedEdgeList) {
					FramedEdgeList<?> result = (FramedEdgeList<?>) elements;
					if (pm.getOrderBys() != null) {
						result = result.sortBy(pm.getOrderBys());
					}
					if (pm.getStart() > 0) {
						if (pm.getCount() > 0) {
							result = (FramedEdgeList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount());
						} else {
							result = (FramedEdgeList<?>) result.subList(pm.getStart(), result.size());
						}
					}
					writer.outArrayLiteral(result);
				} else if (elements instanceof FramedVertexList) {
					FramedVertexList<?> result = (FramedVertexList<?>) elements;
					if (pm.getOrderBys() != null) {
						result = result.sortBy(pm.getOrderBys());
					}
					if (pm.getStart() > 0) {
						if (pm.getCount() > 0) {
							result = (FramedVertexList<?>) result.subList(pm.getStart(), pm.getStart() + pm.getCount());
						} else {
							result = (FramedVertexList<?>) result.subList(pm.getStart(), result.size());
						}
					}
					writer.outArrayLiteral(result);
				} else {
					List<Object> maps = new ArrayList<Object>();
					for (Object element : elements) {
						maps.add(element);
					}
					writer.outArrayLiteral(maps);
				}
			} else {
				FramedVertexList vresult = null;
				FramedEdgeList eresult = null;
				for (CaseInsensitiveString typename : types) {
					Iterable<?> elements = null;
					if (filterkeys != null) {
						elements = graph.getFilteredElements(typename.toString(), filterkeys, filtervalues);
					} else if (partialkeys != null) {
						elements = graph.getFilteredElementsPartial(typename.toString(), partialkeys, partialvalues);
					} else {
						elements = graph.getElements(typename.toString());
					}

					if (elements instanceof FramedVertexList) {
						if (vresult == null) {
							vresult = (FramedVertexList) elements;
						} else {
							vresult.addAll((FramedVertexList) elements);
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
					if (pm.getOrderBys() != null) {
						vresult = vresult.sortBy(pm.getOrderBys());
					}
					if (pm.getStart() > 0) {
						if (pm.getCount() > 0) {
							vresult = (FramedVertexList<?>) vresult.subList(pm.getStart(),
									pm.getStart() + pm.getCount());
						} else {
							vresult = (FramedVertexList<?>) vresult.subList(pm.getStart(), vresult.size());
						}
					}
					writer.outArrayLiteral(vresult);
				}
				if (eresult != null) {
					if (pm.getOrderBys() != null) {
						eresult = eresult.sortBy(pm.getOrderBys());
					}
					if (pm.getStart() > 0) {
						if (pm.getCount() > 0) {
							eresult = (FramedEdgeList<?>) eresult.subList(pm.getStart(), pm.getStart() + pm.getCount());
						} else {
							eresult = (FramedEdgeList<?>) eresult.subList(pm.getStart(), eresult.size());
						}
					}
					writer.outArrayLiteral(eresult);
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

		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFramedObject(String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace) throws JsonException, IOException {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true);
		// System.out.println("TEMP DEBUG Starting new POST...");
		JsonJavaObject jsonItems = null;
		JsonGraphFactory factory = JsonGraphFactory.instance;
		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				jsonItems = (JsonJavaObject) JsonParser.fromJson(factory, reader);
			} finally {
				reader.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (jsonItems != null) {
			Map<CaseInsensitiveString, Object> cisMap = new HashMap<CaseInsensitiveString, Object>();
			for (String jsonKey : jsonItems.keySet()) {
				CaseInsensitiveString cis = new CaseInsensitiveString(jsonKey);
				cisMap.put(cis, jsonItems.get(jsonKey));
			}
			String rawType = jsonItems.getAsString("@type");
			if (rawType != null) {
				try {
					Class<?> type = graph.getTypeRegistry().findClassByName(rawType);
					if (VertexFrame.class.isAssignableFrom(type)) {
						VertexFrame parVertex = (VertexFrame) graph.addVertex(null, type);
						try {
							JsonFrameAdapter adapter = new JsonFrameAdapter(graph, parVertex, null);
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
					} else {
						// System.out.print("TYPE is not a Vertex and therefore we can't create it through a POST yet.");
					}
					graph.commit();
				} catch (IllegalArgumentException iae) {
					throw new RuntimeException(iae);
				}

			}
		} else {
			// System.out.println("TEMP DEBUG Nothing to POST. No JSON items found.");
		}

		jsonEntity = sw.toString();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(jsonEntity);
		Response response = builder.build();
		return response;
	}

}
