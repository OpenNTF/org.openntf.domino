/**
 * 
 */
package org.openntf.domino.events;

/**
 * @author nfreeman
 * 
 */
public interface IDominoEventFactory {
	public IDominoEvent wrap(IDominoEvent event);

	public IDominoEvent generate(EnumEvent event, org.openntf.domino.Base source, org.openntf.domino.Base target, Object payload);

	public void initialize();

	public void terminate();
}
