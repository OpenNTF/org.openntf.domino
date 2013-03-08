package org.openntf.domino;


public interface International extends Base<lotus.domino.International>, lotus.domino.International {

	@Override
	public String getAMString();

	@Override
	public int getCurrencyDigits();

	@Override
	public String getCurrencySymbol();

	@Override
	public String getDateSep();

	@Override
	public String getDecimalSep();

	@Override
	public lotus.domino.International getDelegate();

	@Override
	public Session getParent();

	@Override
	public String getPMString();

	@Override
	public String getThousandsSep();

	@Override
	public String getTimeSep();

	@Override
	public int getTimeZone();

	@Override
	public String getToday();

	@Override
	public String getTomorrow();

	@Override
	public String getYesterday();

	@Override
	public boolean isCurrencySpace();

	@Override
	public boolean isCurrencySuffix();

	@Override
	public boolean isCurrencyZero();

	@Override
	public boolean isDateDMY();

	@Override
	public boolean isDateMDY();

	@Override
	public boolean isDateYMD();

	@Override
	public boolean isDST();

	@Override
	public boolean isTime24Hour();

}
