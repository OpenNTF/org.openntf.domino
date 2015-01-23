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

package org.openntf.domino.design.impl;

import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
enum DesignFactory {
	INSTANCE;

	/**
	 * This method tests the Flags in an equal way as the "NotesUtlis.CmemflagTestMultiple" does
	 * 
	 * @param flags
	 *            the flags to test
	 * @param pattern
	 *            the flag pattern.
	 *            <ul>
	 *            <li>+ means at least one of the flags must appear</li>
	 *            <li>- means, none of the flags must appear</li>
	 *            <li>* means, all of the Flags must appear</li>
	 *            <li>( means, a combination of +, - and *</li>
	 *            <ul>
	 *            e.g (+ab-cd*ef means, that (a or b) && not (c or d) && (e and f) must appear in <code>flags</code>
	 * @return true, if the pattern matches
	 */

	public static boolean testFlag(final String flags, final String pattern) {
		if (pattern.length() < 1)
			return false;

		int i = 0;
		switch (pattern.charAt(i++)) {

		case '+': // flags must contain ANY char of pattern
			while (i < pattern.length()) {
				if (flags.indexOf(pattern.charAt(i++)) >= 0)
					return true;
			}
			return false;

		case '-': // flags must NOT contain ANY char of pattern
			while (i < pattern.length()) {
				if (flags.indexOf(pattern.charAt(i++)) >= 0)
					return false;
			}
			return true;

		case '*': // flags must contain ALL char of pattern
			while (i < pattern.length()) {
				if (flags.indexOf(pattern.charAt(i++)) < 0)
					return false;
			}
			return true;

		case '(': // combined rule
			boolean success = false;

			// must follow with '+' "(+...."
			if (pattern.charAt(i++) != '+')
				return false;

			if (i < pattern.length() && pattern.charAt(i) == '-') { // special case if no plus rule is there "(+-..."
				success = true;
			}
			char ch = 0;
			// verify + rule and scan to next -
			while (i < pattern.length()) {
				ch = pattern.charAt(i++);
				if (ch == '-') {
					if (!success)
						return false;
					break;
				}
				// in + mode, at least one the flags must appear
				if (!success && flags.indexOf(ch) >= 0)
					success = true;
			}

			// verify the negative list and scan to next '*'
			if (!success || ch != '-')
				return false;
			while (i < pattern.length()) {
				ch = pattern.charAt(i++);
				if (ch == '*')
					break;
				if (flags.indexOf(ch) >= 0) {
					// handle negative list
					return false;
				}
			}

			// verify the * rule. all chars must appear
			while (i < pattern.length()) {
				ch = pattern.charAt(i++);
				if (flags.indexOf(ch) < 0)
					return false;
			}
			return true;

		}

		return false;
	}

	private DesignFactory() {
	}

	//  this @Formula was used by FOCONIS to determine the type in a DesignView (only here for own reference)
	//	@IsAvailable(IconBitmap);		"DBICON";
	//	@Contains($Flags;"F");			"FOLDER";							-> DFLAGPAT_FOLDER_ALL_VERSIONS = "*F";
	//	@Contains($Flags;"g");	
	//		@If( @Contains($Flags; "`"); 	"THEME";						-> DFLAGPAT_STYLEKIT = "*g`"
	//			@Contains($Flags; "K"); 	"XPAGE";						-> DFLAGPAT_XSPPAGE = "*gK"
	//			@Contains($Flags; ";"); 	"CUSTOMCONTROL";				-> DFLAGPAT_XSPCC = "*g;"
	//			@Contains($Flags; "~"); 	"XPAGERESOURCE";				-> TODO,  DFLAGPAT_JAVAFILE = "(+-*g["; / and other
	//								"FILE"
	//	);
	//	
	//	@Contains($Flags;"G");			"NAVIGATOR";						-> DFLAGPAT_NAVIGATORSWEB = "+G";
	//	@Contains($Flags;"i");			"IMAGE";							-> DFLAGPAT_IMAGE_RESOURCE = "+i";
	//	@Contains($Flags;"k");			"DATACONN";							-> DFLAGPAT_DATA_CONNECTION_RESOURCE = "+k";
	//	@Contains($Flags;"m");			"OUTLINE";							-> DFLAGPAT_SITEMAP = "+m";
	//	@Contains($Flags;"O");			"FTQUERY";							-> DFLAGPAT_QUERY_V4_OBJECT = "+O";
	//	@Matches ($Flags; "*{sh.}*");
	//		@If(@Contains($FlagsExt; "W");"WEBCONSUMER";					->
	//							 	"SCRIPT"								-> DFLAGPAT_SCRIPTLIB_LS = "(+s-jh.*"; / DFLAGPAT_SCRIPTLIB_JAVA = "*sj"; / DFLAGPAT_SCRIPTLIB_JS = "+h"; / DFLAGPAT_SCRIPTLIB_SERVER_JS = "+.";
	//	);
	//	@Contains($Flags;"t");			"DBSCRIPT";							-> DFLAGPAT_DATABASESCRIPT = "+t";
	//	@Contains($Flags;"U");			"SUBFORM";							-> DFLAGPAT_SUBFORM = "(+U-40n*";
	//	@Contains($Flags;"W");			"PAGE";								-> DFLAGPAT_PAGE = "+W";
	//	@Contains($Flags;"X");			"(AGENT-DATA)";						-> DFLAGPAT_AGENT_DATA = "+X";
	//	@Contains($Flags;"y");			"ACTION";							-> DFLAGPAT_SACTIONS_DESIGN = "+y"
	//	@Contains($Flags;"z");			"DB2VIEW";							-> DFLAGPAT_DB2ACCESSVIEW = "+z";
	//	@Contains($Flags;"#");			"FRAMESET";							-> DFLAGPAT_FRAMESETSWEB = "+#";
	//	@Contains($Flags;"@");			"APPLET";							-> DFLAGPAT_JAVA_RESOURCE = "+@";
	//	@Contains($Flags;"=");			"STYLESHEET";						-> DFLAGPAT_STYLE_SHEET_RESOURCE = "+=";
	//	@Contains($Flags;"{");			"WEBSERVICE";						-> DFLAGPAT_WEBSERVICE = "+{";
	//	@Contains($Flags;"^");			"COLUMN";							-> DFLAGPAT_SHARED_COLS = "(+-*^";
	//	@Contains($Flags;"||");			"COMPOSITEAPP";						-> DFLAGPAT_COMPAPP = "+|";
	//	@Contains($Flags;":");			"WIRINGPROPERTY";					-> DFLAGPAT_COMPDEF = "+:";
	//
	//	---- Agents/Views/HelpAbout/HelpUsing/Field/Form cannot be distinguished by $Flags -----
	//	@IsAvailable($AssistFlags);		"AGENT";							->
	//	@IsAvailable($FormulaClass);		"VIEW";							->
	//	$HelpUsing="1";				"HELPUSING";							->
	//	!@IsAvailable($Title);			"HELPABOUT";						->
	//	$Flags = "" & @Count($FIELDS) = 1									->
	//		& @Count($Title) = 1											->
	//		& $FIELDS = $TITLE;			"FIELD";							->
	//	"FORM"
	//public static final String DFLAGPAT_IMAGE_DBICON = "(+-*i~";

	public static final String DFLAGPAT_FOLDER_ALL_VERSIONS = "*F";
	public static final String DFLAGPAT_STYLEKIT = "*g`";
	public static final String DFLAGPAT_XSPPAGE = "*gK";
	public static final String DFLAGPAT_XSPCC = "*g;";
	public static final String DFLAGPAT_JAVAFILE = "*g[";
	public static final String DFLAGPAT_WIDGET = "*g_";
	public static final String DFLAGPAT_FILE_DL = "(+g-~K[],;`*";
	public static final String DFLAGPAT_FILE_HIDDEN = "(+-K[],;`*g~";
	public static final String DFLAGPAT_JAVAJAR = "*g,";
	//public static final String DFLAGPAT_FILE = "(+g-K;`"; //RPr: corrected. orignal IBM string: "+g-K;`"
	public static final String DFLAGPAT_NAVIGATORSWEB = "+G";
	public static final String DFLAGPAT_IMAGE_RESOURCE = "+i";
	public static final String DFLAGPAT_DATA_CONNECTION_RESOURCE = "+k";
	public static final String DFLAGPAT_SITEMAP = "+m";
	public static final String DFLAGPAT_QUERY_V4_OBJECT = "+O";
	public static final String DFLAGPAT_SCRIPTLIB_LS = "(+s-jh.*";
	public static final String DFLAGPAT_SCRIPTLIB_JAVA = "*sj";
	public static final String DFLAGPAT_SCRIPTLIB_JS = "+h";
	public static final String DFLAGPAT_SCRIPTLIB_SERVER_JS = "+.";
	public static final String DFLAGPAT_DATABASESCRIPT = "+t";
	public static final String DFLAGPAT_SUBFORM_ALL_VERSIONS = "+U";
	public static final String DFLAGPAT_PAGE = "+W";
	public static final String DFLAGPAT_AGENT_DATA = "+X";
	public static final String DFLAGPAT_SACTIONS_DESIGN = "+y";
	public static final String DFLAGPAT_DB2ACCESSVIEW = "+z";
	public static final String DFLAGPAT_FRAMESETSWEB = "+#";
	public static final String DFLAGPAT_APPLET_RESOURCE = "+@";
	public static final String DFLAGPAT_STYLE_SHEET_RESOURCE = "+=";
	public static final String DFLAGPAT_WEBSERVICE = "+{";
	public static final String DFLAGPAT_SHARED_COLS = "(+-*^";
	public static final String DFLAGPAT_COMPAPP = "+|";
	public static final String DFLAGPAT_COMPDEF = "+:";
	public static final String DFLAGPAT_VIEWFORM_ALL_VERSIONS = "-FQMUGXWy#i@K;g~%z^}";

	//	public static final String DFLAGPAT_AGENTSLIST = "-QXstmz{";
	//	public static final String DFLAGPAT_VIEWFORM = "-FQMUGXWy#i@0nK;g~%z^";
	//	public static final String DFLAGPAT_FORMSWEB = "-U#Wi@y";
	//	public static final String DFLAGPAT_FIELD = "-FQMUGXWy#i@0nK;g~%z^";

	public static org.openntf.domino.design.DesignBase fromDocument(final Document doc) {
		if (doc.hasItem("IconBitmap"))
			return new IconNote(doc);
		// RPr: Flags :) Dont ask! accept it! (Tested with a database that contains at least one element of each type)
		String flags = doc.getItemValueString("$Flags");
		if (testFlag(flags, DFLAGPAT_FOLDER_ALL_VERSIONS)) {
			return new Folder(doc);
		}
		if (testFlag(flags, DFLAGPAT_STYLEKIT)) {
			return new Theme(doc);
		}
		if (testFlag(flags, DFLAGPAT_XSPPAGE)) {
			return new XPage(doc);
		}
		if (testFlag(flags, DFLAGPAT_XSPCC)) {
			return new CustomControl(doc);
		}
		if (testFlag(flags, DFLAGPAT_JAVAFILE)) {
			return new JavaFile(doc);
		}
		if (testFlag(flags, DFLAGPAT_WIDGET)) {
			return new CompositeComponent(doc);
		}
		if (testFlag(flags, DFLAGPAT_JAVAJAR)) {
			return new JarResource(doc);
		}
		if (testFlag(flags, DFLAGPAT_FILE_DL)) {
			return new FileResource(doc);
		}
		if (testFlag(flags, DFLAGPAT_FILE_HIDDEN)) {
			String flagsExt = doc.getItemValueString("$FlagsExt");
			if (testFlag(flagsExt, "+w"))
				return new FileResourceWebContent(doc);
			return new FileResourceHidden(doc);
		}
		if (testFlag(flags, DFLAGPAT_NAVIGATORSWEB)) {
			return new Navigator(doc);
		}
		if (testFlag(flags, DFLAGPAT_IMAGE_RESOURCE)) {
			return new ImageResource(doc);
		}
		if (testFlag(flags, DFLAGPAT_DATA_CONNECTION_RESOURCE)) {
			return new DataConnectionResource(doc);
		}
		if (testFlag(flags, DFLAGPAT_SITEMAP)) {
			return new DesignOutline(doc);
		}
		if (testFlag(flags, DFLAGPAT_QUERY_V4_OBJECT)) {
			return new SavedQuery(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_LS)) {
			String flagsExt = doc.getItemValueString("$FlagsExt");
			if (testFlag(flagsExt, "+W"))
				return new WebServiceConsumer(doc);
			return new LotusScriptLibrary(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_JAVA)) {
			return new JavaLibrary(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_JS)) {
			return new JavaScriptLibrary(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_SERVER_JS)) {
			return new SSJSLibrary(doc);
		}
		if (testFlag(flags, DFLAGPAT_DATABASESCRIPT)) {
			return new DatabaseScript(doc);
		}
		if (testFlag(flags, DFLAGPAT_SUBFORM_ALL_VERSIONS)) {
			return new Subform(doc);
		}
		if (testFlag(flags, DFLAGPAT_PAGE)) {
			return new DesignPage(doc);
		}
		if (testFlag(flags, DFLAGPAT_AGENT_DATA)) {
			return new AgentData(doc);
		}
		if (testFlag(flags, DFLAGPAT_SACTIONS_DESIGN)) {
			return new SharedActionsNote(doc);
		}
		if (testFlag(flags, DFLAGPAT_DB2ACCESSVIEW)) {
			return new DB2View(doc);
		}
		if (testFlag(flags, DFLAGPAT_FRAMESETSWEB)) {
			return new Frameset(doc);
		}
		if (testFlag(flags, DFLAGPAT_APPLET_RESOURCE)) {
			return new DesignApplet(doc);
		}
		if (testFlag(flags, DFLAGPAT_STYLE_SHEET_RESOURCE)) {
			return new StyleSheet(doc);
		}
		if (testFlag(flags, DFLAGPAT_WEBSERVICE)) {
			return new WebserviceProvider(doc);
		}
		if (testFlag(flags, DFLAGPAT_SHARED_COLS)) {
			return new SharedColumn(doc);
		}
		if (testFlag(flags, DFLAGPAT_COMPAPP)) {
			return new CompositeApp(doc);
		}
		if (testFlag(flags, DFLAGPAT_COMPDEF)) {
			return new CompositeWiring(doc);
		}

		if (testFlag(flags, DFLAGPAT_VIEWFORM_ALL_VERSIONS)) {
			// something that looks like a view/form/agent/...
			if (doc.hasItem("$AssistFlags")) { // Agent
				return new DesignAgent(doc);
			}
			if (doc.hasItem("$FormulaClass")) { //View
				return new DesignView(doc);
			}
			if ("1".equals(doc.getItemValueString("$HelpUsing"))) { // HelpUsing
				return new UsingDocument(doc);
			}
			if (doc.hasItem("$ACLDigest")) {
				return new ACLNote(doc);
			}
			if ("".equals(doc.getItemValueString("$Title"))) { // HelpAbout
				return new AboutDocument(doc);

			}
			if ("".equals(doc.getItemValueString("$Flags")) && // Shared field 
					doc.getItemValueString("$Fields").equals(doc.getItemValueString("$Title"))) {
				return new SharedField(doc);
			}
			// everything else is treated as FORM
			return new DesignForm(doc);
		} else {
			// e.g. the $BEProfileR7
			return new OtherDesignElement(doc);
		}
	}
	/*
		@SuppressWarnings("unchecked")
		public static <T> T fromDocument(final Document doc, final Class<? extends org.openntf.domino.design.DesignBase> T) {
			if (T == null) {
				return (T) fromDocument(doc);
			}
			// TODO: Replace the code below by this
			if (false) {
				DesignBase ret = fromDocument(doc);
				if (T.isAssignableFrom(ret.getClass())) {
					return (T) ret;
				}
				throw new ClassCastException("something went wrong");
			}
			if (T == ACLNote.class) {
				return (T) (new ACLNote(doc));
			} else if (T == AboutDocument.class) {
				return (T) (new AboutDocument(doc));
			} else if (T == FileResource.class) {
				return (T) (new FileResource(doc));
			} else if (T == DesignForm.class) {
				return (T) (new DesignForm(doc));
			} else if (T == IconNote.class) {
				return (T) (new IconNote(doc));
			} else if (T == ReplicationFormula.class) {
				return (T) (new ReplicationFormula(doc));
			} else if (T == UsingDocument.class) {
				return (T) (new UsingDocument(doc));
			} else if (T == DesignView.class) {
				return (T) (new DesignView(doc));
				//		} else if (T == JavaResource.class) {
				//			return (T) (new JavaResource(doc));
			} else if (T == JarResource.class) {
				return (T) (new JarResource(doc));
			} else if (T == XPage.class) {
				return (T) (new XPage(doc));
			} else if (T == JavaScriptLibrary.class) {
				return (T) (new JavaScriptLibrary(doc));
			}
			return null;
		}
	*/
}
