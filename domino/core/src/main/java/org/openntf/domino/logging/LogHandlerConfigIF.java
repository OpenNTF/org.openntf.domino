package org.openntf.domino.logging;

/**
 * Interface for objects holding specific handler properties. Used by the {@link LogHandlerUpdateIF} interface.
 */
public interface LogHandlerConfigIF {

	public boolean isEqual(LogHandlerConfigIF other);
}
