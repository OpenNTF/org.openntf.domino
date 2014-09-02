package org.openntf.dominoTests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.email.DominoEmail;
import org.openntf.domino.helpers.DocumentSorter;
import org.openntf.domino.helpers.DocumentSyncHelper;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NewHelperBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewHelperBean() {

	}

	public void syncDatabases() {
		Session s = Factory.getSession();
		Database currDb = s.getCurrentDatabase();
		Utils.addAllListeners(currDb);
		java.util.Map<Object, String> syncMap = new java.util.HashMap<Object, String>();
		syncMap.put("Key", "State");
		syncMap.put("Name", "StateName");
		syncMap.put("@Now", "LastSync");
		DocumentSyncHelper helper = new DocumentSyncHelper(DocumentSyncHelper.Strategy.CREATE_AND_REPLACE, syncMap,
				currDb.getServer(), currDb.getFilePath(), "AllContactsByState", "Key");
		View states = currDb.getView("AllStates");
		DocumentCollection sourceCollection = states.getAllDocuments();
		helper.process(sourceCollection);
		ExtLibUtil.getViewScope().put("javaTest", "Done");
	}

	public void sendSimpleEmail() {
		DominoEmail myEmail = new DominoEmail();
		myEmail.createSimpleEmail(Factory.getSession().getEffectiveUserName(), "", "", "OpenNTF Domino API Email",
				"this is an email from Java in the OpenNTF Domino API", "");
	}

	public void sendComplexEmail() {
		DominoEmail myEmail = new DominoEmail();
		ArrayList<String> sendTo = new ArrayList<String>();
		sendTo.add("pwithers@intec.co.uk");
		myEmail.setTo(sendTo);
		ArrayList<String> cc = new ArrayList<String>();
		cc.add("user@domain.com");
		cc.add("anotheruser@domain.com");
		myEmail.setCC(cc);
		ArrayList<String> bcc = new ArrayList<String>();
		bcc.add("user3@domain.com");
		myEmail.setBCC(bcc);
		myEmail.setSubject("Your notification");
		StringBuilder body = new StringBuilder();
		body.append("<h1>Hi!</h1>");
		body.append("<table>");
		body.append("<tbody>");
		body.append("<tr>");
		body.append("<td>contents in a table here</td>");
		body.append("</tr>");
		body.append("</tbody>");
		body.append("</table>");
		myEmail.addHTML(body);
		myEmail.addFileAttachment("c:/temp/report.pdf", "report.pdf", false);
		myEmail.setSenderEmail("pwithers@intec.co.uk");
		myEmail.setSenderName("Paul Withers");
		myEmail.send();
	}

	public void getGroups() {
		Session currSess = Factory.getSession();
		Name currName = currSess.createName(currSess.getEffectiveUserName());
		ExtLibUtil.getViewScope().put("javaTest", currName.getGroups(currSess.getServerName()));
	}

	@SuppressWarnings("unchecked")
	public DocumentCollection getSortedCollection() {
		String sSearch = "FIELD Author contains \"Aline Winters\"";
		org.openntf.domino.DocumentCollection dc = Factory.getSession().getCurrentDatabase().FTSearch(sSearch, 500);
		List criteria = new ArrayList();
		criteria.add("Date");
		DocumentSorter sorter = new org.openntf.domino.helpers.DocumentSorter(dc, criteria);
		DocumentCollection results = sorter.sort();
		ExtLibUtil.getViewScope().put("javaTest", results.getCount());
		return results;
	}
}
