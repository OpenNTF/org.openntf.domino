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
package org.openntf.domino.design;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jgallagher
 * 
 */

// This will help: http://www.horstmann.com/corejsf/faces-config.html
public interface FacesConfig {

	public List<String> getActionListeners();

	public Converter addConverter();

	public List<Converter> getConverters();

	public ManagedBean addManagedBean();

	public List<ManagedBean> getManagedBeans();

	public List<String> getMessageBundles();

	public List<String> getNavigationHandlers();

	public List<String> getPhaseListeners();

	public List<String> getPropertyResolvers();

	public List<String> getVariableResolvers();

	public List<String> getViewHandlers();

	public interface ManagedBean {
		public String getName();

		public void setName(String name);

		public String getClassName();

		public void setClassName(String className);

		public Scope getScope();

		public void setScope(Scope scope);

		public Property addProperty();

		public List<Property> getProperties();

		public String getListValueClassName();

		public void setListValueClassName(String className);

		public List<String> getListEntries();

		public void setListEntries(Collection<?> listEntries);

		public Map<String, String> getMapEntries();

		public void setMapEntries(Map<?, ?> mapEntries);

		public void remove();

		public static interface Property {
			public String getName();

			public void setName(String name);

			public String getListValueClassName();

			public void setListValueClassName(String className);

			public List<String> getListEntries();

			public void setListEntries(Collection<?> listEntries);

			public Map<String, String> getMapEntries();

			public void setMapEntries(Map<?, ?> mapEntries);

			public String getValue();

			public void setValue(String value);

			public void remove();
		}

		public enum Scope {
			NONE, REQUEST, VIEW, SESSION, APPLICATION;
		}
	}

	public interface Converter {
		public String getId();

		public void setId(String id);

		public String getClassName();

		public void setClassName(String className);

		public void remove();
	}
}
