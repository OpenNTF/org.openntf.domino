package org.openntf.domino.design;

import java.util.Collection;
import java.util.List;

/**
 * @author jgallagher
 * 
 */

// This will help: http://www.horstmann.com/corejsf/faces-config.html
public interface FacesConfig extends FileResource {

	public Collection<String> getActionListeners();

	public Converter addConverter();

	public Collection<Converter> getConverters();

	public ManagedBean addManagedBean();

	public Collection<ManagedBean> getManagedBeans();

	public Collection<String> getMessageBundles();

	public Collection<String> getNavigationHandlers();

	public Collection<String> getPhaseListeners();

	public Collection<String> getPropertyResolvers();

	public Collection<String> getVariableResolvers();

	public Collection<String> getViewHandlers();

	public interface ManagedBean {
		public String getName();

		public void setName(String name);

		public String getClassName();

		public void setClassName(String className);

		public Scope getScope();

		public void setScope(Scope scope);

		public static interface Property {
			public String getName();

			public void setName(String name);

			public String getValueClassName();

			public void setValueClassName(String className);

			public List<?> getListEntries();

			public void setListEntries(Collection<?> listEntries);
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
	}
}
