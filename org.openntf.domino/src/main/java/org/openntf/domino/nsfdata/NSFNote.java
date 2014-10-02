package org.openntf.domino.nsfdata;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public interface NSFNote {

	/**
	 * The note class based on the API documentation (NOTE_CLASS_xxx). This includes deprecated and virtual types.
	 */
	public static enum NoteClass {
		DOCUMENT(0x0001), DATA(DOCUMENT), INFO(0x0002), FORM(0x0004), VIEW(0x0008), ICON(0x0010), DESIGN(0x0020), ACL(0x0040),
		HELP_INDEX(0x0080), HELP(0x0100), FILTER(0x0200), FIELD(0x0400), REPLFORMULA(0x0800), PRIVATE(0x1000), DEFAULT(0x8000),
		NOTIFYDELETION(DEFAULT), ALL(0x7fff), ALLNONDATA(0x7ffe), NONE(0x000), SINGLE_INSTANCE(DESIGN.getValue() | ACL.getValue()
				| INFO.getValue() | ICON.getValue() | HELP_INDEX.getValue() | 0), HELPUSINGDOCUMENT(HELP_INDEX), HELPABOUTDOCUMENT(HELP),
		SHAREDFIELD(FORM);

		private final int value_;
		private final boolean alias_;

		private NoteClass(final int value) {
			value_ = value;
			alias_ = false;
		}

		private NoteClass(final NoteClass aliased) {
			value_ = aliased.getValue();
			alias_ = true;
		}

		public int getValue() {
			return value_;
		}

		public boolean isAlias() {
			return alias_;
		}

		public boolean isDesign() {
			return this != DOCUMENT && this != DATA;
		}
	}

	NoteClass getNoteClass();

	int getNoteId();

	String getUniversalId();

	boolean isDefault();

	int getSequence();

	Collection<NSFItem> getItems(String itemName);

	Collection<NSFItem> getItems();

	NSFRichTextData getRichText(String itemName);

	NSFMimeData getMimeData(String itemName);

	boolean hasItem(String itemName);

	void extractFileResource(String itemName, OutputStream os) throws IOException;
}
