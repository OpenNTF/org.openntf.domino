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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.graph2.DKeyResolver;
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

@Path(Routes.ROOT + "/" + Routes.FRAMED + "/" + Routes.NAMESPACE_PATH_PARAM)
public class FramedResource extends AbstractResource {

	public FramedResource(ODAGraphService service) {
		super(service);
	}

	@SuppressWarnings("unchecked")
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

		if (pm.get(Parameters.ID) != null) {
			List<String> ids = pm.get(Parameters.ID);
			if (ids.size() == 0) {
				writer.outNull();
			} else if (ids.size() == 1) {
				String id = ids.get(0);
				NoteCoordinate nc = null;
				if (id.startsWith("E")) {
					nc = ViewEntryCoordinate.Utils.getViewEntryCoordinate(id);
				} else if (id.startsWith("V")) {
					nc = ViewEntryCoordinate.Utils.getViewEntryCoordinate(id);
				} else {
					nc = NoteCoordinate.Utils.getNoteCoordinate(id);
				}
				if (nc == null) {
					System.err.println("NoteCoordinate is null for id " + id);
				}
				if (graph == null) {
					System.err.println("Graph is null for namespace " + namespace);
				}
				Object elem = graph.getElement(nc, null);
				if (elem == null) {
					throw new IllegalStateException("Graph element is null for id " + id);
				}
				writer.outObject(elem);
			} else {
				List<Object> maps = new ArrayList<Object>();
				for (String id : ids) {
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
					maps.add(graph.getElement(nc, null));
				}
				writer.outArrayLiteral(maps);
			}
			jsonEntity = sw.toString();
		} else if (pm.getKeys() != null) {
			Class<?> type = null;
			if (pm.getTypes() != null) {
				List<CaseInsensitiveString> types = pm.getTypes();
				String typename = types.get(0).toString();
				type = graph.getTypeRegistry().findClassByName(typename);
			}
			DKeyResolver resolver = graph.getKeyResolver(type);
			List<CaseInsensitiveString> keys = pm.getKeys();
			if (keys.size() == 0) {
				writer.outNull();
			} else if (keys.size() == 1) {
				CaseInsensitiveString id = keys.get(0);
				NoteCoordinate nc = resolver.resolveKey(type, id);
				if (nc == null) {
					System.err.println("NoteCoordinate is null for id " + id);
				}
				Object elem = graph.getElement(nc, type);
				if (elem == null) {
					throw new IllegalStateException("Graph element is null for id " + id);
				}
				writer.outObject(elem);
			} else {
				List<Object> maps = new ArrayList<Object>();
				for (CaseInsensitiveString id : keys) {
					NoteCoordinate nc = resolver.resolveKey(type, id);
					maps.add(graph.getElement(nc, null));
				}
				writer.outArrayLiteral(maps);
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

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putDocumentByUnid(String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace,
			@HeaderParam(Headers.IF_UNMODIFIED_SINCE) final String ifUnmodifiedSince) throws JsonException, IOException {
		ParamMap pm = Parameters.toParamMap(uriInfo);
		Response response = updateFrameByMetaid(requestEntity, namespace, ifUnmodifiedSince, pm, true);

		return response;
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
		JsonGraphFactory factory = JsonGraphFactory.instance;
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true);

		try {
			StringReader reader = new StringReader(requestEntity);
			try {
				jsonItems = (JsonJavaObject) JsonParser.fromJson(factory, reader);
				// System.out.println("TEMP DEBUG requestEntity " +
				// requestEntity);
				// System.out.println("TEMP DEBUG check " +
				// jsonItems.get("FirstName"));
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
			List<String> ids = pm.get(Parameters.ID);
			if (ids.size() == 0) {
				// TODO no id
			} else {
				JsonFrameAdapter adapter = null;
				for (String id : ids) {
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
					Object element = graph.getElement(nc, null);
					if (element instanceof EdgeFrame) {
						adapter = new JsonFrameAdapter(graph, (EdgeFrame) element, null);
					} else if (element instanceof VertexFrame) {
						adapter = new JsonFrameAdapter(graph, (VertexFrame) element, null);
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
								// if ("fullname".equals(key)) {
								// System.out.println("TEMP DEBUG fullname: " +
								// value);
								// }
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
				}
				graph.commit();
			}
		}
		ResponseBuilder builder = Response.ok();
		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(sw.toString());

		result = builder.build();

		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("rawtypes")
	public Response createFramedObject(String requestEntity, @Context final UriInfo uriInfo,
			@PathParam(Routes.NAMESPACE) final String namespace) {
		// Factory.println("Processing a POST for " + namespace);
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		String jsonEntity = null;
		ResponseBuilder builder = Response.ok();
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true);

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
			List<String> ids = pm.get(Parameters.ID);
			if (ids.size() == 0) {
				// TODO no id
			} else {
				for (String id : ids) {
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
					Object element = graph.getElement(nc, null);
					if (element instanceof VertexFrame) {
						VertexFrame parVertex = (VertexFrame) element;
						Map<CaseInsensitiveString, Method> adders = graph.getTypeRegistry().getAdders(
								parVertex.getClass());
						CaseInsensitiveString rawLabel = new CaseInsensitiveString(jsonItems.getAsString("@label"));
						Method method = adders.get(rawLabel);
						if (method != null) {
							String rawId = jsonItems.getAsString("@id");
							Object otherElement = graph.getElement(rawId, null);
							if (otherElement instanceof VertexFrame) {
								VertexFrame otherVertex = (VertexFrame) otherElement;
								try {
									Object result = method.invoke(parVertex, otherVertex);
									JsonFrameAdapter adapter = new JsonFrameAdapter(graph, (EdgeFrame) result, null);
									Iterator<String> frameProperties = adapter.getJsonProperties();
									while (frameProperties.hasNext()) {
										String key = frameProperties.next();
										if (!key.startsWith("@")) {
											Object value = jsonItems.get(key);
											if (value != null) {
												adapter.putJsonProperty(key, value);
												jsonItems.remove(key);
											}
										}
									}
									for (String jsonKey : jsonItems.keySet()) {
										if (!jsonKey.startsWith("@")) {
											Object value = jsonItems.getJsonProperty(jsonKey);
											if (value != null) {
												adapter.putJsonProperty(jsonKey, value);
											}
										}
									}
									writer.outObject(result);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								// Factory.println("otherElement is not a VertexFrame. It's a "
								// + otherElement.getClass().getName());
							}
						} else {
							// Class[] interfaces =
							// element.getClass().getInterfaces();
							// String intList = "";
							// for (Class inter : interfaces) {
							// intList = intList + inter.getName() + ", ";
							// }
							// String methList = "";
							// for (CaseInsensitiveString key : adders.keySet())
							// {
							// methList = methList + key.toString() + ", ";
							// }
							// Factory.println("No method found for " + rawLabel
							// + " on element " + intList + ": "
							// + ((VertexFrame) element).asVertex().getId() +
							// " methods " + methList);
						}
					} else {
						// Factory.println("element is not a VertexFrame. It's a "
						// + element.getClass().getName());
					}
				}

			}
		}

		builder.type(MediaType.APPLICATION_JSON_TYPE).entity(sw.toString());
		Response response = builder.build();
		return response;
	}

}
