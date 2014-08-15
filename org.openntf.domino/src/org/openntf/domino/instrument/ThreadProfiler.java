package org.openntf.domino.instrument;

import java.util.concurrent.ConcurrentHashMap;

public class ThreadProfiler {

	static class Data {

		public int count;
		public long netTime;

	}

	private long profilerTime[] = new long[1024];
	private long codeTime[] = new long[1024];
	private static ConcurrentHashMap<String, Data> dataMap = new ConcurrentHashMap<String, Data>() {

		@Override
		public Data get(final Object arg0) {
			Data ret = super.get(arg0);
			if (ret == null) {
				synchronized (this) {
					ret = super.get(arg0);
					if (ret == null) {
						ret = new Data();
						super.put((String) arg0, ret);
					}
				}
			}
			return ret;
		}
	};
	private int cnt = 0;
	private int printCnt;

	public void enter(final long wallTime) {
		// TODO Auto-generated method stub
		int curr = cnt;
		cnt = (cnt + 1) % 1024;
		profilerTime[curr] = wallTime;
		profilerTime[cnt] = 0;
		codeTime[curr] = System.nanoTime();
	}

	public void leave(final Class cls, final String method, final String signature, final long wallTime) {
		// TODO Auto-generated method stub
		int next = cnt;
		cnt = (cnt - 1) % 1024;
		long execTime = wallTime - codeTime[cnt]; // this is the time, the code spent running
		long netTime = execTime + profilerTime[next]; // subtract the time that the next profiler has consumed
		String key = cls.getName() + "." + method + signature;
		Data d = dataMap.get(key);
		d.count++;
		d.netTime += netTime;
		if (netTime > 5000) { // print only functions that take longer than 5 us
			if (printCnt++ % 32 == 0)
				System.out.println("NET\tTOTAL\tSUM\tCOUNT\tMETHOD");
			System.out.println((netTime / 1000) + "\t" + (execTime / 1000) + "\t" + (d.netTime / 1000) + "\t" + d.count + "\t" + key);
		}

		profilerTime[cnt] -= System.nanoTime(); // sum up the profiler time (attention: stored value is negative)
	}
}
