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

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.TypeUtils;

import javolution.util.FastMap;

/**
 * A common Base class for almost all org.openntf.domino types.
 *
 * @param <T>
 *            the generic type
 * @param <D>
 *            the delegate type
 * @param <P>
 *            the parent type
 *
 */
public abstract class Base<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base, P extends org.openntf.domino.Base<?>>
		implements org.openntf.domino.Base<D> {
	public static final int SOLO_NOTES_NAMES = 1000;
	public static final int NOTES_SESSION = 1;
	public static final int NOTES_DATABASE = 2;
	public static final int NOTES_VIEW = 3;
	public static final int NOTES_NOTE = 4;
	public static final int NOTES_ITEM = 5;
	public static final int NOTES_RTITEM = 6;
	public static final int NOTES_REPORT = 7;
	public static final int NOTES_TIME = 8;
	public static final int NOTES_MACRO = 9;
	public static final int NOTES_SERVER = 10;
	public static final int NOTES_DOCCOLL = 11;
	public static final int NOTES_AGENTLOG = 12;
	public static final int NOTES_ACL = 13;
	public static final int NOTES_ACLENTRY = 14;
	public static final int NOTES_VIEWCOLUMN = 15;
	public static final int NOTES_EMBEDOBJ = 16;
	public static final int NOTES_REGISTRATION = 17;
	public static final int NOTES_TIMER = 18;
	public static final int NOTES_NAME = 19;
	public static final int NOTES_FORM = 20;
	public static final int NOTES_INTL = 21;
	public static final int NOTES_DATERNG = 22;
	public static final int NOTES_AGENTCTX = 25;
	public static final int NOTES_RTSTYLE = 26;
	public static final int NOTES_VIEWENTRY = 27;
	public static final int NOTES_VECOLL = 28;
	public static final int NOTES_RTPSTYLE = 29;
	public static final int NOTES_RTTAB = 30;
	public static final int NOTES_REPLICATION = 43;
	public static final int NOTES_VIEWNAV = 44;
	public static final int NOTES_OUTLINEENTRY = 48;
	public static final int NOTES_OUTLINE = 49;
	public static final int NOTES_MIMEENTITY = 50;
	public static final int NOTES_RTTABLE = 51;
	public static final int NOTES_RTNAVIGATOR = 52;
	public static final int NOTES_RTRANGE = 53;
	public static final int NOTES_NOTECOLLECTION = 54;
	public static final int NOTES_DXLEXPORTER = 55;
	public static final int NOTES_DXLIMPORTER = 56;
	public static final int NOTES_MIMEHDR = 78;
	public static final int NOTES_SESSTRM = 79;
	public static final int NOTES_ADMINP = 80;
	public static final int NOTES_RTDOCLNK = 81;
	public static final int NOTES_COLOR = 82;
	public static final int NOTES_RTSECTION = 83;
	public static final int NOTES_REPLENT = 84;
	public static final int NOTES_XMLREFORMATTER = 85;
	public static final int NOTES_DOMDOCUMENTFRAGMENTNODE = 86;
	public static final int NOTES_DOMNOTATIONNODE = 87;
	public static final int NOTES_DOMCHARACTERDATANODE = 88;
	public static final int NOTES_PROPERTYBROKER = 89;
	public static final int NOTES_NOTESPROPERTY = 90;
	public static final int NOTES_DIRECTORY = 91;
	public static final int NOTES_DIRNAVIGATOR = 92;
	public static final int NOTES_DIRENTRY = 93;
	public static final int NOTES_DIRENTRYCOLLECTION = 94;
	public static final int NOTES_DOMAIN = 95;
	public static final int NOTES_CALENDAR = 96;
	public static final int NOTES_CALENDARENTRY = 97;
	public static final int NOTES_CALENDARNOTICE = 98;

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(Base.class.getName());

	private static Method getCppObjMethod;
	//	private static Method checkArgMethod;
	//	private static Method checkObjectMethod;
	//	private static Method checkObjectActiveMethod;
	//	private static Method clearCppObjMethod;
	private static Method getCppSessionMethod;
	//	private static Method getGCParentMethod;
	private static Method getSessionMethod;
	//	private static Method getStringArrayPropertyMethod;
	//	private static Method getWeakMethod;
	private static Method isDeadMethod;
	private static Method isInvalidMethod;
	private static Method isEqualMethod;
	//	private static Method markInvalidMethod;
	// private static Method notImplementedMethod;
	//	private static Method validateObjArgMethod;
	// private static Method restoreObjectMethod;
	private static final Object[] EMPTY_ARRAY = null;
	/** the class id of this object type (implemented as precaution) **/
	final int clsid;

	/** The wrapperFactory we are from **/
	// private final WrapperFactory factory_;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					//					checkArgMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckArg", Object.class);
					//					checkArgMethod.setAccessible(true);
					//					checkObjectMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckObject", (Class<?>[]) null);
					//					checkObjectMethod.setAccessible(true);
					//					checkObjectActiveMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckObjectActive", (Class<?>[]) null);
					//					checkObjectActiveMethod.setAccessible(true);
					//					clearCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("ClearCppObj", (Class<?>[]) null);
					//					clearCppObjMethod.setAccessible(true);
					getCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppObjMethod.setAccessible(true);
					getCppSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppSession", (Class<?>[]) null);
					getCppSessionMethod.setAccessible(true);
					//					getGCParentMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getGCParent", (Class<?>[]) null);
					//					getGCParentMethod.setAccessible(true);
					getSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getSession", (Class<?>[]) null);
					getSessionMethod.setAccessible(true);
					//					getWeakMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getWeak", (Class<?>[]) null);
					//					getWeakMethod.setAccessible(true);
					isDeadMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isDead", (Class<?>[]) null);
					isDeadMethod.setAccessible(true);
					isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
					isInvalidMethod.setAccessible(true);
					isEqualMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isEqual", Long.TYPE);
					isEqualMethod.setAccessible(true);
					//					markInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("markInvalid", (Class<?>[]) null);
					//					markInvalidMethod.setAccessible(true);
					//					validateObjArgMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("validateObjArg", Object.class,
					//							Boolean.TYPE);
					//					validateObjArgMethod.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}

	}

	/**
	 * returns the cpp_id. DO NOT REMOVE. Otherwise native funtions won't work
	 *
	 * @return the cpp_id
	 */
	public final long GetCppObj() {
		try {
			return ((Long) getCppObjMethod.invoke(getDelegate(), EMPTY_ARRAY)).longValue();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return 0L;
		}
	}

	protected static final long GetCppObj(final lotus.domino.Base obj) {
		try {
			if (obj instanceof Base) {
				return ((Long) getCppObjMethod.invoke(toLotus(obj), EMPTY_ARRAY)).longValue();
			} else {
				return ((Long) getCppObjMethod.invoke(obj, EMPTY_ARRAY)).longValue();
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return 0L;
		}
	}

	/**
	 * returns the cpp-session id. Needed for some BackendBridge functions
	 *
	 * @return the cpp_id of the session
	 */
	public final long GetCppSession() {
		try {
			return ((Long) getCppSessionMethod.invoke(getDelegate(), EMPTY_ARRAY)).longValue();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return 0L;
		}
	}

	/** The parent/ancestor. */
	protected P parent;
	public long parentCppId;

	/**
	 * Find the parent if no one was specified
	 *
	 * @param delegate
	 * @return
	 */
	protected P findParent(final WrapperFactory wf, final D delegate, final int i) throws NotesException {
		throw new UnsupportedOperationException("You must specify a valid parent when creating a " + getClass().getName());
	}

	/**
	 * Returns the class-id. Currently not used
	 *
	 * @return
	 */
	int GetClassID() {
		return clsid;
	}

	//	public static class WrapCounter extends ThreadLocal<Long> {
	//
	//		@Override
	//		protected Long initialValue() {
	//			return new Long(0);
	//		}
	//
	//		public void increment() {
	//			long cur = super.get();
	//			super.set(cur++);
	//		}
	//	}

	//	public static WrapCounter traceWrapCount = new WrapCounter();
	//
	//	public static ThreadLocal<Long> traceMinDelta = new ThreadLocal<Long>() {
	//		@Override
	//		protected Long initialValue() {
	//			return Long.MAX_VALUE;
	//		}
	//
	//		@Override
	//		public void set(final Long value) {
	//			if (value < super.get()) {
	//				super.set(value);
	//				//				System.out.println("New min delta discovered: " + value);
	//			}
	//		}
	//	};
	//
	//	public static ThreadLocal<Long> traceMaxDelta = new ThreadLocal<Long>() {
	//		@Override
	//		protected Long initialValue() {
	//			return Long.MIN_VALUE;
	//		}
	//
	//		@Override
	//		public void set(final Long value) {
	//			long curValue = super.get();
	//			if (value > curValue) {
	//				if ((curValue - value) > 100000) {
	//					System.out.println("Jump greater than 100000 when we wrapped object count " + traceWrapCount.get());
	//				}
	//				super.set(value);
	//				//				System.out.println("New max delta discovered: " + value);
	//			}
	//		}
	//	};

	//	/**
	//	 * Gets the parent.
	//	 *
	//	 * @return the parent
	//	 */
	//	protected final P getAncestor(final int deprecated) {
	//		return ancestor;
	//	}
	//
	//	public final P getParent() {
	//		return ancestor;
	//	}

	/**
	 * Instantiates a new base.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent (may be null)
	 * @param classId
	 *            the class id
	 */
	protected Base(final D delegate, final P parent, final int classId) {
		if (parent == null) {
			throw new NullPointerException("parent must not be null");
		}
		this.parent = parent;

		clsid = classId;

		if (delegate instanceof lotus.domino.local.NotesBase) {
			setDelegate(delegate, false);
		} else if (delegate != null) {
			// normally you won't get here if you come from fromLotus
			throw new IllegalArgumentException("Why are you wrapping a non-Lotus object? " + delegate.getClass().getName());
		}
	}

	/**
	 * constructor for no arg child objects (deserialzation)
	 */
	protected Base(final int classId) {
		clsid = classId;
	}

	/**
	 * Sets the delegate on init or if resurrect occurred
	 *
	 * @param delegate
	 *            the delegate
	 */
	protected abstract void setDelegate(final D delegate, boolean fromResurrect);

	//	/**
	//	 * Gets the lotus id.
	//	 *
	//	 * @param base
	//	 *            the base
	//	 * @return the lotus id
	//	 */
	//	protected static long getLotusId(final lotus.domino.Base base) {
	//
	//		//		try {
	//		//			if (base instanceof lotus.domino.local.NotesBase) {
	//		//				return ((Long) getCppObjMethod.invoke(base, EMPTY_ARRAY)).longValue();
	//		//			} else if (base instanceof org.openntf.domino.impl.Base) {
	//		//				return ((org.openntf.domino.impl.Base<?, ?, ?>) base).GetCppObj();
	//		//			}
	//		//		} catch (Exception e) {
	//		//		}
	//		return 0L;
	//	}

	/**
	 * Checks if the lotus object is invalid. A object is invalid if it is recycled by the java side.
	 *
	 * i.E.: Some Java code has called base.recycle();
	 *
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	protected static boolean isInvalid(final lotus.domino.Base base) {
		if (base == null) {
			return true;
		}
		try {
			return ((Boolean) isInvalidMethod.invoke(base, EMPTY_ARRAY)).booleanValue();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return true;
		}
	}

	/**
	 * Checks if is dead. A object is dead if it is invalid (=recycled by java) or if it's cpp-object = 0.
	 *
	 * This happens if the parent was recycled.
	 *
	 * @param base
	 *            the base
	 * @return true, if is recycled
	 */
	public static boolean isDead(final lotus.domino.Base base) {

		if (base == null) {
			return true;
		}
		try {
			return ((Boolean) isDeadMethod.invoke(base, EMPTY_ARRAY)).booleanValue();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return true;
		}
	}

	/**
	 * Returns the session for a certain base object
	 *
	 * @param base
	 * @return
	 */
	protected static lotus.domino.Session getSession(final lotus.domino.Base base) {
		if (base == null) {
			return null;
		}
		try {
			return ((lotus.domino.Session) getSessionMethod.invoke(base, EMPTY_ARRAY));
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	//	/**
	//	 * Gets the delegate.
	//	 *
	//	 * @param wrapper
	//	 *            the wrapper
	//	 * @return the delegate
	//	 */
	//	@SuppressWarnings("rawtypes")
	//	public static lotus.domino.Base getDelegate(final lotus.domino.Base wrapper) {
	//		if (wrapper instanceof org.openntf.domino.impl.Base) {
	//			return ((org.openntf.domino.impl.Base) wrapper).getDelegate();
	//		}
	//		return wrapper;
	//	}

	/**
	 * Gets the delegate.
	 *
	 * @return the delegate
	 */
	protected D getDelegate() {
		D ret = getDelegate_unchecked();
		if (this instanceof Resurrectable && isDead(ret)) {
			if (log_.isLoggable(Level.FINE)) {
				log_.fine("[" + Thread.currentThread().getId() + "] Resurrecting " + getClass().getName() + " '" + super.hashCode() + "'");
			}
			resurrect();
			if (log_.isLoggable(Level.FINE)) {
				log_.fine(
						"[" + Thread.currentThread().getId() + "] Resurrect " + getClass().getName() + " '" + super.hashCode() + "' done");
			}
			ret = getDelegate_unchecked();
		}
		return ret;
	}

	protected abstract D getDelegate_unchecked();

	protected abstract D getDelegate_unchecked(boolean fromIsDead);

	protected void resurrect() {
		throw new AbstractMethodError();
	}

	// wrap objects. Delegate this to the wrapperFactory
	/**
	 * Wraps objects. Delegate to WrapperFactory
	 *
	 * @see org.openntf.domino.WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	protected <T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> T1 fromLotus(
			final D1 lotus, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return getFactory().fromLotus(lotus, schema, parent);
	}

	/**
	 * Wraps a collection. Delegate to WrapperFactory
	 *
	 * @see org.openntf.domino.WrapperFactory#fromLotus(Collection, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	<T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> Collection<T1> fromLotus(
			final Collection<?> lotusColl, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return getFactory().fromLotus(lotusColl, schema, parent);
	}

	/**
	 * Wraps a collection and returns it as vector. Delegate to WrapperFactory
	 *
	 * @see org.openntf.domino.WrapperFactory#fromLotusAsVector(Collection, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	protected <T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> Vector<T1> fromLotusAsVector(
			final Collection<?> lotusColl, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return getFactory().fromLotusAsVector(lotusColl, schema, parent);
	}

	/**
	 * Wraps column values
	 *
	 * @see org.openntf.domino.WrapperFactory#wrapColumnValues(Collection, org.openntf.domino.Session)
	 */
	protected Vector<Object> wrapColumnValues(final Collection<?> values, final org.openntf.domino.Session session) {
		return getFactory().wrapColumnValues(values, session);
	}

	/**
	 * returns the WrapperFactory
	 *
	 * @return
	 */
	protected abstract WrapperFactory getFactory();

	/*
	 * (non-Javadoc)
	 * This method recycles the delegate (and counts it as manual recycle)
	 *
	 * @see lotus.domino.Base#recycle()
	 * (it is NOT deprecated in the .impl. package as it is called in several places!)
	 */
	@Override
	public void recycle() {
		D delegate = getDelegate_unchecked(true);
		if (isDead(delegate)) {
			return;
		}
		s_recycle(delegate);// RPr: we must recycle the delegate, not "this". Do not call getDelegate as it may reinstantiate it
		Factory.countManualRecycle(delegate.getClass());
	}

	// unwrap objects

	/**
	 * gets the delegate
	 *
	 * @param wrapper
	 *            the wrapper
	 * @param recycleThis
	 *            adds the delegate to the list, if it has to be recycled.
	 * @return the delegate
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends lotus.domino.Base> T toLotus(final T wrapper, final Collection recycleThis) {
		return TypeUtils.toLotus(wrapper, recycleThis);
		/*if (wrapper instanceof org.openntf.domino.impl.Base) {
			lotus.domino.Base ret = ((org.openntf.domino.impl.Base) wrapper).getDelegate();
			if (wrapper instanceof Encapsulated && recycleThis != null) {
				recycleThis.add(ret);
			}
			return (T) ret;
		}
		return wrapper;*/
	}

	/**
	 * Gets the delegate.
	 *
	 * @param wrapper
	 *            the wrapper
	 * @return the delegate
	 */
	//
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends lotus.domino.Base> T toLotus(final T wrapper) {
		return TypeUtils.toLotus(wrapper);
		/*if (wrapper instanceof org.openntf.domino.impl.Base) {
			return (T) ((org.openntf.domino.impl.Base) wrapper).getDelegate();
		}
		return wrapper;*/
	}

	/**
	 * To lotus.
	 *
	 * @param baseObj
	 *            the base obj
	 * @return the lotus.domino. base version or the object itself, as appropriate
	 */
	@SuppressWarnings("rawtypes")
	protected static Object toLotus(final Object baseObj) {
		if (baseObj instanceof org.openntf.domino.impl.Base) {
			return ((org.openntf.domino.impl.Base) baseObj).getDelegate();
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
	@SuppressWarnings("rawtypes")
	protected static Object toDominoFriendly(final Object value, final Session session, final Collection<lotus.domino.Base> recycleThis)
			throws IllegalArgumentException {
		return TypeUtils.toDominoFriendly(value, session, recycleThis);
	}

	/**
	 *
	 * @param values
	 *            the values
	 * @param context
	 *
	 * @param recycleThis
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected static java.util.Vector<Object> toDominoFriendly(final Collection<?> values, final Session session,
			final Collection<lotus.domino.Base> recycleThis) throws IllegalArgumentException {
		return TypeUtils.toDominoFriendly(values, session, recycleThis);
		/*java.util.Vector<Object> result = new java.util.Vector<Object>();
		for (Object value : values) {
			result.add(toDominoFriendly(value, session, recycleThis));
		}
		return result;*/
	}

	/**
	 * Recycle.
	 *
	 * @param base
	 *            the base
	 * @return true, if successful
	 */
	protected static boolean s_recycle(final lotus.domino.Base base) {
		if (base == null || base instanceof org.openntf.domino.Base) {
			return false;// wrappers and null objects are not recycled!
		}
		boolean result = false;
		lotus.domino.DateTime sdtAux = null;
		lotus.domino.DateTime edtAux = null;
		//if (!isLocked(base)) {
		try {
			if (base instanceof lotus.domino.local.DateRange) {//NTF - check to see if we have valid start/end dates to prevent crashes in 9.0.1
				lotus.domino.local.DateRange dr = (lotus.domino.local.DateRange) base;
				if (dr.getStartDateTime() == null || dr.getEndDateTime() == null) {
					lotus.domino.Session rawsession = toLotus(Base.getSession(base));
					sdtAux = rawsession.createDateTime("2001/01/01");
					edtAux = rawsession.createDateTime("2001/02/02");
					dr.setStartDateTime(sdtAux);
					dr.setEndDateTime(edtAux);
				}
			}
			base.recycle();
			result = true;
		} catch (Throwable t) {
			Factory.countRecycleError(base.getClass());
			DominoUtils.handleException(t);
			// shikata ga nai
		} finally {
			try {
				if (sdtAux != null) {
					sdtAux.recycle();
				}
				if (edtAux != null) {
					edtAux.recycle();
				}
			} catch (NotesException ne) {// Now it's enough
			}
		}
		//} else {
		//	System.out.println("Not recycling a " + base.getClass().getName() + " because it's locked.");
		//}
		return result;
	}

	/**
	 * recycle ALL native objects
	 *
	 * @param o
	 *            the object(s) to recycle
	 */
	protected static void s_recycle(final Object o) {
		// NTF this is for recycling of encapsulated objects like DateTime and Name
		// RPr ' do we need an extra method here?
		if (o instanceof Collection) {
			Collection<?> c = (Collection<?>) o;
			if (!c.isEmpty()) {
				for (Object io : c) {
					if (io.getClass().getName().contains("domino")) {
						s_recycle((lotus.domino.Base) io);
					}
				}
			}
		} else if (o instanceof lotus.domino.Base) {
			s_recycle((lotus.domino.Base) o);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
	@Override
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
	private transient Map<EnumEvent, List<IDominoListener>> listenerCache_;

	// these are important to link deferred against, so that they aren't recycled.
	// defered objects that have the same delegate build a circle, so that they aren't gc-ed as
	// long as someone is holding a reference to at least one object of this circle.

	protected Base<?, ?, ?> siblingWrapper_;

	// The origin of the deferreds
	// RPR: Currently, if you use multiple deferreds pointing to the same document, you MAY
	// get unexpected results, because each Document caches different things.
	// Normally, each document should route every method call to its origin!
	protected T originOfDeferred_;

	@Override
	public final boolean hasListeners() {
		if (originOfDeferred_ != null) {
			return originOfDeferred_.hasListeners();
		}

		return listeners_ != null && !listeners_.isEmpty();
	}

	@Override
	public final List<IDominoListener> getListeners() {
		if (originOfDeferred_ != null) {
			return originOfDeferred_.getListeners();
		}

		if (listeners_ == null) {
			listeners_ = new ArrayList<IDominoListener>();
		}
		return listeners_;
	}

	@Override
	public final void addListener(final IDominoListener listener) {
		if (originOfDeferred_ != null) {
			originOfDeferred_.addListener(listener);
		} else {
			listenerCache_ = null;
			getListeners().add(listener);
		}
	}

	@Override
	public final void removeListener(final IDominoListener listener) {
		if (originOfDeferred_ != null) {
			originOfDeferred_.removeListener(listener);
		} else {
			listenerCache_ = null;
			getListeners().remove(listener);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final List<IDominoListener> getListeners(final EnumEvent event) {
		if (originOfDeferred_ != null) {
			return originOfDeferred_.getListeners(event);
		}

		if (!hasListeners()) {
			return Collections.EMPTY_LIST;
		}

		if (listenerCache_ == null) {
			listenerCache_ = new FastMap<EnumEvent, List<IDominoListener>>();
		}

		List<IDominoListener> result = listenerCache_.get(event);
		if (result == null) {
			result = new ArrayList<IDominoListener>();
			for (IDominoListener listener : getListeners()) {
				for (EnumEvent curEvent : listener.getEventTypes()) {
					if (curEvent.equals(event)) {
						result.add(listener);
						break;
					}
				}
			}
			listenerCache_.put(event, result);
		}
		return result;
	}

	@Override
	public final boolean fireListener(final IDominoEvent event) {
		if (originOfDeferred_ != null) {
			return originOfDeferred_.fireListener(event);
		}

		boolean result = true;
		if (!hasListeners()) {
			return true;
		}
		List<IDominoListener> listeners = getListeners(event.getEvent());
		if (listeners == null || listeners.isEmpty()) {
			return true;
		}
		for (IDominoListener listener : listeners) {
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
		throw new UnimplementedException();
	}

	@Deprecated
	void ClearCppObj() {
		throw new UnimplementedException();
	}

	@Deprecated
	Object getWeak() {
		throw new UnimplementedException();
	}

	@Deprecated
	lotus.domino.Session getSession() {
		throw new UnimplementedException();
	}

	@Deprecated
	Object getGCParent() {
		throw new UnimplementedException();
	}

	@Deprecated
	boolean isInvalid() {
		throw new UnimplementedException();
	}

	void restoreObject(final lotus.domino.Session paramSession, final long paramLong) {
		throw new UnimplementedException();
	}

	@Deprecated
	void CheckObject() {
		throw new UnimplementedException();
	}

	@Deprecated
	void CheckObjectActive() {
		throw new UnimplementedException();
	}

	@Override
	public boolean isDead() {
		//		return isDead(delegate_);
		return isDead(getDelegate_unchecked(true));
	}

	@Deprecated
	void CheckArg(final Object paramObject) {
		throw new UnimplementedException();
	}

	@Deprecated
	boolean isEqual(final long paramLong) {
		throw new UnimplementedException();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	Vector PropGetVector(final int paramInt) {
		throw new UnimplementedException();
	}

	@Deprecated
	void validateObjArg(final Object paramObject, final boolean paramBoolean) {
		throw new UnimplementedException();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	Vector getStringArrayProperty(final int paramInt) {
		throw new UnimplementedException();
	}

	private static final int EXTERNALVERSIONUID = 20141205;// The current date (when it was implemented)

	protected void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(EXTERNALVERSIONUID);
		out.writeObject(parent);
		// factory is final and unchangeable
		// I also do not know if serializing listeners will make sense, so I don't do it
	}

	@SuppressWarnings("unchecked")
	protected void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int version = in.readInt();

		if (version != EXTERNALVERSIONUID) {
			throw new InvalidClassException("Cannot read dataversion " + version);
		}
		parent = (P) in.readObject();

	}

	protected void readResolveCheck(final Object expected, final Object given) {
		if (expected == null ? given != null : !expected.equals(given)) {
			log_.warning("Deserializing different " + getClass().getSimpleName() + ". Given: " + given + ", expected: " + expected);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clsid;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Base)) {
			return false;
		}
		Base<?, ?, ?> other = (Base<?, ?, ?>) obj;
		if (clsid != other.clsid) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

	protected void setNewSibling(final org.openntf.domino.impl.Base<?, ?, ?> oldWrapper) {
		listeners_ = oldWrapper.listeners_;
	}

	@SuppressWarnings("unchecked")
	protected void linkToExisting(final Base<?, ?, ?> oldWrapper) {
		// link the implWrapper into the circle
		if (oldWrapper.siblingWrapper_ == null) {
			oldWrapper.siblingWrapper_ = oldWrapper;
		}

		siblingWrapper_ = oldWrapper.siblingWrapper_;
		oldWrapper.siblingWrapper_ = this;

		// link to origin
		originOfDeferred_ = (T) oldWrapper.originOfDeferred_;

		// if the oldWrapper has no origin, it IS the origin
		if (originOfDeferred_ == null) {
			originOfDeferred_ = (T) oldWrapper;
		}

	}

}
