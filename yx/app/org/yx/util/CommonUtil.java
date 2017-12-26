package org.yx.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.yx.common.entity.PageData;
import org.yx.common.utils.DateUtil;

public class CommonUtil {
	//验证token是否过期
    public static boolean Totoken(PageData pd){
    	boolean re = false;
    	long longstr1 = Long.valueOf(pd.getString("last_active_date").replaceAll("[-\\s:]",""));//token过期时间
		long longstr2 = Long.valueOf(DateUtil.getTime().replaceAll("[-\\s:]",""));//当前时间
		if(longstr2 <= longstr1){
			re = true;
		}
    	return re;
    }
    
    //验证手机验证码是否获取
    public static boolean BePhoneCode(PageData pd){
    	boolean re = false;
    	long longstr1 = Long.valueOf(String.valueOf(pd.get("createTime")).replaceAll("[-\\s:]",""));//验证码过期时间
		long longstr2 = Long.valueOf(DateUtil.getTime().replaceAll("[-\\s:]",""));//当前时间
		if(longstr2 <= (longstr1+24*60*60*1000)){
			re = true;
		}
    	return re;
    }
    
    public static PageData getToken(Object userid) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	PageData topd = new PageData();
    	Date d=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String gtime = df.format(new Date(d.getTime() + (long)3 * 24 * 60 * 60 * 1000));
    	long timeInMillis = Calendar.getInstance().getTimeInMillis();
    	String token=afterMD5(String.valueOf(timeInMillis));
    	topd.put("userid", userid);
    	topd.put("token", token);
    	topd.put("last_active_date",gtime);
    	return topd;
    }
    /**
     * @author MD5摘要
     *
     */
    public static String afterMD5(String str) throws NoSuchAlgorithmException,  
    UnsupportedEncodingException {  
	 // 获取MD5 加密对象,还可以获取SHA加密对象  
	 MessageDigest md5 = MessageDigest.getInstance("MD5");  
	 // 将输入的信息使用指定的编码方式获取字节  
	 byte[] bytes = str.getBytes("UTF-8");
	 // 使用md5 类来获取摘要，也就是加密后的字节
	 md5 .update(bytes);
	 byte[] md5encode = md5.digest();
	 StringBuffer buffer = new StringBuffer();
	 for (int i = 0; i < md5encode.length; i++) { 
	     // 使用&0xff 不足24高位，因为只占了8低位  
	     int val = ((int) md5encode[i]) & 0xff;  
	     if (val < 16) { 
	         buffer.append("0");
	     }
	     // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式。  
	    buffer.append(Integer.toHexString(val)); 
	 }  
	 	return buffer.toString();
  }  
}
