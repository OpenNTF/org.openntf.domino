/**
 * 
 */
package org.openntf.domino.xsp.napi;

import java.util.List;

import org.openntf.domino.utils.DominoUtils;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesNote;

/**
 * @author praml
 * 
 */
public class NapiDocument implements org.openntf.domino.napi.NapiDocument {
	NotesNote delegate;

	/**
	 * @param napiNote
	 */
	public NapiDocument(final NotesNote napiNote) {
		delegate = napiNote;
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#bulkDecryptField(java.lang.String)
	 */
	@Override
	public byte[] bulkDecryptField(final String arg0) {
		try {
			return delegate.bulkDecryptField(arg0);
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#bulkEncryptField(java.lang.String, java.lang.String, java.lang.String, byte[],
	 *      java.lang.String)
	 */
	@Override
	public boolean bulkEncryptField(final String arg0, final String arg1, final String arg2, final byte[] arg3, final String arg4) {
		try {
			delegate.bulkEncryptField(arg0, arg1, arg2, arg3, arg4);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#deleteItem(java.lang.String)
	 */
	@Override
	public boolean deleteItem(final String arg0) {
		try {
			delegate.deleteItem(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#deleteItemIgnoreMissing(java.lang.String)
	 */
	@Override
	public boolean deleteItemIgnoreMissing(final String arg0) {
		try {
			delegate.deleteItemIgnoreMissing(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	// /**
	// * @throws NotesAPIException
	// * @see com.ibm.designer.domino.napi.NotesObject#dump()
	// */
	// @Override
	// public final void dump() {
	// delegate.dump();
	// }

	/**
	 * @return
	 * @see com.ibm.designer.domino.napi.NotesHandle#getClassName()
	 */
	@Override
	public final String getClassName() {
		return delegate.getClassName();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getFieldType(java.lang.String)
	 */
	@Override
	public int getFieldType(final String arg0) {
		try {
			return delegate.getFieldType(arg0);
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getFormFields()
	 */
	@Override
	public String[] getFormFields() {
		try {
			return delegate.getFormFields();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	// TODO RPr: we need a wrapper for NotesNoteItem
	// /**
	// * @param arg0
	// * @return
	// * @throws NotesAPIException
	// * @see com.ibm.designer.domino.napi.NotesNote#getItem(java.lang.String)
	// */
	// @Override
	// public NotesNoteItem getItem(final String arg0) {
	// return delegate.getItem(arg0);
	// }

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getItemAsTextList(java.lang.String)
	 */
	@Override
	public List<String> getItemAsTextList(final String arg0) {
		try {
			return delegate.getItemAsTextList(arg0);
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getItemNames()
	 */
	@Override
	public String[] getItemNames() {
		try {
			return delegate.getItemNames();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getItemValueAsString(java.lang.String)
	 */
	@Override
	public String getItemValueAsString(final String arg0) {
		try {
			return delegate.getItemValueAsString(arg0);
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getModifiedDateTime()
	 */
	@Override
	public String getModifiedDateTime() {
		try {
			return delegate.getModifiedDateTime();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getModifiedDateTimeSeconds()
	 */
	@Override
	public long getModifiedDateTimeSeconds() {
		try {
			return delegate.getModifiedDateTimeSeconds();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteClass()
	 */
	@Override
	public int getNoteClass() {
		try {
			return delegate.getNoteClass();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteFlags()
	 */
	@Override
	public int getNoteFlags() {
		try {
			return delegate.getNoteFlags();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteId()
	 */
	@Override
	public int getNoteId() {
		try {
			return delegate.getNoteId();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteUnid()
	 */
	@Override
	public String getNoteUnid() {
		try {
			return delegate.getNoteUnid();
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	// TODO: wrapper needed!
	// /**
	// * @return
	// * @see com.ibm.designer.domino.napi.NotesObject#getParent()
	// */
	// @Override
	// public NotesObject getParent() {
	// return delegate.getParent();
	// }

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	/**
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#initAsFile()
	 */
	@Override
	public boolean initAsFile() {
		try {
			delegate.initAsFile();
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#initAsFile(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean initAsFile(final String arg0, final String arg1) {
		try {
			delegate.initAsFile(arg0, arg1);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#initAsFile(java.lang.String)
	 */
	@Override
	public boolean initAsFile(final String arg0) {
		try {
			delegate.initAsFile(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#initAsForm()
	 */
	@Override
	public boolean initAsForm() {
		try {
			delegate.initAsForm();
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#initClassAndFlags(java.lang.String, int)
	 */
	@Override
	public boolean initClassAndFlags(final String arg0, final int arg1) {
		try {
			delegate.initClassAndFlags(arg0, arg1);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#isItemPresent(java.lang.String)
	 */
	@Override
	public boolean isItemPresent(final String arg0) {
		try {
			return delegate.isItemPresent(arg0);
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	// TODO: Do we need this
	// /**
	// * @return
	// * @see com.ibm.designer.domino.napi.NotesObject#isRecycled()
	// */
	// @Override
	// public final boolean isRecycled() {
	// return delegate.isRecycled();
	// }
	//
	// /**
	// * @return
	// * @throws NotesAPIException
	// * @see com.ibm.designer.domino.napi.NotesHandle#isValidHandle()
	// */
	// @Override
	// public final boolean isValidHandle() {
	// return delegate.isValidHandle();
	// }
	//
	// /**
	// * @throws NotesAPIException
	// * @see com.ibm.designer.domino.napi.NotesObject#recycle()
	// */
	// @Override
	// public final void recycle() {
	// delegate.recycle();
	// }

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setClass(int)
	 */
	@Override
	public boolean setClass(final int arg0) {
		try {
			delegate.setClass(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setExtendedFlags(java.lang.String)
	 */
	@Override
	public boolean setExtendedFlags(final String arg0) {
		try {
			delegate.setExtendedFlags(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setItemAsTextList(java.lang.String, java.util.List)
	 */
	@Override
	public boolean setItemAsTextList(final String arg0, final List<String> arg1) {
		try {
			delegate.setItemAsTextList(arg0, arg1);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setItemText(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setItemText(final String arg0, final String arg1) {
		try {
			delegate.setItemText(arg0, arg1);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setItemTextAllowEmpty(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setItemTextAllowEmpty(final String arg0, final String arg1) {
		try {
			delegate.setItemTextAllowEmpty(arg0, arg1);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setUnid(java.lang.String)
	 */
	@Override
	public boolean setUnid(final String arg0) {
		try {
			delegate.setUnid(arg0);
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return delegate.toString();
	}

	/**
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#update()
	 */
	@Override
	public boolean update() {
		try {
			delegate.update();
			return true;
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

}
