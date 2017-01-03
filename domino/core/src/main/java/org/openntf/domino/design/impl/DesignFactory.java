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

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.ext.NoteClass;

import com.ibm.commons.util.StringUtil;

/**
 * @author jgallagher
 * 
 */
public enum DesignFactory {
	INSTANCE;

	//public static final String DFLAGPAT_IMAGE_DBICON = "(+-*i~";

	public static final String DFLAGPAT_FOLDER_ALL_VERSIONS = "*F";
	public static final String DFLAGPAT_STYLEKIT = "*g`";
	public static final String DFLAGPAT_XSPPAGE = "*gK";
	public static final String DFLAGPAT_XSPCC = "*g;";
	public static final String DFLAGPAT_JAVAFILE = "*g[";
	public static final String DFLAGPAT_JAVARESOURCE = "(+K;[-*g";
	public static final String DFLAGPAT_WIDGET = "*g_";
	public static final String DFLAGPAT_FILE_DL = "(+g-~K[],;`_*";
	public static final String DFLAGPAT_FILE_HIDDEN = "(+-K[],;`*g~";
	public static final String DFLAGPAT_JAVAJAR = "*g,";
	//public static final String DFLAGPAT_FILE = "(+g-K;`"; //RPr: corrected. orignal IBM string: "+g-K;`"
	public static final String DFLAGPAT_NAVIGATORSWEB = "+G";
	public static final String DFLAGPAT_IMAGE_RESOURCES_DESIGN = "(+i-~*";
	public static final String DFLAGPAT_IMAGE_DBICON = "(+-*i~";
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
	public static final String DFLAGPAT_WEBSERVICE_LS = "(+{-j*";
	public static final String DFLAGPAT_WEBSERVICE_JAVA = "*{j";
	public static final String DFLAGPAT_SHARED_COLS = "(+-*^";
	public static final String DFLAGPAT_COMPAPP = "+|";
	public static final String DFLAGPAT_COMPDEF = "+:";
	public static final String DFLAGPAT_VIEWFORM_ALL_VERSIONS = "-FQMUGXWy#i@K;g~%z^}";

	//	public static final String DFLAGPAT_AGENTSLIST = "-QXstmz{";
	//	public static final String DFLAGPAT_VIEWFORM = "-FQMUGXWy#i@0nK;g~%z^";
	//	public static final String DFLAGPAT_FORMSWEB = "-U#Wi@y";
	//	public static final String DFLAGPAT_FIELD = "-FQMUGXWy#i@0nK;g~%z^";

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

	/**
	 * Builds a formula that matches on <code>pattern</code>
	 * 
	 * @param pattern
	 *            The flag pattern to search for
	 * @return A formula searching for the specified pattern
	 */
	public static String buildFlagFormula(final String pattern) {
		if (pattern.length() < 1)
			return "@False";

		StringBuilder sb = new StringBuilder();
		int i = 0;
		boolean first = true;
		switch (pattern.charAt(i++)) {

		case '+': // flags must contain ANY char of pattern
			sb.append("@Contains($FLAGS;");
			while (i < pattern.length()) {
				if (!first)
					sb.append(':');
				sb.append('"');
				sb.append(pattern.charAt(i++));
				sb.append('"');
				first = false;
			}
			sb.append(')');
			return sb.toString();

		case '-': // flags must NOT contain ANY char of pattern
			sb.append("!@Contains($FLAGS;");
			while (i < pattern.length()) {
				if (!first)
					sb.append(':');
				sb.append('"');
				sb.append(pattern.charAt(i++));
				sb.append('"');
				first = false;
			}
			sb.append(')');
			return sb.toString();

		case '*': // flags must contain ALL char of pattern
			sb.append('(');
			while (i < pattern.length()) {
				if (!first)
					sb.append('&');
				sb.append("@Contains($FLAGS;\"");
				sb.append(pattern.charAt(i++));
				sb.append("\")");
				first = false;
			}
			sb.append(')');
			return sb.toString();

		case '(': // combined rule

			// must follow with '+' "(+...."
			if (pattern.charAt(i++) != '+')
				return "@False";

			char ch = 0;
			// verify + rule and scan to next -
			sb.append("(@True");
			while (i < pattern.length()) {
				ch = pattern.charAt(i++);
				if (ch == '-') {
					if (!first)
						sb.append(")");
					break;
				}
				if (first) {
					sb.append("& @Contains($FLAGS;");
				} else {
					sb.append(':');
				}
				sb.append('"');
				sb.append(ch);
				sb.append('"');
				first = false;
			}

			// verify the negative list and scan to next '*'
			if (ch != '-')
				return "@False";

			first = true;
			while (i < pattern.length()) {
				ch = pattern.charAt(i++);
				if (ch == '*') {
					if (!first)
						sb.append(")");
					break;
				}
				if (first) {
					sb.append("& !@Contains($FLAGS;");
				} else {
					sb.append(':');
				}
				sb.append('"');
				sb.append(ch);
				sb.append('"');
				first = false;
			}

			if (ch != '*')
				return "@False";

			while (i < pattern.length()) {
				sb.append("& @Contains($FLAGS;\"");
				sb.append(pattern.charAt(i++));
				sb.append("\")");
			}
			sb.append(')');
			return sb.toString();

		}

		return "@False";
	}

	private DesignFactory() {
	}

	public static org.openntf.domino.design.DesignBase fromDocument(final Document doc) {
		if (doc.hasItem("IconBitmap") && doc.getNoteClass() == NoteClass.ICON)
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
			return new XspJavaResource(doc);
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
			if (testFlag(flagsExt, "+w")) {
				return new FileResourceWebContent(doc);
			} else {
				return new FileResourceHidden(doc);
			}
		}
		if (testFlag(flags, DFLAGPAT_NAVIGATORSWEB)) {
			return new Navigator(doc);
		}
		if (testFlag(flags, DFLAGPAT_IMAGE_RESOURCES_DESIGN)) {
			return new ImageResource(doc);
		}
		if (testFlag(flags, DFLAGPAT_IMAGE_DBICON)) {
			return new DbImage(doc);
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
			if (testFlag(doc.getItemValueString("$FlagsExt"), "+W"))
				return new WebServiceConsumerLS(doc);
			return new ScriptLibraryLS(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_JAVA)) {
			if (testFlag(doc.getItemValueString("$FlagsExt"), "+W"))
				return new WebServiceConsumerJava(doc);
			return new ScriptLibraryJava(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_JS)) {
			return new ScriptLibraryCSJS(doc);
		}
		if (testFlag(flags, DFLAGPAT_SCRIPTLIB_SERVER_JS)) {
			return new ScriptLibrarySSJS(doc);
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
		if (testFlag(flags, DFLAGPAT_WEBSERVICE_LS)) {
			return new WebServiceProviderLS(doc);
		}
		if (testFlag(flags, DFLAGPAT_WEBSERVICE_JAVA)) {
			return new WebServiceProviderJava(doc);
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

			if (doc.hasItem("$AssistType")) {
				Integer assist = doc.getItemValue("$AssistType", Integer.class);
				if (assist == 65413)
					return new DesignAgentLS(doc);
				if (assist == 65426)
					return new DesignAgentF(doc);
				if (assist == 65427) {
					if (testFlag(flags, "+J"))
						return new DesignAgentJ(doc);
					return new DesignAgentIJ(doc);
				}
				return new DesignAgentA(doc);
			}

			if (doc.hasItem("$FormulaClass")) { //View
				return new DesignView(doc);
			}
			if (doc.hasItem("$ACLDigest")) {
				return new ACLNote(doc);
			}

			// For these elements, we have to distinguish by NoteClass (which is expensive)
			switch (doc.getNoteClass()) {
			case FORM:
				return new DesignForm(doc);
			case HELPABOUTDOCUMENT:
				return new AboutDocument(doc);
			case HELPUSINGDOCUMENT:
				return new UsingDocument(doc);
			case REPLICATIONFORMULA:
				return new ReplicationFormula(doc);
			case SHAREDFIELD:
				return new SharedField(doc);
			default:
				break;
			}

		}
		// e.g. the $BEProfileR7
		return new OtherDesignElement(doc);
	}

	public static boolean isView(final Document doc) {
		return (doc.hasItem("$FormulaClass") || doc.hasItem("$Index") || doc.hasItem("$Collation") || doc.hasItem("$VIEWFORMAT")
				|| doc.hasItem("$Collection"));
	}

	public static final String NOTEID_ICONNOTE = "FFFF0010";

	public static boolean isIcon(final Document doc) {
		return NOTEID_ICONNOTE.equals(doc.getNoteID()) || doc.hasItem("IconBitmap");
	}

	public static boolean isACL(final Document doc) {
		return doc.hasItem("$ACLDigest");
	}

	public static Document getIconNote(final Database db) {
		return db.getIconNote();
	}

	public static <T extends org.openntf.domino.design.DesignBase> DesignCollection<T> search(final Database db, final Class<T> type,
			final String filter) {
		NoteCollection coll = db.createNoteCollection(false);

		String selectFormula = "@All";
		if (!StringUtil.isEmpty(filter)) {
			selectFormula = "(" + filter + ")";
		}

		if (org.openntf.domino.design.IconNote.class.isAssignableFrom(type)) {
			coll.setSelectIcon(true);

		} else if (org.openntf.domino.design.DesignView.class.isAssignableFrom(type)) {
			// Hmm. A view is also a folder.
			coll.setSelectViews(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_VIEWFORM_ALL_VERSIONS);

		} else if (org.openntf.domino.design.Folder.class.isAssignableFrom(type)) {
			coll.setSelectFolders(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_FOLDER_ALL_VERSIONS);

		} else if (org.openntf.domino.design.Theme.class.isAssignableFrom(type)) {
			// a Theme is considered as file resource
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_STYLEKIT);

		} else if (org.openntf.domino.design.XspJavaResource.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_JAVAFILE);

		} else if (org.openntf.domino.design.CompositeComponent.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_WIDGET);

		} else if (org.openntf.domino.design.JarResource.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_JAVAJAR);

		} else if (org.openntf.domino.design.FileResource.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_FILE_DL);

		} else if (org.openntf.domino.design.FileResourceHidden.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_FILE_HIDDEN) + " & !@Contains($FlagsExt;{w})";

		} else if (org.openntf.domino.design.FileResourceWebContent.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_FILE_HIDDEN) + " & @Contains($FlagsExt;{w})";

		} else if (org.openntf.domino.design.Navigator.class.isAssignableFrom(type)) {
			coll.setSelectNavigators(true);

		} else if (org.openntf.domino.design.ImageResource.class.isAssignableFrom(type)) {
			coll.setSelectImageResources(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_IMAGE_RESOURCES_DESIGN);

		} else if (org.openntf.domino.design.DataConnectionResource.class.isAssignableFrom(type)) {
			coll.setSelectDataConnections(true);

		} else if (org.openntf.domino.design.Outline.class.isAssignableFrom(type)) {
			coll.setSelectOutlines(true);

		} else if (org.openntf.domino.design.SavedQuery.class.isAssignableFrom(type)) {
			coll.selectAllDesignElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_QUERY_V4_OBJECT);

		} else if (org.openntf.domino.design.WebServiceConsumer.class.isAssignableFrom(type)) {
			coll.setSelectScriptLibraries(true);

			selectFormula += " & @Contains($FlagsExt;{W})";
			if (org.openntf.domino.design.impl.WebServiceConsumerLS.class.isAssignableFrom(type)) {
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_LS);
			} else if (org.openntf.domino.design.impl.WebServiceConsumerJava.class.isAssignableFrom(type)) {
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_JAVA);
			}

		} else if (org.openntf.domino.design.ScriptLibrary.class.isAssignableFrom(type)) {
			if (org.openntf.domino.design.ScriptLibraryLS.class.isAssignableFrom(type)) {
				coll.setSelectScriptLibraries(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_LS) + " & !@Contains($FlagsExt;{W})";

			} else if (org.openntf.domino.design.ScriptLibraryJava.class.isAssignableFrom(type)) {
				coll.setSelectScriptLibraries(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_JAVA) + " & !@Contains($FlagsExt;{W})";

			} else if (org.openntf.domino.design.ScriptLibraryCSJS.class.isAssignableFrom(type)) {
				coll.setSelectScriptLibraries(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_JS);

			} else if (org.openntf.domino.design.ScriptLibrarySSJS.class.isAssignableFrom(type)) {
				coll.setSelectScriptLibraries(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_SERVER_JS);
			} else {
				// ALL script libraries:
				coll.setSelectScriptLibraries(true);
				selectFormula += " & ((" + buildFlagFormula(DFLAGPAT_SCRIPTLIB_LS) + " & !@Contains($FlagsExt;{W}))";
				selectFormula += " | (" + buildFlagFormula(DFLAGPAT_SCRIPTLIB_JAVA) + " & !@Contains($FlagsExt;{W}))";
				selectFormula += " | ( " + buildFlagFormula(DFLAGPAT_SCRIPTLIB_JS) + ")";
				selectFormula += " | (" + buildFlagFormula(DFLAGPAT_SCRIPTLIB_SERVER_JS) + "))";
			}

		} else if (org.openntf.domino.design.DatabaseScript.class.isAssignableFrom(type)) {
			coll.setSelectDatabaseScript(true);

		} else if (org.openntf.domino.design.Subform.class.isAssignableFrom(type)) {
			coll.setSelectSubforms(true);

		} else if (org.openntf.domino.design.DesignPage.class.isAssignableFrom(type)) {
			coll.setSelectPages(true);

		} else if (org.openntf.domino.design.AgentData.class.isAssignableFrom(type)) {
			coll.setSelectMiscCodeElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_AGENT_DATA);

		} else if (org.openntf.domino.design.SharedActions.class.isAssignableFrom(type)) {
			coll.setSelectActions(true);

		} else if (org.openntf.domino.design.DB2View.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_DB2ACCESSVIEW);

		} else if (org.openntf.domino.design.Frameset.class.isAssignableFrom(type)) {
			coll.setSelectFramesets(true);

		} else if (org.openntf.domino.design.DesignApplet.class.isAssignableFrom(type)) {
			coll.setSelectJavaResources(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_APPLET_RESOURCE);

		} else if (org.openntf.domino.design.StyleSheet.class.isAssignableFrom(type)) {
			coll.setSelectStylesheetResources(true);
			//selectFormula += " & " + buildFlagFormula(DFLAGPAT_STYLE_SHEET_RESOURCE);

		} else if (org.openntf.domino.design.WebServiceProvider.class.isAssignableFrom(type)) {
			coll.setSelectMiscCodeElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_WEBSERVICE);
			if (org.openntf.domino.design.impl.WebServiceProviderLS.class.isAssignableFrom(type)) {
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_WEBSERVICE_LS);
			} else if (org.openntf.domino.design.impl.WebServiceProviderJava.class.isAssignableFrom(type)) {
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_WEBSERVICE_JAVA);
			}
		} else if (org.openntf.domino.design.SharedColumn.class.isAssignableFrom(type)) {
			coll.setSelectMiscIndexElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_SHARED_COLS);

		} else if (org.openntf.domino.design.CompositeApp.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_COMPAPP);

		} else if (org.openntf.domino.design.CompositeWiring.class.isAssignableFrom(type)) {
			coll.setSelectMiscFormatElements(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_COMPDEF);

		} else if (org.openntf.domino.design.DbImage.class.isAssignableFrom(type)) {
			coll.setSelectImageResources(true);
			selectFormula += " & " + buildFlagFormula(DFLAGPAT_IMAGE_DBICON);

			// Agents
		} else if (org.openntf.domino.design.DesignAgent.class.isAssignableFrom(type)) {
			coll.setSelectAgents(true);
			// argh... why is this soooo complex, IBM?
			if (org.openntf.domino.design.impl.DesignAgentF.class.isAssignableFrom(type)) {
				selectFormula += " & $AssistType = 65426";
			} else if (org.openntf.domino.design.impl.DesignAgentLS.class.isAssignableFrom(type)) {
				selectFormula += " & $AssistType = 65413";
			} else if (org.openntf.domino.design.impl.DesignAgentA.class.isAssignableFrom(type)) {
				selectFormula += " & !($AssistType = 65413:65426:65427)";
			} else if (org.openntf.domino.design.impl.DesignAgentJ.class.isAssignableFrom(type)) {
				selectFormula += " & $AssistType = 65427 & " + buildFlagFormula("+J");
			} else if (org.openntf.domino.design.impl.DesignAgentIJ.class.isAssignableFrom(type)) {
				selectFormula += " & $AssistType = 65427 & " + buildFlagFormula("-J");
			}

		} else if (org.openntf.domino.design.UsingDocument.class.isAssignableFrom(type)) {
			coll.setSelectHelpUsing(true);

		} else if (org.openntf.domino.design.AboutDocument.class.isAssignableFrom(type)) {
			coll.setSelectHelpAbout(true);

		} else if (org.openntf.domino.design.ACLNote.class.isAssignableFrom(type)) {
			coll.setSelectAcl(true);

		} else if (org.openntf.domino.design.SharedField.class.isAssignableFrom(type)) {
			coll.setSelectSharedFields(true);

		} else if (org.openntf.domino.design.ReplicationFormula.class.isAssignableFrom(type)) {
			coll.setSelectReplicationFormulas(true);

		} else if (org.openntf.domino.design.DesignForm.class.isAssignableFrom(type)) {
			coll.setSelectForms(true);

		} else if (org.openntf.domino.design.XspResource.class.isAssignableFrom(type)) {
			if (org.openntf.domino.design.XPage.class.isAssignableFrom(type)) {
				coll.setSelectMiscFormatElements(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_XSPPAGE);

			} else if (org.openntf.domino.design.CustomControl.class.isAssignableFrom(type)) {
				coll.setSelectMiscFormatElements(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_XSPCC);

			} else if (org.openntf.domino.design.XspJavaResource.class.isAssignableFrom(type)) {
				coll.setSelectMiscFormatElements(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_JAVAFILE);
			} else {
				coll.setSelectMiscFormatElements(true);
				selectFormula += " & " + buildFlagFormula(DFLAGPAT_JAVARESOURCE);
			}
		} else {
			throw new IllegalArgumentException("Class " + type.getName() + " is unsupported");
		}

		//		System.out.println(selectFormula);
		//		System.out.println(type);
		coll.setSelectionFormula(selectFormula);
		coll.buildCollection();
		return new DesignCollection<T>(coll, type);

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
