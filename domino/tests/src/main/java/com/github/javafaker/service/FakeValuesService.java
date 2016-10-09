package com.github.javafaker.service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FakeValuesService {
	private static final char[] METHOD_NAME_DELIMITERS = { '_' };
	private static final Logger logger = Logger.getLogger(FakeValuesService.class.getName());
	private final Map<String, Object> fakeValuesMap;
	private final RandomService randomService;

	public FakeValuesService(final Locale locale, final RandomService randomService) {
		logger.info("Using locale " + locale);

		String languageCode = locale.getLanguage();
		final InputStream stream = findStream(languageCode + ".yml");
		if (stream == null) {
			throw new LocaleDoesNotExistException(String.format("%s could not be found, does not have a corresponding yaml file", locale));
		}
		Map valuesMap = (Map) new Yaml().load(stream);
		valuesMap = (Map) valuesMap.get(languageCode);
		fakeValuesMap = (Map<String, Object>) valuesMap.get("faker");
		this.randomService = randomService;
	}

	private InputStream findStream(final String filename) {
		InputStream streamOnClass = getClass().getResourceAsStream(filename);
		if (streamOnClass != null) {
			return streamOnClass;
		}
		return getClass().getClassLoader().getResourceAsStream(filename);
	}

	/**
	 * Fetch a random value from an array item specified by the key
	 */
	public Object fetch(final String key) {
		List valuesArray = (List) fetchObject(key);
		return valuesArray.get(nextInt(valuesArray.size()));
	}

	/**
	 * Same as {@link #fetch(String)} except this casts the result into a String.
	 */
	public String fetchString(final String key) {
		return (String) fetch(key);
	}

	/**
	 * Return the object selected by the key from yaml file.
	 * 
	 * @param key
	 *            key contains path to an object. Path segment is separated by dot. E.g. name.first_name
	 */
	public Object fetchObject(final String key) {
		String[] path = key.split("\\.");
		Object currentValue = fakeValuesMap;
		for (String pathSection : path) {
			currentValue = ((Map<String, Object>) currentValue).get(pathSection);
		}
		return currentValue;
	}

	/**
	 * A property that is composed of other properties.
	 * 
	 * It firstly fetches the formatKey using {@link #fetch(String)}. It will proceed to convert the returned properties from the
	 * {@link #fetch(String)} method to a methodName and invoke this method against the object passed in. Finally, concatenation occurs with
	 * the return values of the methods using the joiner parameter as a separator.
	 * 
	 */
	public String composite(final String formatKey, final String joiner, final Object objectToInvokeMethodOn) {
		List<String> format = (List<String>) fetch(formatKey);

		String[] parts = new String[format.size()];
		for (int i = 0; i < parts.length; i++) {
			// remove leading colon
			String methodName = format.get(i).substring(1);
			// convert to camel case
			methodName = WordUtils.capitalizeFully(methodName, METHOD_NAME_DELIMITERS).replaceAll("_", "");
			methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

			try {
				parts[i] = (String) objectToInvokeMethodOn.getClass().getMethod(methodName, (Class[]) null).invoke(objectToInvokeMethodOn);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return StringUtils.join(parts, joiner);
	}

	/**
	 * Returns a string with the '#' characters in the parameter replaced with random digits between 0-9 inclusive.
	 * 
	 * For example, the string "ABC##EFG" could be replaced with a string like "ABC99EFG".
	 * 
	 */
	public String numerify(final String numberString) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < numberString.length(); i++) {
			if (numberString.charAt(i) == '#') {
				sb.append(nextInt(10));
			} else {
				sb.append(numberString.charAt(i));
			}
		}

		return sb.toString();
	}

	/**
	 * Applies both a {@link #numerify(String)} and a {@link #letterify(String)} over the incoming string.
	 * 
	 */
	public String bothify(final String string) {
		return letterify(numerify(string));
	}

	/**
	 * Returns a string with the '?' characters in the parameter replaced with random alphabetic characters.
	 * 
	 * For example, the string "12??34" could be replaced with a string like "12AB34".
	 * 
	 */
	public String letterify(final String letterString) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < letterString.length(); i++) {
			if (letterString.charAt(i) == '?') {
				sb.append((char) (97 + nextInt(26))); // a-z
			} else {
				sb.append(letterString.charAt(i));
			}
		}

		return sb.toString();
	}

	private int nextInt(final int n) {
		return randomService.nextInt(n);
	}
}
