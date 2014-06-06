package org.openntf.domino.xsp.msg;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.openntf.domino.utils.Factory;
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
	private static final String lMsgProvProvClass = lXSPMsgPkg + ".MsgProvProv";

	/*----------------------------------------------------------------------------*/
	private boolean bundleIsFullQual(final String bundleName) {
		return bundleName.startsWith(lFullQualPref);
	}

	/*----------------------------------------------------------------------------*/
	private class ParseMsgPars {

		String iKey = null;
		String iDocName = null;
		String iBundleName = null;

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
		if (!bundleIsFullQual(ret.iBundleName))
			ret.iBundleName = lXSPMsgPkg + "." + ret.iBundleName;
		boolean case$ = (iUIComp.getParent() == null);	// ${msg:...}
		if (case$ && ret.iDocName != null)
			throw new RuntimeException("Including a document in ${msg:...} isn't allowed");
		if (!case$ && ret.iDocName == null)
			ret.iDocName = "currentDocument";
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private ThreadLocal<Method> lGetStringMeth = new ThreadLocal<Method>();

	/*----------------------------------------------------------------------------*/
	public Object getMsg() {
		ParseMsgPars pmp = inspectMsgPars();
		if (pmp == null)
			throw new IllegalArgumentException("Illegal msg binding: '" + iMsgPar + "'");
		prepGetStringMethod();
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
			return lGetStringMeth.get().invoke(null, pmp.iBundleName, pmp.iKey, varArgs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*----------------------------------------------------------------------------*/
	private void prepGetStringMethod() {
		if (lGetStringMeth.get() != null && Factory.isClassLoaded(lMsgProvProvClass))
			return;
		lGetStringMeth.set(null);
		try {
			Class<?> cls = Factory.getClass(lMsgProvProvClass);
			if (cls == null)
				throw new IllegalArgumentException("Not found");
			lGetStringMeth.set(cls.getMethod("getString", String.class, String.class, Object[].class));
		} catch (Exception e) {
			throw new RuntimeException("Class '" + lMsgProvProvClass + "' not found");
		}
	}
}
