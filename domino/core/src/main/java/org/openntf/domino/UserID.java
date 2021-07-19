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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;

/**
 * @author Paul Withers
 * @since ODA 3.2.0
 * @since Domino 9.0.1 FP8
 *
 */
public interface UserID extends lotus.domino.UserID, org.openntf.domino.ext.UserID, Base<lotus.domino.UserID> {

	public static class Schema extends FactorySchema<UserID, lotus.domino.UserID, Session> {

		@Override
		public Class<UserID> typeClass() {
			return UserID.class;
		}

		@Override
		public Class<lotus.domino.UserID> delegateClass() {
			return lotus.domino.UserID.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	}

	public static final Schema SCHEMA = new Schema();

	/**
	 * Gets the encryption keys of a given UserID.
	 */
	@Override
	Vector<String> getEncryptionKeys();

	/**
	 * Gets the user name of a given user ID.
	 */
	@Override
	String getUserName();
}