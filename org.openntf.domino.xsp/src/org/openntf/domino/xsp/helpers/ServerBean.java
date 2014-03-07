/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public final class ServerBean extends ConcurrentHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(ServerBean.class.getName());
	private final static ServerBean instance_ = new ServerBean();

	private ServerBean() {

	}

	public static ServerBean getCurrentInstance() {
		return instance_;
	}
}
