/**
 * 
 */
package org.openntf.domino.schema;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class DocumentDefinition implements Externalizable {
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