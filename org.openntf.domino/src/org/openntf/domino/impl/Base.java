/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
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

import org.openntf.domino.WrapperFactory;
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

	// nProp values
	static final int CNOTES_NPROP_ISSIGNED = 1140;
	static final int CNOTES_NPROP_LASTMOD = 1141;
	static final int CNOTES_NPROP_LASTACCESS = 1142;
	static final int CNOTES_NPROP_CREATED = 1143;
	static final int CNOTES_NPROP_ISRESPONSE = 1144;
	static final int CNOTES_NPROP_FTSCORE = 1145;
	static final int CNOTES_NPROP_ISNEWNOTE = 1146;
	static final int CNOTES_NPROP_AUTHORS = 1147;
	static final int CNOTES_NPROP_NOTEID = 1148;
	static final int CNOTES_NPROP_UNID = 1149;
	static final int CNOTES_NPROP_ITEMS = 1150;
	static final int CNOTES_NPROP_HASATT = 1151;
	static final int CNOTES_NPROP_PARENTDB = 1152;
	static final int CNOTES_NPROP_PARENTVIEW = 1153;
	static final int CNOTES_NPROP_PARENTUNID = 1154;
	static final int CNOTES_NPROP_MENCRYPT = 1155;
	static final int CNOTES_NPROP_MSIGN = 1156;
	static final int CNOTES_NPROP_MSAVEMSG = 1157;
	static final int CNOTES_NPROP_SIGNER = 1158;
	static final int CNOTES_NPROP_VERIFIER = 1159;
	static final int CNOTES_NPROP_RESPONSES = 1160;
	static final int CNOTES_NPROP_ENCRYPTKEYS = 1161;
	static final int CNOTES_NPROP_SENTBYAGENT = 1162;
	static final int CNOTES_NPROP_SIZE = 1163;
	static final int CNOTES_NPROP_COLVALUES = 1164;
	static final int CNOTES_NPROP_INITIALLYMODIFIED = 1254;
	static final int CNOTES_NPROP_EMBEDDED = 2950;
	static final int CNOTES_NPROP_UIDOCOPEN = 2951;
	static final int CNOTES_NPROP_ISPROFILE = 2952;
	static final int CNOTES_NPROP_PROFNAME = 2953;
	static final int CNOTES_NPROP_PROFUNAME = 2954;
	static final int CNOTES_NPROP_FOLDERREFS = 2957;
	static final int CNOTES_NPROP_ISDELETED = 2958;
	static final int CNOTES_NPROP_ISVALID = 2959;
	static final int CNOTES_NPROP_PREFERJAVADATES = 2963;
	static final int CNOTES_NPROP_LOCKHOLDERS = 3830;
	static final int CNOTES_NPROP_ISENCRYPTED = 3834;

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

	/** The is invalid method. */
	private static Method isInvalidMethod;

	/** The wrapperFactory we are from **/
	private final WrapperFactory factory_;

	/** The delegate_. */
	protected D delegate_; // NTF final??? RPr: Final not possible, otherwise ressurect won't work

	/** The parent_. */
	@SuppressWarnings("rawtypes")
	private final org.openntf.domino.Base parent_;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					Class<?> lotusClass = lotus.domino.local.NotesBase.class;
					getCppObjMethod = lotusClass.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppObjMethod.setAccessible(true);
					isInvalidMethod = lotusClass.getDeclaredMethod("isInvalid", (Class<?>[]) null);
					isInvalidMethod.setAccessible(true);
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
	public long GetCppObj() {
		return cpp_object;
	}

	/**
	 * returns the cpp-session id. Needed for some BackendBridge functions
	 * 
	 * @return the cpp_id of the session
	 */
	public long GetCppSession() {
		return cpp_session;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	org.openntf.domino.Base<?> getParent() {
		return parent_;
	}

	// TODO NTF - not sure about maintaining a set pointer to children. Not using for now. Just setting up (no pun intended)
	/** The children_. */
	// private final Set<org.openntf.domino.Base<?>> children_ = Collections
	// .newSetFromMap(new WeakHashMap<org.openntf.domino.Base<?>, Boolean>());

	/**
	 * /** Use constructor with ClassID in future
	 * 
	 * @param delegate
	 * @param parent
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	protected Base(final D delegate, final org.openntf.domino.Base parent) {
		this(delegate, parent, Factory.getWrapperFactory(), 0L, 0);
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
	protected Base(final D delegate, final org.openntf.domino.Base parent, final WrapperFactory wf, final long cppId, final int classId) {
		// final, these will never change
		parent_ = parent;
		clsid = classId;
		factory_ = wf;

		if (delegate instanceof lotus.domino.local.NotesBase) {
			setDelegate(delegate, cppId);
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

	void setDelegate(final D delegate) {
		setDelegate(delegate, 0);
	}

	/**
	 * Sets the delegate on init or if resurrect occured
	 * 
	 * @param delegate
	 */
	void setDelegate(final D delegate, final long cppId) {
		delegate_ = delegate;
		if (cppId != 0) {
			cpp_object = cppId;
		} else {
			cpp_object = getLotusId(delegate);
		}
	}

	/**
	 * Gets the lotus id.
	 * 
	 * @param base
	 *            the base
	 * @return the lotus id
	 */
	public static long getLotusId(final lotus.domino.Base base) {
		try {
			return ((Long) getCppObjMethod.invoke(base, (Object[]) null)).longValue();
		} catch (Exception e) {
			return 0L;
		}
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
	 * This method recycles the delegate (and counts it as manual recycle)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	@Override
	public void recycle() {
		if (isInvalid(delegate_))
			return;
		s_recycle(delegate_); // RPr: we must recycle the delegate, not "this". Do not call getDelegate as it may reinstantiate it
		Factory.countManualRecycle();
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
	// unwrap objects
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

	@Deprecated
	protected static Object toDominoFriendly(final Object value, final Base<?, ?> context) throws IllegalArgumentException {
		return toDominoFriendly(value, context, null);
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
		// TODO RPr: fill recycleThis
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

		// First, go over the normal data types
		if (value instanceof org.openntf.domino.Base) {
			// this is a wrapper
			lotus.domino.Base ret = toLotus((org.openntf.domino.Base) value);
			if (value instanceof Encapsulated && recycleThis != null) {
				recycleThis.add(ret);
			}
			return ret;
		} else if (value instanceof lotus.domino.Base) {
			return value;
		}

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
		}

		// Now for the illegal-but-convertible types
		if (value instanceof Number) {
			// TODO Check if this is greater than what Domino can handle and serialize if so
			return ((Number) value).doubleValue();
		} else if (value instanceof java.util.Date) {
			lotus.domino.Session lsess = ((Session) Factory.getSession(context)).getDelegate();
			try {
				lotus.domino.DateTime dt = lsess.createDateTime((java.util.Date) value);
				if (recycleThis != null) {
					recycleThis.add(dt);
				}
				return dt;
			} catch (Throwable t) {
				DominoUtils.handleException(t);
				return null;
			}
			// return toLotus(Factory.getSession(context).createDateTime((java.util.Date) value));
		} else if (value instanceof java.util.Calendar) {
			lotus.domino.Session lsess = ((Session) Factory.getSession(context)).getDelegate();
			try {
				lotus.domino.DateTime dt = lsess.createDateTime((java.util.Calendar) value);
				if (recycleThis != null) {
					recycleThis.add(dt);
				}
				return dt;
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
	@SuppressWarnings("rawtypes")
	@Deprecated
	protected static java.util.Vector<Object> toDominoFriendly(final Collection<?> values, final Base<?, ?> context)
			throws IllegalArgumentException {
		java.util.Vector<Object> result = new java.util.Vector<Object>();
		for (Object value : values) {
			result.add(toDominoFriendly(value, context));
		}
		return result;
	}

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
	public static boolean s_recycle(final lotus.domino.local.NotesBase base) {
		if (base == null || base instanceof org.openntf.domino.Base) {
			return false; // wrappers and null objects are not recycled!
		}
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

	/**
	 * recycle encapsulated objects
	 * 
	 * @param o
	 *            the objects to recycle (only encapsulated are recycled)
	 */
	public static void enc_recycle(final Object o) {
		// NTF this is for recycling of encapsulated objects like DateTime and Name
		// RPr ' do we need an extra method here?
		if (o instanceof Collection) {
			Collection<?> c = (Collection<?>) o;
			if (!c.isEmpty()) {
				for (Object io : c) {
					if (io instanceof lotus.domino.DateTime || io instanceof lotus.domino.DateRange || io instanceof lotus.domino.Name) {
						s_recycle((lotus.domino.Base) io);
					}
				}
			}
		} else if (o instanceof lotus.domino.DateTime || o instanceof lotus.domino.DateRange || o instanceof lotus.domino.Name) {
			s_recycle((lotus.domino.Base) o);
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

}
