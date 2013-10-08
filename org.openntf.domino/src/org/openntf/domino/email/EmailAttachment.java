/**
 * 
 */
package org.openntf.domino.email;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * @author withersp
 * 
 */
public class EmailAttachment {
	private Type attachmentType;
	private String path;
	private String fileName;
	private String unid;
	private String dbPath;
	private String contentId;
	private boolean isInlineImage;

	public static enum Type {
		DOCUMENT(0), FILE(1);

		private final int value_;

		private Type(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	/**
	 * 
	 */
	public EmailAttachment() {

	}

	/**
	 * @param doc
	 *            from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 */
	public EmailAttachment(final Document doc, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.DOCUMENT);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setUnid(doc.getUniversalID());
			setDbPath(doc.getParentDatabase().getFilePath());
			setContentId(Factory.getSession().evaluate("@Unique").toString());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * @param doc
	 *            from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 */
	public EmailAttachment(final Document doc, final String fileName, final boolean isInlineImage, final String contentId) {
		try {
			setAttachmentType(Type.DOCUMENT);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setUnid(doc.getUniversalID());
			setDbPath(doc.getParentDatabase().getFilePath());
			setContentId(contentId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * @param filePath
	 *            of the on-disk attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 */
	public EmailAttachment(final String filePath, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.FILE);
			setPath(filePath);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setContentId(Factory.getSession().evaluate("@Unique").toString());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * @param filePath
	 *            of the on-disk attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 */
	public EmailAttachment(final String filePath, final String fileName, final boolean isInlineImage, final String contentId) {
		try {
			setAttachmentType(Type.FILE);
			setPath(filePath);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setContentId(contentId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	public Type getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(final Type attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(final String unid) {
		this.unid = unid;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(final String dbPath) {
		this.dbPath = dbPath;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(final String contentId) {
		this.contentId = contentId;
	}

	public boolean isInlineImage() {
		return isInlineImage;
	}

	public void setInlineImage(final boolean isInlineImage) {
		this.isInlineImage = isInlineImage;
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

}
