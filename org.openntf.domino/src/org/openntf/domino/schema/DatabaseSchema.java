/**
 * 
 */
package org.openntf.domino.schema;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.Document;

/**
 * @author nfreeman
 * 
 */
public class DatabaseSchema {
	private static final Logger log_ = Logger.getLogger(DatabaseSchema.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Flags {
		SUMMARY, READERS, AUTHORS, PROTECTED, SIGNED, ENCRYPTED
	}

	static class DocumentDefinition {
		private String name_; // will be used as Form field value
		private Set<ItemDefinition> itemDefinitions_;
		private Map<String, String> overrideLabels_;
	}

	static class ItemDefinition {
		private String name_;
		private String defaultLabel_;
		private Class<?> type_;
		private Set<Flags> flags_;
		private Object defaultValue_;
		private ItemValidation validator_;
	}

	static class ItemValidation {
		private boolean required_;
		private boolean unique_;
		private String uniqueFormula_;
		private Pattern expression_;
		private long maxValue_;
		private long minValue_;
		private int maxMembers_;
		private int minMembers_;

		public void setRegex(String expression) {
			expression_ = Pattern.compile(expression);
		}

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
