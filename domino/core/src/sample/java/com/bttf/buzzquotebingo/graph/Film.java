package com.bttf.buzzquotebingo.graph;

public enum Film {
	ONE("Back to the Future"), TWO("Back to the Future Part Two"), THREE("Back to the Future Part Three");
	private String value;

	private Film(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}