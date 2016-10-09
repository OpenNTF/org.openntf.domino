package org.openntf.domino.nsfdata.structs;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Corresponds to DESIGN_FLAG_* from stdnames.h
 * 
 * @author jgallagher
 *
 */
public enum DesignFlag {
	ADD('A'), ANTIFOLDER('a'), BACKGROUND_FILTER('B'), INITBYDESIGNONLY('b'), NO_COMPOSE('C'), CALENDAR_VIEW('c'), NO_QUERY('D'), DEFAULT_DESIGN(
			'd'), MAIL_FILTER('E'), PUBLICANTIFOLDER('e'), FOLDER_VIEW('F'), V4AGENT('f'), VIEWMAP('G'), FILE('g'), OTHER_DLG('H'), JAVASCRIPT_LIBRARY(
					'h'), V4PASTE_AGENT('I'), IMAGE_RESOURCE('i'), JAVA_AGENT('J'), JAVA_AGENT_WITH_SOURCE('j'), MOBILE_DIGEST('K'), XSPPAGE('K'), CONNECTION_RESOURCE(
			'k'), LOTUSSCRIPT_AGENT('L'), DELETED_DOCS('l'), QUERY_MACRO_FILtER('M'), SITEMAP('m'), NEW('N'), HIDE_FROM_NOTES('n'), QUERY_V4_OBJECT(
			'O'), PRIVATE_STOREDISK('o'), PRESERVE('P'), PRIVATE_1STUSE('p'), QUERY_FILTER('Q'), AGENT_SHOWINSEARCH('q'), REPLACE_SPECIAL(
			'R'), PROPAGATE_NOCHANGE('r'), V4BACKGROUND_MACRO('S'), SCRIPTLIB('s'), VIEW_CATEGORIZED('T'), DATABASESCRIPT('t'), SUBFORM('U'), AGENT_RUNASWEBUSER(
			'u'), AGENT_RUNASINVOKER('u'), PRIVATE_IN_DB('V'), IMAGE_WELL('v'), WEBPAGE('W'), HIDE_FROM_WEB('w'), V4AGENT_DATA('X'), SUBFORM_NORENDER(
			'x'), NO_MENU('Y'), SACTIONS('y'), MULTILINGUAL_PRESERVE_HIDDEN('Z'), SERVLET('z'), ACCESSVIEW('z'), FRAMESET('#'), MULTILINGUAL_ELEMENT(
			'!'), JAVA_RESOURCE('@'), STYLESHEET_RESOURCE('='), WEBSERVICE('{'), SHARED_COL('^'), HIDE_FROM_MOBILE('1'), HIDE_FROM_PORTAL(
			'2'), PROPFILE('2'), HIDE_FROM_V3('3'), HIDE_FROM_V4('4'), HIDE_FROM_V5('5'), HIDE_FROM_V6('6'), HIDE_FROM_V7('7'), HIDE_FROM_V8(
			'8'), HIDE_FROM_V9('9'), MULTILINGUAL_HIDE('0'), WEBHYBRIDDB('%'), READONLY('&'), NEEDSREFRESH('$'), HTMLFILE('>'), JSP('<'), QUERYVIEW(
			'<'), DIRECTORY('/'), PRINTFORM('?'), HIDEFROMDESIGNLIST('~'), HIDEONLYFROMDESIGNLIST('}'), COMPOSITE_APP('|'), COMPOSITE_DEF(
			':'), XSP_CC(';'), JS_SERVER('.'), STYLEKIT('`'), WIDGET('_'), JAVAFILE('[');

	public static final Set<DesignFlag> FLAGS_SUBCLASS = Collections.unmodifiableSet(valuesOf("UW#yi@GFXstmzk=K;g%[]{^,"));
	public static final Set<DesignFlag> FLAGS_DISTINGUISH = Collections.unmodifiableSet(valuesOf("nw123456789"));

	private final char character_;

	private DesignFlag(final char character) {
		character_ = character;
	}

	public char getCharacter() {
		return character_;
	}

	public static Set<DesignFlag> valuesOf(final char character) {
		Set<DesignFlag> result = EnumSet.noneOf(DesignFlag.class);
		for (DesignFlag flag : values()) {
			if (character == flag.getCharacter()) {
				result.add(flag);
			}
		}
		if (result.isEmpty()) {
			throw new IllegalArgumentException("Unknown flag character '" + character + "'");
		} else {
			return result;
		}
	}

	public static Set<DesignFlag> valuesOf(final String pattern) {
		Set<DesignFlag> result = EnumSet.noneOf(DesignFlag.class);
		for (int i = 0; i < pattern.length(); i++) {
			result.addAll(valuesOf(pattern.charAt(i)));
		}
		return result;
	}
}
