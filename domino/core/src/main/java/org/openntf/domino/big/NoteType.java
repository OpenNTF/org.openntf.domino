package org.openntf.domino.big;

public interface NoteType {

	public String getName();

	public java.util.List<ItemMeta> getItemMetaList();

	public ItemMeta getItemMeta(String itemname);

}
