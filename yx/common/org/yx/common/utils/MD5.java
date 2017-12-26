 package org.yx.common.utils;
 
 import java.security.MessageDigest;
 
 public class MD5
 {
   public static String md5(String str)
   {
     try
     {
       MessageDigest md = MessageDigest.getInstance("MD5");
       md.update(str.getBytes("UTF-8"));
       byte[] b = md.digest();
 
       StringBuffer buf = new StringBuffer("");
       for (int offset = 0; offset < b.length; offset++) {
         int i = b[offset];
         if (i < 0)
           i += 256;
         if (i < 16)
           buf.append("0");
         buf.append(Integer.toHexString(i));
       }
       str = buf.toString().toUpperCase();
     } catch (Exception e) {
       e.printStackTrace();
     }
 
     return str;
   }
   public static void main(String[] args) {
     System.out.println(md5("{\"id\":\"123\",\"name\":\"中国草拟吗\"}"));
     //System.out.println(md5("mj1"));
   }
 }

/* Location:           F:\掌上幼儿园\源码\yzy_web\WEB-INF\classes\
 * Qualified Name:     com.fh.util.MD5
 * JD-Core Version:    0.6.2
 */