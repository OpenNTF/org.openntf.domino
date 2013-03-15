package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Outline extends Base<org.openntf.domino.Outline, lotus.domino.Outline> implements org.openntf.domino.Outline {

	public Outline(lotus.domino.Outline delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild) {
		try {
			getDelegate().addEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry), after), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(lotus.domino.OutlineEntry fromEntry, lotus.domino.OutlineEntry referenceEntry, boolean after,
			boolean asChild) {
		try {
			return Factory.fromLotus(getDelegate().createEntry((lotus.domino.OutlineEntry) toLotus(fromEntry),
					(lotus.domino.OutlineEntry) toLotus(referenceEntry), after, asChild), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(String entryName) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry)),
					OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry), after),
					OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry createEntry(String entryName, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(entryName, (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getAlias() {
		try {
			return getDelegate().getAlias();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getChild(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getChild((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getComment() {
		try {
			return getDelegate().getComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getFirst() {
		try {
			return Factory.fromLotus(getDelegate().getFirst(), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getLast() {
		try {
			return Factory.fromLotus(getDelegate().getLast(), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getNext(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNext((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getNextSibling(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getParent(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getParent((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database getParentDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getParentDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getPrev(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrev((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public OutlineEntry getPrevSibling(lotus.domino.OutlineEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling((lotus.domino.OutlineEntry) toLotus(entry)), OutlineEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void moveEntry(lotus.domino.OutlineEntry entry, lotus.domino.OutlineEntry referenceEntry, boolean after, boolean asChild) {
		try {
			getDelegate().moveEntry((lotus.domino.OutlineEntry) toLotus(entry), (lotus.domino.OutlineEntry) toLotus(referenceEntry), after,
					asChild);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void removeEntry(lotus.domino.OutlineEntry entry) {
		try {
			getDelegate().removeEntry((lotus.domino.OutlineEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void setAlias(String alias) {
		try {
			getDelegate().setAlias(alias);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setComment(String comment) {
		try {
			getDelegate().setComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setName(String name) {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
