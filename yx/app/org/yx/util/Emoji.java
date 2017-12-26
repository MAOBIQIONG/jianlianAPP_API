package org.yx.util;

import org.apache.commons.lang.StringUtils;

public class Emoji {  
   public static String filterEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
        	return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", slipStr);//windows系统
            //return source.replaceAll("[\\\\ud800\\\\udc00-\\\\udbff\\\\udfff\\\\ud800-\\\\udfff]", slipStr);(linux系统)
        }else{
            return source;
        }
    }  
   
   public  static void main(String args[]){
	   //Emoji.filterEmoji("12", "12");
	   System.out.println("12"); 
	}
}
