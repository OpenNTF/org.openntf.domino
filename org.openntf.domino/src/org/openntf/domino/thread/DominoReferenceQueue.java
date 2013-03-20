/*
 * Copyright OpenNTF 2013
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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Base;
import org.openntf.domino.exceptions.UnimplementedException;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReferenceQueue.
 */
public class DominoReferenceQueue extends ReferenceQueue<Base> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReferenceQueue.class.getName());

	// /** The lotus reference counter_. */
	// private DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	/**
	 * The reference bag. NTF This is just a junk storehouse for our DominoReference objects BEFORE they get enqueued. if we didn't keep
	 * this, there would be nothing with a pointer to the reference, and the reference itself would be GC'ed
	 * 
	 * */
	private Set<DominoReference> referenceBag = new HashSet<DominoReference>();

	@Override
	public Reference<? extends Base> poll() {
		DominoReference result = (DominoReference) super.poll();
		if (result != null) {
			referenceBag.remove(result);
			int count = result.getSession().subtractId(result.getDelegateId());
			if (count < 1) {
				result.recycle();
			} else {
				if (log_.isLoggable(Level.FINER))
					log_.log(Level.FINER, "Not recycling a " + result._getType().getSimpleName() + " (" + result.getDelegateId()
							+ ") because it still has " + count + " wrappers");
			}
		}
		return result;
	}

	@Override
	public Reference<? extends Base> remove() throws InterruptedException {
		throw new UnimplementedException("DominoReferenceQueue only supports poll()");
	}

	@Override
	public Reference<? extends Base> remove(long wait) throws InterruptedException {
		throw new UnimplementedException("DominoReferenceQueue only supports poll()");
	}

	private int bagginses = 0;

	public void bagReference(DominoReference ref) {
		bagginses++;
		if (bagginses % 5000 == 0) {
			log_.log(Level.FINE, "Bagged 5000 more. Forcing GC...");
			System.gc();
		}
		int count = ref.getSession().addId(ref.getDelegateId());
		referenceBag.add(ref);
	}

	/**
	 * Instantiates a new domino reference queue.
	 */
	public DominoReferenceQueue() {
		// this should only happen once per-thread
	}

}
