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
