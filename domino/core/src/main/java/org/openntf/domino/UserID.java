package org.openntf.domino;

import org.openntf.domino.types.FactorySchema;

/**
 * @author Paul Withers
 * @since ODA 3.2.0
 * @since Domino 9.0.1 FP8
 *
 */
public interface UserID extends lotus.domino.UserID, org.openntf.domino.ext.UserID, Base<lotus.domino.UserID> {

	public static class Schema extends FactorySchema<UserID, lotus.domino.UserID, IDVault> {

		@Override
		public Class<UserID> typeClass() {
			return UserID.class;
		}

		@Override
		public Class<lotus.domino.UserID> delegateClass() {
			return lotus.domino.UserID.class;
		}

		@Override
		public Class<IDVault> parentClass() {
			return IDVault.class;
		}
	}

	public static final Schema SCHEMA = new Schema();

	/* (non-Javadoc)
	 * @see lotus.domino.UserID#getEncryptionKeys()
	 */
	@Override
	public abstract java.util.Vector getEncryptionKeys();

	@Override
	public abstract java.lang.String getUserName();
}