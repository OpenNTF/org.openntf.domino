package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * The COLLATE_DESCRIPTOR structure is one of the components of the view collation item in a view note. The view collation item is an item
 * of TYPE_COLLATION with item name VIEW_COLLATION_ITEM ($Collation). (nifcoll.h)
 * 
 * @since forever
 *
 */
public class COLLATE_DESCRIPTOR extends AbstractStruct {
	/**
	 * COLLATE_TYPE_xxx
	 */
	public static enum Type {
		KEY, UNUSED1, UNUSED2, NOTEID, UNUSED4, UNUSED5, TUMBLER,
		/**
		 * Note: both COLLATE_TYPE_CATEGORY and COLLATE_TYPE_MAX share value 7
		 */
		CATEGORY_AND_MAX
	}

	/**
	 * CDF_xxx
	 */
	public static enum Flag {
		S_descending((byte) 0), M_descending((byte) 0x01), M_caseinsensitive((byte) 0x02), M_ignoreprefixes((byte) 0x02),
		M_accentinsensitive((byte) 0x04), M_permuted((byte) 0x08), M_permuted_pairwise((byte) 0x10), M_flat_in_v5((byte) 0x20),
		M_casesensitive_in_v5((byte) 0x40), M_accentsensitive_in_v5((byte) 0x80);

		private final byte value_;

		private Flag(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final byte flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static final byte COLLATE_DESCRIPTOR_SIGNATURE = 0x66;

	public final Unsigned8 Flags = new Unsigned8();
	public final Unsigned8 signature = new Unsigned8();
	public final Enum8<Type> keytype = new Enum8<Type>(Type.values());
	public final Unsigned16 NameOffset = new Unsigned16();
	public final Unsigned16 NameLength = new Unsigned16();
}
