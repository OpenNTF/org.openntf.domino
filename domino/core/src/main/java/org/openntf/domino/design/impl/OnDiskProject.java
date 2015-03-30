package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;

import com.ibm.commons.util.StringUtil;

public class OnDiskProject {
	private File root_;

	public OnDiskProject(final File root) {
		root_ = root;
	}

	public void export(final org.openntf.domino.design.DesignBase elem_) throws IOException {
		//elem.getDxlString(null)
		AbstractDesignBase elem = (AbstractDesignBase) elem_;
		String odp = elem.getOnDiskPath();
		if (StringUtil.isEmpty(odp)) {
			odp = elem.getNoteID() + ".note";
		}
		//elem.getLastModified();
		File odsFile = new File(root_, odp);
		System.out.println(elem.getClass().getName() + "\t\t\t" + odsFile + "\t" + elem.getNoteID());
		odsFile.getParentFile().mkdirs(); // ensure the path exists
		elem.writeOnDiskFile(odsFile);
		if (elem instanceof HasMetadata) {
			File meta = new File(odsFile.getAbsolutePath() + ".metadata");
			((HasMetadata) elem).writeOnDiskMeta(meta);
		}

	}

}
