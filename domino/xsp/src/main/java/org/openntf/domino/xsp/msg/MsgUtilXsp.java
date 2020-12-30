/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp.msg;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.openntf.domino.AsDocMap;
import org.openntf.domino.i18n.MessageProvider;
import org.openntf.domino.xsp.model.DominoDocumentMapAdapter;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

@SuppressWarnings("nls")
public enum MsgUtilXsp {
	;

	/*----------------------------------------------------------------------------*/
	public static final String lXSPMsgPkg = "org.openntf.domino.xsp.i18n"; //$NON-NLS-1$

	/*----------------------------------------------------------------------------*/
	/**
	 * If a bundleName contains at least two dots, we assume that it is full qualified.
	 * 
	 * @param bundleName
	 * @return
	 */
	private static boolean bundleIsFullQual(final String bundleName) {
		int dotPos = bundleName.indexOf('.');
		if (dotPos < 0)
			return false;
		return bundleName.indexOf('.', dotPos + 1) >= 0;
	}

	/*----------------------------------------------------------------------------*/

	/**
	 * Helper class for parsing
	 */
	private static class ParseMsgPars {
		String iKey = null;
		String iDocName = null;
		String iBundleName = null;
	}

	/**
	 * parses msgPar and returns it in a wrapped object. Format of msgPar
	 * 
	 * <pre>
	 * key
	 * key{@literal @}document
	 * bundleName/key
	 * bundleName/key{@literal @}document
	 * </pre>
	 * 
	 * The <code>key</code> represents the "key" in the <code>.properties</code> file or in your custom providers.<br/>
	 * The <code>bundleName</code> is the BundleName. If none is specified, PageName is used. If the name is not full qualified (= contains
	 * at least 2 dots), "org.openntf.domino.xsp.i18n" is used as prefix.<br/>
	 * The <code>document</code> is the document that should be used for formula evaluation. If none is specified, currentDocument is used.
	 * 
	 * 
	 * @param fc
	 * @param uiComponent
	 * @param msgPar
	 * @return
	 */
	private static ParseMsgPars inspectMsgPars(final FacesContext fc, final UIComponent uiComponent, final String msgPar) {
		String[] parts = msgPar.split("@"); //$NON-NLS-1$
		if (parts.length > 2)
			return null;
		ParseMsgPars ret = new ParseMsgPars();
		if (parts.length == 2)
			ret.iDocName = parts[1];
		parts = parts[0].split("/"); //$NON-NLS-1$
		if (parts.length > 2)
			return null;
		ret.iKey = parts[parts.length - 1];
		if (parts.length == 1) {
			UIViewRoot uvr = fc.getViewRoot();
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
		boolean case$ = (uiComponent.getParent() == null);	// ${msg:...}
		if (case$ && ret.iDocName != null)
			throw new RuntimeException("Including a document in ${msg:...} isn't allowed");
		if (!case$ && ret.iDocName == null)
			ret.iDocName = "currentDocument";
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	public static Object getMsg(final FacesContext fc, final UIComponent uiComponent, final String msgPar) {
		ParseMsgPars pmp = inspectMsgPars(fc, uiComponent, msgPar);
		if (pmp == null)
			throw new IllegalArgumentException("Illegal msg binding: '" + msgPar + "'");

		MessageProvider prov = MessageProvider.getCurrentInstance();

		Map<String, Object> dataMap = null;
		if (pmp.iDocName != null) {
			Object dominoDoc = ExtLibUtil.resolveVariable(fc, pmp.iDocName);
			if (dominoDoc != null) {
				if (dominoDoc instanceof Map) {
					dataMap = (Map<String, Object>) dominoDoc;
				} else if (dominoDoc instanceof AsDocMap) {
					dataMap = ((AsDocMap) dominoDoc).asDocMap();
				} else if (dominoDoc instanceof DominoDocument) {
					dataMap = new DominoDocumentMapAdapter((DominoDocument) dominoDoc);
				}
			}
		}
		if (dataMap == null) {
			return prov.getString(pmp.iBundleName, pmp.iKey);
		} else {
			return prov.getString(pmp.iBundleName, pmp.iKey, dataMap);
		}
	}
}
