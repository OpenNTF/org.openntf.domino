/**
 * 
 */
package org.openntf.domino.schema;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.ext.Formula;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author nfreeman
 * 
 */
public class DatabaseSchema implements Externalizable {
	private static final Logger log_ = Logger.getLogger(DatabaseSchema.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Flags {
		SUMMARY, READERS, AUTHORS, PROTECTED, SIGNED, ENCRYPTED
	}

	public static class DocumentDefinition implements Externalizable {
		private String name_; // will be used as Form field value
		private Set<String> itemDefinitionKeys_ = new HashSet<String>();
		private transient Set<ItemDefinition> itemDefs_;
		private Map<String, String> overrideLabels_ = new HashMap<String, String>();
		private boolean defaultSummary_ = true;
		private transient DatabaseSchema parentSchema_;

		public DocumentDefinition() {

		}

		public void setParent(DatabaseSchema parent) {
			parentSchema_ = parent;
		}

		public String getName() {
			return name_;
		}

		public void setName(String name) {
			name_ = name;
		}

		public boolean isDefaultSummary() {
			return defaultSummary_;
		}

		public void setDefaultSummary(boolean defaultSummary) {
			defaultSummary_ = defaultSummary;
		}

		public Set<ItemDefinition> getItemDefinitions() {
			if (itemDefs_ == null) {
				itemDefs_ = new HashSet<ItemDefinition>();
				for (String key : getItemDefinitionKeys()) {
					ItemDefinition id = parentSchema_.getItemDefinitions().get(key);
					if (id != null) {
						itemDefs_.add(id);
					}
				}
			}
			return itemDefs_;
		}

		public Set<String> getItemDefinitionKeys() {
			return itemDefinitionKeys_;
		}

		public void setItemDefinitionKeys(Set<String> itemDefinitionKeys) {
			itemDefinitionKeys_ = itemDefinitionKeys;
		}

		public void addItemDefinition(ItemDefinition itemDef) {
			if (itemDefs_ != null) {
				itemDefs_.add(itemDef);
			}
			String key = itemDef.getName();
			addItemDefinitionKey(key);
		}

		public void addItemDefinitionKey(String key) {
			itemDefinitionKeys_.add(key);
		}

		public Map<String, String> getOverrideLabels() {
			return overrideLabels_;
		}

		// public void setOverrideLabels(Map<String, String> overrideLabels) {
		// overrideLabels_ = overrideLabels;
		// }

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
		 */
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			name_ = in.readUTF();
			defaultSummary_ = in.readBoolean();
			int defCount = in.readInt();
			for (int i = 0; i < defCount; i++) {
				itemDefinitionKeys_.add(in.readUTF());
			}
			int labelCount = in.readInt();
			for (int i = 0; i < labelCount; i++) {
				String key = in.readUTF();
				String value = in.readUTF();
				overrideLabels_.put(key, value);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
		 */
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeUTF(name_);
			out.writeBoolean(defaultSummary_);
			out.writeInt(itemDefinitionKeys_.size());
			for (String key : itemDefinitionKeys_) {
				out.writeUTF(key);
			}
			out.writeInt(overrideLabels_.size());
			for (Map.Entry<String, String> entry : overrideLabels_.entrySet()) {
				out.writeUTF(entry.getKey());
				out.writeUTF(entry.getValue());
			}
		}
	}

	public static class ItemDefinition implements Externalizable {
		private String name_;
		private String defaultLabel_;
		private Class<?> type_;
		private Set<Flags> flags_ = new HashSet<Flags>();
		private Object defaultValue_;
		private ItemValidation validator_;
		private transient DatabaseSchema parentSchema_;

		public ItemDefinition() {

		}

		public void setParent(DatabaseSchema parent) {
			parentSchema_ = parent;
		}

		public String getName() {
			return name_;
		}

		public void setName(String name) {
			name_ = name;
		}

		public String getDefaultLabel() {
			return defaultLabel_;
		}

		public void setDefaultLabel(String defaultLabel) {
			defaultLabel_ = defaultLabel;
		}

		public Class<?> getType() {
			return type_;
		}

		public void setType(Class<?> type) {
			type_ = type;
		}

		public Set<Flags> getFlags() {
			return flags_;
		}

		public void setFlags(Set<Flags> flags) {
			flags_ = flags;
		}

		public void addFlag(Flags flag) {
			flags_.add(flag);
		}

		public Object getDefaultValue() {
			return defaultValue_;
		}

		public void setDefaultValue(Object defaultValue) {
			defaultValue_ = defaultValue;
		}

		public ItemValidation getValidator() {
			return validator_;
		}

		public void setValidator(ItemValidation validator) {
			validator_ = validator;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
		 */
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			name_ = in.readUTF();
			defaultLabel_ = in.readUTF();
			type_ = Class.forName(in.readUTF());
			int flagCount = in.readInt();
			for (int i = 0; i < flagCount; i++) {
				flags_.add((Flags) in.readObject());
			}
			defaultValue_ = in.readObject();
			validator_ = (ItemValidation) in.readObject();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
		 */
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeUTF(name_);
			out.writeUTF(defaultLabel_);
			out.writeUTF(type_.getCanonicalName());
			out.writeInt(flags_.size());
			for (Flags flag : flags_) {
				out.writeObject(flag);
			}
			out.writeObject(defaultValue_);
			out.writeObject(validator_);
		}

		public Item createDefaultItem(Document doc, DocumentDefinition def) {
			String name = getName();
			Object defaultValue = getDefaultValue();
			if (defaultValue == null) {
				Class<?> checkType = getType();
				if (checkType.isArray()) {
					checkType = getType().getComponentType();
				}
				if (checkType == Integer.TYPE || checkType.equals(Integer.class)) {
					defaultValue = 0;
				} else if (checkType == Long.TYPE || checkType.equals(Long.class)) {
					defaultValue = 0;
				} else if (checkType == Double.TYPE || checkType.equals(Double.class)) {
					defaultValue = 0;
				} else if (checkType == Float.TYPE || checkType.equals(Float.class)) {
					defaultValue = 0;
				} else if (checkType == Short.TYPE || checkType.equals(Short.class)) {
					defaultValue = 0;
				} else if (checkType == Long.TYPE || checkType.equals(Long.class)) {
					defaultValue = 0;
				} else if (checkType.equals(String.class)) {
					defaultValue = "";
				} else if (checkType.equals(DateTime.class)) {
					DateTime dt = doc.getAncestorSession().createDateTime(new Date());
					dt.setAnyDate();
					dt.setAnyTime();
					defaultValue = dt;
				} else {
					defaultValue = null;
				}
			}
			Item item = null;
			if (defaultValue != null) {
				item = doc.replaceItemValue(name, defaultValue);
			} else {
				try {
					item = doc.replaceItemValueCustomDataBytes(name, "", new byte[1]);
				} catch (IOException e) {
					DominoUtils.handleException(e);
				}
			}
			if (!def.isDefaultSummary()) {
				item.setSummary(false);
			}
			for (Flags flag : getFlags()) {
				switch (flag) {
				case SUMMARY:
					item.setSummary(true);
					break;
				case AUTHORS:
					item.setAuthors(true);
					break;
				case READERS:
					item.setReaders(true);
					break;
				case PROTECTED:
					item.setProtected(true);
					break;
				case SIGNED:
					item.setSigned(true);
					break;
				case ENCRYPTED:
					item.setEncrypted(true);
					break;
				}
			}
			return item;
		}

	}

	public static class ItemValidation implements Externalizable {
		private boolean required_;
		private boolean unique_;
		private Formula uniqueFormula_;
		private Pattern expression_;
		private long maxValue_;
		private long minValue_;
		private int maxMembers_;
		private int minMembers_;

		public Pattern getPattern() {
			return expression_;
		}

		public void setRegex(String expression) {
			expression_ = Pattern.compile(expression);
		}

		public boolean isRequired() {
			return required_;
		}

		public void setRequired(boolean required) {
			required_ = required;
		}

		public boolean isUnique() {
			return unique_;
		}

		public void setUnique(boolean unique) {
			unique_ = unique;
		}

		public Formula getUniqueFormula() {
			return uniqueFormula_;
		}

		public void setUniqueFormula(Formula uniqueFormula) {
			uniqueFormula_ = uniqueFormula;
		}

		public long getMaxValue() {
			return maxValue_;
		}

		public void setMaxValue(long maxValue) {
			maxValue_ = maxValue;
		}

		public long getMinValue() {
			return minValue_;
		}

		public void setMinValue(long minValue) {
			minValue_ = minValue;
		}

		public int getMaxMembers() {
			return maxMembers_;
		}

		public void setMaxMembers(int maxMembers) {
			maxMembers_ = maxMembers;
		}

		public int getMinMembers() {
			return minMembers_;
		}

		public void setMinMembers(int minMembers) {
			minMembers_ = minMembers;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
		 */
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			required_ = in.readBoolean();
			unique_ = in.readBoolean();
			maxValue_ = in.readLong();
			minValue_ = in.readLong();
			maxMembers_ = in.readInt();
			minMembers_ = in.readInt();
			uniqueFormula_ = (Formula) in.readObject();
			expression_ = (Pattern) in.readObject();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
		 */
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeBoolean(required_);
			out.writeBoolean(unique_);
			out.writeLong(maxValue_);
			out.writeLong(minValue_);
			out.writeInt(maxMembers_);
			out.writeInt(minMembers_);
			out.writeObject(uniqueFormula_);
			out.writeObject(expression_);
		}

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

	public boolean validate(Document document) {
		boolean result = false;
		// TODO
		return result;
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
