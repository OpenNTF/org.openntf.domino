/**
 * 
 */
package org.openntf.domino.ext;

import java.util.List;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;

/**
 * @author withersp
 * 
 */
public interface Base {

	public void addListener(IDominoListener listener);

	public void removeListener(IDominoListener listener);

	public List<IDominoListener> getListeners();

	public List<IDominoListener> getListeners(EnumEvent event);

	public boolean fireListener(IDominoEvent event);

}
