package org.openntf.domino.design.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.Document;

public class TemplateBuildSharedFieldNote extends SharedField implements org.openntf.domino.design.TemplateBuildSharedFieldNote {

	protected TemplateBuildSharedFieldNote(final Document document) {
		super(document);
	}

	@Override
	public String getTemplateBuildName() {
		return getItemValueString("$TemplateBuildName");
	}

	@Override
	public void setTemplateBuildName(final String templateBuildName) {
		Set<ItemFlag> flags = new HashSet<ItemFlag>();
		flags.add(ItemFlag._SIGN);
		setItemValue("$TemplateBuildName", templateBuildName, flags);
	}

	@Override
	public String getTemplateBuildVersion() {
		return getItemValueString("$TemplateBuild");
	}

	@Override
	public void setTemplateBuildVersion(final String templateBuildVersion) {
		Set<ItemFlag> flags = new HashSet<ItemFlag>();
		flags.add(ItemFlag._SIGN);
		setItemValue("$TemplateBuild", templateBuildVersion, flags);
	}

	@Override
	public Date getTemplateBuildDate() {
		return null;
	}

	@Override
	public void setTemplateBuildDate(final Date date) {
		// TODO Auto-generated method stub

	}

}
