package org.openntf.domino.thread;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import org.openntf.domino.Base;
import org.openntf.domino.utils.DominoUtils;

public class DominoReference extends PhantomReference<org.openntf.domino.Base<?>> {
	private lotus.domino.Base delegate_;

	public DominoReference(Base<?> r, ReferenceQueue<? super Base<?>> q, lotus.domino.Base delegate) {
		super(r, q);
		delegate_ = delegate; // Because the reference separately contains a pointer to the delegate object, it's still available even
								// though the wrapper is null
	}

	@Override
	public boolean enqueue() {
		// FIXME NTF - this needs to be tested to verify that the enqueue() call will be on the same thread as the reference queue itself
		// If not, we'll have to do some polling on the proper thread...

		System.out.println("DominoReference enqueue occurring on thread: " + Thread.currentThread().getName() + " ("
				+ Thread.currentThread().hashCode() + ")");

		// This method of the PhantomReference is only called when there are no accessible handles to the original org.openntf.domino.Base
		// object
		// TODO verify that the base object is not in our locked set
		if (!org.openntf.domino.impl.Base.isLocked(delegate_)) {
			try {
				delegate_.recycle();
			} catch (Throwable t) {
				// TODO
				DominoUtils.handleException(t);
			}
		}
		return super.enqueue();
	}

}
