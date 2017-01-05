package org.openntf.domino.xots.events;

import org.openntf.domino.Base;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;

public class CustomNamedEvent implements IDominoEvent {

	private final String name_;
	private final Object payload_;

	// TODO Move name to an EnumEvent class
	public CustomNamedEvent(final String name, final Object payload) {
		payload_ = payload;
		name_ = name;
	}

	public String getName() {
		return name_;
	}

	@Override
	public EnumEvent getEvent() {
		return null;
	}

	@Override
	public Base<?> getSource() {
		return null;
	}

	@Override
	public Base<?> getTarget() {
		return null;
	}

	@Override
	public Object getPayload() {
		return payload_;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": name=" + name_ + ", payload=" + payload_ + "]";
	}
}
