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
package org.openntf.domino.design.impl;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum ODPMapping {

	// AppProperties
	ACL_NOTE("AppProperties", "database.properties", ACLNote.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DB_IMAGE("AppProperties", "$DBIcon", DbImage.class), //hicolor image //$NON-NLS-1$ //$NON-NLS-2$

	// Code
	DATABASE_SCRIPT("Code", "dbscript.lsdb", DatabaseScript.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SHARED_ACTIONS_NOTE("Code/actions", "Shared Actions", SharedActionsNote.class), // should be *.shac? //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_AGENT_F("Code/Agents", ".fa", DesignAgentF.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_AGENT_IJ("Code/Agents", ".ija", DesignAgentIJ.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_AGENT_J("Code/Agents", ".ja", DesignAgentJ.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_AGENT_LS("Code/Agents", ".lsa", DesignAgentLS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_AGENT_A("Code/Agents", ".aa", DesignAgentA.class), // //$NON-NLS-1$ //$NON-NLS-2$
	JAR_RESOURCE("Code/Jars", null, JarResource.class), // //$NON-NLS-1$
	JAVA_FILE("Code/Java", "*", XspJavaResource.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SCRIPT_LIBRARY_JAVA("Code/ScriptLibraries", ".javalib", ScriptLibraryJava.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SCRIPT_LIBRARY_SSJS("Code/ScriptLibraries", ".jss", ScriptLibrarySSJS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SCRIPT_LIBRARY_CSJS("Code/ScriptLibraries", ".js", ScriptLibraryCSJS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SCRIPT_LIBRARY_LS("Code/ScriptLibraries", ".lss", ScriptLibraryLS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	WEB_SERVICE_CONSUMER_LS("Code/WebServiceConsumer", ".lswsc", WebServiceConsumerLS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	WEB_SERVICE_CONSUMER_JAVA("Code/WebServiceConsumer", ".javalib", WebServiceConsumerJava.class), // //$NON-NLS-1$ //$NON-NLS-2$
	WEB_SERVICE_PROVIDER_LS("Code/WebServices", ".lws", WebServiceProviderLS.class), // //$NON-NLS-1$ //$NON-NLS-2$
	WEB_SERVICE_PROVIDER_JAVA("Code/WebServices", ".jws", WebServiceProviderJava.class), // //$NON-NLS-1$ //$NON-NLS-2$

	// CompositeApplication
	COMPOSITE_APP("CompositeApplications/Applications", ".ca", CompositeApp.class), // //$NON-NLS-1$ //$NON-NLS-2$
	COMPOSITE_COMPONENT("CompositeApplications/Components", null, CompositeComponent.class), // //$NON-NLS-1$
	COMPOSITE_WIRING("CompositeApplications/WiringProperties", ".wsdl", CompositeWiring.class), // //$NON-NLS-1$ //$NON-NLS-2$

	CUSTOM_CONTROL("CustomControls", null, CustomControl.class), // //$NON-NLS-1$

	DATA_CONNECTION_RESOURCE("Data/DataConnections", ".dcr", DataConnectionResource.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DB2_VIEW("Data/DB2AccessViews", ".db2v", DB2View.class), // //$NON-NLS-1$ //$NON-NLS-2$

	FOLDER("Folders", ".folder", Folder.class), // //$NON-NLS-1$ //$NON-NLS-2$

	DESIGN_FORM("Forms", ".form", DesignForm.class), // //$NON-NLS-1$ //$NON-NLS-2$

	FRAMESET("Framesets", ".frameset", Frameset.class), // //$NON-NLS-1$ //$NON-NLS-2$

	DESIGN_PAGE("Pages", ".page", DesignPage.class), // //$NON-NLS-1$ //$NON-NLS-2$

	// Resources
	ABOUT_DOCUMENT("Resources", "AboutDocument", AboutDocument.class), // //$NON-NLS-1$ //$NON-NLS-2$
	ICON_NOTE("Resources", "IconNote", IconNote.class), // //$NON-NLS-1$ //$NON-NLS-2$
	USINGDOCUMENT("Resources", "UsingDocument", UsingDocument.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_APPLET("Resources/Applets", ".applet", DesignApplet.class), // //$NON-NLS-1$ //$NON-NLS-2$
	FILE_RESOURCE("Resources/Files", null, FileResource.class), // //$NON-NLS-1$
	IMAGE_RESOURCE("Resources/Images", null, ImageResource.class), // //$NON-NLS-1$
	STYLESHEET("Resources/StyleSheets", ".css", StyleSheet.class), // //$NON-NLS-1$ //$NON-NLS-2$
	THEME("Resources/Themes", null, Theme.class), // //$NON-NLS-1$

	// Shared elements
	SHARED_COLUMN("SharedElements/Columns", ".column", SharedColumn.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SHARED_FIELD("SharedElements/Fields", ".field", SharedField.class), // //$NON-NLS-1$ //$NON-NLS-2$
	NAVIGATOR("SharedElements/Navigators", ".navigator", Navigator.class), // //$NON-NLS-1$ //$NON-NLS-2$
	DESIGN_OUTLINE("SharedElements/Outlines", ".outline", DesignOutline.class), // //$NON-NLS-1$ //$NON-NLS-2$
	SUBFORM("SharedElements/Subforms", ".subform", Subform.class), // //$NON-NLS-1$ //$NON-NLS-2$

	DESIGN_VIEW("Views", ".view", DesignView.class), // //$NON-NLS-1$ //$NON-NLS-2$

	FILE_RESOURCE_WEB_CONTENT("WebContent", null, FileResourceWebContent.class), // //$NON-NLS-1$
	XPAGE("XPages", null, XPage.class), // //$NON-NLS-1$

	FILE_RESOURCE_HIDDEN("", null, FileResourceHidden.class), // //$NON-NLS-1$

	// other design elements
	AGENT_DATA("Other/AgentData", ".agentdata", AgentData.class), // //$NON-NLS-1$ //$NON-NLS-2$
	OTHER_DESIGN_ELEMENT("Other/Unknown", null, OtherDesignElement.class), // //$NON-NLS-1$
	REPLICATION_FORMULA("Other/ReplicationFormulas", null, ReplicationFormula.class), // //$NON-NLS-1$
	SAVED_QUERY("Other/SavedQueries", null, SavedQuery.class), // //$NON-NLS-1$

	;

	/** The folder where this element is stored */
	private final String onDiskFolder_;

	/** The file extension of this element. e.g. "*.lss". If null, all files in the folder are considered as type of this design element */
	private final String onDiskFileName_;

	/** class of the design element */
	private final Class<? extends AbstractDesignBase> clazz_;

	private ODPMapping(final String onDiskFolder, final String onDiskFileName, final Class<? extends AbstractDesignBase> clazz) {
		onDiskFolder_ = onDiskFolder;
		onDiskFileName_ = onDiskFileName;
		clazz_ = clazz;
	}

	@SuppressWarnings("nls")
	public static ODPMapping valueOf(final Class<? extends AbstractDesignBase> clazz) {
		for (ODPMapping mapping : values()) {
			if (mapping.getInstanceClass() == clazz)
				return mapping;
		}
		throw new IllegalArgumentException("Mapping not found for " + clazz.getName());
	}

	public Class<? extends AbstractDesignBase> getInstanceClass() {
		return clazz_;
	}

	public String getFolder() {
		return onDiskFolder_;
	}

	public String getFileName() {
		return onDiskFileName_;
	}

}
