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
import org.openntf.domino.impl.Session;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReference.
 */
public class DominoReference<T> extends PhantomReference<org.openntf.domino.Base<?>> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReference.class.getName());

	/** The delegate_. */
	private final lotus.domino.Base delegate_;
	/** The delegate type_. */
	private final Class<?> delegateType_;

	/** The delegate id_. */
	//private final long delegateId_;

	private final int originThreadId_;

	/** The referrant hash_. */
	private final int referrantHash_;

	/** The referrant id_. */
	private final int referrantId_;

	/** The referrant session_. */
	private Session referrantSession_;

	/** The watched cpp. */
	private static long watchedCpp = 0l;

	/** This is the CPP-ID or a hash value **/
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
	public DominoReference(final Base<?> r, final ReferenceQueue<Base> q, final T key) {
		super(r, q);

		// Because the reference separately contains a pointer to the delegate object, it's still available even
		// though the wrapper is null
		delegate_ = org.openntf.domino.impl.Base.getDelegate(r);
		key_ = key;

		referrantSession_ = (Session) Factory.getSession(r); // TODO NTF - clean up implementation

		originThreadId_ = System.identityHashCode(Thread.currentThread());
		if (log_.isLoggable(Level.FINE)) {
			delegateType_ = delegate_.getClass();
			referrantHash_ = r.hashCode();
			referrantId_ = System.identityHashCode(r);
		} else {
			delegateType_ = null;
			referrantHash_ = 0;
			referrantId_ = 0;
		}
	}

	//	/**
	//	 * Checks if is trace target.
	//	 * 
	//	 * @return true, if is trace target
	//	 */
	//	public boolean isTraceTarget() {
	//		return delegateId_ == watchedCpp;
	//	}

	//	/**
	//	 * Gets the delegate id.
	//	 * 
	//	 * @return the delegate id
	//	 */
	//	public Long getDelegateId() {
	//		return delegateId_;
	//	}

	public int getOriginThreadId() {
		return originThreadId_;
	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public Session getSession() {
		return referrantSession_;
	}

	/**
	 * _get referrant hash.
	 * 
	 * @return the int
	 */
	public int _getReferrantHash() {
		return referrantHash_;
	}

	/**
	 * _get referrant id.
	 * 
	 * @return the int
	 */
	public int _getReferrantId() {
		return referrantId_;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Class<?> _getType() {
		return delegateType_;
	}

	/**
	 * Recycle.
	 */
	void recycle() {
		int ctid = System.identityHashCode(Thread.currentThread());
		if (!(ctid == getOriginThreadId())) {

			log_.log(Level.WARNING, "Attempting to recycle a Domino reference from thread " + ctid
					+ " which is different from the one that originated it (" + getOriginThreadId()
					+ "). This is probably going to be very very bad...");
		}
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
	 * methods.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof DominoReference))
			return false;

		Object ref1 = this.get();
		@SuppressWarnings("unchecked")
		Object ref2 = ((DominoReference<T>) obj).get();

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
