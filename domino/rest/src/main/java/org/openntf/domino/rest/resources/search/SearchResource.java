package org.openntf.domino.rest.resources.search;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
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
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.search.RichTextReference;
import org.openntf.domino.graph2.builtin.search.Term;
import org.openntf.domino.graph2.builtin.search.Value;
import org.openntf.domino.graph2.impl.DEdgeEntryList.KeyNotFoundException;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.das.utils.ErrorHelper;

@Path(Routes.ROOT + "/" + Routes.SEARCH + "/" + Routes.NAMESPACE_PATH_PARAM)
public class SearchResource extends AbstractResource {

	public SearchResource(final ODAGraphService service) {
		super(service);
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSearchObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace,
			@Context final Request request) throws JsonException, IOException {
		@SuppressWarnings("rawtypes")
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
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
					}
					Object elem = graph.getElement(nc, null);
					if (elem == null) {
						throw new WebApplicationException(ErrorHelper.createErrorResponse(
								"Graph element not found for id " + id, Response.Status.NOT_FOUND));

					}
					if (elem instanceof DVertexFrame && getLastMod) {
						lastModified = ((DVertexFrame) elem).getModified();
					}
					if (elem instanceof DEdgeFrame && getLastMod) {
						lastModified = ((DEdgeFrame) elem).getModified();
					}
					writer.outObject(elem);
				} else {
					List<Value> valueResults = null;
					List<RichTextReference> rtRefResults = null;
					for (String id : ids) {
						NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id.trim());
						Object rawterm =  graph.getElement(nc, null);
						//						System.out.println("TEMP DEBUG got a " + DGraphUtils.getInterfaceList(rawterm) + " from id " + nc);
						Term term = null;
						if (rawterm instanceof Term) {
							term = (Term) rawterm;
						} else {
							//							System.out.println("TEMP DEBUG didn't get a Term!");
						}
						List<Value> values = term.getValues();
						//						System.out.println("TEMP DEBUG found " + values.size()  + " values for term " + term.getValue() + " in a " + values.getClass().getName());
						List<RichTextReference> rtRefs = term.getRichTextReferences();
						//						System.out.println("TEMP DEBUG found " + rtRefs.size()  + " rtrefs for term " + term.getValue() + " in a " + rtRefs.getClass().getName());
						if (valueResults == null) {
							valueResults = values;
						} else {
							valueResults.retainAll(values);
							//							System.out.println("TEMP DEBUG retained " + valueResults.size()  + " values for term " + term.getValue());
						}
						if (rtRefResults == null) {
							rtRefResults = rtRefs;
						} else {
							rtRefResults.retainAll(rtRefs);
							//							System.out.println("TEMP DEBUG retained " + rtRefResults.size()  + " rtrefs for term " + term.getValue());

						}
					}
					List<Object> combinedResults = new ArrayList<Object>();
					combinedResults.addAll(valueResults);
					combinedResults.addAll(rtRefResults);
					writer.outArrayLiteral(combinedResults);
				}

			} else if (pm.getKeys() != null) {
				Class<?> type = Term.class;
				DKeyResolver resolver = graph.getKeyResolver(type);
				List<CharSequence> keys = pm.getKeys();
				if (keys.size() == 0) {
					writer.outNull();
				} else {
					List<Value> valueResults = null;
					List<RichTextReference> rtRefResults = null;
					String key = URLDecoder.decode(String.valueOf(keys.get(0)), "UTF-8");
					String[] array = key.split(" ");
					for (String cur : array) {
						NoteCoordinate nc = resolver.resolveKey(type, cur);
						Object rawterm =  graph.getElement(nc, null);
						Term term = null;
						if (rawterm instanceof Term) {
							term = (Term) rawterm;
						} else {
						}
						List<Value> values = term.getValues();
						List<RichTextReference> rtRefs = term.getRichTextReferences();
						if (valueResults == null) {
							valueResults = values;
						} else {
							valueResults.retainAll(values);
						}
						if (rtRefResults == null) {
							rtRefResults = rtRefs;
						} else {
							rtRefResults.retainAll(rtRefs);

						}
					}
					List<Object> combinedResults = new ArrayList<Object>();
					combinedResults.addAll(valueResults);
					combinedResults.addAll(rtRefResults);
					writer.outArrayLiteral(combinedResults);
				}

				graph.rollback();
			} else {
				//				MultivaluedMap<String, String> mvm = uriInfo.getQueryParameters();
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


}
