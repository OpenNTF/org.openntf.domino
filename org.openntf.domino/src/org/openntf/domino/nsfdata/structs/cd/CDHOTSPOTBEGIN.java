package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the start of a "hot" region in a rich text field. Clicking on a hot region causes some other action to occur.
 * For instance, clicking on a popup will cause a block of text associated with that popup to be displayed. (editods.h)
 *
 */
public class CDHOTSPOTBEGIN extends CDRecord {
	/**
	 * These flags are used to define what type of hotspot is being defined by a CDHOTSPOTBEGIN data structure. (editods.h)
	 *
	 */
	public static enum Type {

		/** The hotspot is a popup */
		POPUP((short) 1),
		/**
		 * The hotspot is a button whose presentation is an arbitrary region of the rich text field. This region is bounded by the
		 * CDHOTSPOTBEGIN and CDHOTSPOTEND records.
		 */
		HOREGION((short) 2),
		/** The hotspot is a button. */
		BUTTON((short) 3),
		/** The hotspot is a file attachment. */
		FILE((short) 4),
		/** The hotspot is a Notes Release 3 section field hotspot. */
		SECTION((short) 7),
		/** Unused */
		ANY((short) 8),
		/**
		 * The hotspot is a document, database, or view link hotspot. The presentation of this hotspot is an arbitrary region of the rich
		 * text field. This region is bounded by the CDHOTSPOTBEGIN and CDHOTSPOTEND records.
		 */
		HOTLINK((short) 11),
		/** The hotspot is a standard collapsible section which is not access controlled. */
		BUNDLE((short) 12),
		/** The hotspot is an access controlled collapsible section. */
		V4_SECTION((short) 13),
		/** The hotspot is a subform. */
		SUBFORM((short) 14),
		/** The hotspot is an active object. */
		ACTIVEOBJECT((short) 15),
		/** The hotspot is an OLE rich text object. */
		OLERICHTEXT((short) 18),
		/** The hotspot is an embedded view. */
		EMBEDDEDVIEW((short) 19),
		/** The hotspot is an embedded folder pane. */
		EMBEDDEDPANE((short) 20),
		/** The hotspot is an embedded navigator. */
		EMBEDDEDNAV((short) 21),
		/** The hotspot is mouse over text popup. */
		MOUSEOVER((short) 22),
		/** The hotspot is a file upload placeholder. */
		FILEUPLOAD((short) 24),
		/** The hotspot is an embedded outline. */
		EMBEDDEDOUTLINE((short) 27),
		/** The hotspot is an embedded control window. */
		EMBEDDEDCTL((short) 28),
		/** The hotspot is an embedded calendar control (date picker). */
		EMBEDDEDCALENDARCTL((short) 30),
		/** The hotspot is an embedded scheduling control. */
		EMBEDDEDSCHEDCTL((short) 31),
		/** The hotspot is a resource link. */
		RCLINK((short) 32),
		/** The hotspot is an embedded editor control. */
		EMBEDDEDEDITCEL((short) 34),
		/** Embeddable buddy list. */
		CONTACTLISTCTL((short) 36);

		private final short value_;

		private Type(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Type valueOf(final short typeCode) {
			for (Type type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Type found for type code " + typeCode);
		}
	}

	static {
		addFixed("Type", Short.class);
		addFixed("Flags", Integer.class);
		addFixedUnsigned("DataLength", Short.class);

		addVariableData("Data", "getHotspotDataLength");
	}

	public CDHOTSPOTBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return HOTSPOTREC_TYPE_xxx
	 */
	public Type getType() {
		return Type.valueOf((Short) getStructElement("Type"));
	}

	/**
	 * @return HOTSPOTREC_RUNFLAG_xxx
	 */
	public int getFlags() {
		// TODO make enum
		return (Integer) getStructElement("Flags");
	}

	public int getHotspotDataLength() {
		return (Integer) getStructElement("DataLength");
	}

	public byte[] getHotspotData() {
		return (byte[]) getStructElement("Data");
	}

	// TODO add accessors for data
	//			/*  if HOTSPOTREC_RUNFLAG_SIGNED, WORD SigLen then SigData follows. */
}
