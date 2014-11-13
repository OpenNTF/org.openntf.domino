package org.openntf.domino.xsp.helpers;

import java.io.IOException;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.designer.runtime.domino.adapter.preload.PreloadRequest;
import com.ibm.designer.runtime.domino.adapter.preload.PreloadResponse;
import com.ibm.designer.runtime.domino.adapter.preload.PreloadSession;
import com.ibm.designer.runtime.domino.adapter.util.PageNotFoundException;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;

public enum ModuleLoader {
	;

	private static NSFService nsfservice_;

	/**
	 * Method to find the active NSFService
	 * 
	 * @return the NSFService
	 */
	public static NSFService getNsfService() {
		if (nsfservice_ == null) {
			for (HttpService service : LCDEnvironment.getInstance().getServices()) {
				if (service instanceof NSFService) {
					nsfservice_ = (NSFService) service;
					break;
				}
			}
			if (nsfservice_ == null) {
				System.out.println("WARNING: Setting up our own NSFService.");
				nsfservice_ = new NSFService(LCDEnvironment.getInstance());
			}
		}
		return nsfservice_;
	}

	public static NSFComponentModule loadModule(final Database db, final boolean ensureRefresh) throws ServletException {
		String dbPath = db.getFilePath().replace('\\', '/');
		dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
		if (!StringUtil.isEmpty(db.getServer())) {
			if (!db.getServer().equals(Factory.getLocalServerName())) {
				dbPath = db.getServer() + "!!" + dbPath;
			}
		}
		return loadModule(dbPath, ensureRefresh);
	}

	public static NSFComponentModule loadModule(final String modName, final boolean ensureRefresh) throws ServletException {
		NSFComponentModule module = getNsfService().loadModule(modName);
		if (module == null)
			return null;

		if (ensureRefresh) {
			// The component module is SO weird. You cannot precheck with "shouldRefresh" as it returns always false for the next 2 seconds.
			// So we ALWAYS do a simple request, to ensure that the module is fresh
			final String path = "/" + module.getModuleName() + "/dummyrequest/to/trigger/refresh";
			final PreloadSession session = new PreloadSession();
			final PreloadRequest request = new PreloadRequest("", "", path);
			final PreloadResponse response = new PreloadResponse();
			try {
				getNsfService().doService("", path, session, request, response);
			} catch (PageNotFoundException pnfe) {
				// Thats ok (unless someone really creates a resource with name dummyrequest/to/trigger/refresh)
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			module = getNsfService().loadModule(modName);
		}

		module.updateLastModuleAccess();
		return module;
	}

}
