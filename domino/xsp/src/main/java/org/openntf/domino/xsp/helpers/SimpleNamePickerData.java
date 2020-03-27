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
package org.openntf.domino.xsp.helpers;

import java.util.List;
import java.util.logging.Logger;

import com.ibm.xsp.extlib.component.picker.data.INamePickerData;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;

/**
 * @author Nathan T. Freeman
 * 
 *         SimpleNamePickerData, for use with the NamePicker control
 */
// TODO: Remove before 3.0 - all functionality introduced in ExtLib 14
public class SimpleNamePickerData implements INamePickerData {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(SimpleNamePickerData.class.getName());

	/**
	 * Constructor
	 */
	public SimpleNamePickerData() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#hasCapability(int)
	 */
	@Override
	public boolean hasCapability(final int capability) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#getSourceLabels()
	 */
	@Override
	public String[] getSourceLabels() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#readEntries(com.ibm.xsp.extlib.component.picker.data.IPickerOptions)
	 */
	@Override
	public IPickerResult readEntries(final IPickerOptions options) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#loadEntries(java.lang.Object[], java.lang.String[])
	 */
	@Override
	public List<IPickerEntry> loadEntries(final Object[] ids, final String[] attributeNames) {
		return null;
	}
}
