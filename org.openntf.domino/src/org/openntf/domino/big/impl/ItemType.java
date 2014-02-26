package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.domino.Item;
import org.openntf.domino.Item.Flags;
import org.openntf.domino.Item.Type;

public class ItemType implements org.openntf.domino.big.ItemType {
	private short lotusType_;
	private byte lotusFlags_;

	public ItemType() {
		// TODO Auto-generated constructor stub
	}

	public ItemType(final org.openntf.domino.Item prototype) {
		lotusFlags_ = (byte) Item.Flags.getFlags(prototype);
		lotusType_ = (short) prototype.getType();
	}

	public boolean validateItem(final org.openntf.domino.Item candidate) {
		byte flags = (byte) Item.Flags.getFlags(candidate);
		short type = (short) candidate.getType();
		return flags == lotusFlags_ && type == lotusType_;
	}

	public boolean isProtected() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.PROTECTED.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.PROTECTED.getValue());
		} else {
			return false;
		}
	}

	public boolean isNames() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.NAMES.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.NAMES.getValue());
		} else {
			return false;
		}
	}

	public boolean isAuthors() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.AUTHORS.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.AUTHORS.getValue());
		} else {
			return false;
		}
	}

	public boolean isReaders() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.READERS.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.READERS.getValue());
		} else {
			return false;
		}
	}

	public boolean isSummary() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.SUMMARY.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.SUMMARY.getValue());
		} else {
			return false;
		}
	}

	public boolean isEncrypted() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.ENCRYPTED.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.ENCRYPTED.getValue());
		} else {
			return false;
		}
	}

	public boolean isSigned() {
		if (lotusFlags_ > 0) {
			int val = Item.Flags.SIGNED.getValue();
			return (((int) lotusFlags_ & val) == Item.Flags.SIGNED.getValue());
		} else {
			return false;
		}
	}

	public Type getType() {
		return Item.Type.getType((int) lotusType_);
	}

	public Flags[] getFlags() {
		int size = Integer.bitCount((int) lotusFlags_);
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

	public Class<?> getJavaType() {
		// TODO Auto-generated method stub
		return null;

	}

	public void setType(final Item.Type type) {
		int value = type.getValue();
		lotusType_ = new Integer(value).shortValue();
	}

	public void setFlags(final int flags) {
		lotusFlags_ = (byte) flags;
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		lotusFlags_ = in.readByte();
		lotusType_ = in.readShort();
	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeByte((int) lotusFlags_);
		out.writeShort((int) lotusType_);
	}

}
