/**
 * 
 */
package org.openntf.domino.email;

import java.util.Collection;
import java.util.List;

import org.openntf.domino.Document;
import org.openntf.domino.MIMEEntity;

/**
 * @author withersp
 * 
 */
public interface IEmail {

	/**
	 * Adds to the body of the email
	 * 
	 * @param content
	 *            can be any CharSequence, such as a String or StringBuilder
	 */
	public void addHTML(CharSequence content);

	/**
	 * @return String List of HTML components
	 */
	public List<String> getHTML();

	/**
	 * Adds plain text to the body of the email
	 * 
	 * @param content
	 *            can be any CharSequence, such as a String or StringBuilder
	 */
	public void addText(CharSequence content);

	/**
	 * @return String List of plain text components
	 */
	public List<String> getText();

	/**
	 * @param jsonContent
	 *            String url for JSON content
	 */
	public void setJSON(String jsonContent);

	/**
	 * @return String url for JSON email data
	 */
	public String getJSON();

	/**
	 * Adds a MIMEEntity to the body of the email
	 * 
	 * @param contentMime
	 *            MIMEEntity containing the content
	 */
	public void addMimeEntity(MIMEEntity contentMime);

	/**
	 * @return List of MIMEEntities from the email
	 */
	public List<MIMEEntity> getMimeEntities();

	/**
	 * Removes a MIMEEntity from the email
	 * 
	 * @param content
	 *            MIMEEntity to remove from the email
	 */
	public void removeMimeEntity(MIMEEntity content);

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
	public String addDocAttachment(String unid, String fileName, boolean isInlineImage);

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
	public String addDocAttachment(String unid, String fileName, boolean isInlineImage, String contentId);

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
	public String addFileAttachment(String path, String fileName, boolean isInlineImage);

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
	public String addFileAttachment(String path, String fileName, boolean isInlineImage, String contentId);

	/**
	 * Adds an attachment to an ArrayList of EmailAttachments
	 * 
	 * @param attachment
	 *            EmailAttachment object
	 */
	public void addAttachment(EmailAttachment attachment);

	/**
	 * Removes an attachment from an ArrayList of attachments
	 * 
	 * @param attachment
	 *            EmailAttachment object
	 */
	public void removeAttachment(EmailAttachment attachment);

	/**
	 * @return gets the List of EmailAttachment objects
	 */
	public List<EmailAttachment> getAttachments();

	/**
	 * Adds all attachments to the parent MIMEEntity, using a passed ArrayList of EmailAttachment objects
	 * 
	 * @param attachments
	 *            ArrayList of EmailAttachment elements
	 * @param parent
	 *            MIMEEntity that the attachments will be added to
	 */
	public void addAttachments(MIMEEntity parent);

	/**
	 * @return value for the To field of the email
	 */
	public List<String> getTo();

	/**
	 * @return value for the CopyTo field of the email
	 */
	public List<String> getCC();

	/**
	 * @return value for the BlindCopyTo field of the email
	 */
	public List<String> getBCC();

	/**
	 * @return value for the subject field of the email
	 */
	public String getSubject();

	/**
	 * @return email address value for the From field (and all its variants) of the email
	 */
	public String getSenderEmail();

	/**
	 * @return name value for the From field (and all its variants) of the email
	 */
	public String getSenderName();

	/**
	 * Removes an individual recipient from the cc ArrayList
	 * 
	 * @param cc
	 */
	public void removeCCAddress(String cc);

	/**
	 * Removes an individual recipient from the bcc ArrayList
	 * 
	 * @param bcc
	 */
	public void removeBCCAddress(String bcc);

	/**
	 * Removes an individual recipient from the to ArrayList
	 * 
	 * @param to
	 */
	public void removeToAddress(String to);

	/**
	 * Sets multiple recipients
	 * 
	 * @param to
	 *            Collection of recipients
	 */
	public void setTo(Collection<String> to);

	/**
	 * Adds an email address to the ArrayList containing the recipients
	 * 
	 * @param to
	 *            String recipient
	 */
	public void addToAddress(String to);

	/**
	 * Sets multiple CC recipients
	 * 
	 * @param to
	 *            Collection of recipients
	 */
	public void setCC(Collection<String> cc);

	/**
	 * Adds an email address to the ArrayList containing the CC recipients
	 * 
	 * @param to
	 *            String recipient
	 */
	public void addCCAddress(String cc);

	/**
	 * Sets multiple BCC recipients
	 * 
	 * @param to
	 *            Collection of recipients
	 */
	public void setBCC(Collection<String> bcc);

	/**
	 * Adds an email address to the ArrayList containing the BCC recipients
	 * 
	 * @param to
	 *            String recipient
	 */
	public void addBCCAddress(String bcc);

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
	 *            email address to send from
	 */
	public void setSenderEmail(String sender);

	/**
	 * Sets From field (and all its variants)
	 * 
	 * @param sender
	 *            name to send from
	 */
	public void setSenderName(String sender);

	/**
	 * Sends the email
	 * 
	 * @return successfully created Document
	 */
	public Document send();

}
