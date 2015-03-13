package org.openntf.domino.design.sync;

public class OnDiskStatistics {
	public int inSync = 0;
	public int exported = 0;
	public int imported = 0;
	public int deleteNSF = 0;
	public int deleteDisk = 0;
	public int errors = 0;

	@Override
	public String toString() {
		return "OnDiskStatistics [inSync=" + inSync + ", exported=" + exported + ", imported=" + imported + ", deleteNSF=" + deleteNSF
				+ ", deleteDisk=" + deleteDisk + ", errors=" + errors + "]";
	}

}