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

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum ODPMapping {

	// AppProperties
	ACL_NOTE("AppProperties", "database.properties", ACLNote.class), //
	DB_IMAGE("AppProperties", "$DBIcon", DbImage.class), //hicolor image

	// Code
	DATABASE_SCRIPT("Code", "dbscript.lsdb", DatabaseScript.class), //
	SHARED_ACTIONS_NOTE("Code/actions", "Shared Actions", SharedActionsNote.class), // should be *.shac?
	DESIGN_AGENT_F("Code/Agents", ".fa", DesignAgentF.class), //
	DESIGN_AGENT_IJ("Code/Agents", ".ija", DesignAgentIJ.class), //
	DESIGN_AGENT_J("Code/Agents", ".ja", DesignAgentJ.class), //
	DESIGN_AGENT_LS("Code/Agents", ".lsa", DesignAgentLS.class), //
	DESIGN_AGENT_A("Code/Agents", ".aa", DesignAgentA.class), //
	JAR_RESOURCE("Code/Jars", null, JarResource.class), //
	JAVA_FILE("Code/Java", "*", XspJavaResource.class), //
	SCRIPT_LIBRARY_JAVA("Code/ScriptLibraries", ".javalib", ScriptLibraryJava.class), //
	SCRIPT_LIBRARY_SSJS("Code/ScriptLibraries", ".jss", ScriptLibrarySSJS.class), //
	SCRIPT_LIBRARY_CSJS("Code/ScriptLibraries", ".js", ScriptLibraryCSJS.class), //
	SCRIPT_LIBRARY_LS("Code/ScriptLibraries", ".lss", ScriptLibraryLS.class), //
	WEB_SERVICE_CONSUMER_LS("Code/WebServiceConsumer", ".lswsc", WebServiceConsumerLS.class), //
	WEB_SERVICE_CONSUMER_JAVA("Code/WebServiceConsumer", ".javalib", WebServiceConsumerJava.class), //
	WEB_SERVICE_PROVIDER_LS("Code/WebServices", ".lws", WebServiceProviderLS.class), //
	WEB_SERVICE_PROVIDER_JAVA("Code/WebServices", ".jws", WebServiceProviderJava.class), //

	// CompositeApplication
	COMPOSITE_APP("CompositeApplications/Applications", ".ca", CompositeApp.class), //
	COMPOSITE_COMPONENT("CompositeApplications/Components", null, CompositeComponent.class), //
	COMPOSITE_WIRING("CompositeApplications/WiringProperties", ".wsdl", CompositeWiring.class), //

	CUSTOM_CONTROL("CustomControls", null, CustomControl.class), //

	DATA_CONNECTION_RESOURCE("Data/DataConnections", ".dcr", DataConnectionResource.class), //
	DB2_VIEW("Data/DB2AccessViews", ".db2v", DB2View.class), //

	FOLDER("Folders", ".folder", Folder.class), //

	DESIGN_FORM("Forms", ".form", DesignForm.class), //

	FRAMESET("Framesets", ".frameset", Frameset.class), //

	DESIGN_PAGE("Pages", ".page", DesignPage.class), //

	// Resources
	ABOUT_DOCUMENT("Resources", "AboutDocument", AboutDocument.class), //
	ICON_NOTE("Resources", "IconNote", IconNote.class), //
	USINGDOCUMENT("Resources", "UsingDocument", UsingDocument.class), //
	DESIGN_APPLET("Resources/Applets", ".applet", DesignApplet.class), //
	FILE_RESOURCE("Resources/Files", null, FileResource.class), //
	IMAGE_RESOURCE("Resources/Images", null, ImageResource.class), //
	STYLESHEET("Resources/StyleSheets", ".css", StyleSheet.class), //
	THEME("Resources/Themes", null, Theme.class), //

	// Shared elements
	SHARED_COLUMN("SharedElements/Columns", ".column", SharedColumn.class), //
	SHARED_FIELD("SharedElements/Fields", ".field", SharedField.class), //
	NAVIGATOR("SharedElements/Navigators", ".navigator", Navigator.class), //
	DESIGN_OUTLINE("SharedElements/Outlines", ".outline", DesignOutline.class), //
	SUBFORM("SharedElements/Subforms", ".subform", Subform.class), //

	DESIGN_VIEW("Views", ".view", DesignView.class), //

	FILE_RESOURCE_WEB_CONTENT("WebContent", null, FileResourceWebContent.class), //
	XPAGE("XPages", null, XPage.class), //

	FILE_RESOURCE_HIDDEN("", null, FileResourceHidden.class), //

	// other design elements
	AGENT_DATA("Other/AgentData", ".agentdata", AgentData.class), //
	OTHER_DESIGN_ELEMENT("Other/Unknown", null, OtherDesignElement.class), //
	REPLICATION_FORMULA("Other/ReplicationFormulas", null, ReplicationFormula.class), //
	SAVED_QUERY("Other/SavedQueries", null, SavedQuery.class), //

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
