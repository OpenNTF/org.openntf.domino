package org.openntf.domino.nsfdata.structs.obj;

import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.design.impl.FileResource;
import org.openntf.domino.nsfdata.structs.cd.CDEVENT;
import org.openntf.domino.nsfdata.structs.cd.CDFILEHEADER;
import org.openntf.domino.nsfdata.structs.cd.CDRecord;
import org.openntf.domino.nsfdata.structs.cd.CData;

public class CDObject {

	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());

	public static CDObject create(final Iterator<CDRecord> records) {
		if (!records.hasNext())
			return null;
		CDRecord nextRecord = records.next();
		if (nextRecord instanceof CDFILEHEADER)
			return new CDResourceFile((CDFILEHEADER) nextRecord, records);

		if (nextRecord instanceof CDEVENT)
			return new CDResourceEvent((CDEVENT) nextRecord, records);

		throw new IllegalArgumentException("Do not know what to do with a " + nextRecord.getClass().getSimpleName());
	}

	public static CDObject create(final CData cdata) {
		Iterator<CDRecord> iter = cdata.iterator();
		try {
			return create(cdata.iterator());
		} finally {
			while (iter.hasNext()) {
				log_.warning("There are dangling elements in CData stream: " + iter.next().getClass().getName());
			}
		}
	}

}
