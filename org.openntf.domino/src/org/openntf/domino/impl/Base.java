package org.openntf.domino.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.openntf.domino.thread.DominoReference;
import org.openntf.domino.thread.DominoReferenceQueue;
import org.openntf.domino.thread.DominoReferenceSet;

public abstract class Base<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base> implements org.openntf.domino.Base<D> {
	private static ThreadLocal<DominoReferenceQueue> recycleQueue = new ThreadLocal<DominoReferenceQueue>() {
		@Override
		protected DominoReferenceQueue initialValue() {
			return new DominoReferenceQueue();
		};
	};

	private static ThreadLocal<Set<DominoReference>> referenceBag = new ThreadLocal<Set<DominoReference>>() {
		@Override
		protected Set<DominoReference> initialValue() {
			return new HashSet<DominoReference>();
		};
	};
	private static final DominoReferenceSet refSet = new DominoReferenceSet();

	protected boolean recycled_;
	protected D delegate_; // NTF final???
	private DominoReference ref_; // this is the PhantomReference that will be enqueued when this Base object HAS BEEN be GC'ed
	private boolean encapsulated_ = false;
	private lotus.domino.Base parent_;
	private Set<lotus.domino.Base> children_;

	protected Set<lotus.domino.Base> getChildren() {
		if (children_ == null) {
			children_ = new HashSet<lotus.domino.Base>();
		}
		return children_;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void parent(org.openntf.domino.impl.Base child) {
		child.parent_ = this;
		getChildren().add(child);
	}

	lotus.domino.Base getParent() {
		return parent_;
	}

	void setParent(lotus.domino.Base parent) {
		parent_ = parent;
	}

	protected Base(D delegate) {
		if (delegate != null) {
			delegate_ = delegate;
			ref_ = new DominoReference(this, recycleQueue.get(), delegate);
			referenceBag.get().add(ref_);
			refSet.add(delegate);
		} else {
			encapsulated_ = true;
		}
	}

	protected Base(D delegate, Base<?, ?> parent) {
		this(delegate);
		if (parent != null) {
			parent(parent);
		}
	}

	public static DominoReferenceQueue _getRecycleQueue() {
		return recycleQueue.get();
	}

	protected D getDelegate() {
		return delegate_;
	}

	public boolean isEncapsulated() {
		return encapsulated_;
	}

	public static boolean isLocked(lotus.domino.Base base) {
		return refSet.isLocked(base);
	}

	public static void lock(lotus.domino.Base base) {
		refSet.lock(base);
	}

	public static void lock(lotus.domino.Base... allYourBase) {
		for (lotus.domino.Base everyZig : allYourBase) {
			refSet.lock(everyZig);
		}
	}

	public static void recycleAll() {
		refSet.clear();
	}

	public void recycle() {
		recycle(this);
	}

	public static boolean recycle(lotus.domino.local.NotesBase base) {
		boolean result = false;
		if (!isLocked(base)) {
			try {
				base.recycle();
				result = true;
			} catch (Throwable t) {
				// shikata ga nai
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void recycle(Object o) {
		if (o instanceof lotus.domino.Base) {
			if (o instanceof lotus.domino.local.NotesBase) {
				recycle((lotus.domino.local.NotesBase) o);
			}
			if (o instanceof org.openntf.domino.impl.Base) {
				Base base = (org.openntf.domino.impl.Base) o;
				System.out.println("Recycling an OpenNTF object of type " + base.getClass().getName());
				if (recycle((lotus.domino.local.NotesBase) base.getDelegate())) {
					base.recycled_ = true;
				}
			}
		}
	}

	public boolean isRecycled() {
		return recycled_;
	}

	@SuppressWarnings("rawtypes")
	public void recycle(Vector arg0) {
		for (Object o : arg0) {
			if (o instanceof org.openntf.domino.impl.Base) {
				recycle((org.openntf.domino.impl.Base) o);
			} else if (o instanceof lotus.domino.local.NotesBase) {
				recycle((lotus.domino.local.NotesBase) o);
			}
		}
	}

}
