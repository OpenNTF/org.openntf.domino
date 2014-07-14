package de.foconis.formula.wrapper;

/*----------------------------------------------------------------------------*/
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.openntf.formula.ASTNode;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaParser;
import org.openntf.formula.FormulaProvider;
import org.openntf.formula.Formulas;

import de.foconis.lsext.Java2LSExt;
import de.foconis.lsext.LSExt2Java;
import de.foconis.lsext.LSFocStringMap;

/*----------------------------------------------------------------------------*/
public class FocLSASTNode {

	private FormulaParser iParser;
	private ASTNode iASTNode;
	private Throwable iLastException = null;
	private int iProtLevel; // = 0;
	private String iProtFile; // = "d:/stein/Misc/formula/FocLSASTNode.log";

	/*----------------------------------------------------------------------------*/
	public FocLSASTNode() {
		this(0, null);
	}

	public FocLSASTNode(final int protLevel, final String protFile) {
		iProtLevel = protLevel;
		iProtFile = protFile;
	}

	/*----------------------------------------------------------------------------*/
	public String parse(final String formula) throws Throwable {
		if (iProtLevel > 0)
			protocol("parse: formula='" + formula + "'");
		iParser = Formulas.getParser();
		try {
			iASTNode = iParser.parse(formula, true);
			return "";
		} catch (Throwable t) {
			iParser = null;
			iASTNode = null;
			iLastException = t;
			if (iProtLevel > 0)
				protocol(null, null, iLastException);
			return getLastExcText();
		}
	}

	/*----------------------------------------------------------------------------*/
	public int getProtLevel() {
		return iProtLevel;
	}

	public void setProtLevel(final int how) {
		iProtLevel = how;
	}

	public String getProtFile() {
		return iProtFile;
	}

	public void setProtFile(final String protFile) {
		iProtFile = protFile;
	}

	/*----------------------------------------------------------------------------*/
	public String getLastExcText() {
		if (iLastException == null)
			return "";
		return "Exception " + iLastException.getClass().getName() + ": " + iLastException.getMessage();
	}

	/*----------------------------------------------------------------------------*/
	public String solve(final String wrStr, final String contextStr) {

		if (iProtLevel > 1)
			protocol("solve: mapStr='" + wrStr + "'\n\tcontextStr='" + contextStr + "'");
		if (iParser == null)
			return nothing();
		LSFocStringMap docMap = null;
		LSFocStringMap contextMap = null;
		try {
			LSExt2Java e2j = new LSExt2Java(wrStr);
			Object docObj = e2j.toJavaObj();
			if (iProtLevel > 2)
				protocol("DocObj:", e2j, docObj);
			e2j = new LSExt2Java(contextStr);
			Object contextObj = e2j.toJavaObj();
			if (iProtLevel > 2)
				protocol("ContextObj:", e2j, contextObj);
			if (docObj == null)
				docObj = new LSFocStringMap(true);
			else if (!(docObj instanceof LSFocStringMap))
				throw new IllegalArgumentException("Deserialized doc map of class " + docObj.getClass().getName());
			docMap = (LSFocStringMap) docObj;
			if (contextObj == null)
				contextObj = new LSFocStringMap();
			else if (!(contextObj instanceof LSFocStringMap))
				throw new IllegalArgumentException("Deserialized context map of class " + contextObj.getClass().getName());
			contextMap = (LSFocStringMap) contextObj;
		} catch (Exception e) {
			iLastException = e;
			if (iProtLevel > 0)
				protocol(null, null, iLastException);
			return nothing();
		}
		return solve(docMap, contextMap);
	}

	/*----------------------------------------------------------------------------*/
	private String solve(final LSFocStringMap docMap, final LSFocStringMap contextMap) {
		FormulaContext ctx = Formulas.createContext(docMap, iParser);
		ctx.useBooleans(false);
		ctx.setParameterProvider(new FormulaProvider<Object>() {
			public Object get(final String what) {
				return contextMap.get(what);
			}
		});
		try {
			docMap.activateChangeControl();
			List<?> res = iASTNode.solve(ctx);
			String ret = getSolveRetStr(res, docMap);
			if (iProtLevel > 0)
				protocol("solve result: '" + ret + "'");
			return ret;
		} catch (Throwable e) {
			iLastException = e;
			if (iProtLevel > 0)
				protocol(null, null, iLastException);
		}
		return nothing();
	}

	/*----------------------------------------------------------------------------*/
	private String getSolveRetStr(final List<?> res, final LSFocStringMap docMap) {
		LSFocStringMap retDocMap = new LSFocStringMap(true);
		Iterator<String> itr = docMap.getRemovedKeys().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			retDocMap.put(key, docMap.get(key));
		}
		itr = docMap.getWrittenKeys().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			retDocMap.put(key, docMap.get(key));
		}
		List<Object> ret = new ArrayList<Object>(2);
		ret.add(res);
		ret.add(retDocMap);
		if (iProtLevel > 1)
			protocol("getSolveRetStr:", null, ret);
		return (new Java2LSExt(ret)).toLSExt();
	}

	/*----------------------------------------------------------------------------*/
	private String nothing() {

		List<Object> ret = new ArrayList<Object>(2);
		ret.add(null);
		ret.add(null);
		Java2LSExt j2ls = new Java2LSExt(ret);
		if (iProtLevel > 0)
			protocol("Returning nothing()");
		return j2ls.toLSExt();
	}

	/*----------------------------------------------------------------------------*/
	public String getFormula() {
		if (iProtLevel > 0)
			protocol("getFormula");
		return (iASTNode == null) ? "" : iASTNode.getFormula();
	}

	/*----------------------------------------------------------------------------*/
	public String getErrMsg() {
		if (iProtLevel > 0)
			protocol("getErrMsg");
		return (iLastException == null) ? "" : iLastException.getMessage();
	}

	/*----------------------------------------------------------------------------*/
	public String getInputFields() {
		ArrayList<String> al;
		if (iASTNode == null)
			al = new ArrayList<String>();
		else
			al = new ArrayList<String>(iASTNode.getReadFields());
		if (iProtLevel > 0)
			protocol("getInputFields:", null, al);
		return (new Java2LSExt(al)).toLSExt();
	}

	/*----------------------------------------------------------------------------*/
	private void protocol(final String what) {
		protocol(what, null, null);
	}

	private void protocol(final String what, LSExt2Java ls2j, final Object o) {
		if (iProtLevel == 0)
			return;
		try {
			FileOutputStream fos = new FileOutputStream(iProtFile, true);
			PrintStream ps = new PrintStream(fos, true);
			ps.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
			if (o instanceof Throwable) {
				ps.println(getLastExcText());
				ps.println("STACKTRACE:");
				((Throwable) o).printStackTrace(ps);
			} else {
				ps.print(what);
				ps.print('\n');
				if (ls2j != null || o != null) {
					if (ls2j == null)
						ls2j = new LSExt2Java("");
					ls2j.dumpObject(ps, o);
				}
			}
			ps.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
