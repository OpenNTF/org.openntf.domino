package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class NotesCalendarNotice extends Base<org.openntf.domino.NotesCalendarNotice, lotus.domino.NotesCalendarNotice> implements
		org.openntf.domino.NotesCalendarNotice {

	public NotesCalendarNotice(lotus.domino.NotesCalendarNotice delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void accept(String comments) {
		try {
			getDelegate().accept(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void acceptCounter(String comments) {
		try {
			getDelegate().acceptCounter(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end) {
		try {
			getDelegate().counter(comments, (lotus.domino.DateTime) toLotus(start), (lotus.domino.DateTime) toLotus(end));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder) {
		try {
			getDelegate().counter(comments, (lotus.domino.DateTime) toLotus(start), (lotus.domino.DateTime) toLotus(end), keepPlaceholder);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void decline(String comments) {
		try {
			getDelegate().decline(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void decline(String comments, boolean keepInformed) {
		try {
			getDelegate().decline(comments, keepInformed);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void declineCounter(String comments) {
		try {
			getDelegate().declineCounter(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo, keepInformed);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Document getAsDocument() {
		try {
			// TODO This should really come from the doc's database
			return Factory.fromLotus(getDelegate().getAsDocument(), Document.class, this.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getOutstandingInvitations() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getOutstandingInvitations(), org.openntf.domino.NotesCalendarNotice.class, this
					.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesCalendar getParent() {
		return (NotesCalendar) super.getParent();
	}

	@Override
	public String getUNID() {
		try {
			return getDelegate().getUNID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isOverwriteCheckEnabled() {
		try {
			return getDelegate().isOverwriteCheckEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String read() {
		try {
			return getDelegate().read();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void removeCancelled() {
		try {
			getDelegate().removeCancelled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void requestInfo(String comments) {
		try {
			getDelegate().requestInfo(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void sendUpdatedInfo(String comments) {
		try {
			getDelegate().sendUpdatedInfo(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setOverwriteCheckEnabled(boolean flag) {
		try {
			getDelegate().setOverwriteCheckEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void tentativelyAccept(String comments) {
		try {
			getDelegate().tentativelyAccept(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
