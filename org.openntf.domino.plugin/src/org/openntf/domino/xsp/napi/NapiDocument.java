/**
 * 
 */
package org.openntf.domino.xsp.napi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.openntf.domino.utils.DominoUtils;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesNote;
import com.ibm.designer.domino.napi.NotesNoteItem;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NsfNote;

/**
 * This is a bunch of hacks to access the NAPI-Functions
 * 
 * @author praml
 * 
 */
public class NapiDocument implements org.openntf.domino.napi.NapiDocument {
	int handle;

	private static Method NGetItemMethod;
	private static Method NDeleteItemMethod;
	private static Method NGetFieldTypeMethod;
	private static Method NGetFieldsMethod;
	private static Method NGetItemNamesMethod;
	private static Method NGetNoteIdMethod;
	private static Method NIsItemPresentMethod;
	private static Method NSetItemTextListMethod;
	private static Constructor notesNoteItemConstructor;

	/**
	 * @param handle
	 */
	public NapiDocument(final int handle) {
		this.handle = handle;
	}

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					notesNoteItemConstructor = NotesNoteItem.class.getDeclaredConstructor(NotesNote.class, long.class);
					notesNoteItemConstructor.setAccessible(true);
					NGetItemMethod = NotesNote.class.getDeclaredMethod("NGetItem", int.class, String.class);
					NGetItemMethod.setAccessible(true);
					NDeleteItemMethod = NotesNote.class.getDeclaredMethod("NDeleteItem", int.class, String.class);
					NDeleteItemMethod.setAccessible(true);
					NGetFieldTypeMethod = NotesNote.class.getDeclaredMethod("NGetFieldType", int.class, String.class);
					NGetFieldTypeMethod.setAccessible(true);
					NGetFieldsMethod = NotesNote.class.getDeclaredMethod("NGetFields", int.class);
					NGetFieldsMethod.setAccessible(true);
					NGetItemNamesMethod = NotesNote.class.getDeclaredMethod("NGetItemNames", int.class);
					NGetItemNamesMethod.setAccessible(true);
					NGetNoteIdMethod = NotesNote.class.getDeclaredMethod("NGetNoteId", int.class);
					NGetNoteIdMethod.setAccessible(true);
					NIsItemPresentMethod = NotesNote.class.getDeclaredMethod("NIsItemPresent", int.class, String.class);
					NIsItemPresentMethod.setAccessible(true);
					NSetItemTextListMethod = NotesNote.class.getDeclaredMethod("NSetItemTextList", int.class, String.class, String.class);
					NSetItemTextListMethod.setAccessible(true);

					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
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
			NDeleteItemMethod.invoke(null, handle, arg0);
			return true;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return false;
		}
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
			return ((Integer) NGetFieldTypeMethod.invoke(null, handle, arg0)).intValue();
		} catch (Exception e) {
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
			return ((String[]) NGetFieldsMethod.invoke(null, handle));
		} catch (Exception e) {
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
	public NotesNoteItem getItem(final String arg0) {
		try {
			long l = (Long) NGetItemMethod.invoke(null, handle, arg0);
			if (l != 0L) {
				return (NotesNoteItem) notesNoteItemConstructor.newInstance(null, l);
			}
		} catch (Exception e) {
			//DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getItemAsTextList(java.lang.String)
	 */
	@Override
	public Vector<String> getItemAsTextVector(final String arg0) {
		// TODO Check type
		NotesNoteItem localNotesNoteItem = null;
		if (isItemPresent(arg0)) {
			localNotesNoteItem = getItem(arg0);
		}
		// TODO RPR Return itemType!
		Vector<String> ret = new Vector<String>();
		if (localNotesNoteItem == null) {
			ret.add("");
			return ret;
		}
		try {
			try {
				if (localNotesNoteItem != null) {
					String str = localNotesNoteItem.getValueAsString('|'); // ARGH TODO RPr
					if (str != null) {
						String[] arrayOfString = str.split("\\|");
						ret.addAll(Arrays.asList(arrayOfString));
						return ret;
					}
				}
			} finally {
				localNotesNoteItem.recycle();
			}
		} catch (NotesAPIException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getItemNames()
	 */
	@Override
	public String[] getItemNames() {
		try {
			return ((String[]) NGetItemNamesMethod.invoke(null, handle));
		} catch (Exception e) {
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
			return NsfNote.NSFItemGetText((long) handle, arg0);
		} catch (NException e) {
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
		return 0; // TODO RPR NAPI
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteClass()
	 */
	@Override
	public int getNoteClass() {
		return 0; // TODO RPR NAPI
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteFlags()
	 */
	@Override
	public int getNoteFlags() {
		return 0; // TODO RPR NAPI
	}

	/**
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#getNoteId()
	 */
	@Override
	public String getNoteId() {
		try {
			Integer nid = (Integer) NGetNoteIdMethod.invoke(null, handle);
			return Integer.toHexString(nid);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return "0";

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
			return NsfNote.NSFNoteGetInfoUNID((long) handle);
		} catch (NException e) {
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
	 * @param arg0
	 * @return
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#isItemPresent(java.lang.String)
	 */
	@Override
	public boolean isItemPresent(final String arg0) {
		try {
			return ((Boolean) NIsItemPresentMethod.invoke(null, handle, arg0)).booleanValue();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/**
	 * @param arg0
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setClass(int)
	 */
	@Override
	public boolean setClass(final int arg0) {
		return false;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#setItemAsTextList(java.lang.String, java.util.List)
	 */
	@Override
	public boolean setItemAsTextList(final String arg0, final List<String> arg1) {
		StringBuilder sb = new StringBuilder();
		for (String s : arg1) {
			if (s == null || s.indexOf(",") != -1) {
				return false;
			}
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(s);
		}

		try {
			NSetItemTextListMethod.invoke(null, handle, arg1, sb.toString());
			return true;
		} catch (Exception e) {
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
			NsfNote.NSFItemSetText(handle, arg0, arg1);
			return true;
		} catch (NException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return false;
	}

	/**
	 * @throws NotesAPIException
	 * @see com.ibm.designer.domino.napi.NotesNote#update()
	 */
	@Override
	public boolean update() {
		return false;
	}

}
