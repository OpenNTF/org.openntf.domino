package org.openntf.domino.utils;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;
import org.openntf.domino.impl.Base;
import org.openntf.domino.impl.Session;
import org.openntf.domino.thread.DominoReference;
import org.openntf.domino.thread.DominoReferenceQueue;

public enum Factory {
	;

	private static final boolean TRACE_COUNTERS = true;

	static class Counter extends ThreadLocal<Integer> {
		// TODO NTF - I'm open to a faster implementation of this. Maybe a mutable int of some kind?
		@Override
		protected Integer initialValue() {
			return Integer.valueOf(0);
		}

		public void increment() {
			set(get() + 1);
		}

		public void decrement() {
			set(get() - 1);
		}
	};

	private static Counter lotusCounter = new Counter();
	private static Counter recycleErrCounter = new Counter();
	private static Counter autoRecycleCounter = new Counter();

	public static int getLotusCount() {
		return lotusCounter.get().intValue();
	}

	public static void countRecycleError() {
		if (TRACE_COUNTERS)
			recycleErrCounter.increment();
	}

	public static void countAutoRecycle() {
		if (TRACE_COUNTERS)
			autoRecycleCounter.increment();
	}

	public static int getAutoRecycleCount() {
		return autoRecycleCounter.get().intValue();
	}

	public static int getRecycleErrorCount() {
		return recycleErrCounter.get().intValue();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base parent) {
		if (T.isAssignableFrom(lotus.getClass())) {
			return (T) lotus;
		}
		if (lotus == null) {
			return null;
		}
		if(lotus instanceof org.openntf.domino.Base) {
			return (T)lotus;
		}
		T result = null;
		if(lotus instanceof lotus.domino.ACL) {
			result = (T) new org.openntf.domino.impl.ACL((lotus.domino.ACL)lotus, parent);
		} else if(lotus instanceof lotus.domino.ACLEntry) {
			result = (T) new org.openntf.domino.impl.ACLEntry((lotus.domino.ACLEntry)lotus, (org.openntf.domino.ACL)parent);
		} else if(lotus instanceof lotus.domino.AdministrationProcess) {
			result = (T) new org.openntf.domino.impl.AdministrationProcess((lotus.domino.AdministrationProcess)lotus, parent);
		} else if(lotus instanceof lotus.domino.Agent) {
			result = (T) new org.openntf.domino.impl.Agent((lotus.domino.Agent)lotus, parent);
		} else if(lotus instanceof lotus.domino.AgentContext) {
			result = (T) new org.openntf.domino.impl.AgentContext((lotus.domino.AgentContext)lotus, parent);
		} else if(lotus instanceof lotus.domino.ColorObject) {
			result = (T) new org.openntf.domino.impl.ColorObject((lotus.domino.ColorObject)lotus, parent);
		} else if (lotus instanceof lotus.domino.Database) {
			result = (T) new org.openntf.domino.impl.Database((lotus.domino.Database) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateRange) {
			result = (T) new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateTime) {
			result = (T) new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus, parent);
		} else if(lotus instanceof lotus.domino.DbDirectory) {
			result = (T) new org.openntf.domino.impl.DbDirectory((lotus.domino.DbDirectory)lotus, parent);
		} else if(lotus instanceof lotus.domino.Directory) {
			result = (T) new org.openntf.domino.impl.Directory((lotus.domino.Directory)lotus, parent);
		} else if(lotus instanceof lotus.domino.DirectoryNavigator) {
			result = (T) new org.openntf.domino.impl.DirectoryNavigator((lotus.domino.DirectoryNavigator)lotus, parent);
		} else if (lotus instanceof lotus.domino.Document) {
			result = (T) new org.openntf.domino.impl.Document((lotus.domino.Document) lotus, parent);
		} else if (lotus instanceof lotus.domino.DocumentCollection) {
			result = (T) new org.openntf.domino.impl.DocumentCollection((lotus.domino.DocumentCollection) lotus, parent);
		} else if(lotus instanceof lotus.domino.DxlExporter) {
			result = (T) new org.openntf.domino.impl.DxlExporter((lotus.domino.DxlExporter)lotus, parent);
		} else if(lotus instanceof lotus.domino.DxlImporter) {
			result = (T) new org.openntf.domino.impl.DxlImporter((lotus.domino.DxlImporter)lotus, parent);
		} else if(lotus instanceof lotus.domino.EmbeddedObject) {
			result = (T) new org.openntf.domino.impl.EmbeddedObject((lotus.domino.EmbeddedObject)lotus, parent);
		} else if (lotus instanceof lotus.domino.Form) {
			result = (T) new org.openntf.domino.impl.Form((lotus.domino.Form) lotus, parent);
		} else if(lotus instanceof lotus.domino.Item) {
			result = (T) new org.openntf.domino.impl.Item((lotus.domino.Item)lotus, parent);
		} else if(lotus instanceof lotus.domino.MIMEEntity) {
			result = (T) new org.openntf.domino.impl.MIMEEntity((lotus.domino.MIMEEntity)lotus, parent);
		} else if(lotus instanceof lotus.domino.MIMEHeader) {
			result = (T) new org.openntf.domino.impl.MIMEHeader((lotus.domino.MIMEHeader)lotus, parent);
		} else if (lotus instanceof lotus.domino.Name) {
			result = (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus, parent);
		} else if(lotus instanceof lotus.domino.NoteCollection) {
			result = (T) new org.openntf.domino.impl.NoteCollection((lotus.domino.NoteCollection)lotus, (org.openntf.domino.Database)parent);
		} else if(lotus instanceof lotus.domino.Outline) {
			result = (T) new org.openntf.domino.impl.Outline((lotus.domino.Outline)lotus, parent);
		} else if(lotus instanceof lotus.domino.OutlineEntry) {
			result = (T) new org.openntf.domino.impl.OutlineEntry((lotus.domino.OutlineEntry)lotus, parent);
		} else if(lotus instanceof lotus.domino.Replication) {
			result = (T) new org.openntf.domino.impl.Replication((lotus.domino.Replication)lotus, parent);
		} else if(lotus instanceof lotus.domino.ReplicationEntry) {
			result = (T) new org.openntf.domino.impl.ReplicationEntry((lotus.domino.ReplicationEntry)lotus, parent);
		} else if (lotus instanceof lotus.domino.Session) {
			result = (T) new org.openntf.domino.impl.Session((lotus.domino.Session) lotus, parent);
		} else if(lotus instanceof lotus.domino.Stream) {
			result = (T) new org.openntf.domino.impl.Stream((lotus.domino.Stream)lotus, parent);
		} else if(lotus instanceof lotus.domino.View) {
			result = (T) new org.openntf.domino.impl.View((lotus.domino.View)lotus, (org.openntf.domino.Database)parent);
		} else if(lotus instanceof lotus.domino.ViewEntryCollection) {
			result = (T) new org.openntf.domino.impl.ViewEntryCollection((lotus.domino.ViewEntryCollection)lotus, (org.openntf.domino.View)parent);
		}
		drainQueue();
		if (result != null) {
			if (TRACE_COUNTERS)
				lotusCounter.increment();
			return result;
		}
		throw new UndefinedDelegateTypeException();
	}

	private static void drainQueue() {
		DominoReferenceQueue drq = Base._getRecycleQueue();
		Reference<?> ref = drq.poll();

		while (ref != null) {
			if (ref instanceof DominoReference) {
				((DominoReference) ref).recycle();
				if (TRACE_COUNTERS)
					lotusCounter.decrement();
			}
			ref = drq.poll();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Collection<T> fromLotus(Collection<?> lotusColl, Class<? extends org.openntf.domino.Base> T,
			org.openntf.domino.Base<?> parent) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> org.openntf.domino.impl.Vector<T> fromLotusAsVector(Collection<?> lotusColl,
			Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base<?> parent) {
		org.openntf.domino.impl.Vector<T> result = new org.openntf.domino.impl.Vector<T>(); // TODO anyone got a better implementation?
		System.out.println("START creation new Vector from lotus objects...");
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.local.NotesBase) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		System.out.println("END creation new Vector from lotus objects...");
		return result;

	}
	
	public static java.util.Vector<Object> wrapColumnValues(Collection<?> values) {
		java.util.Vector<Object> result = new org.openntf.domino.impl.Vector<Object>();
		for(Object value : values) {
			if(value instanceof lotus.domino.DateTime) {
				result.add(fromLotus((lotus.domino.DateTime)value, org.openntf.domino.impl.DateTime.class, getSession()));
			} else if(value instanceof lotus.domino.DateRange) {
				result.add(fromLotus((lotus.domino.DateRange)value, org.openntf.domino.impl.DateRange.class, getSession()));
			} else if(value instanceof Collection) {
				result.add(wrapColumnValues((Collection<?>)value));
			} else {
				result.add(value);
			}
		}
		return result;
	}

	public static org.openntf.domino.Session getSession() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSession();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public static org.openntf.domino.Database getParentDatabase(org.openntf.domino.Base<?> base) {
		org.openntf.domino.Database result = null;
		if (base instanceof org.openntf.domino.Database) {
			result = (org.openntf.domino.Database) base;
		} else if (base instanceof org.openntf.domino.Document) {
			result = ((org.openntf.domino.Document) base).getParentDatabase();
		} else if (base instanceof org.openntf.domino.DocumentCollection) {
			result = (org.openntf.domino.Database) ((org.openntf.domino.DocumentCollection) base).getParent();
		} else if (base instanceof org.openntf.domino.View) {
			result = (org.openntf.domino.Database) ((org.openntf.domino.View) base).getParent();
		} else if (base instanceof org.openntf.domino.Form) {
			result = ((org.openntf.domino.Form) base).getParent();
		} else {
			throw new UndefinedDelegateTypeException();
		}
		return result;
	}

	public static org.openntf.domino.Session getSession(org.openntf.domino.Base<?> base) {
		org.openntf.domino.Session result = null;
		if (base instanceof org.openntf.domino.Session) {
			result = (org.openntf.domino.Session) base;
		} else if (base instanceof org.openntf.domino.Database) {
			result = ((org.openntf.domino.Database) base).getParent();
		} else if (base instanceof org.openntf.domino.Document) {
			result = ((org.openntf.domino.Document) base).getParentDatabase().getParent();
		} else if (base instanceof org.openntf.domino.DocumentCollection) {
			result = (org.openntf.domino.Session) ((org.openntf.domino.DocumentCollection) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.View) {
			result = (org.openntf.domino.Session) ((org.openntf.domino.View) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.DateTime) {
			result = ((org.openntf.domino.DateTime) base).getParent();
		} else if (base instanceof org.openntf.domino.DateRange) {
			result = ((org.openntf.domino.DateRange) base).getParent();
		} else if (base instanceof org.openntf.domino.Form) {
			result = ((org.openntf.domino.Form) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.Name) {
			result = ((org.openntf.domino.Name) base).getParent();
		} else {
			throw new UndefinedDelegateTypeException();
		}
		if (result == null)
			result = Session.getDefaultSession(); // last ditch, get the primary Session;
		return result;
	}

}