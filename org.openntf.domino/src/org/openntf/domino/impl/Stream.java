package org.openntf.domino.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class Stream extends Base<org.openntf.domino.Stream, lotus.domino.Stream> implements org.openntf.domino.Stream {

	public Stream(lotus.domino.Stream delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void close() {
		try {
			getDelegate().close();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getBytes() {
		try {
			return getDelegate().getBytes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getCharset() {
		try {
			return getDelegate().getCharset();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void getContents(OutputStream stream) {
		try {
			getDelegate().getContents(stream);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void getContents(Writer writer) {
		try {
			getDelegate().getContents(writer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getPosition() {
		try {
			return getDelegate().getPosition();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean isEOS() {
		try {
			return getDelegate().isEOS();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isReadOnly() {
		try {
			return getDelegate().isReadOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean open(String pathName) {
		try {
			return getDelegate().open(pathName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean open(String pathName, String charSet) {
		try {
			return getDelegate().open(pathName, charSet);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public byte[] read() {
		try {
			return getDelegate().read();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public byte[] read(int length) {
		try {
			return getDelegate().read(length);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String readText() {
		try {
			return getDelegate().readText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String readText(int length) {
		try {
			return getDelegate().readText(length);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String readText(int length, int eolType) {
		try {
			return getDelegate().readText(length, eolType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setContents(InputStream stream) {
		try {
			getDelegate().setContents(stream);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setContents(Reader reader) {
		try {
			getDelegate().setContents(reader);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPosition(int position) {
		try {
			getDelegate().setPosition(position);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void truncate() {
		try {
			getDelegate().truncate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int write(byte[] buffer) {
		try {
			return getDelegate().write(buffer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int writeText(String text) {
		try {
			return getDelegate().writeText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int writeText(String text, int eolType) {
		try {
			return getDelegate().writeText(text, eolType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}
}
