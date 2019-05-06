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
 * Initializes the OpenNTF Domino API logging mechanism. The mechanism is highly configurable (details are to be found in the package
 * description) If no configuration property file is found, a default configuration is set. If the configuration file is changed, logging
 * will update itself within 1 minute.
 * <p>
 * Logging is initialized during platform startup.
 * </p>
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

	/**
	 * Gets the single instance of this class.
	 */
	public static Logging getInstance() {
		return _theLogger;
	}

	private LogConfig _activeConfig = null;
	private Timer _supervisor = null;
	private static final long _supervisorInterval = 60000;	// 1 minute

	/**
	 * Loads the configuration file if it exists or sets default configuration if it does not and sets a timer to check the configuration
	 * file for changes every minute. This method is called by the ODA platform when it starts.
	 *
	 */
	public void startUp() throws IOException {
		if (!activateCfgFromPropFile()) {
			System.err.println("Logging: Couldn't initialize from PropertyFile; activating fallback ...");
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

	/**
	 * Creates and activates Filter Handlers from the configuration
	 *
	 * @return true if everything was successfull
	 */
	private boolean activateCfgFromPropFile() {
		LogConfig logCfg;
		if ((logCfg = loadCfgFromPropFile()) == null) {
			return false;
		}
		if (!createFilterHandlers(logCfg)) {
			return false;
		}
		if (_activeConfig == null) {
			if (!getCfgPropFileNumbers()) {
				return false;
			}
		}
		activateFilterHandlers(logCfg);
		_activeConfig = logCfg;
		return true;
	}

	static String _logConfigPropFile = null;

	/**
	 * Loads configuration from the configuration file in to an instance of LogConfig.
	 *
	 * @return LogConfig instance if the configuration file exists and has no syntax errors or null
	 */
	private LogConfig loadCfgFromPropFile() {
		File logConfigFile = logCfgFilePrecheck();
		if (logConfigFile == null) {
			return null;
		}
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

	/**
	 * Checks the existence of the configuration file in IBM_TECHNICAL_SUPPORT/org.openntf.domino.logging.logconfig.properties in Domino
	 * data directory.
	 *
	 * @return configuration file as an instance of a File or null if the file does not exist.
	 */
	private File logCfgFilePrecheck() {
		if (_logConfigPropFile == null) {
			_logConfigPropFile = Factory.getDataPath() + "/IBM_TECHNICAL_SUPPORT/org.openntf.domino.logging.logconfig.properties";
		}
		File ret = new File(_logConfigPropFile);
		String errMsg = null;
		if (!ret.exists()) {
			errMsg = "not found";
		} else if (!ret.isFile()) {
			errMsg = "isn't a normal file";
		}
		if (errMsg == null) {
			return ret;
		}
		System.err.println("Logging.logCfgFilePrecheck: File '" + _logConfigPropFile + "' " + errMsg);
		return null;
	}

	/**
	 * @param logCfg
	 * @return
	 */
	private boolean createFilterHandlers(final LogConfig logCfg) {
		try {
			for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values()) {
				LogFilterHandler.getInitializedInstance(llfh, _activeConfig);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values()) {
				if (llfh._myHandler != null) {
					llfh._myHandler.finishUp();
					llfh._myHandler = null;
				}
			}
			return false;
		}
	}

	private void activateFilterHandlers(final LogConfig logCfg) {
		int sz = (_activeConfig == null) ? 0 : _activeConfig._logFilterHandlers.size();
		LogFilterHandler[] oldLFHs = new LogFilterHandler[sz];
		if (_activeConfig != null) {
			int i = 0;
			for (LogConfig.L_LogFilterHandler llfh : _activeConfig._logFilterHandlers.values()) {
				oldLFHs[i++] = llfh._myHandler;
			}
		}
		for (LogConfig.L_LogFilterHandler llfh : logCfg._logFilterHandlers.values()) {
			llfh._myHandler.activateYourself(oldLFHs);
		}
	}

	/**
	 * Activates default configuration when a configuration file is missing.
	 *
	 * @throws IOException
	 */
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

	/**
	 * Computes configuration file CRC and size and stores them in member variables.
	 *
	 * @return true if the numbers were successfully computed.
	 */
	private boolean getCfgPropFileNumbers() {
		long fileLh = 0;
		long fileCRC = 0;
		File f = logCfgFilePrecheck();
		if (f == null) {
			return false;
		}
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

	/**
	 * Checks if the configuration file was updated and loads the changes if there were any. Returns one of these results:
	 * <ul>
	 * <li>ConfigChangeFlag.CFG_UNCHANGED when the file was not changed</li>
	 * <li>ConfigChangeFlag.CFG_UPDATED when the file changed and was loaded</li>
	 * <li>ConfigChangeFlag.CFG_ERROR when there was an error</li>
	 * </ul>
	 *
	 * @return an indication of the result
	 */
	private ConfigChangeFlag lookForCfgChange() {
		long oldFileLh = _propFileLh;
		long oldFileCRC = _propFileCRC;
		if (!getCfgPropFileNumbers()) {
			return ConfigChangeFlag.CFG_ERROR;
		}
		if (_propFileLh == oldFileLh && _propFileCRC == oldFileCRC) {
			return ConfigChangeFlag.CFG_UNCHANGED;
		}
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
