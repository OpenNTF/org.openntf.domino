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
package org.openntf.domino.schema;

import java.io.Externalizable;
import java.util.Map;
import java.util.Set;

/**
 * @author Nathan T. Freeman
 * 
 */
public interface IDocumentDefinition extends Externalizable {
	public void setParent(final IDatabaseSchema parent);

	public String getName();

	public void setName(final String name);

	public boolean isDefaultSummary();

	public void setDefaultSummary(final boolean defaultSummary);

	public Set<String> getItemDefinitionKeys();

	//	public void setItemDefinitionKeys(final Set<String> itemDefinitionKeys);
	//
	//	public void addItemDefinitionKey(final String key);

	public Map<String, IItemDefinition> getItemDefinitions();

	public void addItemDefinition(final IItemDefinition itemDef);

	public Map<String, String> getOverrideLabels();

	public IDatabaseSchema getParent();

}
