/**
 * 
 */
package org.openntf.domino.email;

import java.io.InputStream;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * @author withersp
 * 
 *         EmailAttachment class
 */
public class EmailAttachment implements IEmailAttachment {
	private Type attachmentType;
	private String path;
	private String fileName;
	private String unid;
	private String dbPath;
	private String contentId;
	private InputStream inputStream;
	private byte[] bytes;
	private boolean isInlineImage;

	/**
	 * Constructor
	 */
	public EmailAttachment() {

	}

	private String atUnique() {
		return Factory.getSession(SessionType.CURRENT).evaluate("@Unique").toString();
	}

	/**
	 * Creates an EmailAttachment object from an attachment in a Document
	 * 
	 * @param doc
	 *            from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @since org.openntf.domino 4.5.0
	 */
	public EmailAttachment(final Document doc, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.DOCUMENT);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setUnid(doc.getUniversalID());
			setDbPath(doc.getParentDatabase().getFilePath());
			setContentId(atUnique());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * Creates an EmailAttachment object from an attachment in a Document
	 * 
	 * @param doc
	 *            from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 * @since org.openntf.domino 4.5.0
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
	 * Creates an EmailAttachment object from a file
	 * 
	 * @param filePath
	 *            of the on-disk attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @since org.openntf.domino 4.5.0
	 */
	public EmailAttachment(final String filePath, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.FILE);
			setPath(filePath);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setContentId(atUnique());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * Creates an attachment from a file
	 * 
	 * @param filePath
	 *            of the on-disk attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 * @since org.openntf.domino 4.5.0
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

	/**
	 * Creates an EmailAttachment object from an InputStream
	 * 
	 * @param inputStream
	 *            of the attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @since org.openntf.domino 4.5.0
	 */
	public EmailAttachment(final InputStream inputStream, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.STREAM);
			setInputStream(inputStream);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setContentId(atUnique());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/**
	 * Creates an EmailAttachment object from a byte array
	 * 
	 * @param bytes
	 *            byte array of the attachment
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether it should be inserted as an inline image
	 * @since org.openntf.domino 4.5.0
	 */
	public EmailAttachment(final byte[] bytes, final String fileName, final boolean isInlineImage) {
		try {
			setAttachmentType(Type.BYTES);
			setBytes(bytes);
			setFileName(fileName);
			setInlineImage(isInlineImage);
			setContentId(atUnique());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public Type getAttachmentType() {
		return attachmentType;
	}

	@Override
	public void setAttachmentType(final Type attachmentType) {
		this.attachmentType = attachmentType;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getUnid() {
		return unid;
	}

	@Override
	public void setUnid(final String unid) {
		this.unid = unid;
	}

	@Override
	public String getDbPath() {
		return dbPath;
	}

	@Override
	public void setDbPath(final String dbPath) {
		this.dbPath = dbPath;
	}

	@Override
	public String getContentId() {
		return contentId;
	}

	@Override
	public void setContentId(final String contentId) {
		this.contentId = contentId;
	}

	@Override
	public boolean isInlineImage() {
		return isInlineImage;
	}

	@Override
	public void setInlineImage(final boolean isInlineImage) {
		this.isInlineImage = isInlineImage;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(final String path) {
		this.path = path;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public void setInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public void setBytes(final byte[] bytes) {
		this.bytes = bytes;
	}

}
