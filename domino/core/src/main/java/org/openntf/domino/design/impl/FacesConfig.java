/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.design.XspXmlContent;
import org.openntf.domino.utils.xml.ModifiableStringNodeList;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * @author jgallagher
 * 
 */
// TODO Make the "remove" methods mark the object as unusable in some way
// TODO Verify that null-value is actually legal in XPages
public class FacesConfig extends XspXmlContent implements org.openntf.domino.design.FacesConfig {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FacesConfig.class.getName());

	/**
	 * @param document
	 *            The design note used to represent an existing faces-config.xml file
	 */
	public FacesConfig(final DatabaseDesign databaseDesign) {
		FileResourceWebContent facesConfig = databaseDesign.getDesignElement(FileResourceWebContent.class, "WEB-INF/faces-config.xml");
		if (facesConfig != null) {
			setContainer(facesConfig);
		} else {
			facesConfig = databaseDesign.create(FileResourceWebContent.class);
			facesConfig.setName("WEB-INF/faces-config.xml");
			setContainer(facesConfig);
			try {
				getXml().loadString("<faces-config/>");
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	//	/**
	//	 * @param database
	//	 *            The database in which to create a new faces-config file
	//	 */
	//
	//	public FacesConfig(final Database database) {
	//		super(database);
	//		try {
	//			getXml().loadString("<faces-config/>");
	//		} catch (SAXException e) {
	//			DominoUtils.handleException(e);
	//		} catch (IOException e) {
	//			DominoUtils.handleException(e);
	//		} catch (ParserConfigurationException e) {
	//			DominoUtils.handleException(e);
	//		}
	//	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getActionListeners()
	 */
	@Override
	public List<String> getActionListeners() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "action-listener");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#addConverter()
	 */
	@Override
	public org.openntf.domino.design.FacesConfig.Converter addConverter() {
		return new Converter(getXml().selectSingleNode("/faces-config").addChildElement("converter"));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getConverters()
	 */
	@Override
	public List<org.openntf.domino.design.FacesConfig.Converter> getConverters() {
		return new ModifiableObjectNodeList<org.openntf.domino.design.FacesConfig.Converter>(this, getXml(), "/faces-config", "converter",
				Converter.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#addManagedBean()
	 */
	@Override
	public org.openntf.domino.design.FacesConfig.ManagedBean addManagedBean() {
		return new ManagedBean(getXml().selectSingleNode("/faces-config").addChildElement("managed-bean"));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getManagedBeans()
	 */
	@Override
	public List<org.openntf.domino.design.FacesConfig.ManagedBean> getManagedBeans() {
		return new ModifiableObjectNodeList<org.openntf.domino.design.FacesConfig.ManagedBean>(this, getXml(), "/faces-config",
				"managed-bean", ManagedBean.class);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getMessageBundles()
	 */
	@Override
	public List<String> getMessageBundles() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "message-bundle");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getNavigationHandlers()
	 */
	@Override
	public List<String> getNavigationHandlers() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "navigation-handler");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getPhaseListeners()
	 */
	@Override
	public List<String> getPhaseListeners() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "phase-listener");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getPropertyResolvers()
	 */
	@Override
	public List<String> getPropertyResolvers() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "property-resolver");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getVariableResolvers()
	 */
	@Override
	public List<String> getVariableResolvers() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "variable-resolver");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getViewHandlers()
	 */
	@Override
	public List<String> getViewHandlers() {
		return new ModifiableStringNodeList(getXml(), "/faces-config/application", "view-handler");
	}

	public class ManagedBean implements org.openntf.domino.design.FacesConfig.ManagedBean {
		private final XMLNode node_;

		public ManagedBean(final XMLNode node) {
			node_ = node;
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getName()
		 */
		@Override
		public String getName() {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-name");
			if (valueNode == null) {
				return "";
			}
			return valueNode.getText();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setName(java.lang.String)
		 */
		@Override
		public void setName(final String name) {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-name");
			if (valueNode == null) {
				valueNode = node_.addChildElement("managed-bean-name");
			}
			valueNode.setText(name);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getClassName()
		 */
		@Override
		public String getClassName() {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-class");
			if (valueNode == null) {
				return "";
			}
			return valueNode.getText();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setClassName()
		 */
		@Override
		public void setClassName(final String className) {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-class");
			if (valueNode == null) {
				valueNode = node_.addChildElement("managed-bean-class");
			}
			valueNode.setText(className);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getScope()
		 */
		@Override
		public Scope getScope() {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-scope");
			if (valueNode == null) {
				return Scope.NONE;
			}
			try {
				return Scope.valueOf(valueNode.getText().toUpperCase());
			} catch (IllegalArgumentException iae) {
				// This will crop up if the scope has an illegal/unknown value
				return Scope.NONE;
			}
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setScope(org.openntf.domino.design.FacesConfig.ManagedBean.Scope)
		 */
		@Override
		public void setScope(final Scope scope) {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-scope");
			if (valueNode == null) {
				valueNode = node_.addChildElement("managed-bean-scope");
			}
			valueNode.setText(scope.toString().toLowerCase());
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#addProperty()
		 */
		@Override
		public org.openntf.domino.design.FacesConfig.ManagedBean.Property addProperty() {
			XMLNode propertyNode = node_.addChildElement("managed-property");
			return new Property(propertyNode);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getProperties()
		 */
		@Override
		public List<org.openntf.domino.design.FacesConfig.ManagedBean.Property> getProperties() {
			return new ModifiableObjectNodeList<org.openntf.domino.design.FacesConfig.ManagedBean.Property>(this, node_, "/",
					"managed-property", Property.class);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getListEntries()
		 */
		@Override
		public List<String> getListEntries() {
			List<String> result = new ArrayList<String>();
			List<XMLNode> nodes = node_.selectNodes("list-entries/value | list-entries/null-value");
			for (XMLNode node : nodes) {
				if ("value".equals(node.getNodeName())) {
					result.add(node.getText());
				} else {
					result.add(null);
				}
			}

			return Collections.unmodifiableList(result);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setListEntries(java.util.Collection)
		 */
		@Override
		public void setListEntries(final Collection<?> listEntries) {
			XMLNode listEntriesNode = node_.selectSingleNode("list-entries");
			String valueClassName = getListValueClassName();
			if (listEntriesNode != null) {
				node_.removeChild(listEntriesNode);
			}
			listEntriesNode = node_.addChildElement("list-entries");
			XMLNode valueClassNode = listEntriesNode.addChildElement("value-class");
			valueClassNode.setText(valueClassName);
			for (Object entry : listEntries) {
				if (entry == null) {
					listEntriesNode.addChildElement("null-value");
				} else {
					XMLNode valueNode = listEntriesNode.addChildElement("value");
					valueNode.setText(String.valueOf(entry));
				}
			}

			// Clear out any map-entries, as they're mutually incompatible with list-entries
			XMLNode mapEntries = node_.selectSingleNode("map-entries");
			if (mapEntries != null) {
				node_.removeChild(mapEntries);
			}
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getValueClassName()
		 */
		@Override
		public String getListValueClassName() {
			XMLNode valueNode = node_.selectSingleNode("list-entries/value-class");
			if (valueNode == null) {
				return "";
			}
			return valueNode.getText();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setValueClassName(java.lang.String)
		 */
		@Override
		public void setListValueClassName(final String className) {
			XMLNode valueNode = node_.selectSingleNode("list-entries/value-class");
			if (valueNode == null) {
				XMLNode listEntries = node_.selectSingleNode("list-entries");
				if (listEntries == null) {
					listEntries = node_.addChildElement("list-entries");
				}
				valueNode = listEntries.addChildElement("value-class");
			}
			valueNode.setText(className);

			// Clear out any map-entries, as they're mutually incompatible with list-entries
			XMLNode mapEntries = node_.selectSingleNode("map-entries");
			if (mapEntries != null) {
				node_.removeChild(mapEntries);
			}
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getMapEntries()
		 */
		@Override
		public Map<String, String> getMapEntries() {
			Map<String, String> result = new LinkedHashMap<String, String>();

			List<XMLNode> mapEntries = node_.selectNodes("map-entries/map-entry");
			for (XMLNode entry : mapEntries) {
				XMLNode keyNode = entry.selectSingleNode("key");
				XMLNode valueNode = entry.selectSingleNode("value | null-value");

				if (keyNode != null) {
					if (valueNode == null || "null-value".equals(valueNode.getNodeName())) {
						result.put(keyNode.getText(), null);
					} else {
						result.put(keyNode.getText(), valueNode.getText());
					}
				}
			}

			return Collections.unmodifiableMap(result);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#setMapEntries(java.util.Map)
		 */
		@Override
		public void setMapEntries(final Map<?, ?> mapEntries) {
			XMLNode mapEntriesNode = node_.selectSingleNode("map-entries");
			if (mapEntriesNode != null) {
				node_.removeChild(mapEntriesNode);
			}
			mapEntriesNode = node_.addChildElement("map-entries");
			for (Map.Entry<?, ?> entry : mapEntries.entrySet()) {
				XMLNode entryNode = mapEntriesNode.addChildElement("map-entry");
				XMLNode keyNode = entryNode.addChildElement("key");
				keyNode.setText(String.valueOf(entry.getKey()));
				if (entry.getValue() == null) {
					entryNode.addChildElement("null-value");
				} else {
					XMLNode valueNode = entryNode.addChildElement("value");
					valueNode.setText(String.valueOf(entry.getValue()));
				}
			}

			// Clear out any list-entries, as they're mutually incompatible with map-entries
			XMLNode listEntries = node_.selectSingleNode("list-entries");
			if (listEntries != null) {
				node_.removeChild(listEntries);
			}
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#remove()
		 */
		@Override
		public void remove() {
			node_.getParentNode().removeChild(node_);
		}

		public class Property implements org.openntf.domino.design.FacesConfig.ManagedBean.Property {
			private final XMLNode node_;

			public Property(final XMLNode node) {
				node_ = node;
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getName()
			 */
			@Override
			public String getName() {
				XMLNode valueNode = node_.selectSingleNode("property-name");
				if (valueNode == null) {
					return "";
				}
				return valueNode.getText();
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setName(java.lang.String)
			 */
			@Override
			public void setName(final String name) {
				XMLNode valueNode = node_.selectSingleNode("property-name");
				if (valueNode == null) {
					valueNode = node_.addChildElement("property-name");
				}
				valueNode.setText(name);
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getValue()
			 */
			@Override
			public String getValue() {
				XMLNode valueNode = node_.selectSingleNode("null-value");
				if (valueNode != null) {
					return null;
				}

				valueNode = node_.selectSingleNode("value");
				if (valueNode == null) {
					return "";
				}
				return valueNode.getText();
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setValue(java.lang.String)
			 */
			@Override
			public void setValue(final String value) {
				if (value == null) {
					XMLNode valueNode = node_.selectSingleNode("value");
					if (valueNode != null) {
						node_.removeChild(valueNode);
					}
					node_.addChildElement("null-value");
				} else {
					XMLNode nullNode = node_.selectSingleNode("null-value");
					if (nullNode != null) {
						node_.removeChild(nullNode);
					}

					XMLNode valueNode = node_.selectSingleNode("value");
					if (valueNode == null) {
						valueNode = node_.addChildElement("value");
					}
					valueNode.setText(value);
				}

				// Clear out any list-entries and map-entries, as they're mutually incompatible with single values
				XMLNode listEntries = node_.selectSingleNode("list-entries");
				if (listEntries != null) {
					node_.removeChild(listEntries);
				}
				listEntries = node_.selectSingleNode("map-entries");
				if (listEntries != null) {
					node_.removeChild(listEntries);
				}
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getValueClassName()
			 */
			@Override
			public String getListValueClassName() {
				XMLNode valueNode = node_.selectSingleNode("list-entries/value-class");
				if (valueNode == null) {
					return "";
				}
				return valueNode.getText();
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setValueClassName(java.lang.String)
			 */
			@Override
			public void setListValueClassName(final String className) {
				XMLNode valueNode = node_.selectSingleNode("list-entries/value-class");
				if (valueNode == null) {
					XMLNode listEntries = node_.selectSingleNode("list-entries");
					if (listEntries == null) {
						listEntries = node_.addChildElement("list-entries");
					}
					valueNode = listEntries.addChildElement("value-class");
				}
				valueNode.setText(className);

				// Clear out any value and map-entries, as they're mutually incompatible with list-entries
				XMLNode value = node_.selectSingleNode("value");
				if (value != null) {
					node_.removeChild(value);
				}
				value = node_.selectSingleNode("null-value");
				if (value != null) {
					node_.removeChild(value);
				}
				value = node_.selectSingleNode("map-entries");
				if (value != null) {
					node_.removeChild(value);
				}
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getListEntries()
			 */
			@Override
			public List<String> getListEntries() {
				List<String> result = new ArrayList<String>();
				List<XMLNode> nodes = node_.selectNodes("list-entries/value | list-entries/null-value");
				for (XMLNode node : nodes) {
					if ("value".equals(node.getNodeName())) {
						result.add(node.getText());
					} else {
						result.add(null);
					}
				}

				return Collections.unmodifiableList(result);
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setListEntries(java.util.Collection)
			 */
			@Override
			public void setListEntries(final Collection<?> listEntries) {
				XMLNode listEntriesNode = node_.selectSingleNode("list-entries");
				String valueClassName = getListValueClassName();
				if (listEntriesNode != null) {
					node_.removeChild(listEntriesNode);
				}
				listEntriesNode = node_.addChildElement("list-entries");
				XMLNode valueClassNode = listEntriesNode.addChildElement("value-class");
				valueClassNode.setText(valueClassName);
				for (Object entry : listEntries) {
					if (entry == null) {
						listEntriesNode.addChildElement("null-value");
					} else {
						XMLNode valueNode = listEntriesNode.addChildElement("value");
						valueNode.setText(String.valueOf(entry));
					}
				}

				// Clear out any value and map-entries, as they're mutually incompatible with list-entries
				XMLNode value = node_.selectSingleNode("value");
				if (value != null) {
					node_.removeChild(value);
				}
				value = node_.selectSingleNode("null-value");
				if (value != null) {
					node_.removeChild(value);
				}
				value = node_.selectSingleNode("map-entries");
				if (value != null) {
					node_.removeChild(value);
				}
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getMapEntries()
			 */
			@Override
			public Map<String, String> getMapEntries() {
				Map<String, String> result = new LinkedHashMap<String, String>();

				List<XMLNode> mapEntries = node_.selectNodes("map-entries/map-entry");
				for (XMLNode entry : mapEntries) {
					XMLNode keyNode = entry.selectSingleNode("key");
					XMLNode valueNode = entry.selectSingleNode("value | null-value");

					if (keyNode != null) {
						if (valueNode == null || "null-value".equals(valueNode.getNodeName())) {
							result.put(keyNode.getText(), null);
						} else {
							result.put(keyNode.getText(), valueNode.getText());
						}
					}
				}

				return Collections.unmodifiableMap(result);
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setMapEntries(java.util.Map)
			 */
			@Override
			public void setMapEntries(final Map<?, ?> mapEntries) {
				XMLNode mapEntriesNode = node_.selectSingleNode("map-entries");
				if (mapEntriesNode != null) {
					node_.removeChild(mapEntriesNode);
				}
				mapEntriesNode = node_.addChildElement("map-entries");
				for (Map.Entry<?, ?> entry : mapEntries.entrySet()) {
					XMLNode entryNode = mapEntriesNode.addChildElement("map-entry");
					XMLNode keyNode = entryNode.addChildElement("key");
					keyNode.setText(String.valueOf(entry.getKey()));
					if (entry.getValue() == null) {
						entryNode.addChildElement("null-value");
					} else {
						XMLNode valueNode = entryNode.addChildElement("value");
						valueNode.setText(String.valueOf(entry.getValue()));
					}
				}

				// Clear out any value or list-entries, as they're mutually incompatible with map-entries
				XMLNode value = node_.selectSingleNode("value");
				if (value != null) {
					node_.removeChild(value);
				}
				XMLNode listEntries = node_.selectSingleNode("list-entries");
				if (listEntries != null) {
					node_.removeChild(listEntries);
				}
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#remove()
			 */
			@Override
			public void remove() {
				node_.getParentNode().removeChild(node_);
			}
		}
	}

	public class Converter implements org.openntf.domino.design.FacesConfig.Converter {
		private final XMLNode node_;

		public Converter(final XMLNode node) {
			node_ = node;
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.Converter#getId()
		 */
		@Override
		public String getId() {
			XMLNode valueNode = node_.selectSingleNode("converter-id");
			if (valueNode == null) {
				return "";
			}
			return valueNode.getText();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.Converter#setId(java.lang.String)
		 */
		@Override
		public void setId(final String id) {
			XMLNode valueNode = node_.selectSingleNode("converter-id");
			if (valueNode == null) {
				valueNode = node_.addChildElement("converter-id");
			}
			valueNode.setText(id);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.Converter#getClassName()
		 */
		@Override
		public String getClassName() {
			XMLNode valueNode = node_.selectSingleNode("converter-class");
			if (valueNode == null) {
				return "";
			}
			return valueNode.getText();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.Converter#setClassName(java.lang.String)
		 */
		@Override
		public void setClassName(final String className) {
			XMLNode valueNode = node_.selectSingleNode("converter-class");
			if (valueNode == null) {
				valueNode = node_.addChildElement("converter-class");
			}
			valueNode.setText(className);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.Converter#remove()
		 */
		@Override
		public void remove() {
			node_.getParentNode().removeChild(node_);
		}
	}

}
