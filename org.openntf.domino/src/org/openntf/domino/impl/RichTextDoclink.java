package org.openntf.domino.impl;

import lotus.domino.NotesException;
import lotus.domino.RichTextStyle;

import org.openntf.domino.utils.DominoUtils;

public class RichTextDoclink extends Base<org.openntf.domino.RichTextDoclink, lotus.domino.RichTextDoclink> implements
		org.openntf.domino.RichTextDoclink {

	public RichTextDoclink(lotus.domino.RichTextDoclink delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String getDBReplicaID() {
		try {
			return getDelegate().getDBReplicaID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getDisplayComment() {
		try {
			return getDelegate().getDisplayComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getDocUnID() {
		try {
			return getDelegate().getDocUnID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHotSpotText() {
		try {
			return getDelegate().getHotSpotText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public RichTextStyle getHotSpotTextStyle() {
		try {
			return getDelegate().getHotSpotTextStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getServerHint() {
		try {
			return getDelegate().getServerHint();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getViewUnID() {
		try {
			return getDelegate().getViewUnID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDBReplicaID(String replicaId) {
		try {
			getDelegate().setDBReplicaID(replicaId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDisplayComment(String comment) {
		try {
			getDelegate().setDisplayComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDocUnID(String unid) {
		try {
			getDelegate().setDocUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHotSpotText(String text) {
		try {
			getDelegate().setHotSpotText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHotSpotTextStyle(RichTextStyle rtstyle) {
		try {
			getDelegate().setHotSpotTextStyle((lotus.domino.RichTextStyle) toLotus(rtstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setServerHint(String server) {
		try {
			getDelegate().setServerHint(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setViewUnID(String unid) {
		try {
			getDelegate().setViewUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
