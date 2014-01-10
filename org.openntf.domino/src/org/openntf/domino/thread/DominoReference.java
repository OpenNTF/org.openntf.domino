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

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Base;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReference. T is normally a Long, maybe we change it to a "long" or "Integer" because there may be faster map
 * implementations.
 * 
 * DominoReference should be used only in the DominoReferenceMap
 */
public class DominoReference<T, V extends Base> extends PhantomReference<V> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReference.class.getName());

	/** The delegate_. This is the wrapped Object */
	private final lotus.domino.Base delegate_;

	/** This is the CPP-ID or an other unique hash value **/
	private T key_;

	private transient int hashcode_;

	/**
	 * Instantiates a new domino reference.
	 * 
	 * @param r
	 *            the r
	 * @param q
	 *            the q
	 * @param delegate
	 *            the delegate
	 */
	public DominoReference(final V r, final ReferenceQueue<V> q, final T key) {
		super(r, q);

		// Because the reference separately contains a pointer to the delegate object, it's still available even
		// though the wrapper is null
		delegate_ = org.openntf.domino.impl.Base.getDelegate(r);
		key_ = key;
	}

	/**
	 * Recycle.
	 */
	void recycle() {
		int ctid = System.identityHashCode(Thread.currentThread());
		org.openntf.domino.impl.Base.s_recycle(delegate_);
		int total = Factory.countAutoRecycle();

		if (log_.isLoggable(Level.FINE)) {
			if (total % 5000 == 0) {
				log_.log(Level.FINE, "Auto-recycled " + total + " references");
			}
		}
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
		Object ref2 = ((DominoReference<?, ?>) obj).get();

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
			Base ref = this.get();

			hashcode_ = (ref == null) ? 0 : ref.hashCode();
		}
		return hashcode_;
	}

	T getKey() {
		return key_;
	}

}
