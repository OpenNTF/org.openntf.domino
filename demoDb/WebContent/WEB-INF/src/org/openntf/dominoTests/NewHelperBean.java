package org.openntf.dominoTests;

/*
 	Copyright 2014 OpenNTF Domino API Team Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
	
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.email.DominoEmail;
import org.openntf.domino.helpers.DocumentSorter;
import org.openntf.domino.helpers.DocumentSyncHelper;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.XspOpenLogUtil;
import org.openntf.domino.xsp.helpers.NSA;
import org.openntf.formula.ASTNode;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.Formulas;

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

	public void processFormula() {
		try {
			String passedFormula = (String) ExtLibUtil.getViewScope().get("javaFormula");
			ASTNode ast = null;

			ast = Formulas.getParser().parse(passedFormula);
			FormulaContext ctx1 = Formulas.createContext(null, Formulas.getParser());
			List<Object> result = ast.solve(ctx1);
			ExtLibUtil.getViewScope().put("javaTest", result);
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}

	public void getNSAApps() {
		ExtLibUtil.getViewScope().put("javaTest", NSA.INSTANCE.getReport());
	}

	public void getSessions() {
		Factory.getTrustedSession();
		Factory.getSessionFullAccess();
		ExtLibUtil.getViewScope().put("javaTest", "Done");
	}
}
