package org.openntf.domino.xsp.napi;

import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_APPLET_RESOURCE;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_COMPAPP;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_COMPDEF;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_FILE_DL;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_FILE_HIDDEN;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_FOLDER_ALL_VERSIONS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_IMAGE_DBICON;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_IMAGE_RESOURCES_DESIGN;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_JAVAFILE;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_JAVAJAR;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_JAVARESOURCE;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_QUERY_V4_OBJECT;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_SCRIPTLIB_JAVA;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_SCRIPTLIB_JS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_SCRIPTLIB_LS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_SCRIPTLIB_SERVER_JS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_SHARED_COLS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_STYLEKIT;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_VIEWFORM_ALL_VERSIONS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_WEBSERVICE;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_WEBSERVICE_JAVA;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_WEBSERVICE_LS;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_WIDGET;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_XSPCC;
import static org.openntf.domino.design.impl.DesignFactory.DFLAGPAT_XSPPAGE;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.impl.DesignCollection;
import org.openntf.domino.design.impl.DesignFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.designer.domino.napi.NotesCollection;
import com.ibm.designer.domino.napi.NotesCollectionEntry;
import com.ibm.designer.domino.napi.NotesConstants;
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
public class NapiDatabaseDesign implements org.openntf.domino.design.NapiDatabaseDesign {

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
		public String assistType;
		public int noteClass;
		public int noteId;
		public String universalId;

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
							int returnMask = 0x8007; // 0x8000 = read summary table, 4 = read class, 2 = read unid, 1 = read note id 
							NotesIterator iterator = design.readEntries(returnMask, 0, 32);
							try {
								while (iterator.hasNext()) {
									NotesCollectionEntry entry = (NotesCollectionEntry) iterator.next();
									try {
										NapiEntry nEntry = new NapiEntry();
										nEntry.assistType = entry.getItemValueAsString("$AssistType");
										nEntry.flags = entry.getItemValueAsString("$Flags");
										nEntry.flagsExt = entry.getItemValueAsString("$FlagsExt");
										nEntry.title = "|" + entry.getItemValueAsString("$TITLE") + "|";
										nEntry.noteClass = entry.getNoteClass();
										nEntry.noteId = entry.getNoteID();
										nEntry.universalId = entry.getNoteUNID();
										designList.add(nEntry);
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
			} catch (Exception e) {
				designList = null;
				e.printStackTrace();
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

	@Override
	public <T extends DesignBase> DesignCollection<T> getDesignElementsByName(final Class<T> type, String name) {
		// TODO Auto-generated method stub
		NoteCollection coll = database_.createNoteCollection(false);
		// special cases
		if (org.openntf.domino.design.IconNote.class.isAssignableFrom(type)) {
			// NOTE_CLASS_ICON 			0x0010 	= FFFF0010		/* icon note */
			coll.add(database_.getDocumentByID("FFFF0010"));

		} else if (org.openntf.domino.design.UsingDocument.class.isAssignableFrom(type)) {
			// NOTE_CLASS_HELP 			0x0100 	= FFFF0100		/* designer's help note *
			coll.add(database_.getDocumentByID("FFFF0100"));

		} else if (org.openntf.domino.design.AboutDocument.class.isAssignableFrom(type)) {
			// NOTE_CLASS_INFO 			0x0002	= FFFF0002		/* notefile info (help-about) note */
			coll.add(database_.getDocumentByID("FFFF0002"));

		} else if (org.openntf.domino.design.ACLNote.class.isAssignableFrom(type)) {
			// NOTE_CLASS_ACL 			0x0040 	= FFFF0040		/* acl note */
			coll.add(database_.getDocumentByID("FFFF0040"));
		} else {
			// Collection initialisieren
			if (getDesignList() == null)
				return null;

			if (!StringUtil.isEmpty(name)) {
				name = "|" + name + "|";
			}

			String flags = "-";
			String flagsExt = "-";
			Boolean filterXsp = null;

			int noteClass = NotesConstants.NOTE_CLASS_ALL;
			if (org.openntf.domino.design.DesignView.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_VIEW;
				flags = DFLAGPAT_VIEWFORM_ALL_VERSIONS;

			} else if (org.openntf.domino.design.Folder.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_VIEW;
				flags = DFLAGPAT_FOLDER_ALL_VERSIONS;

			} else if (org.openntf.domino.design.Theme.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_STYLEKIT;

			} else if (org.openntf.domino.design.XspJavaResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_JAVAFILE;

			} else if (org.openntf.domino.design.CompositeComponent.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_WIDGET;

			} else if (org.openntf.domino.design.JarResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_JAVAJAR;

			} else if (org.openntf.domino.design.FileResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_FILE_DL;

			} else if (org.openntf.domino.design.FileResourceHidden.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_FILE_HIDDEN;
				flagsExt = "-w";

			} else if (org.openntf.domino.design.FileResourceWebContent.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_FILE_HIDDEN;
				flagsExt = "*w";

			} else if (org.openntf.domino.design.Navigator.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_VIEW;
				flags = NotesConstants.DFLAGPAT_NAVIGATORSWEB;

			} else if (org.openntf.domino.design.ImageResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_IMAGE_RESOURCES_DESIGN;

			} else if (org.openntf.domino.design.DataConnectionResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = NotesConstants.DFLAGPAT_DATA_CONNECTION_RESOURCE;

			} else if (org.openntf.domino.design.Outline.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = NotesConstants.DFLAGPAT_SITEMAP;

			} else if (org.openntf.domino.design.SavedQuery.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = DFLAGPAT_QUERY_V4_OBJECT;

			} else if (org.openntf.domino.design.WebServiceConsumer.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flagsExt = "*W";
				if (org.openntf.domino.design.impl.WebServiceConsumerLS.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_LS;
				} else if (org.openntf.domino.design.impl.WebServiceConsumerJava.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_JAVA;
				}

			} else if (org.openntf.domino.design.ScriptLibrary.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flagsExt = "-W";

				if (org.openntf.domino.design.ScriptLibraryLS.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_LS;
				} else if (org.openntf.domino.design.ScriptLibraryJava.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_JAVA;
				} else if (org.openntf.domino.design.ScriptLibraryCSJS.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_JS;
				} else if (org.openntf.domino.design.ScriptLibrarySSJS.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_SCRIPTLIB_SERVER_JS;
				} else {
					// ALL script libraries:
					flags = "+h.s";
				}

			} else if (org.openntf.domino.design.DatabaseScript.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = NotesConstants.DFLAGPAT_DATABASESCRIPT;

			} else if (org.openntf.domino.design.Subform.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_SUBFORM_ALL_VERSIONS;

			} else if (org.openntf.domino.design.DesignPage.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_PAGESWEB;

			} else if (org.openntf.domino.design.AgentData.class.isAssignableFrom(type)) {
				return null; // agentData is not part of DesignIndex

			} else if (org.openntf.domino.design.SharedActions.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_SACTIONS_DESIGN;

			} else if (org.openntf.domino.design.DB2View.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_DB2ACCESSVIEW;

			} else if (org.openntf.domino.design.Frameset.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_FRAMESET;

			} else if (org.openntf.domino.design.DesignApplet.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_APPLET_RESOURCE;

			} else if (org.openntf.domino.design.StyleSheet.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_STYLE_SHEET_RESOURCE;

			} else if (org.openntf.domino.design.WebServiceProvider.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = DFLAGPAT_WEBSERVICE;
				if (org.openntf.domino.design.impl.WebServiceProviderLS.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_WEBSERVICE_LS;
				} else if (org.openntf.domino.design.impl.WebServiceProviderJava.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_WEBSERVICE_JAVA;
				}

			} else if (org.openntf.domino.design.SharedColumn.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_VIEW;
				flags = DFLAGPAT_SHARED_COLS;

			} else if (org.openntf.domino.design.CompositeApp.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_COMPAPP;

			} else if (org.openntf.domino.design.CompositeWiring.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_COMPDEF;

			} else if (org.openntf.domino.design.DbImage.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = DFLAGPAT_IMAGE_DBICON;

			} else if (org.openntf.domino.design.DesignAgent.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FILTER;
				flags = NotesConstants.DFLAGPAT_AGENTSLIST;
				// Type is distinguished in loop

			} else if (org.openntf.domino.design.SharedField.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FIELD;

			} else if (org.openntf.domino.design.ReplicationFormula.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_REPLFORMULA;

			} else if (org.openntf.domino.design.DesignForm.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;
				flags = NotesConstants.DFLAGPAT_FORMSWEB;

			} else if (org.openntf.domino.design.XspResource.class.isAssignableFrom(type)) {
				noteClass = NotesConstants.NOTE_CLASS_FORM;

				if (org.openntf.domino.design.XPage.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_XSPPAGE;
					filterXsp = Boolean.TRUE;

				} else if (org.openntf.domino.design.XPageFile.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_XSPPAGE;
					filterXsp = Boolean.FALSE;

				} else if (org.openntf.domino.design.CustomControl.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_XSPCC;
					filterXsp = Boolean.TRUE;

				} else if (org.openntf.domino.design.CustomControlFile.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_XSPCC;
					filterXsp = Boolean.FALSE;

				} else if (org.openntf.domino.design.XspJavaResource.class.isAssignableFrom(type)) {
					flags = DFLAGPAT_JAVAFILE;
				} else {
					flags = DFLAGPAT_JAVARESOURCE;
				}

			} else {
				throw new IllegalArgumentException("Class " + type.getName() + " is unsupported");
			}

			for (NapiEntry entry : getDesignList()) {
				if (entry.title.contains(name) && //
						DesignFactory.testFlag(entry.flags, flags) && //
						DesignFactory.testFlag(entry.flagsExt, flagsExt)) {

					if (org.openntf.domino.design.DesignAgent.class.isAssignableFrom(type)) {
						// argh... why is this soooo complex, IBM?
						if (org.openntf.domino.design.impl.DesignAgentF.class.isAssignableFrom(type)) {
							if ("65426".equals(entry.assistType) || "65412".equals(entry.assistType)) {
								coll.add(entry.noteId);
							}

						} else if (org.openntf.domino.design.impl.DesignAgentLS.class.isAssignableFrom(type)) {
							if ("65413".equals(entry.assistType)) {
								coll.add(entry.noteId);
							}

						} else if (org.openntf.domino.design.impl.DesignAgentJ.class.isAssignableFrom(type)) {
							if ("65427".equals(entry.assistType)) {
								if (flags.contains("J"))
									coll.add(entry.noteId);
							}

						} else if (org.openntf.domino.design.impl.DesignAgentIJ.class.isAssignableFrom(type)) {
							if ("65427".equals(entry.assistType)) {
								if (!flags.contains("J"))
									coll.add(entry.noteId);
							}
						} else if (org.openntf.domino.design.impl.DesignAgentA.class.isAssignableFrom(type)) {
							if ("65426".equals(entry.assistType) || "65412".equals(entry.assistType)) {
								// nop:Formula
							} else if ("65413".equals(entry.assistType)) {
								// nop: LS
							} else if ("65427".equals(entry.assistType)) {
								// nop: importedJava
							} else {
								coll.add(entry.noteId);
							}

						} else {
							coll.add(entry.noteId);
						}

					} else if (filterXsp == null) {
						coll.add(entry.noteId);
					} else {
						// ADD only files that ends (ends not) with .xsp
						if (filterXsp.booleanValue()) {
							if (entry.title.endsWith(".xsp")) {
								coll.add(entry.noteId);
							}
						} else {
							if (!entry.title.endsWith(".xsp")) {
								coll.add(entry.noteId);
							}
						}

					}
				}
			}

			return new DesignCollection<T>(coll, type);

		}

	}
