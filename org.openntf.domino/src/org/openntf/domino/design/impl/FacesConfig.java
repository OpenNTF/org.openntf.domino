/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * @author jgallagher
 * 
 */

// TODO Switch Collection-returning methods to return "views" on the file for manipulation
public class FacesConfig extends FileResource implements org.openntf.domino.design.FacesConfig {
	private static final Logger log_ = Logger.getLogger(FacesConfig.class.getName());
	private static final long serialVersionUID = 1L;

	private final XMLDocument xml_ = new XMLDocument();

	/**
	 * @param document
	 */
	public FacesConfig(final Document document) {
		super(document);

		try {
			xml_.loadString(new String(getFileData()));
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * @param database
	 */
	public FacesConfig(final Database database) {
		super(database);

		try {
			xml_.loadString("<faces-config/>");
		} catch (SAXException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		} catch (ParserConfigurationException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#addConverter()
	 */
	public org.openntf.domino.design.FacesConfig.Converter addConverter() {
		return new Converter(xml_.addChildElement("converter"));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getConverters()
	 */
	public Collection<org.openntf.domino.design.FacesConfig.Converter> getConverters() {
		List<org.openntf.domino.design.FacesConfig.Converter> result = new ArrayList<org.openntf.domino.design.FacesConfig.Converter>();
		for (XMLNode node : xml_.selectNodes("/faces-config/converter")) {
			result.add(new Converter(node));
		}
		return Collections.unmodifiableList(result);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#addManagedBean()
	 */
	public org.openntf.domino.design.FacesConfig.ManagedBean addManagedBean() {
		return new ManagedBean(xml_.addChildElement("managed-bean"));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getManagedBeans()
	 */
	public Collection<org.openntf.domino.design.FacesConfig.ManagedBean> getManagedBeans() {
		List<org.openntf.domino.design.FacesConfig.ManagedBean> result = new ArrayList<org.openntf.domino.design.FacesConfig.ManagedBean>();
		for (XMLNode node : xml_.selectNodes("/faces-config/managed-bean")) {
			result.add(new ManagedBean(node));
		}
		return Collections.unmodifiableList(result);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getPhaseListeners()
	 */
	public Collection<String> getPhaseListeners() {
		List<String> result = new ArrayList<String>();
		for (XMLNode node : xml_.selectNodes("/faces-config/lifecycle/phase-listener")) {
			result.add(node.getText());
		}
		return Collections.unmodifiableList(result);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.FacesConfig#getViewHandlers()
	 */
	public Collection<String> getViewHandlers() {
		List<String> result = new ArrayList<String>();
		for (XMLNode node : xml_.selectNodes("/faces-config/lifecycle/phase-listener")) {
			result.add(node.getText());
		}
		return Collections.unmodifiableList(result);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.impl.AbstractDesignNoteBase#save()
	 */
	@Override
	public void save() {
		try {
			setFileData(xml_.getXml().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			DominoUtils.handleException(e);
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
		super.save();
	}

	public class ManagedBean implements org.openntf.domino.design.FacesConfig.ManagedBean {
		private final XMLNode node_;

		public ManagedBean(final XMLNode node) {
			node_ = node;
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getName()
		 */
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
		public void setName(final String name) {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-class");
			if (valueNode == null) {
				valueNode = node_.addChildElement("managed-bean-class");
			}
			valueNode.setText(name);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.design.FacesConfig.ManagedBean#getClassName()
		 */
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
		public void setScope(final Scope scope) {
			XMLNode valueNode = node_.selectSingleNode("managed-bean-scope");
			if (valueNode == null) {
				valueNode = node_.addChildElement("managed-bean-scope");
			}
			valueNode.setText(scope.toString().toLowerCase());
		}

		public class Property implements org.openntf.domino.design.FacesConfig.ManagedBean.Property {
			private final XMLNode node_;

			public Property(final XMLNode node) {
				node_ = node;
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getName()
			 */
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
			public void setName(final String name) {
				XMLNode valueNode = node_.selectSingleNode("property-name");
				if (valueNode == null) {
					valueNode = node_.addChildElement("property-name");
				}
				valueNode.setText(name);
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getValueClassName()
			 */
			public String getValueClassName() {
				// TODO Auto-generated method stub
				return null;
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setValueClassName(java.lang.String)
			 */
			public void setValueClassName(final String className) {
				// TODO Auto-generated method stub

			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#getListEntries()
			 */
			public List<?> getListEntries() {
				// TODO Auto-generated method stub
				return null;
			}

			/* (non-Javadoc)
			 * @see org.openntf.domino.design.FacesConfig.ManagedBean.Property#setListEntries(java.util.Collection)
			 */
			public void setListEntries(final Collection<?> listEntries) {
				// TODO Auto-generated method stub

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
		public void setClassName(final String className) {
			XMLNode valueNode = node_.selectSingleNode("converter-class");
			if (valueNode == null) {
				valueNode = node_.addChildElement("converter-class");
			}
			valueNode.setText(className);
		}

	}
}
