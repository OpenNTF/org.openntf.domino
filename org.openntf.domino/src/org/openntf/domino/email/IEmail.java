/**
 * 
 */
package org.openntf.domino.email;

import java.util.ArrayList;

import org.openntf.domino.MIMEEntity;

/**
 * @author withersp
 * 
 */
public interface IEmail {

	/**
	 * Adds a StringBuilder to the body of the email
	 * 
	 * @param content
	 *            stored in StringBuilder, better for performance
	 */
	public void addHTML(final StringBuilder content);

	/**
	 * Adds plain text to the body of the email
	 * 
	 * @param content
	 *            stored in StringBuilder, better for performance
	 */
	public void addText(final StringBuilder content);

	/**
	 * Adds a MIMEEntity to the body of the email
	 * 
	 * @param contentMime
	 *            MIMEEntity containing the content
	 */
	public void addMimeEntity(final MIMEEntity contentMime);

	/**
	 * Adds an on-disk file to the internal ArrayList of EmailAttachment objects, generating and returning a unique identifier for the
	 * attachment
	 * 
	 * @param unid
	 *            of document from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether attachment is to be inserted as an inline image
	 * @return String "cid:" + contentId;
	 */
	public String addDocAttachment(final String unid, final String fileName, final Boolean isInlineImage);

	/**
	 * Adds an attachment from a Document to the internal ArrayList of EmailAttachment objects, using a passed string as the unique
	 * identifier for the attachment
	 * 
	 * @param unid
	 *            of document from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether attachment is to be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 * @return String "cid:" + contentId;
	 */
	public String addDocAttachment(final String unid, final String fileName, final Boolean isInlineImage, String contentId);

	/**
	 * Adds an on-disk file to the internal ArrayList of EmailAttachment objects, generating and returning a unique identifier for the
	 * attachment
	 * 
	 * @param path
	 *            of the on-disk file
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether attachment is to be inserted as an inline image
	 * @return String "cid:" + contentId;
	 */
	public String addFileAttachment(final String path, final String fileName, final Boolean isInlineImage);

	/**
	 * Adds an on-disk file to the internal ArrayList of EmailAttachment objects, using a passed string as the unique identifier for the
	 * attachment
	 * 
	 * @param path
	 *            of the on-disk file
	 * @param fileName
	 *            of the file
	 * @param isInlineImage
	 *            whether attachment is to be inserted as an inline image
	 * @param contentId
	 *            a unique reference for each attachment to be inserted into the email
	 * @return String "cid:" + contentId;
	 */
	public String addFileAttachment(final String path, final String fileName, final Boolean isInlineImage, String contentId);

	/**
	 * Adds all attachments to the parent MIMEEntity, using the internal ArrayList of EmailAttachment objects
	 * 
	 * @param parent
	 *            MIMEEntity
	 */
	public void addAttachments(MIMEEntity parent);

	/**
	 * Adds all attachments to the parent MIMEEntity, using a passed ArrayList of EmailAttachment objects
	 * 
	 * @param attachments
	 *            ArrayList of EmailAttachment elements
	 * @param parent
	 *            MIMEEntity that the attachments will be added to
	 */
	public void addAttachments(final ArrayList<EmailAttachment> attachments, MIMEEntity parent);

	/**
	 * @return value for the To field of the email
	 */
	public ArrayList<String> getTo();

	/**
	 * @return value for the CopyTo field of the email
	 */
	public ArrayList<String> getCC();

	/**
	 * @return value for the BlindCopyTo field of the email
	 */
	public ArrayList<String> getBCC();

	/**
	 * @return value for the subject field of the email
	 */
	public String getSubject();

	/**
	 * @return value for the From field (and all its variants) of the email
	 */
	public String getSender();

	/**
	 * Sets multiple recipients
	 * 
	 * @param to
	 *            ArrayList of recipients
	 */
	public void setTo(ArrayList<String> to);

	/**
	 * Creates an ArrayList containing the recipient
	 * 
	 * @param to
	 *            String recipient
	 */
	public void setTo(String to);

	/**
	 * Sets multiple CC recipients
	 * 
	 * @param to
	 *            ArrayList of recipients
	 */
	public void setCC(ArrayList<String> cc);

	/**
	 * Creates an ArrayList containing the CC recipient
	 * 
	 * @param to
	 *            String recipient
	 */
	public void setCC(String cc);

	/**
	 * Sets multiple BCC recipients
	 * 
	 * @param to
	 *            ArrayList of recipients
	 */
	public void setBCC(ArrayList<String> bcc);

	/**
	 * Creates an ArrayList containing the BCC recipient
	 * 
	 * @param to
	 *            String recipient
	 */
	public void setBCC(String bcc);

	/**
	 * Sets the subject for the email
	 * 
	 * @param subject
	 *            String value for the email subject
	 */
	public void setSubject(String subject);

	/**
	 * Sets From field (and all its variants)
	 * 
	 * @param sender
	 *            email address / user to send from
	 */
	public void setSender(String sender);

	/**
	 * Sends the email
	 * 
	 * @return success or failure
	 */
	public boolean send();

}
