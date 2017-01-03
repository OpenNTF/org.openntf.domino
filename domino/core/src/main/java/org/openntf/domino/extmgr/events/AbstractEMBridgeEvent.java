package org.openntf.domino.extmgr.events;

import java.util.EnumMap;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class AbstractEMBridgeEvent implements IEMBridgeEvent {

	private EnumMap<EMBridgeEventParams, Object> eventValuesMap_ = new EnumMap<EMBridgeEventParams, Object>(EMBridgeEventParams.class);

	private int eventId;

	public AbstractEMBridgeEvent(final int eventId) {
		this.eventId = eventId;
	}

	@Override
	public void setEventValue(final EMBridgeEventParams param, final Object value) {
		eventValuesMap_.put(param, value);
	}

	protected EnumMap<EMBridgeEventParams, Object> getEventValuesMap() {
		return eventValuesMap_;
	}

	@Override
	public final int getEventId() {
		return eventId;
	}

	public final String getDestDbPath() {
		return (String) eventValuesMap_.get(EMBridgeEventParams.DestDbpath);
	}

	public final String getUsername() {
		return (String) eventValuesMap_.get(EMBridgeEventParams.Username);
	}

	public final boolean parseEventBuffer(String eventBuffer) {
		String[] values = null;
		if (eventBuffer == null || eventBuffer.length() == 0) {
			values = new String[0];
		} else {
			if (eventBuffer.endsWith(",")) {
				eventBuffer += " ";
			}
			values = eventBuffer.split(",");
		}
		return false;
	}

	protected int parseInt(final String value) {
		return Integer.parseInt(value, 16);
	}

	protected long parseLong(final String value) {
		return Long.parseLong(value, 16);
	}

	//	protected abstract boolean parseEventBuffer(String[] values) throws InvalidEventException;
}
