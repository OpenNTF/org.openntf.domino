/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.thread.AbstractDominoCallable;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.ScheduleData;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xots.XotsUtil;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFResource;
import com.ibm.domino.xsp.module.nsf.RuntimeFileSystem.NSFXspClassResource;

// tell http osgi xots run bundle:org.openntf.domino.xsp org.openntf.domino.xsp.xots.XotsNsfScanner

@Tasklet(session = Tasklet.Session.NATIVE, 	// use server's session
		scope = Tasklet.Scope.SERVER, 				// one scan per server may run concurrent
		context = Tasklet.Context.PLUGIN, 			// in the context of a plugn
		schedule = { "startup", "periodic:90m" }, 	// on Startup and every 90 minutes
		onAllServers = true,						// on all servers
		threadConfig = Tasklet.ThreadConfig.STRICT  // and strict thread config. BubbleExceptions = TRUE
)
/**
 * A Runnable that scans for tasklet classes on a specified server
 * 
 * 
 * @author Nathan T. Freeman
 * @author Roland Praml, FOCONIS AG
 */
@SuppressWarnings("nls")
public class XotsNsfScanner extends AbstractDominoRunnable implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(XotsNsfScanner.class.getName());

	private final String serverName_;

	public XotsNsfScanner() {
		serverName_ = "";
	}

	public XotsNsfScanner(final String serverName) {
		serverName_ = serverName;
	}

	public String getServerName() {
		return serverName_;
	}

	@Override
	public void run() {
		//		Factory.println(this, "Scan started");
		//		List<ScheduleData> ret = scan();
		//		Factory.println(this, "-------------------------------------------------");
		//		Factory.println(this, "Scan stopped. Found tasklets: ");
		//		for (ScheduleData sd : ret) {
		//			Factory.println(this, sd);
		//		}
	}

	/**
	 * Scans all databases on the specified server
	 */
	public List<ScheduleData> scan() {
		Session session = Factory.getSession(SessionType.CURRENT); // Returns a XotsSessionType.NATIVE
		DbDirectory dir = session.getDbDirectory(getServerName());
		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		List<Future<List<ScheduleData>>> futures = new ArrayList<Future<List<ScheduleData>>>();
		for (Database db : dir) {
			try {
				Future<List<ScheduleData>> future = scanDatabase(db);
				if (future != null) {
					futures.add(future);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		List<ScheduleData> ret = new ArrayList<ScheduleData>();
		for (Future<List<ScheduleData>> future : futures) {
			try {
				ret.addAll(future.get());
			} catch (Exception e) {
				// exceptions should already been logged
			}
		}
		setChanged();
		notifyObservers(null);
		return ret;
	}

	//	/**
	//	 * Scan the specified database
	//	 * 
	//	 * @param db
	//	 */
	//	public void scan(final Database db) {
	//		try {
	//			if (!scanDatabase(db)) {
	//				//XotsScheduler.INSTANCE.unregisterTasklets(db.getApiPath());
	//			}
	//		} catch (Throwable t) {
	//			t.printStackTrace();
	//		}
	//
	//		setChanged();
	//		notifyObservers(null);
	//	}

	/**
	 * Scans the given database for xots enabled classes
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	private static class XotsClassScanner extends AbstractDominoCallable<List<ScheduleData>> {
		private static final long serialVersionUID = 1L;

		private String apiPath;

		public XotsClassScanner(final String apiPath) {
			super();
			this.apiPath = apiPath;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return super.getDescription() + ":" + apiPath;
		}

		@Override
		public List<ScheduleData> call() throws Exception {

			NSFComponentModule module = ModuleLoader.loadModule(apiPath, true);
			NotesContext ctx = new NotesContext(module);
			NotesContext.initThread(ctx);
			List<ScheduleData> ret = new ArrayList<ScheduleData>();

			try {
				ctx.initRequest(new FakeHttpRequest(Factory.getLocalServerName()));
				RuntimeFileSystem vfs = module.getRuntimeFileSystem();
				Map<String, NSFResource> resources = vfs.getAllResources();
				ClassLoader mcl = module.getModuleClassLoader();

				String dbPath = ctx.getCurrentDatabase().getFilePath();

				for (NSFResource resource : resources.values()) {
					if (resource instanceof NSFXspClassResource) {
						String path = resource.getNSFPath();

						// Check all classes, but not xsp.*
						if (path.startsWith("WEB-INF/classes/") && path.endsWith(".class") && !path.startsWith("WEB-INF/classes/xsp/")) {
							String className = path.substring(16, path.length() - 6).replace('/', '.');
							try {
								Class<?> clazz = mcl.loadClass(className);
								ScheduleData data = XotsUtil.getSchedule(dbPath, clazz);
								if (data != null) {
									ret.add(data);
								}
								//if (effectiveSchedDefs != null) {
								//	ret.add(new ScheduleDataNSF(replicaID, className, apiPath, effectiveSchedDefs));
								//}

							} catch (Exception e) {
								Factory.println(this, "Cannot load: " + className + ". " + e.getMessage());
							}

						}
					}
					//if (resource instanceof NSFXspClassResource) {
					//	NSFXspClassResource clResource = (NSFXspClassResource) resource;
					//	clResource.
					//	byte[] arrayOfByte = ((RuntimeFileSystem)localObject3).getFileContentAsByteArray(((NotesContext)localObject2).getNotesDatabase(), str);
					//}

				}
			} finally {
				NotesContext.termThread();
			}
			return ret;
		}

	}

	/**
	 * 
	 * @param session
	 * @param db
	 * @return true, if the database has XOTS-Classes
	 * @throws @throws
	 *             ServletException
	 */
	protected Future<List<ScheduleData>> scanDatabase(final Database db) {
		//NTF Keeping JG's implementation since he made an enhancement to the IconNote class for it! :)
		log_.finest("Scanning database " + db.getApiPath() + " for Xots Tasklets");

		try {
			Database template = db.getXPageSharedDesignTemplate();
			DatabaseDesign design = template == null ? db.getDesign() : template.getDesign();
			if (design.isAPIEnabled()) {
				log_.info("ODA enabled database: " + db.getApiPath());
				return Xots.submit(new XotsClassScanner(db.getApiPath()));

			}

			//			
			//			
			//			
			//			
			//			
			//			IconNote icon = design.getIconNote();
			//
			//			if (icon != null) {
			//				String[] xotsClassNames = icon.getXotsClassNames();
			//				if (xotsClassNames != null && xotsClassNames.length > 0) {
			//					if (log_.isLoggable(Level.FINE)) {
			//						log_.fine("Adding Xots Tasklets for database " + db.getApiPath());
			//					}
			//
			//					//					String dbPath = db.getFilePath().replace('\\', '/');
			//					//					dbPath = NSFService.FILE_CASE_INSENSITIVE ? dbPath.toLowerCase() : dbPath;
			//					//					if (!StringUtil.isEmpty(db.getServer())) {
			//					//						dbPath = db.getServer() + "!!" + dbPath;
			//					//					}
			//					//
			//					//					NSFComponentModule module = OpenntfHttpService.sGetNsfService().loadModule(dbPath);
			//					//
			//					//					Runnable loader = new LoaderRunnable(db.getApiPath(), xotsClassNames, module);
			//					//					XotsDaemon.addToQueue(loader);
			//					return true;
			//				}
			//			}
		} catch (UserAccessException uae) {
			Factory.println(this, "WARNING: " + uae.getMessage());
			// we don't log the warning in the log, as this may confuse admins.
			log_.log(Level.INFO, uae.getMessage(), uae);
		} catch (FileNotFoundException e) {
			Factory.println(this, "WARNING: " + e.getMessage());
			log_.log(Level.INFO, e.getMessage(), e);
		}
		return null;
	}
}
