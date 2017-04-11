/**
 * 
 */
package org.openntf.domino.email;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.Session;
import org.openntf.domino.Stream;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.email.IEmailAttachment.Type;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.StringUtil;

/**
 * @author withersp
 * 
 *         Creates an email with text, HTML or JSON content. Also allows attachments. Based on Mark Leusink's XSnippet {@link http
 *         ://openntf.org/XSnippets.nsf/snippet.xsp?id=create-html-mails-in-ssjs-using-mime}
 * 
 *         Usage example (simple):
 * 
 *         <code>
 *   DominoEmail myEmail = new DominoEmail();
 *   myEmail.createSimpleEmail(toNames,ccNames,bccNames,"My Subject",body,"fromEmail@emailDomain.com");
 * </code>
 * 
 *         Usage example (extended):
 * 
 *         <code> DominoEmail myEmail = new DominoEmail(); ArrayList<String> sendTo = new ArrayList<String>();
 *         sendTo.add("pwithers@intec.co.uk"); myEmail.setTo(sendTo); ArrayList<String> cc = new ArrayList<String>();
 *         cc.add("user@domain.com"); cc.add("anotheruser@domain.com"); myEmail.setCC(cc); ArrayList<String> bcc = new ArrayList<String>();
 *         bcc.add("user3@domain.com"); myEmail.setBCC(bcc); myEmail.setSubject("Your notification"); StringBuilder body = new
<<<<<<< HEAD
 *         StringBuilder(); body.append("<h1>Hi!</h1>"); body.append("<table>"); body.append("<tbody>"); body.append("<tr>"); body.append(
 *         "<td>contents in a table here</td>"); body.append("</tr>"); body.append("</tbody>"); body.append("</table>");
 *         myEmail.addHTML(body); myEmail.addFileAttachment( "c:/temp/report.pdf", "report.pdf", false);
=======
 *         StringBuilder(); body.append("
 *         <h1>Hi!</h1>"); body.append("
 *         <table>
 * 		"); body.append("<tbody>"); body.append("
 *         <tr>
 * 		"); body.append("
 *         <td>contents in a table here</td>"); body.append("
 *         </tr>
 * 		"); body.append("</tbody>"); body.append("
 *         </table>
 * 		"); myEmail.addHTML(body); myEmail.addFileAttachment( "c:/temp/report.pdf", "report.pdf", false);
>>>>>>> nathan
 *         myEmail.setSenderEmail("pwithers@intec.co.uk"); myEmail.setSenderName("Paul Withers"); myEmail.send();
 */

public class DominoEmail implements IEmail {
	private List<String> to_ = new ArrayList<String>();
	private List<String> cc_ = new ArrayList<String>();
	private List<String> bcc_ = new ArrayList<String>();
	private String senderEmail_;
	private String senderName_;
	private String subject_;
	private List<String> contentsText_ = new ArrayList<String>();
	private List<String> contentsHTML_ = new ArrayList<String>();
	private String urlJSON_;
	private List<MIMEEntity> mimeEntities_ = new ArrayList<MIMEEntity>();
	private List<EmailAttachment> attachments_ = new ArrayList<EmailAttachment>();
	private Session currSess_;

	/**
	 * Cross ref with
	 * https://github.com/OpenNTF/SocialSDK/blob/2a6bd1cc6300e13b0c00c23d936c876158a79efd/src/eclipse/plugins/com.ibm.sbt.core
	 * /src/com/ibm/sbt/services/client/email/MimeEmailFactory.java and
	 * http://openntf.org/XSnippets.nsf/snippet.xsp?id=create-html-mails-in-ssjs-using-mime
	 * 
	 */
	@Deprecated
	public DominoEmail() {
		currSess_ = Factory.getSession(SessionType.CURRENT);
	}

	/**
	 * RPr: New Constructor. Should specify Session on creation
	 * 
	 * @param sess
	 *            the session of that email
	 */
	public DominoEmail(final Session sess) {
		currSess_ = sess;
	}

	/**
	 * Gets the session related to the email
	 * 
	 * @return Session
	 * @since org.openntf.domino 4.5.0
	 */
	public Session getSession() {
		return currSess_;
	}

	/**
	 * Creates an email in two lines of code:<br/>
	 * <code>
	 *   DominoEmail myEmail = new DominoEmail();
	 *   myEmail.createSimpleEmail(toNames,ccNames,bccNames,"My Subject",body,"fromEmail@emailDomain.com");
	 * </code>
	 * 
	 * @param toNames
	 *            List, array or comma-separated string of names
	 * @param ccNames
	 *            List, array or comma-separated string of names
	 * @param bccNames
	 *            List, array or comma-separated string of names
	 * @param subject
	 *            String subject
	 * @param body
	 *            StringBuilder, String or MIMEEntity. StringBuilder is preferred to String
	 * @param sender
	 *            String email address to send from
	 * @return Document successful memo
	 * @since org.openntf.domino 4.5.0
	 */
	public Document createSimpleEmail(final Object toNames, final Object ccNames, final Object bccNames, final String subject,
			final Object body, final String sender) {
		try {
			setTo(convertObjectToList(toNames, ","));
			setCC(convertObjectToList(ccNames, ","));
			setBCC(convertObjectToList(bccNames, ","));
			setSubject(subject);
			setSenderEmail(sender);
			if (body instanceof CharSequence) {
				addHTML((CharSequence) body);
			} else if (body instanceof MIMEEntity) {
				addMimeEntity((MIMEEntity) body);
			} else {
				StringBuilder tmp = new StringBuilder();
				tmp.append(body.toString());
				addHTML(tmp);
			}
			return send();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	/**
	 * Takes an object - String, List, Array - and converts it to a List for passing into to, cc, bcc
	 * 
	 * @param obj
	 *            object to be tested / converted into a List of strings
	 * @param separator
	 *            String separator to use if obj is a multi-value string, e.g. comma-separated
	 * @return List of Strings
	 * @since org.openntf.domino 4.5.0
	 */
	@SuppressWarnings("unchecked")
	public static List<String> convertObjectToList(final Object obj, final String separator) {
		try {
			// Quit out if the parameter was null
			if (null == obj) {
				return null;
			}
			// Check for common types, else just call obj.toString
			List<String> retVal_ = new ArrayList<String>();
			if (obj instanceof List) {
				retVal_.addAll((List<String>) obj);
			} else if (obj instanceof CharSequence) {
				String tmp = obj.toString();
				String[] tmpArr = tmp.split(",");
				for (int i = 0; i < tmpArr.length; i++) {
					retVal_.add(tmpArr[i]);
				}
			} else if (obj instanceof String[]) {
				String[] tmp = (String[]) obj;
				for (int i = 0; i < tmp.length; i++) {
					retVal_.add(tmp[i]);
				}
			} else {
				retVal_.add(obj.toString());
			}
			return retVal_;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	/**
	 * Generates a content id. This is used to generate a unique reference for each attachment to be added to the email
	 * 
	 * @return String result of @Unique
	 * @since org.openntf.domino 4.5.0
	 */
	@SuppressWarnings("unused")
	private String generateContentId() {
		try {
			Vector<Object> evalResult = getSession().evaluate("@Unique");
			return evalResult.get(0).toString();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addHTML(java.lang.StringBuilder)
	 */
	@Override
	public void addHTML(final CharSequence content) {
		contentsHTML_.add(content.toString());

		// Add plain text part of email
		if (StringUtil.isEmpty(content.toString())) {
			contentsText_.add("");
		} else {
			contentsText_.add(content.toString().replaceAll("<[a-zA-Z\\/][^>]*>", ""));
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getHTML()
	 */
	@Override
	public List<String> getHTML() {
		return new ArrayList<String>(contentsHTML_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addText(java.lang.StringBuilder)
	 */
	@Override
	public void addText(final CharSequence content) {
		contentsText_.add(content.toString());

		// Add HTML part by replacing all line breaks with br tag
		contentsHTML_.add(StringUtil.replace(content.toString(), System.getProperty("line.separator"), "<br/>"));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getText()
	 */
	@Override
	public List<String> getText() {
		return new ArrayList<String>(contentsText_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setJSON(java.lang.String)
	 */
	@Override
	public void setJSON(final String url) {
		urlJSON_ = url;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getJSON()
	 */
	@Override
	public String getJSON() {
		return urlJSON_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addMimeEntity(org.openntf.domino.MIMEEntity)
	 */
	@Override
	public void addMimeEntity(final MIMEEntity contentMime) {
		mimeEntities_.add(contentMime);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getMimeEntities()
	 */
	@Override
	public List<MIMEEntity> getMimeEntities() {
		return new ArrayList<MIMEEntity>(mimeEntities_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#removeMimeEntity(org.openntf.domino.MIMEEntity)
	 */
	@Override
	public void removeMimeEntity(final MIMEEntity content) {
		if (mimeEntities_.contains(content)) {
			mimeEntities_.remove(content);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addDocAttachment(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	@Incomplete
	public String addDocAttachment(final String unid, final String fileName, final boolean isInlineImage) {
		// TODO Implement this or remove comment
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addDocAttachment(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	@Incomplete
	public String addDocAttachment(final String unid, final String fileName, final boolean isInlineImage, final String contentId) {
		// TODO Implement this or remove comment
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addFileAttachment(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public String addFileAttachment(final String path, final String fileName, final boolean isInlineImage) {
		EmailAttachment att = new EmailAttachment(path, fileName, isInlineImage);
		addAttachment(att);
		return "cid:" + att.getContentId();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addFileAttachment(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public String addFileAttachment(final String path, final String fileName, final boolean isInlineImage, final String contentId) {
		EmailAttachment att = new EmailAttachment(path, fileName, isInlineImage, contentId);
		addAttachment(att);
		return "cid:" + contentId;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addAttachments(org.openntf.domino.MIMEEntity)
	 */
	@Override
	public void addAttachment(final EmailAttachment attachment) {
		attachments_.add(attachment);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#removeAttachment(org.openntf.domino.email.EmailAttachment)
	 */
	@Override
	public void removeAttachment(final IEmailAttachment attachment) {
		if (attachments_.contains(attachment)) {
			attachments_.remove(attachment);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getAttachments()
	 */
	@Override
	public List<EmailAttachment> getAttachments() {
		return new ArrayList<EmailAttachment>(attachments_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addAttachments(java.util.ArrayList, org.openntf.domino.MIMEEntity)
	 */
	@Override
	public void addAttachments(final MIMEEntity parent) {
		try {
			Stream streamFile = null;
			for (IEmailAttachment attach : getAttachments()) {
				InputStream is = null;
				EmbeddedObject eo = null;

				String fileName = attach.getFileName();
				// Get content type
				String contentType = URLConnection.guessContentTypeFromName(fileName);
				if (null == contentType) {
					contentType = "application/octet-stream";
				}
				int idex = StringUtil.indexOfIgnoreCase(fileName, ".", fileName.length() - 6);
				if (idex > -1) {
					String extension = fileName.substring(idex);
					if (StringUtil.equals("gif", extension)) {
						contentType = "image/gif";
					} else if (StringUtil.equals("jpg", extension) || StringUtil.equals("jpeg", extension)) {
						contentType = "image/jpeg";
					} else if (StringUtil.equals("png", extension)) {
						contentType = "image/png";
					}
				}
				contentType += "; name=\"" + fileName + "\"";

				try {
					Type attachmentType = attach.getAttachmentType();
					if (attachmentType == Type.DOCUMENT) {
						//retrieve the document containing the attachment to send from the relevant
						Database dbFile = getSession().getDatabase(getSession().getServerName(), attach.getDbPath());
						Document docFile = dbFile.getDocumentByUNID(attach.getUnid());
						if (null != docFile) {
							eo = docFile.getAttachment(attach.getFileName());
							is = eo.getInputStream();
						}
					} else if (attachmentType == Type.FILE) {
						is = new FileInputStream(attach.getPath() + attach.getFileName());
					} else if (attachmentType == Type.STREAM) {
						is = attach.getInputStream();
					} else {
						is = new ByteArrayInputStream(attach.getBytes());
					}

					if (null != is) {
						MIMEEntity mimeChild = parent.createChildEntity();
						MIMEHeader mimeHeader = mimeChild.createHeader("Content-Disposition");

						if (attach.isInlineImage()) {
							mimeHeader.setHeaderVal("inline; filename=\"" + attach.getFileName() + "\"");
						} else {
							mimeHeader.setHeaderVal("attachment; filename=\"" + attach.getFileName() + "\"");
						}

						mimeHeader = mimeChild.createHeader("Content-ID");
						mimeHeader.setHeaderVal("<" + attach.getContentId() + ">");

						streamFile = getSession().createStream();
						streamFile.setContents(is);
						mimeChild.setContentFromBytes(streamFile, contentType, MIMEEntity.ENC_IDENTITY_BINARY);
						streamFile.close();
					}
				} catch (Exception e) {
					DominoUtils.handleException(e);
				} finally {
					if (null != is) {
						is.close();
					}
				}
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getTo()
	 */
	@Override
	public List<String> getTo() {
		return to_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getCC()
	 */
	@Override
	public List<String> getCC() {
		return cc_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getBCC()
	 */
	@Override
	public List<String> getBCC() {
		return bcc_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getSubject()
	 */
	@Override
	public String getSubject() {
		return subject_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getSenderEmail()
	 */
	@Override
	public String getSenderEmail() {
		return senderEmail_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getSenderName()
	 */
	@Override
	public String getSenderName() {
		return senderName_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#removeCCAddress(java.lang.String)
	 */
	@Override
	public void removeCCAddress(final String cc) {
		if (cc_.contains(cc)) {
			cc_.remove(cc);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#removeBCCAddress(java.lang.String)
	 */
	@Override
	public void removeBCCAddress(final String bcc) {
		if (bcc_.contains(bcc)) {
			bcc_.remove(bcc);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#removeToAddress(java.lang.String)
	 */
	@Override
	public void removeToAddress(final String to) {
		if (to_.contains(to)) {
			to_.remove(to);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setTo(java.util.ArrayList)
	 */
	@Override
	public void setTo(final Collection<String> to) {
		to_ = new ArrayList<String>(to);
	}

	public void setTo(final String to) {
		to_ = new ArrayList<String>();
		to_.add(to);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setToAddress(java.lang.String)
	 */
	@Override
	public void addToAddress(final String to) {
		getTo().add(to);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setCC(java.util.ArrayList)
	 */
	@Override
	public void setCC(final Collection<String> cc) {
		cc_ = new ArrayList<String>(cc);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setCCAddress(java.lang.String)
	 */
	@Override
	public void addCCAddress(final String cc) {
		cc_.add(cc);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setBCC(java.util.ArrayList)
	 */
	@Override
	public void setBCC(final Collection<String> bcc) {
		bcc_ = new ArrayList<String>(bcc);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setBCCAddress(java.lang.String)
	 */
	@Override
	public void addBCCAddress(final String bcc) {
		bcc_.add(bcc);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(final String subject) {
		subject_ = subject;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setSenderEmail(java.lang.String)
	 */
	@Override
	public void setSenderEmail(final String sender) {
		senderEmail_ = sender;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setSenderName(java.lang.String)
	 */
	@Override
	public void setSenderName(final String sender) {
		senderName_ = sender;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#send()
	 */
	@Override
	public Document send() {
		try {
			Stream stream;
			MIMEEntity mimeEntity;
			MIMEHeader mimeHeader;
			Database currDb;

			Session currSess = getSession();
			currSess.setConvertMime(false); // in case Khan is still in suspended animation!

			// Create memo doc
			try {
				currDb = currSess.getCurrentDatabase();
				if (null == currDb) {
					// Will this work if we're running from DOTS or OSGi plugin??
					currDb = currSess.getDatabase(currSess.getServerName(), "mail.box");
				}
			} catch (Throwable t) {
				currDb = currSess.getDatabase(currSess.getServerName(), "mail.box");
			}
			Document memo = currDb.createDocument();
			memo.put("RecNoOutOfOffice", "1");    //no replies from out of office agents
			String mimeBoundary = memo.getUniversalID().toLowerCase();

			MIMEEntity mimeRoot = memo.createMIMEEntity("Body");

			mimeHeader = mimeRoot.createHeader("To");
			mimeHeader.setHeaderVal(join(getTo(), ""));
			//memo.replaceItemValue("sendTo", getTo());

			if (cc_.size() > 0) {
				mimeHeader = mimeRoot.createHeader("CC");
				mimeHeader.setHeaderVal(join(getCC(), ""));
				//memo.replaceItemValue("cc", getCC());
			}

			if (bcc_.size() > 0) {
				mimeHeader = mimeRoot.createHeader("BCC");
				mimeHeader.setHeaderVal(join(getBCC(), ""));
				//memo.replaceItemValue("bcc", getBCC());
			}

			//set subject
			mimeHeader = mimeRoot.createHeader("Subject");
			mimeHeader.addValText(getSubject(), "UTF-8");

			//create text/alternative directive: text/plain and text/html part will be childs of this entity
			MIMEEntity mimeRootChild = mimeRoot.createChildEntity();

			mimeHeader = mimeRootChild.createHeader("Content-Type");
			mimeHeader.setHeaderVal("multipart/alternative; boundary=\"" + mimeBoundary + "\"");

			//create plain text part
			if (getText().size() > 0) {
				mimeEntity = mimeRootChild.createChildEntity();
				stream = currSess.createStream();
				stream.writeText(join(getText(), System.getProperty("line.separator")));
				mimeEntity.setContentFromText(stream, "text/plain; charset=\"UTF-8\"", MIMEEntity.ENC_NONE);
				stream.close();
			}

			//create HTML part
			if (contentsHTML_.size() > 0) {
				mimeEntity = mimeRootChild.createChildEntity();
				stream = currSess.createStream();
				stream.writeText(join(contentsHTML_, System.getProperty("line.separator")));
				mimeEntity.setContentFromText(stream, "text/html; charset=\"UTF-8\"", MIMEEntity.ENC_NONE);
				stream.close();
			}

			//create embedded JSON part
			if (StringUtil.isEmpty(getJSON())) {
				mimeEntity = mimeRootChild.createChildEntity();
				stream = currSess.createStream();
				String json = "{\"url\" : \"" + getJSON() + "\"}" + System.getProperty("line.separator");
				stream.writeText(json);
				mimeEntity.setContentFromText(stream, "application/embed+json; charset=\"UTF-8\"", MIMEEntity.ENC_NONE);
				stream.close();
			}

			// Add any attachments
			addAttachments(mimeRoot);

			//set the sender
			setSender(mimeRoot);

			// mime processing done.
			memo.closeMIMEEntities(true);

			//send the e-mail
			memo.send();

			return memo;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	/**
	 * Sets the sender details into the email, using senderName and senderEmail
	 * 
	 * @param mimeRoot
	 *            MIMEEntity root of the email, into which the sender details will be added as a child
	 * @since org.openntf.domino 4.5.0
	 */
	private void setSender(final MIMEEntity mimeRoot) {

		if (StringUtil.isEmpty(getSenderEmail())) {
			return;
		}

		MIMEHeader mimeHeader = null;

		mimeHeader = mimeRoot.createHeader("Reply-To");
		mimeHeader.setHeaderVal(getSenderEmail());

		mimeHeader = mimeRoot.createHeader("Return-Path");
		mimeHeader.setHeaderVal(getSenderEmail());

		if (StringUtil.isEmpty(getSenderName())) {

			mimeHeader = mimeRoot.createHeader("From");
			mimeHeader.setHeaderVal(getSenderEmail());
			mimeHeader = mimeRoot.createHeader("Sender");
			mimeHeader.setHeaderVal(getSenderEmail());

		} else {

			mimeHeader = mimeRoot.createHeader("From");
			mimeHeader.addValText("\"" + getSenderName() + "\" <" + getSenderEmail() + ">", "UTF-8");
			mimeHeader = mimeRoot.createHeader("Sender");
			mimeHeader.addValText("\"" + getSenderName() + "\" <" + getSenderEmail() + ">", "UTF-8");

		}

	};

	/**
	 * Take a Collection and join the values
	 * 
	 * TODO: Move to DominoUtils
	 * 
	 * @param vals
	 *            Collection of values
	 * @param separator
	 *            separator or empty/null to use default of comma
	 * @return String of joined values
	 * @since org.openntf.domino 4.5.0
	 */
	public static String join(final Collection<String> vals, String separator) {
		String retVal_ = "";
		if (StringUtil.isEmpty(separator)) {
			separator = ",";
		}
		for (String s : vals) {
			retVal_ += s + separator;
		}
		return retVal_.substring(0, retVal_.length() - 1);
	}
}
