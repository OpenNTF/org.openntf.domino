/**
 * 
 */
package org.openntf.domino.schema;

import java.util.logging.Logger;

import org.openntf.domino.Document;

/**
 * @author nfreeman
 * 
 */
public class DatabaseSchema {
	private static final Logger log_ = Logger.getLogger(DatabaseSchema.class.getName());
	private static final long serialVersionUID = 1L;

	static class ItemDefinition {
		private String name_;
		private String uiLabel_;

	}

	/**
	 * 
	 */
	public DatabaseSchema() {
		// TODO Auto-generated constructor stub
	}

	public void save() {
		// TODO
	}

	public boolean validate(Document document) {
		boolean result = false;
		// TODO
		return result;
	}

}
