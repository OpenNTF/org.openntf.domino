/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.nsfdata;

import java.util.Set;

public interface NSFItem {

	/**
	 * The item class based on the API documentation (CLASS_xxx).
	 */
	public static enum ItemClass {
		NOCOMPUTE(0 << 8), ERROR(1 << 8), UNAVAILABLE(2 << 8), NUMBER(3 << 8), TIME(4 << 8), TEXT(5 << 8), FORMULA(6 << 8), USERID(7 << 8);

		public static int CLASS_MASK = 0xff0;

		private final int value_;

		private ItemClass(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	/**
	 * The item type based on the API documentation (TYPE_xxx).
	 */
	public static enum Type {
		ERROR(ItemClass.ERROR, 0), UNAVAILABLE(ItemClass.ERROR, 0), TEXT(ItemClass.TEXT, 0), TEXT_LIST(ItemClass.TEXT, 1),
		NUMBER(ItemClass.NUMBER, 0), NUMBER_RANGE(ItemClass.NUMBER, 1), TIME(ItemClass.TIME, 0), TIME_RANGE(ItemClass.TIME, 1),
		FORMULA(ItemClass.FORMULA, 0), USERID(ItemClass.USERID, 0), INVALID_OR_UNKNOWN(ItemClass.NOCOMPUTE, 0),
		COMPOSITE(ItemClass.NOCOMPUTE, 1), COLLATION(ItemClass.NOCOMPUTE, 2), OBJECT(ItemClass.NOCOMPUTE, 3),
		NOTEREF_LIST(ItemClass.NOCOMPUTE, 4), VIEW_FORMAT(ItemClass.NOCOMPUTE, 5), ICON(ItemClass.NOCOMPUTE, 6),
		NOTELINK_LIST(ItemClass.NOCOMPUTE, 7), SIGNATURE(ItemClass.NOCOMPUTE, 8), SEAL(ItemClass.NOCOMPUTE, 9),
		SEALDATA(ItemClass.NOCOMPUTE, 10), SEAL_LIST(ItemClass.NOCOMPUTE, 11), HIGHLIGHTS(ItemClass.NOCOMPUTE, 12),
		WORKSHEET_DATA(ItemClass.NOCOMPUTE, 13), USERDATA(ItemClass.NOCOMPUTE, 14), QUERY(ItemClass.NOCOMPUTE, 15),
		ACTION(ItemClass.NOCOMPUTE, 16), ASSISTANT_INFO(ItemClass.NOCOMPUTE, 17), VIEWMAP_DATASET(ItemClass.NOCOMPUTE, 18),
		VIEWMAP_LAYOUT(ItemClass.NOCOMPUTE, 19), LSOBJECT(ItemClass.NOCOMPUTE, 20), HTML(ItemClass.NOCOMPUTE, 21),
		SCHED_LIST(ItemClass.NOCOMPUTE, 22), CALENDAR_FORMAT(ItemClass.NOCOMPUTE, 24), MIME_PART(ItemClass.NOCOMPUTE, 25),
		RFC822_TEXT(ItemClass.TEXT, 2), TYPE_SEAL2(ItemClass.NOCOMPUTE, 31),

		/**
		 * This appears to be the type code used for Outline data (which seem to be called "sitemaps"), but is not actually an official
		 * constant in the API
		 */
		SITEMAP(ItemClass.NOCOMPUTE, 26);

		private final ItemClass itemClass_;
		private final int valueOffset_;

		private Type(final ItemClass itemClass, final int valueOffset) {
			itemClass_ = itemClass;
			valueOffset_ = valueOffset;
		}

		public int getValue() {
			return itemClass_.getValue() + valueOffset_;
		}

		public ItemClass getItemClass() {
			return itemClass_;
		}

		public static Type valueOf(final int typeCode) {
			for (Type type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Type found for type code " + typeCode);
		}
	}

	/**
	 * The item flags based on the API documentation (ITEM_xxx).
	 */
	// TODO replace with org.openntf.domino.Item.Flags if possible
	public static enum Flag {
		SIGN(0x0001), SEAL(0x0002), SUMMARY(0x0004), READWRITERS(0x0020), NAMES(0x0020), PLACEHOLDER(0x0100), PROTECTED(0x0200),
		READERS(0x0400), UNCHANGED(0x1000);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	/**
	 * @return The name of the item.
	 */
	String getName();

	/**
	 * @return A Set containing the Notes API flags applied to the item, as Flag enums.
	 */
	Set<Flag> getFlags();

	/**
	 * @return Whether the item is flagged for encryption on save.
	 */
	boolean isSeal();

	/**
	 * @return The item's editing sequence number, or -1 if unsupported by the underlying data.
	 */
	int getSequence();

	/**
	 * Optional operation.
	 */
	int getDupItemId();

	/**
	 * @return The Notes API type of the item, as a Type enum.
	 */
	Type getType();

	/**
	 * @return A byte array of the Notes API representation of the data.
	 */
	byte[] getBytes();

	/**
	 * @return The item's data as a Java-friendly object, based on the item type.
	 */
	Object getValue();
}
