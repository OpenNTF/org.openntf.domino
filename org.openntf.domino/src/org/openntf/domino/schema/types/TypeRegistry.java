package org.openntf.domino.schema.types;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openntf.domino.schema.IDominoType;

public enum TypeRegistry {
	INSTANCE;

	static {
		loadDefaultTypes();
	}

	private final Map<Class<?>, IDominoType> registry_ = new ConcurrentHashMap<Class<?>, IDominoType>();

	private static void loadDefaultTypes() {
		INSTANCE.addType(new StringType());
		INSTANCE.addType(new BigStringType());
		INSTANCE.addType(new EmailType());
		INSTANCE.addType(new ColorType());
		INSTANCE.addType(new FormulaType());
		INSTANCE.addType(new PhoneType());
		INSTANCE.addType(new StreetAddressType());
		INSTANCE.addType(new URLType());

		INSTANCE.addType(new IntegerType());
		INSTANCE.addType(new DecimalType());
		INSTANCE.addType(new CurrencyType());

		INSTANCE.addType(new DateTimeType());
		INSTANCE.addType(new DateType());
		INSTANCE.addType(new TimeType());

		INSTANCE.addType(new NameType());
		INSTANCE.addType(new AuthorsType());
		INSTANCE.addType(new ReadersType());

		INSTANCE.addType(new RichTextType());
		INSTANCE.addType(new FileType());
	}

	public IDominoType addType(final IDominoType type) {
		Class<?> cls = type.getClass();
		IDominoType chk = registry_.get(cls);
		if (chk == null) {
			registry_.put(cls, type);
		}
		return type;
	}

	public IDominoType removeType(final IDominoType type) {
		Class<?> cls = type.getClass();
		return registry_.remove(cls);
	}

}
