package org.openntf.domino.xsp.xots;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.domino.xots.XotsContext;

import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * @author Paul Withers
 * @since 2.5.0
 *
 */
public class XotsXspContext extends XotsContext {
	private Map<String, Object> requestScope;
	private Map<String, Object> viewScope;
	private Map<String, Object> sessionScope;
	private Map<String, Object> applicationScope;
	private FacesContext facesContext;
	private XSPContext xspContext;

	/**
	 * Constructor
	 */
	public XotsXspContext() {

	}

	/**
	 * Loads the XPages-related context, ready to be passed to the Xots task
	 * 
	 * @param includeScopes
	 *            Boolean whether or not to include scoped Maps
	 * 
	 */
	public void initialiseXspContext(final Boolean includeScopes) {
		if (includeScopes) {
			setRequestScope(ExtLibUtil.getRequestScope());
			setViewScope(ExtLibUtil.getViewScope());
			setSessionScope(ExtLibUtil.getSessionScope());
			setApplicationScope(ExtLibUtil.getApplicationScope());
		}
		setFacesContext(FacesContext.getCurrentInstance());
		setXspContext(ExtLibUtil.getXspContext());
	}

	/**
	 * Getter for requestScope. Don't write to this for a Runnable - no point, because code continues and dumps requestScope before it can
	 * be accessed!
	 * 
	 * @return Map<String, Object> requestScope map or null
	 */
	public Map<String, Object> getRequestScope() {
		return requestScope;
	}

	/**
	 * Setter for requestScope
	 * 
	 * @param requestScope
	 *            Map<String, Object> requestScope map or null
	 */
	public void setRequestScope(final Map<String, Object> requestScope) {
		this.requestScope = requestScope;
	}

	/**
	 * Getter for viewScope. There may be little point writing to this in a Runnable, because the user may not stay on the current page
	 * while background processing takes place
	 * 
	 * @return Map<String, Object> viewScope map or null
	 */
	public Map<String, Object> getViewScope() {
		return viewScope;
	}

	/**
	 * Setter for viewScope
	 * 
	 * @param viewScope
	 *            Map<String, Object> viewScope map
	 */
	public void setViewScope(final Map<String, Object> viewScope) {
		this.viewScope = viewScope;
	}

	/**
	 * Getter for sessionScope
	 * 
	 * @return Map<String, Object> sessionScope map or null
	 */
	public Map<String, Object> getSessionScope() {
		return sessionScope;
	}

	/**
	 * Setter for sessionScope
	 * 
	 * @param sessionScope
	 *            Map<String, Object> sessionScope map
	 */
	public void setSessionScope(final Map<String, Object> sessionScope) {
		this.sessionScope = sessionScope;
	}

	/**
	 * Getter for applicationScope
	 * 
	 * @return Map<String, Object> applicationScope map or null
	 */
	public Map<String, Object> getApplicationScope() {
		return applicationScope;
	}

	/**
	 * Setter for applicationScope
	 * 
	 * @param applicationScope
	 *            Map<String, Object> applicationScope map
	 */
	public void setApplicationScope(final Map<String, Object> applicationScope) {
		this.applicationScope = applicationScope;
	}

	/**
	 * Getter for FacesContext object
	 * 
	 * @return FacesContext current facesContext
	 */
	public FacesContext getFacesContext() {
		return facesContext;
	}

	/**
	 * Setter for FacesContext object
	 * 
	 * @param facesContext
	 *            FacesContext current facesContext
	 */
	public void setFacesContext(final FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	/**
	 * Getter for XSPContext object
	 * 
	 * @return XSPContext current XspContext or null
	 */
	public XSPContext getXspContext() {
		return xspContext;
	}

	/**
	 * Setter for XSPContext object
	 * 
	 * @param xspContext
	 *            XSPContext current XspContext
	 */
	public void setXspContext(final XSPContext xspContext) {
		this.xspContext = xspContext;
	}

}
