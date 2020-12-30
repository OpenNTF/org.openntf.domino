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
package org.openntf.domino.xots;

import java.util.Arrays;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.logging.BaseOpenLogItem;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet.Interface;

import com.ibm.commons.util.StringUtil;

@SuppressWarnings("nls")
public enum XotsUtil {
	;
	/**
	 * Returns the schedule settings of the given class
	 * 
	 */
	public static ScheduleData getSchedule(final String replicaId, final Class<?> clazz)
			throws IllegalAccessException, InstantiationException {
		Tasklet annot = clazz.getAnnotation(Tasklet.class);
		String[] effectiveSchedDefs = null;

		if (annot == null)
			return null;
		String[] schedDefs = annot.schedule();
		System.out.println("### Tasklet: " + clazz.getName() + " " + replicaId + " Sched: " + Arrays.toString(schedDefs));
		for (String schedDef : schedDefs) {
			if (!schedDef.equals("")) {
				effectiveSchedDefs = schedDefs;
				if (schedDef.equals("dynamic")) {
					Tasklet.Interface ti = (Interface) clazz.newInstance();
					effectiveSchedDefs = ti.getDynamicSchedule();
					break;
				}
			}
		}

		if (effectiveSchedDefs == null)
			return null;
		return new ScheduleDataNSF(replicaId, clazz.getName(), effectiveSchedDefs, annot.onAllServers());
	}

	/**
	 * Returns the schedule settings of the given object
	 */
	public static String[] getSchedule(final Object obj) {
		Tasklet annot = obj.getClass().getAnnotation(Tasklet.class);
		String[] effectiveSchedDefs = null;

		if (annot != null) {
			String[] schedDefs = annot.schedule();

			for (String schedDef : schedDefs) {
				if (!schedDef.equals("")) {
					effectiveSchedDefs = schedDefs;
					if (schedDef.equals("dynamic")) {
						Tasklet.Interface ti = (Interface) obj;
						effectiveSchedDefs = ti.getDynamicSchedule();
						break;
					}
				}
			}

		}
		return effectiveSchedDefs;
	}

	/**
	 * Handle exceptions, defining the "current database" context for OpenLog
	 * 
	 * @param t
	 *            Throwable
	 * @param currDb
	 *            Database current database context
	 */
	public static void handleException(final Throwable t, final Database currDb) {
		BaseOpenLogItem ol = new BaseOpenLogItem();
		ol.setCurrentDatabase(currDb);
		ol.logError(t);
	}

	/**
	 * Handle an exception, passing a XotsContext (used for XPages / OSGi tasks extending {@linkplain AbstractXotsRunnable} or
	 * {@linkplain AbstractXotsCallable})
	 * 
	 * @param t
	 *            Throwable
	 * @param xotsContext
	 *            XotsContext from which to get Log database and current database path
	 */
	public static void handleException(final Throwable t, final XotsContext xotsContext) {
		BaseOpenLogItem ol = new BaseOpenLogItem();

		if (StringUtil.isNotEmpty(xotsContext.getContextApiPath())) {
			try {
				Session currSess = Factory.getSession(SessionType.NATIVE);
				ol.setCurrentDatabase(currSess.getDatabase(xotsContext.getContextApiPath()));
			} catch (Exception e) {
				// No current database??
			}
		}

		if (StringUtil.isNotEmpty(xotsContext.getOpenLogApiPath())) {
			ol.setLogDbName(xotsContext.getOpenLogApiPath());
		}

		if (StringUtil.isNotEmpty(xotsContext.getTaskletClass())) {
			ol.setThisAgent(xotsContext.getTaskletClass());
		}
		ol.logError(t);
	}

	/**
	 * Gets the OpenLogItem for more complex logging, passing database context
	 * 
	 * @param currDb
	 *            Database current database context
	 */
	public static BaseOpenLogItem getOpenLogItem(final Database currDb) {
		BaseOpenLogItem ol = new BaseOpenLogItem();
		ol.setCurrentDatabase(currDb);
		return ol;
	}

	/**
	 * Gets the OpenLogItem for more complex logging, passing a XotsContext (used for XPages / OSGi tasks extending
	 * {@linkplain AbstractXotsRunnable} or {@linkplain AbstractXotsCallable})
	 * 
	 * @param xotsContext
	 *            XotsContext from which to get Log database and current database path
	 */
	public static BaseOpenLogItem getOpenLogItem(final XotsContext xotsContext) {
		BaseOpenLogItem ol = new BaseOpenLogItem();

		if (StringUtil.isNotEmpty(xotsContext.getContextApiPath())) {
			try {
				Session currSess = Factory.getSession(SessionType.NATIVE);
				ol.setCurrentDatabase(currSess.getDatabase(xotsContext.getContextApiPath()));
			} catch (Exception e) {
				// No current database??
			}
		}

		if (StringUtil.isNotEmpty(xotsContext.getOpenLogApiPath())) {
			ol.setLogDbName(xotsContext.getOpenLogApiPath());
		}

		if (StringUtil.isNotEmpty(xotsContext.getTaskletClass())) {
			ol.setThisAgent(xotsContext.getTaskletClass());
		}
		return ol;
	}
}
