package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.domino.Item;
import org.openntf.domino.Item.Flags;
import org.openntf.domino.Item.Type;

public class ItemType implements org.openntf.domino.big.ItemType {
	private Item.Type lotusType_;
	private byte lotusFlags_;

	public ItemType() {
		// TODO Auto-generated constructor stub
	}

	public ItemType(final org.openntf.domino.Item prototype) {
		lotusFlags_ = (byte) Item.Flags.getFlags(prototype);
		lotusType_ = prototype.getTypeEx();
	}

	public boolean validateItem(final org.openntf.domino.Item candidate) {
		byte flags = (byte) Item.Flags.getFlags(candidate);
		Item.Type type = candidate.getTypeEx();
		return flags == lotusFlags_ && type == lotusType_;
	}

	@Override
	public boolean isProtected() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.PROTECTED.getValue();
			return ((lotusFlags_ & val) == Item.Flags.PROTECTED.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isNames() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.NAMES.getValue();
			return ((lotusFlags_ & val) == Item.Flags.NAMES.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isAuthors() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.AUTHORS.getValue();
			return ((lotusFlags_ & val) == Item.Flags.AUTHORS.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isReaders() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.READERS.getValue();
			return ((lotusFlags_ & val) == Item.Flags.READERS.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isSummary() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.SUMMARY.getValue();
			return ((lotusFlags_ & val) == Item.Flags.SUMMARY.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isEncrypted() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.ENCRYPTED.getValue();
			return ((lotusFlags_ & val) == Item.Flags.ENCRYPTED.getValue());
		} else {
			return false;
		}
	}

	@Override
	public boolean isSigned() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.SIGNED.getValue();
			return ((lotusFlags_ & val) == Item.Flags.SIGNED.getValue());
		} else {
			return false;
		}
	}

	@Override
	public Type getType() {
		return lotusType_;
	}

	@Override
	public Flags[] getFlags() {
		int size = Integer.bitCount(lotusFlags_);
		Flags[] result = new Flags[size];
		int current = 0;
		if (isSummary())
			result[current++] = Flags.SUMMARY;
		if (isNames())
			result[current++] = Flags.NAMES;
		if (isAuthors())
			result[current++] = Flags.AUTHORS;
		if (isReaders())
			result[current++] = Flags.READERS;
		if (isProtected())
			result[current++] = Flags.PROTECTED;
		if (isSigned())
			result[current++] = Flags.SIGNED;
		if (isEncrypted())
			result[current++] = Flags.ENCRYPTED;
		return result;
	}

	@Override
	public Class<?> getJavaType() {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public void setType(final Item.Type type) {
		lotusType_ = type;
	}

	@Override
	public void setFlags(final int flags) {
		lotusFlags_ = (byte) flags;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		lotusFlags_ = in.readByte();
		//  To keep compatibility, we read and write the item type as short
		lotusType_ = Item.Type.valueOf(in.readShort());
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeByte(lotusFlags_);
		out.writeShort((short) lotusType_.getValue());
	}

}
