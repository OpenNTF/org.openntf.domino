package org.openntf.domino.rest.utils;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.domino.napi.c.html.HtmlConverter;
import com.ibm.domino.napi.c.html.HtmlReferenceData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.RichTextItem;

public class RTtoHTMLUtil {
	// private IMimeMultipart newValue;
	public static class AttachmentHolder {
		protected int _state;
		protected String _displayName;
		protected String _internalName;
		protected String _mimeType;
		protected String _href;
		protected long _fileSize;
		protected long _lastModifed;
		protected long _created;
		// protected String _persistentName;
		protected String _cid;
		protected String _dbKey;

		public AttachmentHolder(Document document, RichTextItem richTextItem, EmbeddedObject embedObject,
				String displayName, String dbKey) {
			this._state = 0;
			this._internalName = embedObject.getName();
			this._displayName = displayName;
			this._fileSize = embedObject.getFileSize();
			this._lastModifed = embedObject.getFileModified().toJavaDate().getTime();
			this._created = embedObject.getFileCreated().toJavaDate().getTime();
			this._href = getDatabaseAttachmentUrl(document, richTextItem, this._internalName);
			this._dbKey = dbKey;
		}

		public String getCID() {
			return this._cid;
		}

		public void setCID(String paramString) {
			this._cid = paramString;
		}

		private String getDatabaseAttachmentUrl(Document document, RichTextItem rtItem, String internalName) {
			StringBuilder localStringBuilder = new StringBuilder(256);
			localStringBuilder.append("/.ibmmodres/");
			localStringBuilder.append("domino/OpenAttachment");
			localStringBuilder.append("/");

			String str = document.getAncestorDatabase().getFilePath();
			str = StringUtil.replace(str, File.separatorChar, '/');
			localStringBuilder.append(str);
			localStringBuilder.append("/");

			localStringBuilder.append(document.getUniversalID());
			localStringBuilder.append("/");

			localStringBuilder.append(rtItem.getName());
			localStringBuilder.append("/");

			localStringBuilder.append(internalName);
			return localStringBuilder.toString();
		}

		public int getState() {
			return this._state;
		}

		public void setState(int paramInt) {
			this._state = paramInt;
		}

		public String getDbKey() {
			return this._dbKey;
		}

		public String getName() {
			return this._displayName;
		}

		public String getHref() {
			return this._href;
		}

		public long getLastModified() {
			return this._lastModifed;
		}

		public long getCreated() {
			return this._created;
		}

		public long getLength() {
			return this._fileSize;
		}

		public String getType() {
			if (StringUtil.isNotEmpty(this._mimeType)) {
				return this._mimeType;

			}

			// String str1 = MIME.getFileExtension(getName());
			// if (str1 != null) {
			// String str2 = MIME.getMIMETypeFromExtension(str1);
			// if (StringUtil.isNotEmpty(str2)) {
			// return str2;
			// }
			// }
			return "application/octet-stream";
		}
	}

	public static String getHTML(RichTextItem rtItem) {
		return getText(rtItem.getAncestorDocument(), rtItem.getName());
	}

	public static String getCustomHtml(RichTextItem rtItem, Map<String, Integer> converterArgs) {
		return getCustomText(rtItem.getAncestorDocument(), rtItem.getName(), converterArgs);
	}

	private static String getCustomText(Document document, String richTextName, Map<String, Integer> converterArgs) {
		HtmlConverter htmlConverter = new HtmlConverter();

		try {
			htmlConverter.open();
			htmlConverter.setDBHandle(BackendBridge.getDatabaseHandleRO(document.getParentDatabase()));
			htmlConverter.setNoteHandle(BackendBridge.getDocumentHandleRW(document));
			htmlConverter.setFieldName(richTextName);

			ArrayList<String> argArrayList = new ArrayList<String>();
			for (String key : converterArgs.keySet()) {
				argArrayList.add(key + "=" + converterArgs.get(key));
			}

			String[] args = new String[argArrayList.toArray().length];
			System.arraycopy(argArrayList.toArray(), 0, args, 0, argArrayList.toArray().length);
			htmlConverter.ConvertItem(args);
			int i = htmlConverter.getNumRefs();

			if (i > 0) {
				HtmlReferenceData[] refData = htmlConverter.getReferences();

				for (int j = 0; j < i; ++j) {
					int k = refData[j].getRefType();
					int l = refData[j].getCmdID();

					if ((l != 5) || (k != 2)) {
						generateCID(refData[j], richTextName);
					}
				}
			}

			String result = htmlConverter.getConvertText();
			if (result == null) {
				htmlConverter.close();
			}
			if (StringUtil.isNotEmpty(result)) {
				// result = insertNotesClientLinks852(result);
			}

			return result;

		} catch (NException ne) {
			ne.printStackTrace();
		} catch (IOException ieo) {
			ieo.printStackTrace();
		} finally {
			try {
				htmlConverter.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		return ("");
	}

	private static String getText(Document document, String richTextItemName) {
		Map<String, Integer> argMap = new LinkedHashMap<String, Integer>();
		argMap.put("AutoClass", 2);
		argMap.put("RowAtATimeTableAlt", 2);
		argMap.put("SectionAlt", 1);
		argMap.put("XMLCompatibleHTML", 1);
		argMap.put("AttachmentLink", 1);
		argMap.put("TableStyle", 1);
		argMap.put("FontConversion", 1);
		argMap.put("LinkHandling", 1);
		argMap.put("ListFidelity", 1);
		argMap.put("ParagraphIndent", 2);
		argMap.put("LinkHTMLAlt", 1);

		return getCustomText(document, richTextItemName, argMap);
	}

	private static void generateCID(HtmlReferenceData htmlRef, String itemName) throws IOException {
		String str1 = htmlRef.getReferenceUrl();
		StringBuilder localStringBuilder1 = new StringBuilder();
		localStringBuilder1.append("_1_");
		localStringBuilder1.append(System.currentTimeMillis());

		StringBuilder localStringBuilder2 = new StringBuilder();

		localStringBuilder2.append(itemName);
		int i = str1.indexOf(63, 0);
		if (i < 0)

		{
			i = str1.indexOf(33, 0);
		}
		if (i < 0) {
			return;
		}
		int j = str1.lastIndexOf(47, i);
		if (j < 0) {
			return;
		}
		localStringBuilder2.append(str1.substring(j, i));
		localStringBuilder1.append(str1.substring(j + 1, i).replace('.', '-'));
		localStringBuilder2.append('/');

		String str2 = localStringBuilder1.toString();

		String str3 = "FieldElemFormat=";
		int k = str1.indexOf(str3, i);

		if (-1 == k) {
			localStringBuilder1.append(".gif");
			localStringBuilder2.append("gif");
		} else {
			k += str3.length();
			localStringBuilder1.append('.');
			localStringBuilder1.append(str1.substring(k));
			localStringBuilder2.append(str1.substring(k));

		}

		// String str4 =
		// computeUniqueAttachmentName(localStringBuilder1.toString());
		//
		//
		// FacesContext localFacesContext = FacesContext.getCurrentInstance();
		// PersistedContent localPersistedContent =
		// getPersistedContent(localFacesContext, getName(), str4);
		// DominoDocument.AttachmentValueHolder localAttachmentValueHolder = new
		// DominoDocument.AttachmentValueHolder(FacesContext.getCurrentInstance(),
		// localPersistedContent, getParent(), this, str4, -1L);
		// localAttachmentValueHolder.setPersistentName(localPersistedContent);
		//
		// setEmbeddedImages(str2, localAttachmentValueHolder);
		//
		//
		// paramHtmlReferenceData.setFileName(localPersistedContent.getPath());
		// String str5 = encodeHRef(localFacesContext,
		// localAttachmentValueHolder.getHref());
		// paramHtmlReferenceData.setNewReferenceUrl(str5);
	}

	private String computeUniqueAttachmentName(Collection<AttachmentHolder> holders, String paramString) {
		if (holders == null) {
			return paramString;
		}
		String str1 = null;
		int i = paramString.indexOf(46);
		if (i >= 0) {
			str1 = paramString.substring(i);
			paramString = paramString.substring(0, i);
		}
		String str2 = (str1 != null) ? paramString + str1 : paramString;
		int j = 2;
		for (AttachmentHolder holder : holders) {
			if ((holder.getState() != 2) && (holder.getState() != 3) && (StringUtil.equals(holder.getName(), str2))) {
				str2 = paramString + "-" + ++j + ((str1 != null) ? str1 : "");
				break;
			}
		}
		return str2;
	}
}
