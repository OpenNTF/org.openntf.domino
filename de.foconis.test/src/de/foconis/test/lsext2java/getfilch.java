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
			long[] fileNums = new long[2];
			fileNums[0] = f.length();
			if ((errMsg = fileName.endsWith(".java") ? oneJavaFile(f, fileNums) : oneOtherFile(f, fileNums)) == null) {
				fileLh = fileNums[0];
				fileCRC = fileNums[1];
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
	private String oneOtherFile(final File f, final long[] fileNums) {
		try {
			FileInputStream fis = new FileInputStream(f);
			CRC32 crc = new CRC32();
			byte[] buff = new byte[65536];
			int got;
			while ((got = fis.read(buff)) > 0)
				crc.update(buff, 0, got);
			fis.close();
			fileNums[1] = crc.getValue();
			return null;
		} catch (Exception e) {
			return "Exception " + e.getClass().getName() + ": " + e.getMessage();
		}
	}

	/*----------------------------------------------------------------------------*/
	private String oneJavaFile(final File f, final long[] fileNums) {
		byte[] buff = new byte[(int) fileNums[0]];
		try {
			FileInputStream fis = new FileInputStream(f);
			fis.read(buff);
			fis.close();
		} catch (Exception e) {
			return "Exception " + e.getClass().getName() + ": " + e.getMessage();
		}
		buff = normalizeJava(buff, fileNums);
		CRC32 crc = new CRC32();
		crc.update(buff, 0, (int) fileNums[0]);
		fileNums[1] = crc.getValue();
		return null;
	}

	/*----------------------------------------------------------------------------*/
	private byte[] normalizeJava(final byte[] buff, final long[] fileNums) {
		int lh = buff.length;
		byte[] ret = new byte[lh];
		int count = 0;
		for (int i = 0; i < lh; i++) {
			byte b = buff[i];
			if (b == '/' && i < lh - 1 && buff[i + 1] == '/') {
				if (count != 0)
					ret[count++] = ' ';
				for (; i < lh; i++) {
					ret[count++] = buff[i];
					if (buff[i] == '\n')
						break;
				}
				continue;
			}
			if (b == '/' && i < lh - 1 && buff[i + 1] == '*') {
				if (count != 0)
					ret[count++] = ' ';
				ret[count++] = buff[i++];
				ret[count++] = buff[i++];
				for (; i < lh - 1; i++) {
					ret[count++] = buff[i];
					if (buff[i] == '*' && buff[i + 1] == '/') {
						ret[count++] = buff[++i];
						break;
					}
				}
				continue;
			}
			if (b == '"') {
				boolean lastWasBS = false;
				ret[count++] = buff[i++];
				for (; i < lh; i++) {
					ret[count++] = buff[i];
					if (buff[i] == '"' && !lastWasBS)
						break;
					lastWasBS = (!lastWasBS && buff[i] == '\\');
				}
				continue;
			}
			if (b == '\'') {
				boolean lastWasBS = false;
				ret[count++] = buff[i++];
				for (; i < lh; i++) {
					ret[count++] = buff[i];
					if (buff[i] == '\'' && !lastWasBS)
						break;
					lastWasBS = (!lastWasBS && buff[i] == '\\');
				}
				continue;
			}
			if (!Character.isWhitespace(b)) {
				ret[count++] = b;
				continue;
			}
			for (i++; i < lh; i++)
				if (!Character.isWhitespace(buff[i]))
					break;
			if (i >= lh)
				break;
			if ((Character.isLetterOrDigit(buff[i]) || buff[i] == '_') && count > 0
					&& (Character.isLetterOrDigit(ret[count - 1]) || ret[count - 1] == '_'))
				ret[count++] = ' ';
			i--;
		}
		fileNums[0] = count;
		// System.out.println(new String(ret, 0, count));
		return ret;
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
