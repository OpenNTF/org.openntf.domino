package org.openntf.domino.tests.rpr.formula.eval;

public abstract class AtFunctionAbstract implements AtFunction {
	private String image;
	private String imageLcase;

	public AtFunctionAbstract(final String image) {
		this.image = image;
		this.imageLcase = image.toLowerCase();

	}

	public String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
