package org.yx.common.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfConfig {

	private static final String BUNDLE_NAME = "config";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle("config");

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
		}
		return '!' + key + '!';
	}
	
}
