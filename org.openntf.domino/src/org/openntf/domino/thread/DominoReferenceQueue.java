/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.thread;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Base;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.thread.deprecated.DominoChildThread;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReferenceQueue.
 */
public class DominoReferenceQueue extends ReferenceQueue<Base> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReferenceQueue.class.getName());

	/** The child thread_. */
	private final boolean childThread_;

	/** The lotus reference counter_. */
	private DominoReferenceCounter localLotusReferenceCounter_ = new DominoReferenceCounter(false);

	/** The originator set. */
	private Set<DominoReference> originatorSet = new HashSet<DominoReference>();

	/**
	 * The reference bag. NTF This is just a junk storehouse for our DominoReference objects BEFORE they get enqueued. if we didn't keep
	 * this, there would be nothing with a pointer to the reference, and the reference itself would be GC'ed
	 * 
	 * */
	// private final Set<DominoReference> referenceBag = new HashSet<DominoReference>();
	private final List<DominoReference> referenceBag = new ArrayList<DominoReference>();

	/**
	 * Finalize queue.
	 * 
	 * @return the int
	 */
	public int finalizeQueue() {
		int result = 0;
		for (DominoReference ref : originatorSet) {
			ref.recycle();
			result++;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.ref.ReferenceQueue#poll()
	 */
	@Override
	public Reference<? extends Base> poll() {
		return poll(0l);
	}

	public Reference<? extends Base> poll(final long cppid) {
		DominoReference result = (DominoReference) super.poll();

		if (result != null) {
			long did = result.getDelegateId();
			referenceBag.remove(result);
			int count = -1;
			boolean shouldRecycle = false;
			if (childThread_) {
				count = result.getSession().subtractId(did);
				if (count == 0) { // if we're the originating thread, and we're also the last to use it. See ya!
					if (originatorSet.contains(result)) {
						originatorSet.remove(result);
						shouldRecycle = true;
					}
				}
			} else {
				count = localLotusReferenceCounter_.decrement(did);
				if (count < 1) {
					if (did == cppid) {
						// System.out.println("ALERT!!! Attemping to auto-recycle the same handle we're currently wrapping. Don't! " +
						// cppid);
					} else {
						shouldRecycle = true;
					}
				}
			}
			if (shouldRecycle) {
				result.recycle();
			} else {
				if (log_.isLoggable(Level.FINER))
					log_.log(Level.FINER, "Not recycling a " + result._getType().getSimpleName() + " (" + result.getDelegateId()
							+ ") because it still has " + count + " wrappers");
			}

		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.ref.ReferenceQueue#remove()
	 */
	@Override
	public Reference<? extends Base> remove() throws InterruptedException {
		throw new UnimplementedException("DominoReferenceQueue only supports poll()");
	}

	/* (non-Javadoc)
	 * @see java.lang.ref.ReferenceQueue#remove(long)
	 */
	@Override
	public Reference<? extends Base> remove(final long wait) throws InterruptedException {
		throw new UnimplementedException("DominoReferenceQueue only supports poll()");
	}

	/** The bagginses. */
	private int bagginses = 0;

	/**
	 * Bag reference.
	 * 
	 * @param ref
	 *            the ref
	 */
	public void bagReference(final DominoReference ref) {
		bagginses++;
		// if (bagginses % 5000 == 0) {
		// log_.log(Level.FINE, "Bagged 5000 more. Forcing GC...");
		// System.gc();
		// }
		if (childThread_) {
			int count = ref.getSession().addId(ref.getDelegateId());
			if (count == 1) { // we're the thread that's accessing the object for the first time
				originatorSet.add(ref);
			}
		} else {
			int count = localLotusReferenceCounter_.increment(ref.getDelegateId());
		}
		// int localCount = ref.getSession().addId(ref.getDelegateId());
		referenceBag.add(ref);
	}

	/**
	 * Instantiates a new domino reference queue.
	 */
	public DominoReferenceQueue() {
		childThread_ = DominoChildThread.class.isAssignableFrom(Thread.currentThread().getClass());
	}

}
