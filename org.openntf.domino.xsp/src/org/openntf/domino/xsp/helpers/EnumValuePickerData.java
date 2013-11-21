/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.List;
import java.util.logging.Logger;

import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IValuePickerData;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * @author Nathan T. Freeman
 * 
 */
public class EnumValuePickerData implements IValuePickerData {
	private static final Logger log_ = Logger.getLogger(EnumValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EnumValuePickerData() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#hasCapability(int)
	 */
	@Override
	public boolean hasCapability(final int capability) {
		DominoDocument doc;
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#getSourceLabels()
	 */
	@Override
	public String[] getSourceLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#readEntries(com.ibm.xsp.extlib.component.picker.data.IPickerOptions)
	 */
	@Override
	public IPickerResult readEntries(final IPickerOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#loadEntries(java.lang.Object[], java.lang.String[])
	 */
	@Override
	public List<IPickerEntry> loadEntries(final Object[] ids, final String[] attributeNames) {
		// TODO Auto-generated method stub
		return null;
	}
}
