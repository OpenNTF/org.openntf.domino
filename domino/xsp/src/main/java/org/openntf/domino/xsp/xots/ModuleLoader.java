/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.xsp.xots;

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
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NSFService;

@SuppressWarnings("nls")
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
			if (LCDEnvironment.getInstance() != null) {
				for (HttpService service : LCDEnvironment.getInstance().getServices()) {
					if (service instanceof NSFService) {
						nsfservice_ = (NSFService) service;
						break;
					}
				}
			}
			if (nsfservice_ == null) {
				Factory.println("WARNING: Setting up our own NSFService. (This may happen in a test environment, but should NOT happen on a server)");
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
				dbPath = db.getServer() + "!!" + dbPath; //$NON-NLS-1$
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
			final String path = "/" + module.getModuleName() + "/dummyrequest/to/trigger/refresh"; //$NON-NLS-1$ //$NON-NLS-2$
			final PreloadSession session = new PreloadSession();
			final PreloadRequest request = new PreloadRequest("", "", path); //$NON-NLS-1$ //$NON-NLS-2$
			final PreloadResponse response = new PreloadResponse();
			try {
				getNsfService().doService("", path, session, request, response); //$NON-NLS-1$
			} catch (com.ibm.designer.runtime.domino.adapter.util.PageNotFoundException pnfe) {
				// Thats ok (unless someone really creates a resource with name dummyrequest/to/trigger/refresh)
			} catch (com.ibm.xsp.page.PageNotFoundException pnf) {
				// same as above but on Domino v12 this exception is thrown 
			} catch (com.ibm.xsp.acl.NoAccessSignal nas) {
				// Thats ok. (refresh should be done anyway)
			} catch (IOException e) {
				e.printStackTrace();
			}
			module = getNsfService().loadModule(modName);
		}

		module.updateLastModuleAccess();
		return module;
	}

}
