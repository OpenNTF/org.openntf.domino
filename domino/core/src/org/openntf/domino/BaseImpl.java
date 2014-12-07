package org.openntf.domino;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.utils.DominoUtils;

/**
 * Common class for ODA extension where no lotus wrapper exists (WrapperFactory, DocumentList)
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 * @param <D>
 */
public abstract class BaseImpl<D extends lotus.domino.Base> implements Base<D> {

	private List<IDominoListener> listeners_;

	@Override
	public List<IDominoListener> getListeners() {
		if (listeners_ == null) {
			listeners_ = new ArrayList<IDominoListener>();
		}
		return listeners_;
	}

	@Override
	public void addListener(final IDominoListener listener) {
		getListeners().add(listener);
	}

	@Override
	public void removeListener(final IDominoListener listener) {
		getListeners().remove(listener);
	}

	@Override
	public List<IDominoListener> getListeners(final EnumEvent event) {
		List<IDominoListener> result = new ArrayList<IDominoListener>();
		for (IDominoListener listener : getListeners()) {
			for (EnumEvent curEvent : listener.getEventTypes()) {
				if (curEvent.equals(event)) {
					result.add(listener);
					break;
				}
			}
		}
		return result;
	}

	@Override
	public boolean fireListener(final IDominoEvent event) {
		boolean result = true;
		for (IDominoListener listener : getListeners(event.getEvent())) {
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
