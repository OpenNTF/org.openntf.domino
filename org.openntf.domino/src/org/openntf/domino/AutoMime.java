package org.openntf.domino;

public enum AutoMime {

	WRAP_ALL, 	// Always try autowrap when using "replaceItemValue(String, Object)"
				// This is neccessary for compatibility with "old" applications.

	WRAP_32K, 	// Only autowrap Dominofriendly values that exceeds 32K.
				// This should be usesd if possible.
				// get/put should be used to do autowrap
				// replaceItemValueCustomData always wraps the given object.

	WRAP_NONE	// Never autowrap. Useful for foreign DBs that do not support AutoMime
				// put will only write dominofriendly objects.
				// the only way to write a MIME bean is to use replaceItemValueCustomData when 
				// specifying "mime-bean" as dataType
	;
	public static final String MAGIC_MIME_BEAN = "mime-bean";
}
