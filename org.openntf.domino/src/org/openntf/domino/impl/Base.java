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
package org.openntf.domino.impl;

import java.lang.reflect.Method;
import java.util.Collection;
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

// TODO: Auto-generated Javadoc
/**
 * The Class Base.
 * 
 * @param <T>
 *            the generic type
 * @param <D>
 *            the generic type
 */
public abstract class Base<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base> implements org.openntf.domino.Base<D> {

	/** The lotus reference counter_. */
	private static DominoReferenceCounter lotusReferenceCounter_ = new DominoReferenceCounter();

	/**
	 * _get reference counter.
	 * 
	 * @return the domino reference counter
	 */
	public static DominoReferenceCounter _getReferenceCounter() {
		return lotusReferenceCounter_;
	}

	// TODO NTF - we really should keep a Map of lotus objects to references, so we can only auto-recycle when we know there are no other
	// references to the same shared object.
	// problem today is: there's no clear way to determine an identity for the NotesBase object.
	/** The recycle queue. */
	private static ThreadLocal<DominoReferenceQueue> recycleQueue = new ThreadLocal<DominoReferenceQueue>() {
		@Override
		protected DominoReferenceQueue initialValue() {
			return new DominoReferenceQueue();
		};
	};

	/** The reference bag. */
	private static ThreadLocal<Set<DominoReference>> referenceBag = new ThreadLocal<Set<DominoReference>>() {
		@Override
		protected Set<DominoReference> initialValue() {
			return new HashSet<DominoReference>();
		};
	};
	
	/** The Constant refSet. */
	private static final DominoReferenceSet refSet = new DominoReferenceSet();
	
	/** The get cpp method. */
	private static Method getCppMethod;
	
	/** The is invalid method. */
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

	/** The recycled_. */
	protected boolean recycled_;
	
	/** The delegate_. */
	protected D delegate_; // NTF final???
	
	/** The ref_. */
	private DominoReference ref_; // this is the PhantomReference that will be enqueued when this Base object HAS BEEN be GC'ed
	
	/** The encapsulated_. */
	private boolean encapsulated_ = false;
	
	/** The parent_. */
	private org.openntf.domino.Base<?> parent_;

	// TODO NTF - not sure about maintaining a set pointer to children. Not using for now. Just setting up (no pun intended)
	/** The children_. */
	private final Set<org.openntf.domino.Base<?>> children_ = Collections
			.newSetFromMap(new WeakHashMap<org.openntf.domino.Base<?>, Boolean>());

	// protected Set<org.openntf.domino.Base<?>> getChildren() {
	// if (children_ == null) {
	// children_ = new HashSet<org.openntf.domino.Base<?>>();
	// }
	// return children_;
	// }

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	void setParent(org.openntf.domino.Base<?> parent) {
		parent_ = parent;
		// TODO NTF - add to parent's children set?
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	org.openntf.domino.Base<?> getParent() {
		return parent_;
	}

	// void setParent(lotus.domino.Base parent) {
	// parent_ = parent;
	// }

	/**
	 * Instantiates a new base.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Base(D delegate, org.openntf.domino.Base<?> parent) {
		if (delegate != null) {
			delegate_ = delegate;
			ref_ = new DominoReference(this, recycleQueue.get(), delegate);
			referenceBag.get().add(ref_);
			refSet.add(delegate);
			if (delegate instanceof lotus.domino.local.NotesBase) {
				lotus.domino.local.NotesBase base = (lotus.domino.local.NotesBase) delegate;
				int curCount = lotusReferenceCounter_.getCount(base);
				if (curCount > 0) {
					// there's already a reference to this object. WTF is it?
					System.out.println("There's already a reference to " + base.getClass().getSimpleName() + " (" + getLotusId(base)
							+ "). The current call stack is...");
					// Exception e = new RuntimeException();
					// e.printStackTrace();
				} else {
					// System.out.println("No references to " + base.getClass().getSimpleName() + " (" + getLotusId(base)
					// + "). The current call stack is...");
					// Exception e = new RuntimeException();
					// e.printStackTrace();
				}
				int count = lotusReferenceCounter_.increment(base);
			}
		} else {
			encapsulated_ = true;
		}
		if (parent != null) {
			setParent(parent);
		}
	}

	/**
	 * Gets the lotus id.
	 * 
	 * @param base
	 *            the base
	 * @return the lotus id
	 */
	public static long getLotusId(lotus.domino.local.NotesBase base) {
		try {
			return ((Long) getCppMethod.invoke(base, (Object[]) null)).longValue();
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * _get recycle queue.
	 * 
	 * @return the domino reference queue
	 */
	public static DominoReferenceQueue _getRecycleQueue() {
		return recycleQueue.get();
	}

	/**
	 * Gets the delegate.
	 * 
	 * @param wrapper
	 *            the wrapper
	 * @return the delegate
	 */
	@SuppressWarnings("rawtypes")
	public static lotus.domino.Base getDelegate(lotus.domino.Base wrapper) {
		if (wrapper instanceof org.openntf.domino.impl.Base) {
			return ((org.openntf.domino.impl.Base) wrapper).getDelegate();
		}
		return wrapper;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	protected D getDelegate() {
		return delegate_;
	}

	/**
	 * Checks if is encapsulated.
	 * 
	 * @return true, if is encapsulated
	 */
	public boolean isEncapsulated() {
		return encapsulated_;
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is locked
	 */
	public static boolean isLocked(lotus.domino.Base base) {
		return refSet.isLocked(base);
	}

	/**
	 * Lock.
	 * 
	 * @param base
	 *            the base
	 */
	public static void lock(lotus.domino.Base base) {
		refSet.lock(base);
	}

	/**
	 * Lock.
	 * 
	 * @param allYourBase
	 *            the all your base
	 */
	public static void lock(lotus.domino.Base... allYourBase) {
		for (lotus.domino.Base everyZig : allYourBase) {
			refSet.lock(everyZig);
		}
	}

	/**
	 * Recycle all.
	 */
	public static void recycleAll() {
		refSet.clear();
	}

	/* (non-Javadoc)
	 * @see lotus.domino.Base#recycle()
	 */
	public void recycle() {
		recycle(this);
	}

	/**
	 * Checks if is recycled.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	public static boolean isRecycled(lotus.domino.local.NotesBase base) {
		try {
			return ((Boolean) isInvalidMethod.invoke(base, (Object[]) null)).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Decrement counter.
	 * 
	 * @param base
	 *            the base
	 * @return the int
	 */
	public static int decrementCounter(lotus.domino.local.NotesBase base) {
		int count = lotusReferenceCounter_.decrement(base);
		return count;
	}

	/**
	 * Increment counter.
	 * 
	 * @param base
	 *            the base
	 * @return the int
	 */
	public static int incrementCounter(lotus.domino.local.NotesBase base) {
		int count = lotusReferenceCounter_.increment(base);
		return count;
	}

	// Convert a wrapper object to its delegate form
	/**
	 * To lotus.
	 * 
	 * @param baseObj
	 *            the base obj
	 * @return the lotus.domino. base
	 */
	@SuppressWarnings("unchecked")
	public static lotus.domino.Base toLotus(lotus.domino.Base baseObj) {
		if (baseObj instanceof org.openntf.domino.Base) {
			return ((Base) baseObj).getDelegate();
		}
		return baseObj;
	}

	/**
	 * To lotus.
	 * 
	 * @param values
	 *            the values
	 * @return the java.util. vector
	 */
	public static java.util.Vector<Object> toLotus(Collection<?> values) {
		java.util.Vector<Object> result = new java.util.Vector<Object>(values.size());
		for (Object value : values) {
			if (value instanceof lotus.domino.Base) {
				result.add(toLotus((lotus.domino.Base) value));
			} else {
				result.add(value);
			}
		}
		return result;
	}

	/**
	 * Recycle.
	 * 
	 * @param base
	 *            the base
	 * @return true, if successful
	 */
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
						// if (count == 1)
						// System.out.println("Recycling a " + base.getClass().getName() + " (" + getLotusId(base)
						// + ") because we have only this reference remaining");
						// if (count == 0)
						// System.out.println("Recycling a " + base.getClass().getName() + " (" + getLotusId(base)
						// + ") because we have ZERO references remaining");
						// Long id = getLotusId(base);
						// lotusReferenceCounter_.forcedRecycle(id);
						lotusReferenceCounter_.decrement(base);
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

	/**
	 * Recycle.
	 * 
	 * @param o
	 *            the o
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	/**
	 * Checks if is recycled.
	 * 
	 * @return true, if is recycled
	 */
	public boolean isRecycled() {
		return recycled_;
	}

	/* (non-Javadoc)
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
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
