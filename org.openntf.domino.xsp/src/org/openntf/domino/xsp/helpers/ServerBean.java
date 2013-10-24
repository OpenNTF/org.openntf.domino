/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class ServerBean extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(ServerBean.class.getName());
	private static ServerBean instance_;
	private final static Object lock_ = new Object();

	private ServerBean() {

	}

	public static ServerBean getCurrentInstance() {
		if (instance_ == null) {
			synchronized (lock_) {
				instance_ = new ServerBean();
			}
		}
		return instance_;
	}
}
