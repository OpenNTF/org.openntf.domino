package com.ibm.domino.das.service;

import javax.servlet.http.HttpServletRequest;

/**
 * This is just a stub until the real, IBM-crafted version is identified.
 * 
 * @author Jesse Gallagher
 *
 */
public interface IRestServiceExt {

	public boolean beforeDoService(HttpServletRequest request);
	public void afterDoService(HttpServletRequest request);
	public void onError(HttpServletRequest request, Throwable t);
	
}
