/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DatabaseDesign.DbProperties;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * NOTE: The icon note is cached by the Session. So if you change a database property stored in the icon, you won't get the updated setting
 * until the next server request
 *
 * @author jgallagher
 * @author Paul Withers
 *
 */
public class IconNote extends AbstractDesignBaseNamed implements org.openntf.domino.design.IconNote {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IconNote.class.getName());

	/**
	 * @param document
	 */
	protected IconNote(final Document document) {
		super(document);
	}

	protected IconNote(final Database database) {
		super(database, DesignForm.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_iconNote.xml"));
	}

	@Override
	protected boolean enforceRawFormat() {
		// IconNote is exported in RAW-format. There is no DXL representation
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		getDxlNode("/note/item[@name='$TITLE']/text").setTextContent(name);
	}

	@Override
	public void setDASMode(final DASMode mode) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ALLOW_DAS.getPropertyName() + "']");
		if (node == null) {
			node = getDxlNode("/note").addChildElement("item");
			node.setAttribute("name", DbProperties.ALLOW_DAS.getPropertyName());
			node = node.addChildElement("number");
		}
		switch (mode) {
		case NONE:
			node.setTextContent("0");
			break;
		case VIEWS:
			node.setTextContent("1");
			break;
		case VIEWSANDDOCUMENTS:
			node.setTextContent("2");
			break;
		}
	}

	@Override
	public DASMode getDASMode() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ALLOW_DAS.getPropertyName() + "']/number");
		if (node == null) {
			return DASMode.NONE;
		} else {
			if ("1".equals(node.getText())) {
				return DASMode.VIEWS;
			} else if ("2".equals(node.getText())) {
				return DASMode.VIEWSANDDOCUMENTS;
			} else {
				return DASMode.NONE;
			}
		}
	}

	@Override
	public String[] getXotsClassNames() {
		Document iconDoc = getDocument();
		if (iconDoc != null && iconDoc.hasItem("$Xots")) {
			String[] xotsClassNames = iconDoc.getItemValue("$Xots", String[].class);
			if (xotsClassNames != null) {
				return xotsClassNames;
			}
		}
		return new String[] {};
	}

	@Override
	public boolean isEnhancedHTML() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ENHANCED_HTML.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public void setEnhancedHTML(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ENHANCED_HTML.getPropertyName() + "']");
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.ENHANCED_HTML.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isBlockICAA() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.BLOCK_ICAA.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public void setBlockICAA(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.BLOCK_ICAA.getPropertyName() + "']");
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.BLOCK_ICAA.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isDisableViewExport() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.NO_EXPORT_VIEW.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public void setDisableViewExport(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.NO_EXPORT_VIEW.getPropertyName() + "']");
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.NO_EXPORT_VIEW.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isLaunchXPageRunOnServer() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public void setLaunchXPageRunOnServer(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName() + "']");
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isDocumentSummary16MB() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.DOCUMENT_SUMMARY_16MB.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public boolean isDaosEnabled() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.DAOS_ENABLED.getPropertyName() + "']");
		return node != null;
	}

	@Override
	public boolean isAllowDas() {
		if (DASMode.NONE.equals(getDASMode())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int getCssExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$CSSExpires']/text");
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setCssExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$CSSExpires']");
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getFileExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$FileExpires']/text");
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setFileExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$FileExpires']");
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getImageExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$ImageExpires']/text");
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setImageExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$ImageExpires']");
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getJsExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$JSExpires']/text");
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setJsExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$JSExpires']");
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item");
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName());
				node.addChildElement("text").setTextContent("1");
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

}
