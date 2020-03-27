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

import javax.servlet.ServletException;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.xsp.xots.FakeHttpRequest;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.xsp.module.nsf.NotesContext;

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
public class XPageCurrentSessionFactory extends AbstractXPageSessionFactory {

	private static final long serialVersionUID = 1L;
	private String runAs_;

	public XPageCurrentSessionFactory() {
		super();
		final lotus.domino.Session rawSession = NotesContext.getCurrent().getCurrentSession();
		try {
			runAs_ = rawSession.getEffectiveUserName();
			lotus.domino.Database rawDb = rawSession.getCurrentDatabase();
			if (rawDb != null) {
				if (StringUtil.isEmpty(rawDb.getServer())) {
					currentApiPath_ = rawDb.getFilePath();
				} else {
					currentApiPath_ = rawDb.getServer() + "!!" + rawDb.getFilePath();
				}
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}

	}

	/**
	 * returns the current XPage-Session or creates one with the same userName & context database
	 * 
	 * @throws
	 */
	@Override
	public Session createSession() {
		if (runAs_ == null) {
			throw new NullPointerException("No username set");
		}
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		try {
			if (ctx != null) {
				lotus.domino.Session rawSession = null;
				try {
					rawSession = ctx.getCurrentSession();
				} catch (IllegalStateException ex) {
					// This means we are running in XOTS and the Context is not initialized 
					ctx.initRequest(new FakeHttpRequest(runAs_));
					rawSession = ctx.getCurrentSession();
				}
				if (!runAs_.equals(rawSession.getEffectiveUserName())) {
					throw new IllegalStateException("The effective username (" + rawSession.getEffectiveUserName() + ") does not match "
							+ runAs_ + ". It seems that the XPageCurrentSessionFactory is passed across user-sessions");
				} else {
					return wrapSession(ctx.getCurrentSession(), false);
				}
			} else {
				// no context open
				final long userHandle = createUserNameList(runAs_);
				return wrapSession(XSPNative.createXPageSession(runAs_, userHandle, false, true), true);

			}

		} catch (ServletException e) { // ctx.initRequest
			DominoUtils.handleException(e);
		} catch (NotesException e) { // common notes exception
			DominoUtils.handleException(e);
		} catch (NException e) { // napi
			DominoUtils.handleException(e);
		}
		return null;
	}

}
