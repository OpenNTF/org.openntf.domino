/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.IXspHttpServletJsonResponseCallback;
import org.openntf.domino.xsp.IXspHttpServletResponseCallback;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.services.HttpServiceConstants;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.webapp.XspHttpServletResponse;

/**
 * @author Nathan T. Freeman
 * 
 *         Class of XPages utilities
 */
public class XspUtils {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(XspUtils.class.getName());

	/**
	 * Constructor
	 */
	private XspUtils() {

	}

	/**
	 * Gets the back-end Document using a DominoDocument datasource, applying changes in front end, and converts to org.openntf.domino
	 * version.<br/>
	 * Avoids the need to catch a NotesException
	 * 
	 * <b>NOTE:<b> In recent experience, this results in save conflicts, no idea why. I would recommend not using (PSW)
	 * 
	 * @param doc
	 *            DominoDocument datasource
	 * @return Document back-end document with front-end values applied, using doc.getDocument(true)
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public static Document getBEDoc(final DominoDocument doc) {
		Document beDoc;
		try {
			if (ODAPlatform.isAppGodMode(null)) {
				beDoc = (Document) doc.getDocument(true);
			} else {
				beDoc = XSPUtil.wrap(doc.getDocument(true));
			}
		} catch (Throwable e) {
			DominoUtils.handleException(e);
			return null;
		}
		return beDoc;
	}

	/**
	 * A generic method that performs boilerplate code to extract XspHttpServletRequest and HttpServletResponse; triggers a callback method
	 * passed in giving it access to the request, response and a JsonJavaObject; then closes everything down successfully
	 * 
	 * @param callback
	 *            anonymous inner class callback that implements IXspHttpServletResponse, so has a process() method that can be called from
	 *            here
	 * @throws IOException
	 *             that may be caused by manipulating the response
	 * @throws JsonException
	 *             caused by malformed JSON, shouldn't happen
	 * @since ODA 4.3.0
	 */
	public static void initialiseAndProcessResponseAsJson(final IXspHttpServletJsonResponseCallback callback)
			throws IOException, JsonException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ext = ctx.getExternalContext();
		XspHttpServletResponse response = (XspHttpServletResponse) ext.getResponse();
		response.setContentType(HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON);
		response.setHeader("Cache-Control", "no-cache");
		JsonJavaObject result = new JsonJavaObject();
		callback.process((HttpServletRequest) ext.getRequest(), response, result);
		if (!response.isStatusSet()) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		PrintWriter writer = response.getWriter();
		writer.write(result.toString());
		//  Terminate the request processing lifecycle.
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * A more basic generic method that performs boilerplate code to extract XspHttpServletRequest and HttpServletResponse; triggers a
	 * callback method passed in, passing it the request and response; then terminates the response
	 * 
	 * It's down to you to handle printing something to the response
	 * 
	 * @param callback
	 *            anonymous inner class callback that implements IXspHttpServletResponse, so has a process() method that can be called from
	 *            here
	 * @throws IOException
	 *             that may be caused by manipulating the response
	 * @since ODA 4.3.0
	 */
	public static void initialiseAndProcessResponse(final IXspHttpServletResponseCallback callback) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ext = ctx.getExternalContext();
		XspHttpServletResponse response = (XspHttpServletResponse) ext.getResponse();
		response.setContentType(HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON);
		response.setHeader("Cache-Control", "no-cache");
		callback.process((HttpServletRequest) ext.getRequest(), response);
		//  Terminate the request processing lifecycle.
		FacesContext.getCurrentInstance().responseComplete();
	}

}
