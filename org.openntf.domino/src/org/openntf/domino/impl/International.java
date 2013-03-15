package org.openntf.domino.impl;

public class International extends Base<org.openntf.domino.International, lotus.domino.International> implements
		org.openntf.domino.International {

	public International(lotus.domino.International delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String getAMString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrencyDigits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCurrencySymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateSep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDecimalSep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	@Override
	public String getPMString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getThousandsSep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimeSep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimeZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getToday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTomorrow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getYesterday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCurrencySpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrencySuffix() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrencyZero() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDST() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDateDMY() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDateMDY() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDateYMD() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTime24Hour() {
		// TODO Auto-generated method stub
		return false;
	}
}
