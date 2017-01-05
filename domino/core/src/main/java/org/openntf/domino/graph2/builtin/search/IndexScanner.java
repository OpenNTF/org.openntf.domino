package org.openntf.domino.graph2.builtin.search;

import java.util.Set;

import org.openntf.domino.Session;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.types.CaseInsensitiveString;

public class IndexScanner extends DocumentScanner {

	public IndexScanner() {
		setCaseSensitive(false);
		setTrackValueLocation(true);
		setTrackTokenLocation(true);
		setTrackNameLocation(true);
	}

	public IndexScanner(final Set<CharSequence> stopTokenList) {
		super(stopTokenList);
		setCaseSensitive(false);
		setTrackValueLocation(true);
		setTrackTokenLocation(true);
		setTrackNameLocation(true);
	}

	@Override
	protected void processToken(final CharSequence token, final CharSequence itemName, final String address) {
		super.processToken(token, itemName, address);
	}

	@Override
	protected void processName(final CharSequence name, final CharSequence itemName, final Session session, final String address) {
		super.processName(name, itemName, session, address);
	}

	@Override
	public void processTextValue(final CaseInsensitiveString name, final Object value, final String address) {
		super.processTextValue(name, value, address);
	}

	@Override
	public void processValue(final CaseInsensitiveString name, final Object value, final String address) {
		super.processValue(name, value, address);
	}

}
