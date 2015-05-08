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

package org.openntf.domino.design;

import java.util.SortedSet;

/**
 * @author jgallagher
 * 
 */
public interface DatabaseDesign extends org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @param create
	 *            whether a new using document should be created when one does not yet exist in the database
	 * @return the About Document note of the database, or null if no document exists and create is false
	 */
	public AboutDocument getAboutDocument(boolean create);

	/**
	 * @return the ACL note of the database
	 */
	public ACLNote getACL();

	/**
	 * @return the form marked as default for the database, as a DesignForm object
	 */
	public DesignForm getDefaultForm();

	/**
	 * @return the view marked as default for the database, as a DesignForm object
	 */
	public DesignView getDefaultView();

	/**
	 * @return an object representing the faces-config.xml file of the database
	 */
	public FacesConfig getFacesConfig();

	/**
	 * @return a SortedSet of the Java class names defined in Java resources in the database in "canonical" format (e.g.
	 *         "some.package.name.SomeClassName")
	 */
	public SortedSet<String> getJavaResourceClassNames();

	/**
	 * @return the icon note of the database
	 */
	public IconNote getIconNote();

	/**
	 * @return the replication-formula note for the database
	 */
	public ReplicationFormula getReplicationFormula();

	/**
	 * @param create
	 *            whether a new using document should be created when one does not yet exist in the database
	 * @return the Using Document note of the database, or null if no document exists and create is false
	 */
	public UsingDocument getUsingDocument(boolean create);

	/**
	 * Gets an XSP-Property from WEB-INF/xsp.properties
	 * 
	 * @param propertyName
	 * @return the value of that property
	 */
	String[] getXspProperty(String propertyName);

	/**
	 * Checks whether or not the API is enabled for the current database
	 * 
	 * @return boolean whether or not enabled
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAPIEnabled();

	/**
	 * common code to test if a flag is set in the xsp.properties file for the "org.openntf.domino.xsp" value.
	 * 
	 * @param flagName
	 *            use upperCase for flagName, e.g. RAID
	 * @return true if the flag is set
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAppFlagSet(final String flagName);

	/**
	 * Returns all design elements matching to the according formula. The type of the design element is autodetected.<br>
	 * <b><font color=red>WARNING.</font></b> This method is slow on big databases. <br/>
	 * Better use {@link #getDesignElements()} if you do not need Private Forms/Views (for private Forms/Views you can also use
	 * db.getForm())
	 * 
	 * @param formula
	 *            the formula
	 * @return a subclass of DesignBase
	 */
	public DesignCollection<DesignBase> searchDesignElements(String formula);

	/**
	 * Returns all design elements matching to the according formula and type.<br>
	 * <b><font color=red>WARNING.</font></b> This method is slow on big databases. <br/>
	 * Better use {@link #getDesignElements(Class)} or {@link #getDesignElements(Class, String)} db.getForm())
	 * 
	 * @param type
	 *            the type
	 * @param formula
	 *            the formula
	 * @return class of type
	 */
	public <T extends DesignBase> DesignCollection<T> searchDesignElements(final Class<T> type, final String formula);

	/**
	 * Returns all PUBLIC design elements by accessing the DesignIndex directly (only if NAPI is present)<br>
	 * If no NAPI is present, this method may return also private design elements, otherwise not!
	 * 
	 * @return a subclass of DesignBase
	 */
	DesignCollection<DesignBase> getDesignElements();

	/**
	 * Returns all PUBLIC design elements of the given type (only if NAPI is present)<br>
	 * If no NAPI is present, this method may return also private design elements, otherwise not!
	 * 
	 * @param type
	 *            the type
	 * 
	 * @return a class of type
	 */
	public <T extends DesignBase> DesignCollection<T> getDesignElements(Class<T> type);

	/**
	 * Returns all PUBLIC design elements of the given type and name (only if NAPI is present)<br>
	 * If no NAPI is present, this method may return also private design elements, otherwise not!
	 * 
	 * @param type
	 *            the type
	 * @param name
	 *            the name or alias of the element
	 * @return a class of type
	 */
	public <T extends DesignBase> DesignCollection<T> getDesignElements(Class<T> type, String name);

	/**
	 * Returns the first PUBLIC design element of the given type and name (only if NAPI is present)<br>
	 * If no NAPI is present, this method may return also private design elements, otherwise not!
	 * 
	 * @param type
	 *            the type
	 * @param name
	 *            the name or alias of the element
	 * @return a class of type
	 */
	public <T extends DesignBase> T getDesignElement(Class<T> type, String name);

	/**
	 * Clears all internal caches. This method must be called, if the DatabaseDesign has changed
	 */
	void flush();

}
