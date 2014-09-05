package org.openntf.dominoTests;

import java.io.Serializable;

import org.openntf.arpa.NamePartsMap.Key;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class NewNameBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public void getGroups() {
		Session currSess = Factory.getSession();
		Name currName = currSess.createName(currSess.getEffectiveUserName());
		ExtLibUtil.getViewScope().put("javaTest", currName.getGroups(currSess.getServerName()));
	}

	public void getNameParts() {
		StringBuilder sb = new StringBuilder();
		Session currSess = Factory.getSession();
		Name currName = currSess.createName(currSess.getEffectiveUserName());
		sb.append("Abbreviated: " + currName.getNamePart(Key.Abbreviated));
		sb.append("<br/>Addr821: " + currName.getNamePart(Key.Addr821));
		sb.append("<br/>Addr822Comment1: " + currName.getNamePart(Key.Addr822Comment1));
		sb.append("<br/>Addr822Comment2: " + currName.getNamePart(Key.Addr822Comment2));
		sb.append("<br/>Addr822Comment3: " + currName.getNamePart(Key.Addr822Comment3));
		sb.append("<br/>Addr822 Local Part: " + currName.getNamePart(Key.Addr822LocalPart));
		sb.append("<br/>Addr822 Phrase: " + currName.getNamePart(Key.Addr822Phrase));
		sb.append("<br/>ADMD: " + currName.getNamePart(Key.ADMD));
		sb.append("<br/>Canonical: " + currName.getNamePart(Key.Canonical));
		sb.append("<br/>Common: " + currName.getNamePart(Key.Common));
		sb.append("<br/>Country: " + currName.getNamePart(Key.Country));
		sb.append("<br/>Generation: " + currName.getNamePart(Key.Generation));
		sb.append("<br/>Given name: " + currName.getNamePart(Key.Given));
		sb.append("<br/>ID Prefix: " + currName.getNamePart(Key.IDprefix));
		sb.append("<br/>Initials: " + currName.getNamePart(Key.Initials));
		sb.append("<br/>Keyword: " + currName.getNamePart(Key.Keyword));
		sb.append("<br/>Language: " + currName.getNamePart(Key.Language));
		sb.append("<br/>Organization: " + currName.getNamePart(Key.Organization));
		sb.append("<br/>OUs: " + currName.getNamePart(Key.OrgUnit1) + ", " + currName.getNamePart(Key.OrgUnit2) + ", "
				+ currName.getNamePart(Key.OrgUnit3) + ", " + currName.getNamePart(Key.OrgUnit4));
		sb.append("<br/>PRMD: " + currName.getNamePart(Key.PRMD));
		sb.append("<br/>Source String: " + currName.getNamePart(Key.SourceString));
		sb.append("<br/>Surname: " + currName.getNamePart(Key.Surname));
		ExtLibUtil.getViewScope().put("javaTest", sb.toString());
	}

}
