package org.openntf.domino.design.impl;

import java.io.File;
import java.util.concurrent.ExecutionException;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.commons.util.StringUtil;

public class DxlSyncApp implements Runnable {

	private String dbPath;
	private String viewName;
	private File odpDir;
	private boolean gitFriendly = false;
	private boolean raw = false;
	private OnDiskProject.SyncDirection direction = OnDiskProject.SyncDirection.EXPORT;

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

			} else if (args[i].equalsIgnoreCase("-sync")) {
				direction = OnDiskProject.SyncDirection.SYNC;
			} else if (args[i].equalsIgnoreCase("-export")) {
				direction = OnDiskProject.SyncDirection.EXPORT;
			} else if (args[i].equalsIgnoreCase("-import")) {
				direction = OnDiskProject.SyncDirection.IMPORT;
			} else if (args[i].equalsIgnoreCase("-git")) {
				gitFriendly = true;
			} else if (args[i].equalsIgnoreCase("-raw")) {
				raw = true;
			}
		}

		if (dbPath == null || odpDir == null || direction == null) {
			System.out.println("Usage: java " + getClass().getName() + " <parameters>");
			System.out.println("\t-db\tThe NSF-Database (server!!filepath). Required!");
			System.out.println("\t-dir\tThe OnDisk-Directory. Required!");
			System.out.println("\t-view\tA view to synchronize also Documents (optional)");
			System.out.println("\t-git\tDXL is git friendly. That means Tags like @Modified and so on are not exported.");
			System.out.println("\t-raw\tRaw output (not yet implemented!)");
			System.out.println("\t-export\tSync direction: NSF->Disk. (will delete/modify elements on disk)");
			System.out.println("\t-import\tSync direction: NSF<-Disk. (will delete/modify elements in NSF)");
			System.out.println("\t-sync\tSync direction: NSF<>Disk. (will delete/modify elements in NSF and Disk)");
			System.out.println("\t\t(One sync direction is required!)");
		} else {
			TestRunnerUtil.runAsDominoThread(this, TestRunnerUtil.NATIVE_SESSION);
		}
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Database db = session.getDatabase(dbPath);

		OnDiskProject odp = new OnDiskProject(odpDir, db, this.direction);
		odp.setGitFriendly(true);
		odp.setExportMetadata(false);
		odp.syncDesign();
		if (!StringUtil.isEmpty(viewName)) {
			odp.syncDocs(viewName);
		}
		odp.close();
	}
}
