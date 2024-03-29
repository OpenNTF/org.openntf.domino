/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.util.logging.Logger;

import org.openntf.domino.Item;
import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public class CurrencyType extends AbstractDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(CurrencyType.class.getName());

	CurrencyType() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Currency"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateItem(org.openntf.domino.Item)
	 */
	@Override
	public boolean validateItem(final Item item) throws ItemException {
		//		Vector<Object> v = item.getValues();
		//		for (Object o : v) {
		//
		//		}
		return super.validateItem(item);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateValue(java.lang.Object)
	 */
	@Override
	public boolean validateValue(final Object value) throws ItemException {
		return false;
	}
}
