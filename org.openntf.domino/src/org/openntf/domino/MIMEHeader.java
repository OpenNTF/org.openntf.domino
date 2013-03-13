package org.openntf.domino;

public interface MIMEHeader extends Base<lotus.domino.MIMEHeader>, lotus.domino.MIMEHeader {

	@Override
	public boolean addValText(String valueText);

	@Override
	public boolean addValText(String valueText, String charSet);

	@Override
	public String getHeaderName();

	@Override
	public String getHeaderVal();

	@Override
	public String getHeaderVal(boolean folded);

	@Override
	public String getHeaderVal(boolean folded, boolean decoded);

	@Override
	public String getHeaderValAndParams();

	@Override
	public String getHeaderValAndParams(boolean folded);

	@Override
	public String getHeaderValAndParams(boolean folded, boolean decoded);

	@Override
	public String getParamVal(String paramName);

	@Override
	public String getParamVal(String paramName, boolean folded);

	@Override
	public void remove();

	@Override
	public boolean setHeaderVal(String headerValue);

	@Override
	public boolean setHeaderValAndParams(String headerParamValue);

	@Override
	public boolean setParamVal(String parameterName, String parameterValue);

}
