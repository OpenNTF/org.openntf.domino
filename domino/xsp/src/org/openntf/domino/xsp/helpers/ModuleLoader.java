package org.openntf.domino.xsp.helpers;

import javax.servlet.ServletException;

import org.openntf.domino.Database;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
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

	public static NSFComponentModule loadModule(final Database db) throws ServletException {
		String dbPath = db.getFilePath().replace('\\', '/');
		dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
		if (!StringUtil.isEmpty(db.getServer())) {
			if (!db.getServer().equals(Factory.getLocalServerName())) {
				dbPath = db.getServer() + "!!" + dbPath;
			}
		}
		return loadModule(dbPath);
	}

	public static NSFComponentModule loadModule(final String modName) throws ServletException {
		return getNsfService().loadModule(modName);
	}

}
