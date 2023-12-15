/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
/**
 * 
 */
package org.openntf.domino.schema.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.schema.IDominoType;
import org.openntf.domino.schema.IItemDefinition;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author nfreeman
 * 
 */
@Incomplete
public class DatabaseSchema implements IDatabaseSchema, Externalizable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseSchema.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Flags {
		SUMMARY, READERS, AUTHORS, PROTECTED, SIGNED, ENCRYPTED
	}

	private final Map<String, DocumentDefinition> documentDefinitions_ = new ConcurrentHashMap<String, DocumentDefinition>();
	private final Map<String, ItemDefinition> itemDefinitions_ = new ConcurrentHashMap<String, ItemDefinition>();
	private transient Map<Class<? extends IDominoType>, IDominoType> typeDefinitions_;

	/**
	 * 
	 */
	public DatabaseSchema() {
	}

	@Override
	public Map<String, DocumentDefinition> getDocumentDefinitions() {
		return documentDefinitions_;
	}

	// public void setDocumentDefinitions(final Map<String, DocumentDefinition> definitions) {
	// documentDefinitions_ = definitions;
	// }

	@Override
	public Map<String, ItemDefinition> getItemDefinitions() {
		return itemDefinitions_;
	}

	// public void setItemDefinitions(final Map<String, ItemDefinition> definitions) {
	// itemDefinitions_ = definitions;
	// }

	@Override
	public Map<Class<? extends IDominoType>, IDominoType> getTypeDefinitions() {
		if (typeDefinitions_ == null) {
			typeDefinitions_ = new HashMap<Class<? extends IDominoType>, IDominoType>();
		}
		return typeDefinitions_;
	}

	// public void setTypeDefinitions(final Map<Class<? extends IDominoType>, IDominoType> definitions) {
	// typeDefinitions_ = definitions;
	// }

	@Override
	public IDominoType getTypeDefinition(final Class<? extends IDominoType> type) {
		IDominoType result = getTypeDefinitions().get(type);
		if (result == null) {
			// TODO NTF improve exception handling
			try {
				result = type.newInstance();
			} catch (IllegalAccessException e) {
				DominoUtils.handleException(e);
			} catch (InstantiationException e) {
				DominoUtils.handleException(e);
			}
			getTypeDefinitions().put(type, result);
		}
		return result;
	}

	public void save(final Database db) {

	}

	@SuppressWarnings("unused")
	@Override
	public Document createDocument(final Database db, final String doctype) {
		DocumentDefinition def = getDocumentDefinitions().get(doctype);
		if (def == null)
			return null;
		Document result = db.createDocument();
		result.replaceItemValue("$$SchemaType", doctype); //$NON-NLS-1$
		result.replaceItemValue("form", def.getName()); //$NON-NLS-1$
		Map<String, IItemDefinition> itemDefs = def.getItemDefinitions();
		for (String key : itemDefs.keySet()) {
			IItemDefinition itemDef = itemDefs.get(key);
			Item item = itemDef.createDefaultItem(result, def);
		}

		return result;
	}

	@Override
	public boolean validateDocument(final Document doc) {
		String doctype = doc.getItemValueString("$$SchemaType"); //$NON-NLS-1$
		DocumentDefinition def = getDocumentDefinitions().get(doctype);
		if (def == null)
			return true;

		boolean result = true;
		Map<String, IItemDefinition> itemDefs = def.getItemDefinitions();
		for (String key : itemDefs.keySet()) {
			@SuppressWarnings("unused")
			IItemDefinition itemDef = itemDefs.get(key);
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
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
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
	public void writeExternal(final ObjectOutput out) throws IOException {
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

	@Override
	public IItemDefinition createItemDefinition(final String itemKey, final Class<?> type) {
		// TODO Implement this or remove comment
		return null;
	}

}
