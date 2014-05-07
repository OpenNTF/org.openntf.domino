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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * @author praml
 * 
 *         DominoReference tracks the lifetime of reference object and recycles delegate if reference object is GCed
 * 
 */
public class DominoReference extends WeakReference<Object> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReference.class.getName());

	/** The delegate_. This is the wrapped Object */
	private lotus.domino.Base delegate_;

	/** This is the CPP-ID or an other unique hash value **/
	private long key_;

	private transient int hashcode_;

	private boolean noRecycle = false;

	/**
	 * Instantiates a new domino reference.
	 * 
	 * @param reference
	 *            the wrapper to track
	 * @param q
	 *            the q
	 * @param delegate
	 *            the delegate
	 */
	public DominoReference(final long key, final Object reference, final lotus.domino.Base delegate, final ReferenceQueue<Object> q) {
		super(reference, q);

		// Because the reference separately contains a pointer to the delegate object, it's still available even
		// though the wrapper is null
		this.delegate_ = delegate;
		this.key_ = key;
	}

	//void clearLotusReference() {
	//	delegate_ = null;
	//}

	/**
	 * Recycle.
	 */
	void recycle() {
		if (delegate_ != null) {
			try {
				if (org.openntf.domino.impl.Base.isDead(delegate_)) {
					// the object is dead, so let's see who recycled this
					if (org.openntf.domino.impl.Base.isInvalid(delegate_)) {
						// if it is also invalid, someone called recycle on the delegate object.
						// this is already counted as manual recycle, so do not count twice
					} else {
						// otherwise, it's parent is recycled.
						// TODO this should get counted on a own counter
						Factory.countManualRecycle(delegate_.getClass());
					}

				} else {
					// recycle the delegate, because no hard ref points to us.
					if (!noRecycle) {
						delegate_.recycle();
					}
					int total = Factory.countAutoRecycle(delegate_.getClass());

					if (log_.isLoggable(Level.FINE)) {
						if (total % 5000 == 0) {
							log_.log(Level.FINE, "Auto-recycled " + total + " references");
						}
					}

				}
			} catch (NotesException e) {
				Factory.countRecycleError(delegate_.getClass());
				DominoUtils.handleException(e);
			}

			if (delegate_ instanceof lotus.domino.local.EmbeddedObject) {
				// if the wrapper is out of scope and the element was an EmbeddedObject, we call markInvalid 
				// It does not matter if the object was already recycled/dead. MarkInvalid will internally just
				// call "cleanupTempFile" (cannot throw an exception) 
				((lotus.domino.local.EmbeddedObject) delegate_).markInvalid();
			}
		}
	}

	/**
	 * Prevents that the delegate object will be really recycled. Autorecycle counting is done. - So you must ensure that you recycle the
	 * document yourself or wrap it again
	 * 
	 * @param what
	 */
	public void setNoRecycle(final boolean what) {
		noRecycle = what;
	}

	/**
	 * A WeakValue is equal to another WeakValue iff they both refer to objects that are, in turn, equal according to their own equals
	 * methods. Key is not checked here, because there might be "dummy" values without a key so that "contains" works
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof DominoReference))
			return false;

		Object ref1 = this.get();
		Object ref2 = ((DominoReference) obj).get();

		if (ref1 == ref2)
			return true;

		if ((ref1 == null) || (ref2 == null))
			return false;

		return ref1.equals(ref2);
	}

	/**
     *
     */
	@Override
	public int hashCode() {
		if (hashcode_ == 0) {
			Object ref = this.get();
			hashcode_ = (ref == null) ? 0 : ref.hashCode();
		}
		return hashcode_;
	}

	long getKey() {
		return key_;
	}

	boolean isDead() {
		return org.openntf.domino.impl.Base.isDead(delegate_);
	}

}
