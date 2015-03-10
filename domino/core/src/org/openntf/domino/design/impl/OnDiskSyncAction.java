package org.openntf.domino.design.impl;

public enum OnDiskSyncAction {
	/** always export */
	FORCE_EXPROT,

	/** always import */
	FORCE_IMPORT,

	/** delete element in NSF */
	DELETE_NSF,

	/** delete element in NSF */
	DELETE_DISK,

	/** decide by timestamp what to do */
	SYNC;
}