package org.openntf.domino;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface Stream extends Base<lotus.domino.Stream>, lotus.domino.Stream {
	@Override
	public void close();

	@Override
	public int getBytes();

	@Override
	public String getCharset();

	@Override
	public void getContents(OutputStream stream);

	@Override
	public void getContents(Writer writer);

	@Override
	public int getPosition();

	@Override
	public boolean isEOS();

	@Override
	public boolean isReadOnly();

	@Override
	public boolean open(String pathName);

	@Override
	public boolean open(String pathName, String charSet);

	@Override
	public byte[] read();

	@Override
	public byte[] read(int length);

	@Override
	public String readText();

	@Override
	public String readText(int length);

	@Override
	public String readText(int length, int eolType);

	@Override
	public void setContents(InputStream stream);

	@Override
	public void setContents(Reader reader);

	@Override
	public void setPosition(int position);

	@Override
	public void truncate();

	@Override
	public int write(byte[] buffer);

	@Override
	public int writeText(String text);

	@Override
	public int writeText(String text, int eolType);
}
