package org.openntf.formula.impl;

public class StringSplitSimple {

	public enum SpecialSplit {
		BlankOrTab, AnyWhiteSpace, None;
	}

	private char[] _whatToSplit = null;
	private char _splitterC;
	private String _splitterS = null;
	private SpecialSplit _specialSplitter = SpecialSplit.None;
	private int _splitterLh;

	public StringSplitSimple(final String whatToSplit, final char splitter) {
		setWhatToSplit(whatToSplit);
		_splitterC = splitter;
		_splitterLh = 1;
	}

	private void setWhatToSplit(final String whatToSplit) {
		if (whatToSplit != null && !whatToSplit.isEmpty())
			_whatToSplit = whatToSplit.toCharArray();
	}

	public StringSplitSimple(final String whatToSplit, final String splitter) {
		if (splitter == null || (_splitterLh = splitter.length()) == 0)
			throw new IllegalArgumentException("Splitter mustn't be NULL or EMPTY");
		setWhatToSplit(whatToSplit);
		_splitterS = splitter;
		_splitterC = splitter.charAt(0);
	}

	public StringSplitSimple(final String whatToSplit, final SpecialSplit specialSplit) {
		if (specialSplit == SpecialSplit.None)
			throw new IllegalArgumentException("Invalid SpecialSplit");
		setWhatToSplit(whatToSplit);
		_specialSplitter = specialSplit;
		_splitterLh = 1;
	}

	private int _numSplits;
	private int[] _splitBegs;
	private int[] _splitEnds;

	public int split(final boolean includeEmpties) {
		if (_whatToSplit == null)
			return 0;
		int i = _whatToSplit.length >>> 2;
		if (i < 4)
			i = 4;
		else if (i > 1024)
			i = 1024;
		_splitBegs = new int[i];
		_splitEnds = new int[i];
		_splitBegs[0] = 0;
		_numSplits = 0;
		for (i = _splitBegs[_numSplits]; i < _whatToSplit.length; i++) {
			if (_specialSplitter == SpecialSplit.None) {
				if (_whatToSplit[i] != _splitterC)
					continue;
				if (_splitterLh > 1 && !testVsSplitterS(i))
					continue;
			} else if (_specialSplitter == SpecialSplit.BlankOrTab) {
				if (_whatToSplit[i] != ' ' && _whatToSplit[i] != '\t')
					continue;
			} else { // if (_specialSplitter == SpecialSplit.AnyWhiteSpace
				if (!Character.isWhitespace(_whatToSplit[i]))
					continue;
			}
			if (_numSplits + 1 >= _splitBegs.length)
				reallocBegEnd();
			_splitEnds[_numSplits++] = i;
			_splitBegs[_numSplits] = i + _splitterLh;
			i = _splitBegs[_numSplits] - 1;
		}
		_splitEnds[_numSplits++] = i;
		return _numSplits;
	}

	private boolean testVsSplitterS(final int i) {
		for (int j = 1; j < _splitterLh; j++)
			if (i + j >= _whatToSplit.length || _whatToSplit[i + j] != _splitterS.charAt(j))
				return false;
		return true;
	}

	private void reallocBegEnd() {
		int[] newBegs = new int[_numSplits << 1];
		int[] newEnds = new int[_numSplits << 1];
		System.arraycopy(_splitBegs, 0, newBegs, 0, _numSplits + 1);
		_splitBegs = newBegs;
		System.arraycopy(_splitEnds, 0, newEnds, 0, _numSplits + 1);
		_splitEnds = newEnds;
	}

	public String getSplitN(final int n) {
		return getSplitN(n, false);
	}

	public String getSplitN(final int n, final boolean trimIt) {
		if (n >= _numSplits || n < 0)
			return "";
		int beg = _splitBegs[n];
		int end = _splitEnds[n];
		if (trimIt) {
			for (; beg < end; beg++)
				if (!Character.isWhitespace(_whatToSplit[beg]))
					break;
			if (beg < end) {
				for (end--; Character.isWhitespace(_whatToSplit[end]); end--)
					;
				end++;
			}
		}
		return new String(_whatToSplit, beg, end - beg);
	}

}
