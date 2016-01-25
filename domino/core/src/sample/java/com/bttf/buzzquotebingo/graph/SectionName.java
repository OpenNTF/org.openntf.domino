package com.bttf.buzzquotebingo.graph;

public enum SectionName {
	INTRODUCTION("Intro"), PINES("Twin Pines Mall / Lone Pine Mall"), PHOTO("Marty's Family Photo"),
	MARTY_TANNENS("Marty McFly and the Tannens"), POWER_OF_LOVE("Power of Love"), ALTERNATE("Alternate 1985"), INDIANS("Indians!"),
	ERASED("It's Erased!"), SUMMARY("Summary"), NONE("Not Used");
	private String value;

	private SectionName(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}