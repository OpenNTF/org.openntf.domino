package org.openntf.domino.thread;

import java.lang.ref.PhantomReference;

import org.openntf.domino.Base;
import org.openntf.domino.utils.Factory;

public class DominoReference extends PhantomReference<org.openntf.domino.Base<?>> {
	// private static DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	private lotus.domino.Base delegate_;
	// private Long delegateId_;
	private Class<?> delegateType_;

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

	public Class<?> getType() {
		return delegateType_;
	}

	public void recycle() {
		org.openntf.domino.impl.Base.recycle(delegate_);
		Factory.countAutoRecycle();
		// FIXME NTF - take the dead reference out of the reference set!!!
	}

}
