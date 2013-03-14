package org.openntf.domino.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.WeakHashMap;

import org.openntf.domino.thread.DominoReference;
import org.openntf.domino.thread.DominoReferenceCounter;
import org.openntf.domino.thread.DominoReferenceQueue;
import org.openntf.domino.thread.DominoReferenceSet;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public abstract class Base<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base> implements org.openntf.domino.Base<D> {

	private static DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	public static DominoReferenceCounter _getReferenceCounter() {
		return lotusReferenceCounter_;
	}

	// TODO NTF - we really should keep a Map of lotus objects to references, so we can only auto-recycle when we know there are no other
	// references to the same shared object.
	// problem today is: there's no clear way to determine an identity for the NotesBase object.
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
	private static Method getCppMethod;
	private static Method isInvalidMethod;
	static {
		try {
			getCppMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
			getCppMethod.setAccessible(true);
			isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
			isInvalidMethod.setAccessible(true);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

	}

	protected boolean recycled_;
	protected D delegate_; // NTF final???
	private DominoReference ref_; // this is the PhantomReference that will be enqueued when this Base object HAS BEEN be GC'ed
	private boolean encapsulated_ = false;
	private org.openntf.domino.Base<?> parent_;

	// TODO NTF - not sure about maintaining a set pointer to children. Not using for now. Just setting up (no pun intended)
	private final Set<org.openntf.domino.Base<?>> children_ = Collections
			.newSetFromMap(new WeakHashMap<org.openntf.domino.Base<?>, Boolean>());

	// protected Set<org.openntf.domino.Base<?>> getChildren() {
	// if (children_ == null) {
	// children_ = new HashSet<org.openntf.domino.Base<?>>();
	// }
	// return children_;
	// }

	void setParent(org.openntf.domino.Base<?> parent) {
		parent_ = parent;
		// TODO NTF - add to parent's children set?
	}

	org.openntf.domino.Base<?> getParent() {
		return parent_;
	}

	// void setParent(lotus.domino.Base parent) {
	// parent_ = parent;
	// }

	protected Base(D delegate, org.openntf.domino.Base<?> parent) {
		if (delegate != null) {
			delegate_ = delegate;
			ref_ = new DominoReference(this, recycleQueue.get(), delegate);
			referenceBag.get().add(ref_);
			refSet.add(delegate);
			if (delegate instanceof lotus.domino.local.NotesBase) {
				int count = lotusReferenceCounter_.increment((lotus.domino.local.NotesBase) delegate_);
			}
		} else {
			encapsulated_ = true;
		}
		if (parent != null) {
			setParent(parent);
		}
	}

	public static long getLotusId(lotus.domino.local.NotesBase base) {
		try {
			return ((Long) getCppMethod.invoke(base, (Object[]) null)).longValue();
		} catch (Exception e) {
			return 0L;
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

	public static boolean isRecycled(lotus.domino.local.NotesBase base) {
		try {
			return ((Boolean) isInvalidMethod.invoke(base, (Object[]) null)).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	public static int decrementCounter(lotus.domino.local.NotesBase base) {
		int count = lotusReferenceCounter_.decrement(base);
		return count;
	}

	public static int incrementCounter(lotus.domino.local.NotesBase base) {
		int count = lotusReferenceCounter_.increment(base);
		return count;
	}

	// Convert a wrapper object to its delegate form
	@SuppressWarnings("unchecked")
	public static lotus.domino.Base toLotus(lotus.domino.Base baseObj) {
		if (baseObj instanceof org.openntf.domino.Base) {
			return ((Base) baseObj).getDelegate();
		}
		return baseObj;
	}

	public static boolean recycle(lotus.domino.local.NotesBase base) {
		boolean result = false;

		if (!isLocked(base)) {
			int count = lotusReferenceCounter_.getCount(base);
			if (count < 2) {

				try {
					if (isRecycled(base)) {
						// System.out.println("NotesBase object " + getLotusId(base) + " (" + base.getClass().getSimpleName()
						// + ") has already been recycled...");
					} else {
						if (count == 1)
							System.out.println("Recycling a " + base.getClass().getName() + " (" + getLotusId(base)
									+ ") because we have only this reference remaining");
						if (count == 0)
							System.out.println("Recycling a " + base.getClass().getName() + " (" + getLotusId(base)
									+ ") because we have ZERO references remaining");
						base.recycle();
						result = true;
					}

				} catch (Throwable t) {
					Factory.countRecycleError();
					// shikata ga nai
				}
			} else {
				// System.out.println("Not recycling a " + base.getClass().getName() + " (" + getLotusId(base) + ") because it still has "
				// + count + " references.");
			}
		} else {
			System.out.println("Not recycling a " + base.getClass().getName() + " because its locked.");
		}
		return result;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public static void recycle(Object o) {
		if (o instanceof lotus.domino.Base) {
			if (o instanceof lotus.domino.local.NotesBase) {
				recycle((lotus.domino.local.NotesBase) o);
			}
			if (o instanceof org.openntf.domino.impl.Base) {
				Base base = (org.openntf.domino.impl.Base) o;
				// System.out.println("Recycling an OpenNTF object of type " + base.getClass().getName());
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
