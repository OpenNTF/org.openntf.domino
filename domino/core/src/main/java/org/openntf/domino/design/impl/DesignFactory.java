package org.openntf.domino.design.impl;

import static org.openntf.domino.design.impl.AbstractDesignBase.ASSIST_TYPE;
import static org.openntf.domino.design.impl.AbstractDesignBase.FLAGS_EXT_ITEM;
import static org.openntf.domino.design.impl.AbstractDesignBase.FLAGS_ITEM;
import static org.openntf.domino.design.impl.AbstractDesignBase.TITLE_ITEM;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGEXTPAT_NO_WEBCONTENTFILE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGEXTPAT_NO_WEBSERVICELIB;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGEXTPAT_WEBCONTENTFILE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGEXTPAT_WEBSERVICELIB;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_AGENTSLIST;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_AGENTSLIST_IMPORTED_JAVA;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_AGENTSLIST_JAVA;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_APPLET_RESOURCE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_COMPAPP;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_COMPDEF;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_DATABASESCRIPT;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_DATA_CONNECTION_RESOURCE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_DB2ACCESSVIEW;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_FILE_DL;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_FILE_HIDDEN;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_FOLDER_ALL_VERSIONS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_FRAMESET;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_IMAGE_DBICON;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_IMAGE_RESOURCES_DESIGN;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_JAVAFILE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_JAVAJAR;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_JAVARESOURCE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_NAVIGATORSWEB;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_PAGESWEB;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_QUERY_V4_OBJECT;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SACTIONS_DESIGN;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SCRIPTLIB;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SCRIPTLIB_JAVA;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SCRIPTLIB_JS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SCRIPTLIB_LS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SCRIPTLIB_SERVER_JS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SHARED_COLS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SITEMAP;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_STYLEKIT;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_STYLE_SHEET_RESOURCE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_SUBFORM_ALL_VERSIONS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_VIEWFORMFOLDER;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_VIEWFORM_ALL_VERSIONS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_WEBSERVICE;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_WEBSERVICE_JAVA;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_WEBSERVICE_LS;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_WIDGET;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_XSPCC;
import static org.openntf.domino.design.impl.DesignFlags.DFLAGPAT_XSPPAGE;
import static org.openntf.domino.design.impl.DesignFlags.SIG_ACTION_FORMULA;
import static org.openntf.domino.design.impl.DesignFlags.SIG_ACTION_FORMULAONLY;
import static org.openntf.domino.design.impl.DesignFlags.SIG_ACTION_JAVAAGENT;
import static org.openntf.domino.design.impl.DesignFlags.SIG_ACTION_LOTUSSCRIPT;

import java.io.File;
import java.net.URI;

import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.ext.NoteClass;

//@formatter:off
public enum DesignFactory {


	// This is a table that specifies all properties of the different designelements.
	// ENUM Name			Interface class											Implementing class				On disk Path				On disk Extension	Note Class		....
	//																																			"*" means: Do not encode resource name
	DesignView(				org.openntf.domino.design.DesignView.class,				DesignView.class,				"Views", 					".view", 			NoteClass.VIEW, 	DFLAGPAT_VIEWFORM_ALL_VERSIONS),

	Folder(					org.openntf.domino.design.Folder.class,					Folder.class,					"Folders", 					".folder",			NoteClass.VIEW, 	DFLAGPAT_FOLDER_ALL_VERSIONS),
	Theme(					org.openntf.domino.design.Theme.class,					Theme.class,					"Resources/Themes", 		null,				NoteClass.FORM, 	DFLAGPAT_STYLEKIT),

	CompositeComponent(		org.openntf.domino.design.CompositeComponent.class,		CompositeComponent.class,		"CompositeApplications/Components", 	null,	NoteClass.FORM, 	DFLAGPAT_WIDGET),
	JarResource(			org.openntf.domino.design.JarResource.class,			JarResource.class,				"Code/Jars", 				null,				NoteClass.FORM,		DFLAGPAT_JAVAJAR),
	FileResource(			org.openntf.domino.design.FileResource.class,			FileResource.class,				"Resources/Files",			null,				NoteClass.FORM, 	DFLAGPAT_FILE_DL),
	FileResourceHidden(		org.openntf.domino.design.FileResourceHidden.class,		FileResourceHidden.class,		"",	/* = in root folder */	"*",				NoteClass.FORM, 	DFLAGPAT_FILE_HIDDEN, 		DFLAGEXTPAT_NO_WEBCONTENTFILE),
	FileResourceWebContent(	org.openntf.domino.design.FileResourceWebContent.class,	FileResourceWebContent.class,	"WebContent", 				"*",				NoteClass.FORM, 	DFLAGPAT_FILE_HIDDEN, 		DFLAGEXTPAT_WEBCONTENTFILE),

	Navigator(				org.openntf.domino.design.Navigator.class,				Navigator.class,				"SharedElements/Navigators",".navigator",		NoteClass.VIEW, 	DFLAGPAT_NAVIGATORSWEB),
	ImageResource(			org.openntf.domino.design.ImageResource.class,			ImageResource.class,			"Resources/Images", 		null,				NoteClass.FORM, 	DFLAGPAT_IMAGE_RESOURCES_DESIGN),
	DataConnectionResource(	org.openntf.domino.design.DataConnectionResource.class,	DataConnectionResource.class,	"Data/DataConnections", 	".dcr",				NoteClass.FILTER, 	DFLAGPAT_DATA_CONNECTION_RESOURCE),
	Outline(				org.openntf.domino.design.DesignOutline.class,			DesignOutline.class,			"SharedElements/Outlines", 	".outline",			NoteClass.FILTER, 	DFLAGPAT_SITEMAP),
	SavedQuery(				org.openntf.domino.design.SavedQuery.class,				SavedQuery.class,				"Other/SavedQueries", 		null,				NoteClass.FILTER, 	DFLAGPAT_QUERY_V4_OBJECT),

	// Special case - WebServiceConsumers
	WebServiceConsumerLS(	org.openntf.domino.design.WebServiceConsumerLS.class,	WebServiceConsumerLS.class,		"Code/WebServiceConsumer", 	".lswsc",			NoteClass.FILTER,  	DFLAGPAT_SCRIPTLIB_LS, 		DFLAGEXTPAT_WEBSERVICELIB),
	WebServiceConsumerJava(	org.openntf.domino.design.WebServiceConsumerJava.class,	WebServiceConsumerJava.class,	"Code/WebServiceConsumer", 	".javalib",			NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB_JAVA, 	DFLAGEXTPAT_WEBSERVICELIB),
	WebServiceConsumer(		org.openntf.domino.design.WebServiceConsumer.class,		null,							null,						null,				NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB, 		DFLAGEXTPAT_WEBSERVICELIB),


	//	// Special case - WebServiceProviders
	WebServiceProviderLS(	org.openntf.domino.design.WebServiceProviderLS.class,	WebServiceProviderLS.class,		"Code/WebServices", 		".lws",				NoteClass.FILTER, 	DFLAGPAT_WEBSERVICE_LS),
	WebServiceProviderJava(	org.openntf.domino.design.WebServiceProviderJava.class,	WebServiceProviderJava.class,	"Code/WebServices", 		".jws",				NoteClass.FILTER, 	DFLAGPAT_WEBSERVICE_JAVA),
	WebServiceProvider(		org.openntf.domino.design.WebServiceProvider.class,		null,							null,						null,				NoteClass.FILTER, 	DFLAGPAT_WEBSERVICE),
	//
	//	// Special case - Script libraries
	ScriptLibraryLS(		org.openntf.domino.design.ScriptLibraryLS.class,		ScriptLibraryLS.class,			"Code/ScriptLibraries", 	".lss",				NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB_LS, 		DFLAGEXTPAT_NO_WEBSERVICELIB),
	ScriptLibraryJava(		org.openntf.domino.design.ScriptLibraryJava.class,		ScriptLibraryJava.class,		"Code/ScriptLibraries", 	".javalib",			NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB_JAVA, 	DFLAGEXTPAT_NO_WEBSERVICELIB),
	ScriptLibraryCSJS(		org.openntf.domino.design.ScriptLibraryCSJS.class,		ScriptLibraryCSJS.class,		"Code/ScriptLibraries", 	".js",				NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB_JS),
	ScriptLibrarySSJS(		org.openntf.domino.design.ScriptLibrarySSJS.class,		ScriptLibrarySSJS.class,		"Code/ScriptLibraries", 	".jss",				NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB_SERVER_JS),
	//	// all of the above
	ScriptLibrary(			org.openntf.domino.design.ScriptLibrary.class,			null,							null,						null,				NoteClass.FILTER, 	DFLAGPAT_SCRIPTLIB,			DFLAGEXTPAT_NO_WEBSERVICELIB),
	//
	DatabaseScript(			org.openntf.domino.design.DatabaseScript.class,			DatabaseScript.class,			"Code", 					"dbscript.lsdb",	NoteClass.FILTER, 	DFLAGPAT_DATABASESCRIPT),
	Subform(				org.openntf.domino.design.Subform.class,				Subform.class,					"SharedElements/Subforms", 	".subform",			NoteClass.FORM, 	DFLAGPAT_SUBFORM_ALL_VERSIONS),
	DesignPage(				org.openntf.domino.design.DesignPage.class,				DesignPage.class,				"Pages", 					".page",			NoteClass.FORM, 	DFLAGPAT_PAGESWEB),
	AgentData(				org.openntf.domino.design.AgentData.class,				AgentData.class,				"Other/AgentData", 			".agentdata",		NoteClass.FILTER, 	"+X"), // agentData is not part of DesignIndex - I don't think this is really needed!?
	SharedActions(			org.openntf.domino.design.SharedActions.class,			SharedActionsNote.class,		"Code/actions", 			"Shared Actions", 	NoteClass.FORM, 	DFLAGPAT_SACTIONS_DESIGN),
	DB2View(				org.openntf.domino.design.DB2View.class,				DB2View.class,					"Data/DB2AccessViews", 		".db2v",			NoteClass.FORM, 	DFLAGPAT_DB2ACCESSVIEW),
	Frameset(				org.openntf.domino.design.Frameset.class,				Frameset.class,					"Framesets", 				".frameset",		NoteClass.FORM, 	DFLAGPAT_FRAMESET),
	DesignApplet(			org.openntf.domino.design.DesignApplet.class,			DesignApplet.class,				"Resources/Applets", 		".applet",			NoteClass.FORM, 	DFLAGPAT_APPLET_RESOURCE),
	StyleSheet(				org.openntf.domino.design.StyleSheet.class,				StyleSheet.class,				"Resources/StyleSheets", 	".css",				NoteClass.FORM, 	DFLAGPAT_STYLE_SHEET_RESOURCE),
	SharedColumn(			org.openntf.domino.design.SharedColumn.class,			SharedColumn.class,				"SharedElements/Columns", 	".column", 			NoteClass.VIEW, 	DFLAGPAT_SHARED_COLS),
	CompositeApp(			org.openntf.domino.design.CompositeApp.class,			CompositeApp.class,				"CompositeApplications/Applications", ".ca",	NoteClass.FORM, 	DFLAGPAT_COMPAPP),
	CompositeWiring(		org.openntf.domino.design.CompositeWiring.class,		CompositeWiring.class,			"CompositeApplications/WiringProperties", ".wsdl",NoteClass.FORM, 	DFLAGPAT_COMPDEF),
	DbImage(				org.openntf.domino.design.DbImage.class,				DbImage.class,					"AppProperties",			"$DBIcon",			NoteClass.FORM, 	DFLAGPAT_IMAGE_DBICON),
	//
	//	// very special case - Agents
	DesignAgentA(			org.openntf.domino.design.DesignAgentA.class,			DesignAgentA.class,				"Code/Agents", 				".aa",				NoteClass.FILTER, 	DFLAGPAT_AGENTSLIST, 				false, 	SIG_ACTION_FORMULA, SIG_ACTION_FORMULAONLY, SIG_ACTION_JAVAAGENT, SIG_ACTION_LOTUSSCRIPT),
	DesignAgentF(			org.openntf.domino.design.DesignAgentF.class,			DesignAgentF.class,				"Code/Agents", 				".fa", 				NoteClass.FILTER, 	DFLAGPAT_AGENTSLIST, 				true, 	SIG_ACTION_FORMULA, SIG_ACTION_FORMULAONLY ),
	DesignAgentIJ(			org.openntf.domino.design.DesignAgentIJ.class,			DesignAgentIJ.class,			"Code/Agents", 				".ija",				NoteClass.FILTER, 	DFLAGPAT_AGENTSLIST_IMPORTED_JAVA, 	true, 	SIG_ACTION_JAVAAGENT ),
	DesignAgentJ(			org.openntf.domino.design.DesignAgentJ.class,			DesignAgentJ.class,				"Code/Agents", 				".ja", 				NoteClass.FILTER, 	DFLAGPAT_AGENTSLIST_JAVA, 			true,	SIG_ACTION_JAVAAGENT ),
	DesignAgentLS(			org.openntf.domino.design.DesignAgentLS.class,			DesignAgentLS.class,			"Code/Agents", 				".lsa", 			NoteClass.FILTER, 	DFLAGPAT_AGENTSLIST, 				true, 	SIG_ACTION_LOTUSSCRIPT ),
	//	// all of the above
	DesignAgent(			org.openntf.domino.design.DesignAgent.class,			null,							null,						null,				NoteClass.FILTER,	DFLAGPAT_AGENTSLIST),

	DesignForm(				org.openntf.domino.design.DesignForm.class,				DesignForm.class,				"Forms", 					".form",			NoteClass.FORM, 	DFLAGPAT_VIEWFORM_ALL_VERSIONS),
	//
	//	// Special case XPages
	XPage(					org.openntf.domino.design.XPage.class,					XPage.class,					"XPages", 					".xsp",				NoteClass.FORM, 	DFLAGPAT_XSPPAGE, 	Boolean.TRUE),
	XPageFile(				org.openntf.domino.design.XPageFile.class,				XPageFile.class,				"XPages", 					null,				NoteClass.FORM, 	DFLAGPAT_XSPPAGE, 	Boolean.FALSE),
	CustomControl(			org.openntf.domino.design.CustomControl.class,			CustomControl.class,			"CustomControls", 			".xsp",				NoteClass.FORM, 	DFLAGPAT_XSPCC, 	Boolean.TRUE),
	CustomControlFile(		org.openntf.domino.design.CustomControlFile.class,		CustomControlFile.class,		"CustomControls", 			null,				NoteClass.FORM, 	DFLAGPAT_XSPCC, 	Boolean.FALSE),
	XspJavaResource(		org.openntf.domino.design.XspJavaResource.class,		XspJavaResource.class,			"Code/Java", 				"*",				NoteClass.FORM, 	DFLAGPAT_JAVAFILE),
	XspResource(			org.openntf.domino.design.XspResource.class,			null,							null,						null,				NoteClass.FORM, 	DFLAGPAT_JAVARESOURCE),


	AnyFileResource(		org.openntf.domino.design.AnyFileResource.class,		null,							null,						null,				NoteClass.FORM, 	"(+gi-K[];`_*"),
	AnyFolderOrView(		org.openntf.domino.design.AnyFolderOrView.class,		null,							null,						null,				NoteClass.VIEW, 	DFLAGPAT_VIEWFORMFOLDER),
	AnyFormOrSubform(		org.openntf.domino.design.AnyFormOrSubform.class,		null,							null,						null,				NoteClass.FORM, 	DFLAGPAT_VIEWFORMFOLDER),

	SharedField(			org.openntf.domino.design.SharedField.class,			SharedField.class,				"SharedElements/Fields", 	".field",			NoteClass.FIELD),
	ReplicationFormula(		org.openntf.domino.design.ReplicationFormula.class,		ReplicationFormula.class,		"Other/ReplicationFormulas",null,				NoteClass.REPLFORMULA),
	IconNote(				org.openntf.domino.design.IconNote.class,				IconNote.class,					"Resources", 				"IconNote", 		NoteClass.ICON),
	UsingDocument(			org.openntf.domino.design.UsingDocument.class,			UsingDocument.class,			"Resources", 				"UsingDocument", 	NoteClass.HELP),
	AboutDocument(			org.openntf.domino.design.AboutDocument.class,			AboutDocument.class,			"Resources", 				"AboutDocument",	NoteClass.INFO),
	ACLNote(				org.openntf.domino.design.ACLNote.class,				ACLNote.class,					"AppProperties",			"database.properties",NoteClass.ACL),
	//@formatter:on
	;

	/** noteClass of the DesignNote */
	private NoteClass noteClass_;

	/** $Flags of the DesginNote (maybe null) */
	private String flags_;
	/** $FlagsExt of the DesginNote (maybe null) */
	private String flagsExt_;

	/** The folder where this element is stored */
	private final String onDiskFolder_;

	/** The file extension of this element. e.g. "*.lss". If null, all files in the folder are considered as type of this design element */
	private final String onDiskFileExtension_;

	// lowercase cache - only internally used!
	private final String lcOnDiskFolder_;
	private final String lcOnDiskFileExtension_;
	/**
	 * Tri-state-value for XPages:
	 * <ul>
	 * <li>True: $TITLE must end with ".xsp"</li>
	 * <li>False: $TITLE must not end with ".xsp"</li>
	 * <li>null: doesn't matter</li>
	 * <ul>
	 */
	private Boolean filterXsp_;

	/** Array for $AssistTypes (only relevant for Agents) */
	private int[] assistFilter_;

	/** include or exclude the assistTypes specified in assistFilter_ */
	private boolean include_;

	private Class<? extends DesignBase> interfaceClazz_;

	private Class<? extends AbstractDesignBase> implClazz_;

	/**
	 * Create a new filter
	 * 
	 * @param noteClass
	 *            the NoteClass
	 */
	private DesignFactory(final Class<? extends DesignBase> iClazz, final Class<? extends AbstractDesignBase> clazz, final String folder,
			final String ext, final NoteClass noteClass) {
		interfaceClazz_ = iClazz;
		implClazz_ = clazz;
		onDiskFolder_ = folder;
		lcOnDiskFolder_ = folder == null ? null : folder.toLowerCase().concat("/");
		onDiskFileExtension_ = ext;
		lcOnDiskFileExtension_ = ext == null ? null : ext.toLowerCase();
		noteClass_ = noteClass;
	}

	/**
	 * Create a new filter
	 * 
	 * @param noteClass
	 *            the NoteClass
	 * @param flags
	 *            pattern for $FLAGS
	 */
	private DesignFactory(final Class<? extends DesignBase> iClazz, final Class<? extends AbstractDesignBase> clazz, final String folder,
			final String ext, final NoteClass noteClass, final String flags) {
		this(iClazz, clazz, folder, ext, noteClass);
		flags_ = flags;
	}

	/**
	 * Create a new filter (for Agents)
	 * 
	 * @param noteClass
	 *            the NoteClass
	 * @param flags
	 *            pattern for $FLAGS
	 * @param include
	 *            include/exclude given AssistFlags
	 * @param assistFilter
	 *            the AssistFlags
	 */
	private DesignFactory(final Class<? extends DesignBase> iClazz, final Class<? extends AbstractDesignBase> clazz, final String folder,
			final String ext, final NoteClass noteClass, final String flags, final boolean include, final int... assistFilter) {
		this(iClazz, clazz, folder, ext, noteClass);
		flags_ = flags;
		assistFilter_ = assistFilter;
		include_ = include;
	}

	/**
	 * Create a new filter
	 * 
	 * @param noteClass
	 *            the NoteClass
	 * @param flags
	 *            pattern for $FLAGS
	 * @param flagsExt
	 *            pattern for $FLAGSEXT
	 */
	private DesignFactory(final Class<? extends DesignBase> iClazz, final Class<? extends AbstractDesignBase> clazz, final String folder,
			final String ext, final NoteClass noteClass, final String flags, final String flagsExt) {
		this(iClazz, clazz, folder, ext, noteClass);
		flags_ = flags;
		flagsExt_ = flagsExt;
	}

	/**
	 * Create a new filter
	 * 
	 * @param noteClass
	 *            the NoteClass
	 * @param flags
	 *            pattern for $FLAGS
	 * @param filterXsp
	 *            include/exclude xpages (.xsp)
	 */
	private DesignFactory(final Class<? extends DesignBase> iClazz, final Class<? extends AbstractDesignBase> clazz, final String folder,
			final String ext, final NoteClass noteClass, final String flags, final Boolean filterXsp) {
		this(iClazz, clazz, folder, ext, noteClass);
		flags_ = flags;
		filterXsp_ = filterXsp;
	}

	public NoteClass getNoteClass() {
		return noteClass_;
	}

	public String getFlags() {
		return flags_;
	}

	public String getFlagsExt() {
		return flagsExt_;
	}

	public Boolean getFilterXsp() {
		return filterXsp_;
	}

	public int[] getAssistFilter() {
		return assistFilter_;
	}

	public boolean getInclude() {
		return include_;
	}

	public static DesignFactory valueOf(final Document doc) {

		if (doc.hasItem("IconBitmap") && doc.getNoteClass() == NoteClass.ICON)
			return IconNote;

		String flags = doc.getItemValueString(FLAGS_ITEM);
		String flagsExt = doc.getItemValueString(FLAGS_EXT_ITEM);
		String title = doc.getItemValueString(TITLE_ITEM);
		Integer assistType = doc.getItemValue(ASSIST_TYPE, Integer.class);
		DesignFactory candidate = null;

		for (DesignFactory mapping : DesignFactory.values()) {
			if (mapping == IconNote) {
				// nop - skip iconNote
			} else if (mapping.getFlags() == null) {
				// no flags, so noteClass must match 
				if (mapping.getNoteClass() == doc.getNoteClass()) {
					return mapping;
				}
			} else if (DesignFlags.testFlag(flags, mapping.getFlags())) {
				// flags match
				boolean match = true;
				if (match && mapping.getFilterXsp() != null) {
					match = (title.endsWith(".xsp") == mapping.getFilterXsp().booleanValue());
				}
				if (match && mapping.getFlagsExt() != null) {
					match = DesignFlags.testFlag(flagsExt, mapping.getFlagsExt());
				}
				if (match && mapping.getAssistFilter() != null) {
					match = !mapping.getInclude();
					for (int assistFilter : mapping.getAssistFilter()) {
						if (assistType != null && assistFilter == assistType) {
							match = mapping.getInclude();
							break;
						}
					}
				}
				if (match) {
					// now, flags, flagsext & assistType matches
					if (candidate == null) {
						candidate = mapping;
					} else {
						// two or more candidates found:
						if (candidate.getNoteClass() == doc.getNoteClass())
							return candidate;
						if (mapping.getNoteClass() == doc.getNoteClass())
							return mapping;
					}
				}

			}
		}
		return candidate;
	}

	public static DesignFactory valueOf(final Class<? extends DesignBase> type) {
		for (DesignFactory mapping : DesignFactory.values()) {
			if (mapping.interfaceClazz_.isAssignableFrom(type))
				return mapping;
		}
		throw new IllegalArgumentException("No Mapping available for " + type.getName());
	}

	public static DesignBase fromDocument(final Document doc) {
		if (doc == null)
			return null;
		DesignFactory mapping = valueOf(doc);
		if (mapping == null) {
			return new OtherDesignElement(doc);
		} else {
			try {
				AbstractDesignBase ret = mapping.getImplClass().newInstance();
				ret.init(doc);
				return ret;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	public static DesignFactory valueOf(final File parent, final File file) {
		URI relUri = parent.toURI().relativize(file.toURI());
		if (relUri.isAbsolute()) {
			throw new IllegalArgumentException(file + " is not relative to " + parent);
		}
		String lcPath = relUri.getPath().toLowerCase();
		for (DesignFactory fact : DesignFactory.values()) {
			if (fact.lcOnDiskFolder_ != null && fact != DesignFactory.FileResourceHidden) {
				if (lcPath.startsWith(fact.lcOnDiskFolder_)) {
					String ext = fact.lcOnDiskFileExtension_;
					if (ext == null || lcPath.endsWith(ext) || ext.equals("*")) {
						return fact;
					}
				}
			}
		}

		return DesignFactory.FileResourceHidden;
	}

	public Class<? extends DesignBase> getInterfaceClass() {
		return interfaceClazz_;
	}

	public Class<? extends AbstractDesignBase> getImplClass() {
		return implClazz_;
	}

	public String getOnDiskFolder() {
		return onDiskFolder_;
	}

	public String getOnDiskFileExtension() {
		return onDiskFileExtension_;
	}
}
