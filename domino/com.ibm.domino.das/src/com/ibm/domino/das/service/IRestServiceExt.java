package com.ibm.domino.das.service;

import javax.servlet.http.HttpServletRequest;

public interface IRestServiceExt {

	public boolean beforeDoService(HttpServletRequest request);

	public void afterDoService(HttpServletRequest request);

	public void onError(HttpServletRequest request, Throwable t);

}
