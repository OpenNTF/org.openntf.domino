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
 *         Email interface
 */
public interface IEmail {

	/**
	 * Adds HTML content to the body of the email
	 * 
	 * @param content
	 *            can be any CharSequence, such as a String or StringBuilder
	 * @since org.openntf.domino 4.5.0
	 */
	public void addHTML(CharSequence content);

	/**
	 * Gets HTML content added via addHTML()
	 * 
	 * @return List<String> of HTML components
	 * @since org.openntf.domino 4.5.0
	 */
	public List<String> getHTML();

	/**
	 * Adds plain text to the body of the email
	 * 
	 * @param content
	 *            can be any CharSequence, such as a String or StringBuilder
	 * @since org.openntf.domino 4.5.0
	 */
	public void addText(CharSequence content);

	/**
	 * Gets plain text context added via addText()
	 * 
	 * @return String List of plain text components
	 * @since org.openntf.domino 4.5.0
	 */
	public List<String> getText();

	/**
	 * Adds a url to JSON content
	 * 
	 * @param jsonContent
	 *            String url for JSON content
	 * @since org.openntf.domino 4.5.0
	 */
	public void setJSON(String jsonContent);

	/**
	 * Gets content added via setJSON()
	 * 
	 * @return String url for JSON email data
	 * @since org.openntf.domino 4.5.0
	 */
	public String getJSON();

	/**
	 * Adds a MIMEEntity to the body of the email
	 * 
	 * @param contentMime
	 *            MIMEEntity containing the content
	 * @since org.openntf.domino 4.5.0
	 */
	public void addMimeEntity(MIMEEntity contentMime);

	/**
	 * Gets content added via addMIMEEntity
	 * 
	 * @return List<MIMEEntity> of MIME entities from the email
	 * @since org.openntf.domino 4.5.0
	 */
	public List<MIMEEntity> getMimeEntities();

	/**
	 * Removes a MIMEEntity from the email
	 * 
	 * @param content
	 *            MIMEEntity to remove from the email
	 * @since org.openntf.domino 4.5.0
	 */
	public void removeMimeEntity(MIMEEntity content);

	/**
	 * Adds an on-disk file to the internal ArrayList of EmailAttachment objects, generating and returning a unique identifier for the
	 * attachment
	 * 
	 * NOTE: Not yet implemented
	 * 
	 * @param unid
	 *            of document from which to retrieve the attachment
	 * @param fileName
	 *            of the attachment
	 * @param isInlineImage
	 *            whether attachment is to be inserted as an inline image
	 * @return String "cid:" + contentId;
	 * @since org.openntf.domino 4.5.0
	 */
	public String addDocAttachment(String unid, String fileName, boolean isInlineImage);

	/**
	 * Adds an attachment from a Document to the internal ArrayList of EmailAttachment objects, using a passed string as the unique
	 * identifier for the attachment
	 * 
	 * NOTE: Not yet implemented
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
	 * @since org.openntf.domino 4.5.0
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
	 * @since org.openntf.domino 4.5.0
	 */
	public String addFileAttachment(String path, String fileName, boolean isInlineImage, String contentId);

	/**
	 * Adds an attachment to an ArrayList of EmailAttachments
	 * 
	 * @param attachment
	 *            EmailAttachment object
	 * @since org.openntf.domino 4.5.0
	 */
	public void addAttachment(EmailAttachment attachment);

	/**
	 * Removes an attachment from an ArrayList of attachments
	 * 
	 * @param attachment
	 *            EmailAttachment object
	 * @since org.openntf.domino 4.5.0
	 */
	public void removeAttachment(IEmailAttachment attachment);

	/**
	 * Gets all attachments added via addAttachment()
	 * 
	 * @return List<EmailAttachment> of attachment objects
	 * @since org.openntf.domino 4.5.0
	 */
	public List<EmailAttachment> getAttachments();

	/**
	 * Adds all attachments to the parent MIMEEntity, using a passed ArrayList of EmailAttachment objects
	 * 
	 * @param parent
	 *            MIMEEntity that the attachments will be added to
	 * @since org.openntf.domino 4.5.0
	 */
	public void addAttachments(MIMEEntity parent);

	/**
	 * Gets the recipients the email is being sent to, from the To field
	 * 
	 * @return List<String> value for the To field of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public List<String> getTo();

	/**
	 * Gets the recipients the email is being copied to
	 * 
	 * @return List<String> value for the CopyTo field of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public List<String> getCC();

	/**
	 * Gets the recipients the email is being blind copied to
	 * 
	 * @return List<String> value for the BlindCopyTo field of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public List<String> getBCC();

	/**
	 * Gets the subject of the email
	 * 
	 * @return String value for the subject field of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public String getSubject();

	/**
	 * Gets the sender email address
	 * 
	 * @return String email address value for the From field (and all its variants) of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public String getSenderEmail();

	/**
	 * Gets the sender name
	 * 
	 * @return String name value for the From field (and all its variants) of the email
	 * @since org.openntf.domino 4.5.0
	 */
	public String getSenderName();

	/**
	 * Removes an individual recipient from the copy to ArrayList
	 * 
	 * @param cc
	 *            String email address
	 * @since org.openntf.domino 4.5.0
	 */
	public void removeCCAddress(String cc);

	/**
	 * Removes an individual recipient from the bcc ArrayList
	 * 
	 * @param bcc
	 *            String email address
	 * @since org.openntf.domino 4.5.0
	 */
	public void removeBCCAddress(String bcc);

	/**
	 * Removes an individual recipient from the to ArrayList
	 * 
	 * @param to
	 *            String email address
	 * @since org.openntf.domino 4.5.0
	 */
	public void removeToAddress(String to);

	/**
	 * Sets multiple recipients into the To field
	 * 
	 * @param to
	 *            Collection<String> of recipients
	 * @since org.openntf.domino 4.5.0
	 */
	public void setTo(Collection<String> to);

	/**
	 * Adds an email address to the ArrayList containing the recipients in the To field
	 * 
	 * @param to
	 *            String recipient
	 * @since org.openntf.domino 4.5.0
	 */
	public void addToAddress(String to);

	/**
	 * Sets multiple recipients for the email to be copied to
	 * 
	 * @param cc
	 *            Collection<String> of recipients
	 * @since org.openntf.domino 4.5.0
	 */
	public void setCC(Collection<String> cc);

	/**
	 * Adds an email address to the ArrayList containing the CC recipients
	 * 
	 * @param cc
	 *            String recipient
	 * @since org.openntf.domino 4.5.0
	 */
	public void addCCAddress(String cc);

	/**
	 * Sets multiple recipients for the email to be blind copied to
	 * 
	 * @param bcc
	 *            Collection<String> of recipients
	 * @since org.openntf.domino 4.5.0
	 */
	public void setBCC(Collection<String> bcc);

	/**
	 * Adds an email address to the ArrayList containing the BCC recipients
	 * 
	 * @param bcc
	 *            String recipient
	 * @since org.openntf.domino 4.5.0
	 */
	public void addBCCAddress(String bcc);

	/**
	 * Sets the subject for the email
	 * 
	 * @param subject
	 *            String value for the email subject
	 * @since org.openntf.domino 4.5.0
	 */
	public void setSubject(String subject);

	/**
	 * Sets From field (and all its variants) with an email address
	 * 
	 * @param sender
	 *            String email address to send from
	 * @since org.openntf.domino 4.5.0
	 */
	public void setSenderEmail(String sender);

	/**
	 * Sets a name to be added along with the email address. The format will be: "John Doe"<john.doe@jdoe.net>
	 * 
	 * @param sender
	 *            String name to send from
	 * @since org.openntf.domino 4.5.0
	 */
	public void setSenderName(String sender);

	/**
	 * Sends the email, creating an email object with all the other properties of this class
	 * 
	 * @return successfully created Document
	 * @since org.openntf.domino 4.5.0
	 */
	public Document send();

}
