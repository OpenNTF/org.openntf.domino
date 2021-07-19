/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.schema;

import java.io.Externalizable;
import java.util.Set;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.schema.impl.DatabaseSchema;
import org.openntf.domino.schema.impl.DatabaseSchema.Flags;
import org.openntf.domino.schema.impl.DocumentDefinition;

/**
 * @author Nathan T. Freeman
 * 
 */
public interface IItemDefinition extends Externalizable {
	public void setParent(DatabaseSchema parent);

	public IDatabaseSchema getParent();

	public String getName();

	public String getShortName();

	public void setName(String name);

	public String getDefaultLabel();

	public void setDefaultLabel(String defaultLabel);

	public Class<? extends IDominoType> getType();

	public void setType(Class<? extends IDominoType> type);

	public Set<Flags> getFlags();

	public void setFlags(Set<Flags> flags);

	public void addFlag(Flags flag);

	public void removeFlag(Flags flag);

	public Object getDefaultValue();

	public void setDefaultValue(Object defaultValue);

	public IItemValidation getValidator();

	public void setValidator(IItemValidation validator);

	public Set<IItemListener> getItemListeners();

	public void addItemListener(IItemListener listener);

	public Item createDefaultItem(Document result, DocumentDefinition def);
}
