package de.foconis.test.lsext2java;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import de.foconis.lsext.Java2LSExt;
import de.foconis.lsext.LSExt2Java;

public class LSExt2JavaTest2 {

	private String iFileName;

	public LSExt2JavaTest2(final String fileName) {
		iFileName = fileName;
	}

	public static void main(final String[] args) throws IOException {
		LSExt2JavaTest2 ljt = new LSExt2JavaTest2("d:/stein/misc/formula/config.ext.txt");
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
		System.out.println("Erster toJavaObj:");
		ls2j.dumpObject(System.out, o);
		Java2LSExt j2ls = new Java2LSExt(o);
		String newLSStr = j2ls.toLSExt();
		System.out.println("Erster toLSExt:");
		System.out.println(newLSStr);
		ls2j = new LSExt2Java(newLSStr);
		Object o2 = ls2j.toJavaObj();
		System.out.println("Zweiter toJavaObj:");
		ls2j.dumpObject(System.out, o2);
		j2ls = new Java2LSExt(o2);
		String newLSStr2 = j2ls.toLSExt();
		System.out.println("Zweiter toLSExt:");
		System.out.println(newLSStr2);
		System.out.println("newLSStr2 eq newLSStr=" + newLSStr2.equals(newLSStr));
		if (!(o2 instanceof List))
			throw new IllegalStateException("o2 isn't a List???");
		@SuppressWarnings("unchecked")
		List<Object> l2 = (List<Object>) o2;
		l2.add(0, new Long(33123456789L));
		j2ls = new Java2LSExt(o2);
		String newLSStr3 = j2ls.toLSExt();
		System.out.println("Dritter toLSExt:");
		System.out.println(newLSStr3);
		l2.add(0, new Integer(7));
		l2.add(0, new Short((short) 13));
		l2.add(0, new Byte((byte) 27));
		l2.add(0, new Boolean(true));
		l2.add(0, new Boolean(false));
		j2ls = new Java2LSExt(o2);
		String newLSStr4 = j2ls.toLSExt();
		System.out.println("Vierter toLSExt:");
		System.out.println(newLSStr4);
		loopTest(o2);
	}

	private void loopTest(Object o) {
		long start = System.currentTimeMillis();
		System.out.println("Looptest start: " + start);
		for (int i = 0; i < 100000; i++) {
			Java2LSExt j2ls = new Java2LSExt(o);
			String s = j2ls.toLSExt();
			LSExt2Java ls2j = new LSExt2Java(s);
			o = ls2j.toJavaObj();
		}
		long ready = System.currentTimeMillis();
		System.err.println("Looptest ready: " + ready + " [" + (ready - start) + "]");
	}
}
