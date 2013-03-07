package org.openntf.domino;

import java.util.Vector;

public interface MIMEHeader extends Base<lotus.domino.MIMEHeader>, lotus.domino.MIMEHeader {

	@Override
	public boolean addValText(String arg0);

	@Override
	public boolean addValText(String arg0, String arg1);

	@Override
	public lotus.domino.MIMEHeader getDelegate();

	@Override
	public String getHeaderName();

	@Override
	public String getHeaderVal();

	@Override
	public String getHeaderVal(boolean arg0);

	@Override
	public String getHeaderVal(boolean arg0, boolean arg1);

	@Override
	public String getHeaderValAndParams();

	@Override
	public String getHeaderValAndParams(boolean arg0);

	@Override
	public String getHeaderValAndParams(boolean arg0, boolean arg1);

	@Override
	public String getParamVal(String arg0);

	@Override
	public String getParamVal(String arg0, boolean arg1);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public boolean setHeaderVal(String arg0);

	@Override
	public boolean setHeaderValAndParams(String arg0);

	@Override
	public boolean setParamVal(String arg0, String arg1);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
