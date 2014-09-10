package org.openntf.domino.instrument;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ThreadProfiler {

	static class Data implements Comparable<Data> {
		public int count;
		public long netTime;
		public String name;

		@Override
		public int compareTo(final Data o) {
			// we cannot use this, as sorting is done @insert
			//return netTime == o.netTime ? 0 : netTime < o.netTime ? -1 : 1;
			return name.compareTo(o.name);
		}

		@Override
		public String toString() {
			return count + "\t" + netTime + "\t" + name;
		}

	}

	private long profilerTime[] = new long[1024];
	private long codeTime[] = new long[1024];
	private static Set<Data> dataSet = new ConcurrentSkipListSet();

	private static ConcurrentHashMap<String, Data> dataMap = new ConcurrentHashMap<String, Data>() {

		@Override
		public Data get(final Object arg0) {
			Data ret = super.get(arg0);
			if (ret == null) {
				synchronized (this) {
					ret = super.get(arg0);
					if (ret == null) {
						ret = new Data();
						ret.name = (String) arg0;
						dataSet.add(ret);
						super.put(ret.name, ret);
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
			if (printCnt++ % 32 == 0) {
				//System.out.println("NET\tTOTAL\tSUM\tCOUNT\tMETHOD");
				dump();
			}
			//System.out.println((netTime / 1000) + "\t" + (execTime / 1000) + "\t" + (d.netTime / 1000) + "\t" + d.count + "\t" + key);
		}

		profilerTime[cnt] -= System.nanoTime(); // sum up the profiler time (attention: stored value is negative)
	}

	public void dump() {
		try {
			PrintWriter pw = new PrintWriter("D:/daten/profiler.txt");
			for (Data data : dataSet) {
				pw.println(data);
			}
			pw.close();

			// resort by time
			List<Data> li = new ArrayList<Data>(dataSet);
			Collections.sort(li, new Comparator<Data>() {

				@Override
				public int compare(final Data d1, final Data d2) {
					return d1.netTime == d2.netTime ? 0 : d1.netTime > d2.netTime ? -1 : 1;

				}
			});

			pw = new PrintWriter("D:/daten/profiler-time.txt");
			for (Data data : li) {
				pw.println(data);
			}
			pw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
