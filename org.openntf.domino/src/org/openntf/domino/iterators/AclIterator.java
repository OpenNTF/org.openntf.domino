package org.openntf.domino.iterators;

import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.ACL;
import org.openntf.domino.ACLEntry;
import org.openntf.domino.utils.DominoUtils;

public class AclIterator implements Iterator<ACLEntry> {
	private static final Logger log_ = Logger.getLogger(AclIterator.class.getName());
	private ACL acl;
	private transient ACLEntry currentEntry_;
	private boolean started_;
	private boolean done_;

	protected AclIterator(ACL acl) {
		setAcl(acl);
	}

	@Override
	public boolean hasNext() {
		boolean result = false;
		ACLEntry currentEntry = getCurrentEntry();
		ACLEntry nextEntry = null;
		try {
			nextEntry = ((currentEntry == null) ? (isDone() ? null : getAcl().getFirstEntry()) : getAcl().getNextEntry(currentEntry));
			result = (nextEntry != null);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(nextEntry);
		}
		return result;
	}

	@Override
	public ACLEntry next() {
		ACLEntry result = null;
		ACLEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? getAcl().getFirstEntry() : getAcl().getNextEntry(currentEntry));
			if (result == null) {
				setDone(true);
			} else {
				setStarted(true);
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(currentEntry);
			setCurrentEntry(result);
		}
		return result;
	}

	@Override
	public void remove() {
		// NOOP
	}

	public ACLEntry getCurrentEntry() {
		return currentEntry_;
	}

	public void setCurrentEntry(ACLEntry currentEntry) {
		currentEntry_ = currentEntry;
	}

	public ACL getAcl() {
		return acl;
	}

	public void setAcl(ACL acl) {
		this.acl = acl;
	}

	public boolean isDone() {
		return done_;
	}

	public void setDone(boolean done) {
		done_ = done;
	}

	public boolean isStarted() {
		return started_;
	}

	public void setStarted(boolean started) {
		started_ = started;
	}
}
