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
package org.openntf.domino.xsp.session;

import javax.servlet.http.HttpServletRequest;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.osgi.core.context.ContextInfo;

/**
 * The XPageCurrentSessionFactory returns (as the name says) the current XPage Session if available.
 * 
 * 
 * If the ´Factory is passed across threads, it tries to create an XPage-Session with the same username that was available at construction
 * time. (This implies, that there is a valid XPage session available at construction time)
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DasCurrentSessionFactory extends AbstractXPageSessionFactory {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request_;

	public DasCurrentSessionFactory(final HttpServletRequest request) {
		super();
		request_ = request;
	}

	/**
	 * returns the current Das-Session
	 * 
	 * @throws
	 */
	@Override
	public Session createSession() {
		if (request_ == null || request_.getUserPrincipal() == null) {
			lotus.domino.Session rawSession = ContextInfo.getUserSession();
			if (rawSession == null) {
				NativeSessionFactory nsf = new NativeSessionFactory(null);
				Session result = nsf.createSession();
				Factory.setCurrentToSession(result);
				return result;
			}
			try {
				lotus.domino.Database rawDb = rawSession.getCurrentDatabase();
				if (rawDb != null) {
					if (StringUtil.isEmpty(rawDb.getServer())) {
						currentApiPath_ = rawDb.getFilePath();
					} else {
						currentApiPath_ = rawDb.getServer() + "!!" + rawDb.getFilePath(); //$NON-NLS-1$
					}
				}
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			return wrapSession(rawSession, false);
		} else {
			String name = request_.getUserPrincipal().getName();
			Session session = createSession(name);
			Factory.setCurrentToSession(session);
			return session;
		}
	}

	public Session createSession(final String userName) {
		try {
			final long userHandle = createUserNameList(userName);
			lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, false);
			lotus.domino.Database rawDb = rawSession.getCurrentDatabase();
			if (rawDb != null) {
				if (StringUtil.isEmpty(rawDb.getServer())) {
					currentApiPath_ = rawDb.getFilePath();
				} else {
					currentApiPath_ = rawDb.getServer() + "!!" + rawDb.getFilePath(); //$NON-NLS-1$
				}
			}
			return wrapSession(rawSession, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
