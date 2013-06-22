/**
 * 
 */
package org.openntf.domino.schema;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.schema.DatabaseSchema.Flags;
import org.openntf.domino.schema.types.IDominoType;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class ItemDefinition implements Externalizable {
	private String name_;
	private String defaultLabel_;
	private Class<? extends IDominoType> type_;
	private Set<Flags> flags_ = new HashSet<Flags>();
	private Object defaultValue_;
	private ItemValidation validator_;
	private transient DatabaseSchema parentSchema_;

	public ItemDefinition() {

	}

	public void setParent(DatabaseSchema parent) {
		parentSchema_ = parent;
	}

	public DatabaseSchema getParent() {
		return parentSchema_;
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

	public void setType(Class<? extends IDominoType> type) {
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
		Class<?> cl = (Class<?>) Class.forName(in.readUTF(), true, Factory.getClassLoader());
		if (cl.isAssignableFrom(IDominoType.class)) {
			type_ = (Class<? extends IDominoType>) cl;
		}
		int flagCount = in.readInt();
		for (int i = 0; i < flagCount; i++) {
			flags_.add((Flags) in.readObject());
		}
		defaultValue_ = in.readObject();
		validator_ = (ItemValidation) in.readObject();
		validator_.setDefinition(this);
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