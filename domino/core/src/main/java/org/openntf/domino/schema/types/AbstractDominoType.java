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
/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.Collection;
import java.util.logging.Logger;

import org.openntf.domino.schema.IDominoType;
import org.openntf.domino.schema.IItemDefinition;
import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public abstract class AbstractDominoType implements IDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoType.class.getName());

	protected AbstractDominoType() {

	}

	@Override
	public boolean validateItem(final org.openntf.domino.Item item) throws ItemException {
		Collection<Object> values = item.getValues();
		for (Object value : values) {
			if (!validateValue(value))
				return false;
		}
		return true;
	}

	@Override
	public boolean validateItem(final org.openntf.domino.Item item, final IItemDefinition defintion) throws ItemException {
		//TODO make this work!
		Collection<Object> values = item.getValues();
		for (Object value : values) {
			if (!validateValue(value))
				return false;
		}
		return true;
	}

	public abstract boolean validateValue(final Object value) throws ItemException;

	@Override
	public void setItemToDefault(final org.openntf.domino.Item item) {
		item.setValueString(""); //$NON-NLS-1$
	}
}
