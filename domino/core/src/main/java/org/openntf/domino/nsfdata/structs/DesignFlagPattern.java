package org.openntf.domino.nsfdata.structs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum DesignFlagPattern {
	V4SEARCHBAR("(+Qq-Bst5nmz*"), SEARCHBAR("(+QM-st5nmz*"), VIEWFORM("-FQMUGXWy#i:|@0nK;g~%z^"), VIEWFORM_MENUABLE(
			"-FQMUGXWy#i:|@40nK;g~%z^}"), VIEWFORM_ALL_VERSIONS("-FQMUGXWy#i:|@K;g~%z^}"), FORM("-FQMUGXWy#i:|@0nK;g~%z^"), FORM_OR_SIMILAR(
					"-FMGXy#i=:|@0K;g~%z^"), FORM_OR_PAGE("-FQMUGXy#i:|@0nK;g~%z^"), FORM_MENUABLE("-FQMUGXWy#i:|@40nK;g~%z^}"), FORM_ALL_VERSIONS(
							"-FQMUGXWy#i:|@K;g~%z^}"), PRINTFORM_ALL_VERSIONS("+?"), FIELD("-FQMUGXWy#i@0nK;g~%z^"), FIELD_ALL_VERSIONS(
									"-FQMUGXWy#i@K;g~%z^}"), TOOLSRUNMACRO("-QXMBESIst5nmz{"), AGENTSLIST("-QXstmz{"), PASTEAGENTS("+I"), SCRIPTLIB("+sh."), SCRIPTLIB_LS(
			"(+s-jh.*"), SCRIPTLIB_JAVA("*sj"), SCRIPTLIB_JS("+h"), SCRIPTLIB_SERVER_JS("+."), DATABASESCRIPT("+t"), SUBFORM("(+U-40n*"), SUBFORM_DESIGN(
			"(+U-40*"), SUBFORM_ALL_VERSIONS("+U"), DBRUNMACRO("+BS"), COMPOSE("-C40n"), NOHIDDENNOTES("-n"), NOHIDEDENWEB("-w"), QUERYBYFORM(
															"-DU40gnyz{:|"), PRESERVE("+P"), SUBADD("(+-40*UA"), SUBNEW("(+-40*UN"), VIEW("-FG40n^"), VIEWMENUABLE(
																	"-FQMUGXWy#i@40nK;g~%z^}"), VIEW_ALL_VERSIONS("-FG^"), VIEW_DESIGN("-FG40^"), NOTHIDDEN("-40n"), FOLDER("(+-04n*F"), FOLDER_DESIGN(
																			"(+-04*F"), FOLDER_ALL_VERSIONS("*F"), CALENDAR("*c"), SHAREDVIEWS("-FGV^40n"), SHAREDVIEWSFOLDERS("-G^V40p"), SHAREDWEBVIEWS(
			"-FGV40wp^"), SHAREDWEBVIEWSFOLDERS("-GV40wp^"), VIEWS_AND_FOLDERS("-G40n^"), VIEWS_AND_FOLDERS_DESIGN("-G40^"), SHARED_COLS(
			"(+-*^"), VIEWMAP("(+-04n*G"), VIEWMAP_ALL_VERSIONS("*G"), VIEWMAPWEB("(+-04w*G"), VIEWMAP_DESIGN("(+-04*G"), WEBPAGE("(+-*W"), WEBPAGE_NOTES(
			"(+W-n*"), WEBPAGE_WEB("(+W-w*"), OTHER_DLG("(+-04n*H"), CATEGORIZED_VIEW("(+-04n*T"), DEFAULT_DESIGN("+d"), FRAMESET("(+-*#"), FRAMESET_NOTES(
			"(+#-n*"), FRAMESET_WEB("(+#-w*"), SITEMAP("+m"), SITEMAP_NOTES("(+m-n*"), SITEMAP_WEB("(+m-w*"), IMAGE_RESOURCE("+i"), IMAGE_RES_NOTES(
			"(+i-n~*"), IMAGE_RES_WEB("(+i-w~*"), IMAGE_WELL_RESOURCE("(+-*iv"), IMAGE_WELL_NOTES("(+-n*iv"), IMAGE_WELL_WEB("(+-w*iv"), JAVAFILE(
			"(+-*g["), JAVA_RESOURCE("+@"), JAVA_RESOURCE_NOTES("(+@-n*"), JAVA_RESOURCE_WEB("(+@-w*"), XSPPAGE("*gK"), XSPPAGE_WEB(
																																	"(+-w*gK"), XSPPAGE_NOTES("(+-n*gK"), XSPPAGE_NOPROPS("(+-2*gK"), NOPROPS_WEB("(+-2w*gK"), NOPROPS_NOTES("(+-2n*gK"), XSPCC(
																																			"*g;"), XSPCC_WEB("(+-w*g;"), XSPCC_NOTES("(+-n*g;"), DATA_CONNECTION_RESOURCE("+k"), DB2ACCESSVIEW("+z"), STYLE_SHEET_RESOURCE(
																																					"+="), STYLE_SHEETS_NOTES("(+=-n*"), STYLE_SHEETS_WEB("(+=-w*"), FILE("+g-K[];`,"), FILE_DL("(+g-~K[];`,*"), FILE_NOTES(
			"(+g-K[];n`,*"), FILE_WEB("(+g-K[];w`,*"), HTMLFILES("(+-*g>"), HTMLFILES_NOTES("(+-n*g>"), HTMLFILES_WEB("(+-w*g>"), FILE_ELEMS(
			"(+gi|=-/[],*"), SERVLET("+z"), SERVLET_NOTES("(+z-n*"), SERVLET_WEB("(+z-w*"), WEBSERVICE("+{"), JAVA_WEBSERVICE("(+Jj-*{"), LS_WEBSERVICE(
			"*{L"), JSP("(+-*g<"), STYLEKIT("(+-*g`"), STYLEKIT_NOTES("(+-n*g`"), STYLEKIT_WEB("(+-w*g`"), WIDGET("(+-*g_"), WIDGET_NOTES(
																																													"(+-n*g_"), WEDGET_WEB("(+-w*g_"), SACTIONS_DESIGN("+y"), SACTIONS_WEB("(+-0*y"), SACTIONS_NOTES("(+-0*y"), COMPDEF("+:"), COMPAPP(
																																															"+|"), NONWEB("+w80stVXp^"), NONWEB_EXCLUDE("-w80stVXp^"), AGENTSWEB("(+-QXstmz{*"), AGNTORWEBSVCWEB("(+-QXstmz*"), WEBSERVICEWEB(
																																																	"+{"), FORMSWEB("-U#Wi@y:|"), SUBFORMSWEB("+U"), FRAMESETSWEB("+#"), PAGESWEB("+W"), VIEWSWEB("-G"), NAVIGATORSWEB("+G"), SHAREDFIELDSWEB(
																																																			"*"), ALLWEB("*"), NO_FILERES_DIRS("-/"), FIRSTPATTERNCHAR("(+-*"), WEBHYBRIDDB("+%");

	private final String value_;
	private final Set<Character> any_;
	private final Set<Character> none_;
	private final Set<Character> all_;

	@SuppressWarnings("null")
	private DesignFlagPattern(final String value) {
		value_ = value;

		Set<Character> any = null;
		Set<Character> none = null;
		Set<Character> all = null;

		Set<Character> currentBin = null;
		for (int i = 0; i < value_.length(); i++) {
			char current = value_.charAt(i);
			switch (current) {
			case '(':
				// Technically, this indicates that we'll be expecting a combination,
				// but parsing behavior is the same whether or not it's there
				break;

			// One of these will come before any flag characters. If present, create
			// the bin and switch the current one for future events
			case '+':
				any = new HashSet<Character>();
				currentBin = any;
				break;
			case '-':
				none = new HashSet<Character>();
				currentBin = none;
				break;
			case '*':
				all = new HashSet<Character>();
				currentBin = all;
				break;

			// Add the flag character to the current bin
			default:
				currentBin.add(current);
			}
		}

		any_ = any == null ? null : Collections.unmodifiableSet(any);
		none_ = none == null ? null : Collections.unmodifiableSet(none);
		all_ = all == null ? null : Collections.unmodifiableSet(all);
	}

	public String getValue() {
		return value_;
	}

	/**
	 * From stdnames.h:
	 * 
	 * '+' = ANY of the flags, '-' = NONE of the flags, '*' = ALL of the flags
	 * 
	 * '(+-*' = a combination of the above.
	 * 
	 * Example: "(+AB-C*DE" = (A OR B) AND (NOT C) AND (D AND E)
	 * 
	 * Note: be sure to have +-* placeholders even if no flags for some. ie: "(+-C*DE" = (NOT C) AND (D AND E).
	 * 
	 * Note: "(+-Q*" is equivalent to "-Q"
	 *
	 * @param flags
	 *            A Set of DesignFlags from the note to match
	 * @return Whether or not the provided Flags meet the pattern's criteria
	 */
	public boolean matches(final Set<DesignFlag> flags) {
		Set<Character> flagChars = new HashSet<Character>();
		for (DesignFlag flag : flags) {
			flagChars.add(flag.getCharacter());
		}

		if (any_ != null && !any_.isEmpty()) {
			// Make sure flags contains at least one of any's contents
			boolean matched = false;
			for (char matcher : any_) {
				if (flagChars.contains(matcher)) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				return false;
			}
		}

		if (none_ != null) {
			// Make sure flags contains none of none's contents
			for (char matcher : none_) {
				if (flagChars.contains(matcher)) {
					return false;
				}
			}
		}

		if (all_ != null) {
			// All is easy
			if (!flagChars.containsAll(all_)) {
				return false;
			}
		}

		// If we're here, we must match
		return true;
	}

}
