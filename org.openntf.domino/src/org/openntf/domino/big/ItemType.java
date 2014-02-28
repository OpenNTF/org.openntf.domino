package org.openntf.domino.big;

import java.io.Externalizable;

import org.openntf.domino.Item;

public interface ItemType extends Externalizable {
	public boolean isProtected();

	public boolean isNames();

	public boolean isAuthors();

	public boolean isReaders();

	public boolean isSummary();

	public boolean isEncrypted();

	public boolean isSigned();

	public Item.Type getType();

	public void setType(Item.Type type);

	public Item.Flags[] getFlags();

	public void setFlags(int flags);

	public Class<?> getJavaType();
}
