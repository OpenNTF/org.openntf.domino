package de.foconis.test.lsext2java;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;

public class getfilch {

	public static void main(final String[] args) {
		getfilch mainObj = new getfilch(args);
		System.exit(mainObj.doIt());
	}

	/*----------------------------------------------------------------------------*/
	private String[] iArgs;
	ArrayList<String[]> iResList;

	/*----------------------------------------------------------------------------*/
	public getfilch(final String[] args) {
		iArgs = args;
	}

	/*----------------------------------------------------------------------------*/
	public int doIt() {
		if (iArgs.length == 0) {
			System.out.println("Usage: getfilch <file> [<file> ...]");
			return 2;
		}
		iResList = new ArrayList<String[]>(iArgs.length);
		for (int i = 0; i < iArgs.length; i++)
			if (!oneFile(iArgs[i]))
				return 1;
		printResult();
		return 0;
	}

	/*----------------------------------------------------------------------------*/
	private boolean oneFile(final String fileName) {
		File f = new File(fileName);
		long fileLh = 0;
		long fileCRC = 0;
		String errMsg = null;
		if (!f.exists())
			errMsg = "File '" + fileName + "' not found";
		else if (!f.isFile())
			errMsg = "File '" + fileName + "' isn't a normal file";
		else {
			fileLh = f.length();
			try {
				FileInputStream fis = new FileInputStream(f);
				CRC32 crc = new CRC32();
				byte[] buff = new byte[65536];
				int got;
				while ((got = fis.read(buff)) > 0)
					crc.update(buff, 0, got);
				fis.close();
				fileCRC = crc.getValue();
			} catch (Exception e) {
				errMsg = "Exception " + e.getClass().getName() + ": " + e.getMessage();
			}
		}
		if (errMsg != null) {
			System.out.println("getfilch: " + errMsg);
			return false;
		}
		String[] fileEnt = new String[3];
		fileEnt[0] = fileName;
		fileEnt[1] = Long.toString(fileLh);
		fileEnt[2] = Long.toHexString(fileCRC);
		iResList.add(fileEnt);
		return true;
	}

	/*----------------------------------------------------------------------------*/
	private void printResult() {
		int maxNameLh = 0;
		int maxLhLh = 0;
		int sz = iResList.size();
		for (int i = 0; i < sz; i++) {
			String[] ent = iResList.get(i);
			int lh = ent[0].length();
			if (lh > maxNameLh)
				maxNameLh = lh;
			lh = ent[1].length();
			if (lh > maxLhLh)
				maxLhLh = lh;
		}
		if (maxNameLh > 55)
			maxNameLh = 55;
		StringBuilder sb = new StringBuilder(256);
		for (int i = 0; i < sz; i++) {
			sb.setLength(0);
			String[] ent = iResList.get(i);
			String fName = ent[0];
			int lh = fName.length();
			if (lh > maxNameLh) {
				fName = "..." + fName.substring(lh - maxNameLh + 3);
				lh = fName.length();
			}
			sb.append(fName);
			for (; lh < maxNameLh + 4; lh++)
				sb.append(' ');
			lh = ent[1].length();
			for (lh = maxLhLh - lh; lh > 0; lh--)
				sb.append(' ');
			sb.append(ent[1]);
			sb.append("    ");
			lh = ent[2].length();
			for (lh = 8 - lh; lh > 0; lh--)
				sb.append('0');
			sb.append(ent[2]);
			System.out.println(sb.toString());
		}
	}
	/*----------------------------------------------------------------------------*/
}
