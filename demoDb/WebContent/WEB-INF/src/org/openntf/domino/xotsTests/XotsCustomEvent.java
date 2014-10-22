package org.openntf.domino.xotsTests;

import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.xots.XotsAbstractTriggeredTasklet;
import org.openntf.domino.xots.annotations.Persistent;
import org.openntf.domino.xots.annotations.Trigger;

@Trigger("testEvent")
@Persistent
public class XotsCustomEvent extends XotsAbstractTriggeredTasklet {

	private static final long serialVersionUID = 1L;

	public XotsCustomEvent() {

	}

	public void handleEvent(final IDominoEvent event) {
		System.out.println(">>> " + getClass().getName() + " >>> hey, I was told about an event! " + event);

		System.out.println("session is " + getSession());
	}

}
