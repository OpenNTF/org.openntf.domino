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