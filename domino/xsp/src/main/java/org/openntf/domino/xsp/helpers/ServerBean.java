/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;

/**
 * @author Nathan T. Freeman
 * 
 *         Server-scope map
 */
public final class ServerBean extends ConcurrentHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
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

	/**
	 * RPr: What is this? Loading a class from different module will throw a "ModuleClassLoader.InternalClassLoadermismatch" Exception
	 * 
	 * @Deprecated until someone explains me how that should work.
	 */
	@Deprecated
	public void cacheObject(final String key, final String filepath, final String className) {
		try {
			final ServerBean thisBean = this;
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					NSFService nsfservice = null;
					for (HttpService service : LCDEnvironment.getInstance().getServices()) {
						if (service instanceof NSFService) {
							nsfservice = (NSFService) service;
							break;
						}
					}
					if (nsfservice != null) {
						try {
							NSFComponentModule forcedMod = nsfservice.loadModule(filepath);
							Class<?> klazz = forcedMod.getModuleClassLoader().loadClass(className);
							Object cacheMeIfYouCan = klazz.newInstance();
							thisBean.put(key, cacheMeIfYouCan);
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
