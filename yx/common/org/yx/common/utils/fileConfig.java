package org.yx.common.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class fileConfig {

	private static final String filename = "file";
	   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("file");
	 
	   public static String getString(String key)
	   {
	     try
	     {
	       return RESOURCE_BUNDLE.getString(key); } catch (MissingResourceException e) {
	     }
	     return '!' + key + '!';
	   }
	
}
