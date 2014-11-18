package org.openntf.domino.xots;

import org.openntf.domino.events.IDominoEvent;

@Deprecated
public interface XotsITriggeredTasklet {
	public void handleEvent(IDominoEvent event);
}
