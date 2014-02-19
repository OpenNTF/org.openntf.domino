package org.openntf.domino.transactions;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Outline;
import org.openntf.domino.types.DatabaseDescendant;

public class DatabaseTransaction {
	private static final Logger log_ = Logger.getLogger(DatabaseTransaction.class.getName());

	//	private final Database database_;
	private final Set<Database> databases_ = new HashSet<Database>();
	private boolean isCommitting_ = false;
	private Queue<DatabaseDescendant> updateQueue_;
	private Queue<DatabaseDescendant> removeQueue_;

	public DatabaseTransaction(final org.openntf.domino.Database database) {
		databases_.add(database);
		//		database_ = database;
	}

	private boolean isDocLock(final DatabaseDescendant desc) {
		return desc.getAncestorDatabase().isDocumentLockingEnabled();
	}

	//	private boolean isDocLock() {
	//		return database_.isDocumentLockingEnabled();
	//	}

	private boolean isDesignLock(final DatabaseDescendant desc) {
		return desc.getAncestorDatabase().isDesignLockingEnabled();
		//		return database_.isDesignLockingEnabled();
	}

	//	private boolean isDesignLock() {
	//		return database_.isDesignLockingEnabled();
	//	}

	public int getUpdateSize() {
		return getUpdateQueue().size();
	}

	public int getRemoveSize() {
		return getRemoveQueue().size();
	}

	protected Queue<DatabaseDescendant> getUpdateQueue() {
		if (updateQueue_ == null) {
			updateQueue_ = new ArrayDeque<DatabaseDescendant>(); // TODO NTF - Switch to ArrayBlockingQueue and manage total
																	// handles?
		}
		return updateQueue_;
	}

	protected Queue<DatabaseDescendant> getRemoveQueue() {
		if (removeQueue_ == null) {
			removeQueue_ = new ArrayDeque<DatabaseDescendant>(); // TODO NTF - Switch to ArrayBlockingQueue and manage total
																	// handles?
		}
		return removeQueue_;
	}

	public void queueUpdate(final DatabaseDescendant base) {
		databases_.add(base.getAncestorDatabase());
		Queue<DatabaseDescendant> q = getUpdateQueue();
		//		synchronized (q) {
		q.add(base);
		//		}
		if (isDocLock(base) && base instanceof Document) {
			((Document) base).lock();
		}
		if (isDesignLock(base)) {
			if (base instanceof Agent) {
				((Agent) base).lock();
			} else if (base instanceof Outline) {
				// TODO - what? Outline doesn't have a .lock() method
			} else {
				// TODO - NTF build View/Form change transaction cache. Not fun. :-(
			}
		}
	}

	public void queueRemove(final DatabaseDescendant base) {
		databases_.add(base.getAncestorDatabase());
		Queue<DatabaseDescendant> q = getRemoveQueue();
		//		synchronized (q) {
		q.add(base);
		//		}
		if (isDocLock(base) && base instanceof Document) {
			((Document) base).lock();
		}
		if (isDesignLock(base)) {
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
		// System.out.println("Committing transaction with update size " + getUpdateQueue().size());
		isCommitting_ = true;
		Queue<DatabaseDescendant> uq = getUpdateQueue();
		//		synchronized (uq) {
		DatabaseDescendant next = uq.poll();
		while (next != null) {
			if (next instanceof Document) {
				boolean result = ((Document) next).save();
				if (!result) {
					// System.out.println("Transaction document save failed.");
					// TODO NTF - take some action to indicate that the save failed, potentially cancelling the transaction
				} else {
					if (isDocLock(next))
						((Document) next).unlock();
				}
			}
			// TODO NTF - Implement other database objects
			next = uq.poll();
		}
		//		}
		Queue<DatabaseDescendant> rq = getRemoveQueue();
		//		synchronized (rq) {
		/*DatabaseDescendant*/next = rq.poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.Document) {
				org.openntf.domino.Document doc = (org.openntf.domino.Document) next;
				if (isDocLock(doc))
					doc.unlock();
				doc.forceDelegateRemove();
			}
			// TODO NTF - Implement other database objects
			next = rq.poll();
		}
		//		}
		for (Database db : databases_) {
			db.closeTransaction();
		}
		//		database_.closeTransaction();
	}

	public void rollback() {
		// TODO - NTF release locks
		Queue<DatabaseDescendant> uq = getUpdateQueue();
		//		synchronized (uq) {
		DatabaseDescendant next = uq.poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.Document) {
				org.openntf.domino.Document doc = (org.openntf.domino.Document) next;
				doc.rollback();
				if (isDocLock(doc)) {
					doc.unlock();
				}
			}
			// TODO NTF - Implement other database objects
			next = uq.poll();
		}
		//		}
		Queue<DatabaseDescendant> rq = getRemoveQueue();
		//		synchronized (rq) {
		/*DatabaseDescendant*/next = rq.poll();
		while (next != null) {
			if (next instanceof org.openntf.domino.Document) {
				org.openntf.domino.Document doc = (org.openntf.domino.Document) next;
				doc.rollback();
				if (isDocLock(doc))
					doc.unlock();
			}
			// TODO NTF - Implement other database objects
			next = rq.poll();
		}
		//		}
		for (Database db : databases_) {
			db.closeTransaction();
		}
	}

	private String getDbList() {
		StringBuilder sb = new StringBuilder();
		for (Database db : databases_) {
			sb.append(db.getApiPath());
			sb.append(',');
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DatabaseTransaction [databases=" + getDbList() + ", updateQueue_=" + (updateQueue_ == null ? "0" : updateQueue_.size())
				+ ", removeQueue_=" + (removeQueue_ == null ? "0" : removeQueue_.size()) + "]";
	}

}
