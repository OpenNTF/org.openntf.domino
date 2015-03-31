package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.ext.NoteClass;

import com.ibm.commons.util.StringUtil;
import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesCollection;
import com.ibm.designer.domino.napi.NotesCollectionEntry;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.designer.domino.napi.util.NotesIterator;
import com.ibm.domino.napi.c.BackendBridge;

/**
 * Class to speed up design related stuff. It accesses directly the DesignIndex (NoteID: FFFF0020). Unfortunately, NAPI classes are required
 * for this.
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class NapiDatabaseDesign {

	/** The NapiDesignList for the given database */
	private List<NapiEntry> designList;
	private Database database_;
	private boolean init;

	public NapiDatabaseDesign(final Database db) {
		database_ = db;
	}

	/**
	 * An entry in the NapiDesignList
	 * 
	 * @author Roland Praml, FOCONIS AG
	 *
	 */
	public static class NapiEntry {
		public String title;
		public String flags;
		public String flagsExt;
		public int assistType;
		public int noteClass;
		public int noteId;
		public String universalId;

		@Override
		public String toString() {
			return "NapiEntry [title=" + title + ", flags=" + flags + ", flagsExt=" + flagsExt + ", assistType=" + assistType
					+ ", noteClass=" + noteClass + ", noteId=" + noteId + ", universalId=" + universalId + "]";
		}

	}

	public List<NapiEntry> getDesignList() {
		if (!init) {
			init = true;
			try {
				int ndbHandle = (int) BackendBridge.getDatabaseHandleRO(database_);

				NotesSession nsess = new NotesSession();
				designList = new ArrayList<NapiEntry>();
				try {
					NotesDatabase ndb = nsess.getDatabase(ndbHandle);
					try {
						ndb.open();
						// $FormulaClass of designCollection = 3724 of 0xFFFF0020
						// 2048 = 0x0800 = NOTE_CLASS_REPLFORMULA
						// 1024 = 0x0400 = NOTE_CLASS_FIELD
						// 512  = 0x0200 = NOTE_CLASS_FILTER
						// 128  = 0x0080 = NOTE_CLASS_HELP_INDEX
						// 8    = 0x0008 = NOTE_CLASS_VIEW
						// 4    = 0x0004 = NOTE_CLASS_FORM

						// --- Summary ---
						// NOTE_CLASS_DOCUMENT 		0x0001	not required	/* document note */
						// NOTE_CLASS_INFO 			0x0002	= FFFF0002		/* notefile info (help-about) note */
						// NOTE_CLASS_FORM 			0x0004	available		/* form note */
						// NOTE_CLASS_VIEW 			0x0008 	available		/* view note */
						// NOTE_CLASS_ICON 			0x0010 	= FFFF0010		/* icon note */
						// NOTE_CLASS_DESIGN 		0x0020	= FFFF0020		/* design note collection */
						// NOTE_CLASS_ACL 			0x0040 	= FFFF0040		/* acl note */
						// NOTE_CLASS_HELP_INDEX 	0x0080 	available		/* Notes product help index note */
						// NOTE_CLASS_HELP 			0x0100 	= FFFF0100		/* designer's help note */
						// NOTE_CLASS_FILTER 		0x0200 	available		/* filter note */
						// NOTE_CLASS_FIELD 		0x0400  available		/* field note */
						// NOTE_CLASS_REPLFORMULA 	0x0800 	available		/* replication formula */
						// NOTE_CLASS_PRIVATE 		0x1000  not required	/* Private design note, use $PrivateDesign view to locate/classify */

						NotesCollection design = ndb.openCollection(0xFFFF0020, 0);
						try {
							final int returnMask = 0x8007; // 0x8000 = read summary table, 4 = read class, 2 = read unid, 1 = read note id 
							NotesIterator iterator = design.readEntries(returnMask, 0, 32);
							try {
								while (iterator.hasNext()) {
									NotesCollectionEntry entry = (NotesCollectionEntry) iterator.next();
									try {
										NapiEntry nEntry = new NapiEntry();
										try {
											nEntry.assistType = Integer.valueOf(entry.getItemValueAsString("$AssistType"));
										} catch (NumberFormatException nfe) {
										}
										nEntry.flags = entry.getItemValueAsString("$Flags");
										nEntry.flagsExt = entry.getItemValueAsString("$FlagsExt");
										nEntry.title = "|" + entry.getItemValueAsString("$TITLE") + "|";
										nEntry.noteClass = entry.getNoteClass();
										nEntry.noteId = entry.getNoteID();
										nEntry.universalId = entry.getNoteUNID();
										designList.add(nEntry);
										//System.out.println(nEntry);
									} finally {
										entry.recycle();
									}
								}
							} finally {
								iterator.recycle();
							}
						} finally {
							design.recycle();
						}
					} finally {
						ndb.recycle();
					}
				} finally {
					nsess.recycle();
				}
			} catch (NotesAPIException e) {
				if (e.getNativeErrorCode() != 578) { // 578 = nsferr.h/ERR_SPECIAL_ID  "Special database object cannot be located"
					designList = null;
					e.printStackTrace();
				}
			}

			/*
			Task t = new Task(database_);
			try {
				designList = Xots.getService().submit(t).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			 */
		}
		return designList;
	}

	public <T extends DesignBase> DesignCollection<T> getDesignElements(final Class<T> type, String name) {
		NoteCollection coll = database_.createNoteCollection(false);

		if (type == DesignBase.class)
			return getDesignElements();

		DesignFactory mapping = DesignFactory.valueOf(type);

		switch (mapping.getNoteClass()) {
		case DESIGN:
		case HELP_INDEX:
		case ICON:
		case HELP:
		case INFO:
		case ACL:
			coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | mapping.getNoteClass().nativeValue)));
			break;
		default:
			if (!StringUtil.isEmpty(name)) {
				name = "|" + name + "|";
			}

			for (NapiEntry entry : getDesignList()) {

				if ((entry.noteClass & mapping.getNoteClass().nativeValue) != 0) {

					boolean match = name == null ? true : entry.title.contains(name);
					if (match && mapping.getFlags() != null) {
						match = DesignFlags.testFlag(entry.flags, mapping.getFlags());
					}
					if (match && mapping.getFlagsExt() != null) {
						match = DesignFlags.testFlag(entry.flagsExt, mapping.getFlagsExt());
					}
					if (match && mapping.getFilterXsp() != null) {
						match = (entry.title.endsWith(".xsp|") == mapping.getFilterXsp().booleanValue());
					}
					if (match && mapping.getAssistFilter() != null) {
						match = !mapping.getInclude();
						for (int assistFilter : mapping.getAssistFilter()) {
							if (assistFilter == entry.assistType) {
								match = mapping.getInclude();
								break;
							}
						}
					}
					if (match)
						coll.add(entry.noteId);

				}
			}

			break;
		}

		return new DesignCollection<T>(coll, type);
	}

	public <T extends DesignBase> DesignCollection<T> getDesignElements() {
		NoteCollection coll = database_.createNoteCollection(false);
		//Do not add the Design index Note itself
		//coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.DESIGN.nativeValue)));
		coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.HELP_INDEX.nativeValue)));
		coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.ICON.nativeValue)));
		coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.HELP.nativeValue)));
		coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.INFO.nativeValue)));
		coll.add(database_.getDocumentByID(Integer.toHexString(0xFFFF0000 | NoteClass.ACL.nativeValue)));
		for (NapiEntry entry : getDesignList()) {
			coll.add(entry.noteId);
		}

		return new DesignCollection<T>(coll, null);
	}

}
