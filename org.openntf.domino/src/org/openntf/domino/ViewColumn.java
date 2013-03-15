package org.openntf.domino;

public interface ViewColumn extends Base<lotus.domino.ViewColumn>, lotus.domino.ViewColumn {
	@Override
	public int getAlignment();

	@Override
	public int getColumnValuesIndex();

	@Override
	public int getDateFmt();

	@Override
	public int getFontColor();

	@Override
	public String getFontFace();

	@Override
	public int getFontPointSize();

	@Override
	public int getFontStyle();

	@Override
	public String getFormula();

	@Override
	public int getHeaderAlignment();

	@Override
	public int getHeaderFontColor();

	@Override
	public String getHeaderFontFace();

	@Override
	public int getHeaderFontPointSize();

	@Override
	public int getHeaderFontStyle();

	@Override
	public String getItemName();

	@Override
	public int getListSep();

	@Override
	public int getNumberAttrib();

	@Override
	public int getNumberDigits();

	@Override
	public int getNumberFormat();

	@Override
	public View getParent();

	@Override
	public int getPosition();

	@Override
	public String getResortToViewName();

	@Override
	public int getSecondaryResortColumnIndex();

	@Override
	public int getTimeDateFmt();

	@Override
	public int getTimeFmt();

	@Override
	public int getTimeZoneFmt();

	@Override
	public String getTitle();

	@Override
	public int getWidth();

	@Override
	public boolean isAccentSensitiveSort();

	@Override
	public boolean isCaseSensitiveSort();

	@Override
	public boolean isCategory();

	@Override
	public boolean isConstant();

	@Override
	public boolean isField();

	@Override
	public boolean isFontBold();

	@Override
	public boolean isFontItalic();

	@Override
	public boolean isFontStrikethrough();

	@Override
	public boolean isFontUnderline();

	@Override
	public boolean isFormula();

	@Override
	public boolean isHeaderFontBold();

	@Override
	public boolean isHeaderFontItalic();

	@Override
	public boolean isHeaderFontStrikethrough();

	@Override
	public boolean isHeaderFontUnderline();

	@Override
	public boolean isHidden();

	@Override
	public boolean isHideDetail();

	@Override
	public boolean isHideFormula();

	@Override
	public boolean isIcon();

	@Override
	public boolean isNumberAttribParens();

	@Override
	public boolean isNumberAttribPercent();

	@Override
	public boolean isNumberAttribPunctuated();

	@Override
	public boolean isResize();

	@Override
	public boolean isResortAscending();

	@Override
	public boolean isResortDescending();

	@Override
	public boolean isResortToView();

	@Override
	public boolean isResponse();

	@Override
	public boolean isSecondaryResort();

	@Override
	public boolean isSecondaryResortDescending();

	@Override
	public boolean isShowTwistie();

	@Override
	public boolean isSortDescending();

	@Override
	public boolean isSorted();

	@Override
	public void setAccentSensitiveSort(boolean flag);

	@Override
	public void setAlignment(int alignment);

	@Override
	public void setCaseSensitiveSort(boolean flag);

	@Override
	public void setDateFmt(int format);

	@Override
	public void setFontBold(boolean flag);

	@Override
	public void setFontColor(int color);

	@Override
	public void setFontFace(String face);

	@Override
	public void setFontItalic(boolean flag);

	@Override
	public void setFontPointSize(int size);

	@Override
	public void setFontStrikethrough(boolean flag);

	@Override
	public void setFontStyle(int style);

	@Override
	public void setFontUnderline(boolean flag);

	@Override
	public void setFormula(String formula);

	@Override
	public void setHeaderAlignment(int alignment);

	@Override
	public void setHeaderFontBold(boolean flag);

	@Override
	public void setHeaderFontColor(int color);

	@Override
	public void setHeaderFontFace(String face);

	@Override
	public void setHeaderFontItalic(boolean flag);

	@Override
	public void setHeaderFontPointSize(int size);

	@Override
	public void setHeaderFontStrikethrough(boolean flag);

	@Override
	public void setHeaderFontStyle(int style);

	@Override
	public void setHeaderFontUnderline(boolean flag);

	@Override
	public void setHidden(boolean flag);

	@Override
	public void setHideDetail(boolean flag);

	@Override
	public void setHideFormula(boolean flag);

	@Override
	public void setListSep(int separator);

	@Override
	public void setNumberAttrib(int attributes);

	@Override
	public void setNumberAttribParens(boolean flag);

	@Override
	public void setNumberAttribPercent(boolean flag);

	@Override
	public void setNumberAttribPunctuated(boolean flag);

	@Override
	public void setNumberDigits(int digits);

	@Override
	public void setNumberFormat(int format);

	@Override
	public void setPosition(int position);

	@Override
	public void setResize(boolean flag);

	@Override
	public void setResortAscending(boolean flag);

	@Override
	public void setResortDescending(boolean flag);

	@Override
	public void setResortToView(boolean flag);

	@Override
	public void setResortToViewName(String name);

	@Override
	public void setSecondaryResort(boolean flag);

	@Override
	public void setSecondaryResortColumnIndex(int index);

	@Override
	public void setSecondaryResortDescending(boolean flag);

	@Override
	public void setShowTwistie(boolean flag);

	@Override
	public void setSortDescending(boolean flag);

	@Override
	public void setSorted(boolean flag);

	@Override
	public void setTimeDateFmt(int format);

	@Override
	public void setTimeFmt(int format);

	@Override
	public void setTimeZoneFmt(int format);

	@Override
	public void setTitle(String title);

	@Override
	public void setWidth(int width);
}
