package org.openntf.domino.nsfdata.structs;


public abstract class SIG extends AbstractStruct {

	public abstract long getRecordLength();

	public abstract void setRecordLength(long length);

	public abstract int getSigIdentifier();

	public abstract void setSigIdentifier(int identifier);
}
