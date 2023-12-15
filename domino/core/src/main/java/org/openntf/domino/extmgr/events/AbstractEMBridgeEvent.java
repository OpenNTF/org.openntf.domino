/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		@SuppressWarnings("unused")
		String[] values = null;
		if (eventBuffer == null || eventBuffer.length() == 0) {
			values = new String[0];
		} else {
			if (eventBuffer.endsWith(",")) { //$NON-NLS-1$
				eventBuffer += " "; //$NON-NLS-1$
			}
			values = eventBuffer.split(","); //$NON-NLS-1$
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
