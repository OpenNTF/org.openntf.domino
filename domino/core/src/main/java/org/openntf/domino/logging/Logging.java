package org.openntf.domino.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.zip.CRC32;

import org.openntf.domino.utils.Factory;

/**
 * Introduces a highly configurable new logging mechanism. (Details are to be found in logconfig.properties.) If no configuration property
 * file is found, logging is statically initialized as before (as a fallback solution ). If the configuration is changed, logging will
 * update itself within 1 minute.
 * 
 * @author steinsiek
 * 
 */
public class Logging {

	private static ThreadLocal<SimpleDateFormat> sdfISO = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	public static String dateToString(final Date value) {
		return sdfISO.get().format(value);
	}

	private static Logging _theLogger = new Logging();

	private Logging() {
	}

	public static Logging getInstance() {
		return _theLogger;
	}

	private LogConfig _activeConfig = null;
	private Timer _supervisor = null;
	private static final long _supervisorInterval = 60000;	// 1 minute

	public void startUp() throws IOException {
		if (!activateCfgFromPropFile()) {
			//			System.err.println("Logging: Couldn't initialize from PropertyFile; activating fallback ...");
			startUpFallback();
			return;
		}
		System.out.println("Logging: LogConfig successfully initialized from " + _logConfigPropFile);
		// TODO: if we want to access a Config-DB we can use @Tasklet here
		//		Xots.getService().schedule(new Runnable() {
		//			@Override
		//			public void run() {
		//				Logging.getInstance().lookForCfgChange();
		//			}
		//		}, _supervisorInterval, TimeUnit.MILLISECONDS);

		_supervisor = new Timer("LoggingSupervisor", true);
		_supervisor.schedule(new TimerTask() {
			@Override
			public void run() {
				Logging.getInstance().lookForCfgChange();
			}
		}, _supervisorInterval, _supervisorInterval);
	}

	private boolean activateCfgFromPropFile() {
		LogConfig logCfg;
		if ((logCfg = loadCfgFromPropFile()) == null)
			return false;
		if (!createFilterHandlers(logCfg))
			return false;
		if (_activeConfig == null)	// Necessary only if starting up
			if (!getCfgPropFileNumbers())
				return false;
		activateFilterHandlers(logCfg);
		_activeConfig = logCfg;
		return true;
	}

	static String _logConfigPropFile = null;

	private LogConfig loadCfgFromPropFile() {
		File logConfigFile = logCfgFilePrecheck();
		if (logConfigFile == null)
			return null;
		Properties props;
		try {
			FileInputStream fis = new FileInputStream(logConfigFile);
			props = new Properties();
			props.load(fis);
			fis.close();
		} catch (Exception e) {
			System.err.println("Logging.loadFromPropFile: Exception " + e.getClass().getName() + ":");
			e.printStackTrace();
			return null;
		}
		return LogConfig.fromProperties(props);
	}

	private File logCfgFilePrecheck() {
		if (_logConfigPropFile == null)
			_logConfigPropFile = Factory.getDataPath() + "/IBM_TECHNICAL_SUPPORT/org.openntf.domino.logging.logconfig.properties";
		File ret = new File(_logConfigPropFile);
		String errMsg = null;
		if (!ret.exists())
			errMsg = "not found";
		else if (!ret.isFile())
			errMsg = "isn't a normal file";
		if (errMsg == null)
			return ret;
		//		System.err.println("Logging.logCfgFilePrecheck: File '" + _logConfigPropFile + "' " + errMsg);
		return null;
	}

	private boolean createFilterHandlers(final LogConfig logCfg) {
		try {
			for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values())
				LogFilterHandler.getInitializedInstance(llfh, _activeConfig);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values())
				if (llfh._myHandler != null) {
					llfh._myHandler.finishUp();
					llfh._myHandler = null;
				}
			return false;
		}
	}

	private void activateFilterHandlers(final LogConfig logCfg) {
		int sz = (_activeConfig == null) ? 0 : _activeConfig._logFilterHandlers.size();
		LogFilterHandler[] oldLFHs = new LogFilterHandler[sz];
		if (_activeConfig != null) {
			int i = 0;
			for (LogConfig.L_LogFilterHandler llfh : _activeConfig._logFilterHandlers.values())
				oldLFHs[i++] = llfh._myHandler;
		}
		for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values())
			llfh._myHandler.activateYourself(oldLFHs);
	}

	private void startUpFallback() throws IOException {
		String pattern = Factory.getDataPath() + "/IBM_TECHNICAL_SUPPORT/org.openntf.%u.%g.log";
		Logger oodLogger = Logger.getLogger("org.openntf.domino");
		oodLogger.setLevel(Level.WARNING);

		DefaultFileHandler dfh = new DefaultFileHandler(pattern, 50000, 100, true);
		dfh.setFormatter(new FileFormatter());
		dfh.setLevel(Level.WARNING);
		oodLogger.addHandler(dfh);

		DefaultConsoleHandler dch = new DefaultConsoleHandler();
		dch.setFormatter(new ConsoleFormatter());
		dch.setLevel(Level.WARNING);
		oodLogger.addHandler(dch);

		OpenLogHandler olh = new OpenLogHandler();
		olh.setLogDbPath("OpenLog.nsf");
		olh.setLevel(Level.WARNING);
		oodLogger.addHandler(olh);

		LogManager.getLogManager().addLogger(oodLogger);
	}

	private long _propFileLh;
	private long _propFileCRC;

	private boolean getCfgPropFileNumbers() {
		long fileLh = 0;
		long fileCRC = 0;
		File f = logCfgFilePrecheck();
		if (f == null)
			return false;
		try {
			FileInputStream fis = new FileInputStream(f);
			CRC32 crc = new CRC32();
			byte[] buff = new byte[16384];
			int got;
			while ((got = fis.read(buff)) > 0) {
				fileLh += got;
				crc.update(buff, 0, got);
			}
			fis.close();
			fileCRC = crc.getValue();
		} catch (Exception e) {
			System.err.println("Logging.getCfgPropFileNumbers: " + "Exception " + e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
		_propFileLh = fileLh;
		_propFileCRC = fileCRC;
		return true;
	}

	private static enum ConfigChangeFlag {
		CFG_UNCHANGED, CFG_UPDATED, CFG_ERROR;
	}

	private ConfigChangeFlag lookForCfgChange() {
		long oldFileLh = _propFileLh;
		long oldFileCRC = _propFileCRC;
		if (!getCfgPropFileNumbers())
			return ConfigChangeFlag.CFG_ERROR;
		if (_propFileLh == oldFileLh && _propFileCRC == oldFileCRC)
			return ConfigChangeFlag.CFG_UNCHANGED;
		if (activateCfgFromPropFile()) {
			System.out.println("Logging: Updated LogConfig from changed PropertyFile");
			return ConfigChangeFlag.CFG_UPDATED;
		}
		_propFileLh = oldFileLh;
		_propFileCRC = oldFileCRC;
		System.err.println("Logging: Couldn't update LogConfig from changed PropertyFile because of errors");
		return ConfigChangeFlag.CFG_ERROR;
	}

	static boolean _verbose = false;	// For testing

	public static void setVerbose(final boolean how) {
		_verbose = how;
	}
}
