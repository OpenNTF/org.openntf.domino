/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 *         Server-scope map
 */
public final class ServerBean extends ConcurrentHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(ServerBean.class.getName());
	private final static ServerBean instance_ = new ServerBean();

	/**
	 * Constructor
	 */
	private ServerBean() {

	}

	/**
	 * Method to get the currently-loaded instance
	 * 
	 * @return ServerBean
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public static ServerBean getCurrentInstance() {
		return instance_;
	}
}
