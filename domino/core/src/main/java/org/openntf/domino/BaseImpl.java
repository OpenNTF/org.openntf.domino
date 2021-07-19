/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.utils.DominoUtils;

/**
 * Common class for ODA extension where no lotus wrapper exists (WrapperFactory, DocumentList)
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class BaseImpl<D extends lotus.domino.Base> implements Base<D> {

	private List<IDominoListener> listeners_;
	private transient Map<EnumEvent, List<IDominoListener>> listenerCache_;

	@Override
	public final boolean hasListeners() {
		return listeners_ != null && !listeners_.isEmpty();
	}

	@Override
	public final List<IDominoListener> getListeners() {
		if (listeners_ == null) {
			listeners_ = new ArrayList<IDominoListener>();
		}
		return listeners_;
	}

	@Override
	public final void addListener(final IDominoListener listener) {
		listenerCache_ = null;
		getListeners().add(listener);
	}

	@Override
	public final void removeListener(final IDominoListener listener) {
		listenerCache_ = null;
		getListeners().remove(listener);
	}

	@Override
	public final List<IDominoListener> getListeners(final EnumEvent event) {
		if (!hasListeners())
			return Collections.emptyList();

		if (listenerCache_ == null) {
			listenerCache_ = new FastMap<EnumEvent, List<IDominoListener>>();
		}

		return listenerCache_.computeIfAbsent(event, e -> {
			List<IDominoListener> result = new ArrayList<>();
			for (IDominoListener listener : getListeners()) {
				for (EnumEvent curEvent : listener.getEventTypes()) {
					if (curEvent.equals(event)) {
						result.add(listener);
						break;
					}
				}
			}
			return result;
		});
	}

	@Override
	public final boolean fireListener(final IDominoEvent event) {
		boolean result = true;
		if (!hasListeners())
			return true;
		List<IDominoListener> listeners = getListeners(event.getEvent());
		if (listeners == null || listeners.isEmpty())
			return true;
		for (IDominoListener listener : listeners) {
			try {
				if (!listener.eventHappened(event)) {
					result = false;
					break;
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		}
		return result;
	}

}
