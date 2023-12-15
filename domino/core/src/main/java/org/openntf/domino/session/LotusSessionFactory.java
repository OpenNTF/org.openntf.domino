/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import lotus.domino.local.Session;

import org.openntf.domino.utils.DominoUtils;

/**
 * Utility class - not inteded to use outside this package
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
enum LotusSessionFactory {
	;
	private static Method getSessionMethod(final String name, final Class<?>... parameterTypes) {
		return AccessController.doPrivileged(new PrivilegedAction<Method>() {
			@Override
			public Method run() {
				try {
					Method m = lotus.domino.local.Session.class.getDeclaredMethod(name, parameterTypes);
					m.setAccessible(true);
					return m;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

	}

	private static Field F_webuser;
	static {
		try {
			F_webuser = lotus.domino.local.Session.class.getDeclaredField("webuser"); //$NON-NLS-1$
			F_webuser.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//----------------------- FindOrCreateSession 
	private static Method M_FindOrCreateSession = getSessionMethod("FindOrCreateSession", long.class, int.class); //$NON-NLS-1$

	private static lotus.domino.local.Session FindOrCreateSession(final long cpp_id, final int unknown) throws IllegalArgumentException,
	IllegalAccessException, InvocationTargetException {
		return (lotus.domino.local.Session) M_FindOrCreateSession.invoke(null, cpp_id, unknown);
	}

	// ----------------- createNativeSession --------------
	private static Method M_NCreateSession = getSessionMethod("NCreateSession", int.class); //$NON-NLS-1$

	private static long NCreateSession(final int unknown) throws IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		return (Long) M_NCreateSession.invoke(null, unknown);
	}

	static Session createSession() {
		try {

			long cpp = NCreateSession(0);  // don't know what parameter means
			return FindOrCreateSession(cpp, 0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	// ----------------- createTrustedSession --------------
	private static Method M_NCreateTrustedSession = getSessionMethod("NCreateTrustedSession", boolean.class); //$NON-NLS-1$

	private static long NCreateTrustedSession(final boolean unknown) throws IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		return (Long) M_NCreateTrustedSession.invoke(null, unknown);
	}

	static Session createTrustedSession() {
		try {
			long cpp = NCreateTrustedSession(false); // don't know what parameter means
			return FindOrCreateSession(cpp, 0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	// ----------------- createSessionWithFullAccess --------------
	private static Method M_NCreateSessionWithFullAccess = getSessionMethod("NCreateSessionWithFullAccess", String.class); //$NON-NLS-1$

	private static long NCreateSessionWithFullAccess(final String userName) throws IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		return (Long) M_NCreateSessionWithFullAccess.invoke(null, userName);
	}

	static Session createSessionWithFullAccess(final String userName) {
		try {
			long l = NCreateSessionWithFullAccess(userName);
			return FindOrCreateSession(l, 0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	// ----------------- createSessionWithTokenEx --------------
	private static Method M_NCreateSessionWithTokenEx = getSessionMethod("NCreateSessionWithTokenEx", String.class); //$NON-NLS-1$

	private static long NCreateSessionWithTokenEx(final String userName) throws IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		return (Long) M_NCreateSessionWithTokenEx.invoke(null, userName);
	}

	static Session createSessionWithTokenEx(final String paramString) {
		try {
			long l = NCreateSessionWithTokenEx(paramString);
			Session ret = FindOrCreateSession(l, 0);
			F_webuser.set(ret, true);
			return ret;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	// ----------------- createSessionWithTokenEx --------------
	private static Method M_NCreateSessionWithPasswd = getSessionMethod("NCreateSessionWithPasswd", String.class); //$NON-NLS-1$

	private static long NCreateSessionWithPasswd(final String password) throws IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		return (Long) M_NCreateSessionWithPasswd.invoke(null, password);
	}

	static Session createSessionWithPassword(final String paramString) {
		try {
			long l = NCreateSessionWithPasswd(paramString);
			Session ret = FindOrCreateSession(l, 0);
			//			F_webuser.set(ret, true);
			return ret;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
