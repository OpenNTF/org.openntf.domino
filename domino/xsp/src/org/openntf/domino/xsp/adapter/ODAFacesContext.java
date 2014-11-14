package org.openntf.domino.xsp.adapter;

import javax.faces.context.FacesContext;

import com.ibm.xsp.domino.context.DominoFacesContext;

public class ODAFacesContext extends DominoFacesContext {
	public ODAFacesContext(final FacesContext paramFacesContext) {
		super(paramFacesContext);
	}

}
