package org.openntf.domino.nsfdata.impldxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openntf.domino.nsfdata.NSFItem;
import org.openntf.domino.nsfdata.NSFMimeData;
import org.openntf.domino.nsfdata.NSFNote;
import org.openntf.domino.nsfdata.NSFRichTextData;
import org.openntf.domino.nsfdata.impldxl.item.DXLItemComposite;
import org.openntf.domino.nsfdata.impldxl.item.DXLItemFactory;
import org.openntf.domino.nsfdata.structs.cd.CDFILEHEADER;
import org.openntf.domino.nsfdata.structs.cd.CDFILESEGMENT;
import org.openntf.domino.nsfdata.structs.cd.CDRecord;
import org.openntf.domino.nsfdata.structs.cd.CData;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLNote implements NSFNote, Serializable {
	private static final long serialVersionUID = 1L;

	private static final boolean DEBUG = false;

	private final NoteClass noteClass_;
	private final int noteId_;
	private final String universalId_;
	private final boolean default_;
	private final int sequence_;

	private final List<NSFItem> items_ = new ArrayList<NSFItem>();
	private transient Map<String, List<NSFItem>> itemsByName_ = new TreeMap<String, List<NSFItem>>(String.CASE_INSENSITIVE_ORDER);

	public static DXLNote create(final XMLNode node) {
		return new DXLNote(node);
	}

	private DXLNote(final XMLNode node) {
		noteClass_ = NoteClass.valueOf(node.getAttribute("class").toUpperCase());
		default_ = "true".equals(node.getAttribute("default"));

		XMLNode noteInfo = node.selectSingleNode("noteinfo");
		noteId_ = Integer.parseInt(noteInfo.getAttribute("noteid"), 16);
		universalId_ = noteInfo.getAttribute("unid");
		sequence_ = Integer.parseInt(noteInfo.getAttribute("sequence"), 10);

		if (DEBUG)
			System.out.println("\tUNID: " + universalId_);

		for (XMLNode itemNode : node.selectNodes("./item")) {
			if (DEBUG)
				System.out.println("\tItem: " + itemNode.getAttribute("name"));

			// Find out whether this is a duplicate item - solo items are all 0, while
			// dups are 1-based. I am aware that this is horrible.
			String name = itemNode.getAttribute("name");
			boolean duplicate = node.selectNodes("./item[@name='" + name + "']").size() > 1;
			int dupItemId = duplicate ? (getItems(name).size() + 1) : 0;

			NSFItem item = DXLItemFactory.create(itemNode, dupItemId);
			if (item != null) {
				items_.add(item);

				if (!itemsByName_.containsKey(name)) {
					itemsByName_.put(name, new ArrayList<NSFItem>());
				}
				itemsByName_.get(name).add(item);

				if (DEBUG) {
					System.out.print("\t\t[" + item.getType());
					System.out.print(", Class: " + item.getClass().getSimpleName());
					System.out.print(", Dup ID: " + item.getDupItemId());
					System.out.print(", Value: " + item.getValue());
					System.out.println("]");

					// Output composite data
					if (item instanceof DXLItemComposite) {
						CData cdata = ((DXLItemComposite) item).getValue();

						int breaker = 0;
						while (cdata.hasNext()) {
							if (breaker++ > 1000) {
								System.out.println("we went too deep!");
								break;
							}
							CDRecord record = cdata.next();
							System.out.print("\t\t\t[Signature: " + record.getSignature());
							System.out.print(", Length: " + record.getDataLength());
							System.out.print(", Value: " + record);
							System.out.println("]");

						}
					}
				}
			}
		}
	}

	@Override
	public NoteClass getNoteClass() {
		return noteClass_;
	}

	@Override
	public int getNoteId() {
		return noteId_;
	}

	@Override
	public String getUniversalId() {
		return universalId_;
	}

	@Override
	public boolean isDefault() {
		return default_;
	}

	@Override
	public int getSequence() {
		return sequence_;
	}

	@Override
	public Collection<NSFItem> getItems(final String itemName) {
		List<NSFItem> items = itemsByName_.get(itemName);
		if (items != null) {
			return items;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Collection<NSFItem> getItems() {
		return Collections.unmodifiableList(items_);
	}

	@Override
	public NSFMimeData getMimeData(final String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NSFRichTextData getRichText(final String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasItem(final String itemName) {
		return !getItems(itemName).isEmpty();
	}

	@Override
	public void extractFileResource(final String itemName, final java.io.OutputStream os) {
		Collection<NSFItem> items = getItems(itemName);

		int segmentCount = 0;
		int totalSegments = 0;
		for (NSFItem item : items) {
			if (item instanceof DXLItemComposite) {
				CData cdata = ((DXLItemComposite) item).getValue();

				int breaker = 0;
				while (cdata.hasNext()) {
					if (breaker++ > 1000) {
						System.out.println("we went too deep!");
						break;
					}
					CDRecord record = cdata.next();

					if (record instanceof CDFILEHEADER) {
						CDFILEHEADER header = (CDFILEHEADER) record;
						totalSegments = header.getSegCount();
						segmentCount = 0;
					}
					if (record instanceof CDFILESEGMENT) {
						CDFILESEGMENT seg = (CDFILESEGMENT) record;
						ByteBuffer data = seg.getFileData().duplicate();
						try {
							os.write(data.array(), data.position(), data.limit() - data.position());
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						if (++segmentCount >= totalSegments) {
							try {
								os.flush();
								return;
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}

			}
		}
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();

		itemsByName_ = new TreeMap<String, List<NSFItem>>(String.CASE_INSENSITIVE_ORDER);
		for (NSFItem item : items_) {
			String itemName = item.getName();
			if (!itemsByName_.containsKey(itemName)) {
				itemsByName_.put(itemName, new ArrayList<NSFItem>());
			}
			itemsByName_.get(itemName).add(item);
		}
	}
}
