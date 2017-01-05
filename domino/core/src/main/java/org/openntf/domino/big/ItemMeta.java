package org.openntf.domino.big;

import java.util.Date;

public interface ItemMeta extends BaseMeta {

	public NoteMeta getParent();

	public void setLastModified(Date lastModified);

	public Date getLastModified();

	public ItemType getItemType();

	public void setItemType(ItemType type);

	public org.openntf.domino.Item getItem(org.openntf.domino.Document doc);

	public boolean hasLocalValue();

	public Object getValue(boolean cache);

	public void setValue(Object value, boolean cacheOnly);
}
