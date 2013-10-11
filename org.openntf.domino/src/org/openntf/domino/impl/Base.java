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
package org.openntf.domino.impl;

import java.lang.ref.Reference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.ext.Formula;
import org.openntf.domino.thread.DominoLockSet;
import org.openntf.domino.thread.DominoReference;
import org.openntf.domino.thread.DominoReferenceQueue;
import org.openntf.domino.types.Encapsulated;
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
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Base.class.getName());

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

	/** The cpp_object. */
	private long cpp_object = 0l;

	// /** The reference bag. */
	// private static ThreadLocal<Set<DominoReference>> referenceBag = new ThreadLocal<Set<DominoReference>>() {
	// @Override
	// protected Set<DominoReference> initialValue() {
	// return new HashSet<DominoReference>();
	// // return Collections.newSetFromMap(new WeakHashMap<DominoReference, Boolean>());
	// };
	// };

	/** The Constant lockedRefSet. */
	private static final DominoLockSet lockedRefSet = new DominoLockSet();

	/** The get cpp method. */
	private static Method getCppObjMethod;

	/** The getCppSession method. - required for computeWithForm */
	private static Method getCppSessionMethod;

	/** The is invalid method. */
	private static Method isInvalidMethod;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					getCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppObjMethod.setAccessible(true);

					getCppSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppSession", (Class<?>[]) null);
					getCppSessionMethod.setAccessible(true);

					isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
					isInvalidMethod.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}

	}

	long GetCppSession() {
		try {
			return ((Long) getCppSessionMethod.invoke(delegate_, (Object[]) null)).longValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	long GetCppObj() {
		try {
			return ((Long) getCppObjMethod.invoke(delegate_, (Object[]) null)).longValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	// /** The recycled_. */
	// protected boolean recycled_;

	/** The delegate_. */
	protected D delegate_; // NTF final???

	// /** The encapsulated_. */
	// private boolean encapsulated_ = false;

	/** The parent_. */
	private org.openntf.domino.Base<?> parent_;

	// TODO NTF - not sure about maintaining a set pointer to children. Not using for now. Just setting up (no pun intended)
	/** The children_. */
	// private final Set<org.openntf.domino.Base<?>> children_ = Collections
	// .newSetFromMap(new WeakHashMap<org.openntf.domino.Base<?>, Boolean>());

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	void setParent(final org.openntf.domino.Base<?> parent) {
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

	/**
	 * Instantiates a new base.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@SuppressWarnings("rawtypes")
	protected Base(final D delegate, final org.openntf.domino.Base<?> parent) {
		if (parent != null) {
			setParent(parent);
		}
		if (delegate != null) {
			if (delegate instanceof org.openntf.domino.impl.Base) {
				if (log_.isLoggable(Level.INFO))
					log_.log(Level.INFO, "Why are you wrapping a non-Lotus object? " + delegate.getClass().getName());
				recycleQueue.get().bagReference(
						new DominoReference(this, recycleQueue.get(), ((org.openntf.domino.impl.Base) delegate).getDelegate()));
			} else if (delegate instanceof lotus.domino.local.NotesBase) {
				delegate_ = delegate;
				cpp_object = getLotusId((lotus.domino.local.NotesBase) delegate);
				if (delegate instanceof lotus.domino.Name || delegate instanceof lotus.domino.DateTime
						|| delegate instanceof lotus.domino.Session) {
					// No reference needed. Will be recycled directly...
					// Not creating auto-recycle references for Sessions
					// TODO - NTF come up with a better solution for recycling Sessions!!!
				} else {
					recycleQueue.get().bagReference(new DominoReference(this, recycleQueue.get(), delegate));
				}
			} else {
				if (log_.isLoggable(Level.WARNING))
					log_.log(Level.WARNING, "Why are you wrapping a non-Lotus object? " + delegate.getClass().getName());
			}
		}
		drainQueue(cpp_object);
		// else {
		// encapsulated_ = true;
		// }

	}

	void setDelegate(final D delegate) {

		delegate_ = delegate;
		cpp_object = getLotusId((lotus.domino.local.NotesBase) delegate);
		if (delegate instanceof lotus.domino.Name || delegate instanceof lotus.domino.DateTime || delegate instanceof lotus.domino.Session) {
			// TODO - NTF come up with a better solution for recycling Sessions!!!
		} else {
			recycleQueue.get().bagReference(new DominoReference(this, recycleQueue.get(), delegate));
		}
		// if (delegate instanceof lotus.domino.Document) {
		// try {
		// System.out.println("Redelegating a document: " + ((lotus.domino.Document) delegate).getNoteID() + " (" + cpp_object + ")");
		// } catch (NotesException e) {
		// DominoUtils.handleException(e);
		// }
		// }
	}

	/**
	 * Gets the lotus id.
	 * 
	 * @param base
	 *            the base
	 * @return the lotus id
	 */
	public static long getLotusId(final lotus.domino.local.NotesBase base) {
		try {
			return ((Long) getCppObjMethod.invoke(base, (Object[]) null)).longValue();
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * _get recycle queue.
	 * 
	 * @return the domino reference queue
	 */
	private static DominoReferenceQueue _getRecycleQueue() {
		return recycleQueue.get();
	}

	/**
	 * Drain queue.
	 * 
	 * @return the int
	 */
	public static int drainQueue(final long cppid) {
		int result = 0;
		DominoReferenceQueue drq = _getRecycleQueue();
		Reference<?> ref = drq.poll(cppid);

		while (ref != null) {
			ref = drq.poll(cppid);
			result++;
		}
		return result;
	}

	/**
	 * Finalize queue.
	 * 
	 * @return the int
	 */
	public static int finalizeQueue() {
		int result = 0;
		DominoReferenceQueue drq = _getRecycleQueue();
		result = drq.finalizeQueue();
		return result;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @param wrapper
	 *            the wrapper
	 * @return the delegate
	 */
	@SuppressWarnings("rawtypes")
	public static lotus.domino.Base getDelegate(final lotus.domino.Base wrapper) {
		if (wrapper instanceof org.openntf.domino.impl.Base) {
			return ((org.openntf.domino.impl.Base) wrapper).getDelegate();
		}
		return wrapper;
	}

	/**
	 * Gets the cpp_object.
	 * 
	 * @param wrapper
	 *            the wrapper
	 * @return the cpp handle
	 */
	@SuppressWarnings("rawtypes")
	public static long getDelegateId(final org.openntf.domino.impl.Base wrapper) {
		return ((org.openntf.domino.impl.Base) wrapper).cpp_object;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	protected D getDelegate() {
		// if (delegate_ instanceof lotus.domino.local.Document) {
		// try {
		// ((lotus.domino.local.Document) delegate_).isProfile();
		// } catch (NotesException e) {
		// System.out.println("Delegate validation failed on a document with cpp id " + cpp_object);
		// }
		// }
		return delegate_;
	}

	/**
	 * Checks if is encapsulated.
	 * 
	 * @return true, if is encapsulated
	 */
	public boolean isEncapsulated() {
		return (this instanceof Encapsulated);
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is locked
	 */
	public static boolean isLocked(final lotus.domino.Base base) {
		return lockedRefSet.isLocked(base);
	}

	/**
	 * Unlock.
	 * 
	 * @param base
	 *            the base
	 */
	public static void unlock(final lotus.domino.Base base) {
		lockedRefSet.unlock(base);
	}

	/**
	 * Lock.
	 * 
	 * @param base
	 *            the base
	 */
	public static void lock(final lotus.domino.Base base) {
		lockedRefSet.lock(base);
	}

	/**
	 * Lock.
	 * 
	 * @param allYourBase
	 *            the all your base
	 */
	public static void lock(final lotus.domino.Base... allYourBase) {
		for (lotus.domino.Base everyZig : allYourBase) {
			lockedRefSet.lock(everyZig);
		}
	}

	/**
	 * Unlock.
	 * 
	 * @param allYourBase
	 *            the all your base
	 */
	public static void unlock(final lotus.domino.Base... allYourBase) {
		lockedRefSet.unlock(allYourBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	public void recycle() {
		s_recycle(this);
	}

	/**
	 * Checks if is recycled.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	public static boolean isRecycled(final lotus.domino.local.NotesBase base) {
		try {
			return ((Boolean) isInvalidMethod.invoke(base, (Object[]) null)).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	// /**
	// * Decrement counter.
	// *
	// * @param base
	// * the base
	// * @return the int
	// */
	// public static int decrementCounter(lotus.domino.local.NotesBase base) {
	// int count = lotusReferenceCounter_.decrement(base);
	// return count;
	// }

	// /**
	// * Increment counter.
	// *
	// * @param base
	// * the base
	// * @return the int
	// */
	// public static int incrementCounter(lotus.domino.local.NotesBase base) {
	// int count = lotusReferenceCounter_.increment(base);
	// return count;
	// }

	// Convert a wrapper object to its delegate form
	/**
	 * To lotus.
	 * 
	 * @param baseObj
	 *            the base obj
	 * @return the lotus.domino. base
	 */
	public static lotus.domino.Base toLotus(final lotus.domino.Base baseObj) {
		if (baseObj instanceof org.openntf.domino.Base) {
			return ((Base<?, ?>) baseObj).getDelegate();
		}
		return baseObj;
	}

	// Convert a wrapper object to its delegate form, allowing for non-Lotus objects (e.g. for getDocumentByKey)
	/**
	 * To lotus.
	 * 
	 * @param baseObj
	 *            the base obj
	 * @return the lotus.domino. base version or the object itself, as appropriate
	 */
	public static Object toLotus(final Object baseObj) {
		if (baseObj instanceof org.openntf.domino.Base) {
			return ((Base<?, ?>) baseObj).getDelegate();
		}
		return baseObj;
	}

	/**
	 * 
	 * <p>
	 * Attempts to convert a provided scalar value to a "Domino-friendly" data type like DateTime, String, etc. Currently, the data types
	 * supported are the already-Domino-friendly ones, Number, Date, Calendar, and CharSequence.
	 * </p>
	 * 
	 * @param value
	 *            The incoming non-collection value
	 * @param context
	 *            The context Base object, for finding the correct session
	 * @return The Domino-friendly conversion of the object, or the object itself if it is already usable.
	 * @throws IllegalArgumentException
	 *             When the object is not convertible.
	 */
	protected static Object toDominoFriendly(final Object value, final Base<?, ?> context) throws IllegalArgumentException {
		if (value == null) {
			log_.log(Level.INFO, "Trying to convert a null argument to Domino friendly. Returning null...");
			return null;
		}
		if (value instanceof Collection) {
			java.util.Vector<Object> result = new java.util.Vector<Object>();
			Collection<?> coll = (Collection) value;
			for (Object o : coll) {
				result.add(toDominoFriendly(o, context));
			}
			return result;
		}

		// First, go over the normal data types
		if (value instanceof org.openntf.domino.Base) {
			return toLotus((org.openntf.domino.Base) value);
		} else if (value instanceof lotus.domino.DateTime) {
			return toLotus((lotus.domino.DateTime) value);
		} else if (value instanceof lotus.domino.DateRange) {
			return toLotus((lotus.domino.DateRange) value);
		} else if (value instanceof lotus.domino.Item) {
			return toLotus((lotus.domino.Item) value);
		} else if (value instanceof Integer || value instanceof Double) {
			return value;
		} else if (value instanceof String) {
			return value;
		} else if (value instanceof Boolean) {
			if ((Boolean) value) {
				return "1";
			} else {
				return "0";
			}
		}

		// Now for the illegal-but-convertible types
		if (value instanceof Number) {
			// TODO Check if this is greater than what Domino can handle and serialize if so
			return ((Number) value).doubleValue();
		} else if (value instanceof java.util.Date) {
			lotus.domino.Session lsess = (lotus.domino.Session) Base.getDelegate(Factory.getSession(context));
			try {
				return lsess.createDateTime((java.util.Date) value);
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Date) value));
		} else if (value instanceof java.util.Calendar) {
			lotus.domino.Session lsess = (lotus.domino.Session) Base.getDelegate(Factory.getSession(context));
			try {
				return lsess.createDateTime((java.util.Calendar) value);
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Calendar) value));
		} else if (value instanceof CharSequence) {
			return value.toString();
		} else if (value instanceof Pattern) {
			return ((Pattern) value).pattern();
		} else if (value instanceof Class<?>) {
			return ((Class<?>) value).getName();
		} else if (value instanceof Enum<?>) {
			return ((Enum<?>) value).getDeclaringClass().getName() + " " + ((Enum<?>) value).name();
		} else if (value instanceof Formula) {
			return ((Formula) value).getExpression();
		}

		throw new IllegalArgumentException("Cannot convert to Domino friendly from type " + value.getClass().getName());
	}

	/**
	 * To domino friendly.
	 * 
	 * @param values
	 *            the values
	 * @param context
	 *            the context
	 * @return the vector
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	protected static java.util.Vector<Object> toDominoFriendly(final Collection<?> values, final Base<?, ?> context)
			throws IllegalArgumentException {
		java.util.Vector<Object> result = new java.util.Vector<Object>();
		for (Object value : values) {
			result.add(toDominoFriendly(value, context));
		}
		return result;
	}

	/**
	 * To lotus.
	 * 
	 * @param values
	 *            the values
	 * @return the java.util. vector
	 */
	public static java.util.Vector<Object> toLotus(final Collection<?> values) {
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
	public static boolean s_recycle(final lotus.domino.local.NotesBase base) {
		boolean result = false;
		if (!isLocked(base)) {
			try {
				base.recycle();
				result = true;
			} catch (Throwable t) {
				Factory.countRecycleError();
				// shikata ga nai
			}
		} else {
			System.out.println("Not recycling a " + base.getClass().getName() + " because it's locked.");
		}
		return result;
	}

	/**
	 * Recycle.
	 * 
	 * @param o
	 *            the o
	 */
	public static void s_recycle(final Object o) {
		if (o instanceof lotus.domino.Base) {
			if (o instanceof lotus.domino.local.NotesBase) {
				s_recycle((lotus.domino.local.NotesBase) o);
			}
		}

	}

	public static void enc_recycle(final Object o) {
		// NTF this is for recycling of encapsulated objects like DateTime and Name
		if (o instanceof Collection) {
			if (!((Collection) o).isEmpty()) {
				for (Object io : (Collection) o) {
					if (io instanceof lotus.domino.DateTime || io instanceof lotus.domino.Name) {
						s_recycle(io);
					}
				}
			}
		}
		if (o instanceof lotus.domino.DateTime || o instanceof lotus.domino.Name) {
			s_recycle(o);
		}

	}

	// /**
	// * Checks if is recycled.
	// *
	// * @return true, if is recycled
	// */
	// public boolean isRecycled() {
	// return recycled_;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
	@Deprecated
	@SuppressWarnings("rawtypes")
	public void recycle(final Vector arg0) {
		for (Object o : arg0) {
			if (o instanceof org.openntf.domino.impl.Base) {
				s_recycle((org.openntf.domino.impl.Base) o);
			} else if (o instanceof lotus.domino.local.NotesBase) {
				s_recycle((lotus.domino.local.NotesBase) o);
			}
		}
	}

	private List<IDominoListener> listeners_;

	public List<IDominoListener> getListeners() {
		if (listeners_ == null) {
			listeners_ = new ArrayList<IDominoListener>();
		}
		return listeners_;
	}

	public void addListener(final IDominoListener listener) {
		getListeners().add(listener);
	}

	public void removeListener(final IDominoListener listener) {
		getListeners().remove(listener);
	}

	public List<IDominoListener> getListeners(final EnumEvent event) {
		List<IDominoListener> result = new ArrayList<IDominoListener>();
		for (IDominoListener listener : getListeners()) {
			for (EnumEvent curEvent : listener.getEventTypes()) {
				if (curEvent.equals(event)) {
					result.add(listener);
					break;
				}
			}
		}
		return result;
	}

	public boolean fireListener(final IDominoEvent event) {
		boolean result = true;
		for (IDominoListener listener : getListeners(event.getEvent())) {
			try {
				if (!listener.eventHappened(event)) {
					result = false;
					break;
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getDelegate().toString();

	}

}
