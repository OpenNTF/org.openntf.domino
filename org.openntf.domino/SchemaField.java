///**
// * 
// */
//package de.foconis.schema;
//
//import java.util.logging.Logger;
//
///**
// * @author praml
// * 
// */
///**
// * @author praml
// * 
// */
//public class SchemaField {
//	private static final Logger log_ = Logger.getLogger(SchemaField.class.getName());
//	private static final long serialVersionUID = 1L;
//
//	//private String name;
//	//private FieldType type = FieldType.TEXT;
//	//private boolean multivalue;
//
//	private Object defaultValue;
//	private boolean published; 			// TRUE=displayed in data palette
//	private boolean summary = true;		// TRUE=field is stored as summary
//	private boolean saveToDisk = true;	// TRUE=field is saved to disk
//
//	private IFieldValidator validator;
//
//	// for comptibility (normally useless in xsp)
//	private boolean encrypted = false;	// encryption not supported in web
//	private boolean signed = false;		// signing also difficult in web
//
//	/**
//	 * Generates a new singlevalue field based on a name Fields starting with $ are not published to data palette Fields starting with $tmp
//	 * are not saved to disk
//	 * 
//	 * @param name
//	 */
//	public SchemaField(final String name) {
//		super();
//		published = (name.charAt(0) != '$'); // fields starting with $ are not published to data palette
//		saveToDisk = !name.startsWith("$tmp");
//		this.name = name;
//	}
//
//	/**
//	 * Generates a new field based on a name and field type (e.g. Type.NUMBER)
//	 * 
//	 * @param name
//	 * @param type
//	 */
//	public SchemaField(final String name, final FieldType type) {
//		this(name);
//		this.type = type;
//	}
//
//	/**
//	 * Generates a new field based on a name and field type (e.g. Type.NUMBER) and multivalue option
//	 * 
//	 * @param name
//	 * @param type
//	 * @param multivalue
//	 */
//	public SchemaField(final String name, final boolean multivalue) {
//		this(name);
//		this.multivalue = multivalue;
//	}
//
//	/**
//	 * Generates a new field based on a name and multivalue option
//	 * 
//	 * @param name
//	 * @param type
//	 * @param multivalue
//	 */
//	public SchemaField(final String name, final FieldType type, final boolean multivalue) {
//		this(name, type);
//		this.multivalue = multivalue;
//	}
//
//	/**
//	 * @param name
//	 * @param type
//	 * @param defaultValue
//	 * @param multivalue
//	 * @param published
//	 * @param summary
//	 * @param saveToDisk
//	 * @param validator
//	 * @param encrypted
//	 * @param signed
//	 */
//	public SchemaField(final String name, final FieldType type, final boolean multivalue, final Object defaultValue,
//			final boolean published, final boolean summary, final boolean saveToDisk, final IFieldValidator validator,
//			final boolean encrypted, final boolean signed) {
//		this(name, type, multivalue);
//
//		this.defaultValue = defaultValue;
//		this.published = published;
//		this.summary = summary;
//		this.saveToDisk = saveToDisk;
//		this.validator = validator;
//		this.encrypted = encrypted;
//		this.signed = signed;
//	}
//
//	/**
//	 * @return the defaultValue
//	 */
//	public Object getDefaultValue() {
//		return defaultValue;
//	}
//
//	/**
//	 * @param defaultValue
//	 *            the defaultValue to set
//	 * @return this for chaining
//	 */
//	public SchemaField setDefaultValue(final Object defaultValue) {
//		this.defaultValue = defaultValue;
//		return this;
//	}
//
//	/**
//	 * @return the published
//	 */
//	public boolean isPublished() {
//		return published;
//	}
//
//	/**
//	 * @param published
//	 *            the published to set
//	 * @return
//	 * @return this for chaining *
//	 */
//	public SchemaField setPublished(final boolean published) {
//		this.published = published;
//		return this;
//	}
//
//	/**
//	 * @return the summary
//	 */
//	public boolean isSummary() {
//		return summary;
//	}
//
//	/**
//	 * @param summary
//	 *            the summary to set
//	 * @return
//	 * @return this for chaining
//	 */
//	public SchemaField setSummary(final boolean summary) {
//		this.summary = summary;
//		return this;
//	}
//
//	/**
//	 * @return the saveToDisk
//	 */
//	public boolean isSaveToDisk() {
//		return saveToDisk;
//	}
//
//	/**
//	 * @param saveToDisk
//	 *            the saveToDisk to set
//	 * @return
//	 * @return this for chaining
//	 */
//	public SchemaField setSaveToDisk(final boolean saveToDisk) {
//		this.saveToDisk = saveToDisk;
//		return this;
//	}
//
//	/**
//	 * @return the validator
//	 */
//	public IFieldValidator getValidator() {
//		return validator;
//	}
//
//	/**
//	 * @param validator
//	 *            the validator to set
//	 * @return
//	 * @return this for chaining
//	 */
//	public SchemaField setValidator(final IFieldValidator validator) {
//		this.validator = validator;
//		return this;
//	}
//
//	/**
//	 * @return the encrypted
//	 */
//	public boolean isEncrypted() {
//		return encrypted;
//	}
//
//	/**
//	 * @param encrypted
//	 *            the encrypted to set
//	 * @return
//	 * @return this for chaining
//	 */
//	public SchemaField setEncrypted(final boolean encrypted) {
//		this.encrypted = encrypted;
//		return this;
//	}
//
//	/**
//	 * @return the signed
//	 */
//	public boolean isSigned() {
//		return signed;
//	}
//
//	/**
//	 * @param signed
//	 *            the signed to set
//	 * @return
//	 * @return this for chaining
//	 */
//	public SchemaField setSigned(final boolean signed) {
//		this.signed = signed;
//		return this;
//	}
//
//	/**
//	 * @return the name
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * @return the type
//	 */
//	public FieldType getType() {
//		return type;
//	}
//
//	/**
//	 * @return the multivalue
//	 */
//	public boolean isMultivalue() {
//		return multivalue;
//	}
//
//}
