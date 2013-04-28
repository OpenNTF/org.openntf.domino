package org.openntf.domino.design;

public interface FileResource extends DesignBase, org.openntf.domino.types.DatabaseDescendant {

	/**
	 * @return the file resource's data as a byte array
	 */
	public byte[] getFileData();

	/**
	 * @param fileData
	 *            The new data for the file resource, as a byte array
	 */
	public void setFileData(byte[] fileData);

	/**
	 * @return mime type
	 */
	public String getMimeType();

	/**
	 * @param mimeType
	 *            The new MIME type for the file resource
	 */
	public void setMimeType(String mimeType);

	/**
	 * @return whether the file resource is marked as read-only
	 */
	public boolean isReadOnly();
}
