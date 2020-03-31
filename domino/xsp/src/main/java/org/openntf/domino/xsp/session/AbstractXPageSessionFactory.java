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
package org.openntf.domino.xsp.session;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public abstract class AbstractXPageSessionFactory implements ISessionFactory {
	private static final long serialVersionUID = 1L;

	protected String currentApiPath_;

	protected Session wrapSession(final lotus.domino.Session raw, final boolean selfCreated) {
		Session sess = Factory.getWrapperFactory().fromLotus(raw, Session.SCHEMA, null);
		sess.setNoRecycle(!selfCreated);

		boolean allFix = true;
		AutoMime autoMime = AutoMime.WRAP_32K;
		boolean mimeFriendly = true;
		boolean viewExactMatch = false;
		if (NotesContext.getCurrentUnchecked() != null) {
			allFix = ODAPlatform.isAppAllFix(null);
			autoMime = ODAPlatform.getAppAutoMime(null);
			mimeFriendly = ODAPlatform.isAppMimeFriendly(null);
			viewExactMatch = ODAPlatform.isAppViewExactMatch(null);
		}

		if (allFix) {
			for (Fixes fix : Fixes.values()) {
				if (fix.isKhan()) {
					sess.setFixEnable(fix, true);
				}
			}
		}
		sess.setAutoMime(autoMime);
		sess.setViewExactMatch(viewExactMatch);

		if (mimeFriendly) {
			sess.setConvertMIME(false);
		}
		if (selfCreated && currentApiPath_ != null) {
			Database db = sess.getCurrentDatabase();
			if (db == null) {
				db = sess.getDatabase(currentApiPath_);
				setCurrentDatabase(sess, db);
			}
		}

		return sess;
	}

	/**
	 * Registers the userHandle for proper cleanup
	 * 
	 * @param userHandle
	 * @throws NException
	 */
	protected long createUserNameList(final String userName) throws NException {
		final long userHandle = NotesUtil.createUserNameList(userName);
		if (userHandle != 0) {

			Factory.addTerminateHook(new Runnable() {
				@Override
				public void run() {
					try {
						Os.OSMemFree(userHandle);
					} catch (NException e) {
					}
				}
			}, false);
		}
		return userHandle;
	}

	/**
	 * This method may be overwritten by special XPage databases
	 * 
	 * @param sess
	 * @param db
	 */
	protected void setCurrentDatabase(final Session sess, final Database db) {
		sess.setCurrentDatabase(db);
	}
}
