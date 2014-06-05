package org.openntf.domino.email;

import java.io.InputStream;

public interface IEmailAttachment {

	/**
	 * Enum to define attachment type
	 * 
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum Type {
		DOCUMENT(0), FILE(1), STREAM(2), BYTES(3);

		private final int value_;

		private Type(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	/**
	 * Gets the attachment type, using {@link org.openntf.domino.email.IEmailAttachment.Type}
	 * 
	 * @return Type of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract Type getAttachmentType();

	/**
	 * Sets the attachment type for this attachment object - DOCUMENT, FILE, STREAM or BYTES
	 * 
	 * @param attachmentType
	 *            Type for the object
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setAttachmentType(Type attachmentType);

	/**
	 * Gets the file name (not including the path) to load the attachment from, loaded via a FileInputStream
	 * 
	 * @return String file name
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract String getFileName();

	/**
	 * Sets the file name (not including the path) to load the attachment from
	 * 
	 * @param fileName
	 *            String file name
	 */
	public abstract void setFileName(String fileName);

	/**
	 * Gets the UNID for the document, if the attachment is to be retrieved from a Document
	 * 
	 * @return String UNID
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract String getUnid();

	/**
	 * Sets the UNID for the document, if the attachment is to be retrieved from a Document
	 * 
	 * @param unid
	 *            String UNID
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setUnid(String unid);

	/**
	 * Gets the database path from which to retrieve a document, if the attachment resides in a Document
	 * 
	 * @return String database path
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract String getDbPath();

	/**
	 * Sets the database path from which to retrieve a document, if the attachment resides in a Document
	 * 
	 * @param dbPath
	 *            String database path
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setDbPath(String dbPath);

	/**
	 * Gets the ContentId for the attachment, a unique reference
	 * 
	 * @return String content id
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract String getContentId();

	/**
	 * Sets the ContentId for the attachment, a unique reference
	 * 
	 * @param contentId
	 *            String content id
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setContentId(String contentId);

	/**
	 * Whether this attachment is an inline image
	 * 
	 * @return boolean whether an inline image or not
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract boolean isInlineImage();

	/**
	 * Sets whether the attachment is an inline image
	 * 
	 * @param isInlineImage
	 *            boolean whether an inline image or not
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setInlineImage(boolean isInlineImage);

	/**
	 * Gets the folder in which to find a file attachment, loaded via a FileInputStream
	 * 
	 * @return String filepath, not including file name
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract String getPath();

	/**
	 * Sets the folder in which to find a file attachment, loaded via a FileInputStream
	 * 
	 * @param path
	 *            String filepath, not including file name
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setPath(String path);

	/**
	 * Gets the InputStream, if this attachment object is for an InputStream
	 * 
	 * @return InputStream corresponding to the attachment to load
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract InputStream getInputStream();

	/**
	 * Loads an InputStream for the attachment to be created from
	 * 
	 * @param inputStream
	 *            InputStream corresponding to the attachment to load
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setInputStream(InputStream inputStream);

	/**
	 * Gets the byte array, if this attachment object is for a byte array
	 * 
	 * @return byte[] corresponding to the attachment to load
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract byte[] getBytes();

	/**
	 * Loads a byte array for the attachment to be created from
	 * 
	 * @param bytes
	 *            byte[] corresponding to the attachment to load
	 * @since org.openntf.domino 4.5.0
	 */
	public abstract void setBytes(byte[] bytes);

}