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
package org.openntf.domino.rest.resources.search;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.annotations.MixedFramedVertexList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphWriter;
import org.openntf.domino.rest.resources.AbstractResource;
import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.service.Routes;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.das.utils.ErrorHelper;

@SuppressWarnings({ "rawtypes", "unchecked", "nls" })
@Path(Routes.ROOT + "/" + Routes.TERMS + "/" + Routes.NAMESPACE_PATH_PARAM)
public class TermsResource extends AbstractResource {

	public TermsResource(final ODAGraphService service) {
		super(service);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTermsObject(@Context final UriInfo uriInfo, @PathParam(Routes.NAMESPACE) final String namespace,
			@Context final Request request) throws JsonException, IOException {
		DFramedTransactionalGraph graph = this.getGraph(namespace);
		ParamMap pm = Parameters.toParamMap(uriInfo);
		StringWriter sw = new StringWriter();
		JsonGraphWriter writer = new JsonGraphWriter(sw, graph, pm, false, true, true);
		try {
			List<CharSequence> types = new ArrayList<CharSequence>();
			types.add("org.openntf.domino.graph2.builtin.search.Term");
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
						elements = graph.getFilteredElementsPartial(typename.toString(), partialkeys,
								partialvalues);
					} else if (startskeys != null) {
						elements = graph.getFilteredElementsStarts(typename.toString(), startskeys, startsvalues);
					} else {
						elements = graph.getElements(typename.toString());
					}
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

}
