package org.openntf.formula.function;

public class TextMatchLotus {
	/*----------------------------------------------------------------------------*/
	private static final char BACK_SLASH = '\\';
	private static final char FLAG_SPECIAL = 'S';
	private static final char ARB_STRING = '*';
	private static final char ARB_CHAR = '?';
	private static final char ARB_REPEAT = '+';
	private static final char BEGIN_ALTER = '{';
	private static final char BEGIN_NOT_ALTER = '!';
	private static final char END_ALTER = '}';
	private static final char FLAG_ALTER_AREA = '-';
	private static final char FLAG_NONSPEC = 'N';

	/*----------------------------------------------------------------------------*/
	private char[] iNormalPattern;
	private int iNPLength;
	private boolean iCaseMatches;	// if true, case-insensitive match test

	/*----------------------------------------------------------------------------*/
	public TextMatchLotus(final String pattern) {
		init(pattern, true);
		compileMatches(pattern);
	}

	/*----------------------------------------------------------------------------*/
	public TextMatchLotus(final String pattern, final char escape) {
		init(pattern, false);
		compileLike(pattern, escape);
	}

	/*----------------------------------------------------------------------------*/
	private void init(final String pattern, final boolean caseMatches) {
		iNormalPattern = new char[pattern.length() * 2 + 1];
		iNPLength = 0;
		iCaseMatches = caseMatches;
	}

	/*----------------------------------------------------------------------------*/
	private void compileMatches(final String pattern) {
		int lh = pattern.length();
		for (int i = 0; i < lh; i++) {
			char c = Character.toLowerCase(pattern.charAt(i));
			if (c == BACK_SLASH) {
				if (++i == lh)
					throwPatternException(pattern);
				iNormalPattern[iNPLength++] = FLAG_NONSPEC;
				iNormalPattern[iNPLength++] = pattern.charAt(i);
				continue;
			}
			if (c == ARB_REPEAT) {
				if (i + 1 == lh)
					throwPatternException(pattern);
				c = pattern.charAt(i + 1);
				if (c == ARB_STRING || c == ARB_CHAR) {
					i++;
					iNormalPattern[iNPLength++] = FLAG_SPECIAL;
					iNormalPattern[iNPLength++] = ARB_STRING;
					continue;
				}
				iNormalPattern[iNPLength++] = FLAG_SPECIAL;
				iNormalPattern[iNPLength++] = ARB_REPEAT;
				continue;
			}
			if (c == ARB_STRING) {
				iNormalPattern[iNPLength++] = FLAG_SPECIAL;
				iNormalPattern[iNPLength++] = ARB_STRING;
				continue;
			}
			if (c == ARB_CHAR) {
				iNormalPattern[iNPLength++] = FLAG_SPECIAL;
				iNormalPattern[iNPLength++] = ARB_CHAR;
				continue;
			}
			if (c != BEGIN_ALTER) {
				iNormalPattern[iNPLength++] = FLAG_NONSPEC;
				iNormalPattern[iNPLength++] = c;
				continue;
			}
			boolean notAlter = false;
			if (i + 2 >= lh)
				throwPatternException(pattern);
			if (pattern.charAt(++i) == BEGIN_NOT_ALTER) {
				notAlter = true;
				i++;
			}
			if (pattern.charAt(i) == END_ALTER)
				throwPatternException(pattern);
			iNormalPattern[iNPLength++] = FLAG_SPECIAL;
			iNormalPattern[iNPLength++] = notAlter ? BEGIN_NOT_ALTER : BEGIN_ALTER;
			for (;;) {
				c = pattern.charAt(i);
				if (c == END_ALTER) {
					iNormalPattern[iNPLength++] = FLAG_SPECIAL;
					iNormalPattern[iNPLength++] = END_ALTER;
					break;
				}
				if (++i == lh)
					throwPatternException(pattern);
				if (pattern.charAt(i) != '-') {
					iNormalPattern[iNPLength++] = FLAG_NONSPEC;
					iNormalPattern[iNPLength++] = c;
					continue;
				}
				iNormalPattern[iNPLength++] = FLAG_ALTER_AREA;
				iNormalPattern[iNPLength++] = c;
				if (++i == lh)
					throwPatternException(pattern);
				c = pattern.charAt(i);
				if (c == END_ALTER) {
					iNormalPattern[iNPLength++] = (char) 65500;
					continue;
				}
				iNormalPattern[iNPLength++] = c;
				i++;
			}
		}
	}

	/*----------------------------------------------------------------------------*/
	private static final char LIKE_ARB_STRING = '%';
	private static final char LIKE_ARB_CHAR = '_';

	/*----------------------------------------------------------------------------*/
	private void compileLike(final String pattern, final char escape) {
		int lh = pattern.length();
		for (int i = 0; i < lh; i++) {
			char c = pattern.charAt(i);
			if (escape != 0 && c == escape) {
				if (++i == lh)
					throwPatternException(pattern);
				iNormalPattern[iNPLength++] = FLAG_NONSPEC;
				iNormalPattern[iNPLength++] = pattern.charAt(i);
				continue;
			}
			if (c == LIKE_ARB_STRING) {
				iNormalPattern[iNPLength++] = FLAG_SPECIAL;
				iNormalPattern[iNPLength++] = ARB_STRING;
				continue;
			}
			if (c == LIKE_ARB_CHAR) {
				iNormalPattern[iNPLength++] = FLAG_SPECIAL;
				iNormalPattern[iNPLength++] = ARB_CHAR;
				continue;
			}
			iNormalPattern[iNPLength++] = FLAG_NONSPEC;
			iNormalPattern[iNPLength++] = c;
		}
	}

	/*----------------------------------------------------------------------------*/
	private void throwPatternException(final String pattern) {
		throw new IllegalArgumentException("Invalid text pattern: " + pattern);
	}

	/*----------------------------------------------------------------------------*/
	public boolean matches(final String what) {
		iTestStr = what;
		iTestStrLh = iTestStr.length();
		return matches(0, 0);
	}

	/*----------------------------------------------------------------------------*/
	String iTestStr;
	int iTestStrLh;
	int iNextNPPos;

	/*----------------------------------------------------------------------------*/
	private boolean matches(int testStrPos, int npPos) {
		while (npPos < iNPLength) {
			if (iNormalPattern[npPos] == FLAG_SPECIAL && iNormalPattern[npPos + 1] == ARB_STRING) {
				npPos += 2;
				if (npPos == iNPLength)
					return true;
				for (; testStrPos < iTestStrLh; testStrPos++)
					if (matches(testStrPos, npPos))
						return true;
				return false;
			}
			if (iNormalPattern[npPos] != FLAG_SPECIAL || iNormalPattern[npPos + 1] != ARB_REPEAT) {
				if (testStrPos >= iTestStrLh)
					return false;
				if (!matchesChar(iTestStr.charAt(testStrPos++), npPos))
					return false;
				npPos = iNextNPPos;
				continue;
			}
			npPos += 2;
			matchesChar('1', npPos);
			int newNPPos = iNextNPPos;
			if (matches(testStrPos, iNextNPPos))
				return true;
			while (testStrPos < iTestStrLh) {
				if (!matchesChar(iTestStr.charAt(testStrPos), npPos))
					break;
				if (matches(++testStrPos, iNextNPPos))
					return true;
			}
			npPos = newNPPos;
		}
		return testStrPos == iTestStrLh;
	}

	/*----------------------------------------------------------------------------*/
	private boolean matchesChar(final char which, int npPos) {
		if (iNormalPattern[npPos] == FLAG_NONSPEC) {
			iNextNPPos = npPos + 2;
			if (iCaseMatches)
				return Character.toLowerCase(which) == iNormalPattern[npPos + 1];
			else
				return (which == iNormalPattern[npPos + 1]);
		}
		if (iNormalPattern[npPos + 1] == ARB_CHAR) {
			iNextNPPos = npPos + 2;
			return true;
		}
		boolean notAlter = (iNormalPattern[npPos + 1] == BEGIN_NOT_ALTER);
		boolean treffer = false;
		for (npPos += 2;;) {
			if (iNormalPattern[npPos] == FLAG_NONSPEC) {
				if (!treffer)
					treffer = (which == iNormalPattern[npPos + 1]);
				npPos += 2;
				continue;
			}
			if (iNormalPattern[npPos] == FLAG_SPECIAL) {	// must be END_ALTER
				npPos += 2;
				break;
			}
			if (!treffer)
				treffer = (which >= iNormalPattern[npPos + 1] && which <= iNormalPattern[npPos + 2]);
			npPos += 3;
		}
		iNextNPPos = npPos;
		return notAlter ? !treffer : treffer;
	}
	/*----------------------------------------------------------------------------*/
}
