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

import java.lang.reflect.Array;
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
import org.openntf.domino.exceptions.BlockedCrashException;
import org.openntf.domino.ext.Formula;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.NotImplementedException;

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

	/** The cpp_object. */
	private long cpp_object = 0;

	/** The cpp_object. */
	private final long cpp_session;

	/** the class id of this object type (implemented as precaution) **/
	final int clsid;

	/** The Constant lockedRefSet. */
	// private static final DominoLockSet lockedRefSet = new DominoLockSet();

	/** The get cpp method. */
	private static Method getCppObjMethod;

	///** The get cpp method. */
	//private static Method getCppSessionMethod;

	/** The is invalid method. */
	private static Method isInvalidMethod;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					getCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppObjMethod.setAccessible(true);

					//getCppSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppSession", (Class<?>[]) null);
					//getCppSessionMethod.setAccessible(true);

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

	/** The delegate_. */
	protected D delegate_; // NTF final??? RPr: Final not possible, otherwise ressurect won't work

	/** The parent_. */
	private final org.openntf.domino.Base<?> parent_;

	/**
	 * returns the cpp_id. DO NOT REMOVE. Otherwise native funtions won't work
	 * 
	 * @return
	 */
	public long GetCppObj() {
		return cpp_object;
	}

	/**
	 * returns the cpp-session id. Needed for some BackendBridge functions
	 * 
	 * @return
	 */
	public long GetCppSession() {
		return cpp_session;
	}

	/**
	 * Returns the class-id. Currently not used
	 * 
	 * @return
	 */
	int GetClassID() {
		return clsid;
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
	 * Use constructor with ClassID in future
	 * 
	 * @param delegate
	 * @param parent
	 */
	@Deprecated
	protected Base(final D delegate, final org.openntf.domino.Base<?> parent) {
		this(delegate, parent, 0);
	}

	/**
	 * Instantiates a new base.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Base(final D delegate, final org.openntf.domino.Base<?> parent, final int classId) {
		parent_ = parent;
		clsid = classId;

		if (delegate instanceof lotus.domino.local.NotesBase) {
			setDelegate(delegate);
		} else if (delegate != null) {
			// normally you won't get here if you come from Factory.fromLotus
			throw new IllegalArgumentException("Why are you wrapping a non-Lotus object? " + delegate.getClass().getName());
		}

		// copy the cpp_session from the parent
		if (parent != null) {
			cpp_session = ((Base) parent).GetCppSession();
		} else if (delegate instanceof lotus.domino.Session) {
			cpp_session = cpp_object;
		} else {
			cpp_session = 0;
		}
	}

	/**
	 * Sets the delegate on init or if resurrect occured
	 * 
	 * @param delegate
	 */
	void setDelegate(final D delegate) {
		delegate_ = delegate;
		try {
			cpp_object = ((Long) getCppObjMethod.invoke(delegate, (Object[]) null)).longValue();

		} catch (Exception e) {
			cpp_object = 0L;
		}
	}

	/**
	 * Gets a unique key for a lotus object. This is the lotusId where unneccessary bits are removed
	 * 
	 * @param base
	 *            the base
	 * @return the lotus id
	 */
	public static Integer getLotusKey(final lotus.domino.Base base) {
		try {
			long cpp_id = ((Long) getCppObjMethod.invoke((lotus.domino.local.NotesBase) base, (Object[]) null)).longValue();

			return Integer.valueOf((int) ((cpp_id >> 2) & 0xFFFFFFFFL));
		} catch (Exception e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Gets the delegate.
	 * 
	 * @param wrapper
	 *            the wrapper
	 * @return the delegate
	 */
	//
	//@SuppressWarnings("rawtypes")
	public static lotus.domino.Base getDelegate(final lotus.domino.Base wrapper) {
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
		return (this instanceof Encapsulated);
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is locked
	 */
	@Deprecated
	public static boolean isLocked(final lotus.domino.Base base) {
		//return lockedRefSet.isLocked(base);
		return false;
	}

	/**
	 * Unlock.
	 * 
	 * @param base
	 *            the base
	 */
	@Deprecated
	public static void unlock(final lotus.domino.Base base) {
		//lockedRefSet.unlock(base);
	}

	/**
	 * Lock.
	 * 
	 * @param base
	 *            the base
	 */
	@Deprecated
	public static void lock(final lotus.domino.Base base) {
		//lockedRefSet.lock(base);
	}

	/**
	 * Lock.
	 * 
	 * @param allYourBase
	 *            the all your base
	 */
	@Deprecated
	public static void lock(final lotus.domino.Base... allYourBase) {
		//for (lotus.domino.Base everyZig : allYourBase) {
		//	lockedRefSet.lock(everyZig);
		//}
	}

	/**
	 * Unlock.
	 * 
	 * @param allYourBase
	 *            the all your base
	 */
	@Deprecated
	public static void unlock(final lotus.domino.Base... allYourBase) {
		//lockedRefSet.unlock(allYourBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	public void recycle() {
		//s_recycle(this);
		xs_recycle(delegate_); // RPr: we must recycle the delegate, not "this". Do not call getDelegate as it may reinstantiate it
	}

	/**
	 * Checks if is recycled.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	public static boolean isInvalid(final lotus.domino.Base base) {
		if (base == null)
			return true;
		try {
			return ((Boolean) isInvalidMethod.invoke(base, (Object[]) null)).booleanValue();
		} catch (Exception e) {
			return true;
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
		} else {
			return baseObj;
		}
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
	 * <font color='red'>Do not forget to recycle the returned object</font>
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
		//Extended in order to deal with Arrays
		if (value.getClass().isArray()) {
			int i = Array.getLength(value);

			java.util.Vector<Object> result = new java.util.Vector<Object>(i);
			for (int k = 0; k < i; ++k) {
				Object o = Array.get(value, k);
				result.add(toDominoFriendly(o, context));
			}
			return result;
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
		} else if (value instanceof lotus.domino.Name) {
			return toLotus((lotus.domino.Name) value);
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
			lotus.domino.Session lsess = ((Session) Factory.getSession(context)).getDelegate();
			try {
				return lsess.createDateTime((java.util.Date) value);
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Date) value));
		} else if (value instanceof java.util.Calendar) {
			lotus.domino.Session lsess = ((Session) Factory.getSession(context)).getDelegate();
			try {
				return lsess.createDateTime((java.util.Calendar) value);
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Calendar) value));
		} else if (value instanceof CharSequence) {
			return value.toString();
		} else if (value instanceof CaseInsensitiveString) {
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
		//if (!isLocked(base)) {
		try {
			if (base instanceof lotus.domino.local.DateRange) {	//NTF - check to see if we have valid start/end dates to prevent crashes in 9.0.1
				lotus.domino.local.DateRange dr = (lotus.domino.local.DateRange) base;
				lotus.domino.local.DateTime sdt = (lotus.domino.local.DateTime) dr.getStartDateTime();
				lotus.domino.local.DateTime edt = (lotus.domino.local.DateTime) dr.getEndDateTime();
				if (sdt == null || edt == null) {
					//don't recycle. Better to leak some memory than crash the server entirely!
					String message = "Attempted to recycle a DateRange with a null startDateTime or endDateTime. This would have caused a GPF in the Notes API, so we didn't do it.";
					log_.log(Level.WARNING, message);
					throw new BlockedCrashException(message);
				}
			}
			base.recycle();
			result = true;
		} catch (Throwable t) {
			Factory.countRecycleError();
			DominoUtils.handleException(t);
			// shikata ga nai
		}
		//} else {
		//	System.out.println("Not recycling a " + base.getClass().getName() + " because it's locked.");
		//}
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
		if (o instanceof Collection) {
			Collection c = (Collection) o;
			if (!c.isEmpty()) {
				for (Object io : (Collection) o) {
					if (io instanceof lotus.domino.local.NotesBase) {
						s_recycle((lotus.domino.local.NotesBase) io);
					}
				}
			}
		}
	}

	public static void enc_recycle(final Object o) {
		// NTF this is for recycling of encapsulated objects like DateTime and Name
		// RPr ' do we need an extra method here?
		if (o instanceof Collection) {
			if (!((Collection) o).isEmpty()) {
				for (Object io : (Collection) o) {
					if (io instanceof lotus.domino.DateTime || io instanceof lotus.domino.DateRange || io instanceof lotus.domino.Name) {
						s_recycle(io);
					}
				}
			}
		}
		if (o instanceof lotus.domino.DateTime || o instanceof lotus.domino.DateRange || o instanceof lotus.domino.Name) {
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
				((org.openntf.domino.impl.Base) o).recycle();
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

	// ------------ native functions - to be as much compatible to the lotus.domino.local...
	// Methods that have no meaningful implementation  throw a exception

	@Deprecated
	public boolean needsGui() {
		log_.fine("needsGui called");
		return false;
	}

	@Deprecated
	public void dontUseGui() {
		log_.fine("dontUseGui called");
	}

	@Deprecated
	public void okToUseGui() {
		log_.fine("okToUseGui called");
	}

	@Deprecated
	public boolean avoidingGui() {
		log_.fine("avoidingGui called");

		return true;
	}

	// ---- package private
	@Deprecated
	void markInvalid() {
		throw new NotImplementedException();
	}

	@Deprecated
	void ClearCppObj() {
		throw new NotImplementedException();
	}

	@Deprecated
	Object getWeak() {
		throw new NotImplementedException();
	}

	@Deprecated
	Session getSession() {
		throw new NotImplementedException();
	}

	@Deprecated
	Object getGCParent() {
		throw new NotImplementedException();
	}

	@Deprecated
	boolean isInvalid() {
		throw new NotImplementedException();
	}

	void restoreObject(final Session paramSession, final long paramLong) {
		throw new NotImplementedException();
	}

	@Deprecated
	void CheckObject() {
		throw new NotImplementedException();
	}

	@Deprecated
	void CheckObjectActive() {
		throw new NotImplementedException();
	}

	@Deprecated
	boolean isDead() {
		throw new NotImplementedException();
	}

	@Deprecated
	void CheckArg(final Object paramObject) {
		throw new NotImplementedException();
	}

	@Deprecated
	boolean isEqual(final long paramLong) {
		throw new NotImplementedException();
	}

	@Deprecated
	Vector PropGetVector(final int paramInt) {
		throw new NotImplementedException();
	}

	@Deprecated
	void validateObjArg(final Object paramObject, final boolean paramBoolean) {
		throw new NotImplementedException();
	}

	@Deprecated
	Vector getStringArrayProperty(final int paramInt) {
		throw new NotImplementedException();
	}

}
