/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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

/**
 * DatabaseTransaction caches Document updates and removals until the {@link #commit()} method is called. At that point all updated
 * documents are saved and removed documents are removed from database. If a {@link #rollback()} is called, all changes are reverted - no
 * documents are removed and no updates are saved. Use this mechanism typically in a situation where you need to modify multiple related
 * documents and you want to either save all of the documents or none at all when some error condition occurs.
 * <p>
 * <h5>Example</h5> Consider the following example where a file is read line by line, each line representing an order. For each line a new
 * document is created and a customer document is updated. If there is no error the mechanism will save all changed and created documents or
 * discard all changes if an error occurs.
 * 
 * <pre>
 * private void processOrder(String line) {
 *  Order order = parseInput(line);
 *  if (order != null) {
 *    DatabaseTransaction transaction = database.startTransaction();
 *
 *    try {
 *      Document docCustomer = getCustomer(order.getCustomerID())
 *      Document docOrder = database.createDocument("Form", "Order", "CustomerID", order.getCustomerID());
 *
 *      //update both the docCustomer and docOrder
 *      .....
 *      //if no exception or error condition occurs, save all changed documents
 *      transaction.commit();
 *    } catch (Throwable t) {
 *       //error occurred, roll the changes back
 *       transaction.rollback();
 *
 *       //and log the error
 *    }
 *  }
 * }
 * </pre>
 * </p>
 */
@SuppressWarnings("nls")
public class DatabaseTransaction {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseTransaction.class.getName());

	//	private final Database database_;
	private final Set<Database> databases_ = new HashSet<Database>();
	@SuppressWarnings("unused")
	private boolean isCommitting_ = false;
	private Queue<DatabaseDescendant> updateQueue_;
	private Queue<DatabaseDescendant> removeQueue_;

	/**
	 * Constructor. Initializes this transaction for the specified database. To use this transaction also on another database, call
	 * {@link org.openntf.domino.Database#setTransaction(DatabaseTransaction)} on the other database.
	 *
	 * @param database
	 *            Database to monitor for document updates and removals.
	 */
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

	/**
	 * Returns a number of changed Documents currently in the cache.
	 */
	public int getUpdateSize() {
		return getUpdateQueue().size();
	}

	/**
	 * Returns a number of Documents that will be removed once a {@link #commit()} is called.
	 */
	public int getRemoveSize() {
		return getRemoveQueue().size();
	}

	protected Queue<DatabaseDescendant> getUpdateQueue() {
		if (updateQueue_ == null) {
			updateQueue_ = new ArrayDeque<DatabaseDescendant>();// TODO NTF - Switch to ArrayBlockingQueue and manage total
			// handles?
		}
		return updateQueue_;
	}

	protected Queue<DatabaseDescendant> getRemoveQueue() {
		if (removeQueue_ == null) {
			removeQueue_ = new ArrayDeque<DatabaseDescendant>();// TODO NTF - Switch to ArrayBlockingQueue and manage total
			// handles?
		}
		return removeQueue_;
	}

	/**
	 * Adds the specified element to the list of entities which should be saved during a commit. The queue is managed by the database.
	 *
	 * @param base
	 *            Element to add to the queue.
	 */
	public void queueUpdate(final DatabaseDescendant base) {
		databases_.add(base.getAncestorDatabase());
		Queue<DatabaseDescendant> q = getUpdateQueue();
		//		synchronized (q) {
		q.add(base);
		//		}
		if (isDocLock(base) && base instanceof Document) {
			if (!((Document) base).isNewNote()) {
				((Document) base).lock();
			}
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

	/**
	 * Adds the specified element to the list of entities which should be removed from the database during a commit. The queue is managed by
	 * the database.
	 *
	 * @param base
	 *            Element to add to the queue.
	 */
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

	/**
	 * Writes all cached updates to Documents - updated documents are saved and documents marked for removal will be removed. This
	 * transaction is then closed and a new one needs to be {@link org.openntf.domino.Database#startTransaction() started}.
	 */
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
					if (isDocLock(next)) {
						((Document) next).unlock();
					}
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
				if (isDocLock(doc)) {
					doc.unlock();
				}
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

	/**
	 * Discard all cached updates - updated documents are reverted back to their original state and documents marked for removal are kept in
	 * Database. This transaction is then closed and a new one needs to be {@link org.openntf.domino.Database#startTransaction() started}.
	 */
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
				if (isDocLock(doc)) {
					doc.unlock();
				}
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
