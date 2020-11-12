package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	/**
	 * Read a properties file.
	 */
	public static Properties readConfig(String path) {
		try {
			InputStream is = PropertiesUtil.class.getResourceAsStream(path);
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
