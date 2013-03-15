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

import java.lang.ref.PhantomReference;

import org.openntf.domino.Base;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReference.
 */
public class DominoReference extends PhantomReference<org.openntf.domino.Base<?>> {
	// private static DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	/** The delegate_. */
	private lotus.domino.Base delegate_;
	// private Long delegateId_;
	/** The delegate type_. */
	private Class<?> delegateType_;

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
	public DominoReference(Base<?> r, DominoReferenceQueue q, lotus.domino.Base delegate) {
		super(r, q);
		delegate_ = delegate; // Because the reference separately contains a pointer to the delegate object, it's still available even
								// though the wrapper is null
		delegateType_ = delegate.getClass();
		// delegateId_ = org.openntf.domino.impl.Base.getLotusId((lotus.domino.local.NotesBase) delegate);
		// System.out.println("Domino reference created for a " + r.getClass().getName() + " (" + delegate.getClass().getSimpleName()
		// + ") on thread " + Thread.currentThread().getName() + " (" + Thread.currentThread().hashCode() + ")");
	}

	// public Long getLotusId() {
	// return delegateId_;
	// }

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Class<?> getType() {
		return delegateType_;
	}

	/**
	 * Recycle.
	 */
	public void recycle() {
		org.openntf.domino.impl.Base.recycle(delegate_);
		Factory.countAutoRecycle();
		// FIXME NTF - take the dead reference out of the reference set!!!
	}

}
