package de.foconis.test.lsext2java;

import java.io.FileInputStream;
import java.io.IOException;

import de.foconis.lsext.LSExt2Java;

public class LSExt2JavaTest {

	private String iFileName;

	public LSExt2JavaTest(final String fileName) {
		iFileName = fileName;
	}

	public static void main(final String[] args) throws IOException {
		LSExt2JavaTest ljt = new LSExt2JavaTest("d:/stein/misc/formula/config.ext.txt");
		ljt.run();
	}

	private void run() throws IOException {
		FileInputStream fis = new FileInputStream(iFileName);
		byte[] buff = new byte[128000];
		int got = fis.read(buff);
		fis.close();
		String lsStr = new String(buff, 0, got);
		buff = null;
		LSExt2Java ls2j = new LSExt2Java(lsStr);
		Object o = ls2j.toJavaObj();
		ls2j.dumpObject(System.out, o);
	}
}
