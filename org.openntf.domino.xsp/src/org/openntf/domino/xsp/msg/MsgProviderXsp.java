package org.openntf.domino.xsp.msg;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.xsp.model.DominoDocumentMapAdapter;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class MsgProviderXsp {

	private FacesContext iFC;
	private String iMsgPar;
	private UIComponent iUIComp;

	public MsgProviderXsp(final FacesContext fc, final UIComponent uiComponent, final String msgPar) {
		iFC = fc;
		iUIComp = uiComponent;
		iMsgPar = msgPar;
	}

	/*----------------------------------------------------------------------------*/
	private static final String lFullQualPref = "de.foconis.";
	private static final String lXSPMsgPkg = "de.foconis.xsp.messages";

	private boolean bundleIsFullQual(final String bundleName) {
		return bundleName.startsWith(lFullQualPref);
	}

	/*----------------------------------------------------------------------------*/
	private class ParseMsgPars {

		String iPackage = null;
		String iKey = null;
		String iDocName = null;
		String iBundleName = null;
		String iMessagesClassName = null;
		boolean iCaseGetStringXSP = false;

	}

	private ParseMsgPars inspectMsgPars() {
		String[] parts = iMsgPar.split("@");
		if (parts.length > 2)
			return null;
		ParseMsgPars ret = new ParseMsgPars();
		if (parts.length == 2)
			ret.iDocName = parts[1];
		parts = parts[0].split("/");
		if (parts.length > 2)
			return null;
		ret.iKey = parts[parts.length - 1];
		if (parts.length == 1) {
			UIViewRoot uvr = iFC.getViewRoot();
			if (!(uvr instanceof UIViewRootEx))
				throw new RuntimeException("getViewRoot isn't a UIViewRootEx! Can't determine XPage-name!");
			String pageName = ((UIViewRootEx) uvr).getPageName();
			for (int i = pageName.length() - 1; i >= 0; i--) {
				char c = pageName.charAt(i);
				if (c == '.')
					pageName = pageName.substring(0, i);
				else if (c == '/') {
					pageName = pageName.substring(i + 1);
					break;
				}
			}
			ret.iBundleName = pageName;	// .toLowerCase();
		} else
			ret.iBundleName = parts[0];
		ret.iCaseGetStringXSP = !bundleIsFullQual(ret.iBundleName);
		if (ret.iCaseGetStringXSP) {
			ret.iPackage = lXSPMsgPkg;
			ret.iBundleName = lXSPMsgPkg + "." + ret.iBundleName;
		} else
			ret.iPackage = ret.iBundleName;
		ret.iMessagesClassName = ret.iPackage + ".Messages";
		boolean case$ = (iUIComp.getParent() == null);	// ${msg:...}
		if (case$ && ret.iDocName != null)
			throw new RuntimeException("Including a document in ${msg:...} isn't allowed");
		if (!case$ && ret.iDocName == null)
			ret.iDocName = "currentDocument";
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	public Object getMsg() {
		ParseMsgPars pmp = inspectMsgPars();
		if (pmp == null)
			throw new IllegalArgumentException("Illegal msg binding: '" + iMsgPar + "'");
		Method mGetString = null;
		if (getStringMethodCache == null || (mGetString = getStringMethodCache.get(pmp.iMessagesClassName)) == null)
			mGetString = newGetStringMethod(pmp.iMessagesClassName);
		Object[] varArgs;
		Map<String, Object> dataMap = null;
		if (pmp.iDocName != null) {
			DominoDocument dominoDoc = (DominoDocument) ExtLibUtil.resolveVariable(iFC, pmp.iDocName);
			if (dominoDoc != null) {
				if (dominoDoc instanceof Map)
					dataMap = (Map<String, Object>) dominoDoc;
				else
					dataMap = new DominoDocumentMapAdapter(dominoDoc);
			}
		}
		if (dataMap == null)
			varArgs = new Object[0];
		else {
			varArgs = new Object[1];
			varArgs[0] = dataMap;
		}
		try {
			if (pmp.iCaseGetStringXSP)
				return mGetString.invoke(null, pmp.iBundleName, pmp.iKey, varArgs);
			else
				return mGetString.invoke(null, pmp.iKey, varArgs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*----------------------------------------------------------------------------*/
	private static Map<String, Method> getStringMethodCache = null;

	private synchronized Method newGetStringMethod(final String clsName) {
		if (getStringMethodCache == null)
			getStringMethodCache = new HashMap<String, Method>();
		Method ret = getStringMethodCache.get(clsName);
		if (ret != null)
			return ret;
		try {
			Class<?> cls = DominoUtils.getClass(clsName);
			if (cls == null)
				throw new IllegalArgumentException("Not found");
			Method[] mm = cls.getMethods();
			for (int i = 0; i < mm.length; i++) {
				String n = mm[i].getName();
				if (n.equals("getString") || n.equals("getStringXSP")) {
					ret = mm[i];
					break;
				}
			}
			if (ret == null)
				throw new IllegalArgumentException("Method not found: " + clsName + ".getString");
		} catch (Exception e) {
			throw new RuntimeException("Class '" + clsName + "' not found");
		}
		getStringMethodCache.put(clsName, ret);
		return ret;
	}
}
