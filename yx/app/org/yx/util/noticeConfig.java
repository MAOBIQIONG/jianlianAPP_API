package org.yx.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class noticeConfig {

	private static final String filename = "notice";
	   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("notice");
	 
	   public static String getString(String key)
	   {
	     try
	     {
	       return RESOURCE_BUNDLE.getString(key); } catch (MissingResourceException e) {
	     }
	     return '!' + key + '!';
	   }
	
}
