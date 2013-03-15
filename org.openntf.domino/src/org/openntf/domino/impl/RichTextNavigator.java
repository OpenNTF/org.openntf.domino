package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextNavigator extends Base<org.openntf.domino.RichTextNavigator, lotus.domino.RichTextNavigator> implements
		org.openntf.domino.RichTextNavigator {

	public RichTextNavigator(lotus.domino.RichTextNavigator delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public RichTextNavigator Clone() {
		try {
			return Factory.fromLotus(getDelegate().Clone(), RichTextNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean findFirstElement(int type) {
		try {
			return getDelegate().findFirstElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findFirstString(String target) {
		try {
			return getDelegate().findFirstString(target);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findFirstString(String target, int options) {
		try {
			return getDelegate().findFirstString(target, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findLastElement(int type) {
		try {
			return getDelegate().findLastElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNextElement() {
		try {
			return getDelegate().findNextElement();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNextElement(int type) {
		try {
			return getDelegate().findNextElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNextElement(int type, int occurrence) {
		try {
			return getDelegate().findNextElement(type, occurrence);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNextString(String target) {
		try {
			return getDelegate().findNextString(target);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNextString(String target, int options) {
		try {
			return getDelegate().findNextString(target, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean findNthElement(int type, int occurrence) {
		try {
			return getDelegate().findNthElement(type, occurrence);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getElement() {
		try {
			return Factory.fromLotus(getDelegate().getElement(), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getFirstElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getFirstElement(type), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getLastElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getLastElement(type), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getNextElement() {
		try {
			return Factory.fromLotus(getDelegate().getNextElement(), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getNextElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getNextElement(type), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getNextElement(int type, int occurrence) {
		try {
			return Factory.fromLotus(getDelegate().getNextElement(type, occurrence), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Base<?> getNthElement(int type, int occurrence) {
		try {
			return Factory.fromLotus(getDelegate().getNthElement(type, occurrence), Base.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setCharOffset(int offset) {
		try {
			getDelegate().setCharOffset(offset);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPosition(lotus.domino.Base element) {
		try {
			getDelegate().setPosition(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPositionAtEnd(lotus.domino.Base element) {
		try {
			getDelegate().setPositionAtEnd(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
