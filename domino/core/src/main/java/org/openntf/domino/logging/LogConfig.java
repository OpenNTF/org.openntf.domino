package org.openntf.domino.logging;

import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.formula.ASTNode;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.FormulaParser;
import org.openntf.formula.Formulas;

public class LogConfig {

	public static final String cUserName = "$USER$";
	public static final String cDBPath = "$DBPATH$";
	public static final String cLoggerName = "$LOGGER$";
	public static final String cLogMessage = "$MESSAGE$";

	static class L_LogHandler {
		String _handlerName;
		String _handlerClassName;
		Class<?> _handlerClass;
		Method _handlerConfigFromProps;
		Method _handlerGetInstance;
		LogHandlerConfigIF _handlerConfig;
		String _formatterClassName;
		Class<?> _formatterClass;
		Method _formatterGetInstance;
		String _minimalLevelName;
		Level _minimalLevel;
		String _props;
		boolean _inUse;

		/*--------------------------------------------------------------*/
		L_LogHandler(final String handlerName, final String handlerClassName, final String formatterClassName,
				final String minimalLevelName, final String props) {
			_handlerName = handlerName;
			_handlerClassName = handlerClassName;
			_formatterClassName = formatterClassName;
			_minimalLevelName = minimalLevelName;
			_props = props;
			_inUse = false;
		}

		boolean checkYourself() {
			try {
				_handlerClass = Class.forName(_handlerClassName);
				_handlerConfigFromProps = _handlerClass.getMethod("configFromProps", String.class);
				_handlerGetInstance = _handlerClass.getMethod("getInstance", LogHandlerConfigIF.class, boolean.class);
				if (_formatterClassName != null) {
					_formatterClass = Class.forName(_formatterClassName);
					_formatterGetInstance = _formatterClass.getMethod("getInstance");
				}
				Object o = _handlerConfigFromProps.invoke(null, _props);
				if (!(o instanceof LogHandlerConfigIF)) {
					throw new IllegalArgumentException(_handlerClassName + ".configFromProps returned " + o.getClass().getName());
				}
				_handlerConfig = (LogHandlerConfigIF) o;
			} catch (Exception e) {
				System.err.println("LogConfig: Caught " + e.getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();
				return false;
			}
			if (_minimalLevelName != null) {
				if ((_minimalLevel = LogConfig.parseLevel(_minimalLevelName)) == null) {
					return false;
				}
			}
			return true;
		}
	}

	/*--------------------------------------------------------------*/
	static class L_LogFilterHandler {

		static class L_LogFilterConfigEntry {

			static ThreadLocal<FormulaParser> _myFormulaParser = new ThreadLocal<FormulaParser>() {
				@Override
				protected FormulaParser initialValue() {
					return Formulas.getMinimalParser();
				}
			};

			String _logPrefix;
			String _levelName;
			Level _level;
			String[] _logHandlerNames;
			L_LogHandler[] _logHandlerObjs;
			String _formulaCondition;
			ASTNode _parsedCond;
			boolean _condContUserName;
			boolean _condContDBPath;
			String _validUntilStr;
			Date _validUntil;

			/*--------------------------------------------------------------*/
			public L_LogFilterConfigEntry(final String logPrefix, final String levelName, final String[] handlerNames,
					final String formulaCondition, final String validUntilStr) {
				_logPrefix = logPrefix;
				_levelName = levelName;
				_logHandlerNames = handlerNames;
				_formulaCondition = formulaCondition;
				_validUntilStr = validUntilStr;
			}

			boolean checkYourself(final String[] loggerNames) {
				int i;
				for (i = 0; i < loggerNames.length; i++) {
					if (_logPrefix.startsWith(loggerNames[i])) {
						break;
					}
				}
				if (i == loggerNames.length) {
					System.err.println("LogConfig: Invalid prefix: " + _logPrefix);
					return false;
				}
				if ((_level = LogConfig.parseLevel(_levelName)) == null) {
					return false;
				}
				if (_formulaCondition != null) {
					try {
						_parsedCond = _myFormulaParser.get().parse(_formulaCondition);
					} catch (FormulaParseException e) {
						System.err.println("LogConfig: Invalid formula condition: " + _formulaCondition);
						e.printStackTrace();
						return false;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					_condContUserName = _formulaCondition.contains(cUserName);
					_condContDBPath = _formulaCondition.contains(cDBPath);
				}
				if (_validUntilStr != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					sdf.setLenient(false);
					ParsePosition pos = new ParsePosition(0);
					if ((_validUntil = sdf.parse(_validUntilStr, pos)) == null) {
						System.err.println("LogConfig: Invalid validUntil: " + _validUntilStr);
						return false;
					}
					_validUntil.setTime(_validUntil.getTime() + 24 * 3600 * 1000 - 1);
				}
				return true;
			}

			public boolean checkHandlers(final Map<String, L_LogHandler> definedHandlers) {
				_logHandlerObjs = new L_LogHandler[_logHandlerNames.length];
				for (int i = 0; i < _logHandlerNames.length; i++) {
					if ((_logHandlerObjs[i] = definedHandlers.get(_logHandlerNames[i])) == null) {
						System.err.println("LogConfig: Undefined Handler: " + _logHandlerNames[i]);
						return false;
					}
				}
				return true;
			}

			public void setHandlersInUse() {
				for (int i = 0; i < _logHandlerObjs.length; i++) {
					_logHandlerObjs[i]._inUse = true;
				}
			}
		}

		/*--------------------------------------------------------------*/

		String _myName;
		String[] _loggerNames;
		Logger[] _loggers;	// A strong reference to the Logger itself is necessary in certain cases
		String _defaultLevelName;
		Level _defaultLevel;
		String[] _defaultHandlerNames;
		L_LogHandler[] _defaultHandlers;
		List<L_LogFilterConfigEntry> _logFCEList;
		LogFilterHandler _myHandler;

		/*--------------------------------------------------------------*/
		L_LogFilterHandler(final String yourName, final String[] loggerNames, final String defaultLevelName,
				final String[] defaultHandlerNames) {
			_myName = yourName;
			_loggerNames = loggerNames;
			_defaultLevelName = defaultLevelName;
			_defaultHandlerNames = defaultHandlerNames;
			_logFCEList = new ArrayList<L_LogFilterConfigEntry>();
		}

		boolean checkYourself() {
			if ((_defaultLevel = LogConfig.parseLevel(_defaultLevelName)) == null) {
				return false;
			}
			for (L_LogFilterConfigEntry fce : _logFCEList) {
				if (!fce.checkYourself(_loggerNames)) {
					return false;
				}
			}
			return true;
		}

		public void addFCE(final String prefix, final String levelName, final String[] handlerNames, final String formulaCond,
				final String validUntil) {
			L_LogFilterHandler.L_LogFilterConfigEntry lfce = new L_LogFilterHandler.L_LogFilterConfigEntry(prefix, levelName, handlerNames,
					formulaCond, validUntil);
			_logFCEList.add(lfce);
		}

		public boolean checkHandlers(final Map<String, L_LogHandler> definedHandlers) {
			_defaultHandlers = new L_LogHandler[_defaultHandlerNames.length];
			for (int i = 0; i < _defaultHandlerNames.length; i++) {
				if ((_defaultHandlers[i] = definedHandlers.get(_defaultHandlerNames[i])) == null) {
					System.err.println("LogConfig: Undefined DefaultHandler: " + _defaultHandlerNames[i]);
					return false;
				}
			}
			for (L_LogFilterConfigEntry fce : _logFCEList) {
				if (!fce.checkHandlers(definedHandlers)) {
					return false;
				}
			}
			return true;
		}

		public void throwExpiredFCEs(final Date now) {
			for (int i = _logFCEList.size() - 1; i >= 0; i--) {
				L_LogFilterConfigEntry lfce = _logFCEList.get(i);
				if (lfce._validUntil != null && lfce._validUntil.getTime() <= now.getTime()) {
					System.out.println("HINT: Filter Config Entry expired due to ValidUntil=" + lfce._validUntilStr);
					_logFCEList.remove(i);
				}
			}
		}

		public void setHandlersInUse() {
			for (int i = 0; i < _defaultHandlers.length; i++) {
				_defaultHandlers[i]._inUse = true;
			}
			for (L_LogFilterConfigEntry fce : _logFCEList) {
				fce.setHandlersInUse();
			}
		}
	}

	/*--------------------------------------------------------------*/
	Map<String, L_LogHandler> _logHandlers = new HashMap<String, L_LogHandler>();
	Map<String, L_LogFilterHandler> _logFilterHandlers = new HashMap<String, L_LogFilterHandler>();

	public static LogConfig fromProperties(final Properties props) {
		LogConfig ret = new LogConfig();
		if (!ret.initFromProperties(props)) {
			return null;
		}
		return ret;
	}

	private boolean initFromProperties(final Properties props) {
		String[] propList;
		if ((propList = readAndCheckPropAsList(props, "Handlers", true)) == null) {
			return false;
		}
		for (int i = 0; i < propList.length; i++) {
			if (!initOneLogHandler(props, propList[i])) {
				return false;
			}
		}
		if ((propList = readAndCheckPropAsList(props, "FilterHandlers", true)) == null) {
			return false;
		}
		for (int i = 0; i < propList.length; i++) {
			if (!initOneFilterHandler(props, propList[i])) {
				return false;
			}
		}
		if (!checkLogHandlerRefs()) {
			return false;
		}
		throwExpiredFCEs();
		setHandlersInUse();
		return true;
	}

	private boolean initOneLogHandler(final Properties props, final String handlerName) {
		String keyPref = "Handler." + handlerName;
		String className = readProp(props, keyPref + ".Class", true);
		if (className == null) {
			return false;
		}
		String formatterClassName = readProp(props, keyPref + ".Formatter", false);
		String minLevelName = readProp(props, keyPref + ".MinimalLevel", false);
		String props4Handler = readProp(props, keyPref + ".Props", false);
		L_LogHandler handler = new L_LogHandler(handlerName, className, formatterClassName, minLevelName, props4Handler);
		_logHandlers.put(handlerName, handler);
		return handler.checkYourself();
	}

	private boolean initOneFilterHandler(final Properties props, final String filterHandlerName) {
		String keyPref = "FilterHandler." + filterHandlerName;
		String[] loggerNames = readAndCheckPropAsList(props, keyPref + ".LoggerPrefices", true);
		String defaultLevelName = readProp(props, keyPref + ".DefaultLevel", true);
		String[] defaultHandlerNames = readAndCheckPropAsList(props, keyPref + ".DefaultHandlers", true);
		if (loggerNames == null || defaultLevelName == null || defaultHandlerNames == null) {
			return false;
		}
		L_LogFilterHandler lfh = new L_LogFilterHandler(filterHandlerName, loggerNames, defaultLevelName, defaultHandlerNames);
		_logFilterHandlers.put(filterHandlerName, lfh);
		for (int i = 1;; i++) {
			String keyPrefI = keyPref + ".FCE" + i;
			String prefix = readProp(props, keyPrefI, false);
			if (prefix == null) {
				break;
			}
			if (!initOneFCE(props, prefix, keyPrefI, lfh)) {
				return false;
			}
		}
		return lfh.checkYourself();
	}

	private boolean initOneFCE(final Properties props, final String prefix, final String keyPrefI, final L_LogFilterHandler lfh) {
		String levelName = readProp(props, keyPrefI + ".Level", true);
		String[] handlerNames = readAndCheckPropAsList(props, keyPrefI + ".Handlers", true);
		if (levelName == null || handlerNames == null) {
			return false;
		}
		String formulaCond = readProp(props, keyPrefI + ".FormulaCondition", false);
		String validUntil = readProp(props, keyPrefI + ".ValidUntil", false);
		lfh.addFCE(prefix, levelName, handlerNames, formulaCond, validUntil);
		return true;
	}

	private boolean checkLogHandlerRefs() {
		for (L_LogFilterHandler lfh : _logFilterHandlers.values()) {
			if (!lfh.checkHandlers(_logHandlers)) {
				return false;
			}
		}
		return true;
	}

	private void throwExpiredFCEs() {
		Date now = new Date();
		for (L_LogFilterHandler lfh : _logFilterHandlers.values()) {
			lfh.throwExpiredFCEs(now);
		}
	}

	private void setHandlersInUse() {
		for (L_LogFilterHandler lfh : _logFilterHandlers.values()) {
			lfh.setHandlersInUse();
		}
	}

	/*--------------------------------------------------------------*/
	private static String readProp(final Properties props, final String key, final boolean required) {
		String ret = props.getProperty(key);
		if (ret != null && ret.isEmpty()) {
			ret = null;
		}
		if (required && ret == null) {
			System.err.println("LogConfig: Required Property " + key + " isn't supplied");
		}
		return ret;
	}

	private static String[] readPropAsList(final Properties props, final String key, final boolean required) {
		String prop = readProp(props, key, required);
		if (prop == null) {
			return null;
		}
		return splitAlongComma(prop);
	}

	public static String[] splitAlongComma(final String prop) {
		String[] ret = prop.split(",");
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ret[i].trim();
		}
		Arrays.sort(ret);
		return ret;
	}

	public static boolean checkPropList(final String key, final String[] propList) {
		if (propList[0].isEmpty()) {
			System.err.println("LogConfig: Multiple property for key " + key + " contains empty part");
			return false;
		}
		for (int i = 0; i < propList.length - 1; i++) {
			if (propList[i].equals(propList[i + 1])) {
				System.err.println("LogConfig: Multiple property for key " + key + " contains duplicates: " + propList[i]);
				return false;
			}
		}
		return true;
	}

	private static String[] readAndCheckPropAsList(final Properties props, final String key, final boolean required) {
		String[] ret = readPropAsList(props, key, required);
		if (ret != null) {
			if (!checkPropList(key, ret)) {
				ret = null;
			}
		}
		return ret;
	}

	/*--------------------------------------------------------------*/
	public static Level parseLevel(final String levelName) {
		if (Level.SEVERE.getName().equals(levelName)) {
			return Level.SEVERE;
		}
		if (Level.WARNING.getName().equals(levelName)) {
			return Level.WARNING;
		}
		if (Level.INFO.getName().equals(levelName)) {
			return Level.INFO;
		}
		if (Level.CONFIG.getName().equals(levelName)) {
			return Level.CONFIG;
		}
		if (Level.FINE.getName().equals(levelName)) {
			return Level.FINE;
		}
		if (Level.FINER.getName().equals(levelName)) {
			return Level.FINER;
		}
		if (Level.FINEST.getName().equals(levelName)) {
			return Level.FINEST;
		}
		System.err.println("LogConfig.parseLevel: Invalid name for log level: " + levelName);
		return null;
	}

}
