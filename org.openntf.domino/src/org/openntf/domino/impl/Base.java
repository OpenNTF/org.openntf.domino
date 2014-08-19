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

import lotus.domino.NotesException;

import org.openntf.domino.WrapperFactory;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.ext.Formula;
import org.openntf.domino.napi.NapiFactory;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.NotImplementedException;

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
	private static Method checkArgMethod;
	private static Method checkObjectMethod;
	private static Method checkObjectActiveMethod;
	private static Method clearCppObjMethod;
	private static Method getCppSessionMethod;
	private static Method getGCParentMethod;
	private static Method getSessionMethod;
	//	private static Method getStringArrayPropertyMethod;
	private static Method getWeakMethod;
	private static Method isDeadMethod;
	private static Method isInvalidMethod;
	private static Method isEqualMethod;
	private static Method markInvalidMethod;
	// private static Method notImplementedMethod;
	private static Method validateObjArgMethod;
	// private static Method restoreObjectMethod;

	/** the class id of this object type (implemented as precaution) **/
	final int clsid;
	/** The wrapperFactory we are from **/
	private final WrapperFactory factory_;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					checkArgMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckArg", Object.class);
					checkArgMethod.setAccessible(true);
					checkObjectMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckObject", (Class<?>[]) null);
					checkObjectMethod.setAccessible(true);
					checkObjectActiveMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("CheckObjectActive", (Class<?>[]) null);
					checkObjectActiveMethod.setAccessible(true);
					clearCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("ClearCppObj", (Class<?>[]) null);
					clearCppObjMethod.setAccessible(true);
					getCppObjMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppObjMethod.setAccessible(true);
					getCppSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppSession", (Class<?>[]) null);
					getCppSessionMethod.setAccessible(true);
					getGCParentMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getGCParent", (Class<?>[]) null);
					getGCParentMethod.setAccessible(true);
					getSessionMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getSession", (Class<?>[]) null);
					getSessionMethod.setAccessible(true);
					getWeakMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("getWeak", (Class<?>[]) null);
					getWeakMethod.setAccessible(true);
					isDeadMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isDead", (Class<?>[]) null);
					isDeadMethod.setAccessible(true);
					isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
					isInvalidMethod.setAccessible(true);
					isEqualMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isEqual", Long.TYPE);
					isEqualMethod.setAccessible(true);
					markInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("markInvalid", (Class<?>[]) null);
					markInvalidMethod.setAccessible(true);
					validateObjArgMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("validateObjArg", Object.class,
							Boolean.TYPE);
					validateObjArgMethod.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}

	}

	/** The delegate_. */
	protected transient D delegate_ = null;

	/** The CPP-Object ID */
	private transient long cpp_object;

	/** The CPP-Object ID of the session */
	private transient long cpp_session;

	/**
	 * returns the cpp_id. DO NOT REMOVE. Otherwise native funtions won't work
	 * 
	 * @return the cpp_id
	 */
	public long GetCppObj() {
		return cpp_object;
	}

	/** The parent_. */
	protected final P parent_;

	/**
	 * returns the cpp-session id. Needed for some BackendBridge functions
	 * 
	 * @return the cpp_id of the session
	 */
	public long GetCppSession() {
		return cpp_session;
	}

	//	@SuppressWarnings("rawtypes")
	//	void setParent(final P parent) {
	//		parent_ = parent;
	//		if (parent instanceof org.openntf.domino.impl.Base) {
	//			cpp_session = ((org.openntf.domino.impl.Base) parent).GetCppSession();
	//		}
	//	}

	/**
	 * Find the parent if no one was specified
	 * 
	 * @param delegate
	 * @return
	 */
	protected P findParent(final D delegate) throws NotesException {
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

	/** The NAPI-Factory to speed up some things */
	private static NapiFactory napiFactory_ = null;

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	//org.openntf.domino.Base<?> getParent() {
	//	return parent_;
	//}

	protected P getAncestor() {
		return parent_;
	}

	// TODO NTF - not sure about maintaining a set pointer to children. Not using for now. Just setting up (no pun intended)
	/** The children_. */
	// private final Set<org.openntf.domino.Base<?>> children_ = Collections
	// .newSetFromMap(new WeakHashMap<org.openntf.domino.Base<?>, Boolean>());

	/**
	 * @deprecated Use constructor with ClassID in future
	 * 
	 * @param delegate
	 * @param parent
	 */
	@Deprecated
	protected Base(final D delegate, final P parent) {
		this(delegate, parent, null, 0L, 0);
	}

	/**
	 * Instantiates a new base.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent (may be null)
	 * @param wf
	 *            the wrapperFactory
	 * @param cppId
	 *            the cpp-id
	 * @param classId
	 *            the class id
	 */
	@SuppressWarnings("rawtypes")
	protected Base(final D delegate, P parent, final WrapperFactory wf, final long cppId, final int classId) {
		if (wf == null) {
			factory_ = Factory.getWrapperFactory();
		} else {
			factory_ = wf;
		}

		if (parent == null) {
			try {
				parent = findParent(delegate);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}

		// final, these will never change
		parent_ = parent;
		clsid = classId;

		if (delegate instanceof lotus.domino.local.NotesBase) {
			setDelegate(delegate, cppId);
		} else if (delegate != null) {
			// normally you won't get here if you come from fromLotus
			throw new IllegalArgumentException("Why are you wrapping a non-Lotus object? " + delegate.getClass().getName());
		}

		// copy the cpp_session from the parent
		if (delegate instanceof lotus.domino.Session) {
			cpp_session = cpp_object;
		} else if (parent_ instanceof Base) {
			cpp_session = ((Base) parent_).GetCppSession();
		} else {
			cpp_session = 0;
		}
	}

	protected Base(final P parent, final WrapperFactory wf, final int classId) {
		if (wf == null) {
			factory_ = Factory.getWrapperFactory();
		} else {
			factory_ = wf;
		}
		parent_ = parent;
		clsid = classId;
		cpp_session = 0;
	}

	/**
	 * Sets the delegate on init or if resurrect occured
	 * 
	 * @param delegate
	 *            the delegate
	 */
	void setDelegate(final D delegate) {
		setDelegate(delegate, 0);
	}

	/**
	 * Sets the delegate on init or if resurrect occured
	 * 
	 * @param delegate
	 *            the delegate
	 * @param cppId
	 *            the cpp-id
	 */
	void setDelegate(final D delegate, final long cppId) {
		delegate_ = delegate;
		if (cppId != 0) {
			cpp_object = cppId;
		} else {
			cpp_object = getLotusId(delegate);
		}
	}

	//	void setDelegate(final D delegate, final long cppId, final boolean recache) {
	//		setDelegate(delegate, cppId);
	//		if (recache) {
	//			Factory.recacheLotus(delegate, this, parent_);
	//		}
	//	}

	/**
	 * Gets the lotus id.
	 * 
	 * @param base
	 *            the base
	 * @return the lotus id
	 */
	public static long getLotusId(final lotus.domino.Base base) {
		try {
			if (base instanceof lotus.domino.local.NotesBase) {
				return ((Long) getCppObjMethod.invoke(base, (Object[]) null)).longValue();
			} else if (base instanceof org.openntf.domino.impl.Base) {
				return ((org.openntf.domino.impl.Base<?, ?, ?>) base).GetCppObj();
			}
		} catch (Exception e) {
		}
		return 0L;
	}

	/**
	 * Checks if the lotus object is invalid. A object is invalid if it is recycled by the java side.
	 * 
	 * i.E.: Some Java code has called base.recycle();
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
		if (base == null)
			return true;
		try {
			return ((Boolean) isDeadMethod.invoke(base, (Object[]) null)).booleanValue();
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
		if (base == null)
			return null;
		try {
			return ((lotus.domino.Session) getSessionMethod.invoke(base, (Object[]) null));
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
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
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	protected D getDelegate() {
		return delegate_;
	}

	// wrap objects. Delegate this to the wrapperFactory
	/**
	 * Wraps objects. Delegate to WrapperFactory
	 * 
	 * @see org.openntf.domino.WrapperFactory#fromLotus(lotus.domino.Base, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	public <T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> T1 fromLotus(
			final D1 lotus, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return factory_.fromLotus(lotus, schema, parent);
	}

	/**
	 * Wraps a collection. Delegate to WrapperFactory
	 * 
	 * @see org.openntf.domino.WrapperFactory#fromLotus(Collection, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	<T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> Collection<T1> fromLotus(
			final Collection<?> lotusColl, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return factory_.fromLotus(lotusColl, schema, parent);
	}

	/**
	 * Wraps a collection and returns it as vector. Delegate to WrapperFactory
	 * 
	 * @see org.openntf.domino.WrapperFactory#fromLotusAsVector(Collection, FactorySchema, org.openntf.domino.Base)
	 */
	@SuppressWarnings({ "rawtypes" })
	<T1 extends org.openntf.domino.Base, D1 extends lotus.domino.Base, P1 extends org.openntf.domino.Base> Vector<T1> fromLotusAsVector(
			final Collection<?> lotusColl, final FactorySchema<T1, D1, P1> schema, final P1 parent) {
		return factory_.fromLotusAsVector(lotusColl, schema, parent);
	}

	/**
	 * Wraps column values
	 * 
	 * @see org.openntf.domino.WrapperFactory#wrapColumnValues(Collection, org.openntf.domino.Session)
	 */
	Vector<Object> wrapColumnValues(final Collection<?> values, final org.openntf.domino.Session session) {
		return factory_.wrapColumnValues(values, session);
	}

	/**
	 * returns the WrapperFactory
	 * 
	 * @return
	 */
	protected WrapperFactory getFactory() {
		return factory_;
	}

	/**
	 * Checks if is encapsulated.
	 * 
	 * @return true, if is encapsulated
	 */
	@Deprecated
	public boolean isEncapsulated() {
		return (this instanceof Encapsulated);
	}

	/**
	 * Checks if is locked.
	 * 
	 * @deprecated Do not use objects acros threads
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
	 * @deprecated Do not use objects acros threads
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
	 * @deprecated Do not use objects acros threads
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
	 * @deprecated Do not use objects acros threads
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
	 * @deprecated Do not use objects acros threads
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
	 * This method recycles the delegate (and counts it as manual recycle)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	@Override
	public void recycle() {
		if (isDead(delegate_))
			return;
		s_recycle(delegate_); // RPr: we must recycle the delegate, not "this". Do not call getDelegate as it may reinstantiate it
		Factory.countManualRecycle(delegate_.getClass());
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
	public static <T extends lotus.domino.Base> T toLotus(final T wrapper, final Collection recycleThis) {
		if (wrapper instanceof org.openntf.domino.impl.Base) {
			lotus.domino.Base ret = ((org.openntf.domino.impl.Base) wrapper).getDelegate();
			if (wrapper instanceof Encapsulated && recycleThis != null) {
				recycleThis.add(ret);
			}
			return (T) ret;
		}
		return wrapper;
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
	public static <T extends lotus.domino.Base> T toLotus(final T wrapper) {
		if (wrapper instanceof org.openntf.domino.impl.Base) {
			return (T) ((org.openntf.domino.impl.Base) wrapper).getDelegate();
		}
		return wrapper;
	}

	/**
	 * To lotus.
	 * 
	 * @param baseObj
	 *            the base obj
	 * @return the lotus.domino. base version or the object itself, as appropriate
	 */
	@SuppressWarnings("rawtypes")
	public static Object toLotus(final Object baseObj) {
		if (baseObj instanceof org.openntf.domino.impl.Base) {
			return ((org.openntf.domino.impl.Base) baseObj).getDelegate();
		}
		return baseObj;
	}

	/**
	 * Unwraps anything to a dominofriendly object
	 * 
	 * @deprecated use {@link #toDominoFriendly(Object, org.openntf.domino.Base, Collection)} instead
	 * @param value
	 * @param context
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Deprecated
	protected static Object toDominoFriendly(final Object value, final org.openntf.domino.Base<?> context) throws IllegalArgumentException {
		return toDominoFriendly(value, context, null);
	}

	/**
	 * toItemFriendly: special case for "toDominoFriendly" that handles "DateTime" / "DateRange" correctly
	 * 
	 * @param value
	 *            The Object value to coerce into an Item-friendly type.
	 * @param context
	 *            The context object.
	 * @param recycleThis
	 * @return An object value that can be stored in an Item.
	 * @throws IllegalArgumentException
	 *             When the provided value cannot be successfully converted into an Item-safe value.
	 */
	public static Object toItemFriendly(final Object value, final org.openntf.domino.Base<?> context,
			final Collection<lotus.domino.Base> recycleThis) throws IllegalArgumentException {
		if (value == null) {
			log_.log(Level.INFO, "Trying to convert a null argument to Domino friendly. Returning null...");
			return null;
		}

		if (value instanceof lotus.domino.Base) {
			if (value instanceof lotus.domino.Name) {
				// Names are written as canonical
				try {
					return ((lotus.domino.Name) value).getCanonical();
				} catch (NotesException e) {
					DominoUtils.handleException(e);
				}
			} else if (value instanceof org.openntf.formula.DateTime) {
				return javaToDominoFriendly(value, context, recycleThis);
			} else if (value instanceof org.openntf.domino.DateTime || value instanceof org.openntf.domino.DateRange) {
				// according to documentation, these datatypes should be compatible to write to a field ... but DateRanges make problems
				return toLotus((org.openntf.domino.Base<?>) value, recycleThis);
			} else if (value instanceof lotus.domino.DateTime || value instanceof lotus.domino.DateRange) {
				return value;
			}
			throw new IllegalArgumentException("Cannot convert to Domino friendly from type " + value.getClass().getName());
		} else {
			return javaToDominoFriendly(value, context, recycleThis);
		}
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
	protected static Object toDominoFriendly(final Object value, final org.openntf.domino.Base context,
			final Collection<lotus.domino.Base> recycleThis) throws IllegalArgumentException {
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
				result.add(toDominoFriendly(o, context, recycleThis));
			}
			return result;
		}

		if (value instanceof Collection) {
			java.util.Vector<Object> result = new java.util.Vector<Object>();
			Collection<?> coll = (Collection) value;
			for (Object o : coll) {
				result.add(toDominoFriendly(o, context, recycleThis));
			}
			return result;
		}

		if (value instanceof org.openntf.domino.Base) {
			// this is a wrapper
			return toLotus((org.openntf.domino.Base) value, recycleThis);
		} else if (value instanceof lotus.domino.Base) {
			// this is already domino friendly
			return value;
		} else {
			return javaToDominoFriendly(value, context, recycleThis);
		}

	}

	/**
	 * converts a lot of java types to domino-friendly types
	 * 
	 * @param value
	 * @param context
	 * @param recycleThis
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Object javaToDominoFriendly(final Object value, final org.openntf.domino.Base context,
			final Collection<lotus.domino.Base> recycleThis) {

		if (value instanceof Integer || value instanceof Double) {
			return value;
		} else if (value instanceof String) {
			return value;
		} else if (value instanceof Boolean) {
			if ((Boolean) value) {
				return "1";
			} else {
				return "0";
			}
		} else if (value instanceof Character) {
			return value.toString();
		}
		// Now for the illegal-but-convertible types
		if (value instanceof Number) {
			// TODO Check if this is greater than what Domino can handle and serialize if so
			// CHECKME: Is "doubleValue" really needed. (according to help.nsf only Integer and Double is supported, so keep it)
			return ((Number) value).doubleValue();

		} else if (value instanceof java.util.Date || value instanceof java.util.Calendar || value instanceof org.openntf.formula.DateTime) {
			lotus.domino.Session lsess = toLotus(Factory.getSession(context));
			try {

				lotus.domino.DateTime dt = null;
				if (value instanceof java.util.Date) {
					dt = lsess.createDateTime((java.util.Date) value);
				} else if (value instanceof org.openntf.formula.DateTime) {
					dt = lsess.createDateTime(((org.openntf.formula.DateTime) value).toJavaDate());
				} else {
					dt = lsess.createDateTime((java.util.Calendar) value);
				}
				if (recycleThis != null) {
					recycleThis.add(dt);
				}
				return dt;
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Date) value));
		} else if (value instanceof CharSequence) {
			return value.toString();
			//		} else if (value instanceof CaseInsensitiveString) {	// CaseInsensitiveString is a CharSequence
			//			return value.toString();
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
	 * @deprecated use {@link #toDominoFriendly(Collection, org.openntf.domino.Base, Collection)}
	 * @param values
	 *            the values
	 * @param context
	 *            the context
	 * @return the vector
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	@Deprecated
	protected static java.util.Vector<Object> toDominoFriendly(final Collection<?> values, final org.openntf.domino.Base<?> context)
			throws IllegalArgumentException {
		java.util.Vector<Object> result = new java.util.Vector<Object>();
		for (Object value : values) {
			result.add(toDominoFriendly(value, context));
		}
		return result;
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
	@SuppressWarnings("rawtypes")
	protected static java.util.Vector<Object> toDominoFriendly(final Collection<?> values, final org.openntf.domino.Base context,
			final Collection<lotus.domino.Base> recycleThis) throws IllegalArgumentException {
		java.util.Vector<Object> result = new java.util.Vector<Object>();
		for (Object value : values) {
			result.add(toDominoFriendly(value, context, recycleThis));
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
		if (values == null) {
			return null;
		} else {
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
	}

	/**
	 * Recycle.
	 * 
	 * @param base
	 *            the base
	 * @return true, if successful
	 */
	public static boolean s_recycle(final lotus.domino.Base base) {
		if (base == null || base instanceof org.openntf.domino.Base) {
			return false; // wrappers and null objects are not recycled!
		}
		boolean result = false;
		lotus.domino.DateTime sdtAux = null;
		lotus.domino.DateTime edtAux = null;
		//if (!isLocked(base)) {
		try {
			if (base instanceof lotus.domino.local.DateRange) {	//NTF - check to see if we have valid start/end dates to prevent crashes in 9.0.1
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
				if (sdtAux != null)
					sdtAux.recycle();
				if (edtAux != null)
					edtAux.recycle();
			} catch (NotesException ne) {	// Now it's enough
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
	public static void s_recycle(final Object o) {
		// NTF this is for recycling of encapsulated objects like DateTime and Name
		// RPr ' do we need an extra method here?
		if (o instanceof Collection) {
			Collection<?> c = (Collection<?>) o;
			if (!c.isEmpty()) {
				for (Object io : c) {
					s_recycle((lotus.domino.Base) io);
				}
			}
		} else if (o instanceof lotus.domino.Base) {
			s_recycle((lotus.domino.Base) o);
		}

	}

	//	/**
	//	 * recycle encapsulated objects
	//	 * 
	//	 * @param o
	//	 *            the objects to recycle (only encapsulated are recycled)
	//	 */
	//	public static void enc_recycle(final Object o) {
	//		// NTF this is for recycling of encapsulated objects like DateTime and Name
	//		// RPr ' do we need an extra method here?
	//		if (o instanceof Collection) {
	//			Collection<?> c = (Collection<?>) o;
	//			if (!c.isEmpty()) {
	//				for (Object io : c) {
	//					if (io instanceof lotus.domino.DateTime || io instanceof lotus.domino.DateRange || io instanceof lotus.domino.Name) {
	//						s_recycle((lotus.domino.Base) io);
	//					}
	//				}
	//			}
	//		} else if (o instanceof lotus.domino.DateTime || o instanceof lotus.domino.DateRange || o instanceof lotus.domino.Name) {
	//			s_recycle((lotus.domino.Base) o);
	//		}
	//
	//	}

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

	@Override
	public List<IDominoListener> getListeners() {
		if (listeners_ == null) {
			listeners_ = new ArrayList<IDominoListener>();
		}
		return listeners_;
	}

	@Override
	public void addListener(final IDominoListener listener) {
		getListeners().add(listener);
	}

	@Override
	public void removeListener(final IDominoListener listener) {
		getListeners().remove(listener);
	}

	@Override
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

	@Override
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
	lotus.domino.Session getSession() {
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

	void restoreObject(final lotus.domino.Session paramSession, final long paramLong) {
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

	@Override
	public boolean isDead() {
		return isDead(delegate_);
	}

	@Deprecated
	void CheckArg(final Object paramObject) {
		throw new NotImplementedException();
	}

	@Deprecated
	boolean isEqual(final long paramLong) {
		throw new NotImplementedException();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	Vector PropGetVector(final int paramInt) {
		throw new NotImplementedException();
	}

	@Deprecated
	void validateObjArg(final Object paramObject, final boolean paramBoolean) {
		throw new NotImplementedException();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	Vector getStringArrayProperty(final int paramInt) {
		throw new NotImplementedException();
	}

	/**
	 * Set the NAPI Factory if you are running in an XPage-environment.
	 * 
	 * TODO RPr: THIS IS HIGHLY EXPERIMENTAL
	 * 
	 * @param napiFactory
	 *            the NapiFactory Interface
	 */
	public static void setNapiFactory(final NapiFactory napiFactory) {
		napiFactory_ = napiFactory;

	}

	/**
	 * Return the NAPI-Factory
	 * 
	 * @return
	 */
	protected NapiFactory getNapiFactory() {
		return napiFactory_;
	}

}
