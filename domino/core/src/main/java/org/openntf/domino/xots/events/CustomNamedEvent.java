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
package org.openntf.domino.xots.events;

import org.openntf.domino.Base;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;

@SuppressWarnings("nls")
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
