package org.openntf.domino.rest.resources.data;

import javax.ws.rs.Path;

import org.openntf.domino.rest.service.ODAGraphService;
import org.openntf.domino.rest.service.Routes;

@Path(Routes.NAMESPACE_PATH_PARAM + "/" + Routes.DATA + "/" + Routes.DOCUMENT)
public class DocumentResource extends DataResource {

	public DocumentResource(ODAGraphService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

}
