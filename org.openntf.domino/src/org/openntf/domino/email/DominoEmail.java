/**
 * 
 */
package org.openntf.domino.email;

import java.io.FileInputStream;
import java.io.InputStream;
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
import org.openntf.domino.email.EmailAttachment.Type;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.StringUtil;

/**
 * @author withersp
 * 
 *         Creates an email with text, HTML or JSON content. Also allows attachments. Based on Mark Leusink's XSnippet
 *         http://openntf.org/XSnippets.nsf/snippet.xsp?id=create-html-mails-in-ssjs-using-mime#sthash.U5Lgr7N8.dpuf
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
 *         var mail = new HTMLMail(); mail.setTo( "m.leusink@gmail.com") mail.setCC( ["user@domain.com", "anotheruser@domaino.com"] );
 *         mail.setBB( "user3@domaino.com"); mail.setSubject("Your notification"); mail.addHTML("<h1>Hi!</h1>"); mail.addHTML("
 *         <table>
 *         <tbody>
 *         <tr>
 *         <td>contents in a table here</td>
 *         </tr>
 *         </tbody>
 *         </table>
 *         "); mail.addDocAttachment( "DC9126E84C59093FC1257953003C13E6", "jellyfish.jpg") mail.addFileAttachment( "c:/temp/report.pdf");
 *         mail.setSender("m.leusink@gmail.com", "Mark Leusink"); mail.send();
 */

public class DominoEmail implements IEmail {
	private ArrayList<String> to_ = new ArrayList<String>();
	private ArrayList<String> cc_ = new ArrayList<String>();
	private ArrayList<String> bcc_ = new ArrayList<String>();
	private String senderEmail_;
	private String senderName_;
	private String subject_;
	private ArrayList<String> contentsText_ = new ArrayList<String>();
	private ArrayList<String> contentsHTML_ = new ArrayList<String>();
	private String urlJSON_;
	private ArrayList<MIMEEntity> mimeEntities_ = new ArrayList<MIMEEntity>();
	private ArrayList<EmailAttachment> attachments_ = new ArrayList<EmailAttachment>();
	private Session currSess_;

	/**
	 * Cross ref with
	 * https://github.com/OpenNTF/SocialSDK/blob/2a6bd1cc6300e13b0c00c23d936c876158a79efd/src/eclipse/plugins/com.ibm.sbt.core
	 * /src/com/ibm/sbt/services/client/email/MimeEmailFactory.java and
	 * http://openntf.org/XSnippets.nsf/snippet.xsp?id=create-html-mails-in-ssjs-using-mime
	 * 
	 */
	public DominoEmail() {

	}

	public Session getSession() {
		if (null == currSess_) {
			currSess_ = Factory.getSession();
		}
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
	 */
	public Document createSimpleEmail(final Object toNames, final Object ccNames, final Object bccNames, final String subject,
			final Object body, final String sender) {
		try {
			setTo(convertObjectToList(toNames, ","));
			setCC(convertObjectToList(ccNames, ","));
			setBCC(convertObjectToList(bccNames, ","));
			setSubject(subject);
			setSenderEmail(sender);
			if (body instanceof StringBuilder) {
				addHTML((StringBuilder) body);
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

	/*
	 * Based on same method in SBT email, but requires MIMEPart class from there.
	 * Leaving it out for the moment, in case of issues with Apache licensing and dependencies
	public Document createJSONEmail(final JsonObject src) {
		try {
			setTo(convertObjectToList(src.getJsonProperty("to"), ","));
			setCC(convertObjectToList(src.getJsonProperty("cc"), ","));
			setBCC(convertObjectToList(src.getJsonProperty("bcc"), ","));
			Object subjectObj = src.getJsonProperty("subject");
			if (subjectObj instanceof String) {
				setSubject((String) subjectObj);
			} else {
				setSubject("");
			}
			Object senderObj = src.getJsonProperty("sender");
			if (senderObj instanceof String) {
				setSenderEmail((String) senderObj);
			}
			Object mimePartsObj = src.getJsonProperty("mimeParts");
			if (mimePartsObj instanceof List) {
				List<JsonObject> mimeParts = (List<JsonObject>) mimePartsObj;
				for (JsonObject json : mimeParts) {
					
				}
			}
			return send();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
	*/

	/**
	 * Takes an object - String, List, Array - and converts it to a List for passing into to, cc, bcc
	 * 
	 * @param obj
	 *            object to be tested / converted into a List of strings
	 * @param separator
	 *            String separator to use if obj is a multi-value string, e.g. comma-separated
	 * @return List of Strings
	 */
	public List<String> convertObjectToList(final Object obj, final String separator) {
		try {
			// Quit out if the parameter was null
			if (null == obj) {
				return null;
			}
			// Check for common types, else just call obj.toString
			List<String> retVal_ = new ArrayList<String>();
			if (obj instanceof List) {
				retVal_ = (List<String>) obj;
			} else if (obj instanceof String) {
				String tmp = (String) obj;
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

	private String generateContentId() {
		try {
			Vector evalResult = getSession().evaluate("@Unique");
			return evalResult.toString();
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
			content.toString().replaceAll("<[a-zA-Z\\/][^>]*>", "");
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getHTML()
	 */
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
		String tmpHTML = new String();
		tmpHTML = StringUtil.replace(content.toString(), System.getProperty("line.separator"), "<br/>");
		contentsHTML_.add(tmpHTML);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getText()
	 */
	public List<String> getText() {
		return new ArrayList<String>(contentsText_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#setJSON(java.lang.String)
	 */
	public void setJSON(final String url) {
		urlJSON_ = url;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getJSON()
	 */
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
	public String addDocAttachment(final String unid, final String fileName, final boolean isInlineImage) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#addDocAttachment(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public String addDocAttachment(final String unid, final String fileName, final boolean isInlineImage, final String contentId) {
		// TODO Auto-generated method stub
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
	public void removeAttachment(final EmailAttachment attachment) {
		if (attachments_.contains(attachment)) {
			attachments_.remove(attachment);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getAttachments()
	 */
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
			for (EmailAttachment attach : getAttachments()) {
				InputStream is = null;
				EmbeddedObject eo = null;

				// Get content type
				String contentType = "application/octet-stream";
				String fileName = attach.getFileName();
				int idex = StringUtil.indexOfIgnoreCase(fileName, ".", fileName.length() - 6);
				if (idex > -1) {
					String extension = fileName.substring(idex);
					if (StringUtil.equals("gif", extension)) {
						contentType = "image/gif";
					} else if (StringUtil.equals("jpg", extension) | StringUtil.equals("jpeg", extension)) {
						contentType = "image/jpeg";
					} else if (StringUtil.equals("png", extension)) {
						contentType = "image/png";
					}
				}
				contentType += "; name=\"" + fileName + "\"";

				try {
					if (attach.getAttachmentType() == Type.DOCUMENT) {
						//retrieve the document containing the attachment to send from the relevant
						Database dbFile = getSession().getDatabase(getSession().getServerName(), attach.getDbPath());
						Document docFile = dbFile.getDocumentByUNID(attach.getUnid());
						if (null != docFile) {
							eo = docFile.getAttachment(attach.getFileName());
							is = eo.getInputStream();
						}
					} else {
						is = new FileInputStream(attach.getPath() + attach.getFileName());
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
		return new ArrayList<String>(to_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getCC()
	 */
	@Override
	public List<String> getCC() {
		return new ArrayList<String>(cc_);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.email.IEmail#getBCC()
	 */
	@Override
	public List<String> getBCC() {
		return new ArrayList<String>(bcc_);
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
			MIMEEntity mimeRoot = memo.createMIMEEntity("Body");

			mimeHeader = mimeRoot.createHeader("To");
			mimeHeader.setHeaderVal(join(getTo(), ""));

			if (cc_.size() > 0) {
				mimeHeader = mimeRoot.createHeader("CC");
				mimeHeader.setHeaderVal(join(cc_, ""));
			}

			if (bcc_.size() > 0) {
				mimeHeader = mimeRoot.createHeader("BCC");
				mimeHeader.setHeaderVal(join(bcc_, ""));
			}

			//set subject
			mimeHeader = mimeRoot.createHeader("Subject");
			mimeHeader.setHeaderVal(getSubject());

			//create text/alternative directive: text/plain and text/html part will be childs of this entity
			MIMEEntity mimeRootChild = mimeRoot.createChildEntity();
			String mimeBoundary = memo.getUniversalID().toLowerCase();
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

			//send the e-mail
			memo.send();

			return memo;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

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
	 * Take a Collection and join the values TODO: Move to DominoUtils
	 * 
	 * @param vals
	 *            Collection of values
	 * @param separator
	 *            separator or empty/null to use default of comma
	 * @return String of joined values
	 */
	public static String join(final Collection<String> vals, String separator) {
		String retVal_ = "";
		if (StringUtil.isEmpty(separator)) {
			separator = ",";
		}
		for (String s : vals) {
			retVal_ += s + separator;
		}
		return retVal_;
	}
}
