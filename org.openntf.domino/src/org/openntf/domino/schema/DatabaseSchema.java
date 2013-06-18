/**
 * 
 */
package org.openntf.domino.schema;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.annotations.Incomplete;

/**
 * @author nfreeman
 * 
 */
@Incomplete
public class DatabaseSchema implements Externalizable {
	private static final Logger log_ = Logger.getLogger(DatabaseSchema.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Flags {
		SUMMARY, READERS, AUTHORS, PROTECTED, SIGNED, ENCRYPTED
	}

	private Map<String, DocumentDefinition> documentDefinitions_ = new HashMap<String, DocumentDefinition>();
	private Map<String, ItemDefinition> itemDefinitions_ = new HashMap<String, ItemDefinition>();

	/**
	 * 
	 */
	public DatabaseSchema() {
		// TODO Auto-generated constructor stub
	}

	public Map<String, DocumentDefinition> getDocumentDefinitions() {
		return documentDefinitions_;
	}

	public void setDocumentDefinitions(Map<String, DocumentDefinition> definitions) {
		documentDefinitions_ = definitions;
	}

	public Map<String, ItemDefinition> getItemDefinitions() {
		return itemDefinitions_;
	}

	public void setItemDefinitions(Map<String, ItemDefinition> definitions) {
		itemDefinitions_ = definitions;
	}

	public void save(Database db) {

	}

	public Document createDocument(Database db, String doctype) {
		DocumentDefinition def = getDocumentDefinitions().get(doctype);
		if (def == null)
			return null;
		Document result = db.createDocument();
		result.replaceItemValue("$$SchemaType", doctype);
		result.replaceItemValue("form", def.getName());
		Set<ItemDefinition> itemDefs = def.getItemDefinitions();
		for (ItemDefinition itemDef : itemDefs) {
			Item item = itemDef.createDefaultItem(result, def);
		}

		return result;
	}

	public boolean validateDocument(Document doc) {
		String doctype = doc.getItemValueString("$$SchemaType");
		DocumentDefinition def = getDocumentDefinitions().get(doctype);
		if (def == null)
			return true;

		boolean result = true;
		Set<ItemDefinition> itemDefs = def.getItemDefinitions();
		for (ItemDefinition itemDef : itemDefs) {
			// TODO NTF
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int defCount = in.readInt();
		for (int i = 0; i < defCount; i++) {
			String key = in.readUTF();
			DocumentDefinition def = (DocumentDefinition) in.readObject();
			documentDefinitions_.put(key, def);
			def.setParent(this);
		}
		int itemCount = in.readInt();
		for (int i = 0; i < itemCount; i++) {
			String key = in.readUTF();
			ItemDefinition def = (ItemDefinition) in.readObject();
			itemDefinitions_.put(key, def);
			def.setParent(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(documentDefinitions_.size());
		for (Map.Entry<String, DocumentDefinition> entry : documentDefinitions_.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeObject(entry.getValue());
		}
		out.writeInt(itemDefinitions_.size());
		for (Map.Entry<String, ItemDefinition> entry : itemDefinitions_.entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeObject(entry.getValue());
		}
	}

}
