package org.openntf.domino.design.impl;

import java.io.File;
import java.util.concurrent.ExecutionException;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.OnDiskConverter;
import org.openntf.domino.progress.CLIProgressObserver;
import org.openntf.domino.progress.ProgressObserver;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.StringUtil;

public class DxlSyncApp implements Runnable {

	private String dbPath;
	private String viewName;
	private File odpDir;
	private boolean gitFriendly = false;
	private boolean raw = false;
	private OnDiskSyncDirection direction = OnDiskSyncDirection.EXPORT;
	private boolean noDesign;
	private String viewCols = "0:1";

	public static void main(final String... args) throws InterruptedException, ExecutionException {
		new DxlSyncApp(args);
	}

	public DxlSyncApp(final String... args) throws InterruptedException, ExecutionException {

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-db")) {
				dbPath = args[++i];
			} else if (args[i].equalsIgnoreCase("-dir")) {
				odpDir = new File(args[++i]);
			} else if (args[i].equalsIgnoreCase("-view")) {
				viewName = args[++i];
			} else if (args[i].equalsIgnoreCase("-viewcol")) {
				viewCols = args[++i];
			} else if (args[i].equalsIgnoreCase("-sync")) {
				direction = OnDiskSyncDirection.SYNC;
			} else if (args[i].equalsIgnoreCase("-export")) {
				direction = OnDiskSyncDirection.EXPORT;
			} else if (args[i].equalsIgnoreCase("-import")) {
				direction = OnDiskSyncDirection.IMPORT;
			} else if (args[i].equalsIgnoreCase("-git")) {
				gitFriendly = true;
			} else if (args[i].equalsIgnoreCase("-raw")) {
				raw = true;
			} else if (args[i].equalsIgnoreCase("-nodesign")) {
				noDesign = true;
			}
		}

		if (dbPath == null || odpDir == null || direction == null) {
			System.out.println("Usage: java " + getClass().getName() + " <parameters>");
			System.out.println("\t-db\tThe NSF-Database (server!!filepath). Required!");
			System.out.println("\t-dir\tThe OnDisk-Directory. Required!");
			System.out.println("\t-view\tA view to synchronize also Documents (optional)");
			System.out.println("\t-viewcols\tA list of columns to build directory/title. (default 0:1)");
			System.out.println("\t-nodesign\tDo not synchronize design (makes sense only with -view)");
			System.out.println("\t-git\tDXL is git friendly. That means Tags like @Modified and so on are not exported.");
			System.out.println("\t-raw\tRaw output (not yet implemented!)");
			System.out.println("\t-export\tSync direction: NSF->Disk. (will delete/modify elements on disk)");
			System.out.println("\t-import\tSync direction: NSF<-Disk. (will delete/modify elements in NSF)");
			System.out.println("\t-sync\tSync direction: NSF<>Disk. (will delete/modify elements in NSF and Disk)");
			System.out.println("\t\t(One sync direction is required!)");
		} else {
			DominoThread.runApp(this, new NativeSessionFactory(null));
		}
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Database db = session.getDatabase(dbPath);
		OnDiskDesignSync designSync = null;
		OnDiskDocSync docSync = null;
		int work = 0;
		if (!noDesign) {
			work++;
			if (!odpDir.exists())
				odpDir.mkdirs();
			designSync = new OnDiskDesignSync(odpDir, db, this.direction);
			OnDiskConverter odsConverter;
			if (gitFriendly) {
				odsConverter = new GitFriendlyConverter(raw);
			} else {
				odsConverter = new DefaultConverter(raw);
			}
			designSync.setOdsConverter(odsConverter);
		}

		if (!StringUtil.isEmpty(viewName)) {
			work++;
			File docDir = new File(odpDir, OnDiskDesignSync.DOC_DIR);
			if (!docDir.exists())
				docDir.mkdirs();
			docSync = new OnDiskDocSync(docDir, db, viewName, viewCols, direction);
		}
		ProgressObserver progress = new CLIProgressObserver(work, System.out);
		if (designSync != null) {
			designSync.addObserver(progress);
			System.out.println(designSync.call());
		}
		if (docSync != null) {
			docSync.addObserver(progress);
			System.out.println(docSync.call());
		}
		//if (!StringUtil.isEmpty(viewName)) {
		//	odp.syncDocs(viewName);
		//}
		//odp.close();
	}

}
