/**
 * 
 */
package org.openntf.domino.schema.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.schema.IDocumentDefinition;
import org.openntf.domino.schema.IItemDefinition;

public class DocumentDefinition implements IDocumentDefinition, Externalizable {
	private String name_; // will be used as Form field value
	private final Set<String> itemDefinitionKeys_ = new HashSet<String>();
	private transient Map<String, IItemDefinition> itemDefs_ = new ConcurrentHashMap<String, IItemDefinition>();
	private final Map<String, String> overrideLabels_ = new HashMap<String, String>();
	private boolean defaultSummary_ = true;
	private transient IDatabaseSchema parentSchema_;
	@SuppressWarnings("unused")
	private DocumentValidator validator_;

	public DocumentDefinition() {

	}

	@Override
	public void setParent(final IDatabaseSchema parent) {
		parentSchema_ = parent;
	}

	@Override
	public IDatabaseSchema getParent() {
		return parentSchema_;
	}

	@Override
	public String getName() {
		return name_;
	}

	@Override
	public void setName(final String name) {
		name_ = name;
	}

	@Override
	public boolean isDefaultSummary() {
		return defaultSummary_;
	}

	@Override
	public void setDefaultSummary(final boolean defaultSummary) {
		defaultSummary_ = defaultSummary;
	}

	@Override
	public Map<String, IItemDefinition> getItemDefinitions() {
		if (itemDefs_.isEmpty()) {
			for (String key : getItemDefinitionKeys()) {
				ItemDefinition id = parentSchema_.getItemDefinitions().get(key);
				if (id != null) {
					itemDefs_.put(key, id);
				}
			}
		}
		return itemDefs_;
	}

	@Override
	public Set<String> getItemDefinitionKeys() {
		return itemDefinitionKeys_;
	}

	public void setItemDefinitionKeys(final Set<String> itemDefinitionKeys) {
		//TODO NTF - Not really sure this should even have a setter, honestly.
		itemDefinitionKeys_.clear();
		itemDefinitionKeys_.addAll(itemDefinitionKeys);
	}

	@Override
	public void addItemDefinition(final IItemDefinition itemDef) {
		if (itemDefs_ != null) {
			itemDefs_.put(itemDef.getName(), itemDef);
		}
		String key = itemDef.getName();
		addItemDefinitionKey(key);
	}

	public void addItemDefinitionKey(final String key) {
		itemDefinitionKeys_.add(key);
	}

	@Override
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
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
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
	public void writeExternal(final ObjectOutput out) throws IOException {
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