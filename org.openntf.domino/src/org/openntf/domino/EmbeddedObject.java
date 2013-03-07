package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.RichTextItem;
import lotus.domino.XSLTResultTarget;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public interface EmbeddedObject extends Base<lotus.domino.EmbeddedObject>, lotus.domino.EmbeddedObject {

	@Override
	public int activate(boolean arg0);

	@Override
	public void doVerb(String arg0);

	@Override
	public void extractFile(String arg0);

	@Override
	public String getClassName();

	@Override
	public int getFileSize();

	@Override
	public InputSource getInputSource();

	@Override
	public InputStream getInputStream();

	@Override
	public String getName();

	@Override
	public int getObject();

	@Override
	public RichTextItem getParent();

	@Override
	public Reader getReader();

	@Override
	public String getSource();

	@Override
	public int getType();

	@Override
	public Vector getVerbs();

	@Override
	public Document parseXML(boolean arg0) throws IOException;

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void transformXML(Object arg0, XSLTResultTarget arg1);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
