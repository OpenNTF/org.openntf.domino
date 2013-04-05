package org.openntf.domino.transactions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Outline;
import org.openntf.domino.types.DatabaseDescendant;

public class DatabaseTransaction {
	private static final Logger log_ = Logger.getLogger(DatabaseTransaction.class.getName());

	private final Database database_;
	private boolean isCommitting_ = false;
	private Queue<DatabaseDescendant> updateQueue_;
	private Queue<DatabaseDescendant> removeQueue_;

	public DatabaseTransaction(org.openntf.domino.Database database) {
		database_ = database;
	}

	private boolean isDocLock() {
		return database_.isDocumentLockingEnabled();
	}

	private boolean isDesignLock() {
		return database_.isDesignLockingEnabled();
	}

	protected Queue<DatabaseDescendant> getUpdateQueue() {
		if (updateQueue_ == null) {
			updateQueue_ = new LinkedList<DatabaseDescendant>(); // TODO NTF - Switch to ArrayBlockingQueue and manage total handles?
		}
		return updateQueue_;
	}

	protected Queue<DatabaseDescendant> getRemoveQueue() {
		if (removeQueue_ == null) {
			removeQueue_ = new LinkedList<DatabaseDescendant>(); // TODO NTF - Switch to ArrayBlockingQueue and manage total handles?
		}
		return removeQueue_;
	}

	public void queueUpdate(DatabaseDescendant base) {
		getUpdateQueue().add(base); // TODO - NTF get locks when stuff is queued
		if (isDocLock() && base instanceof Document) {
			((Document) base).lock();
		}
		if (isDesignLock()) {
			if (base instanceof Agent) {
				((Agent) base).lock();
			} else if (base instanceof Outline) {
				// TODO - what? Outline doesn't have a .lock() method
			} else {
				// TODO - NTF build View/Form change transaction cache. Not fun. :-(
			}
		}
	}

	public void queueRemove(DatabaseDescendant base) {
		getRemoveQueue().add(base); // TODO - NTF get locks when stuff is queued
		if (isDocLock() && base instanceof Document) {
			((Document) base).lock();
		}
		if (isDesignLock()) {
			if (base instanceof Agent) {
				((Agent) base).lock();
			} else if (base instanceof Outline) {
				// TODO - what? Outline doesn't have a .lock() method
			} else {
				// TODO - NTF build View/Form change transaction cache. Not fun. :-(
			}
		}
	}

	// public boolean isCommitting() {
	// return isCommitting_;
	// }

	public void commit() {
		isCommitting_ = true;
		DatabaseDescendant next = getUpdateQueue().poll();
		while (next != null) {
			if (next instanceof Document) {
				boolean result = ((Document) next).save();
				if (!result) {
					// TODO NTF - take some action to indicate that the save failed, potentially cancelling the transaction
				} else {
					if (isDocLock())
						((Document) next).unlock();
				}
			}
			// TODO NTF - Implement other database objects
			next = getUpdateQueue().poll();
		}
		next = getRemoveQueue().poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.impl.Document) {
				org.openntf.domino.impl.Document doc = (org.openntf.domino.impl.Document) next;
				if (isDocLock())
					doc.unlock();
				doc.forceDelegateRemove();
			}
			// TODO NTF - Implement other database objects
			next = getUpdateQueue().poll();
		}

		database_.closeTransaction();
	}

	public void rollback() {
		// TODO - NTF release locks
		DatabaseDescendant next = getUpdateQueue().poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.impl.Document) {
				org.openntf.domino.impl.Document doc = (org.openntf.domino.impl.Document) next;
				doc.rollback();
				if (isDocLock()) {
					doc.unlock();
				}
			}
			// TODO NTF - Implement other database objects
			next = getUpdateQueue().poll();
		}
		next = getRemoveQueue().poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.impl.Document) {
				org.openntf.domino.impl.Document doc = (org.openntf.domino.impl.Document) next;
				doc.rollback();
				if (isDocLock())
					doc.unlock();
			}
			// TODO NTF - Implement other database objects
			next = getUpdateQueue().poll();
		}
		database_.closeTransaction();
	}

}
