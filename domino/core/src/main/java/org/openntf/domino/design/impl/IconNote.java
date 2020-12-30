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
		super(database, DesignForm.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_iconNote.xml")); //$NON-NLS-1$
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
		getDxlNode("/note/item[@name='$TITLE']/text").setTextContent(name); //$NON-NLS-1$
	}

	@Override
	public void setDASMode(final DASMode mode) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ALLOW_DAS.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		if (node == null) {
			node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
			node.setAttribute("name", DbProperties.ALLOW_DAS.getPropertyName()); //$NON-NLS-1$
			node = node.addChildElement("number"); //$NON-NLS-1$
		}
		switch (mode) {
		case NONE:
			node.setTextContent("0"); //$NON-NLS-1$
			break;
		case VIEWS:
			node.setTextContent("1"); //$NON-NLS-1$
			break;
		case VIEWSANDDOCUMENTS:
			node.setTextContent("2"); //$NON-NLS-1$
			break;
		}
	}

	@Override
	public DASMode getDASMode() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ALLOW_DAS.getPropertyName() + "']/number"); //$NON-NLS-1$ //$NON-NLS-2$
		if (node == null) {
			return DASMode.NONE;
		} else {
			if ("1".equals(node.getText())) { //$NON-NLS-1$
				return DASMode.VIEWS;
			} else if ("2".equals(node.getText())) { //$NON-NLS-1$
				return DASMode.VIEWSANDDOCUMENTS;
			} else {
				return DASMode.NONE;
			}
		}
	}

	@Override
	public String[] getXotsClassNames() {
		Document iconDoc = getDocument();
		if (iconDoc != null && iconDoc.hasItem("$Xots")) { //$NON-NLS-1$
			String[] xotsClassNames = iconDoc.getItemValue("$Xots", String[].class); //$NON-NLS-1$
			if (xotsClassNames != null) {
				return xotsClassNames;
			}
		}
		return new String[] {};
	}

	@Override
	public boolean isEnhancedHTML() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ENHANCED_HTML.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		return node != null;
	}

	@Override
	public void setEnhancedHTML(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.ENHANCED_HTML.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.ENHANCED_HTML.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isBlockICAA() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.BLOCK_ICAA.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		return node != null;
	}

	@Override
	public void setBlockICAA(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.BLOCK_ICAA.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.BLOCK_ICAA.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isDisableViewExport() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.NO_EXPORT_VIEW.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		return node != null;
	}

	@Override
	public void setDisableViewExport(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.NO_EXPORT_VIEW.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.NO_EXPORT_VIEW.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isLaunchXPageRunOnServer() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		return node != null;
	}

	@Override
	public void setLaunchXPageRunOnServer(final boolean enabled) {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		if (enabled) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public boolean isDocumentSummary16MB() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.DOCUMENT_SUMMARY_16MB.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		return node != null;
	}

	@Override
	public boolean isDaosEnabled() {
		XMLNode node = getDxlNode("/note/item[@name='" + DbProperties.DAOS_ENABLED.getPropertyName() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
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
		XMLNode node = getDxlNode("/note/item[@name='$CSSExpires']/text"); //$NON-NLS-1$
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setCssExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$CSSExpires']"); //$NON-NLS-1$
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getFileExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$FileExpires']/text"); //$NON-NLS-1$
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setFileExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$FileExpires']"); //$NON-NLS-1$
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getImageExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$ImageExpires']/text"); //$NON-NLS-1$
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setImageExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$ImageExpires']"); //$NON-NLS-1$
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

	@Override
	public int getJsExpiry() {
		XMLNode node = getDxlNode("/note/item[@name='$JSExpires']/text"); //$NON-NLS-1$
		if (null == node) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.parseInt(node.getText());
		}
	}

	@Override
	public void setJsExpiry(final int days) {
		XMLNode node = getDxlNode("/note/item[@name='$JSExpires']"); //$NON-NLS-1$
		if (days > Integer.MIN_VALUE) {
			if (node == null) {
				node = getDxlNode("/note").addChildElement("item"); //$NON-NLS-1$ //$NON-NLS-2$
				node.setAttribute("name", DbProperties.LAUNCH_XPAGE_ON_SERVER.getPropertyName()); //$NON-NLS-1$
				node.addChildElement("text").setTextContent("1"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if (node != null) {
				node.getParentNode().removeChild(node);
			}
		}
	}

}
