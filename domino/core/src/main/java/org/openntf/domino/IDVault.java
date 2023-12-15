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
package org.openntf.domino;

import lotus.domino.UserID;

import org.openntf.domino.impl.Session;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * @author Paul Withers
 * @since Domino 9.0.1 FP8
 * @since ODA 3.2.0
 *
 */
public interface IDVault extends lotus.domino.IDVault, org.openntf.domino.ext.IDVault, Base<lotus.domino.IDVault>, SessionDescendant {

	public static class Schema extends FactorySchema<IDVault, lotus.domino.IDVault, Session> {

		@Override
		public Class<IDVault> typeClass() {
			return IDVault.class;
		}

		@Override
		public Class<lotus.domino.IDVault> delegateClass() {
			return lotus.domino.IDVault.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	}

	public static final Schema SCHEMA = new Schema();

	@Override
	public String getServerName();

	@Override
	public UserID getUserID(String arg0, String arg1);

	@Override
	public UserID getUserID(String arg0, String arg1, String arg2);

	@Override
	public void getUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public void getUserIDFile(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean isIDInVault(String arg0);

	@Override
	public boolean isIDInVault(String arg0, String arg1);

	@Override
	public void putUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public void putUserIDFile(String arg0, String arg1, String arg2, String arg3);

	@Override
	public void resetUserPassword(String arg0, String arg1);

	@Override
	public void resetUserPassword(String arg0, String arg1, String arg2, int arg3);

	@Override
	public boolean syncUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public boolean syncUserIDFile(String arg0, String arg1, String arg2, String arg3);

}
