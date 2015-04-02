package org.openntf.domino.design.impl;

public enum DesignFlags {
	;
	public static final String DFLAGPAT_AGENT_DATA = "+X";
	public static final String DFLAGPAT_AGENTSLIST = "-QXstmz{";
	public static final String DFLAGPAT_AGENTSLIST_JAVA = "(+J-QXstmz{*";
	public static final String DFLAGPAT_AGENTSLIST_IMPORTED_JAVA = "-JQXstmz{";
	public static final String DFLAGPAT_APPLET_RESOURCE = "+@";
	public static final String DFLAGPAT_COMPAPP = "+|";
	public static final String DFLAGPAT_COMPDEF = "+:";
	public static final String DFLAGPAT_DATA_CONNECTION_RESOURCE = "+k";
	public static final String DFLAGPAT_DATABASESCRIPT = "+t";
	public static final String DFLAGPAT_DB2ACCESSVIEW = "+z";
	public static final String DFLAGPAT_FILE_DL = "(+g-~K[],;`_*";
	public static final String DFLAGPAT_FILE_HIDDEN = "(+-K[],;`*g~";
	public static final String DFLAGPAT_FOLDER_ALL_VERSIONS = "*F";
	public static final String DFLAGPAT_FRAMESET = "(+-*#";
	public static final String DFLAGPAT_FRAMESETSWEB = "+#";
	public static final String DFLAGPAT_IMAGE_RESOURCES_DESIGN = "(+i-~*";
	public static final String DFLAGPAT_IMAGE_DBICON = "(+-*i~";
	public static final String DFLAGPAT_JAVAFILE = "*g[";
	public static final String DFLAGPAT_JAVAJAR = "*g,";
	public static final String DFLAGPAT_JAVARESOURCE = "(+K;[-*g";
	public static final String DFLAGPAT_NAVIGATORSWEB = "+G";
	public static final String DFLAGPAT_PAGE = "+W";
	public static final String DFLAGPAT_PAGESWEB = "+W";
	public static final String DFLAGPAT_SACTIONS_DESIGN = "+y";
	public static final String DFLAGPAT_SCRIPTLIB = "+h.s";
	public static final String DFLAGPAT_SCRIPTLIB_LS = "(+s-jh.*";
	public static final String DFLAGPAT_SCRIPTLIB_JAVA = "*sj";
	public static final String DFLAGPAT_SCRIPTLIB_JS = "+h";
	public static final String DFLAGPAT_SCRIPTLIB_SERVER_JS = "+.";
	public static final String DFLAGPAT_SHARED_COLS = "(+-*^";
	public static final String DFLAGPAT_STYLE_SHEET_RESOURCE = "+=";
	public static final String DFLAGPAT_SUBFORMSWEB = "+U";
	public static final String DFLAGPAT_QUERY_V4_OBJECT = "+O";
	public static final String DFLAGPAT_SITEMAP = "+m";
	public static final String DFLAGPAT_STYLEKIT = "*g`";
	public static final String DFLAGPAT_SUBFORM_ALL_VERSIONS = "+U";
	public static final String DFLAGPAT_WEBSERVICE = "+{";
	public static final String DFLAGPAT_WEBSERVICE_LS = "(+{-j*";
	public static final String DFLAGPAT_WEBSERVICE_JAVA = "*{j";
	public static final String DFLAGPAT_WIDGET = "*g_";
	public static final String DFLAGPAT_XSPPAGE = "*gK";
	public static final String DFLAGPAT_XSPCC = "*g;";
	//public static final String DFLAGPAT_FILE = "(+g-K;`"; //RPr: corrected. orignal IBM string: "+g-K;`"
	public static final String DFLAGPAT_VIEWFORM_ALL_VERSIONS = "-FQMUGXWy#i@K;g~%z^}"; // =:|
	public static final String DFLAGPAT_VIEWFORMFOLDER = "-QMGXWy#i@K;g~%z^}";
	public static final String DFLAGEXTPAT_WEBCONTENTFILE = "+w";
	public static final String DFLAGEXTPAT_NO_WEBCONTENTFILE = "-w";
	public static final String DFLAGEXTPAT_WEBSERVICELIB = "+W";
	public static final String DFLAGEXTPAT_NO_WEBSERVICELIB = "-W";

	public static final int SIG_ACTION_FORMULA = (132 | 0xFF00);
	public static final int SIG_ACTION_LOTUSSCRIPT = (133 | 0xFF00);
	public static final int SIG_ACTION_FORMULAONLY = (146 | 0xFF00);
	public static final int SIG_ACTION_JAVAAGENT = (147 | 0xFF00);

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
	 *            The pattern that should be applied.
	 * @return The Flags as String.
	 */
	public static String buildFlagFormula(final String flagField, final String pattern) {
		if (pattern.length() < 1)
			return "@False";

		StringBuilder sb = new StringBuilder();
		int i = 0;
		boolean first = true;
		switch (pattern.charAt(i++)) {

		case '+': // flags must contain ANY char of pattern
			sb.append("@Contains(" + flagField + ";");
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
			sb.append("!@Contains(" + flagField + ";");
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
				sb.append("@Contains(" + flagField + ";\"");
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
					sb.append("& @Contains(" + flagField + ";");
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
					sb.append("& !@Contains(" + flagField + ";");
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
				sb.append("& @Contains(" + flagField + ";\"");
				sb.append(pattern.charAt(i++));
				sb.append("\")");
			}
			sb.append(')');
			return sb.toString();

		}

		return "@False";
	}

}
