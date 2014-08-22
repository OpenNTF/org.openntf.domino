package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the start of an OLE Object. (editods.h)
 *
 */
public class CDOLEBEGIN extends CDRecord {
	/**
	 * These flags are used to define the type of OLE object a note contains. These flags are used by the Flags member of the CDOLEBEGIN
	 * data structure.
	 *
	 */
	public static enum Flag {
		/**
		 * The data is an OLE embedded object.
		 */
		OBJECT(0x01),
		/**
		 * The data is an OLE link.
		 */
		LINK(0x02),
		/**
		 * If this is a link, it is an Automatic (hot) link.
		 */
		AUTOLINK(0x04),
		/**
		 * If this is a link, it is a Manual (warm) link.
		 */
		MANUALLINK(0x08),
		/**
		 * This is a new object that was just inserted into the note.
		 */
		NEWOBJECT(0x10),
		/**
		 * This is a new object that was just pasted into the note.
		 */
		PASTED(0x20),
		/**
		 * This object came from a form and should be saved every time it changes in the server.
		 */
		SAVEOBJWHENCHANGED(0x40),
		/**
		 * This object was inherited from a form, or object incapable of rendering itself, so don't visualize it.
		 */
		NOVISUALIZE(0x80);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * The symbols defined below specify which implementation of OLE was in use when the OLE object was created. (editods.h)
	 * 
	 * @since Lotus Notes 4.0
	 *
	 */
	public static enum OLEVersion {
		VERSION1(1), VERSION2(2);

		private final int value_;

		private OLEVersion(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static OLEVersion valueOf(final int typeCode) {
			for (OLEVersion type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching OLEVersion found for type code " + typeCode);
		}
	}

	public CDOLEBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return The Notes OLE implementation version.
	 */
	public OLEVersion getVersion() {
		return OLEVersion.valueOf(getData().getShort(getData().position() + 0) & 0xFFFF);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 2));
	}

	/**
	 * @return Clipboard format with which to render data.
	 */
	public DDEFormat getClipFormat() {
		return DDEFormat.valueOf(getData().getShort(getData().position() + 6));
	}

	/**
	 * @return Filename length (without the '\0' terminator) of the attached OLE object file.
	 */
	public int getAttachNameLength() {
		return getData().getShort(getData().position() + 8) & 0xFFFF;
	}

	/**
	 * @return (Optional) Length of the object's readable OLE class name.
	 */
	public int getClassNameLength() {
		return getData().getShort(getData().position() + 10) & 0xFFFF;
	}

	/**
	 * @return (Optional) Length of the object's template class name.
	 */
	public int getTemplateNameLength() {
		return getData().getShort(getData().position() + 12) & 0xFFFF;
	}

	/**
	 * @return Name of the attached OLE object file.
	 */
	public String getAttachName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 14);
		data.limit(data.position() + getAttachNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return The object's readable OLE class name.
	 */
	public String getClassName() {
		int preceding = getAttachNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 14 + preceding);
		data.limit(data.position() + getClassNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return The object's template class name.
	 */
	public String getTemplateName() {
		int preceding = getAttachNameLength() + getClassNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 14 + preceding);
		data.limit(data.position() + getTemplateNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Version=" + getVersion() + ", Flags=" + getFlags() + ", ClipFormat=" + getClipFormat()
				+ ", AttachName=" + getAttachName() + ", ClassName=" + getClassName() + ", TemplateName=" + getTemplateName() + "]";
	}
}
